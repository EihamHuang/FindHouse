package com.findhouse.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.findhouse.data.JsonData;
import com.findhouse.data.User;
import com.findhouse.network.NetworkClient;
import com.findhouse.utils.MD5Util;
import com.findhouse.utils.UrlUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private Button btnRegister ;
    private Button btnLogin;
    private EditText et_login_name, et_login_password;

    private String type = "/user";
    private String route = "/login";
    private List<User> user = new ArrayList<>();
    private MD5Util md5Util = new MD5Util();
    private boolean hasResult = false;

    private String name;
    private String pass;
    private SharedPreferences share;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_login_name = findViewById(R.id.editText1);
        et_login_password = findViewById(R.id.editText2);
        btnRegister = findViewById(R.id.buttonRegister);
        btnLogin = findViewById(R.id.buttonLogin);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = et_login_name.getText().toString();
                pass = et_login_password.getText().toString().trim();
                // 非空验证
                if (name.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "账号或密码不能为空!", Toast.LENGTH_SHORT).show();
                }
                else {
                    doLogin();
                }
            }
        });


    }
    private void doLogin() {
        User userLogin = new User();
        userLogin.setName(name);
        userLogin.setPass(md5Util.stringToMD5(pass));

        UrlUtil baseUrlUtil = new UrlUtil();
        baseUrlUtil.setType(type);
        baseUrlUtil.setRoute(route);
        String url = baseUrlUtil.toString();

        Gson gson = new Gson();
        //使用Gson将对象转换为json字符串
        String json = gson.toJson(userLogin);

        //MediaType  设置Content-Type 标头中包含的媒体类型值
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8")
                , json);

        NetworkClient.postRequest(url, requestBody, new okhttp3.Callback() {

            @Override
            public void onFailure(@NotNull Call call,@NotNull IOException e) {
                e.printStackTrace();

                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "网络请求错误，请重试～", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call,@NotNull Response response) throws IOException {
                String responseJsonData = response.body().string();
                // 解析json
                hasResult = parseJSON(responseJsonData);
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(hasResult){
                            // 创建SharedPreferences对象用于储存帐号和密码
                            share = getSharedPreferences("UserNow", Context.MODE_PRIVATE);
                            share.edit()
                                    .putString("uid", user.get(0).getUid())
                                    .putString("name", name)
                                    .putString("pass", pass)
                                    .putString("tel", user.get(0).getTel())
                                    .putString("authorization", String.valueOf(user.get(0).getAuthorization()))
                                    .apply();

                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            Intent intent_main = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent_main);
                            LoginActivity.this.finish();
                        }
                        //  失败
                        else{
                            Toast.makeText(LoginActivity.this, "账户或密码错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });
    }
    private boolean parseJSON(String jsonData) {
        Gson gson = new Gson();
        JsonData<User> parseData = gson.fromJson(jsonData, new TypeToken<JsonData<User>>(){}.getType());
        if(null == parseData.getData()) {
            return false;
        }else{
            user = parseData.getData();
            return true;
        }
    }
}
