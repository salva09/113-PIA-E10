package view

import javax.swing.ImageIcon
import javax.swing.JCheckBox
import javax.swing.JOptionPane

fun welcome(): Boolean {
    val message: String = "<html>" +
            "Thank you for using my program! <br>" +
            "I hope that you don't face any bugs. <br>" +
            "You have some file examples with this program. <br><br>" +
            "Tip: If you don't know how to use this program <br>" +
            "go to About: How to use." +
            "</html>"
    val checkbox = JCheckBox("Show at startup")
    checkbox.isSelected = true
    val params = arrayOf<Any>(message, checkbox)
    JOptionPane.showMessageDialog(null, params, "Welcome!", JOptionPane.INFORMATION_MESSAGE)

    return checkbox.isSelected
}

fun howToUse() {
    val message: String = "<html>" +
            "This is a code editor for a specific language, it has <br>" +
            "a syntax highlighting and a code completion. <br><br>" +
            "Code completion: <br>" +
            "Press ctlr + space to use the code completion. <br><br>" +
            "File options: <br>" +
            "Open -> Pop ups a menu to select and open a file. <br>" +
            "Save -> Saves the current opened file. <br>" +
            "Exit -> Closes the window and exits the program. <br><br>" +
            "Edit options: <br>" +
            "Common edit options like everyone knows. <br><br>" +
            "Analyze option: <br>" +
            "Analyze language -> Analyzes the code using a <br>" +
            "recursive descent parser. <br><br>" +
            "There is a doc with the information of the language, <br>" +
            "give it a read. <br>" +
            "<html>"
    JOptionPane.showMessageDialog(null, message, "How to use", JOptionPane.QUESTION_MESSAGE)
}

fun aboutTeam() {
    val message: String = "<html>" +
            "<h1> Automata's theory. </h1> <br>" +
            "<h3> Teacher: Yazmany Jahaziel Guerrero Ceja. </h3> <br>" +
            "<h3> Team 10 </h3> <br>" +
            "Team's members: <br> <br>" +
            "Salvador Armando Hernández García 1860667 <br>" +
            "Edson Yael García Silva 1863860 <br>" +
            "María Eugenia Concepción Flores Rodríguez 1818538 <br> <br>" +
            "</html>"
    JOptionPane.showMessageDialog(null, message, "Info", JOptionPane.INFORMATION_MESSAGE)
}

fun aboutLicense() {
    val message: String = "<html>" +
            "<h5> This project is licensed under the MIT License </h5> <br>" +
            "<h6>Copyright 2020 salva09 <br>" +
            "<br>" +
            "Permission is hereby granted, free of charge, to any person obtaining a copy of <br>" +
            "this software and associated documentation files (the \"Software\"), to deal in <br>" +
            "the Software without restriction, including without limitation the rights to use, <br>" +
            "copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the <br>" +
            "Software, and to permit persons to whom the Software is furnished to do so, subject <br>" +
            "to the following conditions: <br>" +
            "<br>" +
            "The above copyright notice and this permission notice shall be included in all <br>" +
            "copies or substantial portions of the Software.<br>" +
            "<br>" +
            "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR <br>" +
            "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS <br>" +
            "FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR <br>" +
            "COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN <br>" +
            "AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION <br>" +
            "WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.</h6>" +
            "</html>"
    JOptionPane.showMessageDialog(null, message, "License", JOptionPane.QUESTION_MESSAGE)
}
