package com.xuecheng.manager.service;

import com.xuecheng.framework.domain.cms.CmsConfig;

/**
 * @Description:
 * @author: YaoGuangXun
 * @date: 2020/7/4 0:08
 * @Version: 1.0
 */
public interface ICmsConfigService {

    /**
     * @Description: 根据id 查询 页面配置信息
     * @Author: YaoGX
     * @Date: 2020/7/4 0:09
     **/
    public CmsConfig findById(String id);
}
