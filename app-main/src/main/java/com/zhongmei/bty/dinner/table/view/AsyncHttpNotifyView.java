package com.zhongmei.bty.dinner.table.view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.async.util.AsyncNetworkUtil;
import com.zhongmei.yunfu.context.util.ThreadUtils;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.commonmodule.database.entity.local.AsyncHttpRecord;
import com.zhongmei.bty.basemodule.async.operates.AsyncDal;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.dinner.adapter.AsyncHttpAdapter;
import com.zhongmei.bty.basemodule.async.manager.AsyncNetworkManager;
import com.zhongmei.bty.commonmodule.database.enums.AsyncHttpState;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by demo on 2018/12/15
 */


public class AsyncHttpNotifyView extends LinearLayout implements CompoundButton.OnCheckedChangeListener, AsyncHttpAdapter.AsyncControlListener, AsyncNetworkManager.AsyncHttpRecordChange {
    private final int WHAT_REFRESHUI = 0x01;

    private ScrollListView lv_asyncHttps;//展示异步Http请求

    private CheckBox cb_notifyControl;//是否展开通知

    private ImageView iv_enhace;//通知栏强调效果

    private AsyncHttpAdapter mHttpNotifyAdapter;

    private List<AsyncHttpRecord> mListHttpRecord;

    private int mNotifyItemHeight = -1;//通知栏默认的高度

    private final int mNotifyItemNumber = 5;//通知栏展开之后显示的通知条数

    //定时器
    private Timer mTimer;

    //清理成功异步纪录的任务器
    private TimerTask mClearRecrodTask;

    private UiHandler mUiHandler;

    //定时清理成功的 2*60*60*1000
    private final long mClearTaskExcutePeriod = 2 * 60 * 60 * 1000;//两小时执行一次

    private int mLastRefreshFailItemCount = 0;//上一次刷新失败的条目数

    //启动强调动画
    private Animation mEnhaceAnimation = null;

    public AsyncHttpNotifyView(Context context) {
        super(context);
    }

    public AsyncHttpNotifyView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public AsyncHttpNotifyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getmNotifyItemHeight() {
        return mNotifyItemHeight;
    }

    public void setmNotifyItemHeight(int mNotifyItemHeight) {
        this.mNotifyItemHeight = mNotifyItemHeight;
    }

    public int getmNotifyItemNumber() {
        return mNotifyItemNumber;
    }


    /**
     * 初始化UI,在DinnerMainActivity中的init()方法中调用
     */
    public void initView() {
        lv_asyncHttps = (ScrollListView) findViewById(R.id.lv_asyncHttp);
        cb_notifyControl = (CheckBox) findViewById(R.id.cb_notifyControl);
        iv_enhace = (ImageView) findViewById(R.id.iv_enhace);

        mListHttpRecord = new ArrayList<AsyncHttpRecord>();
        lv_asyncHttps.setEnableScroll(false);

        mHttpNotifyAdapter = new AsyncHttpAdapter(getContext(), mListHttpRecord);
        mHttpNotifyAdapter.setRequestControlListener(this);
        lv_asyncHttps.setAdapter(mHttpNotifyAdapter);

        mUiHandler = new UiHandler();
    }


    /**
     * 初始化数据
     */
    public void initData() {
        AsyncNetworkManager.getInstance().registDataChangeObserver();
        AsyncNetworkManager.getInstance().addAsyncRecordChangeListener(this);
        AsyncNetworkManager.getInstance().queryAllAsyncRecord(this);//初始化数据
    }

    /**
     * 初始化定时器,定时清理已经成功的异步任务纪录
     */
    private void initTimer() {
        if (mTimer == null || mClearRecrodTask == null) {
            mTimer = new Timer();
            mClearRecrodTask = new ClearRecordOnTimeTask();
            mTimer.schedule(mClearRecrodTask, mClearTaskExcutePeriod, mClearTaskExcutePeriod);
        }
    }


    /**
     * 初始化通知栏Item高度
     * 当有通知时需要计算Item 的height,并设置notifyView的height
     */
    private void initLayoutParams() {
        if (mHttpNotifyAdapter == null || mHttpNotifyAdapter.getCount() <= 0) {
            cb_notifyControl.setChecked(false);//设置通知为收起状态
            setVisibility(View.GONE);
            return;
        }

        if (getVisibility() == View.GONE || mNotifyItemHeight <= 0) {
            View contentView = mHttpNotifyAdapter.getView(0, null, lv_asyncHttps);
            contentView.measure(0, 0);
            mNotifyItemHeight = contentView.getMeasuredHeight();

            setVisibility(View.VISIBLE);
        }
    }

    /**
     * 通知面板的展开与收起
     * 该监听方法弃用
     *
     * @param buttonView
     * @param isChecked
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            setHeight(mNotifyItemHeight < 0 ? 160 : mNotifyItemHeight * mNotifyItemNumber);//默认高度
            lv_asyncHttps.setEnableScroll(true);
        } else {
            setHeight(mNotifyItemHeight < 0 ? ViewGroup.LayoutParams.WRAP_CONTENT : mNotifyItemHeight);
            lv_asyncHttps.smoothScrollToPosition(0);

            //删除成功的
            deleteAllSucceed();
        }
    }

    public void setEnableScroll(boolean scrollEnaable) {
        lv_asyncHttps.setEnableScroll(scrollEnaable);
        if (!scrollEnaable) {
            lv_asyncHttps.smoothScrollToPosition(0);
        }
    }


    /**
     * 设置通知栏的高度
     *
     * @param height
     */
    private void setHeight(int height) {
        ViewGroup.LayoutParams mLayoutParams = getLayoutParams();
        mLayoutParams.height = height;
        setLayoutParams(mLayoutParams);
    }


    /**
     * 取消操作
     *
     * @param record
     * @param position
     * @return
     */
    @Override
    public boolean cancelRequest(final AsyncHttpRecord record, int position) {
        MobclickAgentEvent.onEvent(getContext(), MobclickAgentEvent.dinnerAsyncHttpNotifyCancel);
        AsyncNetworkManager.getInstance().cancel(record);
        return true;
    }

    /**
     * 重试操作
     *
     * @param record
     * @param position
     * @return
     */
    @Override
    public boolean reTryRequest(final AsyncHttpRecord record, int position) {
        MobclickAgentEvent.onEvent(getContext(), MobclickAgentEvent.dinnerAsyncHttpNotifyRetry);
        AsyncNetworkUtil.retryAsyncOperate(record, null);

        return true;
    }


    @Override
    public void onChange(List<AsyncHttpRecord> allRecord) {
        List<AsyncHttpRecord> records = new ArrayList<AsyncHttpRecord>();


        int failCount = 0;
        if (Utils.isNotEmpty(allRecord)) {
            for (AsyncHttpRecord asyncHttpRecord : allRecord) {

                if (asyncHttpRecord.getStatus() == AsyncHttpState.EXCUTING || (asyncHttpRecord.getStatus() == AsyncHttpState.SUCCESS && asyncHttpRecord.getRetryCount() <= 0)) {
                    continue;
                }

                //数据在使用的时候最好clone一下。以免冲突
                AsyncHttpRecord tmpRecord = asyncHttpRecord.clone();

                if (tmpRecord != null) {
                    records.add(tmpRecord);
                }

                if (tmpRecord != null && tmpRecord.getStatus() == AsyncHttpState.FAILED) {
                    failCount++;
                }
            }
        }

        mUiHandler.obtainMessage(WHAT_REFRESHUI, failCount, 0, records).sendToTarget();
    }


    /**
     * 开启有新增异步任务的动画效果
     */
    private void startEnhaceAnimation() {
        if (mHttpNotifyAdapter == null || mHttpNotifyAdapter.getCount() <= 0) {
            return;
        }

        View contentView = lv_asyncHttps.getChildAt(0);
        if (contentView != null) {
            contentView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_view_top_to_bottom));
        }
        if (mEnhaceAnimation == null) {
            mEnhaceAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_view_left_to_right);
            mEnhaceAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    iv_enhace.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    iv_enhace.setVisibility(View.GONE);
                    mEnhaceAnimation = null;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            iv_enhace.startAnimation(mEnhaceAnimation);
        }


    }


    /**
     * 正餐onDestory()方法中调用
     */
    public void onDestory() {
        //正餐关闭时,删除成功已成功的
        deleteAllSucceed();
        AsyncNetworkManager.getInstance().unregistDataChangeObsserver();
        AsyncNetworkManager.getInstance().removeAsyncRecordChangeListener(this);
        AsyncNetworkManager.getInstance().onDestory();

        if (mTimer != null) {//取消定时删除已成功的定时器
            mTimer.cancel();
            mTimer = null;
        }

        if (mClearRecrodTask != null) {
            mClearRecrodTask.cancel();
            mClearRecrodTask = null;
        }

        if (mListHttpRecord != null) {
            mListHttpRecord.clear();
        }
    }

    /**
     * 删除成功的异步请求纪录
     */
    public void deleteAllSucceed() {
        ThreadUtils.runOnWorkThread(new Runnable() {
            @Override
            public void run() {
                AsyncDal asyncDal = OperatesFactory.create(AsyncDal.class);
                try {
                    asyncDal.deleteRecordByStatus(AsyncHttpState.SUCCESS);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 定时清理请求成功的任务记录
     */
    class ClearRecordOnTimeTask extends TimerTask {
        @Override
        public void run() {
            deleteAllSucceed();
        }
    }


    class UiHandler extends Handler {

        public UiHandler() {
            super(Looper.getMainLooper());
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_REFRESHUI:
                    mListHttpRecord.clear();
                    if (msg.obj != null) {
                        mListHttpRecord.addAll((List<AsyncHttpRecord>) msg.obj);
                    }
                    mHttpNotifyAdapter.notifyDataSetChanged();
                    initLayoutParams();
                    initTimer();

                    int failCount = msg.arg1;
                    if (failCount > mLastRefreshFailItemCount) {
                        startEnhaceAnimation();
                    }
                    mLastRefreshFailItemCount = failCount;
                    break;
            }
            super.handleMessage(msg);
        }
    }
}



