package com.github.cecnull1.cecnull1_changed_plus_v2.model;

import net.ltxprogrammer.changed.client.renderer.animate.AnimatorPresets;
import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator;
import net.ltxprogrammer.changed.client.renderer.animate.arm.ArmBobAnimator;
import net.ltxprogrammer.changed.client.renderer.animate.arm.ArmRideAnimator;
import net.ltxprogrammer.changed.client.renderer.animate.arm.ArmSwimAnimator;
import net.ltxprogrammer.changed.client.renderer.animate.upperbody.DragonHeadInitAnimator;
import net.ltxprogrammer.changed.client.renderer.animate.upperbody.WolfHeadInitAnimator;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.client.model.geom.ModelPart;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static net.ltxprogrammer.changed.client.renderer.animate.AnimatorPresets.*;

public final class AnimatorUtils {
    private AnimatorUtils() {}
    public static final AnimatorUtils func = new AnimatorUtils();

    public <T extends ChangedEntity, M extends AdvancedHumanoidModel<T>> Consumer<HumanoidAnimator<T, M>> HumanLikeC(
            ModelPart leftLeg,
            ModelPart rightLeg,
            ModelPart leftArm,
            ModelPart rightArm,
            ModelPart head,
            ModelPart torso
    ) {
        ModelPart dummy = new ModelPart(List.of(), Map.of());
        return (animator) -> animator
                .addPreset(AnimatorPresets.wolfLikeArmor(
                        head,
                        torso,
                        leftArm,
                        rightArm,
                        leftLeg,
                        dummy,
                        dummy,
                        dummy,
                        rightLeg,
                        dummy,
                        dummy,
                        dummy
                ))
                ;
    }
}
