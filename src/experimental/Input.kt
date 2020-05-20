package experimental

import javax.swing.JOptionPane

fun readInput(variable: String): Long {
    val inputString = JOptionPane.showInputDialog(null, "Variable: $variable")
    return inputString.toLong()
}