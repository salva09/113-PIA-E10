import lexer.*
import java.util.LinkedList
import java.util.Stack
import kotlin.math.pow

fun evaluate(tokens: LinkedList<Token>, variables: LinkedHashMap<String, Int>): Int {
    val values = Stack<Int>()
    val operators = Stack<Int>()
    var wasOperator = true

    while (tokens.first.token != SEMICOLON && !tokens.isEmpty()) {
        if (tokens.first.token == OPEN_BRACKET) {
            operators.add(tokens.first.token)
        } else {
            if (tokens.first.token == CLOSE_BRACKET) {
                while (operators.peek() != OPEN_BRACKET && !operators.isEmpty()) {
                    val number1 = values.pop()
                    val number2 = values.pop()
                    val operation = operators.pop()
                    val result = operate(number2, number1, operation)
                    values.add(result)
                }
                operators.pop()
            } else {
                if (wasOperator && tokens.first.token == MINUS) {
                    values.add(-1)
                    operators.add(MULT)
                } else {
                    if (tokens.first.token == VARIABLE) {
                        values.add(variables[tokens.first.sequence])
                    } else {
                        if (tokens.first.token == NUMBER) {
                            values.add(tokens.first.sequence.toInt())
                        } else {
                            while (!operators.isEmpty() && hierarchy(tokens.first.token) <= hierarchy(operators.peek())) {
                                if (operators.peek() != OPEN_BRACKET) {
                                    val number1 = values.pop()
                                    val number2 = values.pop()
                                    val operation = operators.pop()
                                    val result = operate(number2, number1, operation)
                                    values.add(result)
                                }
                            }
                            operators.add(tokens.first.token)
                        }
                    }
                }
            }
        }
        wasOperator = isOperator(tokens.first.token)
        tokens.pop()
    }
    while (!operators.isEmpty()) {
        val number1 = values.pop()
        val number2 = values.pop()
        val operation = operators.pop()
        val result = operate(number2, number1, operation)
        values.add(result)
    }
    return if (values.size == 1) values.first() else throw Exception("There was an error evaluating the expression")
}

private fun isOperator(token: Int) = when (token) {
    PLUS, MINUS, MULT, DIV -> true
    else -> false
}

private fun operate(a: Int, b: Int, operation: Int) = when (operation) {
    PLUS -> a + b
    MINUS -> a - b
    MULT -> a * b
    DIV -> if (b != 0) a / b else throw ArithmeticException("Divide by zero cannot be possible")
    RAISED -> a.toFloat().pow(b).toInt()
    else -> throw Exception("Operator not recognized")
}

private fun hierarchy(token: Int) = when (token) {
    PLUS -> 1
    MINUS -> 1
    MULT -> 2
    DIV -> 2
    RAISED -> 3
    OPEN_BRACKET -> 0
    else -> throw Exception("Operator not recognized")
}