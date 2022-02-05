package src.afsql.paging;

import src.afsql.config.AfSqlObjects;

public class AfPaging implements AfIPaging {
    @Override
    public String getPagingSql(String sql, AfPage page) {
        if ("com.mysql.jdbc.Driver".equals(AfSqlObjects.getConfig().getDriverClassName())) {
            // MySQL
            sql = sql + " LIMIT " + page.getStart() + "," + page.getSize();
        } else if ("oracle.jdbc.driver.OracleDriver".equals(AfSqlObjects.getConfig().getDriverClassName())) {
            // Oracle
            sql = "select * from ( select tab_1.*,ROWNUM rn from ( " + sql + " ) tab_1 " + ") tab_2 where rn between "
                    + (page.getStart() + 1) + " and " + (page.getStart() + page.getSize());
        } else if ("com.microsoft.sqlserver.jdbc.SQLServerDriver".equals(AfSqlObjects.getConfig().getDriverClassName())) {
            // SqlServer
            sql = "select * from ( select *,ROW_NUMBER() over(order by getdate() asc) as row from ( "
                    + sql
                    + " )as T ) as TT where row between "+(page.getStart()+1)
                    +" and "+(page.getStart()+page.getSize());
        } else if ("com.ibm.db2.jcc.DB2Driver".equals(AfSqlObjects.getConfig().getDriverClassName())) {
            // DB2
            sql = "SELECT * FROM (SELECT ROW_NUMBER () OVER () AS rownum_, row_.* FROM ( "
                    + sql
                    + " ) row_) AS temp WHERE rownum_ <= " + page.getSize()
                    + " AND rownum_ > " + page.getStart();
        } else {
            // 来个默认的实现
            sql = sql + " LIMIT " + page.getStart() + "," + page.getSize();
        }
        return sql;
    }
}
