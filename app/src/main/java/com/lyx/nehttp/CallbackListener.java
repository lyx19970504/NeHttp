package com.lyx.nehttp;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 回调类，用于将网络请求的流对象转化为自定义的类
 * @param <T>
 */
public class CallbackListener<T> implements ICallbackListener {

    private Class<T> responseClass;
    private IDataTransformListener dataTransformListener;
    private Handler handler;

    public CallbackListener(Class<T> responseClass,IDataTransformListener dataTransformListener){
        handler = new Handler(Looper.getMainLooper());
        this.responseClass = responseClass;
        this.dataTransformListener = dataTransformListener;
    }

    @Override
    public void onSuccess(InputStream inputStream) {
        //将流转换为string
        String response = getContent(inputStream);
        //将String格式的JSON数据转换为对应的class对象
        final T clazz = JSON.parseObject(response,responseClass);
        handler.post(new Runnable() {
            @Override
            public void run() {
                dataTransformListener.onSuccess(clazz);
            }
        });
    }

    @Override
    public void onFailed() {

    }

    private String getContent(InputStream inputStream){
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder builder = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null){
                builder.append(line + "\n");
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return builder.toString();
    }
}
