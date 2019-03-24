package com.horner.xsm.utils;


public class StringUtils {
    public static boolean isEmpty(String str) {
        if ("".equals(str) || str == null) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNotEmpty(String str) {
        return str != null && (!"".equals(str));
    }

    public static String join(String[] arr, String sep) {
        if (arr == null) {
            return null;
        }
        if (arr.length == 0) {
            return "";
        }
        if (sep == null) {
            sep = "";
        }
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for (String string : arr) {
            if (i != 0) {
                builder.append(sep);
            }
            builder.append(string);
            i++;
        }
        return builder.toString();
    }

    public static String join(String[] arr) {
        return join(arr, ",");
    }
}
