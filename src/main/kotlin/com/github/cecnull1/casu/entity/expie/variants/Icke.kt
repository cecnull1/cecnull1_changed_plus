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

// TODO: Higher move, reload and aim speed the lower your HP is.
class Icke(type: EntityType<out AbstractExperiment>, level: Level) : AbstractExperiment(type, level) {
    override fun setAttributes(attributes: AttributeMap) {
        super.setAttributes(attributes)
        attributes[Attributes.ATTACK_DAMAGE] *= 0.9 * 2
        attributes[Attributes.ATTACK_SPEED] *= 0.9
        attributes[Attributes.MOVEMENT_SPEED] *= 1.14
        attributes[ChangedAttributes.JUMP_STRENGTH.get()] *= 1.5
        attributes[ChangedAttributes.FALL_RESISTANCE.get()] *= 1.5
        attributes[ChangedAttributes.MINING_SPEED.get()] *= 2
        attributes[ForgeMod.SWIM_SPEED.get()] *= 0.9
    }
}