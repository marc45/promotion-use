package com.edu.sky.promotion.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.edu.sky.promotion.aop.ParamAsp;
import com.edu.sky.promotion.model.PageBean;
import com.edu.sky.promotion.po.dao.InventoryMapper;
import com.edu.sky.promotion.po.example.InventoryExample;
import com.edu.sky.promotion.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;

@Service(version = "1.0",retries = 0)
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryMapper inventoryMapper;

    @Override
    public Object inventoryPage(@ParamAsp("pageNum") Integer pageNum,@ParamAsp("pageSize") Integer pageSize) {
        PageBean pageBean = new PageBean(pageNum,pageSize);
        InventoryExample example = new InventoryExample();
        example.setOrderByClause("create_time DESC");
        pageBean.setList(inventoryMapper.selectByPage(example, PageBean.getOffset(pageNum, pageSize), pageSize));
        pageBean.setTotalCount(inventoryMapper.countByExample(example));
        return pageBean;
    }
}
