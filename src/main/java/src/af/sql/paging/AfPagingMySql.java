package src.af.sql.paging;

/**
 * @title
 */
public class AfPagingMySql implements AfPaging {
    @Override
    public String getPagingSql(String sql, AfPage page) {
        String newsql = sql + " LIMIT " + page.getStart() + "," + page.getSize();
        return newsql;
    }
}
