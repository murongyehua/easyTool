package com.tool.client;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.tool.common.SystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class BootClient {

    @Autowired
    @Qualifier("indexFrame")
    private CommonFrame indexFrame;

    @PostConstruct
    public void runApplication() {
        // 读配置
        initConfig();
        // 初始化主窗口
        indexFrame.initFrame();
    }

    private void initConfig() {
        List<String> configList = FileUtil.readLines(SystemConfig.configPath, "utf-8");
        configList.forEach(x -> {
            if (StrUtil.isNotEmpty(x)) {
                String[] configs = x.split("=");
                switch (configs[0]) {
                    case "shjw.token":
                        SystemConfig.shjwToken = configs[1];
                        break;
                    case "hasShowNotice":
                        SystemConfig.hasShowNotice = "0".equals(configs[1]);
                        break;
                    default:
                        break;
                }
            }
        });
    }

}
