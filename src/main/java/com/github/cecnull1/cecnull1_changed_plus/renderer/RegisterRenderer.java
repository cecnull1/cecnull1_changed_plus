package com.github.cecnull1.cecnull1_changed_plus.renderer;

import com.github.cecnull1.cecnull1_changed_plus.Cecnull1_changed_plusKt;
import com.github.cecnull1.cecnull1_changed_plus.entity.ModEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Cecnull1_changed_plusKt.MODID, value = Dist.CLIENT)
public class RegisterRenderer {
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.INSTANCE.getA_ENTITY().get(), AEntityRenderer::new);
    }
}
