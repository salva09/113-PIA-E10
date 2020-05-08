package control

import lexer.Token
import lexer.Tokenizer
import lexer.parse
import javax.swing.JOptionPane

class Validator {
    private var tokenizer = Tokenizer()

    init {
        tokenizer.add("programa", Token.KEYWORD_1)
        tokenizer.add("iniciar", Token.KEYWORD_2)
        tokenizer.add("terminar.", Token.KEYWORD_3)
        tokenizer.add("imprimir|leer", Token.FUNCTION)
        tokenizer.add("[a-z][a-z0-9_]*", Token.VARIABLE)
        tokenizer.add("\\(", Token.OPEN_BRACKET)
        tokenizer.add("\\)", Token.CLOSE_BRACKET)
        tokenizer.add(":=", Token.ASSIGNATION)
        tokenizer.add("[0-9]+", Token.NUMBER)
        tokenizer.add("[+-]", Token.PLUS_MINUS)
        tokenizer.add("[*/]", Token.MULT_DIV)
        tokenizer.add("\\^", Token.RAISED)
        tokenizer.add("-", Token.MINUS)
        tokenizer.add("\n", Token.LINE_BREAK)
        tokenizer.add(";", Token.SEMICOLON)
        tokenizer.add("\\s", Token.WHITESPACE)
        tokenizer.add("\t", Token.TAB)
    }

    fun analyze(input: String) {
        try {
            tokenizer.tokenize(input)

            parse(tokenizer.getTokens())

            JOptionPane.showMessageDialog(null, "The input is valid!", ":)", JOptionPane.INFORMATION_MESSAGE)
        } catch (ex: Exception) {
            JOptionPane.showMessageDialog(null, ex.localizedMessage, "Error", JOptionPane.ERROR_MESSAGE)
        }
    }
}