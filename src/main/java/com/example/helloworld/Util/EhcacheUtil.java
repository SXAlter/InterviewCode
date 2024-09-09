package com.example.helloworld.Util;

import com.alibaba.fastjson.TypeReference;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Ehcache 工具类
 */
public class EhcacheUtil {
    private EhcacheUtil() {
    }

    private final static Logger logger= LoggerFactory.getLogger(EhcacheUtil.class);


    //初始化Ehcache配置
    private static CacheConfiguration<String, String> usesConfiguredInCacheConfig = CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class,
            ResourcePoolsBuilder.newResourcePoolsBuilder().heap(10, MemoryUnit.KB)
                    .offheap(10, MemoryUnit.MB)).withSizeOfMaxObjectGraph(1000)
            .withSizeOfMaxObjectSize(1000, MemoryUnit.B)
            .withExpiry(Expirations.timeToLiveExpiration(Duration.of(5, TimeUnit.MINUTES))) //失效时间30分钟
            .build();
    //初始化管理器
    private static CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
            .withDefaultSizeOfMaxObjectSize(500, MemoryUnit.B)
            .withDefaultSizeOfMaxObjectGraph(2000)
            .withCache("usesConfiguredInCache", usesConfiguredInCacheConfig)
            .build(true);
    //获取cache对象
    private static Cache<String, String> usesConfiguredInCache = cacheManager.getCache("usesConfiguredInCache", String.class, String.class);

    /**
     * @param key key值
     * @param value value值
     * @return void 返回类型
     * @throws @Title: putIntoCache
     * @Description:
     */
    public static void putIntoCache(String key, Object value) {
        usesConfiguredInCache.put(key, JsonUtil.toJson(value));
    }

    /**
     * @param key
     * @param clazz
     * @return T 返回类型
     * @throws @Title: getFromCache
     * @Description: 返回java对象
     */
    public static <T> T getFromCache(String key, Class<T> clazz) {
        return JsonUtil.parseObject(usesConfiguredInCache.get(key), clazz);
    }

    public static <T> T getFromCache(String key, Type type) {
        return JsonUtil.parseObject(usesConfiguredInCache.get(key), type);
    }

    public static <T> T getFromCache(String key, TypeReference<T> typeReference) {
        return JsonUtil.parseObject(usesConfiguredInCache.get(key), typeReference);
    }

    public static void removeFromCache(String key) {
        if (usesConfiguredInCache.containsKey(key)) {
            usesConfiguredInCache.remove(key);
            return;
        }
        logger.info("key[{}]不存在", key);
    }

    /**
     * @param key
     * @param clazz
     * @param
     * @return List<T> 返回类型
     * @throws @Title: getListFromCache
     * @Description: 返回list对象
     */
    public static <T> List<T> getListFromCache(String key, Class<T> clazz) {
        return JsonUtil.parseJsonList(usesConfiguredInCache.get(key), clazz);
    }

    /**
     * @return void 返回类型
     * @throws @Title: clear
     * @Description:清除ehcache缓存数据
     */
    public static void clear() {
        usesConfiguredInCache.clear();
    }

    /**
     * @param
     * @return void 返回类型
     * @throws @Title: close
     * @Description: 关闭管理器
     */
    public static void close() {
        cacheManager.close();
    }
}
