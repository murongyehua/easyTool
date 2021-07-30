package com.tool.client;

import javax.swing.*;

public class MenuButton extends JButton {

    public MenuButton(String name, Icon icon) {
        super(name, icon);
        this.setHorizontalTextPosition(JButton.CENTER);
        this.setVerticalTextPosition(JButton.BOTTOM);
    }

}
