package com.tool.client;

import javax.swing.*;
import java.util.Objects;

public class CommonFrame extends JFrame{

    public void initFrame() {
        this.setIconImage(new ImageIcon(Objects.requireNonNull(CommonFrame.class.getResource("k7.png"))).getImage());
        this.setSize(630, 480);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }

}
