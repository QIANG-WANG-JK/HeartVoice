package com.hv.heartvoice.Util;

public class StringUtil {

    public static boolean isPhone(String value){
        return value.length() == 11 ? true : false;
    }


    public static boolean isPassword(String password) {
        return password.length() > 6 ? true : false;
    }

    public static boolean isCode(String code) {
        return code.length() == 4 ? true : false;
    }
}
