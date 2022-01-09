package src.af.sql.paging;

/**
 * @title
 */
public class AfPagingOracle implements AfPaging {
    @Override
    public String getPagingSql(String sql, AfPage page) {
        String newsql = "select * from (" + "	select tab_1.*,ROWNUM rn from ( " + sql + " ) tab_1 " + ") tab_2 where rn between "
                + (page.getStart() + 1) + " and " + (page.getStart() + page.getSize());
        return newsql;
    }
}
