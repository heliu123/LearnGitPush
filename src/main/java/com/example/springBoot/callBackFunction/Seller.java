package com.example.springBoot.callBackFunction;

/**
 * Created by Administrator on 2019/4/23.
 */
public class Seller implements Job {
    private String name = null;

    public Seller(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public void fillBlank(int a, int b, int result) {
        System.out.println(name + "求助小红算账:" + a + " + " + b + " = " + result + "元");
    }


    public void callHelp(int a, int b,Job job) {
        new SuperCalculator().add(a, b, job);
    }
}
