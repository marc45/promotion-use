package com.edu.sky.promotion.util;

import java.io.Serializable;
import java.util.List;

public class PageBean<T> implements Serializable{
	
	private int cp;
	
	private int pz;

    private long totalCount;

    private List<T> list;

    public PageBean(){
    }
    public PageBean(int cp,int pz){
        this.cp = cp == 0 ? 1 : cp;
        this.pz = pz == 0 ? 10 : pz;
    }

	public static int getOffset(int cp, int pz){
        return (cp - 1) * pz;
    }

    public int getCp() {
        return cp;
    }

    public int getPz() {
        return pz;
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
