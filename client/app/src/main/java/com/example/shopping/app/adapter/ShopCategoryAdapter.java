package com.example.shopping.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shopping.R;
import com.example.shopping.app.AppController;
import com.example.shopping.app.model.Category;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;


public class ShopCategoryAdapter extends RecyclerView.Adapter<ShopCategoryAdapter.ViewHolder> {
    private List<Category> mDataset;

    public ShopCategoryAdapter() {
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
                inflate(R.layout.item_category, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //Picasso.with(holder.cover.getContext()).load(mDataset.get(position).description).into(holder.cover);
        holder.title.setText(mDataset.get(position).name);
    }

    public void add(int position, Category item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(Category item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_category_image) ImageView cover;
        @Bind(R.id.item_category_title) TextView title;

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