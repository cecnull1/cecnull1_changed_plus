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

class Leapy(type: EntityType<out AbstractExperiment>, level: Level) : AbstractExperiment(type, level) {
    override fun setAttributes(attributes: AttributeMap) {
        super.setAttributes(attributes)
        attributes[Attributes.MAX_HEALTH] *= 0.7
        attributes[Attributes.MOVEMENT_SPEED] *= 1.36
        attributes[ForgeMod.SWIM_SPEED.get()] *= 0.9
        attributes[Attributes.ATTACK_DAMAGE] *= 1.10
        attributes[Attributes.ATTACK_SPEED] *= 1.10
        attributes[ChangedAttributes.AIR_CAPACITY.get()] *= 0.77
        attributes[ChangedAttributes.JUMP_STRENGTH.get()] *= 1.3
        attributes[ChangedAttributes.FALL_RESISTANCE.get()] *= 1.3
    }
}