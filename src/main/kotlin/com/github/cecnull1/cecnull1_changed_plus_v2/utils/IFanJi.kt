package com.github.cecnull1.cecnull1_changed_plus_v2.utils

import com.github.cecnull1.cecnull1_changed_plus_v2.sendPositionUpdate
import com.mojang.logging.LogUtils.getLogger
import net.ltxprogrammer.changed.entity.ChangedEntity
import net.minecraft.commands.arguments.EntityAnchorArgument
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec3

interface IFanJi {
    fun onAttackedBy(attacker: Entity) {
        if (this !is ChangedEntity) return

        val livingEntity = this.maybeGetUnderlying()

        livingEntity.lookAt(EntityAnchorArgument.Anchor.EYES, attacker.position() ?: Vec3.ZERO)
        if (livingEntity is Player) {
            livingEntity.attack(attacker)
            if (livingEntity is ServerPlayer) livingEntity.sendPositionUpdate()
            getLogger().info("$livingEntity attack $attacker")
        } else {
            livingEntity.doHurtTarget(attacker)
        }

        livingEntity.swing(InteractionHand.MAIN_HAND, true)
    }
}

/*

*
* */