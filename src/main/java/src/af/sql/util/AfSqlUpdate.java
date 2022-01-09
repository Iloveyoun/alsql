package src.af.sql.util;

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

    public AfSqlUpdate add(String name, String value) {
        sss.add(ctx.name(name) + "=" + ctx.value(value));
        return this;
    }

    ////////////////////////////////
    public AfSqlUpdate add2(String name, String value) {
        value = ctx.escape(value); // 转义
        add(name, value);
        return this;
    }

    public AfSqlUpdate add2(String name, Integer value) {
        add(name, value.toString());
        return this;
    }

    public AfSqlUpdate add2(String name, Long value) {
        add(name, value.toString());
        return this;
    }

    public AfSqlUpdate add2(String name, Short value) {
        add(name, value.toString());
        return this;
    }

    public AfSqlUpdate add2(String name, Boolean value) {
        add(name, value ? "1" : "0");
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
