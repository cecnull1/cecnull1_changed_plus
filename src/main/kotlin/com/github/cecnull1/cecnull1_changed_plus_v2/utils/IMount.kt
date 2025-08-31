package com.github.cecnull1.cecnull1_changed_plus_v2.utils

import net.minecraft.world.entity.player.Player

interface IMount {
    fun canMount(entity: Player): Boolean = false
}