package com.github.cecnull1.cecnull1_changed_plus.entity

import com.github.cecnull1.cecnull1_changed_plus.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus.utils.DismountingAble
import com.github.cecnull1.cecnull1_changed_plus.utils.MountAble
import com.github.cecnull1.cecnull1_changed_plus.utils.VariantTickPlusAble
import net.ltxprogrammer.changed.entity.*
import net.ltxprogrammer.changed.entity.beast.AquaticEntity
import net.ltxprogrammer.changed.entity.beast.DarkLatexEntity
import net.ltxprogrammer.changed.entity.beast.DarkLatexYufeng
import net.ltxprogrammer.changed.entity.robot.Exoskeleton
import net.ltxprogrammer.changed.entity.variant.TransfurVariant
import net.ltxprogrammer.changed.init.ChangedMobCategories
import net.ltxprogrammer.changed.process.ProcessTransfur
import net.ltxprogrammer.changed.util.Color3
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.util.Mth
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.entity.animal.horse.Horse
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject
import java.util.*
import kotlin.math.sin


object ModEntities {
    const val A_ENTITY_ID = "a_entity"
    const val CEXOSKELETON_ID = "cexoskeleton"
    const val ZOMBIE_ID = "zombie"
    const val A_HORSE_ID = "a_horse"
    const val SOUL_ID  = "soul"

    val REGISTER: DeferredRegister<EntityType<*>> = DeferredRegister.create(ForgeRegistries.ENTITIES, MODID)

    val A_ENTITY: RegistryObject<EntityType<AEntity>> = REGISTER.register(A_ENTITY_ID) {
        EntityType.Builder.of(::AEntity, ChangedMobCategories.CHANGED)
            .sized(0.7f, 1.93f)
            .build(A_ENTITY_ID)
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

//    val ZOMBIE: RegistryObject<EntityType<Zombie>> = REGISTER.register(ZOMBIE_ID) {
//        EntityType.Builder.of(::Zombie, ChangedMobCategories.CHANGED)
//            .sized(0.6f, 1.93f)
//            .build(ZOMBIE_ID)
//    }
}

open class AEntity(type: EntityType<out DarkLatexYufeng>?, level: Level?) : DarkLatexYufeng(type, level),
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
        val entity = maybeGetUnderlying()
        val delta = entity.deltaMovement
        val rotation = entity.lookAngle
        if (entity.isInWaterOrBubble) {
            entity.deltaMovement = Vec3(delta.x, delta.y.coerceAtMost(0.0), delta.z)
        }
        if (entity.isFallFlying) {
            entity.deltaMovement = Vec3(
                delta.x + sin(rotation.x) / 64,
                delta.y + sin(rotation.y) / 64,
                delta.z + sin(rotation.z) / 64
            )
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

open class CExoskeleton(p_21368_: EntityType<out Exoskeleton>?, p_21369_: Level?) : Exoskeleton(p_21368_, p_21369_) {
    override fun playerTouch(p_20081_: Player) {
        super.playerTouch(p_20081_)
        if (this.vehicle == null) {
            this.startRiding(p_20081_)
        }
    }
}

open class Zombie(type: EntityType<out ChangedEntity>, level: Level) : ChangedEntity(type, level) {
    override fun getLatexType(): LatexType? {
        return LatexType.NEUTRAL
    }

    override fun getTransfurMode(): TransfurMode? {
        return TransfurMode.REPLICATION
    }
}

open class AHorse(p_30689_: EntityType<out Horse>, p_30690_: Level) : Horse(p_30689_, p_30690_), DismountingAble, MountAble {
    init {
        this.isTamed = true
        this.inventory.setItem(INV_SLOT_SADDLE, ItemStack(Items.SADDLE))
    }

    override fun isNoAi(): Boolean {
        return true
    }

    override fun tick() {
        if (this.isVehicle) {
            this.controllingPassenger?.apply {
                (this as? Player)?.apply {
                    if(!ProcessTransfur.isPlayerTransfurred(this)) {
                        ProcessTransfur.transfur(
                            this,
                            this.level,
                            ModTransfurVariant.A_ENTITY_TRANSFUR_VARIANT.get(),
                            true,
                            TransfurContext.hazard(TransfurCause.LATEX_SYRINGE_FLOOR)
                        )
                    }
                    this@AHorse.ownerUUID = this.uuid // getUUID()
                }
            }
        }

        super.tick()
    }

    override fun isSaddled(): Boolean {
        return true
    }

    override fun isSaddleable(): Boolean {
        return true
    }

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
            if (player is ServerPlayer) {
                val abilitiesPacket = ClientboundPlayerAbilitiesPacket(abilities)
                player.connection.send(abilitiesPacket)
            }
        }
    }
}