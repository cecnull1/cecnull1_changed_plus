package com.github.cecnull1.cecnull1_changed_plus.utils

import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level

interface VariantTickPlusAble {
    fun playerVariantTick(player: Player, level: Level?) {
        return
    }
}