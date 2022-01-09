package src.af.sql.paging;

/**
 * @title
 */
public class AfPagingSqlServer implements AfPaging {
    @Override
    public String getPagingSql(String sql, AfPage page) {
        String newsql=
                "select * from (" +
                        "	select *,ROW_NUMBER() over(order by getdate() asc) as row from ( " +sql + " )as T " +
                        ") as TT " +
                        "where row between "+(page.getStart()+1)+" and "+(page.getStart()+page.getSize());
        return newsql;
    }
}
