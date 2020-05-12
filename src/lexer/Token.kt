package lexer

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
const val PLUS_MINUS = 10
const val MULT_DIV = 11
const val RAISED = 12
const val MINUS = 13
const val LINE_BREAK = 14
const val SEMICOLON = 15
const val WHITESPACE = 16
const val VARIABLE_NOT_VALID = 17

data class Token(var token: Int = 0, var sequence: String = "")
