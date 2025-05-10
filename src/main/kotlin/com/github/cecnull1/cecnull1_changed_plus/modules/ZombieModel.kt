package com.github.cecnull1.cecnull1_changed_plus.modules

import com.github.cecnull1.cecnull1_changed_plus.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus.entity.ModEntities.ZOMBIE_ID
import com.github.cecnull1.cecnull1_changed_plus.entity.Zombie
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.ltxprogrammer.changed.client.renderer.animate.AnimatorPresets
import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModelInterface
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeDeformation
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.HumanoidArm

// Made with Blockbench 4.12.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

class ZombieModel(root: ModelPart) : AdvancedHumanoidModel<Zombie>(root), AdvancedHumanoidModelInterface<Zombie, ZombieModel> {
    private val Torso: ModelPart = root.getChild("Torso")
    private val Head: ModelPart = root.getChild("Head")
    private val RightArm: ModelPart = root.getChild("RightArm")
    private val LeftArm: ModelPart = root.getChild("LeftArm")
    private val RightLeg: ModelPart = root.getChild("RightLeg")
    private val LeftLeg: ModelPart = root.getChild("LeftLeg")
    private val animation: HumanoidAnimator<Zombie, ZombieModel> =
        HumanoidAnimator(this).hipOffset(-1.5f).addPreset(
            AnimatorPresets.humanLike(Head, Torso, LeftArm, RightArm, LeftLeg, RightLeg)
        )

    override fun m_102851_(humanoidArm: HumanoidArm?): ModelPart? {
        return if (humanoidArm == HumanoidArm.LEFT) this.LeftArm else this.RightArm
    }

    override fun getLeg(humanoidArm: HumanoidArm?): ModelPart? {
        return if (humanoidArm == HumanoidArm.LEFT) this.LeftLeg else this.RightLeg
    }

    override fun setupAnim(
        entity: Zombie,
        limbSwing: Float,
        limbSwingAmount: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float
    ) {
        animation.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch)
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch)
    }

    override fun renderToBuffer(
        poseStack: PoseStack,
        vertexConsumer: VertexConsumer,
        packedLight: Int,
        packedOverlay: Int,
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float
    ) {
        Torso.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha)
        Head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha)
        RightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha)
        LeftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha)
        RightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha)
        LeftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha)
    }

    override fun getTorso(): ModelPart {
        return Torso
    }

    override fun setupHand(t: Zombie) {
        animation.setupHand()
    }

    override fun getAnimator(entity: Zombie): HumanoidAnimator<Zombie, ZombieModel> {
        return animation
    }

    override fun prepareMobModel(p_102861_: Zombie, p_102862_: Float, p_102863_: Float, p_102864_: Float) {
        this.prepareMobModel(animation, p_102861_, p_102862_, p_102863_, p_102864_)
    }

    companion object {
        // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
        val LAYER_LOCATION: ModelLayerLocation = ModelLayerLocation(ResourceLocation(MODID, ZOMBIE_ID + "_model"), "main")
        fun createBodyLayer(): LayerDefinition {
            val meshdefinition = MeshDefinition()
            val partdefinition = meshdefinition.root

            val Torso = partdefinition.addOrReplaceChild(
                "Torso",
                CubeListBuilder.create().texOffs(16, 16)
                    .addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val Head = partdefinition.addOrReplaceChild(
                "Head",
                CubeListBuilder.create().texOffs(0, 0)
                    .addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, CubeDeformation(0.0f))
                    .texOffs(32, 0).addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, CubeDeformation(0.5f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val RightArm = partdefinition.addOrReplaceChild(
                "RightArm",
                CubeListBuilder.create().texOffs(40, 16)
                    .addBox(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, CubeDeformation(0.0f)),
                PartPose.offset(-5.0f, 2.0f, 0.0f)
            )

            val LeftArm = partdefinition.addOrReplaceChild(
                "LeftArm",
                CubeListBuilder.create().texOffs(40, 16).mirror()
                    .addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, CubeDeformation(0.0f)).mirror(false),
                PartPose.offset(5.0f, 2.0f, 0.0f)
            )

            val RightLeg = partdefinition.addOrReplaceChild(
                "RightLeg",
                CubeListBuilder.create().texOffs(0, 16)
                    .addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, CubeDeformation(0.0f)),
                PartPose.offset(-1.9f, 12.0f, 0.0f)
            )

            val LeftLeg = partdefinition.addOrReplaceChild(
                "LeftLeg",
                CubeListBuilder.create().texOffs(0, 16).mirror()
                    .addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, CubeDeformation(0.0f)).mirror(false),
                PartPose.offset(1.9f, 12.0f, 0.0f)
            )

            return LayerDefinition.create(meshdefinition, 64, 32)
        }
    }
}