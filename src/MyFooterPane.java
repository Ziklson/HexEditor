import javax.swing.*;
import java.awt.*;

public class MyFooterPane extends JPanel {
    public JProgressBar getjProgressBar() {
        return jProgressBar;
    }

    private JProgressBar jProgressBar;
    MyFooterPane() {
        super();
        Font font=new Font(Font.MONOSPACED, Font.PLAIN,20);
        setBackground(new Color(226, 226, 224));
        JTextArea offsetArea=new JTextArea();
        offsetArea.setEditable(false);
        offsetArea.setText("Offset");
        offsetArea.setFont(font);
        add(offsetArea);
//        add(jProgressBar);


    }
}
