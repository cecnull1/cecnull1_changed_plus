package com.github.cecnull1.cecnull1_changed_plus_v2.entity

import com.github.cecnull1.cecnull1_cforge.big_core.ExperimentalRegistry
import com.github.cecnull1.cecnull1_cforge.big_core.Registry
import com.github.cecnull1.cecnull1_cforge.big_core.RegistryCore.register
import com.github.cecnull1.cecnull1_cforge.core.CForgeEventCore.registerFastEvents
import com.github.cecnull1.cecnull1_changed_plus_v2.ano.ID
import com.github.cecnull1.cecnull1_changed_plus_v2.ano.Licensed
import com.github.cecnull1.cecnull1_changed_plus_v2.bus
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus_v2.entity.LicensedCharacters.USER_LATEX
import com.github.cecnull1.cecnull1_changed_plus_v2.event.NullSafeAttributeCreationEvent
import com.github.cecnull1.cecnull1_changed_plus_v2.event.RegisterRenderers
import com.github.cecnull1.cecnull1_changed_plus_v2.renderer.FrezoMSRenderer
import com.github.cecnull1.cecnull1_changed_plus_v2.renderer.LnvincibleRenderer
import com.github.cecnull1.cecnull1_changed_plus_v2.renderer.TianLingRenderer
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.rlclass
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.toRLString
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity
import net.ltxprogrammer.changed.entity.AttributePresets
import net.ltxprogrammer.changed.entity.ChangedEntity
import net.ltxprogrammer.changed.entity.TransfurCause
import net.ltxprogrammer.changed.entity.TransfurMode
import net.ltxprogrammer.changed.entity.beast.LatexHuman
import net.ltxprogrammer.changed.entity.latex.LatexType
import net.ltxprogrammer.changed.entity.variant.TransfurVariant
import net.ltxprogrammer.changed.init.ChangedLatexTypes
import net.ltxprogrammer.changed.init.ChangedRegistry
import net.ltxprogrammer.changed.util.Color3
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.entity.ai.attributes.AttributeMap
import net.minecraft.world.level.Level
import net.minecraft.world.level.storage.loot.BuiltInLootTables
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject
import java.awt.Color

object LicensedCharacterInit {
    fun registerEvent() {
        bus.registerFastEvents<NullSafeAttributeCreationEvent> { event ->
            STATIC_ATTRIBUTE_REGISTRY.forEach {
                event.put(it.get(), ChangedEntity.createLatexAttributes().build())
            }
        }
        bus.registerFastEvents<RegisterRenderers> { event ->
            LicensedCharacters.apply {
                event.registerEntityRenderer(FREZO_MS.get(), ::FrezoMSRenderer)
                event.registerEntityRenderer(TIAN_LING.get(), ::TianLingRenderer)
                event.registerEntityRenderer(LNVINCIBLE.get(), ::LnvincibleRenderer)
            }
        }
    }

    fun registerEntities(modEventBus: IEventBus) {
        LicensedCharacters.REGISTER.register(modEventBus)
        LicensedCharacters.LATEX_TYPE_REGISTER.register(modEventBus)
        LicensedCharacterTransfurs.REGISTER.register(modEventBus)
    }

    val STATIC_ATTRIBUTE_REGISTRY = mutableListOf<RegistryObject<out EntityType<out LivingEntity>>>()
    val VARIANT_REGISTRY = Registry<TransfurVariant<*>>() // 不使用RegistryObject即可使用的自定义Registry。
}

object LicensedCharacters {
    val REGISTER: DeferredRegister<EntityType<*>> = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID)

    val FREZO_MS: RegistryObject<EntityType<FrezoMS>> = registerEntity(::FrezoMS)
    val TIAN_LING: RegistryObject<EntityType<TianLing>> = registerEntity(::TianLing)
    val SPECIAL: RegistryObject<EntityType<Special>> = registerEntity(::Special)
    val LNVINCIBLE: RegistryObject<EntityType<Lnvincible>> = registerEntity(::Lnvincible)

    val LATEX_TYPE_REGISTER: DeferredRegister<LatexType> = ChangedRegistry.LATEX_TYPE.createDeferred(MODID)
    val USER_LATEX: RegistryObject<LatexType> = LATEX_TYPE_REGISTER.register("user_latex", fun() = UserLatex)
}

object LicensedCharacterTransfurs {
    val REGISTER: DeferredRegister<TransfurVariant<*>> = ChangedRegistry.TRANSFUR_VARIANT.createDeferred(MODID)

    val FREZO_MS_TRANSFUR_VARIANT = LicensedCharacters.FREZO_MS.registerTransfur()
    val TIAN_LING_TRANSFUR_VARIANT = LicensedCharacters.TIAN_LING.registerTransfur()
    val LNVINCIBLE_TRANSFUR_VARIANT = LicensedCharacters.LNVINCIBLE.registerTransfur()
}

private inline fun <reified T: ChangedEntity> registerEntity(
    noinline entityClassRef: (EntityType<out T>, Level) -> T
): RegistryObject<EntityType<T>> {
    return LicensedCharacters.REGISTER.register(rlclass<T>()) {
        EntityType.Builder.of(entityClassRef, MobCategory.MONSTER).apply {
            clientTrackingRange(10)
            sized(0.6F, 1.8F)
        }.build(rlclass<T>())
    }.also {
        LicensedCharacterInit.STATIC_ATTRIBUTE_REGISTRY.add(it)
    }
}

@OptIn(ExperimentalRegistry::class)
private inline fun <reified T: ChangedEntity> RegistryObject<EntityType<T>>.registerTransfur(): RegistryObject<TransfurVariant<T>> {
    return LicensedCharacterTransfurs.REGISTER.register("form_${rlclass<T>()}") {
        val build = TransfurVariant.Builder.of(fun() = this.get()).build()
        LicensedCharacterInit.VARIANT_REGISTRY.register(
            com.github.cecnull1.cecnull1_cforge.core.data.ResourceLocation(MODID, rlclass<T>()),
            build
        )
        build
    }
}

object UserLatex : LatexType() {
    override fun getLootTable(): ResourceLocation = BuiltInLootTables.EMPTY
    override fun isFriendlyTo(otherType: LatexType?): Boolean = super.isFriendlyTo(otherType) || otherType == this

    override fun getRenderPropertiesInternal(): Any = ChangedLatexTypes.WHITE_LATEX.get().renderPropertiesInternal
}

abstract class UserEntity(type: EntityType<out ChangedEntity>, level: Level) : ChangedEntity(type, level) {

    override fun getTransfurMode(): TransfurMode = TransfurMode.REPLICATION
    override fun getLatexType(): LatexType {
        return USER_LATEX.get()
    }

    override fun getSelfVariant(): TransfurVariant<*>? {
        return LicensedCharacterInit.VARIANT_REGISTRY[
            com.github.cecnull1.cecnull1_cforge.core.data.ResourceLocation(MODID, this::class.toRLString())
        ] ?: super.selfVariant
    }

    override fun getTransfurColor(cause: TransfurCause?): Color3 {
        val x = Color(63, 192, 210)
        return Color3(x.red/255f, x.green/255f, x.blue/255f)
    }

    override fun setAttributes(attributes: AttributeMap?) {
        super.setAttributes(attributes)
        AttributePresets.playerLike(attributes)
    }
}

@Licensed(
    name = "Frz Tian Xia No.1 Miao Sha",
    author = "FRZ天下NO.1(喵鲨）",
    id = ID(1058356685uL)
)
open class FrezoMS(type: EntityType<out ChangedEntity>, level: Level): UserEntity(type, level)

@Licensed(
    name = "Tian Ling",
    author = "天凌/帅气小彪彪",
    id = ID(342547260uL)
)
open class TianLing(type: EntityType<out ChangedEntity>, level: Level): UserEntity(type, level)

@Licensed(
    name = "Lnvincible",
    author = """
        unknown.
        by {"SKIN":{"url":"https://textures.minecraft.net/texture/743b1bb1fb7fe7c1e488bca733994c7c4c28c2d6cc4b3734c406223fe4e7c1f1","metadata":{"model":"slim"}}}
    """,
    id =    ID(0x743b1bb1fb7fe7c1uL),
    idEx =  ID(0xe488bca733994c7cuL),
    idEx2 = ID(0x4c28c2d6cc4b3734uL),
    idEx3 = ID(0xc406223fe4e7c1f1uL)
)
open class Lnvincible(type: EntityType<out ChangedEntity>, level: Level): UserEntity(type, level)

/*
* 实验性实体
* */
@Licensed(
    name = "Special",
    author = "cecnull1 / all",
    comments = "Common Human Entity",
    id = ID(0xFFFFFFFFFFFFFFFuL)
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

    override fun onReplicateOther(other: IAbstractChangedEntity) {
        super.onReplicateOther(other)
    }

    companion object {
        var SRL: EntityDataAccessor<String> = SynchedEntityData.defineId(Special::class.java, EntityDataSerializers.STRING)
        var SpecialIsAlex: EntityDataAccessor<Boolean> = SynchedEntityData.defineId(Special::class.java, EntityDataSerializers.BOOLEAN)
        var SpecialIsPlayerSkin: EntityDataAccessor<Boolean> = SynchedEntityData.defineId(Special::class.java, EntityDataSerializers.BOOLEAN)
    }
}