package com.zhongmei.bty.queue.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.bty.basemodule.database.enums.EntranceType;
import com.zhongmei.bty.basemodule.notifycenter.enums.NotificationType;
import com.zhongmei.bty.queue.event.EventIsCanClick;
import com.zhongmei.yunfu.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 排队页面显示空闲桌台的气泡
 */
@EViewGroup(R.layout.view_dinner_notify_bubble)
public class FreeTableNotifyBubble extends LinearLayout {

    private final static int GONE_MSG = 0;

    private final static int GONE_DELAY = 3000;

    private EntranceType entranceType;

    @ViewById(R.id.iv_icon)
    ImageView ivIcon;

    @ViewById(R.id.tv_message)
    TextView tvMessage;

    private boolean isForeground = false;

    private final List<NotifyTextCache> notifyTextCaches = new ArrayList<>();

    private final Handler handler = new Handler(getContext().getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GONE_MSG:
                    goOut();
                    break;
                default:
                    break;
            }
        }
    };

    public FreeTableNotifyBubble(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public FreeTableNotifyBubble(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FreeTableNotifyBubble(Context context) {
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

    /*public void onEventMainThread(ActionNotifySwitcher notifySwitcher) {

        if (entranceType == notifySwitcher.getEntranceType()) {
            if (isForeground && notifySwitcher.getType() == NotificationType.FREE_TABLES) {
                show(notifySwitcher.getText(), notifySwitcher.getType());
            }
        }
    }*/

    private void show(String text, NotificationType type) {


        // 气泡处于未显示状态，直接显示气泡(没有在播放动画，并且没有正在显示)
        if ((getAnimation() == null || getAnimation().hasEnded()) && getVisibility() != View.VISIBLE) {
            tvMessage.setText(text);
            comeIn();
            // 气泡正在显示，先移除消失事件，然后将数据加入缓存队列，等待展示
        } else {
            notifyTextCaches.add(new NotifyTextCache(text, type));
        }
    }

    private void comeIn() {
        setVisibility(View.VISIBLE);
        // 恢复透明度
        setAlpha(1);
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "translationX", getWidth(), 0f).setDuration(200);
        animator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator arg0) {
                setVisibility(View.VISIBLE);
                handler.sendEmptyMessageDelayed(GONE_MSG, GONE_DELAY);
            }
        });
        animator.start();
        EventBus.getDefault().post(new EventIsCanClick(false));
    }

    private void goOut() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f);
        animator.setDuration(200);
        animator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                setVisibility(View.INVISIBLE);
                // 取缓存，进行展示
                if (notifyTextCaches.size() > 0) {
                    NotifyTextCache cache = notifyTextCaches.remove(0);
                    show(cache.getText(), cache.getType());
                }
            }
        });
        animator.start();
        EventBus.getDefault().post(new EventIsCanClick(true));
    }

    @Click({R.id.iv_close})
    void click(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                // 有动画，先清掉动画，然后隐藏
                if (getAnimation() != null && getAnimation().isInitialized() && !getAnimation().hasEnded()) {
                    getAnimation().cancel();
                }
                // 移除让控件goout的message，因为要直接消失
                handler.removeMessages(GONE_MSG);
                setVisibility(View.INVISIBLE);
                EventBus.getDefault().post(new EventIsCanClick(true));
                // 取缓存，进行展示
                if (notifyTextCaches.size() > 0) {
                    NotifyTextCache cache = notifyTextCaches.remove(0);
                    show(cache.getText(), cache.getType());
                }
                break;
            default:
                break;
        }
    }

    public void destroy() {
        EventBus.getDefault().unregister(this);
    }

    public void setEntranceType(final EntranceType entranceType) {

        this.entranceType = entranceType;
    }

    public final static class NotifyTextCache {
        private String text;

        private NotificationType type;

        public NotifyTextCache(String text, NotificationType type) {
            this.text = text;
            this.type = type;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public NotificationType getType() {
            return type;
        }

        public void setType(NotificationType type) {
            this.type = type;
        }

    }

}
