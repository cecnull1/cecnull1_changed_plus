package com.github.cecnull1.casu.entity.expie.variants

import com.github.cecnull1.casu.entity.expie.AbstractExperiment
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.get
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.set
import net.ltxprogrammer.changed.init.ChangedAttributes
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.attributes.AttributeMap
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.level.Level

// TODO: Moving slowly allows you to camouflage.
class Voyager(type: EntityType<out AbstractExperiment>, level: Level) : AbstractExperiment(type, level) {
    override fun setAttributes(attributes: AttributeMap) {
        super.setAttributes(attributes)
        attributes[Attributes.MOVEMENT_SPEED] *= 1.14
        attributes[Attributes.MAX_HEALTH] *= 0.84
        attributes[ChangedAttributes.AIR_CAPACITY.get()] *= 1.05
        attributes[ChangedAttributes.JUMP_STRENGTH.get()] *= 1.10
        attributes[ChangedAttributes.FALL_RESISTANCE.get()] *= 1.10
        attributes[ChangedAttributes.SPRINT_SPEED.get()] *= 1.12
    }
}