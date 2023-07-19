import javax.swing.*;
import java.awt.*;

public class ProgressBarDialog {
    private MyJFrame myJFrame;

    public JDialog getProgressDialog() {
        return progressDialog;
    }

    private JDialog progressDialog;

    private JProgressBar jProgressBar;
    ProgressBarDialog(MyJFrame myJFrame) {
        this.myJFrame=myJFrame;
        progressDialog=createDialog("Saving...",false);
        progressDialog.setVisible(true);
    }

    public JDialog createDialog(String str, boolean mode){
        progressDialog=new JDialog(myJFrame,str,mode);
        progressDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        progressDialog.setMinimumSize(new Dimension(300,100));
        progressDialog.setLocationRelativeTo(null);
        progressDialog.setResizable(false);
        jProgressBar=new JProgressBar();
        jProgressBar.setVisible(true);
        jProgressBar.setMinimum(0);
        jProgressBar.setMaximum(100);

        progressDialog.add(jProgressBar);

        return progressDialog;
    }

    public JProgressBar getjProgressBar(){
        return jProgressBar;
    }



}
