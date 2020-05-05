package com.lexer

import jdk.nashorn.internal.runtime.ParserException
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class Tokenizer {
    private lateinit var tokeninfo: LinkedList<TokenInfo>
    private lateinit var tokens: LinkedList<Token>
    private var output: String = ""

    init {
        tokeninfo = LinkedList()
        tokens = LinkedList()
    }

    fun add(regex: String, token: Int) {
        tokeninfo.add(TokenInfo(Pattern.compile("^("+regex+")"), token))
    }

    fun tokenize(string: String) {
        if (string.isEmpty()) throw ParserException("Expected some input, but empty string was found")

        var input: String = string
        var line: Int = 1
        tokens.clear()

        while (!input.equals("")) {
            var match: Boolean = false

            for (info in tokeninfo) {
                val m: Matcher = info.regex.matcher(input)

                if (m.find()) {
                    match = true

                    val tok: String = m.group().trim()
                    tokens.add(Token(info.token, tok))

                    if (info.token == Token.LINE_BREAK) line++

                    input = m.replaceFirst("")
                    break
                }
            }
            if (!match) {
                throw ParserException("At line " + line + ": " + "Unexpected character: \"" + input + "\"")
            }
        }
    }

    fun getTokens(): LinkedList<Token> {
        return tokens
    }

    fun getOutput(): String {
        return output
    }
}