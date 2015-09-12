package com.example.shopping.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.shopping.R;
import com.example.shopping.app.AppController;
import com.example.shopping.app.model.Shipping;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ShippingClassAdapter extends BaseAdapter {
    private Context mContext;
    private List<Shipping> mDataSet;

    private int mLastSelectedPosition = 0;

    public ShippingClassAdapter(Context context, List<Shipping> dataset) {
        this.mContext = context;
        this.mDataSet = dataset;
    }

    @Override
    public int getCount() {
        return mDataSet.size();
    }

    @Override
    public Object getItem(int i) {
        return mDataSet.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mDataSet.get(i).id;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_class, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // TODO - handle holder
        holder.classText.setText(mDataSet.get(i).method_title);
        holder.classButton.setText(String.valueOf(mDataSet.get(i).total));
        holder.classButton.setChecked(i == mLastSelectedPosition);
        holder.classButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLastSelectedPosition = i;
                notifyDataSetChanged();
            }
        });

        /*if (i == mLastSelectedPosition) {
            AppController.getInstance().getShoppingCart().mOrder.shipping_lines.add(mDataSet.get(i));
        }*/

        return view;
    }

    public static class ViewHolder {
        @Bind(R.id.item_class_layout) View classView;
        @Bind(R.id.item_class_button) RadioButton classButton;
        @Bind(R.id.item_class_text) TextView classText;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);

            classView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    classButton.performClick();
                }
            });
        }
    }
}
