package interpreter

import javax.swing.JOptionPane

fun readInput(variable: String): Long {
    val inputString = JOptionPane.showInputDialog(null, "Variable: $variable")
    return inputString?.toLong() ?: throw Exception("Program execution interrupted")
}