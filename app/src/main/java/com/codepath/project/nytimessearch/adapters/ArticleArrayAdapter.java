package com.codepath.project.nytimessearch.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.project.nytimessearch.R;
import com.codepath.project.nytimessearch.models.Article;

import java.util.ArrayList;

/**
 * Created by GANESH on 9/19/17.
 */

//public class ArticleArrayAdapter extends RecyclerView.Adapter<ArticleArrayAdapter.ViewHolder> {
public class ArticleArrayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<Article> mContacts;
    private final int IMAGE = 0, TEXT = 1;

    public ArticleArrayAdapter(Context context, ArrayList<Article> contacts) {
        mContext = context;
        mContacts = contacts;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*View view = LayoutInflater.from(mContext).inflate(R.layout.item_article_results, parent, false);
        return new ViewHolder(view);*/

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case IMAGE:
                View v1 = inflater.inflate(R.layout.item_article_results, parent, false);
                viewHolder = new ViewHolder(v1);
                return viewHolder;
            case TEXT:
                View v2 = inflater.inflate(R.layout.item_articles_results_text, parent, false);
                viewHolder = new ViewHolderText(v2);
                return viewHolder;
            default:
                View v = inflater.inflate(R.layout.item_article_results, parent, false);
                viewHolder = new ViewHolder(v);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        /*Article contact = mContacts.get(position);
        holder.bind(contact);*/

        switch (holder.getItemViewType()) {
            case IMAGE:
                Log.d("debug", "case image");
                ViewHolder vh1 = (ViewHolder) holder;
                configureViewHolder1(vh1, position);
                break;
            case TEXT:
                Log.d("debug", "case text");
                ViewHolderText vh2 = (ViewHolderText) holder;
                configureViewHolder2(vh2, position);
                break;
            default:

                break;
        }
    }

    private void configureViewHolder1(ViewHolder vh1, int position) {
        Article article = (Article) mContacts.get(position);
        if (article != null) {

            //vh1.getLabel2().setText("Hometown: " + user.hometown);
            ImageView imageView = (ImageView) vh1.getIvImage();
            TextView tvTitle = (TextView) vh1.getTvTitle();

            tvTitle.setText(article.getHeadline());
             imageView.setImageResource(0);

            String thumbnail = article.getThumbNail();
            if (!thumbnail.isEmpty()) {
                Glide.with(mContext)
                        .load(thumbnail)
                        .placeholder(R.drawable.ic_nocover)
                        .into(imageView);
            }
        }
    }


    private void configureViewHolder2(ViewHolderText vh2, int position) {
        //vh2.getImageView().setImageResource(R.drawable.sample_golden_gate);
        Article article = (Article) mContacts.get(position);
        if (article != null) {

            //vh1.getLabel2().setText("Hometown: " + user.hometown);
            TextView tvTitle = (TextView) vh2.getTvArticleText();

            tvTitle.setText(article.getSnippet());
        }
    }

    @Override
    public int getItemCount() {
        int size = mContacts.size();
        return mContacts.size();
    }

    //Returns the view type of the item at position for the purposes of view recycling.
    @Override
    public int getItemViewType(int position) {
        if (mContacts.get(position).getThumbNail().isEmpty()) {
            return TEXT;
        } else {
            return IMAGE;
        }
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    class ViewHolder extends RecyclerView.ViewHolder {

        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        private TextView tvName;
        private ImageView ivImage;

        public ImageView getIvImage() {
            return ivImage;
        }

        public TextView getTvTitle() {
            return tvTitle;
        }

        private TextView tvTitle;
        //private Button btnOnline;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {

            super(itemView);

            /*ImageView imageView = itemView.findViewById(R.id.ivImage);
            imageView.setImageResource(0);
            TextView tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);*/

            ivImage = itemView.findViewById(R.id.ivImage);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }

        // Involves populating data into the item through holder
        //public void bind(final Article contact) {
        public void bind(final Article article) {
            TextView tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvTitle.setText(article.getHeadline());

            ImageView imageView = itemView.findViewById(R.id.ivImage);
            imageView.setImageResource(0);

            String thumbnail = article.getThumbNail();
            if (!thumbnail.isEmpty()) {
                Glide.with(mContext)
                        .load(thumbnail)
                        .placeholder(R.drawable.ic_nocover)
                        .into(imageView);
            }
        }
    }

    class ViewHolderText extends RecyclerView.ViewHolder {

        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        private TextView tvArticleText;
        //private Button btnOnline;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolderText(View itemView) {

            super(itemView);

            TextView tvTitle = (TextView) itemView.findViewById(R.id.tvArticleText);
            tvArticleText = (TextView) itemView.findViewById(R.id.tvArticleText);
        }

        // Involves populating data into the item through holder
        //public void bind(final Article contact) {
        public void bind(final Article article) {
            TextView tvTitle = (TextView) itemView.findViewById(R.id.tvArticleText);
            tvTitle.setText(article.getHeadline());
        }

        public TextView getTvArticleText() {
            return tvArticleText;
        }
    }
}

