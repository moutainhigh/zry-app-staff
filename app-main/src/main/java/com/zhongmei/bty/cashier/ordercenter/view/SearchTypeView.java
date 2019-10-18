package com.zhongmei.bty.cashier.ordercenter.view;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;


@EViewGroup(R.layout.order_center_search_type_item)
public class SearchTypeView extends LinearLayout {
    @ViewById(R.id.search_type_text)
    TextView mTextView;

    public SearchTypeView(Context context) {
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

    public String getContent() {
        return mTextView.getText().toString();
    }
}
