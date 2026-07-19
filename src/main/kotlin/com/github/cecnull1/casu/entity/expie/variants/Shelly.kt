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

// TODO: Lowered health regeneration
class Shelly(type: EntityType<out AbstractExperiment>, level: Level) : AbstractExperiment(type, level) {
    override fun setAttributes(attributes: AttributeMap) {
        super.setAttributes(attributes)
        attributes[Attributes.ATTACK_DAMAGE] *= 0.7
        attributes[Attributes.ATTACK_SPEED] *= 0.7
        attributes[Attributes.MOVEMENT_SPEED] *= 0.79
        attributes[Attributes.MAX_HEALTH] *= 1.5
        attributes[ChangedAttributes.AIR_CAPACITY.get()] *= 1.25
        attributes[ChangedAttributes.JUMP_STRENGTH.get()] *= 0.95
        attributes[ChangedAttributes.FALL_RESISTANCE.get()] *= 0.95
        attributes[ChangedAttributes.SPRINT_SPEED.get()] *= 0.90
        attributes[ForgeMod.SWIM_SPEED.get()] *= 0.85
        attributes[ChangedAttributes.FALL_RESISTANCE.get()] *= 1.25
        // TODO: replace: 80% explosion resistance
        attributes[Attributes.ARMOR] = 20
        attributes[Attributes.ARMOR_TOUGHNESS] = 20
    }

    override fun variantTick(level: Level) {
        super.variantTick(level)
        maybeGetUnderlying().clearFire()
    }
}