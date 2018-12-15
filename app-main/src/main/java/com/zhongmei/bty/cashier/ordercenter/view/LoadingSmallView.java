package com.zhongmei.bty.cashier.ordercenter.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhongmei.yunfu.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by demo on 2018/12/15
 */
@EViewGroup(R.layout.loading_small_view)
public class LoadingSmallView extends LinearLayout {
    @ViewById(R.id.loadingImage)
    public ImageView loadingImage;

    public LoadingSmallView(Context context) {
        super(context);
    }

    public LoadingSmallView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadingSmallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @AfterViews
    void initView() {
        startAnimation();
    }

    public void startAnimation() {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_round_rotate);
        LinearInterpolator interpolator = new LinearInterpolator();
        animation.setInterpolator(interpolator);
        loadingImage.startAnimation(animation);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimation();
    }

    public void stopAnimation() {
        if (loadingImage != null) {
            loadingImage.clearAnimation();
        }
    }
}
