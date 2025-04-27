package com.github.cecnull1.cecnull1_changed_plus.entity

import com.github.cecnull1.cecnull1_changed_plus.MODID
import net.ltxprogrammer.changed.entity.*
import net.ltxprogrammer.changed.entity.beast.DarkLatexEntity
import net.ltxprogrammer.changed.init.ChangedMobCategories
import net.ltxprogrammer.changed.util.Color3
import net.minecraft.util.Mth
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
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

class AEntity(type: EntityType<out ChangedEntity>?, level: Level?) : ChangedEntity(type, level), DarkLatexEntity, PowderSnowWalkable, Cloneable {
    override fun getLatexType() = LatexType.DARK_LATEX
    override fun getTransfurMode() = TransfurMode.REPLICATION
    override fun getTransfurColor(cause: TransfurCause?) = Color3.fromInt(0x3d3d3d)!!
    override fun isMaskless() = false

    override fun variantTick(level: Level?) {
        super.variantTick(level)
        val entity = maybeGetUnderlying()
        if (entity.isInWaterOrBubble) {
            val floatAmount = entity.airSupply.toDouble() / entity.maxAirSupply.toDouble()
            entity.deltaMovement = entity.deltaMovement.add(0.0, floatAmount * 0.06, 0.0)
        }
        applyTerminalVelocity(entity)
    }

    private fun applyTerminalVelocity(entity: LivingEntity) {
        val delta = entity.deltaMovement
        entity.setDeltaMovement(delta.x, Mth.clamp(delta.y, -0.5, 0.5), delta.z)
        entity.resetFallDistance()
    }
}