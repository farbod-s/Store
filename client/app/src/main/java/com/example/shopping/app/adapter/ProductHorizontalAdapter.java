package com.example.shopping.app.adapter;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shopping.R;
import com.example.shopping.app.AppController;
import com.example.shopping.app.model.ProductTemp;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;


public class ProductHorizontalAdapter extends RecyclerView.Adapter<ProductHorizontalAdapter.ViewHolder> {
    private List<ProductTemp> mDataset;

    public ProductHorizontalAdapter() {
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
                inflate(R.layout.item_product_horizontal, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.with(holder.cover.getContext()).load(mDataset.get(position).featured_src).into(holder.cover);
        holder.title.setText(mDataset.get(position).title);
        holder.price.setText(String.valueOf(mDataset.get(position).regular_price));
        holder.discount.setText(String.valueOf(mDataset.get(position).sale_price));

        if (mDataset.get(position).on_sale) {
            holder.discount.setVisibility(View.VISIBLE);
            holder.price.setPaintFlags(holder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.discount.setVisibility(View.GONE);
            holder.price.setPaintFlags(holder.price.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    public void add(int position, ProductTemp item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(ProductTemp item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_product_image) ImageView cover;
        @Bind(R.id.item_product_title) TextView title;
        @Bind(R.id.item_product_original_price) TextView price;
        @Bind(R.id.item_product_special_price) TextView discount;

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