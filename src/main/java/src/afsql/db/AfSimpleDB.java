package src.afsql.db;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import src.afsql.AfSql;
import src.afsql.AfSqlConnection;
import src.afsql.config.AfSqlObjects;
import src.afsql.paging.AfIPage;
import src.afsql.paging.AfPage;

import javax.sql.DataSource;

/**
 * SimpleDB工具： 内含一个 C3P0 连接池
 */
public class AfSimpleDB {
    // 创建连接池 , 全局对象, 从 c3p0-config.xml 里读取默认配置
    public static DataSource dataSource = null;

	/**
	 * 获取连接 , 并包装为 AfSqlConnection
	 *
	 * @return
	 * @throws Exception
	 */
	public static AfSqlConnection getConnection() throws Exception {
	    if (dataSource == null) {
            setDataSource(getDataSource());
        }
	    // 把连接包装成AfSqlConnection返回
        return new AfSqlConnection(AfSql.MYSQL, dataSource.getConnection());
    }

    public static void setDataSource(DataSource dataSource1) {
        dataSource = dataSource1;
    }

    public static DataSource getDataSource() {
        if (dataSource == null) {
            dataSource = AfSqlObjects.getDataSource();
        }
        return dataSource;
    }

	/**
	 * 执行 insert update delete 等SQL
	 *
	 * @param sql INSERT、UPDATE、DELETE等SQL
	 * @return 受影响的行数
	 * @throws Exception
	 */
    public static int execute(String sql) throws Exception {
        AfSqlConnection conn = getConnection();
        try {
            return conn.execute(sql);
        } finally {
            conn.close();
        }
    }

	/**
	 * 执行 SELECT 操作
	 *
	 * @param sql SELECT语句
	 * @return JDBC结果集
	 * @throws Exception
	 */
    public static ResultSet executeQuery(String sql) throws Exception {
        AfSqlConnection conn = getConnection();
        try {
            return conn.executeQuery(sql);
        } finally {
            conn.close();
        }
    }

    /**
     * 查询获取单行记录
     *
     * @param sql
     * @return
     * @throws Exception
     */
    public static String[] getOne(String sql) throws Exception {
        AfSqlConnection conn = getConnection();
        try {
            return conn.getOne(sql);
        } finally {
            conn.close();
        }
    }

    /**
     * 查询获取单行记录,返回Map
     *
     * @param sql
     * @return
     * @throws Exception
     */
    public static Map getOne(String sql, int convert) throws Exception {
        AfSqlConnection conn = getConnection();
        try {
            return conn.getOne(sql, convert);
        } finally {
            conn.close();
        }
    }

    /**
     * 查询获取单行记录,转类相应的POJO类型
     *
     * @param sql
     * @param clazz 转成POJO类型
     */
    public static Object getOne(String sql, Class clazz) throws Exception {
        AfSqlConnection conn = getConnection();
        try {
            return conn.getOne(sql, clazz);
        } finally {
            conn.close();
        }
    }

    /**
	 * 查询多条记录, 返回 List<String[]>
	 *
	 * @param sql
	 * @return
	 * @throws Exception
	 */
    public static List<String[]> query(String sql) throws Exception {
        AfSqlConnection conn = getConnection();
        try {
            return conn.query(sql);
        } finally {
            conn.close();
        }
    }


    /**
     * 查询多条记录, 返回 List<Map>
     *
     * @param sql
     * @param convert 转换参数,未用到,设为0
     */
    public static List<Map> query(String sql, int convert) throws Exception {
        AfSqlConnection conn = getConnection();
        try {
            return conn.query(sql, convert);
        } finally {
            conn.close();
        }
    }

    /**
     * 查询多条记录, 返回 List<POJO>
     *
     * @param sql
     * @param clazz 要转换成的POJO类
     */
    public static List query(String sql, Class clazz) throws Exception {
        AfSqlConnection conn = getConnection();
        try {
            return conn.query(sql, clazz);
        } finally {
            conn.close();
        }
    }

    /**
     * 插入一个POJO对象
     *
     * @param pojo 待插入的POJO对象
     */
    public static void insert(Object pojo) throws Exception {
        AfSqlConnection conn = getConnection();
        try {
            conn.insert(pojo);
        } finally {
            conn.close();
        }
    }

    /*********分页查询*********/
    /**
     * 分页查询,AfIpage内的数据类型为List<String[]>
     * @param sql 查询SQL
     * @param page 分页工具类
     * @return
     */
    public static AfIPage<String[]> query(String sql, AfPage page) throws Exception {
        AfSqlConnection conn = getConnection();
        try {
            return conn.query(sql, page);
        } finally {
            conn.close();
        }
    }

    /**
     * 分页查询,AfIpage内的数据类型为List<Map>
     * @param sql 查询SQL
     * @param convert 转换参数,未用到,设为0
     * @param page 分页工具类
     * @return
     * @throws Exception
     */
    public static AfIPage<Map> query(String sql, int convert, AfPage page) throws Exception {
        AfSqlConnection conn = getConnection();
        try {
            return conn.query(sql, convert, page);
        } finally {
            conn.close();
        }
    }

    /**
     *
     * @param sql 查询SQL
     * @param classz 要转换成的POJO类
     * @param page 分页参数类
     * @return
     * @throws Exception
     */
    public static AfIPage query(String sql, Class classz, AfPage page) throws Exception {
        AfSqlConnection conn = getConnection();
        try {
            return conn.query(sql, classz, page);
        } finally {
            conn.close();
        }
    }

    /*********快捷方法*********/
    /**
     * 根据SQL返回符合的条数
     * @param sql
     * @return
     */
    public static Long getCount(String sql) throws Exception {
        AfSqlConnection conn = getConnection();
        try {
            return conn.getCount(sql);
        } finally {
            conn.close();
        }
    }

    /**
     * 将聚合查询的第一行第一列的值转换成Long类型
     * @param sql
     * @return
     */
    public static Long getScalarInt(String sql) throws Exception {
        AfSqlConnection conn = getConnection();
        try {
            return conn.getScalarInt(sql);
        } finally {
            conn.close();
        }
    }
}
