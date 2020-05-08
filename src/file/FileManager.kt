package file

import javax.swing.JFileChooser
import javax.swing.JMenuItem
import java.io.File
import javax.swing.JOptionPane

private lateinit var fileChooser: JFileChooser
private lateinit var file: File
var fileName = ""
    private set

fun openFile(open_file: JMenuItem): Boolean {
    fileChooser = JFileChooser()
    fileChooser.showOpenDialog(open_file)

    return if (!fileChooser.selectedFile.name.endsWith(".txt")) {
        JOptionPane.showMessageDialog(null, "File type not supported", "Error", JOptionPane.ERROR_MESSAGE)
        false
    } else {
        file = fileChooser.selectedFile
        fileName = file.name
        true
    }
}

fun getFileContent(): String {
    return File(file.absolutePath).readText()
}

fun saveFile(modified_text: String) {
    File(file.absolutePath).writeText(modified_text)
}