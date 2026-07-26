import java.lang.foreign.Arena
import java.lang.foreign.ValueLayout.JAVA_INT

fun main() {
    Class.forName("a")

    Arena.ofConfined().use {
        val e = it.allocate(4)
        e[JAVA_INT, 0] = 176000
        println(e[JAVA_INT, 0])
    }
}