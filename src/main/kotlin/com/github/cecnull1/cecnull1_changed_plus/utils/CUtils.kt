package com.github.cecnull1.cecnull1_changed_plus.utils

import com.github.cecnull1.cecnull1_changed_plus.constant.Constant.MODID
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.Entity

object CUtils

val Entity.modData: CompoundTag
    get() = this.persistentData.getCompound(MODID)