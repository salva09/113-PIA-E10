package view

import file.FileManager
import interpreter.RuntimeException
import interpreter.run
import lexer.LexiconException
import lexer.SyntaxException
import lexer.analyze
import org.fife.ui.autocomplete.AutoCompletion
import org.fife.ui.autocomplete.BasicCompletion
import org.fife.ui.autocomplete.CompletionProvider
import org.fife.ui.autocomplete.DefaultCompletionProvider
import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory
import org.fife.ui.rtextarea.RTextArea
import org.fife.ui.rtextarea.RTextScrollPane
import view.themes.*
import java.awt.BorderLayout
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.io.IOException
import javax.swing.*
import kotlin.system.exitProcess

class Home : JFrame() {
    private var experimentalMode = false

    init {
        val pane = JPanel(BorderLayout())
        val textArea = createTextArea()
        val scrollPane = RTextScrollPane(textArea)
        pane.add(scrollPane)

        contentPane = pane
        title = "~113 PIA E10"
        defaultCloseOperation = WindowConstants.DO_NOTHING_ON_CLOSE

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
        } else {
            setLightLaf(this)
            setLightTextArea(textArea)
            setLightScrollPane(scrollPane)
        }

        experimentalMode = getConfig("experimental").value

        setPreferences()
    }

    private fun createMenuBar(textArea: RSyntaxTextArea) {
        val menuBar = JMenuBar()

        menuBar.add(createFileMenu(textArea))
        menuBar.add(createEditMenu())
        menuBar.add(createToolMenu(textArea))
        menuBar.add(createAboutMenu())

        if (experimentalMode) {
            menuBar.add(createExpMenu())
        }

        jMenuBar = menuBar
    }

    private fun createFileMenu(textArea: RSyntaxTextArea): JMenu {
        val fileManager = FileManager()
        var previousText = ""

        val fileMenu = JMenu("File")

        val new = JMenuItem("New")
        val openFile = JMenuItem("Open")
        val saveFile = JMenuItem("Save")
        val saveFileAs = JMenuItem("Save as")
        val exit = JMenuItem("Exit")

        new.addActionListener {
            if (previousText != textArea.text) {
                val message = "Your file isn't saved!\nDo you want to save it?"
                if (JOptionPane.showConfirmDialog(null, message, "Warning",
                                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                    fileManager.saveFile(openFile, textArea.text)
                }
            }
            textArea.text = fileManager.new()
            previousText = ""
            title = "${fileManager.fileName}~113 PIA E10"
            textArea.discardAllEdits()
        }
        openFile.addActionListener {
            if (previousText != textArea.text) {
                val message = "Your file isn't saved!\nDo you want to save it?"
                if (JOptionPane.showConfirmDialog(null, message, "Warning",
                                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                    fileManager.saveFile(openFile, textArea.text)
                }
            }
            try {
                if (fileManager.openFile(openFile)) {
                    textArea.text = fileManager.getFileContent()
                    previousText = textArea.text
                    title = "${fileManager.fileName}~113 PIA E10"
                    textArea.discardAllEdits()
                }
            } catch (ex: IOException) {
                print(ex.localizedMessage)
            }
        }
        saveFile.addActionListener {
            fileManager.saveFile(openFile, textArea.text)
            previousText = textArea.text
            title = "${fileManager.fileName}~113 PIA E10"
        }
        saveFileAs.addActionListener {
            fileManager.saveFileAs(openFile, textArea.text)
            previousText = textArea.text
            title = "${fileManager.fileName}~113 PIA E10"
        }
        exit.addActionListener {
            if (previousText != textArea.text) {
                val message = "Your file isn't saved!\nDo you want to save it?"
                if (JOptionPane.showConfirmDialog(null, message, "Warning",
                                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                    fileManager.saveFile(openFile, textArea.text)
                }
            }
            exitProcess(0)
        }

        fileMenu.add(new)
        fileMenu.add(openFile)
        fileMenu.add(saveFile)
        fileMenu.add(saveFileAs)
        fileMenu.add(exit)

        this.addWindowListener(object : WindowAdapter() {
            override fun windowClosing(event: WindowEvent) {
                if (previousText != textArea.text) {
                    val message = "Your file isn't saved!\nDo you want to save it?"
                    if (JOptionPane.showConfirmDialog(null, message, "Warning",
                                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                        fileManager.saveFile(openFile, textArea.text)
                    }
                }
                dispose()
                exitProcess(0)
            }
        })

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

    private fun createToolMenu(textArea: RSyntaxTextArea): JMenu {
        val toolMenu = JMenu("Tools")

        val analyze = JMenuItem("Inspect code")
        val run = JMenuItem("Run")

        analyze.addActionListener {
            try {
                analyze(textArea.text)
                JOptionPane.showMessageDialog(null, "The program is correct grammarly", "",
                        JOptionPane.INFORMATION_MESSAGE)
            } catch (ex: SyntaxException) {
                JOptionPane.showMessageDialog(null, ex.localizedMessage, "Syntax error",
                        JOptionPane.ERROR_MESSAGE)
            } catch (ex: LexiconException) {
                JOptionPane.showMessageDialog(null, ex.localizedMessage, "Lexicon error",
                        JOptionPane.ERROR_MESSAGE)
            } catch (ex: Exception) {
                JOptionPane.showMessageDialog(null, ex.localizedMessage, "Unexpected error",
                        JOptionPane.ERROR_MESSAGE)
            }
        }
        run.addActionListener {
            try {
                run(analyze(textArea.text))
                JOptionPane.showMessageDialog(null, "Program executed correctly", "",
                        JOptionPane.INFORMATION_MESSAGE)
            } catch (ex: SyntaxException) {
                JOptionPane.showMessageDialog(null, ex.localizedMessage, "Syntax error",
                        JOptionPane.ERROR_MESSAGE)
            } catch (ex: LexiconException) {
                JOptionPane.showMessageDialog(null, ex.localizedMessage, "Lexicon error",
                        JOptionPane.ERROR_MESSAGE)
            } catch (ex: RuntimeException) {
                JOptionPane.showMessageDialog(null, ex.localizedMessage, "Runtime error",
                        JOptionPane.ERROR_MESSAGE)
            } catch (ex: ArithmeticException) {
                JOptionPane.showMessageDialog(null, ex.localizedMessage, "Arithmetic error",
                        JOptionPane.ERROR_MESSAGE)
            } catch (ex: Exception) {
                JOptionPane.showMessageDialog(null, ex.localizedMessage, "Unexpected error",
                        JOptionPane.ERROR_MESSAGE)
            }
        }

        toolMenu.add(analyze)
        toolMenu.add(run)

        return toolMenu
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

    private fun createExpMenu(): JMenu {
        val experimentalMenu = JMenu("Experimental")

        return experimentalMenu
    }

    private fun createMenuItem(action: Action): JMenuItem? {
        val item = JMenuItem(action)
        item.toolTipText = null
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

    private fun createCompletionProvider(): CompletionProvider {
        val provider = DefaultCompletionProvider()

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
