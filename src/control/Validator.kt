package control

import lexer.*
import javax.swing.JOptionPane

private fun init() {
    add("programa", KEYWORD_1)
    add("iniciar", KEYWORD_2)
    add("terminar.", KEYWORD_3)
    add("imprimir|leer", FUNCTION)
    add("[a-z][a-z0-9_]*", VARIABLE)
    add("\\(", OPEN_BRACKET)
    add("\\)", CLOSE_BRACKET)
    add(":=", ASSIGNATION)
    add("[0-9]+", NUMBER)
    add("[+-]", PLUS_MINUS)
    add("[*/]", MULT_DIV)
    add("\\^", RAISED)
    add("-", MINUS)
    add("\n", LINE_BREAK)
    add(";", SEMICOLON)
    add("\\s", WHITESPACE)
    add("\t", TAB)
}

fun analyze(input: String) {
    init()
    try {
        tokenize(input)
        parse()

        JOptionPane.showMessageDialog(null, "The input is valid!", ":)", JOptionPane.INFORMATION_MESSAGE)
    } catch (ex: LanguageException) {
        JOptionPane.showMessageDialog(null, ex.localizedMessage, "Error", JOptionPane.ERROR_MESSAGE)
    }
}
