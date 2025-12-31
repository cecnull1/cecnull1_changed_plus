package com.github.cecnull1.cecnull1_changed_plus_v2.utils

import com.github.cecnull1.cecnull1_changed_plus_v2.packet.NetworkHandler
import com.mojang.logging.LogUtils.getLogger
import net.ltxprogrammer.changed.data.AccessorySlots
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

interface IPlayerExtendedData {
    var mPlayerExtendedData: MPlayerExtendedData
    fun cecnull1PlayerExtendedSave(tag: CompoundTag)
    fun cecnull1PlayerExtendedLoad(tag: CompoundTag)
}

data class MPlayerExtendedData(
    val haState: HAState = HAState()
) {
    companion object {
        var Player.hasHA: Boolean
            get() {
                if (this is IPlayerExtendedData) {
                    return this.mPlayerExtendedData.haState.hasHA
                }
                return false
            }
            set(value) {
                if (this is IPlayerExtendedData) {
                    val data = this.mPlayerExtendedData
                    val old = data.haState.hasHA
                    if (value != old) {
                        data.haState.hasHA = value
                        getLogger().info("${this.level}: Has HA changed from $old to $value")
                        if (this is ServerPlayer) NetworkHandler.haStateSendToClient(this)
                    }
                }
            }

        var Player.hasArmorHA: Boolean
            get() {
                if (this is IPlayerExtendedData) {
                    return this.mPlayerExtendedData.haState.hasArmorHA
                }
                return false
            }
            set(value) {
                if (this is IPlayerExtendedData) {
                    val data = this.mPlayerExtendedData
                    val old = data.haState.hasArmorHA
                    if (old != value) {
                        data.haState.hasArmorHA = value
                        getLogger().info("${this.level}: Has Armor HA changed from $old to $value")
                        if (this is ServerPlayer) NetworkHandler.haStateSendToClient(this)
                    }
                }
            }

        var Player.haItem: ItemStack
            get() {
                if (this is IPlayerExtendedData) {
                    return this.mPlayerExtendedData.haState.haItem
                }
                return ItemStack.EMPTY
            }
            set(value) {
                if (this is IPlayerExtendedData) {
                    val data = this.mPlayerExtendedData
                    val old = data.haState.haItem
                    if (old !== value && !ItemStack.matches(old, value)) {
                        data.haState.haItem = value
                        getLogger().info("${this.level}: HA Item changed from $old to $value")
                        if (this is ServerPlayer) NetworkHandler.haStateSendToClient(this)
                    }
                }
            }

        var Player.haArmorItems: MutableMap<EquipmentSlot, ItemStack>
            get() {
                if (this is IPlayerExtendedData) {
                    return this.mPlayerExtendedData.haState.haArmorItems
                }
                return mutableMapOf<EquipmentSlot, ItemStack>(
                    EquipmentSlot.HEAD to ItemStack.EMPTY,
                    EquipmentSlot.CHEST to ItemStack.EMPTY,
                    EquipmentSlot.LEGS to ItemStack.EMPTY,
                    EquipmentSlot.FEET to ItemStack.EMPTY
                )
            }
            set(value) {
                if (this is IPlayerExtendedData) {
                    val data = this.mPlayerExtendedData
                    val old = data.haState.haArmorItems
                    if (old != value) {
                        data.haState.haArmorItems = value
                        getLogger().info("${this.level}: HA Armor Items changed from $old to $value")
                        if (this is ServerPlayer) NetworkHandler.haStateSendToClient(this)
                    }
                }
            }

        var Player.haAccessorySlots: AccessorySlots
            get() {
                if (this is IPlayerExtendedData) {
                    this.mPlayerExtendedData.haState.haAccessorySlots
                }
                return AccessorySlots()
            }
            set(value) {
                if (this is IPlayerExtendedData) {
                    val data = this.mPlayerExtendedData
                    val old = data.haState.haAccessorySlots
                    if (old != value) {
                        data.haState.haAccessorySlots = value
                        if (this is ServerPlayer) NetworkHandler.haStateSendToClient(this)
                    }
                }
            }

        var Player.wuDiTime: Int
            get() {
                if (this is IPlayerExtendedData) {
                    return this.mPlayerExtendedData.haState.wuDiTime
                }
                return 0
            }
            set(value) {
                if (this is IPlayerExtendedData) {
                    val data = this.mPlayerExtendedData
                    val old = data.haState.wuDiTime
                    if (old != value) {
                        data.haState.wuDiTime = value
                    }
                }
            }
    }
}