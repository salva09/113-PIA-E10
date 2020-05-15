package control

import lexer.*
import javax.swing.JOptionPane

private fun init() {
    addTokenInfo("programa", KEYWORD_1)
    addTokenInfo("iniciar", KEYWORD_2)
    addTokenInfo("terminar\\.", KEYWORD_3)
    addTokenInfo("imprimir|leer", FUNCTION)
    addTokenInfo("[0-9]+[a-z]+[a-z0-9]*", VARIABLE_NOT_VALID)
    addTokenInfo("[0-9]+", NUMBER)
    addTokenInfo("[a-z][a-z0-9]*", VARIABLE)
    addTokenInfo("\\(", OPEN_BRACKET)
    addTokenInfo("\\)", CLOSE_BRACKET)
    addTokenInfo(":=", ASSIGNATION)
    addTokenInfo("[+-]", PLUS_MINUS)
    addTokenInfo("[*/]", MULT_DIV)
    addTokenInfo("\\^", RAISED)
    addTokenInfo("-", MINUS)
    addTokenInfo("\n", LINE_BREAK)
    addTokenInfo(";", SEMICOLON)
    addTokenInfo("\\s", WHITESPACE)
}

fun analyze(input: String) {
    init()
    try {
        tokenize(input)
        parse()

        val output = "Right now there is not output"
        JOptionPane.showMessageDialog(null, "Output: $output", "The input given is valid!", JOptionPane.INFORMATION_MESSAGE)
    } catch (ex: LanguageException) {
        JOptionPane.showMessageDialog(null, ex.localizedMessage, "Error", JOptionPane.ERROR_MESSAGE)
    }
}
