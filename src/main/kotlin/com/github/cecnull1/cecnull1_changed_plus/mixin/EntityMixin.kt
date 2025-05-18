package com.github.cecnull1.cecnull1_changed_plus.mixin

import com.github.cecnull1.cecnull1_changed_plus.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus.constant.Constant.NBTKeys
import com.github.cecnull1.cecnull1lib.utils.nbt.getModData
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
open class LivingEntityMixin {
    @Inject(
        method = ["m_21255_"],
        at = [At("HEAD")],
        cancellable = true
    )
    open fun isFallFlying(callback: CallbackInfoReturnable<Boolean>) {
        val livingEntity = this as Any as LivingEntity
        if (livingEntity.getModData(MODID).getBoolean(NBTKeys.FLYING)) {
            callback.returnValue = true
            callback.cancel()
        }
    }
}