@file:EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.FORGE)
@file:JvmName("ModEvent")

package com.github.cecnull1.cecnull1_changed_plus_v2.event

import com.github.cecnull1.cecnull1_changed_plus_v2.Events.onCommonSetup
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModTransfurVariant
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
import com.github.cecnull1.cecnull1lib.utils.changed.entityVariant
import net.ltxprogrammer.changed.data.AccessorySlots
import net.ltxprogrammer.changed.process.ProcessTransfur
import net.minecraft.world.entity.player.Player
import net.minecraftforge.event.entity.EntityMountEvent
import net.minecraftforge.event.entity.living.*
import net.minecraftforge.event.entity.player.EntityItemPickupEvent
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.event.level.BlockEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import kotlin.reflect.KClass

/**
 * O(1) 事件处理器映射表：KClass -> (ForgeEvent) -> Unit
 * 注意：Key 是 Forge 事件的 KClass，Value 是处理函数
 */
private val EVENT_HANDLERS: Map<KClass<*>, (Any) -> Unit> = buildMap {
    put(LivingHurtEvent::class) { onHurt(it as LivingHurtEvent) }
    put(LivingAttackEvent::class) { onLivingAttack(it as LivingAttackEvent) }
    put(LivingKnockBackEvent::class) { onLivingKnockBack(it as LivingKnockBackEvent) }
    put(LivingFallEvent::class) { onLivingFall(it as LivingFallEvent) }
    put(EntityItemPickupEvent::class) { onEntityPickup(it as EntityItemPickupEvent) }
    put(PlayerInteractEvent.EntityInteract::class) { onInteract(it as PlayerInteractEvent.EntityInteract) }
    put(BlockEvent.BreakEvent::class) { onBlockBreak(it as BlockEvent.BreakEvent) }

    put(LivingChangeTargetEvent::class) { onLivingChangeTarget(it as LivingChangeTargetEvent) }
    put(EntityMountEvent::class) { onMount(it as EntityMountEvent) }

    put(PlayerEvent.PlayerRespawnEvent::class) { onPlayerRespawn(it as PlayerEvent.PlayerRespawnEvent) }
    put(PlayerEvent.Clone::class) { onPlayerCloned(it as PlayerEvent.Clone) }
    put(PlayerEvent.PlayerChangedDimensionEvent::class) { onDimensionChange(it as PlayerEvent.PlayerChangedDimensionEvent) }
    put(PlayerEvent.PlayerLoggedInEvent::class) { onPlayerLogin(it as PlayerEvent.PlayerLoggedInEvent) }
    put(AccessorySlots.DropItemEvent::class) { onAccessoryDrop(it as AccessorySlots.DropItemEvent) }

    put(ProcessTransfur.EntityVariantAssigned.ChangedVariant::class) { onEntityVariantAssigned(it as ProcessTransfur.EntityVariantAssigned.ChangedVariant) }
    put(ProcessTransfur.KeepConsciousEvent::class) { onKeepConscious(it as ProcessTransfur.KeepConsciousEvent) }
    put(FMLCommonSetupEvent::class) { onCommonSetup(it as FMLCommonSetupEvent) }

}

/**
 * CForge 事件监听器：接收 ByForgeEvent，分发内部的 Forge 事件
 */
fun onForgeEvent(cforgeEvent: ByForgeEvent<*>) {
    val forgeEvent = cforgeEvent.event
    val handler = EVENT_HANDLERS[forgeEvent::class]
    handler?.invoke(forgeEvent)
}

fun onTakeOff(event: TakeOffEvent) {
}

fun onLivingChangeTarget(event: LivingChangeTargetEvent) {
    (event.newTarget as? Player)?.let {
        if (it.entityVariant?.`is`(ModTransfurVariant.SOUL_TRANSFUR_VARIANT.get()) == true) {
            event.newTarget = event.originalTarget
        }
    }
}

fun onKeepConscious(event: ProcessTransfur.KeepConsciousEvent) {
    if (event.variant?.`is`(ModTransfurVariant.A_ENTITY_TRANSFUR_VARIANT) == true ||
        event.variant?.`is`(ModTransfurVariant.PURE_WHITE_LATEX_YUFENG_TRANSFUR_VARIANT) == true ||
        event.variant?.`is`(ModTransfurVariant.PURE_WHITE_LATEX_YUFENG_BY_NCDBOAT_TRANSFUR_VARIANT) == true) {
            event.shouldKeepConscious = true
    }
}