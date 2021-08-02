package com.tool.client;

import com.tool.service.IService;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component("rubbishClearFrame")
public class RubbishClearFrame extends CommonFrame {

    @Autowired
    @Qualifier("rubbishClearServiceImpl")
    private IService rubbishClearServiceImpl;

    @Override
    public void initFrame() {
        super.initFrame();
        this.setTitle("垃圾清理");
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        Container container = this.getContentPane();
        JPanel mainPanel = new JPanel();
        mainPanel.setSize(this.getWidth(), this.getHeight());
        JButton baseClearBtn = new JButton("基础清理");
        baseClearBtn.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));
        JButton diyClearBtn = new JButton("个性化清理");
        diyClearBtn.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));
        diyClearBtn.addActionListener(e -> rubbishClearServiceImpl.apply());
        JButton tencentClearBtn = new JButton("腾讯专清");
        tencentClearBtn.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));
        JLabel tipLabel = new JLabel("");

        mainPanel.add(baseClearBtn);
        mainPanel.add(diyClearBtn);
        mainPanel.add(tencentClearBtn);
        container.add(mainPanel);
        this.setVisible(true);
    }
}
