package com.zhongmei.bty.cashier.ordercenter.presenter;

import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.zhongmei.bty.basemodule.auth.application.FastFoodApplication;
import com.zhongmei.bty.basemodule.commonbusiness.entity.PartnerShopBiz;
import com.zhongmei.bty.basemodule.commonbusiness.operates.SystemSettingDal;
import com.zhongmei.bty.basemodule.database.utils.DbQueryConstant;
import com.zhongmei.bty.basemodule.session.core.user.UserFunc;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.message.BatchDeliveryFee;
import com.zhongmei.bty.basemodule.trade.message.BindOrderResp;
import com.zhongmei.bty.basemodule.trade.message.DeliveryOrderDispatchReq;
import com.zhongmei.bty.basemodule.trade.message.DeliveryOrderDispatchResp;
import com.zhongmei.bty.basemodule.trade.message.DeliveryOrderListReq;
import com.zhongmei.bty.basemodule.trade.message.DeliveryOrderListResp;
import com.zhongmei.bty.basemodule.trade.message.TradePaymentResp;
import com.zhongmei.bty.cashier.ordercenter.bean.FilterData;
import com.zhongmei.bty.cashier.ordercenter.manager.SnackOrderCenterListManager;
import com.zhongmei.bty.cashier.ordercenter.util.OrderCenterBeanConverter;
import com.zhongmei.bty.cashier.ordercenter.view.IOrderCenterListView;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.database.enums.DeliveryOrderStatus;
import com.zhongmei.bty.commonmodule.event.OrderRefreshClickEvent;
import com.zhongmei.bty.commonmodule.event.SideMenuClickEvent;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.bty.data.db.common.OrderNotify;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.context.util.Gsons;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.DeliveryPlatform;
import com.zhongmei.yunfu.db.enums.DeliveryType;
import com.zhongmei.yunfu.db.enums.SourceId;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.resp.data.GatewayTransferResp;
import com.zhongmei.yunfu.util.ValueEnum;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;



public class SnackOrderCenterListPresenter extends OrderCenterListPresenter {


    public SnackOrderCenterListPresenter(IOrderCenterListView view) {
        super(view, new SnackOrderCenterListManager());
        registerEventBus();
    }

    @Override
    public boolean showBackButton() {
        return true;
    }

    @Override
    public boolean showMenuButton() {
        return true;
    }

    @Override
    public void openSideMenu() {
        EventBus.getDefault().post(new SideMenuClickEvent());
    }

    @Override
    public boolean showOrderRefreshButton() {
        return true;
    }

    @Override
    public void openNotifyCenter() {
        EventBus.getDefault().post(new OrderRefreshClickEvent());
    }

    @Override
    public boolean showNotifyCenterTip() {
        return true;
    }

    @Override
    public void destroy() {
        unregisterEventBus();
    }

    @Override
    public void notifyVoice(int type, String tradeUuid, String mobile, String serialNo, String tradeNo) {
        ResponseListener<OrderNotify> listener = new ResponseListener<OrderNotify>() {

            @Override
            public void onResponse(ResponseObject<OrderNotify> response) {
                mView.showToast(response.getMessage());
            }

            @Override
            public void onError(VolleyError error) {
                mView.showToast(error.getMessage());
            }

        };

        mManager.notifyVoice(type, tradeUuid, mobile, serialNo, tradeNo, LoadingResponseListener.ensure(listener, mView.getViewFragmentManager()));
    }

    @Override
    public void deliveryPayment(final List<TradeVo> tradeVos) {
        if (Utils.isEmpty(tradeVos)) {
            return;
        }

        final ResponseListener<TradePaymentResp> listener = new ResponseListener<TradePaymentResp>() {
            @Override
            public void onResponse(ResponseObject<TradePaymentResp> response) {
                mView.showToast(response.getMessage());
                mView.squareAccountFinish();
            }

            @Override
            public void onError(VolleyError error) {
                mView.showToast(error.getMessage());
            }
        };
        VerifyHelper.verifyAlert(mView.getViewActivity(), FastFoodApplication.PERMISSION_FASTFOOD_CASH,
                new VerifyHelper.Callback() {
                    @Override
                    public void onPositive(User user, String code, Auth.Filter filter) {
                        super.onPositive(user, code, filter);
                        mManager.deliveryPayment(tradeVos, LoadingResponseListener.ensure(listener, mView.getViewFragmentManager()));
                    }
                });
    }

    @Override
    public void bindDeliveryUser(List<TradeVo> tradeVos, User authUser) {
        ResponseListener<BindOrderResp> listener = LoadingResponseListener.ensure(new ResponseListener<BindOrderResp>() {
            @Override
            public void onResponse(ResponseObject<BindOrderResp> response) {
                if (ResponseObject.isOk(response)) {
                    List<BindOrderResp.ResultInfo> resultInfos = response.getContent().getResultInfos();
                    StringBuilder stringBuilder = new StringBuilder();
                    if (Utils.isNotEmpty(resultInfos)) {
                        int size = resultInfos.size();
                        for (int i = 0; i < size; i++) {
                            BindOrderResp.ResultInfo resultInfo = resultInfos.get(i);
                            Integer resultStatus = resultInfo.getResultStatus();
                            if (resultStatus != null && resultStatus == 2) {
                                stringBuilder.append(resultInfo.getResultMessage());
                                if (i < size - 1) {
                                    stringBuilder.append(";");
                                    stringBuilder.append("\n");
                                }
                            }
                        }
                    }

                    if (!TextUtils.isEmpty(stringBuilder.toString())) {
                        mView.showToast(stringBuilder.toString());
                    } else {
                        mView.showToast(response.getMessage());
                        mView.batchBindDeliveryUserFinish();
                    }
                } else {
                    mView.showToast(response.getMessage());
                }
            }

            @Override
            public void onError(VolleyError error) {
                mView.showToast(error.getMessage());
            }
        }, mView.getViewFragmentManager());
        mManager.bindDeliveryUser(tradeVos, authUser, listener);
    }

    @Override
    public void addProcessTab(List<Pair<String, Integer>> processTab) {
        addChildTab(processTab, R.string.unprocessed_all, DbQueryConstant.UNPROCESSED_ALL);
        addChildTab(processTab, R.string.unprocessed_new_order, DbQueryConstant.UNPROCESSED_NEW_ORDER);
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
        filterData.addItem(new android.util.Pair<String, ValueEnum>(SourceId.POS.desc(), SourceId.POS));
        filterData.addItem(new android.util.Pair<String, ValueEnum>(SourceId.WECHAT.desc(), SourceId.WECHAT));
        filterData.addItem(new android.util.Pair<String, ValueEnum>(SourceId.KIOSK.desc(), SourceId.KIOSK));
        filterData.addItem(new android.util.Pair<String, ValueEnum>(SourceId.KOU_BEI.desc(), SourceId.KOU_BEI));
        filterData.addItem(new android.util.Pair<String, ValueEnum>(SourceId.ELEME.desc(), SourceId.ELEME));
        filterData.addItem(new android.util.Pair<String, ValueEnum>(SourceId.MEITUAN_TAKEOUT.desc(), SourceId.MEITUAN_TAKEOUT));
        filterData.addItem(new android.util.Pair<String, ValueEnum>(SourceId.BAIDU_TAKEOUT.desc(), SourceId.BAIDU_TAKEOUT));
        filterData.addItem(new android.util.Pair<String, ValueEnum>(SourceId.ON_MOBILE.desc(), SourceId.ON_MOBILE));
        filterData.addItem(new android.util.Pair<String, ValueEnum>(SourceId.FAMILIAR.desc(), SourceId.FAMILIAR));
        filterData.addItem(new android.util.Pair<String, ValueEnum>(SourceId.OPEN_PLATFORM.desc(), SourceId.OPEN_PLATFORM));
        filterDatas.add(filterData);

        if (!mView.isInSquareAccountMode()) {            filterData = new FilterData(getString(R.string.delivery_status));
                        filterData.addItem(new android.util.Pair<String, ValueEnum>(getString(R.string.order_center_detail_waiting_accept), DeliveryOrderStatus.WAITING_ACCEPT));
            filterData.addItem(new android.util.Pair<String, ValueEnum>(getString(R.string.order_center_detail_waiting_pick_up), DeliveryOrderStatus.WAITING_PICK_UP));
            filterData.addItem(new android.util.Pair<String, ValueEnum>(getString(R.string.order_center_detail_deliverying), DeliveryOrderStatus.DELIVERYING));
            filterData.addItem(new android.util.Pair<String, ValueEnum>(getString(R.string.order_center_detail_real_delivery), DeliveryOrderStatus.REAL_DELIVERY));
            filterData.addItem(new android.util.Pair<String, ValueEnum>(getString(R.string.order_center_detail_delivery_cancel), DeliveryOrderStatus.DELIVERY_CANCEL));
            filterDatas.add(filterData);
        }
        return filterDatas;
    }

    @Override
    public void batchBindDeliveryUser() {
        new AsyncTask<Void, Void, List<User>>() {
            @Override
            protected List<User> doInBackground(Void... params) {
                try {
                    return Session.getFunc(UserFunc.class).getUsers(FastFoodApplication.PERMISSION_FASTFOOD_SC);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(List<User> authUsers) {
                if (Utils.isNotEmpty(authUsers)) {
                    mView.showDeliveryUserChooseDialog(authUsers);
                } else {
                    mView.showToast(getString(R.string.order_center_no_delivery_user));
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void showDeliveryPlatformChoose(List<TradeVo> tradeVos) {
        new AsyncTask<List<TradeVo>, Void, List<PartnerShopBiz>>() {
            @Override
            protected List<PartnerShopBiz> doInBackground(List<TradeVo>[] tradeVos) {
                try {
                    SystemSettingDal systemSettingDal = OperatesFactory.create(SystemSettingDal.class);
                                        List<PartnerShopBiz> deliveryPlatformPartnerShopBizs = systemSettingDal.queryPartnerShopBiz(3, true);                    Map<Integer, PartnerShopBiz> deliveryPlatformPartnerShopBizMap = new HashMap<>();
                    if (Utils.isNotEmpty(deliveryPlatformPartnerShopBizs)) {
                        for (PartnerShopBiz partnerShopBiz : deliveryPlatformPartnerShopBizs) {
                            deliveryPlatformPartnerShopBizMap.put(partnerShopBiz.getSource(), partnerShopBiz);
                        }
                    }
                    for (TradeVo tradeVo : tradeVos[0]) {
                        Trade trade = tradeVo.getTrade();
                                                if (trade.getSource() != SourceId.ELEME) {
                            deliveryPlatformPartnerShopBizMap.remove(DeliveryPlatform.ELEME_ZHONGBAO.value());
                        }
                                                if (trade.getSource() != SourceId.MEITUAN_TAKEOUT) {
                            deliveryPlatformPartnerShopBizMap.remove(DeliveryPlatform.MEITUAN_ZHONGBAO.value());
                        }
                    }
                    return new ArrayList<>(deliveryPlatformPartnerShopBizMap.values());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return new ArrayList<>();
            }

            @Override
            protected void onPostExecute(List<PartnerShopBiz> partnerShopBizs) {
                if (Utils.isNotEmpty(partnerShopBizs)) {
                    mView.showDeliveryPlatformChooseView(partnerShopBizs);
                } else {
                    mView.showToast(getString(R.string.order_center_no_can_use_deliver_platform));
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, tradeVos);
    }


    @Override
    public void prepareDeliveryOrderDispatch(final @NotNull List<TradeVo> tradeVos, final @NotNull PartnerShopBiz partnerShopBiz) {
        if (partnerShopBiz.isSupportDeliveryFee()) {
            mManager.batchQueryDeliveryFee(OrderCenterBeanConverter.createBatchQueryDeliveryFeeReq(tradeVos, partnerShopBiz)
                    , LoadingResponseListener.ensure(new ResponseListener<GatewayTransferResp<JsonArray>>() {
                        @Override
                        public void onResponse(ResponseObject<GatewayTransferResp<JsonArray>> response) {
                            if (ResponseObject.isOk(response)) {
                                if (response.getContent().isOk()) {
                                    Gson gson = Gsons.gsonBuilder().create();
                                    Type listType = new TypeToken<List<BatchDeliveryFee>>() {
                                    }.getType();
                                    List<BatchDeliveryFee> deliveryFees = gson.fromJson(response.getContent().getResult().toString(), listType);
                                    if (Utils.isNotEmpty(deliveryFees) && deliveryFees.size() == tradeVos.size()) {
                                        mView.showDeliveryFeeView(tradeVos, deliveryFees, partnerShopBiz);
                                    }
                                } else {
                                    mView.showToast(response.getContent().getMessage());
                                }
                            } else {
                                mView.showToast(response.getMessage());
                            }
                        }

                        @Override
                        public void onError(VolleyError error) {
                            mView.showToast(error.getMessage());
                        }
                    }, mView.getViewFragmentManager()));

        } else {
            deliveryOrderDispatch(tradeVos, partnerShopBiz, null);
        }
    }

    @Override
    public void deliveryOrderDispatch(final List<TradeVo> tradeVos, PartnerShopBiz partnerShopBiz, List<BatchDeliveryFee> deliveryFees) {
        final DeliveryOrderDispatchReq req = OrderCenterBeanConverter.createDeliveryOrderDispatchReq(tradeVos, deliveryFees, partnerShopBiz.getSource());
        mManager.deliveryOrderDispatch(req, LoadingResponseListener.ensure(new ResponseListener<GatewayTransferResp<DeliveryOrderDispatchResp>>() {
            @Override
            public void onResponse(final ResponseObject<GatewayTransferResp<DeliveryOrderDispatchResp>> response) {
                if (ResponseObject.isOk(response)) {
                    if (response.getContent().isOk()) {                        List<DeliveryOrderDispatchResp.FailOrder> failOrders = response.getContent().getResult().getFailOrders();
                        if (Utils.isNotEmpty(failOrders)) {
                            mView.showDispatchFailOrderListAlert(OrderCenterBeanConverter.listDispatchFailOrder(tradeVos, failOrders));
                        } else {
                            mView.showToast(response.getContent().getMessage());
                        }

                        DeliveryOrderListReq req = OrderCenterBeanConverter.createDeliveryOrderListReq(tradeVos, failOrders);
                        mManager.deliveryOrderList(req, LoadingResponseListener.ensure(new ResponseListener<GatewayTransferResp<DeliveryOrderListResp>>() {
                            @Override
                            public void onResponse(ResponseObject<GatewayTransferResp<DeliveryOrderListResp>> response1) {
                                if (ResponseObject.isOk(response1)) {
                                    mView.batchBindDeliveryUserFinish();
                                } else {
                                    mView.showToast(response1.getMessage());
                                }
                            }

                            @Override
                            public void onError(VolleyError error) {
                                mView.showToast(error.getMessage());
                            }
                        }, mView.getViewFragmentManager()));
                    } else if (response.getContent().getCode() == 2000) {                        List<DeliveryOrderDispatchResp.FailOrder> failOrders = response.getContent().getResult().getFailOrders();
                        mView.showDispatchFailOrderListAlert(OrderCenterBeanConverter.listDispatchFailOrder(tradeVos, failOrders));
                    } else {
                        mView.showToast(response.getContent().getMessage());
                    }
                } else {
                    mView.showToast(response.getMessage());
                }
            }

            @Override
            public void onError(VolleyError error) {
                mView.showToast(error.getMessage());
            }
        }, mView.getViewFragmentManager()));
    }

    @Override
    public boolean isShowKoubeiVerification() {
        return mManager.isOpenKoubeiBiz();
    }
}
