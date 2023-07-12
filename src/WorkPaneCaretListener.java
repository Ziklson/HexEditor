import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
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
        if(e.getSource().getClass().getName().equals(hexPane.getClass().getName()) && hexPane.hasFocus() && !hexPane.getWorkPane().getjScrollBarV().hasFocus() && !hexPane.isInserting()){
            int posHex=hexPane.getCaretPosition();
            int size=hexPane.getSymbolsCount();
            Document doc=hexPane.getDocument();
            String strAtPos = "";

            hText.removeAllHighlights();
            hHex.removeAllHighlights();

//            System.out.println("CaretPosHex" + posHex);
//

        if (posHex != size) { // Чтобы не было бага с переносом новой строки
                try {
                    if (posHex % 3 == 0) {
                        hHex.addHighlight(posHex, posHex + 2, DefaultHighlighter.DefaultPainter);
                        strAtPos = doc.getText(posHex, 2);
                        if(strAtPos.charAt(1) != ' ')
                            hexPane.getInfoPane().setByteValueLabel(strAtPos);
                    }
                    if (posHex % 3 == 1) {
                        hHex.addHighlight(posHex - 1, posHex+1, DefaultHighlighter.DefaultPainter);
                        strAtPos = doc.getText(posHex - 1, 2);
                        if(strAtPos.charAt(1) != ' ')
                            hexPane.getInfoPane().setByteValueLabel(strAtPos);
                    }
                    hText.addHighlight(posHex/3, posHex/3+1, DefaultHighlighter.DefaultPainter);

                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
            }else{
            hexPane.getInfoPane().setByteValueLabel("undefined");
        }
        }
        else{
            if(textPane.hasFocus() && !hexPane.isInserting()){

                Document doc=hexPane.getDocument();
                String strAtPos = "0";
                int size=hexPane.getSymbolsCount();
                hText.removeAllHighlights();
                hHex.removeAllHighlights();
                int posText=textPane.getCaretPosition();

//                System.out.println("posText " + posText);
                if(posText*3 != size){
                    try {
                        hHex.addHighlight(posText*3,posText*3+2, DefaultHighlighter.DefaultPainter);
                        hText.addHighlight(posText, posText+ 1, DefaultHighlighter.DefaultPainter);
                        strAtPos = doc.getText(posText*3, 2);
                        if(strAtPos.charAt(0) != ' ')
                            hexPane.getInfoPane().setByteValueLabel(strAtPos);
                    } catch (BadLocationException ex) {
                        ex.printStackTrace();
                    }
                }
                else{
                    hexPane.getInfoPane().setByteValueLabel("undefined");
                }

            }
        }
    }
}
