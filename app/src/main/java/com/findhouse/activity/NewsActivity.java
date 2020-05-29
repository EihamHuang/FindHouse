package com.findhouse.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.findhouse.data.News;


public class NewsActivity extends AppCompatActivity {

    protected TextView tv_Content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        News news = (News) bundle.getSerializable("key_news");

        tv_Content = findViewById(R.id.news_content);
        tv_Content.setText(news.getTitle());

    }
}
