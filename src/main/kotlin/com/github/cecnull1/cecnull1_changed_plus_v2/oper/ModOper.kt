package com.github.cecnull1.cecnull1_changed_plus_v2.oper

import com.github.cecnull1.adeplike.api.OperRegistry
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant

object ModOper {
    val operRegistry = OperRegistry.create(Constant.MODID)
    val test = operRegistry.register("test")
    val aEntityLike = operRegistry.register("a_entity_like")
    val hasHA = operRegistry.register("has_ha")
    val hasArmorHA = operRegistry.register("has_armor_ha")
    val hasNotCanTakeOffWetsuitLike = operRegistry.register("has_cannot_takeoff_wetsult_like")
    val flyingPureWhiteLatexYufengLike = operRegistry.register("fpwly_like")
    val hasCFlying = operRegistry.register("has_c_flying")
    val autoMove = operRegistry.register("auto_move")
    val forceSwim = operRegistry.register("force_swim")
    val forceSprint = operRegistry.register("force_sprint")
    val forceInWater = operRegistry.register("force_in_water")
    val noGravity = operRegistry.register("no_gravity")
    val forceRide = operRegistry.register("force_ride")
    val forceFallFlying = operRegistry.register("force_fall_flying")
    val forceClimbable = operRegistry.register("force_climb")
    val notOnGround = operRegistry.register("no_on_ground")
    val wocwodejiaohenhua = operRegistry.register("guang_hua")
    val hasAutoMove2 = operRegistry.register("has_auto_move_2")
    val meiXiYuanLike = operRegistry.register("mei_xi_yuan_like")
}