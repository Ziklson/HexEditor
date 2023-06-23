import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TextPane extends JTextArea {



    private int columns;
    private int rows;

    private final HexPane hexPane;


    TextPane(HexPane hexPane) {
        super();
        this.hexPane=hexPane;
        columns=hexPane.getBytes();
        rows=hexPane.getRows();
        setColumns(columns);
        setRows(rows);
        setLineWrap(true);
        hexPane.setTextPane(this);
        addKeyListener(new TextKeyListener(this));




        getInputMap().put(KeyStroke.getKeyStroke("BACK_SPACE"), "none");

        Action doNothing = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                // do nothing
            }
        };
        getInputMap().put(KeyStroke.getKeyStroke("ctrl X"),
                "doNothing");

        getInputMap().put(KeyStroke.getKeyStroke("ctrl A"),
                "doNothing");
        getInputMap().put(KeyStroke.getKeyStroke("ctrl C"),
                "doNothing");

        getInputMap().put(KeyStroke.getKeyStroke("shift LEFT"),
                "doNothing");
        getInputMap().put(KeyStroke.getKeyStroke("shift RIGHT"),
                "doNothing");

        getInputMap().put(KeyStroke.getKeyStroke("shift UP"),
                "doNothing");
        getInputMap().put(KeyStroke.getKeyStroke("shift DOWN"),
                "doNothing");

        getInputMap().put(KeyStroke.getKeyStroke("ctrl V"),
                "doNothing");


        getActionMap().put("doNothing",
                doNothing);




    }


    public class TextKeyListener extends KeyAdapter{

        private final TextPane textPane;


        TextKeyListener(TextPane textPane) {
            super();
            this.textPane = textPane;
        }

        @Override
        public void keyTyped(KeyEvent e) {
            char key =e.getKeyChar();
            int pos=textPane.getCaretPosition();
            int size=hexPane.getSymbolsCount();
            e.consume();
            if(key > 20 && key < 256){
                if(pos*3 < size){
                    hexPane.replaceRange(Integer.toHexString((int) key),pos*3,pos*3+2);
                    textPane.replaceRange(Character.toString(key),pos,pos+1);
                    textPane.setCaretPosition(pos+1);
                }
                else{
                    textPane.insert(Character.toString(key),pos);
                    hexPane.insert(Integer.toHexString((int) key),pos*3);
                    hexPane.insert(" ",pos*3+2);
                    hexPane.setSymbolsCount(size+3);
                }
            }

            if (key == KeyEvent.VK_BACK_SPACE) {
                if (pos != 0) {
                    try {
                        hexPane.getDocument().remove(pos*3-3, 3);
                        textPane.getDocument().remove(pos-1,1);
                        hexPane.setSymbolsCount(size-3);
                    } catch (BadLocationException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }


    }


}
