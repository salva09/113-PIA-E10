package com.view

import com.control.Validator
import com.file.FileManager.Companion.getFileContent
import com.file.FileManager.Companion.getFileName
import com.file.FileManager.Companion.openFile
import com.file.FileManager.Companion.saveFile
import com.view.About.Companion.aboutLicense
import com.view.About.Companion.aboutTeam
import com.view.About.Companion.howToUse
import com.view.About.Companion.welcome
import org.fife.ui.autocomplete.AutoCompletion
import org.fife.ui.autocomplete.BasicCompletion
import org.fife.ui.autocomplete.CompletionProvider
import org.fife.ui.autocomplete.DefaultCompletionProvider
import org.fife.ui.rsyntaxtextarea.*
import org.fife.ui.rtextarea.RTextArea
import org.fife.ui.rtextarea.RTextScrollPane
import java.awt.BorderLayout
import java.awt.Color
import java.awt.event.ActionEvent
import java.io.File
import javax.swing.*


class Home : JFrame() {
    private var text_area: RSyntaxTextArea

    private fun createMenuBar() {
        val menu_bar = JMenuBar()
        val file_menu = JMenu("File")
        val open_file = JMenuItem("Open file")
        val save_file = JMenuItem("Save file")
        val exit = JMenuItem("Exit")
        open_file!!.addActionListener { event: ActionEvent? ->
            if (openFile(open_file!!, text_area)) {
                text_area.text = getFileContent()
                title = getFileName() + "~113-PIA-E10"
                text_area.discardAllEdits()
            }
        }
        save_file!!.addActionListener { actionEvent: ActionEvent? -> saveFile(open_file!!, text_area.text) }
        exit!!.addActionListener { actionEvent: ActionEvent? -> System.exit(0) }
        file_menu!!.add(open_file)
        file_menu!!.add(save_file)
        file_menu!!.add(exit)
        menu_bar!!.add(file_menu)

        val edit_menu = JMenu("Edit")
        edit_menu.add(createMenuItem(RTextArea.getAction(RTextArea.UNDO_ACTION)))
        edit_menu.add(createMenuItem(RTextArea.getAction(RTextArea.REDO_ACTION)))
        edit_menu.addSeparator()
        edit_menu.add(createMenuItem(RTextArea.getAction(RTextArea.CUT_ACTION)))
        edit_menu.add(createMenuItem(RTextArea.getAction(RTextArea.COPY_ACTION)))
        edit_menu.add(createMenuItem(RTextArea.getAction(RTextArea.PASTE_ACTION)))
        edit_menu.add(createMenuItem(RTextArea.getAction(RTextArea.DELETE_ACTION)))
        edit_menu.addSeparator()
        edit_menu.add(createMenuItem(RTextArea.getAction(RTextArea.SELECT_ALL_ACTION)))
        menu_bar!!.add(edit_menu)

        val analyzer_menu = JMenu("Analyze")
        val analyze = JMenuItem("Analyze syntax")
        analyze.addActionListener { actionEvent ->
            val validator = Validator()
            validator.analyze(text_area.getText())
        }
        analyzer_menu!!.add(analyze)
        menu_bar!!.add(analyzer_menu)

        val about_menu = JMenu("About")
        val team_info = JMenuItem("Info")
        val license_info = JMenuItem("License")
        val how_to_use = JMenuItem("How to use")
        team_info.addActionListener { actionEvent -> aboutTeam() }
        license_info.addActionListener { actionEvent -> aboutLicense() }
        how_to_use.addActionListener { actionEvent -> howToUse() }
        about_menu.add(team_info)
        about_menu.add(how_to_use)
        about_menu.add(license_info)
        menu_bar!!.add(about_menu)

        jMenuBar = menu_bar
    }

    private fun createMenuItem(action: Action): JMenuItem? {
        val item = JMenuItem(action)
        item.toolTipText = null // Swing annoyingly adds tool tip text to the menu item
        return item
    }
    
    companion object {
        private val frame: JFrame? = null

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
                var file = File("config.txt")
                var show_at_startup: Boolean
                if(!file.exists()) {
                    file.createNewFile()
                    show_at_startup = welcome()
                    if(show_at_startup) {
                        file.writeText("welcome: true")
                    }
                    else {
                        file.writeText("welcome: false")
                    }
                }
                else{
                    file.forEachLine { if(it.contains("welcome: true")) {
                        show_at_startup = welcome()
                        if(show_at_startup) {
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
        val text_area = RSyntaxTextArea(30, 60)
        text_area.isCodeFoldingEnabled = true
        text_area.paintTabLines = true

        //Syntax highlighting config
        val atmf = TokenMakerFactory.getDefaultInstance() as AbstractTokenMakerFactory
        atmf.putMapping("text/program", "com.view.Syntax")
        text_area.syntaxEditingStyle = "text/program"

        //Colors
        val green: Color = Color(123, 160, 91)
        val gray: Color = Color(128, 128, 128)
        val red: Color = Color(220, 20, 60)
        val blue: Color = Color(176, 196, 222)
        val light_background: Color = Color(255, 255, 255)
        val dark_background: Color = Color(35, 38, 41)

        //Color schemes
        val scheme: SyntaxScheme = text_area.syntaxScheme

        scheme.getStyle(Token.RESERVED_WORD).foreground = gray
        scheme.getStyle(Token.RESERVED_WORD_2).foreground = red
        scheme.getStyle(Token.FUNCTION).foreground = green
        scheme.getStyle(Token.LITERAL_NUMBER_DECIMAL_INT).foreground = blue
        text_area.background = light_background
        text_area.currentLineHighlightColor = light_background
        text_area.isMarginLineEnabled = true
        text_area.marginLineColor = Color.DARK_GRAY
        text_area.revalidate()

        //Code completition config
        val provider = createCompletionProvider()
        val ac = AutoCompletion(provider)
        ac.install(text_area)

        return text_area
    }

    init {
        val cp = JPanel(BorderLayout())
        text_area = createTextArea()
        val sp = RTextScrollPane(text_area)
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