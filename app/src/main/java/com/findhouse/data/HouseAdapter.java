package com.findhouse.data;

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


import java.util.List;

public class HouseAdapter extends RecyclerView.Adapter {
    public static final int TYPE_VERTICAL = 0;
    public static final int TYPE_HORIZONAL = 1;
    private Context mContext;
    private List<HouseInfo> mHouseList;
    private HouseAdapter.OnItemClickListener mOnItemClickListener;
    protected boolean isScrolling = false;

    private String[] priceType = {"万", "元/月"};
    private String[] sellType = {"二手", "租房", "新房"};
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
                return new VerticalViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.house_item, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        Log.d("ViewHolder", "onBindViewHolder: " + position);
        final VerticalViewHolder holder = (VerticalViewHolder) viewHolder;
        final HouseInfo houseInfo = mHouseList.get(position);

//        holder.newsImg.setImageResource(R.drawable.ic_launcher_background);
//        holder.newsImg.setTag(news.getImgurl());
//        holder.newsImg.setTag(R.drawable.ic_launcher_background, position);
//        if(!isScrolling){
//            ImageTask imageTask = new ImageTask(holder.newsImg, mContext);
//            imageTask.execute(news.getImgurl());
//        }
        RequestOptions optionsVertical = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground);

        Glide.with(mContext).load(houseInfo.getImg()).
                apply(optionsVertical).
                into(holder.houseImg);

        String type = houseInfo.getType();
        switch (type) {
            case "zufang" :
                choosePrice = 1;
                chooseSell = 1;
                break;
            case "xinfang" :
                chooseSell = 2;
        }

        holder.houseTitle.setText(houseInfo.getTitle());
        holder.houseArea.setText(houseInfo.getAreaInfo()+" - "+houseInfo.getPositionInfo());
        holder.houseType.setText(sellType[chooseSell]);
        holder.houseTotalPrice.setText(houseInfo.getTotalPrice()+priceType[choosePrice]);

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

//    @Override
//    public int getItemViewType(int position) {
//        if(position %2 == 0){
//            return TYPE_VERTICAL;
//        } else {
//            return TYPE_HORIZONAL;
//        }
//    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
