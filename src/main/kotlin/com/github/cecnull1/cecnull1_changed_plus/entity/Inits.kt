package com.github.cecnull1.cecnull1_changed_plus.entity

import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext

// 显式声明为非空类型
val modEventBus: IEventBus = FMLJavaModLoadingContext.get().modEventBus
