package com.tool.client;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

@Component("secretToolFrame")
public class SecretToolFrame extends CommonFrame{



    @Override
    public void initFrame() {
        super.initFrame();
        this.setTitle("加解密集");
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel menuPanel = new JPanel();
        JComboBox<String> secretType = new JComboBox<>();
        secretType.setSize(100, 50);
        secretType.addItem("MD5");
        secretType.addItem("BCrypt");
        menuPanel.add(new JLabel("加密模式"));
        menuPanel.add(secretType);

        JPanel secretPanel = new JPanel(new BorderLayout());
        JLabel tipLabel = new JLabel("Tip: 密文为空时会根据明文生成密文，不为空时会生成明文与密文的比较结果");
        tipLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        tipLabel.setForeground(Color.ORANGE);
        JTextField oriContent = new JTextField(95);
        JTextField secretContent = new JTextField(95);
        JTextArea result = new JTextArea(13, 45);
        result.setEditable(false);
        JPanel contentPanel = new JPanel();
        contentPanel.add(new JLabel("明文"));
        contentPanel.add(oriContent);
        contentPanel.add(new JLabel("密文"));
        contentPanel.add(secretContent);
        contentPanel.add(new JLabel("结果"));
        contentPanel.add(result);
        secretPanel.add(BorderLayout.NORTH, tipLabel);
        secretPanel.add(BorderLayout.CENTER, contentPanel);
        mainPanel.add(BorderLayout.NORTH, menuPanel);
        mainPanel.add(BorderLayout.CENTER, secretPanel);
        JButton createBtn = new JButton("生成");
        createBtn.addActionListener(e -> {
            if (StrUtil.isEmpty(oriContent.getText())) {
                JOptionPane.showMessageDialog(null, "明文不能为空", "提示", JOptionPane.ERROR_MESSAGE);
                return;
            }
            switch (Objects.requireNonNull(secretType.getSelectedItem()).toString()) {
                case "MD5":
                    if (StrUtil.isEmpty(secretContent.getText())) {
                        result.setText(SecureUtil.md5(oriContent.getText()));
                    }else {
                        result.setText(Boolean.toString(ObjectUtil.equals(SecureUtil.md5(oriContent.getText()), secretContent.getText())));
                    }
                    break;
                case "BCrypt":
                    if (StrUtil.isEmpty(secretContent.getText())) {
                        result.setText(new BCryptPasswordEncoder().encode(oriContent.getText()));
                    }else {
                        result.setText(Boolean.toString(new BCryptPasswordEncoder().matches(oriContent.getText(), secretContent.getText())));
                    }
                    break;
                default:
                    break;
            }
        });
        mainPanel.add(BorderLayout.SOUTH, createBtn);
        this.getContentPane().add(mainPanel);

        this.setVisible(true);
    }

}
