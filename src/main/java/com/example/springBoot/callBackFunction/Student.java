package com.example.springBoot.callBackFunction;

/**
 * Created by Administrator on 2019/4/23.
 */
public class Student implements Job{
    private String name = null;

    public Student(String name) {
        this.name = name;
    }


    public void callHelp(int a, int b,Job xiaoming) {
        new Thread() {
            @Override
            public void run() {
                new SuperCalculator().add(a, b, xiaoming);
            }
        }.start();

    }

    @Override
    public void fillBlank(int a, int b, int result) {
        System.out.println(name + "求助小红计算:" + a + " + " + b + " = " + result);
    }

}
