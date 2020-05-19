package experimental

import evaluate
import lexer.*

fun run() {
    while (tokens.contains(Token(WHITESPACE, ""))) {
        tokens.remove(Token(WHITESPACE, ""))
    }

    while (tokens.first.token != KEYWORD_3) {
        if (tokens.first.token == VARIABLE) {
            if (tokens[1].token == ASSIGNATION) {
                tokens.pop()
                tokens.pop()
                evaluate(tokens)
            }
        }
        tokens.pop()
    }
}