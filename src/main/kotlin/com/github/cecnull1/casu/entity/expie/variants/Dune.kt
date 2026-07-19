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

// TODO: Crouch over corpses to eat them and heal
// TODO: Lowered health regeneration
class Dune(type: EntityType<out AbstractExperiment>, level: Level) : AbstractExperiment(type, level) {
    override fun setAttributes(attributes: AttributeMap) {
        super.setAttributes(attributes)
        attributes[Attributes.ATTACK_SPEED] *= 1.5
        attributes[Attributes.ATTACK_DAMAGE] *= 1.35
        attributes[Attributes.MOVEMENT_SPEED] *= 1.11
        attributes[Attributes.MAX_HEALTH] *= 0.9
        attributes[ChangedAttributes.JUMP_STRENGTH.get()] *= 1.15
        attributes[ChangedAttributes.FALL_RESISTANCE.get()] *= 1.15
        attributes[ChangedAttributes.SPRINT_SPEED.get()] *= 1.1
        attributes[ForgeMod.SWIM_SPEED.get()] *= 2.0 // TODO: Fast Swimmer = ?
    }
}