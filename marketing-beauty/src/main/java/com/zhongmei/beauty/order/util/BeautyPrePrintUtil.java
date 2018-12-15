package com.zhongmei.beauty.order.util;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.basemodule.database.enums.PrintTicketTypeEnum;
import com.zhongmei.yunfu.monitor.CalmResponseListener;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.manager.DinnerCashManager;
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.yunfu.net.builder.NetError;
import com.zhongmei.beauty.operates.BeautyOperates;
import com.zhongmei.beauty.operates.message.BeautyTradeResp;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.util.ToastUtil;

import java.math.BigDecimal;
import java.util.List;

/**
 * 预结单打印工具类
 * Created by demo on 2018/12/15
 */

public class BeautyPrePrintUtil {
    private static final String TAG = "BeautyPrePrintUtil";

    /**
     * 打印预结单
     */
    public static void doWillPrint(final FragmentActivity activity) {
        final TradeVo tradeVo = DinnerShoppingCart.getInstance().getOrder();
        if (tradeVo == null || tradeVo.getTrade() == null) {
            return;
        }
        TradeVo newTradeVo = DinnerShoppingCart.getInstance().createOrder();
        //打印预结单先保存原单
        savePrintTrade(activity, newTradeVo);
    }


    private static void savePrintTrade(final FragmentActivity activity, final TradeVo tradeVo) {
        BeautyOperates beautyOperates = OperatesFactory.create(BeautyOperates.class);
        CalmResponseListener responseListener = new CalmResponseListener<ResponseObject<BeautyTradeResp>>() {
            @Override
            public void onError(NetError error) {
                ToastUtil.showLongToast(error.getVolleyError().getMessage());
            }

            @Override
            public void onSuccess(ResponseObject<BeautyTradeResp> resp) {
                if (ResponseObject.isOk(resp)) {
                    saveServerPrint(activity, tradeVo);
                } else {
                    if (resp.getMessage() != null) {
                        ToastUtil.showLongToast(resp.getMessage());
                    }
                }
            }
        };
        if (tradeVo.getTrade().getId() == null) {
            beautyOperates.submitBeauty(tradeVo, responseListener, activity);
        } else {
            beautyOperates.modifyBeauty(tradeVo, responseListener, activity);
        }
    }

    private void localPrint(Context context) {
        TradeVo printVo = DinnerShopManager.getInstance().getShoppingCart().createOrder();
        //PrintOperationOperates printOperationOperates = OperatesFactory.create(PrintOperationOperates.class);
        //doRelWillPayPrint(context, printOperationOperates, printVo);
    }


    private static void saveServerPrint(final FragmentActivity activity, final TradeVo tradeVo) {
        new AsyncTask<Void, Void, TradeVo>() {

            @Override
            protected TradeVo doInBackground(Void... params) {
                Log.e(TAG, "改单成功后开始查询定单");
                TradeDal tradeDal = OperatesFactory.create(TradeDal.class);
                TradeVo _tradeVo = null;
                try {
                    _tradeVo = tradeDal.findTrade(tradeVo.getTrade().getUuid(), false);
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
                return _tradeVo;
            }

            protected void onPostExecute(TradeVo vo) {
                try {
                    //先把原单会员拿出来，等reset方法玩了以后再放回去（临时方案，后续需要优化）
                    CustomerResp customer = CustomerManager.getInstance().getDinnerLoginCustomer();
                    DinnerShoppingCart.getInstance().updateDataFromTradeVo(vo, true);
                    CustomerManager.getInstance().setDinnerLoginCustomer(customer);
                    noticeRefreshCustomerUI(activity, false);
                    // 打印预结单
                    //PrintOperationOperates printOperationOperates = OperatesFactory.create(PrintOperationOperates.class);
                    //doRelWillPayPrint(activity, printOperationOperates, vo);
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    /*private static void doRelWillPayPrint(Context context,
                                          PrintOperationOperates printOperationOperates, TradeVo tradeVo) {
        try {
            if (tradeVo == null) {
                ToastUtil.showLongToast(context.getResources().getString(com.zhongmei.yunfu.data.R.string.dinner_order_null_print_hint));
                return;
            }
            List<IShopcartItem> mListOrderDishshopVo = DinnerShoppingCart.getInstance().getShoppingCartDish();
            if (!DinnerShopManager.getInstance().isHasItems(tradeVo, mListOrderDishshopVo)) {
                ToastUtil.showLongToast(BaseApplication.sInstance.getResources().getString(com.zhongmei.yunfu.data.R.string.dinner_item_null_print_hint));
                return;
            }
            if (tradeVo.getTrade() != null && tradeVo.getTrade().getTradeAmount().compareTo(BigDecimal.ZERO) == -1) {
                ToastUtil.showLongToast(
                        BaseApplication.sInstance.getResources().getString(com.zhongmei.yunfu.data.R.string.dinner_privilege_over));
                return;
            }
            tradeVo.getTrade().validateUpdate();

            IPrintHelper.Holder.getInstance().printPreCashTicket(DinnerCashManager.cloneValidTradeVo(tradeVo), false, new PRTBatchOnSimplePrintListener(PrintTicketTypeEnum.PRECASH));
            PLog.d(PLog.TAG_CALLPRINT_KEY, "info:正餐调用预结单打印接口printPreDinnerCashTrade（）;tradeUuid:" + tradeVo.getTrade().getUuid() + ",position:" + TAG + "->doWillPayPrint()");
            DinnerCashManager.submitPrintStatus(printOperationOperates, tradeVo);
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }*/

    private static void noticeRefreshCustomerUI(FragmentActivity activity, boolean needRefreshCustomerData) {
        CustomerResp customerNew = DinnerShopManager.getInstance().getLoginCustomer();
        if (customerNew != null) {
            customerNew.needRefresh = needRefreshCustomerData;
        }
    }
}
