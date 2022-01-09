package src.af.sql.config;

import com.fly.jdbc.cfg.FlyObjects;
import com.fly.jdbc.paging.FlyPaging;
import com.fly.jdbc.paging.FlyPagingMysql;
import src.af.sql.datasource.AfSqlDataSource;
import src.af.sql.paging.AfPaging;
import src.af.sql.paging.AfPagingMySql;
import src.af.sql.util.AfSqlUtil;

import javax.sql.DataSource;

/**
 * @title 定义所有对象
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

    private static AfPaging paging;	 // 分页对象
    public static AfPaging getPaging() {
        if(paging == null) {
            initPageing();
        }
        return paging;
    }
    public static void setPaging(AfPaging paging) {
        AfSqlObjects.paging = paging;
    }
    public synchronized static void initPageing() {
        if(paging == null) {
            setPaging(new AfPagingMySql());
        }
    }
}
