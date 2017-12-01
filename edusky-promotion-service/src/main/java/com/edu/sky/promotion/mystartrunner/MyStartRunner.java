package com.edu.sky.promotion.mystartrunner;

import com.edu.sky.promotion.model.CouponUserAndInventoryModel;
import com.edu.sky.promotion.po.dao.InventoryMapper;
import com.edu.sky.promotion.service.impl.AsyncTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyStartRunner implements CommandLineRunner{

    /*@Autowired
    private DataSource dataSource;
    @Autowired
    private CouponUserMapper couponUserMapper;

    ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
    //这里的path是相对路径
    private void dataSQL(String path){
        populator.setScripts(new Resource[] { new ClassPathResource(path) });
        DatabasePopulatorUtils.execute(populator, dataSource);
    }*/
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private AsyncTask asyncTask;
    @Autowired
    private InventoryMapper inventoryMapper;
    @Value("${couponUserAndInventoryQueue}")
    private String couponUserAndInventoryQueue;
    //备用
    @Value("${couponUsed}")
    private String couponUsed;
    @Value("${couponBind}")
    private String couponBind;

    @Override
    public void run(String... strings) throws Exception {
       /* List<CouponUser> couponUsers = couponUserMapper.selectByExample(new CouponUserExample());
        System.err.println("---查询表：" + couponUsers.size());
        dataSQL("initSQL/t_coupon_user.sql");
        System.err.println("----------------------------/初始化表结束/-----------------------------------");*/

        //备用：优惠券库存信息在redis中丢失的情况使用！
//        InventoryExample example = new InventoryExample();
//        InventoryExample.Criteria criteria = example.createCriteria();
//        List<Inventory> inventoryList = inventoryMapper.selectByExample(example);
//        inventoryList.forEach(inventory -> {
//            redisTemplate.opsForHash().put(couponBind,inventory.getCouponId(),inventory.getBindCount());
//            redisTemplate.opsForHash().put(couponUsed,inventory.getCouponId(),inventory.getUsedCount());
//        });

        //监控redis队列：建立优惠券和用户关系，刷新优惠券库存相关信息
        Thread redisListener = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Object obj = redisTemplate.opsForList().leftPop(couponUserAndInventoryQueue);
                        if (obj == null) {
                            log.debug(couponUserAndInventoryQueue + "队列为空！");
                            Thread.sleep(1000);
                        } else {
                            asyncTask.couponUserAndRefreshInventory((CouponUserAndInventoryModel) obj);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        redisListener.setDaemon(true);
        redisListener.start();



    }

}
