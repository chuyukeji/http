package com.xianneng.adaptertest.http;

/**
 * 回调用层的接口
 */
public interface IDataListener<M> {
    void onSuccessful(M m);
    void onFailure();
}
