package com.edu.sky.promotion.po.dao;

import com.edu.sky.promotion.po.entity.CouponCode;
import com.edu.sky.promotion.po.example.CouponCodeExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponCodeMapper extends BaseMapper<CouponCode, CouponCodeExample, Long> {

    int insertList(List<CouponCode> couponCodes);

    /**根据openId查询用户的优惠码列表：分页
     * @param openId 用户openId
     * @param couponId 优惠券Id
     * @param usedFlag 使用标识（0没有使用，1已使用，2过期或不可使用 ）
     * @param limit
     * @param offset
     * @return
     */
    List<CouponCode> selectByJoinPage(@Param("openId") String openId,@Param("couponId") Long couponId
            ,@Param("usedFlag") Boolean usedFlag,@Param("limit") Integer limit, @Param("offset") Integer offset);

    /** selectByJoinPage 总数查询
     * @param openId 用户openId
     * @param couponId 优惠券Id
     * @param usedFlag 使用标识（0没有使用，1已使用，2过期或不可使用 ）
     * @return
     */
    long selectByJoinCount(@Param("openId") String openId,@Param("couponId") Long couponId,@Param("usedFlag") Boolean usedFlag);
}