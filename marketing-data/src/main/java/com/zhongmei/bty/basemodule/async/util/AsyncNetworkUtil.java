package com.zhongmei.bty.basemodule.async.util;

import android.text.TextUtils;

import com.zhongmei.yunfu.data.R;
import com.zhongmei.bty.basemodule.async.listener.AsyncBatchModifyResponseListener;
import com.zhongmei.bty.basemodule.async.listener.AsyncModifyResponseListener;
import com.zhongmei.bty.basemodule.async.listener.AsyncOpenTableResponseListener;
import com.zhongmei.bty.basemodule.async.listener.AsyncPayResponseListener;
import com.zhongmei.bty.basemodule.async.manager.AsyncNetworkManager;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.bty.commonmodule.database.entity.local.AsyncHttpRecord;
import com.zhongmei.bty.commonmodule.database.enums.AsyncHttpState;
import com.zhongmei.bty.commonmodule.database.enums.AsyncHttpType;


public class AsyncNetworkUtil {


    public static void retryAsyncModifyTrade(AsyncHttpRecord asyncRec) {
        if (asyncRec == null || asyncRec.getType() == null) {
            return;
        }

        switch (asyncRec.getType()) {
            case MODIFYTRADE:
            case UNION_SUB_MODIFYTRADE:
                AsyncModifyResponseListener modifyListener = new AsyncModifyResponseListener();
                AsyncNetworkManager.getInstance().retry(asyncRec, modifyListener);
                break;
            case UNION_MAIN_MODIFYTRADE:
                AsyncBatchModifyResponseListener batchModifyListener = new AsyncBatchModifyResponseListener();
                AsyncNetworkManager.getInstance().retry(asyncRec, batchModifyListener);
                break;
        }
    }


    public static void retryAsyncOperate(AsyncHttpRecord asyncRec, AsyncHttpRecord sourceRec) {
        if (asyncRec == null || asyncRec.getType() == null) {
            return;
        }

        switch (asyncRec.getType()) {
            case OPENTABLE:
                AsyncOpenTableResponseListener openTableResponseListener = new AsyncOpenTableResponseListener();
                openTableResponseListener.setAsyncSourceRec(sourceRec);
                AsyncNetworkManager.getInstance().retry(asyncRec, openTableResponseListener);
                break;
            case MODIFYTRADE:
            case UNION_SUB_MODIFYTRADE:
                AsyncModifyResponseListener modifyListener = new AsyncModifyResponseListener();
                AsyncNetworkManager.getInstance().retry(asyncRec, modifyListener);
                break;
            case UNION_MAIN_MODIFYTRADE:
                AsyncBatchModifyResponseListener batchModifyListener = new AsyncBatchModifyResponseListener();
                AsyncNetworkManager.getInstance().retry(asyncRec, batchModifyListener);
                break;
            case CASHER:
                AsyncPayResponseListener payListener = new AsyncPayResponseListener();
                AsyncNetworkManager.getInstance().retry(asyncRec, payListener);
                break;
        }
    }


    public static String getAsyncOperateTip(AsyncHttpRecord record) {
        if (record != null) {
            String operateType = "";
            if (record.getType() == AsyncHttpType.OPENTABLE) {
                operateType = BaseApplication.sInstance.getString(R.string.async_open_table);
            } else if (record.getType() == AsyncHttpType.MODIFYTRADE
                    || record.getType() == AsyncHttpType.UNION_MAIN_MODIFYTRADE
                    || record.getType() == AsyncHttpType.UNION_SUB_MODIFYTRADE) {
                operateType = BaseApplication.sInstance.getString(R.string.async_modify_order);
            } else if (record.getType() == AsyncHttpType.CASHER) {
                operateType = BaseApplication.sInstance.getString(R.string.async_pay_for);
            }
            String strAppend = "";

            if (record.getStatus() == AsyncHttpState.FAILED) {
                strAppend = BaseApplication.sInstance.getString(R.string.async_please_cancel_operation_in_notification_bar);
            } else if (record.getStatus() == AsyncHttpState.EXCUTING) {
                strAppend = BaseApplication.sInstance.getString(R.string.async_operating_please_wait);
            } else if (record.getStatus() == AsyncHttpState.RETRING) {
                strAppend = BaseApplication.sInstance.getString(R.string.async_retrying_please_wait);
            }
            String message = BaseApplication.sInstance.getString(R.string.async_trade_tip_toast, operateType, strAppend);

            return message;
        }

        return null;
    }


    public static void retryModifyOrCasher(AsyncHttpRecord rec) {
        switch (rec.getType()) {
            case MODIFYTRADE:
            case UNION_SUB_MODIFYTRADE:
                AsyncModifyResponseListener modifyListener = new AsyncModifyResponseListener();
                AsyncNetworkManager.getInstance().retry(rec, modifyListener);
                break;
            case UNION_MAIN_MODIFYTRADE:
                AsyncBatchModifyResponseListener batchModifyListener = new AsyncBatchModifyResponseListener();
                AsyncNetworkManager.getInstance().retry(rec, batchModifyListener);
                break;
            case CASHER:
                AsyncPayResponseListener payListener = new AsyncPayResponseListener();
                AsyncNetworkManager.getInstance().retry(rec, payListener);
                break;
        }
    }


    public static String getSerialNumber(AsyncHttpRecord record) {
        String serialNumber = "";

        switch (record.getType()) {
            case UNION_MAIN_MODIFYTRADE:
            case UNION_SUB_MODIFYTRADE:
            case MODIFYTRADE:
            case CASHER:
                if (!TextUtils.isEmpty(record.getSerialNumber())) {
                    serialNumber = String.format(BaseApplication.sInstance.getString(R.string.async_index_no), record.getSerialNumber());
                }
                break;
            case OPENTABLE:
                serialNumber = "";
                break;
        }

        return serialNumber;
    }


    public static String getType(AsyncHttpType type) {

        String mType = "Unknow";
        switch (type) {
            case UNION_MAIN_MODIFYTRADE:
            case UNION_SUB_MODIFYTRADE:
            case MODIFYTRADE:
                                mType = BaseApplication.sInstance.getString(R.string.async_modify_order);
                break;
            case CASHER:
                mType = BaseApplication.sInstance.getString(R.string.async_pay_for);
                break;
            case OPENTABLE:
                mType = BaseApplication.sInstance.getString(R.string.async_open_table);
                break;
        }

        return mType;

    }


    public static String getStatus(AsyncHttpState status) {
        String mStatus = "Unknow";
        switch (status) {
            case SUCCESS:
                mStatus = BaseApplication.sInstance.getString(R.string.async_success);
                break;
            case RETRING:
                mStatus = BaseApplication.sInstance.getString(R.string.async_retring);
                break;
            case FAILED:
                mStatus = BaseApplication.sInstance.getString(R.string.async_failed);
                break;
            case EXCUTING:
                mStatus = BaseApplication.sInstance.getString(R.string.async_executing);
                break;
        }

        return mStatus;
    }


}
