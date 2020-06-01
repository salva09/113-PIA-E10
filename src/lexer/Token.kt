package lexer

import java.util.regex.Pattern

const val EPSILON = 0
const val KEYWORD_1 = 1
const val KEYWORD_2 = 2
const val KEYWORD_3 = 3
const val FUNCTION = 4
const val STRING = 5
const val VARIABLE = 6
const val OPEN_BRACKET = 7
const val CLOSE_BRACKET = 8
const val ASSIGNATION = 9
const val NUMBER = 10
const val PLUS = 11
const val MINUS = 12
const val MULT = 13
const val DIV = 14
const val RAISED = 15
const val LINE_BREAK = 16
const val SEMICOLON = 17
const val WHITESPACE = 18
const val VARIABLE_NOT_VALID = 19

data class Token(var token: Int = 0, var sequence: String = "")

data class TokenInfo(val regex: Pattern, val token: Int = 0)
