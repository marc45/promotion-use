package com.edu.sky.promotion.service;

import com.edu.sky.promotion.model.PageBean;
import com.edu.sky.promotion.po.entity.Coupon;
import com.edu.sky.promotion.po.entity.CouponCode;

import java.util.List;
import java.util.Map;

/**
 * 优惠券兑换码service
 */
public interface CouponCodeService {

    /**查询分页列表：根据couponCode各种条件查询
     * @param couponCode 优惠码查询类
     * @param pageSize
     * @param pageNum
     * @return
     */
    PageBean<CouponCode> couponCodePage(CouponCode couponCode, Integer pageSize, Integer pageNum);

    /**绑定用户和优惠券关系：自己领取
     * @param couponId 优惠券id
     * @param openId 用户id
     * @return
     */
    Boolean bindCouponCode4User(Long couponId,String openId);

    /**绑定用户和优惠券关系：多优惠码绑定：仅限无限库存优惠券  :未使用
     * @param couponCodeIds 优惠码id集合
     * @param openId 用户openId
     * @return
     */
    List<CouponCode> bindCouponCode4User(List<Long> couponCodeIds,String openId);

    /**绑定用户和优惠券关系：输入优惠码领取优惠券
     * @param couponCode 优惠码
     * @param openId 用户openId
     * @return
     */
    Boolean bindCouponCode4User(String couponCode,String openId);

    /**用户优惠码列表：可以使用，不可使用
     * @param openId 用户openId
     * @param useFlag 0可以使用，1已使用, 2已过期）
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageBean<CouponCode> couponCodePage4User(String openId,Byte useFlag,Integer pageNum,Integer pageSize);

    /**用户使用优惠券购物，更新库存相关字段
     * @param couponCodeId 优惠码id
     * @param openId 用户openId
     * @return
     */
    Boolean userUseCouponCode(Long couponCodeId, String openId);

    /**根据商品id集合，订单总价查询优惠券信息:
     *      查询优惠券类型，限制条件找出符合的优惠券信息列表
     * @param waresIds 商品id集合
     * @param waresPrice 订单总价
     * @param openId 用户opengId
     * @return
     */
    Map<Long,List<CouponCode>> findCouponListByWaresId(List<Long> waresIds, Integer waresPrice, String openId);

    /**新人注册赠送优惠券
     * @param openId
     * @return
     */
    Coupon registerToGiveAwayCoupon(String openId);

    /**登录送优惠券
     * @param openId
     * @return
     */
    Coupon signInToGiveAwayCoupons(String openId);

    /**查询coupon列表：导出Excel使用
     * @param couponId 优惠券id（可选）
     */
    List<CouponCode> findByJoinCoupon(Long couponId);


}
