package experimental

import lexer.*
import javax.swing.JOptionPane
import kotlin.collections.ArrayList

private val variables = ArrayList<Int>()
private var expressionResult = 0
private var sum = false
private var minus = false
private var mult = false
private var div = false
private var raised = false

fun run() {
    expressionResult = 0
    sum = false
    minus = false
    mult = false
    div = false
    raised = false

    while (tokens.contains(Token(16, ""))) {
        tokens.remove(Token(16, ""))
    }
    while (tokens.first.token != ASSIGNATION) {
        tokens.pop()
    }
    tokens.pop()

    E()

    val message = "Result: $expressionResult"
    JOptionPane.showMessageDialog(null, message, "", JOptionPane.INFORMATION_MESSAGE)
}

private fun E() {
    Y()
    if (tokens.first.token == MINUS) {
        minus = true
        tokens.pop()
        EPrime()
    } else {
        if (tokens.first.sequence == "+") {
            sum = true
            tokens.pop()
            EPrime()
        }
    }
}

private tailrec fun EPrime() {
    Y()
    if (tokens.first.token == MINUS) {
        minus = true
        tokens.pop()
        EPrime()
    } else {
        if (tokens.first.sequence == "+") {
            sum = true
            tokens.pop()
            EPrime()
        }
    }
}

private fun Y() {
    J()
    if (tokens.first.sequence == "/") {
        div = true
        tokens.pop()
        YPrime()
    } else {
        if (tokens.first.sequence == "*") {
            mult = true
            tokens.pop()
            YPrime()
        }
    }
}

private tailrec fun YPrime() {
    J()
    if (tokens.first.sequence == "/") {
        div = true
        tokens.pop()
        YPrime()
    } else {
        if (tokens.first.sequence == "*") {
            mult = true
            tokens.pop()
            YPrime()
        }
    }
}

private fun J() {
    G()
    if (tokens.first.token == RAISED) {
        JPrime()
    }
}

private tailrec fun JPrime() {
    G()
    if (tokens.first.token == RAISED) {
        JPrime()
    }
}

private tailrec fun G() {
    if (tokens.first.token == MINUS) {
        G()
    } else {
        C()
    }
}

private fun C() {
    if (tokens.first.token == OPEN_BRACKET) {
        E()
        if (tokens.first.token == CLOSE_BRACKET) {
            tokens.pop()
        }
    } else {
        if (tokens.first.token == NUMBER) {
            if (sum) {
                sum = false
                expressionResult += tokens.first.sequence.toInt()
            } else {
                if (minus) {
                    minus = false
                    expressionResult -= tokens.first.sequence.toInt()
                } else {
                    if (div) {
                        div = false
                        expressionResult /= tokens.first.sequence.toInt()
                    } else {
                        if (mult) {
                            mult = false
                            expressionResult *= tokens.first.sequence.toInt()
                        } else {
                            expressionResult = tokens.first.sequence.toInt()
                        }
                    }
                }
            }
            tokens.pop()
        } else {
            if (tokens.first.token == VARIABLE) {
                if (variables.contains(tokens.first.sequence.toInt())) {
                    tokens.pop()
                }
            }
        }
    }
}