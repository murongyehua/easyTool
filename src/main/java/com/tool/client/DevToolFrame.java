package com.tool.client;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

@Component("devToolFrame")
public class DevToolFrame extends CommonFrame{

    // 生成
    JTextArea createUUIDResult = new JTextArea(7,45);
    JScrollPane createUUIDPanel = new JScrollPane(createUUIDResult);
    // 替换
    JTextArea oriContentText = new JTextArea(4, 45);
    JTextField regText = new JTextField(30);
    JTextArea resContentText = new JTextArea(7, 45);
    JLabel oriContentLabel = new JLabel("请输入原始内容");
    JScrollPane oriContentPanel = new JScrollPane(oriContentText);
    JLabel regLabel = new JLabel("要替换的正则");
    JScrollPane resContentPanel = new JScrollPane(resContentText);
    JLabel preContentLabel = new JLabel("前缀");
    JLabel sufContentLabel = new JLabel("后缀");
    JTextField preContentText = new JTextField(15);
    JTextField sufContentText = new JTextField(15);
    JPanel replacePanel = new JPanel();

    @Override
    public void initFrame() {
        super.initFrame();
        this.setTitle("开发工具");
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setOpacity(1f);

        createUUIDResult.setEditable(false);
        createUUIDPanel.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        createUUIDPanel.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        oriContentPanel.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        oriContentPanel.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        resContentPanel.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        resContentPanel.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        resContentText.setEditable(false);
        replacePanel.add(oriContentLabel);
        replacePanel.add(oriContentPanel);
        replacePanel.add(regLabel);
        replacePanel.add(regText);
        replacePanel.add(preContentLabel);
        replacePanel.add(preContentText);
        replacePanel.add(sufContentLabel);
        replacePanel.add(sufContentText);
        replacePanel.add(resContentPanel);

        JTabbedPane tabPane = new JTabbedPane();
        tabPane.add("JSON", this.jsonPanel());
        tabPane.add("UUID", this.uuidPanel());
        tabPane.add("COLOR", this.colorPickPanel());
        Container container = this.getContentPane();
        container.add(tabPane);
        this.setBackground(new Color(214, 214, 220));
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
        resJson.setEditable(false);
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
        submit.addActionListener(e -> {
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
        });
        jsonPanel.add(submit);
        return jsonPanel;
    }

    private JPanel uuidPanel() {
        JPanel uuidPanel = new JPanel();
        uuidPanel.setLayout(new BorderLayout());
        JPanel topPanel = new JPanel();
        JLabel modelLabel = new JLabel("模式");
        JComboBox<String> modelBox = new JComboBox<>();
        modelBox.addItem("生成");
        modelBox.addItem("替换");
        modelBox.addItemListener(e -> {
            changeView(uuidPanel, modelBox);
            uuidPanel.repaint();
        });
        JLabel typeLabel = new JLabel("类型");
        JComboBox<String> typeBox = new JComboBox<>();
        typeBox.addItem("uuid");
        typeBox.addItem("原始uuid");
        typeBox.addItem("纯数字");
        topPanel.add(typeLabel);
        topPanel.add(typeBox);
        topPanel.add(modelLabel);
        topPanel.add(modelBox);
        JButton doUUidBtn = new JButton("执行");
        doUUidBtn.addActionListener(e -> {
          if (Objects.requireNonNull(modelBox.getSelectedItem()).toString().equals("生成")) {
              switch (Objects.requireNonNull(typeBox.getSelectedItem()).toString()) {
                  case "uuid":
                      StringBuilder uuids = new StringBuilder();
                      for (int index=0;index<5;index++) {
                          uuids.append(IdUtil.simpleUUID()).append(StrUtil.CRLF);
                      }
                      createUUIDResult.setText(uuids.toString());
                      break;
                  case "原始uuid":
                      StringBuilder normalUUIDS = new StringBuilder();
                      for (int index=0;index<5;index++) {
                          normalUUIDS.append(IdUtil.randomUUID()).append(StrUtil.CRLF);
                      }
                      createUUIDResult.setText(normalUUIDS.toString());
                      break;
                  case "纯数字":
                      StringBuilder numberUUIDS = new StringBuilder();
                      for (int index=0;index<5;index++) {
                          numberUUIDS.append(IdUtil.getSnowflake().nextId()).append(StrUtil.CRLF);
                      }
                      createUUIDResult.setText(numberUUIDS.toString());
                      break;
                  default:
                      break;
              }
          }else {
              String oriContent = oriContentText.getText();
              if (StrUtil.isEmpty(oriContent)) {
                  JOptionPane.showMessageDialog(null, "原始内容不能为空", "提示", JOptionPane.ERROR_MESSAGE);
                  return;
              }
              String reg = regText.getText();
              if (StrUtil.isEmpty(reg)) {
                  JOptionPane.showMessageDialog(null, "待替换的正则不能为空", "提示", JOptionPane.ERROR_MESSAGE);
                  return;
              }
              switch (Objects.requireNonNull(typeBox.getSelectedItem()).toString()) {
                  case "uuid":
                      resContentText.setText(oriContent.replaceAll(reg, preContentText.getText() + IdUtil.simpleUUID() + sufContentText.getText()));
                      break;
                  case "原始uuid":
                      resContentText.setText(oriContent.replaceAll(reg, preContentText.getText() + IdUtil.randomUUID() + sufContentText.getText()));
                      break;
                  case "纯数字":
                      resContentText.setText(oriContent.replaceAll(reg, preContentText.getText() + IdUtil.getSnowflake().nextId() + sufContentText.getText()));
                      break;
                  default:
                      break;
              }
          }
        });

        uuidPanel.add(BorderLayout.NORTH, topPanel);
        this.changeView(uuidPanel, modelBox);
        uuidPanel.add(BorderLayout.SOUTH, doUUidBtn);

        return uuidPanel;
    }

    private void changeView(JPanel centerPanel, JComboBox<String> modelBox) {
        if (Objects.requireNonNull(modelBox.getSelectedItem()).toString().equals("替换")) {
            centerPanel.remove(createUUIDPanel);
            oriContentText.setText("");
            regText.setText("");
            resContentText.setText("");
            centerPanel.add(BorderLayout.CENTER, replacePanel);
            this.repaint();
            this.revalidate();
        }else {
            centerPanel.remove(replacePanel);
            createUUIDResult.setText("");
            centerPanel.add(BorderLayout.CENTER, createUUIDPanel);
            this.repaint();
            this.revalidate();
        }
    }

    private JPanel colorPickPanel() {
        JPanel colorPickPanel = new JPanel();
        JColorChooser chooser = new JColorChooser();
        colorPickPanel.add(chooser);
        return colorPickPanel;
    }

    private boolean checkJson(String json) {
        try {
            JSONUtil.parseObj(json);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    private String formatJson(String json) {
        return JSONUtil.formatJsonStr(json);
    }
}
