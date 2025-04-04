package com.github.cecnull1.cecnull1_changed_plus.entity

import com.github.cecnull1.cecnull1_changed_plus.MODID
import net.ltxprogrammer.changed.entity.LatexType
import net.ltxprogrammer.changed.entity.VisionType
import net.ltxprogrammer.changed.entity.variant.TransfurVariant
import net.ltxprogrammer.changed.init.ChangedMobCategories
import net.ltxprogrammer.changed.init.ChangedRegistry
import net.minecraft.world.entity.EntityType
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries

object Inits {
    val ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, MODID)
    val TRANSFUR_VARIANTS = ChangedRegistry.TRANSFUR_VARIANT.createDeferred(MODID)

    val custom1Entity = ENTITIES?.register("custom1") {
        EntityType.Builder.of(::Custom1, ChangedMobCategories.CHANGED)
            .clientTrackingRange(10)
            .sized(0.7F, 1.93F)
            .build("custom1")
    }

    val custom1Variant = TRANSFUR_VARIANTS.register("custom1") {
        TransfurVariant.Builder.of(custom1Entity)
            .stepSize(3f)
            .faction(LatexType.WHITE_LATEX)
            .glide()
            .visionType(VisionType.NIGHT_VISION)
            .canClimb()
            .doubleJump()
            .breatheMode(TransfurVariant.BreatheMode.ANY)
            .build()
    }
}

val modEventBus = FMLJavaModLoadingContext.get().modEventBus