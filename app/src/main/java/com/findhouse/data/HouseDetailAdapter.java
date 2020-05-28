package com.findhouse.data;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.findhouse.activity.R;

import java.util.List;

public class HouseDetailAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<HouseDetail> mHouseDetailList;

    public HouseDetailAdapter(List<HouseDetail> houseDetailList, Context context) {
        mHouseDetailList = houseDetailList;
        mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView houseTitle;
        TextView housePosition;
        TextView houseTotalPrice;
        TextView houseArea;
        TextView houseApartment;
        TextView houseFix;
        TextView houseOrientation;
        TextView houseFloor;
        TextView houseDes;
        TextView houseInstall;

        public ViewHolder(View view) {
            super(view);
            houseTitle = view.findViewById(R.id.houseTitle);
            houseArea = view.findViewById(R.id.houseArea);
            housePosition = view.findViewById(R.id.housePosition);
            houseTotalPrice = view.findViewById(R.id.houseTotalPrice);
            houseApartment = view.findViewById(R.id.houseApartment);
            houseFix = view.findViewById(R.id.houseFix);
            houseOrientation = view.findViewById(R.id.houseOrientation);
            houseFloor = view.findViewById(R.id.houseFloor);
            houseDes = view.findViewById(R.id.houseDes);
            houseInstall = view.findViewById(R.id.houseInstall);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HouseDetailAdapter.ViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.house_detail_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        Log.d("ViewHolder", "onBindViewHolder: " + position);
        final HouseDetailAdapter.ViewHolder holder = (HouseDetailAdapter.ViewHolder) viewHolder;
        final HouseDetail HouseDetail = mHouseDetailList.get(position);

//        holder.houseTitle.setText(HouseDetail.get);
        holder.houseArea.setText(HouseDetail.getHouseArea());
        Log.d("good", "onBindViewHolder: " + HouseDetail.getHouseArea());
//        holder.housePosition.setText(HouseDetail.getPositionInfo());
//        holder.houseTotalPrice.setText(HouseDetail.getTotalPrice()+"万");
//        holder.houseApartment.setText(HouseDetail.get);
//        holder.houseFix.setText(HouseDetail.getAreaInfo());
//        holder.houseOrientation.setText(HouseDetail.getPositionInfo());
//        holder.houseFloor.setText(HouseDetail.getTotalPrice()+"万");
//        holder.houseInstall.setText(HouseDetail.getTotalPrice()+"万");



    }

    @Override
    public int getItemCount() {
        return mHouseDetailList.size();
    }

}
