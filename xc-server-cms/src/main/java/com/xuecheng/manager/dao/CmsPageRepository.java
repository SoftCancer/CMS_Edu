package com.xuecheng.manager.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Description:
 * @author: YaoGuangXun
 * @date: 2020/6/923:09
 * @Version: 1.0
 */
public interface CmsPageRepository extends MongoRepository<CmsPage,String> {

    /**
     * @Description: 根据 站点id，页面名称，页面地址查询 系统页面。
     * @Author: YaoGX
     * @Date: 2020/6/21 15:52
     **/
    public CmsPage findByPageNameAndSiteIdAndPageWebPath(String pageName,String siteName,String pageWebPath);

}
