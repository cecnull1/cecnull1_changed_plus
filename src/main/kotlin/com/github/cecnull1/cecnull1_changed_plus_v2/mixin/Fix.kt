@file:Mixin(MinecraftClient::class)
@file:JvmName("ClientMixin")
package com.github.cecnull1.cecnull1_changed_plus_v2.mixin

import com.mojang.authlib.minecraft.client.MinecraftClient
import com.mojang.logging.LogUtils.getLogger
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Inject(method = ["<clinit>"], at = [At("HEAD")])
private fun main(ci: CallbackInfo) {
    getLogger().info("HHH")
    getLogger().info("HHH")
}