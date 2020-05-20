package lexer

import java.util.regex.Pattern

const val EPSILON = 0
const val KEYWORD_1 = 1
const val KEYWORD_2 = 2
const val KEYWORD_3 = 3
const val FUNCTION = 4
const val VARIABLE = 5
const val OPEN_BRACKET = 6
const val CLOSE_BRACKET = 7
const val ASSIGNATION = 8
const val NUMBER = 9
const val PLUS = 10
const val MINUS = 11
const val MULT = 12
const val DIV = 13
const val RAISED = 14
const val LINE_BREAK = 15
const val SEMICOLON = 16
const val WHITESPACE = 17
const val VARIABLE_NOT_VALID = 18

data class Token(var token: Int = 0, var sequence: String = "")

data class TokenInfo(val regex: Pattern, val token: Int = 0)
