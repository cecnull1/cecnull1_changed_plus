package com.github.cecnull1.cecnull1_changed_plus

import com.github.cecnull1.cecnull1_changed_plus.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus.constant.Constant.NBTKeys.BetterNeon.WFXC
import com.github.cecnull1.cecnull1_changed_plus.utils.Mount
import com.github.cecnull1.cecnull1_changed_plus.utils.NoDismounting
import net.ltxprogrammer.changed.entity.robot.Exoskeleton
import net.minecraft.world.entity.Entity
import net.minecraftforge.event.TickEvent.PlayerTickEvent
import net.minecraftforge.event.entity.EntityMountEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.FORGE)
object Event {
    @JvmStatic
    @SubscribeEvent
    fun onPlayerTick(event: PlayerTickEvent) {

    }

    @JvmStatic
    @SubscribeEvent
    fun onMount(event: EntityMountEvent) {
        val entityMounting: Entity = event.entityMounting ?: return
        val entityBeingMounted: Entity = event.entityBeingMounted ?: return
        if (entityMounting.isAlive && entityBeingMounted.isAlive && event.isDismounting) {
            when {
                entityBeingMounted is NoDismounting -> event.isCanceled = true
                entityBeingMounted.persistentData.getBoolean(WFXC) -> event.isCanceled = true
                entityBeingMounted is Exoskeleton -> event.isCanceled = true
            }
        }
    }

    @JvmStatic
    @SubscribeEvent
    fun onInteract(event: PlayerInteractEvent.EntityInteract) {
        if ((event.target ?: return) is Mount) {
            event.player.startRiding(event.target ?: return)
        }
//        if (!event.world.isClientSide) {
//            event.player.startRiding(event.target ?: return)
//        }
    }
}