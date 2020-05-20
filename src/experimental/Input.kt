package experimental

import javax.swing.JOptionPane

fun readInput(variable: String): Int {
    val inputString = JOptionPane.showInputDialog(null, "Variable: $variable")
    return inputString.toInt()
}