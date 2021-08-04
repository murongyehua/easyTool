package com.tool;

import com.tool.common.SystemConfig;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;
import java.awt.*;

@SpringBootApplication
public class EasyToolApplication {

    public static void main(String[] args) {
        try {
            BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.translucencySmallShadow;
            BeautyEyeLNFHelper.translucencyAtFrameInactive = false;
            UIManager.put("RootPane.setupButtonVisible", false);
            for (int i = 0; i < SystemConfig.DEFAULT_FONT.length; i++) {
                UIManager.put(SystemConfig.DEFAULT_FONT[i],new Font("微软雅黑", Font.PLAIN,14));
            }
            BeautyEyeLNFHelper.launchBeautyEyeLNF();
            UIManager.put("TabbedPane.tabAreaInsets"
                    , new javax.swing.plaf.InsetsUIResource(0,1,0,20));
        }
        catch(Exception e) {
            //TODO exception
        }
        SpringApplication.run(EasyToolApplication.class, args);
    }

}
