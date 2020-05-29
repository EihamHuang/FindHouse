package com.findhouse.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.findhouse.activity.R;
import com.findhouse.data.HouseInfo;
import com.findhouse.utils.SpiltUtil;


import java.util.List;

public class HouseAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<HouseInfo> mHouseList;
    private HouseAdapter.OnItemClickListener mOnItemClickListener;
    protected boolean isScrolling = false;

    private SpiltUtil spiltUtil = new SpiltUtil();
    private int choosePrice = 0;
    private int chooseSell = 0;

    public HouseAdapter(List<HouseInfo> houseList, Context context) {
        mHouseList = houseList;
        mContext = context;
    }

    public static class VerticalViewHolder extends RecyclerView.ViewHolder {
        ImageView houseImg;
        TextView houseTitle;
        TextView houseArea;
        TextView houseType;
        TextView housePrice;

        public VerticalViewHolder(View view) {
            super(view);
            houseImg = view.findViewById(R.id.house_img);
            houseTitle = view.findViewById(R.id.house_title);
            houseArea = view.findViewById(R.id.house_area);
            houseType = view.findViewById(R.id.house_type);
            housePrice = view.findViewById(R.id.house_price);
        }
    }

    public void setScrolling(boolean scrolling) {
        isScrolling = scrolling;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VerticalViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.house_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        Log.d("ViewHolder", "onBindViewHolder: " + position);
        final VerticalViewHolder holder = (VerticalViewHolder) viewHolder;
        final HouseInfo houseInfo = mHouseList.get(position);

        RequestOptions optionsVertical = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.wait)
                .error(R.drawable.error);

        Glide.with(mContext).load(houseInfo.getImg()).
                apply(optionsVertical).
                into(holder.houseImg);

        String type = houseInfo.getType();
        switch (type) {
            case "ershou" :
                chooseSell = 0;
                choosePrice = 0;
                break;
            case "zufang" :
                chooseSell = 1;
                choosePrice = 1;
                break;
            case "xinfang" :
                chooseSell = 2;
                choosePrice = 2;
                break;
        }

        holder.houseTitle.setText(houseInfo.getTitle());
        holder.houseArea.setText(houseInfo.getAreaInfo()+" - "+houseInfo.getPositionInfo());
        holder.houseType.setText(spiltUtil.sellType[chooseSell]);
        holder.housePrice.setText(houseInfo.getPrice()+" "+spiltUtil.priceType[choosePrice]);

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mHouseList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
