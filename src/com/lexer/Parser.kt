package com.lexer

import com.control.LanguageException
import java.util.*

class Parser {
    private var line: Int = 1
    private var active_division = false
    private var tokens: LinkedList<Token>? = null
    private var lookahead: Token? = null

    fun parse(tokens: LinkedList<Token>?) {
        this.tokens = tokens?.clone() as LinkedList<Token>?
        lookahead = this.tokens!!.first

        startOfProgram()
        start()
        instructions()
        end()

        if (lookahead!!.token != Token.EPSILON && !lookahead!!.sequence?.isEmpty()!!)
            throw LanguageException("At line " + line + ": " + "Unexpected symbol \"" + lookahead!!.sequence + "\" was found")
    }

    private fun nextToken() {
        tokens!!.pop()
        // at the end of input we return an epsilon token
        lookahead = if (tokens!!.isEmpty()) Token(Token.EPSILON, "") else tokens!!.first
    }

    private fun startOfProgram() {
        when (lookahead!!.token) {
            Token.KEYWORD_1 -> {
                nextToken()
                name()
                endOfLine()
                nextLine()
            }
            else -> {
                when {
                    lookahead!!.sequence?.isEmpty()!! -> {
                        throw LanguageException("At line " + line + ": " + "Symbol \"programa\" was expected, but empty string was found")
                    }
                    else -> {
                        throw LanguageException("At line " + line + ": " + "Symbol \"programa\" was expected, but \"" + lookahead!!.sequence + "\" was found")
                    }
                }
            }
        }
    }

    private fun start() {
        when (lookahead!!.token) {
            Token.KEYWORD_2 -> {
                nextToken()
                nextLine()
            }
            else -> {
                when {
                    lookahead!!.sequence?.isEmpty()!! -> {
                        throw LanguageException("At line " + line + ": " + "Symbol \"iniciar\" was expected, but empty string was found")
                    }
                    else -> {
                        throw LanguageException("At line " + line + ": " + "Symbol \"iniciar\" was expected, but \"" + lookahead!!.sequence + "\" was found")
                    }
                }
            }
        }
    }

    private fun instructions() {
        when (lookahead!!.token) {
            Token.FUNCTION -> {
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
                        throw LanguageException("At line " + line + ": " + "Something went wrong")
                    }
                }
            }
            else -> {
                when (lookahead!!.token) {
                    Token.NAME -> {
                        nextToken()
                        assignation()
                        endOfLine()
                        nextLine()
                    }
                    else -> {
                        when {
                            lookahead!!.sequence?.isEmpty()!! -> {
                                throw LanguageException("At line " + line + ": " + "A instruction was expected, but empty string was found")
                            }
                            else -> {
                                throw LanguageException("At line " + line + ": " + "A instruction was expected, but \"" + lookahead!!.sequence + "\" was found")
                            }
                        }
                    }
                }
            }
        }
        if (lookahead!!.token == Token.FUNCTION or Token.NAME) instructions()
    }

    private fun assignation() {
        optionalWhitespace()
        when {
            lookahead!!.token != Token.ASSIGNATION -> {
                when {
                    lookahead!!.sequence?.isEmpty()!! -> {
                        throw LanguageException("At line " + line + ": " + "Symbol \":=\" was expected, but empty string was found")
                    }
                    else -> {
                        throw LanguageException("At line " + line + ": " + "Symbol \":=\" was expected, but \"" + lookahead!!.sequence + "\" was found")
                    }
                }
            }
            else -> {
                nextToken()
                optionalWhitespace()
                expression()
            }
        }
    }

    private fun expression() {
        signedTerm()
        sumOp()
    }

    private fun sumOp() {
        when (lookahead!!.token) {
            Token.PLUS_MINUS -> {
                nextToken()
                term()
                sumOp()
            }
        }
    }

    private fun signedTerm() {
        when (lookahead!!.token) {
            Token.PLUS_MINUS -> {
                nextToken()
                term()
            }
            else -> {
                term()
            }
        }
    }

    private fun term() {
        factor()
        termOp()
    }

    private fun termOp() {
        optionalWhitespace()
        when (lookahead!!.token) {
            Token.MULT_DIV -> {
                if (lookahead!!.sequence.equals("/")) active_division = true
                nextToken()
                signedFactor()
                termOp()
            }
        }
    }

    private fun signedFactor() {
        when (lookahead!!.token) {
            Token.PLUS_MINUS -> {
                nextToken()
                factor()
            }
            else -> {
                factor()
            }
        }
    }

    private fun factor() {
        optionalWhitespace()
        argument()
        factorOp()
    }

    private fun factorOp() {
        when (lookahead!!.token) {
            Token.RAISED -> {
                nextToken()
                signedFactor()
            }
        }
    }

    private fun argument() {
        when (lookahead!!.token) {
            Token.OPEN_BRACKET -> {
                nextToken()
                expression()
                if (lookahead!!.token != Token.CLOSE_BRACKET) {
                    if(lookahead!!.sequence?.isEmpty()!!) {
                        throw LanguageException("Close brackets was expected, but empty string was found")
                    } else {
                        throw LanguageException("Close brackets was expected, but \"" + lookahead!!.sequence + "\" was found")
                    }
                }
                nextToken()
            }
            else -> {
                value()
            }
        }
    }

    private fun value() {
        when (lookahead!!.token) {
            Token.NUMBER -> {
                if (active_division and lookahead!!.sequence.equals("0"))
                    throw LanguageException("At line " + line + ": Divide by zero cannot be possible")
                else
                    active_division = false
                nextToken()
            }
            else -> {
                when (lookahead!!.token) {
                    Token.NAME -> {
                        nextToken()
                    }
                    else -> {
                        throw LanguageException("At line " + line + ": " + "A value was expected, but empty string was found")
                    }
                }
            }
        }
    }

    private fun end() {
        when (lookahead!!.token) {
            Token.KEYWORD_3 -> {
                nextToken()
            }
            else -> {
                when {
                    lookahead!!.sequence?.isEmpty()!! -> {
                        throw LanguageException("At line " + line + ": " + "Symbol \"terminar.\" was expected, but empty string was found")
                    }
                    else -> {
                        throw LanguageException("At line " + line + ": " + "Symbol \"terminar.\" was expected, but \"" + lookahead!!.sequence + "\" was found")
                    }
                }
            }
        }
    }

    private fun name() {
        whitespace()
        when (lookahead!!.token) {
            Token.NAME -> {
                nextToken()
            }
            else -> {
                when {
                    lookahead!!.sequence?.isEmpty()!! -> {
                        throw LanguageException("At line " + line + ": " + "A name was expected, but empty string was found")
                    }
                    else -> {
                        throw LanguageException("At line " + line + ": " + "A name was expected, but \"" + lookahead!!.sequence + "\" was found")
                    }
                }
            }
        }
    }

    private fun whitespace() {
        when (lookahead!!.token) {
            Token.WHITESPACE -> {
                nextToken()
            }
            else -> {
                when {
                    lookahead!!.sequence?.isEmpty()!! -> {
                        throw LanguageException("At line " + line + ": " + "A whitespace was expected, but empty string was found")
                    }
                    else -> {
                        throw LanguageException("At line " + line + ": " + "A whitespace was expected, but \"" + lookahead!!.sequence + "\" was found")
                    }
                }
            }
        }
    }

    private fun optionalWhitespace() {
        if (lookahead!!.token == Token.WHITESPACE) {
            nextToken()
        }
    }

    private fun endOfLine() {
        when (lookahead!!.token) {
            Token.SEMICOLON -> {
                nextToken()
            }
            else -> {
                when {
                    lookahead!!.sequence?.isEmpty()!! -> {
                        throw LanguageException("At line " + line + ": " + "Symbol \";\" was expected, but empty string was found")
                    }
                    else -> {
                        throw LanguageException("At line " + line + ": " + "Symbol \";\" was expected, but \"" + lookahead!!.sequence + "\" was found")
                    }
                }
            }
        }
    }

    private fun nextLine() {
        when (lookahead!!.token) {
            Token.LINE_BREAK -> {
                line++
                nextToken()
            }
            else -> {
                when {
                    lookahead!!.sequence?.isEmpty()!! -> {
                        throw LanguageException("At line " + line + ": " + "Line break was expected")
                    }
                    else -> {
                        throw LanguageException("At line " + line + ": " + "Line break was expected, but \"" + lookahead!!.sequence + "\" was found")
                    }
                }
            }
        }
    }
}
