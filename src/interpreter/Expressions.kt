package interpreter

import lexer.*
import java.util.*
import kotlin.math.pow

fun evaluate(tokens: LinkedList<Token>, variables: LinkedHashMap<String, Long>): Long {
    val values = Stack<Long>()
    val operators = Stack<Int>()
    var wasOperator = true

    while (tokens.first.token != SEMICOLON && !tokens.isEmpty()) {
        when {
            tokens.first.token == OPEN_BRACKET -> operators.add(tokens.first.token)
            tokens.first.token == CLOSE_BRACKET -> {
                while (operators.peek() != OPEN_BRACKET && !operators.isEmpty()) {
                    val number1 = values.pop()
                    val number2 = values.pop()
                    val operation = operators.pop()
                    val result = operate(number2, number1, operation)
                    values.add(result)
                }
                if (operators.peek() == OPEN_BRACKET) operators.pop()
            }
            wasOperator && tokens.first.token == MINUS -> {
                values.add(-1)
                operators.add(MULT)
            }
            wasOperator && tokens.first.token == PLUS -> {}
            tokens.first.token == VARIABLE -> {
                if (variables[tokens.first.sequence] != null) {
                    values.add(variables[tokens.first.sequence])
                } else {
                    throw RuntimeException("At line $line: Variable '${tokens.first.sequence}' must be initialized")
                }
            }
            tokens.first.token == NUMBER -> values.add(tokens.first.sequence.toLong())
            else -> {
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
    return if (values.size == 1) values.first() else throw RuntimeException("There was an error evaluating the expression")
}

private fun isOperator(token: Int) = when (token) {
    PLUS, MINUS, MULT, DIV, OPEN_BRACKET -> true
    else -> false
}

private fun operate(a: Long, b: Long, operation: Int) = when (operation) {
    PLUS -> a + b
    MINUS -> a - b
    MULT -> a * b
    DIV -> if (b.compareTo(0) != 0) a / b else throw ArithmeticException("At line $line: Division by zero cannot be possible")
    RAISED -> a.toFloat().pow(b.toFloat()).toLong()
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