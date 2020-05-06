package com.control

import com.lexer.Parser
import com.lexer.Token
import com.lexer.Tokenizer
import jdk.nashorn.internal.runtime.ParserException
import javax.swing.JOptionPane

class Validator {
    private var tokenizer: Tokenizer = Tokenizer()

    init {

        tokenizer.add("programa", Token.KEYWORD_1)
        tokenizer.add("iniciar", Token.KEYWORD_2)
        tokenizer.add("terminar.", Token.KEYWORD_3)
        tokenizer.add("imprimir|leer", Token.FUNCTION)
        tokenizer.add("[a-z][a-z0-9_]*", Token.NAME)
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

            val parser = Parser()
            parser.parse(tokenizer.getTokens())

            JOptionPane.showMessageDialog(null, "The input is valid!", ":)", JOptionPane.INFORMATION_MESSAGE)
        }
        catch (ex: ParserException) {
            JOptionPane.showMessageDialog(null, ex.localizedMessage, "Error", JOptionPane.ERROR_MESSAGE)
        }
    }
}