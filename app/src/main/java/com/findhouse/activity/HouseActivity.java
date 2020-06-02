package com.findhouse.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.findhouse.data.HouseDetail;

import com.findhouse.data.HouseInfo;
import com.findhouse.adapter.InstallEntity;
import com.findhouse.data.JsonData;
import com.findhouse.network.NetworkClient;
import com.findhouse.utils.StringUtil;
import com.findhouse.utils.UrlUtil;
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
import static com.findhouse.fragment.MainFragment.KEY_HOUSE_DETAIL;

public class HouseActivity extends AppCompatActivity implements OnBannerListener, View.OnClickListener {

    private String type = "/house";
    private String route = "/detail";
    private StringUtil stringUtil = new StringUtil();
    private int choosePrice = 0;

    private RecyclerView recyclerView;
    private Button btnPhone;
    private Button btnOrder;
    private Banner banner;
    private TextView houseTitle;
    private TextView housePosition;
    private TextView housePrice;
    private TextView houseArea;
    private TextView houseApartment;
    private TextView houseFix;
    private TextView houseOrientation;
    private TextView houseFloor;
    private TextView houseDes;

    private Bundle bundle;
    private boolean hasResult = false;
    private HouseInfo houseInfo;
    private List<HouseDetail> houseDetailList;
    private List<InstallEntity> installEntityList = new ArrayList<>();
    private SharedPreferences share;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house);

        banner = findViewById(R.id.banner);
        btnPhone = findViewById(R.id.btnPhone);
        btnOrder = findViewById(R.id.btnOrder);
        btnPhone.setOnClickListener(this);
        btnOrder.setOnClickListener(this);

        Intent intent = this.getIntent();
        bundle = intent.getExtras();
        houseInfo = (HouseInfo) bundle.getSerializable(KEY_HOUSE);
        share = getSharedPreferences("UserNow",
                Context.MODE_PRIVATE);
        uid = share.getString("uid", "");

        UrlUtil baseUrlUtil = new UrlUtil();
        baseUrlUtil.setType(type);
        baseUrlUtil.setRoute(route);
        String url = baseUrlUtil.toString()+"?houseId="+houseInfo.getId()+"&type="+houseInfo.getType();

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
                            String[] urls = stringUtil.spiltSemicolon(houseDetailList.get(0).getHouseImg());
                            initImg(urls);

                            initDetail();
                        }
                        //  失败
                        else{
                            Toast.makeText(HouseActivity.this, "暂时无内容～", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });

    }

    private void initDetail() {
        // 未出租的房子才可租房
        if(houseInfo.getIsOrder().equals("0")) {
            btnOrder.setEnabled(true);
            btnOrder.setText("租房");
            btnOrder.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        houseTitle = findViewById(R.id.houseTitle);
        houseArea = findViewById(R.id.houseArea);
        housePosition = findViewById(R.id.housePosition);
        housePrice = findViewById(R.id.housePrice);
        houseApartment = findViewById(R.id.houseApartment);
        houseFix = findViewById(R.id.houseFix);
        houseOrientation = findViewById(R.id.houseOrientation);
        houseFloor = findViewById(R.id.houseFloor);
        houseDes = findViewById(R.id.houseDes);

        String type = houseInfo.getType();
        switch (type) {
            case "二手房" :
                choosePrice = 0;
                break;
            case "租房" :
                choosePrice = 1;
                btnOrder.setVisibility(View.VISIBLE);
                break;
            case "新房" :
                choosePrice = 2;
                break;
        }

        if(uid.equals(houseInfo.getId())) {

        }

        initInstall();

        houseTitle.setText(houseInfo.getTitle());
        housePosition.setText(houseInfo.getRegionInfo()+" - "+houseInfo.getAreaInfo()+" - "+houseInfo.getPositionInfo());

        housePrice.setText(Html.fromHtml("价格：<font color='#000000'>"+houseInfo.getPrice()+" "+ stringUtil.priceType[choosePrice]+"</font>"));
        houseArea.setText(Html.fromHtml("面积：<font color='#000000'>"+houseDetailList.get(0).getHouseArea()+" 平方米</font>"));

        houseApartment.setText(Html.fromHtml("房型：<font color='#000000'>"+houseDetailList.get(0).getHouseApartment()+"</font>"));
        houseFix.setText(Html.fromHtml("装修：<font color='#000000'>"+houseDetailList.get(0).getHouseFix()+"</font>"));

        houseOrientation.setText(Html.fromHtml("朝向：<font color='#000000'>"+houseDetailList.get(0).getHouseOrientation()+"</font>"));
        houseFloor.setText(Html.fromHtml("楼层：<font color='#000000'>"+houseDetailList.get(0).getHouseFloor()+"</font>"));

        houseDes.setText(Html.fromHtml("描述：<font color='#000000'>"+houseDetailList.get(0).getHouseDes()+"</font>"));

        btnPhone.setText("联系 "+houseDetailList.get(0).getUserName());

    }

    private void initInstall() {
        int[] installId = stringUtil.spiltInstall(houseDetailList.get(0).getHouseInstall());
        for (int i = 0; i < installId.length; i++) {
            InstallEntity installEntity = new InstallEntity();
            installEntity.setId(installId[i]);
            installEntity.setInstall(stringUtil.installType[i]);
            installEntityList.add(installEntity);
        }

        recyclerView = findViewById(R.id.houseInstall);
        GridLayoutManager layoutManager = new GridLayoutManager(this,5) {
            @Override
            // 禁止滑动
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(layoutManager);
        //直接new匿名类，不需要单独创建Adapter类文件。
        recyclerView.setAdapter(new BaseQuickAdapter<InstallEntity, BaseViewHolder>(R.layout.install_item, installEntityList) {
            @Override
            protected void convert(BaseViewHolder helper, InstallEntity item) {
                // 用反射获取图片资源ID
                try {
                    int imgID = R.drawable.class.getField( "install"+String.valueOf(helper.getLayoutPosition())).getInt(new R.drawable());
                    helper.setText(R.id.tv_houseInstall, item.getInstall());
                    helper.setImageResource(R.id.image_houseInstall, imgID);

                    // 没有该设施则变灰
                    if(item.getId()==0) {
                        helper.setTextColor(R.id.tv_houseInstall, Color.parseColor("#ffcccccc"));
                        ImageView imageView = helper.getView(R.id.image_houseInstall);
                        imageView.setColorFilter(Color.parseColor("#ffcccccc"));
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void initImg(String[] urls) {
        List<String> imageList = new ArrayList<>();
        final RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.wait)
                .error(R.drawable.error);

        for (int i=0; i<urls.length; i++) {
            imageList.add(urls[i]);//把图片资源循环放入list里面
            //设置图片加载器，通过Glide加载图片
            banner.setImageLoader(new ImageLoader() {
                @Override
                public void displayImage(Context context, Object path, ImageView imageView) {
                    Glide.with(HouseActivity.this).load(path).
                            apply(options).
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
        JsonData<HouseDetail> parseResult = gson.fromJson(jsonData, new TypeToken<JsonData<HouseDetail>>(){}.getType());
        if(null == parseResult.getData()) {
            return false;
        }else{
            houseDetailList = parseResult.getData();
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPhone :
                Intent dialIntent =  new Intent(Intent.ACTION_DIAL);//跳转到拨号界面，同时传递电话号码
                Uri data = Uri.parse("tel:" + houseDetailList.get(0).getUserTel());
                dialIntent.setData(data);
                startActivity(dialIntent);
                break;
            case R.id.btnOrder :
                Intent orderIntent =  new Intent(HouseActivity.this, OrderActivity.class);
                bundle.putSerializable(KEY_HOUSE_DETAIL, houseDetailList.get(0));
                orderIntent.putExtras(bundle);
                startActivity(orderIntent);
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
