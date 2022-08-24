package src.afsql.util;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String工具类
 */
public class AfSqlStringUtils {
    /**
     * 判断是否是数字
     * @param str 字符串
     * @return true/false
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断String是否为 null或者""
     * @param trim 字符串
     * @return true/false
     */
    public static boolean isNotEmpty(String trim) {
        return trim != null && trim.length() != 0;
    }

    /**
     * 去除String的空格，如果为Null，返回默认值
     * @param str 字符串
     * @param defaultString 默认值
     * @return 字符串
     */
    public static String defaultIfEmpty(String str, String defaultString) {
        if (str == null) {
            return defaultString;
        } else {
            return str.trim().length() == 0 ? " " : str;
        }
    }

    /**
     * 把String转成Boolean,
     * @param str 字符串
     * @param defaultBoolean 默认值
     * @return 字符串
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

    private static Pattern linePattern = Pattern.compile("_(\\w)");

    /**
     * 下划线转驼峰
     * @param str
     * @return
     */
    public static String lineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static Pattern humpPattern = Pattern.compile("[A-Z]");
    /**
     * 驼峰转下划线
     * @param str
     * @return
     */
    public static String humpToLine(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
