package com.edu.sky.promotion.po.dao;

import com.edu.sky.promotion.po.entity.CouponUser;
import com.edu.sky.promotion.po.example.CouponUserExample;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponUserMapper extends BaseMapper<CouponUser, CouponUserExample, Integer> {
}