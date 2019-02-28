package com.xianneng.adaptertest.http;

import java.io.InputStream;

/**
 * 封装响应
 */
public interface IHttpListener {
    //接收上一个接口的结果
    void onSucces(InputStream inputStream);
    void onFailure();
}
