package com.zhongmei.bty.dinner.async;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.context.util.HomeWatcher;
import com.zhongmei.bty.dinner.table.view.AsyncHttpNotifyView;

/**
 * Created by demo on 2018/12/15
 */
public class AsyncHttpNotifyManager implements CompoundButton.OnCheckedChangeListener, HomeWatcher.OnHomePressedListener {
    private static final String TAG = AsyncHttpNotifyManager.class.getSimpleName();

    private Context mContext;

    private static AsyncHttpNotifyManager ourInstance;

    private WindowManager mWindowManager;

    private WindowManager.LayoutParams wmParams;

    private AsyncHttpNotifyView mAsyncHttpNotifyView;

    private CheckBox cb_notifyControl;//是否展开通知

    private boolean isShow = false;

//    private HomeWatcher mHomeWatcher;

    public static AsyncHttpNotifyManager getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new AsyncHttpNotifyManager(context.getApplicationContext());
        }
        return ourInstance;
    }

    private AsyncHttpNotifyManager(Context context) {
        this.mContext = context;
    }

    public boolean isShow() {
        return isShow;
    }

    public void showAsyncHttpNotify() {
        if (isShow) {//通知栏已显示
            return;
        }

        //获取的是LocalWindowManager对象
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) mContext.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        }

        //获取LayoutParams对象
        if (wmParams == null) {
            wmParams = new WindowManager.LayoutParams();
            wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            wmParams.format = PixelFormat.RGBA_8888;

            wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            wmParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
            wmParams.x = 0;
            wmParams.y = 0;
            wmParams.width = DensityUtil.dip2px(MainApplication.getInstance(), 440);
            wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        }

        if (mAsyncHttpNotifyView == null) {
            mAsyncHttpNotifyView = (AsyncHttpNotifyView) LayoutInflater.from(mContext).inflate(R.layout.layout_asynchttp_notify, null);
            cb_notifyControl = ((CheckBox) mAsyncHttpNotifyView.findViewById(R.id.cb_notifyControl));
            cb_notifyControl.setOnCheckedChangeListener(this);
            mAsyncHttpNotifyView.initView();
        }


        mWindowManager.addView(mAsyncHttpNotifyView, wmParams);

        mAsyncHttpNotifyView.initData();//注册监听,查询数据等

//        mHomeWatcher = new HomeWatcher(mContext);
//        mHomeWatcher.setOnHomePressedListener(this);
//        mHomeWatcher.startWatch();

        isShow = true;

        initNotifyHeight();
    }

    @Override
    public void onHomePressed() {
        Log.i(TAG, "onHomePressed 关闭通知栏");
        closeAsyncHttpNotify();
    }

    @Override
    public void onHomeLongPressed() {
        Log.i(TAG, "onHomeLongPressed 关闭通知栏");
        closeAsyncHttpNotify();
    }

    public void initNotifyHeight() {
        cb_notifyControl.setChecked(false);
        setHeight(mAsyncHttpNotifyView.getmNotifyItemHeight() < 0 ? ViewGroup.LayoutParams.WRAP_CONTENT : mAsyncHttpNotifyView.getmNotifyItemHeight());
    }

    public void closeAsyncHttpNotify() {
        if (isShow && mAsyncHttpNotifyView != null) {
            mWindowManager.removeView(mAsyncHttpNotifyView);
            cb_notifyControl.setOnCheckedChangeListener(null);
            mAsyncHttpNotifyView.onDestory();
        }

        isShow = false;
        mContext = null;
        mAsyncHttpNotifyView = null;
        ourInstance = null;
        mWindowManager = null;


//        if(mHomeWatcher != null){
//            mHomeWatcher.destory();
//            mHomeWatcher = null;
//        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            setHeight(mAsyncHttpNotifyView.getmNotifyItemHeight() < 0 ? 160 : mAsyncHttpNotifyView.getmNotifyItemHeight() * mAsyncHttpNotifyView.getmNotifyItemNumber());//默认高度
            mAsyncHttpNotifyView.setEnableScroll(true);
        } else {
            setHeight(mAsyncHttpNotifyView.getmNotifyItemHeight() < 0 ? ViewGroup.LayoutParams.WRAP_CONTENT : mAsyncHttpNotifyView.getmNotifyItemHeight());
            mAsyncHttpNotifyView.setEnableScroll(false);
            //删除成功的
            mAsyncHttpNotifyView.deleteAllSucceed();

        }
    }

    /**
     * 设置通知栏的高度
     *
     * @param height
     */
    private void setHeight(int height) {
        if (mWindowManager != null && wmParams != null && isShow) {
            wmParams.height = height;
            mWindowManager.updateViewLayout(mAsyncHttpNotifyView, wmParams);
        }
    }

}
