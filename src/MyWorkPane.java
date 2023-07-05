import javax.swing.*;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

public class MyWorkPane extends JPanel {




    private HexPane hexArea;

    private JScrollPane jScrollPane;


    private JPanel hexAreaPane;

    private Font font;

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


    private int rows;

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
        rowHeader.setRows(rows);

    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    private int columns;


    MyWorkPane() {
        super();
        rows=20;
        columns=16;
        setLayout(new GridBagLayout());
        hexArea=new HexPane(this);
        hexAreaPane=new JPanel();
        textPane=new TextPane(hexArea);

        columnHeader=new JTextArea();

        columnHeader.setRows(1);
        columnHeader.setColumns(columns*3-1);
        columnHeader.setEditable(false);

        rowHeader=new JTextArea();

        rowHeader.setRows(rows);
        rowHeader.setColumns(8);

        rowHeader.setWrapStyleWord(true);



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

        WorkPaneCaretListener workPaneCaretListener=new WorkPaneCaretListener(hexArea,textPane);

        hexArea.addCaretListener(workPaneCaretListener);
        textPane.addCaretListener(workPaneCaretListener);




//        hexAreaPane.add(rowHeader,new GridBagConstraints(0,1,1,2,0.001,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));

//        hexAreaPane.add(columnHeader,new GridBagConstraints(1,0,1,1,1,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));

//        hexAreaPane.add(hexArea,new GridBagConstraints(1,1,1,1,1,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));

//        hexAreaPane.add(textPane,new GridBagConstraints(2,1,1,1,1,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));


        hexAreaPane.add(hexArea,new GridBagConstraints(0,0,1,1,0,1,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0, 0, 1, 1),0,0));

        hexAreaPane.add(textPane,new GridBagConstraints(1,0,1,1,1,1,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0, 20, 1, 1),0,0));

        jScrollPane=new JScrollPane(hexAreaPane);

        JViewport jViewport = new JViewport();
        JViewport jViewport2 = new JViewport();

        jViewport.setView(columnHeader);
        jViewport2.setView(rowHeader);
        columnHeader.setBackground(new Color(229,228,226));
        columnHeader.setForeground(new Color(255,0,0));
        rowHeader.setBackground(new Color(229,228,226));
        rowHeader.setForeground(new Color(255,0,0));
        jScrollPane.setColumnHeader(jViewport);
        jScrollPane.setRowHeader(jViewport2);



        jScrollBarV = new JScrollBar(Adjustable.VERTICAL);

        jScrollBarV.setMinimum(0);
        jScrollBarV.setMaximum(200);

        jScrollBarV.setUnitIncrement(1);
        System.out.println(jScrollBarV.getVisibleAmount() + " VISIBLE AMOUNT");
        jScrollBarV.setVisibleAmount(1);
        System.out.println(jScrollBarV.getVisibleAmount() + " VISIBLE AMOUNT NEW");



        jScrollBarV.addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {

                System.out.println(e.getValue());
                int value = e.getValue();

                updateRowHeader(rows,columns,value);
                hexArea.readBuffer(value);


            }
        });







        add(columnHeader,new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL,new Insets(1, 1, 1, 1),0,0));


        add(rowHeader,new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.NORTH,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));

        add(hexAreaPane,new GridBagConstraints(1,1,1,1,1,1,GridBagConstraints.NORTH,GridBagConstraints.BOTH,new Insets(1, 1, 1, 1),0,0));

//        add(jScrollPane,new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.NORTH,GridBagConstraints.BOTH,new Insets(1, 1, 1, 1),0,0));

        add(jScrollBarV,new GridBagConstraints(2,1,1,1,0,0,GridBagConstraints.EAST,GridBagConstraints.VERTICAL,new Insets(1, 1, 1, 1),0,0));





        setBackground(new Color(229,228,226));
    }


    public void createRowHeader(int rows, int columns){
        rowHeader.setText("");
        for(int i=0,diff=0;i<rows;i++,diff+=columns){
            rowHeader.append(Integer.toHexString(diff) + "\n");
        }
    }

    public void updateRowHeader(int rows,int columns, int offset){
        rowHeader.setText("");
        for(int i=offset,diff=offset*columns;i<offset+rows;i++,diff+=columns){
            rowHeader.append(Integer.toHexString(diff) +"\n");
        }
    }

    public void createColumnHeader(int columns) {
        columnHeader.setText("");
        for(int i=0;i<columns-1;i++) {
                columnHeader.append("0" + Integer.toHexString(i) + " ");
        }
        columnHeader.append("0" + Integer.toHexString(columns-1));


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
