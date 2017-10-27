//package com.edu.sky.promotion.util;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.LinkedHashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.concurrent.TimeUnit;
//
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.DefaultTypedTuple;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
//import org.springframework.stereotype.Component;
//import org.springside.modules.utils.mapper.JsonMapper;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.SerializationFeature;
//
///**
// *
// * @Title RedisUtil.java
// * @Package com.jd.marathon.common.utils
// * @Description Redis工具类
// * @author yujianliang@igetwell.cn
// * @date 2017年4月26日 上午9:46:38
// * @version V1.0
// */
//@Component
//public class RedisUtil {
//
//    private static Logger logger = LoggerFactory.getLogger(RedisUtil.class);
//
//    private static RedisTemplate<String, String> redisTemplate;
//
//    @Autowired
//    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
//        RedisUtil.redisTemplate = redisTemplate;
//    }
//
//    /* ----------- common --------- */
//    public static Collection<String> keys(String pattern) {
//        return redisTemplate.keys(pattern);
//    }
//
//    public static void delete(String key) {
//        redisTemplate.delete(key);
//    }
//
//    public static void delete(Collection<String> key) {
//        redisTemplate.delete(key);
//    }
//
//    /* ----------- string --------- */
//    public static <T> T get(String key, Class<T> clazz) {
//        String value = redisTemplate.opsForValue().get(key);
//        return parseJson(value, clazz);
//    }
//
//    public static String get(String key) {
//        String value = redisTemplate.opsForValue().get(key);
//        return parseJson(value, String.class);
//    }
//
//    public static <T> T get(String key, TypeReference<T> typeReference) {
//        String value = redisTemplate.opsForValue().get(key);
//        return parseJson(value, typeReference);
//    }
//
//    public static <T> List<T> mget(Collection<String> keys, Class<T> clazz) {
//        List<String> values = redisTemplate.opsForValue().multiGet(keys);
//        return parseJsonList(values, clazz);
//    }
//
//    public static <T> void set(String key, T obj, Long timeout, TimeUnit unit) {
//        if (obj == null) {
//            return;
//        }
//
//        String value = toJson(obj);
//        if (timeout != null) {
//            redisTemplate.opsForValue().set(key, value, timeout, unit);
//        } else {
//            redisTemplate.opsForValue().set(key, value);
//        }
//    }
//
//    public static <T> T getAndSet(String key, T obj, Class<T> clazz) {
//        if (obj == null) {
//            return get(key, clazz);
//        }
//
//        String value = redisTemplate.opsForValue().getAndSet(key, toJson(obj));
//        return parseJson(value, clazz);
//    }
//
//    public static int decrement(String key, int delta) {
//        Long value = redisTemplate.opsForValue().increment(key, -delta);
//        return value.intValue();
//    }
//
//    public static int increment(String key, int delta) {
//        Long value = redisTemplate.opsForValue().increment(key, delta);
//        return value.intValue();
//    }
//
//    /* ----------- list --------- */
//    public static int size(String key) {
//        return redisTemplate.opsForList().size(key).intValue();
//    }
//
//    public static <T> List<T> range(String key, long start, long end, Class<T> clazz) {
//        List<String> list = redisTemplate.opsForList().range(key, start, end);
//        return parseJsonList(list, clazz);
//    }
//
//    public static void rightPushAll(String key, Collection<?> values, Long timeout, TimeUnit unit) {
//        if (values == null || values.isEmpty()) {
//            return;
//        }
//
//        redisTemplate.opsForList().rightPushAll(key, toJsonList(values));
//        if (timeout != null) {
//            redisTemplate.expire(key, timeout, unit);
//        }
//    }
//
//    public static <T> void leftPush(String key, T obj) {
//        if (obj == null) {
//            return;
//        }
//
//        redisTemplate.opsForList().leftPush(key, toJson(obj));
//    }
//
//    public static <T> T leftPop(String key, Class<T> clazz) {
//        String value = redisTemplate.opsForList().leftPop(key);
//        return parseJson(value, clazz);
//    }
//
//    public static void remove(String key, int count, Object obj) {
//        if (obj == null) {
//            return;
//        }
//
//        redisTemplate.opsForList().remove(key, count, toJson(obj));
//    }
//
//    /* ----------- zset --------- */
//    public static int zcard(String key) {
//        return redisTemplate.opsForZSet().zCard(key).intValue();
//    }
//
//    public static <T> List<T> zrange(String key, long start, long end, Class<T> clazz) {
//        Set<String> set = redisTemplate.opsForZSet().range(key, start, end);
//        return parseJsonList(setToList(set), clazz);
//    }
//
//    private static List<String> setToList(Set<String> set) {
//        if (set == null) {
//            return null;
//        }
//        return new ArrayList<String>(set);
//    }
//
//    public static void zadd(String key, Object obj, double score) {
//        if (obj == null) {
//            return;
//        }
//        redisTemplate.opsForZSet().add(key, toJson(obj), score);
//    }
//
//    public static void zaddAll(String key, List<TypedTuple<?>> tupleList, Long timeout, TimeUnit unit) {
//        if (tupleList == null || tupleList.isEmpty()) {
//            return;
//        }
//
//        Set<TypedTuple<String>> tupleSet = toTupleSet(tupleList);
//        redisTemplate.opsForZSet().add(key, tupleSet);
//        if (timeout != null) {
//            redisTemplate.expire(key, timeout, unit);
//        }
//    }
//
//    private static Set<TypedTuple<String>> toTupleSet(List<TypedTuple<?>> tupleList) {
//        Set<TypedTuple<String>> tupleSet = new LinkedHashSet<TypedTuple<String>>();
//        for (TypedTuple<?> t : tupleList) {
//            tupleSet.add(new DefaultTypedTuple<String>(toJson(t.getValue()), t.getScore()));
//        }
//        return tupleSet;
//    }
//
//    public static void zrem(String key, Object obj) {
//        if (obj == null) {
//            return;
//        }
//        redisTemplate.opsForZSet().remove(key, toJson(obj));
//    }
//
//    public static void unionStore(String destKey, Collection<String> keys, Long timeout, TimeUnit unit) {
//        if (keys == null || keys.isEmpty()) {
//            return;
//        }
//
//        Object[] keyArr = keys.toArray();
//        String key = (String) keyArr[0];
//
//        Collection<String> otherKeys = new ArrayList<String>(keys.size() - 1);
//        for (int i = 1; i < keyArr.length; i++) {
//            otherKeys.add((String) keyArr[i]);
//        }
//
//        redisTemplate.opsForZSet().unionAndStore(key, otherKeys, destKey);
//        if (timeout != null) {
//            redisTemplate.expire(destKey, timeout, unit);
//        }
//    }
//
//    /**
//     *
//     * </p>
//     * 判断key是否存在
//     * </p>
//     *
//     * @param key
//     * @return
//     * @author yujianliang@igetwell.cn
//     * @date 2017年4月26日
//     */
//    public static boolean exist(String key) {
//        return redisTemplate.hasKey(key);
//    }
//
//    /**
//     *
//     * </p>
//     * 设置key的存活时间，单位为秒
//     * </p>
//     *
//     * @param key
//     * @param timeout
//     *            存活时间（秒）
//     * @author yujianliang@igetwell.cn
//     * @date 2017年4月26日
//     */
//    public static void expire(String key, long timeout) {
//        redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
//    }
//
//    /**
//     *
//     * </p>
//     * 设置key的存活时间
//     * </p>
//     *
//     * @param key
//     * @param timeout
//     *            存活时间
//     * @param timeUnit
//     *            时间单位
//     * @author yujianliang@igetwell.cn
//     * @date 2017年4月26日
//     */
//    public static void expire(String key, long timeout, TimeUnit timeUnit) {
//        redisTemplate.expire(key, timeout, timeUnit);
//    }
//
//    /* ----------- tool methods --------- */
//    public static String toJson(Object obj) {
//        try {
//            return JsonMapper.INSTANCE.getMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false).writeValueAsString(obj);
//        } catch (JsonProcessingException e) {
//            logger.warn("write to json string error:" + obj, e);
//            return null;
//        }
//    }
//
//    public static <T> T parseJson(String json, Class<T> clazz) {
//        return JsonMapper.INSTANCE.fromJson(json, clazz);
//    }
//
//    public static <T> T parseJson(String json, TypeReference<T> valueType) {
//        if (StringUtils.isEmpty(json)) {
//            return null;
//        }
//
//        try {
//            return JsonMapper.INSTANCE.getMapper().readValue(json, valueType);
//        } catch (IOException e) {
//            logger.warn("parse json string error:" + json, e);
//            return null;
//        }
//    }
//
//    public static List<String> toJsonList(Collection<?> values) {
//        if (values == null) {
//            return null;
//        }
//
//        List<String> result = new ArrayList<String>();
//        for (Object obj : values) {
//            result.add(toJson(obj));
//        }
//        return result;
//    }
//
//    public static <T> List<T> parseJsonList(List<String> list, Class<T> clazz) {
//        if (list == null) {
//            return null;
//        }
//
//        List<T> result = new ArrayList<T>();
//        for (String s : list) {
//            result.add(parseJson(s, clazz));
//        }
//        return result;
//    }
//}
