package com.zhongmei.bty.common.view;

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
@EViewGroup(R.layout.loading_view)
public class LoadingView extends LinearLayout {

    @ViewById(R.id.loadingImage)
    public ImageView loadingImage;

    private Context context;

    public LoadingView(Context context) {
        super(context);
        this.context = context;
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @AfterViews
    protected void initViews() {
//        startAnimation();
    }

    /**
     * 启动动画
     */
    public void startAnimation() {
        Animation circle_anim = AnimationUtils.loadAnimation(context, R.anim.anim_round_rotate);
        LinearInterpolator interpolator = new LinearInterpolator();  //设置匀速旋转，在xml文件中设置会出现卡顿
        circle_anim.setInterpolator(interpolator);
        if (circle_anim != null) {
            loadingImage.startAnimation(circle_anim);  //开始动画
        }
    }


    /**
     * 移除动画
     */
    public void stopAnimation() {
        if (loadingImage != null) {
            loadingImage.clearAnimation();
        }
    }

}
