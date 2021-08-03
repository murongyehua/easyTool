package com.tool.client;

import cn.hutool.core.util.StrUtil;
import com.tool.service.RubbishClearService;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component("rubbishClearFrame")
public class RubbishClearFrame extends CommonFrame {

    @Autowired
    private RubbishClearService rubbishClearServiceImpl;

    @Override
    public void initFrame() {
        super.initFrame();
        this.setTitle("垃圾清理");
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        Container container = this.getContentPane();
        JPanel mainPanel = new JPanel();
        mainPanel.setSize(this.getWidth(), this.getHeight());
        mainPanel.setLayout(new BorderLayout());
        JTextArea tipLabelText = new JTextArea(this.getText());
        tipLabelText.setEditable(false);
        JPanel tipPanel = new JPanel();
        tipPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        JPanel topBtnPanel = new JPanel();
        JButton baseClearBtn = new JButton("基础清理");
        baseClearBtn.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));
        baseClearBtn.addActionListener(e -> {
            rubbishClearServiceImpl.baseClear();
            JOptionPane.showMessageDialog(null, "基础清理完成", "提示", JOptionPane.INFORMATION_MESSAGE);
            tipLabelText.setText(this.getText());
            tipPanel.repaint();
        });
        JButton diyClearBtn = new JButton("个性化清理");
        diyClearBtn.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));
        diyClearBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "正在触发个性化清理...请留意任务栏弹出的新窗口", "提示", JOptionPane.INFORMATION_MESSAGE);
            rubbishClearServiceImpl.diyClear();
        });
        JButton tencentClearBtn = new JButton("腾讯专清");
        tencentClearBtn.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));
        tencentClearBtn.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(null, "此操作会删除微信与QQ的所有账号数据(恢复到刚安装的状态)，确定继续吗？", "提示", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_NO_OPTION) {
                rubbishClearServiceImpl.clearForTencent();
                JOptionPane.showMessageDialog(null, "腾讯专清完成", "提示", JOptionPane.INFORMATION_MESSAGE);
                tipLabelText.setText(this.getText());
                tipPanel.repaint();
            }
        });
        JLabel tipLabel = new JLabel("使用说明");
        tipLabel.setForeground(Color.ORANGE);
        topBtnPanel.add(baseClearBtn);
        topBtnPanel.add(diyClearBtn);
        topBtnPanel.add(tencentClearBtn);
        tipPanel.add(tipLabel);
        tipPanel.add(tipLabelText);
        mainPanel.add(BorderLayout.NORTH, topBtnPanel);
        mainPanel.add(BorderLayout.CENTER, tipPanel);
        container.add(mainPanel);
        this.setVisible(true);
    }

    private String getText() {
        return String.format("建议基础清理、个性化清理、腾讯专清按需配合使用，三者清理的内容不同且无重复" + StrUtil.CRLF + StrUtil.CRLF
                        + "基础清理：仅清理电脑使用时产生的临时文件" + StrUtil.CRLF + StrUtil.CRLF
                        + "个性化清理：调用系统自带的垃圾清理功能" + StrUtil.CRLF + StrUtil.CRLF
                        + "腾讯专清：清理微信和QQ的账号数据，包括通过微信或QQ下载的文件，谨慎使用" + StrUtil.CRLF + StrUtil.CRLF
                        + "目前【基础清理】可清理空间大小为 %s M" + StrUtil.CRLF
                        + "目前【腾讯专清】可清理空间大小为 %s M" + StrUtil.CRLF + StrUtil.CRLF
                        + "有时清理完成后还有一点点残留，是因为部分文件有程序正在使用，属正常现象",
                rubbishClearServiceImpl.canBaseClearSize(), rubbishClearServiceImpl.canTencentClearSize());
    }
}
