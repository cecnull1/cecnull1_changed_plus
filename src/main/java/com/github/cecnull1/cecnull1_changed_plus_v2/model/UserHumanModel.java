package com.github.cecnull1.cecnull1_changed_plus_v2.model;// Made with Blockbench 5.0.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.ltxprogrammer.changed.client.animations.Limb;
import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModelInterface;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorHumanModel;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorModelSet;
import net.ltxprogrammer.changed.client.renderer.model.armor.LatexHumanoidArmorModel;
import net.ltxprogrammer.changed.client.tfanimations.HelperModel;
import net.ltxprogrammer.changed.client.tfanimations.TransfurHelper;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import static com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.MODID;
import static com.github.cecnull1.cecnull1_changed_plus_v2.renderer.RendererKt.newrl;

public class UserHumanModel<T extends ChangedEntity> extends AdvancedHumanoidModel<T> implements AdvancedHumanoidModelInterface<T, UserHumanModel<T>> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION_STEVE = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(MODID, "steve_model"), "main");
    public static final ModelLayerLocation LAYER_LOCATION_ALEX = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(MODID, "alex_model"), "main");
	private final ModelPart RightLeg;
	private final ModelPart LeftLeg;
	private final ModelPart Head;
	private final ModelPart Torso;
	private final ModelPart RightArm;
	private final ModelPart LeftArm;
    private final HumanoidAnimator<T, UserHumanModel<T>> animator;
    public static final Fun func = new Fun();

	public UserHumanModel(ModelPart root) {
        super(root);
        this.RightLeg = root.getChild("RightLeg");
		this.LeftLeg = root.getChild("LeftLeg");
		this.Head = root.getChild("Head");
		this.Torso = root.getChild("Torso");
		this.RightArm = root.getChild("RightArm");
		this.LeftArm = root.getChild("LeftArm");

        this.animator = HumanoidAnimator.of(this).hipOffset(-1.5f).legLength(10.5f)
                .addPreset(AnimatorUtils.func.HumanLikeC(LeftLeg, RightLeg, LeftArm, RightArm, Head, Torso));
	}

    @Override
    public ModelPart getArm(HumanoidArm arm) {
        return arm == HumanoidArm.LEFT ? LeftArm : RightArm;
    }

    @Override
    public ModelPart getLeg(HumanoidArm leg) {
        return leg == HumanoidArm.LEFT ? LeftLeg : RightLeg;
    }

    public static class Fun {
        private Fun() {}

        public LayerDefinition createSteveLayer() {
            MeshDefinition meshdefinition = new MeshDefinition();
            PartDefinition partdefinition = meshdefinition.getRoot();

            PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                    .texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

            PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                    .texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(1.9F, 12.0F, 0.0F));

            PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                    .texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

            PartDefinition Torso = partdefinition.addOrReplaceChild("Torso", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                    .texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

            PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                    .texOffs(40, 32).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

            PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                    .texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(5.0F, 2.0F, 0.0F));

            return LayerDefinition.create(meshdefinition, 64, 64);
        }

        public LayerDefinition createAlexLayer() {
            MeshDefinition meshdefinition = new MeshDefinition();
            PartDefinition partdefinition = meshdefinition.getRoot();

            PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                    .texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

            PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                    .texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(1.9F, 12.0F, 0.0F));

            PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                    .texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

            PartDefinition Torso = partdefinition.addOrReplaceChild("Torso", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                    .texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

            PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(40, 16).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                    .texOffs(40, 32).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

            PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                    .texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(5.0F, 2.0F, 0.0F));

            return LayerDefinition.create(meshdefinition, 64, 64);
        }
    }

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.animator.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		RightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		LeftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Torso.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		RightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		LeftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

    @Override
    public ModelPart getTorso() {
        return Torso;
    }

    @Override
    public void setupHand(T entity) {
        this.animator.setupHand();
    }

    @Override
    public ModelPart getHead() {
        return Head;
    }

    @Override
    public HumanoidAnimator<T, UserHumanModel<T>> getAnimator(T entity) {
        return animator;
    }

    @Override
    public void prepareMobModel(T p_102861_, float p_102862_, float p_102863_, float p_102864_) {
        this.prepareMobModel(animator, p_102861_, p_102862_, p_102863_, p_102864_);
    }

    public static class ArmorModel<AT extends ChangedEntity> extends LatexHumanoidArmorModel<AT, ArmorModel<AT>> {
        public static final ArmorModelSet<ChangedEntity, ArmorModel<ChangedEntity>> MODEL_SET =
                ArmorModelSet.of(newrl(MODID, "user_entity_model"), ArmorHumanModel::createArmorLayer, ArmorModel::new);

        private final ModelPart RightLeg;
        private final ModelPart LeftLeg;
        private final ModelPart Head;
        private final ModelPart Torso;
        private final ModelPart RightArm;
        private final ModelPart LeftArm;
        private final HumanoidAnimator<AT, ArmorModel<AT>> animator;

        public ArmorModel(ModelPart root, net.ltxprogrammer.changed.client.renderer.model.armor.ArmorModel armorModel) {
            super(root, armorModel);
            this.RightLeg = root.getChild("right_leg");
            this.LeftLeg = root.getChild("left_leg");
            this.Head = root.getChild("head");
            this.Torso = root.getChild("body");
            this.RightArm = root.getChild("right_arm");
            this.LeftArm = root.getChild("left_arm");

            this.animator = HumanoidAnimator.of(this).hipOffset(-1.5f).legLength(10.5f)
                    .addPreset(AnimatorUtils.func.HumanLikeC(LeftLeg, RightLeg, LeftArm, RightArm, Head, Torso));
        }

        @Override
        public void renderForSlot(AT entity, RenderLayerParent<? super AT, ?> parent, ItemStack stack, EquipmentSlot slot, PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
            poseStack.pushPose();
            this.scaleForSlot(parent, slot, poseStack);

            switch (slot) {
                case HEAD -> Head.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
                case CHEST -> {
                    Torso.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
                    LeftArm.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
                    RightArm.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
                }
                case LEGS -> {
                    Torso.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
                    LeftLeg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
                    RightLeg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
                }
                case FEET -> {
                    LeftLeg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
                    RightLeg.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
                }
            }

            poseStack.popPose();
        }

        @Override
        public @Nullable HelperModel getTransfurHelperModel(Limb limb) {
            if (limb == Limb.TORSO)
                return TransfurHelper.getFeminineTorso(this.armorModel);
            return super.getTransfurHelperModel(limb);
        }

        @Override
        public ModelPart getArm(HumanoidArm arm) {
            return arm == HumanoidArm.LEFT ? LeftArm : RightArm;
        }

        @Override
        public ModelPart getLeg(HumanoidArm leg) {
            return leg == HumanoidArm.LEFT ? LeftLeg : RightLeg;
        }

        @Override
        public ModelPart getHead() {
            return Head;
        }

        @Override
        public ModelPart getTorso() {
            return Torso;
        }

        @Override
        public HumanoidAnimator<AT, ArmorModel<AT>> getAnimator(AT entity) {
            return animator;
        }

        @Override
        public void prepareMobModel(AT p_102861_, float p_102862_, float p_102863_, float p_102864_) {
            this.prepareMobModel(animator, p_102861_, p_102862_, p_102863_, p_102864_);
        }

        @Override
        public void setupAnim(AT entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
            this.animator.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        }
    }
}