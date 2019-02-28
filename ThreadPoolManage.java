package com.xianneng.adaptertest.http;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolManage {
    //1.把任务添加到请求队列中（问题1：请求队列用什么样的数据结构,任务用什么方式给我：链表数据结构，链表阻塞式队列）
    private LinkedBlockingQueue<Runnable> queue=new LinkedBlockingQueue<>();
    //添加任务
    public void execute(Runnable runnable){
        if (runnable!=null) {
            try {
                queue.put(runnable);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    //2.把队列任务放入到线程池中（也就是处理中心中）
    private ThreadPoolExecutor threadPoolExecutor;
    private ThreadPoolManage(){
        threadPoolExecutor = new ThreadPoolExecutor(4,20,15,TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(4),rejectedExecutionHandler);
        //开启传送带，让程序运行起来
        threadPoolExecutor.execute(runnable);
    }
    private RejectedExecutionHandler rejectedExecutionHandler = new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            //参数r就是超时的线程
            try {
                queue.put(r);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
    //3.让他们开始工作起来
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (true){
                Runnable runnable = null;
                //从队列中取出请求
                try {
                    runnable = queue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (runnable!=null){
                    threadPoolExecutor.execute(runnable);
                }
            }
        }
    };
    //单例
    private static ThreadPoolManage ourInstance=new ThreadPoolManage();
    public static ThreadPoolManage getOurInstance(){
        return ourInstance;
    }
}
