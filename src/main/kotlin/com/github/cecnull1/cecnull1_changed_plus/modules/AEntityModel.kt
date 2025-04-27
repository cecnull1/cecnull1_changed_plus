package com.github.cecnull1.cecnull1_changed_plus.modules

import com.github.cecnull1.cecnull1_changed_plus.MODID
import com.github.cecnull1.cecnull1_changed_plus.entity.AEntity
import com.github.cecnull1.cecnull1_changed_plus.entity.ModEntities.A_ENTITY_ID
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.ltxprogrammer.changed.client.renderer.animate.AnimatorPresets
import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModelInterface
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.*
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.HumanoidArm
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

// Made with Blockbench 4.1.5
// Exported for Minecraft version 1.17 with Mojang mappings
// Paste this class into your mod and generate all required imports

@OnlyIn(Dist.CLIENT)
class AEntityModel(root: ModelPart) : AdvancedHumanoidModel<AEntity>(root),
    AdvancedHumanoidModelInterface<AEntity, AEntityModel> {
    private val RightLeg: ModelPart = root.getChild("RightLeg")
    private val LeftLeg: ModelPart = root.getChild("LeftLeg")
    private val RightArm: ModelPart = root.getChild("RightArm")
    private val LeftArm: ModelPart = root.getChild("LeftArm")

    private val Head: ModelPart = root.getChild("Head")
    private val Torso: ModelPart = root.getChild("Torso")
    private val Tail: ModelPart = Torso.getChild("Tail")

    private val Mask: ModelPart = Head.getChild("Mask")
    private val RightWing: ModelPart = Torso.getChild("RightWing")
    private val LeftWing: ModelPart = Torso.getChild("LeftWing")
    private val animator: HumanoidAnimator<AEntity, AEntityModel>

    init {
        val tailPrimary = Tail.getChild("TailPrimary")
        val tailSecondary = tailPrimary.getChild("TailSecondary")
        val tailTertiary = tailSecondary.getChild("TailTertiary")

        val leftLowerLeg = LeftLeg.getChild("LeftLowerLeg")
        val leftFoot = leftLowerLeg.getChild("LeftFoot")
        val rightLowerLeg = RightLeg.getChild("RightLowerLeg")
        val rightFoot = rightLowerLeg.getChild("RightFoot")

        val leftWingRoot = LeftWing.getChild("leftWingRoot")
        val rightWingRoot = RightWing.getChild("rightWingRoot")

        animator = HumanoidAnimator.of(this).hipOffset(-1.5f)
            .addPreset(
                AnimatorPresets.wingedDragonLike(
                    Head,
                    Torso,
                    LeftArm,
                    RightArm,
                    Tail,
                    listOf(tailPrimary, tailSecondary, tailTertiary),
                    LeftLeg,
                    leftLowerLeg,
                    leftFoot,
                    leftFoot.getChild("LeftPad"),
                    RightLeg,
                    rightLowerLeg,
                    rightFoot,
                    rightFoot.getChild("RightPad"),

                    leftWingRoot,
                    leftWingRoot.getChild("leftSecondaries"),
                    leftWingRoot.getChild("leftSecondaries").getChild("leftTertiaries"),
                    rightWingRoot,
                    rightWingRoot.getChild("rightSecondaries"),
                    rightWingRoot.getChild("rightSecondaries").getChild("rightTertiaries")
                )
            )
    }

    fun isPartNotMask(part: ModelPart): Boolean {
        return Mask.allParts.noneMatch { obj: ModelPart? -> part.equals(obj) }
    }

    override fun prepareMobModel(p_102861_: AEntity, p_102862_: Float, p_102863_: Float, p_102864_: Float) {
        this.prepareMobModel(animator, p_102861_, p_102862_, p_102863_, p_102864_)
    }

    override fun setupHand(entity: AEntity) {
        animator.setupHand()
    }

    override fun setupAnim(
        entity: AEntity,
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
        return if (humanoidArm == HumanoidArm.LEFT) this.LeftArm else this.RightArm
    }

    override fun getLeg(p_102852_: HumanoidArm): ModelPart {
        return if (p_102852_ == HumanoidArm.LEFT) this.LeftLeg else this.RightLeg
    }

    override fun getHead(): ModelPart {
        return this.Head
    }

    override fun getTorso(): ModelPart {
        return Torso
    }

    override fun renderToBuffer(
        poseStack: PoseStack,
        buffer: VertexConsumer,
        packedLight: Int,
        packedOverlay: Int,
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float
    ) {
        RightLeg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha)
        LeftLeg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha)
        Head.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha)
        Torso.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha)
        RightArm.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha)
        LeftArm.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha)
    }

    override fun getAnimator(entity: AEntity): HumanoidAnimator<AEntity, AEntityModel> {
        return animator
    }

    companion object {
        // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
        val LAYER_LOCATION: ModelLayerLocation =
            ModelLayerLocation(ResourceLocation(MODID, "form_$A_ENTITY_ID"), "main")

        fun createBodyLayer(): LayerDefinition {
            val meshdefinition = MeshDefinition()
            val partdefinition = meshdefinition.root

            val RightLeg = partdefinition.addOrReplaceChild(
                "RightLeg",
                CubeListBuilder.create(),
                PartPose.offset(-2.5f, 10.5f, 0.0f)
            )

            val RightThigh_r1 = RightLeg.addOrReplaceChild(
                "RightThigh_r1",
                CubeListBuilder.create().texOffs(32, 44)
                    .addBox(-2.0f, 0.0f, -2.0f, 4.0f, 7.0f, 4.0f, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, -0.2182f, 0.0f, 0.0f)
            )

            val RightLowerLeg = RightLeg.addOrReplaceChild(
                "RightLowerLeg",
                CubeListBuilder.create(),
                PartPose.offset(0.0f, 6.375f, -3.45f)
            )

            val RightCalf_r1 = RightLowerLeg.addOrReplaceChild(
                "RightCalf_r1",
                CubeListBuilder.create().texOffs(48, 47)
                    .addBox(-1.99f, -0.125f, -2.9f, 4.0f, 6.0f, 4.0f, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0f, -2.125f, 1.95f, 0.8727f, 0.0f, 0.0f)
            )

            val RightFoot = RightLowerLeg.addOrReplaceChild(
                "RightFoot",
                CubeListBuilder.create(),
                PartPose.offset(0.0f, 0.8f, 7.175f)
            )

            val RightArch_r1 = RightFoot.addOrReplaceChild(
                "RightArch_r1",
                CubeListBuilder.create().texOffs(0, 59)
                    .addBox(-2.0f, -8.45f, -0.725f, 4.0f, 6.0f, 3.0f, CubeDeformation(0.005f)),
                PartPose.offsetAndRotation(0.0f, 7.075f, -4.975f, -0.3491f, 0.0f, 0.0f)
            )

            val RightPad = RightFoot.addOrReplaceChild(
                "RightPad",
                CubeListBuilder.create().texOffs(48, 9)
                    .addBox(-2.0f, 0.0f, -2.5f, 4.0f, 2.0f, 5.0f, CubeDeformation.NONE),
                PartPose.offset(0.0f, 4.325f, -4.425f)
            )

            val LeftLeg = partdefinition.addOrReplaceChild(
                "LeftLeg",
                CubeListBuilder.create(),
                PartPose.offset(2.5f, 10.5f, 0.0f)
            )

            val LeftThigh_r1 = LeftLeg.addOrReplaceChild(
                "LeftThigh_r1",
                CubeListBuilder.create().texOffs(0, 48)
                    .addBox(-2.0f, 0.0f, -2.0f, 4.0f, 7.0f, 4.0f, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, -0.2182f, 0.0f, 0.0f)
            )

            val LeftLowerLeg = LeftLeg.addOrReplaceChild(
                "LeftLowerLeg",
                CubeListBuilder.create(),
                PartPose.offset(0.0f, 6.375f, -3.45f)
            )

            val LeftCalf_r1 = LeftLowerLeg.addOrReplaceChild(
                "LeftCalf_r1",
                CubeListBuilder.create().texOffs(48, 22)
                    .addBox(-2.01f, -0.125f, -2.9f, 4.0f, 6.0f, 4.0f, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0f, -2.125f, 1.95f, 0.8727f, 0.0f, 0.0f)
            )

            val LeftFoot = LeftLowerLeg.addOrReplaceChild(
                "LeftFoot",
                CubeListBuilder.create(),
                PartPose.offset(0.0f, 0.8f, 7.175f)
            )

            val LeftArch_r1 = LeftFoot.addOrReplaceChild(
                "LeftArch_r1",
                CubeListBuilder.create().texOffs(14, 61)
                    .addBox(-2.0f, -8.45f, -0.725f, 4.0f, 6.0f, 3.0f, CubeDeformation(0.005f)),
                PartPose.offsetAndRotation(0.0f, 7.075f, -4.975f, -0.3491f, 0.0f, 0.0f)
            )

            val LeftPad = LeftFoot.addOrReplaceChild(
                "LeftPad",
                CubeListBuilder.create().texOffs(24, 0)
                    .addBox(-2.0f, 0.0f, -2.5f, 4.0f, 2.0f, 5.0f, CubeDeformation.NONE),
                PartPose.offset(0.0f, 4.325f, -4.425f)
            )

            val Head = partdefinition.addOrReplaceChild(
                "Head",
                CubeListBuilder.create().texOffs(0, 16)
                    .addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, CubeDeformation.NONE)
                    .texOffs(64, 53).addBox(-1.5f, -1.0f, -6.0f, 3.0f, 1.0f, 2.0f, CubeDeformation.NONE),
                PartPose.offset(0.0f, -0.5f, 0.0f)
            )

            val Base_r1 = Head.addOrReplaceChild(
                "Base_r1",
                CubeListBuilder.create().texOffs(56, 65)
                    .addBox(-1.9f, 0.15f, -0.3f, 2.0f, 3.0f, 2.0f, CubeDeformation.NONE),
                PartPose.offsetAndRotation(5.5f, -5.0f, 0.0f, -1.2217f, 0.4712f, 0.6981f)
            )

            val Base_r2 = Head.addOrReplaceChild(
                "Base_r2",
                CubeListBuilder.create().texOffs(0, 16)
                    .addBox(-1.4f, 0.25f, 2.3f, 2.0f, 3.0f, 2.0f, CubeDeformation.NONE),
                PartPose.offsetAndRotation(5.5f, -5.0f, 0.0f, -1.4312f, 0.6458f, 0.5847f)
            )

            val Base_r3 = Head.addOrReplaceChild(
                "Base_r3",
                CubeListBuilder.create().texOffs(64, 65)
                    .addBox(-0.1f, 0.15f, -0.3f, 2.0f, 3.0f, 2.0f, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-5.5f, -5.0f, 0.0f, -1.2217f, -0.4712f, -0.6981f)
            )

            val Base_r4 = Head.addOrReplaceChild(
                "Base_r4",
                CubeListBuilder.create().texOffs(0, 0)
                    .addBox(-0.6f, 0.25f, 2.3f, 2.0f, 3.0f, 2.0f, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-5.5f, -5.0f, 0.0f, -1.4312f, -0.6458f, -0.5847f)
            )

            val Hair = Head.addOrReplaceChild(
                "Hair",
                CubeListBuilder.create().texOffs(0, 0)
                    .addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, CubeDeformation(0.2f))
                    .texOffs(24, 8).addBox(-4.0f, -8.0f, -4.0f, 8.0f, 6.0f, 8.0f, CubeDeformation(0.3f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val TopHorn = Head.addOrReplaceChild(
                "TopHorn",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(0.0f, -4.0f, 2.3f, 0.6109f, 0.0f, 0.0f)
            )

            val TopHorn_r1 = TopHorn.addOrReplaceChild(
                "TopHorn_r1",
                CubeListBuilder.create().texOffs(48, 62)
                    .addBox(-1.0f, -1.0f, -2.2f, 2.0f, 2.0f, 3.0f, CubeDeformation(0.15f)),
                PartPose.offsetAndRotation(0.0f, -4.5f, 0.5f, 0.0f, 0.0f, 0.7854f)
            )

            val RightEar = Head.addOrReplaceChild(
                "RightEar",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(-2.5f, -8.0f, 0.0f, 0.0f, 0.0f, -0.2618f)
            )

            val Base_r5 = RightEar.addOrReplaceChild(
                "Base_r5",
                CubeListBuilder.create().texOffs(24, 0)
                    .addBox(-0.6f, -1.5f, -3.0f, 1.0f, 3.0f, 1.0f, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-0.5f, 0.0f, 0.0f, -0.2094f, -0.056f, 0.0059f)
            )

            val Base_r6 = RightEar.addOrReplaceChild(
                "Base_r6",
                CubeListBuilder.create().texOffs(38, 62)
                    .addBox(-2.0f, -4.75f, -1.0f, 2.0f, 7.0f, 3.0f, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, -0.7854f, -0.2182f, 0.0785f)
            )

            val LeftEar = Head.addOrReplaceChild(
                "LeftEar",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(2.5f, -8.0f, 0.0f, 0.0f, 0.0f, 0.2618f)
            )

            val Base_r7 = LeftEar.addOrReplaceChild(
                "Base_r7",
                CubeListBuilder.create().texOffs(0, 32)
                    .addBox(-0.4f, -1.5f, -3.0f, 1.0f, 3.0f, 1.0f, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.5f, 0.0f, 0.0f, -0.2094f, 0.056f, -0.0059f)
            )

            val Base_r8 = LeftEar.addOrReplaceChild(
                "Base_r8",
                CubeListBuilder.create().texOffs(28, 61)
                    .addBox(0.0f, -4.75f, -1.0f, 2.0f, 7.0f, 3.0f, CubeDeformation.NONE),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, -0.7854f, 0.2182f, -0.0785f)
            )

            val Mask = Head.addOrReplaceChild(
                "Mask",
                CubeListBuilder.create().texOffs(0, 5)
                    .addBox(-1.0f, -31.0f, -5.0f, 2.0f, 2.0f, 1.0f, CubeDeformation.NONE)
                    .texOffs(16, 38).addBox(-2.0f, -33.0f, -5.0f, 4.0f, 1.0f, 1.0f, CubeDeformation.NONE)
                    .texOffs(64, 28).addBox(-3.0f, -32.0f, -5.0f, 6.0f, 1.0f, 1.0f, CubeDeformation.NONE)
                    .texOffs(0, 21).addBox(-1.0f, -34.0f, -5.0f, 2.0f, 1.0f, 1.0f, CubeDeformation.NONE)
                    .texOffs(16, 32).addBox(-4.0f, -31.0f, -5.0f, 1.0f, 2.0f, 1.0f, CubeDeformation.NONE)
                    .texOffs(12, 32).addBox(3.0f, -31.0f, -5.0f, 1.0f, 2.0f, 1.0f, CubeDeformation.NONE)
                    .texOffs(64, 26).addBox(-3.0f, -29.0f, -5.0f, 6.0f, 1.0f, 1.0f, CubeDeformation.NONE)
                    .texOffs(24, 22).addBox(-4.0f, -28.0f, -5.0f, 8.0f, 1.0f, 1.0f, CubeDeformation.NONE)
                    .texOffs(61, 9).addBox(-2.0f, -29.0f, -7.0f, 4.0f, 2.0f, 2.0f, CubeDeformation.NONE),
                PartPose.offset(0.0f, 26.0f, 0.0f)
            )

            val Torso = partdefinition.addOrReplaceChild(
                "Torso",
                CubeListBuilder.create().texOffs(28, 28)
                    .addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, CubeDeformation.NONE),
                PartPose.offset(0.0f, -0.5f, 0.0f)
            )

            val Tail = Torso.addOrReplaceChild("Tail", CubeListBuilder.create(), PartPose.offset(0.0f, 11.0f, 0.0f))

            val TailPrimary =
                Tail.addOrReplaceChild("TailPrimary", CubeListBuilder.create(), PartPose.offset(0.0f, 0.0f, 0.0f))

            val Base_r9 = TailPrimary.addOrReplaceChild(
                "Base_r9",
                CubeListBuilder.create().texOffs(56, 57)
                    .addBox(-2.0f, -2.9f, 0.4f, 4.0f, 4.0f, 4.0f, CubeDeformation(-0.3f)),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, -0.5236f, 0.0f, 0.0f)
            )

            val TailSecondary = TailPrimary.addOrReplaceChild(
                "TailSecondary",
                CubeListBuilder.create(),
                PartPose.offset(0.0f, 1.0f, 3.5f)
            )

            val Base_r10 = TailSecondary.addOrReplaceChild(
                "Base_r10",
                CubeListBuilder.create().texOffs(48, 0)
                    .addBox(-1.5f, -1.4f, -2.7f, 3.0f, 3.0f, 6.0f, CubeDeformation(-0.1f)),
                PartPose.offsetAndRotation(0.0f, 1.0f, 2.5f, -0.3927f, 0.0f, 0.0f)
            )

            val TailTertiary = TailSecondary.addOrReplaceChild(
                "TailTertiary",
                CubeListBuilder.create(),
                PartPose.offset(0.0f, 2.5f, 5.0f)
            )

            val Base_r11 = TailTertiary.addOrReplaceChild(
                "Base_r11",
                CubeListBuilder.create().texOffs(46, 38)
                    .addBox(-1.5f, -13.225f, 6.6f, 3.0f, 3.0f, 6.0f, CubeDeformation(-0.32f)),
                PartPose.offsetAndRotation(0.0f, 10.5f, -8.5f, -0.1309f, 0.0f, 0.0f)
            )

            val TailQuaternary = TailTertiary.addOrReplaceChild(
                "TailQuaternary",
                CubeListBuilder.create(),
                PartPose.offset(0.0f, 0.5f, 4.5f)
            )

            val Base_r12 = TailQuaternary.addOrReplaceChild(
                "Base_r12",
                CubeListBuilder.create().texOffs(16, 32)
                    .addBox(-1.0f, -10.45f, 13.5f, 2.0f, 2.0f, 4.0f, CubeDeformation(-0.05f)),
                PartPose.offsetAndRotation(0.0f, 10.0f, -13.0f, 0.0436f, 0.0f, 0.0f)
            )

            val LeftWing = Torso.addOrReplaceChild(
                "LeftWing",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(2.0f, 5.0f, 2.0f, 0.0f, -0.48f, 0.0f)
            )

            val leftWingRoot =
                LeftWing.addOrReplaceChild("leftWingRoot", CubeListBuilder.create(), PartPose.offset(0.0f, 0.0f, 0.0f))

            val cube_r1 = leftWingRoot.addOrReplaceChild(
                "cube_r1",
                CubeListBuilder.create().texOffs(37, 0)
                    .addBox(18.975f, -4.475f, 1.65f, 7.0f, 5.0f, 0.0f, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-2.0f, 20.0f, -2.0f, 0.0f, 0.0f, -1.2654f)
            )

            val cube_r2 = leftWingRoot.addOrReplaceChild(
                "cube_r2",
                CubeListBuilder.create().texOffs(60, 47)
                    .addBox(19.075f, -12.7f, 1.2f, 6.0f, 2.0f, 1.0f, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-2.0f, 20.0f, -2.0f, 0.0f, 0.0f, -0.7854f)
            )

            val cube_r3 = leftWingRoot.addOrReplaceChild(
                "cube_r3",
                CubeListBuilder.create().texOffs(64, 43)
                    .addBox(7.775f, -19.75f, 1.2f, 5.0f, 2.0f, 1.0f, CubeDeformation(-0.01f)),
                PartPose.offsetAndRotation(-2.0f, 20.0f, -2.0f, 0.0f, 0.0f, -0.3491f)
            )

            val leftSecondaries = leftWingRoot.addOrReplaceChild(
                "leftSecondaries",
                CubeListBuilder.create().texOffs(52, 67)
                    .addBox(-0.8f, -0.475f, -0.3f, 1.0f, 7.0f, 1.0f, CubeDeformation(0.01f)),
                PartPose.offsetAndRotation(7.3f, -7.0f, -0.5f, 0.0f, 0.0f, -0.5236f)
            )

            val cube_r4 = leftSecondaries.addOrReplaceChild(
                "cube_r4",
                CubeListBuilder.create().texOffs(12, 70)
                    .addBox(-2.025f, -22.55f, 1.2f, 1.0f, 6.0f, 1.0f, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-9.3f, 27.0f, -1.5f, 0.0f, 0.0f, 0.48f)
            )

            val cube_r5 = leftSecondaries.addOrReplaceChild(
                "cube_r5",
                CubeListBuilder.create().texOffs(60, 0)
                    .addBox(15.525f, -13.85f, 1.648f, 9.0f, 6.0f, 0.0f, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-9.3f, 27.0f, -1.5f, 0.0f, 0.0f, -0.7418f)
            )

            val cube_r6 = leftSecondaries.addOrReplaceChild(
                "cube_r6",
                CubeListBuilder.create().texOffs(36, 57)
                    .addBox(13.4f, 10.625f, 1.651f, 9.0f, 5.0f, 0.0f, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-9.3f, 27.0f, -1.5f, 0.0f, 0.0f, -1.8326f)
            )

            val leftTertiaries = leftSecondaries.addOrReplaceChild(
                "leftTertiaries",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(-0.3f, 0.0f, 0.0f, 0.0f, 0.0f, -0.9599f)
            )

            val cube_r7 = leftTertiaries.addOrReplaceChild(
                "cube_r7",
                CubeListBuilder.create().texOffs(48, 67)
                    .addBox(-3.3f, -22.5f, 1.2f, 1.0f, 7.0f, 1.0f, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-9.0f, 27.0f, -1.5f, 0.0f, 0.0f, 0.5236f)
            )

            val cube_r8 = leftTertiaries.addOrReplaceChild(
                "cube_r8",
                CubeListBuilder.create().texOffs(56, 16)
                    .addBox(16.125f, -10.525f, 1.64f, 9.0f, 5.0f, 0.0f, CubeDeformation.NONE),
                PartPose.offsetAndRotation(-9.0f, 27.0f, -1.5f, 0.0f, 0.0f, -0.8727f)
            )

            val cube_r9 = leftTertiaries.addOrReplaceChild(
                "cube_r9",
                CubeListBuilder.create().texOffs(8, 68)
                    .addBox(9.15f, -26.2f, 1.2f, 1.0f, 6.0f, 1.0f, CubeDeformation(0.005f)),
                PartPose.offsetAndRotation(-9.0f, 27.0f, -1.5f, 0.0f, 0.0f, -0.0436f)
            )

            val RightWing = Torso.addOrReplaceChild(
                "RightWing",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(-2.0f, 5.0f, 2.0f, 0.0f, 0.48f, 0.0f)
            )

            val rightWingRoot = RightWing.addOrReplaceChild(
                "rightWingRoot",
                CubeListBuilder.create(),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val cube_r10 = rightWingRoot.addOrReplaceChild(
                "cube_r10",
                CubeListBuilder.create().texOffs(60, 21)
                    .addBox(-25.975f, -4.475f, 1.65f, 7.0f, 5.0f, 0.0f, CubeDeformation.NONE),
                PartPose.offsetAndRotation(2.0f, 20.0f, -2.0f, 0.0f, 0.0f, 1.2654f)
            )

            val cube_r11 = rightWingRoot.addOrReplaceChild(
                "cube_r11",
                CubeListBuilder.create().texOffs(32, 24)
                    .addBox(-25.075f, -12.7f, 1.2f, 6.0f, 2.0f, 1.0f, CubeDeformation.NONE),
                PartPose.offsetAndRotation(2.0f, 20.0f, -2.0f, 0.0f, 0.0f, 0.7854f)
            )

            val cube_r12 = rightWingRoot.addOrReplaceChild(
                "cube_r12",
                CubeListBuilder.create().texOffs(64, 50)
                    .addBox(-12.775f, -19.75f, 1.2f, 5.0f, 2.0f, 1.0f, CubeDeformation(-0.01f)),
                PartPose.offsetAndRotation(2.0f, 20.0f, -2.0f, 0.0f, 0.0f, 0.3491f)
            )

            val rightSecondaries = rightWingRoot.addOrReplaceChild(
                "rightSecondaries",
                CubeListBuilder.create().texOffs(0, 68)
                    .addBox(-0.2f, -0.475f, -0.3f, 1.0f, 7.0f, 1.0f, CubeDeformation(0.01f)),
                PartPose.offsetAndRotation(-7.3f, -7.0f, -0.5f, 0.0f, 0.0f, 0.5236f)
            )

            val cube_r13 = rightSecondaries.addOrReplaceChild(
                "cube_r13",
                CubeListBuilder.create().texOffs(20, 70)
                    .addBox(1.025f, -22.55f, 1.2f, 1.0f, 6.0f, 1.0f, CubeDeformation.NONE),
                PartPose.offsetAndRotation(9.3f, 27.0f, -1.5f, 0.0f, 0.0f, -0.48f)
            )

            val cube_r14 = rightSecondaries.addOrReplaceChild(
                "cube_r14",
                CubeListBuilder.create().texOffs(17, 56)
                    .addBox(-22.4f, 10.625f, 1.651f, 9.0f, 5.0f, 0.0f, CubeDeformation.NONE),
                PartPose.offsetAndRotation(9.3f, 27.0f, -1.5f, 0.0f, 0.0f, 1.8326f)
            )

            val cube_r15 = rightSecondaries.addOrReplaceChild(
                "cube_r15",
                CubeListBuilder.create().texOffs(58, 37)
                    .addBox(-24.525f, -13.85f, 1.648f, 9.0f, 6.0f, 0.0f, CubeDeformation.NONE),
                PartPose.offsetAndRotation(9.3f, 27.0f, -1.5f, 0.0f, 0.0f, 0.7418f)
            )

            val rightTertiaries = rightSecondaries.addOrReplaceChild(
                "rightTertiaries",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(0.3f, 0.0f, 0.0f, 0.0f, 0.0f, 0.9599f)
            )

            val cube_r16 = rightTertiaries.addOrReplaceChild(
                "cube_r16",
                CubeListBuilder.create().texOffs(4, 68)
                    .addBox(2.3f, -22.5f, 1.2f, 1.0f, 7.0f, 1.0f, CubeDeformation.NONE),
                PartPose.offsetAndRotation(9.0f, 27.0f, -1.5f, 0.0f, 0.0f, -0.5236f)
            )

            val cube_r17 = rightTertiaries.addOrReplaceChild(
                "cube_r17",
                CubeListBuilder.create().texOffs(16, 70)
                    .addBox(-10.15f, -26.2f, 1.2f, 1.0f, 6.0f, 1.0f, CubeDeformation(0.005f)),
                PartPose.offsetAndRotation(9.0f, 27.0f, -1.5f, 0.0f, 0.0f, 0.0436f)
            )

            val cube_r18 = rightTertiaries.addOrReplaceChild(
                "cube_r18",
                CubeListBuilder.create().texOffs(52, 32)
                    .addBox(-25.125f, -10.525f, 1.64f, 9.0f, 5.0f, 0.0f, CubeDeformation.NONE),
                PartPose.offsetAndRotation(9.0f, 27.0f, -1.5f, 0.0f, 0.0f, 0.8727f)
            )

            val RightArm = partdefinition.addOrReplaceChild(
                "RightArm",
                CubeListBuilder.create().texOffs(0, 32)
                    .addBox(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, CubeDeformation.NONE),
                PartPose.offset(-5.0f, 1.5f, 0.0f)
            )

            val LeftArm = partdefinition.addOrReplaceChild(
                "LeftArm",
                CubeListBuilder.create().texOffs(16, 40)
                    .addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, CubeDeformation.NONE),
                PartPose.offset(5.0f, 1.5f, 0.0f)
            )

            return LayerDefinition.create(meshdefinition, 96, 96)
        }
    }
}