package com.github.cecnull1.cecnull1_changed_plus.renderer;

import com.github.cecnull1.cecnull1_changed_plus.Cecnull1_changed_plusKt;
import com.github.cecnull1.cecnull1_changed_plus.entity.AEntity;
import com.github.cecnull1.cecnull1_changed_plus.modules.AEntityModel;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.TransfurCapeLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleWolfModel;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class AEntityRenderer extends AdvancedHumanoidRenderer<AEntity, AEntityModel, ArmorLatexMaleWolfModel<AEntity>> {
    @SuppressWarnings("removal")
    private static final ResourceLocation TEXTURE = new ResourceLocation(Cecnull1_changed_plusKt.MODID, "textures/entities/a_entity.png");

    public AEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new AEntityModel(context.bakeLayer(AEntityModel.Companion.getLAYER_LOCATION())), ArmorLatexMaleWolfModel.MODEL_SET, 0.5f);
        this.addLayer(new LatexParticlesLayer<>(this, getModel(), model::isPartNotMask));
        this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
        this.addLayer(CustomEyesLayer.builder(this, context.getModelSet())
                .withSclera(Color3.fromInt(0x242424))
                .withIris(CustomEyesLayer.fixedIfNotDarkLatexOverrideLeft(Color3.WHITE),
                        CustomEyesLayer.fixedIfNotDarkLatexOverrideRight(Color3.WHITE))
                .build());
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull AEntity entity) {
        return TEXTURE;
    }
}