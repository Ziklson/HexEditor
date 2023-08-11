import javax.swing.*;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class MyWorkPane extends JPanel {


    private MyJFrame myJFrame;

    private HexPane hexArea;

    private JScrollPane jScrollPane;

    public int getLastFocus() {
        return lastFocus;
    }

    public void setLastFocus(int lastFocus) {
        this.lastFocus = lastFocus;
    }

    private int lastFocus; // if 0 ==> last focus was on HexPane, if 1 ==> on TextPane, if ==> -1 none of them


    private JPanel hexAreaPane;

    private Font font;

    public WorkPaneCaretListener getWorkPaneCaretListener() {
        return workPaneCaretListener;
    }

    private WorkPaneCaretListener workPaneCaretListener;

    private JTextArea columnHeader;

    private JTextArea rowHeader;

    private TextPane textPane;

    public JScrollBar getjScrollBarV() {
        return jScrollBarV;
    }

    public void setjScrollBarV(JScrollBar jScrollBarV) {
        this.jScrollBarV = jScrollBarV;
    }

    private JScrollBar jScrollBarV;


    public JScrollPane getjScroll() {
        return jScroll;
    }

    public void setjScroll(JScrollPane jScroll) {
        this.jScroll = jScroll;
    }

    private JScrollPane jScroll;

    private int rows;

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
        rowHeader.setRows(rows);
        updateRowHeader(rows,columns,getjScrollBarV().getValue());
    }



    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
        columnHeader.setColumns(columns*3-1);
        createColumnHeader(this.columns);
        updateRowHeader(rows,columns,getjScrollBarV().getValue());
    }

    private int columns;


    MyWorkPane(MyJFrame myJFrame) {
        super();
        this.myJFrame=myJFrame;
        setLayout(new GridBagLayout());
        hexArea=new HexPane(this,myJFrame);
        hexAreaPane=new JPanel();
        textPane=new TextPane(hexArea);
        lastFocus=-1;
        rows=hexArea.getRows();
        columns=hexArea.getBytes();

        columnHeader=new JTextArea();

        columnHeader.setRows(1);
        columnHeader.setColumns(columns*3-1);
        columnHeader.setEditable(false);

        rowHeader=new JTextArea();

        rowHeader.setRows(rows);
        rowHeader.setColumns(8);

        //  rowHeader.setWrapStyleWord(true);



        createColumnHeader(columns);
        createRowHeader(rows, columns);
        columnHeader.setEditable(false);
        rowHeader.setEditable(false);

        font=new Font(Font.MONOSPACED, Font.PLAIN,22);

        columnHeader.setFont(font);
        rowHeader.setFont(font);
        hexArea.setFont(font);
        textPane.setFont(font);



        hexAreaPane.setLayout(new GridBagLayout());

        hexAreaPane.setBackground(new Color(229,228,226));

        workPaneCaretListener=new WorkPaneCaretListener(hexArea,textPane);

        hexArea.addCaretListener(workPaneCaretListener);
        textPane.addCaretListener(workPaneCaretListener);





        columnHeader.setBackground(new Color(229,228,226));
        columnHeader.setForeground(new Color(255,0,0));
        rowHeader.setBackground(new Color(229,228,226));
        rowHeader.setForeground(new Color(255,0,0));



        jScrollBarV = new JScrollBar(Adjustable.VERTICAL);

        jScrollBarV.setUnitIncrement(1);
        jScrollBarV.setBlockIncrement(1);






        jScrollBarV.addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {

                //System.out.println("ScVal "+ e.getValue());
                int value = e.getValue();

                updateRowHeader(rows,columns,value);
                hexArea.readBuffer(value);

                if(!hexArea.isSelecting()){
                    hexArea.doSelection();
                }


            }
        });





        JTextArea cl=new JTextArea();
        cl.setColumns(columns);
        cl.setFont(font);
        cl.setEditable(false);

        cl.setBackground(new Color(229,228,226));
        cl.setForeground(new Color(255,0,0));
        for(int i=0;i<columns;i++){
            cl.append("0");
        }


        JPanel jp=new JPanel();
        jp.setLayout(new GridBagLayout());



        jp.add(hexArea,new GridBagConstraints(1,1,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0, 0, 0, 0),0,0));
        jp.add(textPane,new GridBagConstraints(2,1,1,1,1,1,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0, 20, 0, 0),0,0));


        jScroll=new JScrollPane();


        jScroll.setViewportView(jp);
        jScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        JViewport jv1=new JViewport();
        JViewport jv2=new JViewport();
        jv1.setView(rowHeader);
        jv2.setView(columnHeader);
        jScroll.setRowHeader(jv1);
        jScroll.setColumnHeader(jv2);

        jScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScroll.removeMouseWheelListener(jScroll.getMouseWheelListeners()[0]);

        jScroll.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if(e.getWheelRotation() > 0){
                    jScrollBarV.setValue(jScrollBarV.getValue()+1);
                }
                else{
                    jScrollBarV.setValue(jScrollBarV.getValue()-1);
                }
            }
        });



        add(jScroll,new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.NORTH,GridBagConstraints.BOTH,new Insets(1, 1, 1, 1),0,0));

        add(jScrollBarV,new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.NORTH,GridBagConstraints.VERTICAL,new Insets(1, 1, 1, 1),0,0));






        setBackground(new Color(229,228,226));
    }


    public void createRowHeader(int rows, int columns){
        rowHeader.setText("");
        StringBuilder sb=new StringBuilder("00000000");
        for(int i=0,diff=0;i<rows;i++,diff+=columns){
            int len=Integer.toHexString(diff).length();
            sb.replace(8-len,8,Integer.toHexString(diff));

            // rowHeader.append(Integer.toHexString(diff) + "\n");
            if(i != rows-1)
                rowHeader.append(sb.toString() + "\n");
            else
                rowHeader.append(sb.toString());
        }
    }

    public void updateRowHeader(int rows,int columns, int offset){
        //System.out.println("OFFSET " + offset);
        if(offset == 0){
            createRowHeader(rows,columns);
        }
        else{
            rowHeader.setText("");
            StringBuilder sb=new StringBuilder("00000000");
            for(int i=offset,diff=offset*columns;i<offset+rows;i++,diff+=columns){
                int len=Integer.toHexString(diff).length();
                sb.replace(8-len,8,Integer.toHexString(diff));
                if(i != offset+rows-1){
                    rowHeader.append(sb.toString() + "\n");
                }

                else{
                    rowHeader.append(sb.toString());
                }
            }
        }
    }

    public void createColumnHeader(int columns) {
        columnHeader.setText("");
        StringBuilder sb=new StringBuilder();
        StringBuilder sb2=new StringBuilder();
        for(int i=0;i<columns;i++) {
                int len = Integer.toHexString(i).length();
                if(len == 1){
                    columnHeader.append("0"+Integer.toHexString(i));
                }
                else
                    columnHeader.append(Integer.toHexString(i));
                if(i != columns -1){
                    columnHeader.append(" ");
                }
//                columnHeader.append("0" + Integer.toHexString(i) + " ");
        }
        for(int i=0;i<hexArea.getBytes()+10;i++){
            sb2.append(" ");
        }
        columnHeader.append(sb2.toString());
//        columnHeader.append("0" + Integer.toHexString(columns-1));
    }

    public HexPane getHexArea() {
        return hexArea;
    }

    public void setHexArea(HexPane hexArea) {
        this.hexArea = hexArea;
    }


    public void setJScrollBarVSize(int size){
        jScrollBarV.setMaximum(size);
    }


}
