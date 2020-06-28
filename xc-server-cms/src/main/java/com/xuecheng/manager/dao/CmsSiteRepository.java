package com.xuecheng.manager.dao;

import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Description:
 * @author: YaoGuangXun
 * @date: 2020/6/2611:46
 * @Version: 1.0
 */
public interface CmsSiteRepository extends MongoRepository<CmsSite,String> {

}
