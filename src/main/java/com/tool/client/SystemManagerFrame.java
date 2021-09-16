package com.tool.client;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.tool.common.NetCheckUtil;
import com.tool.common.SystemConfig;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;

@Component("systemManagerFrame")
public class SystemManagerFrame extends CommonFrame{

    @Autowired
    @Qualifier("indexFrame")
    private CommonFrame indexFrame;

    @Value("${project.version}")
    private String version;

    @Value("${tool.server.host}")
    private String toolServerHost;

    @Value("${tool.server.port}")
    private String toolServerPort;;

    private List<String> serviceConfigLines;

    private final Map<String, List<JPanel>> menuClassMap = new HashMap<>();

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
        JPanel menusPanel = new JPanel(new BorderLayout());
        JPanel menuClassPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JPanel menuShowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        // 初始化菜单
        this.initMenus();
        JLabel classLabel = new JLabel("选择分类");
        JComboBox<String> classBox = new JComboBox<>();
        classBox.addItem("全部分类");
        for (String key : menuClassMap.keySet()) {
            classBox.addItem(key);
        }
        menuClassPanel.add(classLabel);
        menuClassPanel.add(classBox);
        this.functionPanels(Objects.requireNonNull(classBox.getSelectedItem()).toString()).forEach(menuShowPanel::add);
        classBox.addItemListener(e -> {
            menuShowPanel.removeAll();
            this.functionPanels(Objects.requireNonNull(classBox.getSelectedItem()).toString()).forEach(menuShowPanel::add);
            menuShowPanel.repaint();
            menuShowPanel.revalidate();
        });
        menusPanel.add(BorderLayout.NORTH, menuClassPanel);
        menusPanel.add(BorderLayout.CENTER, menuShowPanel);
        JButton commitBtn = new JButton("提交修改");
        commitBtn.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
        commitBtn.setSize(150, 30);
        commitBtn.addActionListener(e -> {
            FileUtil.writeLines(serviceConfigLines, SystemConfig.ServiceConfigPath, "utf-8", false);
            indexFrame.repaint();
            indexFrame.invalidate();
            indexFrame.validate();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, "修改完成，重新启动后生效", "提示", JOptionPane.INFORMATION_MESSAGE);
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
        checkVersionBtn.addActionListener(e -> {
            if (!NetCheckUtil.check(toolServerHost, Integer.parseInt(toolServerPort))) {
                return;
            }
            String res = HttpUtil.get(String.format("http://%s:%s/toolServer/version/fetchVersion", toolServerHost, toolServerPort), StandardCharsets.UTF_8);
            JSONObject jsonObject = JSONUtil.parseObj(res);
            String newVersion = (String) jsonObject.get("version");
            if (newVersion.equals(this.version)) {
                JOptionPane.showMessageDialog(null, "当前已是最新版本", "提示", JOptionPane.INFORMATION_MESSAGE);
            }else {
                String updateContent = (String) jsonObject.get("updateContent");
                int option = JOptionPane.showConfirmDialog(null, String.format("获取到最新版本: %s\r\n更新内容:\r\n%s\r\n\r\n是否立即更新？\r\nTip:更新完成前请勿关闭程序或电脑!", newVersion, updateContent), "提示", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    HttpUtil.downloadFile(String.format("http://%s:%s/toolServer/version/getNewVersion", toolServerHost, toolServerPort), new File("newVersion.zip"));
                    try {
                        this.update();
                    }catch (Exception exception) {
                        JOptionPane.showMessageDialog(null, "更新失败，请联系开发者!", "提示", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    JOptionPane.showMessageDialog(null, "更新准备就绪，将自动退出，请稍候重新启动", "提示", JOptionPane.INFORMATION_MESSAGE);
                    try {
                        this.runUpdateExe();
                    } catch (IOException ioException) {
                        JOptionPane.showMessageDialog(null, "更新失败，请联系开发者!", "提示", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    System.exit(0);
                }
            }
        });
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
        suggestContent.setText("请将反馈内容发送到邮箱：murongyehua@163.com\r\n使用过程中发现的问题\r\n对未来新功能的期望\r\n都可以通过邮件发给我\r\n感谢使用和配合~");
        suggestContent.setEditable(false);
        JPanel suggestContentPanel = new JPanel();
        suggestContentPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 6, 6));
        suggestContentPanel.add(suggestContent);
        suggestPanel.add(BorderLayout.NORTH, suggestLabel);
        suggestPanel.add(BorderLayout.CENTER, suggestContentPanel);
        suggestPanel.add(BorderLayout.SOUTH, IndexFrame.getFootPanel());

        mainPanel.add(BorderLayout.NORTH, versionPanel);
        mainPanel.add(BorderLayout.CENTER, activePanel);
        mainPanel.add(BorderLayout.SOUTH, suggestPanel);
        this.setVisible(true);
    }

    private List<JPanel> functionPanels(String className) {
        if ("全部分类".equals(className)) {
            List<JPanel> result = new LinkedList<>();
            for (String key : menuClassMap.keySet()) {
                result.addAll(menuClassMap.get(key));
            }
            return result;
        } else {
            return menuClassMap.get(className);
        }
    }

    private void initMenus() {
        menuClassMap.clear();
        serviceConfigLines = FileUtil.readLines(SystemConfig.ServiceConfigPath, "utf-8");
        for (int index=1;index<serviceConfigLines.size();index++) {
            String lineContent = serviceConfigLines.get(index);
            String[] cs = lineContent.split("\\|\\|");
            if(!menuClassMap.containsKey(cs[4])) {
                menuClassMap.put(cs[4], new LinkedList<>());
            }
            List<JPanel> jPanels = menuClassMap.get(cs[4]);
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
    }

    private void update() {
        File newFile = new File("newVersion.zip");
        File unzip = ZipUtil.unzip(newFile);
        try {
            File easyTool = new File(unzip.getAbsoluteFile() + "\\easyTool\\");
            for (File file : Objects.requireNonNull(easyTool.listFiles())) {
                FileUtil.move(file, new File(file.getName()), true);
            }
        } finally {
            FileUtil.del(unzip);
            FileUtil.del(newFile.getAbsoluteFile());
        }

    }

    private void runUpdateExe() throws IOException {
        Runtime.getRuntime().exec("updateTool.exe /k start");
    }
}
