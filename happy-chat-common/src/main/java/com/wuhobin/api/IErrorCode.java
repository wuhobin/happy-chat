package com.wuhobin.api;

/**
 * @author wuhongbin
 * @version 1.0.0
 * @date 2023/7/5 16:42
 * @description
 */
public interface IErrorCode {

    /**
     * 获取返回码
     * @return 1
     */
    long getCode();

    /**
     * 获取返回信息
     * @return
     */
    String getMessage();
}
