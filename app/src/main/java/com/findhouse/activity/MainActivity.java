package com.findhouse.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.findhouse.fragment.MainFragment;
import com.findhouse.fragment.MeFragment;
import com.findhouse.fragment.NewsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private Fragment f1;
    private Fragment f2;
    private Fragment f3;
    private BottomNavigationView navigation;

    private String name;
    private String pass;
    private SharedPreferences share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        share = getSharedPreferences("UserNow",
                Context.MODE_PRIVATE);
        name = share.getString("name", "");
        pass = share.getString("pass", "");
        // 未登录的先登录
        if(name.isEmpty() || pass.isEmpty()) {
            Intent intent_login = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent_login);
            MainActivity.this.finish();
        }

        setContentView(R.layout.activity_main);

        navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        setFragment(1);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.item_home:
                    setFragment(1);
                    return true;
                case R.id.item_news:
                    setFragment(2);
                    return true;
                case R.id.item_me:
                    setFragment(3);
                    return true;
            }
            return false;
        }
    };

    // 设置fragment
    private void setFragment(int i){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        hideFragment(transaction);
        switch (i){
            case 1:
                if(f1==null){
                    f1 = new MainFragment();
                    transaction.add(R.id.fragment_container,f1);
                }else{
                    transaction.show(f1);
                }
                break;
            case 2:
                if(f2==null){
                    f2 = new NewsFragment();
                    transaction.add(R.id.fragment_container,f2);
                }else{
                    transaction.show(f2);
                }
                break;
            case 3:
                if(f3==null){
                    f3 = new MeFragment();
                    transaction.add(R.id.fragment_container,f3);
                }else{
                    transaction.show(f3);
                }
                break;

            default:
                break;
        }
        transaction.commit();
    }

    // 隐藏所有fragment
    private void hideFragment(FragmentTransaction transaction){
        if(f1!=null){
            transaction.hide(f1);
        }
        if(f2!=null){
            transaction.hide(f2);
        }
        if(f3!=null){
            transaction.hide(f3);
        }

    }

}
