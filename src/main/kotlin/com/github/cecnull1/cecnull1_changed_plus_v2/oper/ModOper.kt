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
}