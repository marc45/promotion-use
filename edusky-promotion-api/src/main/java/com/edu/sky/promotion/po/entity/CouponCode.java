package com.edu.sky.promotion.po.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CouponCode implements Serializable {
    private static final long serialVersionUID = -635717719130049934L;
    /**
     * 
     * 表字段 : t_coupon_code.id
     * 
     */
    private Long id;

    /**
     * 优惠券id
     * 表字段 : t_coupon_code.coupon_id
     * 
     */
    private Long couponId;
    private String couponName;

    /**
     * 优惠券兑换码
     * 表字段 : t_coupon_code.code
     * 
     */
    private String code;

    /**
     * 使用标识（0没有使用，1已使用，2过期或不可使用 ）
     * 表字段 : t_coupon_code.used_flag
     * 
     */
    private Byte usedFlag;

    /**
     * 0未导出;1已导出
     * 表字段 : t_coupon_code.export_flag
     * 
     */
    private Boolean exportFlag;

    /**
     * 0教师绑定;1自己领取绑定;2输入优惠码绑定;3系统自动绑定
     * 表字段 : t_coupon_code.bind_type
     * 
     */
    private Byte bindType;

    /**
     * 绑定时间
     * 表字段 : t_coupon_code.bind_time
     * 
     */
    private Date bindTime;

    /**
     * 使用时间
     * 表字段 : t_coupon_code.used_time
     * 
     */
    private Date usedTime;

    /**
     * 到期时间
     * 表字段 : t_coupon_code.expiration_time
     * 
     */
    private Date expirationTime;

    /**
     * 
     * 表字段 : t_coupon_code.create_time
     * 
     */
    private Date createTime;

    /**
     * 
     * 表字段 : t_coupon_code.update_time
     * 
     */
    private Date updateTime;
    private Double faceValue;

    private String openId;//用户code
    private Byte couponApplicationType;//使用类型
    private List<RestrictCondition> restrictConditions;//优惠券使用条件集合

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public Byte getUsedFlag() {
        return usedFlag;
    }

    public void setUsedFlag(Byte usedFlag) {
        this.usedFlag = usedFlag;
    }

    public Boolean getExportFlag() {
        return exportFlag;
    }

    public void setExportFlag(Boolean exportFlag) {
        this.exportFlag = exportFlag;
    }

    public Byte getBindType() {
        return bindType;
    }

    public void setBindType(Byte bindType) {
        this.bindType = bindType;
    }

    public Date getBindTime() {
        return bindTime;
    }

    public void setBindTime(Date bindTime) {
        this.bindTime = bindTime;
    }

    public Date getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(Date usedTime) {
        this.usedTime = usedTime;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
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

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public List<RestrictCondition> getRestrictConditions() {
        return restrictConditions;
    }

    public void setRestrictConditions(List<RestrictCondition> restrictConditions) {
        this.restrictConditions = restrictConditions;
    }

    public Byte getCouponApplicationType() {
        return couponApplicationType;
    }

    public void setCouponApplicationType(Byte couponApplicationType) {
        this.couponApplicationType = couponApplicationType;
    }

    public Double getFaceValue() {
        return faceValue;
    }

    public void setFaceValue(Double faceValue) {
        this.faceValue = faceValue;
    }
}