package com.xuecheng.framework.model.response;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @Description:
 * @author: YaoGuangXun
 * @date: 2020/6/26 16:52
 * @Version: 1.0
 */
public class ResponseResultUtils {

    /**
     * @Description: 成功响应
     * @Author: YaoGX
     * @Date: 2020/6/26 17:26
     **/
    public static QueryResponseResult successResult(Page page) {
        QueryResult queryResult = new QueryResult();
        queryResult.setTotal(page.getTotalElements());
        queryResult.setList(page.getContent());
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }

    /**
     * @Description: 成功响应
     * @Author: YaoGX
     * @Date: 2020/6/26 17:26
     **/
    public static QueryResponseResult successResult(List list) {
        QueryResult queryResult = new QueryResult();
        queryResult.setList(list);
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }


    /**
     * 失败响应
     * @Description:
     * @Author: YaoGX
     * @Date: 2020/6/26 17:28
     **/
    public static QueryResponseResult errorResult() {
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.FAIL);
        return queryResponseResult;
    }
}
