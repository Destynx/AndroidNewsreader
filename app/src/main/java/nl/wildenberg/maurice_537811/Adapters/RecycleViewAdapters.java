package nl.wildenberg.maurice_537811.Adapters;

import android.content.Context;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nl.wildenberg.maurice_537811.R;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;


import nl.wildenberg.maurice_537811.Models.Article;


public class RecycleViewAdapters extends RecyclerView.Adapter<RecycleViewAdapters.ArticleViewHolder> {

    private List<Article> mItems;
    private Context mContext;
    public ArticleListener mListener;

    public interface ArticleListener
    {
        void onItemClick(View view, Article content);
    }

    public RecycleViewAdapters(Context context, List<Article> items, ArticleListener listener)
    {
        mContext = context;
        mItems = items;
        mListener = listener;

    }
    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new ArticleViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.overview, parent, false));
    }

    public void AddArticles (List<Article> articles) {
        mItems.addAll(articles);
    }

    @Override
    public void onBindViewHolder(final ArticleViewHolder holder, int position)
    {
        //get the item for the current position
        final Article node = (Article) getItem(position);

        //fill the views in the viewholder with the content for the current position
        holder.title.setText(node.Title);
        Glide.with(mContext).load(node.Image).centerCrop().crossFade().into(holder.img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(holder.itemView, node);
            }
        });
        if(node.IsLiked == true) {

            holder.title.setTextColor(Color.parseColor("#f4f142"));
        }
        else{
            holder.title.setTextColor(Color.parseColor("#000000"));

        }
    }

    public int getItemCount() {
        return mItems.size();
    }


    public Object getItem(int position) {
        return mItems.get(position);
    }

    static class ArticleViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView title;
        TextView is_liked;

        ArticleViewHolder(View itemView)
        {
            super(itemView);
            img = itemView.findViewById(R.id.relative_views_image);
            title = itemView.findViewById(R.id.relative_views_title);
            is_liked = itemView.findViewById(R.id.is_liked);
        }
    }
}
