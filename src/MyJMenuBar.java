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
                    myJFrame.getMyworkPane().getHexArea().readFile(path,0,true,false);
                }
            }
        });


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
        file.add(save);
        file.add(saveAs);
        return file;
    }
    private JMenu CreateEditMenu() {
        JMenu edit = new JMenu("Edit");
        JMenuItem cut = new JMenuItem("Cut");
        JMenu paste = new JMenu("Paste");
        JMenuItem find = new JMenuItem("Find");
        edit.add(cut);
        edit.add(paste);
        edit.add(find);

        JMenuItem pasteOver=new JMenuItem("Overwrite the bytes at cursor position");
        JMenuItem pasteIns=new JMenuItem("Insert the bytes at cursor position");

        paste.add(pasteOver);
        paste.add(pasteIns);

        cut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myJFrame.getMyworkPane().getHexArea().cut();
            }
        });



        find.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SearchDialog searchDialog=new SearchDialog(myJFrame);
            }
        });

        pasteOver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myJFrame.getMyworkPane().getHexArea().insertOver(true);
            }
        });
        pasteIns.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myJFrame.getMyworkPane().getHexArea().insertOver(false);
            }
        });

        return edit;
    }

    private JMenu CreateViewMenu(){
        JMenu view=new JMenu("View");
        JMenu rows=new JMenu("Rows");
        JMenu columns=new JMenu("Columns");
        JMenuItem rowsOther=new JMenuItem("Other");
        JMenuItem columnsOther=new JMenuItem("Other");

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
        columnsOther.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane jOptionPane=new JOptionPane();
                String i=jOptionPane.showInputDialog(myJFrame,"Select columns count");
                if(i != null) {
                    try{
                        int columnsCount=Integer.parseInt(i);
                        if(columnsCount <= 0){
                            actionPerformed(e);
                        }
                        else{
                            myJFrame.getMyworkPane().getHexArea().setColumnsCount(columnsCount);
                        }
                    }
                    catch (NumberFormatException nfe) {
                        actionPerformed(e);
                    }
                }
            }
        });




        rows.add(rowsOther);
        columns.add(columnsOther);

        view.add(rows);
        view.add(columns);
        return view;


    }
}
