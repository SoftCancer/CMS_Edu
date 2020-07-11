package com.xuecheng.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @Description:
 * @author: YaoGuangXun
 * @date: 2020/6/7 23:15
 * @Version: 1.0
 */
@SpringBootApplication(scanBasePackages = "com.xuecheng.manager.controller")
@EntityScan("com.xuecheng.framework.domain.cms")
@ComponentScan(basePackages = {"com.xuecheng.api"})
@ComponentScan(basePackages = {"com.xuecheng.manager"}) // 扫描本项目下的所有类
@ComponentScan(basePackages = {"com.xuecheng.framework"}) // 扫描common下的所有类
public class ManagerAppli {

    public static void main(String[] args) {
        SpringApplication.run(ManagerAppli.class,args);
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }
}
