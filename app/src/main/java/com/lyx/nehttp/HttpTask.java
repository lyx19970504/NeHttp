package com.lyx.nehttp;

import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 自定义线程对象，用于将HttpRequest对象进行封装，并通过run方法执行
 * @param <T>
 */
public class HttpTask<T> implements Runnable, Delayed {

    private static final String TAG = "HttpTask";
    private IHttpRequest httpRequest;

    public HttpTask(String url, T requestData, IHttpRequest httpRequest, CallbackListener callbackListener){
        httpRequest.setUrl(url);
        String content = JSON.toJSONString(requestData);
        httpRequest.setListener(callbackListener);
        try {
            httpRequest.setData(content.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.httpRequest = httpRequest;
    }

    @Override
    public void run() {
        try {
            this.httpRequest.execute();
        }catch (Exception e){
            ThreadPoolManager.getInstance().addDelayedTask(this);
        }
    }


    //延迟时间
    private long delayTime;
    //重试次数
    private int retryCount;

    public long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = System.currentTimeMillis() + delayTime;
        Log.d(TAG, "setDelayTime: " + delayTime);
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        Log.d(TAG, "getDelay: " + unit.convert(delayTime - System.currentTimeMillis(),TimeUnit.MILLISECONDS));
        return unit.convert(delayTime - System.currentTimeMillis(),TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return 0;
    }
}
