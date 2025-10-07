package com.github.cecnull1.cecnull1_changed_plus_v2.utils

import com.github.cecnull1.cecnull1_changed_plus_v2.mixin.LivingEntityAccessor
import com.google.common.collect.ImmutableList
import net.minecraft.world.entity.LivingEntity
import java.util.stream.Stream

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