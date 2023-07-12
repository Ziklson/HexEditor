import javax.swing.*;
import java.awt.*;

public class MyFooterPane extends JPanel {
    MyFooterPane() {
        super();
        Font font=new Font(Font.MONOSPACED, Font.PLAIN,20);
        setBackground(new Color(255,100,255));
        JTextArea offsetArea=new JTextArea();
        offsetArea.setEditable(false);
        offsetArea.setText("Offset");
        offsetArea.setFont(font);
        add(offsetArea);


    }
}
