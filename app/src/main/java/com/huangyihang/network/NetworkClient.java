package com.huangyihang.network;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * - @Description:
 * - @Author:  huangyihang
 * - @Time:  2019-08-14 15:54
 */
public class NetworkClient {

    public static void sendRequest(String address, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
