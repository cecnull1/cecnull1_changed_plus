package com.github.cecnull1.cecnull1_changed_plus_v2.command

import com.github.cecnull1.cecnull1_cforge.core.ComponentCore.addComponent
import com.github.cecnull1.cecnull1_changed_plus_v2.component.AutoMove
import com.github.cecnull1.cecnull1_changed_plus_v2.component.Flying
import com.github.cecnull1.cecnull1_changed_plus_v2.component.WFXCOwner
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.EntityExtendedComponent
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.toRL
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.DoubleArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import net.minecraft.commands.CommandRuntimeException
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.Commands.literal
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.network.chat.Component
import net.minecraftforge.event.RegisterCommandsEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.server.command.EnumArgument

@Mod.EventBusSubscriber(modid = MODID)
object CommandEvent {
    @JvmStatic
    @SubscribeEvent
    fun onRegisterCommandsEvent(event: RegisterCommandsEvent) {
        val dispatcher = event.dispatcher

        dispatcher.createCommand(MODID) c1@ {
            this@c1.then("component") c2@ {
                this@c2.then("set") c3@ {
                    argument("target", EntityArgument.entity()).run c4@ {
                        this@c4.then("WFXCOwner") {
                            argument("owner", EntityArgument.player()).executes {
                                val target = EntityArgument.getEntity(it, "target")
                                val owner = EntityArgument.getPlayer(it, "owner")
                                (target as EntityExtendedComponent).components.addComponent("WFXCOwner".toRL(),
                                    WFXCOwner(owner.uuid))
                                0
                            }
                        }.then("Flying") {
                            argument("enable", enableEnum).executes {
                                runCatching {
                                    val target = EntityArgument.getEntity(it, "target")
                                    val enable = it.getArgument("enable", BoolEnable::class.java)
                                    (target as EntityExtendedComponent).components.addComponent("Flying".toRL(), Flying(enable.isEnable()))
                                }.onFailure { throwable ->
                                    throw CommandRuntimeException(Component.literal(throwable.toString()))
                                }
                                0
                            }
                        }.then("AutoMove") {
                            argument("enable", enableEnum).then(argument("divSpeed", DoubleArgumentType.doubleArg()).executes {
                                val target = EntityArgument.getEntity(it, "target")
                                val enable = it.getArgument("enable", BoolEnable::class.java)
                                val speed = DoubleArgumentType.getDouble(it, "divSpeed")
                                (target as EntityExtendedComponent).components.addComponent("AutoMove".toRL(), AutoMove(
                                    enable.isEnable(),
                                    speed
                                ))
                                0
                            })
                        }
                    }
                }
            }
        }
    }
}

val enableEnum: EnumArgument<BoolEnable> = EnumArgument.enumArgument(BoolEnable::class.java)

enum class BoolEnable {
    False,
    True;

    fun isEnable(): Boolean = this === True
}

fun CommandDispatcher<CommandSourceStack>.createCommand(commandName: String, node: LiteralArgumentBuilder<CommandSourceStack>.() -> LiteralArgumentBuilder<CommandSourceStack>) {
    this.root.addChild(node(literal(commandName)).build())
}

fun LiteralArgumentBuilder<CommandSourceStack>.then(commandName: String, node: ArgumentBuilder<CommandSourceStack, *>.() -> ArgumentBuilder<CommandSourceStack, *>): LiteralArgumentBuilder<CommandSourceStack> {
    return this.then(node(literal(commandName)))
}

fun ArgumentBuilder<CommandSourceStack, *>.then(commandName: String, node: ArgumentBuilder<CommandSourceStack, *>.() -> ArgumentBuilder<CommandSourceStack, *>): ArgumentBuilder<CommandSourceStack, *> {
    return this.then(node(literal(commandName)))
}

fun RequiredArgumentBuilder<CommandSourceStack, *>.then(commandName: String, node: ArgumentBuilder<CommandSourceStack, *>.() -> ArgumentBuilder<CommandSourceStack, *>): RequiredArgumentBuilder<CommandSourceStack, *> {
    return this.then(node(literal(commandName)))
}

fun <T> argument(name: String, type: ArgumentType<T>): RequiredArgumentBuilder<CommandSourceStack, T> {
    return Commands.argument(name, type)
}