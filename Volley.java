package com.xianneng.adaptertest.http;

public class Volley {
    public static<T,M> void sendJSONRequest(T requestInfo,String url,Class<M> responce,IDataListener<M> dataListener){
        IHttpService httpService=new JsonHttpService();
        IHttpListener httpListener=new JsonHttpListener(responce,dataListener);
        //两个拼成一个任务
        HttpTask<T> httpTask=new HttpTask<T>(requestInfo,url,httpService,httpListener);
        ThreadPoolManage.getOurInstance().execute(httpTask);
    }

}
