package com.zhongmei.bty.snack.orderdish.buinessview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * 空态页
 */
@EViewGroup(R.layout.layout_custom_empty)
public class CustomEmptyView extends FrameLayout {
    @ViewById(R.id.tv_content)
    protected TextView tvContent;

    public CustomEmptyView(Context context) {
        super(context);
    }

    public CustomEmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @AfterViews
    protected void init() {

    }
}
