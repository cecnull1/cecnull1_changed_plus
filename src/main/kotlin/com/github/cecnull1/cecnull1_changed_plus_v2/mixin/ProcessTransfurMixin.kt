@file:JvmName("ProcessTransfurMixin")
@file:Mixin(ProcessTransfur::class)
package com.github.cecnull1.cecnull1_changed_plus_v2.mixin

import com.github.cecnull1.cecnull1_changed_plus_v2.gamerule.ModGameRule
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.RealKeepForm
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.level
import com.github.cecnull1.cecnull1lib.utils.changed.playerTransfurVariant
import net.ltxprogrammer.changed.process.ProcessTransfur
import net.minecraft.world.entity.player.Player
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Inject(method = ["removePlayerTransfurVariant"], at = [At("HEAD")], cancellable = true, remap = false)
private fun removePlayerTransfurVariant(player: Player, ci: CallbackInfo) {
    val b1 = player.level.gameRules.getBoolean(ModGameRule.realKeepForm)
    val oldVariant = player.playerTransfurVariant
    val b2 = oldVariant?.changedEntity is RealKeepForm && player.level.gameRules.getBoolean(ModGameRule.canRealKeepForm)
    if (b1 || b2) ci.cancel()
}