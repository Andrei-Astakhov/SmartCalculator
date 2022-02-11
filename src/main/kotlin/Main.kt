package calculator

class Calculator {
    val commandRegex = Regex("""/[a-z]+""")
    val expressionRegex = Regex("""[-+]?(\d+|[a-zA-Z]+)(\s*[-+]*\s*-?(\d+|[a-zA-Z]+))*""")
    val variableRegex = Regex("""\s*\w+(\s*=\s*\w+\s*)*""")
    val globalVariables = mutableMapOf<String, Int>()
    val errors = mapOf(
        "InvalidAssignment" to "Invalid assignment",
        "InvalidExpression" to "Invalid expression",
        "InvalidIdentifier" to "Invalid identifier",
        "UnknownCommand" to "Unknown command",
        "UnknownVariable" to "Unknown variable"
    )

    fun run() {
        while (true) {
            val inputString = readLine()!!
            try {
                when {
                    inputString.isBlank() -> continue
                    inputString.matches(variableRegex)  -> processVariable(inputString)
                    inputString.matches(expressionRegex) -> calculate(inputString)
                    inputString.matches(commandRegex) -> {
                        when (inputString) {
                            "/exit" -> break
                            "/help" -> showHelp()
                            else -> printError("UnknownCommand")
                        }
                    }
                    else -> printError("InvalidExpression")
                }
            } catch(e: Exception) {
                continue
            }
        }
        println("Bye!")
    }

    fun processVariable(expression: String) {
        val variables = expression.split("""\s*=\s*""".toRegex())
        when (variables.size) {
            1 -> printVariable(variables.first())
            2 -> setVariable(variables)
            else -> printError("InvalidAssignment")
        }
    }

    fun printError(errorCode: String) {
        if(errors.containsKey(errorCode)) println(errors.getValue(errorCode))
        else println("UNKNOWN ERROR")
    }

    fun printVariable(variable: String) {
        if (!variable.matches("""[a-zA-Z]+""".toRegex())) {
            printError("InvalidIdentifier")
        } else if (globalVariables.containsKey(variable)) {
            println(globalVariables[variable])
        } else {
            printError("UnknownVariable")
        }
    }

    fun setVariable(variables: List<String>) {
        val leftValue = variables.first().trim()
        val rightValue = variables.last().trim()

        when {
            !leftValue.matches("""[a-zA-Z]+""".toRegex()) -> printError("InvalidIdentifier")
            rightValue.matches("""\d+""".toRegex()) -> globalVariables[leftValue] = rightValue.toInt()
            rightValue.matches("""[a-zA-Z]+""".toRegex()) -> {
                if (globalVariables.containsKey(rightValue)) {
                    globalVariables[leftValue] = globalVariables[rightValue]!!.toInt()
                } else {
                    printError("UnknownVariable")
                }
            }
            else -> printError("InvalidAssignment")
        }
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
        var sum = getValue(members.first())
        for (index in 1..members.lastIndex) {
            if (!members[index].matches("""[-+]""".toRegex())) continue

            if (members[index] == "+") {
                sum += getValue(members[index + 1])
            } else {
                sum -= getValue(members[index + 1])
            }
        }
        println(sum)
    }

    fun getValue(inputValue: String): Int {
        return try {
            inputValue.toInt()
        } catch (e: NumberFormatException) {
            var result = 0
            if(globalVariables.containsKey(inputValue.trim())) {
                result = globalVariables[inputValue.trim()]!!
            } else {
                printError("UnknownVariable")
                throw Exception("Unknown variable")
            }
            result
        }
    }

    fun simplifyOperators(members: MutableList<String>) {
        for(index in 0..members.lastIndex) {
            if (members[index].matches("\\++".toRegex())) {
                members[index] = "+"
            }

            if (members[index].matches("-+".toRegex())) {
                members[index] = if (members[index].length % 2 == 0) "+" else "-"
            }
        }
    }
}

fun main() {
    val calculator = Calculator()
    calculator.run()
}