package lexer

import control.LanguageException
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

private var tokeninfo = LinkedList<TokenInfo>()
var tokens =  LinkedList<Token>()

fun add(regex: String, token: Int) {
    tokeninfo.add(TokenInfo(Pattern.compile("^(" + regex + ")"), token))
}

fun tokenize(string: String) {
    tokens = LinkedList<Token>()
    if (string.isEmpty()) throw LanguageException("Expected some input, but empty string was found")

    var input = string
    var line = 1
    tokens.clear()

    while (!input.equals("")) {
        var match = false

        for (info in tokeninfo) {
            val m: Matcher = info.regex.matcher(input)

            if (m.find()) {
                match = true

                val tok: String = m.group().trim()
                tokens.add(Token(info.token, tok))

                if (info.token == Token.LINE_BREAK) line++

                input = m.replaceFirst("")
                break
            }
        }
        if (!match) {
            throw LanguageException("Lexicon error\n" +
                    "At line $line: Unexpected character : \"" + input[0] + "\""
            )
        }
    }
}