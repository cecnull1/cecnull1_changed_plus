package com.github.cecnull1.cecnull1_changed_plus_v2.animation

import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.MODID
import com.mojang.serialization.Codec
import net.ltxprogrammer.changed.entity.animation.AnimationAssociation
import net.ltxprogrammer.changed.entity.animation.AnimationAssociation.Match
import net.ltxprogrammer.changed.entity.animation.AnimationEvent
import net.ltxprogrammer.changed.entity.animation.AnimationParameters
import net.ltxprogrammer.changed.init.ChangedRegistry
import net.minecraft.world.entity.LivingEntity
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.RegistryObject

object Animations {
    val REGISTRY: DeferredRegister<AnimationEvent<*>> = ChangedRegistry.ANIMATION_EVENTS.createDeferred(MODID)

    val CP_STASIS_IDLE: RegistryObject<AnimationEvent<StasisAnimationParameters>> = REGISTRY.register("cp_stasis_idle") {
        AnimationEvent(StasisAnimationParameters.CODEC)
    }
}

object StasisAnimationParameters: AnimationParameters {
    val CODEC: Codec<StasisAnimationParameters> = Codec.unit { this }
    override fun matchesAssociation(p0: AnimationAssociation?): Match {
        return Match.ALLOW
    }

    override fun shouldEndAnimation(livingEntity: LivingEntity, totalTime: Float): Boolean {
        return livingEntity.vehicle == null && totalTime > 0.2f
    }

    override fun shouldLoop(livingEntity: LivingEntity?, totalTime: Float): Boolean {
        return true
    }
}