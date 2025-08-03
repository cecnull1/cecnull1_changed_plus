package com.github.cecnull1.cecnull1_changed_plus_v2.entity

import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.NBTKeys.IS_HA
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.NBTKeys.PLAYER
import com.github.cecnull1.cecnull1_changed_plus_v2.item.ModItems
import com.github.cecnull1.cecnull1_changed_plus_v2.sendAbilitiesUpdate
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.IDismount
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.IFanJi
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.IMount
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.MPlayerExtendedData.Companion.haArmorItems
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.MPlayerExtendedData.Companion.haItem
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.MPlayerExtendedData.Companion.hasArmorHA
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.MPlayerExtendedData.Companion.hasHA
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.VariantTickPlusAble
import com.github.cecnull1.cecnull1lib.utils.MCreatorFunction.findNearestEntity
import com.github.cecnull1.cecnull1lib.utils.changed.TransfurContextUtils.toTransfurContext
import com.github.cecnull1.cecnull1lib.utils.changed.TransfurData
import com.github.cecnull1.cecnull1lib.utils.changed.ifPlayerNotTransfurred
import com.github.cecnull1.cecnull1lib.utils.changed.removePlayerTransfurVariant
import com.github.cecnull1.cecnull1lib.utils.changed.transfur
import com.github.cecnull1.cecnull1lib.utils.nbt.getModData
import com.github.cecnull1.cecnull1lib.utils.nbt.set
import com.github.cecnull1.cecnull1lib.utils.vector.div
import com.github.cecnull1.cecnull1lib.utils.vector.plus
import com.github.cecnull1.cecnull1lib.utils.vector.times
import net.ltxprogrammer.changed.entity.ChangedEntity
import net.ltxprogrammer.changed.entity.PowderSnowWalkable
import net.ltxprogrammer.changed.entity.TransfurCause
import net.ltxprogrammer.changed.entity.TransfurMode
import net.ltxprogrammer.changed.entity.beast.AquaticEntity
import net.ltxprogrammer.changed.entity.beast.DarkLatexEntity
import net.ltxprogrammer.changed.entity.beast.DarkLatexYufeng
import net.ltxprogrammer.changed.entity.beast.LatexHuman
import net.ltxprogrammer.changed.entity.latex.LatexType
import net.ltxprogrammer.changed.entity.robot.Exoskeleton
import net.ltxprogrammer.changed.entity.variant.TransfurVariant
import net.ltxprogrammer.changed.init.ChangedAccessorySlots
import net.ltxprogrammer.changed.init.ChangedAttributes
import net.ltxprogrammer.changed.init.ChangedLatexTypes
import net.ltxprogrammer.changed.init.ChangedMobCategories
import net.ltxprogrammer.changed.item.ClothingItem.CLOSED
import net.ltxprogrammer.changed.util.Color3
import net.ltxprogrammer.changed.util.ItemUtil
import net.minecraft.server.level.ServerPlayer
import net.minecraft.util.Mth
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.*
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeMap
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.animal.horse.Horse
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.vehicle.Boat
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import net.minecraftforge.common.ForgeMod
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject
import java.util.*
import kotlin.math.abs

object ModEntities {
    const val A_ENTITY_ID = "a_entity"
    const val CEXOSKELETON_ID = "cexoskeleton"
    const val ZOMBIE_ID = "zombie"
    const val A_HORSE_ID = "a_horse"
    const val SOUL_ID  = "soul"
    const val CPLAYER_ID = "cplayer"
    const val PURE_WHITE_LATEX_YUFENG_ID = "pure_white_latex_yufeng"
    const val NOT_CAN_DISMOUNT_BOAT_ID = "not_can_dismount_boat"
    const val NONE_ENTITY_ID = "none_entity"
    const val MISC_ID = "misc"
    const val PURE_WHITE_LATEX_YUFENG_BY_NCDBOAT_ID = "pure_white_latex_yufeng_by_ncdboat"

    val REGISTER: DeferredRegister<EntityType<*>> = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID)

    val A_ENTITY: RegistryObject<EntityType<AEntity>> = REGISTER.register(A_ENTITY_ID) {
        EntityType.Builder.of(::AEntity, ChangedMobCategories.CHANGED)
            .sized(0.7f, 1.93f)
            .build(A_ENTITY_ID)
    }

    val PURE_WHITE_LATEX_YUFENG : RegistryObject<EntityType<PureWhiteLatexYufeng>> = REGISTER.register(PURE_WHITE_LATEX_YUFENG_ID) {
        EntityType.Builder.of(::PureWhiteLatexYufeng, ChangedMobCategories.CHANGED)
            .sized(0.7f, 1.93f)
            .build(PURE_WHITE_LATEX_YUFENG_ID)
    }

    val CEXOSKELETON: RegistryObject<EntityType<CExoskeleton>> = REGISTER.register(CEXOSKELETON_ID) {
        EntityType.Builder.of(::CExoskeleton, MobCategory.MISC)
            .sized(0.7f, 1.93f)
            .build(CEXOSKELETON_ID)
    }

    val A_HORSE: RegistryObject<EntityType<AHorse>> = REGISTER.register(A_HORSE_ID) {
        EntityType.Builder.of(::AHorse, MobCategory.MISC)
            .build(A_HORSE_ID)
    }

    val SOUL: RegistryObject<EntityType<Soul>> = REGISTER.register(SOUL_ID) {
        EntityType.Builder.of(::Soul, ChangedMobCategories.CHANGED)
            .sized(0f, 0f)
            .build(SOUL_ID)
    }

    val MISC: RegistryObject<EntityType<Misc>> = REGISTER.register(MISC_ID) {
        EntityType.Builder.of(::Misc, MobCategory.MISC)
            .sized(0.7f, 1.93f)
            .build(MISC_ID)
    }

    val PURE_WHITE_LATEX_YUFENG_BY_NCDBOAT: RegistryObject<EntityType<PureWhiteLatexYufengByNCDBoat>> = REGISTER.register(PURE_WHITE_LATEX_YUFENG_BY_NCDBOAT_ID) {
        EntityType.Builder.of(::PureWhiteLatexYufengByNCDBoat, ChangedMobCategories.CHANGED)
            .sized(0.7f, 1.93f)
            .build(PURE_WHITE_LATEX_YUFENG_BY_NCDBOAT_ID)
    }

//    val C_PLAYER : RegistryObject<EntityType<CPlayer>> = REGISTER.register(CPLAYER_ID) {
//        EntityType.Builder.of(::CPlayer, MobCategory.MISC)
//            .sized(0.7f, 1.8f)
//            .build(CPLAYER_ID)
//    }

    val NOT_CAN_DISMOUNT_BOAT : RegistryObject<EntityType<NotCanDismountBoat>> = REGISTER.register(NOT_CAN_DISMOUNT_BOAT_ID) {
        EntityType.Builder.of(::NotCanDismountBoat, MobCategory.MISC)
            .sized(1.375F, 0.5625F)
            .build(NOT_CAN_DISMOUNT_BOAT_ID)
    }

    val NONE_ENTITY : RegistryObject<EntityType<NoneTransfurVariant>> = REGISTER.register(NONE_ENTITY_ID) {
        EntityType.Builder.of(::NoneTransfurVariant, MobCategory.MISC).build(NONE_ENTITY_ID)
    }

//    val ZOMBIE: RegistryObject<EntityType<Zombie>> = REGISTER.register(ZOMBIE_ID) {
//        EntityType.Builder.of(::Zombie, ChangedMobCategories.CHANGED)
//            .sized(0.6f, 1.93f)
//            .build(ZOMBIE_ID)
//    }
}



open class AEntity(type: EntityType<out DarkLatexYufeng>, level: Level?) : DarkLatexYufeng(type, level),
    DarkLatexEntity,
    PowderSnowWalkable,
    AquaticEntity,
    IFanJi {

    override fun getLatexType(): LatexType = ChangedLatexTypes.DARK_LATEX.get()
    override fun getTransfurMode() = TransfurMode.REPLICATION
    override fun getTransfurColor(cause: TransfurCause): Color3 = Color3.fromInt(0x3d3d3d)
    override fun isMaskless() = false
    override fun getTransfurVariant(): TransfurVariant<*>? = this.selfVariant

    override fun variantTick(level: Level?) {
        super.variantTick(level)
        aEntityTick()
    }

    override fun getOwnerUUID(): UUID? {
        return entityData.get(DATA_OWNERUUID_ID).orElse(null)
    }

    protected override fun setAttributes(attributes: AttributeMap) {
        super.setAttributes(attributes)
        attributes[Attributes.MOVEMENT_SPEED] = 1.5
        attributes[Attributes.MAX_HEALTH] = 40.0
        attributes[ForgeMod.SWIM_SPEED.get()] = 2.0
        attributes[Attributes.ATTACK_DAMAGE] = 20.0
        attributes[ChangedAttributes.TRANSFUR_DAMAGE.get()] = 20.0
    }
}

open class CExoskeleton(p_21368_: EntityType<out Exoskeleton>?, p_21369_: Level?) : Exoskeleton(p_21368_, p_21369_)

open class Zombie(type: EntityType<out ChangedEntity>, level: Level) : ChangedEntity(type, level) {
    override fun getLatexType(): LatexType {
        return ChangedLatexTypes.NONE.get()
    }

    override fun getTransfurMode(): TransfurMode? {
        return TransfurMode.REPLICATION
    }
}

open class AHorse(p_30689_: EntityType<out Horse>, p_30690_: Level) : Horse(p_30689_, p_30690_), IDismount,
    IMount {
    init {
        this.isTamed = true
        this.inventory.setItem(INV_SLOT_SADDLE, ItemStack(Items.SADDLE))
    }

    override fun isNoAi(): Boolean = true

    override fun tick() {
        if (this.isVehicle) {
            this.controllingPassenger?.apply {
                (this as? Player)?.apply {
                    this.ifPlayerNotTransfurred {
                        this.transfur(
                            TransfurData(
                                ModTransfurVariant.A_ENTITY_TRANSFUR_VARIANT.get(),
                                true,
                                TransfurCause.LATEX_SYRINGE_FLOOR.toTransfurContext()
                            )
                        )
                    }
                    this@AHorse.ownerUUID = this.uuid // getUUID()
                }
            }
        }

        super.tick()
    }

    override fun isSaddled(): Boolean = true
    override fun isSaddleable(): Boolean = true
    override fun canDismount(): Boolean = false
}

open class Soul(type: EntityType<out ChangedEntity>, level: Level) : ChangedEntity(type, level), VariantTickPlusAble {
    init {
        noPhysics = true
    }

    override fun getLatexType(): LatexType {
        return ChangedLatexTypes.NONE.get()
    }

    override fun getTransfurMode(): TransfurMode {
        return TransfurMode.NONE
    }

    override fun isNoAi(): Boolean {
        return true
    }

    override fun variantTick(level: Level?) {
        noPhysics = true
        super.variantTick(level)
        if (health.isNaN()) {
            health = Float.POSITIVE_INFINITY
        }
    }

    override fun playerVariantTick(player: Player, level: Level?) {
        player.noPhysics = true
        player.health = Float.POSITIVE_INFINITY
        player.foodData.foodLevel = 20
        player.foodData.setSaturation(5f)
        val abilities = player.abilities
        if (!abilities.flying) {
            abilities.flying = true
            player.onUpdateAbilities()
            if (player is ServerPlayer) player.sendAbilitiesUpdate()
        }
    }
}

open class PureWhiteLatexYufeng(type: EntityType<out AEntity>, level: Level?) : AEntity(type, level), VariantTickPlusAble, IFanJi {

    override fun getLatexType() = ChangedLatexTypes.WHITE_LATEX.get()
    override fun getTransfurMode(): TransfurMode = TransfurMode.REPLICATION
    override fun isNoAi(): Boolean = false
    override fun variantTick(level: Level?) {
        super.variantTick(level)
    }
}

open class CPlayer(p_19870_: EntityType<out LatexHuman>, p_19871_: Level) : LatexHuman(p_19870_, p_19871_) {
    override fun getTransfurMode(): TransfurMode? {
        return TransfurMode.NONE
    }

    override fun getLatexType(): LatexType {
        return ChangedLatexTypes.NONE.get()
    }
}

open class PureWhiteLatexYufengByNCDBoat(type: EntityType<out AEntity>, level: Level?) : PureWhiteLatexYufeng(type,
    level
)

open class NotCanDismountBoat(type: EntityType<out Boat>, level: Level): Boat(type, level), IDismount {
    companion object {
        const val YU_ZHI = 10f
    }

    override fun isOnFire(): Boolean {
        return false
    }

    override fun isInLava(): Boolean {
        return false
    }

    override fun isInWater(): Boolean {
        return true
    }

    override fun isUnderWater(): Boolean {
        return false
    }

    override fun getGroundFriction(): Float {
        return 0.8f
    }

    override fun canDismount(): Boolean {
        val passengers = this.passengers.asSequence()
        return passengers.filterIsInstance<Player>().let {
            it.forEach {
                it.ifPlayerNotTransfurred {
                    it.transfur(
                        transfurData = TransfurData(
                            variant = ModTransfurVariant.PURE_WHITE_LATEX_YUFENG_BY_NCDBOAT_TRANSFUR_VARIANT.get(),
                            keepConscious = true
                        )
                    )
                    notCanDismountBoatAddArmor(it)
                }
            }
            it.any {
                it.health <= YU_ZHI
            }
        }
    }

    override fun tick() {
        level().findNearestEntity(x, y, z, 2.0, Player::class.java)?.let {
                player ->
            if (player.health >= YU_ZHI) {
                player.vehicle ?: player.startRiding(this)

                this.deltaMovement = Vec3(
                    this.deltaMovement.x +  this.lookAngle.x/3/(abs(this.deltaMovement.x*8)+1),
                    this.deltaMovement.y.coerceAtLeast(0.0)+0.04,
                    this.deltaMovement.z + this.lookAngle.z/3/(abs(this.deltaMovement.z*8)+1)
                )
            } else {
                player.hasHA = false
            }
            this.persistentData[MODID] = this.getModData(MODID).also {
                    persistentData ->
                persistentData.putUUID(PLAYER, player.uuid)
            }
        }
        if (this.getModData(MODID).contains(PLAYER)) {
            val player = level().getPlayerByUUID(this.getModData(MODID).getUUID(PLAYER))
            if (player != null) {
                if (player.health >= YU_ZHI) {
                    player.startRiding(this)
                }
            }
        }
        super.tick()
    }
}

open class NoneTransfurVariant(type: EntityType<out ChangedEntity>, level: Level) : ChangedEntity(type, level),
    VariantTickPlusAble {
    init {
        this.remove(RemovalReason.DISCARDED)
    }
    override fun getLatexType(): LatexType {
        return ChangedLatexTypes.NONE.get()
    }
    override fun getTransfurMode(): TransfurMode? {
        return TransfurMode.NONE
    }
    override fun playerVariantTick(player: Player, level: Level?) {
        player.removePlayerTransfurVariant()

    }
}

open class Misc(type: EntityType<out ChangedEntity>, level: Level) : ChangedEntity(type, level) {
    override fun getTransfurMode(): TransfurMode? {
        return TransfurMode.REPLICATION
    }

    override fun getLatexType(): LatexType {
        return ChangedLatexTypes.NONE.get()
    }

    override fun registerGoals() {}
    override fun isNoAi() = false
}

fun ChangedEntity.aEntityTick() {
    val entity = maybeGetUnderlying()
    if (entity.isFallFlying) {
        entity.autoMove(48)
    }
    if (entity.isInWaterOrBubble) {
        if (entity.isSwimming) {
            entity.autoMove(32)
        } else {
            entity.autoMove(40)
        }
    }
    if (false
        || entity.isOnFire
        || entity.isInLava
        || entity.activeEffects.any {
            it.effect.let {
                false
                    || it == MobEffects.WITHER
                    || it == MobEffects.POISON
                    || it == MobEffects.HUNGER
                    || it == MobEffects.WEAKNESS
            }
        }) {
        if (entity is Player) {
            entity.startFallFlying()
        }
        entity.autoMove(12)
    }
    if (entity is Player) {
        if (entity.foodData.foodLevel <= 6) {
            entity.autoMove(32)
            entity.startFallFlying()
        }
    }
    applyTerminalVelocity(entity)
}

fun Entity.notCanDismountBoatAddArmor(player: Player) {
    ItemUtil.tryEquipAccessory(
        player,
        run {
            val item = ModItems.NOT_CAN_TAKE_OFF_LAB_COAT.get()
            val itemStack = ItemStack(item)
            item.setClothingState(itemStack, item.getClothingState(itemStack).setValue(CLOSED, true))
            itemStack.count = 1
            itemStack.tag = itemStack.orCreateTag.apply {
                this[IS_HA] = true
            }
            itemStack
        },
        ChangedAccessorySlots.FULL_BODY.get()
    )
    ItemUtil.tryEquipAccessory(
        player,
        ItemStack(ModItems.NOT_CAN_TAKE_OFF_WETSUIT.get()).apply {
            count = 1
            this.tag = orCreateTag.apply {
                this[IS_HA] = true
            }
        },
        ChangedAccessorySlots.BODY.get()
    )

    val modifierIdString = "${MODID}:${player.uuid}.riderIn(${this.uuid})"
    val uuid = UUID.nameUUIDFromBytes(modifierIdString.toByteArray(Charsets.US_ASCII)) // 将字符串转化为UUID
    player.hasHA = true
    player.haItem = ItemStack(Items.DIAMOND_SWORD).apply {
        addAttributeModifier(
            Attributes.ATTACK_DAMAGE,
            AttributeModifier(
                uuid,
                modifierIdString,
                15.0,
                AttributeModifier.Operation.ADDITION
            ),
            EquipmentSlot.MAINHAND
        )
        addAttributeModifier(
            Attributes.ATTACK_SPEED,
            AttributeModifier(
                uuid,
                modifierIdString,
                -1.6,
                AttributeModifier.Operation.ADDITION
            ),
            EquipmentSlot.MAINHAND
        )
    }

    fun ItemStack.addArmorAttributeModifiers(): ItemStack {
        val slot = when (val item = this.item) {
            is net.minecraft.world.item.ArmorItem -> item.type.slot // 通过原版盔甲系统获取槽位
            else -> EquipmentSlot.MAINHAND // 默认槽位（主手）
        }

        val modifierIdString = "${MODID}:${player.uuid}.riderIn(${this@notCanDismountBoatAddArmor.uuid}) item=$item slot=$slot"
        val uuid = UUID.nameUUIDFromBytes(modifierIdString.toByteArray(Charsets.US_ASCII))

        this.addAttributeModifier(
            Attributes.ARMOR,
            AttributeModifier(
                uuid,
                modifierIdString,
                10.0,
                AttributeModifier.Operation.ADDITION
            ),
            slot
        )
        this.addAttributeModifier(
            Attributes.ARMOR_TOUGHNESS,
            AttributeModifier(
                uuid,
                modifierIdString,
                10.0,
                AttributeModifier.Operation.ADDITION
            ),
            slot
        )

        return this
    }
    player.hasArmorHA = true
    player.haArmorItems = mutableMapOf<EquipmentSlot, ItemStack>(
        EquipmentSlot.HEAD to ItemStack(Items.DIAMOND_HELMET).addArmorAttributeModifiers(),
        EquipmentSlot.CHEST to ItemStack(Items.DIAMOND_CHESTPLATE).addArmorAttributeModifiers(),
        EquipmentSlot.LEGS to ItemStack(Items.DIAMOND_LEGGINGS).addArmorAttributeModifiers(),
        EquipmentSlot.FEET to ItemStack(Items.DIAMOND_BOOTS).addArmorAttributeModifiers()
    )
}

operator fun <T: Number> AttributeMap.set(attribute: Attribute, value: T) = this.getInstance(attribute)?.baseValue = value.toDouble()
operator fun AttributeMap.get(attribute: Attribute) = this.getInstance(attribute)?.baseValue ?: 0.0

/**
 * 有副作用函数：自动调整实体的移动增量（修改 deltaMovement）。
 * @param divSpeed 速度除数（越大，移动速度越慢）。
 */
inline fun <reified T: Number> Entity.autoMove(divSpeed: T, yEnabled: Boolean = true) {
    val doubleSpeed = divSpeed.toDouble() // 安全转换（所有 Number 子类型均支持）
    val entity = this
    entity.deltaMovement = entity.deltaMovement.funcAutoMove(
        rotation = entity.lookAngle,
        divSpeed = doubleSpeed,
        yEnabled = yEnabled
    )
}

/**
 * 无副作用纯函数：计算自动移动的位移向量（不修改实体状态）。
 * @param rotation 旋转角度向量（影响移动方向）。
 * @param divSpeed 基础速度（各轴使用此值）。
 * @param yEnabled 是否启用 Y 轴移动（`true` 保留 Y 轴速度，`false` 禁用）。
 */
fun Vec3.funcAutoMove(rotation: Vec3, divSpeed: Double, yEnabled: Boolean = true) = this + ((rotation * Vec3(1.0, yEnabled.compareTo(false).toDouble(), 1.0)) / Vec3(divSpeed, divSpeed, divSpeed))

fun applyTerminalVelocity(entity: LivingEntity) {
    val delta = entity.deltaMovement
    entity.deltaMovement = Vec3(delta.x, Mth.clamp(delta.y, -0.5, 0.5), delta.z)
    entity.resetFallDistance()
}

