import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
        int bt=0;
        if(e.getSource().getClass().getName().equals(hexPane.getClass().getName()) && hexPane.hasFocus() && !hexPane.getWorkPane().getjScrollBarV().hasFocus() && !hexPane.isInserting()){
            int posHex=hexPane.getCaretPosition();
            int size=hexPane.getSymbolsCount();
            Document doc=hexPane.getDocument();
            String strAtPos = "";

            hexPane.getWorkPane().setLastFocus(0);
//            hText.removeAllHighlights();
            //hHex.removeAllHighlights();
            if(hexPane.getCaretHigh() != null)
                hHex.removeHighlight(hexPane.getCaretHigh());
            if(textPane.getCaretHigh() != null)
                hText.removeHighlight(textPane.getCaretHigh());

//
            int strr=hexPane.getWorkPane().getjScrollBarV().getValue();
            bt=strr*hexPane.getBytes() + posHex/3 - hexPane.getCurSize();


        if (posHex != size) { // Чтобы не было бага с переносом новой строки != size было
                try {
                    if (posHex % 3 == 0) {
                        hexPane.setCaretHigh(hHex.addHighlight(posHex, posHex + 2, new DefaultHighlighter.DefaultHighlightPainter(new Color(173, 203, 255))));
                    }
                    if (posHex % 3 == 1) {
                        hexPane.setCaretHigh(hHex.addHighlight(posHex - 1, posHex+1, new DefaultHighlighter.DefaultHighlightPainter(new Color(173, 203, 255))));
//
//                        System.out.println("PosHex " + posHex);

                    }
                    textPane.setCaretHigh(hText.addHighlight(posHex/3, posHex/3+1, new DefaultHighlighter.DefaultHighlightPainter(new Color(173, 203, 255))));
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
            }else{
////            hexPane.getInfoPane().setByteValueLabel("undefined");
        }
        }
        else{
            if(textPane.hasFocus() && !hexPane.isInserting()){
                hexPane.getWorkPane().setLastFocus(1);
                Document doc=hexPane.getDocument();
                String strAtPos = "0";
                int size=hexPane.getSymbolsCount();
                if(hexPane.getCaretHigh() != null)
                    hHex.removeHighlight(hexPane.getCaretHigh());
                if(textPane.getCaretHigh() != null)
                    hText.removeHighlight(textPane.getCaretHigh());
                int posText=textPane.getCaretPosition();


                int strr=hexPane.getWorkPane().getjScrollBarV().getValue();
                bt=strr*hexPane.getBytes() + posText - hexPane.getCurSize();


                if(posText*3 != size){
                    try {
                        hexPane.setCaretHigh(hHex.addHighlight(posText*3,posText*3+2, new DefaultHighlighter.DefaultHighlightPainter(new Color(173, 203, 255))));
                        textPane.setCaretHigh(hText.addHighlight(posText, posText+ 1, new DefaultHighlighter.DefaultHighlightPainter(new Color(173, 203, 255))));
                    } catch (BadLocationException ex) {
                        ex.printStackTrace();
                    }
                }
                else{
//                    hexPane.getInfoPane().setByteValueLabel("undefined");
                }

            }
        }

        List<Byte> arr = hexPane.getByteArr();
        byte[] arr2=new byte[8];
        if(bt >= arr.size()){

        }
        else{
            hexPane.getInfoPane().setByteValueLabel(arr.get(bt));
            if(bt + arr2.length < arr.size()){
                for(int i=0;i<arr2.length;i++){
                    arr2[i]= arr.get(bt);
                    bt++;
                }
            }
            else{
                for(int i=arr.size()-1,j=arr2.length-1;i>=bt;i--,j--){
                    arr2[j]=arr.get(i);
                }
            }
            hexPane.getInfoPane().setBlockValueLabels(arr2);
        }

    }
}
