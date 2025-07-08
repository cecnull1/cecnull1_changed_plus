package com.github.cecnull1.cecnull1_changed_plus_v2.entity

import com.github.cecnull1.cecnull1_changed_plus_v2.capability.haEnabled
import com.github.cecnull1.cecnull1_changed_plus_v2.capability.haItem
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.NBTKeys.PLAYER
import com.github.cecnull1.cecnull1_changed_plus_v2.sendAbilitiesUpdate
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.*
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.TransfurContextUtils.toTransfurContext
import com.github.cecnull1.cecnull1lib.utils.MCreatorFunction.findNearestEntity
import com.github.cecnull1.cecnull1lib.utils.nbt.getModData
import com.github.cecnull1.cecnull1lib.utils.nbt.set
import net.ltxprogrammer.changed.entity.*
import net.ltxprogrammer.changed.entity.beast.AquaticEntity
import net.ltxprogrammer.changed.entity.beast.DarkLatexEntity
import net.ltxprogrammer.changed.entity.beast.DarkLatexYufeng
import net.ltxprogrammer.changed.entity.beast.LatexHuman
import net.ltxprogrammer.changed.entity.robot.Exoskeleton
import net.ltxprogrammer.changed.entity.variant.TransfurVariant
import net.ltxprogrammer.changed.init.ChangedAccessorySlots
import net.ltxprogrammer.changed.init.ChangedItems
import net.ltxprogrammer.changed.init.ChangedMobCategories
import net.ltxprogrammer.changed.util.Color3
import net.ltxprogrammer.changed.util.ItemUtil
import net.minecraft.server.level.ServerPlayer
import net.minecraft.util.Mth
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.animal.horse.Horse
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.vehicle.Boat
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject
import java.util.*
import kotlin.math.abs
import kotlin.math.sin

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
    AquaticEntity {

    override fun getLatexType() = LatexType.DARK_LATEX
    override fun getTransfurMode() = TransfurMode.REPLICATION
    override fun getTransfurColor(cause: TransfurCause?) = Color3.fromInt(0x3d3d3d)!!
    override fun isMaskless() = false
    override fun getTransfurVariant(): TransfurVariant<*>? = this.selfVariant

    override fun variantTick(level: Level?) {
        super.variantTick(level)
        flyAndInWaterLogic()
    }

    override fun getOwnerUUID(): UUID? {
        return entityData.get(DATA_OWNERUUID_ID).orElse(null)
    }
}

open class CExoskeleton(p_21368_: EntityType<out Exoskeleton>?, p_21369_: Level?) : Exoskeleton(p_21368_, p_21369_)

open class Zombie(type: EntityType<out ChangedEntity>, level: Level) : ChangedEntity(type, level) {
    override fun getLatexType(): LatexType? {
        return LatexType.NEUTRAL
    }

    override fun getTransfurMode(): TransfurMode? {
        return TransfurMode.REPLICATION
    }
}

open class AHorse(p_30689_: EntityType<out Horse>, p_30690_: Level) : Horse(p_30689_, p_30690_), DismountAble,
    MountAble {
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
        return LatexType.NEUTRAL
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

open class PureWhiteLatexYufeng(type: EntityType<out AEntity>, level: Level?) : AEntity(type, level) {
    override fun getLatexType(): LatexType = LatexType.WHITE_LATEX
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

    override fun getLatexType(): LatexType? {
        return LatexType.NEUTRAL
    }
}

open class NotCanDismountBoat(type: EntityType<out Boat>, level: Level): Boat(type, level), DismountAble {
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
                    transfurAndAddArmor(it)
                }
            }
            it.any {
                it.health <= YU_ZHI
            }
        }
    }

    private fun transfurAndAddArmor(player: Player) {
        player.transfur(
            transfurData = TransfurData(
                variant = ModTransfurVariant.PURE_WHITE_LATEX_YUFENG_TRANSFUR_VARIANT.get(),
                keepConscious = true
            )
        )
        ItemUtil.tryEquipAccessory(
            player,
            ItemStack(ChangedItems.LAB_COAT.get()).apply {
                enchant(Enchantments.BINDING_CURSE, 1)
                enchant(Enchantments.VANISHING_CURSE, 1)
                count = 1
            },
            ChangedAccessorySlots.FULL_BODY.get()
        )
        ItemUtil.tryEquipAccessory(
            player,
            ItemStack(ChangedItems.BLACK_TSHIRT.get()).apply {
                enchant(Enchantments.BINDING_CURSE, 1)
                enchant(Enchantments.VANISHING_CURSE, 1)
                count = 1
            },
            ChangedAccessorySlots.BODY.get()
        )
    }

    override fun tick() {
        level().findNearestEntity(x, y, z, 2.0, Player::class.java)?.let {
                player ->
            if (player.health >= YU_ZHI) {
                player.startRiding(this)
                player.haEnabled = true
                player.haItem = ItemStack(Items.DIAMOND_SWORD).apply {
                    val modifierIdString = "${MODID}:${player.uuid}.riderIn[${this@NotCanDismountBoat.uuid}]"
                    val uuid = UUID.nameUUIDFromBytes(modifierIdString.toByteArray(Charsets.US_ASCII)) // 将字符串转化为UUID
                    addAttributeModifier(
                        Attributes.ATTACK_DAMAGE,
                        AttributeModifier(
                            uuid,
                            modifierIdString,
                            5.0,
                            AttributeModifier.Operation.ADDITION
                        ),
                        EquipmentSlot.MAINHAND
                    )
                }
                this.deltaMovement = Vec3(
                    this.deltaMovement.x + sin( this.lookAngle.x)/3/(abs(this.deltaMovement.x*8)+1),
                    this.deltaMovement.y.coerceAtLeast(0.0)+0.04,
                    this.deltaMovement.z + sin( this.lookAngle.z)/3/(abs(this.deltaMovement.z*8)+1)
                )
            } else {
                player.haEnabled = false
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
    override fun getLatexType(): LatexType? {
        return LatexType.NEUTRAL
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

    override fun getLatexType(): LatexType? {
        return LatexType.NEUTRAL
    }
}

fun ChangedEntity.flyAndInWaterLogic(speed: Double = 64.0) {
    val entity = maybeGetUnderlying()
    val delta = entity.deltaMovement
    val rotation = entity.lookAngle
    if (entity.isInWaterOrBubble) {
        entity.deltaMovement = Vec3(delta.x, delta.y.coerceAtMost(0.0), delta.z)
    }
    if (entity.isFallFlying) {
        val fixSpeed =
            if (speed == 0.0 || speed.isNaN()) Double.POSITIVE_INFINITY /*返回无穷大以停止推进*/
            else speed
        entity.deltaMovement = Vec3(
            delta.x + sin(rotation.x) / fixSpeed,
            delta.y + sin(rotation.y) / fixSpeed,
            delta.z + sin(rotation.z) / fixSpeed
        )
    }
    applyTerminalVelocity(entity)
}

fun applyTerminalVelocity(entity: LivingEntity) {
    val delta = entity.deltaMovement
    entity.deltaMovement = Vec3(delta.x, Mth.clamp(delta.y, -0.5, 0.5), delta.z)
    entity.resetFallDistance()
}