package com.github.cecnull1.cecnull1_changed_plus_v2.event

import com.github.cecnull1.cecnull1_cforge.core.IEvent
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import net.minecraftforge.event.TickEvent

data class TakeOffEvent(val player: Player, val slot: Slot, val itemStack: ItemStack): IEvent {
    override var isCanceled: Boolean = false
    override val isCancelable: Boolean = true
}

@JvmInline
value class ByForgeEvent<T: net.minecraftforge.eventbus.api.Event>(val event: T): IEvent {
    override inline var isCanceled: Boolean
    inline get() = event.isCanceled
    inline set(value) {
        if (isCancelable) event.isCanceled = value
    }

    override inline val isCancelable: Boolean
    inline get() = event.isCancelable
    override val isInterruptibleWhenCanceled: Boolean
        get() = true
}

data class CPlayerTickEvent(val player: Player, val phase: TickEvent.Phase): IEvent {
    override var isCanceled: Boolean = false
    override val isCancelable: Boolean = false
}

data class CLivingTickEvent(val entity: net.minecraft.world.entity.LivingEntity, val phase: TickEvent.Phase): IEvent {
    override var isCanceled: Boolean = false
    override val isCancelable: Boolean = false
}