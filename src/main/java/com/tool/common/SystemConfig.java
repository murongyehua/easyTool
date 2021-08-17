package com.tool.common;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.tool.service.IService;

import java.util.List;
import java.util.Objects;

public class SystemConfig {

    public static String[] DEFAULT_FONT  = new String[]{
            "Table.font"
            ,"TableHeader.font"
            ,"CheckBox.font"
            ,"Tree.font"
            ,"Viewport.font"
            ,"ProgressBar.font"
            ,"RadioButtonMenuItem.font"
            ,"ToolBar.font"
            ,"ColorChooser.font"
            ,"ToggleButton.font"
            ,"Panel.font"
            ,"TextArea.font"
            ,"Menu.font"
            ,"TableHeader.font"
            // ,"TextField.font"
            ,"OptionPane.font"
            ,"MenuBar.font"
            ,"Button.font"
            ,"Label.font"
            ,"PasswordField.font"
            ,"ScrollPane.font"
            ,"MenuItem.font"
            ,"ToolTip.font"
            ,"List.font"
            ,"EditorPane.font"
            ,"Table.font"
            ,"TabbedPane.font"
            ,"RadioButton.font"
            ,"CheckBoxMenuItem.font"
            ,"TextPane.font"
            ,"PopupMenu.font"
            ,"TitledBorder.font"
            ,"ComboBox.font"
    };

    public static Boolean canUseScreenCutHotKey = true;

    public static String shjwToken;

    public static Boolean hasShowNotice;

    public static final String ServiceConfigPath = Objects.requireNonNull(IService.class.getResource("service.ini")).getPath();
    public static final String configPath =  Objects.requireNonNull(IService.class.getResource("config.ini")).getPath();
//    public static final String ServiceConfigPath = new File("service.ini").getAbsolutePath();
//    public static final String configPath = new File("config.ini").getAbsolutePath();


    public static void updateConfig(String name, String value) {
        List<String> configList = FileUtil.readLines(SystemConfig.configPath, "utf-8");
        Integer targetIndex = null;
        for (int index=0; index<configList.size(); index++) {
            String old = configList.get(index);
            if (StrUtil.isNotEmpty(old) && name.equals(old.split("=")[0])) {
                targetIndex = index;
                break;
            }
        }
        if (targetIndex != null) {
            configList.set(targetIndex, String.format("%s=%s", name, value));
        }
        FileUtil.writeLines(configList, SystemConfig.configPath, "utf-8", false);
    }

}
