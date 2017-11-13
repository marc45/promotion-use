package com.edu.sky.promotion.myStartRunner;

import com.alibaba.fastjson.JSON;
import com.edu.sky.promotion.model.CouponUserAndInventoryModel;
import com.edu.sky.promotion.po.entity.CouponCode;
import com.edu.sky.promotion.po.entity.Inventory;
import com.edu.sky.promotion.service.impl.AsyncTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;

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
    @Value("${couponUserAndInventoryQueue}")
    private String couponUserAndInventoryQueue;

    @Override
    public void run(String... strings) throws Exception {
       /* List<CouponUser> couponUsers = couponUserMapper.selectByExample(new CouponUserExample());
        System.err.println("---查询表：" + couponUsers.size());
        dataSQL("initSQL/t_coupon_user.sql");
        System.err.println("----------------------------/初始化表结束/-----------------------------------");*/

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
