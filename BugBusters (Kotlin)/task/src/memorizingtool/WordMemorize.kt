package memorizingtool

import java.util.regex.PatternSyntaxException
import kotlin.reflect.KClass

class WordMemorize : Memorize<String>(String::class) {
    override val commands: Map<String, Array<KClass<*>>> = super.commands + mapOf(
        "/concat" to arrayOf(Int::class, Int::class),
        "/swapCase" to arrayOf(Int::class), "/upper" to arrayOf(Int::class),
        "/lower" to arrayOf(Int::class), "/reverse" to arrayOf(Int::class), "/length" to arrayOf(Int::class),
        "/join" to arrayOf(String::class), "/regex" to arrayOf(String::class)
    )

    override fun converterTo(line: String): String = line
    override fun converterFrom(element: String): String = element

    override fun help() {
        super.help()
        println(
            """
            ===========================================================================================================
            Word-specific commands:
            ===========================================================================================================
            /concat [<int> INDEX1] [<int> INDEX2] - concatenate two specified strings
            /swapCase [<int> INDEX] - output swapped case version of the specified string
            /upper [<int> INDEX] - output uppercase version of the specified string
            /lower [<int> INDEX] - output lowercase version of the specified string
            /reverse [<int> INDEX] - output reversed version of the specified string
            /length [<int> INDEX] - get the length of the specified string
            /join [<string> DELIMITER] - join all the strings with the specified delimiter
            /regex [<string> PATTERN] - search for all elements that match the specified regular expression pattern
            ===========================================================================================================
        """.trimIndent()
        )
    }

    fun concat(i: Int, j: Int) = try {
        println("Concatenated string: ${list[i] + list[j]}")
    } catch (e: IndexOutOfBoundsException) {
        println("Index out of bounds!")
    }

    fun swapCase(index: Int) = try {
        val swappedCase = list[index]
            .map { ch ->
                if (ch.isUpperCase()) ch.lowercase() else ch.uppercase()
            }
            .joinToString("")
        println("\"${list[index]}\" string with swapped case: $swappedCase")
    } catch (e: IndexOutOfBoundsException) {
        println("Index out of bounds!")
    }

    fun upper(index: Int) = try {
        println("Uppercase \"${list[index]}\" string: ${list[index].uppercase()}")
    } catch (e: IndexOutOfBoundsException) {
        println("Index out of bounds!")
    }

    fun lower(index: Int) = try {
        println("Lowercase \"${list[index]}\" string: ${list[index].lowercase()}")
    } catch (e: IndexOutOfBoundsException) {
        println("Index out of bounds!")
    }

    fun reverse(index: Int) = try {
        println("Reversed \"${list[index]}\" string: ${list[index].reversed()}")
    } catch (e: IndexOutOfBoundsException) {
        println("Index out of bounds!")
    }

    fun length(index: Int) = try {
        println("Length of \"${list[index]}\" string: ${list[index].length}")
    } catch (e: IndexOutOfBoundsException) {
        println("Index out of bounds!")
    }

    fun join(delimiter: String) {
        println("Joined string: ${list.joinToString(delimiter)}")
    }

    fun regex(pattern: String) = try {
        val regex = Regex(pattern)
        val matches = list.filter { regex.matches(it) }
        if (matches.isNotEmpty()){
            println("Strings that match provided regex:")
            println(matches)
        } else {
            println("There are no strings that match provided regex")
        }
    } catch (e: PatternSyntaxException) {
        println("Incorrect regex pattern provided")
    }
}
