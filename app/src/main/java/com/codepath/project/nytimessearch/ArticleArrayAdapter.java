package com.codepath.project.nytimessearch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by GANESH on 9/19/17.
 */

public class ArticleArrayAdapter extends RecyclerView.Adapter<ArticleArrayAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Article> mContacts;
    //private ArrayList<Contact> mContacts;

    public ArticleArrayAdapter(Context context, ArrayList<Article> contacts) {
        mContext = context;
        mContacts = contacts;
        //super(context, android.R.layout.simple_list_item_1, contacts);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("debug", "onCreateViewHolder");
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_article_results, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d("debug", "onBindViewHolder");
        Article contact = mContacts.get(position);
        //Contact contact = mContacts.get(position);
        holder.bind(contact);
    }

    @Override
    public int getItemCount() {
        int size = mContacts.size();
        Log.d("debug", Integer.toString(size));
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

            Log.d("debug", "ViewHolder");

            ImageView imageView = itemView.findViewById(R.id.ivImage);
            imageView.setImageResource(0);
            TextView tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvTitle.setText("test");

        }

        // Involves populating data into the item through holder
        //public void bind(final Article contact) {
        public void bind(final Article contact) {
            Log.d("debug", "bind");
            //tvName.setText(contact.getHeadline());
            TextView tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvTitle.setText(contact.getHeadline());

            ImageView imageView = itemView.findViewById(R.id.ivImage);
            imageView.setImageResource(0);


            String thumbnail = contact.getThumbNail();
            if (!thumbnail.isEmpty()) {
                Picasso.with(mContext).load(thumbnail).into(imageView);
            }


           /* btnOnline.setText("hai");
            btnOnline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "hello", Toast.LENGTH_SHORT).show();
                }
            });*/
        }
    }

}


/*public class ArticleArrayAdapter extends ArrayAdapter<Article> {

    public ArticleArrayAdapter(Context context, List<Article> articles) {
        super(context, android.R.layout.simple_list_item_1, articles);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Article article = this.getItem(position);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_article_results, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.ivImage);
        imageView.setImageResource(0);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(article.getHeadline());

        String thumbnail = article.getThumbNail();

        if (!TextUtils.isEmpty(thumbnail)) {
            Picasso.with(getContext()).load(thumbnail).into(imageView);

        }
        return convertView;

    }
}*/

