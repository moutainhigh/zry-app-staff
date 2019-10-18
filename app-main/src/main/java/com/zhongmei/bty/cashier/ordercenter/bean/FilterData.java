package com.zhongmei.bty.cashier.ordercenter.bean;

import android.util.Pair;

import com.zhongmei.yunfu.util.ValueEnum;

import java.util.ArrayList;
import java.util.List;



public class FilterData {
    private String category;
    private List<Pair<String, ValueEnum>> mChildList = new ArrayList<>();

    public FilterData(String category) {
        this.category = category;
    }

    public void addItem(Pair<String, ValueEnum> pair) {
        mChildList.add(pair);
    }

    public String getCategory() {
        return category;
    }

    public List<Pair<String, ValueEnum>> getChildList() {
        return mChildList;
    }

    public int getItemCount() {
        return mChildList.size() + 1;
    }

    public Object getItem(int position) {
        if (position == 0) {
            return category;
        } else {
            return mChildList.get(position - 1);
        }
    }

}
