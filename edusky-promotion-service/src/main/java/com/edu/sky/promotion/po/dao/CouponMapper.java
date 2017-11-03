package com.edu.sky.promotion.po.dao;

import com.edu.sky.promotion.po.entity.Coupon;
import com.edu.sky.promotion.po.example.CouponExample;
import com.edu.sky.wares.api.domain.Wares;
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

    /**分页查询
     * @param coupon 查询实体
     * @param limit
     * @param offset
     * @return
     */
    List<Coupon> selectByPage(@Param("record") Coupon coupon, @Param("offset") Integer offset, @Param("limit") Integer limit);

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

    /**根据couponId查询coupon信息：包含库存和限制条件等
     * @param couponId
     * @return
     */
    Coupon selectByIdJoinInventoryAndConditions(Long couponId);
}