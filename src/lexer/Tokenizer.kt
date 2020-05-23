package lexer

import java.util.*
import java.util.regex.Pattern

class LexiconException(message: String) : Exception(message)

class Tokenizer() {
    private var tokeninfo = LinkedList<TokenInfo>()
    val tokens = LinkedList<Token>()
    private var input = ""
    private var line = 1

    fun addTokenInfo(regex: String, token: Int) {
        tokeninfo.add(TokenInfo(Pattern.compile("^($regex)"), token))
    }

    infix fun tokenize(string: String) {
        tokens.clear()
        if (string.isEmpty()) throw LexiconException("Expected a program, but empty string was found")

        input = string
        line = 1

        while (input != "") {
            var match = false

            for (info in tokeninfo) {
                match = findMatch(info)
                if (match) break
            }
            if (!match) {
                throw LexiconException("At line $line: Unexpected character: '${input[0]}'")
            }
        }
    }

    private fun findMatch(info: TokenInfo): Boolean {
        val matcher = info.regex.matcher(input)

        if (matcher.find()) {
            val sequence = matcher.group().trim()
            tokens.add(Token(info.token, sequence))

            if (info.token == VARIABLE_NOT_VALID)
                throw LexiconException("At line $line: Variable declaration not valid")
            if (info.token == LINE_BREAK) line++

            input = matcher.replaceFirst("")
            return true
        }
        return false
    }
}
