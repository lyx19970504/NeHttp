package com.lyx.nehttp;

public interface IHttpRequest {

    //设置Url
    void setUrl(String url);

    //设置参数
    void setData(byte[] data);

    void setListener(ICallbackListener callbackListener);

    void execute();
}
