package com.xianneng.adaptertest.http;

/**
 * 封装请求
 */
public interface IHttpService {
    void setUrl(String url);
    void setRequestData(byte[] requestData);
    void execute();
    //需要设置两个接口之间的关系
    void setHttpCallback(IHttpListener httpListener);
}
