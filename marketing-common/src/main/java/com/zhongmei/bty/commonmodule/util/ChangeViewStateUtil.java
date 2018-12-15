package com.zhongmei.bty.commonmodule.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmei.yunfu.commonmodule.R;
import com.zhongmei.yunfu.util.ToastUtil;

/**
 * Created by demo on 2018/12/15
 */

public class ChangeViewStateUtil {
    private static final String TAG = ChangeViewStateUtil.class.getSimpleName();


    /**
     * 还原成正常的drawable
     *
     * @param view
     */
    private static void resetOriginDrawable(View view) {
        if (view instanceof ImageView) {
            Drawable drawable = (Drawable) view.getTag(R.id.origin_src_drawable);
            if (drawable != null) {
                ((ImageView) view).setImageDrawable(drawable);
            }
        } else {
            Drawable drawable = (Drawable) view.getTag(R.id.origin_background_drawable);
            if (drawable != null) {
                view.setBackground(drawable);
            }
        }
    }

    private static void saveOriginDrawable(View view) {
        if (view instanceof ImageView) {
            view.setTag(R.id.origin_src_drawable, ((ImageView) view).getDrawable());
        } else {
            view.setTag(R.id.origin_background_drawable, view.getBackground());
        }
    }

    private static void setNoNetworkDrawable(View view, Drawable drawable) {
        saveOriginDrawable(view);
        if (view instanceof ImageView) {
            ((ImageView) view).setImageDrawable(drawable);
        } else {
            view.setBackground(drawable);
        }
    }

    private static void setOffline(View view) {
        view.setEnabled(false);
        if (view instanceof TextView) {

        }
    }

    private static void setOnline(View view) {
        view.setEnabled(true);
    }

    private static void setViewDrawableByNetworkState(View view, Drawable drawable, boolean networkAvailable) {
        if (!networkAvailable) {
            setNoNetworkDrawable(view, drawable);
        } else {
            resetOriginDrawable(view);
        }
    }

    /**
     * 根据网络状态设置view的点击事件
     *
     * @param view
     */
    private static void setViewClickByNetworkState(View view, final int msgId, final boolean networkAvailable) {
        if (networkAvailable) {
            view.setOnTouchListener(null);
        } else {
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        ToastUtil.showShortToast(msgId);
                        return true;
                    }
                    return false;
                }
            });
        }
    }

   /* private static void setViewOfflineClick(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    ToastUtil.showShortToast(R.string.src_no_work_reject_toast);
                    return true;
                }
                return false;
            }
        });
    }*/

    private static void setViewOnlineClick(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }

    public static void setViewStateByNetworkState(Context context, boolean networkAvailable, int drawableId, View... views) {
        for (View view : views) {
            setViewDrawableByNetworkState(view, context.getResources().getDrawable(drawableId), networkAvailable);
            setViewClickByNetworkState(view, R.string.src_no_work_reject_toast, networkAvailable);
        }
    }

    public static void setViewStateByNetworkState(Context context, boolean networkAvailable, int drawableId, int msgId, View... views) {
        for (View view : views) {
            setViewDrawableByNetworkState(view, context.getResources().getDrawable(drawableId), networkAvailable);
            setViewClickByNetworkState(view, msgId, networkAvailable);
        }
    }

    private static void setViewOfflineState(View view) {
        if (view instanceof ViewGroup) {
            setOffline(view);
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                setViewOfflineState(((ViewGroup) view).getChildAt(i));
            }
        }
        setOffline(view);
    }

    /*public static void setViewOffline(View view) {
        setViewOfflineState(view);
        setViewOfflineClick(view);
    }

    public static void setViewOnline(View view) {
        setViewOnlineState(view);
        setViewOnlineClick(view);
       // setViewClickByNetworkState(view, R.string.no_work_reject_toast, true);
    }*/

    private static void setViewOnlineState(View view) {
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                setViewOnlineState(((ViewGroup) view).getChildAt(i));
            }
        }
        setOnline(view);
    }
}
