package com.myweather.user.myweather.util;

/**
 * Created by User on 2016/8/27.
 */
public interface HttpCallbackLister {
    void onFinish(String response);

    void onError(Exception e);
}
