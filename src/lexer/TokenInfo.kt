package lexer

import java.util.regex.Pattern

data class TokenInfo(val regex: Pattern, val token: Int = 0)
