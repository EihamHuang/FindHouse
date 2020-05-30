package com.findhouse.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.findhouse.data.HouseDetail;
import com.findhouse.data.HouseInfo;
import com.findhouse.data.HouseOrder;
import com.findhouse.data.JsonData;
import com.findhouse.network.NetworkClient;
import com.findhouse.utils.SpiltUtil;
import com.findhouse.utils.TimeUtil;
import com.findhouse.utils.Url;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.findhouse.fragment.MainFragment.KEY_HOUSE;
import static com.findhouse.fragment.MainFragment.KEY_HOUSE_DETAIL;

public class OrderActivity extends AppCompatActivity implements View.OnClickListener {

    private String type = "/house";
    private String route = "/order";

    private TimePickerView pvTime;
    private TextView startTime;
    private TextView endTime;
    private TextView totalPrice;
    private Button btnOk;
    private Button btnCancel;

    private Bundle bundle;
    private boolean hasResult = false;
    private HouseInfo houseInfo;
    private HouseDetail houseDetail;

    private TimeUtil timeUtil = new TimeUtil();
    private String startDay = null;
    private String endDay = null;
    private int month = 0;
    private int day = 0;
    private int price = 0;

    private List<HouseOrder> houseOrder = new ArrayList<>();
    private SharedPreferences share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Intent intent = this.getIntent();
        bundle = intent.getExtras();
        houseInfo = (HouseInfo) bundle.getSerializable(KEY_HOUSE);
        houseDetail = (HouseDetail) bundle.getSerializable(KEY_HOUSE_DETAIL);

        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
        totalPrice = findViewById(R.id.totalPrice);
        btnOk = findViewById(R.id.btnOk);
        btnCancel = findViewById(R.id.btnCancel);

        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        startTime.setOnClickListener(this);
        endTime.setOnClickListener(this);
        startTime.addTextChangedListener(listener(startTime));
        endTime.addTextChangedListener(listener(endTime));

        totalPrice.setText(price+" 元");

        // 控制时间范围(如果不设置范围，则使用默认时间1900-2100年)
        // 系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2020, 0, 0);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2029, 11, 28);
        //时间选择器
        pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                TextView tv = (TextView) v;
                tv.setText(timeUtil.dateToStr(date));
            }})
            .setType(new boolean[]{true, true, true, false, false, false})
            .setLabel("年", "月", "日", "", "", "")
            .isCenterLabel(true)
            .setDividerColor(Color.DKGRAY)
            .setContentSize(21)
            .setDate(selectedDate)
            .setRangDate(startDate, endDate)
            .setDecorView(null)
            .build();

    }

    private TextWatcher listener(final View view) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(view == startTime) {
                    startDay = startTime.getText().toString();
                } else if(view == endTime){
                    endDay = endTime.getText().toString();
                }
                day = timeUtil.getTimeDifference(startDay,endDay);
                month = timeUtil.getMonth(day);
                price = month * houseInfo.getPrice();
                totalPrice.setText(price+" 元");

            }
        };
    }

    private void sendOrder() {

        // 从SharedPreferences中获取当前用户uid
        share = getSharedPreferences("UserNow",
                Context.MODE_PRIVATE);
        HouseOrder houseOrderSend = new HouseOrder();
        houseOrderSend.setHouseId(houseInfo.getId());
        houseOrderSend.setTenantId(share.getString("uid", ""));
        houseOrderSend.setLandlordId(houseDetail.getUid());
        houseOrderSend.setStartTime(startTime.getText().toString());
        houseOrderSend.setEndTime(endTime.getText().toString());
        houseOrderSend.setTotalPrice(price);

        Url baseUrl = new Url();
        baseUrl.setType(type);
        baseUrl.setRoute(route);
        String url = baseUrl.toString();

        //使用Gson将对象转换为json字符串
        Gson gson = new Gson();
        String json = gson.toJson(houseOrderSend);
        //MediaType  设置Content-Type 标头中包含的媒体类型值
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8")
                , json);

        NetworkClient.postRequest(url, requestBody, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

                OrderActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(OrderActivity.this, "网络请求错误，请重试～", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseJsonData = response.body().string();
                // 解析json
                hasResult = parseJSON(responseJsonData);
                OrderActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(hasResult){
                            Toast.makeText(OrderActivity.this, "租房成功", Toast.LENGTH_SHORT).show();
                            Intent intent_main = new Intent(OrderActivity.this, MainActivity.class);
                            startActivity(intent_main);
                            OrderActivity.this.finish();
                        }
                        //  失败
                        else{
                            Toast.makeText(OrderActivity.this, "sorry，该房子已出租", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });

    }

    private boolean parseJSON(String jsonData) {
        Gson gson = new Gson();
        JsonData<HouseOrder> parseData = gson.fromJson(jsonData, new TypeToken<JsonData<HouseOrder>>(){}.getType());
        if(null == parseData.getData()) {
            return false;
        }else{
            houseOrder = parseData.getData();
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startTime :
                pvTime.show(startTime);
                break;
            case R.id.endTime :
                pvTime.show(endTime);
                break;
            case R.id.btnOk :
                if(price>0) {
                    sendOrder();
                } else {
                    Toast.makeText(OrderActivity.this, "请选择正确的时间", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnCancel :
                onBackPressed();
        }
    }
}
