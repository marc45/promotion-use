package com.edu.sky.promotion.po.entity;

import java.io.Serializable;
import java.util.Date;

public class CouponShop implements Serializable {
    private static final long serialVersionUID = -1714268578947803228L;
    /**
     * id
     * 表字段 : t_coupon_shop.id
     * 
     */
    private Long id;

    /**
     * 优惠券id
     * 表字段 : t_coupon_shop.coupon_id
     * 
     */
    private Long couponId;

    /**
     * 商品id
     * 表字段 : t_coupon_shop.shop_code
     * 
     */
    private Long shopCode;

    /**
     * 
     * 表字段 : t_coupon_shop.create_time
     * 
     */
    private Date createTime;

    /**
     * 
     * 表字段 : t_coupon_shop.update_time
     * 
     */
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public Long getShopCode() {
        return shopCode;
    }

    public void setShopCode(Long shopCode) {
        this.shopCode = shopCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}