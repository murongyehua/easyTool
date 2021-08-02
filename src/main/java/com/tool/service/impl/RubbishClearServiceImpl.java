package com.tool.service.impl;

import com.tool.common.ExecCMD;
import com.tool.service.IService;
import org.springframework.stereotype.Service;

@Service("rubbishClearServiceImpl")
public class RubbishClearServiceImpl implements IService {

    @Override
    public void apply() {
        ExecCMD.exec("cleanmgr");
    }

}
