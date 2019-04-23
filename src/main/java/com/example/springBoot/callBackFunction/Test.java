package com.example.springBoot.callBackFunction;

/**
 * Created by Administrator on 2019/4/23.
 */
public class Test {
    public static void main(String[] args) {
        int a = 26549;
        int b = 16487;
        Student s = new Student("小明");
        System.out.println(Thread.currentThread() + "开始调用：" + a + " + " + b + " = " );
        s.callHelp(a, b,s);
        Seller s2 = new Seller("老婆婆");
        s2.callHelp(a, b,s2);
        System.out.println(Thread.currentThread() + "已结束！！！等待回调结果" + a + " + " + b + " = " );
        System.out.println(Thread.currentThread() + "小明自己等不及了，口算结果为：" + a + " + " + b + " = " +(a+b));

    }
}
