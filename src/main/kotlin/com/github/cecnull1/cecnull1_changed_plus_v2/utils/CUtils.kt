package com.github.cecnull1.cecnull1_changed_plus_v2.utils

import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.MODID
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.Entity

object CUtils

val Entity.modData: CompoundTag
    get() = this.persistentData.getCompound(MODID)