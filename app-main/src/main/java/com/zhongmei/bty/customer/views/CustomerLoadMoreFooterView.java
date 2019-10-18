package com.zhongmei.bty.customer.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeLoadMoreFooterLayout;
import com.zhongmei.yunfu.R;


public class CustomerLoadMoreFooterView extends SwipeLoadMoreFooterLayout {
    private TextView tvLoadMore;
    private ImageView ivProgress;

    private int mFooterHeight;

    private Animation rotateCircle;


    public CustomerLoadMoreFooterView(Context context) {
        this(context, null);
    }

    public CustomerLoadMoreFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomerLoadMoreFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mFooterHeight = getResources().getDimensionPixelOffset(R.dimen.load_more_footer_height);
        rotateCircle = AnimationUtils.loadAnimation(context, R.anim.rotate_circle_progress);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tvLoadMore = (TextView) findViewById(R.id.tvLoadMore);
        ivProgress = (ImageView) findViewById(R.id.ivProgress);
    }

    @Override
    public void onPrepare() {
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            ivProgress.setVisibility(GONE);
            if (-y >= mFooterHeight) {
                tvLoadMore.setText(R.string.customer_loader_more_move_up);
            } else {
                tvLoadMore.setText(R.string.customer_loader_more);
            }
        }
    }

    @Override
    public void onLoadMore() {
        ivProgress.clearAnimation();
        ivProgress.startAnimation(rotateCircle);
        tvLoadMore.setText(R.string.customer_loader_more_loading);
        ivProgress.setVisibility(VISIBLE);
    }

    @Override
    public void onRelease() {

    }

    @Override
    public void onComplete() {
        tvLoadMore.setText(R.string.customer_loader_more_complete);
        ivProgress.clearAnimation();
        ivProgress.setVisibility(GONE);
    }

    @Override
    public void onReset() {
        ivProgress.clearAnimation();
    }
}
