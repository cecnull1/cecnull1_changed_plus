package com.github.cecnull1.cecnull1_changed_plus_v2.entity

import net.ltxprogrammer.changed.entity.ChangedEntity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level

interface IChangedEntityEx {
    fun variantTickEnd(level: Level) {}
}

abstract class ChangedEntityEx(type: EntityType<out ChangedEntity>, level: Level): ChangedEntity(type, level), IChangedEntityEx {
    override fun tick() {
        super.tick()
    }
}