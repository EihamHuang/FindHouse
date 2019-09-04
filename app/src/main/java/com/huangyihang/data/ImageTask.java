package com.huangyihang.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.huangyihang.network.NetworkClient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.huangyihang.activity.MainActivity.MSG_IMAGE;

/**
 * - @Description:  图片处理
 * - @Author:  huangyihang
 * - @Time:  2019/8/18 15:09
 */
public class ImageTask extends AsyncTask<String ,Void, Bitmap> {
    private ImageView mImageView;
    private Context mContext;
    private String mUrl;
//    private Handler handler = new Handler();

    public ImageTask(ImageView imageView, Context context) {
        mImageView = imageView;
        mContext = context;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        mUrl = params[0];
        Bitmap bitmap = downloadBitmap(mUrl);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
        return scaledBitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (mUrl.equals(mImageView.getTag())){
            mImageView.setImageBitmap(bitmap);
        }
    }

//    private Bitmap compressRGB565(Bitmap srcBitmap) {
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inPreferredConfig = Bitmap.Config.RGB_565;
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test, options);
//        return bitmap;
//    }

    private Bitmap downloadBitmap(String url) {
        if(TextUtils.isEmpty(url)){
            return null;
        }
        Bitmap bitmap = null;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            bitmap = BitmapFactory.decodeStream(response.body().byteStream());
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    Message message = new Message();
//                    message.what = MSG_IMAGE;
//                    message.obj = bitmap;
//                    handler.sendMessage(message);
//                }
//            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
