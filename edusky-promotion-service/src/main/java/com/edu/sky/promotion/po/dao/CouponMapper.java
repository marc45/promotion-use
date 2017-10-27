package com.edu.sky.promotion.po.dao;

import com.edu.sky.promotion.po.entity.Coupon;
import com.edu.sky.promotion.po.example.CouponExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponMapper extends BaseMapper<Coupon, CouponExample, Long> {

    /**根据id查询
     * @param id
     * @return
     */
    Coupon selectById(Long id);

    /**追加更新优惠券：只能更新时间和追加数量
     * @param coupon
     * @return
     */
    int addToUpdateCoupon(Coupon coupon);

    /**分页查询
     * @param coupon 查询实体
     * @param limit
     * @param offset
     * @return
     */
    List<Coupon> selectByPage(@Param("record") Coupon coupon, @Param("limit") Integer limit, @Param("offset") Integer offset);

    /**分页查询总数
     * @param coupon
     * @return
     */
    long selectByPageCount(@Param("record") Coupon coupon);

    /**根据couponId集合查询优惠券
     * @param couponIds
     * @return
     */
    List<Coupon> selectByIdList(List<Long> couponIds);

    /**根据优惠码code或者id查询优惠券信息
     * @param code
     * @param couponCodeId
     * @return
     */
    Coupon selectJoinByCodeOrCouponCodeId(String code, Long couponCodeId);
}