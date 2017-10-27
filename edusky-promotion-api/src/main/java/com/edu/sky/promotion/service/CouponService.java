package com.edu.sky.promotion.service;

import com.edu.sky.promotion.po.entity.Coupon;

import java.util.List;

/**优惠券基本操作service
 * @author songwei 2017-09-25
 */
public interface CouponService {

    /**增加优惠券
     * @param coupon
     * @return
     */
    Object addCoupon(Coupon coupon) ;

    /**更改优惠券：只能追加数量和延长时间,修改个人是否能够重复领取
     * @param coupon
     * @return
     */
    Boolean updateAddToCoupon(Coupon coupon) ;

    /**更新优惠券状态
     * @param couponId
     * @param activeState ０正常使用，１停用
     * @return
     */
    Boolean changeActiveState(Long couponId, Integer activeState);

    /**优惠券列表
     * @param coupon
     * @return
     */
    List<Coupon> couponList(Coupon coupon);

    /**优惠券分页查询
     * @param pageSize
     * @param pageNum
     * @param coupon
     * @return
     */
    Object couponPage(Coupon coupon, Integer pageSize, Integer pageNum);


    /**根据优惠码code或者id查询coupon
     * @param code 优惠码code；可选，不选则写null
     * @param couponCodeId 优惠码id；可选，不选则写null
     * @return
     */
    Coupon findCouponByCodeOrCouponCode(String code,Long couponCodeId);

}
