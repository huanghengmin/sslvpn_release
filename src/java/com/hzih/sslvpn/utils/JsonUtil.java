package com.hzih.sslvpn.utils;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-12
 * Time: 上午9:47
 * To change this template use File | Settings | File Templates.
 */
public class JsonUtil {
    public static  Object checkNull(Object o){
        if(null!=o){
            return o;
        } else {
            return "";
        }
    }
}
