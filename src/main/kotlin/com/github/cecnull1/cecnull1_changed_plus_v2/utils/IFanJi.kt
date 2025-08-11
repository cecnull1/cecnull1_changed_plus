package com.github.cecnull1.cecnull1_changed_plus_v2.utils

import com.github.cecnull1.cecnull1_changed_plus_v2.event.sendPositionUpdate
import com.github.cecnull1.cecnull1_changed_plus_v2.gamerule.ModGameRule
import com.github.cecnull1.cecnull1lib.utils.nbt.asInt
import com.github.cecnull1.cecnull1lib.utils.nbt.set
import com.mojang.logging.LogUtils.getLogger
import net.ltxprogrammer.changed.entity.ChangedEntity
import net.minecraft.commands.arguments.EntityAnchorArgument
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec3

interface IFanJi {
    fun onAttackedBy(attacker: Entity) {
        if (this !is ChangedEntity) return
        if (!this.level().gameRules.getBoolean(ModGameRule.canFanJi)) return
        val livingEntity = this.maybeGetUnderlying()
//        for (player in livingEntity.level().players().filterIsInstance<ServerPlayer>()) {
//            player.displayClientMessage(Component.literal("$livingEntity->$attacker"), false)
//        }
//        getLogger().info("$livingEntity->$attacker")
        livingEntity.lookAt(EntityAnchorArgument.Anchor.EYES, attacker.position() ?: Vec3.ZERO)
        if (livingEntity is Player) {
            attacker.persistentData["NoStackOverflowError"] = attacker.persistentData["NoStackOverflowError"].asInt() + 1
            if (attacker.persistentData["NoStackOverflowError"].asInt() > 5) {
                attacker.persistentData.remove("NoStackOverflowError")
            } else {
                livingEntity.attack(attacker)
            }
            if (livingEntity is ServerPlayer) livingEntity.sendPositionUpdate()

        } else {
            livingEntity.doHurtTarget(attacker)
        }

        livingEntity.swing(InteractionHand.MAIN_HAND, true)
    }
}

/*

*
* */