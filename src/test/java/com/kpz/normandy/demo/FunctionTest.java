package com.kpz.normandy.demo;

import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class FunctionTest {
    @Test
    public void testFunction() {
        String s = "binghe";
        // apply
        System.out.println(handleString(s, a -> a.toUpperCase()));

        // compose
        System.out.println(handleStringCompose(s, a -> a.length() + "", b -> StringUtils.abbreviate(b, 5)));

        // andThen
        System.out.println(handleStringAndThen(s, a -> a.length() + "", b -> StringUtils.abbreviate(b, 5)));

        // identity
        System.out.println(handleString(s, Function.identity()));

    }

    private String handleString(String s, Function<String, String> function) {
        return function.apply(s);
    }

    private String handleStringCompose(String s, Function<String, String> function1, Function<String, String> function2) {
        // 先执行function2,后执行function1
        return function1.compose(function2).apply(s);
    }

    private String handleStringAndThen(String s, Function<String, String> function1, Function<String, String> function2) {
        // 先执行function2,后执行function1
        return function1.andThen(function2).apply(s);
    }

    @Test
    public void testSupplier() {
        System.out.println(getIntList(10, () -> new Random().nextInt(100)));
    }

    private List<Integer> getIntList(int num, Supplier<Integer> supplier) {
        List<Integer> result = Lists.newArrayList();

        IntStream.range(0, num).forEach(i -> result.add(supplier.get()));

        return result;
    }

    @Test
    public void testConsumer() {
        handleConsumer(10, System.out::println);
        handleConsumer(10, System.out::println, integer -> System.out.println(integer * integer));
    }

    private void handleConsumer(Integer num, Consumer<Integer> consumer) {
        consumer.accept(num);
    }

    private void handleConsumer(Integer num, Consumer<Integer> consumer1, Consumer<Integer> consumer2) {
        consumer1.andThen(consumer2).accept(num);
    }

    @Test
    public void testPredicate() {
        List<String> list = Lists.newArrayList("Hello", "Lambda", "binghe", "lyz", "World");
        // not
        System.out.println(filterString(list, Predicate.not(s -> s.length() >= 5)));

        // and
        System.out.println(filterString(list, ((Predicate<String>) s -> s.length() >= 5).and(s -> s.startsWith("H"))));

        // or
        System.out.println(filterString(list, ((Predicate<String>) s -> s.length() >= 5).or(s -> s.startsWith("l"))));

        // negate
        System.out.println(filterString(list, ((Predicate<String>) s -> s.length() >= 5).and(s -> s.startsWith("H")).negate()));

        // isEqual
        System.out.println(filterString(list, Predicate.isEqual("Hello")));

    }

    private List<String> filterString(List<String> list, Predicate<String> predicate) {
        List<String> result = Lists.newArrayList();
        for (String s : list) {
            if (predicate.test(s)) {
                result.add(s);
            }
        }
        return result;
    }
}
