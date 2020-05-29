package com.findhouse.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.findhouse.data.HouseDetail;
import com.findhouse.data.HouseInfo;
import com.findhouse.utils.SpiltUtil;
import com.findhouse.utils.TimeUtil;
import com.youth.banner.Banner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.findhouse.fragment.MainFragment.KEY_HOUSE;

public class OrderActivity extends AppCompatActivity implements View.OnClickListener {

    private String type = "/house";
    private String route = "/detail";
    private SpiltUtil spiltUtil = new SpiltUtil();

    private TimePickerView pvTime;
    private TextView startTime;
    private TextView endTime;

    private Bundle bundle;
    private boolean hasResult = false;
    private HouseInfo houseInfo;

    private TimeUtil timeUtil = new TimeUtil();
    private String startDate = "2020-01-02";
    private String endDate = "2019-01-01";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        int day = timeUtil.getTimeDifference(startDate,endDate);

        Intent intent = this.getIntent();
        bundle = intent.getExtras();
        houseInfo = (HouseInfo) bundle.getSerializable(KEY_HOUSE);

        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);

        startTime.setOnClickListener(this);
        endTime.setOnClickListener(this);

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击组件的点击事件
                pvTime.show(endTime);
            }
        });

        // 控制时间范围(如果不设置范围，则使用默认时间1900-2100年)
        // 系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2020, 0, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2029, 11, 28);
        //时间选择器
        pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                TextView btn = (TextView) v;
                btn.setText(timeUtil.dateToStr(date));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startTime :
                pvTime.show(startTime);
                startDate = startTime.getText().toString();
                break;
            case R.id.endTime :
                pvTime.show(endTime);
                endDate = endTime.getText().toString();
                break;
        }

    }
}
