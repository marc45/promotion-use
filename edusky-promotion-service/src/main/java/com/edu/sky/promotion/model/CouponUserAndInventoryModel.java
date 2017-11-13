package com.edu.sky.promotion.model;

import com.edu.sky.promotion.po.entity.Inventory;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * redis队列model
 */
public class CouponUserAndInventoryModel implements Serializable {

    private Boolean flag;
    private Long couponCodeId;
    private Long couponId;
    private String openId;
    private Inventory inventory;

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public Long getCouponCodeId() {
        return couponCodeId;
    }

    public void setCouponCodeId(Long couponCodeId) {
        this.couponCodeId = couponCodeId;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
