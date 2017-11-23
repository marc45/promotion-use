package com.edu.sky.promotion.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.edu.sky.promotion.po.entity.Coupon;
import com.edu.sky.promotion.po.entity.CouponCode;
import com.edu.sky.promotion.service.CouponCodeService;
import com.edu.sky.promotion.service.CouponService;
import com.edu.sky.promotion.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/promotion")
public class PromotionController{

    private static Logger logger = LoggerFactory.getLogger(PromotionController.class);

    @Reference(version = "1.0",timeout = 30000,retries = 0,check = false,url = "dubbo://192.168.1.163:33053")
    private CouponService couponService;
    @Reference(version = "1.0",timeout = 30000,retries = 0,check = false,url = "dubbo://192.168.1.163:33053")
    private CouponCodeService couponCodeService;
    @Reference(version = "1.0",timeout = 30000,retries = 0,check = false,url = "dubbo://192.168.1.163:33053")
    private InventoryService inventoryService;

    @GetMapping(value = "/{name}")
    public String testController(@PathVariable("name") String code){
        return code;
    }

    @PostMapping(value = "/addCoupon")
    public Object addCoupon(@RequestBody Coupon coupon){
//        Coupon coupon = JSON.parseObject(requestBody,Coupon.class);
        if (coupon == null) {
            return "coupon 不能为空";
        }
        if (coupon.getType() == null) {
            return "coupon 类型 不能为空";
        } else {
            if (coupon.getType().byteValue() == 4 || coupon.getType().byteValue() == 5) {
                if (coupon.getInventoryFlag()) {
                    return "coupon 类型为 注册优惠券或登录优惠券时 库存标识 不能为true";
                }
                if (coupon.getFixType().byteValue() != 2) {
                    return "coupon 类型为 注册优惠券或登录优惠券时 时间范围标识 只能是固定天数";
                }
                if (coupon.getOnLineFlag().byteValue() != 1) {
                    return "coupon 类型为 注册优惠券或登录优惠券时 线上线下标识 只能是线上";
                }
                if (!coupon.getRepeatFlag()) {
                    return "coupon 类型为 注册优惠券或登录优惠券时 重复标识 只能是可以重复领取";
                }
                if (coupon.getHomeShow()) {
                    return "coupon 类型为 注册优惠券或登录优惠券时 界面显示标识 只能是不可显示";
                }
            }
        }
        if (coupon.getName() == null) {
            return "coupon 名称 不能为空";
        }
        if (coupon.getFaceValue() == null) {
            return "coupon 面值 不能为空";
        }
        if (coupon.getAmount() == null) {
            return "coupon 总数量 不能为空";
        }
        if (coupon.getRepeatFlag() == null) {
            return "coupon 是否重复领取标识 不能为空";
        }
        if (coupon.getOnLineFlag() == null) {
            return "coupon 线上线下标识 不能为空";
        }
        if (coupon.getHomeShow() == null) {
            return "coupon 界面显示标识 不能为空";
        }
        if (coupon.getRestrictFlag() == null) {
            return "coupon 限制条件标识 不能为空";
        } else {
            if (coupon.getRestrictConditions() == null || coupon.getRestrictConditions().isEmpty()) {
                return "coupon 限制条件列表 不能为空";
            }
        }
        if (coupon.getFixType() == null) {
            return "coupon 时间范围标识 不能为空";
        } else {
            if (coupon.getFixType().byteValue() == 2) {
                if (coupon.getExpireDay() == null) {
                    return "coupon 固定天数 不能为空";
                }
            } else {
                if (coupon.getStartTime() == null || coupon.getEndTime() == null) {
                    return "coupon 开始时间 or 结束时间 不能为空";
                }
                if (coupon.getEndTime().compareTo(coupon.getStartTime()) != 1) {
                    return "coupon 结束时间 不能小于 开始时间";
                }
            }
        }
        Object addCoupon = couponService.addCoupon(coupon);
        return addCoupon;
    }

    @GetMapping(value = "/couponPage")
    public Object couponPage(Coupon coupon,@RequestParam Integer pageSize,@RequestParam Integer pageNum){
        Object couponPage = couponService.couponPage(coupon, pageSize, pageNum);
        return couponPage;
    }
    @GetMapping(value = "/bindCouponCodeBySelf")//自己领取绑定
    public Object bindCouponCodeBySelf(@RequestParam Map<String,Object> param){
        long start = System.currentTimeMillis();
        Long couponId = Long.valueOf(param.get("couponId").toString());
        String openId = param.get("openId").toString();
        Object o = couponCodeService.bindCouponCode4User(couponId, openId);
        logger.info("bindCouponCodeBySelf自己领取绑定耗时(毫秒)：" + (System.currentTimeMillis() - start));
        return o;
    }
    @GetMapping(value = "/bindCouponCodeByCode")//输入优惠码绑定
    public Object bindCouponCodeByCode(@RequestParam Map<String,Object> param){
        long start = System.currentTimeMillis();
        String code = param.get("code").toString();
        String openId = param.get("openId").toString();
        Object o = couponCodeService.bindCouponCode4User(code, openId);
        logger.info("bindCouponCodeByCode输入优惠码绑定耗时(毫秒)：" + (System.currentTimeMillis() - start));
        return o;
    }
    @GetMapping(value = "/bindCouponCodeBySystem")//系统绑定
    public Object bindCouponCodeBySystem(@RequestParam Map<String,Object> param){
        long start = System.currentTimeMillis();
        Long[] couponIds = (Long[]) param.get("couponIds");
        String openId = param.get("openId").toString();
        Object o = couponCodeService.bindCouponCode4User(Arrays.asList(couponIds),openId);
        logger.info("bindCouponCodeBySystem系统绑定耗时(毫秒)：" + (System.currentTimeMillis() - start));
        return o;
    }
    @GetMapping(value = "/couponCodeListForUser")
    public Object userCouponCodeList(String openId){
        Object page4User = couponCodeService.couponCodePage4User(openId, (byte)0,1, 10);
        return page4User;
    }
    @GetMapping(value = "/couponInventoryList")
    public Object couponInventoryList(){
        return inventoryService.inventoryPage(1,10);
    }
    @GetMapping(value = "/useCouponCode")
    public Object useCouponCode(@RequestParam Long couponCodeId,@RequestParam String openId){
        return couponCodeService.userUseCouponCode(couponCodeId,openId);
    }



}
