package com.github.cecnull1.cecnull1_changed_plus

import com.github.cecnull1.cecnull1_changed_plus.entity.ModEntities
import com.github.cecnull1.cecnull1_changed_plus.entity.ModEntities.A_ENTITY
import com.github.cecnull1.cecnull1_changed_plus.entity.ModTransfurVariant
import com.github.cecnull1.cecnull1_changed_plus.entity.modEventBus
import com.github.cecnull1.cecnull1_changed_plus.modules.AEntityModel
import com.github.cecnull1.cecnull1_changed_plus.renderer.AEntityRenderer
import net.ltxprogrammer.changed.entity.ChangedEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers
import net.minecraftforge.event.entity.EntityAttributeCreationEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventBusSubscriber


const val MODID = "cecnull1_changed_plus"

@Mod(MODID)
class Cecnull1_changed_plus {
    init {
        ModEntities.REGISTRY.register(modEventBus)
        ModTransfurVariant.REGISTRY.register(modEventBus)
    }

    object Const {
        const val ENIGMATIC_LEGACY = com.integral.enigmaticlegacy.EnigmaticLegacy.MODID
    }
}

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD)
object Events {
    @JvmStatic
    @SubscribeEvent
    fun registerEntityAttributes(event: EntityAttributeCreationEvent) {
        event.put(
            A_ENTITY.get(), ChangedEntity.createLatexAttributes()
                .add(Attributes.MAX_HEALTH, 24.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 2.0)
                .build()
        )
    }
}

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = [Dist.CLIENT])
object ClientEvents {
    @JvmStatic
    @SubscribeEvent
    fun registerLayerDefinitions(event: RegisterLayerDefinitions) {
        event.registerLayerDefinition(AEntityModel.LAYER_LOCATION, AEntityModel::createBodyLayer)
    }

    @JvmStatic
    @SubscribeEvent
    fun registerEntityRenderers(event: RegisterRenderers) {
        event.registerEntityRenderer(
            A_ENTITY.get()
        ) { context: EntityRendererProvider.Context ->
            AEntityRenderer(context)
        }
    }
}