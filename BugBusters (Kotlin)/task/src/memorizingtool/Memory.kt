package memorizingtool

fun main() {
    while (true) {
        println(
            """
            Welcome to Data Memory!
            Possible actions:
            1. Memorize booleans
            2. Memorize numbers
            3. Memorize words
            0. Quit
        """.trimIndent()
        )

        when (readln()) {
            "1" -> BooleanMemorize().run()
            "2" -> NumberMemorize().run()
            "3" -> WordMemorize().run()
            "0" -> break
            else -> println("Incorrect command")
        }

        System.gc()
    }
}
