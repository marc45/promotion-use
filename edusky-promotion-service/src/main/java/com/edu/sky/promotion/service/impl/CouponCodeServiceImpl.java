package com.edu.sky.promotion.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.edu.sky.promotion.aop.ParamAsp;
import com.edu.sky.promotion.model.WaresQueryModel;
import com.edu.sky.promotion.po.dao.CouponCodeMapper;
import com.edu.sky.promotion.po.dao.CouponMapper;
import com.edu.sky.promotion.po.dao.CouponUserMapper;
import com.edu.sky.promotion.po.dao.InventoryMapper;
import com.edu.sky.promotion.po.entity.*;
import com.edu.sky.promotion.po.example.CouponCodeExample;
import com.edu.sky.promotion.po.example.InventoryExample;
import com.edu.sky.promotion.service.CouponCodeService;
import com.edu.sky.promotion.util.DateUtils;
import com.edu.sky.promotion.util.PageBean;
import com.edu.sky.promotion.util.RandomCodeUtils;
import com.edu.sky.wares.api.domain.Wares;
import com.edu.sky.wares.api.domain.WaresQuery;
import com.edu.sky.wares.api.service.WaresService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service(version = "1.0",timeout = 15000,retries = 0)
@org.springframework.stereotype.Service
public class CouponCodeServiceImpl implements CouponCodeService {

    private final static Logger logger = LoggerFactory.getLogger(CouponCodeServiceImpl.class);

    @Autowired
    private CouponCodeMapper couponCodeMapper;
    @Autowired
    private CouponUserMapper couponUserMapper;
    @Autowired
    private CouponMapper couponMapper;
    @Autowired
    private InventoryMapper inventoryMapper;
    @Reference(version = "1.0",timeout = 12000)
    private WaresService waresService;

    @Override
    public List<CouponCode> couponCodeList(@ParamAsp("couponCode") CouponCode couponCode) {
        CouponCodeExample example = getExam(couponCode,true);
        return couponCodeMapper.selectByExample(example);
    }

    @Override
    public Object couponCodePage(@ParamAsp("couponCode") CouponCode couponCode,@ParamAsp("pageSize") Integer pageSize
            ,@ParamAsp("pageNum") Integer pageNum) {
        PageBean<CouponCode> pageBean = new PageBean(pageNum,pageSize);
        CouponCodeExample example = getExam(couponCode,true);
        pageBean.setList(couponCodeMapper.selectByPage(example, PageBean.getOffset(pageNum, pageSize), pageSize));
        pageBean.setTotalCount(couponCodeMapper.countByExample(example));
        return pageBean;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object bindCouponCode4User(@ParamAsp("couponId") Long couponId,@ParamAsp("openId") String openId) {
        long start = System.currentTimeMillis();
        Coupon coupon = couponMapper.selectById(couponId);
        if (coupon == null || coupon.getCommonState() != 0) {
            return 100;//该优惠券不可使用
        }
        if (!coupon.getRepeatFlag()) {
            long count = couponCodeMapper.selectByJoinCount(openId, couponId, null);
            if (count > 0) {
                return 103;//该优惠券不可重复领取
            }
        }
        Inventory inventory = inventoryMapper.selectByCouponId(couponId);
        if (inventory == null) {
            return 101;//优惠券错误
        }
        CouponCode couponCode1 = new CouponCode();
        couponCode1.setBindType((byte)2);
        //有库存的优惠券查询一个绑定；没有库存的直接创建一个
        if (coupon.getInventoryFlag()) {
            if (inventory.getTotalAmount() - inventory.getBindCount() == 0) {
                return 102;//该优惠券没有库存了
            }
            CouponCode couponCode = new CouponCode();
            couponCode.setUsedFlag((byte) 0);
            couponCode.setExportFlag(false);
            couponCode.setBindType((byte) 0);
            couponCode.setCouponId(coupon.getId());
            CouponCodeExample example = getExam(couponCode, false);
            List<CouponCode> couponCodes = couponCodeMapper.selectByPage(example, PageBean.getOffset(1, 1), 1);
            if (couponCodes == null || couponCodes.isEmpty()) {
                return false;
            }
            couponCode1 = couponCodes.get(0);
            CouponCodeExample example1 = new CouponCodeExample();
            CouponCodeExample.Criteria criteria = example1.createCriteria();
            criteria.andIdEqualTo(couponCode1.getId());
            criteria.andUpdateTimeEqualTo(couponCode1.getUpdateTime());
            couponCode1.setBindType((byte) 2);
            couponCode1.setBindTime(new Date());
            couponCode1.setUpdateTime(new Date());
            if (coupon.getFixType() == 2) {
                couponCode1.setExpirationTime(DateUtils.getDayEndString(DateUtils.addDay(new Date(), coupon.getExpireDay())));
            }
        }
        Boolean flag = bindCouponCodeAndRefreshInventory(coupon, couponCode1, openId, inventory);
        logger.info("用户自己领取优惠券耗时(毫秒)：" + (System.currentTimeMillis() - start));
        return flag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object bindCouponCode4User(@ParamAsp("couponIds") List<Long> couponIds,@ParamAsp("openId") String openId) {
        long start = System.currentTimeMillis();
        List<Coupon> coupons = couponMapper.selectByIdList(couponIds);
        if (coupons == null || couponIds.isEmpty()) {
            return 100;//优惠券不可用
        }
        List<Inventory> inventories = inventoryMapper.selectByCouponIdList(couponIds);
        List<CouponCode> couponCodes = new ArrayList<>();
        boolean res = false;
        for (Coupon coupon : coupons) {
            CouponCode couponCode = new CouponCode();
            couponCode.setBindType((byte)1);
            for (Inventory inventory : inventories) {
                if(coupon.getId().longValue() == inventory.getCouponId().longValue()){
                    res = bindCouponCodeAndRefreshInventory(coupon, couponCode, openId, inventory);
                }
            }
            if (res) {
                couponCodes.add(couponCode);
            }
        }
        logger.info("用户默认绑定优惠券耗时(耗时)：" + (System.currentTimeMillis() - start));
        return couponCodes;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object bindCouponCode4User(@ParamAsp("code") String code,@ParamAsp("openId") String openId) {
        long start = System.currentTimeMillis();
        CouponCodeExample example = new CouponCodeExample();
        CouponCodeExample.Criteria criteria = example.createCriteria();
        criteria.andCodeEqualTo(code).andBindTypeEqualTo((byte)0);
        List<CouponCode> couponCodes = couponCodeMapper.selectByExample(example);
        if (couponCodes == null || couponCodes.isEmpty()) {
            return 101;//该优惠券已被使用
        }
        CouponCode couponCode = couponCodes.get(0);
        couponCode.setBindType((byte)3);
        Long couponId = couponCode.getCouponId();
        Coupon coupon = couponMapper.selectById(couponId);
        if (coupon == null) {
            return 100;//优惠券不存在
        }
        if (!coupon.getRepeatFlag()) {
            long repeat = couponCodeMapper.selectByJoinCount(openId, couponId, null);
            if (repeat > 0) {
                return 103;//该优惠券不可重复领取
            }
        }
        Inventory inventory = inventoryMapper.selectByCouponId(couponId);
        if (inventory == null) {
            return 101;//优惠券错误
        }
        if (coupon.getInventoryFlag()) {
            if (inventory.getTotalAmount() - inventory.getBindCount() == 0) {
                return 102;//该优惠券没有库存了
            }
        }
        Boolean flag = bindCouponCodeAndRefreshInventory(coupon, couponCode, openId, inventory);
        logger.info("用户输入优惠码绑定优惠券耗时(毫秒)：" + (System.currentTimeMillis() - start));
        return flag;
    }

    @Override
    public Object couponCodePage4User(@ParamAsp("openId") String openId,@ParamAsp("openId") Boolean useFlag
            ,@ParamAsp("pageNum")Integer pageNum,@ParamAsp("pageSize")Integer pageSize) {
        PageBean<CouponCode> pageBean = new PageBean(pageNum,pageSize);
        if (StringUtils.isEmpty(useFlag)) {
            useFlag = false;
        }
        pageBean.setList(couponCodeMapper.selectByJoinPage(openId ,useFlag, pageSize
                ,PageBean.getOffset(pageNum, pageSize)));
        pageBean.setTotalCount(couponCodeMapper.selectByJoinCount(openId,null,null));
        return pageBean;
    }

    @Override
    public Object userUseCouponCode(@ParamAsp("code") String code,@ParamAsp("openId") String openId) {
        long start = System.currentTimeMillis();
        CouponCodeExample example = new CouponCodeExample();
        CouponCodeExample.Criteria criteria = example.createCriteria();
        criteria.andCodeEqualTo(code);
        List<CouponCode> couponCodes = couponCodeMapper.selectByExample(example);
        if (couponCodes == null || couponCodes.isEmpty()) {
            return 102;//该优惠码不存在
        }
        CouponCode couponCode = couponCodes.get(0);
        Long couponId = couponCode.getCouponId();
        Coupon coupon = couponMapper.selectById(couponId);
        if (coupon == null) {
            return 100;//优惠券不存在
        }
        Inventory inventory = inventoryMapper.selectByCouponId(couponId);
        if (inventory == null) {
            return 101;//优惠券错误
        }
        CouponCodeExample example1 = new CouponCodeExample();
        CouponCodeExample.Criteria criteria1 = example.createCriteria();
        CouponCode couponCodeNew = new CouponCode();
        criteria1.andIdEqualTo(couponCode.getId());
        couponCodeNew.setUsedFlag((byte)1);
        couponCodeNew.setUsedTime(new Date());
        couponCodeNew.setUpdateTime(new Date());
        Boolean flag = false;
        flag = couponCodeMapper.updateByExampleSelective(couponCodeNew, example1) == 1;
        if (flag) {
            flag = refreshInventoryAndRetry(flag, coupon, inventory, 1);
        }
        logger.info("userUseCouponCode用户使用优惠券耗时(毫秒)：" + (System.currentTimeMillis() - start));
        return flag;
    }

    @Override
    public Object findCouponListByWaresId(@ParamAsp("waresIds") List<Long> waresIds,@ParamAsp("waresPrice") Integer waresPrice
            ,@ParamAsp("openId") String openId) {
        List<Wares> waresList = waresService.queryWaresListByWaresIds(waresIds);
        if (waresList == null || waresList.isEmpty()) {
            return null;
        }
        //每个商品的productType,gradeCode,subjectCode
        List<WaresQueryModel> waresQueryModels = new ArrayList<>();
        for (Wares wares : waresList) {
            for (Map<String, Object> product : wares.getProducts()) {
                WaresQueryModel waresQueryModel = new WaresQueryModel();
                waresQueryModel.setWaresId(wares.getId());
                waresQueryModel.setProductType(Integer.valueOf(product.get("productType").toString()));
                waresQueryModel.setId(Long.valueOf(product.get("id").toString()));// 产品id
                waresQueryModel.setSubjectCode(product.get("subjectCode").toString());
                waresQueryModel.setGradeCode(product.get("gradeCode").toString());
                waresQueryModels.add(waresQueryModel);
            }
        }
        List<CouponCode> couponCodes = couponCodeMapper.selectConditionByOpenId(openId);
        if (couponCodes == null || couponCodes.isEmpty()) {
            return null;
        }
        Map<Long,List<CouponCode>> map = new HashMap<>();
        //通过商品列表遍历用户优惠码列表
        for (Wares wares : waresList) {
            List<CouponCode> waresCouponCodes = new ArrayList<>();
            for (CouponCode couponCode : couponCodes) {
                List<RestrictCondition> restrictConditions = couponCode.getRestrictConditions();
                boolean flag = judgeRestrictCondition(restrictConditions,waresPrice,waresQueryModels,wares.getId());
                if (flag) {
                    waresCouponCodes.add(couponCode);
                }
            }
            map.put(wares.getId(),waresCouponCodes);
        }

        return map;
    }

    /*--------------------------------------------------------------------------------------------------------------------*/
    private CouponCodeExample getExam(CouponCode couponCode,boolean orderFlag){
        CouponCodeExample example = new CouponCodeExample();
        if (orderFlag) {
            example.setOrderByClause("createTime DESC");
        }
        if (couponCode != null) {
            CouponCodeExample.Criteria criteria = example.createCriteria();
            if (!StringUtils.isEmpty(couponCode.getCouponId())) {
                criteria.andCouponIdEqualTo(couponCode.getCouponId());
            }
            if (!StringUtils.isEmpty(couponCode.getCode())) {
                criteria.andCodeEqualTo(couponCode.getCode().toUpperCase());
            }
            if (!StringUtils.isEmpty(couponCode.getUsedFlag())) {
                criteria.andUsedFlagEqualTo(couponCode.getUsedFlag());
            }
            if (!StringUtils.isEmpty(couponCode.getExportFlag())) {
                criteria.andExportFlagEqualTo(couponCode.getExportFlag());
            }
            if (!StringUtils.isEmpty(couponCode.getBindType())) {
                criteria.andBindTypeEqualTo(couponCode.getBindType());
            }
            if (!StringUtils.isEmpty(couponCode.getExpirationTime())) {
                criteria.andExpirationTimeLessThanOrEqualTo(couponCode.getExpirationTime());
            }
        }
        return example;
    }

    /**绑定优惠码和刷新库存：无论有无库存都需要记录优惠券的使用数据
     * @param coupon
     * @param openId
     * @return
     */
    private boolean bindCouponCodeAndRefreshInventory(Coupon coupon,CouponCode couponCode,String openId
            ,Inventory inventory) {
        boolean flag = false;
        flag = insertOrUpdateCouponCode(couponCode,coupon);
        return buildCouponUserAndRefreshInventory(flag,couponCode,coupon,openId,inventory);
    }

    /**增加或更新优惠码表
     * @param couponCode
     * @param coupon
     * @return
     */
    private boolean insertOrUpdateCouponCode(CouponCode couponCode,Coupon coupon){
        long start = System.currentTimeMillis();
        boolean flag = false;
        if (couponCode == null || couponCode.getId() == null) {
            couponCode.setCreateTime(new Date());
            couponCode.setUpdateTime(new Date());
            couponCode.setUsedFlag((byte)0);
            couponCode.setExportFlag(false);
            couponCode.setBindTime(new Date());
            couponCode.setCouponId(coupon.getId());
            couponCode.setCode(RandomCodeUtils.getCouponCode());
            if (coupon.getFixType() == 2) {
                Date date = DateUtils.addDay(new Date(), coupon.getExpireDay());
                couponCode.setExpirationTime(DateUtils.getDayEndString(date));
            } else {
                couponCode.setExpirationTime(coupon.getEndTime());
            }
            flag = couponCodeMapper.insertSelective(couponCode) == 1;
        } else {
            CouponCodeExample example = new CouponCodeExample();
            CouponCodeExample.Criteria criteria = example.createCriteria();
            CouponCode couponCodeNew = new CouponCode();
            criteria.andIdEqualTo(couponCode.getId());
            couponCodeNew.setBindType(couponCode.getBindType());
            couponCodeNew.setBindTime(new Date());
            couponCodeNew.setUpdateTime(new Date());
            if (coupon.getFixType() == 2) {
                couponCodeNew.setExpirationTime(DateUtils.getDayEndString(DateUtils.addDay(new Date()
                        , coupon.getExpireDay())));
            }
            flag = couponCodeMapper.updateByExampleSelective(couponCodeNew, example) == 1;
        }
        logger.info("insertOrUpdateCouponCode增加或更新优惠码耗时(毫秒)：" + (System.currentTimeMillis() - start));
        return  flag;
    }

    /**增加优惠码和用户关系，刷新库存相关记录
     * @param flag
     * @param couponCode
     * @param coupon
     * @param openId
     * @param inventory
     * @return
     */
    private boolean buildCouponUserAndRefreshInventory(boolean flag,CouponCode couponCode,Coupon coupon,String openId
            ,Inventory inventory) {
        long start = System.currentTimeMillis();
        if (flag) {
            flag = insertCouponUser(couponCode.getId(),openId);
        }
        if (flag) {
            flag = refreshInventoryAndRetry(flag,coupon,inventory,0);
        }
        logger.info("buildCouponUserAndRefreshInventory增加优惠码和用户关系，刷新库存相关记录耗时(毫秒)：" +
                (System.currentTimeMillis() - start));
        if (!flag) {
            throw new RuntimeException("绑定失败！");
        }
        return flag;
    }

    /**添加优惠码和用户关系
     * @param couponCodeId
     * @param openId
     * @return
     */
    private boolean insertCouponUser(Long couponCodeId,String openId){
        CouponUser couponUser = new CouponUser();
        couponUser.setCouponCodeId(couponCodeId);
        couponUser.setUserOpenId(openId);
        couponUser.setCreateTime(new Date());
        couponUser.setUpdateTime(new Date());
        return couponUserMapper.insertSelective(couponUser) == 1;
    }

    /**刷新库存：简单1+2次重试
     * @param flag
     * @param coupon
     * @return
     */
    private boolean refreshInventoryAndRetry(boolean flag,Coupon coupon,Inventory inventory,int useOrbind){
        if (flag) {
            flag = updateInventory(coupon,inventory,useOrbind) == 1;
            //再试2次
            if (!flag) {
                for (int i = 0; i < 2; i++) {
                    logger.info("刷新优惠券库存次数：" + i);
                    if (updateInventory(coupon,inventory,useOrbind) == 1) {
                        break;
                    }
                }
            }
        }
        return flag;
    }

    /**更新绑定的数量
     * @param coupon
     * @param useOrbind 0绑定数量加一，1使用数量加一
     * @return 0 没有更新，1更新成功，2 没有库存，3 库存中没有此优惠券
     */
    private int updateInventory(Coupon coupon,Inventory inventory,int useOrbind) {
        long start = System.currentTimeMillis();
        int res = 0;
        long version = inventory.getVersion();
        InventoryExample inventoryExample = new InventoryExample();
        InventoryExample.Criteria criteria1 = inventoryExample.createCriteria();
        criteria1.andCouponIdEqualTo(coupon.getId());
        criteria1.andVersionEqualTo(version);
        inventory.setVersion(inventory.getVersion() + 1);
        if (useOrbind == 0) {
            inventory.setBindCount(inventory.getBindCount() + 1);
        }else{
            inventory.setUsedCount(inventory.getUsedCount() + 1);
        }
        //没有库存属性
        if (!coupon.getInventoryFlag() || inventory.getTotalAmount() == null) {
            res = inventoryMapper.updateByExampleSelective(inventory, inventoryExample);
            //有库存属性，判断库存量
        }else if (inventory.getTotalAmount() - inventory.getBindCount() > 0){
            res = inventoryMapper.updateByExampleSelective(inventory, inventoryExample);
        }else {
            res = 2;//没有库存
        }
        logger.info("updateInventory更新库存状态耗时(毫秒)：" + (System.currentTimeMillis() - start));
        return res;
    }

    /**判断条件：一个商品对比所有优惠券的限制条件，
     * @param conditions
     * @param waresPrice
     * @param waresQueryModels
     * @param waresId
     * @return
     */
    private boolean judgeRestrictCondition(List<RestrictCondition> conditions,Integer waresPrice
            ,List<WaresQueryModel> waresQueryModels,Long waresId){
        Map<String,Boolean> judgeMap1 = new HashMap<>();
        //一个商品的查询条件，对比所有优惠券限制条件：根据查询条件对象
        for (WaresQueryModel model : waresQueryModels) {
            Map<String,Boolean> judgeMap = new HashMap<>();
            for (RestrictCondition condition : conditions) {
                Boolean flag = false;
                if (condition.getType().byteValue() == 0) {
                    if (condition.getRestrictValue() <= waresPrice) {
                        flag = true;
                    }
                }
                judgeMap.put("price",flag);
                if (condition.getType().byteValue() == 3) {
                    if (model.getWaresId().longValue() == waresId.longValue()) {
                        if (model.getProductType().intValue() == condition.getRestrictValue().intValue()) {
                            flag = true;
                        }
                    }
                    judgeMap.put("productType",flag);
                }
                if (condition.getType().byteValue() == 1) {
                    if (model.getWaresId().longValue() == waresId.longValue()) {
                        if (Integer.valueOf(model.getSubjectCode()).intValue()
                                == condition.getRestrictValue().intValue()) {
                            flag = true;
                        }
                    }
                    judgeMap.put("subjectCode",flag);
                }
                if (condition.getType().byteValue() == 2) {
                    if (model.getWaresId().longValue() == waresId.longValue()) {
                        if (Integer.valueOf(model.getGradeCode()).intValue()
                                == condition.getRestrictValue().intValue()) {
                            flag = true;
                        }
                    }
                    judgeMap.put("gradeCode",flag);
                }
            }
            boolean b = (judgeMap.get("price") != null ? judgeMap.get("price") : true)
                    && (judgeMap.get("productType") != null ? judgeMap.get("productType") : true)
                    && (judgeMap.get("subjectCode") != null ? judgeMap.get("subjectCode") : true)
                    && (judgeMap.get("gradeCode") != null ? judgeMap.get("gradeCode") : true );
            judgeMap1.put(model.getId().toString(),b);
        }
        for (Map.Entry<String, Boolean> entry : judgeMap1.entrySet()) {
            if (entry.getValue()) {
                return true;
            }
        }
        return false;
    }




}
