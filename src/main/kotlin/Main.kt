package calculator

fun main() {
    while (true) {
        val inputString = readLine()!!
        when {
            inputString.isBlank() -> continue
            inputString == "/exit" -> break
            !inputString.contains(" ") -> {
                println(inputString)
                continue
            }
            else -> {
                val (a, b) = inputString.split(" ")
                val sum = a.toInt() + b.toInt()
                println(sum)
            }
        }
    }
    println("Bye!")
}