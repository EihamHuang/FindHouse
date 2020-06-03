package com.findhouse.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.transition.Transition;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;

import com.bumptech.glide.request.RequestOptions;

import com.bumptech.glide.request.target.SimpleTarget;
import com.findhouse.activity.R;
import com.findhouse.data.News;

import java.util.List;


/**
 * - @Description:  新闻类数据适配器
 * - @Author:  huangyihang
 * - @Time:  2019-08-15 18:12
 */
public class NewsAdapter extends RecyclerView.Adapter {
    public static final int TYPE_VERTICAL = 0;
    public static final int TYPE_HORIZONAL = 1;
    private Context mContext;
    private List<News> mNewsList;
    private OnItemClickListener mOnItemClickListener;
    protected boolean isScrolling = false;

    public NewsAdapter(List<News> newsList, Context context) {
        mNewsList = newsList;
        mContext = context;
    }

    public static class VerticalViewHolder extends RecyclerView.ViewHolder {
        ImageView newsImg;
        TextView newsTitle;
        TextView newsSrc;
        TextView newsPtime;

        public VerticalViewHolder(View view) {
            super(view);
            newsImg = view.findViewById(R.id.news_img);
            newsTitle = view.findViewById(R.id.news_title);
            newsSrc = view.findViewById(R.id.news_src);
            newsPtime = view.findViewById(R.id.news_ptime);
        }
    }

    public static class HorizonalViewHolder extends RecyclerView.ViewHolder {
        ImageView newsImg;
        TextView newsTitle;
        TextView newsSrc;
        TextView newsPtime;

        public HorizonalViewHolder(View view) {
            super(view);
            newsImg = view.findViewById(R.id.news_img);
            newsTitle = view.findViewById(R.id.news_title);
            newsSrc = view.findViewById(R.id.news_src);
            newsPtime = view.findViewById(R.id.news_ptime);
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
                        .inflate(R.layout.news_item_ver, parent, false));
            case TYPE_HORIZONAL:
                return new HorizonalViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.news_item_hor, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        Log.d("ViewHolder", "onBindViewHolder: " + position);
        final VerticalViewHolder holder = (VerticalViewHolder) viewHolder;
        final News news = mNewsList.get(position);

//        holder.newsImg.setImageResource(R.drawable.ic_launcher_background);
//        holder.newsImg.setTag(news.getimgUrl());
//        holder.newsImg.setTag(R.drawable.ic_launcher_background, position);
//        if(!isScrolling){
//            ImageTask imageTask = new ImageTask(holder.newsImg, mContext);
//            imageTask.execute(news.getimgUrl());
//        }
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.wait)
                .error(R.drawable.error);

        Glide.with(mContext).load(news.getimgUrl()).
                apply(options).
                into(holder.newsImg);

        holder.newsTitle.setText(news.getTitle());
        holder.newsSrc.setText("来源：" + news.getCategory());
        holder.newsPtime.setText("日期：" + news.getDate());

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
        return mNewsList.size();
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
