package com.xuecheng.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description: 捕获异常类
 * @author: YaoGuangXun
 * @date: 2020/6/28 17:33
 * @Version: 1.0
 */
// 控制器增强
@ControllerAdvice
public class CatchException {

    private static final Logger logger = LoggerFactory.getLogger(CatchException.class);

    // 定义 map，配置常见的异常类型
    private static ImmutableMap<Class<? extends Throwable>,ResultCode> EXCEPTIONTYPEMAP;

    // 定义Map 的 builder 的对象，去构建ImmutableMap
    protected static ImmutableMap.Builder<Class<? extends Throwable>,ResultCode> builder = ImmutableMap.builder();

    // 捕获指定类型的异常,可预知异常
    @ResponseBody  // 把对象转化成json
    @ExceptionHandler(CustomException.class)
    public ResponseResult catchExc(CustomException customException){
        // 打印异常日志
        logger.error("catch exception:{}", customException.getMessage());
        return  new ResponseResult(customException.getResultCode());
    }


    // 捕获指定类型的异常,可预知异常
    @ResponseBody  // 把对象转化成json
    @ExceptionHandler(Exception.class)
    public ResponseResult catchException(Exception exception){
        // 打印异常日志
        logger.error("catch exception:{}", exception.getMessage());
        if (EXCEPTIONTYPEMAP == null){
            // EXCEPTIONTYPEMAP 构建成功，一旦构建成功且不可修改
            EXCEPTIONTYPEMAP = builder.build();
        }

        // 根据异常类 查询是否为静态方法块 中定义的异常类型
        ResultCode resultCode = EXCEPTIONTYPEMAP.get(exception.getClass());
        if (null != resultCode){
            new ResponseResult(resultCode);
        }
        return  new ResponseResult(CommonCode.SERVER_ERROR);
    }


/**
 * @Description: 定义 不可知异常
 * @Author: YaoGX
 * @Date: 2020/6/28 22:23
 **/
static {
        builder.put(HttpMessageNotReadableException.class,CommonCode.ILLEGAL_PARAMETER);
        builder.put(NullPointerException.class,CommonCode.NULL_POINTER);
}



}
