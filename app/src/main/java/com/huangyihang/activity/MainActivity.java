package com.huangyihang.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.huangyihang.data.News;
import com.huangyihang.network.NetworkClient;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private String appkey = "d3180a0872444771942636c146a32aed";
    protected EditText et_Search;
    protected TextView tv_Search;
    protected Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_Search = findViewById(R.id.et_search);
        tv_Search = findViewById(R.id.tv_search);
        searchButton = findViewById(R.id.searchButton);

        tv_Search.setMovementMethod(ScrollingMovementMethod.getInstance());

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isInputValid()){
                    String search = et_Search.getText().toString();
                    // 时政新闻
                    String url = "http://api.avatardata.cn/ActNews/Query?key=" + appkey + "&keyword=" + search;
                    // 天气
                    String url2 = "http://api.jirengu.com/getWeather.php?city=北京";
                    NetworkClient.sendRequest(url , new okhttp3.Callback() {

                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();

                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "网络请求错误，请重试～", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseJsonData = response.body().string();
                            int code = response.code();
                            final String result = parseJSON(responseJsonData);
                            Log.d("okhttp", "code: " + code);
                            Log.d("okhttp", "body: " + responseJsonData);

                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv_Search.setText(result);
                                }
                            });
                        }

                    });

                }
            }
        });
    }

    private String parseJSON(String jsonData){
        StringBuilder result = new StringBuilder();
        JsonElement jsonElement = new JsonParser().parse(jsonData);
        String data = jsonElement.getAsJsonObject().get("result").toString();
        Gson gson = new Gson();
        List<News> newsList = gson.fromJson(data, new TypeToken<List<News>>(){}.getType());
        for(News news : newsList){
            result.append("title=" + news.getTitle() + " content=" + news.getPdate() + " src=" + news.getSrc()
                    + " pdate_src=" + news.getPdate_src() + " img=" + news.getImg() + " url=" + news.getUrl() + "\n");
        }
        return result.toString();
    }

    private boolean isInputValid(){
        String tip = null;
        if(TextUtils.isEmpty(et_Search.getText())){
            tip = "搜索内容不能为空";
        }
        if(tip != null){
            Toast.makeText(getApplicationContext(), tip, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
