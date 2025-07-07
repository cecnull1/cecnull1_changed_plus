import kotlin.concurrent.thread
import java.time.Instant

fun main() {
    val start = Instant.now()

    val workers = listOf(
        thread(start = true) { heavyComputation() },
        thread(start = true) { heavyComputation() },
        thread(start = true) { heavyComputation() },
        thread(start = true) { heavyComputation() }
    )

    workers.forEach { it.join() }

    val end = Instant.now()
    val duration = java.time.Duration.between(start, end).toMillis()
    println("Total time: $duration ms")
}

fun heavyComputation() {
    var result: Long = 0
    for (i in 0L until 20_000_000L) {
        result += i
    }
    //println("Result: $result")  // 可选打印结果
}