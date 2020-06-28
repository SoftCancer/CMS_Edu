package com.xuecheng.manager.controller;

import com.xuecheng.framework.domain.cms.request.QuerySiteRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.manager.service.ICmsSiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @author: YaoGuangXun
 * @date: 2020/6/26 20:53
 * @Version: 1.0
 */
@RequestMapping("cms/site")
@RestController
public class CmsSiteController {

    @Autowired
    private ICmsSiteService cmsSiteService;

    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findSiteList(@PathVariable int page, @PathVariable int size, QuerySiteRequest querySiteRequest) {
        return cmsSiteService.findSiteList(page, size, querySiteRequest);
    }

    @GetMapping("/list/all")
    public QueryResponseResult findSiteList() {
        return cmsSiteService.findAll();
    }
}
