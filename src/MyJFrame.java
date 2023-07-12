import javax.swing.*;
import java.awt.*;

public class MyJFrame extends JFrame {

    private MyInfoPane myInfoPane;



    private MyWorkPane myworkPane;
    private MySearchPane mySearchPane;
    private MyFooterPane myFooterPane;


    private MyJMenuBar menu;
    MyJFrame() {
        super();

        setTitle("HexEditor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280,720);
        setLayout(new GridBagLayout());
        setMinimumSize(new Dimension(600,400));
        setLocationRelativeTo(null);

        myInfoPane=new MyInfoPane();
        myFooterPane=new MyFooterPane();
        myworkPane=new MyWorkPane();
//        mySearchPane=new MySearchPane();
        menu=new MyJMenuBar(this);



        myworkPane.getHexArea().setInfoPane(myInfoPane);

//        JScrollPane jScroll = new JScrollPane(myworkPane);

//        jScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);


        setJMenuBar(menu);
        add(myInfoPane,new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.VERTICAL,new Insets(1, 1, 1, 1),0,0));
        add(myworkPane,new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.NORTH,GridBagConstraints.BOTH,new Insets(1, 1, 1, 1),0,0));

//        add(jScroll,new GridBagConstraints(1,0,1,1,0,1,GridBagConstraints.NORTH,GridBagConstraints.BOTH,new Insets(1, 1, 1, 1),0,0));

//        add(myworkPane,new GridBagConstraints(1,0,1,1,0,1,GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL,new Insets(1, 1, 1, 1),0,0));

//        add(mySearchPane,new GridBagConstraints(2,0,1,1,1,1,GridBagConstraints.EAST,GridBagConstraints.BOTH,new Insets(1, 1, 1, 1),0,0));
//        add(myFooterPane,new GridBagConstraints(0,1,3,1,0,0,GridBagConstraints.SOUTH,GridBagConstraints.BOTH,new Insets(1, 1, 1, 1),0,0));

        add(myFooterPane,new GridBagConstraints(0,1,2,1,1,1,GridBagConstraints.SOUTH,GridBagConstraints.HORIZONTAL,new Insets(1, 1, 1, 1),0,0));

        setVisible(true);
    }


    public MyWorkPane getMyworkPane() {
        return myworkPane;
    }

    public void setMyworkPane(MyWorkPane myworkPane) {
        this.myworkPane = myworkPane;
    }
}
