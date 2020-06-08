package com.findhouse.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.fragment.app.Fragment;

import com.bigkoo.pickerview.OptionsPickerView;
import com.findhouse.activity.LoginActivity;
import com.findhouse.activity.OrderViewActivity;
import com.findhouse.activity.PublishActivity;
import com.findhouse.activity.R;
import com.findhouse.activity.ReleasedActivity;
import com.findhouse.activity.StarViewActivity;
import com.findhouse.data.User;

import java.util.ArrayList;

import static com.findhouse.fragment.MainFragment.KEY_TYPE;


public class MeFragment extends BaseFragment implements View.OnClickListener {

    private TextView tvName;
    private TextView tvPhone;

    private ArrayList<String> typeList = new ArrayList<>();
    private String houseType = "";

    private SharedPreferences share;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);

        tvName = view.findViewById(R.id.tvName);
        tvPhone = view.findViewById(R.id.tvPhone);

        view.findViewById(R.id.btnReleased).setOnClickListener(this);
        view.findViewById(R.id.btnRent).setOnClickListener(this);
        view.findViewById(R.id.btnStar).setOnClickListener(this);
        view.findViewById(R.id.btnPublish).setOnClickListener(this);
        view.findViewById(R.id.btnManage).setOnClickListener(this);
        view.findViewById(R.id.btnLogout).setOnClickListener(this);

        share = getActivity().getSharedPreferences("UserNow",
                Context.MODE_PRIVATE);
        tvName.setText(share.getString("name", ""));
        tvPhone.setText(share.getString("tel", ""));

        return view;
    }

    private void showPickerViewType() {// 弹出选择器（省市区三级联动）
        typeList.add("二手房");
        typeList.add("新房");
        typeList.add("租房");
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(getContext(), new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                houseType = typeList.get(options1);
                Bundle bundle = new Bundle();
                bundle.putString(KEY_TYPE, houseType);
                Intent publishIntent =  new Intent(getContext(), PublishActivity.class);
                publishIntent.putExtras(bundle);
                startActivity(publishIntent);
            }
        })
                .setTitleText("请选择房源类型")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();
        pvOptions.setPicker(typeList);//一级选择器
        pvOptions.show();
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
            case R.id.btnStar:
                Intent starIntent =  new Intent(getContext(), StarViewActivity.class);
                startActivity(starIntent);
                break;
            case R.id.btnPublish:
                showPickerViewType();
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
