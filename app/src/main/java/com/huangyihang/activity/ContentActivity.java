package com.huangyihang.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.huangyihang.data.News;

import static com.huangyihang.activity.MainActivity.NEWS_KEY;

public class ContentActivity extends AppCompatActivity {

    protected TextView tv_Content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        News news = (News) bundle.getSerializable(NEWS_KEY);

        tv_Content = findViewById(R.id.news_content);
        tv_Content.setText(news.getTitle());

    }
}
