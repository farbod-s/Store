package com.example.shopping.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shopping.R;
import com.example.shopping.app.AppController;
import com.example.shopping.app.model.Feed;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;


public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {
    private List<Feed> mDataset;

    public FeedAdapter(List<Feed> dataset) {
        this.mDataset = dataset;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_feed, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.with(holder.cover.getContext()).load(mDataset.get(position).imageUrl).into(holder.cover);
        holder.title.setText(mDataset.get(position).title);
        holder.description.setText(mDataset.get(position).description);
        holder.datetime.setText(mDataset.get(position).datetime);
        holder.comments.setText(String.valueOf(mDataset.get(position).comments));
    }

    public void add(int position, Feed item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(Feed item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_feed_image) ImageView cover;
        @Bind(R.id.item_feed_title) TextView title;
        @Bind(R.id.item_feed_description) TextView description;
        @Bind(R.id.item_feed_datetime_content) TextView datetime;
        @Bind(R.id.item_feed_comments) TextView comments;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppController.getInstance().getEventBus().post(mDataset.get(getPosition()));
                }
            });
        }
    }
}