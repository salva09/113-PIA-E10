package experimental

import evaluate
import lexer.*

fun run() {
    while (tokens.contains(Token(WHITESPACE, ""))) {
        tokens.remove(Token(WHITESPACE, ""))
    }

    val variables = LinkedHashMap<String, Int>()
    val valuesToShow = ArrayList<Int?>()

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
                valuesToShow.add(variables[tokens.first.sequence])
            }
        }
        tokens.pop()
    }
    if (valuesToShow.isNotEmpty()) showOutput(valuesToShow)
}