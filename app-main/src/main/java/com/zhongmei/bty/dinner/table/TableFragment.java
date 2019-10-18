package com.zhongmei.bty.dinner.table;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.zhongmei.bty.basemodule.auth.application.DinnerApplication;
import com.zhongmei.bty.basemodule.booking.event.EventRefreshReserveNotice;
import com.zhongmei.bty.basemodule.commonbusiness.enums.ReasonType;
import com.zhongmei.bty.basemodule.customer.entity.RejectReason;
import com.zhongmei.bty.basemodule.database.enums.TicketTypeEnum;
import com.zhongmei.bty.basemodule.database.utils.DbQueryConstant;
import com.zhongmei.bty.basemodule.inventory.message.InventoryItemReq;
import com.zhongmei.bty.basemodule.notifycenter.event.ActionShowTrade;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.bty.basemodule.orderdish.fragment.DishQuantityAndSeatEditDialogFragment;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeInfo;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeModel;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeModelMoveDish;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeVo;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertable;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertableTrade;
import com.zhongmei.bty.basemodule.trade.bean.IDinnertableTradeMoveDish;
import com.zhongmei.bty.basemodule.trade.bean.IZone;
import com.zhongmei.bty.basemodule.trade.bean.Reason;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.cache.DinnertableStateCache;
import com.zhongmei.bty.basemodule.trade.constants.DinnerTableConstant;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.bty.cashier.ordercenter.dialog.VerificationDialog;
import com.zhongmei.bty.cashier.ordercenter.dialog.VerificationDialog_;
import com.zhongmei.bty.cashier.ordercenter.view.OrderCenterOperateDialogFragment;
import com.zhongmei.bty.cashier.ordercenter.view.OrderCenterOperateDialogFragment.OperateListener;
import com.zhongmei.bty.cashier.ordercenter.view.OrderCenterOperateDialogFragment.OperateResult;
import com.zhongmei.bty.cashier.util.TradeSourceUtils;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.database.entity.local.AsyncHttpRecord;
import com.zhongmei.bty.commonmodule.database.enums.AsyncHttpState;
import com.zhongmei.bty.commonmodule.util.DinnerMobClickAgentEvent;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.dinner.shopcart.adapter.DinnerShopCartAdapter;
import com.zhongmei.bty.dinner.table.event.EventFunctionChange;
import com.zhongmei.bty.dinner.table.event.EventInsertDinnerNotice;
import com.zhongmei.bty.dinner.table.event.EventRefreshDinnertableNotice;
import com.zhongmei.bty.dinner.table.event.EventRefreshOpenTableAsync;
import com.zhongmei.bty.dinner.table.event.EventZoneSwitchNotice;
import com.zhongmei.bty.dinner.table.event.EventZonesNotice;
import com.zhongmei.bty.dinner.table.manager.DinnertableManager;
import com.zhongmei.bty.dinner.table.model.DinnertableModel;
import com.zhongmei.bty.dinner.table.model.ZoneModel;
import com.zhongmei.bty.dinner.table.view.BatchOperationTableDialogFragment;
import com.zhongmei.bty.dinner.table.view.LeisureTableDialogFragment;
import com.zhongmei.bty.dinner.table.view.MoreFunctionPopWindow;
import com.zhongmei.bty.dinner.table.view.OnControlListener;
import com.zhongmei.bty.dinner.table.view.OpentablePopWindow;
import com.zhongmei.bty.dinner.table.view.TableFragmentBase;
import com.zhongmei.bty.dinner.table.view.TableViewBase;
import com.zhongmei.bty.dinner.table.view.ZoneContentView;
import com.zhongmei.bty.dinner.table.view.ZoneView;
import com.zhongmei.bty.dinner.table.view.ZonesIndicator;
import com.zhongmei.bty.dinner.util.UnionUtil;
import com.zhongmei.bty.dinner.vo.SwitchTableTradeVo;
import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.context.util.ThreadUtils;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.enums.DeliveryType;
import com.zhongmei.yunfu.db.enums.DishType;
import com.zhongmei.yunfu.db.enums.SourceId;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment.CommonDialogFragmentBuilder;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.util.DialogUtil;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.util.ToastUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.greenrobot.event.EventBus;


@EFragment(R.layout.dinnertable_manager)
public abstract class TableFragment extends TableFragmentBase {

    private static final String TAG = TableFragment.class.getSimpleName();


    private static final String SP_DINNERTABLE_ZONE_ID = "dinnertable.zone.id";


    private static final int REFRESH_SPEND_TIME_INTERVAL = 60 * 1000;

    public static final String DIALOG_TAG = "dinnertable_dialog";

    public static final String SHOW_ISSUE_TIME_KEY = "show_issue_time_key";

    public static final String SHOW_PRECASH_KEY = "show_pre_cash_key";


    public static Drawable defaultJoinDrawable;
    public static Drawable defaultAmountDrawable;
    public static Drawable defaultTimeDrawable;
    public static Drawable defaultSerialDrawable;
    public static Drawable unissueJoinDrawable;
    public static Drawable unissueAmountDrawable;
    public static Drawable unissueTimeDrawable;
    public static Drawable unissueSerialDrawable;
    public static Drawable issuedJoinDrawable;
    public static Drawable issuedAmountDrawable;
    public static Drawable issuedTimeDrawable;
    public static Drawable issuedSerialDrawable;
    public static Drawable servedJoinDrawable;
    public static Drawable servedAmountDrawable;
    public static Drawable servedTimeDrawable;
    public static Drawable servedSerialDrawable;

    @ViewById(R.id.zone_view)
    protected ZoneView mZoneView;

    @ViewById(R.id.zones_indicator)
    protected ZonesIndicator mZonesIndicator;

    @ViewById(R.id.dinner_status_serving)
    protected View statusServingView;

    @ViewById(R.id.dinner_status_done)
    protected View statusDoneView;


    @ViewById(R.id.show_money_rb)
    protected CheckBox showMoneyRb;

    @ViewById(R.id.layout_moreFuntion)
    protected LinearLayout layout_moreFunction;

    @ViewById(R.id.tv_leisure_table)
    protected TextView mTvLeisureTable;

    @ViewById(R.id.tv_moreFunction)
    protected TextView tv_moreFunction;

    @ViewById(R.id.tv_join_trade)
    protected TextView tvJoinTrade;

    protected TableInfoFragment infoFragment;

    protected DinnertableManager mManager;

    private Handler handler;

    private View shadowView;

    private ZoneModel zoneModel;

    private TableViewBase opentableDinnertableView;
    private MoreFunctionPopWindow moreFunctionPop;

    private String uuid;

    protected SwitchTableTradeVo mSwitchTableTradeVo = null;

    private OnControlListener mOnControlListener = new OnControlListener() {

        @Override
        public void onSwitchZone(IZone zone) {
            MobclickAgentEvent.onEvent(getActivity(), DinnerMobClickAgentEvent.tableAreaSwitch);
            mManager.switchZone(zone);
        }

        @Override
        public boolean onSelect(final DinnertableModel model, final TableViewBase dinnertableView) {
            return onTableSelect(model, dinnertableView);
        }

        @Override
        public void onMerge(final IDinnertableTrade orginal, final IDinnertableTrade dest) {
            MobclickAgentEvent.onEvent(getActivity(), DinnerMobClickAgentEvent.tableMergeTrade);
            mergeTrade(orginal, dest);
        }

        @Override
        public void onTransfer(final IDinnertableTrade orginal, final IDinnertable dest) {
            MobclickAgentEvent.onEvent(getActivity(), DinnerMobClickAgentEvent.tableTransferTable);
            transferTable(orginal, dest);
        }

        @Override
        public void onDelete(final IDinnertableTrade dinnertableTrade) {
            MobclickAgentEvent.onEvent(getActivity(), DinnerMobClickAgentEvent.tableDetailsDelete);
            abolishTrade(dinnertableTrade);
        }

        @Override
        public void onShowControl(IDinnertable dinnertable) {
            Log.e("TAG", "onShowControl.....>");
        }

        @Override
        public void onHideControl() {
            mManager.select(null);
            mZoneView.setSelectState(null);
        }


        @Override
        public void onTransferMoveDish(final IDinnertableTradeMoveDish orginal, final IDinnertable dest) {
            VerifyHelper.verifyAlert(getActivity(), DinnerApplication.PERMISSION_DINNER_CREATE,
                    new VerifyHelper.Callback() {
                        @Override
                        public void onPositive(User user, String code, Auth.Filter filter) {
                            super.onPositive(user, code, filter);
                            doTransferMoveDish(orginal, dest);
                        }
                    });
        }


        @Override
        public void onMergeMoveDish(final IDinnertableTradeMoveDish orginal, final IDinnertableTrade dest) {
            VerifyHelper.verifyAlert(getActivity(), DinnerApplication.PERMISSION_DINNER_CREATE,
                    new VerifyHelper.Callback() {
                        @Override
                        public void onPositive(User user, String code, Auth.Filter filter) {
                            super.onPositive(user, code, filter);
                            doMergeMoveDish(orginal, dest);
                        }
                    });
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        defaultJoinDrawable = getResources().getDrawable(R.drawable.dinnertable_join_default);
        defaultAmountDrawable = getResources().getDrawable(R.drawable.dinnertable_amount_default);
        defaultTimeDrawable = getResources().getDrawable(R.drawable.dinnertable_time_default);
        defaultSerialDrawable = getResources().getDrawable(R.drawable.dinnertable_serial_default);
        unissueJoinDrawable = getResources().getDrawable(R.drawable.dinnertable_join_unissue);
        unissueAmountDrawable = getResources().getDrawable(R.drawable.dinnertable_amount_unissue);
        unissueTimeDrawable = getResources().getDrawable(R.drawable.dinnertable_time_unissue);
        unissueSerialDrawable = getResources().getDrawable(R.drawable.dinnertable_serial_unissue);
        issuedJoinDrawable = getResources().getDrawable(R.drawable.dinnertable_join_issue);
        issuedAmountDrawable = getResources().getDrawable(R.drawable.dinnertable_amount_issued);
        issuedTimeDrawable = getResources().getDrawable(R.drawable.dinnertable_time_issued);
        issuedSerialDrawable = getResources().getDrawable(R.drawable.dinnertable_serial_issued);
        servedJoinDrawable = getResources().getDrawable(R.drawable.dinnertable_join_served);
        servedAmountDrawable = getResources().getDrawable(R.drawable.dinnertable_amount_served);
        servedTimeDrawable = getResources().getDrawable(R.drawable.dinnertable_time_served);
        servedSerialDrawable = getResources().getDrawable(R.drawable.dinnertable_serial_served);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected boolean onTableSelect(final DinnertableModel model, final TableViewBase dinnertableView) {
        if (model.isEmpty() && model.isDone()) {
            MobclickAgentEvent.onEvent(getActivity(), DinnerMobClickAgentEvent.tableClean);
            VerifyHelper.verifyAlert(getActivity(), DinnerApplication.PERMISSION_DINNER_CREATE,
                    new VerifyHelper.Callback() {
                        @Override
                        public void onPositive(User user, String code, Auth.Filter filter) {
                            super.onPositive(user, code, filter);
                            doClearTable(model);
                        }
                    });
            return false;
        } else if (model.isEmpty()) {
            if (model.asyncOpeningTable()) {
                ToastUtil.showShortToast(getString(R.string.dinner_table_opening_table));
                return false;
            }


            MobclickAgentEvent.onEvent(getActivity(), DinnerMobClickAgentEvent.tableCreateTrade);
            OpentablePopWindow opentablePopWindow = OpentablePopWindow.getInstance(getActivity());
            opentablePopWindow.setModel(model);
            opentablePopWindow.setmBusinessType(getBussinessType());
            opentablePopWindow.show(dinnertableView);
            dinnertableView.showOpenTableButton(true);
            opentableDinnertableView = dinnertableView;

            opentablePopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    opentableDinnertableView.showOpenTableButton(false);
                }
            });
            return true;

        } else {                        MobclickAgentEvent.onEvent(getActivity(), DinnerMobClickAgentEvent.tableDetails);
            infoFragment.event = null;            new Thread(new Runnable() {

                @Override
                public void run() {
                    mManager.select(model);
                }
            }).start();

            mZoneView.setSelectState(model.getId());
            return true;
        }
    }

    protected void checkMergeOrTransfer(String orginalTradeUuid, List<TradeItem> tradeItems, Long destTableId, TicketTypeEnum ticketType, final Runnable runnable) {
        TradeDal tradeDal = OperatesFactory.create(TradeDal.class);
        try {
            if (Utils.isEmpty(tradeItems)) {
                tradeItems = tradeDal.listTradeItemByTradeUuid(orginalTradeUuid);
            }
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        if (Utils.isNotEmpty(tradeItems)) {
                        for (int i = tradeItems.size() - 1; i >= 0; i--) {
                TradeItem tradeItem = tradeItems.get(i);
                if (tradeItem.getType() != DishType.SINGLE && tradeItem.getType() != DishType.COMBO) {
                    tradeItems.remove(i);
                }
            }


            runnable.run();
                    } else {
            runnable.run();
        }
    }

    private Runnable refreshSpendTimeRunnable = new Runnable() {

        @Override
        public void run() {
            mZoneView.refreshSpendTime();
            handler.postDelayed(refreshSpendTimeRunnable, REFRESH_SPEND_TIME_INTERVAL);
        }

    };

    protected void doMerge(final IDinnertableTrade orginal, final IDinnertableTrade dest) {
        if (orginal.getTradeStatus() == TradeStatus.UNPROCESSED || dest.getTradeStatus() == TradeStatus.UNPROCESSED) {
            ToastUtil.showLongToast(R.string.dinner_table_merge_unprocess_toast);
            return;
        }
                DatabaseHelper helper = null;
        try {
            helper = DBHelperManager.getHelper();
            Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
            Trade destTrade = tradeDao.queryForId(dest.getTradeUuid());
            if (TradeSourceUtils.isTradePayedAndConfirmed(destTrade, SourceId.DIANPING)) {
                ToastUtil.showLongToast(R.string.dinner_table_merge_dianping_payed_toast);
                return;
            }
                        if (TradeSourceUtils.isTradePayedAndConfirmed(destTrade, SourceId.XIN_MEI_DA)) {
                ToastUtil.showLongToast(R.string.dinner_table_merge_xinmeida_payed_toast);
                return;
            }
                        if (TradeSourceUtils.isTradePayedAndConfirmed(destTrade, SourceId.KOU_BEI)) {
                ToastUtil.showLongToast(R.string.dinner_table_merge_koubei_payed_toast);
                return;
            }
                        if (TradeSourceUtils.isTradePayedAndConfirmed(destTrade, SourceId.BAIDU_RICE)) {
                ToastUtil.showLongToast(R.string.dinner_table_merge_baidurice_payed_toast);
                return;
            }
                        if (TradeSourceUtils.isTradePayedAndConfirmed(destTrade, SourceId.FAMILIAR)) {
                ToastUtil.showLongToast(R.string.dinner_table_merge_shuke_payed_toast);
                return;
            }

                        AsyncHttpRecord record = ((DinnertableTradeModel) dest).getAsyncHttpRecord();
            if (record != null && record.getStatus() != AsyncHttpState.SUCCESS) {
                ToastUtil.showLongToast(R.string.dinner_table_merge_async_http_toast);
                return;
            }

            Trade originalTrade = tradeDao.queryForId(orginal.getTradeUuid());
            if (TradeSourceUtils.isTradePayedAndConfirmed(originalTrade, SourceId.DIANPING)) {
                ToastUtil.showLongToast(R.string.dinner_table_merge_dianping_payed_toast);
                return;
            }
                        if (TradeSourceUtils.isTradePayedAndConfirmed(originalTrade, SourceId.XIN_MEI_DA)) {
                ToastUtil.showLongToast(R.string.dinner_table_merge_xinmeida_payed_toast);
                return;
            }
                        if (TradeSourceUtils.isTradePayedAndConfirmed(originalTrade, SourceId.KOU_BEI)) {
                ToastUtil.showLongToast(R.string.dinner_table_merge_koubei_payed_toast);
                return;
            }
                        if (TradeSourceUtils.isTradePayedAndConfirmed(originalTrade, SourceId.BAIDU_RICE)) {
                ToastUtil.showLongToast(R.string.dinner_table_merge_baidurice_payed_toast);
                return;
            }
                        if (TradeSourceUtils.isTradePayedAndConfirmed(originalTrade, SourceId.FAMILIAR)) {
                ToastUtil.showLongToast(R.string.dinner_table_merge_shuke_payed_toast);
                return;
            }

                        DinnertableTradeVo dinnerTradeVo = infoFragment.talbeInfoContentBean.getDinnerTableTradeVo();
            if (dinnerTradeVo != null && dinnerTradeVo.getTradeVo() != null && dinnerTradeVo.getTradeVo().getTradeEarnestMoney() > 0) {
                ToastUtil.showLongToast(R.string.dinner_table_merge_earnest_payed_toast);
                return;
            }

        } catch (SQLException e1) {
            Log.e(TAG, e1.getMessage());
            return;
        } finally {
            if (helper != null) {
                DBHelperManager.releaseHelper(helper);
            }
        }

                doMergeDialog(orginal, dest);
    }

    private void doDialogByType(final IDinnertableTrade dinnertableTrade, final int reasonType) {
        final TradeVo tradeVo = getTradeVo(dinnertableTrade);
        OrderCenterOperateDialogFragment dialog = new OrderCenterOperateDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", reasonType);
        dialog.setArguments(bundle);
        dialog.registerListener(new OperateListener() {
            @Override
            public boolean onSuccess(OperateResult result) {
                return disposeTheRequest(dinnertableTrade, tradeVo, result, reasonType);
            }

        });

        String tradeUuid = infoFragment.talbeInfoContentBean.getDinnerTableTradeVo().getTradeVo().getTrade().getUuid();
        dialog.setPayment(mManager.getPaymentVo(tradeUuid));
        if (reasonType == RejectReason.REASON_TYPE_DELETE_DISH) {
            dialog.show(getFragmentManager(), "refund");
        } else if (reasonType == RejectReason.REASON_TYPE_REFUSE) {
            dialog.show(getFragmentManager(), "refuse");
        } else if (reasonType == RejectReason.REASON_TYPE_DESTROY) {
            dialog.setTradeVo(tradeVo);
            dialog.show(getFragmentManager(), "destroy");
        } else if (reasonType == RejectReason.REASON_TYPE_REPAY) {
            dialog.show(getFragmentManager(), "repay");
        }

    }

    private TradeVo getTradeVo(IDinnertableTrade dinnertableTrade) {
        TradeVo tradeVo = null;
                if (UnionUtil.isUnionMainTrade(dinnertableTrade.getTradeType())) {
            DinnertableTradeInfo mainTradeInfo = getInfoFragment().getDataManager().getUnionMainInfo();
            if (mainTradeInfo == null) {
                return null;
            }

            TradeVo mainTradeVo = mainTradeInfo.getTradeVo();
            if (mainTradeVo == null) {
                return null;
            }

            mainTradeVo = mainTradeVo.clone();            if (Utils.isNotEmpty(mainTradeInfo.getSubTradeInfoList())) {
                for (DinnertableTradeInfo subTradeInfo : mainTradeInfo.getSubTradeInfoList()) {
                    TradeVo subTradeVo = subTradeInfo.getTradeVo();
                    if (subTradeVo != null && Utils.isNotEmpty(subTradeVo.getTradeItemList())) {
                        mainTradeVo.getTradeItemList().addAll(subTradeVo.getTradeItemList());
                    }
                }
            }

            tradeVo = mainTradeVo;
        } else {
                        DinnertableTradeVo dinnertableTradeVo = getInfoFragment().getDinnerTableTradeVo(dinnertableTrade.getTradeUuid());
            if (dinnertableTradeVo != null) {
                tradeVo = dinnertableTradeVo.getTradeVo();
            }
        }

                if (tradeVo != null && tradeVo.getMealShellVo() != null && tradeVo.getMealShellVo().getTradeItem() != null) {
            if (tradeVo.getTradeItemList() == null) {
                tradeVo.setTradeItemList(new ArrayList<TradeItemVo>());
            }
            TradeItemVo tradeItemVo = new TradeItemVo();
            tradeItemVo.setTradeItem(tradeVo.getMealShellVo().getTradeItem());
            tradeVo.getTradeItemList().add(tradeItemVo);
        }

        return tradeVo;
    }

    private boolean disposeTheRequest(final IDinnertableTrade dinnertableTrade, final TradeVo tradeVo, OperateResult result, int reasonType) {
        Log.d(TAG, "disposeTheRequest:" + reasonType);
        UserActionEvent.start(UserActionEvent.DINNER_DEL_ORDER);
        Reason reason = result.reason;
        boolean isPrintChecked = result.isPrintChecked;
        List<InventoryItemReq> inventoryItems = result.returnInventoryItemReqs;
        switch (reasonType) {
            case RejectReason.REASON_TYPE_DESTROY:
                mManager.delete(dinnertableTrade, tradeVo, isPrintChecked, reason, inventoryItems);
                break;
        }
        return false;
    }

    protected void doDelete(final IDinnertableTrade dinnertableTrade) {
        if (dinnertableTrade.getTradeStatus() == TradeStatus.UNPROCESSED) {
            ToastUtil.showLongToast(R.string.dinner_table_delete_unprocess_toast);
            return;
        }

        if (dinnertableTrade.getTradeStatus() == TradeStatus.CONFIRMED) {
            DatabaseHelper helper = null;
            Trade trade = null;
            try {
                helper = DBHelperManager.getHelper();
                Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
                trade = tradeDao.queryForId(dinnertableTrade.getTradeUuid());
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            } finally {
                if (helper != null) {
                    DBHelperManager.releaseHelper(helper);
                }
            }
            DinnertableTradeVo dinnerTradeVo = infoFragment.talbeInfoContentBean.getDinnerTableTradeVo();
                        if (trade != null && trade.getSource() == SourceId.BAIDU_RICE && trade.getTradePayStatus() == TradePayStatus.PAID) {
                ToastUtil.showLongToast(R.string.dinner_table_delete_finished_toast);
                return;
            }
        }
        DinnertableTradeVo dinnerTradeVo = infoFragment.talbeInfoContentBean.getDinnerTableTradeVo();
        if (dinnerTradeVo != null && dinnerTradeVo.getTradeVo() != null && dinnerTradeVo.getTradeVo().getTradeEarnestMoney() > 0) {
            DialogUtil.showWarnConfirmDialog(getFragmentManager(),
                    R.string.dinner_order_center_recision_earnest_not_fefund,
                    R.string.dinner_ok,
                    R.string.dinner_cancel,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            doDialogByType(dinnertableTrade, ReasonType.TRADE_INVALID.value().intValue());
                        }
                    },
                    null,
                    "doRecision");
        } else {
            doDialogByType(dinnertableTrade, ReasonType.TRADE_INVALID.value().intValue());
        }
    }

    private void doClearTable(final DinnertableModel model) {
        new CommonDialogFragmentBuilder(MainApplication.getInstance()).title(R.string.clear_table_or_not)
                .iconType(R.drawable.commonmodule_dialog_icon_warning)
                .positiveText(R.string.dinner_clear)
                .positiveLinstner(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {                        UserActionEvent.start(UserActionEvent.DINNER_TABLE_CLEAR);
                        mManager.clear(model);
                    }
                })
                .negativeText(R.string.dinner_cancel)
                .negativeLisnter(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {                    }
                })
                .build()
                .show(getFragmentManager(), DIALOG_TAG);
    }

    @AfterViews
    public void init() {
        Log.i(TAG, "init...");


        final String[] functions = getResources().getStringArray(R.array.dinner_table_fnction);
        tv_moreFunction.setText(String.format(getString(R.string.dinner_table_morefunction), "" + getSwitchsFromSharePreference().size(), "" + functions.length));

        mZonesIndicator.setOnControlListener(mOnControlListener);
        mZoneView.setOnControlListener(mOnControlListener);
        mZoneView.initChildren();

        infoFragment = initInfoFragment();
        infoFragment.setZoneView(mZoneView);
        replaceFragment(mZoneView.getControlViewId(), infoFragment, DinnerTableConstant.CONTROL_FRAGMENT_TAG);

        handler = new Handler();
        mManager = new DinnertableManager();

        registerEventBus();
        uuid = SystemUtils.genOnlyIdentifier();
        Log.e("UUID", "init=>" + uuid);
        DinnertableStateCache.open(mManager, getBussinessType());        mManager.open(this, uuid);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mSwitchTableTradeVo = new SwitchTableTradeVo(null, bundle.getLong("tableId"));
        }
        super.init();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }

    public ZoneView getZoneView() {
        return mZoneView;
    }

    public DinnertableManager getDinnertableManager() {
        return mManager;
    }

    @Override
    public void onStop() {
        super.onStop();
        mZoneView.hideControl();
        DinnerShopCartAdapter.setDishCheckMode(false);
        enableDinnertableClick(true);
        DinnertableStateCache.onStop();
        Log.i("zhubo", "DinnertableFragment onstop");

    }


    @Override
    public void onDestroyView() {
        Log.i(TAG, "onDestroyView...");
        handler.removeCallbacks(refreshSpendTimeRunnable);
        mManager.close();
        unregisterEventBus();
        OpentablePopWindow.release();
        if (moreFunctionPop != null) {
            moreFunctionPop.hide();
        }
        moreFunctionPop = null;
        super.onDestroyView();
    }


    public boolean hideMoreMenuPop(MotionEvent event) {
        if (layout_moreFunction != null || (event != null && !DensityUtil.isRangeOfView(layout_moreFunction, event))) {
            if (moreFunctionPop != null && moreFunctionPop.isShowing()) {
                moreFunctionPop.hide();
                return true;
            }
        }
        return false;
    }



    public void onEventMainThread(EventZonesNotice event) {
        if (!event.uuid.equals(uuid)) {
            return;
        }
        mZonesIndicator.setZones(event.zones);
        IZone zone = null;
        if (Utils.isNotEmpty(event.zones)) {
                        Integer index = null;
            if (SharedPreferenceUtil.getSpUtil().contains(SP_DINNERTABLE_ZONE_ID)) {
                Long spZoneId = SharedPreferenceUtil.getSpUtil().getLong(SP_DINNERTABLE_ZONE_ID, -1);

                int size = event.zones.size();
                for (int i = 0; i < size; i++) {
                    IZone tempZone = event.zones.get(i);
                    if (spZoneId.equals(tempZone.getId())) {
                        index = i;
                        break;
                    }
                }
            }
            if (index == null) {
                index = 0;
            }
            zone = event.zones.get(index);
        }
        if (zone != null) {
            mZonesIndicator.switchZone(zone);
        }

                if (mManager.isEnableServing()) {
            statusServingView.setVisibility(View.VISIBLE);
        } else {
            statusServingView.setVisibility(View.GONE);
        }
        if (mManager.isAutoClearTable()) {
            statusDoneView.setVisibility(View.GONE);
        } else {
            statusDoneView.setVisibility(View.VISIBLE);
        }

        selectTrade(mSwitchTableTradeVo);
    }



    public void onEventMainThread(EventZoneSwitchNotice event) {
        zoneModel = event.zoneModel;
        mZoneView.setModel(event.zoneModel);
        final EventZoneSwitchNotice event1 = event;
        if (event.zoneModel != null) {
            SharedPreferenceUtil.getSpUtil().putLong(SP_DINNERTABLE_ZONE_ID, event.zoneModel.getId());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DinnertableStateCache.querybyId(event1.zoneModel.getId() + "");
                }
            }).start();
        }
        mZoneView.setSelectState(mManager.getSelectedDinnertableId());
        handler.removeCallbacks(refreshSpendTimeRunnable);
        handler.postDelayed(refreshSpendTimeRunnable, REFRESH_SPEND_TIME_INTERVAL);
                enableDinnertableClick(!DinnerShopCartAdapter.isDishCheckMode());
    }

    public void onEventMainThread(EventFunctionChange event) {
        tv_moreFunction.setText(String.format(getString(R.string.dinner_table_morefunction), "" + event.checkNum, "" + event.totalNum));
        switch (event.funcationBean.functionCode) {
            case 1001:                MobclickAgentEvent.onEvent(getActivity(), DinnerMobClickAgentEvent.tableMoreFunctionSn);
                SharedPreferenceUtil.getSpUtil().putBoolean(DinnerTableConstant.SHOW_TRADE_MONEY_KEY, !event.isCheck);
                                mZoneView.setModel(zoneModel);
                if (mZoneView.ismShowingControl()) {
                                        infoFragment.refreshHeadInfo();
                                    }
                break;
            case 1002:                MobclickAgentEvent.onEvent(getActivity(), DinnerMobClickAgentEvent.tableMoreFunctionPreCheckout);
                SharedPreferenceUtil.getSpUtil().putBoolean(SHOW_PRECASH_KEY, event.isCheck);
                mZoneView.refreshDinnertable();
                if (mZoneView.ismShowingControl()) {
                    infoFragment.refreshView();
                }

                break;
            case 1003:                MobclickAgentEvent.onEvent(getActivity(), DinnerMobClickAgentEvent.tableMoreFunctionTime);
                SharedPreferenceUtil.getSpUtil().putBoolean(SHOW_ISSUE_TIME_KEY, event.isCheck);
                if (mZoneView.ismShowingControl()) {
                    infoFragment.refreshView();
                }
                break;
            default:
                break;
        }


    }


    public void onEventMainThread(EventRefreshDinnertableNotice event) {
        mZoneView.setModel(zoneModel);
        mZonesIndicator.refreshZone();

        mZoneView.setSelectState(mManager.getSelectedDinnertableId());
                enableDinnertableClick(!DinnerShopCartAdapter.isDishCheckMode());
        if (mTvLeisureTable != null) {
            String emptyTableHint = getString(R.string.dinner_free_table) + getString(R.string.dinner_data_hint, event.getmEmptyTableCount() + "");
            mTvLeisureTable.setText(emptyTableHint);
        }
    }


    public void onEventMainThread(final EventInsertDinnerNotice event) {

        ThreadUtils.runOnWorkThread(new Runnable() {
            @Override
            public void run() {
                mManager.refreshDinnertableTrade(event.tradeUuid, event.tableId);
            }
        });
            }


    public void selectTrade(SwitchTableTradeVo switchTableTradeVo) {

        if (switchTableTradeVo == null) {
            return;
        }

        this.mSwitchTableTradeVo = switchTableTradeVo;

        if (mSwitchTableTradeVo.getZoneId() == null) {
            Long zoneId = mManager.getZoneIdByTable(mSwitchTableTradeVo.getTableId());
            if (zoneId == null) {
                return;
            }
            mSwitchTableTradeVo.setZoneId(zoneId);
        }

        if (switchTableTradeVo.isCloseTableInfoFragment()) {
            infoFragment.resetView();            mZoneView.hideControl();
        }

        if (mZonesIndicator != null && mZonesIndicator.switchZone(switchTableTradeVo.getZoneId())) {
            if (mZoneView != null) {
                                mManager.setSelectedDinnertableId(switchTableTradeVo.getTableId());
                mZoneView.setSelectState(switchTableTradeVo.getTableId());
                mManager.notifyDinnertable(switchTableTradeVo.getTableId());
                enableDinnertableClick(true);
            }


        }

        mSwitchTableTradeVo = null;    }


    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
    }


    private void doMergeMoveDish(final IDinnertableTradeMoveDish orginal, final IDinnertableTrade dest) {
        if (orginal.getBusinessType() != dest.getBusinessType()) {
            ToastUtil.showShortToast(getString(R.string.dinner_merge_trade_error_bussinesstype));
            return;
        }

        if (orginal.getTradeStatus() == TradeStatus.UNPROCESSED || dest.getTradeStatus() == TradeStatus.UNPROCESSED) {
            ToastUtil.showLongToast(R.string.dinner_table_merge_unprocess_toast);
            return;
        }
                try {
            DatabaseHelper helper = DBHelperManager.getHelper();
            Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
            Trade destTrade = tradeDao.queryForId(dest.getTradeUuid());
            if (TradeSourceUtils.isTradePayedAndConfirmed(destTrade, SourceId.DIANPING)) {
                ToastUtil.showLongToast(R.string.dinner_table_merge_dianping_payed_toast);
                return;
            }
            if (TradeSourceUtils.isTradePayedAndConfirmed(destTrade, SourceId.XIN_MEI_DA)) {
                ToastUtil.showLongToast(R.string.dinner_table_merge_xinmeida_payed_toast);
                return;
            }
            if (TradeSourceUtils.isTradePayedAndConfirmed(destTrade, SourceId.KOU_BEI)) {
                ToastUtil.showLongToast(R.string.dinner_table_merge_koubei_payed_toast);
                return;
            }
            if (TradeSourceUtils.isTradePayedAndConfirmed(destTrade, SourceId.BAIDU_RICE)) {
                ToastUtil.showLongToast(R.string.dinner_table_merge_baidurice_payed_toast);
                return;
            }

        } catch (SQLException e1) {
            e1.printStackTrace();
            return;
        }


        doMergeMoveDishDialog(orginal, dest);

    }



    private void doTransferMoveDish(IDinnertableTradeMoveDish orginal, IDinnertable dest) {
        if (!dest.isCurBusinessType(getBussinessType())) {
            ToastUtil.showShortToast(getString(R.string.dinner_merge_trade_error_bussinesstype));
            return;
        }

        if (orginal.getTradeStatus() == TradeStatus.UNPROCESSED) {
            ToastUtil.showLongToast(R.string.dinner_move_dish_unprocess_toast);
            return;
        }
                try {

            DatabaseHelper helper = DBHelperManager.getHelper();
            Dao<Trade, String> tradeDao = helper.getDao(Trade.class);
            Trade trade = tradeDao.queryForId(orginal.getTradeUuid());
            if (TradeSourceUtils.isTradePayedAndConfirmed(trade, SourceId.DIANPING)) {
                ToastUtil.showLongToast(R.string.dinner_move_dish_dianping_payed_toast);
                return;
            }
            if (TradeSourceUtils.isTradePayedAndConfirmed(trade, SourceId.XIN_MEI_DA)) {
                ToastUtil.showLongToast(R.string.dinner_move_dish_xinmeida_payed_toast);
                return;
            }
            if (TradeSourceUtils.isTradePayedAndConfirmed(trade, SourceId.KOU_BEI)) {
                ToastUtil.showLongToast(R.string.dinner_move_dish_koubei_payed_toast);
                return;
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
            return;
        }

        doTransferMoveDishDialog(orginal, dest);
    }



    public void enableDinnertableClick(boolean enable) {
        ZoneContentView zoneContentView = mZoneView.getmZoneContentView();


        for (int i = 0; i < zoneContentView.getChildCount(); i++) {
            View view = zoneContentView.getChildAt(i);
            if (view instanceof TableViewBase) {
                if (enable) {
                    view.setOnClickListener(zoneContentView.getmOnClickListener());
                } else {
                    view.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ToastUtil.showShortToast(R.string.dinner_table_info_close_movedish);

                        }
                    });
                }
            }


        }


    }


    public List<TradeItem> getMoveDishItems(IDinnertableTradeMoveDish orginal) {
        DinnertableTradeModelMoveDish modelMoveDish = (DinnertableTradeModelMoveDish) orginal;
        List<TradeItem> tradeItems = new ArrayList<TradeItem>();

        if (modelMoveDish.getItems() != null) {
            for (IShopcartItem item : modelMoveDish.getItems()) {
                ReadonlyShopcartItem readonlyShopcartItem = (ReadonlyShopcartItem) item;
                tradeItems.add(readonlyShopcartItem.tradeItem);
            }
        }
        return tradeItems;
    }

    private CompoundButton.OnCheckedChangeListener showMoneyListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            MobclickAgentEvent.onEvent(getActivity(), MobclickAgentEvent.dinnerTableClickShowMoneyButton);
            SharedPreferenceUtil.getSpUtil().putBoolean(DinnerTableConstant.SHOW_TRADE_MONEY_KEY, !isChecked);
                        mZoneView.setModel(zoneModel);
            if (mZoneView.ismShowingControl()) {
                                                infoFragment.talbeInfoContentBean.showOperateButton(infoFragment.talbeInfoContentBean.getDinnerTableTradeVo());
            }


        }
    };


    protected DishQuantityAndSeatEditDialogFragment bulildDishQuantitySeatEditDialog(String headTitle, String title, int iconType, int positiveText, int negativetext) {
        CommonDialogFragment.CommonDialogFragmentBuilder builder = new DishQuantityAndSeatEditDialogFragment.MoveDishQuantityDialogFragmentBuilder(MainApplication.getInstance());
        DishQuantityAndSeatEditDialogFragment fragment = (DishQuantityAndSeatEditDialogFragment) builder.headTitle(headTitle).title(title)
                .iconType(iconType)
                .positiveText(positiveText)
                .negativeText(negativetext)
                .build();

        return fragment;
    }


    public TableInfoFragment getInfoFragment() {
        return infoFragment;
    }




    public void onEventMainThread(EventRefreshReserveNotice event) {
        mZoneView.refresReserveStatus(event.bookingMap);

    }



    public void onEventMainThread(EventRefreshOpenTableAsync event) {
        mZoneView.refresOpenTableStauts();

    }

    @Click({R.id.layout_moreFuntion, R.id.tv_leisure_table, R.id.tv_join_trade, R.id.tv_dinner_nav_carry_out, R.id.tv_dinner_nav_verification})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_moreFuntion:
                if (moreFunctionPop != null && moreFunctionPop.isShowing()) {
                    moreFunctionPop.hide();
                    return;
                }

                MobclickAgentEvent.onEvent(getActivity(), DinnerMobClickAgentEvent.tableMoreFunction);
                if (moreFunctionPop == null) {
                    moreFunctionPop = new MoreFunctionPopWindow(getActivity(), this.getBussinessType());
                }

                moreFunctionPop.initItemChecked(getSwitchsFromSharePreference());
                moreFunctionPop.show(view);
                break;
            case R.id.tv_leisure_table:
                if (!ClickManager.getInstance().isClicked()) {
                    MobclickAgentEvent.onEvent(getActivity(), DinnerMobClickAgentEvent.tableEmpty);
                    new LeisureTableDialogFragment().setmBusinessType(getBussinessType()).show(getFragmentManager(),
                            LeisureTableDialogFragment.class.getSimpleName());
                }
                break;
            case R.id.tv_join_trade:
                if (!ClickManager.getInstance().isClicked()) {
                    MobclickAgentEvent.onEvent(getActivity(), DinnerMobClickAgentEvent.tableUnionTrade);
                    new BatchOperationTableDialogFragment().setmBusinessType(getBussinessType()).show(getFragmentManager(),
                            BatchOperationTableDialogFragment.class.getSimpleName());
                }
                break;
            case R.id.tv_dinner_nav_carry_out:
                ActionShowTrade actionShowTrade = new ActionShowTrade(uuid, null, DbQueryConstant.SALES_ALL);
                actionShowTrade.setFilterConditions(DeliveryType.TAKE);
                EventBus.getDefault().post(actionShowTrade);
                break;
            case R.id.tv_dinner_nav_verification:
                if (!ClickManager.getInstance().isClicked()) {
                    VerificationDialog verification = new VerificationDialog_();
                    verification.show(getFragmentManager());
                }
                break;
        }
    }



    private Set<Integer> getSwitchsFromSharePreference() {
        Set<Integer> chooseItems = new HashSet<>();
        if (!SharedPreferenceUtil.getSpUtil().getBoolean(DinnerTableConstant.SHOW_TRADE_MONEY_KEY, true)) {
            chooseItems.add(1001);
        }

        if (SharedPreferenceUtil.getSpUtil().getBoolean(SHOW_PRECASH_KEY, false)) {
            chooseItems.add(1002);
        }

        if (SharedPreferenceUtil.getSpUtil().getBoolean(SHOW_ISSUE_TIME_KEY, false)) {
            chooseItems.add(1003);
        }
        return chooseItems;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("zhubo", "DinnertableFragment onPause");

    }

    @Override
    public void onResume() {
        super.onResume();
                Log.i("zhubo", "DinnertableFragment onResume");
        OpentablePopWindow.getInstance(getActivity()).hide();
    }


    public abstract void mergeTrade(IDinnertableTrade orginal, IDinnertableTrade dest);


    public abstract void transferTable(IDinnertableTrade orginal, IDinnertable dest);


    public abstract void abolishTrade(IDinnertableTrade dinnertableTrade);


    public abstract void doMergeMoveDishDialog(final IDinnertableTradeMoveDish orginal, final IDinnertableTrade dest);


    public abstract void doTransferMoveDishDialog(final IDinnertableTradeMoveDish orginal, final IDinnertable dest);


    public void doMergeDialog(final IDinnertableTrade orginal, final IDinnertableTrade dest) {
        new CommonDialogFragmentBuilder(MainApplication.getInstance()).title(getActivity().getString(R.string.dinner_trade_no_merge,
                orginal.getSn(),
                dest.getSn()))
                .iconType(R.drawable.commonmodule_dialog_icon_warning)
                .positiveText(R.string.dinner_ok)
                .positiveLinstner(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {                        String tradeTableUuid = dest.getUuid();
                        try {
                            TradeTable tradeTable = DBHelperManager.queryById(TradeTable.class, tradeTableUuid);
                            if (tradeTable != null) {
                                checkMergeOrTransfer(orginal.getTradeUuid(), null, tradeTable.getTableId(), TicketTypeEnum.MERGETABLE, new Runnable() {
                                    public void run() {
                                        mManager.merge(null, orginal, dest);
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .negativeText(R.string.dinner_cancel)
                .negativeLisnter(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {                    }
                })
                .build()
                .show(getFragmentManager(), DIALOG_TAG);
    }

    ;


    public abstract void transferTableDialog(IDinnertableTrade orginal, IDinnertable dest);
}
