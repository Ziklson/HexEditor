import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;

public class HexPane extends JTextArea {

    private final int[] keys;

    private int symbolsCount;

    private int columns;
    private int rows;

    public TextPane getTextPane() {
        return textPane;
    }

    public void setTextPane(TextPane textPane) {
        this.textPane = textPane;
    }

    private TextPane textPane;

    public int getBytes() {
        return bytes;
    }

    public void setBytes(int bytes) {
        this.bytes = bytes;
    }

    private int bytes;

    public int getSymbolsCount() {
        return symbolsCount;
    }

    public void setSymbolsCount(int size) {
        this.symbolsCount = size;
    }

    HexPane() {
        super();
        bytes = 16;
        columns = 3 * bytes - 1;
        rows = 16;
        setColumns(columns);
        symbolsCount = 0;
        setRows(rows);
        setLineWrap(true);
        setWrapStyleWord(true);
        keys = new int[16];
        int index = 0;
        for (int c = KeyEvent.VK_0; c <= KeyEvent.VK_9; ++c, ++index) keys[index] = c;
        for (int c = KeyEvent.VK_A; c <= KeyEvent.VK_F; ++c, ++index) keys[index] = c;

        getCaret().setBlinkRate(1000);

        addKeyListener(new HexKeyListener(this));
//        addCaretListener(new HexCaretListener(this));

        getInputMap().put(KeyStroke.getKeyStroke("BACK_SPACE"), "none");
        getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "none");


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


    public int[] getKeys() {
        return keys;
    }


    public String toChar(String str) {

        StringBuilder output = new StringBuilder("");

        for (int i = 0; i < str.length(); i += 2) {
            String newStr = str.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
    }




    public class HexKeyListener extends KeyAdapter {

        private final HexPane hexPane;


        HexKeyListener(HexPane hexPane) {
            super();
            this.hexPane = hexPane;
        }

        @Override
        public void keyTyped(KeyEvent e) {
            char c = Character.toUpperCase(e.getKeyChar());
            int pos = hexPane.getCaretPosition();
            int code = e.getKeyCode();
            Document doc = hexPane.getDocument();
            e.consume(); // ignore event

            if ((Arrays.stream(keys).anyMatch(x -> x == c))) {
                String str = String.valueOf(c);
                String strAtPos = "";
                if (symbolsCount > pos) {
                    if (pos % 3 == 0) {
                        hexPane.replaceRange(str, pos, pos + 1);
                       // hexPane.setCaretPosition(pos + 1);
                        try {
                            strAtPos = doc.getText(pos, 2);
                        } catch (BadLocationException ex) {
                            ex.printStackTrace();
                        }
                        textPane.replaceRange(hexPane.toChar(strAtPos), pos / 3, pos / 3 + 1);
                        hexPane.setCaretPosition(pos+1);

                    }
                    if (pos % 3 == 1) {
                        hexPane.replaceRange(str, pos, pos + 1);
                        //hexPane.setCaretPosition(pos + 2);
                        try {
                            strAtPos = doc.getText(pos - 1, 2);
                        } catch (BadLocationException ex) {
                            ex.printStackTrace();
                        }
                        textPane.replaceRange(hexPane.toChar(strAtPos), pos / 3, pos / 3 + 1);
                        hexPane.setCaretPosition(pos+2);
                    }
                //    textPane.replaceRange(hexPane.toChar(strAtPos), pos / 3, pos / 3 + 1);

                } else {
                    textPane.insert(hexPane.toChar(str+"0"), pos / 3);

                    hexPane.insert(str, pos);
                    hexPane.insert("0", pos + 1);
                    hexPane.insert(" ", pos + 2);

                    hexPane.setCaretPosition(pos + 1);
                    symbolsCount += 3;
//                    try {
//                        strAtPos = doc.getText(pos, 2);
//
//                    } catch (BadLocationException ex) {
//                        ex.printStackTrace();
//                    }
//                    textPane.insert(hexPane.toChar(strAtPos), pos / 3);


                }
            }
            if (c == KeyEvent.VK_BACK_SPACE) {
                if (pos != 0 && pos % 3 == 0) {
                    try {
                        hexPane.getDocument().remove(pos - 3, 3);
                        textPane.getDocument().remove(pos/3 -1,1);
                        symbolsCount -= 3;
                    } catch (BadLocationException ex) {
                        ex.printStackTrace();
                    }
                }
            }

        }

        @Override
        public void keyPressed(KeyEvent e) {
            int code = e.getKeyCode();
            e.consume();
            int pos = hexPane.getCaretPosition();
            if (code == KeyEvent.VK_LEFT) {
                if (pos > 0) {
                    if (pos % 3 == 0)
                        hexPane.setCaretPosition(pos - 3);
                    if (pos % 3 == 1 && pos > 1)
                        hexPane.setCaretPosition(pos - 4);
                    if (pos % 3 == 2)
                        hexPane.setCaretPosition(pos - 2);
                    if (pos == 1)
                        hexPane.setCaretPosition(0);
                }

            }
            if (code == KeyEvent.VK_RIGHT) {
                if (pos < symbolsCount) {
                    if (pos % 3 == 0) {
                        hexPane.setCaretPosition(pos + 3);
                    }
                    if (pos % 3 == 1) {
                        hexPane.setCaretPosition(pos + 2);
                    }
                    if (pos == symbolsCount - 1) {
                        hexPane.setCaretPosition(symbolsCount);
                    }
                    if (pos % 3 == 2)
                        hexPane.setCaretPosition(pos + 1);
                }
            }
            if (code == KeyEvent.VK_UP) {
                if (pos > columns) { // заменить на смещение по строкам
                    hexPane.setCaretPosition(pos - columns - 1);
                }

            }
            if (code == KeyEvent.VK_DOWN) {
                if (pos + columns < symbolsCount) { // заменить на смещение по строкам
                    hexPane.setCaretPosition(pos + columns + 1);
                }
            }

        }
    }

}
