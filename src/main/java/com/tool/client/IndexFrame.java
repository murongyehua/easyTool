package com.tool.client;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class IndexFrame extends CommonFrame {


    @Override
    public void initFrame() {
        super.initFrame();
        this.setTitle("Easy-Tool");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(2,2));
        mainPanel.setSize(this.getWidth(), this.getHeight());
        mainPanel.add(BorderLayout.CENTER, this.getMainPanel());
        mainPanel.add(BorderLayout.NORTH, this.getTopPanel());
        mainPanel.add(BorderLayout.SOUTH, this.getFootPanel());
        container.add(mainPanel);
        this.setVisible(true);
    }

    /**
     * 顶部
     * @return
     */
    private JPanel getTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.CYAN);
        topPanel.setSize(this.getWidth()-10, 40);
        JLabel label = new JLabel("welcome for use Easy-Tool!");
        label.setSize(this.getWidth(), 50);
        topPanel.add(label);
        return topPanel;
    }

    /**
     * 主体
     * @return
     */
    private JPanel getMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.CYAN);
        mainPanel.setSize(this.getWidth()-10, 400);
        JPanel menuName = new JPanel();
        menuName.setLayout(new FlowLayout(FlowLayout.LEFT, 0,0));
        menuName.setSize(this.getWidth()-10, 25);
        JLabel nameContent = new JLabel("已开启的功能列表");
        nameContent.setFont(new Font("微软雅黑", Font.BOLD, 12));
        menuName.add(nameContent);
        JPanel menu = new JPanel();
        menu.setSize(this.getWidth()-10, 375);
        menu.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        for (int i=0; i<20; i++) {
            MenuButton setting = new MenuButton("功能设置", new ImageIcon(Objects.requireNonNull(IndexFrame.class.getResource("test.png"))));
            menu.add(setting);
        }
        mainPanel.add(BorderLayout.NORTH, menuName);
        mainPanel.add(BorderLayout.CENTER, menu);
        return mainPanel;
    }

    /**
     * 底部
     * @return
     */
    private JPanel getFootPanel() {
        JPanel footPanel = new JPanel();
        footPanel.setBackground(Color.CYAN);
        footPanel.setSize(this.getWidth()-10, 25);
        JLabel copyRight = new JLabel("@author murongyehua");
        copyRight.setHorizontalAlignment(SwingConstants.CENTER);
        footPanel.add(copyRight);
        return footPanel;
    }

}
