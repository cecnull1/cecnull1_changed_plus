package com.github.cecnull1.cecnull1_changed_plus.mixin

import net.ltxprogrammer.changed.entity.robot.Exoskeleton
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

@Mixin(Exoskeleton::class)
open class ExoskeletonMixin { // 注意这里的 open 关键字

    @Inject(
        method = ["isWearerValid"],
        at = [At("HEAD")],
        cancellable = true
    )
    open fun isWearerValid(wearer: Entity, callback: CallbackInfoReturnable<Boolean>) {
        callback.returnValue = true
        callback.cancel()
    }
}

@Mixin(LivingEntity::class)
open class LivingEntityMixin