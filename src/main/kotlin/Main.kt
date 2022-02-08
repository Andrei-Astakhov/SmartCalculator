package calculator

fun main() {
    while (true) {
        val inputString = readLine()!!
        when {
            inputString.isBlank() -> continue
            inputString == "/exit" -> break
            inputString == "/help" -> println("The program calculates the sum of numbers")
            else -> {
                val inputNumbers = inputString.split(" ").map { it.toInt() }
                val sum = inputNumbers.sum()
                println(sum)
            }
        }
    }
    println("Bye!")
}