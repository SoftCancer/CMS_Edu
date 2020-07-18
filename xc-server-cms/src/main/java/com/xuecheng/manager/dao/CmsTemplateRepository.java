package com.xuecheng.manager.dao;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Description:
 * @author: YaoGuangXun
 * @date: 2020/7/1821:29
 * @Version: 1.0
 */
public interface CmsTemplateRepository extends MongoRepository<CmsTemplate,String> {
}
