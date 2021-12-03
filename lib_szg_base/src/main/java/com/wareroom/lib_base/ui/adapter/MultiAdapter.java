package com.wareroom.lib_base.ui.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class MultiAdapter<T> extends BaseAdapter<T, MultiAdapter.SimpleViewHolder> {

    @NonNull
    @Override
    public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(getItemLayout(viewType), parent, false);
        return new SimpleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleViewHolder holder, int position) {
        T itemData = null;
        if (mData != null && mData.size() > position) {
            itemData = mData.get(position);
        }
        T finalItemData = itemData;
        holder.itemView.setOnClickListener(v -> {
            onItemClick(finalItemData, position, getItemViewType(position, finalItemData));
        });
        convert(holder, position, getItemViewType(position, itemData), itemData);
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        private SparseArray<View> mViews;
        private View mConvertView;
        private Context mContext;


        public SimpleViewHolder(@NonNull View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            mConvertView = itemView;
            mViews = new SparseArray();
        }

        public <T extends View> T getView(int viewId) {
            View view = mViews.get(viewId);
            if (view == null) {
                view = mConvertView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T) view;
        }

        public void setText(int viewId, String text) {
            TextView tv = getView(viewId);
            tv.setText(text);
        }

        public void setTextColor(int viewId, int textColor) {
            TextView view = getView(viewId);
            view.setTextColor(textColor);
        }
    }

    @Override
    public int getItemViewType(int position) {
        T itemData = null;
        if (mData != null && mData.size() > position) {
            itemData = mData.get(position);
        }
        return getItemViewType(position, itemData);
    }

    public abstract int getItemViewType(int position, T itemData);

    public abstract int getItemLayout(int viewType);

    public abstract void convert(SimpleViewHolder viewHolder, int position, int viewType, T itemData);

    public abstract void onItemClick(T data, int position, int viewType);

}
