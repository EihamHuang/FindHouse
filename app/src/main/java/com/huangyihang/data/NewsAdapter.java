package com.huangyihang.data;

import android.graphics.Bitmap;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.huangyihang.activity.R;

import java.util.List;
import android.os.Handler;

import static com.huangyihang.activity.MainActivity.MSG_IMAGE;

/**
 * - @Description:  新闻类数据适配器
 * - @Author:  huangyihang
 * - @Time:  2019-08-15 18:12
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final News news = mNewsList.get(position);

        holder.newsImg.setImageResource(R.drawable.ic_launcher_background);
        holder.newsImg.setTag(news.getImg());

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == MSG_IMAGE) {
                    Bitmap bm = (Bitmap) msg.obj;
                    if (bm != null) {
                        if (TextUtils.equals((String) holder.newsImg.getTag(), news.getImg())) {
                            holder.newsImg.setImageBitmap(bm);
                        }
                    }
                }
            }
        };

        holder.newsTitle.setText("标题：" + news.getTitle());
        holder.newsSrc.setText("来源：" + news.getSrc());
        holder.newsPtime.setText("日期：" + news.getPtime());

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
