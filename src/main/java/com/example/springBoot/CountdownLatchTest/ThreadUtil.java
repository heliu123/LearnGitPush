package com.example.springBoot.CountdownLatchTest;


import com.example.springBoot.CountdownLatchTest.bean.ActivityBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadUtil {
    //创建定长线程池，初始化线程
    private static Logger log = LoggerFactory.getLogger(ThreadUtil.class);

    /**
     * 对List进行多线程处理(限制 对List只读 如果想修改List 可以处理完毕后把要修改或删除的List返回 多线程执行完后再修改或删除)
     * @param list 要处理的List
     * @param threadSize 用几个线程处理
     * @param threadLoadback 处理的回调(具体业务员)
     * @param <T> 每个回调的返回结果
     * @param <V> List<V>的泛型
     * @return
     */
    public static <T,V>List<T> executorsTasks(final List<V> list, final  int threadSize, final  ThreadLoadback<T,V> threadLoadback){
        // 开始时间
        long start = System.currentTimeMillis();
        // 总数据条数
        int dataSize = list.size();
        // 线程数
        int threadNum = dataSize / threadSize + 1;
        // 定义标记,过滤threadNum为整数
        boolean special = dataSize % threadSize == 0;
        // 创建一个线程池
        ExecutorService exec = Executors.newFixedThreadPool(threadNum);
        // 定义一个任务集合
        List<Callable<T>> tasks = new ArrayList<Callable<T>>();
        Callable<T> task = null;
        List cutList = null;

        for (int i = 0; i < threadNum; i++) {
            if (i == threadNum - 1) {
                if (special) {
                    break;
                }
                cutList = list.subList(threadSize * i, dataSize);
            } else {
                cutList = list.subList(threadSize * i, threadSize * (i + 1));
            }
            // System.out.println("第" + (i + 1) + "组：" + cutList.toString());
            final List listStr = cutList;
            task = new Callable<T>() {
                @Override
                public T  call() throws Exception {
                    // System.out.println(Thread.currentThread().getName() + "线程：" + listStr);
                    return (T) threadLoadback.load(listStr);
                    //  return


                }
            };
            // 这里提交的任务容器列表和返回的Future列表存在顺序对应的关系
            tasks.add(task);
        }
        List<Future<T>> resultsFuture = null;
        try {
            log.debug("线程任务执行开始:任务数"+tasks.size());
            resultsFuture = exec.invokeAll(tasks);
            System.out.println(resultsFuture.size());
            System.out.println("线程池执行invokeAll方法消耗时间 ：" + (System.currentTimeMillis() - start) + "毫秒");
            //resultsFuture = StartUp.executorService.invokeAll(tasks);
            List<T> results = new ArrayList<>();
            for (Future<T> future : resultsFuture) {
                T result=future.get();
                System.out.println("线程池执行future.get()方法消耗时间 ：" + (System.currentTimeMillis() - start) + "毫秒");
                if(result!=null) {
                    results.add(result);
                }
            }
            return results;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }finally {
            // 关闭线程池
            exec.shutdown();
            log.debug("线程任务执行结束");
            log.debug("执行任务消耗了 ：" + (System.currentTimeMillis() - start) + "毫秒");
             System.out.println("执行任务消耗了 ：" + (System.currentTimeMillis() - start) + "毫秒");
        }

    }

    interface ThreadLoadback<T,V> {
        T load(List<V> list) throws Exception;
    }



    public static void main(String[] args) {
        List<ActivityBean> list = new ArrayList<>();
        for(int i=0;i<1000;i++){
            list.add(new ActivityBean(i+1+""));
        }
        List<List<Object>> resultList=   ThreadUtil.executorsTasks(list, 10, new ThreadLoadback<List<Object>, ActivityBean>() {
            @Override
            public List<Object> load(List<ActivityBean> list) throws Exception {
                List<Object> result= new ArrayList<>();
                for(ActivityBean str:list){
                   try {
                        str.setActivityName(str.getWhiteDefineId()+"@@@测试");
                        if(Integer.parseInt(str.getWhiteDefineId()) % 5 == 0){
                            throw new RuntimeException(str.getWhiteDefineId() + "@@@id被5整除");
                        }
                        result.add(str);
                    } catch (Exception e) {
                        result.add(e);
                    }
                    Thread.sleep(1000L);
                    System.out.println(Thread.currentThread().getName()+"休息1秒");
                }
                return result;
            }
        });
        if(!CollectionUtils.isEmpty(resultList)){
            List<ActivityBean> integers = new ArrayList<>();
            List<Exception> ex = new ArrayList<>();
            resultList.stream().forEach(items -> {
                        if (!CollectionUtils.isEmpty(resultList)) {
                            items.stream().forEach(item -> {
                                if(item instanceof ActivityBean){
                                    integers.add((ActivityBean)item);
                                }else{
                                    ex.add((Exception)item);
                                }

                            });
                        }
                    }
            );
            integers.stream().forEach(item->System.out.println(item));
            ex.stream().forEach(item->System.out.println(item));
        }
    }


}