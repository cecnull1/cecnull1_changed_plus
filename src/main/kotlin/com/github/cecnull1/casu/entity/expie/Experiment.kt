package com.github.cecnull1.casu.entity.expie

import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.attributes.AttributeMap
import net.minecraft.world.level.Level

/**
 * # Character (gunsaw):
 * > - The original artificial hybrid, and the for all other species. All others are in one way or another, related to it.
 * > - Tend to be quite skinny, medium height.
 * > - Average in just about every way. Intelligence, skill, endurance. Mentally sensitive in comparison.
 * > - Their darker fur makes them better at hiding, though not by much. Supports their cowardly nature.
 * > - Less social than others, and odd-minded. Can act a bit mischievous at times. Good at cracking jokes.
 * > - Being the first to arrive, they are very high in numbers. Low ranking cannon fodder.
 * > - Canine.
 * >
 * > The one we all come from?
 * > Probably not natural.
 *
 * */
class Experiment(type: EntityType<out AbstractExperiment>, level: Level) : AbstractExperiment(type, level) {
    override fun setAttributes(attributes: AttributeMap) {
        super.setAttributes(attributes)
    }
}