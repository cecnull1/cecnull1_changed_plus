package com.github.cecnull1.cecnull1_changed_plus.modules

import com.github.cecnull1.cecnull1_changed_plus.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus.entity.Soul
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.ltxprogrammer.changed.client.renderer.animate.AnimatorPresets
import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModelInterface
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.HumanoidArm

// Made with Blockbench 4.12.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


class SoulModel(root: ModelPart) : AdvancedHumanoidModel<Soul>(root), AdvancedHumanoidModelInterface<Soul, SoulModel> {
    private val RightLeg: ModelPart = root.getChild("RightLeg")
    private val LeftLeg: ModelPart = root.getChild("LeftLeg")
    private val Head: ModelPart = root.getChild("Head")
    private val Torso: ModelPart = root.getChild("Torso")
    private val RightArm: ModelPart = root.getChild("RightArm")
    private val LeftArm: ModelPart = root.getChild("LeftArm")
    private val animator: HumanoidAnimator<Soul, SoulModel> =
        HumanoidAnimator.of<Soul?, SoulModel?>(this).hipOffset(-1.5f).addPreset(
            AnimatorPresets.humanLike<Soul?, SoulModel?>(
                Head,
                Torso,
                LeftArm,
                RightArm,
                LeftLeg,
                RightLeg
            )
        )

    override fun setupAnim(
        entity: Soul,
        limbSwing: Float,
        limbSwingAmount: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float
    ) {
        animator.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch)
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch)
    }

    override fun m_102851_(humanoidArm: HumanoidArm): ModelPart {
        return if (humanoidArm == HumanoidArm.RIGHT) RightArm else LeftArm
    }

    override fun getLeg(humanoidArm: HumanoidArm): ModelPart {
        return if (humanoidArm == HumanoidArm.RIGHT) RightLeg else LeftLeg
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
        RightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha)
        LeftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha)
        Head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha)
        Torso.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha)
        RightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha)
        LeftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha)
    }

    override fun getTorso(): ModelPart {
        return Torso
    }

    companion object {
        // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
        val LAYER_LOCATION: ModelLayerLocation =
            ModelLayerLocation(ResourceLocation(MODID, "soul"), "main")

        fun createBodyLayer(): LayerDefinition {
            val meshdefinition = MeshDefinition()
            val partdefinition = meshdefinition.root

            val RightLeg = partdefinition.addOrReplaceChild(
                "RightLeg",
                CubeListBuilder.create(),
                PartPose.offset(-2.5f, 10.5f, 0.0f)
            )

            val LeftLeg = partdefinition.addOrReplaceChild(
                "LeftLeg",
                CubeListBuilder.create(),
                PartPose.offset(2.5f, 10.5f, 0.0f)
            )

            val Head =
                partdefinition.addOrReplaceChild("Head", CubeListBuilder.create(), PartPose.offset(0.0f, -0.5f, 0.0f))

            val Torso =
                partdefinition.addOrReplaceChild("Torso", CubeListBuilder.create(), PartPose.offset(0.0f, -0.5f, 0.0f))

            val RightArm = partdefinition.addOrReplaceChild(
                "RightArm",
                CubeListBuilder.create(),
                PartPose.offset(-5.0f, 1.5f, 0.0f)
            )

            val LeftArm =
                partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create(), PartPose.offset(5.0f, 1.5f, 0.0f))

            return LayerDefinition.create(meshdefinition, 96, 96)
        }
    }

    override fun getAnimator(p0: Soul?): HumanoidAnimator<Soul, SoulModel> = animator

    override fun setupHand(p0: Soul) {
        animator.setupHand()
    }
}