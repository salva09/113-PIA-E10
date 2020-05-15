package control

import com.formdev.flatlaf.FlatDarculaLaf
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.Token
import java.awt.Color
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.UIManager

fun setDarkTheme(textArea: RSyntaxTextArea, frame: JFrame): RSyntaxTextArea {
    UIManager.setLookAndFeel(FlatDarculaLaf())
    SwingUtilities.updateComponentTreeUI(frame)

    //Colors
    val green = Color(123, 180, 91)
    val gray = Color(128, 128, 128)
    val red = Color(230, 20, 60)
    val blue = Color(176, 196, 222)
    val currentLine = Color(50, 50, 50)
    val background = Color(43, 43, 43)
    val foreground = Color(140, 168, 173)

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
    textArea.revalidate()
    return textArea
}