package com.example.springBoot.CountdownLatchTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2019/5/8.
 */
public class CountDownLatchVsWait {
    //添加volatile，使t2能够得到通知
    volatile List lists = new ArrayList();

    public void add(Object o) {
        lists.add(o);
    }

    public int size() {
        return lists.size();
    }

   /* public static void main(String[] args) {
        CountDownLatchVsWait c = new CountDownLatchVsWait();

        final Object lock = new Object();

        new Thread(() -> {
            synchronized(lock) {
                System.out.println("t2启动");
                if(c.size() != 5) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("t2 结束");
                //通知t1继续执行
                lock.notify();
            }
        }, "t2").start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        new Thread(() -> {
            System.out.println("t1启动");
            synchronized(lock) {
                for(int i=0; i<10; i++) {
                    c.add(new Object());
                    System.out.println("add " + i);

                    if(c.size() == 5) {
                        lock.notify();
                        //释放锁，让t2得以执行
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "t1").start();
    }*/

    /**
     * 通常情况下由于wait与notify使用起来比较复杂，在多线程中使用wait与notify就像使用汇编语言一样，麻烦而且不好控制，实际中不怎么使用，而是用CountDownLatch替换
     *
     * CountDownLatch不涉及锁定，当count的值为零时阻塞的线程继续运行,当不涉及同步，只是涉及线程通信的时候，用synchronized + wait/notify就显得太重了。主线程必须在启动其他线程后立即调用CountDownLatch.await()方法。这样主线程的操作就会在这个方法上阻塞，直到其他线程完成各自的任务。·

     wait() 99%的情况会与while一起使用，而不是if，while会在wait()被叫醒后再去判断一下。--effective java
     永远不要使用notify，要使用notifyAll替代。--effective java
     * @param args
     */

    public static void main(String[] args) {
        CountDownLatchVsWait c = new CountDownLatchVsWait();

        CountDownLatch latch = new CountDownLatch(1);

        new Thread(() -> {
            System.out.println("t2启动");
            if (c.size() != 5) {
                try {
                    latch.await();
                    //也可以指定等待时间
                    //latch.await(5000, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("t2 结束");

        }, "t2").start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        new Thread(() -> {
            System.out.println("t1启动");
            for (int i = 0; i < 10; i++) {
                c.add(new Object());
                System.out.println("add " + i);

                if (c.size() == 5) {
                    // 打开门闩，让t2得以执行
                    latch.countDown();
                }

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }, "t1").start();

    }



}
