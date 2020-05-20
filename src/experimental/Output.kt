package experimental

import javax.swing.JOptionPane

fun showOutput(value: Long?) {
    var output = "Output: $value"
    JOptionPane.showMessageDialog(null, output, "", JOptionPane.INFORMATION_MESSAGE)
}