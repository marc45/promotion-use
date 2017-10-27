package com.edu.sky.promotion.po.dao;

import com.edu.sky.promotion.po.entity.Inventory;
import com.edu.sky.promotion.po.example.InventoryExample;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryMapper extends BaseMapper<Inventory, InventoryExample, Long> {

    Inventory selectByCouponId(long couponId);

    List<Inventory> selectByCouponIdList(List<Long> couponIds);

}