package com.edu.sky.promotion.po.entity;

import java.io.Serializable;
import java.util.Date;

public class Coupon implements Serializable {
    private static final long serialVersionUID = 6123064388003116678L;
    /**
     * id
     * 表字段 : t_coupon.id
     * 
     */
    private Long id;

    /**
     * 名称(暂定使用)
     * 表字段 : t_coupon.name
     * 
     */
    private String name;

    /**
     * 1普通优惠券，2打折扣券
     * 表字段 : t_coupon.type
     * 
     */
    private Byte type;

    /**
     * 优惠券面值（可以为折扣，优惠的价格）
     * 表字段 : t_coupon.face_value
     * 
     */
    private Double faceValue;

    /**
     * 是否有库存0否，1是
     * 表字段 : t_coupon.inventory_flag
     * 
     */
    private Boolean inventoryFlag;

    /**
     * 是否有限制条件(0无，1有):默认无
     * 表字段 : t_coupon.restrict_flag
     * 
     */
    private Boolean restrictFlag;

    /**
     * 使用类型（0通用，1其他带扩展）
     * 表字段 : t_coupon.application_type
     * 
     */
    private Byte applicationType;

    /**
     * 固定类型(0固定日期范围,1固定时间范围,2固定天数)
     * 表字段 : t_coupon.fix_type
     * 
     */
    private Byte fixType;

    /**
     * 优惠券开始时间
     * 表字段 : t_coupon.start_time
     * 
     */
    private Date startTime;

    /**
     * 优惠券结束时间
     * 表字段 : t_coupon.end_time
     * 
     */
    private Date endTime;

    /**
     * 有效天数
     * 表字段 : t_coupon.expire_day
     * 
     */
    private Short expireDay;

    /**
     * 描述或者使用结果描述
     * 表字段 : t_coupon.description
     * 
     */
    private String description;

    /**
     * 0正常，1停用，2删除，3用光了
     * 表字段 : t_coupon.common_state
     * 
     */
    private Byte commonState;

    /**
     * 0线下，1线上
     * 表字段 : t_coupon.on_line_flag
     * 
     */
    private Byte onLineFlag;

    /**
     * 是否可以单人重复领取相同类型的优惠券0不可，1可以
     * 表字段 : t_coupon.repeat_flag
     */
    private Boolean repeatFlag;

    /**
     * 创建时间
     * 表字段 : t_coupon.create_time
     * 
     */
    private Date createTime;

    /**
     * 修改时间
     * 表字段 : t_coupon.update_time
     * 
     */
    private Date updateTime;

    //库存总量
    private Long amount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Double getFaceValue() {
        return faceValue;
    }

    public void setFaceValue(Double faceValue) {
        this.faceValue = faceValue;
    }

    public Boolean getInventoryFlag() {
        return inventoryFlag;
    }

    public void setInventoryFlag(Boolean inventoryFlag) {
        this.inventoryFlag = inventoryFlag;
    }

    public Boolean getRestrictFlag() {
        return restrictFlag;
    }

    public void setRestrictFlag(Boolean restrictFlag) {
        this.restrictFlag = restrictFlag;
    }

    public Byte getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(Byte applicationType) {
        this.applicationType = applicationType;
    }

    public Byte getFixType() {
        return fixType;
    }

    public void setFixType(Byte fixType) {
        this.fixType = fixType;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Short getExpireDay() {
        return expireDay;
    }

    public void setExpireDay(Short expireDay) {
        this.expireDay = expireDay;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Byte getCommonState() {
        return commonState;
    }

    public void setCommonState(Byte commonState) {
        this.commonState = commonState;
    }

    public Byte getOnLineFlag() {
        return onLineFlag;
    }

    public void setOnLineFlag(Byte onLineFlag) {
        this.onLineFlag = onLineFlag;
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

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Boolean getRepeatFlag() {
        return repeatFlag;
    }

    public void setRepeatFlag(Boolean repeatFlag) {
        this.repeatFlag = repeatFlag;
    }
}