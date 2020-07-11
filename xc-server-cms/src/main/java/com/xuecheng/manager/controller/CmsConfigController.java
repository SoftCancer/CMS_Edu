package com.xuecheng.manager.controller;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.manager.service.ICmsConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @author: YaoGuangXun
 * @date: 2020/7/4 0:15
 * @Version: 1.0
 */
@RequestMapping("/cms/config")
@RestController
public class CmsConfigController {

    @Autowired
    private ICmsConfigService cmsConfigService;

    @GetMapping("/model/{id}")
    public CmsConfig getConfigModelById(@PathVariable String id){
      return cmsConfigService.findById(id);
    }
}
