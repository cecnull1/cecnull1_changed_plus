package com.github.cecnull1.casu.entity.expie.variants

import com.github.cecnull1.casu.entity.expie.AbstractExperiment
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.get
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.set
import net.ltxprogrammer.changed.init.ChangedAttributes
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.attributes.AttributeMap
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.level.Level
import net.minecraftforge.common.ForgeMod

/**
 * Gansaw: End + Space: Unlock Albino
 *
 * Gameplay (gunsaw):
 * > Weak, frail and unfit for combat.
 * > Basically hard mode.
 *
 * @see com.github.cecnull1.casu.entity.expie.Experiment
 * */
class Albino(type: EntityType<out AbstractExperiment>, level: Level) : AbstractExperiment(type, level) {
    override fun setAttributes(attributes: AttributeMap) {
        super.setAttributes(attributes)
        attributes[Attributes.ATTACK_DAMAGE] *= 0.85 * 0.8
        attributes[Attributes.ATTACK_SPEED] *= 0.85
        attributes[Attributes.MOVEMENT_SPEED] *= 0.93
        attributes[Attributes.MAX_HEALTH] *= 0.8
        attributes[ChangedAttributes.AIR_CAPACITY.get()] *= 0.83
        attributes[ChangedAttributes.JUMP_STRENGTH.get()] *= 0.9
        attributes[ChangedAttributes.FALL_RESISTANCE.get()] *= 0.9
        attributes[ChangedAttributes.SPRINT_SPEED.get()] *= 0.96
        attributes[ChangedAttributes.MINING_SPEED.get()] *= 0.8
        attributes[ForgeMod.SWIM_SPEED.get()] *= 0.8
    }
}