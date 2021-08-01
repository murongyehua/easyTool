package com.tool.client;

import org.springframework.stereotype.Component;

@Component("systemManagerFrame")
public class SystemManagerFrame extends CommonFrame{

    @Override
    public void initFrame() {
        super.initFrame();
        this.setTitle("系统设置");
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
