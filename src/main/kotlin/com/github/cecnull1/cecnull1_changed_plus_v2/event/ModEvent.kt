@file:EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.FORGE)
@file:JvmName("ModEvent")

package com.github.cecnull1.cecnull1_changed_plus_v2.event

import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus_v2.event.Event.onAccessoryDrop
import com.github.cecnull1.cecnull1_changed_plus_v2.event.Event.onBlockBreak
import com.github.cecnull1.cecnull1_changed_plus_v2.event.Event.onDimensionChange
import com.github.cecnull1.cecnull1_changed_plus_v2.event.Event.onEntityPickup
import com.github.cecnull1.cecnull1_changed_plus_v2.event.Event.onEntityVariantAssigned
import com.github.cecnull1.cecnull1_changed_plus_v2.event.Event.onHurt
import com.github.cecnull1.cecnull1_changed_plus_v2.event.Event.onInteract
import com.github.cecnull1.cecnull1_changed_plus_v2.event.Event.onLivingAttack
import com.github.cecnull1.cecnull1_changed_plus_v2.event.Event.onLivingFall
import com.github.cecnull1.cecnull1_changed_plus_v2.event.Event.onLivingKnockBack
import com.github.cecnull1.cecnull1_changed_plus_v2.event.Event.onMount
import com.github.cecnull1.cecnull1_changed_plus_v2.event.Event.onPlayerCloned
import com.github.cecnull1.cecnull1_changed_plus_v2.event.Event.onPlayerLogin
import com.github.cecnull1.cecnull1_changed_plus_v2.event.Event.onPlayerRespawn
import net.ltxprogrammer.changed.data.AccessorySlots
import net.ltxprogrammer.changed.process.ProcessTransfur
import net.minecraftforge.event.entity.EntityMountEvent
import net.minecraftforge.event.entity.living.LivingAttackEvent
import net.minecraftforge.event.entity.living.LivingFallEvent
import net.minecraftforge.event.entity.living.LivingHurtEvent
import net.minecraftforge.event.entity.living.LivingKnockBackEvent
import net.minecraftforge.event.entity.player.EntityItemPickupEvent
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.event.level.BlockEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber

fun onTakeOff(event: TakeOffEvent) {
    event.isCanceled = true
}

fun onForgeEvent(event: ByForgeEvent<*>) {
    when (val e = event.event) {
        is LivingFallEvent -> onLivingFall(e)
        is AccessorySlots.DropItemEvent -> onAccessoryDrop(e)
        is PlayerEvent.PlayerLoggedInEvent -> onPlayerLogin(e)
        is PlayerEvent.PlayerChangedDimensionEvent -> onDimensionChange(e)
        is PlayerEvent.PlayerRespawnEvent -> onPlayerRespawn(e)
        is PlayerEvent.Clone -> onPlayerCloned(e)
        is BlockEvent.BreakEvent -> onBlockBreak(e)
        is EntityItemPickupEvent -> onEntityPickup(e)
        is LivingKnockBackEvent -> onLivingKnockBack(e)
        is LivingAttackEvent -> onLivingAttack(e)
        is LivingHurtEvent -> onHurt(e)
        is ProcessTransfur.EntityVariantAssigned.ChangedVariant -> onEntityVariantAssigned(e)
        is PlayerInteractEvent.EntityInteract -> onInteract(e)
        is EntityMountEvent -> onMount(e)
    }
}