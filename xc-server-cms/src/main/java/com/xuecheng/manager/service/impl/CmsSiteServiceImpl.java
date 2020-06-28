package com.xuecheng.manager.service.impl;

import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.request.QuerySiteRequest;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResultUtils;
import com.xuecheng.manager.dao.CmsSiteRepository;
import com.xuecheng.manager.service.ICmsSiteService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @author: YaoGuangXun
 * @date: 2020/6/26 12:20
 * @Version: 1.0
 */
@Service
public class CmsSiteServiceImpl implements ICmsSiteService {

    @Autowired
    private CmsSiteRepository cmsSiteRepository;

    @Override
    public QueryResponseResult findSiteList(int page, int size, QuerySiteRequest querySiteRequest) {

        if (null == querySiteRequest){
            querySiteRequest = new QuerySiteRequest();
        }

        if (page <0){
            page = 0;
        }
        if (size<0){
            size = 10;
        }

        String siteName = querySiteRequest.getSiteName();
        String siteDomain = querySiteRequest.getSiteDomain();
        String sitePort = querySiteRequest.getSitePort();

        CmsSite cmsSite = new CmsSite();
        if (StringUtils.isNotBlank(siteName)){
            cmsSite.setSiteName(siteName);
        }
        if (StringUtils.isNotBlank(siteDomain)){
            cmsSite.setSiteDomain(siteDomain);
        }
        if (StringUtils.isNotBlank(sitePort)){
            cmsSite.setSitePort(sitePort);
        }
        // 模糊查询
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withMatcher("siteName",ExampleMatcher.GenericPropertyMatchers.contains());
        Example<CmsSite> example = Example.of(cmsSite,exampleMatcher);

        Pageable pageable = PageRequest.of(page,size);
        Page<CmsSite> cmsSitePage = cmsSiteRepository.findAll(example,pageable);
        return ResponseResultUtils.successResult(cmsSitePage);
    }

    @Override
    public QueryResponseResult findAll() {
       List<CmsSite> cmsSiteList = cmsSiteRepository.findAll();
        return ResponseResultUtils.successResult(cmsSiteList);
    }
}
