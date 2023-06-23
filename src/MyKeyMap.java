import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MyKeyMap {
    private int keys[];
    private InputMap inputMap;
    private ActionMap actionMap;

    MyKeyMap() {

        inputMap=new InputMap();
        actionMap=new ActionMap();



        keys=new int[16];
        int index = 0;
        for (int c = KeyEvent.VK_0; c <= KeyEvent.VK_9; ++c, ++index) keys[index] = c;
        for (int c = KeyEvent.VK_A; c <= KeyEvent.VK_F; ++c, ++index) keys[index] = c;

    }

    public class MyInsertAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

}
