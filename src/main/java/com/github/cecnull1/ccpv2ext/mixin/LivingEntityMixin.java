package com.github.cecnull1.ccpv2ext.mixin;

import com.github.cecnull1.adeplike.api.OperValue;
import com.github.cecnull1.cecnull1_changed_plus_v2.oper.ModOper;
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.CUtilsKt;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @ModifyReturnValue(method = "onClimbable", at = @At("RETURN"))
    private boolean onClimbable(boolean original) {
        var livingEntity = (LivingEntity) (Object) this;
        return CUtilsKt.notZero(OperValue.Companion.getOperNoItem(livingEntity, ModOper.INSTANCE.getForceClimbable())) || original;
    }

    @ModifyReturnValue(method = "isFallFlying", at = @At("RETURN"))
    private boolean isFallFlying(boolean original) {
        var livingEntity = (LivingEntity) (Object) this;
        return CUtilsKt.notZero(OperValue.Companion.getOperNoItem(livingEntity, ModOper.INSTANCE.getForceFallFlying())) || original;
    }

    @Definition(id = "f3", local = @Local(type = float.class, name = "f3"))
    @Expression("f3")
    @ModifyExpressionValue(method = "travel", at = @At("MIXINEXTRAS:EXPRESSION"))
    private float guang_hua(float original) {
        var livingEntity = (LivingEntity) (Object) this;
        return (CUtilsKt.notZero(OperValue.Companion.getOperNoItem(livingEntity, ModOper.INSTANCE.getWocwodejiaohenhua())))
                ? 1f :
                original;
    }

    @Definition(id = "vec36", local = @Local(type = Vec3.class, name = "vec36"))
    @Definition(id = "f4", local = @Local(type = float.class, name = "f4"))
    @Definition(id = "multiply", method = "Lnet/minecraft/world/phys/Vec3;multiply(DDD)Lnet/minecraft/world/phys/Vec3;")
    @Expression("vec36.multiply((double) f4, ?, (double) f4)")
    @ModifyExpressionValue(method = "travel", at = @At("MIXINEXTRAS:EXPRESSION"))
    private Vec3 guang_hua_water(Vec3 original, @Local(name = "vec36") Vec3 vec36) {
        var livingEntity = (LivingEntity) (Object) this;
        return (CUtilsKt.notZero(OperValue.Companion.getOperNoItem(livingEntity, ModOper.INSTANCE.getWocwodejiaohenhua())))
                ? new Vec3(vec36.x, original.y, vec36.z)
                : original;
    }

    @Definition(id = "Vec3", type = Vec3.class)
    @Definition(id = "xxa", field = "Lnet/minecraft/world/entity/LivingEntity;xxa:F")
    @Definition(id = "yya", field = "Lnet/minecraft/world/entity/LivingEntity;yya:F")
    @Definition(id = "zza", field = "Lnet/minecraft/world/entity/LivingEntity;zza:F")
    @Expression("new Vec3((double) this.xxa, (double) this.yya, (double) this.zza)")
    @ModifyExpressionValue(method = "aiStep", at = @At("MIXINEXTRAS:EXPRESSION"))
    private Vec3 autoMove(Vec3 original) {
        var livingEntity = (LivingEntity) (Object) this;
        var oper = OperValue.Companion.getOperNoItem(livingEntity, ModOper.INSTANCE.getHasAutoMove2());
        return (CUtilsKt.notZero(oper)
                ? new Vec3(original.x, original.y, Math.max(original.z, oper.getAttribute() == null ? 0.0 : oper.getAttribute().getValue()))
                : original);
    }
}
