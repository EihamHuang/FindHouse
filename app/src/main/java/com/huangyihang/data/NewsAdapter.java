package com.huangyihang.data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.huangyihang.activity.R;

import java.util.List;

/**
 * - @Description:  新闻类数据适配器
 * - @Author:  huangyihang
 * - @Time:  2019-08-15 18:12
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private List<News> mNewsList;

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
    public NewsAdapter(List<News> newsList) {
        mNewsList = newsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        News news = mNewsList.get(position);
        if(null == news.getBitmap()){
            holder.newsImg.setImageResource(R.drawable.ic_launcher_background);
        }
        else{
            holder.newsImg.setImageBitmap(news.getBitmap());
        }
        holder.newsTitle.setText("标题：" + news.getTitle());
        holder.newsSrc.setText("来源：" + news.getSrc());
        holder.newsPtime.setText("日期：" + news.getPtime());
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }
}
