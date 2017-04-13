package com.scu.hufu.config;

import com.google.gson.Gson;
import com.scu.hufu.enums.ResponseEnum;
import com.scu.hufu.util.ResponseUtil;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by tianfei on 2017/4/12.
 */

public class RequestFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest;

        try {
            httpRequest=(HttpServletRequest)servletRequest;
        }catch (Exception e){
            errReturn(servletResponse,ResponseEnum.ONLY_HTTP_ALLOWED);
            return;
        }

        if (!"POST".equals(httpRequest.getMethod())){
           errReturn(servletResponse,ResponseEnum.ONLY_POST_SUPPORTED);
           return;
        }

        String url=httpRequest.getRequestURL().toString();

        if (url.contains(".action")){
            filterChain.doFilter(servletRequest,servletResponse);
        }else {
            errReturn(servletResponse,ResponseEnum.BAD_URL_FORMAT);
            return;
        }

    }

    public void errReturn(ServletResponse servletResponse,ResponseEnum e) throws  IOException{
        servletResponse.setContentType("application/json;charset=utf-8");
        String res=new Gson().toJson(ResponseUtil.error(e));
        servletResponse.getWriter().append(res);
    }

}
