package experimental

import javax.swing.JOptionPane

fun showOutput(values: ArrayList<Int?>) {
    var output = "Output:\n"
    values.forEach { output += "$it\n" }
    JOptionPane.showMessageDialog(null, output, "", JOptionPane.INFORMATION_MESSAGE)
}