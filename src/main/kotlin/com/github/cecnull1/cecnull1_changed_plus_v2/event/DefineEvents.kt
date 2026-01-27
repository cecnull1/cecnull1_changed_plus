package com.github.cecnull1.cecnull1_changed_plus_v2.event

import com.github.cecnull1.cecnull1_cforge.core.IEvent
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraftforge.client.event.EntityRenderersEvent
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.entity.EntityAttributeCreationEvent

data class TakeOffEvent(val player: Player, val slot: Slot, val itemStack: ItemStack): IEvent {
    override var isCanceled: Boolean = false
    override val isCancelable: Boolean = true
}

@JvmInline
value class ByForgeEvent(val event: net.minecraftforge.eventbus.api.Event): IEvent {
    override inline var isCanceled: Boolean
    inline get() = event.isCanceled
    inline set(value) {
        if (isCancelable) event.isCanceled = value
    }

    override val isCancelable: Boolean
    inline get() = event.isCancelable
    override val isInterruptibleWhenCanceled: Boolean
        get() = true
}

data class CPlayerTickEvent(val player: Player, val phase: TickEvent.Phase): IEvent {
    override var isCanceled: Boolean = false
    override val isCancelable: Boolean = false
}

data class CLivingTickEvent(val entity: LivingEntity, val phase: TickEvent.Phase): IEvent {
    override var isCanceled: Boolean = false
    override val isCancelable: Boolean = false
}

@JvmInline
value class NullSafeAttributeCreationEvent(val event: EntityAttributeCreationEvent): IEvent {
    override inline var isCanceled: Boolean
        inline get() = event.isCanceled
        inline set(value) {
            if (isCancelable) event.isCanceled = value
        }

    override val isCancelable: Boolean
        inline get() = event.isCancelable
    override val isInterruptibleWhenCanceled: Boolean
        get() = false

    fun put(entity: EntityType<out LivingEntity>, map: AttributeSupplier) {
        event.put(entity, map)
    }
}

@JvmInline
value class RegisterRenderers(val event: EntityRenderersEvent.RegisterRenderers): IEvent {
    override inline var isCanceled: Boolean
        inline get() = event.isCanceled
        inline set(value) {
            if (isCancelable) event.isCanceled = value
        }

    override val isCancelable: Boolean
        inline get() = event.isCancelable
    override val isInterruptibleWhenCanceled: Boolean
        get() = false

    fun <T : Entity> registerEntityRenderer(
        entityType: EntityType<out T>,
        entityRendererProvider: EntityRendererProvider<T>
    ) = event.registerEntityRenderer<T>(entityType, entityRendererProvider)


    fun <T : BlockEntity> registerBlockEntityRenderer(
        blockEntityType: BlockEntityType<out T>,
        blockEntityRendererProvider: BlockEntityRendererProvider<T>
    ) = event.registerBlockEntityRenderer<T>(blockEntityType, blockEntityRendererProvider)
}