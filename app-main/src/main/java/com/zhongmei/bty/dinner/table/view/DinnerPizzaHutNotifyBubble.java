package com.zhongmei.bty.dinner.table.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.bty.basemodule.notifycenter.enums.NotificationType;
import com.zhongmei.yunfu.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import de.greenrobot.event.EventBus;


@EViewGroup(R.layout.view_dinner_notify_bubble)
public class DinnerPizzaHutNotifyBubble extends LinearLayout {

    @ViewById(R.id.iv_icon)
    ImageView ivIcon;

    @ViewById(R.id.tv_message)
    TextView tvMessage;

    private boolean isForeground = false;

    public DinnerPizzaHutNotifyBubble(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public DinnerPizzaHutNotifyBubble(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DinnerPizzaHutNotifyBubble(Context context) {
        super(context);
        init();
    }

    private void init() {
        setBackgroundResource(R.drawable.dinner_notify_bubble_bg);
        setOrientation(LinearLayout.HORIZONTAL);
        EventBus.getDefault().register(this);
    }

    public void resume() {
        isForeground = true;
    }

    public void pause() {
        isForeground = false;
    }



    public void show(String text, NotificationType type) {
                if ((getAnimation() == null || getAnimation().hasEnded()) && getVisibility() != View.VISIBLE) {
            ivIcon.setImageResource(R.drawable.notice_icon);
            tvMessage.setText(text);
            comeIn();
                    } else {
            tvMessage.setText(text);
        }
    }

    private void comeIn() {
        setVisibility(View.VISIBLE);
                setAlpha(1.0f);
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "translationX", getWidth(), 0f).setDuration(200);
        animator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator arg0) {
                setVisibility(View.VISIBLE);
            }
        });
        animator.start();
    }

    @Click({R.id.iv_close})
    void click(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                                if (getAnimation() != null && getAnimation().isInitialized() && !getAnimation().hasEnded()) {
                    getAnimation().cancel();
                }
                setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
    }

    public void destroy() {
        EventBus.getDefault().unregister(this);
    }

}
