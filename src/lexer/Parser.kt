package lexer

import control.LanguageException
import kotlin.collections.ArrayList

private var line: Int = 0
private var activeDivision = false
private lateinit var variables: ArrayList<String>
private lateinit var lookahead: Token

fun parse() {
    line = 1
    variables = ArrayList()
    lookahead = tokens.first

    startOfProgram()
    start()
    instructions()
    end()

    while (lookahead.sequence.isEmpty() && lookahead.token != EPSILON) {
        nextToken()
    }
    if (lookahead.token != EPSILON)
        throw LanguageException("Syntax error\n" +
                "At line " + line + ": " + "Unexpected symbol \"" + lookahead.sequence + "\" was found")
}

private fun nextToken() {
    tokens.pop()
    // at the end of input we return an epsilon token
    lookahead = if (tokens.isEmpty()) Token(EPSILON, "") else tokens.first
}

private fun startOfProgram() {
    when (lookahead.token) {
        KEYWORD_1 -> {
            nextToken()
            name()
            endOfLine()
            nextLine()
        }
        else -> {
            when {
                lookahead.sequence.isEmpty() -> {
                    throw LanguageException("Syntax error\n" +
                            "At line $line: Symbol \"programa\" was expected, but empty string was found")
                }
                else -> {
                    throw LanguageException("Syntax error\n" +
                            "At line " + line + ": " + "Symbol \"programa\" was expected, but \"" +
                            lookahead.sequence + "\" was found")
                }
            }
        }
    }
}

private fun start() {
    when (lookahead.token) {
        KEYWORD_2 -> {
            nextToken()
            nextLine()
        }
        else -> {
            when {
                lookahead.sequence.isEmpty() -> {
                    throw LanguageException("Syntax error\n" +
                            "At line $line: Symbol \"iniciar\" was expected, but empty string was found")
                }
                else -> {
                    throw LanguageException("Syntax error\n" +
                            "At line $line: Symbol \"iniciar\" was expected, but \"" + lookahead.sequence +
                            "\" was found")
                }
            }
        }
    }
}

private fun instructions() {
    when (lookahead.token) {
        FUNCTION -> {
            leer()
            imprimir()
        }
        else -> {
            when (lookahead.token) {
                VARIABLE -> {
                    variable()
                    assignation()
                    endOfLine()
                    nextLine()
                }
                else -> {
                    when {
                        lookahead.sequence.isEmpty() -> {
                            throw LanguageException("Syntax error\n" +
                                    "At line $line: A instruction was expected, but empty string was found")
                        }
                        else -> {
                            throw LanguageException("Syntax error\n" +
                                    "At line $line: A instruction was expected, but \"" + lookahead.sequence +
                                    "\" was found")
                        }
                    }
                }
            }
        }
    }
    if (lookahead.token == FUNCTION) instructions()
    if (lookahead.token == VARIABLE) instructions()
}

private fun leer() {
    if (lookahead.sequence == "leer") {
        nextToken()
        whitespace()
        variable()
        endOfLine()
        nextLine()
    }
}

private fun imprimir() {
    if (lookahead.sequence == "imprimir") {
        nextToken()

        val argument: Boolean = try {
            whitespace()
            if (lookahead.token == VARIABLE) {
                if (variables.contains(lookahead.sequence)) {
                    nextToken()
                } else {
                    throw LanguageException("Syntax error\n" +
                            "At line $line: Variable not declared")
                }
            } else {
                throw ArithmeticException("Syntax error\n" +
                        "At line $line: A variable was expected, but \"" + lookahead.sequence + "\" was found")
            }
            true
        } catch (ex: LanguageException) {
            throw LanguageException("Syntax error\n" +
                    "At line $line: Variable not declared")
        } catch (ex: ArithmeticException) {
            false
        }

        if (!argument) {
            try {
                expression()
            } catch (ex: LanguageException) {
                throw LanguageException("Syntax error\n" +
                        "At line $line: Function imprimir is expecting an argument")
            }
        }

        endOfLine()
        nextLine()
    }
}

private fun assignation() {
    optionalWhitespace()
    when {
        lookahead.token != ASSIGNATION -> {
            when {
                lookahead.sequence.isEmpty() -> {
                    throw LanguageException("Syntax error\n" +
                            "At line $line: Symbol \":=\" was expected, but empty string was found")
                }
                else -> {
                    throw LanguageException("Syntax error\n" +
                            "At line $line: Symbol \":=\" was expected, but \"" + lookahead.sequence +
                            "\" was found")
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
    signedNumber()
    sumOperation()
}

private fun sumOperation() {
    when (lookahead.token) {
        PLUS_MINUS -> {
            nextToken()
            number()
            sumOperation()
        }
    }
}

private fun signedNumber() {
    when (lookahead.token) {
        PLUS_MINUS -> {
            nextToken()
            number()
        }
        else -> {
            number()
        }
    }
}

private fun number() {
    factor()
    numberOperation()
}

private fun numberOperation() {
    when (lookahead.token) {
        MULT_DIV -> {
            if (lookahead.sequence == "/") activeDivision = true
            nextToken()
            signedFactor()
            numberOperation()
        }
    }
}

private fun signedFactor() {
    when (lookahead.token) {
        PLUS_MINUS -> {
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
    factorOperation()
}

private fun factorOperation() {
    optionalWhitespace()
    when (lookahead.token) {
        RAISED -> {
            nextToken()
            signedFactor()
        }
    }
}

private fun argument() {
    when (lookahead.token) {
        OPEN_BRACKET -> {
            nextToken()
            expression()
            if (lookahead.token != CLOSE_BRACKET) {
                if (lookahead.sequence.isEmpty()) {
                    throw LanguageException("Syntax error\n" +
                            "At line $line: Close brackets was expected, but empty string was found")
                } else {
                    throw LanguageException("Syntax error\n" +
                            "At line $line: Close brackets was expected, but \"" + lookahead.sequence +
                            "\" was found")
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
    when (lookahead.token) {
        NUMBER -> {
            if (activeDivision and (lookahead.sequence == "0"))
                throw LanguageException("Arithmetic error\n" +
                        "At line $line: Divide by zero cannot be possible")
            else
                activeDivision = false
            nextToken()
        }
        else -> {
            when (lookahead.token) {
                VARIABLE -> {
                    if (variables.contains(lookahead.sequence)) {
                        nextToken()
                    } else {
                        throw LanguageException("Syntax error\n" +
                                "At line $line: Variable not declared")
                    }
                }
                else -> {
                    throw LanguageException("Syntax error\n" +
                            "At line $line: A value was expected, but empty string was found")
                }
            }
        }
    }
}

private fun end() {
    when (lookahead.token) {
        KEYWORD_3 -> {
            nextToken()
        }
        else -> {
            when {
                lookahead.sequence.isEmpty() -> {
                    throw LanguageException("Syntax error\n" +
                            "At line $line: Symbol \"terminar.\" was expected, but empty string was found")
                }
                else -> {
                    throw LanguageException("Syntax error\n" +
                            "At line $line: Symbol \"terminar.\" was expected, but \"" + lookahead.sequence +
                            "\" was found")
                }
            }
        }
    }
}

private fun name() {
    whitespace()
    when (lookahead.token) {
        VARIABLE -> {
            nextToken()
        }
        else -> {
            when {
                lookahead.sequence.isEmpty() -> {
                    throw LanguageException("Syntax error\n" +
                            "At line $line: A name was expected, but empty string was found")
                }
                else -> {
                    throw LanguageException("Syntax error\n" +
                            "At line $line: A name was expected, but \"" + lookahead.sequence + "\" was found")
                }
            }
        }
    }
}

private fun variable() {
    when (lookahead.token) {
        VARIABLE -> {
            variables.add(lookahead.sequence)
            nextToken()
        }
        else -> {
            when {
                lookahead.sequence.isEmpty() -> {
                    throw LanguageException("Syntax error\n" +
                            "At line $line: A variable was expected, but empty string was found")
                }
                else -> {
                    throw LanguageException("Syntax error\n" +
                            "At line $line: A variable was expected, but \"" + lookahead.sequence +
                            "\" was found")
                }
            }
        }
    }
}

private fun whitespace() {
    when (lookahead.token) {
        WHITESPACE -> {
            nextToken()
        }
        else -> {
            when {
                lookahead.sequence.isEmpty() -> {
                    throw LanguageException("Syntax error\n" +
                            "At line $line: A whitespace was expected, but empty string was found")
                }
                else -> {
                    throw LanguageException("Syntax error\n" +
                            "At line $line: A whitespace was expected, but \"" + lookahead.sequence +
                            "\" was found")
                }
            }
        }
    }
}

private fun optionalWhitespace() {
    if (lookahead.token == WHITESPACE) {
        nextToken()
    }
}

private fun endOfLine() {
    if (lookahead.token == WHITESPACE) {
        throw LanguageException("Syntax error\n" +
                "At line $line: Symbol \";\" was expected, but whitespace was found")
    }
    when (lookahead.token) {
        SEMICOLON -> {
            nextToken()
        }
        else -> {
            when {
                lookahead.sequence.isEmpty() -> {
                    throw LanguageException("Syntax error\n" +
                            "At line $line: Symbol \";\" was expected, but empty string was found")
                }
                else -> {
                    throw LanguageException("Syntax error\n" +
                            "At line $line: Symbol \";\" was expected, but \"" + lookahead.sequence + "\" was found")
                }
            }
        }
    }
}

private fun nextLine() {
    when (lookahead.token) {
        LINE_BREAK -> {
            line++
            nextToken()
        }
        else -> {
            when {
                lookahead.sequence.isEmpty() -> {
                    throw LanguageException("Syntax error\n" +
                            "At line $line: Line break was expected")
                }
                else -> {
                    throw LanguageException("Syntax error\n" +
                            "At line $line: Line break was expected, but \"" + lookahead.sequence + "\" was found")
                }
            }
        }
    }
}
