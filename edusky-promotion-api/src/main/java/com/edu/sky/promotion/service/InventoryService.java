package com.edu.sky.promotion.service;

import com.edu.sky.promotion.model.PageBean;

public interface InventoryService {

    /**
     * 优惠券库存列表：分页
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageBean inventoryPage(Integer pageNum, Integer pageSize);

}
