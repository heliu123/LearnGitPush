package com.example.springBoot.controller;

/**
 * Created by Administrator on 2019/3/22.
 */
public class MyThreadLocal {
    public static int myThreadLocalCount;

    private static final ThreadLocal<Object> threadLocal = new ThreadLocal<Object>() {
        /**
         * ThreadLocal没有被当前线程赋值时或当前线程刚调用remove方法后调用get方法，返回此方法值
         */
        @Override
        protected Object initialValue() {
            System.out.println("调用get方法时，当前线程共享变量没有设置，调用initialValue获取默认值！");
            return null;
        }
    };

    public static void main(String[] args) {

        try {
            Thread t1 =   new Thread(new MyIntegerTask("IntegerTask1",myThreadLocalCount));
            t1.start();
           // t1.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // new Thread(new MyStringTask("StringTask1")).start();
        new Thread(new MyIntegerTask("IntegerTask2",myThreadLocalCount)).start();
        //new Thread(new MyStringTask("StringTask2")).start();
    }

    public static class MyIntegerTask implements Runnable {
        private String name;
        private int count;

        MyIntegerTask(String name,int count) {
            this.name = name;
            this.count=count;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                setCount(getCount()+1);
                synchronized (MyThreadLocal.class){
                    myThreadLocalCount++;
                }

                //System.out.println("线程" + name + ":count值为 "+count);
                System.out.println("线程" + name + ":myThreadLocalCount值为 "+myThreadLocalCount);
                // ThreadLocal.get方法获取线程变量
                /*if (null == MyThreadLocal.threadLocal.get()) {
                    // ThreadLocal.et方法设置线程变量
                    MyThreadLocal.threadLocal.set(0);
                    System.out.println("线程" + name + ": 0");
                } else {
                    int num = (Integer) MyThreadLocal.threadLocal.get();
                    MyThreadLocal.threadLocal.set(num + 1);
                    System.out.println("线程" + name + ": " + MyThreadLocal.threadLocal.get());
                    if (i == 3) {
                        MyThreadLocal.threadLocal.remove();
                    }
                }*/
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static class MyStringTask implements Runnable {
        private String name;

        MyStringTask(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                if (null == MyThreadLocal.threadLocal.get()) {
                    MyThreadLocal.threadLocal.set("a");
                    System.out.println("线程" + name + ": a");
                } else {
                    String str = (String) MyThreadLocal.threadLocal.get();
                    MyThreadLocal.threadLocal.set(str + "a");
                    System.out.println("线程" + name + ": " + MyThreadLocal.threadLocal.get());
                }
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}