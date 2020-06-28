package com.findhouse.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.findhouse.adapter.HouseAdapter;
import com.findhouse.data.HouseInfo;
import com.findhouse.data.JsonData;
import com.findhouse.network.NetworkClient;
import com.findhouse.utils.SpacesItemDecoration;
import com.findhouse.utils.UrlUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static com.findhouse.fragment.MainFragment.KEY_FROM;
import static com.findhouse.fragment.MainFragment.KEY_HOUSE;

public class OrderViewActivity extends AppCompatActivity {
    private String type = "/house";
    private String route = "/orderList";

    private List<HouseInfo> houseList = new ArrayList<>();
    private HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
    private boolean hasResult = false;
    private Context context;

    private SharedPreferences share;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_view);
        context = this;

        share = getSharedPreferences("UserNow",
                Context.MODE_PRIVATE);
        uid = share.getString("uid", "");

        UrlUtil baseUrlUtil = new UrlUtil();
        baseUrlUtil.setType(type);
        baseUrlUtil.setRoute(route);
        String url = baseUrlUtil.toString()+"?tenantId="+uid;

        NetworkClient.getRequest(url, new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(OrderViewActivity.this, "网络请求错误，请重试～", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseJsonData = response.body().string();
                // 解析json
                hasResult = parseJSON(responseJsonData);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(hasResult){
                            initRecyclerView(houseList);
                        }
                        //  失败
                        else{
                            Toast.makeText(OrderViewActivity.this, "暂时无内容～", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });
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

    private void initRecyclerView(final List<HouseInfo> houseList) {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL));
        final HouseAdapter houseAdapter = new HouseAdapter(houseList, context);
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
                    Glide.with(context).resumeRequests();
                }
                else {
//                    houseAdapter.setScrolling(true);
                    Glide.with(context).pauseRequests();
                }
            }
        });

        houseAdapter.setOnItemClickListener(new HouseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                HouseInfo houseInfo = houseList.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable(KEY_HOUSE, houseInfo);
                bundle.putString(KEY_FROM, "OrderViewActivity");
                if(houseInfo.getType().equals("新房")) {
                    Intent intent = new Intent(OrderViewActivity.this, NewHouseActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(OrderViewActivity.this, HouseActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }

        });

    }

}
