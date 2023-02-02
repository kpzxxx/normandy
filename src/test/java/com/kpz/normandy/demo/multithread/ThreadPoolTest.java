package com.kpz.normandy.demo.multithread;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.*;

public class ThreadPoolTest {

    @Test
    public void testExecutorService(){
        // 无界队列不可使用，会OOM
        ExecutorService fixed = Executors.newFixedThreadPool(1);

        Map<String, String> map = Map.of();
        Map<String, String> mapA = Map.of("A", "B");

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 10, 0L,
                TimeUnit.DAYS, null);
    }
}
