package com.example.springBoot.util.common;

/**
 * 分页实体
 * 
 * sql使用pageStart、pageEnd进行分页 需要包含pageEnd、不包含pageStart
 * 
 * pageStart起始位0
 * 
 * @author zhufeng
 * 
 */
public class Page {
	/** 单页记录数 */
	private Integer pageSize = 10;
	/** 当前页数 */
    private Integer pageNo = 0;
    /** 记录总数 */
    private Integer pageCount = 0;
    /** 限制最大记录数 */
    private Integer maxNo = 0;
    /** 是否有下一页 */
    //private String hasMore = "0";//0:没有     1:有
    /** 分页开始序号 */
    //private Integer pageStart;
    /** 分页结束序号 */
    //private Integer pageEnd;

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }
    
    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

	public Integer getPageStart() {
		return this.pageSize * this.pageNo;
	}

/*	public void setPageStart(Integer pageStart) {
		this.pageStart = pageStart;
	}*/

	public Integer getPageEnd() {
		return this.pageSize * (this.pageNo + 1);
	}
/*	public void setPageEnd(Integer pageEnd) {
		this.pageEnd = pageEnd;
	}*/
	public Integer getPageCount() {
		return pageCount;
	}
	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}
	public String getHasMore() {
		if(this.pageCount > this.pageSize * (this.pageNo + 1)){
			return "1";
		}else{
			return "0";
		}
	}

	public Integer getMaxNo() {
		return maxNo;
	}

	public void setMaxNo(Integer maxNo) {
		this.maxNo = maxNo;
	}
	
}
