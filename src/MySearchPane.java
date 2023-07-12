import javax.swing.*;
import java.awt.*;

public class MySearchPane extends JPanel {
    private JPanel searchPanel;
    private JTextArea searchArea;

    private JScrollPane searchAreaPane;
    private JButton upButton;
    private JButton downButton;
    private JLabel searchLabel;
    private Font font;


    private JPanel blockPanel;
    private JLabel blockLabel;
    private JTextArea blockArea;

    MySearchPane() {
        super();
        font=new Font(Font.MONOSPACED, Font.PLAIN,22);

        searchPanel=new JPanel();
        searchArea=new JTextArea();
        searchArea.setColumns(23);
        searchArea.setRows(4);
        searchArea.setLineWrap(true);
        searchArea.setWrapStyleWord(true);
        searchAreaPane=new JScrollPane(searchArea);
        searchAreaPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        searchLabel=new JLabel("Search");
        searchLabel.setFont(font);
        searchArea.setFont(font);
        upButton=new JButton("UP");
        downButton=new JButton("DOWN");

        searchPanel.setLayout(new GridBagLayout());

        searchPanel.add(searchLabel,new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTH,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));

        searchPanel.add(searchAreaPane,new GridBagConstraints(0,1,3,1,0,0,GridBagConstraints.NORTH,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));

        searchPanel.add(upButton,new GridBagConstraints(1,2,1,1,0,0,GridBagConstraints.NORTH,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));

        searchPanel.add(downButton,new GridBagConstraints(2,2,1,1,0,0,GridBagConstraints.NORTH,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));

        searchPanel.setBackground(new Color(255, 229, 229));
        setLayout(new GridBagLayout());

        add(searchPanel);
        add(searchPanel,new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));


//        setBackground(new Color(0,0,255));

    }
}
