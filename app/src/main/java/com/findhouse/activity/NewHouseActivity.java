package com.findhouse.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.findhouse.data.NewHouseDetail;
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

import static com.findhouse.fragment.MainFragment.KEY_HOUSE;

public class NewHouseActivity extends AppCompatActivity implements OnBannerListener, View.OnClickListener {

    private String type = "/house";
    private String route = "/detail";
    private SpiltUtil spiltUtil = new SpiltUtil();
    private int choosePrice = 2;

    private RecyclerView rvApartment;
    private Button btnPhone;
    private Banner banner;
    private TextView houseTitle;
    private TextView housePosition;
    private TextView housePrice;
    private TextView houseOpen;
    private TextView houseArea;
    private TextView houseApartment;
    private TextView houseType;

    private TextView houseDes;

    private boolean hasResult = false;
    private HouseInfo houseInfo;
    private List<NewHouseDetail> NewHouseDetailList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_house);

        banner = findViewById(R.id.banner);
        btnPhone = findViewById(R.id.btnPhone);
        btnPhone.setOnClickListener(this);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        houseInfo = (HouseInfo) bundle.getSerializable(KEY_HOUSE);

        Url baseUrl = new Url();
        baseUrl.setType(type);
        baseUrl.setRoute(route);
        String url = baseUrl.toString()+"?houseId="+houseInfo.getId()+"&type="+houseInfo.getType();

        NetworkClient.getRequest(url, new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NewHouseActivity.this, "网络请求错误，请重试～", Toast.LENGTH_SHORT).show();
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
                            String[] urls = spiltUtil.spiltSemicolon(NewHouseDetailList.get(0).getHouseImg());
                            initImg(urls);

                            initDetail();
                        }
                        //  失败
                        else{
                            Toast.makeText(NewHouseActivity.this, "暂时无内容，请稍候重试～", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });

    }

    private void initDetail() {
        houseTitle = findViewById(R.id.houseTitle);
        houseArea = findViewById(R.id.houseArea);
        housePosition = findViewById(R.id.housePosition);
        housePrice = findViewById(R.id.housePrice);
        houseOpen = findViewById(R.id.houseOpen);
        houseApartment = findViewById(R.id.houseApartment);
        houseType = findViewById(R.id.houseType);

        houseDes = findViewById(R.id.houseDes);

        SpiltUtil spiltUtil = new SpiltUtil();
        int num = spiltUtil.spiltApartment(NewHouseDetailList.get(0).getHouseApartment());

        houseTitle.setText(houseInfo.getTitle());
        housePosition.setText(houseInfo.getRegionInfo()+" - "+houseInfo.getAreaInfo()+" - "+houseInfo.getPositionInfo());

        housePrice.setText(Html.fromHtml("价格：<font color='#000000'>"+houseInfo.getPrice()+" "+spiltUtil.priceType[choosePrice]+"</font>"));
        houseArea.setText(Html.fromHtml("建面：<font color='#000000'>"+NewHouseDetailList.get(0).getHouseArea()+" 平方米</font>"));

        houseOpen.setText(Html.fromHtml("开盘：<font color='#000000'>"+NewHouseDetailList.get(0).getHouseOpen()+"</font>"));
        houseApartment.setText(Html.fromHtml("户型：<font color='#000000'>"+num+"种</font>"));

        houseType.setText(Html.fromHtml("用途：<font color='#000000'>"+NewHouseDetailList.get(0).getHouseType()+"</font>"));


        houseDes.setText(Html.fromHtml("描述：<font color='#000000'>"+NewHouseDetailList.get(0).getHouseDes()+"</font>"));

        btnPhone.setText("联系 "+NewHouseDetailList.get(0).getUserName());

    }

    private void initImg(String[] urls) {
        List<String> imageList = new ArrayList<>();
        final RequestOptions optionsVertical = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.wait)
                .error(R.drawable.error);

        for (int i=0; i<urls.length; i++) {
            imageList.add(urls[i]);//把图片资源循环放入list里面
            //设置图片加载器，通过Glide加载图片
            banner.setImageLoader(new ImageLoader() {
                @Override
                public void displayImage(Context context, Object path, ImageView imageView) {
                    Glide.with(NewHouseActivity.this).load(path).
                            apply(optionsVertical).
                            into(imageView);
                }
            });
            //设置轮播的动画效果
            banner.setBannerAnimation(Transformer.Accordion);
            banner.setImages(imageList);//设置图片资源
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);//设置banner显示样式
            //设置指示器位置（小圆点）
            banner.setIndicatorGravity(BannerConfig.RIGHT);
            banner.setDelayTime(3*1000);//设置轮播时间3秒切换下一图
            banner.setOnBannerListener(this);//设置监听
            banner.start();//开始进行banner渲染
        }
    }

    private boolean parseJSON(String jsonData) {
        Gson gson = new Gson();
        JsonData<NewHouseDetail> parseResult = gson.fromJson(jsonData, new TypeToken<JsonData<NewHouseDetail>>(){}.getType());
        if(null == parseResult.getData()) {
            return false;
        }else{
            NewHouseDetailList = parseResult.getData();
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPhone :
                Intent dialIntent =  new Intent(Intent.ACTION_DIAL);//跳转到拨号界面，同时传递电话号码
                Uri data = Uri.parse("tel:" + NewHouseDetailList.get(0).getUserTel());
                dialIntent.setData(data);
                startActivity(dialIntent);
                break;
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
