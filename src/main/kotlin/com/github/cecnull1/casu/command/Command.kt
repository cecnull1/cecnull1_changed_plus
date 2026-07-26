package com.github.cecnull1.casu.command

import com.github.cecnull1.cecnull1_changed_plus_v2.command.argument
import com.github.cecnull1.casu.MODID
import net.minecraft.commands.Commands.literal
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.network.chat.Component
import net.minecraftforge.event.RegisterCommandsEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(modid = MODID)
object Command {
    @JvmStatic
    @SubscribeEvent
    fun onRegisterCommandsEvent(event: RegisterCommandsEvent) {
        val dispatcher = event.dispatcher

        dispatcher.root.addChild(literal("heal").then(argument("players", EntityArgument.players()).executes { context ->
            val players = EntityArgument.getPlayers(context, "players")
            players.forEach {
                it.health = it.maxHealth
                it.foodData.foodLevel = 20
                it.foodData.setSaturation(5.0F)
                it.foodData.setExhaustion(0.0F)
                it.clearFire()
                it.removeAllEffects()
                it.airSupply = it.maxAirSupply
            }
            context.source.sendSuccess(
                {Component.literal("Healed the ${
                    players.joinToString {
                        it.displayName.string
                    }
                }")},
                true
            )
            1
        }.build()).build())
    }
}