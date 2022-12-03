package com.dicemy.reggie.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class mailUtilTest {
    @Autowired
    private QQMailUtils QQMailUtils;
    @Test
    public void test() {
        QQMailUtils.sendSimpleMail("1583925267@qq.com","test","test");
    }
}
