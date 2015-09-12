package com.example.shopping.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.shopping.R;
import com.example.shopping.app.AppController;
import com.example.shopping.app.model.ShippingAddress;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ShippingAddressAdapter extends RecyclerView.Adapter<ShippingAddressAdapter.ViewHolder> {
    private List<ShippingAddress> mDataset;

    private int mLastSelectedPosition = 0;

    public ShippingAddressAdapter() {
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
                inflate(R.layout.item_shipping_address, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ShippingAddress shipAdd = mDataset.get(position);
        holder.fullName.setText(shipAdd.first_name + " " + shipAdd.last_name);
        holder.state.setText(shipAdd.state);
        holder.city.setText(shipAdd.city);
        holder.address.setText(shipAdd.address_1 + " " + shipAdd.address_2);
        holder.postalCode.setText(shipAdd.postcode);
        holder.emergencyPhone.setText(""); // TODO
        holder.phone.setText(""); // TODO

        holder.checkButton.setChecked(position == mLastSelectedPosition);
        holder.checkButton.setTag(position);

        if (position == mLastSelectedPosition) {
            AppController.getInstance().getShoppingCart().mOrder.shipping_address = shipAdd;
        }
    }

    public void add(int position, ShippingAddress item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(ShippingAddress item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.order_full_name) TextView fullName;
        @Bind(R.id.province) TextView state;
        @Bind(R.id.city) TextView city;
        @Bind(R.id.address) TextView address;
        @Bind(R.id.postalCode) TextView postalCode;
        @Bind(R.id.emergencyPhone) TextView emergencyPhone;
        @Bind(R.id.phone) TextView phone;
        @Bind(R.id.address_delete) TextView removeButton;
        @Bind(R.id.shipping_address_item_check_layout) View checkView;
        @Bind(R.id.chk_btn) RadioButton checkButton;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = getPosition();
                    AppController.getInstance().getEventBus().post(mDataset.get(position));
                    remove(mDataset.get(position));
                    if (mLastSelectedPosition == position) { // select others
                        mLastSelectedPosition = Math.max(0, position - 1);
                        notifyDataSetChanged();
                    }
                }
            });

            checkView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkButton.performClick();
                }
            });

            checkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mLastSelectedPosition = getPosition(); // (Integer) view.getTag();
                    notifyDataSetChanged();
                }
            });
        }
    }
}