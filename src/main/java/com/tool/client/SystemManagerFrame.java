package com.tool.client;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import com.tool.service.IService;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Component("systemManagerFrame")
public class SystemManagerFrame extends CommonFrame{

    @Autowired
    @Qualifier("indexFrame")
    private CommonFrame indexFrame;

    @Value("${project.version}")
    private String version;

    private List<String> serviceConfigLines;

    @Override
    public void initFrame() {
        super.initFrame();
        this.setTitle("系统设置");
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        Container container = this.getContentPane();
        JPanel mainPanel = new JPanel();
        mainPanel.setSize(this.getWidth(), this.getHeight());
        mainPanel.setLayout(new BorderLayout());
        container.add(mainPanel);
        // 功能开关面板
        JPanel activePanel = new JPanel();
        activePanel.setLayout(new BorderLayout());
        activePanel.setSize(this.getWidth(), 350);
        JLabel activeOption = new JLabel("功能选项 勾选表示启用");
        activeOption.setFont(new Font("微软雅黑", Font.BOLD, 12));
        JPanel menusPanel = new JPanel();
        menusPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        this.functionPanels().forEach(menusPanel::add);
        JButton commitBtn = new JButton("提交修改");
        commitBtn.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
        commitBtn.setSize(150, 30);
        commitBtn.addActionListener(e -> {
            FileUtil.writeLines(serviceConfigLines, Objects.requireNonNull(IService.class.getResource("service.ini")).getPath(), "utf-8", false);
            indexFrame.repaint();
            indexFrame.invalidate();
            indexFrame.validate();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, "修改完成", "提示", JOptionPane.INFORMATION_MESSAGE);
        });
        JPanel commitBtnPanel = new JPanel();
        commitBtnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        commitBtnPanel.add(commitBtn);
        activePanel.add(BorderLayout.NORTH, activeOption);
        activePanel.add(BorderLayout.CENTER, menusPanel);
        activePanel.add(BorderLayout.SOUTH, commitBtnPanel);
        // 版本更新面板
        JPanel versionPanel = new JPanel();
        versionPanel.setLayout(new BorderLayout());
        versionPanel.setSize(this.getWidth(), 60);
        JLabel versionLabel = new JLabel("检查更新");
        versionLabel.setFont(new Font("微软雅黑", Font.BOLD, 12));
        JPanel version = new JPanel();
        version.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 3));
        version.add(new JLabel(String.format("当前版本: %s", this.version)));
        JButton checkVersionBtn = new JButton("检查更新");
        checkVersionBtn.setUI(new BEButtonUI(). setNormalColor(BEButtonUI.NormalColor.normal));
        version.add(checkVersionBtn);
        JLabel tipLabel = new JLabel("不强制更新，但使用旧版可能导致部分功能无法正常使用，建议保持最新版本");
        tipLabel.setFont(new Font("微软雅黑", Font.BOLD, 12));
        tipLabel.setForeground(Color.ORANGE);
        versionPanel.add(BorderLayout.NORTH, versionLabel);
        versionPanel.add(BorderLayout.CENTER, version);
        versionPanel.add(BorderLayout.SOUTH, tipLabel);
        // 意见反馈面板
        JPanel suggestPanel = new JPanel();
        suggestPanel.setLayout(new BorderLayout());
        JLabel suggestLabel = new JLabel("意见反馈");
        suggestLabel.setFont(new Font("微软雅黑", Font.BOLD, 12));

        JTextArea suggestContent = new JTextArea(5, 45);
        suggestContent.setLineWrap(true);
        suggestContent.setText("请在此处输入反馈内容(输入前请删除此文本)");
        JPanel suggestContentPanel = new JPanel();
        suggestContentPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 6, 6));
        suggestContentPanel.add(suggestContent);
        JButton suggestBtn = new JButton("提交反馈");
        suggestBtn.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
        JPanel suggestBtnPanel = new JPanel();
        suggestBtnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 3 ,3 ));
        suggestBtnPanel.add(suggestBtn);
        suggestPanel.add(BorderLayout.NORTH, suggestLabel);
        suggestPanel.add(BorderLayout.CENTER, suggestContentPanel);
        suggestPanel.add(BorderLayout.SOUTH, suggestBtnPanel);

        mainPanel.add(BorderLayout.NORTH, versionPanel);
        mainPanel.add(BorderLayout.CENTER, activePanel);
        mainPanel.add(BorderLayout.SOUTH, suggestPanel);
        this.setVisible(true);
    }

    private List<JPanel> functionPanels() {
        serviceConfigLines = FileUtil.readLines(Objects.requireNonNull(IService.class.getResource("service.ini")), "utf-8");
        List<JPanel> jPanels = new LinkedList<>();
        for (int index=1;index<serviceConfigLines.size();index++) {
            String lineContent = serviceConfigLines.get(index);
            String[] cs = lineContent.split("\\|\\|");
            JPanel jPanel = new JPanel();
            jPanel.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
            JCheckBox checkBox = new JCheckBox(cs[0]);
            checkBox.setSelected("1".equals(cs[3]));
            int finalIndex = index;
            checkBox.addItemListener(e -> {
                JCheckBox box = (JCheckBox) e.getItem();
                if (box.isSelected()) {
                    cs[3] = "1";
                }else {
                    cs[3] = "0";
                }
                String afterChange = CollectionUtil.join(Arrays.asList(cs), "||");
                serviceConfigLines.set(finalIndex, afterChange);
            });
            jPanel.add(checkBox);
            jPanels.add(jPanel);
        }
        return jPanels;
    }
}
