package com.github.cecnull1.cecnull1_changed_plus_v2.utils

import com.github.cecnull1.cecnull1_changed_plus_v2.capability.HAState
import com.github.cecnull1.cecnull1_changed_plus_v2.packet.HaStateNetworkHandler
import com.mojang.logging.LogUtils.getLogger
import net.ltxprogrammer.changed.data.AccessorySlots
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

interface IPlayerExtendedData {
    fun getMPlayerExtendedData(): MPlayerExtendedData
    fun setMPlayerExtendedData(data: MPlayerExtendedData)
    fun save(tag: CompoundTag)
    fun load(tag: CompoundTag)
}

data class MPlayerExtendedData(
    val haState: HAState = HAState()
) {
    companion object {
        var Player.hasHA: Boolean
            get() {
                if (this is IPlayerExtendedData) {
                    return this.getMPlayerExtendedData().haState.hasHA
                }
                return false
            }
            set(value) {
                if (this is IPlayerExtendedData) {
                    val data = this.getMPlayerExtendedData()
                    val old = data.haState.hasHA
                    if (value != old) {
                        data.haState.hasHA = value
                        getLogger().info("${this.level()}: Has HA changed from $old to $value")
                        if (this is ServerPlayer) HaStateNetworkHandler.sendToClient(this)
                    }
                }
            }

        var Player.hasArmorHA: Boolean
            get() {
                if (this is IPlayerExtendedData) {
                    return this.getMPlayerExtendedData().haState.hasArmorHA
                }
                return false
            }
            set(value) {
                if (this is IPlayerExtendedData) {
                    val data = this.getMPlayerExtendedData()
                    val old = data.haState.hasArmorHA
                    if (old != value) {
                        data.haState.hasArmorHA = value
                        getLogger().info("${this.level()}: Has Armor HA changed from $old to $value")
                        if (this is ServerPlayer) HaStateNetworkHandler.sendToClient(this)
                    }
                }
            }

        var Player.haItem: ItemStack
            get() {
                if (this is IPlayerExtendedData) {
                    return this.getMPlayerExtendedData().haState.haItem
                }
                return ItemStack.EMPTY
            }
            set(value) {
                if (this is IPlayerExtendedData) {
                    val data = this.getMPlayerExtendedData()
                    val old = data.haState.haItem
                    if (old !== value && !ItemStack.matches(old, value)) {
                        data.haState.haItem = value
                        getLogger().info("${this.level()}: HA Item changed from $old to $value")
                        if (this is ServerPlayer) HaStateNetworkHandler.sendToClient(this)
                    }
                }
            }

        var Player.haArmorItems: MutableMap<EquipmentSlot, ItemStack>
            get() {
                if (this is IPlayerExtendedData) {
                    return this.getMPlayerExtendedData().haState.haArmorItems
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
                    val data = this.getMPlayerExtendedData()
                    val old = data.haState.haArmorItems
                    if (old !== value && old != value) {
                        data.haState.haArmorItems = value
                        getLogger().info("${this.level()}: HA Armor Items changed from $old to $value")
                        if (this is ServerPlayer) HaStateNetworkHandler.sendToClient(this)
                    }
                }
            }

        var Player.haAccessorySlots: AccessorySlots
            get() {
                if (this is IPlayerExtendedData) {
                    this.getMPlayerExtendedData().haState.haAccessorySlots
                }
                return AccessorySlots()
            }
            set(value) {
                if (this is IPlayerExtendedData) {
                    val data = this.getMPlayerExtendedData()
                    val old = data.haState.haAccessorySlots
                    if (old !== value && old != value) {
                        data.haState.haAccessorySlots = value
                        if (this is ServerPlayer) HaStateNetworkHandler.sendToClient(this)
                    }
                }
            }
    }
}