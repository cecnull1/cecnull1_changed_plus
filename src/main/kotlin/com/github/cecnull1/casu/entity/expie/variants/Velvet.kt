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

class Velvet(type: EntityType<out AbstractExperiment>, level: Level) : AbstractExperiment(type, level) {
    override fun setAttributes(attributes: AttributeMap) {
        super.setAttributes(attributes)
        attributes[Attributes.ATTACK_DAMAGE] *= 0.9
        attributes[Attributes.ATTACK_SPEED] *= 0.9
        attributes[Attributes.MOVEMENT_SPEED] *= 0.93
        attributes[Attributes.MAX_HEALTH] *= 1.2
        attributes[ChangedAttributes.AIR_CAPACITY.get()] *= 0.91
        attributes[ChangedAttributes.JUMP_STRENGTH.get()] *= 0.95
        attributes[ChangedAttributes.FALL_RESISTANCE.get()] *= 0.95
        attributes[ChangedAttributes.SPRINT_SPEED.get()] *= 0.98
        attributes[ForgeMod.SWIM_SPEED.get()] *= 0.90
    }
}