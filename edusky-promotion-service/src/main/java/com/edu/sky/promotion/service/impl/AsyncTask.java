package com.edu.sky.promotion.service.impl;

import com.edu.sky.promotion.model.CouponUserAndInventoryModel;
import com.edu.sky.promotion.po.dao.CouponUserMapper;
import com.edu.sky.promotion.po.dao.InventoryMapper;
import com.edu.sky.promotion.po.entity.CouponUser;
import com.edu.sky.promotion.po.entity.Inventory;
import com.edu.sky.promotion.po.example.InventoryExample;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Service
public class AsyncTask {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private CouponUserMapper couponUserMapper;
    @Autowired
    private InventoryMapper inventoryMapper;
    @Value("${couponUsed}")
    private String couponUsed;
    @Value("${couponBind}")
    private String couponBind;

    @Async
    @Transactional(rollbackFor = Exception.class)
    public void couponUserAndRefreshInventory(CouponUserAndInventoryModel model) {
        buildCouponUserAndRefreshInventory(model.getFlag(),model.getCouponCodeId(),model.getCouponId()
                ,model.getOpenId());
    }

    public boolean buildCouponUserAndRefreshInventory(boolean flag, Long couponCodeId, Long couponId, String openId) {
        try {
            long start = System.currentTimeMillis();
                if (flag) {
                    flag = insertCouponUser(couponCodeId,openId);
                }
                if (flag) {
                    refreshInventoryAndRetry(flag,couponId,0);
                }
            log.info("buildCouponUserAndRefreshInventory增加优惠码和用户关系，刷新库存相关记录耗时(毫秒)：" +
                    (System.currentTimeMillis() - start));
            if (!flag) {
                //如果绑定失败，把该优惠码放回该优惠券的队列中
//              throw new AsyncException(15390,"绑定失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    private boolean insertCouponUser(Long couponCodeId,String openId){
        CouponUser couponUser = new CouponUser();
        couponUser.setCouponCodeId(couponCodeId);
        couponUser.setUserOpenId(openId);
        couponUser.setCreateTime(new Date());
        couponUser.setUpdateTime(new Date());
        return couponUserMapper.insertSelective(couponUser) == 1;
    }
    @Async
    public void refreshInventoryAndRetry(boolean flag,Long couponId,int useOrbind){
        if (flag) {
            usedAndBindFlag(couponId);
            InventoryExample inventoryExample = new InventoryExample();
            InventoryExample.Criteria criteria1 = inventoryExample.createCriteria();
            criteria1.andCouponIdEqualTo(couponId);
            Inventory inventory1 = new Inventory();
            if (useOrbind == 0) {
                redisTemplate.opsForHash().increment(couponBind,couponId,1);
                Integer couponBindCount = (Integer) redisTemplate.opsForHash().get(couponBind, couponId);
                inventory1.setBindCount(couponBindCount.longValue());
            }else{
                redisTemplate.opsForHash().increment(couponUsed,couponId,1);
                Integer couponUsedCount = (Integer) redisTemplate.opsForHash().get(couponUsed, couponId);
                inventory1.setUsedCount(couponUsedCount.longValue());
            }
            inventoryMapper.updateByExampleSelective(inventory1, inventoryExample);
        }

    }
    private void usedAndBindFlag(Long couponId){
        Object bind = redisTemplate.opsForHash().get(couponBind, couponId);
        Object used = redisTemplate.opsForHash().get(couponUsed, couponId);
        if (bind == null || used == null) {
            Inventory inventory = inventoryMapper.selectByCouponId(couponId);
            redisTemplate.opsForHash().put(couponBind,couponId,inventory.getBindCount());
            redisTemplate.opsForHash().put(couponUsed,couponId,inventory.getUsedCount());
        }

    }


}
