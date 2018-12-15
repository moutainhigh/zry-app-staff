package com.zhongmei.bty.basemodule.commonbusiness.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zhongmei.yunfu.context.base.BaseApplication;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseListAdapter<Holder, Item> extends BaseAdapter {
    protected List<Item> mItemList;
    protected LayoutInflater mLayoutInflater;
    protected int mItemLayout;
    protected Context mContext;

    public BaseListAdapter(int itemLayout, List<Item> items) {
        mLayoutInflater = LayoutInflater.from(BaseApplication.sInstance);
        if (items == null) {
            mItemList = new ArrayList<Item>();
        } else {
            mItemList = items;
        }
        mItemLayout = itemLayout;
    }

    public BaseListAdapter(Context context, int itemLayout, List<Item> items) {
        mLayoutInflater = LayoutInflater.from(context);
        if (items == null) {
            mItemList = new ArrayList<Item>();
        } else {
            mItemList.addAll(items);
        }
        mItemLayout = itemLayout;
        mContext = context;
    }

    public BaseListAdapter(Context context, int itemLayout) {
        this(context, itemLayout, null);
    }

    public void clear() {
        mItemList.clear();
        notifyDataSetChanged();
    }

    public void addItem(Item item) {
        mItemList.add(item);
        notifyDataSetChanged();
    }

    public void addList(List<Item> items) {
        mItemList.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mItemList.size();
    }

    @Override
    public Item getItem(int position) {
        return mItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    abstract public Holder initViewHodler(View convertView);

    abstract public void setViewHodler(Holder viewHolder, int position);

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder viewHolder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(mItemLayout, null);
            viewHolder = initViewHodler(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (Holder) convertView.getTag();
        }
        setViewHodler(viewHolder, position);
        return convertView;
    }

    abstract class BaseHolder {
        abstract void initByView(View convertView);
    }
}
