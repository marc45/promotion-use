package com.edu.sky.promotion.service.impl;

import com.edu.sky.promotion.po.dao.CouponCodeMapper;
import com.edu.sky.promotion.po.entity.CouponCode;
import com.edu.sky.promotion.po.example.CouponCodeExample;
import com.edu.sky.promotion.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 定时删除（物理删除）过期优惠码
 */
@Slf4j
@Component
@EnableScheduling
public class ScheduledTask {

    @Autowired
    private CouponCodeMapper couponCodeMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Value("${currentCouponCodeQueue}")
    private String currentCouponCodeQueue;

    //10 * * * * ?:每分钟10秒执行一次
    //0 0 1 * * ?
    @Scheduled(cron = "0 0 1 * * ?")//每天凌晨1点执行
    public void timeToDealWithDelete(){
        delAndUpdateCouponCode();
        log.info("定时器执行过期优惠券删除和更新~~~~~~~");
    }

    /**
     * 删除，更新
     */
    private void delAndUpdateCouponCode(){
        Date dayEnd = DateUtils.getDayEnd(DateUtils.addDay(new Date(), -1));
        CouponCodeExample example = new CouponCodeExample();
        CouponCodeExample.Criteria criteria = example.createCriteria();
        criteria.andBindTypeEqualTo((byte) 0).andExpirationTimeIsNotNull()
                .andExpirationTimeLessThanOrEqualTo(dayEnd);
        List<CouponCode> couponCodes = couponCodeMapper.selectByExample(example);
        if (Objects.nonNull(couponCodes) && !couponCodes.isEmpty()) {
            delRedisCouponCode(couponCodes);
            couponCodeMapper.deleteByExample(example);
        }

        CouponCode couponCode = new CouponCode();
        couponCode.setUsedFlag((byte)2);
        couponCode.setUpdateTime(new Date());
        CouponCodeExample example1 = new CouponCodeExample();
        CouponCodeExample.Criteria criteria1 = example1.createCriteria();
        criteria1.andBindTypeNotEqualTo((byte)0).andUsedFlagEqualTo((byte) 0).andExpirationTimeIsNotNull()
                .andExpirationTimeLessThanOrEqualTo(dayEnd);
        couponCodeMapper.updateByExampleSelective(couponCode,example1);
    }

    /**
     * 清除redis缓存中的数据
     */
    private void delRedisCouponCode(List<CouponCode> couponCodes){

        if (Objects.nonNull(couponCodes) && !couponCodes.isEmpty()) {
            Set<Long> couponIds = couponCodes.parallelStream()
                    .map(c -> c.getCouponId())
                    .collect(Collectors.toSet());
            Set<String> ids = couponIds.parallelStream()
                    .map(id -> currentCouponCodeQueue + id)
                    .collect(Collectors.toSet());
            redisTemplate.delete(ids);
        }
    }

}
