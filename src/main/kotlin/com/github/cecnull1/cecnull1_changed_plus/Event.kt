package com.github.cecnull1.cecnull1_changed_plus

import com.github.cecnull1.cecnull1_changed_plus.utils.MCreatorFunction.findNearestEntity
import com.github.cecnull1.cecnull1_changed_plus.utils.modData
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.event.entity.living.LivingEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.FORGE, value = [Dist.DEDICATED_SERVER])
object Event {
    @JvmStatic
    @SubscribeEvent
    fun onEntityTick(event: LivingEvent.LivingUpdateEvent) {
        val entity = event.entityLiving
        val entityModData = event.entityLiving.modData
        when {
            entityModData.contains("ride") -> {
                val ride = entity.level.findNearestEntity(entity.position(), 4.0)
                if (ride != null)
                    entity.startRiding(ride)
                entityModData.remove("ride")
            }
        }
    }
}