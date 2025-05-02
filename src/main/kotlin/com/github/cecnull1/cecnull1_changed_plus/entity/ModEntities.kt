package com.github.cecnull1.cecnull1_changed_plus.entity

import com.github.cecnull1.cecnull1_changed_plus.constant.Constant.MODID
import net.ltxprogrammer.changed.entity.LatexType
import net.ltxprogrammer.changed.entity.PowderSnowWalkable
import net.ltxprogrammer.changed.entity.TransfurCause
import net.ltxprogrammer.changed.entity.TransfurMode
import net.ltxprogrammer.changed.entity.beast.AbstractDarkLatexEntity
import net.ltxprogrammer.changed.entity.beast.DarkLatexEntity
import net.ltxprogrammer.changed.init.ChangedMobCategories
import net.ltxprogrammer.changed.util.Color3
import net.minecraft.util.Mth
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject
import java.util.*

object ModEntities {
    const val A_ENTITY_ID = "a_entity"

    val REGISTRY: DeferredRegister<EntityType<*>> = DeferredRegister.create(ForgeRegistries.ENTITIES, MODID)

    val A_ENTITY: RegistryObject<EntityType<AEntity>> = REGISTRY.register(A_ENTITY_ID) {
        EntityType.Builder.of(::AEntity, ChangedMobCategories.CHANGED)
            .sized(0.7f, 1.93f)
            .build(A_ENTITY_ID)
    }
}

class AEntity(type: EntityType<out AbstractDarkLatexEntity>?, level: Level?) : AbstractDarkLatexEntity(type, level), DarkLatexEntity, PowderSnowWalkable {
    override fun getLatexType() = LatexType.DARK_LATEX
    override fun getTransfurMode() = TransfurMode.REPLICATION
    override fun getTransfurColor(cause: TransfurCause?) = Color3.fromInt(0x3d3d3d)!!
    override fun isMaskless() = false

    override fun variantTick(level: Level?) {
        super.variantTick(level)
        val entity = maybeGetUnderlying()
        val delta = entity.deltaMovement
        if (entity.isInWaterOrBubble) {
            entity.deltaMovement = Vec3(delta.x, delta.y.coerceAtMost(0.0), delta.z)
        }
        applyTerminalVelocity(entity)
    }

    private fun applyTerminalVelocity(entity: LivingEntity) {
        val delta = entity.deltaMovement
        entity.deltaMovement = Vec3(delta.x, Mth.clamp(delta.y, -0.5, 0.5), delta.z)
        entity.resetFallDistance()
    }

    override fun getOwnerUUID(): UUID? {
        return entityData.get(DATA_OWNERUUID_ID).orElse(null)
    }
}