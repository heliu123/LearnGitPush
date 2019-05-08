package com.example.springBoot.CountdownLatchTest.TheadExecutionSequence;

/**
 * ---------------------
 作者：gary-liu
 来源：CSDN
 原文：https://blog.csdn.net/revitalizing/article/details/61008956
 版权声明：本文为博主原创文章，转载请附上博文链接！
 * Created by Administrator on 2019/5/8.
 */
public class JoinPractice {
    static class Worker implements Runnable {

        private String name;

        public Worker(String name){
            this.name = name;
        }

        @Override
        public void run(){
            System.out.println(name + " is working");

        }
    }

    static class Boss implements Runnable{

        private String name;

        public Boss(String name){
            this.name = name;
        }

        @Override
        public void run(){
            System.out.println("boss checks work");
        }
    }

    public static void main(String[] args){

        Worker worker1 = new Worker("worker1");
        Worker worker2 = new Worker("worker2");
        Worker worker3 = new Worker("worker3");
        Boss boss = new Boss("boss");

        Thread thread1 = new Thread(worker1);
        Thread thread2 = new Thread(worker2);
        Thread thread3 = new Thread(worker3);
        Thread thread4 = new Thread(boss);

        thread1.start();
        thread2.start();
        thread3.start();

        try {
            thread1.join();
            thread2.join();
            thread3.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        thread4.start();
    }
}
