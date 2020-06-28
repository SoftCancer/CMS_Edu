package com.xuecheng.manager.service;

import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.request.QuerySiteRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;

/**
 * @Description:
 * @author: YaoGuangXun
 * @date: 2020/6/2612:19
 * @Version: 1.0
 */
public interface ICmsSiteService {

    public QueryResponseResult findSiteList(int page, int size, QuerySiteRequest querySiteRequest);


    public QueryResponseResult findAll();
}
