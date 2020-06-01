package com.findhouse.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bigkoo.pickerview.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.findhouse.activity.HouseActivity;
import com.findhouse.activity.NewHouseActivity;
import com.findhouse.activity.R;
import com.findhouse.adapter.HouseAdapter;
import com.findhouse.data.HouseInfo;
import com.findhouse.data.JsonCity;
import com.findhouse.data.JsonData;
import com.findhouse.utils.GetJsonDataUtil;
import com.findhouse.utils.SpacesItemDecoration;
import com.findhouse.network.NetworkClient;
import com.findhouse.utils.StringUtil;
import com.findhouse.utils.UrlUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


public class MainFragment extends BaseFragment implements View.OnClickListener {
    public static final String KEY_HOUSE = "key_house";
    public static final String KEY_HOUSE_DETAIL = "key_house_detail";

    private List<HouseInfo> houseList = new ArrayList<>();
    private List<HouseInfo> houseListNew = new ArrayList<>();
    private HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
    private boolean hasResult = false;

    private ArrayList<JsonCity> options1Items = new ArrayList<>(); //省
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();//市
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();//区

    private ArrayList<String> rentPriceList = new ArrayList<>();
    private ArrayList<String> ershouPriceList = new ArrayList<>();
    private ArrayList<String> xinfangPriceList = new ArrayList<>();
    private ArrayList<String> sortList = new ArrayList<>();
    private ArrayList<String> typeList = new ArrayList<>();

    private String type = "/house";
    private String route = "/list";

    private String houseType = "";
    private String city = "";
    private String region = "";
    private String price = "";
    private String sort = "";
    private StringUtil stringUtil = new StringUtil();

    private Button btnCity;
    private Button btnPrice;
    private Button btnType;
    private Button btnSort;
    private HouseAdapter houseAdapter;
    private RefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    private int page = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_main, container, false);

        btnCity = view.findViewById(R.id.pickCity);
        btnPrice = view.findViewById(R.id.pickPrice);
        btnType = view.findViewById(R.id.pickType);
        btnSort = view.findViewById(R.id.pickSort);

        btnCity.setOnClickListener(this);
        btnPrice.setOnClickListener(this);
        btnType.setOnClickListener(this);
        btnSort.setOnClickListener(this);

        recyclerView = view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        stringIntegerHashMap.put(SpacesItemDecoration.TOP_DECORATION,25);//顶部间距
        stringIntegerHashMap.put(SpacesItemDecoration.BOTTOM_DECORATION,25);//底部间距
        recyclerView.addItemDecoration(new SpacesItemDecoration(stringIntegerHashMap));

        houseAdapter = new HouseAdapter(houseList, getContext());
        recyclerView.setAdapter(houseAdapter);

        initJsonData();
        initOtherData();
        initData(houseType, city, region, price, sort);

        refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setEnableAutoLoadMore(true);
        refreshLayout.setEnableLoadMore(true);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        houseList.clear();

                        initData(houseType, city, region, price, sort);
                        refreshLayout.finishRefresh();
                        refreshLayout.resetNoMoreData();//setNoMoreData(false);
                    }
                }, 500);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initData(houseType, city, region, price, sort);
                        refreshLayout.finishLoadMore();
                    }
                }, 500);
            }
        });
        return view;
    }

    private void initData(String houseType,String city,String region,String price,String sort) {
        UrlUtil baseUrlUtil = new UrlUtil();
        baseUrlUtil.setType(type);
        baseUrlUtil.setRoute(route);
        String url = baseUrlUtil.toString();

        if(houseType!="" || city!="" || region!="" || price!="" || sort!="") {
            baseUrlUtil.setRoute("/select");
            url = baseUrlUtil.toString()+"?type="+houseType+"&city="+city
                    +"&region="+region+"&price="+price+"&sort="+sort;
        }

        NetworkClient.getRequest(url, new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "网络请求错误，请重试～", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseJsonData = response.body().string();
                // 解析json
                hasResult = parseJSON(responseJsonData);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(hasResult){
                            page++;
                            houseAdapter.notifyDataSetChanged();
                            setListener(houseList);
                        }
                        //  失败
                        else{
                            Toast.makeText(getContext(), "暂时无内容～", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });
    }

    private void setListener(final List<HouseInfo> houseList) {

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Glide.with(getContext()).resumeRequests();
                }
                else {
                    Glide.with(getContext()).pauseRequests();
                }
            }
        });

        houseAdapter.setOnItemClickListener(new HouseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                HouseInfo houseInfo = houseList.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable(KEY_HOUSE, houseInfo);
                if(houseInfo.getType().equals("新房")) {
                    Intent intent = new Intent(getContext(), NewHouseActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(getContext(), HouseActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }

        });

    }

    private boolean parseJSON(String jsonData) {
        Gson gson = new Gson();
        JsonData<HouseInfo> parseResult = gson.fromJson(jsonData, new TypeToken<JsonData<HouseInfo>>(){}.getType());
        if(null == parseResult.getData()) {
            return false;
        }else{
            houseListNew = parseResult.getData();
            houseListNew.removeAll(houseList);
            houseList.addAll(houseListNew);
            return true;
        }
    }

    private void showPickerViewType() {// 弹出选择器（省市区三级联动）
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(getContext(), new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                btnType.setText(typeList.get(options1));
                houseType = typeList.get(options1);
                refreshLayout.autoRefresh();
            }
        })
                .setTitleText("类型选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();
        pvOptions.setPicker(typeList);//一级选择器
        pvOptions.show();
    }

    private void showPickerViewCity() {// 弹出选择器（省市区三级联动）
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(getContext(), new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                btnCity.setText(options2Items.get(options1).get(options2) + " "
                        + options3Items.get(options1).get(options2).get(options3));
                city = options2Items.get(options1).get(options2);
                region = options3Items.get(options1).get(options2).get(options3);
                refreshLayout.autoRefresh();
            }
        })
                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    private void showPickerViewPrice(final ArrayList<String> priceList) {// 弹出选择器（省市区三级联动）
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(getContext(), new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                btnPrice.setText(priceList.get(options1));
                price = priceList.get(options1);
                refreshLayout.autoRefresh();
            }
        })
                .setTitleText("价格选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();
        pvOptions.setPicker(priceList);//一级选择器
        pvOptions.show();
    }

    private void showPickerViewSort(final ArrayList<String> sortList) {// 弹出选择器（省市区三级联动）
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(getContext(), new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                btnSort.setText(sortList.get(options1));
                sort = sortList.get(options1);
                refreshLayout.autoRefresh();
            }
        })
                .setTitleText("排序选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();
        pvOptions.setPicker(sortList);//一级选择器
        pvOptions.show();
    }

    private void initJsonData() {//解析数据 （省市区三级联动）
        String JsonData = new GetJsonDataUtil().getJson(getContext(), "province.json");//获取assets目录下的json文件数据

        ArrayList<JsonCity> jsonCity = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonCity;

        for (int i = 0; i < jsonCity.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三级）

            for (int c = 0; c < jsonCity.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonCity.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市
                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonCity.get(i).getCityList().get(c).getArea() == null
                        || jsonCity.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {
                    City_AreaList.addAll(jsonCity.get(i).getCityList().get(c).getArea());
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }
    }

    private void initOtherData() {
        typeList.add("二手房");
        typeList.add("新房");
        typeList.add("租房");

        rentPriceList.add("不限");
        rentPriceList.add("1500元以下");
        rentPriceList.add("1500-2000元");
        rentPriceList.add("2000-3000元");
        rentPriceList.add("3000-4000元");
        rentPriceList.add("4000元以上");

        ershouPriceList.add("不限");
        ershouPriceList.add("100万以下");
        ershouPriceList.add("100-200万");
        ershouPriceList.add("200-300万");
        ershouPriceList.add("300-400万");
        ershouPriceList.add("400万以上");

        xinfangPriceList.add("不限");
        xinfangPriceList.add("500万以下");
        xinfangPriceList.add("500-750万");
        xinfangPriceList.add("750-1000万");
        xinfangPriceList.add("1000-1500万");
        xinfangPriceList.add("1500万以上");

        sortList.add("默认排序");
        sortList.add("价格升序");
        sortList.add("价格降序");
    }

    private ArrayList<JsonCity> parseData(String result) {//Gson 解析
        ArrayList<JsonCity> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonCity entity = gson.fromJson(data.optJSONObject(i).toString(), JsonCity.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pickType :
                showPickerViewType();
                break;
            case R.id.pickCity :
                if(houseType == "") {
                    Toast.makeText(getContext(), "请先选择房屋类型", Toast.LENGTH_SHORT).show();
                } else {
                    showPickerViewCity();
                }
                break;
            case R.id.pickPrice :
                if(houseType == "") {
                    Toast.makeText(getContext(), "请先选择房屋类型", Toast.LENGTH_SHORT).show();
                } else if(houseType == "租房") {
                    showPickerViewPrice(rentPriceList);
                } else if(houseType == "新房") {
                    showPickerViewPrice(xinfangPriceList);
                } else {
                    showPickerViewPrice(ershouPriceList);
                }
                break;
            case R.id.pickSort :
                if(houseType == "") {
                    Toast.makeText(getContext(), "请先选择房屋类型", Toast.LENGTH_SHORT).show();
                } else {
                    showPickerViewSort(sortList);
                }
                break;
        }
    }
}
