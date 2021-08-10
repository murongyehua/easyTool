package com.tool.client;

import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

@Component
public class CommonFrame extends JFrame{

    public void initFrame() {
        this.setIconImage(new ImageIcon(Objects.requireNonNull(CommonFrame.class.getResource("k7.png"))).getImage());
        this.setSize(630, 480);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.setResizable(false);
    }

    public void setTray(String text, Image image) {
        //判断当前平台是否支持托盘功能
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            PopupMenu popMenu = new PopupMenu();
            MenuItem itmOpen = new MenuItem("open");
            itmOpen.addActionListener(e -> this.Show());
            MenuItem itmExit = new MenuItem("exit");
            itmExit.addActionListener(e -> this.Exit());
            popMenu.add(itmOpen);
            popMenu.add(itmExit);
            TrayIcon trayIcon = new TrayIcon(image, text, popMenu);
            trayIcon.setImageAutoSize(true);
            try {
                tray.add(trayIcon);
            } catch (AWTException e1) {
                e1.printStackTrace();
            }

        }

    }

    public void Show() {
        throw new RuntimeException("子类实现");
    }

    public void Exit() {
        throw new RuntimeException("子类实现");
    }

}
