package com.dicemy.reggie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/*
 * @SpringBootApplication = @SpringBootConfiguration + @EnableAutoConfiguration + @ComponentScan + @Filter
 * @SpringBootConfiguration 可以作为 Spring 标准中 @Configuration 注解的替代。SpringBoot 项目中推荐使用@SpringBootConfiguration 替代 @Configuration。没区别
 * @EnableAutoConfiguration = @AutoConfigurationPackage + @Import(AutoConfigurationImportSelector.class)
 */

/*
 * @ServletComponentScan注解是为了可以扫描到@WebFilter注解标注的类
 * 可以扫描到的注解有：@WebServlet、@WebFilter、@WebListener
 */

/*
 * @EnableTransactionManagement开启事务管理
 * @EnableCaching开启缓存
 */
@Slf4j
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
@EnableCaching
public class ReggieApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class);
        log.info("项目启动成功...");
    }
}
