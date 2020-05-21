package interpreter

import javax.swing.JOptionPane

fun readInput(): Long {
    val inputString = JOptionPane.showInputDialog(null, "")
    try {
        return inputString?.toLong() ?: throw RuntimeException("Program execution interrupted")
    } catch (ex: NumberFormatException) {
        throw RuntimeException("Input must be a number")
    }
}