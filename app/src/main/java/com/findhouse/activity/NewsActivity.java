package com.findhouse.activity;

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
import com.findhouse.data.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.findhouse.data.ImageTask;
import com.findhouse.data.JsonDataNews;
import com.findhouse.data.News;
import com.findhouse.data.NewsAdapter;
import com.findhouse.data.SpacesItemDecoration;
import com.findhouse.network.NetworkClient;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class NewsActivity extends AppCompatActivity {
    public static final String NEWS_KEY = "key_news";
    public static final int MSG_IMAGE = 1;

    // 新闻接口
    private String appkey = "7abf7cfcbedfb2471b914adc0041f917";
    private String top = "toutiao";
    private String url = "http://v.juhe.cn/toutiao/index?type=";
    // top(头条，默认),shehui(社会),guonei(国内),guoji(国际),yule(娱乐),tiyu(体育)junshi(军事),keji(科技),caijing(财经),shishang(时尚)

    protected EditText et_Search;
    protected Button btn_Search;

    private List<News> newsList = new ArrayList<>();
    private ImageTask imageTask;
    private HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
    private boolean hasResult = false;
    private Context mContext = NewsActivity.this;

    private static class ImgHandler extends Handler {
        private final WeakReference<NewsActivity> mActivity;

        private ImgHandler(NewsActivity activity) {
            mActivity = new WeakReference<NewsActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            NewsActivity activity = mActivity.get();
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
        setContentView(R.layout.activity_news);
        url += top;
        url += "&key=";
        url += appkey;
        NetworkClient.getRequest(url , new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

                NewsActivity.this.runOnUiThread(new Runnable() {
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
                NewsActivity.this.runOnUiThread(new Runnable() {
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
        User user=(User)getIntent().getSerializableExtra("user");
        Toast.makeText(mContext, user.getName()+" "+user.getTel(), Toast.LENGTH_SHORT).show();
    }

    private boolean parseJSON(String jsonData) {
        Gson gson = new Gson();
        JsonDataNews<News> parseResult = gson.fromJson(jsonData, new TypeToken<JsonDataNews<News>>(){}.getType());
        if(null == parseResult.getResult()) {
            return false;
        }else{
            newsList = parseResult.getResult().getData();
            return true;
        }
    }

    private void initRecyclerView(final List<News> newsList) {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(NewsActivity.this,DividerItemDecoration.VERTICAL));
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
//                    newsAdapter.setScrolling(false);
//                    newsAdapter.notifyDataSetChanged();
                    Glide.with(mContext).resumeRequests();
                }
                else {
//                    newsAdapter.setScrolling(true);
                    Glide.with(mContext).pauseRequests();
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
                Intent intent = new Intent(NewsActivity.this, ContentActivity.class);
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
