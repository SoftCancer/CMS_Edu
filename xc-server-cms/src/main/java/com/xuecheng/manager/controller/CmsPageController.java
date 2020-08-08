package com.xuecheng.manager.controller;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manager.service.ICmsPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description:
 * @author: YaoGuangXun
 * @date: 2020/6/9 23:31
 * @Version: 1.0
 */
@RequestMapping("/cms/page")
@RestController
public class CmsPageController {

    @Autowired
    private ICmsPageService cmsPageService;

    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult queryCmsPageAll(@PathVariable("page") int page, @PathVariable("size") int size, QueryPageRequest queryPageRequest){
          return cmsPageService.findCmsList(page,size,queryPageRequest);
    }

    @PostMapping("/add")
    public CmsPageResult addCms(@RequestBody CmsPage cmsPage){
          return cmsPageService.addCms(cmsPage);
    }

    @GetMapping("/get/{pageId}")
    public CmsPageResult findById(@PathVariable("pageId") String pageId){
        return cmsPageService.findById(pageId);
    }

    @PutMapping("/edit")
    public CmsPageResult editCms(@RequestBody CmsPage cmsPage){
          return cmsPageService.editCmsPage(cmsPage);
    }

    @DeleteMapping("/delete/{pageId}")
    public ResponseResult editCms(@PathVariable("pageId") String pageId){
          return cmsPageService.deleteById(pageId);
    }

    /**
     * 发布 HTML 页面
     * @Author: YaoGX
     * @Date: 2020/8/8 23:36
     **/
    @PostMapping("/postPage/{pageId}")
    public ResponseResult postPageToMQ(@PathVariable("pageId") String pageId){
        return cmsPageService.postPageToMongoDB(pageId);
    }

}
