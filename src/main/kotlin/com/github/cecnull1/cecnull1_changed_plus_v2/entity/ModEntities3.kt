package com.github.cecnull1.cecnull1_changed_plus_v2.entity

import com.github.cecnull1.cecnull1_cforge.core.PipeCtrl.then
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.rlclass
import com.github.cecnull1.cecnull1lib.utils.MCreatorFunction.findNearestEntity
import net.ltxprogrammer.changed.entity.AttributePresets
import net.ltxprogrammer.changed.entity.ChangedEntity
import net.ltxprogrammer.changed.entity.TransfurMode
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance
import net.ltxprogrammer.changed.init.ChangedMobCategories
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeMap
import net.minecraft.world.level.Level
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

object ModEntities3 {
    val REGISTRY: DeferredRegister<EntityType<*>> = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID)

    val FutiEntityType: RegistryObject<EntityType<Futi>> = REGISTRY.register(rlclass<Futi>()) {
        EntityType.Builder.of(::Futi, ChangedMobCategories.CHANGED).apply {
            sized(.7f, 1.73f)
        }.build(rlclass<Futi>())
    }
}

class Futi(type: EntityType<out ChangedEntity>, level: Level): ChangedEntity(type, level) {
    init {
        noPhysics = true
    }

    override fun getTransfurMode(): TransfurMode = TransfurMode.NONE
    override fun variantTick(level: Level) {
        super.variantTick(level)
        level.findNearestEntity(position(), 1.0, LivingEntity::class.java, maybeGetUnderlying()) then Living@ {
            maybeGetUnderlying() then WithPlayer@ {
                TransfurVariantInstance.syncEntityPosRotWithEntity(this@Living, this)
            }
        }
    }

    override fun setAttributes(attributes: AttributeMap) {
        super.setAttributes(attributes)
        AttributePresets.wolfLike(attributes)
    }

    override fun canCollideWith(entity: Entity): Boolean {
        return false
    }

    override fun canBeCollidedWith(): Boolean {
        return false
    }
}