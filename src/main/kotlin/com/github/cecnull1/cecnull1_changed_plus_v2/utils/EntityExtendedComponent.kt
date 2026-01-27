@file:OptIn(ExperimentalSerializationApi::class, ExperimentalRegistry::class)

package com.github.cecnull1.cecnull1_changed_plus_v2.utils

import com.github.cecnull1.cecnull1_cforge.big_core.ExperimentalRegistry
import com.github.cecnull1.cecnull1_cforge.big_core.Registry
import com.github.cecnull1.cecnull1_cforge.big_core.RegistryCore.register
import com.github.cecnull1.cecnull1_cforge.core.ComponentContainer
import com.github.cecnull1.cecnull1_cforge.core.IComponent
import com.github.cecnull1.cecnull1_cforge.core.data.ResourceLocation
import com.github.cecnull1.cecnull1_changed_plus_v2.cbor.RLSer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KType
import kotlin.reflect.typeOf

interface EntityExtendedComponent {
    var components: ComponentContainer
}

typealias ComponentsSer = MutableMap<String, MutableMap<ResourceLocation, IComponentSer>>

object EntityExtendedComponentSer : KSerializer<ComponentsSer> {

    // 内层 map: ResourceLocation -> IComponentSer
    private val innerMapSerializer = MapSerializer(
        keySerializer = RLSer,
        valueSerializer = PolymorphicSerializer(IComponentSer::class)
    )

    // 外层 map: String (FQCN) -> innerMap
    private val outerMapSerializer = MapSerializer(
        keySerializer = String.serializer(),
        valueSerializer = innerMapSerializer
    )

    override val descriptor: SerialDescriptor = outerMapSerializer.descriptor

    override fun serialize(encoder: Encoder, value: ComponentsSer) {
        // 直接序列化整个分组结构
        encoder.encodeSerializableValue(outerMapSerializer, value)
    }

    override fun deserialize(decoder: Decoder): ComponentsSer {
        // 先反序列化出外层 map
        val deserialized = decoder.decodeSerializableValue(outerMapSerializer)

        // 转换为 mutable 结构（ConcurrentHashMap）
        val result = ConcurrentHashMap<String, MutableMap<ResourceLocation, IComponentSer>>()
        for ((typeName, innerMap) in deserialized) {
            result[typeName] = ConcurrentHashMap(innerMap)
        }
        return result
    }
}

fun ComponentContainer.toComponentsSer(): ComponentsSer {
    val result = ConcurrentHashMap<String, MutableMap<ResourceLocation, IComponentSer>>()

    for ((type, innerMap) in this) {
        // 只处理 IComponentSer 子类
        val serializableInner = innerMap
            .filter { (_, comp) -> comp is IComponentSer }
            .mapValues { (_, comp) -> comp as IComponentSer }

        if (serializableInner.isNotEmpty()) {
            val typeName = type.toString()
            result[typeName] = ConcurrentHashMap(serializableInner)
        }
    }

    return result
}

fun ComponentsSer.toComponentContainer(): ComponentContainer {
    val result = ComponentContainer()

    // 遍历所有内层 map，忽略外层 typeName！
    for (innerMap in this) {
        for ((rl, component) in innerMap.value) {
            result.computeIfAbsent(TypeRegistry.registry[innerMap.key.toRL()]) { ConcurrentHashMap() }[rl] = component
        }
    }

    return result
}

object TypeRegistry {
    val registry: Registry<KType> = Registry()
    inline fun <reified T> register() {
        registry.register(typeOf<T>().toString().toRL(), typeOf<T>())
    }
}

interface IComponentSer : IComponent