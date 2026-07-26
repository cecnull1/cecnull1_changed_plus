package com.github.cecnull1.casu.mixin

import com.github.cecnull1.casu.CasualtiesUnknownMainKt
import com.github.cecnull1.casu.base.Ref
import com.github.cecnull1.casu.health.{Body, CasualtiesUnknownLimb, CasualtiesUnknownLimbType, Limb, LimbType}
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.player.Player
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import org.spongepowered.asm.mixin.injection.{At, Inject}
import org.spongepowered.asm.mixin.{Mixin, Unique}

@Mixin(Array(classOf[Player]))
class PlayerMixinHealth extends Body:
  @Unique override val $casu$limbs: Ref[Map[LimbType, Limb]] = Ref(Map.empty)
  @Unique override def $casu$update(): Unit =
    val modP = this.asInstanceOf[Player].getPersistentData
    if modP.contains(CasualtiesUnknownMainKt.MODID) then
      modP.put(CasualtiesUnknownMainKt.MODID, CompoundTag())
      $casu$onInit()
    end if

    $casu$limbs.value.foreach((lt, l) => l.update(lt, this))
  end $casu$update

  @Unique override def $casu$onSave(): Unit = ()
  @Unique override def $casu$onLoad(): Unit = ()
  @Unique override def $casu$onInit(): Unit =
    $casu$limbs.value = Map(
      CasualtiesUnknownLimbType.GlobalBody -> new CasualtiesUnknownLimb.GlobalBodyLimb {}
    )

  @Inject(method = Array("m_8119_"), at = Array(new At(value = "RETURN")), remap = false)
  def update(ci: CallbackInfo): Unit = $casu$update()
end PlayerMixinHealth
