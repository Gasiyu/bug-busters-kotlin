package memorizingtool

import java.io.FileNotFoundException
import kotlin.reflect.KClass
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.valueParameters

abstract class Memorize<T>(klass: KClass<T>) where T : Comparable<T>, T : Any {
    protected val list: ArrayList<T> = ArrayList()
    private var finished = false

    protected open val commands: Map<String, Array<KClass<*>>> = mapOf(
        "/help" to emptyArray(),
        "/menu" to emptyArray(),
        "/add" to arrayOf(klass),
        "/remove" to arrayOf(Int::class),
        "/replace" to arrayOf(Int::class, klass),
        "/replaceAll" to arrayOf(klass, klass),
        "/index" to arrayOf(klass),
        "/sort" to arrayOf(String::class),
        "/frequency" to emptyArray(),
        "/print" to arrayOf(Int::class),
        "/printAll" to arrayOf(String::class),
        "/getRandom" to emptyArray(),
        "/count" to arrayOf(klass),
        "/size" to emptyArray(),
        "/equals" to arrayOf(Int::class, Int::class),
        "/readFile" to arrayOf(String::class),
        "/writeFile" to arrayOf(String::class),
        "/clear" to emptyArray(),
        "/compare" to arrayOf(Int::class, Int::class),
        "/mirror" to emptyArray(),
        "/unique" to emptyArray()
    )

    fun run() {
        while (!finished) {
            try {
                println("Perform action:")
                val data = readln()
                    .split(" ")
                    .map(String::trim)
                    .filter(String::isNotEmpty)

                val command = data.firstOrNull()

                if (command.isNullOrBlank()) {
                    println("No command provided")
                    continue
                }

                if (!commands.containsKey(command)) {
                    println("No such command")
                    continue
                }

                val args: List<*> = data.drop(1)
                    .mapIndexed { index, arg ->
                        when (commands[command]?.get(index)) {
                            Int::class -> arg.toInt()
                            Boolean::class -> arg.toBooleanStrict()
                            else -> arg
                        }
                    }

                val parametersSize =
                    this::class.memberFunctions.find { it.name == command.substring(1) }?.valueParameters?.size ?: 0

                if (parametersSize != args.size) {
                    println("Incorrect amount of arguments")
                    continue
                }

                (this::class.memberFunctions.find { it.name == command.substring(1) && it.valueParameters.size == args.size })
                    ?.call(this, *args.toTypedArray())
            } catch (e: NumberFormatException) {
                println("Some arguments can't be parsed!")
            } catch (e: IllegalArgumentException) {
                println("Some arguments can't be parsed!")
            }
        }
    }

    protected abstract fun converterTo(line: String): T

    protected abstract fun converterFrom(element: T): String

    open fun help() {
        println(
            """
            ===========================================================================================================
            Usage: COMMAND [<TYPE> PARAMETERS]
            ===========================================================================================================
            General commands:
            ===========================================================================================================
            /help - display this help message
            /menu - return to the menu

            /add [<T> ELEMENT] - add the specified element to the list
            /remove [<int> INDEX] - remove the element at the specified index from the list
            /replace [<int> INDEX] [<T> ELEMENT] - replace the element at specified index with the new one
            /replaceAll [<T> OLD] [<T> NEW] - replace all occurrences of specified element with the new one

            /index [<T> ELEMENT] - get the index of the first specified element in the list
            /sort [ascending|descending] - sort the list in ascending or descending order
            /frequency - the frequency count of each element in the list
            /print [<int> INDEX] - print the element at the specified index in the list
            /printAll [asList|lineByLine|oneLine] - print all elements in the list in specified format
            /getRandom - get a random element from the list
            /count [<T> ELEMENT] - count the number of occurrences of the specified element in the list
            /size - get the number of elements in the list
            /equals [<int> INDEX1] [<int> INDEX2] - check if two elements are equal
            /clear - remove all elements from the list
            /compare [<int> INDEX1] [<int> INDEX2] compare elements at the specified indices in the list
            /mirror - mirror elements' positions in list
            /unique - unique elements in the list\n
            /readFile [<string> FILENAME] - import data from the specified file and add it to the list
            /writeFile [<string> FILENAME] - export the list data to the specified file
        """.trimIndent()
        )
    }

    fun menu() {
        finished = true
        list.clear()
        System.gc()
    }

    fun add(element: T) {
        list.add(element)
        println("Element $element added")
    }

    fun remove(index: Int) = try {
        list.removeAt(index)
        println("Element on $index position removed")
    } catch (e: IndexOutOfBoundsException) {
        println("Index out of bounds!")
    }

    fun replace(index: Int, element: T) = try {
        list[index] = element
        println("Element on $index position replaced with $element")
    } catch (e: IndexOutOfBoundsException) {
        println("Index out of bounds!")
    }

    fun replaceAll(from: T, to: T) {
        list.replaceAll { if (it == from) to else it }
        println("Each $from element replaced with $to")
    }

    fun index(value: T) {
        val index = list.indexOf(value)
        println(if (index != -1) "First occurrence of $value is on $index position" else "There is no such element")
    }

    fun sort(order: String) {
        when (order) {
            "ascending" -> list.sort()
            "descending" -> list.sortDescending()
            else -> println("Incorrect argument, possible arguments: ascending, descending")
        }
        println("Memory sorted $order")
    }

    fun frequency() {
        if (list.isEmpty()) println("There are no elements").run { return }
        val counts = list.groupingBy { it }.eachCount()
        println("Frequency:")
        counts.forEach { (k, v) ->
            println("$k: $v")
        }
    }

    fun print(index: Int) = try {
        println("Element on $index position is ${list[index]}")
    } catch (e: IndexOutOfBoundsException) {
        println("Index out of bounds!")
    }

    fun getRandom() = try {
        println("Random element: ${list.random()}")
    } catch (e: NoSuchElementException) {
        println("There are no elements memorized")
    }

    fun printAll(type: String) {
        when (type) {
            "asList" -> {
                println("List of elements:")
                println(list)
            }
            "lineByLine" -> {
                println("List of elements:")
                list.forEach(::println)
            }
            "oneLine" -> {
                println("List of elements:")
                println(list.joinToString(" "))
            }
            else -> println("Incorrect argument, possible arguments: asList, lineByLine, oneLine")
        }
    }

    fun count(value: T) {
        println("Amount of $value: ${list.count { it == value }}")
    }

    fun size() {
        println("Amount of elements: ${list.size}")
    }

    fun equals(i: Int, j: Int) = try {
        val res = list[i] == list[j]
        println(
            "$i and $j elements are${if (!res) " not" else ""} equal: ${list[i]} ${if (res) "=" else "!="} ${list[j]}"
        )
    } catch (e: IndexOutOfBoundsException) {
        println("Index out of bounds!")
    }

    fun readFile(path: String) = try {
        val reader = FileReader(::converterTo)
        val data = reader.read(path)
        list.addAll(data)
        println("Data imported: ${data.size}")
    } catch (e: FileNotFoundException) {
        println("File not found!")
    }

    fun writeFile(path: String) {
        val writer = FileWriter(::converterFrom)
        writer.write(path, list)
        println("Data exported: ${list.size}")
    }

    fun clear() {
        list.clear()
        println("Data cleared")
    }

    fun compare(i: Int, j: Int) = try {
        when {
            list[i] > list[j] -> println("Result: ${list[i]} > ${list[j]}")
            list[i] < list[j] -> println("Result: ${list[i]} < ${list[j]}")
            else -> println("Result: ${list[i]} = ${list[j]}")
        }
    } catch (e: IndexOutOfBoundsException) {
        println("Index out of bounds!")
    }

    fun mirror() {
        list.reverse()
        println("Data reversed")
    }

    fun unique() {
        println("Unique values: ${list.distinct()}")
    }
}
