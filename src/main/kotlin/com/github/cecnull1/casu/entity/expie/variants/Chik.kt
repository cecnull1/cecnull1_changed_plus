package com.github.cecnull1.casu.entity.expie.variants

import com.github.cecnull1.casu.entity.expie.AbstractExperiment
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.get
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.set
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.attributes.AttributeMap
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import net.minecraftforge.common.ForgeMod

class Chik(type: EntityType<out AbstractExperiment>, level: Level) : AbstractExperiment(type, level) {
    override fun setAttributes(attributes: AttributeMap) {
        super.setAttributes(attributes)
        attributes[Attributes.ATTACK_SPEED] *= 1.4
        attributes[Attributes.MOVEMENT_SPEED] *= 1.14
        attributes[Attributes.MAX_HEALTH] *= 0.9
        attributes[ForgeMod.SWIM_SPEED.get()] *= 0.8
    }

    override fun variantTick(level: Level) {
        super.variantTick(level)
        val living = maybeGetUnderlying()
        living.deltaMovement = Vec3(living.deltaMovement.x, living.deltaMovement.y.coerceAtLeast(-0.4), living.deltaMovement.z)
        living.resetFallDistance()
    }
}