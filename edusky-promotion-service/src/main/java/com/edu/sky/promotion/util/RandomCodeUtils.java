package com.edu.sky.promotion.util;

import java.util.Random;
import java.util.UUID;

public class RandomCodeUtils {

    public static String getCouponCode() {
        String str = UUID.randomUUID().toString().toString().replace("-","").trim().toString();
        return str.substring(0, 16).toUpperCase();
    }
    /**
     * 生成随机 6位字符串
     *
     * @param len
     * @return
     */
    public static String getRandom(int len) {
        len = len == 0 ? 6 : len;
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        int maxPos = chars.length;
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            sb = sb.append(chars[random.nextInt(maxPos)]);
        }
        return sb.toString();
    }

}
