import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultHighlighter;
import java.awt.*;
import java.awt.event.*;

public class TextPane extends JTextArea {



    private int columns;
    private int rows;

    private Font font;
    private final HexPane hexPane;

    public Object getMouseHigh() {
        return mouseHigh;
    }

    public void setMouseHigh(Object mouseHigh) {
        this.mouseHigh = mouseHigh;
    }

    private Object mouseHigh;

    public Object getCaretHigh() {
        return caretHigh;
    }

    public void setCaretHigh(Object caretHigh) {
        this.caretHigh = caretHigh;
    }

    private Object caretHigh;

    public Object getSearchHigh() {
        return searchHigh;
    }

    public void setSearchHigh(Object searchHigh) {
        this.searchHigh = searchHigh;
    }

    private Object searchHigh;


    TextPane(HexPane hexPane) {
        super();
        //
        font=new Font(Font.MONOSPACED, Font.PLAIN,22);
        int charW=getFontMetrics(font).charWidth(' ');
        int charH=getFontMetrics(font).getHeight();
        //
        this.hexPane=hexPane;
        columns=hexPane.getBytes();
        rows=hexPane.getRows();
        setMinimumSize(new Dimension(0,0));

        setColumns(columns);
        setRows(rows);
        setLineWrap(true);
        hexPane.setTextPane(this);
        addKeyListener(new TextKeyListener(this));
        ///
        DefaultCaret caret=(DefaultCaret) getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        ///
        setPreferredSize(new Dimension(0,0));
//


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



        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                int x=e.getX();
                int y=e.getY();
                int xPos=x/charW;
                int yPos=(y/charH)*(hexPane.getBytes());
                xPos+=yPos;
                try {
                    if(hexPane.getMouseHigh() != null)
                        hexPane.getHighlighter().removeHighlight(hexPane.getMouseHigh());
                    if(getMouseHigh() != null)
                        getHighlighter().removeHighlight(getMouseHigh());
                    setMouseHigh(getHighlighter().addHighlight(xPos,xPos+1,new DefaultHighlighter.DefaultHighlightPainter(new Color(198, 237, 248))));
                    hexPane.setMouseHigh(hexPane.getHighlighter().addHighlight(xPos*3,xPos*3+2,new DefaultHighlighter.DefaultHighlightPainter(new Color(198, 237, 248))));
                } catch (BadLocationException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });


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
                    hexPane.setInserting(true);
                    hexPane.replaceRange(Integer.toHexString((int) key),pos*3,pos*3+2);
                    if((int) key == 127)
                        textPane.replaceRange("·",pos,pos+1);
                    else
                        textPane.replaceRange(Character.toString(key),pos,pos+1);
                    hexPane.setInserting(false);
                    if(pos == columns*rows-1){
                        hexPane.getWorkPane().getjScrollBarV().setValue(hexPane.getWorkPane().getjScrollBarV().getValue()+1);
                        setCaretPosition(columns*(rows-1));
                    }
                    else{
                        setCaretPosition(pos+1);
                    }
                    int curStr=hexPane.getWorkPane().getjScrollBarV().getValue();
                    int offset=curStr*hexPane.getBytes()+pos - hexPane.getStartBuff();
                    if(!hexPane.isBufferChanged())
                        hexPane.setBufferChanged(true);
                    Byte b= (byte) key;
                    hexPane.getByteArr().set(offset,b);

                }
                else{
                    size+=3;
                    hexPane.setSymbolsCount(size);
                    hexPane.setInserting(true);
                    textPane.insert(Character.toString(key),pos);
                    hexPane.insert(Integer.toHexString((int) key),pos*3);
                    hexPane.insert(" ",pos*3+2);
                    hexPane.getInfoPane().setFileSizeValueLabel(Integer.toString((size+1)/3));
                    hexPane.setInserting(false);
                    if(pos == columns*rows-1){
                        hexPane.getWorkPane().getjScrollBarV().setValue(hexPane.getWorkPane().getjScrollBarV().getValue()+1);
                        setCaretPosition(columns*(rows-1));
                    }
                }
            }

            if (key == KeyEvent.VK_BACK_SPACE) {
                if (pos != 0) {
                    try {
                        size-=3;
                        hexPane.setSymbolsCount(size);
                        hexPane.getInfoPane().setFileSizeValueLabel(Integer.toString((size+1)/3));
                        textPane.getDocument().remove(pos-1,1);
                        hexPane.getDocument().remove(pos*3-3, 3);
                        hexPane.setFileSize(hexPane.getFileSize()-1);

                        int curStr=hexPane.getWorkPane().getjScrollBarV().getValue();
                        int offset=curStr*hexPane.getBytes()+pos-1 - hexPane.getStartBuff();
                        if(!hexPane.isBufferChanged())
                            hexPane.setBufferChanged(true);
                        hexPane.getByteArr().remove(offset);
                        hexPane.insertPage((hexPane.getWorkPane().getjScrollBarV().getValue()-hexPane.getStartBuff()/hexPane.getBytes())*hexPane.getBytes());
                        setCaretPosition(pos-1);


                    } catch (BadLocationException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

        @Override
        public void keyPressed(KeyEvent e){
            int code = e.getKeyCode();
            e.consume();

            int pos = getCaretPosition();
            int str = hexPane.getWorkPane().getjScrollBarV().getValue();


            if (code == KeyEvent.VK_LEFT) {
                if(pos > 0){
                    setCaretPosition(pos-1);
                }
                else{
                    if(str != 0){
                        hexPane.getWorkPane().getjScrollBarV().setValue(str-1);
                        setCaretPosition(columns-1);
                    }
                }
            }
            if (code == KeyEvent.VK_RIGHT) {
                if(pos < (rows*columns) - 1){
                    setCaretPosition(pos+1);
                }
                else{
                    if(str != hexPane.getWorkPane().getjScrollBarV().getMaximum()){
                        hexPane.getWorkPane().getjScrollBarV().setValue(str+1);
                        setCaretPosition(columns*(rows-1));
                    }
                }
            }
            if (code == KeyEvent.VK_UP) {
                if (pos >= columns) { // заменить на смещение по строкам
                        setCaretPosition(pos - columns);
                }
                else{
                    if(str != 0){
                        hexPane.getWorkPane().getjScrollBarV().setValue(str-1);
                        if(pos == 0){
                            setCaretPosition(1); // Он не хотел регистрировать переход на 0 как событие обновление каретки, пришлось делать такой костыль
                        }
                        setCaretPosition(pos);


                    }
                }
            }
            if (code == KeyEvent.VK_DOWN) {
                if (pos < (rows-1)*columns) { // заменить на смещение по строкам // было < symbolsCount
                    setCaretPosition(pos + columns);
                }
                else{
                    if(str != hexPane.getWorkPane().getjScrollBarV().getMaximum()){
                        hexPane.getWorkPane().getjScrollBarV().setValue(str+1);
                        setCaretPosition(pos);
                    }
                }
            }
        }


    }

    public void setVisibleRows(int rows){
        this.rows=rows;
        setRows(rows);
    }

    public void setColumnsCount(int columns){
        this.columns=columns;
        setColumns(columns);

    }


}
