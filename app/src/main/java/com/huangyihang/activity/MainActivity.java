package com.huangyihang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.huangyihang.data.ImageUtils;
import com.huangyihang.data.JsonData;
import com.huangyihang.data.News;
import com.huangyihang.data.NewsAdapter;
import com.huangyihang.data.SpacesItemDecoration;
import com.huangyihang.network.NetworkClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static final String NEWS_KEY = "key_news";

    // appkey
    private String appkey = "d3180a0872444771942636c146a32aed";
    // 时政新闻接口
    private String url = "http://api.avatardata.cn/ActNews/Query?key=" + appkey + "&keyword=";

    protected EditText et_Search;
    protected Button btn_Search;

    private List<News> newsList = new ArrayList<>();
    private ImageUtils imageUtils;
    private HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
    private boolean hasResult = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_Search = findViewById(R.id.et_search);
        btn_Search = findViewById(R.id.btn_search);

        btn_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isInputValid()){
                    String search = et_Search.getText().toString();
                    url += search;
                    NetworkClient.sendRequest(url , new okhttp3.Callback() {

                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();

                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "网络请求错误，请重试～", Toast.LENGTH_SHORT).show();
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
                                        // 加载图片
                                        initImg(newsList);
                                        // 加载RecyclerView
                                        initRecyclerView(newsList);
                                    }
                                    //  该关键词没有结果
                                    else{
                                        Toast.makeText(MainActivity.this, "该关键词暂时无内容，请输入其他关键词～", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                    });

                }
            }
        });
    }

    private boolean parseJSON(String jsonData) {
        Gson gson = new Gson();
        JsonData<News> parseResult = gson.fromJson(jsonData, new TypeToken<JsonData<News>>(){}.getType());
        if(null == parseResult.getResult()) {
            return false;
        }else{
            newsList = parseResult.getResult();
            return true;
        }
    }

    private void initImg(List<News> newsList){
        if(null == newsList)
            return;
        imageUtils = ImageUtils.getIntance();
        for(News news : newsList){
            Bitmap srcBitmap = imageUtils.getBitmap(news.getImg());
            news.setBitmap(srcBitmap);
        }
    }

    private void initRecyclerView(final List<News> newsList) {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        NewsAdapter adapter = new NewsAdapter(newsList);
        stringIntegerHashMap.put(SpacesItemDecoration.TOP_DECORATION,50);//顶部间距
        stringIntegerHashMap.put(SpacesItemDecoration.BOTTOM_DECORATION,50);//底部间距
        recyclerView.addItemDecoration(new SpacesItemDecoration(stringIntegerHashMap));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, "点击新闻： " + position,
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
            Toast.makeText(getApplicationContext(), tip, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
