package com.tool.service;

public interface RubbishClearService {

    /**
     * 基础清理，全自动
     */
    void baseClear();

    /**
     * 自定义清理
     */
    void diyClear();

    /**
     * 腾讯专清(QQ/微信)
     */
    void clearForTencent();

}
