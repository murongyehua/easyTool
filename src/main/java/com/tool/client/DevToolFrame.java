package com.tool.client;

import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component("devToolFrame")
public class DevToolFrame extends CommonFrame{

    @Override
    public void initFrame() {
        super.initFrame();
        this.setTitle("开发工具");
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setOpacity(1f);
//        JPanel mainPane = new JPanel();
//        mainPane.setSize(this.getWidth(), this.getHeight());
//        mainPane.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JTabbedPane tabPane = new JTabbedPane();
        tabPane.add("JSON", this.jsonPanel());
        tabPane.add("UUID", this.uuidPanel());
        tabPane.add("COLOR", this.colorPickPanel());
//        mainPane.add(tabPane);
        Container container = this.getContentPane();
        container.add(tabPane);


        this.setUndecorated(true);
        this.setBackground(new Color(0,0,1));
        this.setVisible(true);
    }

    private JPanel jsonPanel() {
        JPanel jsonPanel = new JPanel();
        jsonPanel.setBackground(Color.YELLOW);
        jsonPanel.add(new JLabel("敬请期待.."));
        return jsonPanel;
    }

    private JPanel uuidPanel() {
        JPanel uuidPanel = new JPanel();
        uuidPanel.setBackground(Color.RED);
        uuidPanel.add(new JLabel("敬请期待.."));
        return uuidPanel;
    }

    private JPanel colorPickPanel() {
        JPanel colorPickPanel = new JPanel();
        colorPickPanel.setBackground(Color.GREEN);
        colorPickPanel.add(new JLabel("敬请期待.."));
        return colorPickPanel;
    }
}
