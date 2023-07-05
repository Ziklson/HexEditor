import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;

public class MyJMenuBar extends JMenuBar {

    private MyJFrame myJFrame;


    MyJMenuBar() {
        super();

        setBackground(new Color(245,255,250));
        add(CreateFileMenu());
        add(CreateEditMenu());
    }

    MyJMenuBar(MyJFrame myJFrame){
        super();
        setBackground(new Color(245,255,250));
        add(CreateFileMenu());
        add(CreateEditMenu());
        this.myJFrame=myJFrame;
    }
    private JMenu CreateFileMenu() {
        JMenu file = new JMenu("File");
        JMenuItem open = new JMenuItem("Open");
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyFileChooser myFileChooser =new MyFileChooser();
                int i =myFileChooser.showOpenDialog(myJFrame);
                if(i == 0){
                    Path path = myFileChooser.getSelectedFile().toPath();
                    myJFrame.getMyworkPane().getHexArea().readFile(path,0,true);
                }
            }
        });


        JMenuItem create = new JMenuItem("Create");
        JMenuItem exit = new JMenuItem("Exit");
        JMenuItem save = new JMenuItem("Save");
        JMenuItem saveAs=new JMenuItem("Save as...");
        file.add(open);
        file.add(create);
        file.add(save);
        file.add(saveAs);
        file.add(exit);
        return file;
    }
    private JMenu CreateEditMenu() {
        JMenu edit = new JMenu("Edit");
        JMenuItem cut = new JMenuItem("Cut");
        JMenuItem copy = new JMenuItem("Copy");
        JMenuItem paste = new JMenuItem("Paste");
        edit.add(copy);
        edit.add(cut);
        edit.add(paste);
        return edit;
    }
}
