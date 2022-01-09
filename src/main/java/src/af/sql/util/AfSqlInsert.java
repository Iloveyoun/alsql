package src.af.sql.util;

import src.af.sql.AfSqlContext;

import java.util.ArrayList;
import java.util.List;

/**
 * 生成Insert语句工具类
 */
public class AfSqlInsert {
    String table = "";
    // 字段名list
    List<String> names = new ArrayList<>();
    // 字段值list
    List<String> values = new ArrayList<>();

    private AfSqlContext ctx = AfSqlContext.getInstance();

    public AfSqlInsert(String table) {
        this.table = table;
    }

    // 不提供列名，则SQL里只写值，不写列名
    public AfSqlInsert add(String value) {
        values.add(value);
        return this;
    }

    // 提供列名和值
    public AfSqlInsert add(String name, String value) {
        names.add(name);
        values.add(value);
        return this;
    }

    // 按类型
    public AfSqlInsert add2(String name, String value) {
        value = ctx.escape(value); // 转义
        add(name, value);
        return this;
    }

    public AfSqlInsert add2(String name, Integer value) {
        add(name, value.toString());
        return this;
    }

    public AfSqlInsert add2(String name, Long value) {
        add(name, value.toString());
        return this;
    }

    public AfSqlInsert add2(String name, Short value) {
        add(name, value.toString());
        return this;
    }

    public AfSqlInsert add2(String name, Boolean value) {
        add(name, value ? "1" : "0");
        return this;
    }

    @Override
    public String toString() {
        String ccc = "";
        if (names.size() > 0) {
            if (names.size() != values.size())
                return "SQL拼写出错! 列和值个数不一致!";

            ccc = "(";
            for (int i = 0; i < names.size(); i++) {
                String name = names.get(i);
                if (i > 0) ccc += ",";
                ccc += ctx.name(name);
            }
            ccc += ")";
        }

        String vvv = "";
        if (values.size() > 0) {
            vvv = "(";
            for (int i = 0; i < values.size(); i++) {
                String str = values.get(i);
                if (i > 0) vvv += ",";
                vvv += ctx.value(str);
            }
            vvv += ")";
        }

        String sql = " INSERT INTO " + ctx.name(table)
                + ccc
                + " VALUES "
                + vvv;

        return sql;
    }
}
