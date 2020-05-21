package interpreter

import javax.swing.JOptionPane

fun readInput(): Long {
    val inputString = JOptionPane.showInputDialog(null, "")
    try {
        return inputString?.toLong() ?: throw Exception("Program execution interrupted")
    } catch (ex: NumberFormatException) {
        throw Exception("Input must be a number")
    }
}