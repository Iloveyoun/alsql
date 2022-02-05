package src.afsql.paging;

/**
 * 分页接口
 */
public interface AfIPaging {
    /**
     * 根据原始SQL，返回分页形式的SQL
     * @param sql SELECT语句
     * @param page 分页工具
     * @return 分页SQL
     */
    public String getPagingSql(String sql, AfPage page);
}
