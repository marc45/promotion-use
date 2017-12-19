package com.edu.sky.promotion.po.dao;

import com.edu.sky.promotion.po.entity.CouponCode;
import com.edu.sky.promotion.po.example.CouponCodeExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponCodeMapper extends BaseMapper<CouponCode, CouponCodeExample, Long> {

    /**批量添加优惠码
     * @param couponCodes
     * @return
     */
    int insertList(List<CouponCode> couponCodes);

    /**分页查询
     * @param couponCode
     * @param limit
     * @param offset
     * @return
     */
    List<CouponCode> selectAndConditionByPage(@Param("record") CouponCode couponCode,@Param("limit") Integer limit
            , @Param("offset") Integer offset);

    long selectAndConditionByPageCount(@Param("record") CouponCode couponCode);

    /**根据openId查询用户的优惠码列表：分页
     * @param openId 用户openId
     * @param usedFlag 使用标识（0没有使用，1已使用，2过期或不可使用 ）
     * @param limit
     * @param offset
     * @return
     */
    List<CouponCode> selectByJoinPage(@Param("openId") String openId,@Param("usedFlag") Byte usedFlag
            ,@Param("limit") Integer limit, @Param("offset") Integer offset);

    /** selectByJoinPage 总数查询
     * @param openId 用户openId
     * @param couponId 优惠券Id
     * @param usedFlag 使用标识（0没有使用，1已使用，2过期或不可使用      ）
     * @return
     */
    long selectByJoinCount(@Param("openId") String openId,@Param("couponId") Long couponId,@Param("usedFlag") Byte usedFlag);

    /**查询个人优惠券的使用条件
     * @param openId 用户openId
     * @return
     */
    List<CouponCode> selectConditionByOpenId(@Param("openId") String openId);

    /**批量修改
     * @param couponId
     * @return
     */
    long updateListExpirationTime(@Param("couponId") Long couponId,@Param("expireDay") Integer expireDay);

    /**查询优惠码信息：附带优惠券名称，面值等信息
     * @param couponCode 查询实体
     * @return
     */
    List<CouponCode> selectByJoinCoupon(CouponCode couponCode);

    List<CouponCode> selectByCouponIdAndUseable(Long couponId);

    /**excel 导出后更新字段
     * @param couponId
     * @param bindType
     * @return
     */
    long updateExcel(@Param("couponId") Long couponId,@Param("bindType") Byte bindType);
}