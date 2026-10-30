package com.github.cecnull1.cecnull1_changed_plus_v2.model

import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus_v2.renderer.newrl
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.ltxprogrammer.changed.Changed
import net.ltxprogrammer.changed.client.renderer.animate.AnimatorPresets
import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorHumanModel
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorModelSet
import net.ltxprogrammer.changed.client.renderer.model.armor.LatexHumanoidArmorModel
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
import net.minecraft.world.entity.EquipmentSlot.*
import net.minecraft.world.entity.HumanoidArm
import net.minecraft.world.item.ItemStack
import java.util.function.BiFunction
import java.util.function.Function

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
        entity: T,
        limbSwing: Float,
        limbSwingAmount: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float
    ) {
        this.animator.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch)
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

    class ArmorModel<AT : ChangedEntity>(
        root: ModelPart,
        armorModel: net.ltxprogrammer.changed.client.renderer.model.armor.ArmorModel?
    ) : LatexHumanoidArmorModel<AT, ArmorModel<AT>>(root, armorModel) {
        private val Head: ModelPart = root.getChild("head")
        private val Hat: ModelPart = root.getChild("hat")
        private val Torso: ModelPart = root.getChild("body")
        private val LeftLeg: ModelPart = root.getChild("left_leg")
        private val RightLeg: ModelPart = root.getChild("right_leg")
        private val LeftArm: ModelPart = root.getChild("left_arm")
        private val RightArm: ModelPart = root.getChild("right_arm")
        private val animator: HumanoidAnimator<AT, ArmorModel<AT>> = HumanoidAnimator.of(this).hipOffset(-1.5f).legLength(10.5f)
            .addPreset(
                AnimatorPresets.humanLike(
                    Head,
                    Torso,
                    LeftArm,
                    RightArm,
                    LeftLeg,
                    RightLeg
                )
            )

        override fun getAnimator(p0: AT): HumanoidAnimator<AT, *> = animator

        override fun setupAnim(
            entity: AT,
            limbSwing: Float,
            limbSwingAmount: Float,
            ageInTicks: Float,
            netHeadYaw: Float,
            headPitch: Float
        ) {
            super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch)
            Hat.copyFrom(Head)
        }

        override fun getArm(arm: HumanoidArm): ModelPart {
            return if (arm == HumanoidArm.LEFT) this.LeftArm else this.RightArm
        }

        override fun getLeg(leg: HumanoidArm?): ModelPart? {
            return if (leg == HumanoidArm.LEFT) this.LeftLeg else this.RightLeg
        }

        override fun getHead(): ModelPart {
            return this.Head
        }

        override fun getTorso(): ModelPart {
            return Torso
        }

        override fun renderForSlot(
            entity: AT,
            parent: RenderLayerParent<in AT, *>,
            stack: ItemStack,
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
                HEAD -> {
                    Head.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha)
                    Hat.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha)
                }

                CHEST -> {
                    Torso.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha)
                    LeftArm.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha)
                    RightArm.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha)
                }

                LEGS -> {
                    Torso.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha)
                    LeftLeg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha)
                    RightLeg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha)
                }

                FEET -> {
                    LeftLeg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha)
                    RightLeg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha)
                }

                else -> {}
            }

            poseStack.popPose()
        }

        companion object {
            val MODEL_SET: ArmorModelSet<ChangedEntity, ArmorHumanModel<ChangedEntity>> =
                ArmorModelSet.of(
                    newrl(MODID, "armor_player"),
                    { layer: net.ltxprogrammer.changed.client.renderer.model.armor.ArmorModel ->
                        ArmorHumanModel.createArmorLayer(
                            layer,
                            false
                        )
                    },
                    { modelPart: ModelPart?, model: net.ltxprogrammer.changed.client.renderer.model.armor.ArmorModel ->
                        ArmorHumanModel(
                            modelPart,
                            model
                        )
                    },
                    false
                )
            val MODEL_SET_SLIM: ArmorModelSet<ChangedEntity, ArmorHumanModel<ChangedEntity>> =
                ArmorModelSet.of(
                    newrl(MODID, "armor_player_slim"),
                    { layer: net.ltxprogrammer.changed.client.renderer.model.armor.ArmorModel ->
                        ArmorHumanModel.createArmorLayer(
                            layer,
                            true
                        )
                    },
                    { modelPart: ModelPart?, model: net.ltxprogrammer.changed.client.renderer.model.armor.ArmorModel ->
                        ArmorHumanModel(
                            modelPart,
                            model
                        )
                    },
                    true
                )
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