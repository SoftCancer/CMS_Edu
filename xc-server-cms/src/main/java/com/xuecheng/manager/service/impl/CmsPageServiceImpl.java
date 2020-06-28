package com.xuecheng.manager.service.impl;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manager.dao.CmsPageRepository;
import com.xuecheng.manager.service.ICmsPageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @Description: service 层实现类
 * @author: YaoGuangXun
 * @date: 2020/6/9 23:13
 * @Version: 1.0
 */
@Service
public class CmsPageServiceImpl implements ICmsPageService {

    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Override
    public QueryResponseResult findCmsList(int page, int size, QueryPageRequest queryPageRequest) {


        if (page < 0) {
            page = 0;
        }

        if (size < 0) {
            size = 10;
        }

        if (queryPageRequest == null) {
            queryPageRequest = new QueryPageRequest();
        }
        String siteId = queryPageRequest.getSiteId();
        String templateId = queryPageRequest.getTemplateId();
        String pageAliase = queryPageRequest.getPageAliase();

        // 条件值对象
        CmsPage cmsPage = new CmsPage();
        // 设置条件值 站点id
        if (StringUtils.isNotBlank(siteId)) {
            cmsPage.setSiteId(siteId);
        }

        if (StringUtils.isNotBlank(templateId)) {
            cmsPage.setTemplateId(templateId);
        }

        if (StringUtils.isNotBlank(pageAliase)) {
            cmsPage.setPageAliase(pageAliase);
        }

        // 自定义条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);

        Pageable pageable = PageRequest.of(page-1, size);
        Page<CmsPage> cmsPages = cmsPageRepository.findAll(example, pageable);

        // 优化封装结果
        QueryResult queryResult = new QueryResult();
        queryResult.setList(cmsPages.getContent());
        queryResult.setTotal(cmsPages.getTotalElements());
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }

    @Override
    public CmsPageResult addCms(CmsPage cmsPage) {
        String pageName = cmsPage.getPageName();
        String pageWebPath = cmsPage.getPageWebPath();
        String siteId = cmsPage.getSiteId();
        CmsPage cmsPage_1 = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(pageName,siteId,pageWebPath);
        if (cmsPage_1 != null){
            return new CmsPageResult(CommonCode.FAIL,"数据已存在！");
        }
        CmsPage cmsPageResult = cmsPageRepository.save(cmsPage);
        return new CmsPageResult(CommonCode.SUCCESS,cmsPageResult);
    }

    @Override
    public CmsPageResult findById(String id) {

        if (StringUtils.isEmpty(id)){
            return new CmsPageResult(CommonCode.FAIL,"数据不存在！");
        }
        Optional<CmsPage> optionalPage = cmsPageRepository.findById(id);
        if (optionalPage.isPresent()){
            CmsPage cmsPage = optionalPage.get();
            return new CmsPageResult(CommonCode.SUCCESS,cmsPage);
        }
        return new CmsPageResult(CommonCode.FAIL,"数据不存在!");
    }

    @Override
    public CmsPageResult editCmsPage(CmsPage cmsPage) {
        String pageId = cmsPage.getPageId();
        if (StringUtils.isEmpty(pageId)){
            return new CmsPageResult(CommonCode.FAIL,"数据不存在!");
        }

        Optional<CmsPage> optionalPage = cmsPageRepository.findById(pageId);
        if (optionalPage.isPresent()){
            CmsPage cmsPageResult = cmsPageRepository.save(cmsPage);
            return new CmsPageResult(CommonCode.SUCCESS,cmsPageResult);
        }
        return new CmsPageResult(CommonCode.FAIL,"数据不存在!");
    }

    @Override
    public ResponseResult deleteById(String id) {
        Optional<CmsPage> optionalPage = cmsPageRepository.findById(id);
        if (optionalPage.isPresent()){
            cmsPageRepository.deleteById(id);
            return ResponseResult.SUCCESS();
        }
        return ResponseResult.FAIL();
    }
}
