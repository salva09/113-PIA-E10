package view

import control.LanguageException
import control.analyze
import experimental.run
import file.fileName
import file.getFileContent
import file.openFile
import file.saveFile
import lexer.tokens
import org.fife.ui.autocomplete.AutoCompletion
import org.fife.ui.autocomplete.BasicCompletion
import org.fife.ui.autocomplete.CompletionProvider
import org.fife.ui.autocomplete.DefaultCompletionProvider
import org.fife.ui.rtextarea.RTextArea
import org.fife.ui.rtextarea.RTextScrollPane
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory
import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory
import view.themes.*
import java.awt.BorderLayout
import javax.swing.*
import kotlin.system.exitProcess

class Home : JFrame() {
    private var experimentalMode = false

    init {
        val pane = JPanel(BorderLayout())
        val textArea = createTextArea()
        val scrollPane = RTextScrollPane(textArea)
        pane.add(scrollPane)

        //Light theme
        setLightLaf(this)
        setLightTextArea(textArea)
        setLightScrollPane(scrollPane)

        contentPane = pane
        title = "113-PIA-E10"
        defaultCloseOperation = EXIT_ON_CLOSE

        applyPreferences(textArea, scrollPane)
        createMenuBar(textArea)

        pack()

        setLocationRelativeTo(null)
    }

    private fun applyPreferences(textArea: RSyntaxTextArea, scrollPane: RTextScrollPane) {
        getPreferences()

        if (getConfig("welcome").value) {
            getConfig("welcome").value = welcome()
        }

        if (getConfig("dark").value) {
            setDarkLaf(this)
            setDarkTextArea(textArea)
            setDarkScrollPane(scrollPane)
        }

        experimentalMode = getConfig("experimental").value

        setPreferences()
    }

    private fun createMenuBar(textArea: RSyntaxTextArea) {
        val menuBar = JMenuBar()

        menuBar.add(createFileMenu(textArea))
        menuBar.add(createEditMenu())
        menuBar.add(createAnalyzeMenu(textArea))
        menuBar.add(createAboutMenu())

        if (experimentalMode) {
            menuBar.add(createExpMenu(textArea))
        }

        jMenuBar = menuBar
    }

    private fun createFileMenu(textArea: RSyntaxTextArea): JMenu {
        val fileMenu = JMenu("File")

        val openFile = JMenuItem("Open")
        val saveFile = JMenuItem("Save")
        val saveFileAs = JMenuItem("Save as")
        val exit = JMenuItem("Exit")

        openFile.addActionListener {
            try {
                if (openFile(openFile)) {
                    textArea.text = getFileContent()
                    title = "$fileName~113-PIA-E10"
                    textArea.discardAllEdits()
                }
            } catch (ex: Exception) {
                print(ex.localizedMessage)
            }
        }
        saveFile.addActionListener { saveFile(openFile, textArea.text) }
        saveFileAs.addActionListener { file.saveFileAs(openFile, textArea.text) }
        exit.addActionListener { exitProcess(0) }

        fileMenu.add(openFile)
        fileMenu.add(saveFile)
        fileMenu.add(saveFileAs)
        fileMenu.add(exit)

        return fileMenu
    }

    private fun createEditMenu(): JMenu {
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

        return editMenu
    }

    private fun createAnalyzeMenu(textArea: RSyntaxTextArea): JMenu {
        val analyzerMenu = JMenu("Analyze")

        val analyze = JMenuItem("Analyze language")

        analyze.addActionListener {
            try {
                analyze(textArea.text)
                JOptionPane.showMessageDialog(null, "The input given is valid!", "", JOptionPane.INFORMATION_MESSAGE)
            } catch (ex: LanguageException) {
                JOptionPane.showMessageDialog(null, ex.localizedMessage, "Error", JOptionPane.ERROR_MESSAGE)
            }
        }

        analyzerMenu.add(analyze)

        return analyzerMenu
    }

    private fun createAboutMenu(): JMenu {
        val aboutMenu = JMenu("About")

        val teamInfo = JMenuItem("Information")
        val licenseInfo = JMenuItem("License")
        val howToUse = JMenuItem("How to use")

        teamInfo.addActionListener { aboutTeam() }
        licenseInfo.addActionListener { aboutLicense() }
        howToUse.addActionListener { howToUse() }

        aboutMenu.add(teamInfo)
        aboutMenu.add(howToUse)
        aboutMenu.add(licenseInfo)

        return aboutMenu
    }

    private fun createExpMenu(textArea: RSyntaxTextArea): JMenu {
        val experimentalMenu = JMenu("Experimental")

        val run = JMenuItem("Run")

        run.addActionListener {
            try {
                analyze(textArea.text)
                run()
            } catch (ex: LanguageException) {
                JOptionPane.showMessageDialog(null, ex.localizedMessage, "Error", JOptionPane.ERROR_MESSAGE)
            }
        }

        experimentalMenu.add(run)

        return experimentalMenu
    }

    private fun createMenuItem(action: Action): JMenuItem? {
        val item = JMenuItem(action)
        item.toolTipText = null // Swing annoyingly adds tool tip text to the menu item
        return item
    }

    private fun createTextArea(): RSyntaxTextArea {
        var textArea = RSyntaxTextArea(30, 60)
        textArea.isCodeFoldingEnabled = true
        textArea.paintTabLines = true

        //Syntax highlighting config
        val atmf = TokenMakerFactory.getDefaultInstance() as AbstractTokenMakerFactory
        atmf.putMapping("text/program", "view.Syntax")
        textArea.syntaxEditingStyle = "text/program"

        //Code completion config
        val provider = createCompletionProvider()
        val ac = AutoCompletion(provider)
        ac.install(textArea)

        return textArea
    }

    //Experimental field

    private fun createCompletionProvider(): CompletionProvider {
        // A DefaultCompletionProvider is the simplest concrete implementation
        // of CompletionProvider. This provider has no understanding of
        // language semantics. It simply checks the text entered up to the
        // caret position for a match against known completions. This is all
        // that is needed in the majority of cases.
        val provider = DefaultCompletionProvider()

        // Add completions for keywords. A BasicCompletion is just
        // a straightforward word completion.
        provider.addCompletion(BasicCompletion(provider, "programa "))
        provider.addCompletion(BasicCompletion(provider, "iniciar"))
        provider.addCompletion(BasicCompletion(provider, "terminar."))
        provider.addCompletion(BasicCompletion(provider, "leer "))
        provider.addCompletion(BasicCompletion(provider, "imprimir "))

        return provider
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            // Start all Swing applications on the EDT.
            SwingUtilities.invokeLater {
                Home().isVisible = true
            }
        }
    }
}
