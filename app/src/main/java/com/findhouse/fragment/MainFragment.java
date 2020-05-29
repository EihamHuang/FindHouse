package com.findhouse.fragment;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.findhouse.activity.HouseActivity;
import com.findhouse.activity.NewHouseActivity;
import com.findhouse.activity.R;
import com.findhouse.adapter.HouseAdapter;
import com.findhouse.data.HouseInfo;
import com.findhouse.data.JsonData;
import com.findhouse.utils.SpacesItemDecoration;
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


public class MainFragment extends BaseFragment {
    public static final String KEY_HOUSE = "key_house";
    public static final String KEY_HOUSE_DETAIL = "key_house_detail";

    private List<HouseInfo> houseList = new ArrayList<>();
    private HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
    private boolean hasResult = false;

    private String type = "/house";
    private String route = "/list";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_main, container, false);

        Url baseUrl = new Url();
        baseUrl.setType(type);
        baseUrl.setRoute(route);
        String url = baseUrl.toString();

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
                String responseJsonData = response.body().string();
                // 解析json
                hasResult = parseJSON(responseJsonData);
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
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
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
//                    houseAdapter.setScrolling(false);
//                    houseAdapter.notifyDataSetChanged();
                    Glide.with(getContext()).resumeRequests();
                }
                else {
//                    houseAdapter.setScrolling(true);
                    Glide.with(getContext()).pauseRequests();
                }
            }
        });

        houseAdapter.setOnItemClickListener(new HouseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                HouseInfo houseInfo = houseList.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable(KEY_HOUSE, houseInfo);
                if(houseInfo.getType().equals("xinfang")) {
                    Intent intent = new Intent(getContext(), NewHouseActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(getContext(), HouseActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }

        });

    }

}
