package com.edu.sky.promotion.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.edu.sky.promotion.aop.ParamAsp;
import com.edu.sky.promotion.po.dao.*;
import com.edu.sky.promotion.po.entity.*;
import com.edu.sky.promotion.po.example.CouponCodeExample;
import com.edu.sky.promotion.po.example.CouponExample;
import com.edu.sky.promotion.po.example.InventoryExample;
import com.edu.sky.promotion.service.CouponService;
import com.edu.sky.promotion.util.DateUtils;
import com.edu.sky.promotion.util.PageBean;
import com.edu.sky.promotion.util.RandomCodeUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service(version = "1.0",timeout = 30000,retries = 0)
@org.springframework.stereotype.Service
public class CouponServiceImpl implements CouponService {

    private static Logger logger = LoggerFactory.getLogger(CouponServiceImpl.class);

    @Autowired
    private CouponMapper couponMapper;
    @Autowired
    private CouponCodeMapper couponCodeMapper;
    @Autowired
    private InventoryMapper inventoryMapper;
    @Autowired
    private RestrictConditionMapper restrictConditionMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object addCoupon(@ParamAsp("coupon") Coupon coupon) {
        long startTime = System.currentTimeMillis();
        coupon.setCreateTime(new Date());
        coupon.setUpdateTime(new Date());
        coupon.setCommonState((byte) 0);
        if (coupon.getType().byteValue() == 4 || coupon.getType().byteValue() == 5) {
            coupon.setInventoryFlag(false);
            coupon.setFixType((byte)2);
            coupon.setOnLineFlag((byte)1);
            coupon.setHomeShow(false);
            coupon.setRepeatFlag(coupon.getFixType().byteValue() == 5 ? true : false);
        }
        //时间范围类型(0固定日期范围,1固定时间范围,2固定天数)
        Date expirationTime = null;
        if (coupon.getFixType().byteValue() == 0) {
            coupon.setStartTime(DateUtils.getDayStart(coupon.getStartTime()));
            expirationTime = DateUtils.getDayEndString(coupon.getEndTime());
            coupon.setEndTime(expirationTime);
            coupon.setExpireDay(null);
        } else if (coupon.getFixType().byteValue() == 1) {
            expirationTime = coupon.getEndTime();
            coupon.setExpireDay(null);
        } else if(coupon.getFixType().byteValue() == 2){
            coupon.setStartTime(null);
            coupon.setEndTime(null);
        }
        boolean flag = couponMapper.insertSelective(coupon) == 1;
        if (!flag) {
            return false;
        }
        Long id = coupon.getId();
        Inventory inventory = new Inventory();
        inventory.setCouponId(id);
        inventory.setCreateTime(new Date());
        if (coupon.getInventoryFlag()) {
            inventory.setTotalAmount(coupon.getAmount());
        }
        if (inventoryMapper.insertSelective(inventory) != 1) {
            throw new RuntimeException("优惠码增加库存失败!");
        }
        if (coupon.getRestrictFlag()) {
            int res1 = inventoryMapper.insertSelective(inventory);
            List<RestrictCondition> restrictConditions = coupon.getRestrictConditions();
            restrictConditions.forEach(cond -> {
                cond.setCouponId(id);
                cond.setCreateTime(new Date());
                cond.setUpdateTime(new Date());
            });
            int res3 = restrictConditionMapper.insertList(restrictConditions);
            if (res3 != restrictConditions.size()) {
                throw new RuntimeException("优惠码增加限制条件失败!");
            }
        }
        List<CouponCode> couponCodes = new ArrayList<>();
        for (int i = 0; i < coupon.getAmount(); i++) {
            CouponCode couponCode = new CouponCode();
            couponCode.setCouponId(id);
            couponCode.setExportFlag(false);
            couponCode.setExpirationTime(expirationTime);
            couponCode.setCreateTime(new Date());
            couponCode.setUpdateTime(new Date());
            couponCode.setUsedFlag((byte) 0);
            couponCode.setBindType((byte)0);
            couponCode.setCode(RandomCodeUtils.getCouponCode());
            couponCodes.add(couponCode);
        }
        if (!couponCodes.isEmpty()) {
            int res2 = couponCodeMapper.insertList(couponCodes);
            if (res2 == couponCodes.size()) {
                long endTimes = System.currentTimeMillis();
                logger.info("批量优惠码增加数据耗时(毫秒)：" + (endTimes - startTime));
                return coupon.getId();
            } else {
                throw new RuntimeException("批量优惠码增加数据失败！");
            }
        }
        return coupon.getId();
    }

    @Override
    public Coupon findCouponInfo(Long couponId) {
        return couponMapper.selectByIdJoinInventoryAndConditions(couponId);
    }

    /**只能追加数量和延长时间,修改个人是否能够重复领取;
     * 追加逻辑（暂定）：
     *      1：追加优惠券数量：更新有限库存数量；批量增加追加的优惠码
     *      2：延长优惠码过期时间：更新该优惠券下所有以产生优惠码的过期时间：根据fixType类型判断修改字段，修改没有使用的优惠码
     */
    @Override//只能追加数量和延长时间,修改个人是否能够重复领取
    @Transactional(rollbackFor = Exception.class)
    public Object updateAddToCoupon(@ParamAsp("coupon") Coupon coupon,@ParamAsp("changeType") Integer changeType) {
        boolean flag = false;
        CouponExample example = new CouponExample();
        CouponExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(coupon.getId()).andDelFlagEqualTo(false);
        List<Coupon> couponList = couponMapper.selectByExample(example);
        if (couponList == null || couponList.isEmpty()) {
            return "此优惠券无效!";
        }
        Coupon coupon1 = couponList.get(0);
        if (coupon1.getCommonState().byteValue() == 1) {
            return "追加前请先把优惠券下架!";
        }
        //优惠券数量追加
        if (changeType.intValue() == 1) {
            if (!coupon1.getInventoryFlag()) {
                return "此为无限库存优惠券不支持库存修改!";
            }
            if (coupon.getAmount() == null || coupon.getAmount() <= 0) {
                return "追加数量不能为空或者小于等于0!";
            }
            return addToAmount(coupon1,coupon.getAmount());
        }
        //优惠券时间更改
        if (changeType.intValue() == 2) {
            if (coupon.getFixType() == null) {
                return "时间类型不能为空";
            }
            if (coupon1.getFixType().byteValue() != coupon.getFixType().byteValue()) {
                return "原优惠券时间类型和输入时间类型不匹配！";
            }
            return addToTime(coupon1, coupon);
        }
        return flag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean changeActiveState(@ParamAsp("couponId") Long couponId,@ParamAsp("commonState") Integer commonState) {
        if (couponId == null || commonState == null) {
            return false;
        }
        CouponExample example = new CouponExample();
        CouponExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(couponId);
        Coupon coupon = new Coupon();
        coupon.setId(couponId);
        coupon.setCommonState(commonState.byteValue());
        coupon.setUpdateTime(new Date());
        return couponMapper.updateByExampleSelective(coupon, example) == 1;
    }

    @Override
    public Object couponPage(@ParamAsp("coupon") Coupon coupon,@ParamAsp("pageSize") Integer pageSize
            ,@ParamAsp("pageNum") Integer pageNum) {
        PageBean<Coupon> pageBean = new PageBean<>(pageNum,pageSize);
        CouponExample example = getExample(coupon, true);
        pageBean.setList(couponMapper.selectByPage(example, PageBean.getOffset(pageNum, pageSize),pageSize));
        pageBean.setTotalCount(couponMapper.countByExample(example));
        return pageBean;
    }

    @Override
    public List<Coupon> findCouponByHomeShow() {
        CouponExample example = new CouponExample();
        CouponExample.Criteria criteria = example.createCriteria();
        criteria.andDelFlagEqualTo(false).andCommonStateEqualTo((byte) 1)
                .andOnLineFlagEqualTo((byte) 1)
                .andHomeShowEqualTo(true);
        return couponMapper.selectByExample(example);
    }

/*--------------------------------------------------------------------------------------------------------------------*/

    private CouponExample getExample(Coupon coupon,boolean orderFlag){
        CouponExample example = new CouponExample();
        if (orderFlag) {
            example.setOrderByClause("create_time DESC");
        }
        if (coupon != null) {
            CouponExample.Criteria criteria = example.createCriteria();
            criteria.andDelFlagEqualTo(false);
            if (coupon.getCommonState() != null) {
                criteria.andCommonStateEqualTo(coupon.getCommonState());
            }
            if (coupon.getRepeatFlag() != null ) {
                criteria.andRepeatFlagEqualTo(coupon.getRepeatFlag());
            }
            if (coupon.getInventoryFlag() != null) {
                criteria.andInventoryFlagEqualTo(coupon.getInventoryFlag());
            }
            if (coupon.getFixType() != null) {
                criteria.andFixTypeEqualTo(coupon.getFixType());
            }
            if (coupon.getType() != null) {
                criteria.andTypeEqualTo(coupon.getType());
            }
            if (coupon.getOnLineFlag() != null) {
                criteria.andOnLineFlagEqualTo(coupon.getOnLineFlag());
            }
            if (coupon.getHomeShow() != null) {
                criteria.andHomeShowEqualTo(coupon.getHomeShow());
            }
            if (coupon.getApplicationType() != null) {
                criteria.andApplicationTypeEqualTo(coupon.getApplicationType());
            }
        }
        return example;
    }

    /**更改优惠券信息：追加数量，或 调整绑定数量
     * @param coupon
     * @param bindOrAmount 0绑定数量加一，1使用数量加一
     * @return
     */
    private boolean addtoAmountInventory(Coupon coupon,int bindOrAmount){
        Inventory inventory = inventoryMapper.selectByCouponId(coupon.getId());
        Long version = inventory.getVersion();
        InventoryExample example = new InventoryExample();
        InventoryExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(inventory.getId()).andVersionEqualTo(version);
        Inventory inventory1 = new Inventory();
        if (bindOrAmount == 1) {
            inventory1.setTotalAmount(inventory.getTotalAmount() + coupon.getAmount());
        }
        if (bindOrAmount == 0) {
            inventory1.setBindCount(inventory.getBindCount() + 1);
        }
        inventory1.setVersion(version + 1);
        return inventoryMapper.updateByExampleSelective(inventory1, example) == 1;
    }

    /**优惠券追加数量
     * @param coupon1
     * @param amount
     * @return
     */
    private boolean addToAmount(Coupon coupon1,Long amount){
        boolean flag = false;
        //时间范围类型(0固定日期范围,1固定时间范围,2固定天数)
        List<CouponCode> couponCodes = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            CouponCode couponCode = new CouponCode();
            couponCode.setCouponId(coupon1.getId());
            couponCode.setExportFlag(false);
            couponCode.setExpirationTime(coupon1.getEndTime());
            couponCode.setCreateTime(new Date());
            couponCode.setUpdateTime(new Date());
            couponCode.setUsedFlag((byte) 0);
            couponCode.setBindType((byte)0);
            couponCode.setCode(RandomCodeUtils.getCouponCode());
            couponCodes.add(couponCode);
        }
        if (!couponCodes.isEmpty()) {
            int res2 = couponCodeMapper.insertList(couponCodes);
            if (res2 == couponCodes.size()) {
                flag = true;
            } else {
                throw new RuntimeException("批量优惠码增加数据失败！");
            }

        }
        if (flag) {
            //追加优惠券库存表总库存数量
            Coupon coupon = new Coupon();
            coupon.setId(coupon1.getId());
            coupon.setAmount(amount);
            flag = addtoAmountInventory(coupon,1);
            if (!flag) {
                for (int i = 0; i < 2; i++) {
                    flag = addtoAmountInventory(coupon,1);
                    if (flag) {
                        break;
                    }
                }
            }
        }
        return flag;
    }

    /**优惠券修改过期时间
     * @param coupon1
     * @param coupon
     * @return
     */
    private boolean addToTime(Coupon coupon1,Coupon coupon){
        boolean flag = false;
        Date expirationTime = null;
        if (coupon.getFixType().byteValue() == 0) {
            if (coupon.getStartTime() != null) {
                coupon1.setStartTime(DateUtils.getDayStart(coupon.getStartTime()));
            }
            if (coupon.getEndTime() != null) {
                expirationTime = DateUtils.getDayEndString(coupon.getEndTime());
                coupon1.setEndTime(expirationTime);
            }
        } else if (coupon.getFixType().byteValue() == 1) {
            if (coupon.getStartTime() != null) {
                coupon1.setStartTime(coupon.getStartTime());
            }
            if (coupon.getEndTime() != null) {
                expirationTime = coupon.getEndTime();
                coupon1.setEndTime(expirationTime);
            }
        } else if (coupon.getFixType().byteValue() == 2) {
            if (coupon.getExpireDay() != null && coupon.getExpireDay() > 0) {
                Integer expireDay = coupon1.getExpireDay() + coupon.getExpireDay();
                coupon1.setExpireDay(expireDay.shortValue());
            }
        }
        coupon1.setUpdateTime(new Date());
        CouponExample example1 = new CouponExample();
        CouponExample.Criteria criteria1 = example1.createCriteria();
        criteria1.andIdEqualTo(coupon1.getId());
        flag = couponMapper.updateByExampleSelective(coupon1,example1) == 1;
        if (flag) {
            //更新该优惠券生成的部分优惠码的到期时间
            if (expirationTime != null) {
                CouponCode couponCode = new CouponCode();
                CouponCodeExample example = new CouponCodeExample();
                CouponCodeExample.Criteria criteria = example.createCriteria();
                criteria.andCouponIdEqualTo(coupon1.getId());
                couponCode.setExpirationTime(expirationTime);
                flag = couponCodeMapper.updateByExampleSelective(couponCode, example) > 0;
            }
            if (coupon1.getExpireDay() != null) {
                couponCodeMapper.updateListExpirationTime(coupon.getId(),coupon.getExpireDay().intValue());
            }
        }
        return flag;
    }

}
