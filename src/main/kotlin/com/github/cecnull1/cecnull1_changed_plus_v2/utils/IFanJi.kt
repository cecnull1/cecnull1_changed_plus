package com.github.cecnull1.cecnull1_changed_plus_v2.utils

import com.github.cecnull1.cecnull1_changed_plus_v2.event.syncHeadLookAt
import com.github.cecnull1.cecnull1_changed_plus_v2.gamerule.ModGameRule
import com.github.cecnull1.cecnull1lib.utils.nbt.asInt
import com.github.cecnull1.cecnull1lib.utils.nbt.set
import net.ltxprogrammer.changed.entity.ChangedEntity
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player

interface IFanJi {
    fun onAttackedBy(attacker: Entity) {
        if (this !is ChangedEntity) return
        if (!this.level().gameRules.getBoolean(ModGameRule.canFanJi)) return
        val livingEntity = this.maybeGetUnderlying()
        syncHeadLookAt(livingEntity, attacker)
        if (livingEntity is Player) {
            attacker.persistentData["NoStackOverflowError"] = attacker.persistentData["NoStackOverflowError"].asInt() + 1
            if (attacker.persistentData["NoStackOverflowError"].asInt() > 5) {
                attacker.persistentData.remove("NoStackOverflowError")
            } else {
                livingEntity.attack(attacker)
            }
        } else {
            livingEntity.doHurtTarget(attacker)
        }

        livingEntity.swing(InteractionHand.MAIN_HAND, true)
    }
}

/*

*
* */