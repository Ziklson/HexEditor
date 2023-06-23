import javax.swing.*;
import java.awt.*;

public class MyJFrame extends JFrame {
    private MyJMenuBar menu= new MyJMenuBar();
    MyJFrame() {
        super();
        setTitle("HexEditor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(1280,720);
        setLayout(new GridBagLayout());
        setMinimumSize(new Dimension(600,400));
        setLocationRelativeTo(null);
        setJMenuBar(menu);
        add(new MyInfoPane(),new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(1, 1, 1, 1),0,0));
        add(new MyWorkPane(),new GridBagConstraints(1,0,1,1,5,20,GridBagConstraints.NORTH,GridBagConstraints.BOTH,new Insets(1, 1, 1, 1),0,0));

        add(new MySearchPane(),new GridBagConstraints(2,0,1,1,1,1,GridBagConstraints.EAST,GridBagConstraints.BOTH,new Insets(1, 1, 1, 1),0,0));
        add(new MyFooterPane(),new GridBagConstraints(0,1,3,1,1,1,GridBagConstraints.SOUTH,GridBagConstraints.BOTH,new Insets(1, 1, 1, 1),0,0));

        setVisible(true);
    }
}
