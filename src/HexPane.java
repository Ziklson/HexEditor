import javax.swing.*;

import javax.swing.text.BadLocationException;

import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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

    public int getSymbolsCount() {
        return symbolsCount;
    }

    public void setSymbolsCount(int size) {
        this.symbolsCount = size;
    }

    HexPane(MyWorkPane workPane, MyJFrame myJFrame) {
        super();
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
                        int offset=curStr*bytes+(pos-3)/3 - startBuff;

                        if(!isBufferChanged())
                            setBufferChanged(true);
                        byteArr.remove(offset);
                        insertPage((workPane.getjScrollBarV().getValue()-startBuff/bytes)*bytes);
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

                Arrays.fill(byteBuffer,(byte) 0);

                if(size < byteBuffer.length)
                    buffFullness=(int) size;
                else
                    buffFullness=byteBuffer.length;

                raf.read(byteBuffer,0,byteBuffer.length);
                byteArr = IntStream.range(0, byteBuffer.length).mapToObj(i -> byteBuffer[i]).collect(Collectors.toList());



                curPath=path;
//                long size=raf.length();
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
            if(isTempFileExist((int) pos)){
                source=getTempFile((int) pos);
                try {
                    size=Files.size(source);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                source=curPath;
                size=byteBuffer.length/2;
            }
            if(size != 0){
                if(buffFullness+size <= byteBuffer.length){
                    try(RandomAccessFile raf=new RandomAccessFile(source.toString(),"r")){
                        if(source.equals(curPath)){
                            raf.seek(pos);
                            raf.read(byteBuffer,buffFullness,(int) size);
                        }
                        else{
                            raf.read(byteBuffer,buffFullness,(int) size);
                        }

                        if(buffFullness == byteBuffer.length){
                            //buffFullness=0;
                            if(!saving){
                                byteArr = IntStream.range(0, buffFullness).mapToObj(i -> byteBuffer[i]).collect(Collectors.toList());
                                insertPage(workPane.getjScrollBarV().getValue()*bytes - startBuff);
                            }

                        }
                        else{
                            buffFullness+=size;
                            readFile(curPath,pos+byteBuffer.length/2,false,saving);
                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                else{
                    if(!saving){
                        byteArr = IntStream.range(0, buffFullness).mapToObj(i -> byteBuffer[i]).collect(Collectors.toList());
                        insertPage(workPane.getjScrollBarV().getValue()*bytes - startBuff);
                    }
                }

            }
            else{
                readFile(curPath,pos+byteBuffer.length/2,false,saving);
            }
        }
    }


    public void insertPage(int pos){
        StringBuilder sbHex = new StringBuilder();
        StringBuilder sbText = new StringBuilder();
        for(int i=pos;i<pos+bytes*rows;i++) {
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


        if(pos*bytes > startBuff + byteArr.size() || pos*bytes < startBuff){ // pos*bytes - на каком байте сейчас JScrollBar (байт на начало строки)
            if(isBufferChanged()){
                setBufferChanged(false);
                saveBuffer(startBuff);
            }
            page=pos*bytes/(byteBuffer.length/2); // page - номер буффера(половинки), (0-32000) - 0, (32000-64000) - 1 и т.д.
//            System.out.println("Я отработал пушто был какой то резкий переход " + page);
            startBuff=page*byteBuffer.length/2;
            Arrays.fill(byteBuffer,(byte) 0);
            byteArr.clear();
            buffFullness=0;
            readFile(curPath,startBuff, false,false);
        }
        else{
            if(startBuff !=0 && pos<startBuff/bytes + prox){
//                System.out.println("Я на нижней границе! " + pos);
                if(isBufferChanged()){
                    setBufferChanged(false);
                    saveBuffer(startBuff);
                }
                page-=1;
                startBuff-=byteBuffer.length/2;
                Arrays.fill(byteBuffer,(byte) 0);
                byteArr.clear();
                buffFullness=0;
                readFile(curPath, startBuff,false,false);
            }
            else{
                int k = byteArr.size()/bytes;
                if(startBuff+byteArr.size() < getFileSize() && pos>(startBuff/bytes)+k-prox){
                    if(isBufferChanged()){
                        setBufferChanged(false);
                        saveBuffer(startBuff);
                    }

//                    System.out.println("Я на верхней границе! " + pos);

                    page+=1;
                    startBuff+=byteBuffer.length/2;
                    Arrays.fill(byteBuffer,(byte) 0);
                    byteArr.clear();
                    buffFullness=0;
                    readFile(curPath, startBuff,false,false);
                }
                else{
                    insertPage((pos-startBuff/bytes)*bytes);
                }
            }
        }

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

    public void saveBuffer(int offset){
        int dirNumb=startBuff/limit_file_per_dir;

        int size=byteArr.size();
        Path path;

        if(size <= byteBuffer.length){
            if(size <= byteBuffer.length/2){
                if(isTempFileExist(offset)){
                    path=getTempFile(offset);
                }
                else{
                    path=createTempFile(offset);
                }
                writeTempFile(path,size,0);

                if(isTempFileExist(offset+byteBuffer.length/2)){
                    path=getTempFile(offset+byteBuffer.length/2);
                }
                else{
                    path=createTempFile(offset+byteBuffer.length/2);
                }

                writeTempFile(path,0,0);
            }
            else{
                if(isTempFileExist(offset)){
                    path=getTempFile(offset);
                }
                else{
                    path=createTempFile(offset);
                }

                writeTempFile(path,byteBuffer.length/2,0);

                if(isTempFileExist(offset+byteBuffer.length/2)){
                    path=getTempFile(offset+byteBuffer.length/2);
                }
                else{
                    path=createTempFile(offset+byteBuffer.length/2);
                }
                writeTempFile(path,size-byteBuffer.length/2,byteBuffer.length/2);
            }
        }
        else{

        }

    }


    public void writeTempFile(Path path, int size,int pos){
        try(RandomAccessFile raf = new RandomAccessFile(path.toString(), "rw")){
            raf.setLength(size);
            if(size != 0) {
                byte[] newArr = new byte[size];
                for (int i = 0; i < size; i++) newArr[i] = byteArr.get(i+pos);
                raf.write(newArr, 0, size);
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
//        StringBuilder path = new StringBuilder();
//        path.append(curDir.toString());
//        path.append("\\tmp\\");
//        path.append(fileName / limit_file_per_dir);
//        path.append("\\" + fileName + "b.tmp");

        return false;
        //return Files.exists(Paths.get(path.toString()), LinkOption.NOFOLLOW_LINKS);
    }

    public Path getTempFile(int fileName){
        StringBuilder path = new StringBuilder();
        path.append(curDir.toString());
        path.append("\\tmp\\");
        path.append(fileName / limit_file_per_dir);
        path.append("\\" + fileName + "b.tmp");
        return Paths.get(path.toString());
    }


//    public void saveAs(Path path, boolean overwrite){
//
//
//
//        long size = getFileSize();
//        long pos=0;
//        int curStartBuf=startBuff;
//        int bufActSize=0;
//        System.out.println("SIIZE " +size);
//
//
//        if(!overwrite){
//            try {
//                Files.createFile(path);
//            } catch (IOException ex){
//                System.err.println(ex);
//            }
//        }
//
//
//        try(RandomAccessFile raf = new RandomAccessFile(path.toString(), "rw")){
//
//
//            long start_time=System.nanoTime();
//
//            for(int i=0;i<size;i+=byteBuffer.length){
//                pr=i;
//                if(i != curStartBuf){
//                    readFile(curPath,i,false,true);
//                    bufActSize=buffFullness;
//                }
//                else{
//                    for(int k=0;k<buffFullness;k++){
//                        byteBuffer[k]=byteArr.get(k);
//                    }
//                    bufActSize=byteArr.size();
//                }
//                raf.seek(pos);
//                raf.write(byteBuffer,0,bufActSize);
//                pos+=bufActSize;
//                buffFullness=0;
//                byteArr.clear();
//                System.out.println("Записано "+ i/(size/100)+ "%");
//            }
//            raf.setLength(size);
//            long end_time=System.nanoTime();
//            long result_time=(end_time-start_time)/1000000;
//            System.out.println("Запись окончена " + result_time);
//        }catch (FileNotFoundException e) {
//                e.printStackTrace();
//        } catch (IOException e) {
//                e.printStackTrace();
//        }
//        readFile(curPath,curStartBuf,false,false);
//    }



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


    public void test(Path path,boolean overwrite){
        ProgressWorker pw = new ProgressWorker(path,overwrite);
        pw.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                String name = evt.getPropertyName();
                if (name.equals("progress")) {
                    int progress = (int) evt.getNewValue();
                    myJFrame.getMyFooterPane().getjProgressBar().setValue(progress);
                    repaint();
                } else if (name.equals("state")) {
                    SwingWorker.StateValue state = (SwingWorker.StateValue) evt.getNewValue();
                    switch (state) {
                        case DONE:
                            break;
                    }
                }
            }
        });
        pw.execute();
    }

//    public int search(byte[] searchArr,int direction,boolean firstSearch,int pos_start,int startBuff){ // 0 direction down, 1 - direction up
    public int search(byte[] searchArr,int direction,int pos_start){ // 0 direction down, 1 - direction up
        int str=getWorkPane().getjScrollBarV().getValue();
        //int curBuff=startBuff;

        //int pos=str*bytes-curBuff+pos_start;

        System.out.println("STARTBUF " + startBuff);
        int curBuff=(str*bytes/byteBuffer.length) * byteBuffer.length;

        System.out.println("CurBuff " + curBuff);


        if(curBuff != startBuff){
            readFile(curPath,curBuff,false,true);
        }

        int pos=(str*bytes)%byteBuffer.length;
        System.out.println("pos " + pos);




        boolean find;
        int offset=0;
        int offset2=0;
        if(direction == 0){
            offset=byteBuffer.length;
            offset2=1;
            pos=(str*bytes)%byteBuffer.length;
        }
        else{
            offset=-byteBuffer.length;
            offset2=-1;
            pos=((str+rows)*bytes)%byteBuffer.length;
        }


//        if(direction == 0){
            for(int cb=curBuff;cb<getFileSize() && cb>=0;cb+=offset){
                find=true;
                for(int p=pos;p<buffFullness && p>=0;p+=offset2){
                    int p2=p;
                    if(lastIndFound != -1 && ((p <= lastIndFound && direction == 0) || (p>=lastIndFound && direction == 1))) {
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
                            getHighlighter().addHighlight((p%bytes)*3,(p%bytes)*3+2, DefaultHighlighter.DefaultPainter);
                            textPane.getHighlighter().addHighlight((p%bytes),(p%bytes)+1,DefaultHighlighter.DefaultPainter);
                        } catch (BadLocationException e) {
                            e.printStackTrace();
                        }
                        //return p%bytes;
                        return 1;
                    }
                    else{
                        find=true;
                    }
                }
                if((curBuff + buffFullness < getFileSize() && direction==0) || (direction==1 && curBuff>=0)){
                    buffFullness=0;
                    curBuff+=offset;
                    if(curBuff<0)
                        return -1;
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
//        }
//        else{
//
//        }
        return -1;
//        int pos;
//        boolean find=false;
//
//        int curBuff=startBuff;
//        if(firstSearch){
//            int str=getWorkPane().getjScrollBarV().getValue();
//            curBuff=this.startBuff;
//            pos=str*bytes - curBuff + pos_start;
//            System.out.println("StartBuff "+ this.startBuff);
//        }
//        else
//        {
//            pos=pos_start;
//        }
//
//        if(direction == 0){
//            for(int i=pos;i<buffFullness;i++){
//                int m=i;
//                find=true;
//                for(int k=0;k<searchArr.length;k++){
//                    if (m < buffFullness - searchArr.length) {
//                        if (searchArr[k] == byteBuffer[m]) {
//                            m++;
//                            continue;
//                        } else {
//                            find = false;
//                            break;
//                        }
//                    } else {
//                        find = false;
//                    }
//                }
//                if(find){
//                    for(int l=i,s=0;s<searchArr.length;l++,s++){
//                        System.out.println(byteBuffer[l] + " " + searchArr[s]);
//                    }
//                    getWorkPane().getjScrollBarV().setValue(i/bytes + curBuff/bytes);
//                    System.out.println("Нашел на " +i/bytes + curBuff/bytes + " строке");
//                    System.out.println("Это " + i%bytes + " байт");
//                    try {
//                        setCaretPosition((i%bytes)*3);
//                        getHighlighter().addHighlight((i%bytes)*3,(i%bytes)*3+2, DefaultHighlighter.DefaultPainter);
//                        textPane.getHighlighter().addHighlight((i%bytes),(i%bytes)+1,DefaultHighlighter.DefaultPainter);
//                    } catch (BadLocationException e) {
//                        e.printStackTrace();
//                    }
//                    return i%bytes;
//                }
//            }
//            if(curBuff + buffFullness < getFileSize()){
//                buffFullness=0;
//                System.out.println("Я здесь! " + curBuff);
//                readFile(curPath,curBuff+byteBuffer.length/2,false,true);
//                int result=search(searchArr,0,false,0,curBuff+byteBuffer.length/2);
//                return result;
//            }
//            else
//            {
//                System.out.println("Не найдено!");
//                return -1;
//            }
//        }
//        else{
//            for(int i=pos;i>=0;i--){
//                int m=i;
//                find=true;
//                for(int k=0;k<searchArr.length;k++) {
//                    if (m < buffFullness - searchArr.length) {
//                        if (searchArr[k] == byteBuffer[m]) {
//                            m++;
//                            continue;
//                        } else {
//                            find = false;
//                            break;
//                        }
//                    } else {
//                        find = false;
//                    }
//                }
//                if(find){
//                        for(int l=i,s=0;s<searchArr.length;l++,s++){
//                            System.out.println(byteBuffer[l] + " " + searchArr[s]);
//                        }
//                        getWorkPane().getjScrollBarV().setValue(i/bytes + curBuff/bytes);
//                        System.out.println("Нашел на " +i/bytes + curBuff/bytes + " строке");
//                        System.out.println("Это " + i%bytes + " байт");
//                        try {
//                            setCaretPosition((i%bytes)*3);
//                            getHighlighter().addHighlight((i%bytes)*3,(i%bytes)*3+2, DefaultHighlighter.DefaultPainter);
//                            textPane.getHighlighter().addHighlight((i%bytes),(i%bytes)+1,DefaultHighlighter.DefaultPainter);
//                        } catch (BadLocationException e) {
//                            e.printStackTrace();
//                        }
//                        return i%bytes;
//                    }
//            }
//            if(curBuff != 0){
//                buffFullness=0;
//                System.out.println("Я здесь! " + curBuff);
//                readFile(curPath,curBuff-byteBuffer.length/2,false,true);
//                int result=search(searchArr,1,false,buffFullness,curBuff-byteBuffer.length/2);
//                return result;
//            }
//            else{
//                System.out.println("Не найдено!");
//                return -1;
//            }
//        }
    }
}
