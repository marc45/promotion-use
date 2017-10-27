package com.edu.sky.promotion.po.entity;

import java.io.Serializable;
import java.util.Date;

public class CouponUser implements Serializable {
    private static final long serialVersionUID = 2099280355192373396L;
    /**
     * 
     * 表字段 : t_coupon_user.id
     * 
     */
    private Long id;

    /**
     * 优惠券code
     * 表字段 : t_coupon_user.coupon_code_id
     * 
     */
    private Long couponCodeId;

    /**
     * 用户code
     * 表字段 : t_coupon_user.user_open_id
     * 
     */
    private String userOpenId;

    /**
     * 
     * 表字段 : t_coupon_user.create_time
     * 
     */
    private Date createTime;

    /**
     * 
     * 表字段 : t_coupon_user.update_time
     * 
     */
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCouponCodeId() {
        return couponCodeId;
    }

    public void setCouponCodeId(Long couponCodeId) {
        this.couponCodeId = couponCodeId;
    }

    public String getUserOpenId() {
        return userOpenId;
    }

    public void setUserOpenId(String userOpenId) {
        this.userOpenId = userOpenId == null ? null : userOpenId.trim();
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