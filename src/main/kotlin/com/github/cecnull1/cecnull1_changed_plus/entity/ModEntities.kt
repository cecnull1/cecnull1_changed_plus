package com.github.cecnull1.cecnull1_changed_plus.entity

import com.github.cecnull1.cecnull1_changed_plus.MODID
import net.ltxprogrammer.changed.entity.*
import net.ltxprogrammer.changed.entity.beast.DarkLatexEntity
import net.ltxprogrammer.changed.init.ChangedMobCategories
import net.ltxprogrammer.changed.util.Color3
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

object ModEntities {
    const val A_ENTITY_ID = "a_entity"

    val REGISTRY: DeferredRegister<EntityType<*>> = DeferredRegister.create(ForgeRegistries.ENTITIES, MODID)

    val A_ENTITY: RegistryObject<EntityType<AEntity>> = REGISTRY.register(A_ENTITY_ID) {
        EntityType.Builder.of(::AEntity, ChangedMobCategories.CHANGED)
            .sized(0.7f, 1.93f)
            .build(A_ENTITY_ID)
    }
}

class AEntity(type: EntityType<out ChangedEntity>?, level: Level?) : ChangedEntity(type, level), DarkLatexEntity, PowderSnowWalkable {
    override fun getLatexType(): LatexType {
        return LatexType.DARK_LATEX
    }

    override fun getTransfurMode(): TransfurMode {
        return TransfurMode.REPLICATION
    }

    override fun getTransfurColor(cause: TransfurCause?): Color3 {
        return Color3.fromInt(0x3d3d3d)
    }

    override fun isMaskless(): Boolean {
        return true
    }
}