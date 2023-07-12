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
        add(CreateViewMenu());
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


        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Path path=myJFrame.getMyworkPane().getHexArea().getCurPath();
                myJFrame.getMyworkPane().getHexArea().saveAs(path,true);
            }
        });

        saveAs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyFileChooser myFileChooser=new MyFileChooser();
                int i = myFileChooser.showSaveDialog(myJFrame);
                if(i == JFileChooser.APPROVE_OPTION){
                    Path path = myFileChooser.getSelectedFile().toPath();

                    if(path.toFile().exists()){
                        int result = JOptionPane.showConfirmDialog(myJFrame,"Такой файл уже существует! Вы хотите его перезаписать?","Save",JOptionPane.YES_NO_OPTION);
                        if(result == 0)
                            myJFrame.getMyworkPane().getHexArea().saveAs(path,true);
                        else
                            actionPerformed(e);
                    }
                    else
                        myJFrame.getMyworkPane().getHexArea().saveAs(path,false);
                }
            }
        });


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

    private JMenu CreateViewMenu(){
        JMenu view=new JMenu("View");
        JMenu rows=new JMenu("Rows");
        JMenu columns=new JMenu("Columns");
        JMenuItem rowsOther=new JMenuItem("Other");

        rowsOther.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane jOptionPane=new JOptionPane();
                String i=jOptionPane.showInputDialog(myJFrame,"Select rows count (it's count of visible rows on the screen)");
                if(i != null) {
                    try{
                        int rowsCount=Integer.parseInt(i);
                        if(rowsCount <= 0){
                            actionPerformed(e);
                        }
                        else{
                            myJFrame.getMyworkPane().getHexArea().setVisibleRows(rowsCount);
                        }
                    }
                    catch (NumberFormatException nfe) {
                        actionPerformed(e);
                    }
                }
            }
        });


        rows.add(rowsOther);

        view.add(rows);
        view.add(columns);
        return view;


    }
}
