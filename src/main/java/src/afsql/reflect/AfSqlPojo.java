package src.afsql.reflect;

import src.afsql.annotation.AFCOLUMNS;
import src.afsql.annotation.AFTABLE;
import src.afsql.config.AfSqlObjects;
import src.afsql.util.AfSqlStringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * POJO类信息，内部含有与表的对应关系
 * tableName 表名
 * generatedKey 自增主键
 * columns 每一列的定义
 */
public class AfSqlPojo {
    public String tableName; // 表名
    public Class clazz;
    public String clazzName;  // 类名

    public List<AfSqlColumn> columns = new ArrayList<>();
    public String generatedKey; // 自增主键的列名

    public Map<String, Method> getters = new HashMap<>();
    public Map<String, Method> setters = new HashMap<>();

    // 查找 Getter/Setter方法
    public Method findGetter(String field) {
        return getters.get(field);
    }

    public Method findSetter(String field) {
        return setters.get(field);
    }

    // 从类信息中，解析出映射关系
    public void parse(Class clazz) throws Exception {
        this.clazzName = clazz.getName(); // name
        this.clazz = clazz;

        if (clazz.isAnnotationPresent(AFTABLE.class)) {
            // 如果类中有AFTABLE 注解，则根据注解来解析
            parseWithAnnotation(clazz);
        } else {
            // 该类没有AFTABLE注解，当只当作普通类操作，只得到getter和setter
            //throw new Exception("类" + clazz.getName() + "里没有AFTABLE注解!");
            parseWithoutAnnotation(clazz);
        }
    }

    private void parseWithAnnotation(Class clazz) {
        // 提取 AFTABLE 注解，得到表名
        AFTABLE aftable = (AFTABLE) clazz.getAnnotation(AFTABLE.class);
        this.tableName = aftable.name();

        // 提取 AFCOLUMNS 注解，检查有没有自增主键
        String generated = "";
        if (clazz.isAnnotationPresent(AFCOLUMNS.class)) {
            AFCOLUMNS a = (AFCOLUMNS) clazz.getAnnotation(AFCOLUMNS.class);
            generated = a.generated();
        }

        // 列名 <-> 字段名 自动匹配
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            AfSqlColumn p = new AfSqlColumn();
            columns.add(p);

            // 提取出每一列，并转为下划线方式
            p.name = field.getName();
            p.humpName = AfSqlObjects.getConfig().getHumpToline() ? AfSqlStringUtils.humpToLine(field.getName()) : field.getName();
            // 判断该字段是否为主键
            if (p.name.equals(generated)) {
                this.generatedKey = generated;
            }

            getters.put(p.name, AfSqlReflect.findGetter(clazz, p.name));
            setters.put(p.name, AfSqlReflect.findSetter(clazz, p.name));
        }
    }


    // 即使类里没有注解，也可以解析, 但无法得到表名
    private void parseWithoutAnnotation(Class clazz) {
        // 列名 <-> 字段名 自动匹配
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            AfSqlColumn p = new AfSqlColumn();
            this.columns.add(p);

            // 提取出每一列
            p.name = field.getName();
            p.humpName = AfSqlObjects.getConfig().getHumpToline() ? AfSqlStringUtils.humpToLine(field.getName()) : field.getName();

            getters.put(p.name, AfSqlReflect.findGetter(this.clazz, p.name));
            setters.put(p.name, AfSqlReflect.findSetter(this.clazz, p.name));
        }
    }

    // 缓存 POJO Class信息
    public static Map<String, AfSqlPojo> clazzMap = new HashMap<>();

    // 先从缓存里查找，如果没有的话，则提取这个Class信息
    public static AfSqlPojo from(Class clazz) throws Exception {
        String clazzName = clazz.getName();

        AfSqlPojo po = clazzMap.get(clazzName);
        if (po == null) {
            po = new AfSqlPojo();
            po.parse(clazz);
        }

        return po;
    }
}
