package com.edu.sky.promotion.service.impl;

import com.alibaba.fastjson.JSON;
import com.edu.sky.promotion.model.CouponUserAndInventoryModel;
import com.edu.sky.promotion.po.dao.CouponUserMapper;
import com.edu.sky.promotion.po.dao.InventoryMapper;
import com.edu.sky.promotion.po.entity.CouponCode;
import com.edu.sky.promotion.po.entity.CouponUser;
import com.edu.sky.promotion.po.entity.Inventory;
import com.edu.sky.promotion.po.example.CouponUserExample;
import com.edu.sky.promotion.po.example.InventoryExample;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class AsyncTask {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private CouponUserMapper couponUserMapper;
    @Autowired
    private InventoryMapper inventoryMapper;

    @Async
    @Transactional(rollbackFor = Exception.class)
    public void couponUserAndRefreshInventory(CouponUserAndInventoryModel model) {
        buildCouponUserAndRefreshInventory(model.getFlag(),model.getCouponCodeId(),model.getCouponId()
                ,model.getOpenId(),model.getInventory());
    }

    public boolean buildCouponUserAndRefreshInventory(boolean flag, Long couponCodeId, Long couponId, String openId
            , Inventory inventory) {
        try {
            long start = System.currentTimeMillis();
                if (flag) {
                    flag = insertCouponUser(couponCodeId,openId);
                }
                if (flag) {
                    refreshInventoryAndRetry(flag,couponId,inventory,0);
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
    public void refreshInventoryAndRetry(boolean flag,Long couponId,Inventory inventory,int useOrbind){
        if (flag) {
            InventoryExample inventoryExample = new InventoryExample();
            InventoryExample.Criteria criteria1 = inventoryExample.createCriteria();
            criteria1.andCouponIdEqualTo(couponId);
            Inventory inventory1 = new Inventory();
            if (useOrbind == 0) {
                redisTemplate.opsForHash().increment("couponBind",couponId,1);
                Integer counter = (Integer) redisTemplate.opsForHash().get("couponBind", couponId);
                inventory1.setBindCount(counter.longValue());
            }else{
                redisTemplate.opsForHash().increment("couponUsed",couponId,1);
                inventory1.setUsedCount((Long)redisTemplate.opsForHash().get("couponUsed", couponId));
            }
            inventoryMapper.updateByExampleSelective(inventory1, inventoryExample);
        }

    }


}
