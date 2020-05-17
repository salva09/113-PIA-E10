package experimental

import java.util.*
import javax.swing.JOptionPane

fun run(tokens: LinkedList<lexer.Token>) {
    println(tokens)
    JOptionPane.showMessageDialog(null, tokens, "", JOptionPane.INFORMATION_MESSAGE)
}
