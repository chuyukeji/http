package com.xianneng.adaptertest.http;

import android.os.Handler;
import android.os.Looper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JsonHttpListener<M> implements IHttpListener{
    Class<M> responceClass;
    IDataListener<M> dataListener;

    //用于切换线程
    Handler handler=new Handler(Looper.getMainLooper());

    public JsonHttpListener(Class<M> responceClass,IDataListener dataListener){
        this.responceClass=responceClass;
        this.dataListener=dataListener;
    }

    @Override
    public void onSucces(InputStream inputStream) {
        //获取的响应结果，把byte数据转换成String数据
        String content = getContent(inputStream);
        //响应结果（json字符串）转换成对象
        final M responce= JSON.parseObject(content,responceClass);
        //把结果对象传送到调用层(利用统一的接口往外传)
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (dataListener!=null){
                    //把结果利用回调接口的方法传送到调用层，调用层用回调接口中的方法就可以获得结果
                    dataListener.onSuccessful(responce);
                }
            }
        });

    }

    private String getContent(InputStream inputStream) {
        String content =null;
        try {
            // 字节流转换成字符流，再放入缓冲中
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();// 可延长字符串
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

            } catch (IOException e) {
                System.out.print("Error" + e.toString());
            } finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.out.print("Error" + e.toString());
                }
            }
            return sb.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return content;
    }

    @Override
    public void onFailure() {

        handler.post(new Runnable() {
            @Override
            public void run() {
                //把结果利用回调接口传送到调用层，调用层用回调接口就可以获得结果
                if (dataListener!=null){
                    dataListener.onFailure();
                }
            }
        });
    }
}
