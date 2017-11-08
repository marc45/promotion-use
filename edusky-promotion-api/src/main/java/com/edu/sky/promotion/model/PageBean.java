package com.edu.sky.promotion.model;

import java.io.Serializable;
import java.util.List;

public class PageBean<T> implements Serializable{
	
	private int pageNo;
	
	private int pageSize;

    private long totalCount;

    private List<T> list;

    public PageBean(){
    }
    public PageBean(int pageNo, int pageSize){
        this.pageNo = pageNo == 0 ? 1 : pageNo;
        this.pageSize = pageSize == 0 ? 10 : pageSize;
    }

	public static int getOffset(int pageNo, int pageSize){
        return (pageNo - 1) * pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
