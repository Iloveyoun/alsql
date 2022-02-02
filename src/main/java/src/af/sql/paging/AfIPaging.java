package src.af.sql.paging;

/**
 * @author 分页接口
 */
public interface AfIPaging {
    /**
     * 根据原始SQL，返回分页形式的SQL
     * @param sql
     * @param page
     * @return
     */
    public String getPagingSql(String sql, AfPage page);
}
