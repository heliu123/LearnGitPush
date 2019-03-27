package com.example.springBoot.CountdownLatchTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2019/3/27.
 */
public class ExecuteOrderPractice {

    public void orderPractice(){
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        long start = System.currentTimeMillis();
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
                //System.out.println("Finally do something ");
                long end = System.currentTimeMillis();
                System.out.println("用时: " + (end - start) + "ms");
                break;
            }

        }
    }

    public static void main(String[] args){
        new ExecuteOrderPractice().orderPractice();

    }

}
