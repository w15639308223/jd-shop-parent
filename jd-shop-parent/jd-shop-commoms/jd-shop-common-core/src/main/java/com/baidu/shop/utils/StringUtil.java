package com.baidu.shop.utils;

/**
 * @version V1.0
 * @ClassName IntelliJ IDEA
 * @author:王双全
 * @date: 2020/8/31 19:33
 */
public class StringUtil {
    //判断字符串类型不为空且不为null
    public static Boolean isNotEmpty(String str){

        return null != str && !"".equals(str);
    }

    //判断字符串类型为空或为null
    public static Boolean isEmpty(String str){

        return null == str || "".equals(str);
    }

    public  static  Integer toInteger(String str){
        if (isNotEmpty(str)) return Integer.parseInt(str);
        return 0;
    }
}
