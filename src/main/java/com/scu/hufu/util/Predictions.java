package com.scu.hufu.util;

import com.scu.hufu.enums.ResponseEnum;
import com.scu.hufu.exception.ServerExpection;
import com.sun.istack.internal.Nullable;

/**
 * Created by tianfei on 2017/4/7.
 */
public final class Predictions {


    public static <T> T checkNotNull(T reference){
        if(reference == null) {
            throw new ServerExpection("NullObjectException");
        } else {
            return reference;
        }
    }

    public static <T> T checkNotNull(T reference, @Nullable ResponseEnum error) {
        if(reference == null) {
            throw new ServerExpection(error);
        } else {
            return reference;
        }
    }

}
