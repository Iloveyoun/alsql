package src.af.sql.util;

import com.fly.jdbc.cfg.FlyConsts;
import src.af.sql.config.AfSqlObjects;

/**
 * 一个简单工具类
 */
public class AfSqlUtil {
    // 打印
    public static void printAfSql() {
        String str = "             ______    _____    ____    _      \n" +
                "     /\\     |  ____|  / ____|  / __ \\  | |     \n" +
                "    /  \\    | |__    | (___   | |  | | | |     \n" +
                "   / /\\ \\   |  __|    \\___ \\  | |  | | | |     \n" +
                "  / ____ \\  | |       ____) | | |__| | | |____ \n" +
                " /_/    \\_\\ |_|      |_____/   \\___\\_\\ |______|\n";
        System.out.println(str);
    }

    /**
     * 输出执行的SQL语句
     * @param sql SQL语句
     */
    public static void printSqlInfo(String sql) {
        if (AfSqlObjects.getConfig().getPrintSql()) {
            System.out.println(AfSqlObjects.getConfig().getSqlhh() + sql);
        }
    }
}
