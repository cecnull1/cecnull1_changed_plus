package com.github.cecnull1.cecnull1_changed_plus.entity

import com.github.cecnull1.cecnull1_changed_plus.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus.entity.ModEntities.A_ENTITY_ID
import net.ltxprogrammer.changed.entity.LatexType
import net.ltxprogrammer.changed.entity.VisionType
import net.ltxprogrammer.changed.entity.variant.TransfurVariant
import net.ltxprogrammer.changed.init.ChangedAbilities
import net.ltxprogrammer.changed.init.ChangedRegistry
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.RegistryObject

object ModTransfurVariant {
    val REGISTRY: DeferredRegister<TransfurVariant<*>> = ChangedRegistry.TRANSFUR_VARIANT.createDeferred(MODID)
    val A_ENTITY_TRANSFURVARIANT: RegistryObject<TransfurVariant<AEntity>> = REGISTRY.register("form_$A_ENTITY_ID") {
        TransfurVariant.Builder.of(ModEntities.A_ENTITY)
            .glide()
            .visionType(VisionType.NIGHT_VISION)
            .faction(LatexType.DARK_LATEX)
            .stepSize(1f)
            .breatheMode(TransfurVariant.BreatheMode.ANY)
            .addAbility(ChangedAbilities.TOGGLE_WAVE_VISION)
            .build()
    }
}