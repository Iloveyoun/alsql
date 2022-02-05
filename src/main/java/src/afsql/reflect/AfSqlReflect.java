package src.afsql.reflect;

import src.afsql.AfSqlContext;

import java.lang.reflect.Method;
import java.sql.Types;


public class AfSqlReflect {
    /**
     * 表名转类名
     * @param table 表名
     * @return 类名
     */
    public static String tableClass(String table) {
        String[] sss = table.split("[_-]");
        String className = "";
        for (String s : sss) {
            StringBuffer sb = new StringBuffer(s);
            char ch = sb.charAt(0);
            sb.setCharAt(0, Character.toUpperCase(ch));
            className += sb.toString();
        }
        return className;
    }

    /**
     * 属性名 - Getter名
     * @param field String
     * @return 转换后的String
     */
    public static String getter(String field) {
        // "name" -> "getName()"
        char firstChar = Character.toUpperCase(field.charAt(0));
        StringBuffer strbuf = new StringBuffer("get" + field);
        strbuf.setCharAt(3, firstChar);
        return strbuf.toString();
    }

    /**
     * 属性名 - Setter名
     * @param field String
     * @return 转换后的String
     */
    public static String setter(String field) {
        // "name" -> "setName()"
        char firstChar = Character.toUpperCase(field.charAt(0));
        StringBuffer strbuf = new StringBuffer("set" + field);
        strbuf.setCharAt(3, firstChar);
        return strbuf.toString();
    }

    public static Method findGetter(Class clazz, String field) {
        return getMethod(clazz, getter(field));
    }

    public static Method findSetter(Class clazz, String field) {
        return getMethod(clazz, setter(field));
    }

    /**
     * 在一个Class中按名称查找一个方法
     * @param clazz .class
     * @param name 方法名
     * @return 方法
     */
    public static Method getMethod(Class clazz, String name) {
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (name.equals(method.getName()))
                return method;
        }
        return null;
    }

    /**
     * 将SQL字段类型转成 Java类型的对象
     * 支持：
     * - 整数类型 Integer,Long, Short, Byte (INT, BIGINT, SMALLINT, TINYINT)
     * - 布尔类型 Boolean ( TINYINT(1), BOOL )
     * - 小数类型 Double, Float, ( DOUBLE, FLOAT, REAL)
     * - 文本类型 String (CHAR, VARCHAR, TEXT)
     * - 时间类型 Date ( DATE, DATETIME, TIMESTAMP)
     * @param c ..
     * @param type ..
     * @param value ..
     * @return 基本类型
     */
    public static Object typedValue(AfSqlContext c, int type, String value) {
        if (value == null) return null;

        // 根据类型，传递合适的参数给setter
        Object result = null;
        if (type == Types.CHAR || type == Types.VARCHAR) {
            result = value; // 给方法传一个String参数
        } else if (type == Types.LONGVARCHAR) { // text
            result = value;
        } else if (type == Types.BIT) { // tinyint(1)
            result = (value.equals("1"));
        } else if (type == Types.DATE) { // date
            try {
                result = c.dateFormat().parse(value);
            } catch (Exception e) {
                throw new RuntimeException("不能转换" + value + "为日期格式");
            }
        } else if (type == Types.TIME) { // time
            result = value; // 不转换
        } else if (type == Types.TIMESTAMP) { // datetime timestamp
            try {
                result = c.dateTimeFormat().parse(value);
            } catch (Exception e) {
                throw new RuntimeException("不能转换" + value + "为日期日间格式");
            }
        } else if (type == Types.REAL)  { // SQLite里的REAL类型,对应double或float
            result = Double.valueOf(value);
        } else if (type == Types.TINYINT) {
            result = Byte.valueOf(value);
        } else if (type == Types.TINYINT) {
            result = Short.valueOf(value);
        } else if (type == Types.INTEGER) {
            result = Integer.valueOf(value);
        } else if (type == Types.BIGINT) {
            result = Long.valueOf(value);
        } else if (type == Types.DOUBLE) {
            result = Long.valueOf(value);
        } else if (type == Types.FLOAT) {
            result = Long.valueOf(value);
        }

        return result;
    }

    /**
     * 将一个String类型，转成基础类型 Clazz
     *
     * @param clazz 目标类型 ：Integer,Long,Short,Byte,Boolean,Double,Float,String
     * @param value 字符串
     * @return 要的类型
     */
    public static Object typedValue(Class clazz, String value) {
        // Setter的参数值转换
        if (value == null) {
            return null;
        } else if (clazz.equals(String.class)) {
            return value;
        } else if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
            return Integer.valueOf(value);
        } else if (clazz.equals(Long.class) || clazz.equals(long.class)) {
            return Long.valueOf(value);
        } else if (clazz.equals(Short.class) || clazz.equals(short.class)) {
            return Short.valueOf(value);
        } else if (clazz.equals(Byte.class) || clazz.equals(byte.class)) {
            return Byte.valueOf(value);
        } else if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
            return new Boolean(!value.equals("0"));
        } else if (clazz.equals(Double.class) || clazz.equals(double.class)) {
            return Double.valueOf(value);
        } else if (clazz.equals(Float.class) || clazz.equals(float.class)) {
            return Double.valueOf(value);
        }

        return null;
    }

    /**
     * 设置POJO类的属性
     *
     * @param pojo  POJO对象实例
     * @param field 列名 (等同于类里的属性名)
     * @param value 列值 (String值)
     */
    public static void setPojo(Object pojo, String field, Object value) {
        Class clazz = pojo.getClass();
        // name->setName
        String setterName = setter(field);

        // setName()方法
        Method setter = getMethod(clazz, setterName);
        if (setter == null) {
            // return;
            throw new RuntimeException("POJO缺少Setter方法!" + clazz.getName() + "." + setterName + "()");
        }

        // 获取setter的第一个参数的类型
        Class[] parameterTypes = setter.getParameterTypes();
        Class pt = parameterTypes[0];

        // 检查传入的值的类型，是否与setter的参数类型一致
        if (value != null && !pt.equals(value.getClass()))
            throw new RuntimeException("POJO列值类型不匹配! " + clazz.getName() + "." + field);

        // 调用 Setter
        Object args[] = {value};
        try {
            setter.invoke(pojo, args);
        } catch (Exception e) {
            throw new RuntimeException("无法运行Setter来设置POJO!" + clazz.getName() + "." + setterName + "()");
        }

    }

    /**
     * 设置整型属性的值
     * @param pojo ..
     * @param setter ..
     * @param value ..
     * @throws Exception 错误
     */
    public static void setPojo(Object pojo, Method setter, String value) throws Exception {
        // Setter的参数类型
        Class[] parameterTypes = setter.getParameterTypes();
        Class c = parameterTypes[0];

        Object arg0 = typedValue(c, value);

        Object args[] = {arg0};
        try {
            if (arg0 != null) setter.invoke(pojo, args);
        } catch (IllegalArgumentException e) {
            //System.out.println("期望类型:" + c.getCanonicalName() + "，实际类型:" + arg0.getClass().getCanonicalName() );
        }
    }
}
