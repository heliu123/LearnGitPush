package com.example.springBoot.callBackFunction;

/**
 * Created by Administrator on 2019/4/23.
 */
public class SuperCalculator {
    public void add(int a, int b, Job xiaoming) {

        int result = a + b;
        //模拟处理时间
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        xiaoming.fillBlank(a, b, result);
    }
}
