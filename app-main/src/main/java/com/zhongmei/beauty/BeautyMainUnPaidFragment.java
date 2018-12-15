package com.zhongmei.beauty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.zhongmei.beauty.adapter.UnpaidTradeAdapter;
import com.zhongmei.beauty.adapter.UnpaidTradeItemView;
import com.zhongmei.beauty.entity.UnpaidTradeVo;
import com.zhongmei.beauty.operates.BeautyNotifyCache;
import com.zhongmei.beauty.operates.BeautyOperates;
import com.zhongmei.beauty.operates.BeautyTradeDataManager;
import com.zhongmei.beauty.operates.message.BeautyTradeResp;
import com.zhongmei.beauty.ordercenter.BeautyOrderCenterOperateDialogFragment;
import com.zhongmei.beauty.utils.BeautyOrderConstants;
import com.zhongmei.bty.basemodule.auth.application.BeautyApplication;
import com.zhongmei.bty.basemodule.auth.permission.manager.AuthLogManager;
import com.zhongmei.bty.basemodule.commonbusiness.enums.ReasonType;
import com.zhongmei.bty.basemodule.inventory.message.InventoryItemReq;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.bty.basemodule.trade.bean.Reason;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.database.enums.OrderActionEnum;
import com.zhongmei.bty.entity.enums.InventoryShowType;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.monitor.CalmResponseListener;
import com.zhongmei.yunfu.net.builder.NetError;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.yunfu.ui.view.recycler.RecyclerLinearLayoutManager;
import com.zhongmei.yunfu.util.ToastUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 */
@EFragment(R.layout.beauty_main_unpaid_fragment)
public class BeautyMainUnPaidFragment extends BasicFragment implements UnpaidTradeItemView.OnOperateListener, BeautyNotifyCache.BeautyDataListener {

    @ViewById(R.id.iv_empty_view)
    protected View mIvEmptyView;

    @ViewById(R.id.lv_trades)
    protected RecyclerView mTradesListView;

    private BeautyTradeDataManager mTradeManager = null;

    private BeautyNotifyCache mBeautyNotifyCache;

    @Bean
    protected UnpaidTradeAdapter mUnpaidTradeAdapter;

    @AfterViews
    protected void init() {
        mTradeManager = new BeautyTradeDataManager();

        final LinearLayoutManager manager = new RecyclerLinearLayoutManager(getActivity());
        mTradesListView.setLayoutManager(manager);

        mUnpaidTradeAdapter.setOperateListener(this);
        mTradesListView.setAdapter(mUnpaidTradeAdapter);

        initData();//初始化数据

    }

    private void initData() {
        mBeautyNotifyCache = BeautyNotifyCache.getInstance();
        mBeautyNotifyCache.setmBeautyDataListener(this);
        mBeautyNotifyCache.startModuleCache(BeautyNotifyCache.MODULE_TRADES);
    }

    /**
     * 订单删除
     *
     * @param unPaidradeVo
     */
    @Override
    public void tradeDelete(final UnpaidTradeVo unPaidradeVo) {
        VerifyHelper.verifyAlert(getActivity(), BeautyApplication.PERMISSION_BEAUTY_INVALID, new VerifyHelper.Callback() {
            @Override
            public void onPositive(User user, String code, Auth.Filter filter) {
                super.onPositive(user, code, filter);
                tradeInvalid(unPaidradeVo);
            }
        });


    }

    private void tradeInvalid(UnpaidTradeVo unPaidradeVo) {
        try {
            TradeDal tradeDal = OperatesFactory.create(TradeDal.class);
            final TradeVo tradeVo = tradeDal.findTrade(unPaidradeVo.getTradeId());
            BeautyOrderCenterOperateDialogFragment dialog = new BeautyOrderCenterOperateDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("type", ReasonType.TRADE_INVALID.value().intValue());
            dialog.setmInventoryStyle(InventoryShowType.ONLY_SHOW_INVENTORY.value());
            dialog.setArguments(bundle);
            dialog.registerListener(new BeautyOrderCenterOperateDialogFragment.OperateListener() {
                @Override
                public boolean onSuccess(BeautyOrderCenterOperateDialogFragment.OperateResult result) {
                    return disposeTheRequest(tradeVo, result);
                }

            });

            String tradeUuid = tradeVo.getTrade().getUuid();
            dialog.setPayment(mTradeManager.getPaymentVo(tradeUuid));

            dialog.setTradeVo(tradeVo);
            dialog.show(getFragmentManager(), "destroy");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean disposeTheRequest(final TradeVo tradeVo, BeautyOrderCenterOperateDialogFragment.OperateResult result) {
        UserActionEvent.start(UserActionEvent.DINNER_DEL_ORDER);
        Reason reason = result.reason;
        boolean isPrintChecked = result.isPrintChecked;
        List<InventoryItemReq> inventoryItems = result.returnInventoryItemReqs;
        deleteTrade(tradeVo, reason, inventoryItems, isPrintChecked);
        return true;
    }

    private void deleteTrade(TradeVo tradeVo, Reason reason, List<InventoryItemReq> inventoryItems, final boolean isPrintChecked) {
        CalmResponseListener<ResponseObject<BeautyTradeResp>> listener = new CalmResponseListener<ResponseObject<BeautyTradeResp>>() {
            @Override
            public void onError(NetError error) {
                ToastUtil.showLongToast(error.getVolleyError().getMessage());
            }

            @Override
            public void onSuccess(ResponseObject<BeautyTradeResp> resp) {
                if (!ResponseObject.isOk(resp)) {
                    String msg = resp.getMessage();
                    if (!TextUtils.isEmpty(msg)) {
                        ToastUtil.showShortToast(msg);
                    } else {
                        ToastUtil.showShortToast(R.string.dinner_recision_failed);
                    }
                } else {
                    // 打印作废单
                    Trade trade = resp.getContent().getTrade();
                    if (trade != null) {
                        String uuid = trade.getUuid();
//                            PRTPrintOperator operator = new PRTPrintOperator();
//                            operator.printCancelTicket(uuid, null, result.isPrintChecked, false);
                        //IPrintHelper.Holder.getInstance().printCancelTicket(uuid, null, false, new PRTOnSimplePrintListener(PrintTicketTypeEnum.CANCEL));
                        AuthLogManager.getInstance().flush(OrderActionEnum.ACTION_CANCEL_ORDER, trade.getId(), trade.getUuid(), trade.getClientUpdateTime());
                    }
                }
            }
        };

        Long tradeId = tradeVo.getTrade().getId();
        Long serverUpdateTime = tradeVo.getTrade().getServerUpdateTime();
        BeautyOperates dal = OperatesFactory.create(BeautyOperates.class);

        dal.deleteTrade(tradeId,
                serverUpdateTime,
                reason, inventoryItems, listener, this);
    }

    /**
     * 订单进入
     *
     * @param beautyTradeVo
     */
    @Override
    public void tradeDetilas(UnpaidTradeVo beautyTradeVo) {
        showOrderDish(beautyTradeVo.getTradeId());
    }

    private void showEmptyView(boolean show) {
        if (show) {
            mIvEmptyView.setVisibility(View.VISIBLE);
            mTradesListView.setVisibility(View.GONE);
        } else {
            mIvEmptyView.setVisibility(View.GONE);
            mTradesListView.setVisibility(View.VISIBLE);
        }
    }


    public void showOrderDish(Long tradeId) {
        Intent mIntent = new Intent();
        mIntent.setClass(getActivity(), BeautyOrderActivity.class);
        mIntent.putExtra(BeautyOrderConstants.IS_ORDER_EDIT, true);
        mIntent.putExtra(BeautyOrderConstants.ORDER_EDIT_TRADEID, tradeId);
        startActivity(mIntent);
    }

    @Override
    public void refreshReserverTrade() {

    }

    @UiThread
    @Override
    public void refreshTrade(List<UnpaidTradeVo> beautyTradeVos) {
        if (mUnpaidTradeAdapter != null) {
            mUnpaidTradeAdapter.setItems(beautyTradeVos);
            mUnpaidTradeAdapter.notifyDataSetChanged();
        }

        showEmptyView(Utils.isEmpty(beautyTradeVos));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBeautyNotifyCache != null) {
            mBeautyNotifyCache.setmBeautyDataListener(null);
        }
    }
}
