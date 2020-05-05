package com.lexer

class Token(token: Int, sequence: String?) {
    companion object {
        const val EPSILON = 0
        const val KEYWORD_1 = 1
        const val KEYWORD_2 = 2
        const val KEYWORD_3 = 3
        const val FUNCTION = 4
        const val NAME = 5
        const val OPEN_BRACKET = 6
        const val CLOSE_BRACKET = 7
        const val ASSIGNATION = 8
        const val NUMBER = 9
        const val PLUS_MINUS = 10
        const val MULT_DIV = 11
        const val RAISED = 12
        const val MINUS = 13
        const val LINE_BREAK = 14
        const val SEMICOLON = 15
        const val WHITESPACE = 16
        const val TAB = 17
    }

    var token = 0
    var sequence: String? = null

    init {
        this.token = token
        this.sequence = sequence
    }
}