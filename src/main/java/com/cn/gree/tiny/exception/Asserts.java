package com.cn.gree.tiny.exception;

import com.cn.gree.tiny.common.result.IErrorCode;

/**
 * 断言处理类，用于抛出各种API异常
 *
 */
public class Asserts {
    public static void fail(String message) {
        throw new ApiException(message);
    }

    public static void fail(IErrorCode errorCode) {
        throw new ApiException(errorCode);
    }
}
