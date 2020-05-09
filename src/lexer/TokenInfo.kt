package lexer

import java.util.regex.Pattern

data class TokenInfo(var regex: Pattern, var token: Int = 0)
