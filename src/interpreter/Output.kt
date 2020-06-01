package interpreter

import javax.swing.JOptionPane

fun showOutput(value: Long?) {
    val output = "$value"
    JOptionPane.showMessageDialog(null, output, "", JOptionPane.INFORMATION_MESSAGE)
}

fun showOutput(value: String) {
    val output = value.drop(1).dropLast(1)
    JOptionPane.showMessageDialog(null, output, "", JOptionPane.INFORMATION_MESSAGE)
}
