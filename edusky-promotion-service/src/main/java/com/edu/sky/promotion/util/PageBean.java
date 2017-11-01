package com.edu.sky.promotion.util;

import java.io.Serializable;
import java.util.List;

public class PageBean<T> implements Serializable{
	
	private int currentPage;
	
	private int pageSize;

    private long totalCount;

    private List<T> list;

    public PageBean(){
    }
    public PageBean(int cp,int pz){
        this.currentPage = cp == 0 ? 1 : cp;
        this.pageSize = pz == 0 ? 10 : pz;
    }

	public static int getOffset(int cp, int pz){
        return (cp - 1) * pz;
    }

    public int getCp() {
        return currentPage;
    }

    public int getPz() {
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
