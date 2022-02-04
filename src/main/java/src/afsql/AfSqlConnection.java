package src.afsql;

import src.afsql.config.AfSqlConfig;
import src.afsql.config.AfSqlObjects;
import src.afsql.paging.AfIPage;
import src.afsql.paging.AfPage;
import src.afsql.reflect.AfSqlColumn;
import src.afsql.reflect.AfSqlPojo;
import src.afsql.reflect.AfSqlReflect;
import src.afsql.util.AfSqlInsert;
import src.afsql.util.AfSqlUtil;

import java.lang.reflect.Method;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;


public class AfSqlConnection {

    // SQL相关工具集
    public AfSqlContext ctx = AfSqlContext.getInstance();
    public int driver = AfSql.MYSQL;

    // JDBC连接
    public Connection conn;

    public AfSqlConnection() {
    }

    // 外部创建好 JDBC Connection 然后传进来
    public AfSqlConnection(Connection conn) {
        this.conn = conn;
    }

    // 外部创建好 JDBC Connection 然后传进来
    public AfSqlConnection(int driver, Connection conn) {
        this.conn = conn;
    }

    public AfSqlConnection(int driver) {
        this.driver = driver;
    }

    // 连接数据库
    public void connect(String server, int port, String database, String user, String password) throws Exception {
        String jdbcUrl = ctx.jdbcUrl(server, port, database);
        conn = DriverManager.getConnection(jdbcUrl, user, password);
    }

    // 连接数据库
    public void connect(String driverClassName, String server, int port, String database, String user, String password) throws Exception {
        Class.forName(driverClassName);
        String jdbcUrl = ctx.jdbcUrl(server, port, database);
        conn = DriverManager.getConnection(jdbcUrl, user, password);
    }

    // 连接数据库
    public void connect(AfSqlConfig afConfig) throws Exception {
        Class.forName(afConfig.getDriverClassName());
        conn = DriverManager.getConnection(afConfig.getUrl(), afConfig.getUsername(), afConfig.getPassword());
    }

    /**
     * 关闭连接
     */
    public void close() {
        try {
            conn.close();
        } catch (Exception e) {
        }
    }

    /**
     * 事务支持-开启事务
     * @throws SQLException
     */
    public void beginTransaction() throws SQLException {
        conn.setAutoCommit(false);
    }

    /**
     * 提交
     * @throws SQLException
     */
    public void commit() throws SQLException {
        conn.commit();
    }

    /**
     * 回滚
     * @throws SQLException
     */
    public void rollback() throws SQLException {
        conn.rollback();
    }

    /**
     * 回滚
     * @throws SQLException
     */
    public void rollback(Savepoint sp) throws SQLException {
        conn.rollback(sp);
    }

    /**
     * 设置事务回滚点
     * @throws SQLException
     * @return
     */
    public Savepoint setSavepoint() throws SQLException {
        return conn.setSavepoint();
    }

    public void setSavepoint(String name) throws SQLException {
        conn.setSavepoint(name);
    }

    public void setTransactionIsolation(int level) throws SQLException {
        conn.setTransactionIsolation(level);
    }

    public int getTransactionIsolation() throws SQLException {
        return conn.getTransactionIsolation();
    }

    /**
     * 执行INSERT, UPDATE, DELETE 操作
     *
     * @param sql
     * @return 受影响行数
     * @throws Exception
     */
    public int execute(String sql) throws Exception {
        AfSqlUtil.printSqlInfo(sql);
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
        return stmt.getUpdateCount();
    }

    /**
     * 执行 SELECT 操作
     *
     * @param sql
     * @return JDBC结果集
     * @throws Exception
     */
    public ResultSet executeQuery(String sql) throws Exception {
        AfSqlUtil.printSqlInfo(sql);
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(sql);
    }

    /**
     * 查询单行记录
     *
     * @param sql
     * @return
     * @throws Exception
     */
    public String[] getOne(String sql) throws Exception {
        List<String[]> rows = query(sql + " LIMIT 0,1");
        if (rows.size() > 0) {
            return rows.get(0);
        }
        return null;
    }

    /**
     * 查询单行记录
     *
     * @param sql
     * @param convert 转换参数，后期可以改成驼峰命名等
     * @return 结果集封装成List<Map>
     * @throws Exception
     */
    public Map getOne(String sql, int convert) throws Exception {
        List<Map> rows = query(sql + " LIMIT 0,1", 0);
        if (rows.size() > 0) {
            return rows.get(0);
        }
        return null;
    }

    /**
     * 查询获取单行记录,转类相应的POJO类型
     *
     * @param sql
     * @param clazz 转成POJO类型
     */
    public Object getOne(String sql, Class clazz) throws Exception {
        List rows = query(sql + " LIMIT 0,1", clazz);
        if (rows.size() > 0) {
            return rows.get(0);
        }
        return null;
    }

    /**
     * 查询多条记录
     *
     * @param sql
     * @return 结果集封装成List<String>
     * @throws Exception
     */
    public List<String[]> query(String sql) throws Exception {
        ResultSet rs = executeQuery(sql);
        // 查看一共有多少列
        int numColumns = rs.getMetaData().getColumnCount();
        // 取出数据
        List<String[]> result = new ArrayList<>(numColumns);
        while (rs.next()) {
            String[] values = new String[numColumns];
            for (int i = 0; i < numColumns; i++) {
                values[i] = rs.getString(i + 1);
            }
            result.add(values); // 添加一行
        }
        return result;
    }

    /**
     * 查询多条记录
     *
     * @param sql
     * @param convert 转换参数，后期可以改成驼峰命名等
     * @return 结果集封装成List<Map>
     * @throws Exception
     */
    public List<Map> query(String sql, int convert) throws Exception {
        ResultSet rs = executeQuery(sql);
        // 获取结果集元数据 MetaData
        ResultSetMetaData rsmd = rs.getMetaData();
        AfSqlMeta[] metaArray = AfSqlMeta.read(rsmd);
        int numColumns = rsmd.getColumnCount();
        // 取出数据
        List<Map> result = new ArrayList<>();
        while (rs.next()) {
            Map<String, String> row = new HashMap<>();// 一行
            for (int i = 0; i < numColumns; i++) {
                row.put(metaArray[i].label, rs.getString(i + 1));
            }
            result.add(row);    // 添加一行
        }
        return result;
    }

    /**
     * 查询多条记录
     *
     * @param sql
     * @param clazz POJO.class
     * @return
     * @throws Exception
     */
    public List query(String sql, Class clazz) throws Exception {
        // 查询
        ResultSet rs = executeQuery(sql);

        // 取得MetaData
        ResultSetMetaData rsmd = rs.getMetaData();
        AfSqlMeta[] metaArray = AfSqlMeta.read(rsmd);

        // 构造一个List返回
        int numColumn = metaArray.length;
        List result = new ArrayList();
        while (rs.next()) {
            // 将每行数据转为一个POJO对象
            Object pojo = clazz.newInstance();
            result.add(pojo);

            // 通过setter设置pojo的各个属性
            for (int i = 0; i < numColumn; i++) {
                AfSqlMeta meta = metaArray[i]; // 该列的Meta数据
                String value = rs.getString(i + 1); // 该列的值

                // 根据类型提示，转成Integer, String, Boolean, Date等基本类型
                Object typedValue = AfSqlReflect.typedValue(ctx, meta.type, value);

                // 将列值设置给POJO的属性
                try {
                    AfSqlReflect.setPojo(pojo, meta.label, typedValue);
                } catch (Exception e) {
                    // e.printStackTrace();
                    System.out.println("** 错误: " + e.getMessage());
                }
            }
        }
        return result;
    }

    /**
     * 插入一个POJO对象
     *
     * @param pojo 待插入的POJO对象
     */
    public void insert(Object pojo) throws Exception {
        // 解析类的信息
        Class clazz = pojo.getClass();
        AfSqlPojo po = AfSqlPojo.from(clazz);
        if (po.tableName == null)
            throw new Exception("类 " + clazz.getName() + "中缺少AFTABLE注解! 无法自动插入!");

        // 准备创建SQL语句
        AfSqlInsert insertSQL = new AfSqlInsert(po.tableName);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 根据映射关系，类的属性名 <-> 列名，拼凑SQL语句
        for (AfSqlColumn c : po.columns) {
            String fieldName = c.name; // 列名
            Method getter = po.findGetter(fieldName);
            try {
                Object value = getter.invoke(pojo); // 每一列的值
                if (value != null) {
                    if (value instanceof Boolean) {
                        Boolean v = (Boolean) value;
                        value = v ? "1" : "0";
                    } else if (value instanceof Date) {
                        Date v = (Date) value;
                        value = sdf.format(v);
                    } else if (value instanceof String) {
                        // SQL 转义
                        value = ctx.escape((String) value);
                    }
                    insertSQL.add(fieldName, value.toString());
                }
            } catch (Exception e) {
            }
        }

        // 执行INSERT语句
        String sql = insertSQL.toString();
        AfSqlUtil.printSqlInfo(sql);

        Statement stmt = conn.createStatement();
        if (po.generatedKey == null) {
            // 无自增ID
            stmt.execute(sql);
        } else {
            // 自增主键ID处理
            // 1  如果用户在插入时已经自己指定了一个值，则MySQL会使用这个值，并返回这个值
            // 2  如果用户在插入时未定自增主键的值，则MySQL会生成一个自增的值，并返回
            stmt.execute(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                // 取回自增的ID
                String id = keys.getString(1);
                try {
                    Method setter = AfSqlReflect.findSetter(clazz, po.generatedKey);
                    AfSqlReflect.setPojo(pojo, setter, id);
                } catch (Exception e) {
                }
            }
        }
    }

    /*********分页查询*********/
    /**
     * 分页查询,AfIpage内的数据类型为List<String[]>
     * @param sql 查询SQL
     * @param page 分页工具类
     * @return
     */
    public AfIPage<String[]> query(String sql, AfPage page) throws Exception {
        if (page == null) {
            page = new AfPage(1, AfSqlObjects.getConfig().getDefaultLimit());
        }
        if (page.getTotal() <= 0) {
            page.setTotal(getCount(sql));
        }
        if (page.getTotal() == 0) {
            return null;
        }
        sql = AfSqlObjects.getPaging().getPagingSql(sql, page);
        return page.setRecords(query(sql));
    }

    /**
     * 分页查询,AfIpage内的数据类型为List<Map>
     * @param sql 查询SQL
     * @param convert 转换参数,未用到,设为0
     * @param page 分页工具类
     * @return
     * @throws Exception
     */
    public AfIPage<Map> query(String sql, int convert, AfPage page) throws Exception {
        if (page == null) {
            page = new AfPage(1, AfSqlObjects.getConfig().getDefaultLimit());
        }
        if (page.getTotal() <= 0) {
            page.setTotal(getCount(sql));
        }
        if (page.getTotal() == 0) {
            return null;
        }
        sql = AfSqlObjects.getPaging().getPagingSql(sql, page);
        return page.setRecords(query(sql, convert));
    }

    /**
     *
     * @param sql 查询SQL
     * @param classz 要转换成的POJO类
     * @param page 分页参数类
     * @return
     * @throws Exception
     */
    public AfIPage query(String sql, Class classz, AfPage page) throws Exception {
        if (page == null) {
            page = new AfPage(1, AfSqlObjects.getConfig().getDefaultLimit());
        }
        if (page.getTotal() <= 0) {
            page.setTotal(getCount(sql));
        }
        if (page.getTotal() == 0) {
            return null;
        }
        sql = AfSqlObjects.getPaging().getPagingSql(sql, page);
        return page.setRecords(query(sql, classz));
    }

    /*********快捷方法*********/
    /**
     * 根据SQL返回符合的条数
     * @param sql
     * @return
     */
    public Long getCount(String sql) throws Exception {
        String s = "SELECT COUNT(*) FROM (%s) AS a_table";
        return getScalarInt(String.format(s, sql));
    }

    /**
     * 将聚合查询的第一行第一列的值转换成Long类型
     * @param sql
     * @return
     */
    public Long getScalarInt(String sql) throws Exception {
        String[] one = getOne(sql);
        return one == null ? null : Long.parseLong(one[0]);
    }
}
