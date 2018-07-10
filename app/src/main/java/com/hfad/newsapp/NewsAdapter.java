package com.hfad.newsapp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private ArrayList<News> news;

    NewsAdapter(ArrayList<News> news) {
        this.news = news;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder newsViewHolder, int position) {
        News news = this.news.get(position);

        String url = news.getUrlToImage();
        if (!url.equals("")) {
            Picasso.get().load(url).into(newsViewHolder.image);
        }

        newsViewHolder.title.setText(news.getTitle());
        newsViewHolder.description.setText(news.getDescription());
        newsViewHolder.author.setText(news.getAuthor());
        newsViewHolder.date.setText(news.getDate());


    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        TextView description;
        TextView author;
        TextView date;

        NewsViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.news_image);
            title = itemView.findViewById(R.id.news_title);
            description = itemView.findViewById(R.id.news_description);
            author = itemView.findViewById(R.id.news_author);
            date = itemView.findViewById(R.id.news_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = news.get(getAdapterPosition()).getUrl();
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    v.getContext().startActivity(webIntent);
                }
            });
        }

    }
}