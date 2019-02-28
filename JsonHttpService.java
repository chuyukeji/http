package com.xianneng.adaptertest.http;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonHttpService implements IHttpService{
    private String url;
    private byte[] requestData;
    IHttpListener httpListener;
    @Override
    public void setUrl(String url) {
        this.url=url;
    }

    @Override
    public void setRequestData(byte[] requestData) {
        this.requestData=requestData;
    }

    @Override
    public void setHttpCallback(IHttpListener httpListener) {
        this.httpListener=httpListener;
    }

    //真实的网络操作在这里实现
    @Override
    public void execute() {
        httpUrlconnPost();
    }


    HttpURLConnection urlConnection=null;
    private void httpUrlconnPost() {
        URL url =null;
        try {
            url=new URL(this.url);
            urlConnection = (HttpURLConnection) url.openConnection();//打开http连接
            urlConnection.setConnectTimeout(6000);//连接的超时时间
            urlConnection.setUseCaches(false);//不使用缓存
            urlConnection.setInstanceFollowRedirects(true);//是成员函数，仅作用于当前函数
            urlConnection.setReadTimeout(3000);//响应的超时时间
            urlConnection.setDoInput(true);//设置这个连接是否可以写入数据
            urlConnection.setDoOutput(true);//设置这个连接是否可以输出数据
            urlConnection.setRequestMethod("POST");//设置请求的方式，如果请求方式是GET，则实现不了
            urlConnection.setRequestProperty("Content-Type","application/json;charset=UTF-8");
            urlConnection.connect();//连接，从上述至此的配置必须要在connect之前完成，
            //------------使用字节流发送数据-------------------
            OutputStream out=urlConnection.getOutputStream();//上面用POST,这里就是OutputStream
            BufferedOutputStream bos=new BufferedOutputStream(out);//字节流放入缓冲字节流中包装
            if (requestData!=null){
                bos.write(requestData);//将要发送的requestData数据写入到缓冲bos里面
            }

            //把这个字节数组的数据写入缓冲区中
            bos.flush();//刷新缓冲区，发送数据
            out.close();
            bos.close();
            //----------字符流写入数据-------------
            // 这是一个不完全的框架，如果请求方式是GET，则实现不了
            if (urlConnection.getResponseCode()==HttpURLConnection.HTTP_OK){//得到服务器返回结果码
                InputStream in = urlConnection.getInputStream();//接着从服务器得到返回的数据
                httpListener.onSucces(in);//然后得到的响应流从接口IHttpListener这里就可以返回到框架内部了
            }

        } catch (Exception e) {
            e.printStackTrace();
            httpListener.onFailure();
        }finally {
            urlConnection.disconnect();//使用完关闭TCP连接，释放资源
        }
    }





}
