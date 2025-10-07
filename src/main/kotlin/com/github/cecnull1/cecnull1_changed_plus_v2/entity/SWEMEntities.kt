package com.github.cecnull1.cecnull1_changed_plus_v2.entity

import com.alaharranhonor.swem.entities.horse.SWEMHorseEntity
import com.alaharranhonor.swem.entities.horse.SWEMHorseEntityBase
import com.alaharranhonor.swem.entities.horse.behaviors.IBehavior
import com.alaharranhonor.swem.entities.horse.behaviors.impl.FlightBehavior
import com.alaharranhonor.swem.items.TackType
import com.alaharranhonor.swem.items.TackType.*
import com.alaharranhonor.swem.registry.EntitySetup
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.MODID
import com.github.cecnull1.cecnull1_changed_plus_v2.utils.IDismount
import com.github.cecnull1.cecnull1lib.utils.MCreatorFunction.findNearestEntity
import com.github.cecnull1.cecnull1lib.utils.nbt.set
import com.github.cecnull1.cecnull1lib.utils.vector.KVec3
import com.github.cecnull1.cecnull1lib.utils.vector.toKVec3
import com.github.cecnull1.cecnull1lib.utils.vector.toVec3
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject
import kotlin.math.sqrt


object SWEMEntities {
    val REGISTER: DeferredRegister<EntityType<*>> by lazy {
        DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID)
    }
    val O_ENTITY: RegistryObject<EntityType<O>> = REGISTER.register("o") {
        val e = EntitySetup.SWEM_HORSE_ENTITY.get()
        EntityType.Builder.of(::O, e.category)
            .sized(e.width, e.height)
            .build("o")
    }
}

open class O(type: EntityType<out O>,
             worldIn: Level
) : SWEMHorseEntity(type, worldIn), IDismount {
    var owner2: LivingEntity? = null

    override fun addAdditionalSaveData(tag: CompoundTag) {
        super.addAdditionalSaveData(tag)
        tag["BridleItem"] = ItemStack.EMPTY.serializeNBT()
        tag["SWEMArmorItem"] = ItemStack.EMPTY.serializeNBT()
        tag["SaddleItem"] = ItemStack.EMPTY.serializeNBT()
    }

    override fun getTack(type: TackType?): ItemStack? {
        return when(type) {
            HORSE_ARMOR -> ItemStack(com.alaharranhonor.swem.registry.ItemSetup.NETHERITE_HORSE_ARMOR.get())
            BRIDLE -> ItemStack(com.alaharranhonor.swem.registry.ItemSetup.ADVENTURE_BRIDLE.get())
            SADDLE -> ItemStack(com.alaharranhonor.swem.registry.ItemSetup.ADVENTURE_SADDLE_WITHERED.get())
            else -> ItemStack.EMPTY
        }
    }

    override fun canDismount(): Boolean {
        return false
    }

    override fun isTamed(): Boolean {
        return true
    }

    override fun getOwner(): LivingEntity? {
        if (rootVehicle is LivingEntity && owner2 == null) owner2 = rootVehicle as LivingEntity?
        if (this === owner2) owner2 = null
        return owner2
    }

    override fun getBridle(): ItemStack? {
        return ItemStack(com.alaharranhonor.swem.registry.ItemSetup.ADVENTURE_BRIDLE.get())
    }

    override fun getArmor(): ItemStack? {
        return ItemStack(com.alaharranhonor.swem.registry.ItemSetup.NETHERITE_HORSE_ARMOR.get())
    }

    override fun getSaddle(): ItemStack? {
        return ItemStack(com.alaharranhonor.swem.registry.ItemSetup.ADVENTURE_SADDLE_WITHERED.get())
    }

    override fun getGait(): Gait? {
        return if (!isFlying) Gait.GALLOP else super.getGait()
    }

    override fun tick() {
        if (canFly()) startFlying()
        super.tick()
        level().findNearestEntity(
            vec3 = position(),
            length = 10.0,
            clazz = Player::class.java
        )?.let {
            // 当前实体(A)是 "this"
            val entityA = this

            // === 获取位置和速度向量 ===
            val posA = entityA.position()
            val posB = it.position()
            val velocityB = it.deltaMovement

            // === 计算位置差和距离 ===
            val deltaPos = posA.subtract(posB)
            val sqDistance = deltaPos.lengthSqr()
            val distance = sqrt(sqDistance)

            // === 检查执行距离条件 ===
            if (distance <= 0.5) { // 魔法数字1.0: 触发距离
                execute(entityA, it)
                return@let // 结束牵引处理
            }

            // === 计算方向单位向量 ===
            val direction = deltaPos.normalize()

            // === 魔法数字配置 (直接内联) ===
            val springConstant = 1  // 弹性系数 (拉力强度)
            val dampingFactor = 0.2    // 阻尼系数 (运动平滑度)
            val equilibriumDist = 0.5  // 平衡距离 (执行距离)
            val mass = 1.0             // 实体质量 (简化)
            val tickTime = 0.05         // tick时间 (1/20秒)
            val maxEffectDist = 10.0     // 最大作用距离 (与findNearestEntity一致)

            // 超过最大距离则不处理
            if (distance > maxEffectDist) return@let

            // === 计算牵引力 ===
            // 胡克定律：F = k * (d - d0)
            val forceMagnitude = springConstant * (distance - equilibriumDist)
            val springForce = direction.scale(forceMagnitude)

            // 阻尼力：与速度方向相反
            val dampingForce = velocityB.scale(-dampingFactor)

            // 合力
            val totalForce = springForce.add(dampingForce)

            // === 计算加速度 ===
            val acceleration = totalForce.scale(1.0 / mass)

            // === 增量更新速度 ===
            val deltaV = acceleration.scale(tickTime)
            it.deltaMovement = velocityB.add(deltaV)
        }
        resetFallDistance()
    }

    private fun execute(entityA: LivingEntity, entityB: LivingEntity) {
        entityB.startRiding(entityA)
    }

    override fun canMove(): Boolean {
        return true
    }

    override fun getRiddenInput(player: Player, vec3: Vec3): Vec3 {
        return (
                super.getRiddenInput(player, vec3).toKVec3() + KVec3(0.0, 0.0, 1.0-player.zza)
                ).toVec3()
    }

    override fun <T : IBehavior?> getBehavior(clazz: Class<T?>?): T? {
        return if (clazz != FlightBehavior::class.java) super.getBehavior(clazz) else {
            OF(this,
                canFly(),
                super.getBehavior(clazz).speedXZ+0.75f,
                super.getBehavior(clazz).speedY+0.05f,
                super.getBehavior(clazz).rotation,
                super.getBehavior(clazz).accelerationState,
                super.getBehavior(clazz).turnState) as? T
        }
    }

    override fun isStanding(): Boolean {
        return false
    }

    override fun decrementSpeed() {
    }

    override fun isFlying(): Boolean {
        return !super.onGround()
    }

    override fun canFly(): Boolean {
        return !super.onGround()
    }
}

class OF(
    horse: SWEMHorseEntityBase,
    val x: Boolean, speedXZA: Float,
    speedYA: Float, rotationA: Float,
    val accelerationStateA: AccelerationState,
    val turnSateA: TurnState
) : FlightBehavior(horse) {
    init {
        speedXZ = speedXZA
        speedY = speedYA
        rotation = rotationA
    }

    override fun <T : Any?> getData(key: EntityDataAccessor<T?>?): T? {
        return if (key != IS_FLYING) super.getData(key) else {
            (super.getData(key as? EntityDataAccessor<Boolean>) || x) as? T
        }
    }

    override fun getAccelerationState(): AccelerationState {
        return accelerationStateA
    }

    override fun getTurnState(): TurnState {
        return turnSateA
    }
}