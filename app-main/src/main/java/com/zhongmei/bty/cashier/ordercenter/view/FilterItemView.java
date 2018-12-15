package com.zhongmei.bty.cashier.ordercenter.view;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by demo on 2018/12/15
 */
@EViewGroup(R.layout.order_center_filter_item)
public class FilterItemView extends LinearLayout {
    @ViewById(R.id.filter_item)
    TextView mTextView;

    public FilterItemView(Context context) {
        super(context);
    }

    public void bind(String text) {
        mTextView.setText(text);
    }

    public void setSelect(boolean select) {
        if (select) {
            mTextView.setSelected(true);
        } else {
            mTextView.setSelected(false);
        }
    }
}
