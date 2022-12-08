package com.dicemy.reggie.common;

import lombok.extern.java.Log;

/*
 * 将用户登录后的ID保存到当前的线程中。简化每次都需要在session中取id的操作
 * 获取和修改当前的线程中的threadLocal量。每一个线程中的threadLocal都是独立的。
 * 每一次经过过滤器，判断时都setCurrentId一下，保证每一个线程中都有正确的id
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }
}
