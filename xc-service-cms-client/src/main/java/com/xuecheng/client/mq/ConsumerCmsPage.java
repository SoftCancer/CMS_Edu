package com.xuecheng.client.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.client.service.PageService;
import com.xuecheng.framework.domain.cms.CmsPage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Description:
 * @author: YaoGuangXun
 * @date: 2020/8/5 23:27
 * @Version: 1.0
 */
@Component
public class ConsumerCmsPage {

    @Autowired
    private PageService pageService;


    @RabbitListener(queues = {"${cmsedu.mq.queue}"})
    public void getMqMessage(String msg){

        // 解析数据
        Map map = JSON.parseObject(msg,Map.class);

        String pageId = String.valueOf(map.get("pageId"));
        if (StringUtils.isEmpty(pageId)){
            return;
        }

        // 调用 savePageToServerPath 方法将页面 从 GridFS 中下载到服务器
       pageService.savePageToServerPath(pageId);

    }
}
