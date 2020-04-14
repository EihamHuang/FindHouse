package com.findhouse.network;

import androidx.annotation.NonNull;

/**
 * - @Description:  网络请求回调接口
 * - @Author:  huangyihang
 * - @Time:  2019-08-14 15:07
 */
public interface NetworkCallbackListener <T> {
    void onSuccess(@NonNull T result);

    void onError(Error error);

}
