package com.github.cecnull1.casu.health

import com.github.cecnull1.casu.CasuMinecraft.MixinComment
import com.github.cecnull1.casu.base.{FixedPoint32, Ref}
import net.minecraft.world.entity.player.Player

trait Limb:
  var limbHealth: FixedPoint32 = FixedPoint32(100.0)
  def update(limbType: LimbType, body: Body): Unit = ()
  def onRemove(limbType: LimbType, body: Body): Unit = ()
  def onInit(limbType: LimbType, body: Body): Unit = ()
  def onSave(limbType: LimbType, body: Body): Array[Byte] = Array.emptyByteArray
  def onLoad(ser: Array[Byte], limbType: LimbType, body: Body): Unit = ()
end Limb

trait LimbType

object CasualtiesUnknownLimb:
  trait LeftUp
  trait RightDown

  trait UpArm // Arm
  trait DownArm
  trait Hand

  trait Thigh // Leg
  trait Crus
  trait Foot

  trait Torso
  trait Head:
    var brainHealth: FixedPoint32 = FixedPoint32(100.0)
    var happiness: FixedPoint32 = FixedPoint32(0.0)
  end Head

  trait GlobalBody:
    var heartRate: FixedPoint32 = FixedPoint32(60.0)
    var heartProg: FixedPoint32 = FixedPoint32(0.0)
  end GlobalBody

  trait HeadLimb extends Limb
  trait GlobalBodyLimb extends Limb with GlobalBody

  trait UpTorsoLimb extends Limb with LeftUp with Torso
  trait DownTorsoLimb extends Limb with RightDown with Torso

  trait LeftUpArmLimb extends Limb with LeftUp with UpArm
  trait RightUpArmLimb extends Limb with RightDown with UpArm

  trait LeftDownArmLimb extends Limb with LeftUp with DownArm
  trait RightDownArmLimb extends Limb with RightDown with DownArm

  trait LeftHandArmLimb extends Limb with LeftUp with Hand
  trait RightHandArmLimb extends Limb with RightDown with Hand

  trait LeftThighLimb extends Limb with LeftUp with Thigh
  trait RightThighLimb extends Limb with RightDown with Thigh

  trait LeftCrusLimb extends Limb with LeftUp with Crus
  trait RightCrusLimb extends Limb with RightDown with Crus

  trait LeftFootLimb extends Limb with LeftUp with Foot
  trait RightFootLimb extends Limb with RightDown with Foot
end CasualtiesUnknownLimb

object CasualtiesExtLimb:
  trait Tail
  class TailLimb extends Limb with Tail
end CasualtiesExtLimb

enum CasualtiesUnknownLimbType extends LimbType:
  case LeftUp
  case RightDown
  case UpArm // Arm
  case DownArm
  case Hand
  case Thigh // Leg
  case Crus
  case Foot
  case Torso
  case Head
  case GlobalBody
end CasualtiesUnknownLimbType


enum CasualtiesExtLimbType extends LimbType:
  case Tail
end CasualtiesExtLimbType

@MixinComment[Player]
trait Body:
  val $casu$limbs: Ref[Map[LimbType, Limb]]
  def $casu$update(): Unit
  def $casu$onSave(): Unit
  def $casu$onLoad(): Unit
  def $casu$onInit(): Unit
end Body
