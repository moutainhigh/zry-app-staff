package com.zhongmei.bty.base;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.zhongmei.bty.AppDialog;
import com.zhongmei.bty.basemodule.async.event.ActionExitLoginRemind;
import com.zhongmei.bty.basemodule.async.event.ActionModifyTradePrintFailed;
import com.zhongmei.bty.basemodule.async.event.ActionOpenTableFailed;
import com.zhongmei.bty.basemodule.async.listener.AsyncOpenTableResponseListener;
import com.zhongmei.bty.basemodule.async.manager.AsyncNetworkManager;
import com.zhongmei.bty.basemodule.commonbusiness.event.SpeechCallStopEvent;
import com.zhongmei.bty.basemodule.commonbusiness.manager.MediaPlayerQueueManager;
import com.zhongmei.bty.basemodule.database.enums.PrintTicketTypeEnum;
import com.zhongmei.bty.commonmodule.database.entity.local.AsyncHttpRecord;
import com.zhongmei.bty.commonmodule.database.enums.PrintStatesEnum;
import com.zhongmei.bty.commonmodule.util.ActivityUtil;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.util.ActivityUtils;
import com.zhongmei.yunfu.ui.base.BaseActivity;
import com.zhongmei.yunfu.util.DialogUtil;
import com.zhongmei.yunfu.util.MobclickAgentEvent;

/**
 * Created by demo on 2018/12/15
 */

public class MainBaseActivity extends BaseActivity {
    private static final String TAG = MainBaseActivity.class.getSimpleName();

    @Override
    protected void onResume() {
        super.onResume();
        Class clazz = getClass();
        ActivityUtil.setTopActivityClazz(clazz);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Class topActivityClazz = ActivityUtil.getTopActivityClazz();
        Class clazz = getClass();
        if (topActivityClazz != null && topActivityClazz.equals(clazz)) {
            //相等时，说明不是其他activity切换，而是其他应用导致该应用到后台
            ActivityUtil.setTopActivityClazz(null);
        }
    }

    /**
     * 开台失败处理
     */
    public void onEventMainThread(final ActionOpenTableFailed action) {
        boolean needOperate = ActivityUtils.isForeground(this, this.getClass().getName());
        if (needOperate) {
            final AsyncHttpRecord asyncRec = action.asyncRec;
            if (asyncRec != null) {
                String text = getString(R.string.dinner_tables) + asyncRec.getTableName() + getString(R.string.open_table_fail)
                        + "\n" + action.errorMsg;
                if (action.canRetry) {
                    DialogUtil.showWarnConfirmDialog(getSupportFragmentManager(), text,
                            R.string.retry_now, R.string.operate_later,
                            new OnClickListener() {

                                @Override
                                public void onClick(View arg0) {
                                    //重试改单
                                    MobclickAgentEvent.onEvent(MainBaseActivity.this, MobclickAgentEvent.dinnerAsyncHttpDailoRetry);
                                    AsyncOpenTableResponseListener listener = new AsyncOpenTableResponseListener();
                                    AsyncNetworkManager.getInstance().retry(asyncRec, listener);
                                }
                            },
                            new OnClickListener() {

                                @Override
                                public void onClick(View arg0) {
                                    //忽略
                                    MobclickAgentEvent.onEvent(MainBaseActivity.this, MobclickAgentEvent.dinnerAsyncHttpDailoIgnore);
                                }
                            },
                            "modify_trade_failed");
                } else {
                    DialogUtil.showWarnConfirmDialog(getSupportFragmentManager(), text, R.string.know,
                            null, false, "modify_trade_failed");
                }
            }
        }
    }

    public void onEventMainThread(final ActionExitLoginRemind action) {
        if (ActivityUtils.isForeground(this, this.getClass().getName())) {
            AppDialog.showExitLoginRemindDialog();
        }
    }

    /**
     * 改单打印失败处理
     */
    public void onEventMainThread(final ActionModifyTradePrintFailed action) {
        if (ActivityUtils.isForeground(this, this.getClass().getName())) {
            if (action.receiptSendData == null && action.kitchenAllSendData == null
                    && action.kitchenCellSendData == null && action.labelSendData == null) {
                return;
            }

            //可以重试的才弹框
            StringBuilder sb = new StringBuilder();
            if (action.receiptSendData != null) {
                sb.append(action.receiptTicketType.value() + "、");
            }
            if (action.kitchenAllSendData != null) {
                sb.append(PrintTicketTypeEnum.KITCHENALL.value() + "、");
            }
            if (action.kitchenCellSendData != null) {
                sb.append(PrintTicketTypeEnum.KITCHENCELL.value() + "、");
            }
            if (action.labelSendData != null) {
                sb.append(PrintTicketTypeEnum.LABEL.value() + "、");
            }
            if (TextUtils.isEmpty(sb)) {
                return;
            }

            sb.delete(sb.length() - 1, sb.length());
            sb.append(PrintStatesEnum.PRINT_GLOBAL_ALL_FAIL.message());
            if (!TextUtils.isEmpty(action.getTableName()) && !TextUtils.isEmpty(action.getSerialNumber())) {
                sb = new StringBuilder(getString(R.string.dinner_tables) + action.getTableName()
                        + getString(R.string.kitchen_business_no) + action.getSerialNumber()
                        + "\n" + sb);
            }
        }
    }


    /**
     * 服务铃播放结束
     */
    public void onEventMainThread(SpeechCallStopEvent selectEvent) {
        MediaPlayerQueueManager.getInstance().onCompletion(null);
    }
}
