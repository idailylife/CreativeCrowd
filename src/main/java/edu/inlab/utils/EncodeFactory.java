package edu.inlab.utils;

import java.io.UnsupportedEncodingException;
import java.security.*;

/**
 * Created by inlab-dell on 2016/5/4.
 */

public class EncodeFactory {
    public static String getEncodedString(String inStr){
        try{
            byte[] bytes = inStr.getBytes("UTF-8");
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] digest = messageDigest.digest(bytes);
            return new String(digest);
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
            return null;
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            return null;
        }
    }
}
