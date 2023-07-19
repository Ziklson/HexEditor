import javax.swing.*;
import java.awt.*;

public class SearchDialog {
    private JDialog searchDialog;
    private MyJFrame myJFrame;
    SearchDialog(MyJFrame myJFrame) {
        this.myJFrame=myJFrame;
        searchDialog=createDialog("Find",true);
        searchDialog.setVisible(true);
    }
    public JDialog createDialog(String str, boolean mode){
            searchDialog=new JDialog(myJFrame,str,mode);
            searchDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            searchDialog.setMinimumSize(new Dimension(270,225));
            searchDialog.setLocationRelativeTo(null);
            searchDialog.setResizable(false);
            MySearchPane mySearchPane=new MySearchPane(myJFrame.getMyworkPane().getHexArea());
            searchDialog.add(mySearchPane);

            return searchDialog;
    }
}
