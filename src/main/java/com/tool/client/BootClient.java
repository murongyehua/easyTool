package com.tool.client;

import com.tool.common.SystemConfig;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;

@Service
public class BootClient {

    @Autowired
    @Qualifier("indexFrame")
    private IFrame indexFrame;

    @PostConstruct
    public void runApplication() {
        indexFrame.initFrame();
    }

}
