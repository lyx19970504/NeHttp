package com.lyx.nehttp;

public class NeHttp {

    /**
     *
     * @param url 请求地址
     * @param requestData 请求参数（可以为空）
     * @param response 自定义类，用于将String形式的JSON格式数据映射成对象
     * @param listener 回调监听器
     * @param <T> 泛型
     * @param <M> 泛型
     */
    public static<T,M> void sendRequest(String url,T requestData,Class<M> response,IDataTransformListener listener){
        IHttpRequest request = new HttpRequest();
        CallbackListener callbackListener = new CallbackListener(response,listener);
        HttpTask httpTask = new HttpTask(url,requestData,request,callbackListener);
        ThreadPoolManager.getInstance().addTask(httpTask);
    }


}
