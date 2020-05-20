package experimental

import lexer.*

fun run() {
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
                showOutput(evaluate(tokens, variables))
            } else {
                if (tokens.first.sequence == "leer") {
                    tokens.pop()
                    variables[tokens.first.sequence] = readInput(tokens.first.sequence)
                }
            }
        }
        tokens.pop()
    }
}