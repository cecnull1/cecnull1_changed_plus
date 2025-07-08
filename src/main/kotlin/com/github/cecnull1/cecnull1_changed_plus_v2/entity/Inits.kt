package com.github.cecnull1.cecnull1_changed_plus_v2.entity

import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext

// 显式声明为非空类型
val modEventBus: IEventBus by lazy {
    FMLJavaModLoadingContext.get().modEventBus
}