package com.scu.hufu.util;
import java.security.MessageDigest;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by tianfei on 2017/4/8.
 */
public class Encrypted {


    public static String HMAC_SHA256(final String strText,final String key)throws Exception{
        // 返回值
        String strResult = null;

        // 是否是有效字符串
        if (strText != null && strText.length() > 0) {

            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(keySpec);

            byte byteBuffer[] = mac.doFinal(strText.getBytes());

            strResult=getStrFromByte(byteBuffer);
        }
        return strResult;
    }


    public static String SHA_MD5(final String strText,final String cryptoType) throws Exception{

        // 返回值
        String strResult = null;

        // 是否是有效字符串
        if (strText != null && strText.length() > 0)
        {
                // SHA 加密开始
                // 创建加密对象,传入要加密的类型
                MessageDigest messageDigest = MessageDigest.getInstance(cryptoType);
                // 传入要加密的字符串
                messageDigest.update(strText.getBytes());
                // 得到 byte 返回结果
                byte[] byteBuffer = messageDigest.digest();


                strResult = getStrFromByte(byteBuffer);
            }

        return strResult;
    }

    private static String getStrFromByte(byte[] byteBuffer){

        // byte转string
        StringBuffer strHexString = new StringBuffer();

        for (int i = 0; i < byteBuffer.length; i++)
        {
            String hex = Integer.toHexString(0xff & byteBuffer[i]);
            if (hex.length() == 1)
            {
                strHexString.append('0');
            }
            strHexString.append(hex);
        }

        return strHexString.toString();
    }

}

