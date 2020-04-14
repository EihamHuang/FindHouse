package com.findhouse.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.findhouse.data.JsonData;
import com.findhouse.data.User;
import com.findhouse.network.NetworkClient;
import com.findhouse.utils.Url;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    Button btnRegister ;
    Button btnLogin;
    EditText et_login_name, et_login_password;

    private String type = "/user";
    private String route = "/login";
    private List<User> user = new ArrayList<>();
    private boolean hasResult = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_login_name = (EditText) findViewById(R.id.editText1);
        et_login_password = (EditText) findViewById(R.id.editText2);
        btnRegister = (Button) findViewById(R.id.buttonRegister);
        btnLogin = (Button) findViewById(R.id.buttonLogin);

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
                String user_num = et_login_name.getText().toString();
                String user_password = et_login_password.getText().toString().trim();
                // 非空验证
                if (user_num.isEmpty() || user_password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "账号或密码不能为空!", Toast.LENGTH_SHORT).show();
                    return;
                }

                User userLogin = new User();
                userLogin.setName(user_num);
                userLogin.setPass(user_password);

                Url baseUrl = new Url();
                baseUrl.setType(type);
                baseUrl.setRoute(route);
                String url = baseUrl.toString();

                Gson gson = new Gson();
                //使用Gson将对象转换为json字符串
                String json = gson.toJson(userLogin);

                //MediaType  设置Content-Type 标头中包含的媒体类型值
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8")
                        , json);

                NetworkClient.postRequest(url, requestBody, new okhttp3.Callback() {

                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();

                        LoginActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "网络请求错误，请重试～", Toast.LENGTH_SHORT).show();
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
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(hasResult){
                                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                    Intent intent_main = new Intent(LoginActivity.this,MainActivity.class);
                                    intent_main.putExtra("user",user.get(0));
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
