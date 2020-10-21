package com.xuecheng.course.rabbitmq.config;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component

public class DirectReceiver {

    @RabbitHandler
    @RabbitListener(queues = "TestDirectQueue")//监听的队列名称 TestDirectQueue
    public void process(Map testMessage) {
        System.out.println("DirectReceiver消费者收到消息  : " + testMessage);
        Object o = testMessage.get("messageData");
        if (null != o){
            String messageData = String.valueOf(o);
            System.out.println(messageData);
            test02(messageData);
        }
    }

    public Map<String,Object> test02(String str) {
        String[] strArr = str.split("&");
        // 获取表名
        String tableName = strArr[0];
        System.out.println("表名：" + tableName);
        Map map = new HashMap();
        // 移除数组的第一个元素表名
        String[] newArray = Arrays.copyOfRange(strArr, 1, strArr.length);
        for (int i = 0; i < newArray.length; i++) {
            String[] attributeArr = newArray[i].split("=");
            String value = "*".equals(attributeArr[1])? null:attributeArr[1];
            map.put(attributeArr[0],value);
        }
        System.out.println(map);
        return map;
    }

    @RabbitHandler
    @RabbitListener(queues = "directTestA")
    public void receive(Message message, Channel channel) throws IOException {
        try {
//            int a = 1/0;  // 此处异常 ，消息无法被消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
            System.out.println("接受到的消息为:"+new String(message.getBody(),"UTF-8"));
        } catch (Exception e) {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
        }
    }


}