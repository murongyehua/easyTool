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

    /**
     * 基础清理可清理的文件大小
     * @return 大小，单位M
     */
    int canBaseClearSize();

    /**
     * 腾讯专清可清理的文件大小
     * @return 大小，单位M
     */
    int canTencentClearSize();

}
