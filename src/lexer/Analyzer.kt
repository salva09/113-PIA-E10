package lexer

import java.util.*

private fun Tokenizer.setTokenInfo() {
    addTokenInfo("programa", KEYWORD_1)
    addTokenInfo("iniciar", KEYWORD_2)
    addTokenInfo("terminar\\.", KEYWORD_3)
    addTokenInfo("imprimir|leer", FUNCTION)
    addTokenInfo("\".*\"", STRING)
    addTokenInfo("[0-9]+[a-z]+[a-z0-9]*", VARIABLE_NOT_VALID)
    addTokenInfo("[0-9]+", NUMBER)
    addTokenInfo("[a-z][a-z0-9]*", VARIABLE)
    addTokenInfo("\\(", OPEN_BRACKET)
    addTokenInfo("\\)", CLOSE_BRACKET)
    addTokenInfo(":=", ASSIGNATION)
    addTokenInfo("\\+", PLUS)
    addTokenInfo("-", MINUS)
    addTokenInfo("\\*", MULT)
    addTokenInfo("/", DIV)
    addTokenInfo("\\^", RAISED)
    addTokenInfo("\n", LINE_BREAK)
    addTokenInfo(";", SEMICOLON)
    addTokenInfo("\\s", WHITESPACE)
}

fun analyze(input: String): LinkedList<Token> {
    Tokenizer.setTokenInfo()
    Tokenizer.tokenize(input)
    Parser.parse(Tokenizer.tokens)
    return Tokenizer.tokens
}
