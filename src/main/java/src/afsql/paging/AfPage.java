package src.afsql.paging;

import java.util.Collections;
import java.util.List;

/**
 * @title
 */
public class AfPage<T> implements AfIPage{
    private static final long serialVersionUID = 8545996863226528798L;

    /**
     * 查询数据列表
     */
    private List<T> records = Collections.emptyList();
    /**
     * 总数，当 total 不为 0 时分页插件不会进行 count 查询
     */
    private long total = 0;
    /**
     * 每页显示条数，默认 10
     */
    private long size = 10;
    /**
     * 当前页
     */
    private long current = 1;

    /**
     * 起始位置
     */
    private long start;

    public AfPage() {
        // to do nothing
    }

    /**
     * 分页构造函数
     * @param current 当前页
     * @param size    每页显示条数
     */
    public AfPage(long current, long size) {
        this(current, size, 0);
    }

    /**
     * 分页构造
     * @param current 当前页
     * @param size 每页显示条数
     * @param total 总条数：总条数不为零不会进行
     */
    public AfPage(long current, long size, long total) {
        if (current > 1) {
            this.current = current;
        }
        this.size = size;
        this.total = total;
        initStart();
    }

    /**
     * 静态方法获取分页工具类
     * @param current
     * @param size
     * @return
     */
    public static AfPage getPage(long current, long size){
        return new AfPage(current, size);
    }

    /**
     * 是否存在上一页
     * @return true / false
     */
    public boolean hasPrevious() {
        return this.current > 1;
    }

    /**
     * 是否存在下一页
     * @return true / false
     */
    public boolean hasNext() {
        return this.current < this.getPages();
    }
    /**
     * 计算当前分页偏移量
     */
    @Override
    public long offset() {
        return getCurrent() > 0 ? (getCurrent() - 1) * getSize() : 0;
    }

    /**
     * 当前分页总页数
     */
    @Override
    public long getPages() {
        if (getSize() == 0) {
            return 0L;
        }
        long pages = getTotal() / getSize();
        if (getTotal() % getSize() != 0) {
            pages++;
        }
        return pages;
    }

    @Override
    public List<T> getRecords() {
        return this.records;
    }

    @Override
    public AfIPage<T> setRecords(List records) {
        this.records = records;
        return this;
    }

    @Override
    public long getTotal() {
        return this.total;
    }

    @Override
    public AfPage<T> setTotal(long total) {
        this.total = total;
        return this;
    }

    @Override
    public long getSize() {
        return this.size;
    }

    @Override
    public AfPage<T> setSize(long size) {
        this.size = size;
        return this;
    }

    @Override
    public long getCurrent() {
        return this.current;
    }

    @Override
    public AfPage<T> setCurrent(long current) {
        this.current = current;
        return this;
    }

    public void initStart(){
        this.start = (current - 1) * size;
    }

    public long getStart() {
        return start;
    }
    public AfPage<T> setStart(int start) {
        this.start = start;
        return this;
    }

    @Override
    public String toString() {
        return "AfPage{" +
                "records=" + records +
                ", total=" + total +
                ", size=" + size +
                ", current=" + current +
                ", start=" + start +
                '}';
    }
}
