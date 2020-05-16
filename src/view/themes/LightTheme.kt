package view.themes

import com.formdev.flatlaf.FlatLightLaf
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.Token
import org.fife.ui.rtextarea.RTextScrollPane
import java.awt.Color
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.UIManager

//Light colors
private val green = Color(123, 160, 91)
private val gray = Color(128, 128, 128)
private val red = Color(220, 20, 60)
private val blue = Color(176, 196, 222)
private val background = Color(255, 255, 255)
private val foreground = Color(0, 0, 0)

fun setLightLaf(frame: JFrame) {
    UIManager.setLookAndFeel(FlatLightLaf())
    SwingUtilities.updateComponentTreeUI(frame)
}

fun setLightTextArea(textArea: RSyntaxTextArea) {
    //Color schemes
    val scheme = textArea.syntaxScheme

    scheme.getStyle(Token.RESERVED_WORD).foreground = gray
    scheme.getStyle(Token.RESERVED_WORD_2).foreground = red
    scheme.getStyle(Token.FUNCTION).foreground = green
    scheme.getStyle(Token.LITERAL_NUMBER_DECIMAL_INT).foreground = blue
    textArea.background = background
    textArea.foreground = foreground
    textArea.currentLineHighlightColor = background
    textArea.isMarginLineEnabled = true
    textArea.marginLineColor = Color.DARK_GRAY

    textArea.revalidate()
}

fun setLightScrollPane(scrollPane: RTextScrollPane) {
    scrollPane.gutter.borderColor = Color.GRAY
    scrollPane.gutter.background = background

    scrollPane.revalidate()
}