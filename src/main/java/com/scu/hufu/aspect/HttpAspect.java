package com.scu.hufu.aspect;

import com.scu.hufu.bean.AppKeyValue;
import com.scu.hufu.enums.ResponseEnum;
import com.scu.hufu.exception.ServerExpection;
import com.scu.hufu.repository.AppKeyValueRepository;
import com.scu.hufu.util.Encrypted;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final String CONTENT_TYPE="application/x-www-form-urlencoded";

    @Autowired
    private AppKeyValueRepository keyValueRepository;


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

        //获取Header参数  包括：AppKey, nonce, curTime, checkSum, Signature
        //URL签名算法步骤如下：
        //1. 将所有参数按参数名进行升序排序；
        //2. 将排序后的参数名和值拼接成字符串stringParams，格式：key1value1key2value2...；
        //3. 在上一步的字符串前面拼接上请求URI的Endpoint，字符串后面拼接上AppSecret，即：stringURI + stringParams
        //4. 使用AppSecret为密钥，对上一步的结果字符串使用HMAC算法计算MAC值，这个MAC值就是签名。这里使用HMAC_SHA256
        String appKey=checkNotNull(request.getHeader("AppKey"), ResponseEnum.BAD_REQUEST);
        String nonce=checkNotNull(request.getHeader("nonce"),ResponseEnum.BAD_REQUEST);
        String curTime=checkNotNull(request.getHeader("curTime"),ResponseEnum.BAD_REQUEST);
        String checkSum=checkNotNull(request.getHeader("checkSum"),ResponseEnum.BAD_REQUEST);

        String content_type=checkNotNull(request.getHeader("Content-Type"),ResponseEnum.BAD_REQUEST);

        //content_type不符合
        if (!CONTENT_TYPE.equals(content_type)){
            throw  new ServerExpection(ResponseEnum.UNKNOWN_CONTENT_TYPE);
        }

        //nonce过长
        if (nonce.length()>255){
            throw  new ServerExpection(ResponseEnum.BAD_REQUEST);
        }

        //比较curTime，如果超出5秒忽略这条请求
        long serverTime=System.currentTimeMillis()/1000;
        if (serverTime-Long.parseLong(curTime)>5){
            throw new ServerExpection(ResponseEnum.OVER_TIME_REQUEST);
        }

        //检验nonce是否缺失随机性，抛出"禁止重放"异常
        //未完成之前只检验是否存在nonce即可
        //待完成

        /*
        *
        * 检验nonce是否缺失随机性，抛出"禁止重放"异常
        *
        *
        * */



        StringBuffer buffer=new StringBuffer();
        //url
        buffer.append(request.getRequestURI());
        //params
        buffer.append("AppKey").append(appKey)
                .append("curTime").append(curTime)
                .append("nonce").append(nonce);

        //查询数据库获取AppSecret
        AppKeyValue keyValue=keyValueRepository.findOne(appKey);

        //使用getAppSecret加密
        String _checkSum=Encrypted.HMAC_SHA256(buffer.toString(),keyValue.getAppSecret());

        //比较加密结果和checksum
        if (!checkSum.equals(_checkSum)){
            throw new ServerExpection(ResponseEnum.WRONG_CHECKSUM);
        }

        //只用通过了以上的验证才能进入Controller的相关API请求
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
