package com.github.cecnull1.cecnull1_changed_plus_v2.utils

import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.MODID
import com.google.common.collect.ImmutableList
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.Entity
import java.util.stream.Stream

object CUtils

val Entity.modData: CompoundTag
    get() = this.persistentData.getCompound(MODID)

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