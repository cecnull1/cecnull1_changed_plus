package com.github.cecnull1.cecnull1_changed_plus_v2.utils

import com.github.cecnull1.cecnull1_cforge.core.PipeCore.calc
import com.github.cecnull1.cecnull1_cforge.core.ResourceLocation
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant
import com.github.cecnull1.cecnull1_changed_plus_v2.mixin.EntityAccessor
import com.github.cecnull1.cecnull1_changed_plus_v2.mixin.LivingEntityAccessor
import com.github.cecnull1.cecnull1_changed_plus_v2.mixin.ModelPartAccessor
import com.github.cecnull1.cecnull1_changed_plus_v2.mixin.RegistryObjectAccessor
import com.google.common.collect.ImmutableList
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityDimensions
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import net.minecraftforge.registries.RegistryObject
import java.lang.Math.fma
import java.util.stream.Stream
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

// 通用扩展函数
fun <T : Any> Iterator<T?>.toImmutableSafeList(): List<T> {
    return ImmutableList.copyOf(this).filterNotNull()
}

// 可选：支持 Iterable
fun <T : Any> Iterable<T?>.toImmutableSafeList(): List<T> {
    return ImmutableList.copyOf(this.iterator()).filterNotNull()
}

// 可选：支持 Stream
fun <T : Any> Stream<T?>.toImmutableSafeList(): List<T> {
    return ImmutableList.copyOf(this.iterator()).filterNotNull()
}

//inline fun <reified T: Entity, R: Entity> EntityType<T>.copy(new: EntityType.EntityFactory<R>) =
//    EntityType.Builder.of<R>(new, this.category).apply {
//        sized(this@copy.width, this@copy.height)
//        setTrackingRange(this@copy.clientTrackingRange())
//        setUpdateInterval(this@copy.updateInterval())
//        setShouldReceiveVelocityUpdates(this@copy.trackDeltas())
//    }

val LivingEntity.fieldIsJumping: Boolean
    get() {
        return (this as LivingEntityAccessor).isJumping
    }

val <T> RegistryObject<T>.value: T?
    get() {
        @Suppress("UNCHECKED_CAST")
        return (this as RegistryObjectAccessor).rawValue as T?
    }

val LivingEntity.dimensions: EntityDimensions
    get() = (this as EntityAccessor).dimensions

val ModelPart.children: Map<String, ModelPart>
    get() = (this as ModelPartAccessor).children

/**
 * 将 yaw 角度（度）转换成水平视线方向向量，pitch 固定为 0。
 * 0° 指向 +Z，90° 指向 +X，180° 指向 –Z，–90° 指向 –X。
 */
fun Float.toHorizontalViewVec(): Vec3 {
    val rad = -this * Math.PI / 180.0
    return Vec3(sin(rad), 0.0, cos(rad))
}

inline val Entity.level: Level
    get() = this.level()

/**
 * 对运动向量进行“方向引导插值”。
 *
 * @param direction 引导方向单位向量（通常是期望前进的方向）
 * @param influence 插值权重 [0.0, 1.0]，0=完全不变，1=完全对齐方向
 * @param applyToY 是否将插值应用于 Y 分量
 * @return 新的运动向量
 */
fun Vec3.lerpedTowards(
    direction: Vec3,
    influence: Double = 0.5,
    applyToY: Boolean = true
): Vec3 {
    val speed = this.length()
    return Vec3(
        x * (1 - influence) + direction.x * speed * influence,
        if (applyToY) y * (1 - influence) + direction.y * speed * influence else y,
        z * (1 - influence) + direction.z * speed * influence
    )
}

private const val UNIT_TOLERANCE = 1e-10

fun Vec3.calcl(
    direction: Vec3,
    influence: Double = 0.5,
    applyToY: Boolean = true
): Vec3 {
    // Step 1: 智能处理 direction —— 若非单位向量，自动归一
    val dirToUse = if (direction.lengthSqr().let { abs(it - 1.0) <= UNIT_TOLERANCE }) {
        direction // 已是单位向量，直接用
    } else {
        // 非单位向量：可能是位置误传，安全归一
        if (direction.lengthSqr() == 0.0) Vec3.ZERO else direction.normalize()
    }

    val speed = this.length()
    if (speed == 0.0) return this // 静止物体不动

    val blended = Vec3(
        fma(x, 1 - influence, dirToUse.x * speed * influence),
        if (applyToY) fma(y, 1 - influence, dirToUse.y * speed * influence) else y,
        fma(z, 1 - influence, dirToUse.z * speed * influence)
    )

    // Step 2: 处理 blended 为零的情况
    return if (blended.lengthSqr() == 0.0) {
        Vec3.ZERO
    } else {
        blended.normalize().scale(speed)
    }
}

fun String.toRL() = ResourceLocation(Constant.MODID, this)