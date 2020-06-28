package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResultCode;
import net.bytebuddy.implementation.bytecode.Throw;

/**
 * @Description: 自定义异常
 * @author: YaoGuangXun
 * @date: 2020/6/28 17:16
 * @Version: 1.0
 */
public class CustomException extends RuntimeException {

    private ResultCode resultCode;

    public CustomException(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }


    /**
     * @Description: 异常抛出，封装 CustomException 自定义异常
     * @Author: YaoGX
     * @Date: 2020/6/28 17:31 
     **/
    public static void throwException(ResultCode resultCode){
        throw new CustomException(resultCode);
    }

}
