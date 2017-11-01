package com.edu.sky.promotion.po.dao;

import com.edu.sky.promotion.po.entity.RestrictCondition;
import com.edu.sky.promotion.po.example.RestrictConditionExample;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestrictConditionMapper extends BaseMapper<RestrictCondition, RestrictConditionExample, Integer> {

    /**批量增加优惠券限制条件
     * @param restrictConditions
     * @return
     */
    int insertList(List<RestrictCondition> restrictConditions);
}