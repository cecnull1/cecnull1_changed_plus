package com.github.cecnull1.cecnull1_changed_plus_v2.utils

import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level

interface VariantTickPlusAble {
    /**
     * 为 ChangedEntity 的类提供扩展的 VariantTick
     * 以解决原始的 VariantTick 无法处理对于玩家的情况的问题
     *
     * @param player 将要被处理的玩家对象
     * @param level 当前世界对象
     * */
    fun playerVariantTick(player: Player, level: Level?) {
        return
    }
}