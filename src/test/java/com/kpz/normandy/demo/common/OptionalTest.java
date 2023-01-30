package com.kpz.normandy.demo.common;

import org.junit.jupiter.api.Test;

import java.util.Optional;

public class OptionalTest {
    @Test
    public void testOrElse(){
        Optional<String> optionalS = Optional.of("ABC");

        System.out.println(optionalS.orElse("AAA"));
    }
}
