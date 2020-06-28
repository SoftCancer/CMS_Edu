package com.xuecheng.framework.domain.cms.request;

import lombok.Data;

/**
 * @Description:
 * @author: YaoGuangXun
 * @date: 2020/6/26 12:33
 * @Version: 1.0
 */
@Data
public class QuerySiteRequest {

    //站点名称
    private String siteName;
    //站点名称
    private String siteDomain;
    //站点端口
    private String sitePort;
}
