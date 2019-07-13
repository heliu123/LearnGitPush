package com.example.springBoot.threadPoolExecutor;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 定义放在延迟队列中的对象，需要实现Delayed接口
 * Created by Administrator on 2019/6/21.
 */
public class DelayedTask implements Delayed {
    private int _expireInSecond = 0;

    public DelayedTask(int delaySecond) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, delaySecond);
        _expireInSecond = (int) (cal.getTimeInMillis() / 1000);
    }

    public int compareTo(Delayed o) {
        long d = (getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS));
        return (d == 0) ? 0 : ((d < 0) ? -1 : 1);
    }

    public long getDelay(TimeUnit unit) {
        //TODO Auto-generated method stub
        Calendar cal = Calendar.getInstance();
        return _expireInSecond - (cal.getTimeInMillis() / 1000);
    }


    public static void main(String[] args) throws InterruptedException {

        //TODO Auto-generated method stub
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //定义延迟队列
        DelayQueue<DelayedTask> delayQueue = new DelayQueue<DelayedTask>();

        //定义三个延迟任务
        DelayedTask task1 = new DelayedTask(10);
        DelayedTask task2 = new DelayedTask(5);
        DelayedTask task3 = new DelayedTask(15);

        delayQueue.add(task1);
        delayQueue.add(task2);
        delayQueue.add(task3);

        System.out.println(sdf.format(new Date()) + " start");

        while(delayQueue.size() != 0) {

            //如果没到时间，该方法会返回

            DelayedTask task = delayQueue.poll();
            if(task != null) {
                Date now = new Date();
                System.out.println(sdf.format(now));
            }

            Thread.sleep(1000);
        }
    }
}