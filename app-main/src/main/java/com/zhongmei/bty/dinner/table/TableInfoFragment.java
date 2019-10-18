package com.zhongmei.bty.dinner.table;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.async.manager.AsyncNetworkManager;
import com.zhongmei.bty.basemodule.auth.application.DinnerApplication;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.bty.basemodule.notifycenter.event.EventSelectDinnertableNotice;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.shopmanager.handover.manager.ServerSettingManager;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeInfo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.enums.DinnertableStatus;
import com.zhongmei.bty.basemodule.trade.enums.OperateType;
import com.zhongmei.bty.basemodule.trade.event.ActionTableInfoBottomBarStatus;
import com.zhongmei.bty.basemodule.trade.event.ActionTableInfoBottomBarStatus.BottomBarStatus;
import com.zhongmei.yunfu.context.util.DateTimeUtils;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;
import com.zhongmei.bty.cashier.shoppingcart.vo.CreateTradeVoFromAdddish;
import com.zhongmei.bty.cashier.util.TradeSourceUtils;
import com.zhongmei.bty.cashier.util.TradeStatusUtil;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.bty.basemodule.orderdish.bean.AddItemVo;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.SourceId;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;
import com.zhongmei.bty.commonmodule.util.DinnerMobClickAgentEvent;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.dinner.orderdish.DinnerDishTradeInfoFragment;
import com.zhongmei.bty.dinner.shopcart.adapter.DinnerShopCartAdapter;
import com.zhongmei.bty.dinner.table.bean.AddItemBatchBean;
import com.zhongmei.bty.dinner.table.event.EventDinnertableVoNotice;
import com.zhongmei.bty.dinner.table.event.EventMoveDishNotice;
import com.zhongmei.bty.dinner.table.event.EventRefreshTradeAsyncHttp;
import com.zhongmei.bty.dinner.table.manager.OpenTableManager;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeModel;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeModelMoveDish;
import com.zhongmei.bty.dinner.table.view.AddItemBatchView;
import com.zhongmei.bty.dinner.table.view.DinnertableTradeView;
import com.zhongmei.bty.dinner.table.view.MoreMenuPopWindow;
import com.zhongmei.bty.dinner.table.view.MovedishDragView;
import com.zhongmei.bty.dinner.table.view.OpentablePopWindow;
import com.zhongmei.bty.dinner.table.view.TableFragmentBase;
import com.zhongmei.bty.dinner.table.view.TradeGridView;
import com.zhongmei.bty.dinner.table.view.UnionTradeGroupView;
import com.zhongmei.bty.dinner.table.view.ZoneView;
import com.zhongmei.bty.dinner.table.view.ZoneView.DeleteCallback;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeVo;
import com.zhongmei.bty.dinner.vo.DinnertableVo;
import com.zhongmei.bty.commonmodule.util.SpendTimeFormater;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;



@EFragment(R.layout.dinner_table_info)
public abstract class TableInfoFragment extends BasicFragment {
    private static final String TAG = TableInfoFragment.class.getSimpleName();
    @ViewById(R.id.title_rl)
    protected RelativeLayout titleRL;

    @ViewById(R.id.table_id_tv)
    protected TextView tableIdTv;

    @ViewById(R.id.floor_tv)
    protected TextView floorTv;

    @ViewById(R.id.close_btn)
    protected ImageButton closeBtn;

    @ViewById(R.id.delete_trade_ll)
    protected View deleteButton;
    @ViewById(R.id.delete_trade_iv)
    protected ImageView deleteIv;

    @ViewById(R.id.delete_trade_tv)
    protected TextView deleteTv;

    @ViewById(R.id.move_dish_topbar_rl)
    protected RelativeLayout moveDishTopbarRL;

    @ViewById(R.id.trade_view)
    protected MovedishDragView movedishDragView;

    @ViewById(R.id.title_tv)
    protected TextView titleTv;

    @ViewById(R.id.hint_tv)
    protected TextView messageTv;

        @ViewById(R.id.allcheck_cb)
    protected CheckBox allCheckCb;

        @ViewById(R.id.dish_operate_checkbar)
    protected LinearLayout dishOperateCheckbar;

        @ViewById(R.id.check_number_tv)
    protected TextView checkNumberTv;

        @ViewById(R.id.ll_number_and_waiter)
    protected LinearLayout llNumberAndWaiter;

    @ViewById(R.id.ll_number_and_waiter_viewgroup)
    protected LinearLayout ll_numberAndWaiterGroup;

        @ViewById(R.id.iv_number_and_waiter_tip)
    public ImageView ivNumberAndWaiterTip;

    @ViewById(R.id.tv_customer_count)
    protected TextView tvCustomerCount;

    @ViewById(R.id.tv_waiter_name)
    protected TextView tvWaiterName;

    @ViewById(R.id.trade_gridview)
    protected TradeGridView tradeGridView;

    @ViewById(R.id.trade_fl)
    protected FrameLayout fl_tradeview;

        @ViewById(R.id.trade_view2)
    protected DinnertableTradeView tradeView;

    @ViewById(R.id.more_btn)
    protected ImageButton btn_more;

    @ViewById(R.id.trade_back_iv)
    protected ImageView tradeBackIv;

        @ViewById(R.id.trade_type_iv)
    protected ImageView tradeTypeIv;

        @ViewById(R.id.indicator_iv)
    protected ImageView indicatorIv;

        @ViewById(R.id.add_order_btn)
    protected ImageView addOrderBtn;

    @ViewById(R.id.add_order_tv)
    protected TextView addOrderTv;

        @ViewById(R.id.dish_operate_title_ll)
    protected LinearLayout dishOperateTitleLL;

        @ViewById(R.id.operate_name_tv)
    protected TextView operateNameTv;

    @ViewById(R.id.add_item_batch_view)
    protected AddItemBatchView addItemBatchView;

    @ViewById(R.id.fl_join_trade)
    protected UnionTradeGroupView flJoinTrade;

    @ViewById(R.id.tv_join_table_down)
    protected TextView tvJoinTableDown;

    @ViewById(R.id.iv_join_trade_indicator)
    protected ImageView ivJoinTradeIndicator;

    @ViewById(R.id.dinner_appointment_order_hint_tv)
    protected TextView tvAppointmentOrderHint;

    @Bean
    public TableInfoContentBean talbeInfoContentBean;
    public static DataManage dataManage;

    private ZoneView zoneView;

    private static boolean enableServing = false;
    protected DinnertableTradeVo dinnertableTradeVo;
    protected TableFragmentBase dinnertableFragment;

    EventDinnertableVoNotice event;
    static int currentPosition = 0;
    private int choosePositionAddItem = -1;
    private static int unionTradePositon = 0;

    private int mTradeModelType = DataManage.MODELTYPE_UNION;

    MoreMenuPopWindow moreMenuPopWindow;
    private List<IShopcartItem> shopcartItemsAddDish;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
    }

    @AfterViews
    public void initialViews() {
        Log.i(TAG, "width:" + DensityUtil.getScreenWidth(getActivity()) + " height:" + DensityUtil.getScreenHeight(getActivity()));
        getView().setVisibility(View.INVISIBLE);
        ivNumberAndWaiterTip.setVisibility(View.GONE);

        indicatorIv.setVisibility(View.INVISIBLE);

        registerEventBus();

        dinnertableFragment = (TableFragmentBase) getFragmentManager().findFragmentByTag(TableFragmentBase.TAG_TABLE_FRAGMENT);
        if (zoneView != null) {
            zoneView.bindDragDeleteView(deleteButton, new DeleteCallback() {

                @Override
                public void onDragStarted() {
                    updateDeleteButton(DragStatus.DRAGSTARTED);
                }

                @Override
                public void onEntered(View view) {
                    updateDeleteButton(DragStatus.ENTERED);
                }

                @Override
                public void onExited(View view) {
                    updateDeleteButton(DragStatus.EXITED);
                }

                @Override
                public void onDragEnded() {
                    updateDeleteButton(DragStatus.DRAGEND);
                }
            });
        }
    }

    public void setZoneView(ZoneView zoneView) {
        this.zoneView = zoneView;
    }

    public ZoneView getZoneView() {
        return zoneView;
    }

    public void refreshView() {
        setCurrentOrderPosition();
        DinnertableTradeVo oldDinnertableTradeVo = dinnertableTradeVo;
        int curTradeIndex = dataManage.isNomalModel(mTradeModelType) ? TableInfoFragment.getCurrentPosition() : TableInfoFragment.getUnionTradePositon();
        dinnertableTradeVo = dataManage.getTradeObjcet(curTradeIndex, mTradeModelType);
        if (dinnertableTradeVo == null) {
            if (zoneView != null) {
                zoneView.hideControl();
            }
            getView().setVisibility(View.INVISIBLE);
            return;
        }

        if (!equalTable(oldDinnertableTradeVo)) {
            DinnerDishTradeInfoFragment tradeInfoFragment = (DinnerDishTradeInfoFragment) getFragmentManager().findFragmentByTag("dinner_table_page0");
            tradeInfoFragment.setDishServingModify(false);
        }
        talbeInfoContentBean.refreshView(dinnertableTradeVo);
        refreshHeadInfo();
        refreshAddItemBatchView();
    }


    public DinnertableTradeVo getDinnerTableTradeVo() {
        if (dinnertableTradeVo == null) {
            dinnertableTradeVo = dataManage.getTradeObjcet(TableInfoFragment.getCurrentPosition(), mTradeModelType);
        }
        return dinnertableTradeVo;
    }

    public DinnertableTradeVo getDinnerTableTradeVo(String tradeUuid) {
        List<DinnertableTradeVo> dinnertableTradeVos = dataManage.getAllTrade();
        if (Utils.isNotEmpty(dinnertableTradeVos)) {
            for (DinnertableTradeVo dinnertableTradeVo : dinnertableTradeVos) {
                if (TextUtils.equals(dinnertableTradeVo.getTradeUuid(), tradeUuid)) {
                    return dinnertableTradeVo;
                }
            }
        }

        return null;
    }


    private boolean equalTable(DinnertableTradeVo oldDinnertableTradeVo) {
        if (oldDinnertableTradeVo != null && dinnertableTradeVo != null && dinnertableTradeVo.getInfo() != null) {
            DinnertableTradeInfo oldTableInfo = oldDinnertableTradeVo.getInfo();
            if (oldTableInfo != null && oldTableInfo.getTradeTableId() != null) {
                return oldTableInfo.getTradeTableId().equals(dinnertableTradeVo.getInfo().getTradeTableId());
            }
        }
        return false;
    }


    public void refreshHeadInfo() {
        String tableName = dataManage.getTableName();
        tableIdTv.setText(tableName);

        if (dataManage.getFloor().length() > 5) {
            floorTv.setText(dataManage.getFloor().substring(0, 5));
        } else {
            floorTv.setText(dataManage.getFloor());
        }

                if (addOrderBtn.getTag() != null) {
            addOrderBtn.setImageResource(R.drawable.dinner_table_info_add_order);
            addOrderTv.setVisibility(View.GONE);
            addOrderBtn.setTag(null);
        }

                if (tradeGridView.getVisibility() == View.VISIBLE) {
            tradeGridView.setVisibility(View.GONE);
            indicatorIv.setVisibility(View.INVISIBLE);
        }
        TradeVo tradeVo = dinnertableTradeVo.getTradeVo();
        if (tradeVo.getTrade().getTradeType() == TradeType.UNOIN_TABLE_SUB
                || tradeVo.getTrade().getTradeType() == TradeType.UNOIN_TABLE_MAIN) {
            btn_more.setVisibility(View.GONE);
        } else {
            btn_more.setVisibility(View.VISIBLE);
        }
        refreshJoinTradeView();
        refreshTradeView();
        updateNumberAndWaiter();

        tvAppointmentOrderHint.setVisibility(tradeVo.isAppointmentOrder() ? View.VISIBLE : View.GONE);
        if (tradeVo.isAppointmentOrder()) {
            tvAppointmentOrderHint.setText(getString(R.string.dinner_appointment_order_hint, DateTimeUtils.formatDate(tradeVo.getTradeExtra().getExpectTime(), DateTimeUtils.DATE_TIME_FORMAT4)));
        }
    }

    @Click({R.id.close_btn, R.id.back_iv, R.id.trade_view2, R.id.more_btn, R.id.add_order_btn, R.id.fl_join_trade})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_btn:
                MobclickAgentEvent.onEvent(getActivity(), MobclickAgentEvent.dinnerTableClickCloseButton);
                                if (DinnerShopCartAdapter.isDishCheckMode()) {
                    post(new ActionTableInfoBottomBarStatus(BottomBarStatus.TRADE_OPERATE));
                }

                if (zoneView != null) {
                    zoneView.hideControl();
                }
                getView().setVisibility(View.INVISIBLE);
                break;
            case R.id.back_iv:                post(new ActionTableInfoBottomBarStatus(BottomBarStatus.TRADE_OPERATE));
                if (event != null) {
                    post(event);
                }
                break;
            case R.id.fl_join_trade:
                                if (!ClickManager.getInstance().isClicked()) {
                    showUnion();
                }
                break;
            case R.id.trade_view2:
                if (!ClickManager.getInstance().isClicked()) {
                    MobclickAgentEvent.onEvent(getActivity(), DinnerMobClickAgentEvent.tableDetailsTradeSwitch);
                    if (dataManage.isAddItemModel(mTradeModelType)) {                        setTradeModelType(DataManage.MODELTYPE_NOMAL);
                        refreshView();
                        choosePositionAddItem = -1;
                    } else {                        MobclickAgentEvent.onEvent(getActivity(), MobclickAgentEvent.dinnerDetailsTradesClick);
                        setTradeModelType(DataManage.MODELTYPE_NOMAL);
                        ivJoinTradeIndicator.setVisibility(View.INVISIBLE);
                        if (tradeGridView.getVisibility() == View.VISIBLE) {
                            tradeGridView.setVisibility(View.GONE);
                            indicatorIv.setVisibility(View.INVISIBLE);
                        } else {
                            if (dataManage.getAllTrade().size() > 1 && dinnertableTradeVo.getTableId() == dataManage.getTableId()) {
                                tradeGridView.setData(getActivity(), dataManage.getAllTrade(), dinnertableTradeVo);
                                tradeGridView.setItemClickListener(new TradeGridView.ItemClickLisenter() {
                                    @Override
                                    public void clickItem(int postion) {
                                        chooseTrade(postion);
                                    }
                                });
                                tradeGridView.setVisibility(View.VISIBLE);
                                indicatorIv.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                }

                break;
            case R.id.more_btn:
                if (!ClickManager.getInstance().isClicked()) {
                    if (!TradeStatusUtil.checkPayStatus(dinnertableTradeVo) && getBusinessType() != BusinessType.BUFFET) {
                        ToastUtil.showShortToast(getString(R.string.dinner_paying));
                        return;
                    }

                                        if (dinnertableTradeVo != null && AsyncNetworkManager.getInstance().queryNotSuccess(getActivity(), dinnertableTradeVo.getTradeVo().getTrade().getId())) {
                        return;
                    }

                    if (isAddDishMode()) {
                        ToastUtil.showShortToast(R.string.dinner_table_choice_trade);
                        return;
                    }

                    MobclickAgentEvent.onEvent(getActivity(), DinnerMobClickAgentEvent.tableDetailsOperator);
                    moreMenuPopWindow = new MoreMenuPopWindow(getActivity(), v, DensityUtil.dip2px(getActivity(), 405));
                    moreMenuPopWindow.show();
                }

                break;
            case R.id.add_order_btn:
                if (v.getTag() == null) {
                                        if (!ServerSettingManager.allowMultiTradesOnTable()) {
                        ToastUtil.showShortToast(R.string.dinner_table_opentable_tip);
                        return;
                    }


                    MobclickAgentEvent.onEvent(getActivity(), DinnerMobClickAgentEvent.tableDetailsCreateTrade);
                    addOrderBtn.setImageResource(R.drawable.dinner_table_info_add_order2);
                    addOrderTv.setVisibility(View.VISIBLE);
                    addOrderBtn.setTag(new Object());
                } else {
                    MobclickAgentEvent.onEvent(getActivity(), MobclickAgentEvent.dinnerTableDetailsOpenTrade);
                    if (!ClickManager.getInstance().isClicked(R.id.add_order_btn)) {
                        enableAddOrderBtn(false);
                        VerifyHelper.verifyAlert(getActivity(),
                                DinnerApplication.PERMISSION_DINNER_CREATE,
                                new VerifyHelper.Callback() {
                                    @Override
                                    public void onPositive(User user, String code, Auth.Filter filter) {
                                        super.onPositive(user, code, filter);
                                        finishOpenTable();
                                    }
                                });
                    }

                    hideOpenTableWindow(null, true);
                }
                break;
            default:
                break;
        }

    }

    public void showUnion() {
        MobclickAgentEvent.onEvent(getActivity(), DinnerMobClickAgentEvent.tableDetailsUnionTradeSwitch);
        if (dataManage.isAddItemModel(mTradeModelType)) {            setTradeModelType(DataManage.MODELTYPE_NOMAL);
            refreshView();
            choosePositionAddItem = -1;
        } else {            MobclickAgentEvent.onEvent(getActivity(), MobclickAgentEvent.dinnerDetailsTradesClick);
            setTradeModelType(DataManage.MODELTYPE_UNION);
            indicatorIv.setVisibility(View.INVISIBLE);
            if (tradeGridView.getVisibility() == View.VISIBLE) {
                tradeGridView.setVisibility(View.GONE);
                ivJoinTradeIndicator.setVisibility(View.INVISIBLE);
            } else {
                if (dataManage.getUnionSubTrades().size() > 1) {
                    tradeGridView.setData(getActivity(), dataManage.getUnionSubTrades(), dinnertableTradeVo);
                    tradeGridView.setItemClickListener(new TradeGridView.ItemClickLisenter() {
                        @Override
                        public void clickItem(int postion) {
                            chooseUnionTrade(postion);
                        }
                    });
                    tradeGridView.setVisibility(View.VISIBLE);
                    ivJoinTradeIndicator.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    public boolean checkPayStatus() {
        if (dinnertableTradeVo == null || dinnertableTradeVo.getTradeVo() == null)
            return true;
        Trade trade = dinnertableTradeVo.getTradeVo().getTrade();
        return TradeStatusUtil.checkPayStatus(trade);
    }

    @Override
    public void onStop() {
        if (moreMenuPopWindow != null && moreMenuPopWindow.isShowing()) {
            moreMenuPopWindow.dismiss();
        }
        dishOperateTitleLL.setVisibility(View.GONE);        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        talbeInfoContentBean.unregisterEventBus();
        unregisterEventBus();
                if (dataManage != null) {
            dataManage.release();
            dataManage = null;
        }
                DinnerDishTradeInfoFragment.setMoveDishDragMode(false);
        DinnerShopCartAdapter.setDishCheckMode(false);
                super.onDestroy();
    }

    public void onEventMainThread(EventDinnertableVoNotice event) {
        if (DinnerShopCartAdapter.isDishCheckMode()) {
            this.event = event;
            Log.i(TAG, "菜品选择模式不允许刷新");
            return;
        }

        this.enableServing = event.enableServing;
        Log.i(TAG, "enableServing:" + enableServing);
                DataManage oldDataManage = dataManage;
        dataManage = new DataManage(event.dinnertableVo);
        getView().setVisibility(View.VISIBLE);
        reSetTradeTypeModel(oldDataManage, dataManage);
        refreshView();
    }

    private void reSetTradeTypeModel(DataManage oldDataManager, DataManage newDataManager) {
        if (oldDataManager == null || dinnertableTradeVo == null || (dinnertableTradeVo != null && dinnertableTradeVo.getTableId() != newDataManager.getTableId()) && newDataManager.isUnionTrade()) {
            setTradeModelType(DataManage.MODELTYPE_UNION);
            TableInfoFragment.setUnionTradePositon(0);
        }

        if (!newDataManager.isUnionTrade() && newDataManager.isUnionModel(mTradeModelType)) {            setTradeModelType(DataManage.MODELTYPE_NOMAL);
        }

        if (isAddDishMode()) {            setTradeModelType(DataManage.MODELTYPE_NOMAL);
        }

        oldDataManager = null;
    }

    public void setCurrentOrderPosition() {
        if (dataManage.isNomalModel(mTradeModelType)) {
            if (TableInfoFragment.getCurrentPosition() > dataManage.getAllTrade().size() - 1) {
                TableInfoFragment.setCurrentPosition(0);
            }
        } else {
            if (TableInfoFragment.getUnionTradePositon() > dataManage.getUnionSubTrades().size() - 1) {
                TableInfoFragment.setUnionTradePositon(0);
            }
        }

    }

    public static class DataManage {
        public static final int MODELTYPE_NOMAL = 0x10;        public static final int MODELTYPE_ADDTIEM = 0x20;        public static final int MODELTYPE_UNION = 0x30;        private DinnertableVo dinnerTableVo;


        public DataManage(DinnertableVo dinnertableVo) {
            this.dinnerTableVo = dinnertableVo;
        }

        public String getTableName() {
            return dinnerTableVo.getTableName();
        }

        public String getFloor() {
            return dinnerTableVo.getZone().getName();
        }

        public Long getZoneId() {
            return dinnerTableVo.getZone().getId();
        }

        public Long getTableId() {
            return dinnerTableVo.getTableId();
        }

        public boolean isAddItemModel(int tradeModelType) {
            return tradeModelType == MODELTYPE_ADDTIEM;
        }

        public boolean isNomalModel(int tradeModelType) {
            return tradeModelType == MODELTYPE_NOMAL;
        }

        public boolean isUnionModel(int tradeModelType) {
            return tradeModelType == MODELTYPE_UNION;
        }


        public String getTableCustomerNumberInfo() {
            int realCustomerNum = 0;
            for (DinnertableTradeVo tradeVo : dinnerTableVo.getDinnertableTradeVos()) {
                realCustomerNum += tradeVo.getNumberOfMeals();
            }

            String result = realCustomerNum + "/" + dinnerTableVo.getNumberOfSeats();
            return result;
        }


        public int getTableRealCustomerNumber() {
            int realCustomerNum = 0;
            for (DinnertableTradeVo tradeVo : dinnerTableVo.getDinnertableTradeVos()) {
                realCustomerNum += tradeVo.getNumberOfMeals();
            }

            return realCustomerNum;
        }


        public String getTradeInfo(int position) {
            if (dinnerTableVo.getDinnertableTradeVos() == null || dinnerTableVo.getDinnertableTradeVos().size() == 0) {
                return "";
            }

            DinnertableTradeVo tradeVo = dinnerTableVo.getDinnertableTradeVos().get(position);
            if (tradeVo == null || tradeVo.getStatus() == DinnertableStatus.EMPTY) {
                return "";
            }
            int tradeCustomerNum = 0;
            for (TradeTable table : tradeVo.getTradeVo().getTradeTableList()) {
                if (dinnerTableVo.getTableId() == table.getTableId()) {
                    tradeCustomerNum = table.getTablePeopleCount();
                    break;
                }
            }
            String result = tradeVo.getSn() + "-" + getSpendTime(tradeVo);
            return result;

        }


        public String getTradeSn(int position) {
            if (dinnerTableVo.getDinnertableTradeVos() == null || dinnerTableVo.getDinnertableTradeVos().size() == 0) {
                return "";
            }

            DinnertableTradeVo tradeVo = dinnerTableVo.getDinnertableTradeVos().get(position);
            if (tradeVo == null || tradeVo.getStatus() == DinnertableStatus.EMPTY) {
                return "";
            }
            return tradeVo.getSn();
        }


        public String getTradeSpendtime(int position) {
            if (dinnerTableVo.getDinnertableTradeVos() == null || dinnerTableVo.getDinnertableTradeVos().size() == 0) {
                return "";
            }

            DinnertableTradeVo tradeVo = dinnerTableVo.getDinnertableTradeVos().get(position);
            if (tradeVo == null || tradeVo.getStatus() == DinnertableStatus.EMPTY) {
                return "";
            }
            return getSpendTime(tradeVo);
        }


        public String getTradePeopleNumber(int position) {
            if (dinnerTableVo.getDinnertableTradeVos() == null || dinnerTableVo.getDinnertableTradeVos().size() == 0) {
                return "";
            }

            DinnertableTradeVo tradeVo = dinnerTableVo.getDinnertableTradeVos().get(position);
            if (tradeVo == null || tradeVo.getStatus() == DinnertableStatus.EMPTY) {
                return "";
            }

            return String.valueOf(tradeVo.getNumberOfMeals());
        }


        public String getTradePeopleNumberAndTablePeople(int position) {
            if (dinnerTableVo.getDinnertableTradeVos() == null || dinnerTableVo.getDinnertableTradeVos().size() == 0) {
                return "";
            }

            DinnertableTradeVo tradeVo = dinnerTableVo.getDinnertableTradeVos().get(position);
            if (tradeVo == null || tradeVo.getStatus() == DinnertableStatus.EMPTY) {
                return "";
            }
            String result = tradeVo.getNumberOfMeals() + "/" + dinnerTableVo.getNumberOfSeats();
            return result;
        }

        private String getSpendTime(DinnertableTradeVo tradeVo) {
            String result = "";
            int spendTime = tradeVo.getDinnertableTrade().getSpendTime();
            return SpendTimeFormater.format(spendTime);
        }


        public DinnertableTradeVo getTradeObjcet(int position, int tradeTypeModel) {
            List<DinnertableTradeVo> tradeVos = null;
            if (isUnionModel(tradeTypeModel)) {
                tradeVos = dinnerTableVo.getUnionSubTradeVos();
            } else {
                tradeVos = dinnerTableVo.getDinnertableTradeVos();
            }

            if (tradeVos == null || tradeVos.size() == 0 || position > tradeVos.size() - 1) {
                return null;
            } else {
                DinnertableTradeVo tradeVo = tradeVos.get(position);
                if (isUnionSubTrade(tradeVo)) {
                    tradeVo.setUnionMainTradeInfo(getUnionMainInfo());
                }
                return tradeVo;
            }

        }

        public boolean isUnionSubTrade(DinnertableTradeVo tradeVo) {
            return tradeVo.getTradeVo().getTrade().getTradeType() == TradeType.UNOIN_TABLE_SUB;
        }

        public List<DinnertableTradeVo> getAllTrade() {
            return dinnerTableVo.getDinnertableTradeVos();

        }

        public List<DinnertableTradeVo> getUnionSubTrades() {
            return dinnerTableVo.getUnionSubTradeVos();
        }

        public boolean isUnionTrade() {
            return dinnerTableVo.getUnionMainTradeVo() != null;
        }

        public DinnertableTradeInfo getUnionMainInfo() {
            if (dinnerTableVo.getUnionMainTradeVo() != null) {
                return dinnerTableVo.getUnionMainTradeVo().getInfo();
            }
            return null;
        }


        public TradeTable getOpenTableInfo(DinnertableTradeVo tradeVo) {
            List<TradeTable> tables = tradeVo.getTradeVo().getTradeTableList();
            TradeTable tradeTable = null;
            if (Utils.isNotEmpty(tables)) {
                for (TradeTable table : tables) {
                    if (dinnerTableVo.getTableId().equals(table.getTableId())) {
                        tradeTable = table;
                        break;
                    }

                }
            }
            return tradeTable;
        }


        public static TradeTable getOpenTableInfoInDish(DinnertableTradeInfo tradeInfo) {
            if (tradeInfo != null && tradeInfo.getTradeVo() != null) {
                List<TradeTable> tables = tradeInfo.getTradeVo().getTradeTableList();
                TradeTable tradeTable = null;
                if (tables == null) return null;
                for (TradeTable table : tables) {
                    if (tradeInfo.getTradeTableId().equals(table.getId())) {
                        tradeTable = table;
                        break;
                    }

                }
                return tradeTable;
            } else {
                return null;
            }

        }

        public DinnertableVo getDinnerTableVo() {
            return dinnerTableVo;
        }

        public int getMaxSeatNuber() {
            if (dinnerTableVo != null) {
                return dinnerTableVo.getNumberOfSeats();
            } else {
                return 0;
            }

        }

        public void release() {
            dinnerTableVo = null;
        }
    }

    public static DataManage getDataManager() {
        return dataManage;
    }

    private void updateDeleteButton(DragStatus status) {
        switch (status) {
            case DRAGSTARTED:
                post(new ActionTableInfoBottomBarStatus(BottomBarStatus.DELETE_TRADE));
                break;
            case ENTERED:
                deleteButton.setBackgroundColor(Color.parseColor("#e6563b"));
                deleteIv.setImageResource(R.drawable.dinner_table_info_delete_button_choose);
                deleteTv.setTextColor(Color.parseColor("#ffffff"));
                break;
            case EXITED:
                deleteButton.setBackgroundColor(Color.parseColor("#ff6a4d"));
                deleteIv.setImageResource(R.drawable.dinner_table_info_delete_button_choose);
                deleteTv.setTextColor(Color.parseColor("#ffffff"));
                break;

            case DRAGEND:
                DinnertableTradeVo tradeVo = dataManage.getTradeObjcet(TableInfoFragment.getCurrentPosition(), mTradeModelType);
                if (TradeSourceUtils.isTradePayedAcceptedFromBAIDURICE(tradeVo)
                        || TradeSourceUtils.isTradePayedFromOpenFlatform(tradeVo)
                        || TradeSourceUtils.isTradePayedFromShuKe(tradeVo)
                        || TradeSourceUtils.isTradePayedAndConfirmed(tradeVo, SourceId.DIANPING)
                        || TradeSourceUtils.isTradePayedAndConfirmed(dinnertableTradeVo, SourceId.XIN_MEI_DA)
                        || TradeSourceUtils.isTradePayedAndConfirmed(dinnertableTradeVo, SourceId.KOU_BEI)) {
                                        post(new ActionTableInfoBottomBarStatus(BottomBarStatus.TRADE_FINISH));
                } else {
                    post(new ActionTableInfoBottomBarStatus(BottomBarStatus.TRADE_OPERATE));
                }
                deleteButton.setBackgroundColor(Color.parseColor("#ff6a4d"));
                deleteIv.setImageResource(R.drawable.dinner_table_info_delete_button_choose);
                deleteTv.setTextColor(Color.parseColor("#ffffff"));
                break;
            default:
                break;
        }
    }


    public void selectTrade(Long tradeId) {
    }

    public enum DragStatus {
        DRAGSTARTED(0),
        ENTERED(1),
        EXITED(2),
        DRAGEND(3);
        private int status;

        private DragStatus(int status) {
            this.status = status;
        }

        public int getStatus() {
            return status;
        }
    }

    public static boolean isEnableServing() {
        return enableServing;
    }


    public void showMoveDishDragMode(boolean show) {
        if (show) {
            dinnertableFragment.enableDinnertableClick(false);

            titleRL.setVisibility(View.GONE);
            moveDishTopbarRL.setVisibility(View.VISIBLE);
            talbeInfoContentBean.showMoveDishDragMode(true);

            DinnertableTradeModelMoveDish modelMoveDish = new DinnertableTradeModelMoveDish(talbeInfoContentBean.tradeInfoFragment.getSelectedDishAdapter().getCheckedIShopcartItems().size());
            modelMoveDish.setSourceTradeVo(dinnertableTradeVo);
            List<IShopcartItem> items = talbeInfoContentBean.tradeInfoFragment.getSelectedDishAdapter().getCheckedIShopcartItems();
            modelMoveDish.setItems(items);

            movedishDragView.setModel(modelMoveDish);
            movedishDragView.setEnabledDrag(true);
        } else {
            dinnertableFragment.enableDinnertableClick(true);

            titleRL.setVisibility(View.VISIBLE);
            moveDishTopbarRL.setVisibility(View.GONE);
            talbeInfoContentBean.showMoveDishDragMode(false);
        }

    }


    public void onEventMainThread(EventMoveDishNotice event) {
        if (event.result) {
            post(new ActionTableInfoBottomBarStatus(BottomBarStatus.TRADE_OPERATE));
        }

    }


    public void refreshMovedishDragView() {
        DinnerDishTradeInfoFragment tradeInfoFragment = (DinnerDishTradeInfoFragment) getFragmentManager().findFragmentByTag("dinner_table_page0");
        DinnertableTradeModelMoveDish modelMoveDish = (DinnertableTradeModelMoveDish) movedishDragView.getModel();
        List<IShopcartItem> items = tradeInfoFragment.getSelectedDishAdapter().getCheckedIShopcartItems();
        modelMoveDish.setItems(items);
        modelMoveDish.setCopyDishItemSize(items.size());
        movedishDragView.setModel(modelMoveDish);
                movedishDragView.setEnabledDrag(items.size() == 0 ? false : true);
    }


    public void refreshDishCheckTitleBar() {
                DinnertableTradeModelMoveDish modelMoveDish = new DinnertableTradeModelMoveDish(talbeInfoContentBean.tradeInfoFragment.getSelectedDishAdapter().getCheckedIShopcartItems().size());
        modelMoveDish.setSourceTradeVo(talbeInfoContentBean.getDinnerTableTradeVo());

        DinnerDishTradeInfoFragment tradeInfoFragment = (DinnerDishTradeInfoFragment) getFragmentManager().findFragmentByTag("dinner_table_page0");
        List<IShopcartItem> items = tradeInfoFragment.getSelectedDishAdapter().getCheckedIShopcartItems();
        modelMoveDish.setItems(items);

        String keyStr = null;
        if (MoreMenuPopWindow.operateType == OperateType.MOVE_DISH) {
            keyStr = getActivity().getString(R.string.dinner_movedish);            modelMoveDish.setModeType((byte) 0, 0);
        } else {            keyStr = getActivity().getString(R.string.dinner_copy);            modelMoveDish.setModeType((byte) 1, 0);
        }

        movedishDragView.setModel(modelMoveDish);
        movedishDragView.setEnabledDrag(false);
        titleTv.setText(getResources().getString(R.string.dinner_move_dish_title, keyStr));
        messageTv.setText(getResources().getString(R.string.dinner_move_dish_message, keyStr));

    }


    public void addCopydishTimes() {
        DinnertableTradeModelMoveDish modelMoveDish = (DinnertableTradeModelMoveDish) movedishDragView.getModel();
        modelMoveDish.setModeType((byte) 1, modelMoveDish.getCopyCount() + 1);
        movedishDragView.setModel(modelMoveDish);
    }


    public void initialDishAllCheckBar(boolean show) {
        if (show) {
            dishOperateCheckbar.setVisibility(View.VISIBLE);
            allCheckCb.setOnClickListener(checkAllClickLisenter);
            DinnerShopCartAdapter adapter = talbeInfoContentBean.tradeInfoFragment.getSelectedDishAdapter();

            checkNumberTv.setText(getString(R.string.dinner_orderdish_dishcheck_number, String.valueOf(adapter.getCheckedNumber())));
            allCheckCb.setChecked(adapter.isCheckedAll());

        } else {
            dishOperateCheckbar.setVisibility(View.GONE);
        }
    }


    private View.OnClickListener checkAllClickLisenter = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.allcheck_cb) {                talbeInfoContentBean.tradeInfoFragment.getSelectedDishAdapter().checkAllDishForMoveDish(allCheckCb.isChecked());
                checkNumberTv.setText(getString(R.string.dinner_orderdish_dishcheck_number,
                        String.valueOf(talbeInfoContentBean.tradeInfoFragment.getSelectedDishAdapter().getCheckedNumber())));
                if (DinnerDishTradeInfoFragment.isMoveDishDragMode()) {
                    refreshMovedishDragView();                }
            }

        }
    };

    public static int getCurrentPosition() {
        return currentPosition;
    }

    public static void setCurrentPosition(int currentPos) {
        currentPosition = currentPos;
    }

    public static int getUnionTradePositon() {
        return unionTradePositon;
    }

    public static void setUnionTradePositon(int unionTradePos) {
        unionTradePositon = unionTradePos;
    }

    @Click(R.id.ll_number_and_waiter)
    public void clickLLNumberAndwaiter(View v) {
        if (dinnertableTradeVo == null) {
            return;
        }

        if (!ClickManager.getInstance().isClicked()) {
                        if (TradeSourceUtils.isTradeUnProcessed(dinnertableTradeVo, SourceId.WECHAT)                     || TradeSourceUtils.isTradeUnProcessed(dinnertableTradeVo, SourceId.DIANPING)
                    || TradeSourceUtils.isTradeUnProcessed(dinnertableTradeVo, SourceId.XIN_MEI_DA)
                    || TradeSourceUtils.isTradeUnProcessed(dinnertableTradeVo, SourceId.KOU_BEI)
                    || TradeSourceUtils.isTradePayedUnAcceptFromFAMILIAR(dinnertableTradeVo)
                    || TradeSourceUtils.isTradePayedUnAcceptFromOpenPlatform(dinnertableTradeVo)
                    || TradeSourceUtils.isTradePayedUnAcceptFromBAIDURICE(dinnertableTradeVo)) {
                ToastUtil.showShortToast(R.string.dinner_table_need_accept);
                return;
            }

            if (!TradeStatusUtil.checkPayStatus(dinnertableTradeVo) && getBusinessType() == BusinessType.BUFFET) {
                ToastUtil.showShortToast(getString(R.string.dinner_paying));
                return;
            }

                        if (AsyncNetworkManager.getInstance().queryNotSuccess(getActivity(), dinnertableTradeVo.getTradeVo().getTrade().getId())) {
                return;
            }

            MobclickAgentEvent.onEvent(getActivity(), DinnerMobClickAgentEvent.tableDetailsModifyCustomer);
            if (ll_numberAndWaiterGroup.getVisibility() == View.VISIBLE) {
                ll_numberAndWaiterGroup.setVisibility(View.GONE);
                ivNumberAndWaiterTip.setVisibility(View.GONE);
            } else {
                ll_numberAndWaiterGroup.setVisibility(View.VISIBLE);
                ivNumberAndWaiterTip.setVisibility(View.VISIBLE);
                ll_numberAndWaiterGroup.removeAllViews();
                ll_numberAndWaiterGroup.addView(getNumberAndWaiterView());
                setNumberAndWaiter(dinnertableTradeVo);
            }
        }
    }


    public boolean hideNumberAndWaiterWindow(MotionEvent event) {
        if (ll_numberAndWaiterGroup != null && ll_numberAndWaiterGroup.getVisibility() == View.VISIBLE) {
                        int touchScreenX = (int) event.getRawX();
            int touchScreenY = (int) event.getRawY();

                        int[] locations = new int[2];
            ll_numberAndWaiterGroup.getLocationOnScreen(locations);
            int x = locations[0];
            int y = locations[1];
            int width = ll_numberAndWaiterGroup.getWidth();
            int height = ll_numberAndWaiterGroup.getHeight();

                        if (!(touchScreenX >= x && touchScreenX <= (x + width)
                    && touchScreenY >= y && touchScreenY <= (y + height))) {
                ll_numberAndWaiterGroup.setVisibility(View.GONE);
                ivNumberAndWaiterTip.setVisibility(View.GONE);
                return true;
            }
        }
        return false;
    }




    protected void updateNumberAndWaiter() {
                TradeVo tradeVo = dinnertableTradeVo.getTradeVo();
        String customerCount = "";
        String waiterName = "";
        if (tradeVo != null && Utils.isNotEmpty(tradeVo.getTradeTableList())) {
            llNumberAndWaiter.setVisibility(View.VISIBLE);
            TradeTable tradeTable = tradeVo.getTradeTableList().get(0);
            customerCount = String.valueOf(tradeTable.getTablePeopleCount());

            waiterName = tradeTable.getWaiterName();
            if (!TextUtils.isEmpty(waiterName)) {
                waiterName = Utils.getDisplayName(waiterName);
            } else {
                waiterName = "";
            }
        } else {
            waiterName = Session.getAuthUser().getName();
        }

        tvCustomerCount.setText(String.format(getString(R.string.dinner_guests_hint), TextUtils.isEmpty(customerCount) ? "" : (customerCount + "/" + dataManage.getMaxSeatNuber())));
        tvWaiterName.setText(String.format(getString(R.string.dinner_waiter_hint), waiterName));
    }


    private void refreshJoinTradeView() {
                if (isShowJoinTrade()) {
            flJoinTrade.setVisibility(View.VISIBLE);
            tvJoinTableDown.setVisibility(View.VISIBLE);
            ivJoinTradeIndicator.setVisibility(View.INVISIBLE);
        } else {
            flJoinTrade.setVisibility(View.GONE);
        }
    }


    private void refreshTradeView() {
                if (dataManage.getAllTrade().size() > 1 && dinnertableTradeVo.getTableId() == dataManage.getTableId()) {
            tradeBackIv.setVisibility(View.VISIBLE);
        } else {
            tradeBackIv.setVisibility(View.INVISIBLE);
        }

        tradeView.enablePreCashDisplay(false);
        tradeView.setEnableTransparent(false);

        tradeView.setModel(dinnertableTradeVo.getDinnertableTrade());
        if (dataManage.getUnionMainInfo() != null) {
            flJoinTrade.setModel(dataManage.getUnionMainInfo().getiDinnertableTrade());
        }

        if (dinnertableTradeVo.getInfo().getTradeType() == TradeType.UNOIN_TABLE_MAIN) {
            fl_tradeview.setVisibility(View.INVISIBLE);
        } else {
            fl_tradeview.setVisibility(View.VISIBLE);
        }

        if (dataManage.isUnionTrade()) {
            addOrderBtn.setVisibility(View.GONE);
        } else {
            addOrderBtn.setVisibility(View.VISIBLE);
        }

                if (dinnertableTradeVo.getStatus() == DinnertableStatus.EMPTY
                || TradeSourceUtils.isTradeUnProcessed(dinnertableTradeVo, SourceId.WECHAT)                 || TradeSourceUtils.isTradePayedAndConfirmed(dinnertableTradeVo, SourceId.DIANPING)
                || TradeSourceUtils.isTradePayedAndConfirmed(dinnertableTradeVo, SourceId.XIN_MEI_DA)
                || TradeSourceUtils.isTradePayedAndConfirmed(dinnertableTradeVo, SourceId.KOU_BEI)
                || TradeSourceUtils.isTradePayedUnAcceptFromBAIDURICE(dinnertableTradeVo)
                || TradeSourceUtils.isTradePayedUnAcceptFromFAMILIAR(dinnertableTradeVo)
                || TradeSourceUtils.isTradePayedUnAcceptFromOpenPlatform(dinnertableTradeVo)
                || TradeSourceUtils.isTradePayedFromShuKe(dinnertableTradeVo)
                || TableInfoContentBean.isAsyncHttpExcuting(dinnertableTradeVo)) {            tradeView.setEnabledDrag(false);
        } else {
            tradeView.setEnabledDrag(true);
        }

                if (TradeSourceUtils.isTradeUnProcessed(dinnertableTradeVo, SourceId.WECHAT)) {
            tradeTypeIv.setVisibility(View.VISIBLE);
            tradeTypeIv.setImageResource(R.drawable.dinner_table_wechat);
        } else if (TradeSourceUtils.isTradePayedAndConfirmed(dinnertableTradeVo, SourceId.DIANPING)) {
            tradeTypeIv.setVisibility(View.VISIBLE);
            tradeTypeIv.setImageResource(R.drawable.dinner_table_dianping);
        } else if (TradeSourceUtils.isTradePayedAndConfirmed(dinnertableTradeVo, SourceId.XIN_MEI_DA)) {
            tradeTypeIv.setVisibility(View.VISIBLE);
            tradeTypeIv.setImageResource(R.drawable.dinner_table_xinmeida);
        } else if (TradeSourceUtils.isTradeUnProcessed(dinnertableTradeVo, SourceId.KOU_BEI)) {
            tradeTypeIv.setVisibility(View.VISIBLE);
            tradeTypeIv.setImageResource(R.drawable.dinner_table_koubei);
        } else if (TradeSourceUtils.isTradePayedUnAcceptFromBAIDURICE(dinnertableTradeVo) || TradeSourceUtils.isTradePayedAcceptedFromBAIDURICE(dinnertableTradeVo)) {
                        tradeTypeIv.setVisibility(View.VISIBLE);
            tradeTypeIv.setImageResource(R.drawable.dinner_table_nuomi);
        } else if (TradeSourceUtils.isTradePayedFromShuKe(dinnertableTradeVo)) {
            tradeTypeIv.setVisibility(View.VISIBLE);
            tradeTypeIv.setImageResource(R.drawable.dinner_table_shuke);
        } else if (hasAddDish(dinnertableTradeVo)) {            tradeTypeIv.setVisibility(View.VISIBLE);
            tradeTypeIv.setImageResource(R.drawable.add_dish_icon);
        } else if (dinnertableTradeVo.getDinnertableTrade().getPreCashPrintStatus() == YesOrNo.YES && SharedPreferenceUtil.getSpUtil().getBoolean(TableFragment.SHOW_PRECASH_KEY, false)) {
            tradeTypeIv.setVisibility(View.VISIBLE);
            tradeTypeIv.setImageResource(R.drawable.dinner_table_print_precash_icon);
        } else {
            tradeTypeIv.setVisibility(View.GONE);
        }

    }


    public void onEventMainThread(EventRefreshTradeAsyncHttp event) {
        if (tradeView != null) {
            tradeView.showHttpRecord();
        }
    }


    private void finishOpenTable() {
        setCurrentPosition(dataManage.getAllTrade().size());
        OpentablePopWindow.isQuickOpentable = true;
        OpenTableManager manager = new OpenTableManager(dataManage.getDinnerTableVo(), getEmptyTradeDefaultCustomerNumber(), getActivity(), dinnertableFragment.getBussinessType());
        manager.finishOpenTable();
    }

    public boolean isTradeGridWindowVisible() {
        if (tradeGridView != null && tradeGridView.getVisibility() == View.VISIBLE) {
            return true;
        } else {
            return false;
        }
    }


    public boolean hideTradeGridWindow(MotionEvent event) {
        if (tradeGridView != null && tradeGridView.getVisibility() == View.VISIBLE) {
            if (!DensityUtil.isRangeOfView(tradeGridView, event)) {
                tradeGridView.setVisibility(View.GONE);
                indicatorIv.setVisibility(View.INVISIBLE);
                ivJoinTradeIndicator.setVisibility(View.INVISIBLE);
                return true;
            }
        }
        return false;
    }


    public boolean hideOpenTableWindow(MotionEvent event) {
        return hideOpenTableWindow(event, false);
    }

    private boolean hideOpenTableWindow(MotionEvent event, boolean forceDo) {
        if (addOrderBtn != null && addOrderBtn.getTag() != null) {
            if (forceDo || (event != null && !DensityUtil.isRangeOfView(addOrderBtn, event))) {
                addOrderBtn.setImageResource(R.drawable.dinner_table_info_add_order);
                addOrderTv.setVisibility(View.GONE);
                addOrderBtn.setTag(null);
                return true;
            }
        }

        return false;
    }


    private void chooseTrade(int position) {
        tradeGridView.choosePosition(position);
        indicatorIv.setVisibility(View.INVISIBLE);
        tradeGridView.setVisibility(View.GONE);
        TableInfoFragment.setCurrentPosition(position);
        refreshView();
    }



    private void chooseUnionTrade(int position) {
        tradeGridView.choosePosition(position);
        ivJoinTradeIndicator.setVisibility(View.INVISIBLE);
        tradeGridView.setVisibility(View.GONE);
        TableInfoFragment.setUnionTradePositon(position);
        refreshView();
        EventBus.getDefault().post(new EventSelectDinnertableNotice(dinnertableTradeVo.getZoneId(), dinnertableTradeVo.getTableId(), null, false));
    }

    public void enableAddOrderBtn(boolean enable) {
        addOrderBtn.setEnabled(enable);
    }


    private int getEmptyTradeDefaultCustomerNumber() {
        int realCustomerNumber = TableInfoFragment.getDataManager().getTableRealCustomerNumber();
        int tableMaxCustomerNumber = TableInfoFragment.getDataManager().getMaxSeatNuber();
        int defaultCustomerNumber = 0;
        if (realCustomerNumber == 0) {            defaultCustomerNumber = tableMaxCustomerNumber;
        } else if (realCustomerNumber < tableMaxCustomerNumber) {            defaultCustomerNumber = tableMaxCustomerNumber - realCustomerNumber;
        } else {            defaultCustomerNumber = 2;
        }
        return defaultCustomerNumber;
    }


    public void refreshAddItemBatchView() {
        List<AddItemBatchBean> beans = getAddItemBatchBean();
        if (beans == null) {
            addItemBatchView.setVisibility(View.GONE);
        } else {
            addItemBatchView.setVisibility(View.VISIBLE);

            addItemBatchView.setData(beans, -1);
            addItemBatchView.setItemClick(new AddItemBatchView.ItemClick() {
                @Override
                public void onItemCick(int position, AddItemBatchBean bean) {
                    setTradeModelType(DataManage.MODELTYPE_ADDTIEM);
                    Log.i("zhubo", "position:" + position);
                    choosePositionAddItem = position;
                    TradeVo addDishTradeVo = CreateTradeVoFromAdddish.create(bean.getAddItemVo(), dinnertableTradeVo);
                    List<IShopcartItem> shopcartItems = CreateTradeVoFromAdddish.getShopcartItems(addDishTradeVo);
                    talbeInfoContentBean.tradeInfoFragment.updateOrderDishList(shopcartItems, addDishTradeVo, false, false, false);
                    post(new ActionTableInfoBottomBarStatus(BottomBarStatus.CHOOSE_ADD_DISH_ITEM));

                    shopcartItemsAddDish = shopcartItems;                }
            });
        }
    }


    private List<AddItemBatchBean> getAddItemBatchBean() {
        DinnertableTradeModel model = (DinnertableTradeModel) dinnertableTradeVo.getDinnertableTrade();
        List<AddItemVo> addItemVos = model.getAddItemVos();

        if (addItemVos != null && addItemVos.size() != 0) {
            int index = 1;
            List<AddItemBatchBean> beans = new ArrayList<>();
            for (AddItemVo addItemVo : addItemVos) {
                AddItemBatchBean bean = new AddItemBatchBean();
                bean.setSequenceNumber(String.valueOf(index));
                bean.setTime(addItemVo.getmAddItemBatch().getServerCreateTime());
                bean.setAddItemVo(addItemVo);
                beans.add(bean);
                index++;
            }
            return beans;
        } else {
            return null;
        }
    }


    public AddItemVo getChooseAddItemVo() {
        if (dinnertableTradeVo == null) {
            return null;
        }
        DinnertableTradeModel model = (DinnertableTradeModel) dinnertableTradeVo.getDinnertableTrade();
        List<AddItemVo> addItemVos = model.getAddItemVos();
        if (choosePositionAddItem != -1 && addItemVos != null && addItemVos.size() > choosePositionAddItem) {
            return addItemVos.get(choosePositionAddItem);
        }
        return null;
    }

    public List<IShopcartItem> getShopcartItemsAddDish() {
        return shopcartItemsAddDish;
    }


    public static boolean hasAddDish(DinnertableTradeVo dinnertableTradeVo) {
        DinnertableTradeModel model = (DinnertableTradeModel) dinnertableTradeVo.getDinnertableTrade();
        List<AddItemVo> addItemVos = model.getAddItemVos();
        if (addItemVos != null && addItemVos.size() != 0) {
            return true;
        } else {
            return false;
        }

    }

    public boolean isAddDishMode() {
        return dataManage.isAddItemModel(mTradeModelType);
    }

    public void resetView() {
                dinnertableFragment.enableDinnertableClick(true);

        titleRL.setVisibility(View.VISIBLE);
        moveDishTopbarRL.setVisibility(View.GONE);

        talbeInfoContentBean.resetView();
    }

    public void hideNumberAndWaiterView(int visiable) {
        if (ivNumberAndWaiterTip != null) {
            ivNumberAndWaiterTip.setVisibility(visiable);
        }
        if (ll_numberAndWaiterGroup != null) {
            ll_numberAndWaiterGroup.setVisibility(View.GONE);
        }
    }

    public BusinessType getBusinessType() {
        if (dinnertableFragment != null) {
            return dinnertableFragment.getBussinessType();
        }
        return BusinessType.DINNER;
    }

    public TradeType getTradeType() {
        if (dinnertableTradeVo != null) {
            return dinnertableTradeVo.getDinnertableTrade().getTradeType();
        }
        return null;
    }

    public void setTradeModelType(int tradeModelType) {
        this.mTradeModelType = tradeModelType;
    }

    public abstract View getNumberAndWaiterView();

    public abstract void setNumberAndWaiter(DinnertableTradeVo dinnertableTradeVo);

    public abstract boolean isShowJoinTrade();
}
