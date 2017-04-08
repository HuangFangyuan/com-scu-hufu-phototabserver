package com.scu.hufu.aspect;

import com.scu.hufu.enums.ResponseEnum;
import com.scu.hufu.exception.ServerExpection;
import com.scu.hufu.util.Encrypted;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import javax.servlet.http.HttpServletRequest;

import static com.scu.hufu.util.Predictions.checkNotNull;

/**
 * Created by tianfei on 2017/4/7.
 */
@Aspect
@Component
public class HttpAspect {

    private static Logger logger= LoggerFactory.getLogger(HttpAspect.class);

    //指定所有的Controller为Header验证点
    @Pointcut("execution(public * com.scu.hufu.controller.*.*(..))")
    public void checkHeaderPoint(){}

    //指定所有登录为验证token的切入点
    @Pointcut("execution(public * com.scu.hufu.controller.UserController.token*(..))")
    public void dologin(){}



    //所有的用户自动登录前先进行token鉴权
    @Before("dologin()")
    public void checkToken(JoinPoint joinPoint){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        //记录登录请求
        log(joinPoint,attributes);

        //检验token
    }

    //对所有的HTTP请求进行Header验证
    @Before("checkHeaderPoint()")
    public void checkHeader(JoinPoint joinPoint) throws Exception{
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request=attributes.getRequest();

        //记录日志
        log(joinPoint,attributes);

        //获取Header参数  包括：AppKey, Content-Type, nonce, curTime, checkSum, Signature
        //URL签名算法步骤如下：
        //1. 将所有参数按参数名进行升序排序；
        //2. 将排序后的参数名和值拼接成字符串stringParams，格式：key1value1key2value2...；
        //3. 在上一步的字符串前面拼接上请求URI的Endpoint，字符串后面拼接上AppSecret，即：stringURI + stringParams + AppSecret；
        //4. 使用AppSecret为密钥，对上一步的结果字符串使用HMAC算法计算MAC值，这个MAC值就是签名。这里使用HMAC_SHA256
        String appKey=checkNotNull(request.getHeader("AppKey"), ResponseEnum.BAD_REQUEST);
        String nonce=checkNotNull(request.getHeader("nonce"),ResponseEnum.BAD_REQUEST);
        String curTime=checkNotNull(request.getHeader("curTime"),ResponseEnum.BAD_REQUEST);
        String checkSum=checkNotNull(request.getHeader("checkSum"),ResponseEnum.BAD_REQUEST);
        String content_type=checkNotNull(request.getHeader("Content-Type"),ResponseEnum.BAD_REQUEST);

        //比较curTime，如果超出5秒忽略这条请求
        long serverTime=System.currentTimeMillis()/1000;
        if (serverTime-Long.parseLong(curTime)>5){
            throw new ServerExpection(ResponseEnum.OVER_TIME_REQUEST);
        }

        StringBuffer buffer=new StringBuffer();
        //url
        buffer.append(request.getRequestURI());
        //params
        buffer.append(appKey);
        //buffer.append(checkSum);
        buffer.append(content_type);
        buffer.append(curTime);
        buffer.append(nonce);

        //查询数据库获取AppSecret
        //getAppSecret


        //加密
        Encrypted.HMAC_SHA256(buffer.toString(),"temp");

        //比较加密结果和checksum


    }


    private void log(JoinPoint point,ServletRequestAttributes attributes){

       // ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        //url
        logger.info("url={}", request.getRequestURL());

        //method
        logger.info("method={}", request.getMethod());

        //ip
        logger.info("ip={}", request.getRemoteAddr());

        //类方法
        logger.info("class_method={}", point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName());

        //参数
        logger.info("args={}", point.getArgs());
    }


}
