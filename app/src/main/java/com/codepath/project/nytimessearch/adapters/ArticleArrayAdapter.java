package com.codepath.project.nytimessearch.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.project.nytimessearch.R;
import com.codepath.project.nytimessearch.models.Article;

import java.util.ArrayList;

/**
 * Created by GANESH on 9/19/17.
 */

public class ArticleArrayAdapter extends RecyclerView.Adapter<ArticleArrayAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Article> mContacts;

    public ArticleArrayAdapter(Context context, ArrayList<Article> contacts) {
        mContext = context;
        mContacts = contacts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_article_results, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Article contact = mContacts.get(position);
        holder.bind(contact);
    }

    @Override
    public int getItemCount() {
        int size = mContacts.size();
        return mContacts.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    class ViewHolder extends RecyclerView.ViewHolder {

        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        private TextView tvName;
        private Button btnOnline;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {

            super(itemView);

            ImageView imageView = itemView.findViewById(R.id.ivImage);
            imageView.setImageResource(0);
            TextView tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        }

        // Involves populating data into the item through holder
        //public void bind(final Article contact) {
        public void bind(final Article contact) {
            TextView tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvTitle.setText(contact.getHeadline());

            ImageView imageView = itemView.findViewById(R.id.ivImage);
            imageView.setImageResource(0);

            String thumbnail = contact.getThumbNail();
            if (!thumbnail.isEmpty()) {
                Glide.with(mContext)
                        .load(thumbnail)
                        .placeholder(R.drawable.ic_nocover)
                        .into(imageView);
            }
        }
    }

}

