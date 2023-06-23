import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

public class WorkPaneCaretListener implements CaretListener {
    private HexPane hexPane;
    private TextPane textPane;

    private Highlighter hHex;
    private Highlighter hText;

    public HexPane getHexPane() {
        return hexPane;
    }

    public void setHexPane(HexPane hexPane) {
        this.hexPane = hexPane;
    }

    public TextPane getTextPane() {
        return textPane;
    }

    public void setTextPane(TextPane textPane) {
        this.textPane = textPane;
    }

    WorkPaneCaretListener(HexPane hexPane, TextPane textPane) {
        this.hexPane=hexPane;
        this.textPane=textPane;
        hHex=hexPane.getHighlighter();
        hText=textPane.getHighlighter();
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        if(e.getSource().getClass().getName().equals(hexPane.getClass().getName()) && hexPane.hasFocus()){
            int posHex=hexPane.getCaretPosition();
            int size=hexPane.getSymbolsCount();



            hText.removeAllHighlights();
            hHex.removeAllHighlights();
            

        if (posHex != size) { // Чтобы не было бага с переносом новой строки

                try {
                    if (posHex % 3 == 0) {
                        hHex.addHighlight(posHex, posHex + 2, DefaultHighlighter.DefaultPainter);
                    }
                    if (posHex % 3 == 1) {
                        hHex.addHighlight(posHex - 1, posHex+1, DefaultHighlighter.DefaultPainter);
                    }

                    hText.addHighlight(posHex/3, posHex/3+1, DefaultHighlighter.DefaultPainter);

                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
            }
        }
        else{
            if(textPane.hasFocus()){
                int size=hexPane.getSymbolsCount();
                hText.removeAllHighlights();
                hHex.removeAllHighlights();
                int posText=textPane.getCaretPosition();
                if(posText*3 != size){
                    try {
                        hText.addHighlight(posText, posText+ 1, DefaultHighlighter.DefaultPainter);
                        hHex.addHighlight(posText*3,posText*3+2, DefaultHighlighter.DefaultPainter);
                    } catch (BadLocationException ex) {
                        ex.printStackTrace();
                    }
                }

            }
        }
    }
}
