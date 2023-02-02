package com.kpz.normandy.demo.caffeine;

import com.github.benmanes.caffeine.cache.*;
import org.assertj.core.util.Lists;
import org.checkerframework.checker.index.qual.NonNegative;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class CaffeineTest {

    @Test
    public void testInvalidate() {
        Cache<String, DataObject> cache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .maximumSize(100)
                .build();

        String key = "A";

        // 获取，如果没有，返回null
        DataObject dataObject = cache.getIfPresent(key);

        System.out.println(dataObject);

        // 获取，如果没有，加载
        System.out.println(cache.get(key, k -> DataObject.get("Data for A")));

        // 此时缓存里已加载
        System.out.println(cache.getIfPresent(key));

        // 失效key
        cache.invalidate(key);

        System.out.println(cache.getIfPresent(key));

    }

    @Test
    public void testLoad() {
        // 预置get方法获取不到的处理方式
        LoadingCache<String, DataObject> cache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .maximumSize(100)
                .build(k -> DataObject.get("Data for " + k));

        DataObject dataObject = cache.get("A");

        System.out.println(dataObject);

        // 按key的集合获取，返回map
        System.out.println(cache.getAll(Lists.newArrayList("A", "B", "C")));

    }

    @Test
    public void testAsync() {
        // 异步加载
        AsyncCache<String, DataObject> cache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .maximumSize(100)
                .buildAsync(k -> DataObject.get("Data for " + k));

        // 此时get返回的是Future，通过get获取，通过thenAccept进行后续操作
        cache.get("A", x -> DataObject.get("Data for " + x)).thenAccept(System.out::println);

    }

    @Test
    public void testSizeEviction(){
        // 预置get方法获取不到的处理方式
        LoadingCache<String, DataObject> cache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .maximumSize(1)
                .build(k -> DataObject.get("Data for " + k));

        System.out.println(cache.get("A"));

        System.out.println(cache.estimatedSize());

        System.out.println(cache.get("B"));

        // eviction是异步过程，主动调用这个方法可以立刻执行
        cache.cleanUp();

        System.out.println(cache.estimatedSize());

    }

    @Test
    public void testWeigher(){
        LoadingCache<String, DataObject> cache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .maximumWeight(10)
                .weigher((k,v) -> 5)
                .build(k -> DataObject.get("Data for " + k));

        System.out.println(cache.get("A"));

        System.out.println(cache.estimatedSize());

        System.out.println(cache.get("B"));

        // eviction是异步过程，主动调用这个方法可以立刻执行
        cache.cleanUp();

        System.out.println(cache.estimatedSize());

        System.out.println(cache.get("C"));
        cache.cleanUp();

        System.out.println(cache.estimatedSize());

    }

    @Test
    public void testTimeEviction(){
        // 第一种，未访问多长时间后eviction
        LoadingCache<String, DataObject> cache1 = Caffeine.newBuilder()
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .build(k -> DataObject.get("Data for " + k));

        // 第二种，写入多长时间后eviction
        LoadingCache<String, DataObject> cache2 = Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .weakKeys()
                .weakValues()
                .build(k -> DataObject.get("Data for " + k));

        // 第三种，自定义
        LoadingCache<String, DataObject> cache3 = Caffeine.newBuilder()
                .expireAfter(new Expiry<String, DataObject>() {

                    @Override
                    public long expireAfterCreate(String key, DataObject value, long currentTime) {
                        return value.data.length()* 1000L;
                    }

                    @Override
                    public long expireAfterUpdate(String key, DataObject value, long currentTime, @NonNegative long currentDuration) {
                        return currentDuration;
                    }

                    @Override
                    public long expireAfterRead(String key, DataObject value, long currentTime, @NonNegative long currentDuration) {
                        return currentDuration;
                    }
                })
                .build(k -> DataObject.get("Data for " + k));
    }

    @Test
    public void testReferenceEviction(){
        // 由GC决定什么时候eviction，第一种，弱引用
        LoadingCache<String, DataObject> cache1 = Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .weakKeys()
                .weakValues()
                .build(k -> DataObject.get("Data for " + k));

        // 第二种，软引用
        LoadingCache<String, DataObject> cache2 = Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .softValues()
                .build(k -> DataObject.get("Data for " + k));
    }

    @Test
    public void testRefreshAfter() throws InterruptedException {
        // refreshAfter与expireAfter的区别：expireAfter访问过期key时会同步加载，refresh异步加载
        LoadingCache<String, DataObject> cache1 = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.SECONDS)
                .weakKeys()
                .weakValues()
                .build(k -> DataObject.get("Data for " + k));

        LoadingCache<String, DataObject> cache2 = Caffeine.newBuilder()
                .refreshAfterWrite(1, TimeUnit.SECONDS)
                .weakKeys()
                .weakValues()
                .build(k -> DataObject.get("Data for " + k));

        System.out.println(cache1.get("A"));
        System.out.println(cache2.get("A"));

        Thread.sleep(1001);

        System.out.println(cache1.get("A"));
        System.out.println(cache2.get("A"));
    }

    @Test
    public void testStats(){
        // 测试统计，1ms过期时间
        LoadingCache<String, DataObject> cache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.MILLISECONDS)
                .recordStats()
                .weakKeys()
                .weakValues()
                .build(k -> DataObject.get("Data for " + k));

        // 获取10次A
        IntStream.range(0,10).forEach((x)->cache.get("A"));
        // 获取100次B
        IntStream.range(0,100).forEach((x)->cache.get("B"));

        // 命中率
        System.out.println(cache.stats().hitRate());
        // 命中数
        System.out.println(cache.stats().hitCount());
        // 穿透数
        System.out.println(cache.stats().missCount());
        // 穿透率
        System.out.println(cache.stats().missRate());
        // 驱逐数
        System.out.println(cache.stats().evictionCount());
        // 驱逐权重
        System.out.println(cache.stats().evictionWeight());
    }


    private static class DataObject {
        private final String data;
        private static int objectCounter = 0;

        public DataObject(String data) {
            this.data = data;
        }

        public static DataObject get(String data) {
            objectCounter++;
            return new DataObject(data);
        }

        @Override
        public String toString() {
            return "Data: " + data;
        }
    }
}
