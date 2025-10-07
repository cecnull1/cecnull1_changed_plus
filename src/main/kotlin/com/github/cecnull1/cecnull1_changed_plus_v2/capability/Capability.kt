package com.github.cecnull1.cecnull1_changed_plus_v2.capability

import com.github.cecnull1.cecnull1_changed_plus_v2.event.initHaArmorItems
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.IPlayerExtendedData
import com.github.cecnull1.cecnull1lib.utils.nbt.asCompoundTag
import com.github.cecnull1.cecnull1lib.utils.nbt.buildNBT
import com.github.cecnull1.cecnull1lib.utils.nbt.entries
import com.github.cecnull1.cecnull1lib.utils.nbt.set
import com.mojang.logging.LogUtils.getLogger
import net.ltxprogrammer.changed.data.AccessorySlots
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import java.util.*

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
            copyFrom(player.mPlayerExtendedData.haState, false)
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