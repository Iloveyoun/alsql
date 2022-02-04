package src.afsql;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * SQL列值转换器
 * 将String类型转成 Java基础类型
 */
public class AfSqlValueParser {

    // 时间转换
    private DateFormat sdf_dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private DateFormat sdf_d = new SimpleDateFormat("yyyy-MM-dd");
    private DateFormat sdf_t = new SimpleDateFormat("HH:mm:ss");

	/**
	 * 将str转换成Integer类型
	 * @param str 字符串
	 * @param defValue 字符串为Null或者转换出错，就返回这个默认值
	 * @return
	 */
    public Integer asInt(String str, Integer defValue) {
        if (str == null) return defValue;
        try {
            return Integer.valueOf(str);
        } catch (Exception e) {
            return defValue;
        }
    }

	/**
	 * 将str转换成Long类型
	 * @param str 字符串
	 * @param defValue 字符串为Null或者转换出错，就返回这个默认值
	 * @return
	 */
    public Long asLong(String str, Long defValue) {
        if (str == null) return defValue;
        try {
            return Long.valueOf(str);
        } catch (Exception e) {
            return defValue;
        }
    }

	/**
	 * 将str转换成Short类型
	 * @param str 字符串
	 * @param defValue 字符串为Null或者转换出错，就返回这个默认值
	 * @return
	 */
    public Short asShort(String str, Short defValue) {
        if (str == null) return defValue;
        try {
            return Short.valueOf(str);
        } catch (Exception e) {
            return defValue;
        }
    }

	/**
	 * 将str转换成Byte类型
	 * @param str 字符串
	 * @param defValue 字符串为Null或者转换出错，就返回这个默认值
	 * @return
	 */
    public Byte asByte(String str, Byte defValue) {
        if (str == null) return defValue;
        try {
            return Byte.valueOf(str);
        } catch (Exception e) {
            return defValue;
        }
    }

	/**
	 * 将str转换成Double类型
	 * @param str 字符串
	 * @param defValue 字符串为Null或者转换出错，就返回这个默认值
	 * @return
	 */
    public Double asDouble(String str, Double defValue) {
        if (str == null) return defValue;
        try {
            return Double.valueOf(str);
        } catch (Exception e) {
            return defValue;
        }
    }

	/**
	 * 将str转换成Float类型
	 * @param str 字符串
	 * @param defValue 字符串为Null或者转换出错，就返回这个默认值
	 * @return
	 */
    public Float asFloat(String str, Float defValue) {
        if (str == null) return defValue;
        try {
            return Float.valueOf(str);
        } catch (Exception e) {
            return defValue;
        }
    }

	/**
	 * 将str转换成Boolean类型
	 * @param str 字符串
	 * @param defValue 字符串为Null或者转换出错，就返回这个默认值
	 * @return
	 */
    public Boolean asBoolean(String str, Boolean defValue) {
        if (str == null) return defValue;
        try {
            return !str.equals("0");
        } catch (Exception e) {
            return defValue;
        }
    }

	/**
	 * 将str转换成String类型
	 * @param str 字符串
	 * @param defValue 字符串为Null或者转换出错，就返回这个默认值
	 * @return
	 */
    public String asString(String str, String defValue) {
        if (str == null) return defValue;
        return str;
    }

	/**
	 * 将str转换成Date类型
	 * @param str 字符串-日期+时间
	 * @param defValue 字符串为Null或者转换出错，就返回这个默认值
	 * @return
	 */
    public Date asDateTime(String str, Date defValue) {
        if (str == null) return defValue;
        try {
            return sdf_dt.parse(str);
        } catch (Exception e) {
            return defValue;
        }
    }

	/**
	 * 将str转换成Date类型
	 * @param str 字符串-日期
	 * @param defValue 字符串为Null或者转换出错，就返回这个默认值
	 * @return
	 */
    public Date asDate(String str, Date defValue) {
        if (str == null) return defValue;
        try {
            return sdf_d.parse(str);
        } catch (Exception e) {
            return defValue;
        }
    }
}
