package edu.inlab.utils;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.Random;

/**
 * Created by inlab-dell on 2016/5/4.
 */

public class EncodeFactory {
    public static String SALT_PREFIX = "whoknows";

    /**
     * Generate random salt according to SALT_PREFIX and current time
     * @return
     */
    public static String getSalt(){
        String timestampStr = SALT_PREFIX + String.valueOf(System.currentTimeMillis());
        Random random = new Random();
        timestampStr += random.nextInt();
        return timestampStr;
    }

    public static String getEncodedString(String str){
        String reStr = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(str.getBytes());
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : bytes){
                int bt = b&0xff;
                if (bt < 16){
                    stringBuffer.append(0);
                }
                stringBuffer.append(Integer.toHexString(bt));
            }
            reStr = stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return reStr;
    }
}
