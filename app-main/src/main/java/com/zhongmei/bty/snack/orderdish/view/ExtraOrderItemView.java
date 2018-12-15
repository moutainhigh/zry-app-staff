package com.zhongmei.bty.snack.orderdish.view;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by demo on 2018/12/15
 */
@EViewGroup(R.layout.extra_order_item)
public class ExtraOrderItemView extends LinearLayout {
    @ViewById(R.id.extra_order_item)
    TextView mTextView;

    public ExtraOrderItemView(Context context) {
        super(context);
    }

    public void bind(String text) {
        mTextView.setText(text);
    }
}
