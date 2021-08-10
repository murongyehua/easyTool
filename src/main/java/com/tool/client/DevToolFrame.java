package com.tool.client;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Component("devToolFrame")
public class DevToolFrame extends CommonFrame{

    @Override
    public void initFrame() {
        super.initFrame();
        this.setTitle("开发工具");
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setOpacity(1f);
        JTabbedPane tabPane = new JTabbedPane();
        tabPane.add("JSON", this.jsonPanel());
        tabPane.add("UUID", this.uuidPanel());
        tabPane.add("COLOR", this.colorPickPanel());
        Container container = this.getContentPane();
        container.add(tabPane);


        this.setBackground(new Color(0,0,1));
        this.setVisible(true);
    }

    private JPanel jsonPanel() {
        JPanel jsonPanel = new JPanel();
        JLabel inputTip = new JLabel(" 请输入原始JSON字符串");
        JTextArea oriJson = new JTextArea(6, 45);
        JScrollPane oriScrollPane = new JScrollPane(oriJson);
        oriScrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        oriScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        oriScrollPane.setSize(this.getWidth(), 200);
        JLabel outputTip = new JLabel(" 结果");
        JTextArea resJson = new JTextArea(7, 45);
        JScrollPane resScrollPane = new JScrollPane(resJson);
        resScrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        resScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        resScrollPane.setSize(this.getWidth(), 200);
        jsonPanel.add(inputTip);
        jsonPanel.add(oriScrollPane);
        jsonPanel.add(outputTip);
        jsonPanel.add(resScrollPane);
        JButton submit = new JButton("校验并格式化");
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String json = oriJson.getText();
                if (StrUtil.isEmpty(oriJson.getText())) {
                    JOptionPane.showMessageDialog(null, "原始JSON字符串不能为空", "提示", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!checkJson(json)) {
                    JOptionPane.showMessageDialog(null, "JSON格式校验不通过，请检查", "提示", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                resJson.setText(formatJson(json));
                JOptionPane.showMessageDialog(null, "检验并格式化成功", "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        jsonPanel.add(submit);
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

    private boolean checkJson(String json) {
        try {
            JSONObject jsonObject = JSONUtil.parseObj(json);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    private String formatJson(String json) {
        return JSONUtil.formatJsonStr(json);
    }
}
