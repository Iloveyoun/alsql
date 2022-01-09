package src.af.sql;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/* SQL 相关方法工具集
 *
 * 必须通过 getInstance() 来获取实例
 *
 * 不同类型的服务器  MySQL , Oracle, SQL Server 在实现上可能略有差异
 * 这么设计是为了方便扩展
 *
 */
public class AfSqlContext {
    // 获取实例
    public static AfSqlContext getInstance() {
        return new AfSqlContext();
    }
    public static AfSqlContext getInstance(int driver) {
        return new AfSqlContext();
    }

    // 时间转换
    private DateFormat sdf_dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private DateFormat sdf_d = new SimpleDateFormat("yyyy-MM-dd");
    private DateFormat sdf_t = new SimpleDateFormat("HH:mm:ss");

    // 数据库URL格式化
    public String jdbcUrl(String server, int port, String database) {
        // 示例 jdbc:mysql://127.0.0.1:3306/af_school?useUnicode=true&characterEncoding=UTF-8
        String fmt = "jdbc:mysql://%s:%d/%s?useUnicode=true&characterEncoding=UTF-8";
        String url = String.format(fmt, server, port, database);
        return url;
    }

    // 转义 替换 \ 和 '
    public String escape(String sql) {
        StringBuffer sb = new StringBuffer(sql.length() * 2);
        for (int i = 0; i < sql.length(); i++) {
            char ch = sql.charAt(i);
            if (ch == '\'' || ch == '\\') {
                sb.append('\\');
            }
            sb.append(ch);
        }
        return sb.toString();
    }

    // 几种时间格式化工具
    public DateFormat dateTimeFormat() {
        return sdf_dt;
    }

    public DateFormat dateFormat() {
        return sdf_d;
    }

    public DateFormat timeFormat() {
        return sdf_t;
    }

    // 名字加反引号
    public String name(String str) {
        if (str.indexOf('.') >= 0)
            return str; // "db.table.column"
        if (str.indexOf('`') >= 0)
            return str; // 已经有了反引号
        return "`" + str + "`";
    }

    // 值加单引号
    public String value(String str) {
        return "'" + str + "'";
    }

}
