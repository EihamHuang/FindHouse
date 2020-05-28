package com.findhouse.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.findhouse.data.HouseDetail;

import com.findhouse.data.HouseInfo;
import com.findhouse.data.JsonData;
import com.findhouse.network.NetworkClient;
import com.findhouse.utils.SpiltUtil;
import com.findhouse.utils.Url;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static com.findhouse.fragment.HomeFragment.KEY_HOUSE;

public class HouseActivity extends AppCompatActivity implements OnBannerListener {

    private String type = "/house";
    private String route = "/detail";
    private String[] priceType = {"万", "元/月"};
    private int choose = 0;

    private Banner banner;
    private TextView houseTitle;
    private TextView housePosition;
    private TextView houseTotalPrice;
    private TextView houseArea;
    private TextView houseApartment;
    private TextView houseFix;
    private TextView houseOrientation;
    private TextView houseFloor;
    private TextView houseDes;
    private TextView houseInstall;

    private boolean hasResult = false;
    private HouseInfo houseInfo;
    private List<HouseDetail> houseDetailList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house);

        banner = findViewById(R.id.banner);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        houseInfo = (HouseInfo) bundle.getSerializable(KEY_HOUSE);

        Url baseUrl = new Url();
        baseUrl.setType(type);
        baseUrl.setRoute(route);
        String url = baseUrl.toString()+"?houseId="+houseInfo.getId();

        NetworkClient.getRequest(url, new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(HouseActivity.this, "网络请求错误，请重试～", Toast.LENGTH_SHORT).show();
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(hasResult){
                            SpiltUtil spiltUtil = new SpiltUtil();
                            String[] urls = spiltUtil.spiltSemicolon(houseDetailList.get(0).getHouseImg());
                            initImg(urls);

                            initDetail();
                        }
                        //  失败
                        else{
                            Toast.makeText(HouseActivity.this, "暂时无内容，请稍候重试～", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });

    }

    private void initDetail() {
//        RecyclerView recyclerView = findViewById(R.id.houseDetail);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//        HouseDetailAdapter houseDetailAdapter = new HouseDetailAdapter(houseDetailList, this);
//        recyclerView.setAdapter(houseDetailAdapter);
        houseTitle = findViewById(R.id.houseTitle);
        houseArea = findViewById(R.id.houseArea);
        housePosition = findViewById(R.id.housePosition);
        houseTotalPrice = findViewById(R.id.houseTotalPrice);
        houseApartment = findViewById(R.id.houseApartment);
        houseFix = findViewById(R.id.houseFix);
        houseOrientation = findViewById(R.id.houseOrientation);
        houseFloor = findViewById(R.id.houseFloor);
        houseInstall = findViewById(R.id.houseInstall);
        houseDes = findViewById(R.id.houseDes);

        if(houseInfo.getType().equals("zufang")) {
            choose = 1;
        }

        houseTitle.setText(houseInfo.getTitle());
        housePosition.setText(houseInfo.getRegionInfo()+" - "+houseInfo.getAreaInfo()+" - "+houseInfo.getPositionInfo());

        houseTotalPrice.setText(Html.fromHtml("价格：<font color='#000000'>"+houseInfo.getTotalPrice()+" "+priceType[choose]+"</font>"));
        houseArea.setText(Html.fromHtml("面积：<font color='#000000'>"+houseDetailList.get(0).getHouseArea()+" 平方米</font>"));

        houseApartment.setText(Html.fromHtml("房型：<font color='#000000'>"+houseDetailList.get(0).getHouseApartment()+"</font>"));
        houseFix.setText(Html.fromHtml("装修：<font color='#000000'>"+houseDetailList.get(0).getHouseFix()+"</font>"));

        houseOrientation.setText(Html.fromHtml("朝向：<font color='#000000'>"+houseDetailList.get(0).getHouseOrientation()+"</font>"));
        houseFloor.setText(Html.fromHtml("楼层：<font color='#000000'>"+houseDetailList.get(0).getHouseFloor()+"</font>"));

        houseDes.setText(Html.fromHtml("描述：<font color='#000000'>"+houseDetailList.get(0).getHouseDes()+"</font>"));

    }

    private void initImg(String[] urls) {
        List<String> imageList = new ArrayList<>();
        String[] mTitle = new String[]{"", "", ""};
        List<String> titleList = new ArrayList<>();

        final RequestOptions optionsVertical = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground);

        for (int i=0; i<urls.length; i++) {
            imageList.add(urls[i]);//把图片资源循环放入list里面
            titleList.add(mTitle[i]);//把标题循环设置进列表里面
            //设置图片加载器，通过Glide加载图片
            banner.setImageLoader(new ImageLoader() {
                @Override
                public void displayImage(Context context, Object path, ImageView imageView) {
                    Glide.with(HouseActivity.this).load(path).
                            apply(optionsVertical).
                            into(imageView);
                }
            });
            //设置轮播的动画效果,里面有很多种特效,可以到GitHub上查看文档。
            banner.setBannerAnimation(Transformer.Accordion);
            banner.setImages(imageList);//设置图片资源
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);//设置banner显示样式（带标题的样式）
            banner.setBannerTitles(titleList); //设置标题集合（当banner样式有显示title时）
            //设置指示器位置（即图片下面的那个小圆点）
            banner.setIndicatorGravity(BannerConfig.CENTER);
            banner.setDelayTime(3*1000);//设置轮播时间3秒切换下一图
            banner.setOnBannerListener(this);//设置监听
            banner.start();//开始进行banner渲染
        }
    }

    private boolean parseJSON(String jsonData) {
        Gson gson = new Gson();
        JsonData<HouseDetail> parseResult = gson.fromJson(jsonData, new TypeToken<JsonData<HouseDetail>>(){}.getType());
        if(null == parseResult.getData()) {
            return false;
        }else{
            houseDetailList = parseResult.getData();
            return true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        banner.startAutoPlay();//开始轮播
    }

    @Override
    protected void onStop() {
        super.onStop();
        banner.stopAutoPlay();//结束轮播
    }

    //轮播图点击事件
    @Override
    public void OnBannerClick(int position) {

    }

}
