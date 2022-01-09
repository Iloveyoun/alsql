package src.af.sql.config;

import src.af.sql.exception.AfSysException;
import src.af.sql.reflect.AfSqlReflect;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class AfSqlConfigFactory {
    /**
     * 初始化配置文件
     */
    public static AfSqlConfig createConfig(String configPath) {
        Map<String, String> map = readPropToMap(configPath);
        if(map == null){
            throw new AfSysException("找不到配置文件：" + configPath, null);
        }
        AfSqlConfig afSqlConfig = new AfSqlConfig();
        afSqlConfig.fromMap(map);
        return afSqlConfig;
    }

    private static Map<String, String> readPropToMap(String propertiesPath){
        Map<String, String> map = new HashMap<>();
        try {
            InputStream is = AfSqlConfigFactory.class.getClassLoader().getResourceAsStream(propertiesPath);
            if(is == null){
                return null;
            }
            Properties prop = new Properties();
            prop.load(is);
            for (String key : prop.stringPropertyNames()) {
                map.put(key, prop.getProperty(key));
            }
        } catch (IOException e) {
            throw new AfSysException("配置文件(" + propertiesPath + ")加载失败", e);
        }
        return map;
    }

    /**
     * 将 Map 的值映射到 Model 上
     * Params:
     * map – 属性集合
     * obj – 对象，或类型
     * Returns:
     * 返回实例化后的对象
     */
    private static Object initPropByMap(Map<String, String> map, Object obj){
        if(map == null){
            map = new HashMap<>();
        }

        // 1、取出类型
        Class<?> cs = null;
        if(obj instanceof Class){	// 如果是一个类型，则将obj=null，以便完成静态属性反射赋值
            cs = (Class<?>)obj;
            obj = null;
        }else{	// 如果是一个对象，则取出其类型
            cs = obj.getClass();
        }

        // 2、遍历类型属性，反射赋值
        for (Field field : cs.getDeclaredFields()) {
            String value = map.get(field.getName());
            if (value == null) {
                continue;	// 如果为空代表没有配置此项
            }
            try {
                Object valueConvert = AfSqlReflect.typedValue(field.getType(), value);	// 转换值类型
                field.setAccessible(true);
                field.set(obj, valueConvert);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new AfSysException("属性赋值出错：" + field.getName(), e);
            }
        }
        return obj;
    }
}
