package com.xuecheng.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Description:
 * @author: YaoGuangXun
 * @date: 2020/7/28 22:15
 * @Version: 1.0
 */
@SpringBootApplication
@EntityScan("com.xuecheng.framework.domain.cms")
@ComponentScan(basePackages = {"com.xuecheng.client"}) // 扫描本项目下的所有类
@ComponentScan(basePackages = {"com.xuecheng.framework"}) // 扫描common下的所有类
public class CmsClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(CmsClientApplication.class, args);
    }
}
