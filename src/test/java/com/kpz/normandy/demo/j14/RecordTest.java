package com.kpz.normandy.demo.j14;

import org.junit.jupiter.api.Test;

import java.util.Objects;

public class RecordTest {

    @Test
    public void test1(){
        System.out.println(new Person("NAME", "ADDRESS"));
        System.out.println(new Person("NAME"));
        System.out.println(Person.unnamed("ADDRESS"));
    }

    private record Person(String name, String address){
        public static String UNKNOWN_ADDRESS = "Unknown";
        public Person{
            Objects.requireNonNull(name);
            Objects.requireNonNull(address);
        }

        public Person(String name){
            this(name, UNKNOWN_ADDRESS);
        }

        public static Person unnamed(String address) {
            return new Person("Unnamed", address);
        }
    }
}
