package com.tool.client;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONNull;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.tool.client.assembly.InstructButton;
import com.tool.common.NetCheckUtil;
import com.tool.common.SystemConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

@Component
public class SHJWFrame extends CommonFrame {

    @Value("${shjw.url}")
    private String url;

    JPanel mainPanel;
    JPanel enterPanel;
    JPanel regPanel;

    private static boolean firstJoin = true;

    JPanel gamePanel;

    @Override
    public void initFrame() {
        super.initFrame();

        this.setTitle("山海见闻");
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        this.initPanel();

        mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5 ,5));
        mainPanel.setSize(this.getSize());
        this.getContentPane().add(mainPanel);

        enterPanel.setLayout(new BoxLayout(enterPanel, BoxLayout.LINE_AXIS));
        Box enterBox = Box.createVerticalBox();
        JButton enterBtn = new JButton("进入游戏");
        JLabel tip1 = new JLabel("Tip: 此界面会进行游戏初始化，该过程可能会出现略微的卡顿，这可能需要几秒钟。");
        JLabel tip2 = new JLabel("Tip_Plus: 游戏中的按钮均支持快捷键，使用ALT+对应的命令即可使用");
        enterBtn.setFont(new java.awt.Font("华文行楷",  1,  15));
        enterBox.add(Box.createVerticalStrut(150));
        enterBox.add(enterBtn);
        enterBox.add(tip1);
        enterBox.add(tip2);
        enterPanel.add(enterBox);

        regPanel.setLayout(new BoxLayout(regPanel, BoxLayout.LINE_AXIS));
        Box regBox1 = Box.createVerticalBox();
        regBox1.add(Box.createVerticalStrut(120));
        JLabel jLabel = new JLabel("首次登录需要注册，请输入昵称");
        jLabel.setSize(250,50);
        jLabel.setFont(new java.awt.Font("华文行楷",  1,  15));
        JTextField nickNameText = new JTextField();
        nickNameText.setSize(200, 50);
        JButton regBtn = new JButton("确定");
        regBtn.setFont(new java.awt.Font("华文行楷",  1,  15));
        regBtn.setMargin(new Insets(10, 152, 0, 152));
        regBox1.add(jLabel);
        regBox1.add(nickNameText);
        regBox1.add(regBtn);
        regPanel.add(regBox1);

        gamePanel.setSize(this.getSize());
        gamePanel.setLayout(new BorderLayout());
        JTextArea textArea = new JTextArea(16, 1);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setSize(this.getWidth() - 10, 300);
        gamePanel.add(BorderLayout.CENTER, scrollPane);
        initMainFrame(textArea);
        enterBtn.addActionListener(e -> enterBtn(textArea));
        regBtn.addActionListener(e -> regBtn(nickNameText));

        if (firstJoin) {
            mainPanel.add(enterPanel);
            this.setVisible(true);
            sendPost("退出");
            firstJoin = false;
        }else {
            this.setVisible(true);
        }
    }

    private void initPanel() {
        mainPanel = new JPanel();
        enterPanel = new JPanel();
        regPanel = new JPanel();
        gamePanel = new JPanel();
    }

    private void enterBtn(JTextArea jTextArea) {
        try {
            if (NetCheckUtil.check("39.99.216.184", 9091)) {
                if ("null".equals(SystemConfig.shjwToken)) {
                    mainPanel.removeAll();
                    mainPanel.add(regPanel);
                    mainPanel.repaint();
                    mainPanel.revalidate();
                }else {
                    sendPost("山海见闻");
                    sendPostAndChangeText("1", jTextArea);
                    mainPanel.removeAll();
                    mainPanel.add(gamePanel);
                    mainPanel.repaint();
                    mainPanel.revalidate();
                }
            }
        } catch (Exception e) {
            Object[] options = {"确定", "取消"};
            JOptionPane.showOptionDialog(null, "网络连接错误", "提示", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE, null, options, options[0]);
        }

    }

    private void regBtn(JTextField textField) {
        String nickName = textField.getText();
        if (nickName == null || nickName.length() > 6 || nickName.length() < 1) {
            Object[] options = { "确定", "取消" };
            JOptionPane.showOptionDialog(null, "请输入1-6位昵称", "提示", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE,null, options, options[0]);
            return;
        }
        String jsonStr = sendReg(nickName);
        JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
        String code = (String) jsonObject.get("code");
        String info = "";
        if (jsonObject.get("info") != JSONNull.NULL) {
            info = (String) jsonObject.get("info");
        }
        String data = "";
        if (jsonObject.get("data") != JSONNull.NULL) {
            data = (String) jsonObject.get("data");
        }
        if (!"0".equals(code)) {
            Object[] options = { "确定", "取消" };
            JOptionPane.showOptionDialog(null, info, "提示", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE,null, options, options[0]);
            return;
        }
        SystemConfig.updateConfig("shjw.token", data);
        mainPanel.removeAll();
        mainPanel.add(gamePanel);
        mainPanel.repaint();
        mainPanel.revalidate();
    }

    private void initMainFrame(JTextArea textArea) {
        JPanel btnPanel = new JPanel(new BorderLayout());
        InstructButton btn1 = new InstructButton("1");
        btn1.addActionListener(e -> sendPostAndChangeText("1", textArea));
        btn1.setMnemonic(KeyEvent.VK_1);
        InstructButton btn2 = new InstructButton("2");
        btn2.addActionListener(e -> sendPostAndChangeText("2", textArea));
        btn2.setMnemonic(KeyEvent.VK_2);
        InstructButton btn3 = new InstructButton("3");
        btn3.addActionListener(e -> sendPostAndChangeText("3", textArea));
        btn3.setMnemonic(KeyEvent.VK_3);
        InstructButton btn4 = new InstructButton("4");
        btn4.addActionListener(e -> sendPostAndChangeText("4", textArea));
        btn4.setMnemonic(KeyEvent.VK_4);
        InstructButton btn5 = new InstructButton("5");
        btn5.addActionListener(e -> sendPostAndChangeText("5", textArea));
        btn5.setMnemonic(KeyEvent.VK_5);
        InstructButton btn6 = new InstructButton("6");
        btn6.addActionListener(e -> sendPostAndChangeText("6", textArea));
        btn6.setMnemonic(KeyEvent.VK_6);
        InstructButton btn7 = new InstructButton("7");
        btn7.addActionListener(e -> sendPostAndChangeText("7", textArea));
        btn7.setMnemonic(KeyEvent.VK_7);
        InstructButton btn8 = new InstructButton("8");
        btn8.addActionListener(e -> sendPostAndChangeText("8", textArea));
        btn8.setMnemonic(KeyEvent.VK_8);
        InstructButton btn9 = new InstructButton("9");
        btn9.addActionListener(e -> sendPostAndChangeText("9", textArea));
        btn9.setMnemonic(KeyEvent.VK_9);
        InstructButton btn0 = new InstructButton("0");
        btn0.addActionListener(e -> sendPostAndChangeText("0", textArea));
        btn0.setMnemonic(KeyEvent.VK_0);
        InstructButton btnQ = new InstructButton("Q");
        btnQ.addActionListener(e -> sendPostAndChangeText("Q", textArea));
        btnQ.setMnemonic(KeyEvent.VK_Q);
        JLabel jLabel = new JLabel("上列快捷按钮不支持的操作请使用输入框：");
        JTextField jTextField = new JTextField();
        jTextField.setPreferredSize(new Dimension(100,30));
        InstructButton send = new InstructButton("发送");
        send.setMnemonic(KeyEvent.VK_ENTER);
        send.setPreferredSize(new Dimension(75, 30));
        send.addActionListener(e -> {
            String msg = jTextField.getText();
            if (StrUtil.isEmpty(msg)) {
                Object[] options = { "确定", "取消" };
                JOptionPane.showOptionDialog(null, "发送内容不能为空", "提示", JOptionPane.DEFAULT_OPTION,
                        JOptionPane.WARNING_MESSAGE,null, options, options[0]);
                return;
            }
            sendPostAndChangeText(msg, textArea);
        });
        JPanel systemBtnPanel = new JPanel();
        systemBtnPanel.add(btn1);
        systemBtnPanel.add(btn2);
        systemBtnPanel.add(btn3);
        systemBtnPanel.add(btn4);
        systemBtnPanel.add(btn5);
        systemBtnPanel.add(btn6);
        systemBtnPanel.add(btn7);
        systemBtnPanel.add(btn8);
        systemBtnPanel.add(btn9);
        systemBtnPanel.add(btn0);
        systemBtnPanel.add(btnQ);
        JPanel diyBtnPanel = new JPanel();
        diyBtnPanel.add(jLabel);
        diyBtnPanel.add(jTextField);
        diyBtnPanel.add(send);
        btnPanel.add(BorderLayout.CENTER, systemBtnPanel);
        btnPanel.add(BorderLayout.SOUTH, diyBtnPanel);
        gamePanel.add(BorderLayout.SOUTH, btnPanel);
    }

    private void sendPostAndChangeText(String msg, JTextArea textArea) {
        textArea.setText(sendPost(msg));
        textArea.setCaretPosition(0);
    }

    public String sendPost(String msg) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("token", SystemConfig.shjwToken);
        map.put("msg", msg);
        return HttpUtil.post(url + "/client", map);
    }

    public String sendReg(String msg) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("token", SystemConfig.shjwToken);
        map.put("nickName", msg);
        return HttpUtil.post(url + "/reg", map);
    }

}
