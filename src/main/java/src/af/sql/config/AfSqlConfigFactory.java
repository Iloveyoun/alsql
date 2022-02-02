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
        return new AfSqlConfig().fromMap(map);
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
}
