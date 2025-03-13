package memorizingtool

import kotlin.reflect.KClass

class BooleanMemorize : Memorize<Boolean>(Boolean::class) {
    override val commands: Map<String, Array<KClass<*>>> = super.commands + mapOf(
        "/flip" to arrayOf(Int::class),
        "/negateAll" to emptyArray(),
        "/and" to arrayOf(Int::class, Int::class),
        "/or" to arrayOf(Int::class, Int::class),
        "/logShift" to arrayOf(Int::class),
        "/convertTo" to arrayOf(String::class),
        "/morse" to emptyArray()
    )

    override fun converterTo(line: String): Boolean = line.toBooleanStrict()

    override fun converterFrom(element: Boolean): String = element.toString()

    override fun help() {
        super.help()
        println(
            """
            ===========================================================================================================
            Boolean-specific commands:
            ===========================================================================================================
            /flip [<int> INDEX] - flip the specified boolean
            /negateAll - negate all the booleans in memory
            /and [<int> INDEX1] [<int> INDEX2] - calculate the bitwise AND of the two specified elements
            /or [<int> INDEX1] [<int> INDEX2] - calculate the bitwise OR of the two specified elements
            /logShift [<int> NUM] - perform a logical shift of elements in memory by the specified amount
            /convertTo [string/number] - convert the boolean(bit) sequence in memory to the specified type
            /morse - convert the boolean(bit) sequence to Morse code
            ===========================================================================================================
        """.trimIndent()
        )
    }

    fun flip(index: Int) = try {
        list[index] = !list[index]
        println("Element on $index position flipped")
    } catch (e: IndexOutOfBoundsException) {
        println("Index out of bounds!")
    }

    fun negateAll() {
        list.replaceAll { !it }
        println("All elements negated")
    }

    fun and(i: Int, j: Int) = try {
        val a = list[i]
        val b = list[j]
        val res = a && b
        println("Operation performed: ($a && $b) is $res")
    } catch (e: IndexOutOfBoundsException) {
        println("Index out of bounds!")
    }

    fun or(i: Int, j: Int) = try {
        val a = list[i]
        val b = list[j]
        val res = b || a
        println("Operation performed: ($a || $b) is $res")
    } catch (e: IndexOutOfBoundsException) {
        println("Index out of bounds!")
    }

    fun logShift(n: Int) {
        val size = list.size

        var shiftAmount = n % size
        if (shiftAmount < 0) {
            shiftAmount += size
        }

        val originalList = list.toList()

        for (i in 0 until size) {
            val newPos = (i + shiftAmount) % size
            list[newPos] = originalList[i]
        }

        println("Elements shifted by $n")
    }

    fun convertTo(type: String) = try {
        val binary = list.joinToString("") { if (it) "1" else "0" }
        when (type.lowercase()) {
            "number" -> println("Converted: ${binary.toLong(2)}")
            "string" -> {
                val byteSize = Byte.SIZE_BITS
                val sb = StringBuilder()
                if (binary.length % byteSize != 0) {
                    println("Amount of elements is not divisible by 8, so the last ${binary.length % byteSize} of them will be ignored")
                }
                for (i in binary.indices step byteSize) {
                    val segment = binary.substring(i, minOf(i + byteSize, binary.length))
                    val asciiValue = segment.toInt(2)
                    sb.append(asciiValue.toChar())
                }
                println("Converted: $sb")
            }

            else -> println("Incorrect argument, possible arguments: string, number")
        }
    } catch (e: NumberFormatException) {
        println("No data memorized")
    }

    fun morse() {
        if (list.isEmpty()) println("No data memorized").run { return }
        val morseCode = list.joinToString("", prefix = "Morse code: ") { if (it) "." else "_" }
        println(morseCode)
    }
}
