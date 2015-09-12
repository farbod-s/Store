package com.example.shopping.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shopping.R;
import com.example.shopping.app.AppController;
import com.example.shopping.app.model.Collection;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;


public class ShopCollectionAdapter extends RecyclerView.Adapter<ShopCollectionAdapter.ViewHolder> {
    private List<Collection> mDataset;

    public ShopCollectionAdapter(List<Collection> dataset) {
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
                inflate(R.layout.item_collection, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.with(holder.cover.getContext()).load(mDataset.get(position).imageCover).into(holder.cover);
        Picasso.with(holder.image1.getContext()).load(mDataset.get(position).imageMore1).into(holder.image1);
        Picasso.with(holder.image2.getContext()).load(mDataset.get(position).imageMore2).into(holder.image2);
        Picasso.with(holder.image3.getContext()).load(mDataset.get(position).imageMore3).into(holder.image3);
        Picasso.with(holder.image4.getContext()).load(mDataset.get(position).imageMore4).into(holder.image4);
        holder.title.setText(mDataset.get(position).title);
    }

    public void add(int position, Collection item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(Collection item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_collection_image) ImageView cover;
        @Bind(R.id.item_collection_image_more_1) ImageView image1;
        @Bind(R.id.item_collection_image_more_2) ImageView image2;
        @Bind(R.id.item_collection_image_more_3) ImageView image3;
        @Bind(R.id.item_collection_image_more_4) ImageView image4;
        @Bind(R.id.item_collection_title) TextView title;

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