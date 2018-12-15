package com.zhongmei.bty.mobilepay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.bty.mobilepay.bean.PayMethodItem;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.bty.mobilepay.views.indicator.Indicator;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class PayMethodIndicatorAdapter extends Indicator.Adapter {

    private Context context;
    private List<PayMethodItem> dataList;
    private int columns = 6;//默认列数

    public PayMethodIndicatorAdapter(Context context) {
        this.context = context;
        this.dataList = new ArrayList<>();
    }

    public void addData(Collection<PayMethodItem> data) {
        notNull(data, "Data is null");
        this.dataList.addAll(data);
    }

    public void setData(Collection<PayMethodItem> data) {
        notNull(data, "Data is null");
        this.dataList.clear();
        this.dataList.addAll(data);
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    @Override
    public View getPrevActionView(Indicator indicator, int orientation) {
        ImageButton imageButton = new ImageButton(context);
        imageButton.setImageDrawable(context.getResources().getDrawable(R.drawable.selector_pay_method_action_left));
        imageButton.setBackground(context.getResources().getDrawable(R.drawable.selector_pay_method_action));

        ViewGroup.LayoutParams layoutParams;
        if (orientation == LinearLayout.HORIZONTAL) {
            layoutParams = new ViewGroup.LayoutParams(generateActionSize(), ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, generateActionSize());
        }
        imageButton.setLayoutParams(layoutParams);
        generateActionView(imageButton);
        return imageButton;
    }

    @Override
    public View getNextActionView(Indicator indicator, int orientation) {
        ImageButton imageButton = new ImageButton(context);
        imageButton.setImageDrawable(context.getResources().getDrawable(R.drawable.selector_pay_method_action_right));
        imageButton.setBackground(context.getResources().getDrawable(R.drawable.selector_pay_method_action));

        ViewGroup.LayoutParams layoutParams;
        if (orientation == LinearLayout.HORIZONTAL) {
            layoutParams = new ViewGroup.LayoutParams(generateActionSize(), ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, generateActionSize());
        }
        imageButton.setLayoutParams(layoutParams);
        generateActionView(imageButton);
        return imageButton;
    }

    private void generateActionView(ImageButton imageButton) {
        if (getCount() < columns) {
            imageButton.setVisibility(View.INVISIBLE);
        }
    }

    private int generateActionSize() {
        return DensityUtil.dip2px(context, 30);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        int realSize = dataList.size();
        int newPosition = position % realSize;
        return dataList.get(newPosition);
    }

    @Override
    public View getView(Indicator indicator, int position) {
//        目前Indicator并没有对最小单元的子视图加入回收功能，所以这里可以不采用ViewHolder模式。

        View view = LayoutInflater.from(context).inflate(R.layout.pay_method_item, null, false);

        ImageView methodIconImg = (ImageView) view.findViewById(R.id.pay_method_item_img);
        TextView methodNameTxv = (TextView) view.findViewById(R.id.pay_method_item_txv);

        PayMethodItem item = (PayMethodItem) getItem(position);

        boolean isSelected = item.isSelected;

        view.setSelected(isSelected);
        methodIconImg.setSelected(isSelected);
        methodNameTxv.setSelected(item.isSelected);
        methodNameTxv.setText(item.methodName);
        methodIconImg.setImageResource(item.methodResId);
        //如果禁用，不允许点击  add 20170411 yutang
        if (item.enabled == false) {
            methodIconImg.setEnabled(false);
            methodNameTxv.setEnabled(false);
            view.setEnabled(false);
        }
        return view;
    }

    private static void notNull(Object object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
    }
}
