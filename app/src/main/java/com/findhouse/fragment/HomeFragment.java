package com.findhouse.fragment;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.findhouse.activity.ContentActivity;
import com.findhouse.activity.R;
import com.findhouse.data.HouseAdapter;
import com.findhouse.data.HouseInfo;
import com.findhouse.data.ImageTask;
import com.findhouse.data.JsonData;
import com.findhouse.data.SpacesItemDecoration;
import com.findhouse.network.NetworkClient;
import com.findhouse.utils.Url;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


public class HomeFragment extends BaseFragment {
    public static final String HOUSE_KEY = "key_house";
    public static final int MSG_IMAGE = 1;

    private List<HouseInfo> houseList = new ArrayList<>();
    private ImageTask imageTask;
    private HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
    private boolean hasResult = false;

    private String type = "/house";
    private String route = "/list";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        String city = "sh";
        String typeTwo = "ershou";

        Url baseUrl = new Url();
        baseUrl.setType(type);
        baseUrl.setRoute(route);
        String url = baseUrl.toString()+"?city="+city+"&type="+typeTwo;

        NetworkClient.getRequest(url, new okhttp3.Callback() {

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
                Log.d("okhttp", "code: " + code);
                Log.d("okhttp", "body: " + responseJsonData);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(hasResult){
                            initRecyclerView(houseList,view);
                        }
                        //  失败
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
        JsonData<HouseInfo> parseResult = gson.fromJson(jsonData, new TypeToken<JsonData<HouseInfo>>(){}.getType());
        if(null == parseResult.getData()) {
            return false;
        }else{
            houseList = parseResult.getData();
            return true;
        }
    }

    private void initRecyclerView(final List<HouseInfo> houseList,final View view) {
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        final HouseAdapter houseAdapter = new HouseAdapter(houseList, getContext());
        stringIntegerHashMap.put(SpacesItemDecoration.TOP_DECORATION,25);//顶部间距
        stringIntegerHashMap.put(SpacesItemDecoration.BOTTOM_DECORATION,25);//底部间距
        recyclerView.addItemDecoration(new SpacesItemDecoration(stringIntegerHashMap));
        recyclerView.setAdapter(houseAdapter);

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

    }

}
