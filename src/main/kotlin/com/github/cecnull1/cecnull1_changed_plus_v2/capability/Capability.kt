package com.github.cecnull1.cecnull1_changed_plus_v2.capability

import com.github.cecnull1.cecnull1_changed_plus_v2.event.initHaArmorItems
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.IPlayerExtendedData
import com.github.cecnull1.cecnull1lib.utils.nbt.asCompoundTag
import com.github.cecnull1.cecnull1lib.utils.nbt.buildNBT
import com.github.cecnull1.cecnull1lib.utils.nbt.entries
import com.github.cecnull1.cecnull1lib.utils.nbt.set
import com.mojang.logging.LogUtils.getLogger
import net.ltxprogrammer.changed.data.AccessorySlots
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.CapabilityManager
import net.minecraftforge.common.capabilities.CapabilityToken
import net.minecraftforge.common.capabilities.ICapabilityProvider
import net.minecraftforge.common.util.INBTSerializable
import net.minecraftforge.common.util.LazyOptional
import java.util.*

@Deprecated("")
data class ExtendedPlayerData(
    val haState: HAState = HAState(),
) {
    fun saveNBTData(nbt: CompoundTag) {
        haState.saveNBTData(nbt)
    }

    fun loadNBTData(nbt: CompoundTag) {
        haState.loadNBTData(nbt)
    }

    fun copyFrom(other: ExtendedPlayerData) {
        haState.copyFrom(other.haState)
    }
}

@Deprecated("")
class ExtendedPlayerDataProvider : ICapabilityProvider, INBTSerializable<CompoundTag> {
    companion object {
        @JvmField
        val EXTENDED_PLAYER_DATA: Capability<ExtendedPlayerData> =
            CapabilityManager.get<ExtendedPlayerData>(object : CapabilityToken<ExtendedPlayerData>() {})
    }

    private var extendedPlayerData: ExtendedPlayerData? = null

    private val optional: LazyOptional<ExtendedPlayerData> = LazyOptional.of(this::createExtendedPlayerData)

    private fun createExtendedPlayerData(): ExtendedPlayerData {
        val naStateL = extendedPlayerData ?: ExtendedPlayerData()
        extendedPlayerData = ExtendedPlayerData()
        return naStateL
    }

    override fun <T : Any?> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T> {
        return if (cap == EXTENDED_PLAYER_DATA) {
            optional.cast()
        } else {
            LazyOptional.empty()
        }
    }

    override fun serializeNBT(): CompoundTag {
        val nbt = CompoundTag()
        createExtendedPlayerData().saveNBTData(nbt)
        return nbt
    }

    override fun deserializeNBT(nbt: CompoundTag) {
        createExtendedPlayerData().loadNBTData(nbt)
    }
}

@Deprecated("")
var Player.haEnabledOld
    get(): Boolean {
        return this.getCapability(ExtendedPlayerDataProvider.EXTENDED_PLAYER_DATA).orElse(ExtendedPlayerData()).haState.hasHA
    }
    set(value) {
        val old = this.haEnabledOld
        if (old != value) {
            this.getCapability(ExtendedPlayerDataProvider.EXTENDED_PLAYER_DATA).orElse(ExtendedPlayerData()).haState.hasHA = value
        }
    }

@Deprecated("")
var Player.haItemOld
    get(): ItemStack {
        return this.getCapability(ExtendedPlayerDataProvider.EXTENDED_PLAYER_DATA).orElse(ExtendedPlayerData()).haState.haItem
    }
    set(value) {
        val old = this.haItemOld
        if (old !== value && !ItemStack.matches(old, value)) {
            this.getCapability(ExtendedPlayerDataProvider.EXTENDED_PLAYER_DATA)
                .orElse(ExtendedPlayerData()).haState.haItem = value
        }
    }

data class HAState(
    var hasHA: Boolean = false,
    var hasArmorHA: Boolean = false,
    var haItem: ItemStack = ItemStack.EMPTY,
    var haArmorItems: MutableMap<EquipmentSlot, ItemStack> = initHaArmorItems(),
    var haAccessorySlots: AccessorySlots = AccessorySlots(),
    var wuDiTime: Int = 0
) {
    fun copyFrom(other: HAState, deepCopy: Boolean = false) {
        hasHA = other.hasHA
        hasArmorHA = other.hasArmorHA
        haItem = if (deepCopy) other.haItem.copy() else other.haItem
        haArmorItems = other.haArmorItems.mapValues { (_, itemStack) ->
            if (deepCopy) itemStack.copy() else itemStack
        }.toMutableMap()

        val otherHaAccessorySlots = other.haAccessorySlots
        haAccessorySlots = if (deepCopy) AccessorySlots(otherHaAccessorySlots.owner).apply {
            load(otherHaAccessorySlots.save())
            // 2. 复制 lastItems（通过公共方法读写）
            otherHaAccessorySlots.slotTypes.forEach { slotType ->
                val lastStack = otherHaAccessorySlots.getLastItem(slotType)
                this.setLastItem(slotType, lastStack)
            }
            orderedSlots
        } else other.haAccessorySlots

        wuDiTime = other.wuDiTime
    }

    fun copyFrom(player: Player) {
        if (player is IPlayerExtendedData) {
            copyFrom(player.getMPlayerExtendedData().haState, false)
        }
    }

    fun loadNBTData(nbt: CompoundTag): HAState {
        hasHA = nbt.getBoolean("hasHA")
        hasArmorHA = nbt.getBoolean("hasArmorHA")
        haItem = ItemStack.of(nbt.getCompound("haItem"))
        haArmorItems = buildMap {
            for ((slot, itemStack) in nbt.getCompound("haArmorItems").entries) {
                try {
                    put(EquipmentSlot.byName(slot), ItemStack.of(itemStack.asCompoundTag()))
                } catch (_: IllegalArgumentException) {
                    getLogger().warn("Invalid slot name: $slot")
                }
            }
        }.toMutableMap()
        haAccessorySlots.load(nbt.getCompound("haAccessorySlots"))
        wuDiTime = nbt.getInt("wuDiTime")
        return this
    }

    // 私有核心方法
    private fun writeTo(tag: CompoundTag): CompoundTag {
        tag["hasHA"] = hasHA
        tag["hasArmorHA"] = hasArmorHA
        tag["haItem"] = haItem.serializeNBT()
        tag["haArmorItems"] = buildNBT {
            for ((slot, itemStack) in haArmorItems) {
                this[slot.name.lowercase(Locale.ROOT)] = itemStack.serializeNBT()
            }
        }
        tag["haAccessorySlots"] = haAccessorySlots.save()
        tag["wuDiTime"] = wuDiTime
        return tag
    }

    // 覆盖式写入
    fun saveNBTData(nbt: CompoundTag = CompoundTag()) = writeTo(nbt)

    // 安全序列化
    fun serialize(): CompoundTag = writeTo(CompoundTag())

    fun deserialize(nbt: CompoundTag): HAState = loadNBTData(nbt)
}

/*



*    U   *   P              *        *
           *    P   P   L   E       O
          N                    L        O
                            *   L   *    *


J U M P     F A B E
O   A P P L E   O
G   N     O L I O
          A L A K
              M
              B

* */