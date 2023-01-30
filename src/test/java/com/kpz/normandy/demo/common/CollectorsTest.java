package com.kpz.normandy.demo.common;

import com.alibaba.fastjson2.JSON;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectorsTest {
    private static final List<Integer> intList = Lists.newArrayList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

    @Test
    public void testCreateStream() {
        // Collection创建
        Stream stream1 = intList.stream();

        // 数组创建
        Stream stream2 = Arrays.stream(new String[]{"a", "b", "c"});

        // 值创建
        Stream stream3 = Stream.of("a", "b", "c");

        // Stream.iterate 无限流
        Stream stream4 = Stream.iterate("", UnaryOperator.identity());

        // Stream.generate 无限流
        Stream stream5 = Stream.generate(() -> "");
    }

    @Test
    public void testCounting() {
        // counting = count
        System.out.println("count=" + intList.stream().collect(Collectors.counting()));
//        System.out.println("count=" + (Long) intList.stream().count());

        // summingInt = mapToInt().sum()
        System.out.println("sum=" + intList.stream().collect(Collectors.summingInt(x -> x)));
//        System.out.println("sum=" + (Integer) intList.stream().mapToInt(x -> x).sum());

        // averagingInt
        System.out.println("average=" + intList.stream().collect(Collectors.averagingInt(x -> x)));

        // summarizingInt
        System.out.println(intList.stream().collect(Collectors.summarizingInt(x -> x)));

        // joining
        System.out.println(intList.stream().map(Object::toString).collect(Collectors.joining()));

        // groupingBy
        System.out.println(intList.stream().collect(Collectors.groupingBy(x -> x % 2)));

        // partitioningBy
        System.out.println(intList.stream().collect(Collectors.partitioningBy(x -> x % 2 == 0)));

        // collectingAndThen
        System.out.println(JSON.toJSONString(intList.stream().collect(Collectors.collectingAndThen(Collectors.toList(), List::size))));

        // reducing = reduce
        System.out.println(JSON.toJSONString(intList.stream().collect(Collectors.reducing(BinaryOperator.maxBy(Integer::compare)))));
//        System.out.println(JSON.toJSONString(intList.stream().reduce(BinaryOperator.maxBy(Integer::compare))));

        // maxBy = max
        System.out.println(JSON.toJSONString(intList.stream().collect(Collectors.maxBy(Integer::compare))));
//        System.out.println(JSON.toJSONString(intList.stream().max(Integer::compare)));

    }
}
