package com.example.springBoot.CountdownLatchTest;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * ClassName: ThreadTiming <br/>
 * Function: 计算多个线程任务执行完后的用时<br/>
 *
 * @author gary.liu
 * @date 2017/6/24
 */
public class ThreadTiming {

    private int nThread;

    private CountDownLatch startGate;
    private CountDownLatch endGate;

    public ThreadTiming(int nThread, CountDownLatch startGate, CountDownLatch endGate) {

        this.nThread = nThread;
        this.startGate = startGate;
        this.endGate = endGate;
    }

    class worker implements Runnable {

        public void run() {
            try {
                startGate.await();
                Random random = new Random();
                int num = random.nextInt(500) + 500;
                System.out.println(Thread.currentThread().getName() + " start and sleep: " + num + "ms");
                Thread.sleep(num);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                endGate.countDown();
            }
        }
    }

    public long timeTasks() {

        for(int i = 0; i < nThread; i++){
            Thread thread = new Thread(new worker());
            thread.start();
        }

        long start = System.currentTimeMillis();
        //所有阻塞的任务同时开始
        startGate.countDown();
        try {
            //主线程阻塞,等待其他所有 worker 线程完成后再执行
            endGate.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("用时: " + (end - start) + "ms");

        return end - start;
    }

    public static void main(String[] args) {

        int nThread = 5;
        CountDownLatch startGate = new CountDownLatch(1);
        CountDownLatch endGate = new CountDownLatch(nThread);

        new ThreadTiming(nThread, startGate, endGate).timeTasks();

    }
}
