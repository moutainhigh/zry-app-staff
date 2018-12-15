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
@EViewGroup(R.layout.order_center_filter_section)
public class FilterTitleView extends LinearLayout {
    @ViewById(R.id.section_text)
    TextView mTextView;

    public FilterTitleView(Context context) {
        super(context);
    }

    public void bind(String text) {
        mTextView.setText(text);
    }
}
