package com.example.springBoot.CountdownLatchTest;

import java.util.concurrent.CountDownLatch;

/**
 *
 * CountDownLatch 与 join 比较
 调用thread.join() 方法必须等thread 执行完毕，当前线程才能继续往下执行，
 而CountDownLatch通过计数器提供了更灵活的控制，只要检测到计数器为0当前线程就可以往下执行而不用管相应的thread是否执行完毕。
 ---------------------
 作者：gary-liu
 来源：CSDN
 原文：https://blog.csdn.net/revitalizing/article/details/61008956
 版权声明：本文为博主原创文章，转载请附上博文链接！
 * Created by Administrator on 2019/5/8.
 */
public class CountDownLatchVsJoin {
    /**
     * 方式1： 用join实现三个线程有序执行
     * @param args
     */
/*    public static void main(String[] args) {

        final Thread t1=new Thread(new Runnable(){
            @Override
            public void run() {
                System.out.println("t1");
            }
        });

        final Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    t1.join();
                }catch(InterruptedException e){

                }
                System.out.println("t2");

            }

        });
        final Thread t3=new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    t2.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("t3");

            }
        });

        t1.start();
        t2.start();
        t3.start();

    }*/

    /**
     * 方式2： 用CountDownLatch实现三个线程有序执行
     * @param args
     */
    public static void main(String[] args) {
        CountDownLatch c0 = new CountDownLatch(0); //①
        CountDownLatch c1 = new CountDownLatch(1); //②
        CountDownLatch c2 = new CountDownLatch(1); //③

        Thread t1 = new Thread(new Work(c0, c1));
        //c0为0，t1可以执行。t1的计数器减1

        Thread t2 = new Thread(new Work(c1, c2));
        //t1的计数器为0时，t2才能执行。t2的计数器c2减1

        Thread t3 = new Thread(new Work(c2, c2));
        //t2的计数器c2为0时，t3才能执行

        t2.start();
        t1.start();
        t3.start();

    }
    //定义Work线程类，需要传入开始和结束的CountDownLatch参数
    static class Work implements Runnable {
        CountDownLatch c1;
        CountDownLatch c2;
        Work(CountDownLatch c1, CountDownLatch c2){
            super();
            this.c1=c1;
            this.c2=c2;
        }
        public void run() {
            try {
                c1.await();//前一线程为0才可以执行
                System.out.println("开始执行线程:"+ Thread.currentThread().getName());
                c2.countDown();//本线程计数器减少
            } catch (InterruptedException e) {
            }

        }
    }

}
