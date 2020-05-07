package com.lexer

import java.util.regex.Pattern

class TokenInfo(regex: Pattern, token: Int) {
    var regex = regex
    var token = 0

    init {
        this.token = token
    }
}