package com.github.cecnull1.cecnull1_changed_plus_v2.entity

import com.github.cecnull1.cecnull1_changed_plus_v2.SOUL_USE_ITEM_MODE
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModEntities.A_ENTITY_ID
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModEntities.PURE_WHITE_LATEX_YUFENG_ID
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModEntities.SOUL_ID
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.ModEntities.PURE_WHITE_LATEX_YUFENG_AND_ARMOR_ID
import net.ltxprogrammer.changed.entity.MiningStrength
import net.ltxprogrammer.changed.entity.TransfurMode
import net.ltxprogrammer.changed.entity.VisionType
import net.ltxprogrammer.changed.entity.variant.TransfurVariant
import net.ltxprogrammer.changed.init.ChangedAbilities
import net.ltxprogrammer.changed.init.ChangedRegistry
import net.minecraft.sounds.SoundEvents
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.RegistryObject

object ModTransfurVariant {
    val REGISTRY: DeferredRegister<TransfurVariant<*>> = ChangedRegistry.TRANSFUR_VARIANT.createDeferred(MODID)

    val A_ENTITY_TRANSFUR_VARIANT: RegistryObject<TransfurVariant<AEntity>> = REGISTRY.register("form_$A_ENTITY_ID") {
        TransfurVariant.Builder.of(ModEntities.A_ENTITY).apply {
            glide()
            visionType(VisionType.NIGHT_VISION)
            stepSize(320 + 64f) // 320-(-64) = 320+64
            miningStrength(MiningStrength.STRONG)
            breatheMode(TransfurVariant.BreatheMode.ANY)
            addAbility(ChangedAbilities.TOGGLE_WAVE_VISION)
        }.build()
    }

    val SOUL_TRANSFUR_VARIANT: RegistryObject<TransfurVariant<Soul>> = REGISTRY.register("form_$SOUL_ID") {
        TransfurVariant.Builder.of(ModEntities.SOUL).apply {
            visionType(VisionType.NIGHT_VISION)
            breatheMode(TransfurVariant.BreatheMode.NONE)
            transfurMode(TransfurMode.NONE)
            itemUseMode(SOUL_USE_ITEM_MODE)
        }.build()
    }

//    val C_PLAYER_TRANSFUR_VARIANT: RegistryObject<TransfurVariant<CPlayer>> = REGISTRY.register("form_$CPLAYER_ID") {
//        TransfurVariant.Builder.of(ModEntities.C_PLAYER).apply {
//            faction(LatexType.NEUTRAL)
//        }.build()
//    }

    val PURE_WHITE_LATEX_YUFENG_TRANSFUR_VARIANT: RegistryObject<TransfurVariant<PureWhiteLatexYufeng>> = REGISTRY.register("form_$PURE_WHITE_LATEX_YUFENG_ID") {
        TransfurVariant.Builder.of(ModEntities.PURE_WHITE_LATEX_YUFENG).apply {
            visionType(VisionType.NIGHT_VISION)
            breatheMode(TransfurVariant.BreatheMode.ANY)
            glide()
        }.build()
    }

    val PURE_WHITE_LATEX_YUFENG_AND_ARMOR_TRANSFUR_VARIANT: RegistryObject<TransfurVariant<PureWhiteLatexYufengAndArmor>> = REGISTRY.register("form_$PURE_WHITE_LATEX_YUFENG_AND_ARMOR_ID") {
        TransfurVariant.Builder.of(ModEntities.PURE_WHITE_LATEX_YUFENG_AND_ARMOR).apply {
            visionType(VisionType.NIGHT_VISION)
            breatheMode(TransfurVariant.BreatheMode.ANY)
            glide()
        }.build()
    }

    val NONE_TRANSFUR_VARIANT: RegistryObject<TransfurVariant<NoneTransfurVariant>> = REGISTRY.register("form_none") {
        TransfurVariant.Builder.of(ModEntities.NONE_ENTITY).build()
    }

    val MEI_XI_YUAN_TRANSFUR_VARIANT: RegistryObject<TransfurVariant<MeiXiYuan>> = REGISTRY.register("form_mei_xi_yuan") {
        TransfurVariant.Builder.of(ModEntities2.MEI_XI_YUAN).apply {
            breatheMode(TransfurVariant.BreatheMode.ANY)
            miningStrength(MiningStrength.STRONG)
            sound(SoundEvents.AXOLOTL_IDLE_WATER.location)
        }.build()
    }

//    val ZOMBIE_TRANSFUR_VARIANT: RegistryObject<TransfurVariant<Zombie>> = REGISTRY.register("form_$ZOMBIE_ID") {
//        TransfurVariant.Builder.of(ModEntities.ZOMBIE)
//            .visionType(VisionType.NIGHT_VISION)
//            .faction(LatexType.NEUTRAL)
//            .breatheMode(TransfurVariant.BreatheMode.ANY)
//            .build()
//    }
}

