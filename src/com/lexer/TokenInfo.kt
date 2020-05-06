package com.lexer

import java.util.regex.Pattern

class TokenInfo(regex: Pattern, token: Int) {
    var regex: Pattern = regex
    var token: Int = 0

    init {
        this.token = token
    }
}