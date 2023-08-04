import javax.swing.*;

import javax.swing.text.*;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class HexPane extends JTextArea {

    private final int[] keys;

    private MyJFrame myJFrame;

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


    public void setBufferChanged(boolean bufferChanged) {
        BufferChanged = bufferChanged;
    }

    public boolean isBufferChanged() {
        return BufferChanged;
    }

    private boolean BufferChanged;
    private boolean saving;


    public int getStartBuff() {
        return startBuff;
    }

    private int startBuff;

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    private long fileSize;

    private int fileInBuffCount;

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

    private int limit_file_per_dir;

    public Path getCurPath() {
        return curPath;
    }

    private Path curPath;

    private Path curDir;
    private Path tempFile;

    public void setLastIndFound(int lastIndFound) {
        this.lastIndFound = lastIndFound;
    }

    private int lastIndFound;

    private byte[] byteBuffer;

    public int getBuffFullness() {
        return buffFullness;
    }

    private int buffFullness;

    public List<Byte> getByteArr() {
        return byteArr;
    }

    private List<Byte> byteArr;

    private Font font;
    public int getSymbolsCount() {
        return symbolsCount;
    }

    public void setSymbolsCount(int size) {
        this.symbolsCount = size;
    }

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

    boolean isCutting;


    public int getCurSize() {
        return curSize;
    }

    public void setCurSize(int curSize) {
        this.curSize = curSize;
    }

    private int curSize;

    public int getSelStart() {
        return selStart;
    }

    public void setSelStart(int selStart) {
        this.selStart = selStart;
    }

    private int selStart;

    public int getSelEnd() {
        return selEnd;
    }

    public void setSelEnd(int selEnd) {
        this.selEnd = selEnd;
    }

    private int selEnd;

    public boolean isSelecting() {
        return isSelecting;
    }

    public void setSelecting(boolean selecting) {
        isSelecting = selecting;
    }

    private boolean isSelecting;

    public Object getSelHigh() {
        return selHigh;
    }

    public void setSelHigh(Object selHigh) {
        this.selHigh = selHigh;
    }

    private Object selHigh;


    HexPane(MyWorkPane workPane, MyJFrame myJFrame) {
        super();
        //
        curSize=0;
        selStart=0;
        selEnd=0;
        isSelecting=false;
        font=new Font(Font.MONOSPACED, Font.PLAIN,22);
        int charW=getFontMetrics(font).charWidth(' ');
        int charH=getFontMetrics(font).getHeight();
//        hMouse=new DefaultHighlighter.DefaultHighlightPainter(Color.GRAY);

        isCutting=false;
//
        this.myJFrame=myJFrame;
        setInserting(false);
        setBufferChanged(false);
        this.workPane=workPane;
        curDir=Paths.get("").toAbsolutePath();
        startBuff=0;
       // bytes = 16;
        lastIndFound=-1;
        setMinimumSize(new Dimension(0,0));
        bytes=16;
        columns = 3 * bytes - 1;
        buffFullness=0;
        rows = 20;
        prox=rows*4;
        byteBuffer=new byte[64000];
        byteArr=new ArrayList<Byte>();

        limit_file_per_dir=byteBuffer.length/2 * 400; // Ограничиваем по 400 файлов в одной dir
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

        DefaultCaret caret=(DefaultCaret) getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
//
        setPreferredSize(new Dimension(0,0));

        //
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




        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                int x=e.getX();
                int y=e.getY();
                int str=workPane.getjScrollBarV().getValue();

                int xPos=x/charW;

                int yPos=(y/charH)*(columns+1);

                xPos+=yPos;


                //selStart=(xPos/3)*(str+1);
                int cp=getCaretPosition();
                if(cp % 3 == 2)
                    cp+=1;

                selStart=(cp/3) + (str*bytes);

                isSelecting=true;



            }

            @Override
            public void mouseReleased(MouseEvent e) {
                int x=e.getX();
                int y=e.getY();
                int str=workPane.getjScrollBarV().getValue();
                if(x>(charW*columns))
                    x=charW*columns;
                if(y>charH*rows)
                    y=charH*(rows-1);

                int xPos=x/charW;

                int yPos=(y/charH)*(columns+1);

                xPos+=yPos;

                selEnd=(xPos/3) + (str*bytes);

                if(selEnd < selStart)
                    selStart-=1;
//                int cp=getCaretPosition()/3 + (str*bytes);
//                if(cp < selStart)
//                    selStart-=1;
//
//                selEnd=cp;

                if(selEnd == selStart){
                    if(selHigh != null)
                        getHighlighter().removeHighlight(selHigh);
                    if(textPane.getSelHigh() != null)
                        textPane.getHighlighter().removeHighlight(textPane.getSelHigh());
                }



                if(getCaretHigh() != null)
                    getHighlighter().removeHighlight(caretHigh);
                if(textPane.getCaretHigh() != null)
                    textPane.getHighlighter().removeHighlight(textPane.getCaretHigh());
                isSelecting=false;
                doSelection();
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });



        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {


                int x=e.getX();
                int y=e.getY();
                int str=workPane.getjScrollBarV().getValue();

                if(selHigh != null)
                    getHighlighter().removeHighlight(selHigh);
                if(textPane.getSelHigh() != null)
                    textPane.getHighlighter().removeHighlight(textPane.getSelHigh());



                int startText=getHighlighter().getHighlights()[getHighlighter().getHighlights().length-1].getStartOffset();

                int endText=getHighlighter().getHighlights()[getHighlighter().getHighlights().length-1].getEndOffset();



                try {
                    textPane.setSelHigh(textPane.getHighlighter().addHighlight(startText/3,endText/3,DefaultHighlighter.DefaultPainter));
                } catch (BadLocationException ex) {
                    throw new RuntimeException(ex);
                }

                if(y < 0 && str > 0){


                    workPane.getjScrollBarV().setValue(str-1);
                    int xPos=x/charW;
                    if(xPos < 0)
                        xPos=0;

                    if(xPos > (columns+1))
                        xPos=(columns+1);
                    try {
                        int end;

                        end=(selStart-((str-1)*bytes))*3 + 3;

                        if(end < xPos || end > (rows*columns+1))
                            end=rows*(columns+1);


                        selHigh=getHighlighter().addHighlight(xPos,end,DefaultHighlighter.DefaultPainter);
                        textPane.setSelHigh(textPane.getHighlighter().addHighlight(xPos/3,end/3,DefaultHighlighter.DefaultPainter));


                    } catch (BadLocationException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                if(y>rows*charH && str < workPane.getjScrollBarV().getMaximum()-20){

                        workPane.getjScrollBarV().setValue(str+1);
                        int xPos=x/charW;
                        if(xPos < 0)
                            xPos=0;
                        if(xPos > (columns+1))
                            xPos=(columns+1);
                        int yPos=(rows-1)*(columns+1);
                        xPos+=yPos;
                        try {
                            int start;

                            start=(selStart-((str+1)*bytes))*3;
                            if(start < 0 || start > xPos)
                                start=0;

                            selHigh=getHighlighter().addHighlight(start,xPos,DefaultHighlighter.DefaultPainter);
                            textPane.setSelHigh(textPane.getHighlighter().addHighlight(start/3,xPos/3,DefaultHighlighter.DefaultPainter));


                        } catch (BadLocationException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }


            @Override
            public void mouseMoved(MouseEvent e) {
                int x=e.getX();
                int y=e.getY();
                if(x % (3*charW) < 2*charW){
                    int xPos=x/charW;
                    int yPos=(y/charH)*(columns+1);
                    xPos+=yPos;
                    try {
                        if(mouseHigh != null)
                            getHighlighter().removeHighlight(mouseHigh);
                        if(textPane.getMouseHigh() != null)
                            textPane.getHighlighter().removeHighlight(textPane.getMouseHigh());
                        if(xPos % 3 == 0){
                            mouseHigh=getHighlighter().addHighlight(xPos,xPos+2,new DefaultHighlighter.DefaultHighlightPainter(new Color(198, 237, 248)));
                        }
                        if(xPos % 3 == 1){
                            mouseHigh=getHighlighter().addHighlight(xPos - 1,xPos+1,new DefaultHighlighter.DefaultHighlightPainter(new Color(198, 237, 248)));
                        }
                        textPane.setMouseHigh(textPane.getHighlighter().addHighlight(xPos/3,xPos/3+1,new DefaultHighlighter.DefaultHighlightPainter(new Color(198, 237, 248))));
                    } catch (BadLocationException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

    }


    public void doSelection(){
        if(selHigh != null)
            getHighlighter().removeHighlight(selHigh);

        if(textPane.getSelHigh() != null)
            textPane.getHighlighter().removeHighlight(textPane.getSelHigh());

        if(selStart != selEnd){
//            System.out.println("Start Sel " +selStart + " endSel " + selEnd);
            if(selStart > selEnd){
                int temp=selStart;
                selStart=selEnd;
                selEnd=temp;
            }

            int start;
            int end;
            int str=workPane.getjScrollBarV().getValue();
            if(selStart < str*bytes){
                start=0;
            }
            else{
                if(selStart < str*bytes + rows*bytes){
                    start=(selStart-((str)*bytes))*3;
                }
                else{
                    start=-1;
                }
            }
            if(selEnd<str*bytes){
                end=-1;
            }
            else{
                if(selEnd < str*bytes + rows*bytes){
                    end=(selEnd-((str)*bytes))*3+2;
                }
                else{
                    end=rows*bytes*3;
                }
            }
            if(end < 0 || start < 0){
                // nothing
            }
            else{
                try {
                    selHigh=getHighlighter().addHighlight(start,end,DefaultHighlighter.DefaultPainter);
                    textPane.setSelHigh(textPane.getHighlighter().addHighlight(start/3,end/3 + 1,DefaultHighlighter.DefaultPainter));
                } catch (BadLocationException e) {
                    throw new RuntimeException(e);
                }
//                System.out.println("Start  " +start + " end " + end);
            }

        }
    }

    public int[] getKeys() {
        return keys;
    }


    public String toChar(String str) {



        StringBuilder output = new StringBuilder("");

        int k = Integer.parseInt(str,16);

        if(k < 32 || k == 127)
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

                        int curStr=workPane.getjScrollBarV().getValue();
                        int offset=curStr*bytes+pos/3 - startBuff;
                        if(!isBufferChanged())
                            setBufferChanged(true);
                        Byte b= (byte) Integer.parseInt(strAtPos,16);
                        byteArr.set(offset,b);

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
                        int offset=curStr*bytes+(pos-1)/3 - startBuff;
                        if(!isBufferChanged())
                            setBufferChanged(true);
                        Byte b= (byte) Integer.parseInt(strAtPos,16);
                        byteArr.set(offset,b);
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

                        setFileSize(getFileSize()-1);

                        int curStr=workPane.getjScrollBarV().getValue();
                        int offset=curStr*bytes+(pos-3)/3 - curSize;

                        if(!isBufferChanged())
                            setBufferChanged(true);
                        byteArr.remove(offset);
                        insertPage((workPane.getjScrollBarV().getValue()-curSize/bytes)*bytes);
                        setCaretPosition(pos-3);
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
                        if(pos == 0){
                            setCaretPosition(1); // Он не хотел регистрировать переход на 0 как событие обновление каретки, пришлось делать такой костыль
                        }
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




    public void readFile(Path path, long pos, boolean firstRead,boolean saving){
        if(firstRead){
            try(RandomAccessFile raf=new RandomAccessFile(path.toString(),"r")){
                long size=raf.length();
                byteArr.clear();
                Arrays.fill(byteBuffer,(byte) 0);
                if(size < byteBuffer.length)
                    buffFullness=(int) size;
                else
                    buffFullness=byteBuffer.length;
                raf.read(byteBuffer,0,byteBuffer.length);
                byteArr = IntStream.range(0, byteBuffer.length).mapToObj(i -> byteBuffer[i]).collect(Collectors.toList());
                curPath=path;

                if(buffFullness <=byteBuffer.length/2)
                    fileInBuffCount=1;
                else
                    fileInBuffCount=2;
                int barSize=(int) size/bytes + 20;

                workPane.setJScrollBarVSize(barSize);
                infoPane.setFileSizeValueLabel(Long.toString(size));
                setSymbolsCount((int) size * 3 - 1);
                setFileSize(size);
                workPane.getjScrollBarV().setVisibleAmount(rows);
                workPane.getjScrollBarV().setValue(0);
                infoPane.setFileNameValueLabel(curPath.getFileName().toString());
                insertPage(0);
                if(Files.exists(Paths.get(curDir.toString()+"/tmp")))
                    deleteTempDir(Paths.get(curDir.toString() + "/tmp"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            Path source;
            long size=0;
            int offset=0;
            fileInBuffCount=0;
            buffFullness=0;
            byteArr.clear();
            Arrays.fill(byteBuffer,(byte) 0);

            while((curSize+buffFullness < getFileSize())){
                if(isTempFileExist((int) pos)){
                    source=getTempFile((int) pos);
                    try {
                        size=Files.size(source);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    if(Files.exists(getInsLogFile(),LinkOption.NOFOLLOW_LINKS)){
                        offset=getInsOffset((int) pos);
                    }
                    source=curPath;
                    size=byteBuffer.length/2;
                }
                if(buffFullness+size <= byteBuffer.length){
                    try(RandomAccessFile raf=new RandomAccessFile(source.toString(),"r")){
                        if(source.equals(curPath)){
                            raf.seek(pos - offset);
                            int k=(int) pos-offset;
//                            System.out.println("Pos-offset " + k);
                            raf.read(byteBuffer,buffFullness,(int) size);
                        }
                        else{
                            raf.read(byteBuffer,buffFullness,(int) size);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    buffFullness+=size;
                    fileInBuffCount++;
                    pos+=byteBuffer.length/2;
                }
                else{
                    break;
                }
            }
            if(!saving){
                byteArr = IntStream.range(0, buffFullness).mapToObj(i -> byteBuffer[i]).collect(Collectors.toList());
                int str=workPane.getjScrollBarV().getValue()*bytes;

                if(!isCutting)
                    insertPage(str-curSize);
            }
        }
    }

    public void insertPage(int pos){
        StringBuilder sbHex = new StringBuilder();
        StringBuilder sbText = new StringBuilder();
        for(int i=pos,j=0;i<pos+bytes*rows;i++,j++) {
            int a = (int) byteArr.get(i);
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

//            if(i != pos+bytes*rows -1){
//                if((i != 0) && ((i+1) % bytes == 0)){
//                    sbHex.append("\n");
//                }
//                else{
//                    sbHex.append(" ");
//                }
//            }
            if(j != bytes*rows -1){
                if((j != 0) && ((j+1) % bytes == 0)){
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



//    public class BufferWorker extends SwingWorker<Object, Object> {
//        int size;
//        int curBuff;
//        int size2;
//        int str;
//        BufferWorker(int str){
//          size=0;
//          curBuff=0;
//          size2=0;
//          this.str=str;
//        }
//
//        @Override
//        protected Object doInBackground() throws Exception {
//            while(size <= str){
//                if(isTempFileExist(curBuff)){
//                    size2=(int) getTempFileSize(getTempFile(curBuff));
////                size+=getTempFileSize(getTempFile(curBuff));
//                }
//                else{
//                    size2=byteBuffer.length/2;
////                size+=byteBuffer.length/2;
//                }
//                curBuff+=byteBuffer.length/2;
//                size+=size2;
//            }
//            curSize=size-size2;
//            return null;
//        }
//    }



    public int getCurBuff(int str){
        int size=0;
        int curBuff=0;
        int size2=0;
//        System.out.println("Old curSize " + curSize);
////        System.out.println("Old startBuff " + startBuff);
        curBuff=startBuff;
        size=curSize;
        if(size <= str){
            while(size <= str){
                if(isTempFileExist(curBuff)){
                    size2=(int) getTempFileSize(getTempFile(curBuff));
                }
                else{
                    size2=byteBuffer.length/2;
                }
                curBuff+=byteBuffer.length/2;
                size+=size2;
            }
            curSize=size-size2;
            curBuff-=byteBuffer.length/2;
        }
        else{
            curBuff-=byteBuffer.length/2;
            if(curBuff < 0){
                curBuff=0;
                curSize=0;
            }
            else{
                while(size > str){
                    if(isTempFileExist(curBuff)){
                        size2=(int) getTempFileSize(getTempFile(curBuff));
                    }
                    else{
                        size2=byteBuffer.length/2;
                    }
                    curBuff-=byteBuffer.length/2;
                    size-=size2;
//                    if(size <= 0 || curBuff <= 0){
//                        size=0;
//                        curBuff=0;
//                        break;
//                    }
                }
                curSize=size;
                curBuff+=byteBuffer.length/2;
            }
        }
//        System.out.println("curSize " + curSize);
//        System.out.println("Str " + str);
//        System.out.println("StartBuff " + curBuff);
        return curBuff;

//        while(size <= str){
//            if(isTempFileExist(curBuff)){
//                size2=(int) getTempFileSize(getTempFile(curBuff));
//            }
//            else{
//                size2=byteBuffer.length/2;
//            }
//            curBuff+=byteBuffer.length/2;
//            size+=size2;
//        }
//        curSize=size-size2;
//        System.out.println("curSize " + curSize);
//        System.out.println("Str " + str);
//        int ppl=curBuff-byteBuffer.length/2;
//        System.out.println("StartBuff " + ppl);

        //return curBuff-byteBuffer.length/2;
    }

    public void readBuffer(int pos){ // pos - положение JScrollBar
        if(((curSize+buffFullness < getFileSize())&&(pos*bytes > curSize+buffFullness)) || (pos*bytes < curSize)){
            if(isBufferChanged()){
                setBufferChanged(false);
                saveBuffer(startBuff);
            }
            int l=pos*bytes;
            startBuff=getCurBuff(pos*bytes);
            readFile(curPath,startBuff, false,false);
        }
        else{
            if((startBuff != 0) & (pos*bytes < curSize + prox*bytes)){
                if(isBufferChanged()){
                    setBufferChanged(false);
                    saveBuffer(startBuff);
                }
                startBuff-=byteBuffer.length/2;
                if(isTempFileExist(startBuff)){
                    curSize-=getTempFileSize(getTempFile(startBuff));
                }
                else{
                    curSize-=byteBuffer.length/2;
                }
//                System.out.println("StartBuff " + startBuff);
                readFile(curPath,startBuff, false,false);

            }
            else{
                if((curSize+buffFullness < getFileSize()) && (pos*bytes > curSize+buffFullness-prox*bytes)){
                    if(isBufferChanged()){
                        setBufferChanged(false);
                        saveBuffer(startBuff);
                    }
                    if(isTempFileExist(startBuff)){
                        curSize+=getTempFileSize(getTempFile(startBuff));
                    }
                    else{
                        curSize+=byteBuffer.length/2;
                    }
                    startBuff+=byteBuffer.length/2;
//                    System.out.println("StartBuff " + startBuff);
                    readFile(curPath,startBuff, false,false);
                }
                else{
                    insertPage(pos*bytes - curSize);
                }
            }
        }
//        if((pos*bytes > startBuff + buffFullness) || (pos*bytes < startBuff)){ // pos*bytes - на каком байте сейчас JScrollBar (байт на начало строки)
//            if(isBufferChanged()){
//                setBufferChanged(false);
//                saveBuffer(startBuff);
//            }
//            page=pos*bytes/(byteBuffer.length/2); // page - номер буффера(половинки), (0-32000) - 0, (32000-64000) - 1 и т.д.
////            startBuff=page*byteBuffer.length/2;
////            Arrays.fill(byteBuffer,(byte) 0);
////            byteArr.clear();
////            buffFullness=0;
//            startBuff=getCurBuff(pos*bytes);
////            System.out.println("CurBuff " + getCurBuff(pos*bytes));
//            System.out.println("StartBuff " + startBuff);
////            System.out.println("Я отработал из за резкого перехода");
//            readFile(curPath,startBuff, false,false);
//            int out2 =(pos-startBuff/bytes)*bytes;
////            System.out.println("Pos "+pos);
////            int out3 =(pos-curSize/bytes)*bytes;
////            System.out.println("out3 "+out3);
//        }
//        else{
////            if(startBuff !=0 && pos<startBuff/bytes + prox){
//            if(startBuff !=0 && pos<curSize/bytes + prox){
//                if(isBufferChanged()){
//                    setBufferChanged(false);
//                    saveBuffer(startBuff);
//                }
//                page-=1;
////                startBuff-=byteBuffer.length/2;
//                startBuff=getCurBuff((pos-prox)*bytes);
////                if(isTempFileExist(startBuff)){
////                    curSize-=getTempFileSize(getTempFile(startBuff));
////                }
////                else{
////                    curSize-=byteBuffer.length/2;
////                }
////                Arrays.fill(byteBuffer,(byte) 0);
////                byteArr.clear();
////                buffFullness=0;
//                readFile(curPath, startBuff,false,false);
//            }
//            else{
//                int k = buffFullness/bytes;
////                if((startBuff+buffFullness < getFileSize()) && (pos>(startBuff/bytes)+k-prox)){
//                    if((startBuff+buffFullness < getFileSize()) && (pos>(curSize/bytes)+k-prox)){
//                        if(isBufferChanged()){
//                        setBufferChanged(false);
//                        saveBuffer(startBuff);
//                    }
//                    page+=1;
////                    if(isTempFileExist(startBuff)){
////                        curSize+=getTempFileSize(getTempFile(startBuff));
////                    }
////                    else{
////                        curSize+=byteBuffer.length/2;
////                    }
////                    startBuff+=byteBuffer.length/2;
//                      startBuff=getCurBuff((pos+k)*bytes);
////                    Arrays.fill(byteBuffer,(byte) 0);
////                    byteArr.clear();
////                    buffFullness=0;
//                    readFile(curPath, startBuff,false,false);
//                }
//                else{
////                    insertPage((pos-startBuff/bytes)*bytes);
//                    insertPage((pos-curSize/bytes)*bytes);
//                }
//            }
//        }

    }


    public Path createTempFile(int startBuff){
        int dirNumb=startBuff/limit_file_per_dir;

        try {
            Files.createDirectories(Paths.get(curDir.toString() + "\\tmp\\"  + dirNumb));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            tempFile=Files.createFile(Paths.get(curDir.toString()+"\\tmp\\"+dirNumb+"\\"+startBuff+"b.tmp"));

        } catch (IOException ex){
            System.err.println(ex);
        }
        return tempFile;
    }

    public void saveBuffer(int offset){ //offset == startBuffer

        int size=byteArr.size();
        int curBuff=offset;
        int pos=0;
        Path path;
        boolean enoughSpace=false; // есть место чтобы дозаписать в последний файл

        int preLastFileSize=byteBuffer.length/2;
        int lastFileSize=0;


//        if(size <= byteBuffer.length){
//            if(size <= byteBuffer.length/2){
//                if(isTempFileExist(offset)){
//                    path=getTempFile(offset);
//                }
//                else{
//                    path=createTempFile(offset);
//                }
//                writeTempFile(path,size,0);
//
//                if(isTempFileExist(offset+byteBuffer.length/2)){
//                    path=getTempFile(offset+byteBuffer.length/2);
//                }
//                else{
//                    path=createTempFile(offset+byteBuffer.length/2);
//                }
//                writeTempFile(path,0,0);
//            }
//            else{
//                if(isTempFileExist(offset)){
//                    path=getTempFile(offset);
//                }
//                else{
//                    path=createTempFile(offset);
//                }
//                writeTempFile(path,byteBuffer.length/2,0);
//                if(isTempFileExist(offset+byteBuffer.length/2)){
//                    path=getTempFile(offset+byteBuffer.length/2);
//                }
//                else{
//                    path=createTempFile(offset+byteBuffer.length/2);
//                }
//                writeTempFile(path,size-byteBuffer.length/2,byteBuffer.length/2);
//            }
//        }

        //

        int fcount=(size/(byteBuffer.length/2))+1; // сколько файлов вмещает в себя текущий byteArr

        if(size % (byteBuffer.length/2) == 0){
            fcount-=1;
            lastFileSize=byteBuffer.length/2;
        }

        if((size % (byteBuffer.length/2)) >= (byteBuffer.length/4)){
            // можем записывать файлы, мин.размер соблюден
            lastFileSize=size % (byteBuffer.length/2);
        }
        else{
            if(size % (byteBuffer.length/2) == 0){
                lastFileSize=byteBuffer.length/2;
            }
            else{
                preLastFileSize=(byteBuffer.length/2)-(byteBuffer.length/4 - (size % (byteBuffer.length/4))); // before last file size
                lastFileSize=byteBuffer.length/4;
            }
        }

        if(size > fileInBuffCount * byteBuffer.length/2){
            int extraFilesCount=((size-(fileInBuffCount * (byteBuffer.length/2)))/(byteBuffer.length/2))+1;
            if(size % byteBuffer.length/2 == 0){
                extraFilesCount-=1;
            }
            if(isTempFileExist(curBuff+byteBuffer.length)){
                path=getTempFile(curBuff+byteBuffer.length);
                long tmpFileSize=getTempFileSize(path);
                //
//                if((tmpFileSize != byteBuffer.length/2) && (tmpFileSize+(size % byteBuffer.length/2) < byteBuffer.length/2)){
//                    enoughSpace=true;
//                }
                if((tmpFileSize != byteBuffer.length/2) && (tmpFileSize+(size % byteBuffer.length/2) < byteBuffer.length/2) && (tmpFileSize+(size % byteBuffer.length/2) >=byteBuffer.length/4)){
                    enoughSpace=true;
                }
            }
            if(enoughSpace){
                if(extraFilesCount == 1){ // тогда переименовывать файлы не требуется
                    ///
                }
                else{
                    System.out.println("Enough space");
                    int newOffset=(extraFilesCount-1)*byteBuffer.length/2;
                    rename(newOffset,curBuff+byteBuffer.length/2);
                }
            }
            else{
                System.out.println("Not enough space");
                int newOffset=extraFilesCount*byteBuffer.length/2;
                rename(newOffset,curBuff);
            }

        }


        for(int i=1;i<fcount;i++){
            if(isTempFileExist(curBuff)){
                path=getTempFile(curBuff);
            }
            else{
                path=createTempFile(curBuff);
                if(i > 2)
                    writeInsLogFile(curBuff);
            }
            if(i == fcount -1){
                writeTempFile(path,preLastFileSize,pos,false);
                pos+=preLastFileSize;
            }
            else {
                writeTempFile(path,byteBuffer.length/2,pos,false);
                pos+=byteBuffer.length/2;
            }
            curBuff+=byteBuffer.length/2;
            System.out.println("POS " + pos);
        }

        if(fcount == 1){
            int curBuff2=curBuff;
            long wrSize=0;
            Path nextPath=null;
            if(isTempFileExist(curBuff)){
                path=getTempFile(curBuff);
            }
            else{
                path=createTempFile(curBuff);
            }
            while(curBuff2+byteBuffer.length/2 < getFileSize()){
                if(isTempFileExist(curBuff2+byteBuffer.length/2)){
                    nextPath=getTempFile(curBuff2+byteBuffer.length/2);
                    wrSize=getTempFileSize(nextPath);
                }
                else{
                    createTempFile(curBuff2+byteBuffer.length/2);
                    nextPath=curPath;
                    wrSize=byteBuffer.length/2;
                }
                if(wrSize != 0){
                    break;
                }
                else{
                    curBuff2+=byteBuffer.length/2;
                }
            }
            if(nextPath != null){
                byte[] tmpBuff=new byte[(int) wrSize];
                try(RandomAccessFile raf=new RandomAccessFile(nextPath.toString(),"r")) {
                    if(nextPath.equals(curPath)){
                        raf.seek(curBuff2+byteBuffer.length/2);
                    }
                    raf.read(tmpBuff,0,(int) wrSize);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                for(int j=0;j<tmpBuff.length;j++){
                    byteArr.add(tmpBuff[j]);
                }
                writeTempFile(path,byteArr.size()-byteBuffer.length/4,0,false);
                writeTempFile(nextPath,byteBuffer.length/4,byteBuffer.length/4,false);
            }
            else{
               writeTempFile(path,size,0,false);
            }
        }
        else{
            if(isTempFileExist(curBuff)){
                path=getTempFile(curBuff);
            }
            else{
                path=createTempFile(curBuff);
                if(fcount > 2)
                    writeInsLogFile(curBuff);
            }
            if(enoughSpace){
                writeTempFile(path,lastFileSize,pos,true);
            }
            else{
                writeTempFile(path,lastFileSize,pos,false);
            }
        }


//        if(fcount < fileInBuffCount){
//            System.out.println("Buffer has more files");
//            for(int i=fcount+1;i<fileInBuffCount+1;i++){
//                if(isTempFileExist(curBuff+(i-1)*byteBuffer.length/2)){
//                    path=getTempFile(curBuff+(i-1)*byteBuffer.length/2);
//                }
//                else{
//                    path=createTempFile(curBuff+(i-1)*byteBuffer.length/2);
//                }
//                try(RandomAccessFile raf=new RandomAccessFile(path.toString(),"r")) {
//                    raf.setLength(0);
//                } catch (FileNotFoundException e) {
//                    throw new RuntimeException(e);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }

    }

    public void rename(int offset,int startBuff){ //
        long size=getFileSize();
        int end=(((int) size/(byteBuffer.length/2)) + 1)*byteBuffer.length/2;
        int start=startBuff+(byteBuffer.length);
//        int curBuff=end;
        Path path;
        for(int i=end;i>=start;i-=byteBuffer.length/2){
            if(isTempFileExist(i)){
                path=getTempFile(i);
            }
            else{
                continue;
            }
            try {
                Files.move(path,getTempFile(i+offset));
                System.out.println("rename "+path.toString() + " into " +getTempFile(i+offset).toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
//            curBuff-=byteBuffer.length/2;
        }
    }



    public void writeTempFile(Path path, int size,int pos,boolean ins){
        try(RandomAccessFile raf = new RandomAccessFile(path.toString(), "rw")){
//            raf.setLength(size);
            byte[] tmp=new byte[(int)raf.length()];
            if(ins){
                raf.read(tmp,0,(int) raf.length());
                raf.setLength(0);
                raf.setLength(size+raf.length());
                raf.seek(0);
//                for(int i=0;i<tmp.length;i++)
//                    System.out.println(tmp[i]);
            }
            else{
                raf.setLength(size);
            }
            if(size != 0) {
                byte[] newArr = new byte[size];
                for (int i = 0; i < size; i++) newArr[i] = byteArr.get(i+pos);
                raf.write(newArr, 0, size);
            }
            if(ins){
                raf.write(tmp,0,tmp.length);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteTempDir(Path dir){
        try {
            Files.walk(dir)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isTempFileExist(int fileName){ // fileName - startBuf + b + .tmp
        StringBuilder path = new StringBuilder();
        path.append(curDir.toString());
        path.append("\\tmp\\");
        path.append(fileName / limit_file_per_dir);
        path.append("\\" + fileName + "b.tmp");
//        return false;
        return Files.exists(Paths.get(path.toString()), LinkOption.NOFOLLOW_LINKS);
    }

    public Path getTempFile(int fileName){
        StringBuilder path = new StringBuilder();
        path.append(curDir.toString());
        path.append("\\tmp\\");
        path.append(fileName / limit_file_per_dir);
        path.append("\\" + fileName + "b.tmp");
        return Paths.get(path.toString());
    }


    public void setVisibleRows(int rows){
        this.rows=rows;
        this.setRows(rows);
        prox=rows*4;
        textPane.setVisibleRows(rows);
        workPane.setRows(rows);


        readBuffer(workPane.getjScrollBarV().getValue());
    }

    public void setColumnsCount(int columns){
        setBytes(columns);
        this.columns=3*bytes-1;
        this.setColumns(this.columns);
        workPane.setColumns(columns);
        if(curPath != null){
            System.out.println(workPane.getjScrollBarV().getMaximum());
            int barSize=(int) getFileSize()/bytes + 20;
            workPane.setJScrollBarVSize(barSize);
            System.out.println(workPane.getjScrollBarV().getMaximum());
        }
        textPane.setColumnsCount(columns);
        readBuffer(workPane.getjScrollBarV().getValue());
        setPreferredSize(new Dimension(0,0));
        textPane.setPreferredSize(new Dimension(0,0));

    }


    public class ProgressWorker extends SwingWorker<Object, Object> {
        private long size;
        private long pos;
        private int curStartBuf;
        private int bufActSize;
        private boolean overwrite;
        private Path path;

        ProgressWorker(Path path, boolean overwrite){
            pos=0;
            this.path=path;
            this.overwrite=overwrite;
            this.curStartBuf=startBuff;
            this.bufActSize=0;
            size=getFileSize();
        }

        @Override
        protected Object doInBackground() throws Exception {
            if(isBufferChanged()){
                setBufferChanged(false);
                saveBuffer(curStartBuf);
            }
            if(!overwrite){
                try {
                    Files.createFile(path);
                } catch (IOException ex){
                    System.err.println(ex);
                }
            }
            try(RandomAccessFile raf = new RandomAccessFile(path.toString(), "rw")){
                long start_time=System.nanoTime();
                buffFullness=0;
                for(int i=0;i<size;i+=byteBuffer.length){
                    //byteBuffer=new byte[64000];
                    setProgress(i/((int) size/100));
                    if(i != curStartBuf){
                        readFile(curPath,i,false,true);
                        bufActSize=buffFullness;
                    }
                    else{
                        for(int k=0;k<buffFullness;k++){
                            byteBuffer[k]=byteArr.get(k);
                        }
                        bufActSize=byteArr.size();
                    }
                    raf.seek(pos);
                    raf.write(byteBuffer,0,bufActSize);
                    pos+=bufActSize;
                    buffFullness=0;
                   // byteArr.clear();
                }
                raf.setLength(size);
                long end_time=System.nanoTime();
                long result_time=(end_time-start_time)/1000000;
                setProgress(100);
                System.out.println("Запись окончена " + result_time);
            }catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("CURSTART BUF " + curStartBuf);
            //readFile(curPath,0,false,false);
            return null;
        }
    }


    public void saveAs(Path path,boolean overwrite){
        ProgressWorker pw = new ProgressWorker(path,overwrite);
        ProgressBarDialog pd=new ProgressBarDialog(myJFrame);
        pw.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                String name = evt.getPropertyName();
                if (name.equals("progress")) {
                    int progress = (int) evt.getNewValue();
//                    myJFrame.getMyFooterPane().getjProgressBar().setValue(progress);
                    pd.getjProgressBar().setValue(progress);
                    repaint();
                } else if (name.equals("state")) {
                    SwingWorker.StateValue state = (SwingWorker.StateValue) evt.getNewValue();
                    switch (state) {
                        case DONE:
                            pd.getProgressDialog().dispose();
                            break;
                    }
                }
            }
        });
        pw.execute();
    }

    public int search(byte[] searchArr,int direction,int pos_start){ // 0 direction down, 1 - direction up


        // Переписать с учетом curSize

        int str=getWorkPane().getjScrollBarV().getValue();
//        int curBuff=((str*bytes)/byteBuffer.length) * byteBuffer.length;

        int curBuff=getCurBuff(str*bytes);

        if(curBuff != startBuff){
            readFile(curPath,curBuff,false,true);
        }

        int pos=(str*bytes)%byteBuffer.length;


        if(lastIndFound != -1)
            pos=lastIndFound;

        boolean find;
        int offset=0;
        int offset2=0;

        if(direction == 0){ // down
            offset=byteBuffer.length;
            offset2=1;
        }
        else{ // up
            offset=-byteBuffer.length;
            offset2=-1;
        }
            for(int cb=curBuff;cb<getFileSize() && cb>=0;cb+=offset){
                find=true;
                for(int p=pos;p<buffFullness && p>=0;p+=offset2){
                    int p2=p;
                    if((lastIndFound != -1) && ((p <= lastIndFound && direction == 0) || (p>=lastIndFound && direction == 1))) {
                        continue;
                    }
                    for(int p3=0;p3<searchArr.length;p3++){
                        if (p2 < buffFullness - searchArr.length) {
                            if (searchArr[p3] == byteBuffer[p2]) {
                                p2++;
                            } else {
                                find = false;
                                break;
                            }
                        } else {
                            find = false;
                            break;
                        }
                    }
                    if(find){
                        lastIndFound=p;
                        getWorkPane().getjScrollBarV().setValue((p+curBuff)/bytes);
                        try {
                            setCaretPosition((p%bytes)*3);
                            if(searchHigh != null)
                                getHighlighter().removeHighlight(searchHigh);

                            if(textPane.getSearchHigh() != null)
                                textPane.getHighlighter().removeHighlight(textPane.getSearchHigh());

                            setSearchHigh(getHighlighter().addHighlight((p%bytes)*3,(p%bytes)*3+2, new DefaultHighlighter.DefaultHighlightPainter(new Color(248, 198, 198))));
                            textPane.setSearchHigh(textPane.getHighlighter().addHighlight((p%bytes),(p%bytes)+1,new DefaultHighlighter.DefaultHighlightPainter(new Color(248, 198, 198))));
                        } catch (BadLocationException e) {
                            e.printStackTrace();
                        }
                        return 1;
                    }
                    else{
                        find=true;
                    }
                }
                if((curBuff + buffFullness < getFileSize() && direction==0) || (direction==1 && curBuff>=0)){
                    curBuff+=offset;
                    if(curBuff<0)
                        return -1;
                    buffFullness=0;
                    readFile(curPath,curBuff,false,true);
                    if(direction==0)
                        pos=0;
                    else
                        pos=buffFullness-1;
                }
                else{
                    System.out.println("Не найдено");
                    return -1;
                }
            }
        return -1;
    }

    public void insertOver(boolean overwrite){
        Clipboard clipboard=Toolkit.getDefaultToolkit().getSystemClipboard();
        DataFlavor flavor =DataFlavor.stringFlavor;
        int caretPosition=0;
        int pos=0;
        int curBuffer=startBuff;
        if(clipboard.isDataFlavorAvailable(flavor)){
            try {
                String[] ins= clipboard.getData(flavor).toString().split("");
                int str=workPane.getjScrollBarV().getValue();

                if(workPane.getLastFocus() == 0){
                    caretPosition = getCaretPosition();
//                    pos=str*bytes + (caretPosition/3)-startBuff;
                    pos=str*bytes + (caretPosition/3)-curSize;
                }
                else{
                    if(workPane.getLastFocus() == 1){
                        caretPosition=textPane.getCaretPosition();
//                        pos=str*bytes+caretPosition-startBuff;
                        pos=str*bytes+caretPosition-curSize;
                    }
                    else{
                        System.out.println("No Focus");
                        return;
                    }
                }
                if(overwrite){
                    if(ins.length < getFileSize() - str*bytes+caretPosition){
                        for(int i=0;i<ins.length;i++){
                            if(pos < buffFullness){
                                Byte b= ins[i].getBytes()[0];
                                byteArr.set(pos,b);
                                pos++;
                            }
                            else{
                                saveBuffer(curBuffer);
                                curBuffer+=byteBuffer.length;
                                System.out.println("Я хочу прочитать след буффер! " + curBuffer);
                                buffFullness=0;
                                byteArr.clear();
                                readFile(curPath,curBuffer,false,false);
                                pos=0;
                            }
                        }
                        saveBuffer(curBuffer);
                        if(curBuffer != startBuff){
//                            saveBuffer(curBuffer);
                            System.out.println("Я загружаю startBuffer!" + startBuff);
                            buffFullness=0;
                            byteArr.clear();
                            readFile(curPath,startBuff,false,false);
                        }
                        insertPage(str*bytes - curSize);
                        if(workPane.getLastFocus() == 0)
                            setCaretPosition(caretPosition);
                        else
                            textPane.setCaretPosition(caretPosition);
                    }
                    else{
                        System.out.println("Я не работаю!");
                    }
                }
                else{
                    for(int i=0;i<ins.length;i++){
                        Byte b= ins[i].getBytes()[0];
                        byteArr.add(pos,b);
                        pos++;
                    }

                    long newSize=getFileSize() + ins.length;
                    setFileSize(newSize);
                    int barSize=((int) newSize/bytes) + 20;
                    workPane.setJScrollBarVSize(barSize);
                    infoPane.setFileSizeValueLabel(Long.toString(newSize));
                    setSymbolsCount((int) newSize * 3 - 1);

                    saveBuffer(curBuffer);
                    startBuff=getCurBuff(str*bytes);

                    readFile(curPath,startBuff,false,false);
                    insertPage(str*bytes - curSize);
                }


            } catch (UnsupportedFlavorException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void createInsLogFile(){
//        Path path=Paths.get(curDir.toString() + "\\tmp\\" +"insLog.tmp");
        if(!Files.exists(getInsLogFile(),LinkOption.NOFOLLOW_LINKS)){
            try {
                Files.createFile(getInsLogFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Path getInsLogFile(){
        return Paths.get(curDir.toString()+"\\tmp\\"+"insLog.tmp");
    }

    public void writeInsLogFile(int offset){
        Path path =getInsLogFile();
        StringBuilder sb=new StringBuilder();
        sb.append(offset);
        sb.append(" ");
        sb.append(1);
        sb.append("\n");
        try(RandomAccessFile raf = new RandomAccessFile(path.toString(), "rw")){
            long size =raf.length();
            raf.seek(size);
            raf.writeBytes(sb.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public long getTempFileSize(Path path){
        long size=0;
        try(RandomAccessFile raf = new RandomAccessFile(path.toString(), "r")){
            size =raf.length();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return size;
    }


    public int getInsOffset(int startBuff){
        Path path=getInsLogFile();
        String str;
        int count=0;
        try(RandomAccessFile raf = new RandomAccessFile(path.toString(), "rw")){
            str=raf.readLine();

            while(str != null){
                String[] strArr=str.split(" ");
                if(Integer.parseInt(strArr[0]) <startBuff){
                    count++;
                }
                str=raf.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count*byteBuffer.length/2;
    }


    public void cut(){

        isCutting=true;
        int cutCount=selEnd-selStart+1;
        int tmpBuff=startBuff;
        int ind=selStart;
        int str=workPane.getjScrollBarV().getValue();

        if(selStart >= curSize && selStart < curSize+buffFullness){
            startBuff=getCurBuff(selStart);
            readFile(curPath,startBuff,false,false);
        }




        while(cutCount != 0){
            if(selStart >= curSize && selStart < curSize+buffFullness){
                if(ind != 0)
                    ind=selStart-curSize;

                byteArr.remove(ind);
                buffFullness--;
                cutCount--;
            }
            else{
                saveBuffer(startBuff);
                startBuff=getCurBuff(selStart);
                readFile(curPath,startBuff,false,false);
                ind=0;
            }
        }
        saveBuffer(startBuff);
        long size=getFileSize() - (selEnd-selStart+1);
        int barSize=(int) size/bytes + 20;
        workPane.setJScrollBarVSize(barSize);
        infoPane.setFileSizeValueLabel(Long.toString(size));
        setSymbolsCount((int) size * 3 - 1);
        setFileSize(size);

        isCutting=false;

        workPane.getjScrollBarV().setValue(selStart/bytes);
//        startBuff=tmpBuff;
//        readFile(curPath,startBuff,false,false);
//        insertPage(ind);
//

        selStart=0;
        selEnd=0;
        doSelection();
        System.out.println(workPane.getjScrollBarV().getValue());



    }

}
