package com.edu.sky.promotion.po.entity;

import java.io.Serializable;
import java.util.Date;

public class RestrictCondition implements Serializable {
    private static final long serialVersionUID = -939312278873094577L;
    /**
     * 
     * 表字段 : t_restrict_condition.id
     * 
     */
    private Long id;

    /**
     * 限制条件描述
     * 表字段 : t_restrict_condition.describe
     * 
     */
    private String description;

    /**
     * 类型(0最低价格，1学科，2学年，3其他待定)
     * 表字段 : t_restrict_condition.type
     * 
     */
    private Byte type;

    /**
     * 限制内容(最低使用价格等):字段类型待定
     * 表字段 : t_restrict_condition.restrict_value
     * 
     */
    private Integer restrictValue;

    /**
     * 
     * 表字段 : t_restrict_condition.create_time
     * 
     */
    private Date createTime;

    /**
     * 
     * 表字段 : t_restrict_condition.update_time
     * 
     */
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Integer getRestrictValue() {
        return restrictValue;
    }

    public void setRestrictValue(Integer restrictValue) {
        this.restrictValue = restrictValue;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}