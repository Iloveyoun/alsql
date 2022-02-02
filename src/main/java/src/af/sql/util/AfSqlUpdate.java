package src.af.sql.util;

import com.sun.org.apache.xpath.internal.operations.Bool;
import src.af.sql.AfSqlContext;

import java.util.ArrayList;
import java.util.List;

/**
 * 生成Update语句工具类
 */
public class AfSqlUpdate {
    String table = "";
    List<String> sss = new ArrayList<>();

    private AfSqlContext ctx = AfSqlContext.getInstance();

    public AfSqlUpdate(String table) {
        this.table = table;
    }

    public AfSqlUpdate add(String expr) {
        sss.add(expr);
        return this;
    }

    public AfSqlUpdate add(String expr, Boolean isJoin) {
        if (isJoin) {
            sss.add(expr);
        }
        return this;
    }

    public AfSqlUpdate add(String name, String value) {
        return add(name, value, true);
    }

    public AfSqlUpdate add(String name, String value, Boolean isJoin) {
        if (isJoin) {
            sss.add(ctx.name(name) + "=" + ctx.value(value));
        }
        return this;
    }

    /***************按类型***************/

    public AfSqlUpdate add2(String name, String value) {
        return add2(name, value, true);
    }

    public AfSqlUpdate add2(String name, String value, Boolean isJoin) {
        value = ctx.escape(value); // 转义
        add(name, value, isJoin);
        return this;
    }

    public AfSqlUpdate add2(String name, Integer value) {
        return add2(name, value, true);
    }

    public AfSqlUpdate add2(String name, Integer value, Boolean isJoin) {
        add(name, value.toString(), isJoin);
        return this;
    }

    public AfSqlUpdate add2(String name, Long value) {
        return add2(name, value, true);
    }

    public AfSqlUpdate add2(String name, Long value, Boolean isJoin) {
        add(name, value.toString(), isJoin);
        return this;
    }

    public AfSqlUpdate add2(String name, Short value) {
        return add2(name, value, true);
    }

    public AfSqlUpdate add2(String name, Short value, Boolean isJoin) {
        add(name, value.toString(), isJoin);
        return this;
    }

    public AfSqlUpdate add2(String name, Boolean value) {
        return add2(name, value, true);
    }

    public AfSqlUpdate add2(String name, Boolean value, Boolean isJoin) {
        add(name, value ? "1" : "0", isJoin);
        return this;
    }

    @Override
    public String toString() {
        String sql = " UPDATE " + ctx.name(table)
                + " SET ";
        for (int i = 0; i < sss.size(); i++) {
            if (i > 0) sql += ",";
            sql += sss.get(i);
        }
        sql += " ";

        return sql;
    }
}
