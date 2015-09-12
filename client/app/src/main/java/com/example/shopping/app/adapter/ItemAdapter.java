package com.example.shopping.app.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shopping.R;
import com.example.shopping.app.AppController;
import com.example.shopping.app.model.Item;
import com.example.shopping.app.model.OrderVariation;
import com.example.shopping.app.model.ShoppingCartMessage;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ItemAdapter extends BaseAdapter {
    private final static String tag = ItemAdapter.class.getSimpleName();

    private Context mContext;
    private List<Item> mItems;

    public ItemAdapter(Context context, List<Item> items) {
        this.mContext = context;
        this.mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mItems.get(i).id;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_shopping_cart, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Picasso.with(holder.image.getContext()).load(mItems.get(i).featured_src).into(holder.image);
        holder.title.setText(mItems.get(i).name);
        holder.quantity.setText(String.valueOf(mItems.get(i).quantity));
        final StringBuilder options = new StringBuilder();
        for (OrderVariation var : mItems.get(i).variations) {
            if (options.length() > 0) {
                options.append(mContext.getString(R.string.delimiter)); // ","
                options.append(" "); // space
            }
            options.append(var.value);
        }
        holder.option.setText(options.toString());
        holder.price.setText(mItems.get(i).price);

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // edit message
                AppController.getInstance().getEventBus().post(new ShoppingCartMessage(mItems.get(i), "edit"));
            }
        });
        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // remove message
                AppController.getInstance().getEventBus().post(new ShoppingCartMessage(mItems.get(i), "remove"));
            }
        });

        return view;
    }

    public static class ViewHolder {
        @Bind(R.id.cart_item_title) TextView title;
        @Bind(R.id.cart_item_image) ImageView image;
        @Bind(R.id.cart_item_quantity) TextView quantity;
        @Bind(R.id.cart_item_option) TextView option;
        @Bind(R.id.cart_item_price) TextView price;

        @Bind(R.id.cart_item_remove) TextView removeButton;
        @Bind(R.id.cart_item_edit) TextView editButton;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
