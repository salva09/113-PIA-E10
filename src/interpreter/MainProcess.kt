package interpreter

import lexer.*
import java.util.*
import kotlin.collections.LinkedHashMap

class RuntimeException(message: String) : Exception(message)

var line = 1

fun run(tokens: LinkedList<Token>) {
    line = 1

    while (tokens.contains(Token(WHITESPACE, ""))) {
        tokens.remove(Token(WHITESPACE, ""))
    }

    val variables = LinkedHashMap<String, Long>()

    while (tokens.first.token != KEYWORD_3) {
        if (tokens.first.token == VARIABLE) {
            if (tokens[1].token == ASSIGNATION) {
                val variable = tokens.first.sequence
                tokens.pop()
                tokens.pop()
                variables[variable] = evaluate(tokens, variables)
            }
        }
        if (tokens.first.token == FUNCTION) {
            if (tokens.first.sequence == "imprimir") {
                tokens.pop()
                if (tokens.first.token == STRING) {
                    showOutput(tokens.first.sequence)
                } else {
                    showOutput(evaluate(tokens, variables))
                }
            } else {
                if (tokens.first.sequence == "leer") {
                    tokens.pop()
                    variables[tokens.first.sequence] = readInput()
                }
            }
        }
        if (tokens.first.token == LINE_BREAK) line++
        tokens.pop()
    }
}