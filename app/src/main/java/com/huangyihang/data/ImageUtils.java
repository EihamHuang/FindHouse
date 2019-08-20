package com.huangyihang.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.huangyihang.network.NetworkClient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Response;

import static com.huangyihang.activity.MainActivity.MSG_IMAGE;

/**
 * - @Description:  图片处理
 * - @Author:  huangyihang
 * - @Time:  2019/8/18 15:09
 */
public class ImageUtils {
    private static Bitmap bitmap = null;
    private Handler handler = new Handler();

    private ImageUtils() {}
    private static final ImageUtils imageUtils = new ImageUtils();
    public static ImageUtils getIntance(){
        return imageUtils;
    }

    public void getBitmap(String url) {
        if(TextUtils.isEmpty(url)){
            return;
        }
        NetworkClient.sendRequest(url, new okhttp3.Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int code = response.code();
                Log.d("img", "code: " + code);
                InputStream in = response.body().byteStream();
                bitmap = BitmapFactory.decodeStream(in);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = MSG_IMAGE;
                        message.obj = bitmap;
                        handler.sendMessage(message);
                    }
                }).start();
            }
        });
    }

    public byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    public Bitmap RGB_565(Bitmap bitmap) {
        byte[] bytes = bitmap2Bytes(bitmap);
        Log.d("img", "压缩前: " + bytes.length);
        BitmapFactory.Options options2 = new BitmapFactory.Options();
        options2.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap1 = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options2);
        byte[] bytes1 = bitmap2Bytes(bitmap1);
        Log.d("img", "压缩后: " + bytes1.length);
        return bitmap1;
    }

}
