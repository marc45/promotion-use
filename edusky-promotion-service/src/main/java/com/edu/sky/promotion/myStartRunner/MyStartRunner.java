package com.edu.sky.promotion.myStartRunner;

import com.edu.sky.promotion.po.dao.CouponMapper;
import com.edu.sky.promotion.po.dao.CouponUserMapper;
import com.edu.sky.promotion.po.entity.Coupon;
import com.edu.sky.promotion.po.entity.CouponUser;
import com.edu.sky.promotion.po.example.CouponExample;
import com.edu.sky.promotion.po.example.CouponUserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

//@Component
public class MyStartRunner implements CommandLineRunner{

    @Autowired
    private DataSource dataSource;
    @Autowired
    private CouponUserMapper couponUserMapper;

    ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
    //这里的path是相对路径
    private void dataSQL(String path){
        populator.setScripts(new Resource[] { new ClassPathResource(path) });
        DatabasePopulatorUtils.execute(populator, dataSource);
    }

    @Override
    public void run(String... strings) throws Exception {
        List<CouponUser> couponUsers = couponUserMapper.selectByExample(new CouponUserExample());
        System.err.println("---查询表：" + couponUsers.size());
        dataSQL("initSQL/t_coupon_user.sql");
        System.err.println("----------------------------/初始化表结束/-----------------------------------");

    }
}
