package com.github.cecnull1.casu.entity.expie

import com.github.cecnull1.casu.MODID
import com.github.cecnull1.casu.entity.expie.variants.Albino
import com.github.cecnull1.casu.entity.expie.variants.Baron
import com.github.cecnull1.casu.entity.expie.variants.Chik
import com.github.cecnull1.casu.entity.expie.variants.Crystal
import com.github.cecnull1.casu.entity.expie.variants.Dune
import com.github.cecnull1.casu.entity.expie.variants.Icke
import com.github.cecnull1.casu.entity.expie.variants.Leapy
import com.github.cecnull1.casu.entity.expie.variants.Milky
import com.github.cecnull1.casu.entity.expie.variants.Orange
import com.github.cecnull1.casu.entity.expie.variants.Roza
import com.github.cecnull1.casu.entity.expie.variants.Shelly
import com.github.cecnull1.casu.entity.expie.variants.Velvet
import com.github.cecnull1.casu.entity.expie.variants.Voyager
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.form
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.rlclass
import net.ltxprogrammer.changed.entity.variant.TransfurVariant
import net.ltxprogrammer.changed.init.ChangedAbilities
import net.ltxprogrammer.changed.init.ChangedRegistry
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject
import java.util.function.Supplier

object ExpieRegister {
    val entityRegistry: DeferredRegister<EntityType<*>> = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID)
    val variantRegistry: DeferredRegister<TransfurVariant<*>> = ChangedRegistry.TRANSFUR_VARIANT.createDeferred(MODID)

    val experimentType = entityRegistry.genericExperimentType(::Experiment)
    val orangeType = entityRegistry.genericExperimentType(::Orange)
    val milkyType = entityRegistry.genericExperimentType(::Milky)
    val leapyType = entityRegistry.genericExperimentType(::Leapy)
    val rozaType = entityRegistry.genericExperimentType(::Roza)
    val voyagerType = entityRegistry.genericExperimentType(::Voyager)
    val baronType = entityRegistry.genericExperimentType(::Baron)
    val chikType = entityRegistry.genericExperimentType(::Chik)
    val crystalType = entityRegistry.genericExperimentType(::Crystal)
    val velvetType = entityRegistry.genericExperimentType(::Velvet)
    val ickeType = entityRegistry.genericExperimentType(::Icke)
    val shellyType = entityRegistry.genericExperimentType(::Shelly)
    val duneType = entityRegistry.genericExperimentType(::Dune)
    val albinoType = entityRegistry.genericExperimentType(::Albino)

    val experimentVariant = variantRegistry.genericExperimentVariant(experimentType)
    val orangeVariant = variantRegistry.genericExperimentVariant(orangeType)
    val milkyVariant = variantRegistry.genericExperimentVariant(milkyType)
    val leapyVariant = variantRegistry.genericExperimentVariant(leapyType)
    val rozaVariant = variantRegistry.genericExperimentVariant(rozaType)
    val voyagerVariant = variantRegistry.genericExperimentVariant(voyagerType)
    val baronVariant = variantRegistry.genericExperimentVariant(baronType)
    val chikVariant: RegistryObject<TransfurVariant<Chik>> = variantRegistry.register(rlclass<Chik>().form) {
        TransfurVariant.Builder.of(chikType)
            .extraJumps(2)
            .build()
    }
    val crystalVariant: RegistryObject<TransfurVariant<Crystal>> = variantRegistry.register(rlclass<Crystal>().form) {
        TransfurVariant.Builder.of(crystalType)
            .addAbility(ChangedAbilities.GRAB_ENTITY_ABILITY)
            .build()
    }
    val velvetVariant: RegistryObject<TransfurVariant<Velvet>> = variantRegistry.register(rlclass<Velvet>().form) {
        TransfurVariant.Builder.of(velvetType)
            .addAbility(ChangedAbilities.CREATE_COBWEB)
            .build()
    }
    val ickeVariant = variantRegistry.genericExperimentVariant(ickeType)
    val shellyVariant = variantRegistry.genericExperimentVariant(shellyType)
    val duneVariant: RegistryObject<TransfurVariant<Dune>> = variantRegistry.register(rlclass<Dune>().form) {
        TransfurVariant.Builder.of(duneType)
            .breatheMode(TransfurVariant.BreatheMode.ANY)
            .build()
    }
    val albinoVariant = variantRegistry.genericExperimentVariant(albinoType)
}

inline fun <reified T: AbstractExperiment> DeferredRegister<EntityType<*>>.genericExperimentType(e: EntityType.EntityFactory<T>): RegistryObject<EntityType<T>> = this.register(rlclass<T>()) {
    EntityType.Builder.of(e, MobCategory.CREATURE)
        .sized(0.45F, 1.35F)
        .build(rlclass<T>())
}

inline fun <reified T: AbstractExperiment> DeferredRegister<TransfurVariant<*>>.genericExperimentVariant(e: Supplier<EntityType<T>>): RegistryObject<TransfurVariant<T>> = this.register(rlclass<T>().form) {
    TransfurVariant.Builder.of(e)
        .build()
}