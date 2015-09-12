package com.example.shopping.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.shopping.R;
import com.example.shopping.app.model.Review;
import com.example.shopping.materialdrawer.view.BezelImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ReviewListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Review> mReviews;

    public ReviewListAdapter(Context context, List<Review> reviews) {
        this.mContext = context;
        this.mReviews = reviews;
    }

    @Override
    public int getCount() {
        return mReviews.size();
    }

    @Override
    public Object getItem(int i) {
        return mReviews.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mReviews.get(i).id;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_comment, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // TODO - handle avatar image
        holder.username.setText(mReviews.get(i).reviewer_name);
        holder.content.setText(mReviews.get(i).review);

        return view;
    }

    public static class ViewHolder {
        @Bind(R.id.comment_avatar) BezelImageView avatar;
        @Bind(R.id.comment_username) TextView username;
        @Bind(R.id.comment_text) TextView content;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
