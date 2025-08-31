package com.github.cecnull1.cecnull1_changed_plus_v2.cforge.event

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import com.github.cecnull1.cecnull1_changed_plus_v2.cforge.event.CForgeEventCore.registerEvents
import com.github.cecnull1.cecnull1_changed_plus_v2.cforge.event.CForgeEventCore.post
import com.github.cecnull1.cecnull1_changed_plus_v2.cforge.event.CForgeEventCore.registerFastEvents

typealias IEventBus = ConcurrentMap<KClass<out IEvent>, MutableList<(IEvent) -> Unit>>

interface IEvent {
    var isCanceled: Boolean
    val isCancelable: Boolean
    val isInterruptibleWhenCanceled: Boolean
        get() = false
}

object CForgeEventBus {
    inline fun <reified T: IEvent> registerEvents(crossinline block: (T) -> Unit) = cForgeEventMap.registerEvents<T>(block)

    inline fun <reified T: IEvent> registerFastEvents(noinline block: (T) -> Unit) = cForgeEventMap.registerFastEvents<T>(block)

    inline fun IEvent.post() = this.post(cForgeEventMap)

    /*public!*/ val cForgeEventMap: IEventBus = ConcurrentHashMap(16)
}

object CForgeEventCore {
    inline fun <reified T: IEvent> IEventBus.registerEvents(crossinline block: (T) -> Unit) {
        this.getOrPut(T::class) { mutableListOf() }.add {
            event ->
            if (event is T) {
                block(event)
            }
        }
    }

    fun IEvent.post(eventBus: IEventBus) {
        // 重置事件状态（确保每次post都是初始状态）
        isCanceled = false

        // 获取当前事件类型对应的监听器列表，若为空则提前返回
        val listeners = eventBus[this::class] ?: return

        // 遍历所有监听器
        for (handler in listeners) {
            // 如果事件已被取消，则中断后续监听器的执行
            if (isCanceled && isInterruptibleWhenCanceled) break

            // 执行监听器逻辑（不捕获异常，由调用方处理）
            handler(this)

            // 在监听器执行后，检查事件取消的合法性
            if (isCanceled && !isCancelable) {
                // 抛出详细异常信息，帮助开发者快速定位问题
                throw UnsupportedOperationException(
                    "不可取消事件 '${this::class.simpleName}' 被监听器取消。" +
                            "请检查以下可能：" +
                            "\n1. 该事件应声明为可取消: override val isCancelable = true" +
                            "\n2. 或监听器中不应设置 isCanceled = true"
                )
            }
        }
    }

    inline fun <reified T: IEvent> IEventBus.registerFastEvents(noinline block: (T) -> Unit) {
        this.getOrPut(T::class) { mutableListOf() }.add(block as (IEvent) -> Unit)
    }

    fun initEventBus(): IEventBus {
        return ConcurrentHashMap(16)
    }

    fun IEventBus.destroyEventBus() {
        this.forEach {
            this[it.key]?.clear()
        }
        this.clear()
    }
}