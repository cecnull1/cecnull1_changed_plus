package com.github.cecnull1.cecnull1_changed_plus_v2.utils

import com.github.cecnull1.cecnull1_cforge.core.*
import com.github.cecnull1.cecnull1_cforge.core.ComponentCore.addComponent
import com.github.cecnull1.cecnull1_cforge.core.ComponentCore.getComponents
import com.github.cecnull1.cecnull1_cforge.core.RegistryCore.get
import com.github.cecnull1.cecnull1_cforge.core.RegistryCore.register
import com.mojang.logging.LogUtils.getLogger
import com.mojang.serialization.Codec
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.Tag
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object CodecRegistry {
    @OptIn(ExperimentalRegistry::class)
    val REG = RegistryCore.createRegistry<Codec<out IPersistent>>(ResourceLocation("cforge", "persistent_codec"))

    /** 一次性注册，拿到 ResourceLocation 钥匙 */
    @OptIn(ExperimentalRegistry::class)
    inline fun <reified T : IPersistent> registerCodec(id: ResourceLocation, codec: Codec<T>) {
        REG.register(id, codec)
    }

    @OptIn(ExperimentalRegistry::class)
    inline fun <reified T : IPersistent> registerCodec(codec: Codec<T>) {
        REG.register(ResourceLocation("cforge", T::class.simpleName!!), codec)
    }

    /** 通过组件实例直接拿到对应 Codec（无反射，无 uncheckedCast） */
    @OptIn(ExperimentalRegistry::class)
    @Suppress("UNCHECKED_CAST")
    fun codecOf(comp: IPersistent): Codec<IPersistent> {
        val id = ResourceLocation("cforge", comp::class.simpleName!!)
        return REG[id] as? Codec<IPersistent> ?: error("Codec not registered for $id")
    }
}

fun serializerEntityComponent(entity: Any, map: ComponentMap, w: CompoundTag) {
    val drawer = entity.getComponents(map)
    drawer.forEach { (_, subMap) ->
        subMap.forEach { (rl, comp) ->
            if (comp is IPersistent) {
                val codec = CodecRegistry.codecOf(comp)
                val key = "cforge:${comp::class.simpleName}:${rl.path}"
                encodeToTag(codec, comp)?.let {
                    w.put(key, it)
                }
                getLogger().info("Serializing component $comp with key $key")
            }
        }
    }
}

@OptIn(ExperimentalRegistry::class)
fun deserializerEntityComponent(
    entity: Any,
    map: ComponentMap,
    r: CompoundTag
) {
    r.allKeys
        .filter { it.startsWith("cforge:") }
        .forEach { rawKey ->
            val parts = rawKey.split(':')
            if (parts.size != 3) return@forEach

            val className = parts[1]
            val rlPath = parts[2]
            val codecId = ResourceLocation("cforge", className)
            val codec = CodecRegistry.REG[codecId] ?: return@forEach

            // 🔑 关键：从 CompoundTag 获取 Tag，再解码
            val tag = r.get(rawKey) ?: return@forEach
            decodeFromTag(codec, tag)?.let { comp ->
                val fullRl = ResourceLocation("cforge", rlPath)
                entity.addComponent(map, fullRl, comp)
                getLogger().info("Deserializing component $comp")
            }
        }
}
/**
 * 将任意对象通过 Codec 序列化为 NBT Tag
 */
fun <T> encodeToTag(codec: Codec<T>, value: T): Tag? {
    return codec.encodeStart(NbtOps.INSTANCE, value)
        .result()
        .orElse(null)
}

/**
 * 从 NBT Tag 反序列化为对象
 */
fun <T> decodeFromTag(codec: Codec<T>, tag: Tag): T? {
    return codec.parse(NbtOps.INSTANCE, tag)
        .result()
        .orElse(null)
}