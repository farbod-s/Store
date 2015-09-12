package com.example.shopping.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shopping.R;
import com.example.shopping.app.model.Review;
import com.example.shopping.materialdrawer.view.BezelImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewRecyclerAdapter.ViewHolder> {
    private List<Review> mDataset;

    public ReviewRecyclerAdapter() {
        this.mDataset = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_comment, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // TODO - handle avatar image
        holder.username.setText(mDataset.get(position).reviewer_name);
        holder.content.setText(mDataset.get(position).review);
    }

    public void add(int position, Review item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(Review item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.comment_avatar) BezelImageView avatar;
        @Bind(R.id.comment_username) TextView username;
        @Bind(R.id.comment_text) TextView content;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
