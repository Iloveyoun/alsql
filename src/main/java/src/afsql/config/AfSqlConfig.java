package src.afsql.config;

import src.afsql.util.AfSqlStringUtils;

import java.util.Map;

/**
 * 定义所有配置
 */
public class AfSqlConfig {

    // 连接池配置
    private String driverClassName;
    private String url;
    private String username;
    private String password;

    private Boolean ispool = false; // 是否使用连接池，其值若为false，则代表不再使用连接池
    private int init = 10; // 初始化连接数
    private int min = 5; // 最小链接数
    private int max = 20; // 最大连接数
    private int poolTimeToWait = 3000; // 获取连接最大等待时间

    // 运行配置
    private Boolean printSql = false; // 是否在控制台输出每次执行的SQL
    private String sqlhh = "**[SQL] "; // 输出SQL的前缀

    private int defaultLimit = 1000; // Page == null时默认取出的数据量
    private Boolean isV = true; // 是否在初始化配置时打印版本字符画

    private int transactionIsolation;   // 事务隔离级别
    private Boolean isHumpToline;  // 是否开启下划线跟驼峰互转

    public String getDriverClassName() {
        return driverClassName;
    }
    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Boolean getIspool() {
        return ispool;
    }
    public void setIspool(Boolean ispool) {
        this.ispool = ispool;
    }
    public int getInit() {
        return init;
    }
    public void setInit(int init) {
        this.init = init;
    }
    public int getMin() {
        return min;
    }
    public void setMin(int min) {
        this.min = min;
    }
    public int getMax() {
        return max;
    }
    public void setMax(int max) {
        this.max = max;
    }
    public Boolean getPrintSql() {
        return printSql;
    }
    public void setPrintSql(Boolean printSql) {
        this.printSql = printSql;
    }
    public String getSqlhh() {
        return sqlhh;
    }
    public void setSqlhh(String sqlhh) {
        this.sqlhh = sqlhh;
    }
    public int getDefaultLimit() {
        return defaultLimit;
    }
    public void setDefaultLimit(int defaultLimit) {
        this.defaultLimit = defaultLimit;
    }
    public Boolean getIsV() {
        return isV;
    }
    public void setIsV(Boolean isV) {
        this.isV = isV;
    }
    public int getPoolTimeToWait() {
        return poolTimeToWait;
    }
    public void setPoolTimeToWait(int poolTimeToWait) {
        this.poolTimeToWait = poolTimeToWait;
    }
    public Boolean getV() {
        return isV;
    }
    public void setV(Boolean v) {
        this.isV = v;
    }
    public int getTransactionIsolation() {
        return transactionIsolation;
    }
    public void setTransactionIsolation(int transactionIsolation) {
        this.transactionIsolation = transactionIsolation;
    }
    public Boolean getHumpToline() {
        return isHumpToline;
    }
    public void setHumpToline(Boolean humpToline) {
        isHumpToline = humpToline;
    }

    /**
     * 传入的MAP转换为配置类的属性
     * @param map Map
     * @return 当前对象
     */
    public AfSqlConfig fromMap(Map<String, String> map) {
        setDriverClassName(AfSqlStringUtils.defaultIfEmpty(map.get("driverClassName"), " "));
        setUrl(AfSqlStringUtils.defaultIfEmpty(map.get("url"), " "));
        setUsername(AfSqlStringUtils.defaultIfEmpty(map.get("username"), " "));
        setPassword(AfSqlStringUtils.defaultIfEmpty(map.get("password"), " "));
        setIspool(AfSqlStringUtils.strToBoolean(map.get("ispool"), false));
        setInit(Integer.parseInt(AfSqlStringUtils.defaultIfEmpty(map.get("init"), "10")));
        setMin(Integer.parseInt(AfSqlStringUtils.defaultIfEmpty(map.get("min"), "5")));
        setMax(Integer.parseInt(AfSqlStringUtils.defaultIfEmpty(map.get("max"), "20")));
        setPoolTimeToWait(Integer.parseInt(AfSqlStringUtils.defaultIfEmpty(map.get("poolTimeToWait"), "3000")));
        setPrintSql(AfSqlStringUtils.strToBoolean(map.get("printSql"), false));
        setSqlhh(AfSqlStringUtils.defaultIfEmpty(map.get("sqlhh"), "**[SQL] "));
        setDefaultLimit(Integer.parseInt(AfSqlStringUtils.defaultIfEmpty(map.get("defaultLimit"), "1000")));
        setIsV(AfSqlStringUtils.strToBoolean(map.get("isV"), true));
        setTransactionIsolation(Integer.parseInt(AfSqlStringUtils.defaultIfEmpty(map.get("transactionIsolation"), "4")));
        setIspool(AfSqlStringUtils.strToBoolean(map.get("isHumpToline"), true));

        return this;
    }

    @Override
    public String toString() {
        return "AfSqlConfig{" +
                "driverClassName='" + driverClassName + '\'' +
                ", url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", ispool=" + ispool +
                ", init=" + init +
                ", min=" + min +
                ", max=" + max +
                ", poolTimeToWait=" + poolTimeToWait +
                ", printSql=" + printSql +
                ", sqlhh='" + sqlhh + '\'' +
                ", defaultLimit=" + defaultLimit +
                ", isV=" + isV +
                ", transactionIsolation=" + transactionIsolation +
                ", isHumpToline=" + isHumpToline +
                '}';
    }
}
