import javax.swing.*;
import java.awt.*;

public class MyJMenuBar extends JMenuBar {
    MyJMenuBar() {
        super();

        setBackground(new Color(245,255,250));
        add(CreateFileMenu());
        add(CreateEditMenu());
    }
    private JMenu CreateFileMenu() {
        JMenu file = new JMenu("File");
        JMenuItem open = new JMenuItem("Open");
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
