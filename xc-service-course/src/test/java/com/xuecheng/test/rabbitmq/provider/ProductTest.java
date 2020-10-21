package com.xuecheng.test.rabbitmq.provider;

import com.xuecheng.course.CourseApplication;
import com.xuecheng.course.rabbitmq.provider.DirectSend;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Description:
 * @author: YaoGuangXun
 * @date: 2020/9/26 17:51
 * @Version: 1.0
 */
@SpringBootTest(classes = CourseApplication.class)
@RunWith(SpringRunner.class)
public class ProductTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private DirectSend directSend;

    @Test
    public void sendDirectMessage(){
        String messageId = String.valueOf(UUID.randomUUID()).replace("-","");
        String messageData = "ldperson&serialno=116&payintv=*&operator=姚崇2&birthday=1939-12-08&makedate=2020-09-17&maketime=19:51:22";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String,Object> map = new HashMap<>();
        map.put("messageId",messageId);
        map.put("messageData",messageData);
        map.put("createTime",createTime);

        directSend.send(messageData);
    }
}
