package memorizingtool

import java.math.BigDecimal
import kotlin.math.pow
import kotlin.reflect.KClass

class NumberMemorize : Memorize<Int>(Int::class) {
    override val commands: Map<String, Array<KClass<*>>> = super.commands + mapOf(
        "/sum" to arrayOf(Int::class, Int::class),
        "/subtract" to arrayOf(Int::class, Int::class),
        "/multiply" to arrayOf(Int::class, Int::class),
        "/divide" to arrayOf(Int::class, Int::class),
        "/pow" to arrayOf(Int::class, Int::class),
        "/factorial" to arrayOf(Int::class),
        "/sumAll" to emptyArray(),
        "/average" to emptyArray()
    )

    override fun converterTo(line: String): Int = line.toInt()

    override fun converterFrom(element: Int): String = element.toString()

    override fun help() {
        super.help()
        println(
            """
    ===========================================================================================================
    Number-specific commands:
    ===========================================================================================================
    /sum [<int> INDEX1] [<int> INDEX2] - calculate the sum of the two specified elements
    /subtract [<int> INDEX1] [<int> INDEX2] - calculate the difference between the two specified elements
    /multiply [<int> INDEX1] [<int> INDEX2] - calculate the product of the two specified elements
    /divide [<int> INDEX1] [<int> INDEX2] - calculate the division of the two specified elements
    /pow [<int> INDEX1] [<int> INDEX2] - calculate the power of the specified element to the specified exponent element
    /factorial [<int> INDEX] - calculate the factorial of the specified element
    /sumAll - calculate the sum of all elements
    /average - calculate the average of all elements
    ===========================================================================================================
""".trimIndent()
        )
    }

    fun sum(i: Int, j: Int) = try {
        val a = list[i].toBigInteger()
        val b = list[j].toBigInteger()
        val res = a + b
        println("Calculation performed: $a + $b = $res")
    } catch (e: IndexOutOfBoundsException) {
        println("Index out of bounds!")
    }

    fun subtract(i: Int, j: Int) = try {
        val a = list[i].toBigInteger()
        val b = list[j].toBigInteger()
        val res = a - b
        println("Calculation performed: $a - $b = $res")
    } catch (e: IndexOutOfBoundsException) {
        println("Index out of bounds!")
    }

    fun multiply(i: Int, j: Int) = try {
        val a = list[i].toBigInteger()
        val b = list[j].toBigInteger()
        val res = a * b
        println("Calculation performed: $a * $b = $res")
    } catch (e: IndexOutOfBoundsException) {
        println("Index out of bounds!")
    }

    fun divide(i: Int, j: Int) = try {
        val a = list[i]
        val b = list[j]

        if (a == 0 || b == 0) throw ArithmeticException()

        val result = a.toDouble() / b.toDouble()
        val formattedResult = "%.6f".format(result)
        println("Calculation performed: $a / $b = $formattedResult")
    } catch (e: IndexOutOfBoundsException) {
        println("Index out of bounds!")
    } catch (e: ArithmeticException) {
        println("Division by zero")
    }

    fun pow(i: Int, j: Int) = try {
        val a = list[i]
        val b = list[j]

        val result = if (b >= 0) {
            var res = java.math.BigInteger.ONE
            val base = java.math.BigInteger.valueOf(a.toLong())

            repeat(b) {
                res = res.multiply(base)
            }
            res.toString()
        } else {
            val res = 1.0 / a.toDouble().pow(-b.toDouble())
            "%.6f".format(res).replace(',', '.')
        }
        println("Calculation performed: $a ^ $b = $result")
    } catch (e: IndexOutOfBoundsException) {
        println("Index out of bounds!")
    }

    fun factorial(index: Int) {
        try {
            val number = list[index]
            var res = 1L
            var i = 2

            if (number < 0 ) println("undefined").run { return }

            if (number == 0) {
                println("Calculation performed: 0! = $res")
                return
            }

            do {
                res *= i++
            } while (i <= number)

            println("Calculation performed: ${number}! = $res")
        } catch (e: IndexOutOfBoundsException) {
            println("Index out of bounds!")
        }
    }

    fun sumAll() {
        val sum = list.sumOf { it.toBigInteger() }
        println("Sum of all elements: $sum")
    }

    fun average() {
        val sum = list.sumOf { it.toBigInteger() }
        val avg = BigDecimal(sum).divide(BigDecimal(list.size), 6, java.math.RoundingMode.HALF_UP)
        println("Average of all elements: $avg")
    }
}
