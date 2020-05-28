package com.findhouse.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.findhouse.data.HouseDetail;
import com.findhouse.data.HouseInfo;
import com.findhouse.data.JsonData;
import com.findhouse.network.NetworkClient;
import com.findhouse.utils.Url;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static com.findhouse.fragment.HomeFragment.KEY_HOUSE;

public class HouseActivity extends AppCompatActivity {

    private TextView title;

    private String type = "/house";
    private String route = "/detail";

    private boolean hasResult = false;
    private HouseInfo houseInfo;
    private List<HouseDetail> houseDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house);

        title = findViewById(R.id.title);

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
                            title.setText(houseDetail.get(0).getHouseDes());
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

    private boolean parseJSON(String jsonData) {
        Gson gson = new Gson();
        JsonData<HouseDetail> parseResult = gson.fromJson(jsonData, new TypeToken<JsonData<HouseDetail>>(){}.getType());
        if(null == parseResult.getData()) {
            return false;
        }else{
            houseDetail = parseResult.getData();
            return true;
        }
    }
}
