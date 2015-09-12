package com.example.shopping.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shopping.R;
import com.example.shopping.app.AppController;
import com.example.shopping.app.model.OrderTemp;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<OrderTemp> mDataset;

    public OrderAdapter() {
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
                inflate(R.layout.item_order, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.orderId.setText(String.valueOf(mDataset.get(position).order_number));
        holder.count.setText(mDataset.get(position).total_line_items_quantity);
        holder.status.setText(mDataset.get(position).status);
        holder.date.setText(mDataset.get(position).created_at);
        holder.total.setText(mDataset.get(position).total);
    }

    public void add(int position, OrderTemp item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(OrderTemp item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_order_header_view) View showOrderView;
        @Bind(R.id.item_order_id_text) TextView orderId;
        @Bind(R.id.item_order_view_link) TextView showOrderLink;
        @Bind(R.id.item_order_count) TextView count;
        @Bind(R.id.item_order_status) TextView status;
        @Bind(R.id.item_order_date) TextView date;
        @Bind(R.id.item_order_total_price) TextView total;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            showOrderView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showOrderLink.performClick();
                }
            });

            showOrderLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppController.getInstance().getEventBus().post(mDataset.get(getPosition()));
                }
            });
        }
    }
}
