import javax.swing.*;
import java.awt.*;

public class MyFooterPane extends JPanel {
    public JProgressBar getjProgressBar() {
        return jProgressBar;
    }

    private JProgressBar jProgressBar;
    MyFooterPane() {
        super();
        jProgressBar=new JProgressBar();
        jProgressBar.setVisible(true);
        jProgressBar.setMinimum(0);
        jProgressBar.setMaximum(100);
        jProgressBar.setStringPainted(true);
        Font font=new Font(Font.MONOSPACED, Font.PLAIN,20);
        setBackground(new Color(255,100,255));
        JTextArea offsetArea=new JTextArea();
        offsetArea.setEditable(false);
        offsetArea.setText("Offset");
        offsetArea.setFont(font);
        add(offsetArea);
        add(jProgressBar);


    }
}
