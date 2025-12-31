package com.github.cecnull1.cecnull1_changed_plus_v2.utils

import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player

interface IOnMount {
    fun onMount(mountType: MountType = MountType.None): Boolean = true
}

sealed interface MountType {
    data class Mount(val entity: Entity, val target: Entity): MountType
    data class Move(val entity: Entity, val target: Entity): MountType
    data class Dismount(val entity: Entity): MountType
    data class PlayerSelfDismount(val player: Player): MountType
    data object None: MountType
}