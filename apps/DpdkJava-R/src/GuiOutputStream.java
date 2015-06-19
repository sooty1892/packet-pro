import java.io.OutputStream;
import javax.swing.*;

/*
 * Output stream to redirect printlns to gui console
 */

public class GuiOutputStream extends OutputStream {
    private JTextArea textArea;
     
    public GuiOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }
     
    @Override
    public void write(int b) {
        textArea.append(String.valueOf((char)b));
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
}