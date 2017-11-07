package com.edu.sky.promotion.model;

import java.io.Serializable;
import java.util.List;

public class RequestPromotionModel implements Serializable{

    private List<Long> waresIds;//商品ids
    private Integer waresPrice;//商品总价
    private String openId;//用户openId

    public List<Long> getWaresIds() {
        return waresIds;
    }

    public void setWaresIds(List<Long> waresIds) {
        this.waresIds = waresIds;
    }

    public Integer getWaresPrice() {
        return waresPrice;
    }

    public void setWaresPrice(Integer waresPrice) {
        this.waresPrice = waresPrice;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
