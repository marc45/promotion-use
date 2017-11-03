package com.edu.sky.promotion.service;

import com.edu.sky.promotion.po.entity.Coupon;

import java.util.List;

/**
 * 优惠券基本操作service
 */
public interface CouponService {

    /**增加优惠券
     * 添加优惠券时注意：需要你前端坐下限制，请求不过去
     *   faceValue  为int类型，输入输出时你需要做处理
     *   type ：1普通优惠券,2打折扣券,3满减券,4新人注册优惠券,5每日登录赠送代金券
     *   type为2时faceValue 为折扣注意
     *   type为 4或者5 时：inventroyFlag设为0，fix_type设为2，onLineFlag设为1，repeatFlag为（4时0,5时1），homeShow设为0
     *   fix_type：时间类型(0固定日期范围,1固定时间范围,2固定天数)
     *   fix_type为0和1时，startTime，endTime有值；为2时expireDay有值
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
     * @param changeType 1追加数量，2更改过期时间Integer changeType
     * @return
     */
    Object updateAddToCoupon(Coupon coupon,Integer changeType) ;

    /**更新优惠券上下架状态
     * @param couponId
     * @param commonState 0未上架，1上架，2下架
     * @return
     */
    Boolean changeActiveState(Long couponId, Integer commonState);

    /**优惠券分页查询
     * @param pageSize
     * @param pageNum
     * @param coupon
     * @return
     */
    Object couponPage(Coupon coupon, Integer pageSize, Integer pageNum);

    /**查找界面显示的优惠券列表：界面显示优惠券，线上，已上架：供客户端使用
     * @return
     */
    List<Coupon> findCouponByHomeShow();

}
