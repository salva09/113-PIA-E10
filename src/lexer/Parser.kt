package lexer

import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class SyntaxException(message: String) : Exception(message)

object Parser {
    private var line: Int = 0
    private var space by Delegates.notNull<Boolean>()
    private lateinit var programName: String
    private lateinit var tokensToParse: LinkedList<Token>
    private lateinit var variables: ArrayList<String>
    private lateinit var lookahead: Token

    fun parse(tokens: LinkedList<Token>) {
        tokensToParse = tokens.clone() as LinkedList<Token>
        line = 1
        space = false
        variables = ArrayList()
        lookahead = tokensToParse.first

        startOfProgram()
        start()
        instructions()
        end()

        if (lookahead.token != EPSILON)
            throw SyntaxException("The program must finish with 'terminar.' and an empty string")
    }

    private fun nextToken() {
        tokensToParse.pop()
        // at the end of input we return an epsilon token
        lookahead = if (tokensToParse.isEmpty()) Token(EPSILON, "") else tokensToParse.first

        if (space) {
            if (lookahead.token == WHITESPACE) {
                throw SyntaxException("At line $line: Number of whitespaces exceeds one")
            } else {
                space = false
            }
        }
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
                        throw SyntaxException("At line $line: Symbol 'programa' was expected, but empty string was found")
                    }
                    else -> {
                        throw SyntaxException("At line $line: Symbol 'programa' was expected, but '${lookahead.sequence}' was found")
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
                        throw SyntaxException("At line $line: Symbol 'iniciar' was expected, but empty string was found")
                    }
                    else -> {
                        throw SyntaxException("At line $line: Symbol 'iniciar' was expected, but '${lookahead.sequence}' was found")
                    }
                }
            }
        }
    }

    private tailrec fun instructions() {
        when (lookahead.token) {
            FUNCTION -> {
                read()
                print()
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
                                throw SyntaxException("At line $line: A instruction was expected, but empty string was found")
                            }
                            else -> {
                                throw SyntaxException("At line $line: A instruction was expected, but '${lookahead.sequence}' was found")
                            }
                        }
                    }
                }
            }
        }
        if (lookahead.token == FUNCTION) instructions()
        if (lookahead.token == VARIABLE) instructions()
    }

    private fun read() {
        if (lookahead.sequence == "leer") {
            nextToken()
            whitespace()
            variable()
            endOfLine()
            nextLine()
        }
    }

    private fun print() {
        if (lookahead.sequence == "imprimir") {
            nextToken()
            whitespace()
            if (lookahead.token == STRING) {
                nextToken()
            } else {
                e()
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
                        throw SyntaxException("At line $line: Symbol ':=' was expected, but empty string was found")
                    }
                    else -> {
                        throw SyntaxException("At line $line: Symbol ':=' was expected, but '${lookahead.sequence}' was found")
                    }
                }
            }
            else -> {
                nextToken()
                optionalWhitespace()
                e()
            }
        }
    }

    private fun e() {
        optionalWhitespace()
        y()
        optionalWhitespace()
        if (lookahead.token == MINUS || lookahead.sequence == "+") {
            ePrime()
        }
    }

    private tailrec fun ePrime() {
        nextToken()
        y()
        optionalWhitespace()
        if (lookahead.token == MINUS || lookahead.sequence == "+") {
            ePrime()
        }
    }

    private fun y() {
        j()
        optionalWhitespace()
        if (lookahead.token == MULT || lookahead.token == DIV) {
            yPrime()
        }
    }

    private tailrec fun yPrime() {
        nextToken()
        j()
        optionalWhitespace()
        if (lookahead.token == MULT || lookahead.token == DIV) {
            yPrime()
        }
    }

    private fun j() {
        g()
        optionalWhitespace()
        if (lookahead.token == RAISED) {
            jPrime()
        }
    }

    private tailrec fun jPrime() {
        nextToken()
        g()
        optionalWhitespace()
        if (lookahead.token == RAISED) {
            jPrime()
        }
    }

    private tailrec fun g() {
        optionalWhitespace()
        if (lookahead.token == MINUS || lookahead.token == PLUS) {
            nextToken()
            g()
        } else {
            c()
        }
    }

    private fun c() {
        if (lookahead.token == OPEN_BRACKET) {
            nextToken()
            e()
            if (lookahead.token == CLOSE_BRACKET) {
                nextToken()
            } else {
                if (lookahead.sequence.isEmpty()) {
                    throw SyntaxException("At line $line: Close brackets was expected, but empty string was found")
                } else {
                    throw SyntaxException("At line $line: Close brackets was expected, but '${lookahead.sequence}' was found")
                }
            }
        } else {
            if (lookahead.token == NUMBER) {
                nextToken()
            } else {
                if (lookahead.token == VARIABLE) {
                    if (variables.contains(lookahead.sequence)) {
                        nextToken()
                    } else {
                        throw SyntaxException("At line $line: Variable '${lookahead.sequence}' not declared")
                    }
                } else {
                    if (lookahead.sequence.isEmpty()) {
                        throw SyntaxException("At line $line: A value was expected, but empty string was found")
                    } else {
                        throw SyntaxException("At line $line: A value was expected, but '${lookahead.sequence}' was found")
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
                        throw SyntaxException("At line $line: Symbol 'terminar.' was expected, but empty string was found")
                    }
                    else -> {
                        throw SyntaxException("At line $line: Symbol 'terminar.' was expected, but '${lookahead.sequence}' was found")
                    }
                }
            }
        }
    }

    private fun name() {
        whitespace()
        when (lookahead.token) {
            VARIABLE -> {
                programName = lookahead.sequence
                nextToken()
            }
            else -> {
                when {
                    lookahead.sequence.isEmpty() -> {
                        throw SyntaxException("At line $line: A name was expected, but empty string was found")
                    }
                    else -> {
                        throw SyntaxException("At line $line: A name was expected, but '${lookahead.sequence}' was found")
                    }
                }
            }
        }
    }

    private fun variable() {
        when (lookahead.token) {
            VARIABLE -> {
                if (lookahead.sequence == programName) {
                    throw SyntaxException("At line $line: Variable name cannot be the same as program's name")
                }
                variables.add(lookahead.sequence)
                nextToken()
            }
            else -> {
                when {
                    lookahead.sequence.isEmpty() -> {
                        throw SyntaxException("At line $line: A variable was expected, but empty string was found")
                    }
                    else -> {
                        throw SyntaxException("At line $line: A variable was expected, but '${lookahead.sequence}' was found")
                    }
                }
            }
        }
    }

    private fun whitespace() {
        when (lookahead.token) {
            WHITESPACE -> {
                space = true
                nextToken()
            }
            else -> {
                when {
                    lookahead.sequence.isEmpty() -> {
                        throw SyntaxException("At line $line: A whitespace was expected, but empty string was found")
                    }
                    else -> {
                        throw SyntaxException("At line $line: A whitespace was expected, but '${lookahead.sequence}' was found")
                    }
                }
            }
        }
    }

    private fun optionalWhitespace() {
        if (lookahead.token == WHITESPACE) {
            space = true
            nextToken()
        }
    }

    private fun endOfLine() {
        if (lookahead.token == WHITESPACE) {
            throw SyntaxException("At line $line: Symbol ';' was expected, but whitespace was found")
        }
        when (lookahead.token) {
            SEMICOLON -> {
                nextToken()
            }
            else -> {
                when {
                    lookahead.sequence.isEmpty() -> {
                        throw SyntaxException("At line $line: Symbol ';' was expected, but empty string was found")
                    }
                    else -> {
                        throw SyntaxException("At line $line: Symbol ';' was expected, but '${lookahead.sequence}' was found")
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
                        throw SyntaxException("At line $line: Line break was expected")
                    }
                    else -> {
                        throw SyntaxException("At line $line: Line break was expected, but '${lookahead.sequence}' was found")
                    }
                }
            }
        }
    }

}
