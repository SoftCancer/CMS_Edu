package com.xuecheng.manager.service.impl;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.model.response.ResponseResultUtils;
import com.xuecheng.manager.dao.CmsConfigRepository;
import com.xuecheng.manager.service.ICmsConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.OpenOption;
import java.util.Optional;

/**
 * @Description:
 * @author: YaoGuangXun
 * @date: 2020/7/4 0:09
 * @Version: 1.0
 */
@Service
public class CmsConfigImpl implements ICmsConfigService {

    @Autowired
    private CmsConfigRepository cmsConfigRepository;

    @Override
    public CmsConfig findById(String id) {
       Optional<CmsConfig> optionalCmsConfig = cmsConfigRepository.findById(id);
       if (optionalCmsConfig.isPresent()){
         CmsConfig cmsConfig = optionalCmsConfig.get();
         return cmsConfig;
       }
        return null;
    }
}
