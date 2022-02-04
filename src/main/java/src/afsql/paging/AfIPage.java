package src.afsql.paging;

import java.io.Serializable;
import java.util.List;

/**
 * @title 分页工具类
 */
public interface AfIPage<T> extends Serializable {

    /**
     * 计算当前分页偏移量
     */
    long offset();

    /**
     * 当前分页总页数
     */
    long getPages();

    /**
     * 分页记录列表
     * @return 分页对象记录列表
     */
    List<T> getRecords();

    /**
     * 设置分页记录列表
     */
    AfIPage<T> setRecords(List<T> records);

    /**
     * 当前满足条件总行数
     * 当 total 为 null 或者大于 0 分页插件不在查询总数
     * @return 总条数
     */
    long getTotal();

    /**
     * 设置当前满足条件总行数
     * 当 total 为 null 或者大于 0 分页不在查询总数
     */
    AfIPage<T> setTotal(long total);

    /**
     * 当前分页总页数
     * @return 总页数
     */
    long getSize();

    /**
     * 设置当前分页总页数
     */
    AfIPage<T> setSize(long size);

    /**
     * 当前页，默认 1
     * @return 当前页
     */
    long getCurrent();

    /**
     * 设置当前页
     */
    AfIPage<T> setCurrent(long current);
}
