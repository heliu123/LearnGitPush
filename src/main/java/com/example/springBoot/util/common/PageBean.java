package com.example.springBoot.util.common;

/**
 * 分页实体
 * @author zhufeng
 * 
 */
public class PageBean {
	/** 单页记录数 */
	private Integer pageSize = 10;
	/** 当前页数  默认第1页*/
    private Integer pageNum = 1;
    /** 总页数 */
    private Integer pageCount = 0;
    /** 是否有下一页 */
    private Integer pageHasMore = 0;//0:没有     1:有
    /**
     * 分页查询基准时间  
     * 可以根据此时间来筛选记录
	 * 保证分页时只会返回此时间之前的数据
	 * 需要sql中进行判断
     */
    private String pageQueryTime;
    
    public Integer getPageNum() {
        return pageNum;
    }
    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }
    public Integer getPageSize() {
        return pageSize;
    }
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
	public Integer getPageCount() {
		return pageCount;
	}
	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}
	public Integer getPageHasMore() {
		return pageHasMore;
	}
	public void setPageHasMore(Integer pageHasMore) {
		this.pageHasMore = pageHasMore;
	}
	public String getPageQueryTime() {
		return pageQueryTime;
	}
	public void setPageQueryTime(String pageQueryTime) {
		this.pageQueryTime = pageQueryTime;
	}
	
}
