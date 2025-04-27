package com.github.cecnull1.cecnull1_changed_plus.utils

import net.ltxprogrammer.changed.util.ItemUtil.getWearingItems
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraftforge.fml.ModList

object EUtils {
    /**
     * 检查玩家是否穿戴了带有特定附魔的物品。
     *
     * @param player 玩家实体
     * @param enchantment 要检查的附魔
     * @return 如果玩家穿戴了带有指定附魔的物品，则返回 true；否则返回 false。
     */
    fun hasSpecificEnchantment(player: LivingEntity, enchantment: Enchantment): Boolean {
        if (ModList.get().isLoaded("changed")) {
            for (slot in getWearingItems(player)) {
                val itemStack = slot.itemStack
                if (hasEnchantment(itemStack, enchantment)) return true
            }
        }
        for (slot in EquipmentSlot.entries) {
            val itemStack = player.getItemBySlot(slot)
            if (hasEnchantment(itemStack, enchantment)) return true
        }
        return false
    }

    fun hasEnchantment(
        itemStack: ItemStack,
        enchantment: Enchantment
    ): Boolean {
        if (!itemStack.isEmpty && EnchantmentHelper.getItemEnchantmentLevel(enchantment, itemStack) > 0) {
            return true
        }
        return false
    }

    /**
     * 获取玩家装备中特定附魔的最高等级。
     *
     * @param player 玩家实体
     * @param enchantment 要检查的附魔
     * @return 玩家装备中特定附魔的最高等级，如果没有找到，则返回 0。
     */
    fun getHighestEnchantmentLevel(player: Player, enchantment: Enchantment): Int {
        var highestLevel = 0
        for (slot in EquipmentSlot.entries) {
            val itemStack = player.getItemBySlot(slot)
            if (!itemStack.isEmpty) {
                val enchantmentLevel = EnchantmentHelper.getItemEnchantmentLevel(enchantment, itemStack)
                if (enchantmentLevel > highestLevel) {
                    highestLevel = enchantmentLevel
                }
            }
        }
        return highestLevel
    }
}