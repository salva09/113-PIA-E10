import java.util.LinkedList
import java.util.Stack
import kotlin.math.pow

fun main() {
    println("Enter an expression: ")
    val input = readLine()!!
    val tokens = LinkedList<Char>()
    for (char in input) {
        if (char != ' ') {
            tokens.add(char)
        }
    }
    try {
        println("Result: ${evaluate(tokens)}")
    } catch (ex: ArithmeticException) {
        println(ex.localizedMessage)
    } catch (ex: Exception) {
        println(ex.localizedMessage)
    }
}
fun evaluate(tokens: LinkedList<Char>): Stack<Int> {
    val values = Stack<Int>()
    val operators = Stack<Char>()
    var wasOperator = true

    while (tokens.first != ';' && !tokens.isEmpty()) {
        if (tokens.first == '(') {
            operators.add(tokens.first)
        } else {
            if (tokens.first == ')') {
                while (operators.peek() != '(' && !operators.isEmpty()) {
                    val number1 = values.pop()
                    val number2 = values.pop()
                    val operation = operators.pop()
                    val result = operate(number2, number1, operation)
                    values.add(result)
                }
                operators.pop()
            } else {
                if (wasOperator && tokens.first == '-') {
                    values.add(-1)
                    operators.add('*')
                } else {
                    if (tokens.first.isDigit()) {
                        values.add(tokens.first.toString().toInt())
                    } else {
                        while (!operators.isEmpty() && hierarchy(tokens.first) <= hierarchy(operators.peek())) {
                            if (operators.peek() != '(') {
                                val number1 = values.pop()
                                val number2 = values.pop()
                                val operation = operators.pop()
                                val result = operate(number2, number1, operation)
                                values.add(result)
                            }
                        }
                        operators.add(tokens.first)
                    }
                }
            }
        }
        wasOperator = isOperator(tokens.first)
        tokens.pop()
    }
    while (!operators.isEmpty()) {
        val number1 = values.pop()
        val number2 = values.pop()
        val operation = operators.pop()
        val result = operate(number2, number1, operation)
        values.add(result)
    }
    return values
}

fun isOperator(sequence: Char) = when (sequence) {
    '+', '-', '*', '/', '(' -> true
    else -> false
}

fun operate(a: Int, b: Int, operation: Char) = when (operation) {
    '+' -> a + b
    '-' -> a - b
    '*' -> a * b
    '/' -> if (b != 0) a / b else throw ArithmeticException("Divide by zero cannot be possible")
    '^' -> a.toFloat().pow(b).toInt()
    else -> throw Exception("Operator not recognized")
}

fun hierarchy(operation: Char) = when (operation) {
    '+' -> 1
    '-' -> 1
    '*' -> 2
    '/' -> 2
    '^' -> 3
    '(' -> 0
    else -> throw Exception("Operator not recognized")
}