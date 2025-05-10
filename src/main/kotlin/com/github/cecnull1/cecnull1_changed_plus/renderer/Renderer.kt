package com.github.cecnull1.cecnull1_changed_plus.renderer

import com.github.cecnull1.cecnull1_changed_plus.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus.entity.AEntity
import com.github.cecnull1.cecnull1_changed_plus.entity.Zombie
import com.github.cecnull1.cecnull1_changed_plus.modules.AEntityModel
import com.github.cecnull1.cecnull1_changed_plus.modules.AEntityModel.Companion.LAYER_LOCATION
import com.github.cecnull1.cecnull1_changed_plus.modules.ZombieModel
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer
import net.ltxprogrammer.changed.client.renderer.layers.TransfurCapeLayer
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorHumanModel
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleWolfModel
import net.ltxprogrammer.changed.util.Color3
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.resources.ResourceLocation
import java.util.function.Predicate

class AEntityRenderer(context: EntityRendererProvider.Context) :
    AdvancedHumanoidRenderer<AEntity, AEntityModel, ArmorLatexMaleWolfModel<AEntity>>(
        context, AEntityModel(
            context.bakeLayer(
                LAYER_LOCATION
            )
        ), ArmorLatexMaleWolfModel.MODEL_SET, 0.5f
    ) {
    init {
        this.addLayer(
            LatexParticlesLayer(
                this, getModel(),
                Predicate { part: ModelPart? -> model.isPartNotMask(part!!) })
        )
        this.addLayer(TransfurCapeLayer.normalCape(this, context.modelSet))
        this.addLayer(
            CustomEyesLayer.builder(this, context.modelSet)
                .withSclera(Color3.fromInt(0x242424))
                .withIris(
                    CustomEyesLayer.fixedIfNotDarkLatexOverrideLeft(Color3.WHITE),
                    CustomEyesLayer.fixedIfNotDarkLatexOverrideRight(Color3.WHITE)
                )
                .build()
        )
    }

    override fun getTextureLocation(entity: AEntity): ResourceLocation {
        return TEXTURE
    }

    companion object {
        private val TEXTURE = ResourceLocation(MODID, "textures/entities/a_entity.png")
    }
}

class ZombieRenderer(context: EntityRendererProvider.Context) :
    AdvancedHumanoidRenderer<Zombie, ZombieModel, ArmorHumanModel<Zombie>>(
        context, ZombieModel(context.bakeLayer(LAYER_LOCATION)), ArmorHumanModel.MODEL_SET, 0.5f
    ) {
    override fun getTextureLocation(entity: Zombie): ResourceLocation {
        return TEXTURE
    }

    companion object {
        private val TEXTURE = ResourceLocation(MODID, "textures/entities/zombie.png")
    }
    }