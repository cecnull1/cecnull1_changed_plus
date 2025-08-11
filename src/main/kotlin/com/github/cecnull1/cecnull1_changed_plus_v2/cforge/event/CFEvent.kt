package com.github.cecnull1.cecnull1_changed_plus_v2.cforge.event

import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

interface IEvent {
    var isCanceled: Boolean
    val isCancelable: Boolean
}

object CForgeEvent {
    inline fun <reified T: IEvent> registerEvents(noinline block: (T) -> Unit) {
        // 创建安全的适配器函数
        val adapter: (IEvent) -> Unit = { event ->
            // 安全检查：仅当事件类型匹配时才调用原函数
            if (event is T) {
                block(event)
            }
        }
        // 获取或创建事件类型对应的处理器列表
        val handlerList = cForgeEventMap.getOrPut(T::class) { mutableListOf() }
        // 添加处理器（现在类型已匹配）
        handlerList.add(adapter)
    }

    fun IEvent.post() {
        // 重置事件状态（确保每次post都是初始状态）
        isCanceled = false

        // 获取当前事件类型对应的监听器列表，若为空则提前返回
        val listeners = cForgeEventMap[this::class] ?: return

        // 遍历所有监听器
        for (handler in listeners) {
            // 如果事件已被取消，则中断后续监听器的执行
            if (isCanceled) break

            // 执行监听器逻辑（不捕获异常，由调用方处理）
            handler(this)

            // 在监听器执行后，检查事件取消的合法性
            if (isCanceled && !isCancelable) {
                // 抛出详细异常信息，帮助开发者快速定位问题
                throw IllegalStateException(
                    "不可取消事件 '${this::class.simpleName}' 被监听器取消。" +
                            "请检查以下可能：" +
                            "\n1. 该事件应声明为可取消: override val isCancelable = true" +
                            "\n2. 或监听器中不应设置 isCanceled = true"
                )
            }
        }
    }

    val cForgeEventMap: ConcurrentHashMap<
            KClass<out IEvent>,
            MutableList<(IEvent) -> Unit>
            > by lazy { ConcurrentHashMap() }

    fun init() {
        cForgeEventMap
    }
}