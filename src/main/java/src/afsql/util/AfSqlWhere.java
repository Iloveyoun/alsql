package src.afsql.util;

import java.util.ArrayList;
import java.util.List;

import src.afsql.AfSqlContext;

/**
 * 生成Where字段SQL语句工具类
 */
public class AfSqlWhere {
    ArrayList<String> conditions = new ArrayList<String>();

    private AfSqlContext ctx = AfSqlContext.getInstance();

    public AfSqlWhere() {
    }

    /**
     * 加入单个查询条件
     * @param condition 条件表达式
     * @return 当前类
     */
    public AfSqlWhere add(String condition) {
        return add(condition, true);
    }

    public AfSqlWhere add(String condition, Boolean isJoin) {
        if (isJoin) {
            condition = condition.trim();
            if (!condition.startsWith("(")) {
                condition = "(" + condition + ")";
            }
            conditions.add(condition);
        }
        return this;
    }

    /**
     * 加入有操作符的表达式
     * @param name 字段名
     * @param op 符号
     * @param value 值
     * @return 当前类
     */
    public AfSqlWhere add(String name, String op, Object value) {
        return add(name, op, value, true);
    }

    public AfSqlWhere add(String name, String op, Object value, Boolean isJoin) {
        if (value==null) {
            add(ctx.name(name) + op + value, isJoin);
            return this;
        }
        add(ctx.name(name) + op + ctx.value(value.toString()), isJoin);
        return this;
    }

    /////////////////////////////////

    /**
     * 字符串值 比较
     * @param name 字段名
     * @param value 值
     * @return 当前类
     */
    public AfSqlWhere add2(String name, String value) {
        return add2(name, value, true);
    }

    public AfSqlWhere add2(String name, String value, Boolean isJoin) {
        value = ctx.escape(value); // 转义
        add(name, "=", value, isJoin);
        return this;
    }

    /**
     * 数值 比较
     * @param name 字段名
     * @param value 值
     * @return 当前类
     */
    public AfSqlWhere add2(String name, Long value) {
        return add2(name, value, true);
    }

    public AfSqlWhere add2(String name, Long value, Boolean isJoin) {
        add(name, "=", value, isJoin);
        return this;
    }

    /**
     * 数值 比较
     * @param name 字段名
     * @param value 值
     * @return 当前类
     */
    public AfSqlWhere add2(String name, Integer value) {
        return add2(name, value, true);
    }

    public AfSqlWhere add2(String name, Integer value, Boolean isJoin) {
        add(name, "=", value, isJoin);
        return this;
    }

    /**
     * 数值 比较
     * @param name 字段名
     * @param value 值
     * @return 当前类
     */
    public AfSqlWhere add2(String name, Short value) {
        return add2(name, value, true);
    }

    public AfSqlWhere add2(String name, Short value, Boolean isJoin) {
        add(name, "=", value, isJoin);
        return this;
    }

    public AfSqlWhere add2(String name, Boolean value) {
        return add2(name, value, true);
    }

    public AfSqlWhere add2(String name, Boolean value, Boolean isJoin) {
        add(name, "=", value ? "1" : "0", isJoin);
        return this;
    }

    /**
     * 构造Like子句，用户要根据需要自己在value里添加%
     * @param name 字段名
     * @param value 值
     * @return 当前类
     */
    public AfSqlWhere addLike(String name, String value) {
        return addLike(name, value, true);
    }

    public AfSqlWhere addLike(String name, String value, Boolean isJoin) {
        //conditions.add( colName + " LIKE '%" + colValue + "%'");
        // 把中间的空格替换成%通配符
        // String filter = value.replace('　', '%').replace(' ', '%').replace('，', '%'); // 中文空格
        add(name, " LIKE ", value, isJoin);
        return this;
    }

    /**
     * IN
     * WHERE id IN ('07922860270B47FDB02ADD231F885DAB', '08C74DB5EBC64F439BB747EECBEC3862')
     * @param name 字段名
     * @param values 值
     * @return 当前类
     */
    public AfSqlWhere addIn(String name, List values) {
        return addIn(name, values, true);
    }

    public AfSqlWhere addIn(String name, List values, Boolean isJoin) {
        if (isJoin) {
            if (values.size() == 0) return this;
            String sql = ctx.name(name) + " IN (";
            for (int i = 0; i < values.size(); i++) {
                String s = null;
                if (values.get(i)!=null) {
                    s = ctx.value(values.get(i).toString());
                }

                sql += s;
                if (i != values.size() - 1) sql += ",";
            }
            sql += ") ";
            add(sql);
        }
        return this;
    }

    public AfSqlWhere addIn(String name, Object[] values) {
        return addIn(name, values, true);
    }

    public AfSqlWhere addIn(String name, Object[] values, Boolean isJoin) {
        if (isJoin) {
            if (values.length == 0) return this;
            String sql = ctx.name(name) + "IN (";
            for (int i = 0; i < values.length; i++) {
                String a = null;
                if (values[i]!=null) {
                    a = values[i].toString().trim();
                }
                String s = ctx.value(a);
                sql += s;
                if (i != values.length - 1) sql += ",";
            }
            sql += ")";
            add(sql);
        }
        return this;
    }

    @Override
    public String toString() {
        if (conditions.size() == 0) return " ";

        String where = " WHERE ";
        for (int i = 0; i < conditions.size(); i++) {
            if (i != 0) where += " AND ";
            where += conditions.get(i);
        }
        return where;
    }

}
