package com.xuecheng.framework.model.response;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class QueryResponseResult extends ResponseResult {

    QueryResult queryResult;

//    List<Object> list;

    public QueryResponseResult(ResultCode resultCode,QueryResult queryResult){
        super(resultCode);
       this.queryResult = queryResult;
    }

    public QueryResponseResult(ResultCode resultCode){
        super(resultCode);
    }

//    public QueryResponseResult(ResultCode resultCode, List<Object> list){
//        super(resultCode);
//       this.list = list;
//    }

}
