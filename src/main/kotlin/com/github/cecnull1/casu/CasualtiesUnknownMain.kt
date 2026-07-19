package com.github.cecnull1.casu

import com.github.cecnull1.casu.entity.expie.AbstractExperiment
import com.github.cecnull1.casu.entity.expie.ExpieRegister
import com.github.cecnull1.cecnull1_changed_plus_v2.renderer.NoneEntityRenderer
import net.ltxprogrammer.changed.entity.ChangedEntity
import net.ltxprogrammer.changed.init.ChangedAttributes
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.level.GameRules
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers
import net.minecraftforge.event.entity.EntityAttributeCreationEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import java.util.function.Supplier

const val MODID = "casualties_cecnull1"

@Mod(MODID)
class CasualtiesUnknownMain(context: FMLJavaModLoadingContext) {
    init {
        val modBus = context.modEventBus
        ExpieRegister.entityRegistry.register(modBus)
        ExpieRegister.variantRegistry.register(modBus)
        ModGameRules.initExperiment
    }
}

object ModGameRules {
    val initExperiment: GameRules.Key<GameRules.BooleanValue> = GameRules.register(
        "$MODID:initExperiment",
        GameRules.Category.PLAYER,
        GameRules.BooleanValue.create(false)
    )
}

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
object Events {
    @JvmStatic
    @SubscribeEvent
    fun onCommonSetup(event: FMLCommonSetupEvent) {
    }

    @JvmStatic
    @SubscribeEvent
    fun registerEntityAttributes(event: EntityAttributeCreationEvent) {
        ExpieRegister.apply {
            with(event) {
                registerExpieAttributes(experimentType)
                registerExpieAttributes(orangeType)
                registerExpieAttributes(milkyType)
                registerExpieAttributes(leapyType)
                registerExpieAttributes(rozaType)
                registerExpieAttributes(voyagerType)
                registerExpieAttributes(baronType)
                registerExpieAttributes(chikType)
                registerExpieAttributes(crystalType)
                registerExpieAttributes(velvetType)
                registerExpieAttributes(ickeType)
                registerExpieAttributes(shellyType)
                registerExpieAttributes(duneType)
                registerExpieAttributes(albinoType)
            }
        }
    }

    context(t: EntityAttributeCreationEvent)
    private fun registerExpieAttributes(e: Supplier<out EntityType<out AbstractExperiment>>) {
        t.put(e.get(), ChangedEntity.createLatexAttributes()
            .add(Attributes.ATTACK_SPEED, 4.0).build())
    }

    @JvmStatic
    @SubscribeEvent
    fun registerEntityRenderers(event: RegisterRenderers) {
        ExpieRegister.apply {
            with(event) {
                registerExpieRenderer(experimentType)
                registerExpieRenderer(orangeType)
                registerExpieRenderer(milkyType)
                registerExpieRenderer(leapyType)
                registerExpieRenderer(rozaType)
                registerExpieRenderer(voyagerType)
                registerExpieRenderer(baronType)
                registerExpieRenderer(chikType)
                registerExpieRenderer(crystalType)
                registerExpieRenderer(velvetType)
                registerExpieRenderer(ickeType)
                registerExpieRenderer(shellyType)
                registerExpieRenderer(duneType)
                registerExpieRenderer(albinoType)
            }
        }
    }

    context(t: RegisterRenderers)
    private fun registerExpieRenderer(e: Supplier<out EntityType<out AbstractExperiment>>) {
        t.registerEntityRenderer(e.get(), ::NoneEntityRenderer)
    }
}