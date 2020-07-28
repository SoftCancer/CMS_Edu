package com.xuecheng.client.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @author: YaoGuangXun
 * @date: 2020/7/28 22:29
 * @Version: 1.0
 */
@Configuration
public class RabbitMQConfig {

    // 交换机名字
    public static final String CMS_CLIENT_EX ="cms_client_exchange";

    // 队列名称
    @Value("${cmsedu.mq.queue}")
    private String queue_cms_client;

    // 路由
    @Value("${cmsedu.mq.routingKey}")
    private String routingKey;


    /**
     * @Description: 获取交换机
     * durable(true) ： 设置消息持久化
     * @Author: YaoGX
     * @Date: 2020/7/28 22:56
     **/
    @Bean
    public Exchange cmsExchangeDirect(){
        return ExchangeBuilder.directExchange(CMS_CLIENT_EX).durable(true).build();
    }

    /**
     * @Description: 声明队列
     * @Author: YaoGX
     * @Date: 2020/7/28 22:57 
     **/
    @Bean
    public Queue cmsQuery(){
        Queue queue = new Queue(queue_cms_client);
        return queue;
    }

    /**
     * @Description: 绑定队列到交换机
     * @Author: YaoGX
     * @Date: 2020/7/28 22:58 
     **/
    @Bean
    public Binding bindingQueueExchange(@Qualifier("cmsQuery") Queue queue,@Qualifier("cmsExchangeDirect") Exchange exchange){

        return BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs();

    }
}
