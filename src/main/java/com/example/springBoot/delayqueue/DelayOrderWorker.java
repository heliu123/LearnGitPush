package com.example.springBoot.delayqueue;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 具体执行相关业务的业务类
 * @author whd
 * @date 2017年9月25日 上午12:49:32
 */
public class DelayOrderWorker implements Runnable  {
    @Override
    public void run() {
        // TODO Auto-generated method stub
        //相关业务逻辑处理
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.format(new Date()) + " start");
        System.out.println(Thread.currentThread().getName()+" do something ……");
    }
}
