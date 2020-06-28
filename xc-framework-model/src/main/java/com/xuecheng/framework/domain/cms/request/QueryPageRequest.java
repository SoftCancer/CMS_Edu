package com.xuecheng.framework.domain.cms.request;

import lombok.Data;

/**
 * @Description:
 * @author: YaoGuangXun
 * @date: 2020/6/7 21:22
 * @Version: 1.0
 */
@Data
public class QueryPageRequest {

    /**
     *  站点id
     **/
    private String siteId;

    /**
     *  页面id
     **/
    private String pageId;

    /**
     *  页面名称
     **/
    private String pageName;

    /**
     *  别名
     **/
    private String pageAliase;

    /**
     *  模板id
     **/
    private String templateId;

}
