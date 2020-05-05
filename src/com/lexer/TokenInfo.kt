package com.lexer

import java.util.regex.Pattern

class TokenInfo(regex: Pattern, token: Int) {
    public lateinit var regex: Pattern
    public var token: Int = 0

    init {
        this.regex = regex
        this.token = token
    }
}