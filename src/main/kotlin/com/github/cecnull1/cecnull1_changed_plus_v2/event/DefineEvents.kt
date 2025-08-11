package com.github.cecnull1.cecnull1_changed_plus_v2.event

import com.github.cecnull1.cecnull1_changed_plus_v2.cforge.event.IEvent
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

data class TakeOffEvent(val player: Player, val slot: Slot, val itemStack: ItemStack): IEvent {
    override var isCanceled: Boolean = false
    override val isCancelable: Boolean = true
}

data class ByForgeEvent<T: net.minecraftforge.eventbus.api.Event>(val event: T): IEvent {
    override var isCanceled: Boolean
        get() = event.isCanceled
        set(value) {
            if (isCancelable) event.isCanceled = value
        }
    override val isCancelable: Boolean
        get() = event.isCancelable
}