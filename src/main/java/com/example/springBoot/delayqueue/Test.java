package com.example.springBoot.delayqueue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2019/8/14.
 */
public class Test {
    public static void main(String[] args) {
        DelayOrderWorker work1 = new DelayOrderWorker();// 任务1
        DelayOrderWorker work2 = new DelayOrderWorker();// 任务2
        DelayOrderWorker work3 = new DelayOrderWorker();// 任务3
        // 延迟队列管理类，将任务转化消息体并将消息体放入延迟对列中等待执行
        DelayOrderQueueManager manager = DelayOrderQueueManager.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println( "任务开始时间为："+sdf.format(new Date()) + " start");
        manager.put(work1, 3000, TimeUnit.MILLISECONDS);
        manager.put(work2, 6000, TimeUnit.MILLISECONDS);
        manager.put(work3, 9000, TimeUnit.MILLISECONDS);
    }
}
