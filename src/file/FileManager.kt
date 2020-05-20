package file

import java.io.File
import javax.swing.JFileChooser
import javax.swing.JMenuItem
import javax.swing.JOptionPane

private lateinit var fileChooser: JFileChooser
private var file: File? = null
var fileName = ""
    private set

fun openFile(openFile: JMenuItem): Boolean {
    fileChooser = JFileChooser()
    val result = fileChooser.showOpenDialog(openFile)

    if (result != 0) throw Exception("No file open")

    return if (!fileChooser.selectedFile.name.endsWith(".txt")) {
        JOptionPane.showMessageDialog(null, "File type not supported", "Error", JOptionPane.ERROR_MESSAGE)
        false
    } else {
        file = fileChooser.selectedFile
        fileName = file!!.name
        true
    }
}

fun getFileContent(): String {
    return File(file!!.absolutePath).readText()
}

fun saveFile(openFile: JMenuItem, modifiedText: String) {
    if (file != null) {
        File(file!!.absolutePath).writeText(modifiedText)
    } else {
        fileChooser = JFileChooser()
        fileChooser.dialogTitle = "Save file"
        val result = fileChooser.showSaveDialog(openFile)

        if (result == JFileChooser.APPROVE_OPTION) {
            file = File(fileChooser.selectedFile.absolutePath)
            file!!.writeText(modifiedText)
        }
    }
    fileName = file!!.name
}

fun saveFileAs(openFile: JMenuItem, modifiedText: String) {
    fileChooser = JFileChooser()
    fileChooser.dialogTitle = "Save file"
    val result = fileChooser.showSaveDialog(openFile)

    if (result == JFileChooser.APPROVE_OPTION) {
        file = File(fileChooser.selectedFile.absolutePath)
        file!!.writeText(modifiedText)
    }
    fileName = file!!.name
}
