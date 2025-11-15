package com.github.cecnull1.cecnull1_changed_plus_v2.entity

import com.github.cecnull1.cecnull1_cforge.core.PipeCore.mutate
import com.github.cecnull1.cecnull1_cforge.core.PipeCore.process
import com.github.cecnull1.cecnull1_cforge.core.PipeCtrl.orElseProcess
import com.github.cecnull1.cecnull1_cforge.core.PipeCtrl.pipeIf
import com.github.cecnull1.cecnull1_cforge.core.PipeCtrl.pipeUnless
import com.github.cecnull1.cecnull1_cforge.core.PipeCtrl.then
import com.github.cecnull1.cecnull1_changed_plus_v2.block.BBlockEntity
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.IDismount
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.MountType
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.fieldIsJumping
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.lerpedTowards
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.toHorizontalViewVec
import com.github.cecnull1.cecnull1lib.utils.changed.TransfurData
import com.github.cecnull1.cecnull1lib.utils.changed.transfur
import com.github.cecnull1.cecnull1lib.utils.vector.getKVec3
import com.github.cecnull1.cecnull1lib.utils.vector.putKVec3
import com.github.cecnull1.cecnull1lib.utils.vector.toKVec3
import com.github.cecnull1.cecnull1lib.utils.vector.toVec3
import net.ltxprogrammer.changed.entity.PowderSnowWalkable
import net.ltxprogrammer.changed.entity.SeatEntity
import net.ltxprogrammer.changed.entity.beast.AquaticEntity
import net.ltxprogrammer.changed.entity.beast.LatexOrca
import net.ltxprogrammer.changed.init.ChangedAttributes
import net.ltxprogrammer.changed.init.ChangedMobCategories
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.entity.MoverType
import net.minecraft.world.entity.ai.attributes.AttributeMap
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.animal.horse.Horse
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import net.minecraftforge.common.ForgeMod
import net.minecraftforge.common.capabilities.CapabilityProvider
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject
import org.joml.Vector3f
import java.util.*
import kotlin.jvm.optionals.getOrNull

object ModEntities2 {
    val REGISTER: DeferredRegister<EntityType<*>> = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID)
    val MOVE_ENTITY: RegistryObject<EntityType<BBlockMoveEntity>> = REGISTER.register("move_entity") {
        EntityType.Builder.of(::BBlockMoveEntity, MobCategory.MISC).sized(0f, 0.5f).build("move_entity")
    }
    val MEI_XI_YUAN: RegistryObject<EntityType<MeiXiYuan>> = REGISTER.register("mei_xi_yuan") {
        EntityType.Builder.of(::MeiXiYuan, ChangedMobCategories.CHANGED).sized(.7f, 1.73f).build("mei_xi_yuan")
    }
    val B_HORSE: RegistryObject<EntityType<BHorse>> = REGISTER.register("b_horse") {
        EntityType.Builder.of(::BHorse, MobCategory.AMBIENT).sized(
            EntityType.HORSE.width,
            EntityType.HORSE.height
        ).build("b_horse")
    }
}

class BBlockMoveEntity(entityType: EntityType<out SeatEntity>, level: Level) : SeatEntity(entityType, level), IDismount {
    var offsetP: Vector3f
        get() = this.entityData[OFFSET_P]
        set(value) {
            this.entityData[OFFSET_P] = value
        }

    var xyza: Vector3f
        get() = this.entityData[XYZA]
        set(value) {
            this.entityData[XYZA] = value
        }


    override fun tick() {
        super.tick()
        level().getBlockEntity(attachedBlockPos) as? BBlockEntity then {
            this.entityHolder as? BBlockMoveEntity then moveEntity@ {
                this@then.seatedEntity then {
                    val yyaPlayer = if (this is Player) {
                        (if (this.fieldIsJumping) 1f else 0f) -
                                (if (this.isShiftKeyDown) 1f else 0f)
                    } else {
                        this.yya   // 非玩家走原字段
                    }

                    val speed = 32f
                    val forward = this.yRot.toHorizontalViewVec().toVector3f().mul(zza/speed, 0.0f, zza/speed)
                    val up      = Vector3f(0f, yyaPlayer/8-0.0625f, 0f)
                    val right   = this.yRot.toHorizontalViewVec().yRot(Math.PI.toFloat() * 0.5f)   // +90°
                        .toVector3f().mul(this.xxa/speed, 0.0f, this.xxa/speed)

                    this@moveEntity.offsetP = this@moveEntity.offsetP
                        .add(forward)
                        .add(up)
                        .add(right)
                        .div(1.0625f)

//                    this@moveEntity.offsetP = this.deltaMovement.toVector3f().add(up)

                    this.setDeltaMovement(
                        this@moveEntity.offsetP.x.toDouble(),
                        this@moveEntity.offsetP.y.toDouble(),
                        this@moveEntity.offsetP.z.toDouble()
                    )

                    this@BBlockMoveEntity.xyza = Vector3f(
                        this.xxa,
                        this.yya,
                        this.zza
                    )
                }
            } orElseProcess {
                this@then.entityHolder = this@BBlockMoveEntity
            }
        }
        val oldPos = y
        this.vehicle then {
            this.setDeltaMovement(
                this@BBlockMoveEntity.offsetP.x.toDouble(),
                this@BBlockMoveEntity.offsetP.y.toDouble(),
                this@BBlockMoveEntity.offsetP.z.toDouble()
            )
        } orElseProcess {
            // 1. 把偏移转成“速度”
            val dx = offsetP.x().toDouble()
            val dy = offsetP.y().toDouble()
            val dz = offsetP.z().toDouble()


            // 2. 用 move() 让网络层发“连续移动”包
            move(MoverType.SELF, Vec3(dx, dy, dz))

        }
        if (oldPos == y) offsetP.y = 0.0f
        /* 2. 强制标记“位置已变” → 下一 tick 必发包 */
        setPosRaw(position().x, position().y, position().z)   // 把 xo/yo/zo 推开
        hasImpulse = true                                     // 1.19+ 连续移动标记

        passengers.forEach {
            it.resetFallDistance()
        }
    }

    override fun remove(reason: RemovalReason) = Unit process {
        val blockPos = attachedBlockPos
        pipeIf(!level().isLoaded(blockPos) && reason == RemovalReason.DISCARDED) then {
            // 区块未加载，阻止移除
            return
        }
        super.remove(reason)
    }

    override fun onBelowWorld() {
        setPos(x, y+320, z)
        level().getBlockEntity(attachedBlockPos) as? BBlockEntity then {
            seatedEntity then {
                this.transfur(
                    TransfurData(
                        variant = ModTransfurVariant.PURE_WHITE_LATEX_YUFENG_AND_ARMOR_TRANSFUR_VARIANT.get(),
                        keepConscious = true
                    )
                )
            }
        }
    }

    override fun defineSynchedData() {
        super.defineSynchedData()
        this.entityData.define(OFFSET_P, Vector3f(0f, 0f, 0f))
        this.entityData.define(XYZA, Vector3f(0f, 0f, 0f))
    }

    override fun addAdditionalSaveData(compound: CompoundTag) {
        super.addAdditionalSaveData(compound)
        compound.putKVec3("Off", Vec3(offsetP.x.toDouble(), offsetP.y.toDouble(), offsetP.z.toDouble()).toKVec3())      // 存偏移
        compound.putKVec3("Real", position().toKVec3())  // 存真实位置
    }

    override fun readAdditionalSaveData(compound: CompoundTag) {
        super.readAdditionalSaveData(compound)
        offsetP = compound.getKVec3("Off").toVec3().toVector3f() // 读回偏移
        setPos(compound.getKVec3("Real").toVec3())
        setPosRaw(position().x, position().y, position().z) // 还原位置
        hasImpulse = true
    }

    override fun canDismount(mountType: MountType): Boolean {
        return when (mountType) {
            is MountType.Dismount -> {
                entityData[SEATED_CAN_DISMOUNT]
            }
            is MountType.Mount -> true
            is MountType.Move -> {
                this.startRiding(mountType.target)
                entityData[SEATED_CAN_DISMOUNT]
            }
            else -> true
        }
    }

    companion object {
        val OFFSET_P: EntityDataAccessor<Vector3f> = SynchedEntityData.defineId(BBlockMoveEntity::class.java, EntityDataSerializers.VECTOR3)
        val XYZA: EntityDataAccessor<Vector3f> = SynchedEntityData.defineId(BBlockMoveEntity::class.java, EntityDataSerializers.VECTOR3)
    }
}

fun SeatEntity.toBBlockMoveEntity(): BBlockMoveEntity? {
    if (this is BBlockMoveEntity) return this
    val moveEntity = ModEntities2.MOVE_ENTITY.get().create(level())
    moveEntity?.deserializeNBT(this.serializeNBT())
    if (moveEntity != null) {
        level().addFreshEntity(moveEntity)
    }
    this.discard()
    return moveEntity
}

open class MeiXiYuan(entityType: EntityType<out LatexOrca>, level: Level) : LatexOrca(entityType, level),
    PowderSnowWalkable, AquaticEntity {
    override fun setAttributes(attributes: AttributeMap) = super.setAttributes(attributes) mutate {
        attributes[Attributes.MAX_HEALTH] += 10
        attributes[Attributes.ARMOR] += 10
        attributes[Attributes.ARMOR_TOUGHNESS] += 1
        attributes[Attributes.MOVEMENT_SPEED] -= 0.1
        attributes[ForgeMod.SWIM_SPEED.get()] += 0.1
        attributes[ChangedAttributes.TRANSFUR_DAMAGE.get()] = 3
    }

    override fun variantTick(level: Level) = super.variantTick(level) process {
        maybeGetUnderlying() then self@ {
            pipeIf (this.isInWaterOrBubble && this.random.nextInt(50) == 0) then {
                this.heal(1f)
                if (this is Player) {
                    foodData.foodLevel++
                    foodData.setExhaustion(0f)
                    foodData.setSaturation(foodData.saturationLevel+1)
                }
            }
            val v = 0.5
            pipeUnless (this.isInWaterOrBubble) then {
                if (this.health.toInt() > 15) {
                    this.hurt(this.level().damageSources().drown(), health-15)
                }
                deltaMovement = deltaMovement.lerpedTowards(
                    direction = yRot.toHorizontalViewVec(),
                    influence = v,
                    applyToY = false  // 关键：不改变 Y 分量
                )
            } orElseProcess {
                deltaMovement = deltaMovement.lerpedTowards(
                    direction = lookAngle,
                    influence = v,
                    applyToY = true
                )
            }
            pipeIf(!hasEffect(MobEffects.DOLPHINS_GRACE)) then {
                addEffect(MobEffects.DOLPHINS_GRACE.new(
                    duration = MobEffectInstance.INFINITE_DURATION,
                    ambient = true,
                    visible = false
                ))
            }
        }
    }
}

open class BHorse(entityType: EntityType<out Horse>, level: Level) : Horse(entityType, level), IDismount {
    override fun isTamed(): Boolean {
        return true
    }

    override fun isSaddleable(): Boolean {
        return true
    }

    override fun isSaddled(): Boolean {
        return true
    }

    override fun canDismount(mountType: MountType): Boolean {
        return when(mountType) {
            is MountType.PlayerSelfDismount -> {
                false
            }
            else -> true
        }
    }
}

fun newEffectInstance(
    mobEffect: MobEffect,
    duration: Int = 400,
    amplifier: Int = 0,
    ambient: Boolean = false,
    visible: Boolean = true,
    showIcon: Boolean = visible,
    hiddenEffect: MobEffectInstance? = null,
    factorData: MobEffectInstance.FactorData? = mobEffect.createFactorData().getOrNull()
): MobEffectInstance = MobEffectInstance(mobEffect, duration, amplifier, ambient, visible, showIcon, hiddenEffect,
    Optional.ofNullable(factorData))

fun MobEffect.new(
    duration: Int = 400,
    amplifier: Int = 0,
    ambient: Boolean = false,
    visible: Boolean = true,
    showIcon: Boolean = visible,
    hiddenEffect: MobEffectInstance? = null,
    factorData: MobEffectInstance.FactorData? = this.createFactorData().getOrNull()
) = newEffectInstance(this, duration, amplifier, ambient, visible, showIcon, hiddenEffect, factorData)