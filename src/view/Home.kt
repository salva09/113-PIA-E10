package view

import control.Validator
import file.FileManager.Companion.fileName
import file.FileManager.Companion.getFileContent
import file.FileManager.Companion.openFile
import file.FileManager.Companion.saveFile
import org.fife.ui.autocomplete.AutoCompletion
import org.fife.ui.autocomplete.BasicCompletion
import org.fife.ui.autocomplete.CompletionProvider
import org.fife.ui.autocomplete.DefaultCompletionProvider
import org.fife.ui.rsyntaxtextarea.*
import org.fife.ui.rtextarea.RTextArea
import org.fife.ui.rtextarea.RTextScrollPane
import java.awt.BorderLayout
import java.awt.Color
import java.io.File
import javax.swing.*

class Home : JFrame() {
    private var textArea: RSyntaxTextArea

    private fun createMenuBar() {
        val menuBar = JMenuBar()
        val fileMenu = JMenu("File")
        val openFile = JMenuItem("Open file")
        val saveFile = JMenuItem("Save file")
        val exit = JMenuItem("Exit")
        openFile.addActionListener {
            if (openFile(openFile)) {
                textArea.text = getFileContent()
                title = fileName + "~113-PIA-E10"
                textArea.discardAllEdits()
            }
        }
        saveFile.addActionListener { saveFile(textArea.text) }
        exit.addActionListener { System.exit(0) }
        fileMenu.add(openFile)
        fileMenu.add(saveFile)
        fileMenu.add(exit)
        menuBar.add(fileMenu)

        val editMenu = JMenu("Edit")
        editMenu.add(createMenuItem(RTextArea.getAction(RTextArea.UNDO_ACTION)))
        editMenu.add(createMenuItem(RTextArea.getAction(RTextArea.REDO_ACTION)))
        editMenu.addSeparator()
        editMenu.add(createMenuItem(RTextArea.getAction(RTextArea.CUT_ACTION)))
        editMenu.add(createMenuItem(RTextArea.getAction(RTextArea.COPY_ACTION)))
        editMenu.add(createMenuItem(RTextArea.getAction(RTextArea.PASTE_ACTION)))
        editMenu.add(createMenuItem(RTextArea.getAction(RTextArea.DELETE_ACTION)))
        editMenu.addSeparator()
        editMenu.add(createMenuItem(RTextArea.getAction(RTextArea.SELECT_ALL_ACTION)))
        menuBar.add(editMenu)

        val analyzerMenu = JMenu("Analyze")
        val analyze = JMenuItem("Analyze language")
        analyze.addActionListener {
            val validator = Validator()
            validator.analyze(textArea.text)
        }
        analyzerMenu.add(analyze)
        menuBar.add(analyzerMenu)

        val aboutMenu = JMenu("About")
        val teamInfo = JMenuItem("Info")
        val licenseInfo = JMenuItem("License")
        val howToUse = JMenuItem("How to use")
        teamInfo.addActionListener { aboutTeam() }
        licenseInfo.addActionListener { aboutLicense() }
        howToUse.addActionListener { howToUse() }
        aboutMenu.add(teamInfo)
        aboutMenu.add(howToUse)
        aboutMenu.add(licenseInfo)
        menuBar.add(aboutMenu)

        jMenuBar = menuBar
    }

    private fun createMenuItem(action: Action): JMenuItem? {
        val item = JMenuItem(action)
        item.toolTipText = null // Swing annoyingly adds tool tip text to the menu item
        return item
    }
    
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            // Start all Swing applications on the EDT.
            SwingUtilities.invokeLater {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
                } catch (e: Exception) { /* Never happens */
                }
                Home().isVisible = true

                //Read config file
                val file = File("config.txt")
                var showAtStartup: Boolean
                if(!file.exists()) {
                    file.createNewFile()
                    showAtStartup = welcome()
                    if(showAtStartup) {
                        file.writeText("welcome: true")
                    }
                    else {
                        file.writeText("welcome: false")
                    }
                }
                else{
                    file.forEachLine { if(it.contains("welcome: true")) {
                        showAtStartup = welcome()
                        if(showAtStartup) {
                            file.writeText("welcome: true")
                        }
                        else {
                            file.writeText("welcome: false")
                        }
                    } }
                }
            }
        }
    }

    private fun createTextArea(): RSyntaxTextArea {
        val textArea = RSyntaxTextArea(30, 60)
        textArea.isCodeFoldingEnabled = true
        textArea.paintTabLines = true

        //Syntax highlighting config
        val atmf = TokenMakerFactory.getDefaultInstance() as AbstractTokenMakerFactory
        atmf.putMapping("text/program", "view.Syntax")
        textArea.syntaxEditingStyle = "text/program"

        //Colors
        val green = Color(123, 160, 91)
        val gray = Color(128, 128, 128)
        val red = Color(220, 20, 60)
        val blue = Color(176, 196, 222)
        val lightBackground = Color(255, 255, 255)

        //Color schemes
        val scheme: SyntaxScheme = textArea.syntaxScheme

        scheme.getStyle(Token.RESERVED_WORD).foreground = gray
        scheme.getStyle(Token.RESERVED_WORD_2).foreground = red
        scheme.getStyle(Token.FUNCTION).foreground = green
        scheme.getStyle(Token.LITERAL_NUMBER_DECIMAL_INT).foreground = blue
        textArea.background = lightBackground
        textArea.currentLineHighlightColor = lightBackground
        textArea.isMarginLineEnabled = true
        textArea.marginLineColor = Color.DARK_GRAY
        textArea.revalidate()

        //Code completion config
        val provider = createCompletionProvider()
        val ac = AutoCompletion(provider)
        ac.install(textArea)

        return textArea
    }

    init {
        val cp = JPanel(BorderLayout())
        textArea = createTextArea()
        val sp = RTextScrollPane(textArea)
        cp.add(sp)

        contentPane = cp
        title = "113-PIA-E10"
        defaultCloseOperation = EXIT_ON_CLOSE
        pack()
        setLocationRelativeTo(null)
        createMenuBar()
    }

    private fun createCompletionProvider(): CompletionProvider? {

        // A DefaultCompletionProvider is the simplest concrete implementation
        // of CompletionProvider. This provider has no understanding of
        // language semantics. It simply checks the text entered up to the
        // caret position for a match against known completions. This is all
        // that is needed in the majority of cases.
        val provider = DefaultCompletionProvider()

        // Add completions for keywords. A BasicCompletion is just
        // a straightforward word completion.
        provider.addCompletion(BasicCompletion(provider, "programa"))
        provider.addCompletion(BasicCompletion(provider, "iniciar"))
        provider.addCompletion(BasicCompletion(provider, "terminar."))
        provider.addCompletion(BasicCompletion(provider, "leer"))
        provider.addCompletion(BasicCompletion(provider, "imprimir"))

        return provider
    }
}