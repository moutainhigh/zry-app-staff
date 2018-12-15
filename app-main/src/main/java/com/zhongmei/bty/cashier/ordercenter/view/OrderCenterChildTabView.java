package com.zhongmei.bty.cashier.ordercenter.view;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by demo on 2018/12/15
 */
@EViewGroup(R.layout.order_center_child_tab_layout)
public class OrderCenterChildTabView extends RelativeLayout {
    @ViewById(R.id.child_tab_text)
    TextView mTextView;
    @ViewById(R.id.child_tab_text_number)
    TextView mNumTextView;

    public OrderCenterChildTabView(Context context) {
        super(context);
    }

    public void bind(String text) {
        mTextView.setText(text);
    }

    public void setNum(long count) {
        if (count == 0) {
            mNumTextView.setVisibility(GONE);
        } else if (count > 99) {
            mNumTextView.setText("99+");
            mNumTextView.setVisibility(VISIBLE);
        } else {
            mNumTextView.setText(String.valueOf(count));
            mNumTextView.setVisibility(VISIBLE);
        }
    }
}
