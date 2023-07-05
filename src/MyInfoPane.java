import javax.swing.*;
import java.awt.*;

public class MyInfoPane extends JPanel {

    private JLabel fileInfoLabel;

    private JLabel fileNameLabel;

    private JLabel fileNameValueLabel;



    private JLabel fileSizeLabel;

    private JLabel fileSizeValueLabel;

    private JLabel byteInfoLabel;

    private JLabel byteTypeLabel;

    private JLabel unsignedLabel;

    private JLabel signedLabel;

    private JLabel byteTypeUnsignedValueLabel;

    private JLabel byteTypeSignedValueLabel;

    private JLabel byteTypeValueLabel;


    private Font font;

    MyInfoPane() {
        super();
        font=new Font(Font.SANS_SERIF, Font.PLAIN,16);

        fileInfoLabel=new JLabel("File information");
        fileNameLabel=new JLabel("File Name: ");
        fileSizeLabel=new JLabel("File Size: ");
        byteInfoLabel=new JLabel("Byte information");
        fileSizeValueLabel=new JLabel("0 bytes");
        fileNameValueLabel=new JLabel("-Untitled-");

        byteTypeLabel=new JLabel("Type");
        unsignedLabel=new JLabel("Unsigned (+)");
        signedLabel=new JLabel("Signed(±)");
        byteTypeValueLabel=new JLabel("Integer");

        byteTypeUnsignedValueLabel=new JLabel("0");
        byteTypeSignedValueLabel=new JLabel("0");



        fileInfoLabel.setFont(font);
        fileNameLabel.setFont(font);
        fileSizeLabel.setFont(font);
        byteInfoLabel.setFont(font);

        fileNameValueLabel.setFont(font);
        fileSizeValueLabel.setFont(font);

        byteTypeLabel.setFont(font);
        unsignedLabel.setFont(font);
        signedLabel.setFont(font);
        byteTypeValueLabel.setFont(font);
        byteTypeUnsignedValueLabel.setFont(font);
        byteTypeSignedValueLabel.setFont(font);



        setLayout(new GridBagLayout());


        add(fileInfoLabel,new GridBagConstraints(0,0,3,1,1,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));
        add(fileNameLabel,new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));
        add(fileNameValueLabel,new GridBagConstraints(1,1,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));

        add(fileSizeLabel,new GridBagConstraints(0,2,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));
        add(fileSizeValueLabel,new GridBagConstraints(1,2,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));

        add(byteInfoLabel,new GridBagConstraints(0,3,3,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));

        add(byteTypeLabel,new GridBagConstraints(0,4,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));

        add(unsignedLabel,new GridBagConstraints(1,4,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));

        add(signedLabel,new GridBagConstraints(2,4,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));

        add(byteTypeValueLabel,new GridBagConstraints(0,5,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));

        add(byteTypeUnsignedValueLabel,new GridBagConstraints(1,5,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));
        add(byteTypeSignedValueLabel,new GridBagConstraints(2,5,1,1,0,1,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));



        setBackground(new Color(229,228,226));
    }



    public void setFileSizeValueLabel(String size) {
        this.fileSizeValueLabel.setText(size + " bytes");
    }

    public void setFileNameValueLabel(String name){this.fileNameValueLabel.setText(name);}


    public void setByteValueLabel(String byteValue){
        if(byteValue.equals("undefined")){
            this.byteTypeUnsignedValueLabel.setText(byteValue);
            this.byteTypeSignedValueLabel.setText(byteValue);
        }
        else{
            int unsigned = Integer.parseInt(byteValue,16);
            int signed=unsigned;
            if(unsigned>127)
                signed=unsigned-256;
            this.byteTypeUnsignedValueLabel.setText(Integer.toString(unsigned));
            this.byteTypeSignedValueLabel.setText(Integer.toString(signed));
        }
//        else{
//            char c=byteValue.charAt(0);
//            int unsigned=(int) c;
//            int signed=unsigned;
//            if(unsigned>127)
//                signed=unsigned-256;
//            this.byteTypeUnsignedValueLabel.setText(Integer.toString(unsigned));
//            this.byteTypeSignedValueLabel.setText(Integer.toString(signed));
//        }
        //this.setByteValueLabel(byteValue);



    }
}
