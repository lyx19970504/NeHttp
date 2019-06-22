package com.lyx.nehttp;

import java.io.InputStream;

public interface ICallbackListener {

    void onSuccess(InputStream inputStream);

    void onFailed();
}
