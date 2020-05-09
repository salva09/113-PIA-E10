package control

import lexer.Token
import lexer.add
import lexer.tokenize
import lexer.tokens
import lexer.parse
import javax.swing.JOptionPane

private fun init() {
    add("programa", Token.KEYWORD_1)
    add("iniciar", Token.KEYWORD_2)
    add("terminar.", Token.KEYWORD_3)
    add("imprimir|leer", Token.FUNCTION)
    add("[a-z][a-z0-9_]*", Token.VARIABLE)
    add("\\(", Token.OPEN_BRACKET)
    add("\\)", Token.CLOSE_BRACKET)
    add(":=", Token.ASSIGNATION)
    add("[0-9]+", Token.NUMBER)
    add("[+-]", Token.PLUS_MINUS)
    add("[*/]", Token.MULT_DIV)
    add("\\^", Token.RAISED)
    add("-", Token.MINUS)
    add("\n", Token.LINE_BREAK)
    add(";", Token.SEMICOLON)
    add("\\s", Token.WHITESPACE)
    add("\t", Token.TAB)
}

fun analyze(input: String) {
    init()
    try {
        tokenize(input)

        parse()

        JOptionPane.showMessageDialog(null, "The input is valid!", ":)", JOptionPane.INFORMATION_MESSAGE)
    } catch (ex: Exception) {
        JOptionPane.showMessageDialog(null, ex.localizedMessage, "Error", JOptionPane.ERROR_MESSAGE)
    }
}