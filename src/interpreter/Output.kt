package interpreter

import javax.swing.JOptionPane

fun showOutput(value: Long?) {
    val output = "$value"
    JOptionPane.showMessageDialog(null, output, "", JOptionPane.INFORMATION_MESSAGE)
}
