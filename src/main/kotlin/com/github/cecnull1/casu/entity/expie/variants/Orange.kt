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

class Orange(type: EntityType<out AbstractExperiment>, level: Level) : AbstractExperiment(type, level) {
    override fun setAttributes(attributes: AttributeMap) {
        super.setAttributes(attributes)
        attributes[Attributes.MAX_HEALTH] *= 1.3
        attributes[Attributes.MOVEMENT_SPEED] *= 0.86
        attributes[ForgeMod.SWIM_SPEED.get()] *= 0.9
        attributes[Attributes.ATTACK_SPEED] *= 0.75
        attributes[Attributes.ATTACK_DAMAGE] *= 0.75
        attributes[ChangedAttributes.AIR_CAPACITY.get()] *= 1.14
    }
}