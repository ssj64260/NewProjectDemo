package com.android.newprojectdemo.utils;

import java.text.DecimalFormat;

public class StringUtils {

    private static final DecimalFormat numFormat = new DecimalFormat("#0.0");

    //返回保留一位小数的字符串
    public static String getNumberString(double num) {
        return numFormat.format(num);
    }
}
