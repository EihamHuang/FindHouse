package com.findhouse.fragment;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.findhouse.activity.ContentActivity;
import com.findhouse.activity.R;
import com.findhouse.data.ImageTask;
import com.findhouse.data.JsonDataNews;
import com.findhouse.data.News;
import com.findhouse.data.NewsAdapter;
import com.findhouse.data.SpacesItemDecoration;
import com.findhouse.network.NetworkClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


public class NewsFragment extends BaseFragment {

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_news, container, false);

        url += top;
        url += "&key=";
        url += appkey;
        NetworkClient.getRequest(url , new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "网络请求错误，请重试～", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int code = response.code();
                String responseJsonData = response.body().string();
                // 解析json
                hasResult = parseJSON(responseJsonData);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(hasResult){
                            // 加载RecyclerView
                            initRecyclerView(newsList,view);
                        }
                        //  该关键词没有结果
                        else{
                            Toast.makeText(getContext(), "暂时无内容，请稍候重试～", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });

        return view;
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

    private void initRecyclerView(final List<News> newsList,final View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        final NewsAdapter newsAdapter = new NewsAdapter(newsList, getContext());
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
                    Glide.with(getContext()).resumeRequests();
                }
                else {
//                    newsAdapter.setScrolling(true);
                    Glide.with(getContext()).pauseRequests();
                }
            }
        });

        newsAdapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getContext(), "点击新闻： " + position,
                        Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putSerializable(NEWS_KEY, newsList.get(position));
                Intent intent = new Intent(getContext(), ContentActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }

        });

    }
}



