package com.github.cecnull1.cecnull1_changed_plus_v2.utils

import com.github.cecnull1.cecnull1_changed_plus_v2.mixin.AccessorySlotsAccessor
import net.ltxprogrammer.changed.data.AccessorySlotType
import net.ltxprogrammer.changed.data.AccessorySlots
import net.ltxprogrammer.changed.entity.ChangedEntity
import net.ltxprogrammer.changed.entity.LivingEntityDataExtension
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

val AccessorySlots.fieldItems: Map<AccessorySlotType, ItemStack>?
    get() = (this as? AccessorySlotsAccessor)?.getItems()

fun LivingEntity.accessorySlotsFast(): AccessorySlots? {
    var cur: LivingEntity? = this
    while (cur != null) {
        when (cur) {
            is ChangedEntity if cur.underlyingPlayer != null ->
                cur = cur.underlyingPlayer

            is LivingEntityDataExtension -> return cur.accessorySlots.orElse(null)
            else -> return null
        }
    }
    return null
}