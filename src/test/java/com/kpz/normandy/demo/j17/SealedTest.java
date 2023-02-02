package com.kpz.normandy.demo.j17;

public class SealedTest {

    private sealed interface Service permits Car, Truck {
    }

    // sealed permits 修饰的类必须有相应的子类继承，且子类和父类需要在同一个模块
    private sealed abstract class Vehicle permits Car, Truck {

    }

    // 继承sealed类的子类必须标识是否可以继续限定继承，需用final, sealed-permits, non-sealed修饰
    private non-sealed class Car extends Vehicle implements Service {
    }

    private final class Truck extends Vehicle implements Service {
    }

}
