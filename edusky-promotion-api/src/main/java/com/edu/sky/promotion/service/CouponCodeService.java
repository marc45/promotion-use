package com.edu.sky.promotion.service;

import com.edu.sky.promotion.po.entity.CouponCode;

import java.util.List;

public interface CouponCodeService {

    /**查询列表
     * @param couponCode 优惠码查询类
     * @return
     */
    List<CouponCode> couponCodeList(CouponCode couponCode);

    /**查询分页列表
     * @param couponCode 优惠码查询类
     * @param pageSize
     * @param pageNum
     * @return
     */
    Object couponCodePage(CouponCode couponCode,Integer pageSize,Integer pageNum);

    /**绑定用户和优惠券关系：自己领取
     * @param couponId 优惠券id
     * @param openId 用户id
     * @return
     */
    Object bindCouponCode4User(Long couponId,String openId);

    /**绑定用户和优惠券关系：多优惠码绑定：仅限无限库存优惠券
     * @param couponCodeIds 优惠码id集合
     * @param openId 用户openId
     * @return
     */
    Object bindCouponCode4User(List<Long> couponCodeIds,String openId);

    /**绑定用户和优惠券关系：输入优惠码领取优惠券
     * @param couponCode 优惠码
     * @param openId 用户openId
     * @return
     */
    Object bindCouponCode4User(String couponCode,String openId);

    /**用户优惠码列表：已绑定没有使用的，已过期的
     * @param openId
     * @return
     */
    Object couponCodePage4User(String openId,Integer pageNum,Integer pageSize);

    /**用户使用优惠券购物，更新库存相关字段
     * @param code
     * @param openId
     * @return
     */
    Object userUseCouponCode(String code, String openId);


}
