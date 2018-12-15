package com.zhongmei.bty.cashier.ordercenter.presenter;

import android.support.v4.util.Pair;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.bty.basemodule.auth.application.DinnerApplication;
import com.zhongmei.bty.cashier.ordercenter.bean.FilterData;
import com.zhongmei.bty.cashier.ordercenter.manager.DinnerOrderCenterListManager;
import com.zhongmei.bty.cashier.ordercenter.view.IOrderCenterListView;
import com.zhongmei.bty.basemodule.database.utils.DbQueryConstant;
import com.zhongmei.yunfu.db.enums.DeliveryType;
import com.zhongmei.bty.commonmodule.database.enums.OrderActionEnum;
import com.zhongmei.yunfu.db.enums.SourceId;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.bty.basemodule.auth.permission.manager.AuthLogManager;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.bty.basemodule.trade.message.TradePaymentResp;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.util.ValueEnum;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单中心订单列表正餐Presenter
 */

public class DinnerOrderCenterListPresenter extends OrderCenterListPresenter {

    public DinnerOrderCenterListPresenter(IOrderCenterListView view) {
        super(view, new DinnerOrderCenterListManager());
    }

    @Override
    public void deliveryPayment(final List<TradeVo> tradeVos) {
        if (Utils.isEmpty(tradeVos)) {
            return;
        }

        final ResponseListener<TradePaymentResp> listener = new ResponseListener<TradePaymentResp>() {
            @Override
            public void onResponse(ResponseObject<TradePaymentResp> response) {
                AuthLogManager.getInstance().flush(OrderActionEnum.ACTION_CLEAR_BALANCE, null, null, null);
                mView.showToast(response.getMessage());
                mView.squareAccountFinish();
            }

            @Override
            public void onError(VolleyError error) {
                mView.showToast(error.getMessage());
            }
        };
        VerifyHelper.verifyAlert(mView.getViewActivity(), DinnerApplication.PERMISSION_DINNER_TAKEOUT_CLEAR,
                new VerifyHelper.Callback() {
                    @Override
                    public void onPositive(User user, String code, Auth.Filter filter) {
                        super.onPositive(user, code, filter);
                        mManager.clearAccounts(tradeVos, LoadingResponseListener.ensure(listener, mView.getViewFragmentManager()));
                    }
                });
    }

    @Override
    public void addProcessTab(List<Pair<String, Integer>> processTab) {
        addChildTab(processTab, R.string.unprocessed_all, DbQueryConstant.UNPROCESSED_ALL);
        addChildTab(processTab, R.string.unprocessed_new_order, DbQueryConstant.UNPROCESSED_NEW_ORDER);
        /* addChildTab(processTab, R.string.unprocessed_dinner_invalid, DbQueryConstant.UNPROCESSED_INVALID);*/
        //modify v8.15 添加取消请求和取消
        addChildTab(processTab, R.string.unprocessed_cancel_request, DbQueryConstant.UNPROCESSED_CANCEL_REQUEST);
        addChildTab(processTab, R.string.unprocessed_invalid, DbQueryConstant.UNPROCESSED_INVALID);
    }

    @Override
    public void addSaleNoteTab(List<Pair<String, Integer>> childTabs) {
        addChildTab(childTabs, R.string.sales_all, DbQueryConstant.SALES_ALL);
        addChildTab(childTabs, R.string.sales_unpaid, DbQueryConstant.SALES_UNPAID);
        addChildTab(childTabs, R.string.sales_paying, DbQueryConstant.SALES_PAYING);
        addChildTab(childTabs, R.string.sales_paid, DbQueryConstant.SALES_PAID);
        addChildTab(childTabs, R.string.sales_refunded, DbQueryConstant.SALES_REFUNDED);
        addChildTab(childTabs, R.string.sales_invalid, DbQueryConstant.SALES_INVALID);
    }

    @Override
    public List<FilterData> getFilterData() {
        List<FilterData> filterDatas = new ArrayList<>();
        FilterData filterData = new FilterData(getString(R.string.order_type));
        filterData.addItem(new android.util.Pair<String, ValueEnum>(getString(R.string.order_here), DeliveryType.HERE));
        filterData.addItem(new android.util.Pair<String, ValueEnum>(getString(R.string.order_carry), DeliveryType.CARRY));
        filterData.addItem(new android.util.Pair<String, ValueEnum>(getString(R.string.order_send), DeliveryType.SEND));
        filterData.addItem(new android.util.Pair<String, ValueEnum>(getString(R.string.order_take), DeliveryType.TAKE));
        filterDatas.add(filterData);

        filterData = new FilterData(getString(R.string.order_source));

        filterData.addItem(new android.util.Pair<String, ValueEnum>(SourceId.WECHAT.desc(), SourceId.WECHAT));
        filterData.addItem(new android.util.Pair<String, ValueEnum>(SourceId.BAIDU_RICE.desc(), SourceId.BAIDU_RICE));
        filterData.addItem(new android.util.Pair<String, ValueEnum>(SourceId.KIOSK.desc(), SourceId.KIOSK));
        filterData.addItem(new android.util.Pair<String, ValueEnum>(SourceId.POS.desc(), SourceId.POS));
        filterData.addItem(new android.util.Pair<String, ValueEnum>(SourceId.ON_MOBILE.desc(), SourceId.ON_MOBILE));
        filterData.addItem(new android.util.Pair<String, ValueEnum>(SourceId.FAMILIAR.desc(), SourceId.FAMILIAR));
        filterData.addItem(new android.util.Pair<String, ValueEnum>(SourceId.DIANPING.desc(), SourceId.DIANPING));
        filterData.addItem(new android.util.Pair<String, ValueEnum>(SourceId.XIN_MEI_DA.desc(), SourceId.XIN_MEI_DA));
        filterData.addItem(new android.util.Pair<String, ValueEnum>(SourceId.KOU_BEI.desc(), SourceId.KOU_BEI));
        filterData.addItem(new android.util.Pair<String, ValueEnum>(SourceId.KIOSK_ANDROID.desc(), SourceId.KIOSK_ANDROID));
        filterData.addItem(new android.util.Pair<String, ValueEnum>(SourceId.OPEN_PLATFORM.desc(), SourceId.OPEN_PLATFORM));

        filterDatas.add(filterData);
        return filterDatas;
    }

    @Override
    public void countOrder() {
        countOrder(DbQueryConstant.UNPROCESSED_NEW_ORDER);
        countOrder(DbQueryConstant.UNPROCESSED_CANCEL_REQUEST);
    }

    @Override
    public boolean isShowKoubeiVerification() {
        return true;
    }
}
