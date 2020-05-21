package lexer

import java.util.*
import java.util.regex.Pattern

class Tokenizer() {
    private var tokeninfo = LinkedList<TokenInfo>()
    val tokens = LinkedList<Token>()

    fun addTokenInfo(regex: String, token: Int) {
        tokeninfo.add(TokenInfo(Pattern.compile("^($regex)"), token))
    }

    infix fun tokenize(string: String) {
        tokens.clear()
        if (string.isEmpty()) throw ParserException("Expected some input, but empty string was found")

        var input = string
        var line = 1

        while (input != "") {
            var match = false

            for (info in tokeninfo) {
                val matcher = info.regex.matcher(input)

                if (matcher.find()) {
                    match = true

                    val sequence = matcher.group().trim()
                    tokens.add(Token(info.token, sequence))

                    if (info.token == VARIABLE_NOT_VALID)
                        throw ParserException("Lexicon error\n" +
                                "At line $line: Variable declaration not valid"
                        )
                    if (info.token == LINE_BREAK) line++

                    input = matcher.replaceFirst("")
                    break
                }
            }
            if (!match) {
                throw ParserException("Lexicon error\n" +
                        "At line $line: Unexpected character: '${input[0]}'"
                )
            }
        }
    }

}
