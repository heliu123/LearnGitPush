package com.example.springBoot.jdkDynamicProxy;

/**
 * 真实主题类
 * Created by Administrator on 2019/4/3.
 */
public class RealSubject implements Subject{


    @Override
    public void doSomething() {
        System.out.println("RealSubject do something");
    }
}
