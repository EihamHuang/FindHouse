package com.huangyihang.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huangyihang.data.ImageTask;
import com.huangyihang.data.JsonData;
import com.huangyihang.data.News;
import com.huangyihang.data.NewsAdapter;
import com.huangyihang.data.SpacesItemDecoration;
import com.huangyihang.network.NetworkClient;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static final String NEWS_KEY = "key_news";
    public static final int MSG_IMAGE = 1;

    // appkey
    private String appkey = "d3180a0872444771942636c146a32aed";
    // 旧接口
    private String url = "http://api.avatardata.cn/ActNews/Query?key=" + appkey + "&keyword=";
    // 新接口
    private String newsUrl = "http://www.xieast.com/api/news/news.php";

    protected EditText et_Search;
    protected Button btn_Search;

    private List<News> newsList = new ArrayList<>();
    private ImageTask imageTask;
    private HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
    private boolean hasResult = false;
    private Context mContext = MainActivity.this;

    private static class ImgHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;

        private ImgHandler(MainActivity activity) {
            mActivity = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mActivity.get();
            if (activity != null) {
                super.handleMessage(msg);
                switch (msg.what){
                    case MSG_IMAGE:
                        Bitmap bm = (Bitmap) msg.obj;
                        if (bm != null) {
//                            if (TextUtils.equals((String) holder.newsImg.getTag(), news.getImg())) {
//                                holder.newsImg.setImageBitmap(bm);
//                            }
                        }
                        break;
                }
            }
        }
    }

    private final ImgHandler imgHandler = new ImgHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_Search = findViewById(R.id.et_search);
        btn_Search = findViewById(R.id.btn_search);

        btn_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                    String search = et_Search.getText().toString();
//                    url += search;
                NetworkClient.sendRequest(newsUrl , new okhttp3.Callback() {

                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();

                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "网络请求错误，请重试～", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        int code = response.code();
                        String responseJsonData = response.body().string();
                        // 解析json
                        hasResult = parseJSON(responseJsonData);
                        Log.d("okhttp", "code: " + code);
                        Log.d("okhttp", "body: " + responseJsonData);
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(hasResult){
                                    // 加载RecyclerView
                                    initRecyclerView(newsList);
                                }
                                //  该关键词没有结果
                                else{
                                    Toast.makeText(mContext, "暂时无内容，请稍候重试～", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                });

            }
        });
    }

    private boolean parseJSON(String jsonData) {
        Gson gson = new Gson();
        JsonData<News> parseResult = gson.fromJson(jsonData, new TypeToken<JsonData<News>>(){}.getType());
        if(null == parseResult.getData()) {
            return false;
        }else{
            newsList = parseResult.getData();
            return true;
        }
    }

//    private void initImg(List<News> newsList){
//        if(null == newsList)
//            return;
//        imageTask = ImageTask.getIntance();
//        for(News news : newsList){
//            imageTask.getBitmap(news.getImgurl());
//        }
//    }

    private void initRecyclerView(final List<News> newsList) {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this,DividerItemDecoration.VERTICAL));
        final NewsAdapter newsAdapter = new NewsAdapter(newsList, this);
        stringIntegerHashMap.put(SpacesItemDecoration.TOP_DECORATION,25);//顶部间距
        stringIntegerHashMap.put(SpacesItemDecoration.BOTTOM_DECORATION,25);//底部间距
        recyclerView.addItemDecoration(new SpacesItemDecoration(stringIntegerHashMap));
        recyclerView.setAdapter(newsAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                    newsAdapter.setScrolling(false);
                    newsAdapter.notifyDataSetChanged();
//                    Glide.with(mContext).resumeRequests();
                }
                else {
                    newsAdapter.setScrolling(true);
//                    Glide.with(mContext).pauseRequests();
                }
            }
        });

        newsAdapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(mContext, "点击新闻： " + position,
                        Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putSerializable(NEWS_KEY, newsList.get(position));
                Intent intent = new Intent(MainActivity.this, ContentActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }

        });

    }

    private boolean isInputValid() {
        String tip = null;
        if(TextUtils.isEmpty(et_Search.getText())){
            tip = "搜索内容不能为空";
        }
        if(tip != null){
            Toast.makeText(mContext, tip, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
