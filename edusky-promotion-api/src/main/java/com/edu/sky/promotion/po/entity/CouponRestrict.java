package com.edu.sky.promotion.po.entity;

import java.io.Serializable;
import java.util.Date;

public class CouponRestrict implements Serializable {
    private static final long serialVersionUID = 4860755017607487800L;
    /**
     * 
     * 表字段 : t_coupon_restrict.id
     * 
     */
    private Long id;

    /**
     * 
     * 表字段 : t_coupon_restrict.coupon_id
     * 
     */
    private Long couponId;

    /**
     * 
     * 表字段 : t_coupon_restrict.restrict_condition_id
     * 
     */
    private Long restrictConditionId;

    /**
     * 
     * 表字段 : t_coupon_restrict.create_time
     * 
     */
    private Date createTime;

    /**
     * 
     * 表字段 : t_coupon_restrict.update_time
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

    public Long getRestrictConditionId() {
        return restrictConditionId;
    }

    public void setRestrictConditionId(Long restrictConditionId) {
        this.restrictConditionId = restrictConditionId;
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