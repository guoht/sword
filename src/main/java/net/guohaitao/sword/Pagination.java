package net.guohaitao.sword;

import javax.annotation.Nonnegative;

/**
 * Created by i@guohaitao.net on 14-9-17.
 * Description: 分页对象
 */
public class Pagination {
    /**
     * 当前页码
     */
    private int pageNo = 1;
    /**
     * 每页长度
     */
    private int pageSize;
    /**
     * 总数
     */
    private int total;

    /**
     * @param pageNo
     *         当前页码
     * @param pageSize
     *         每页长度
     * @param total
     *         总数
     */
    public Pagination(@Nonnegative int pageNo, @Nonnegative int pageSize, @Nonnegative int total) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.total = total;
    }

    private Pagination() {
    }

    /**
     * 当前页的起始索引
     *
     * @return
     */
    public int getStart() {
        return (pageNo - 1) * pageSize;
    }

    /**
     * 当前页
     *
     * @return
     */
    public int getPageNo() {
        return pageNo;
    }

    /**
     * 最大页数
     *
     * @return
     */
    public int getPageMax() {
        return (int) Math.ceil(total / (double) pageSize);
    }

    /**
     * 每页长度
     *
     * @return
     */
    public int getPageSize() {
        return pageSize;
    }


    /**
     * 最大数
     *
     * @return
     */
    public int getTotal() {
        return total;
    }

    /**
     * 下一页编号
     *
     * @return
     */
    public int getNextPage() {
        return pageNo < getPageMax() ? pageNo + 1 : pageNo;
    }

    /**
     * 上一页编号
     *
     * @return
     */
    public int getPrevPage() {
        return (pageNo > 1) ? pageNo - 1 : pageNo;
    }

    /**
     * 是否有下一页
     *
     * @return
     */
    public boolean hasNext() {
        return pageNo < getPageMax();
    }

    /**
     * 是否有上一页
     *
     * @return
     */
    public boolean hasPrev() {
        return pageNo > 1;
    }

}
