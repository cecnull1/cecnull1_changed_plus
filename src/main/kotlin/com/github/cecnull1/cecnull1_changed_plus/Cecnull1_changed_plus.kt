package com.github.cecnull1.cecnull1_changed_plus

import com.github.cecnull1.cecnull1_changed_plus.block.ModBlocks
import com.github.cecnull1.cecnull1_changed_plus.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus.entity.ModEntities
import com.github.cecnull1.cecnull1_changed_plus.entity.ModEntities.A_ENTITY
import com.github.cecnull1.cecnull1_changed_plus.entity.ModEntities.CEXOSKELETON
import com.github.cecnull1.cecnull1_changed_plus.entity.ModEntities.ZOMBIE
import com.github.cecnull1.cecnull1_changed_plus.entity.ModTransfurVariant
import com.github.cecnull1.cecnull1_changed_plus.entity.modEventBus
import com.github.cecnull1.cecnull1_changed_plus.item.ModItems
import com.github.cecnull1.cecnull1_changed_plus.modules.AEntityModel
import com.github.cecnull1.cecnull1_changed_plus.modules.ZombieModel
import com.github.cecnull1.cecnull1_changed_plus.renderer.ZombieRenderer
import net.ltxprogrammer.changed.client.renderer.DarkLatexYufengRenderer
import net.ltxprogrammer.changed.client.renderer.ExoskeletonRenderer
import net.ltxprogrammer.changed.entity.ChangedEntity
import net.ltxprogrammer.changed.entity.robot.Exoskeleton
import net.ltxprogrammer.changed.init.ChangedAttributes
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers
import net.minecraftforge.common.ForgeMod
import net.minecraftforge.event.entity.EntityAttributeCreationEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventBusSubscriber


@Mod(MODID)
class Cecnull1_changed_plus {
    init {
        ModEntities.REGISTER.register(modEventBus)
        ModTransfurVariant.REGISTRY.register(modEventBus)
        ModBlocks.REGISTER.register(modEventBus)
        ModItems.REGISTER.register(modEventBus)
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
                .add(ForgeMod.SWIM_SPEED.get(), 2.0)
                .add(Attributes.ATTACK_DAMAGE, 20.0)
                .add(ChangedAttributes.TRANSFUR_DAMAGE.get(), 20.0)
                .build()
        )
        event.put(
            CEXOSKELETON.get(),
            Exoskeleton.createAttributes().build()
        )
        event.put(
            ZOMBIE.get(), ChangedEntity.createLatexAttributes()
                .add(Attributes.MAX_HEALTH, 24.0)
                .add(Attributes.MOVEMENT_SPEED, 0.1)
                .add(ForgeMod.SWIM_SPEED.get(), 2.0)
                .add(Attributes.ATTACK_DAMAGE, 10.0)
                .add(ChangedAttributes.TRANSFUR_DAMAGE.get(), 10.0)
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
        event.registerLayerDefinition(ZombieModel.LAYER_LOCATION, ZombieModel::createBodyLayer)
    }

    @JvmStatic
    @SubscribeEvent
    fun registerEntityRenderers(event: RegisterRenderers) {
        event.registerEntityRenderer(
            A_ENTITY.get()
        ) { context: EntityRendererProvider.Context ->
            DarkLatexYufengRenderer(context)
        }
        event.registerEntityRenderer(
            CEXOSKELETON.get()
        ) { context: EntityRendererProvider.Context ->
            ExoskeletonRenderer(context)
        }
    }
}