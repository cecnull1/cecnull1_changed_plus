package com.github.cecnull1.cecnull1_changed_plus.entity

import com.github.cecnull1.cecnull1_changed_plus.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus.entity.ModEntities.A_ENTITY_ID
import com.github.cecnull1.cecnull1_changed_plus.entity.ModEntities.ZOMBIE_ID
import net.ltxprogrammer.changed.entity.LatexType
import net.ltxprogrammer.changed.entity.VisionType
import net.ltxprogrammer.changed.entity.variant.TransfurVariant
import net.ltxprogrammer.changed.init.ChangedAbilities
import net.ltxprogrammer.changed.init.ChangedRegistry
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.RegistryObject

object ModTransfurVariant {
    val REGISTRY: DeferredRegister<TransfurVariant<*>> = ChangedRegistry.TRANSFUR_VARIANT.createDeferred(MODID)

    val A_ENTITY_TRANSFUR_VARIANT: RegistryObject<TransfurVariant<AEntity>> = REGISTRY.register("form_$A_ENTITY_ID") {
    TransfurVariant.Builder.of(ModEntities.A_ENTITY)
        .glide()
        .visionType(VisionType.NIGHT_VISION)
        .faction(LatexType.DARK_LATEX)
        .stepSize(320+64f)
        .breatheMode(TransfurVariant.BreatheMode.NONE)
        .addAbility(ChangedAbilities.TOGGLE_WAVE_VISION)
        .build()
    }

    val ZOMBIE_TRANSFUR_VARIANT: RegistryObject<TransfurVariant<Zombie>> = REGISTRY.register("form_$ZOMBIE_ID") {
        TransfurVariant.Builder.of(ModEntities.ZOMBIE)
            .visionType(VisionType.NIGHT_VISION)
            .faction(LatexType.NEUTRAL)
            .breatheMode(TransfurVariant.BreatheMode.ANY)
            .build()
    }
}