package com.xuecheng.manager.dao;

import com.xuecheng.framework.domain.cms.CmsConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Description: CMS 页面配置DAO
 * @author: YaoGuangXun
 * @date: 2020/7/40:06
 * @Version: 1.0
 */
public interface CmsConfigRepository extends MongoRepository<CmsConfig,String> {
}
