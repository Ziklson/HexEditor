import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;

public class MySearchPane extends JPanel {
    private JPanel searchPanel;
    private JTextArea searchArea;

    public int getSymbolsCount() {
        return symbolsCount;
    }

    private int symbolsCount;

    private JScrollPane searchAreaPane;
    private JButton upButton;
    private JButton downButton;
    private JLabel searchLabel;
    private Font font;
    private JLabel result;

    private HexPane hexPane;

    private int columns;
    private boolean inserting;

    private int curByte;
    private int lastInd;
    private String strFound;



    MySearchPane(HexPane hexPane) {
        super();
        this.hexPane=hexPane;
        curByte=0;
        strFound="";
        lastInd=0;
        font=new Font(Font.MONOSPACED, Font.PLAIN,16);
        inserting=false;
        symbolsCount=0;
        searchPanel=new JPanel();
        searchArea=new JTextArea();
        columns=23;
        searchArea.setColumns(columns);
        searchArea.setRows(4);
        searchArea.setLineWrap(true);
        searchArea.setWrapStyleWord(true);
        searchAreaPane=new JScrollPane(searchArea);
        searchAreaPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        searchLabel=new JLabel("Search");

        result=new JLabel("Result");
        result.setFont(font);

        searchLabel.setFont(font);
        searchArea.setFont(font);
        searchArea.addKeyListener(new SearchKeyListener());
        searchArea.addCaretListener(new SearchCaretListener());
        upButton=new JButton(" UP ");
        downButton=new JButton("DOWN");
        searchAreaPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        downButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hexPane.getHighlighter().removeAllHighlights();
                hexPane.getTextPane().getHighlighter().removeAllHighlights();
                search(searchArea.getText(),0);
            }
        });

        upButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hexPane.getHighlighter().removeAllHighlights();
                hexPane.getTextPane().getHighlighter().removeAllHighlights();
                search(searchArea.getText(),1);
            }
        });

        upButton.setFont(font);
        downButton.setFont(font);

        searchPanel.setLayout(new GridBagLayout());

        searchPanel.add(searchLabel,new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTH,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));

        searchPanel.add(searchAreaPane,new GridBagConstraints(0,1,4,1,0,0,GridBagConstraints.NORTH,GridBagConstraints.BOTH,new Insets(1, 1, 1, 1),0,0));

        searchPanel.add(upButton,new GridBagConstraints(0,2,2,1,0,0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(1, 1, 1, 1),0,0));

        searchPanel.add(downButton,new GridBagConstraints(2,2,1,1,0,0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(1, 1, 1, 1),0,0));

        searchPanel.add(result,new GridBagConstraints(0,3,4,1,0,0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(1, 1, 1, 1),0,0));


        searchPanel.setBackground(new Color(255, 229, 229));
        setLayout(new GridBagLayout());

        add(searchPanel,new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(1, 1, 1, 1),0,0));




        searchArea.getInputMap().put(KeyStroke.getKeyStroke("BACK_SPACE"), "none");
        searchArea.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "none");


        Action doNothing = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                // do nothing
            }
        };
        searchArea.getInputMap().put(KeyStroke.getKeyStroke("ctrl X"),
                "doNothing");

        searchArea.getInputMap().put(KeyStroke.getKeyStroke("ctrl A"),
                "doNothing");
        searchArea.getInputMap().put(KeyStroke.getKeyStroke("ctrl C"),
                "doNothing");

        searchArea.getInputMap().put(KeyStroke.getKeyStroke("shift LEFT"),
                "doNothing");
        searchArea.getInputMap().put(KeyStroke.getKeyStroke("shift RIGHT"),
                "doNothing");

        searchArea.getInputMap().put(KeyStroke.getKeyStroke("shift UP"),
                "doNothing");
        searchArea.getInputMap().put(KeyStroke.getKeyStroke("shift DOWN"),
                "doNothing");

        searchArea.getInputMap().put(KeyStroke.getKeyStroke("ctrl V"),
                "doNothing");


        searchArea.getActionMap().put("doNothing",
                doNothing);




    }


    public class SearchKeyListener extends KeyAdapter {
        @Override
        public void keyTyped(KeyEvent e) {
            char c = Character.toUpperCase(e.getKeyChar());
            int pos = searchArea.getCaretPosition();
            int code = e.getKeyCode();
            Document doc = searchArea.getDocument();
            e.consume(); // ignore event
            if ((Arrays.stream(hexPane.getKeys()).anyMatch(x -> x == c))) {
                String str = String.valueOf(c);
                if (symbolsCount > pos) {
                    if (pos % 3 == 0) {
                        inserting=true;
                        searchArea.replaceRange(str, pos, pos + 1);
                        inserting=false;
                        searchArea.setCaretPosition(pos+1);
                    }
                    if (pos % 3 == 1) {
                        inserting=true;
                        searchArea.replaceRange(str, pos, pos + 1);
                        inserting=false;
                        searchArea.setCaretPosition(pos+2);
                    }
                } else {
                    symbolsCount += 3;
                    inserting=true;
                    searchArea.insert(str+"0 ", pos);
                    inserting=false;
                    searchArea.setCaretPosition(pos + 1);
                }
            }
            if (c == KeyEvent.VK_BACK_SPACE) {
                if (pos != 0 && pos % 3 == 0) {
                    try {
                        symbolsCount -= 3;
                        searchArea.getDocument().remove(pos - 3, 3);
                        searchArea.setCaretPosition(pos-3);
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
            int pos = searchArea.getCaretPosition();
            if (code == KeyEvent.VK_LEFT) {
                if (pos > 0) {
                    if (pos % 3 == 0)
                        searchArea.setCaretPosition(pos - 3);
                    if (pos % 3 == 1 && pos > 1)
                        searchArea.setCaretPosition(pos - 4);
                    if (pos % 3 == 2)
                        searchArea.setCaretPosition(pos - 2);
                    if (pos == 1)
                        searchArea.setCaretPosition(0);
                }
            }
            if (code == KeyEvent.VK_RIGHT) {
                if (pos < symbolsCount) {    // Было symbolsCount
                    if (pos % 3 == 0) {
                        searchArea.setCaretPosition(pos + 3);
                    }
                    if (pos % 3 == 1) {
                        searchArea.setCaretPosition(pos + 2);
                    }
                    if (pos == symbolsCount - 1) {
                        searchArea.setCaretPosition(symbolsCount);
                    }
                    if (pos % 3 == 2)
                        searchArea.setCaretPosition(pos + 1);
                }
            }
            if (code == KeyEvent.VK_UP) {
                if (pos > columns) { // заменить на смещение по строкам
                    searchArea.setCaretPosition(pos - columns - 1);
                }
            }
            if (code == KeyEvent.VK_DOWN) {
                if (pos < symbolsCount-columns) { // заменить на смещение по строкам // было < symbolsCount
                    searchArea.setCaretPosition(pos + columns + 1);
                }
            }

        }

    }

    public class SearchCaretListener implements CaretListener{

        private Highlighter h;

        SearchCaretListener(){
            h=searchArea.getHighlighter();
        }


        @Override
        public void caretUpdate(CaretEvent e) {
            if(!inserting){
                int pos=searchArea.getCaretPosition();
                int size=getSymbolsCount();
                h.removeAllHighlights();
                if (pos != size) { // Чтобы не было бага с переносом новой строки
                    try {
                        if (pos % 3 == 0) {
                            h.addHighlight(pos, pos + 2, DefaultHighlighter.DefaultPainter);
                        }
                        if (pos % 3 == 1) {
                            h.addHighlight(pos - 1, pos+1, DefaultHighlighter.DefaultPainter);
                        }

                    } catch (BadLocationException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }


   public void search(String str,int direction){
            if(!strFound.equals(str)){
                strFound=str;
                hexPane.setLastIndFound(-1);
                System.out.println("Я обнулил индекс!");
            }
            String[] arr=str.split(" ");
            byte[] searchArr=new byte[arr.length];
            for(int i=0;i<searchArr.length;i++){
                searchArr[i]=(byte) Integer.parseInt(arr[i],16);
            }
            result.setText("Result: in process");
            int k=hexPane.search(searchArr,direction,curByte); // 0 -direction down 1 - direction up;

            if(k==-1){
                result.setForeground(new Color(147, 24, 24));
                result.setText("Result: not found");
            }
            else{
                result.setForeground(new Color(15, 147, 15));
                result.setText("Result: found");
            }
    }

}
