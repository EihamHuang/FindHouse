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
import com.findhouse.activity.OrderViewActivity;
import com.findhouse.activity.PublishActivity;
import com.findhouse.activity.R;
import com.findhouse.activity.ReleasedActivity;
import com.findhouse.data.User;


public class MeFragment extends BaseFragment implements View.OnClickListener {
    private Context context;
    private ImageView image;
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

        view.findViewById(R.id.btnReleased).setOnClickListener(this);
        view.findViewById(R.id.btnRent).setOnClickListener(this);
        view.findViewById(R.id.btnStar).setOnClickListener(this);
        view.findViewById(R.id.btnPublish).setOnClickListener(this);
        view.findViewById(R.id.btnManage).setOnClickListener(this);
        view.findViewById(R.id.btnLogout).setOnClickListener(this);

        share = getActivity().getSharedPreferences("UserNow",
                Context.MODE_PRIVATE);
        name = share.getString("name", "");
        tv1.setText(name);


        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnReleased:
                Intent releasedIntent =  new Intent(getContext(), ReleasedActivity.class);
                startActivity(releasedIntent);
                break;
            case R.id.btnRent:
                Intent orderIntent =  new Intent(getContext(), OrderViewActivity.class);
                startActivity(orderIntent);
                break;
            case R.id.btnPublish:
                Intent publishIntent =  new Intent(getContext(), PublishActivity.class);
                startActivity(publishIntent);
                break;
            case R.id.btnLogout :
                share.edit()
                        .clear()
                        .apply();
                Intent intent_login = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent_login);
                getActivity().onBackPressed();
                break;
        }
    }
}
