package com.huangyihang.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.huangyihang.activity.R;

import java.util.List;
import android.os.Handler;


/**
 * - @Description:  新闻类数据适配器
 * - @Author:  huangyihang
 * - @Time:  2019-08-15 18:12
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private Context mContext;
    private List<News> mNewsList;
    private OnItemClickListener mOnItemClickListener;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView newsImg;
        TextView newsTitle;
        TextView newsSrc;
        TextView newsPtime;

        public ViewHolder(View view) {
            super(view);
            newsImg = view.findViewById(R.id.news_img);
            newsTitle = view.findViewById(R.id.news_title);
            newsSrc = view.findViewById(R.id.news_src);
            newsPtime = view.findViewById(R.id.news_ptime);
        }
    }
    public NewsAdapter(List<News> newsList, Context context) {
        mNewsList = newsList;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final News news = mNewsList.get(position);

        holder.newsImg.setTag(R.drawable.ic_launcher_background, position);
        Glide.with(mContext).load(news.getImgurl()).error(R.drawable.ic_launcher_foreground)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        if(position == (Integer) holder.newsImg.getTag(R.drawable.ic_launcher_background)) {
                            holder.newsImg.setImageDrawable(resource);
                        }
                    }
                    @Override
                    public void onStart() {
                        super.onStart();
                        holder.newsImg.setImageResource(R.drawable.ic_launcher_background);
                    }
                });

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

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

}
