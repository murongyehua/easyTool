package com.tool.client;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.SecureUtil;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component("fileCompareFrame")
public class FileCompareFrame extends CommonFrame{

    @Override
    public void initFrame() {
        super.initFrame();

        this.setTitle("文件比对");
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        List<File> fileList = new ArrayList<>();
        JPanel fileListPanel = new JPanel();
        fileListPanel.setLayout(new BoxLayout(fileListPanel,  BoxLayout.LINE_AXIS));
        JButton chooseFile = new JButton("选择文件");
        chooseFile.addActionListener(e -> {
            if (fileList.size() == 2) {
                JOptionPane.showMessageDialog(null, "只能选择两个文件进行比对", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            JFileChooser fileChooserOne = new JFileChooser();
            fileChooserOne.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooserOne.showDialog(new JLabel(), "选择");
            fileList.add(fileChooserOne.getSelectedFile());
            fileListPanel.removeAll();
            fileList.forEach(x -> {
                Box fileLine = Box.createVerticalBox();
                fileLine.add(Box.createVerticalStrut(50));
                fileLine.add(new JLabel(x.getName()));
                fileLine.add(Box.createHorizontalGlue());
                fileListPanel.add(fileLine);
            });
            fileListPanel.revalidate();
        });

        JPanel chooseFilePanel = new JPanel();
        chooseFilePanel.add(chooseFile);

        JPanel btnPanel = new JPanel();
        JLabel resultLabel = new JLabel("结果：");
        JLabel result = new JLabel();
        result.setFont(new Font("微软雅黑", Font.BOLD, 16));
        result.setForeground(Color.RED);
        result.setText("待比较 (比较的是两个文件内容是否完全一致)");
        JButton submitBtn = new JButton("比较");
        submitBtn.addActionListener(x -> {
            if (fileList.size() != 2) {
                JOptionPane.showMessageDialog(null, "请选择两个文件进行比较", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (ObjectUtil.equals(SecureUtil.md5(fileList.get(0)), SecureUtil.md5(fileList.get(1)))) {
                result.setText("一致");
            }else {
                result.setText("不一致");
            }
            fileList.clear();
            btnPanel.revalidate();
            fileListPanel.removeAll();
            fileListPanel.revalidate();
        });

        btnPanel.add(submitBtn);
        btnPanel.add(resultLabel);
        btnPanel.add(result);
        mainPanel.add(BorderLayout.NORTH, chooseFilePanel);
        mainPanel.add(BorderLayout.CENTER, fileListPanel);
        mainPanel.add(BorderLayout.SOUTH, btnPanel);
        this.getContentPane().add(mainPanel);

        this.setVisible(true);
    }
}
