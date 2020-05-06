package com.file

import javax.swing.JFileChooser
import javax.swing.JMenuItem
import java.io.File
import javax.swing.JOptionPane


class FileManager {
    companion object {
        private var file_chooser: JFileChooser? = null
        private lateinit var file: File
        private var file_name: String = ""

        fun openFile(open_file: JMenuItem): Boolean {
            file_chooser = JFileChooser()
            file_chooser!!.showOpenDialog(open_file)

            return if (!file_chooser!!.selectedFile.name.endsWith(".txt")) {
                JOptionPane.showMessageDialog(null, "File type not supported", "Error", JOptionPane.ERROR_MESSAGE)
                false
            } else {
                file = file_chooser!!.selectedFile
                file_name = file.name
                true
            }
        }

        fun getFileName(): String {
            return file_name
        }

        fun getFileContent(): String {
            return File(file.absolutePath).readText()
        }

        fun saveFile(modified_text: String): Unit {
            File(file.absolutePath).writeText(modified_text)
        }
    }
}