package com.github.cecnull1.cecnull1_changed_plus_v2.renderer

import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.AEntity
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.Soul
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.Zombie
import com.github.cecnull1.cecnull1_changed_plus_v2.model.AEntityModel
import com.github.cecnull1.cecnull1_changed_plus_v2.model.AEntityModel.Companion.LAYER_LOCATION
import com.github.cecnull1.cecnull1_changed_plus_v2.model.SoulModel
import com.github.cecnull1.cecnull1_changed_plus_v2.model.ZombieModel
import com.mojang.blaze3d.vertex.PoseStack
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer
import net.ltxprogrammer.changed.client.renderer.layers.TransfurCapeLayer
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorHumanModel
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleWolfModel
import net.ltxprogrammer.changed.util.Color3
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
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
                Predicate { part: ModelPart? -> part != null && model.isPartNotMask(part) })
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

class SoulRenderer(context:  EntityRendererProvider.Context): AdvancedHumanoidRenderer<Soul, SoulModel, ArmorHumanModel<Soul>>(
    context, SoulModel(context.bakeLayer(LAYER_LOCATION)), ArmorHumanModel.MODEL_SET, 0.5f
) {
    override fun getTextureLocation(entity: Soul): ResourceLocation {
        return TEXTURE
    }

    companion object {
        private val TEXTURE = ResourceLocation(MODID, "textures/entities/soul.png")
    }
}

class NoneTransfurVariantRenderer<T: Entity>(context: EntityRendererProvider.Context)
    : EntityRenderer<T>(context) {

    // 不渲染任何内容
    override fun render(
        entity: T,
        yaw: Float,
        partial: Float,
        stack: PoseStack,
        buffers: MultiBufferSource,
        light: Int
    ) {
        // 完全空实现
    }

    // 返回null避免默认名称渲染
    override fun getTextureLocation(entity: T): ResourceLocation {
        return ResourceLocation("")
    }
}