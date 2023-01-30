package com.kpz.normandy.demo.common;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

public class BitwiseTest {

    @Test
    public void testBitwise() {
        List<Integer> intList = Lists.newArrayList();

        intList.add(5);
        intList.add(-5);
        intList.add(5 << 2);
        intList.add(-5 << 2);
        intList.add(5 >> 2);
        intList.add(-5 >> 2);
        intList.add(-5 >>> 2);
        intList.add(5 & 3);
        intList.add(5 | 3);
        intList.add(5 ^ 3);
        intList.add(~5);

        intList.forEach(x -> System.out.println(Integer.toBinaryString(x)));
    }


    /**
     * 不用额外的变量，实现2个数交换
     */
    @Test
    public void testSwap() {
        int i1 = 3, i2 = 5;
        System.out.println(i1 + "," + i2);

        // 算法 011,101 > 110,101 > 110,011 > 101,011
        i1 ^= i2; // i1^i2 = i2^i1  i1变成了i1^i2
        i2 ^= i1; // i2^(i1^i2) = i2^i1^i2 = i1 <交换律> i2变成了i1
        i1 ^= i2; // (i1^i2)^i1 = i1^i2^i1 = i2 i1变成了i2

        System.out.println(i1 + "," + i2);

    }

    /**
     * 不用判断语句，求绝对值
     */
    @Test
    public void testAbs() {
        int i1 = 100;
        int i2 = -100;

        System.out.println(Math.abs(i1));
        System.out.println(Math.abs(i2));
        System.out.println(abs(i1));
        System.out.println(abs(i2));
    }

    private int abs(int i) {
        int sign = i >> 31;
        System.out.println("i=" + Integer.toBinaryString(i));
        System.out.println("sign=" + Integer.toBinaryString(sign) + "," + sign);
        System.out.println("i^sign=" + Integer.toBinaryString(i ^ sign));
        // 符号是正，减0；符号是负，减-1，正好是加1
        return (i ^ sign) - sign;
    }

    /**
     * 生成一个集合的所有子集合，如集合{2,3,4}，那么它的子集合即为{}, {2}, {3}, {4}, {2,3}, {2,4}, {3,4}, {2,3,4}
     */
    @Test
    public void testSubSet() {
        List<Integer> list = Lists.newArrayList(2, 3, 4);

        List<List<Integer>> result = getSub(list);

        System.out.println(result);

    }

    private List<List<Integer>> getSub(List<Integer> list) {
        List<List<Integer>> result = Lists.newArrayList();
        IntStream.range(0, 1 << list.size()).forEach(x ->
        {
            List<Integer> sub = Lists.newArrayList();
            IntStream.range(0, list.size()).forEach(i -> {
                if ((x >> i & 1) == 1) {
                    sub.add(list.get(i));
                }
            });
            result.add(sub);
        });
        return result;
    }

}
