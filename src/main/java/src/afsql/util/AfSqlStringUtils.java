package src.afsql.util;

import java.util.Date;
import java.util.regex.Pattern;

/**
 * String工具类
 * @title 工具类
 */
public class AfSqlStringUtils {
    /**
     * 判断是否是数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * null或者""
     * @param trim
     * @return
     */
    public static boolean isNotEmpty(String trim) {
        return trim != null && trim.length() != 0;
    }

    /**
     * @param str
     * @param defaultString
     * @return
     */
    public static String defaultIfEmpty(String str, String defaultString) {
        if (str == null) {
            return defaultString;
        } else {
            return str.trim().length() == 0 ? " " : str;
        }
    }

    /**
     * @param str
     * @param defaultBoolean
     * @return
     */
    public static Boolean strToBoolean(String str, Boolean defaultBoolean) {
        if (str == null) {
            return defaultBoolean;
        } else {
            return Boolean.parseBoolean(str);
        }
    }

    public static String toString(Object obj) {
        if (obj == null) {
            return null;
        } else {
            return obj.toString();
        }
    }
}
