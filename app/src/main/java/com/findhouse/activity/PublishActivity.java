package com.findhouse.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.bigkoo.pickerview.OptionsPickerView;

public class PublishActivity extends AppCompatActivity {

    private SharedPreferences share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        share = getSharedPreferences("UserNow", Context.MODE_PRIVATE);



    }

}
