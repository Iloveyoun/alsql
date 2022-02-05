package src.afsql.config;

import src.afsql.datasource.AfSqlDataSource;
import src.afsql.paging.AfIPaging;
import src.afsql.paging.AfPaging;
import src.afsql.util.AfSqlUtil;

import javax.sql.DataSource;

/**
 * 定义所有对象
 */
public class AfSqlObjects {
    public static String configPath = "afsql.properties"; // 表示afsql配置文件地址
    private static DataSource dataSource;   // 连接池
    private static AfSqlConfig config;  // 配置信息

    ////连接池对象/////////////////////
    public static DataSource getDataSource() {
        if(dataSource == null){
            initDataSource();
        }
        return dataSource;
    }
    public static void setDataSource(DataSource dataSource1) {
        dataSource = dataSource1;
    }
    public synchronized static void initDataSource() {
        if(dataSource == null){
            AfSqlConfig c = getConfig();
            setDataSource(new AfSqlDataSource(c));
        }
    }

    ////配置信息对象/////////////////////
    public static AfSqlConfig getConfig() {
        if (config == null) {
            initConfig();
        }
        return config;
    }
    public static void setConfig(AfSqlConfig config1) {
        config = config1;
        if(config.getIsV()) {
            AfSqlUtil.printAfSql();
        }
    }
    public synchronized static void initConfig() {
        if (config == null) {
            setConfig(AfSqlConfigFactory.createConfig(configPath));
        }
    }

    private static AfIPaging paging;	 // 分页对象
    public static AfIPaging getPaging() {
        if(paging == null) {
            initPageing();
        }
        return paging;
    }
    public static void setPaging(AfIPaging paging) {
        AfSqlObjects.paging = paging;
    }
    public synchronized static void initPageing() {
        if(paging == null) {
            setPaging(new AfPaging());
        }
    }
}
