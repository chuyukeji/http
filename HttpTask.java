package com.xianneng.adaptertest.http;

import com.alibaba.fastjson.JSON;

import java.io.UnsupportedEncodingException;

public class HttpTask<T> implements Runnable {
    private IHttpService httpService;
    private IHttpListener httpListener;
    public<T> HttpTask(T requestInfo,String url,IHttpService httpService,IHttpListener httpListener){
        this.httpService=httpService;
        this.httpListener=httpListener;
        httpService.setUrl(url);
        httpService.setHttpCallback(httpListener);
        if (requestInfo!=null){
            String requestContent= JSON.toJSONString(requestInfo);
            try {
                httpService.setRequestData(requestContent.getBytes("utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        httpService.execute();
    }
}
