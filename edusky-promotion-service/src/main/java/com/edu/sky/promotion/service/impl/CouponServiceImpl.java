package com.edu.sky.promotion.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.edu.sky.promotion.aop.ParamAsp;
import com.edu.sky.promotion.po.dao.CouponCodeMapper;
import com.edu.sky.promotion.po.dao.CouponMapper;
import com.edu.sky.promotion.po.dao.InventoryMapper;
import com.edu.sky.promotion.po.entity.Coupon;
import com.edu.sky.promotion.po.entity.CouponCode;
import com.edu.sky.promotion.po.entity.Inventory;
import com.edu.sky.promotion.po.example.CouponCodeExample;
import com.edu.sky.promotion.po.example.CouponExample;
import com.edu.sky.promotion.po.example.InventoryExample;
import com.edu.sky.promotion.service.CouponService;
import com.edu.sky.promotion.util.DateUtils;
import com.edu.sky.promotion.util.PageBean;
import com.edu.sky.promotion.util.RandomCodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object addCoupon(@ParamAsp("coupon") Coupon coupon) {
        long startTime = System.currentTimeMillis();
        coupon.setCreateTime(new Date());
        coupon.setUpdateTime(new Date());
        coupon.setCommonState((byte) 0);
        Date expirationTime = null;
        //时间范围类型(0固定日期范围,1固定时间范围,2固定天数)
        if (coupon.getFixType() == 0) {
            coupon.setStartTime(DateUtils.getDayStart(coupon.getStartTime()));
            expirationTime = DateUtils.getDayEndString(coupon.getEndTime());
            coupon.setEndTime(expirationTime);
        } else if (coupon.getFixType() == 1) {
            expirationTime = coupon.getEndTime();
        }
        int res = couponMapper.insertSelective(coupon);
        if (res < 1) {
            return null;
        }
        Long id = coupon.getId();
        Inventory inventory = new Inventory();
        inventory.setCouponId(id);
        inventory.setCreateTime(new Date());
        if (coupon.getInventoryFlag() == null || coupon.getInventoryFlag()) {
            inventory.setTotalAmount(coupon.getAmount());
        }
        int res1 = inventoryMapper.insertSelective(inventory);
        List<CouponCode> couponCodes = new ArrayList<>();
        for (int i = 0; i < coupon.getAmount(); i++) {
            CouponCode couponCode = new CouponCode();
            couponCode.setCouponId(id);
            couponCode.setExportFlag(false);
            couponCode.setExpirationTime(expirationTime);
            couponCode.setCreateTime(new Date());
            couponCode.setUpdateTime(new Date());
            couponCode.setUsedFlag((byte) 0);
            couponCode.setCode(RandomCodeUtils.getCouponCode());
            couponCodes.add(couponCode);
        }
        if (!couponCodes.isEmpty()) {
            int res2 = couponCodeMapper.insertList(couponCodes);
            if (res2 == couponCodes.size()) {
                long endTimes = System.currentTimeMillis();
                logger.info("批量插入数据耗时(毫秒)：" + (endTimes - startTime));
                return coupon.getId();
            } else {
                throw new RuntimeException("批量插入数据出现异常");
            }
        }
        //后续:抢红包或者秒杀系列放入redis中

        return coupon.getId();
    }


    @Override//只能追加数量和延长时间,修改个人是否能够重复领取
    @Transactional
    public Boolean updateAddToCoupon(@ParamAsp("coupon") Coupon coupon) {
        coupon.setUpdateTime(new Date());
        boolean flag = false;
        Coupon coupon1 = couponMapper.selectById(coupon.getId());
        if (coupon1 == null && coupon1.getId() == null) {
            return flag;
        }
        //数量追加
        if (coupon.getAmount() != null && coupon.getAmount() > 0 ) {
            coupon1.setAmount(coupon1.getAmount() + coupon.getAmount());
        }
        //个人是否能够重复领取 更改
        if (coupon.getRepeatFlag() != null) {
            coupon1.setRepeatFlag(coupon.getRepeatFlag());
        }
        //时间更改
        Date expirationTime = null;
        if (coupon.getFixType() == 0) {
            if (coupon.getStartTime() != null) {
                coupon1.setStartTime(DateUtils.getDayStart(coupon.getStartTime()));
            }
            if (coupon.getEndTime() != null) {
                expirationTime = DateUtils.getDayEndString(coupon.getEndTime());
                coupon1.setEndTime(expirationTime);
            }
        } else if (coupon.getFixType() == 1) {
            if (coupon.getStartTime() != null) {
                coupon1.setStartTime(coupon.getStartTime());
            }
            if (coupon.getEndTime() != null) {
                expirationTime = coupon.getEndTime();
                coupon1.setEndTime(expirationTime);
            }
        } else if (coupon.getFixType() == 2) {
            if (coupon.getExpireDay() != null && coupon.getExpireDay() > 0) {
                coupon1.setExpireDay(coupon.getExpireDay());
            }
        }
        int res = 0;
        flag = couponMapper.addToUpdateCoupon(coupon1) == 1;
        if (flag) {
            //追加优惠券库存表总库存数量
            flag = addtoAmountInventory(coupon);
            if (!flag) {
                for (int i = 0; i < 2; i++) {
                    flag = addtoAmountInventory(coupon);
                    if (flag) {
                        break;
                    }
                }
            }
        }
        if (flag) {
            //更新该优惠券生成的部分优惠码的到期时间
            CouponCode couponCode = new CouponCode();
            if (expirationTime != null) {
                couponCode.setExpirationTime(expirationTime);
            }
            CouponCodeExample example = new CouponCodeExample();
            CouponCodeExample.Criteria criteria = example.createCriteria();
            criteria.andCouponIdEqualTo(coupon1.getId()).andExportFlagEqualTo(false);
            int res2 = couponCodeMapper.updateByExampleSelective(couponCode, example);
            if (res2 > 0) {
                flag = true;
            }
        }
        if (!flag) {
            throw new RuntimeException("优惠券追加失败!");
        }
        return flag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean changeActiveState(@ParamAsp("couponId") Long couponId,@ParamAsp("activeState") Integer activeState) {
        CouponExample example = new CouponExample();
        CouponExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(couponId);
        Coupon coupon = new Coupon();
        coupon.setId(couponId);
        coupon.setCommonState(activeState.byteValue());
        coupon.setUpdateTime(new Date());
        return couponMapper.updateByExampleSelective(coupon, example) == 1;
    }

    @Override
    public List<Coupon> couponList(@ParamAsp("coupon") Coupon coupon) {
        CouponExample example = new CouponExample();
        CouponExample.Criteria criteria = example.createCriteria();
        criteria.andCommonStateEqualTo((byte) 0);
        return couponMapper.selectByExample(example);
    }

    @Override
    public Object couponPage(@ParamAsp("coupon") Coupon coupon,@ParamAsp("pageSize") Integer pageSize
            ,@ParamAsp("pageNum") Integer pageNum) {
        PageBean<Coupon> pageBean = new PageBean<>(pageNum,pageSize);
        pageBean.setList(couponMapper.selectByPage(coupon, pageSize, PageBean.getOffset(pageNum, pageSize)));
        pageBean.setTotalCount(couponMapper.selectByPageCount(coupon));
        return pageBean;
    }

    @Override
    public Coupon findCouponByCodeOrCouponCode(String code, Long couponCodeId) {
        if (code == null && couponCodeId == null) {
            return null;
        }
        return couponMapper.selectJoinByCodeOrCouponCodeId(code,couponCodeId);
    }

/*--------------------------------------------------------------------------------------------------------------------*/
    /**追加数量到优惠券的库存
     * @param coupon
     * @return
     */
    private boolean addtoAmountInventory(Coupon coupon){
        Inventory inventory = inventoryMapper.selectByCouponId(coupon.getId());
        Long version = inventory.getVersion();
        InventoryExample example = new InventoryExample();
        InventoryExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(inventory.getId()).andVersionEqualTo(version);
        Inventory inventory1 = new Inventory();
        inventory.setTotalAmount(inventory.getTotalAmount() + coupon.getAmount());
        inventory.setVersion(version + 1);
        return inventoryMapper.updateByExampleSelective(inventory1, example) == 1;
    }

}
