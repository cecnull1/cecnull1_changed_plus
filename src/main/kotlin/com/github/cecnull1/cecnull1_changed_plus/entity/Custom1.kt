package com.github.cecnull1.cecnull1_changed_plus.entity

import net.ltxprogrammer.changed.entity.LatexType
import net.ltxprogrammer.changed.entity.beast.LatexPinkYuinDragon
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.attributes.AttributeMap
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.level.Level
import net.minecraftforge.common.ForgeMod

class Custom1(p_19870_: EntityType<out LatexPinkYuinDragon>?, p_19871_: Level?) : LatexPinkYuinDragon(p_19870_,
    p_19871_
) {
    override fun setAttributes(attributes: AttributeMap?) {
        super.setAttributes(attributes)
        AttributePresets.custom1Attributes(attributes)
    }

    override fun getLatexType(): LatexType {
        return LatexType.WHITE_LATEX
    }
}

object AttributePresets {
    fun custom1Attributes(map: AttributeMap?) {
        map?.getInstance(Attributes.MOVEMENT_SPEED)?.baseValue = 1.5
        map?.getInstance(ForgeMod.SWIM_SPEED.get())?.baseValue = 1.5
        map?.getInstance(Attributes.MAX_HEALTH)?.baseValue = 24.0
    }
}