package calculator

class Calculator {
    fun run() {
        while (true) {
            val inputString = readLine()!!
            when {
                inputString.isBlank() -> continue
                inputString == "/exit" -> break
                inputString == "/help" -> showHelp()
                else -> calculate(inputString)
            }
        }
        println("Bye!")

    }

    fun showHelp() {
        println("The program calculates the sum of numbers.")
        println("Your can enter several same operators following each other, e.g.: 9 +++ 10 -- 8.")
        println("The even number of minuses gives a plus, and the odd number of minuses gives a minus!")
        println("Look at it this way: 2 -- 2 equals 2 - (-2) equals 2 + 2.")
    }

    fun calculate(expression: String) {

        val members = expression.split(" ").filter { it.isNotEmpty() }.toMutableList()
        simplifyOperators(members)
        var sum = members.first().toInt()
        for (index in 1..members.lastIndex) {
            if (isNumber(members[index])) continue

            if (members[index] == "+") {
                sum += members[index + 1].toInt()
            } else {
                sum -= members[index + 1].toInt()
            }
        }
        println(sum)
    }

    fun simplifyOperators(members: MutableList<String>) {
        for(index in 0..members.lastIndex) {
            val value = members[index]
            if (isNumber(value)) continue
            if (value.first() == '+') {
                members[index] = "+"
            } else if (value.first() == '-') {
                members[index] = if (value.length % 2 == 0) "+" else "-"
            }
        }
    }

    fun isNumber(s: String): Boolean {
        return when(s.toIntOrNull())
        {
            null -> false
            else -> true
        }
    }
}

fun main() {
    val calculator = Calculator()
    calculator.run()
}