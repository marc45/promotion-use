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

    /**查询优惠券信息：本身信息，库存信息，限制条件等信息
     * @param couponId
     * @return
     */
    Coupon findCouponInfo(Long couponId);

    /**更改优惠券：只能追加数量和延长时间,修改个人是否能够重复领取
     * @param coupon
     * @return
     */
    Object updateAddToCoupon(Coupon coupon) ;

    /**更新优惠券上下架状态
     * @param couponId
     * @param commonState 0未上架，1上架，2下架
     * @return
     */
    Boolean changeActiveState(Long couponId, Integer commonState);

    /**优惠券列表：
     * @param coupon 参数：
     *               CommonState:0未上架，1上架，2下架
     *               RepeatFlag:是否有限制条件(0无，1有)
     *               InventoryFlag:是否有库存0否，1是
     *               FixType:固定类型(0固定日期范围,1固定时间范围,2固定天数)
     *               Type:1普通优惠券,2打折扣券,3满减券,4新人注册优惠券,5每日登录赠送代金券
     *               OnLineFlag:0线下，1线上
     *               HomeShow:是否界面显示(0否,1是)
     *               ApplicationType:领取方式（0通用,1首页领取,2注册领取,3登录领取）
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

    /**查找界面显示的优惠券列表：界面显示优惠券必须无库存，线上，已上架，
     * @return
     */
    List<Coupon> findCouponByHomeShow();

}
