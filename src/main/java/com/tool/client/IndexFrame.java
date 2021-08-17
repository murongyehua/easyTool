package com.tool.client;

import cn.hutool.core.io.FileUtil;
import com.tool.common.SystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component("indexFrame")
public class IndexFrame extends CommonFrame {

    @Autowired
    private Map<String, CommonFrame> frameMap;

    @Value("${system.notice}")
    private String noticeMsg;

    @Override
    public void initFrame() {
        super.initFrame();
        setTray("Easy-Tool", this.getIconImage());
        this.setTitle("Easy-Tool");
        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        Container container = this.getContentPane();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(2,2));
        mainPanel.setSize(this.getWidth(), this.getHeight());
        mainPanel.add(BorderLayout.CENTER, this.getMainPanel());
        mainPanel.add(BorderLayout.NORTH, this.getTopPanel());
        mainPanel.add(BorderLayout.SOUTH, getFootPanel());
        container.add(mainPanel);
        this.setVisible(true);
        if (SystemConfig.hasShowNotice) {
            Object[] options ={ "知道了", "不再提示" };  //自定义按钮上的文字
            int key = JOptionPane.showOptionDialog(this, noticeMsg, "公告",JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            if (key == 1) {
                SystemConfig.updateConfig("hasShowNotice", "1");
            }
        }
    }

    /**
     * 顶部
     * @return
     */
    private JPanel getTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.CYAN);
        topPanel.setSize(this.getWidth()-10, 40);
        JLabel label = new JLabel("welcome to use Easy-Tool!");
        label.setSize(this.getWidth(), 50);
        topPanel.add(label);
        return topPanel;
    }

    /**
     * 主体
     * @return
     */
    private JPanel getMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.CYAN);
        mainPanel.setSize(this.getWidth(), 400);
        JPanel menuName = new JPanel();
        menuName.setSize(this.getWidth(), 25);
        JLabel nameContent = new JLabel("已开启的功能列表");
        nameContent.setFont(new Font("微软雅黑", Font.BOLD, 12));
        JButton noticeBtn = new JButton(new ImageIcon(Objects.requireNonNull(IndexFrame.class.getResource("notice.png"))));
        noticeBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, noticeMsg, "公告", JOptionPane.PLAIN_MESSAGE));
        menuName.setLayout(new BoxLayout(menuName, BoxLayout.LINE_AXIS));
        menuName.add(nameContent);
        menuName.add(Box.createHorizontalGlue());
        menuName.add(noticeBtn);
        JPanel menu = new JPanel();
        menu.setSize(this.getWidth()-10, 375);
        menu.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        this.renderMenu().forEach(menu::add);
        mainPanel.add(BorderLayout.NORTH, menuName);
        mainPanel.add(BorderLayout.CENTER, menu);
        return mainPanel;
    }

    /**
     * 底部
     * @return
     */
    public static JPanel getFootPanel() {
        JPanel footPanel = new JPanel();
        footPanel.setBackground(Color.CYAN);
        JLabel copyRight = new JLabel("@author murongyehua");
        copyRight.setHorizontalAlignment(SwingConstants.CENTER);
        footPanel.add(copyRight);
        return footPanel;
    }

    private List<MenuButton> renderMenu() {
        List<String> serviceConfigLines = FileUtil.readLines(SystemConfig.ServiceConfigPath, "utf-8");
        List<MenuButton> menuButtons = new LinkedList<>();
        serviceConfigLines.forEach(serviceConfig -> {
            String[] cs = serviceConfig.split("\\|\\|");
            if ("1".equals(cs[3])) {
                MenuButton menuButton = new MenuButton(cs[0], new ImageIcon(Objects.requireNonNull(IndexFrame.class.getResource(cs[1]))));
                menuButton.addActionListener(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        CommonFrame frame = frameMap.get(cs[2] + "Frame");
                        frame.initFrame();
                    }
                });
                menuButtons.add(menuButton);
            }
        });
        return menuButtons;
    }

    @Override
    public void Show() {
        this.setVisible(true);
    }

    @Override
    public void Exit() {
        System.exit(0);
    }

}
