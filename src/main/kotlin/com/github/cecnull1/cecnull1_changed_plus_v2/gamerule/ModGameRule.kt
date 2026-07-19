package com.github.cecnull1.cecnull1_changed_plus_v2.gamerule

import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.MODID
import net.minecraft.world.level.GameRules
import net.minecraft.world.level.GameRules.BooleanValue


object ModGameRule {
    val KeepHA: GameRules.Key<BooleanValue> by lazy {
        GameRules.register(
            "$MODID:keepHa",
            GameRules.Category.PLAYER,
            BooleanValue.create(false)
        )
    }
    val KeepArmorHA: GameRules.Key<BooleanValue> by lazy {
        GameRules.register(
            "$MODID:keepArmorHa",
            GameRules.Category.PLAYER,
            BooleanValue.create(false)
        )
    }
    val canFanJi: GameRules.Key<BooleanValue> by lazy {
        GameRules.register(
            "$MODID:canFanJi",
            GameRules.Category.PLAYER,
            BooleanValue.create(true)
        )
    }
    val canRealKeepForm: GameRules.Key<BooleanValue> by lazy {
        GameRules.register(
            "$MODID:canRealKeepForm",
            GameRules.Category.PLAYER,
            BooleanValue.create(false)
        )
    }
    val realKeepForm: GameRules.Key<BooleanValue> by lazy {
        GameRules.register(
            "$MODID:realKeepForm",
            GameRules.Category.PLAYER,
            BooleanValue.create(false)
        )
    }
    val isJiXianShiTF: GameRules.Key<BooleanValue> by lazy {
        GameRules.register(
            "$MODID:isJiXianShiTF",
            GameRules.Category.PLAYER,
            BooleanValue.create(false)
        )
    }

    fun register() {
        KeepHA
        KeepArmorHA
        canFanJi
        canRealKeepForm
        realKeepForm
        isJiXianShiTF
    }
}