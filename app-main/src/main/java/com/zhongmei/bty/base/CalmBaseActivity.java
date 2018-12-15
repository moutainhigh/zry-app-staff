package com.zhongmei.bty.base;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
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
import com.zhongmei.bty.common.util.CalmActivityUtil;
import com.zhongmei.bty.commonmodule.database.entity.local.AsyncHttpRecord;
import com.zhongmei.bty.commonmodule.database.enums.PrintStatesEnum;
import com.zhongmei.bty.commonmodule.util.ActivityUtil;
import com.zhongmei.bty.dinner.action.ActionContractStatus;
import com.zhongmei.bty.dinner.async.AsyncHttpNotifyManager;
import com.zhongmei.bty.entity.enums.CommercialStatus;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.context.util.ActivityUtils;
import com.zhongmei.yunfu.ui.base.BaseActivity;
import com.zhongmei.yunfu.util.DialogUtil;
import com.zhongmei.yunfu.util.MobclickAgentEvent;

/**
 * Created by demo on 2018/12/15
 */

public class CalmBaseActivity extends BaseActivity {
    private static final String TAG = CalmBaseActivity.class.getSimpleName();

    @Override
    protected void onResume() {
        super.onResume();
        Class clazz = getClass();
        ActivityUtil.setTopActivityClazz(clazz);

        //已经开了，直接返回
        if (AsyncHttpNotifyManager.getInstance(this).isShow()) {
            return;
        }

        if (CalmActivityUtil.isDinnerActivity(clazz)) {
            Log.i(TAG, getClass().getSimpleName() + " onResume 打开异步通知栏");
            AsyncHttpNotifyManager.getInstance(this).showAsyncHttpNotify();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Class topActivityClazz = ActivityUtil.getTopActivityClazz();
        Class clazz = getClass();
        if (topActivityClazz != null && topActivityClazz.equals(clazz)) {
            //相等时，说明不是其他activity切换，而是其他应用导致该应用到后台
            ActivityUtil.setTopActivityClazz(null);
            topActivityClazz = null;
        }

        //已经关了，直接返回
        if (!AsyncHttpNotifyManager.getInstance(this).isShow()) {
            return;
        }

        //当前最上层不是正餐activity，关闭通知栏
        if (!CalmActivityUtil.isDinnerActivity(topActivityClazz) && !CalmActivityUtil.isDinnerShareActivity(topActivityClazz)) {
            Log.i(TAG, getClass().getSimpleName() + " onStop 关闭异步通知栏");
            AsyncHttpNotifyManager.getInstance(this).closeAsyncHttpNotify();
        }
    }

    // 用来收到弹出打印机<状态的对话框
    /*public void onEventMainThread(ActionPrinterListStatus actionPrinterListStatus) {
        if (ActivityUtils.isForeground(this, this.getClass().getName())) {//添加最上层界面的判断
            Map<Long, ActionPrinterStatusChange> statusMap = actionPrinterListStatus.getStatusMap();
            if (statusMap == null || statusMap.size() <= 0) {
                Log.i(PrinterStateUtil.TAG, PrinterStateUtil.TAG + "------onEventMainThread收到打印机列表状态返回的List.size=0或者为null");
                return;
            } else {
                Log.i(PrinterStateUtil.TAG, PrinterStateUtil.TAG + "------onEventMainThread收到打印机列表状态返回的List.size=" + statusMap.size());
            }

            // 上传打印机健康状态
            PrinterStateUtil.updatePrintersStatus(statusMap, true);

            List<ActionPrinterStatusChange> list = new ArrayList<ActionPrinterStatusChange>(statusMap.values());

            PrinterStateUtil.doShowPrinterDialog(this, getSupportFragmentManager(), list);
        }
    }*/

    /**
     * 合同状态处理
     */
    public void onEventMainThread(ActionContractStatus action) {
        if (ActivityUtils.isForeground(this, this.getClass().getName())) {
            if (action.getStatus() == CommercialStatus.UNAVAILABLE) {
                DialogUtil.showErrorConfirmDialog(getSupportFragmentManager(), R.string.contract_unvailable, R.string.ok, new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        exitPrintServer();
                        BaseApplication.sInstance.finishAllActivity(null);
                    }
                }, true, "contract_unvailable");
            }
        }
    }

    private void exitPrintServer() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        am.killBackgroundProcesses("com.demo.print");
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
                                    MobclickAgentEvent.onEvent(CalmBaseActivity.this, MobclickAgentEvent.dinnerAsyncHttpDailoRetry);
                                    AsyncOpenTableResponseListener listener = new AsyncOpenTableResponseListener();
                                    AsyncNetworkManager.getInstance().retry(asyncRec, listener);
                                }
                            },
                            new OnClickListener() {

                                @Override
                                public void onClick(View arg0) {
                                    //忽略
                                    MobclickAgentEvent.onEvent(CalmBaseActivity.this, MobclickAgentEvent.dinnerAsyncHttpDailoIgnore);
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

    /**
     * 改单失败处理
     *
     * @deprecated 此代码已经放在DinnerMainActivity上去了
     */
    /*public void onEventMainThread(final ActionModifyTradeFailed action) {
        boolean needOperate = this instanceof DinnerMainActivity;
        if (!needOperate) {
            return;
        }

        final AsyncHttpRecord asyncRec = action.asyncRec;
        if (asyncRec == null) {
            return;
        }

        String text = action.errorMsg;
        if (action.canRetry) {
            DialogUtil.showWarnConfirmDialog(getSupportFragmentManager(), text,
                    R.string.retry_now, R.string.operate_later,
                    new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            //重试改单
                            MobclickAgentEvent.onEvent(CalmBaseActivity.this, MobclickAgentEvent.dinnerAsyncHttpDailoRetry);
                            AsyncNetworkUtil.retryAsyncModifyTrade(asyncRec);
                        }
                    },
                    new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            //忽略
                            MobclickAgentEvent.onEvent(CalmBaseActivity.this, MobclickAgentEvent.dinnerAsyncHttpDailoIgnore);
                        }
                    },
                    "modify_trade_failed");
        } else {
            DialogUtil.showWarnConfirmDialog(getSupportFragmentManager(), text, R.string.know,
                    null, false, "modify_trade_failed");
        }
    }*/
    public void onEventMainThread(final ActionExitLoginRemind action) {
        if (ActivityUtils.isForeground(this, this.getClass().getName())) {
            AppDialog.showExitLoginRemindDialog();
        }
    }

    /**
     * 收银失败处理
     * @deprecated 此代码已经放在DinnerMainActivity上去了
     */
    /*public void onEventMainThread(final ActionPayFailed action) {
        boolean needOperate = this instanceof DinnerMainActivity;
        if (needOperate) {
            final AsyncHttpRecord asyncRec = action.asyncRec;
            if (asyncRec != null) {
                String text = getString(R.string.dinner_tables) + asyncRec.getTableName()
                        + getString(R.string.kitchen_business_no) + asyncRec.getSerialNumber()
                        + getString(R.string.pay_fail) + "\n" + action.errorMsg;
                if (action.canRetry) {
                    DialogUtil.showWarnConfirmDialog(getSupportFragmentManager(), text,
                            R.string.retry_now, R.string.operate_later,
                            new OnClickListener() {

                                @Override
                                public void onClick(View arg0) {
                                    //重试收银
                                    AsyncPayResponseListener listener = new AsyncPayResponseListener();
                                    AsyncNetworkManager.getInstance().retry(asyncRec, listener);
                                }
                            },
                            new OnClickListener() {

                                @Override
                                public void onClick(View arg0) {
                                    //忽略
                                    MobclickAgentEvent.onEvent(CalmBaseActivity.this, MobclickAgentEvent.dinnerAsyncHttpDailoIgnore);
                                }
                            },
                            "pay_failed");
                } else {
                    DialogUtil.showWarnConfirmDialog(getSupportFragmentManager(), text, R.string.know,
                            null, false, "pay_failed");
                }
            }
        }
    }*/

    /**
     * 打印失败处理（对于可以重试的结果）
     */
    /*public void onEventMainThread(final ActionPrintFailed action) {
        if (action.sendData == null) {
            return;
        }

        if (ActivityUtils.isForeground(this, this.getClass().getName())) {
            *//*DialogUtil.showWarnConfirmDialog(getSupportFragmentManager(), action.msg, R.string.retry_now, R.string.operate_later,
                    new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            //重试打印
                            *//**//*if (action.sendData != null) {
                                PrintTool.retry(action.sendData, new PRTOnSimplePrintListener(action.ticketType));
                            }*//**//*
                        }
                    }, null, "print_failed");*//*
        }
    }*/

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

            /*DialogUtil.showWarnConfirmDialog(getSupportFragmentManager(), sb, R.string.retry_now, R.string.operate_later,
                    new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            //重试打印
                            PrintOperator operator = new PrintOperator(true);
                            operator.setTableName(action.getTableName());
                            operator.setSerialNUmber(action.getSerialNumber());
                            operator.retryPrint(action.receiptTicketType, action.receiptSendData,
                                    action.kitchenAllSendData, action.kitchenCellSendData, action.labelSendData);
                        }
                    },
                    null, "modify_print_failed");*/
        }
    }

    // v7.15 关帐提醒Dialog重复弹出
    /*private DialogFragment dialogFragment = null;

    public void onEventMainThread(AccountClosingManager.AlertEvent alertEvent) {
        if (alertEvent != null
                && ActivityUtil.getTopActivityClazz() == this.getClass()
                && !ILoginController.class.isAssignableFrom(ActivityUtil.getTopActivityClazz())) {
            switch (alertEvent.what) {
                case AccountClosingManager.AHEAD_30:
                    if (dialogFragment != null) {
                        dialogFragment.dismiss();
                    }
                    dialogFragment = DialogUtil.showWarnConfirmDialog(getSupportFragmentManager(),
                            getString(R.string.closing_alert_30),
                            R.string.closing_negative,
                            R.string.closing_todo,
                            new OnClickListener() {
                                @Override
                                public void onClick(View v) {
//                                    EMPTY
                                }
                            },
                            new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AccountClosingManager.toAccountClosing(CalmBaseActivity.this);
                                }
                            },
                            "account_alert_stub");
                    break;
                case AccountClosingManager.AHEAD_10:
                    if (dialogFragment != null) {
                        dialogFragment.dismiss();
                    }
                    dialogFragment = DialogUtil.showWarnConfirmDialog(getSupportFragmentManager(),
                            R.string.closing_alert_10,
                            R.string.closing_todo,
                            new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AccountClosingManager.toAccountClosing(CalmBaseActivity.this);
                                }
                            },
                            true,
                            "account_alert_delay");
                    break;
            }
        }
    }*/

    /*
        服务铃播放结束
         */
    public void onEventMainThread(SpeechCallStopEvent selectEvent) {
        MediaPlayerQueueManager.getInstance().onCompletion(null);
    }
}
