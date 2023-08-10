import javax.swing.*;
import java.awt.*;
import java.nio.ByteBuffer;

public class MyInfoPane extends JPanel {

    private JLabel fileInfoLabel;

    private JLabel fileNameLabel;

    private JLabel fileNameValueLabel;



    private JLabel fileSizeLabel;

    private JLabel fileSizeValueLabel;

    private JLabel byteInfoLabel;

    private JLabel byteTypeLabel;

    private JLabel uINT8Label;

    private JLabel INT8Label;

    private JLabel uINT8ValueLabel;

    private JLabel INT8ValueLabel;

    private JLabel byteTypeValueLabel;

    private JLabel blockLabel;



    private JLabel uINT16ValueBlockLabel; // для блока из 2ух байт
    private JLabel INT16ValueBlockLabel;  // для блока из 2ух байт

    private JLabel uINT32ValueBlockLabel; // для блока из 4ех байт

    private JLabel INT32ValueBlockLabel; // для блока из 4ех байт

    private JLabel floatValueBlockLabel; // для блока из 4ех байт
    private JLabel doubleValueBlockLabel; // для блока из 8ми байт

    private JLabel uINT16BlockLabel;
    private JLabel INT16BlockLabel;
    private JLabel uINT32BlockLabel;
    private JLabel INT32BlockLabel;

    private JLabel floatBlockLabel;
    private JLabel doubleBlockLabel;



    private Font font;

    MyInfoPane() {
        super();
        font=new Font(Font.SANS_SERIF, Font.PLAIN,16);

        blockLabel=new JLabel("Block info");
        uINT16BlockLabel=new JLabel("uINT16"); // Big-Endian
        INT16BlockLabel=new JLabel("INT16");
        uINT32BlockLabel=new JLabel("uINT32"); // Big-Endian
        INT32BlockLabel=new JLabel("INT32");

        floatBlockLabel=new JLabel("Float");
        doubleBlockLabel=new JLabel("Double");

        uINT16ValueBlockLabel=new JLabel("0"); // Big-Endian
        INT16ValueBlockLabel=new JLabel("0");
        uINT32ValueBlockLabel=new JLabel("0"); // Big-Endian
        INT32ValueBlockLabel=new JLabel("0");
        floatValueBlockLabel=new JLabel("0");
        doubleValueBlockLabel=new JLabel("0");





        fileInfoLabel=new JLabel("File information");
        fileNameLabel=new JLabel("File Name: ");
        fileSizeLabel=new JLabel("File Size: ");
        byteInfoLabel=new JLabel("Byte information");
        fileSizeValueLabel=new JLabel("0 bytes");
        fileNameValueLabel=new JLabel("-Untitled-");
        fileNameValueLabel.setSize(20,20);
        byteTypeLabel=new JLabel("Type");
        uINT8Label=new JLabel("Unsigned (+)");
        INT8Label=new JLabel("Signed(±)          "); // 5 пробелов, шо б ширина инфопанели всегда была статична и не изменялась из за double value
        byteTypeValueLabel=new JLabel("INT8");

        uINT8ValueLabel=new JLabel("0");
        INT8ValueLabel=new JLabel("0");


        blockLabel.setFont(font);

        uINT16BlockLabel.setFont(font);
        INT16BlockLabel.setFont(font);
        uINT32BlockLabel.setFont(font);
        INT32BlockLabel.setFont(font);
        floatBlockLabel.setFont(font);
        doubleBlockLabel.setFont(font);

        uINT16ValueBlockLabel.setFont(font);
        INT16ValueBlockLabel.setFont(font);

        uINT32ValueBlockLabel.setFont(font);
        INT32ValueBlockLabel.setFont(font);


        floatValueBlockLabel.setFont(font);
        doubleValueBlockLabel.setFont(font);


        fileInfoLabel.setFont(font);
        fileNameLabel.setFont(font);
        fileSizeLabel.setFont(font);
        byteInfoLabel.setFont(font);

        fileNameValueLabel.setFont(font);
        fileSizeValueLabel.setFont(font);

        byteTypeLabel.setFont(font);
        uINT8Label.setFont(font);
        INT8Label.setFont(font);
        byteTypeValueLabel.setFont(font);
        uINT8ValueLabel.setFont(font);
        INT8ValueLabel.setFont(font);



        setLayout(new GridBagLayout());


        add(fileInfoLabel,new GridBagConstraints(0,0,3,1,1,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));
        add(fileNameLabel,new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));
        add(fileNameValueLabel,new GridBagConstraints(1,1,2,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));

        add(fileSizeLabel,new GridBagConstraints(0,2,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));
        add(fileSizeValueLabel,new GridBagConstraints(1,2,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));

        add(byteInfoLabel,new GridBagConstraints(0,3,3,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));

        add(byteTypeLabel,new GridBagConstraints(0,4,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));

        add(uINT8Label,new GridBagConstraints(1,4,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));

        add(INT8Label,new GridBagConstraints(2,4,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));

        add(byteTypeValueLabel,new GridBagConstraints(0,5,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));

        add(uINT8ValueLabel,new GridBagConstraints(1,5,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));
        add(INT8ValueLabel,new GridBagConstraints(2,5,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));

//        add(blockLabel,new GridBagConstraints(0,6,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));




//        add(uINT16BlockLabel,new GridBagConstraints(0,7,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));

        add(INT16BlockLabel,new GridBagConstraints(0,6,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));

//        add(uINT32BlockLabel,new GridBagConstraints(0,8,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));

        add(INT32BlockLabel,new GridBagConstraints(0,7,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));



        add(floatBlockLabel,new GridBagConstraints(0,8,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));

        add(doubleBlockLabel,new GridBagConstraints(0,9,1,1,0,1,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));

        add(uINT16ValueBlockLabel,new GridBagConstraints(1,6,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));

        add(INT16ValueBlockLabel,new GridBagConstraints(2,6,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));

        add(uINT32ValueBlockLabel,new GridBagConstraints(1,7,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));

        add(INT32ValueBlockLabel,new GridBagConstraints(2,7,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));



        add(floatValueBlockLabel,new GridBagConstraints(1,8,2,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));

        add(doubleValueBlockLabel,new GridBagConstraints(1,9,2,1,0,1,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(1, 1, 1, 1),0,0));


        setBackground(new Color(229,228,226));
    }



    public void setFileSizeValueLabel(String size) {
        this.fileSizeValueLabel.setText(size + " bytes");
    }

    public void setFileNameValueLabel(String name){
        StringBuilder sb=new StringBuilder(name);
        if(name.length() > 15){
            sb.setLength(10);
            sb.append("...");
        }
        this.fileNameValueLabel.setText(sb.toString());
    }


    public void setByteValueLabel(byte b){
        uINT8ValueLabel.setText(Integer.toString(Byte.toUnsignedInt(b)));
        INT8ValueLabel.setText(Byte.toString(b));

    }

    public void setBlockValueLabels(byte[] arr){
        byte[] barr=new byte[8];

        for(int i= arr.length-1,j=barr.length-1;i>=0;i--,j--){
            barr[j]=arr[i];
        }
//        System.out.println("byteLength " +arr.length);
//        StringBuilder sb=new StringBuilder();
//        for(byte b: barr){
//            sb.append(b);
//            sb.append(" ");
//        }
//        sb.deleteCharAt(sb.length()-1);
//        blockHexValueLabel.setText(sb.toString());
        ByteBuffer buffer = ByteBuffer.wrap(barr);


        short a=buffer.getShort();
        uINT16ValueBlockLabel.setText(Integer.toString(Short.toUnsignedInt(a)));
        INT16ValueBlockLabel.setText(Short.toString(a));
        buffer=ByteBuffer.wrap(barr);
        int b=buffer.getInt();
        uINT32ValueBlockLabel.setText(Long.toString(Integer.toUnsignedLong(b)));
        INT32ValueBlockLabel.setText(Integer.toString(b));
        buffer=ByteBuffer.wrap(barr);
        floatValueBlockLabel.setText(Float.toString(buffer.getFloat()));
        buffer=ByteBuffer.wrap(barr);
        doubleValueBlockLabel.setText(Double.toString(buffer.getDouble()));



    }
}
