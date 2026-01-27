package com.github.cecnull1.cecnull1_changed_plus_v2.event

// EventDispatcher.kt
import com.github.cecnull1.cecnull1_cforge.core.CForgeEventCore.post
import com.github.cecnull1.cecnull1_changed_plus_v2.bus
import com.github.cecnull1.cecnull1_changed_plus_v2.constant.Constant.MODID
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
object FMLDispatcher {
    @JvmStatic
    @SubscribeEvent
    fun onModEvent(event: FMLCommonSetupEvent) { // 新增MOD事件转发
        ByForgeEvent(event).post(bus)
    }

    @JvmStatic
    @SubscribeEvent
    fun onLoadComplete(event: FMLLoadCompleteEvent) {
        // 标记启动阶段结束
    }
}

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
object RuntimeDispatcher {
    @JvmStatic
    @SubscribeEvent(priority = EventPriority.HIGH)
    fun toCForgeEvent(event: net.minecraftforge.eventbus.api.Event) {
        ByForgeEvent(event).post(bus)
    }
}