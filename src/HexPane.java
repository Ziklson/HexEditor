import javax.swing.*;

import javax.swing.text.BadLocationException;

import javax.swing.text.Document;

import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;


public class HexPane extends JTextArea {

    private final int[] keys;

    private MyInfoPane infoPane;

    public MyWorkPane getWorkPane() {
        return workPane;
    }

    public void setWorkPane(MyWorkPane workPane) {
        this.workPane = workPane;
    }

    private MyWorkPane workPane;

    private int symbolsCount;

    private int columns;
    private int rows;

    private int page;

    public boolean isInserting() {
        return isInserting;
    }

    public void setInserting(boolean inserting) {
        isInserting = inserting;
    }

    private boolean isInserting;

    private int startBuff;

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    private long fileSize;

    private int prox; // близость

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

    private Path curPath;

    private Path tempFile;

    private static final String TEMP_FILE_POSTFIX = ".tmp";


    private byte[] byteBuffer;

    private ArrayList<Byte> byteArr;

    public int getSymbolsCount() {
        return symbolsCount;
    }

    public void setSymbolsCount(int size) {
        this.symbolsCount = size;
    }

    HexPane(MyWorkPane workPane) {
        super();
        setInserting(false);
        this.workPane=workPane;
        bytes = 16;
        columns = 3 * bytes - 1;
        rows = 20;
        prox=rows*4;
        byteBuffer=new byte[64000];
        byteArr=new ArrayList<Byte>();
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

        int k = Integer.parseInt(str,16);

        if(k < 32)
            return "·";

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
                        setInserting(true);
                        hexPane.replaceRange(str, pos, pos + 1);
                        try {
                            strAtPos = doc.getText(pos, 2);
                        } catch (BadLocationException ex) {
                            ex.printStackTrace();
                        }
                        textPane.replaceRange(hexPane.toChar(strAtPos), pos / 3, pos / 3 + 1);
                        setInserting(false);
                        hexPane.setCaretPosition(pos+1);
                    }
                    if (pos % 3 == 1) {
                        setInserting(true);
                        hexPane.replaceRange(str, pos, pos + 1);
                        try {
                            strAtPos = doc.getText(pos - 1, 2);
                        } catch (BadLocationException ex) {
                            ex.printStackTrace();
                        }
                        textPane.replaceRange(hexPane.toChar(strAtPos), pos / 3, pos / 3 + 1);
                        setInserting(false);
                        if(pos != (columns+1)*rows - 2)
                            hexPane.setCaretPosition(pos+2);
                        else{
                            workPane.getjScrollBarV().setValue(workPane.getjScrollBarV().getValue()+1);
                            setCaretPosition((columns+1)*(rows-1));
                        }
                        int curStr=workPane.getjScrollBarV().getValue();
                        int offset=curStr*bytes+(pos-1)/3;
                        writeTempFile(strAtPos,0,offset);

                    }


                } else {
                    setInserting(true);
                    symbolsCount += 3;
                    infoPane.setFileSizeValueLabel(Integer.toString((symbolsCount+1)/3));
                    textPane.insert(hexPane.toChar(str+"0"), pos / 3);
                    hexPane.insert(str+"0 ", pos);
                    setInserting(false);
                    hexPane.setCaretPosition(pos + 1);


                }
            }
            if (c == KeyEvent.VK_BACK_SPACE) {
                if (pos != 0 && pos % 3 == 0) {
                    try {
                        symbolsCount -= 3;

                        String strAtPos = doc.getText(pos - 3, 2);

                        infoPane.setFileSizeValueLabel(Integer.toString((symbolsCount+1)/3));
                        textPane.getDocument().remove(pos/3 -1,1);
                        hexPane.getDocument().remove(pos - 3, 3);
//                        textPane.getDocument().remove(pos/3 -1,1);

                        int curStr=workPane.getjScrollBarV().getValue();
                        int offset=curStr*bytes+(pos-3)/3;
                        writeTempFile(strAtPos,-1,offset);

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
            int str = workPane.getjScrollBarV().getValue();
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
                else{
                    if(str != 0){
                        workPane.getjScrollBarV().setValue(str-1);
                        hexPane.setCaretPosition(columns-2);
                    }
                }

            }
            if (code == KeyEvent.VK_RIGHT) {
                if (pos < (rows*(columns+1)) - 3) {    // Было symbolsCount
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
                else{
                    if(str != workPane.getjScrollBarV().getMaximum()){
                        workPane.getjScrollBarV().setValue(str+1);
                        setCaretPosition((columns+1)*(rows-1));
                    }
                }
            }
            if (code == KeyEvent.VK_UP) {
                if (pos > columns) { // заменить на смещение по строкам
                    hexPane.setCaretPosition(pos - columns - 1);
                }
                else{
                    if(str != 0){
                        workPane.getjScrollBarV().setValue(str-1);
                        setCaretPosition(pos);
                    }
                }

            }
            if (code == KeyEvent.VK_DOWN) {
                if (pos < (rows-1)*(columns+1)) { // заменить на смещение по строкам // было < symbolsCount
                    hexPane.setCaretPosition(pos + columns + 1);
                }
                else{
                    if(str != workPane.getjScrollBarV().getMaximum()){
                        workPane.getjScrollBarV().setValue(str+1);
                        setCaretPosition(pos);
                    }
                }
            }

        }
    }

    public MyInfoPane getInfoPane() {
        return infoPane;
    }

    public void setInfoPane(MyInfoPane infoPane) {
        this.infoPane = infoPane;
    }




    public void readFile(Path path, long pos, boolean firstRead){
        try(RandomAccessFile raf = new RandomAccessFile(path.toString(), "r")){

            System.out.println("Я прочитал с позиции " + pos);
            curPath=path;

            raf.seek(pos);
            Arrays.fill(byteBuffer,(byte) 0);
            raf.read(byteBuffer, 0, byteBuffer.length);


            byteArr.clear();



            for(byte b: byteBuffer){
                byteArr.add(new Byte(b));
            }

            startBuff=page*byteBuffer.length/2;
            System.out.println("StartBuff "+ startBuff);




            if(firstRead){
                insertPage((int) pos);
                long size=raf.length();
                setFileSize(size);
                int barSize=(int) size/bytes + 1;
                workPane.setJScrollBarVSize(barSize);
                infoPane.setFileSizeValueLabel(Long.toString(size));
                setSymbolsCount(rows*columns+rows+3);
                createTempFile(path);
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void insertPage(int pos){
        StringBuilder sbHex = new StringBuilder();
        StringBuilder sbText = new StringBuilder();
        for(int i=pos;i<pos+bytes*rows;i++) {
            int a = (int) byteBuffer[i];
            if (a < 0)
                a += 256;
            String str=Integer.toHexString(a);
            if(str.length() == 1){
                sbHex.append("0" + str);
                sbText.append(toChar("0"+str));
            }

            else{
                sbHex.append(str);
                sbText.append(toChar(str));
            }

            if(i != pos+bytes*rows -1){
                if((i != 0) && ((i+1) % bytes == 0)){
                    sbHex.append("\n");
                }
                else{
                    sbHex.append(" ");
                }
            }
        }


        setInserting(true);
        setText(sbHex.toString());
        textPane.setText(sbText.toString());
        setInserting(false);

    }


    public void readBuffer(int pos){

        int k=byteBuffer.length/bytes; // сколько строк вмещается в буффер

        if(pos*bytes > startBuff +byteBuffer.length || pos*bytes < startBuff){
            page=pos*bytes/(byteBuffer.length/2);
            System.out.println("Я отработал пушто был какой то резкий переход " + page );
            readFile(curPath,page*byteBuffer.length/2, false);
        }

        if(startBuff !=0 && pos < startBuff/bytes +prox){
            System.out.println("Я на нижней границе! " + pos);
            page-=1;
            readFile(curPath, page*byteBuffer.length/2,false);
        }
        else{
            if(startBuff+byteBuffer.length<getFileSize() && pos>(startBuff/bytes)+k - prox){
                System.out.println("Я на верхней границе! " + pos);
                page+=1;
                readFile(curPath, page*byteBuffer.length/2,false);
            }
            else{

                insertPage((pos-startBuff/bytes)*bytes);
            }
        }
    }


    public void createTempFile(Path path){
        try {
            if (tempFile == null) {
                tempFile = Files.createTempFile(Paths.get("").toAbsolutePath(),"history", TEMP_FILE_POSTFIX);
                tempFile.toFile().deleteOnExit();
                tempFile.toFile().setWritable(true);
            }
        } catch (IOException ex){
            System.err.println(ex);
        }

    }

    public void writeTempFile(String str,int operation,int offset){
        try(RandomAccessFile raf = new RandomAccessFile(tempFile.toString(), "rw")){
            StringBuilder sb= new StringBuilder();
            raf.seek(raf.length());
            sb.append(offset);
            sb.append(' ');
            sb.append(operation);
            sb.append(' ');
            sb.append(str);
            sb.append('\n');
            //raf.writeBytes(sb.toString());
            raf.write(sb.toString().getBytes(StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
