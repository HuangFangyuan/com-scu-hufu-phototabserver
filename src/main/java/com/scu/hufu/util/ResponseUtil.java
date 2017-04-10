package com.scu.hufu.util;

import com.scu.hufu.bean.Response;
import com.scu.hufu.enums.ResponseEnum;

/**
 * Created by tianfei on 2017/4/7.
 */
public class ResponseUtil {

    public static Response success(Object object) {
        Response result = new Response();
        result.setCode(200);
        result.setMessage("success");
        result.setData(object);
        return result;
    }

    public static Response success() {
        return success(null);
    }

    public static Response error(Integer code,String msg) {
        Response result = new Response();
        result.setCode(code);
        result.setMessage(msg);
        return result;
    }

    public static Response error(ResponseEnum e){
        Response result = new Response();
        result.setCode(e.getCode());
        result.setMessage(e.getMsg());
        return result;
    }

}
