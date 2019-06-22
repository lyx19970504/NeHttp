package com.lyx.nehttp;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Http请求类
 */
public class HttpRequest implements IHttpRequest {

    private String url;
    private byte[] data;
    private ICallbackListener listener;
    private HttpURLConnection httpURLConnection;

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public void setListener(ICallbackListener callbackListener) {
        this.listener = callbackListener;
    }

    @Override
    public void execute() {
        URL url;
        try {
            url = new URL(this.url);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setInstanceFollowRedirects(true);
            httpURLConnection.setConnectTimeout(6000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setReadTimeout(3000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type","application/json;charset=UTF-8");
            httpURLConnection.connect();
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            bufferedOutputStream.write(this.data);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
            outputStream.close();
            if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = httpURLConnection.getInputStream();
                this.listener.onSuccess(inputStream);
                inputStream.close();
            }else{
                throw new RuntimeException("请求失败!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("请求失败!");
        }finally {
            httpURLConnection.disconnect();
        }
    }


}
