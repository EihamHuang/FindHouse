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

import com.findhouse.activity.R;

import java.util.List;

public class InstallAdapter extends RecyclerView.Adapter {
    public static final int TYPE_VERTICAL = 0;
    public static final int TYPE_HORIZONAL = 1;
    private Context mContext;
    private List<String> mHouseInstallList;
    private InstallAdapter.OnItemClickListener mOnItemClickListener;
    protected boolean isScrolling = false;

    private String[] installType = {"洗衣机", "空调", "衣柜","电视", "冰箱", "热水器","床", "暖气", "宽带", "天然气"};

    public InstallAdapter(List<String> houseInstall, Context context) {
        mHouseInstallList = houseInstall;
        mContext = context;
    }

    public static class VerticalViewHolder extends RecyclerView.ViewHolder {
        ImageView houseImg;
        TextView houseTitle;
        TextView houseArea;
        TextView houseType;
        TextView houseTotalPrice;

        public VerticalViewHolder(View view) {
            super(view);
            houseImg = view.findViewById(R.id.house_img);
            houseTitle = view.findViewById(R.id.house_title);
            houseArea = view.findViewById(R.id.house_area);
            houseType = view.findViewById(R.id.house_type);
            houseTotalPrice = view.findViewById(R.id.house_totalPrice);
        }
    }


    public void setScrolling(boolean scrolling) {
        isScrolling = scrolling;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_VERTICAL:
                return new InstallAdapter.VerticalViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.house_item, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        Log.d("ViewHolder", "onBindViewHolder: " + position);
        final InstallAdapter.VerticalViewHolder holder = (InstallAdapter.VerticalViewHolder) viewHolder;




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
        return mHouseInstallList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void setOnItemClickListener(InstallAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
