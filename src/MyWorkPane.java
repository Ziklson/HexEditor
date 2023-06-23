import javax.swing.*;
import java.awt.*;

public class MyWorkPane extends JPanel {


    private HexPane hexArea;

    private JScrollPane jScrollPane;


    private JPanel hexAreaPane;

    private Font font;

    private JTextArea columnHeader;

    private JTextArea rowHeader;

    private TextPane textPane;



    MyWorkPane() {
        super();

        setLayout(new GridBagLayout());
        hexArea=new HexPane();
        hexAreaPane=new JPanel();
        textPane=new TextPane(hexArea);

        columnHeader=new JTextArea();

        columnHeader.setRows(1);
        columnHeader.setColumns(47);
        columnHeader.setEditable(false);

        rowHeader=new JTextArea();

        rowHeader.setRows(64);
        rowHeader.setColumns(3);

        rowHeader.setWrapStyleWord(true);



        createColumnHeader(16);
        createRowHeader(64, 16);
        columnHeader.setEditable(false);
        rowHeader.setEditable(false);

        font=new Font(Font.MONOSPACED, Font.PLAIN,16);

        columnHeader.setFont(font);
        rowHeader.setFont(font);
        hexArea.setFont(font);
        textPane.setFont(font);



        hexAreaPane.setLayout(new GridBagLayout());

        hexAreaPane.setBackground(new Color(229,228,226));

        WorkPaneCaretListener workPaneCaretListener=new WorkPaneCaretListener(hexArea,textPane);

        hexArea.addCaretListener(workPaneCaretListener);
        textPane.addCaretListener(workPaneCaretListener);


        JPanel testPane = new JPanel();

        testPane.setBackground(new Color(115,23,21));


        hexAreaPane.add(rowHeader,new GridBagConstraints(0,1,1,2,0.001,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));

        hexAreaPane.add(columnHeader,new GridBagConstraints(1,0,1,1,1,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));

        hexAreaPane.add(hexArea,new GridBagConstraints(1,1,1,1,1,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));

        hexAreaPane.add(textPane,new GridBagConstraints(2,1,1,1,1,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));


        jScrollPane=new JScrollPane(hexAreaPane);


        add(jScrollPane,new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.NORTH,GridBagConstraints.BOTH,new Insets(1, 1, 1, 1),0,0));

        setBackground(new Color(255,0,0));
    }


    public void createRowHeader(int rows, int columns){
        rowHeader.setText("");
        for(int i=0,diff=0;i<rows;i++,diff+=columns){
            rowHeader.append(Integer.toHexString(i) + "\n");
        }
    }

    public void createColumnHeader(int columns) {
        columnHeader.setText("");
        for(int i=0;i<columns-1;i++) {
                columnHeader.append("0" + Integer.toHexString(i) + " ");
        }
        columnHeader.append("0" + Integer.toHexString(columns-1));


    }


}
