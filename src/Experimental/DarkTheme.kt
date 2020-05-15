package Experimental

import com.formdev.flatlaf.FlatDarculaLaf
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.Token
import org.fife.ui.rtextarea.RTextScrollPane
import java.awt.Color
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.UIManager

//Colors
private val green = Color(123, 180, 91)
private val gray = Color(128, 128, 128)
private val red = Color(230, 20, 60)
private val blue = Color(176, 196, 222)
private val currentLine = Color(50, 50, 50)
private val background = Color(43, 43, 43)
private val foreground = Color(140, 168, 173)

fun setDarkLaf(frame: JFrame) {
    UIManager.setLookAndFeel(FlatDarculaLaf())
    SwingUtilities.updateComponentTreeUI(frame)
}

fun setDarkTextArea(textArea: RSyntaxTextArea) {
    //Color schemes
    val scheme = textArea.syntaxScheme

    scheme.getStyle(Token.RESERVED_WORD).foreground = Color.WHITE
    scheme.getStyle(Token.RESERVED_WORD_2).foreground = red
    scheme.getStyle(Token.FUNCTION).foreground = green
    scheme.getStyle(Token.OPERATOR).foreground = gray
    scheme.getStyle(Token.LITERAL_NUMBER_DECIMAL_INT).foreground = blue
    textArea.background = background
    textArea.foreground = foreground
    textArea.currentLineHighlightColor = background
    textArea.isMarginLineEnabled = true
    textArea.marginLineColor = Color.DARK_GRAY
    textArea.currentLineHighlightColor = currentLine
    textArea.caretColor = Color.WHITE

    textArea.revalidate()
}

fun setDarkScrollPane(scrollPane: RTextScrollPane) {
    scrollPane.gutter.borderColor = Color.GRAY
    scrollPane.gutter.background = background

    scrollPane.revalidate()
}