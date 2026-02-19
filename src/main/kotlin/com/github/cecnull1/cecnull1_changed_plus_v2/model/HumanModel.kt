package com.github.cecnull1.cecnull1_changed_plus_v2.model

import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus_v2.renderer.newrl
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.ltxprogrammer.changed.client.animations.Limb
import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorHumanModel
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorModelSet
import net.ltxprogrammer.changed.client.renderer.model.armor.LatexHumanoidArmorModel
import net.ltxprogrammer.changed.client.tfanimations.HelperModel
import net.ltxprogrammer.changed.client.tfanimations.TransfurHelper
import net.ltxprogrammer.changed.entity.ChangedEntity
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeDeformation
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.HumanoidArm
import net.minecraft.world.item.ItemStack

// Made with Blockbench 5.0.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


class UserHumanModel<T : ChangedEntity>(root: ModelPart) : AdvancedHumanoidModel<T>(root) {
    private val RightLeg: ModelPart = root.getChild("RightLeg")
    private val LeftLeg: ModelPart = root.getChild("LeftLeg")
    private val Head: ModelPart = root.getChild("Head")
    private val Torso: ModelPart = root.getChild("Torso")
    private val RightArm: ModelPart = root.getChild("RightArm")
    private val LeftArm: ModelPart = root.getChild("LeftArm")
    private val animator: HumanoidAnimator<T, UserHumanModel<T>> = HumanoidAnimator.of(this).hipOffset(-1.5f).legLength(10.5f)
        .addPreset(
            AnimatorUtils.func.humanLikeC(
                LeftLeg,
                RightLeg,
                LeftArm,
                RightArm,
                Head,
                Torso
            )
        )

    override fun getArm(arm: HumanoidArm): ModelPart {
        return if (arm == HumanoidArm.LEFT) LeftArm else RightArm
    }

    override fun getLeg(leg: HumanoidArm?): ModelPart? {
        return if (leg == HumanoidArm.LEFT) LeftLeg else RightLeg
    }

    override fun setupAnim(
        entity: T?,
        limbSwing: Float,
        limbSwingAmount: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float
    ) {
        this.animator.setupAnim(entity!!, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch)
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

    override fun setupHand(entity: T?) {
        this.animator.setupHand()
    }

    override fun getHead(): ModelPart {
        return Head
    }

    override fun getAnimator(entity: T): HumanoidAnimator<T, UserHumanModel<T>> {
        return animator
    }

    override fun prepareMobModel(entity: T?, limbSwing: Float, limbSwingAmount: Float, partialTicks: Float) {
        super.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks)
    }

    class ArmorModel<AT : ChangedEntity>(
        root: ModelPart,
        armorModel: net.ltxprogrammer.changed.client.renderer.model.armor.ArmorModel?
    ) : LatexHumanoidArmorModel<AT, ArmorModel<AT>>(root, armorModel) {
        private val RightLeg: ModelPart = root.getChild("right_leg")
        private val LeftLeg: ModelPart = root.getChild("left_leg")
        private val Head: ModelPart = root.getChild("head")
        private val Torso: ModelPart = root.getChild("body")
        private val RightArm: ModelPart = root.getChild("right_arm")
        private val LeftArm: ModelPart = root.getChild("left_arm")
        private val animator: HumanoidAnimator<AT, ArmorModel<AT>> =
            HumanoidAnimator.of<AT, ArmorModel<AT>>(this).hipOffset(-1.5f).legLength(10.5f)
                .addPreset(
                    AnimatorUtils.func.humanLikeC<AT, ArmorModel<AT>>(
                        LeftLeg,
                        RightLeg,
                        LeftArm,
                        RightArm,
                        Head,
                        Torso
                    )
                )

        override fun renderForSlot(
            entity: AT?,
            parent: RenderLayerParent<in AT?, *>?,
            stack: ItemStack?,
            slot: EquipmentSlot,
            poseStack: PoseStack,
            buffer: VertexConsumer,
            packedLight: Int,
            packedOverlay: Int,
            red: Float,
            green: Float,
            blue: Float,
            alpha: Float
        ) {
            poseStack.pushPose()
            this.scaleForSlot(parent, slot, poseStack)

            when (slot) {
                EquipmentSlot.HEAD -> Head.render(
                    poseStack,
                    buffer,
                    packedLight,
                    packedOverlay,
                    red,
                    green,
                    blue,
                    alpha
                )

                EquipmentSlot.CHEST -> {
                    Torso.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha)
                    LeftArm.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha)
                    RightArm.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha)
                }

                EquipmentSlot.LEGS -> {
                    Torso.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha)
                    LeftLeg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha)
                    RightLeg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha)
                }

                EquipmentSlot.FEET -> {
                    LeftLeg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha)
                    RightLeg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha)
                }

                else -> {}
            }

            poseStack.popPose()
        }

        override fun getTransfurHelperModel(limb: Limb?): HelperModel? {
            if (limb == Limb.TORSO) return TransfurHelper.getFeminineTorso(this.armorModel)
            return super.getTransfurHelperModel(limb)
        }

        override fun getArm(arm: HumanoidArm): ModelPart {
            return if (arm == HumanoidArm.LEFT) LeftArm else RightArm
        }

        override fun getLeg(leg: HumanoidArm?): ModelPart? {
            return if (leg == HumanoidArm.LEFT) LeftLeg else RightLeg
        }

        override fun getHead(): ModelPart {
            return Head
        }

        override fun getTorso(): ModelPart {
            return Torso
        }

        override fun getAnimator(entity: AT): HumanoidAnimator<AT, ArmorModel<AT>> {
            return animator
        }

        override fun setupAnim(
            entity: AT?,
            limbSwing: Float,
            limbSwingAmount: Float,
            ageInTicks: Float,
            netHeadYaw: Float,
            headPitch: Float
        ) {
            this.animator.setupAnim(entity!!, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch)
            super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch)
        }

        companion object {
            val MODEL_SET: ArmorModelSet<ChangedEntity, ArmorModel<ChangedEntity>> =
                ArmorModelSet.of(
                    newrl(MODID, "user_entity_model"),
                    { layer: net.ltxprogrammer.changed.client.renderer.model.armor.ArmorModel? ->
                        ArmorHumanModel.createArmorLayer(
                            layer
                        )
                    },
                    { root: ModelPart?, armorModel: net.ltxprogrammer.changed.client.renderer.model.armor.ArmorModel? ->
                        ArmorModel(
                            root!!,
                            armorModel
                        )
                    })
        }
    }

    companion object Fun {
        inline val func get() = this

        // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
        val LAYER_LOCATION_STEVE: ModelLayerLocation =
            ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(MODID, "steve_model"), "main")
        val LAYER_LOCATION_ALEX: ModelLayerLocation =
            ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(MODID, "alex_model"), "main")

        fun createAlexLayer(): LayerDefinition {
            val meshdefinition = MeshDefinition()
            val partdefinition = meshdefinition.root

            val RightLeg = partdefinition.addOrReplaceChild(
                "RightLeg",
                CubeListBuilder.create().texOffs(0, 16)
                    .addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, CubeDeformation(0.0f))
                    .texOffs(0, 32).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, CubeDeformation(0.25f)),
                PartPose.offset(-1.9f, 12.0f, 0.0f)
            )

            val LeftLeg = partdefinition.addOrReplaceChild(
                "LeftLeg",
                CubeListBuilder.create().texOffs(16, 48)
                    .addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, CubeDeformation(0.0f))
                    .texOffs(0, 48).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, CubeDeformation(0.25f)),
                PartPose.offset(1.9f, 12.0f, 0.0f)
            )

            val Head = partdefinition.addOrReplaceChild(
                "Head",
                CubeListBuilder.create().texOffs(0, 0)
                    .addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, CubeDeformation(0.0f))
                    .texOffs(32, 0).addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, CubeDeformation(0.5f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val Torso = partdefinition.addOrReplaceChild(
                "Torso",
                CubeListBuilder.create().texOffs(16, 16)
                    .addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, CubeDeformation(0.0f))
                    .texOffs(16, 32).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, CubeDeformation(0.25f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val RightArm = partdefinition.addOrReplaceChild(
                "RightArm",
                CubeListBuilder.create().texOffs(40, 16)
                    .addBox(-2.0f, -2.0f, -2.0f, 3.0f, 12.0f, 4.0f, CubeDeformation(0.0f))
                    .texOffs(40, 32).addBox(-2.0f, -2.0f, -2.0f, 3.0f, 12.0f, 4.0f, CubeDeformation(0.25f)),
                PartPose.offset(-5.0f, 2.0f, 0.0f)
            )

            val LeftArm = partdefinition.addOrReplaceChild(
                "LeftArm",
                CubeListBuilder.create().texOffs(32, 48)
                    .addBox(-1.0f, -2.0f, -2.0f, 3.0f, 12.0f, 4.0f, CubeDeformation(0.0f))
                    .texOffs(48, 48).addBox(-1.0f, -2.0f, -2.0f, 3.0f, 12.0f, 4.0f, CubeDeformation(0.25f)),
                PartPose.offset(5.0f, 2.0f, 0.0f)
            )

            return LayerDefinition.create(meshdefinition, 64, 64)
        }

        fun createSteveLayer(): LayerDefinition {
            val meshdefinition = MeshDefinition()
            val partdefinition = meshdefinition.root

            val RightLeg = partdefinition.addOrReplaceChild(
                "RightLeg",
                CubeListBuilder.create().texOffs(0, 16)
                    .addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, CubeDeformation(0.0f))
                    .texOffs(0, 32).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, CubeDeformation(0.25f)),
                PartPose.offset(-1.9f, 12.0f, 0.0f)
            )

            val LeftLeg = partdefinition.addOrReplaceChild(
                "LeftLeg",
                CubeListBuilder.create().texOffs(16, 48)
                    .addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, CubeDeformation(0.0f))
                    .texOffs(0, 48).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, CubeDeformation(0.25f)),
                PartPose.offset(1.9f, 12.0f, 0.0f)
            )

            val Head = partdefinition.addOrReplaceChild(
                "Head",
                CubeListBuilder.create().texOffs(0, 0)
                    .addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, CubeDeformation(0.0f))
                    .texOffs(32, 0).addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, CubeDeformation(0.5f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val Torso = partdefinition.addOrReplaceChild(
                "Torso",
                CubeListBuilder.create().texOffs(16, 16)
                    .addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, CubeDeformation(0.0f))
                    .texOffs(16, 32).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, CubeDeformation(0.25f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val RightArm = partdefinition.addOrReplaceChild(
                "RightArm",
                CubeListBuilder.create().texOffs(40, 16)
                    .addBox(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, CubeDeformation(0.0f))
                    .texOffs(40, 32).addBox(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, CubeDeformation(0.25f)),
                PartPose.offset(-5.0f, 2.0f, 0.0f)
            )

            val LeftArm = partdefinition.addOrReplaceChild(
                "LeftArm",
                CubeListBuilder.create().texOffs(32, 48)
                    .addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, CubeDeformation(0.0f))
                    .texOffs(48, 48).addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, CubeDeformation(0.25f)),
                PartPose.offset(5.0f, 2.0f, 0.0f)
            )

            return LayerDefinition.create(meshdefinition, 64, 64)
        }
    }
}