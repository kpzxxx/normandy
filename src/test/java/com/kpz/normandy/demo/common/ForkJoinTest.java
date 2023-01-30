package com.kpz.normandy.demo.common;

import com.alibaba.fastjson2.JSON;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

public class ForkJoinTest {

    @Test
    public void testForkJoin() {
        ForkJoinPool pool = new ForkJoinPool();
        try {
            ForkJoinImpl forkJoin = new ForkJoinImpl(1, 100);

            ForkJoinTask<Integer> result = pool.submit(forkJoin);
            System.out.println(JSON.toJSONString(result));
            pool.shutdown();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private static class ForkJoinImpl extends RecursiveTask<Integer> {
        // 满足<=2步长才计算
        public static final int threshold = 2;
        private final int start;
        private final int end;

        public ForkJoinImpl(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            int sum;
            //
            boolean canCompute = (end - start) <= threshold;

            if (canCompute) {
                // 区间累加求和
                sum = IntStream.rangeClosed(start, end).sum();
            } else {
                // 切分fork任务
                int middle = (start + end) / 2;
                ForkJoinImpl left = new ForkJoinImpl(start, middle);
                ForkJoinImpl right = new ForkJoinImpl(middle + 1, end);

                left.fork();
                right.fork();

                int leftResult = left.join();
                int rightResult = right.join();

                sum = leftResult + rightResult;
            }

            return sum;
        }
    }
}
