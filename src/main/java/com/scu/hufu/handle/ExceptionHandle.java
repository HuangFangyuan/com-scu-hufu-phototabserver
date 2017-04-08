package com.scu.hufu.handle;

import com.scu.hufu.bean.Response;
import com.scu.hufu.exception.ServerExpection;
import com.scu.hufu.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by tianfei on 2017/4/7.
 */
@ControllerAdvice
public class ExceptionHandle {

    private final static Logger logger = LoggerFactory.getLogger(ExceptionHandle.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Response handle(Exception e) {
        if (e instanceof ServerExpection) {
            ServerExpection sE = (ServerExpection) e;
            return ResponseUtil.error(sE.getCode(), sE.getMessage());
        }else {
            logger.error("【系统异常】{}", e);
            return ResponseUtil.error(-1, "未知错误");
        }
    }



}
