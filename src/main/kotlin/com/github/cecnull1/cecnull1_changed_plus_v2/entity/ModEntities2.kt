package com.github.cecnull1.cecnull1_changed_plus_v2.entity

import com.github.cecnull1.cecnull1_cforge.core.PipeCtrl.orElseProcess
import com.github.cecnull1.cecnull1_cforge.core.PipeCtrl.then
import com.github.cecnull1.cecnull1_changed_plus_v2.block.BBlockEntity
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.fieldIsJumping
import com.github.cecnull1.cecnull1lib.utils.vector.getKVec3
import com.github.cecnull1.cecnull1lib.utils.vector.putKVec3
import com.github.cecnull1.cecnull1lib.utils.vector.toKVec3
import com.github.cecnull1.cecnull1lib.utils.vector.toVec3
import net.ltxprogrammer.changed.entity.SeatEntity
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.entity.MoverType
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject
import org.joml.Vector3f

object ModEntities2 {
    val REGISTER: DeferredRegister<EntityType<*>> = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID)
    val MOVE_ENTITY: RegistryObject<EntityType<MoveEntity>> = REGISTER.register("move_entity") {
        EntityType.Builder.of(::MoveEntity, MobCategory.MISC).build("move_entity")
    }
}

class MoveEntity(entityType: EntityType<out SeatEntity>, level: Level) : SeatEntity(entityType, level) {
    var offsetP: Vector3f
        get() = this.entityData.get(OFFSET_P)
        set(value) {
            this.entityData.set(OFFSET_P, value)
        }

    override fun tick() {
        super.tick()
        level().getBlockEntity(attachedBlockPos) as? BBlockEntity then {
            this.entityHolder as? MoveEntity then moveEntity@ {
                this@then.seatedEntity then {
                    val yyaPlayer = if (this is Player) {
                        (if (this.fieldIsJumping) 1f else 0f) -
                                (if (this.isShiftKeyDown) 1f else 0f)
                    } else {
                        this.yya   // 非玩家走原字段
                    }

                    val forward = this.lookAngle.toVector3f().mul(this.zza/4, 0.0f, this.zza/4)
                    val up      = Vector3f(0f, yyaPlayer/4, 0f)
                    val right   = this.lookAngle.yRot(Math.PI.toFloat() * 0.5f)   // +90°
                        .toVector3f().mul(this.xxa/4, 0.0f, this.xxa/4)

                    this@moveEntity.offsetP = this@moveEntity.offsetP
                        .add(forward)
                        .add(up)
                        .add(right)
                    this@moveEntity.offsetP = this@moveEntity.offsetP.div(1.25f)

                    this@MoveEntity.yRot = this.yRot
                    this@MoveEntity.yHeadRot = this.yHeadRot
                }
            } orElseProcess {
                this@then.entityHolder = this@MoveEntity
            }
        }

        // 1. 把偏移转成“速度”
        val dx = offsetP.x().toDouble()
        val dy = offsetP.y().toDouble()
        val dz = offsetP.z().toDouble()

        // 2. 用 move() 让网络层发“连续移动”包
        val old = noPhysics
        noPhysics=true
        move(MoverType.SELF, Vec3(dx, dy, dz))
        noPhysics=old

        /* 2. 强制标记“位置已变” → 下一 tick 必发包 */
        setPosRaw(position().x, position().y, position().z)   // 把 xo/yo/zo 推开
        hasImpulse = true                                     // 1.19+ 连续移动标记

        passengers.forEach {
            it.resetFallDistance()
        }
    }

    override fun remove(reason: RemovalReason) {
        val blockPos = attachedBlockPos
        if (!level().isLoaded(blockPos) && reason == RemovalReason.DISCARDED) {
            // 区块未加载，阻止移除
            return
        }
        super.remove(reason)
    }

    override fun defineSynchedData() {
        super.defineSynchedData()
        this.entityData.define(OFFSET_P, Vector3f(0f, 0f, 0f))
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

    companion object {
        val OFFSET_P: EntityDataAccessor<Vector3f> = SynchedEntityData.defineId(MoveEntity::class.java, EntityDataSerializers.VECTOR3)
    }
}

fun SeatEntity.toMoveEntity(): MoveEntity? {
    val moveEntity = ModEntities2.MOVE_ENTITY.get().create(level())
    moveEntity?.deserializeNBT(this.serializeNBT())
    if (moveEntity != null) {
        level().addFreshEntity(moveEntity)
    }
    this.discard()
    return moveEntity
}