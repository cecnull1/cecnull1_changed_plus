package com.github.cecnull1.cecnull1_changed_plus_v2.entity

import com.github.cecnull1.cecnull1_cforge.core.PipeCtrl.then
import com.github.cecnull1.cecnull1_changed_plus_v2.ano.Licensed
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.value
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity
import net.ltxprogrammer.changed.entity.AttributePresets
import net.ltxprogrammer.changed.entity.ChangedEntity
import net.ltxprogrammer.changed.entity.TransfurMode
import net.ltxprogrammer.changed.entity.beast.LatexHuman
import net.ltxprogrammer.changed.entity.latex.LatexType
import net.ltxprogrammer.changed.entity.variant.TransfurVariant
import net.ltxprogrammer.changed.init.ChangedLatexTypes
import net.ltxprogrammer.changed.init.ChangedMobCategories
import net.ltxprogrammer.changed.init.ChangedTransfurVariants
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.attributes.AttributeMap
import net.minecraft.world.level.Level
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

object LicensedCharacters {
    val REGISTER: DeferredRegister<EntityType<*>> = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID)
    val FREZO_MS: RegistryObject<EntityType<FrezoMS>> = REGISTER.register("frezo_ms") {
        EntityType.Builder.of(::FrezoMS, ChangedMobCategories.CHANGED)
            .clientTrackingRange(10)
            .sized(0.6F, 1.8F)
            .build("frezo_ms")
    }

    val TIAN_LING: RegistryObject<EntityType<TianLing>> = REGISTER.register("tian_ling") {
        EntityType.Builder.of(::TianLing, ChangedMobCategories.CHANGED)
            .clientTrackingRange(10)
            .sized(0.6F, 1.8F)
            .build("tian_ling")
    }

    val SPECIAL: RegistryObject<EntityType<Special>> = REGISTER.register("special") {
        EntityType.Builder.of(::Special, ChangedMobCategories.CHANGED)
            .clientTrackingRange(10)
            .sized(0.6F, 1.8F)
            .build("special")
    }
}

abstract class UserEntity(type: EntityType<out ChangedEntity>, level: Level) : ChangedEntity(type, level) {
    override fun getTransfurMode(): TransfurMode = TransfurMode.ABSORPTION
    override fun getLatexType(): LatexType? {
        return ChangedLatexTypes.NONE.value
    }

    override fun setAttributes(attributes: AttributeMap?) {
        super.setAttributes(attributes)
        AttributePresets.playerLike(attributes)
    }
}

@Licensed(
    name = "Frz Tian Xia No.1 Miao Sha",
    author = "FRZ天下NO.1(喵鲨）",
    id = 1058356685L
)
open class FrezoMS(type: EntityType<out ChangedEntity>, level: Level): UserEntity(type, level)

@Licensed(
    name = "Tian Ling",
    author = "天凌/帅气小彪彪",
    id = 342547260L
)
open class TianLing(type: EntityType<out ChangedEntity>, level: Level) : UserEntity(type, level)

/*
* 实验性实体
* */
@Licensed(
    name = "Special",
    author = "cecnull1 / all",
    comments = "Common Human Entity",
    id = 0xFFFFFFFFFFFFFFFL
)
open class Special(type: EntityType<out Special>, level: Level) : LatexHuman(type, level) {
    var isAlex: Boolean
        get() = entityData[SpecialIsAlex]
        set(value) {
            entityData[SpecialIsAlex] = value
        }

    var isPlayerSkin: Boolean
        get() = entityData[SpecialIsPlayerSkin]
        set(value) {
            entityData[SpecialIsPlayerSkin] = value
        }

    var location: String
        get() = entityData[SRL]
        set(value) {
            entityData[SRL] = value
        }

    override fun readAdditionalSaveData(tag: CompoundTag) {
        super.readAdditionalSaveData(tag)
        isAlex = tag.getBoolean("isAlex")
        isPlayerSkin = tag.getBoolean("isPlayerSkin")
        location = tag.getString("SRL")
    }

    override fun addAdditionalSaveData(tag: CompoundTag) {
        super.addAdditionalSaveData(tag)
        tag.putBoolean("isAlex", isAlex)
        tag.putBoolean("isPlayerSkin", isPlayerSkin)
        tag.putString("SRL", location)
    }

    override fun savePlayerVariantData(): CompoundTag {
        return super.savePlayerVariantData().apply {
            putBoolean("isAlex", isAlex)
            putBoolean("isPlayerSkin", isPlayerSkin)
            putString("SRL", location)
        }
    }

    override fun readPlayerVariantData(tag: CompoundTag) {
        super.readPlayerVariantData(tag)
        isAlex = tag.getBoolean("isAlex")
        isPlayerSkin = tag.getBoolean("isPlayerSkin")
        location = tag.getString("SRL")
    }

    override fun defineSynchedData() {
        super.defineSynchedData()
        entityData.define(SpecialIsAlex, false)
        entityData.define(SpecialIsPlayerSkin, true)
        entityData.define(SRL, "")
    }

    override fun onReplicateOther(other: IAbstractChangedEntity, variant: TransfurVariant<*>) {
        super.onReplicateOther(other, variant)
        //if (this.getUUID() != this.getRepresentUUID()) return;
        if (variant.`is`(ModTransfurVariant.SPECIAL_TRANSFUR_VARIANT)) {
            (other.changedEntity as? LatexHuman) then {
                setRepresentPlayer(this.representUUID)
            }
        }
    }

    companion object {
        var SRL: EntityDataAccessor<String> = SynchedEntityData.defineId(Special::class.java, EntityDataSerializers.STRING)
        var SpecialIsAlex: EntityDataAccessor<Boolean> = SynchedEntityData.defineId(Special::class.java, EntityDataSerializers.BOOLEAN)
        var SpecialIsPlayerSkin: EntityDataAccessor<Boolean> = SynchedEntityData.defineId(Special::class.java, EntityDataSerializers.BOOLEAN)
    }
}