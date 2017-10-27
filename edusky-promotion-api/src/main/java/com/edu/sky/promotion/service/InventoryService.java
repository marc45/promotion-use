package com.edu.sky.promotion.service;

public interface InventoryService {

    /**
     * 优惠券库存列表：分页
     * @param pageNum
     * @param pageSize
     * @return
     */
    Object inventoryPage(Integer pageNum,Integer pageSize);

}
