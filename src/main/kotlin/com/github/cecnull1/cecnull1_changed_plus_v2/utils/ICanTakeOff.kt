package com.github.cecnull1.cecnull1_changed_plus_v2.utils

import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

interface ICanTakeOff {
    fun canTakeOff(player: Player, slot: Slot, item: ItemStack): Boolean = true
}