package com.zhongmei.bty.base;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.dinner.orderdish.TitleBarFragment;
import com.zhongmei.bty.dinner.orderdish.TitleBarFragment_;
import com.zhongmei.yunfu.ui.base.BasicFragment;

/**
 * 统一左边侧栏菜单
 *
 * @date 2017/2/9 16:00
 */
public abstract class SlidingMainActivity extends MainBaseActivity {

    private View rootView;
    protected View contentLayout;
    private View leftMenu;
    private View contentShadow;
    private ValueAnimator menuAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TitleBarFragment titleBarFragment = new TitleBarFragment_();
        replaceFragment(R.id.main_status_bar, titleBarFragment, titleBarFragment.getClass().getName());
    }

    protected void init() {
        leftMenu = findViewById(R.id.main_left_menu);
        contentShadow = findViewById(R.id.main_shadow);
        contentShadow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLeftMenu(false);
            }
        });
    }

    public void setLeftMenu(BasicFragment fragment) {
        replaceFragment(R.id.main_left_menu, fragment, fragment.getClass().getName());
        //fragment.showFragment(this, R.id.main_left_menu);
    }

    public void switchLeftMenu() {
        setLeftMenu(!contentShadow.isShown());
    }

    public void setLeftMenu(boolean visible) {
        if (menuAnim != null) {
            return;
        }

        if (visible) {
            // 展开左侧菜单时，显示遮罩
            contentShadow.setVisibility(View.VISIBLE);
            final int tx = leftMenu.getLayoutParams().width;
            menuAnim = ValueAnimator.ofInt(0, tx);
            menuAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    leftMenu.setTranslationX(value);
                    contentLayout.setTranslationX(value);
                    if (tx == value) {
                        menuAnim = null;
                    }
                }
            });//DecelerateInterpolator
            menuAnim.setInterpolator(new DecelerateInterpolator());
            menuAnim.setDuration(300);
            menuAnim.start();
        } else {
            // 关闭左侧菜单时，隐藏遮罩
            contentShadow.setVisibility(View.GONE);
            final int tx = leftMenu.getLayoutParams().width;
            menuAnim = ValueAnimator.ofInt(0, tx);
            menuAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    leftMenu.setTranslationX(tx - value);
                    contentLayout.setTranslationX(tx - value);
                    if (value == tx) {
                        menuAnim = null;
                    }
                }
            });//AccelerateInterpolator
            menuAnim.setInterpolator(new AccelerateInterpolator());
            menuAnim.setDuration(300);
            menuAnim.start();
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        View contentView = View.inflate(this, layoutResID, null);
        setContentView(contentView);
    }

    @Override
    public void setContentView(View view) {
        setContentView(view, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        rootView = View.inflate(this, R.layout.activity_sliding_main, null);
        contentLayout = rootView.findViewById(R.id.main_right_content);
        super.setContentView(rootView, params);
        ViewGroup mainLayout = (ViewGroup) rootView.findViewById(R.id.main_content);
        mainLayout.addView(view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        init();
    }

    public View getRootView() {
        return rootView;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (menuAnim != null) {
            menuAnim.cancel();
        }
    }

    class ScrollerFramLayout extends FrameLayout {

        Scroller scroller;

        public ScrollerFramLayout(Context context) {
            super(context);
            scroller = new Scroller(context, new DecelerateInterpolator());
        }

        public void open() {

        }

        public void close() {

        }
    }
}
