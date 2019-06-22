package com.lyx.nehttp;

import android.util.Log;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池管理类
 */
public class ThreadPoolManager {

    private static final String TAG = "ThreadPoolManager";
    //单例
    private static ThreadPoolManager threadPoolManager;
    //线程池对象
    private ThreadPoolExecutor executor;

    //请求队列
    private LinkedBlockingQueue<Runnable> linkedBlockingQueue;

    //延迟队列
    private DelayQueue<HttpTask> delayQueue;

    public static ThreadPoolManager getInstance(){
        if(threadPoolManager == null){
            threadPoolManager = new ThreadPoolManager();
        }
        return threadPoolManager;
    }

    public ThreadPoolManager(){
        linkedBlockingQueue = new LinkedBlockingQueue<>();
        delayQueue = new DelayQueue<>();
        executor = new ThreadPoolExecutor(3, 15, 6,
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(4), new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                addTask(r);
            }
        });
        //开启核心线程
        executor.execute(coreThread);
        executor.execute(delayThread);
    }

    public void addTask(Runnable runnable){
        if(runnable != null){
            try {
                linkedBlockingQueue.put(runnable);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void addDelayedTask(HttpTask task){
        if(task != null){
            //设置延迟时间为3秒
            task.setDelayTime(3000);
            delayQueue.offer(task);
        }
    }

    //核心线程，负责从队列中得到请求对象并添加到线程池中的线程中
    private Thread coreThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true){
                try {
                    Runnable runnable = linkedBlockingQueue.take();

                    //真正开始实现线程中的任务（调用runnable的run方法）
                    executor.execute(runnable);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    //核心线程，负责从队列中得到延迟请求，并重复执行
    private Thread delayThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                HttpTask task;
                while (true) {
                    task = delayQueue.take();
                    if (task.getRetryCount() < 3) {
                        executor.execute(task);
                        task.setRetryCount(task.getRetryCount() + 1);
                        Log.d(TAG, "run: " + "重试");
                    } else {
                        Log.d(TAG, "run: " + "失败");
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });
}
