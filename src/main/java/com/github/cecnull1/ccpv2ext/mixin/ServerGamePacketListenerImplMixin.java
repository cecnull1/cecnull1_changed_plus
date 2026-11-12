package com.github.cecnull1.ccpv2ext.mixin;

import com.github.cecnull1.adeplike.api.OperValue;
import com.github.cecnull1.cecnull1_changed_plus_v2.oper.ModOper;
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.CUtilsKt;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {
    @Definition(id = "player", field = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;player:Lnet/minecraft/server/level/ServerPlayer;")
    @Definition(id = "noPhysics", field = "Lnet/minecraft/server/level/ServerPlayer;noPhysics:Z")
    @Expression("this.player.noPhysics")
    @ModifyExpressionValue(method = "handleMovePlayer", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean handleMovePlayer(boolean original) {
        var entity = ((ServerGamePacketListenerImpl) (Object) this).player;
        return CUtilsKt.notZero(OperValue.Companion.getOperNoItem(entity, ModOper.INSTANCE.getNoPhy())) || original;
    }
}
