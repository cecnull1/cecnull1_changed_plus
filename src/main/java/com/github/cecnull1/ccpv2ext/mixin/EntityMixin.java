package com.github.cecnull1.ccpv2ext.mixin;

import com.github.cecnull1.adeplike.api.OperValue;
import com.github.cecnull1.cecnull1_changed_plus_v2.oper.ModOper;
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.CUtilsKt;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @ModifyReturnValue(method = "isSwimming", at = @At("RETURN"))
    private boolean isSwimming(boolean original) {
        var entity = (Entity) (Object) this;
        if (entity instanceof LivingEntity livingEntity) {
            return CUtilsKt.notZero(OperValue.Companion.getOperNoItem(livingEntity, ModOper.INSTANCE.getForceSwim())) || original;
        }
        return original;
    }

    @ModifyReturnValue(method = "isInWater", at = @At("RETURN"))
    private boolean isInWater(boolean original) {
        var entity = (Entity) (Object) this;
        if (entity instanceof LivingEntity livingEntity) {
            return CUtilsKt.notZero(OperValue.Companion.getOperNoItem(livingEntity, ModOper.INSTANCE.getForceInWater())) || original;
        }
        return original;
    }

    @ModifyReturnValue(method = "onGround", at = @At("RETURN"))
    private boolean onGround(boolean original) {
        var entity = (Entity) (Object) this;
        if (entity instanceof LivingEntity livingEntity) {
            return !CUtilsKt.notZero(OperValue.Companion.getOperNoItem(livingEntity, ModOper.INSTANCE.getNotOnGround())) && original;
        }
        return original;
    }

    @ModifyReturnValue(method = "isNoGravity", at = @At("RETURN"))
    private boolean isNoGravity(boolean original) {
        var entity = (Entity) (Object) this;
        if (entity instanceof LivingEntity livingEntity) {
            return CUtilsKt.notZero(OperValue.Companion.getOperNoItem(livingEntity, ModOper.INSTANCE.getNoGravity())) || original;
        }
        return original;
    }

    @ModifyReturnValue(method = "isSprinting", at = @At("RETURN"))
    private boolean isSprinting(boolean original) {
        var entity = (Entity) (Object) this;
        if (entity instanceof LivingEntity livingEntity) {
            return CUtilsKt.notZero(OperValue.Companion.getOperNoItem(livingEntity, ModOper.INSTANCE.getForceSprint())) || original;
        }
        return original;
    }
    
    @Inject(method = "stopRiding", at = @At("HEAD"), cancellable = true)
    private void stopRiding(CallbackInfo ci) {
        var entity = (Entity) (Object) this;
        if (entity instanceof LivingEntity livingEntity) {
            if (CUtilsKt.notZero(OperValue.Companion.getOperNoItem(livingEntity, ModOper.INSTANCE.getForceRide()))) {
                ci.cancel();
            }
        }
    }

    @Definition(id = "noPhysics", field = "Lnet/minecraft/world/entity/Entity;noPhysics:Z")
    @Expression("this.noPhysics")
    @ModifyExpressionValue(method = {"move", "refreshDimensions", "push(Lnet/minecraft/world/entity/Entity;)V", "isInWall"}, at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean move(boolean original) {
        var entity = (Entity) (Object) this;
        if (entity instanceof LivingEntity livingEntity) {
            return CUtilsKt.notZero(OperValue.Companion.getOperNoItem(livingEntity, ModOper.INSTANCE.getNoPhy())) || original;
        }
        return original;
    }

    @ModifyReturnValue(method = "isInWall", at = @At("RETURN"))
    private boolean isInWall(boolean original) {
        var entity = (Entity) (Object) this;
        if (entity instanceof LivingEntity livingEntity) {
            return !CUtilsKt.notZero(OperValue.Companion.getOperNoItem(livingEntity, ModOper.INSTANCE.getNoInWall())) && original;
        }
        return original;
    }
}
