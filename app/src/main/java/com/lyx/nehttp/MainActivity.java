package com.lyx.nehttp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    //正确的测试url
    public static final String url = "http://web.juhe.cn:8080/environment/water/river?river=淮河流域";
    //故意错误的url，为了重试机制的测试
    //public static final String url = "http://wxxxxxxxxxxxxxxxxxxxxxxx";
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NeHttp.sendRequest(url, null, RequestObject.class, new IDataTransformListener<RequestObject>() {
            @Override
            public void onSuccess(RequestObject object) {
                Log.d(TAG, "onSuccess: " + object);
            }
        });
    }
}
