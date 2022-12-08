package com.dicemy.reggie.common;


/**
 *异常的自定义包装类
 */
public class CustomException extends RuntimeException{
    public CustomException(String message) {
        super(message);
    }
}
