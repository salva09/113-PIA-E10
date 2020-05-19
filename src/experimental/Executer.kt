package experimental

import evaluate
import lexer.*
import javax.swing.JOptionPane

fun run() {
    while (tokens.contains(Token(WHITESPACE, ""))) {
        tokens.remove(Token(WHITESPACE, ""))
    }
    while (tokens.first.token != ASSIGNATION) {
        tokens.pop()
    }
    tokens.pop()

    val message = "Result: ${evaluate(tokens)}"
    JOptionPane.showMessageDialog(null, message, "", JOptionPane.INFORMATION_MESSAGE)
}
