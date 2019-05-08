package com.example.springBoot.CountdownLatchTest.TheadExecutionSequence;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 CountDownLatch 与 join 比较
 调用thread.join() 方法必须等thread 执行完毕，当前线程才能继续往下执行，而CountDownLatch通过计数器提供了更灵活的控制，只要检测到计数器为0当前线程就可以往下执行而不用管相应的thread是否执行完毕。
 ---------------------
 作者：gary-liu
 来源：CSDN
 原文：https://blog.csdn.net/revitalizing/article/details/61008956
 版权声明：本文为博主原创文章，转载请附上博文链接！
 * Created by Administrator on 2019/5/8.
 */
public class OrderThreadExecute {
    class Worker implements Runnable {
        private CountDownLatch downLatch;
        private String name;

        public Worker(CountDownLatch downLatch, String name) {
            this.downLatch = downLatch;
            this.name = name;
        }

        @Override
        public void run() {
            this.doWork();
            try {
                TimeUnit.SECONDS.sleep(new Random().nextInt(10));
            } catch (InterruptedException ie) {
            }
            System.out.println(this.name + "活干完了！");
            this.downLatch.countDown();
        }

        private void doWork() {
            System.out.println(this.name + "正在干活...");
        }

    }

    class Boss implements Runnable {
        private CountDownLatch downLatch;

        public Boss(CountDownLatch downLatch) {
            this.downLatch = downLatch;
        }

        @Override
        public void run() {
            System.out.println("老板正在等所有的工人干完活......");
            try {
                this.downLatch.await();
            } catch (InterruptedException e) {
            }
            System.out.println("工人活都干完了，老板开始检查了！");
        }

    }

    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        CountDownLatch latch = new CountDownLatch(3);

        OrderThreadExecute orderThread = new OrderThreadExecute();

        Worker w1 = orderThread.new Worker(latch, "张三");
        Worker w2 = orderThread.new Worker(latch, "李四");
        Worker w3 = orderThread.new Worker(latch, "王二");

        Boss boss = orderThread.new Boss(latch);

        executor.execute(boss);
        executor.execute(w3);
        executor.execute(w2);
        executor.execute(w1);

        executor.shutdown();

    }
}
