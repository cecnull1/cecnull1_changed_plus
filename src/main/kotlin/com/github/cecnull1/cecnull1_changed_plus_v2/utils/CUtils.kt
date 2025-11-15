package com.github.cecnull1.cecnull1_changed_plus_v2.utils

import com.github.cecnull1.cecnull1_changed_plus_v2.mixin.LivingEntityAccessor
import com.github.cecnull1.cecnull1_changed_plus_v2.mixin.RegistryObjectAccessor
import com.google.common.collect.ImmutableList
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.Vec3
import net.minecraftforge.registries.RegistryObject
import java.util.stream.Stream
import kotlin.math.cos
import kotlin.math.sin

object CUtils

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

/**
 * 将 yaw 角度（度）转换成水平视线方向向量，pitch 固定为 0。
 * 0° 指向 +Z，90° 指向 +X，180° 指向 –Z，–90° 指向 –X。
 */
fun Float.toHorizontalViewVec(): Vec3 {
    val rad = -this * Math.PI / 180.0
    return Vec3(sin(rad), 0.0, cos(rad))
}

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