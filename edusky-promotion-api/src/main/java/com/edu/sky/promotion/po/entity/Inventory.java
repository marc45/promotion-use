package com.edu.sky.promotion.po.entity;

import java.io.Serializable;
import java.util.Date;

public class Inventory implements Serializable {
    private static final long serialVersionUID = 796067918244455239L;
    /**
     * 
     * 表字段 : t_inventory.id
     * 
     */
    private Long id;

    /**
     * 优惠券id
     * 表字段 : t_inventory.coupon_id
     * 
     */
    private Long couponId;
    private String couponName;

    /**
     * 库存总数量
     * 表字段 : t_inventory.total_amount
     * 
     */
    private Long totalAmount;

    /**
     * 已使用数量
     * 表字段 : t_inventory.used_count
     * 
     */
    private Long usedCount;

    /**
     * 已绑定数量
     * 表字段 : t_inventory.bind_count
     * 
     */
    private Long bindCount;

    /**
     * 
     * 表字段 : t_inventory.version
     * 
     */
    private Long version;

    /**
     * 
     * 表字段 : t_inventory.create_time
     * 
     */
    private Date createTime;

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

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(Long usedCount) {
        this.usedCount = usedCount;
    }

    public Long getBindCount() {
        return bindCount;
    }

    public void setBindCount(Long bindCount) {
        this.bindCount = bindCount;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }
}