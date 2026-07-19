package com.github.cecnull1.casu.event

import com.github.cecnull1.casu.MODID
import com.github.cecnull1.casu.ModGameRules
import com.github.cecnull1.casu.entity.expie.ExpieRegister
import com.github.cecnull1.cecnull1_cforge.core.PipeCtrl.then
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.level
import com.github.cecnull1.cecnull1lib.utils.changed.TransfurContextUtils.toTransfurContext
import com.github.cecnull1.cecnull1lib.utils.changed.TransfurData
import com.github.cecnull1.cecnull1lib.utils.changed.ifPlayerNotTransfurred
import com.github.cecnull1.cecnull1lib.utils.changed.setPlayerTransfurVariant
import com.github.cecnull1.cecnull1lib.utils.nbt.modPersistentData
import net.ltxprogrammer.changed.entity.TransfurCause
import net.ltxprogrammer.changed.entity.variant.TransfurVariant
import net.ltxprogrammer.changed.init.ChangedAnimationEvents
import net.ltxprogrammer.changed.process.ProcessTransfur
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
object ModEvents {
    @JvmStatic
    @SubscribeEvent
    fun onDie(event: LivingDeathEvent) {
        (event.entity as? Player).then {
        }
    }

    @JvmStatic
    @SubscribeEvent
    fun onTick(event: TickEvent.PlayerTickEvent) {
        (event.player as? ServerPlayer).then {
            if (!this.modPersistentData.contains("FirstVariant") && this.level.gameRules.getRule(ModGameRules.initExperiment).get()) {
                this.ifPlayerNotTransfurred {
                    this.setPlayerTransfurVariant(
                        TransfurData(
                            ExpieRegister.experimentVariant.get(),
                            true,
                            TransfurCause.GRAB_REPLICATE.toTransfurContext()
                        ),
                        1.0f
                    )
                    this.modPersistentData.putBoolean("FirstVariant", true)
                }
            }
        }
    }
}