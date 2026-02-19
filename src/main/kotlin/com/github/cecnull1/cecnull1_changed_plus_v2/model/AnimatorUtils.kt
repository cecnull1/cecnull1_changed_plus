package com.github.cecnull1.cecnull1_changed_plus_v2.model

import net.ltxprogrammer.changed.client.renderer.animate.AnimatorPresets
import net.ltxprogrammer.changed.client.renderer.animate.HumanoidAnimator
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel
import net.ltxprogrammer.changed.entity.ChangedEntity
import net.minecraft.client.model.geom.ModelPart
import java.util.function.Consumer

object AnimatorUtils {
    inline val func get() = Fun
    object Fun {
        fun <T : ChangedEntity, M : AdvancedHumanoidModel<T>> humanLikeC(
            leftLeg: ModelPart,
            rightLeg: ModelPart,
            leftArm: ModelPart,
            rightArm: ModelPart,
            head: ModelPart,
            torso: ModelPart
        ): Consumer<HumanoidAnimator<T, M>> {
            //val dummy = ModelPart(mutableListOf<ModelPart.Cube>(), mapOf<String, ModelPart>())
            return Consumer { animator: HumanoidAnimator<T, M> ->
                animator.addPreset(AnimatorPresets.humanLike<T, M>(head, torso, leftArm, rightArm, leftLeg, rightLeg))
            }
        }
    }
}
