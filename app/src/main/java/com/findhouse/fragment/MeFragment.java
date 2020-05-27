package com.findhouse.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.fragment.app.Fragment;

import com.findhouse.activity.LoginActivity;
import com.findhouse.activity.R;
import com.findhouse.data.User;


public class MeFragment extends BaseFragment {
    private Context context;
    private ImageView im;
    private TextView tv1;
    private TextView tv2;
    private Button btnLogout;
    private Button btn5;
    private Button ord;
    private Button accept;
    private Button btn3;

    private String name;
    private String pass;
    private SharedPreferences share;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);

        tv1 = view.findViewById(R.id.textView1);
//        tv2 = (TextView)view.findViewById(R.id.textView2);
        btnLogout = view.findViewById(R.id.buttonLogout);
//        btn5 = (Button)view.findViewById(R.id.button5);
//        ord = (Button)view.findViewById(R.id.btnorder);
//        accept = (Button)view.findViewById(R.id.btnaccept);
//        btn3 = (Button)view.findViewById(R.id.button3);
//
        share = getActivity().getSharedPreferences("Login",
                Context.MODE_PRIVATE);
        name = share.getString("Name", "");
        tv1.setText(name);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 清除SharedPreferences的数据
                share.edit()
                        .clear()
                        .apply();
                Intent intent_login = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent_login);
                getActivity().onBackPressed();
            }
        });
//
//        btn3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent3= new Intent(context, AboutusActivity.class);
//                startActivity(intent3);
//            }
//        });
//
//        btn5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                modifyInstallationUser();
//            }
//        });
//
//        ord.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent_order = new Intent(context, MyOrderActivity.class);
//                startActivity(intent_order);
//            }
//        });
//
//        accept.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent_accept = new Intent(context, MyOrderAcceptActivity.class);
//                startActivity(intent_accept);
//            }
//        });

        return view;
    }

}
