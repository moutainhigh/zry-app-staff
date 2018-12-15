package com.zhongmei.bty.basemodule.async.listener;

import android.text.TextUtils;
import android.util.Log;

import com.zhongmei.bty.basemodule.pay.message.PaymentResp;
import com.zhongmei.bty.commonmodule.database.entity.local.AsyncHttpRecord;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.ResponseObject;

/**
 * 异步收银回调
 * Created by demo on 2018/12/15
 */
public class AsyncPayResponseListener implements AsyncResponseListener<PaymentResp> {

    private final static String TAG = AsyncPayResponseListener.class.getSimpleName();

    private AsyncHttpRecord asyncRec;

    public AsyncHttpRecord getAsyncRec() {
        return asyncRec;
    }

    public void setAsyncRec(AsyncHttpRecord asyncRec) {
        this.asyncRec = asyncRec;
    }

    @Override
    public void onResponse(ResponseObject<PaymentResp> response) {
        switch (response.getStatusCode()) {
            case ResponseObject.OK:
                if (asyncRec != null && !TextUtils.isEmpty(asyncRec.getTradeUuId())) {
                    String tradeUuid = asyncRec.getTradeUuId();
                    //打印结账单 moify v8.8
                    /*IPrintContentQueue.Holder.getInstance().printDinnerCashTrade(tradeUuid, false, null,
                            null, new OnSimplePrintListener(PrintTicketTypeEnum.CASH));*/
                    //IPrintHelper.Holder.getInstance().printDinnerPayTicket(tradeUuid, null, false, false, new PRTBatchOnSimplePrintListener(PrintTicketTypeEnum.CASH));
                }
                break;
            //业务失败在AsyncNetworkManager层已经处理，这里不用处理了
//            default:
//                payFailed(response.getMessage(), false);
//                break;
        }

    }

    @Override
    public void onError(VolleyError error) {
        try {
//            payFailed(error.getMessage(), true);
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }

//    public void payFailed(String msg, boolean canRetry) {
//        if (asyncRec != null) {
//            int retryCount = asyncRec.getRetryCount() == null ? 0 : asyncRec.getRetryCount().intValue();
//            if (retryCount == 1) {
//                //发送eventbus，通知界面弹框
//                ActionPayFailed action = new ActionPayFailed();
//                action.setAsyncRec(asyncRec);
//                action.setErrorMsg(msg);
//                action.setCanRetry(canRetry);
//                EventBus.getDefault().post(action);
//            }
//            switch (retryCount) {
//                //没重试过
//                case 0:
//                    if (canRetry) {
//                        //可以重试，自动重试一次
//                        AsyncPayResponseListener listener = new AsyncPayResponseListener();
//                        AsyncNetworkManager.getInstance().retry(asyncRec.getUuid(), listener);
//                    } else {
//                        //不能重试，删除异步记录
//                        try {
//                            DBHelper.deleteById(AsyncHttpRecord.class, asyncRec.getUuid());
//                        } catch (Exception e) {
//                            Log.e(TAG, e.getMessage(), e);
//                        }
//                    }
//                    break;
//                //重试过一次后
//                case 1:
//                    //发送eventbus，通知界面弹框
//                    ActionPayFailed action = new ActionPayFailed();
//                    action.setAsyncRec(asyncRec);
//                    action.setErrorMsg(msg);
//                    action.setCanRetry(canRetry);
//                    EventBus.getDefault().post(action);
//                    break;
//                default:
//                    if(!canRetry){
//                        //不能重试，删除异步记录
//                        try {
//                            DBHelper.deleteById(AsyncHttpRecord.class, asyncRec.getUuid());
//                        } catch (Exception e) {
//                            Log.e(TAG, e.getMessage(), e);
//                        }
//                    }
//                    break;
//            }
//        }
//    }

}
