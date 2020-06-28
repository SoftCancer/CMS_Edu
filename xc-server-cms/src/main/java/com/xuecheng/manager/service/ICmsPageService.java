package com.xuecheng.manager.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;

/**
 * @Description: 对 进行分页查询
 * @author: YaoGuangXun
 * @date: 2020/6/923:11
 * @Version: 1.0
 */
public interface ICmsPageService {

    /**
     * 分页条件查询
     * @Description:
     * @Author: YaoGX
     * @Date: 2020/6/27 21:36
     **/
    public QueryResponseResult findCmsList(int page, int size, QueryPageRequest queryPageRequest);

    /**
     * 新增页面
     * @Author: YaoGX
     * @Date: 2020/6/21 15:35 
     **/
    public CmsPageResult addCms(CmsPage cmsPage);

    /**
     * 根据id查询页面
     * @Author: YaoGX
     * @Date: 2020/6/21 15:35
     **/
    public CmsPageResult findById(String id);

    /**
     * 修改页面
     * @Author: YaoGX
     * @Date: 2020/6/21 15:35
     **/
    public CmsPageResult editCmsPage(CmsPage cmsPage);


    /**
     * 删除页面
     * @Author: YaoGX
     * @Date: 2020/6/21 15:35
     **/
    public ResponseResult deleteById(String id);

}
