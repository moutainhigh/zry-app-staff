package com.zhongmei.bty.dinner.orderdish.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;


public class DishTwoTypeItemView extends LinearLayout implements Checkable {

    private TextView mText;
    private ImageView mCheckedImg;
    private int mLayoutResId = R.layout.listitem_dish_two_type;
    private int bgResId;

    public DishTwoTypeItemView(Context context) {
        super(context);
        initView(context);
    }

    public DishTwoTypeItemView(Context context, int bgResId, int layoutResId) {
        super(context);
        this.bgResId = bgResId;
        this.mLayoutResId = layoutResId;
        initView(context);
    }

    public DishTwoTypeItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public DishTwoTypeItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(mLayoutResId, this, true);
        RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.rl_content);
        layout.setBackgroundResource(getBackgroundResId());
        mText = (TextView) v.findViewById(R.id.tv_name);
        mCheckedImg = (ImageView) v.findViewById(R.id.iv_sel_icon);
    }

    private int getBackgroundResId() {
        return this.bgResId;
    }

    @Override
    public void setChecked(boolean checked) {
        if (checked)
            mCheckedImg.setVisibility(VISIBLE);
        else
            mCheckedImg.setVisibility(GONE);
        mText.setSelected(checked);
    }

    @Override
    public boolean isChecked() {
        return mCheckedImg.getVisibility() == VISIBLE;
    }

    @Override
    public void toggle() {

    }

    public void setText(String txt) {
        mText.setText(txt);
    }
}
