package com.example.springBoot.CountdownLatchTest.TheadExecutionSequence;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ---------------------
 作者：gary-liu
 来源：CSDN
 原文：https://blog.csdn.net/revitalizing/article/details/61008956
 版权声明：本文为博主原创文章，转载请附上博文链接！
 * Created by Administrator on 2019/5/8.
 */
public class ExecuteOrderPractice {
    public void orderPractice(){
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for(int i = 0; i < 5; i++){
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try{
                        Thread.sleep(1000);
                        System.out.println(Thread.currentThread().getName() + " do something");
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            });
        }

        executorService.shutdown();

        while(true){
            if(executorService.isTerminated()){
                System.out.println("Finally do something ");
                break;
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        new ExecuteOrderPractice().orderPractice();

    }

}
