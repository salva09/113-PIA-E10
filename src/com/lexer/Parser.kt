package com.lexer

import jdk.nashorn.internal.runtime.ParserException
import java.util.*

class Parser {
    private var line: Int = 1
    private var number_assigned: Boolean = false
    private var tokens: LinkedList<Token>? = null
    private var lookahead: Token? = null

    public fun parse(tokens: LinkedList<Token>?) {
        this.tokens = tokens?.clone() as LinkedList<Token>?
        lookahead = this.tokens!!.first

        startOfProgram()
        start()
        instructions()
        end()

        if (lookahead!!.token != Token.EPSILON) {
            if (!lookahead!!.sequence?.isEmpty()!!)
                throw ParserException("At line " + line + ": " + "Unexpected symbol \"" + lookahead!!.sequence + "\" was found")
        }
    }

    private fun nextToken() {
        tokens!!.pop()
        // at the end of input we return an epsilon token
        lookahead = if (tokens!!.isEmpty()) Token(Token.EPSILON, "") else tokens!!.first
    }

    private fun startOfProgram() {
        if (lookahead!!.token == Token.KEYWORD_1) {
            nextToken()
            name()
            endOfLine()
            nextLine()
        }
        else {
            if (lookahead!!.sequence?.isEmpty()!!) {
                throw ParserException("At line " + line + ": " + "Symbol \"programa\" was expected, but empty string was found")
            }
            else {
                throw ParserException("At line " + line + ": " + "Symbol \"programa\" was expected, but \"" + lookahead!!.sequence + "\" was found")
            }
        }
    }

    private fun start() {
        if (lookahead!!.token == Token.KEYWORD_2) {
            nextToken()
            nextLine()
        }
        else {
            if (lookahead!!.sequence?.isEmpty()!!) {
                throw ParserException("At line " + line + ": " + "Symbol \"iniciar\" was expected, but empty string was found")
            }
            else {
                throw ParserException("At line " + line + ": " + "Symbol \"iniciar\" was expected, but \"" + lookahead!!.sequence + "\" was found")
            }
        }
    }

    private fun instructions() {
        if (lookahead!!.token == Token.FUNCTION) {
            when (lookahead!!.sequence) {
                "leer" -> {
                    nextToken()
                    name()
                    endOfLine()
                    nextLine()
                }
                "imprimir" -> {
                    nextToken()
                    name()
                    endOfLine()
                    nextLine()
                }
                else -> {
                    throw ParserException("At line " + line + ": " + "Something went wrong")
                }
            }
        }
        else {
            if (lookahead!!.token == Token.NAME) {
                nextToken()
                assignation()
                endOfLine()
                nextLine()
            }
            else {
                if (lookahead!!.sequence?.isEmpty()!!) {
                    throw ParserException("At line " + line + ": " + "A instruction was expected, but empty string was found")
                }
                else {
                    throw ParserException("At line " + line + ": " + "A instruction was expected, but \"" + lookahead!!.sequence + "\" was found")
                }
            }
        }
        try {
            instructions()
        }
        catch (ex: ParserException) { }
    }

    private fun assignation() {
        optionalWhitespace()
        if (lookahead!!.token == Token.ASSIGNATION) {
            nextToken()
            optionalWhitespace()
            expression()
        }
        else {
            if (lookahead!!.sequence?.isEmpty()!!) {
                throw ParserException("At line " + line + ": " + "Symbol \":=\" was expected, but empty string was found")
            }
            else {
                throw ParserException("At line " + line + ": " + "Symbol \":=\" was expected, but \"" + lookahead!!.sequence + "\" was found")
            }
        }
    }

    private fun expression() {
        signedTerm()
        sumOp()
    }

    private fun sumOp() {
        if (lookahead!!.token == Token.PLUS_MINUS) {
            nextToken()
            term()
            sumOp()
        }
        else{

        }
    }

    private fun signedTerm() {
        if (lookahead!!.token == Token.PLUS_MINUS) {
            nextToken()
            term()
        }
        else {
            term()
        }
    }

    private fun term() {
        factor()
        termOp()
    }

    private fun termOp() {
        optionalWhitespace()
        if (lookahead!!.token == Token.MULT_DIV) {
            nextToken()
            signedFactor()
            termOp()
        }
        else {

        }
    }

    private fun signedFactor() {
        if (lookahead!!.token == Token.PLUS_MINUS) {
            nextToken()
            factor()
        }
        else {
            factor()
        }
    }

    private fun factor() {
        optionalWhitespace()
        argument()
        factorOp()
    }

    private fun factorOp() {
        if (lookahead!!.token == Token.RAISED) {
            nextToken()
            signedFactor()
        }
        else {

        }
    }

    private fun argument() {
        if (lookahead!!.token == Token.OPEN_BRACKET) {
            nextToken()
            expression()
            if (lookahead!!.token != Token.CLOSE_BRACKET) {
                if(lookahead!!.sequence?.isEmpty()!!) {
                    throw ParserException("Close brackets was expected, but empty string was found")
                }
                else {
                    throw ParserException("Close brackets was expected, but \"" + lookahead!!.sequence + "\" was found")
                }
            }
            nextToken();
        }
        else {
            value()
        }
    }

    private fun value() {
        if (lookahead!!.token == Token.NUMBER) {
            nextToken()
        }
        else {
            if (lookahead!!.token == Token.NAME) {
                nextToken()
            }
            else {
                throw ParserException("At line " + line + ": " + "A value was expected, but empty string was found")
            }
        }
    }

    private fun end() {
        if (lookahead!!.token == Token.KEYWORD_3) {
            nextToken()
        }
        else {
            if (lookahead!!.sequence?.isEmpty()!!) {
                throw ParserException("At line " + line + ": " + "Symbol \"terminar.\" was expected, but empty string was found")
            }
            else {
                throw ParserException("At line " + line + ": " + "Symbol \"terminar.\" was expected, but \"" + lookahead!!.sequence + "\" was found")
            }
        }
    }

    private fun name() {
        whitespace()
        if (lookahead!!.token == Token.NAME) {
            nextToken()
        }
        else {
            if (lookahead!!.sequence?.isEmpty()!!) {
                throw ParserException("At line " + line + ": " + "A name was expected, but empty string was found")
            }
            else {
                throw ParserException("At line " + line + ": " + "A name was expected, but \"" + lookahead!!.sequence + "\" was found")
            }
        }
    }

    private fun whitespace() {
        if (lookahead!!.token == Token.WHITESPACE) {
            nextToken()
        }
        else {
            if (lookahead!!.sequence?.isEmpty()!!) {
                throw ParserException("At line " + line + ": " + "A whitespace was expected, but empty string was found")
            }
            else {
                throw ParserException("At line " + line + ": " + "A whitespace was expected, but \"" + lookahead!!.sequence + "\" was found")
            }
        }
    }

    private fun optionalWhitespace() {
        if (lookahead!!.token == Token.WHITESPACE) {
            nextToken()
        }
    }

    private fun endOfLine() {
        if (lookahead!!.token == Token.SEMICOLON) {
            nextToken()
        }
        else {
            if (lookahead!!.sequence?.isEmpty()!!) {
                throw ParserException("At line " + line + ": " + "Symbol \";\" was expected, but empty string was found")
            }
            else {
                throw ParserException("At line " + line + ": " + "Symbol \";\" was expected, but \"" + lookahead!!.sequence + "\" was found")
            }
        }
    }

    private fun nextLine() {
        if (lookahead!!.token == Token.LINE_BREAK) {
            line++
            nextToken()
        }
        else {
            if (lookahead!!.sequence?.isEmpty()!!) {
                throw ParserException("At line " + line + ": " + "Line break was expected")
            }
            else {
                throw ParserException("At line " + line + ": " + "Line break was expected, but \"" + lookahead!!.sequence + "\" was found")
            }
        }
    }
}
