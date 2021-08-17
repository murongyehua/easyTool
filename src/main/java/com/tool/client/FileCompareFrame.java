package com.tool.client;

import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component("fileCompareFrame")
public class FileCompareFrame extends CommonFrame{

    @Override
    public void initFrame() {
        super.initFrame();

        this.setTitle("文件比对");
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.green);
        mainPanel.add(new JLabel("敬请期待..."));
        this.getContentPane().add(mainPanel);

        this.setVisible(true);
    }
}
