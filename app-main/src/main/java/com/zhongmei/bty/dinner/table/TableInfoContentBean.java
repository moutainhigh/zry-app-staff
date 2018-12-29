package com.zhongmei.bty.dinner.table;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.zhongmei.bty.basemodule.async.event.ActionOpenTable;
import com.zhongmei.bty.basemodule.async.manager.AsyncNetworkManager;
import com.zhongmei.bty.basemodule.auth.application.DinnerApplication;
import com.zhongmei.bty.basemodule.auth.permission.manager.AuthLogManager;
import com.zhongmei.bty.basemodule.commonbusiness.enums.ReasonSource;
import com.zhongmei.bty.basemodule.customer.entity.RejectReason;
import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.bty.basemodule.shopmanager.handover.manager.ServerSettingManager;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.shoppingcart.utils.MathShoppingCartTool;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeInfo;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeModel;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeVo;
import com.zhongmei.bty.basemodule.trade.bean.Reason;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.constants.DinnerTableConstant;
import com.zhongmei.bty.basemodule.trade.enums.DinnertableStatus;
import com.zhongmei.bty.basemodule.trade.enums.OperateType;
import com.zhongmei.bty.basemodule.trade.event.ActionTableInfoBottomBarStatus;
import com.zhongmei.bty.basemodule.trade.event.ActionTableInfoBottomBarStatus.BottomBarStatus;
import com.zhongmei.bty.basemodule.trade.message.TradeFinishResp;
import com.zhongmei.bty.basemodule.trade.message.TradeResp;
import com.zhongmei.bty.basemodule.trade.operates.TradeOperates;
import com.zhongmei.bty.buffet.orderdish.DepositInfoDialog;
import com.zhongmei.bty.cashier.ordercenter.view.OrderCenterOperateDialogFragment;
import com.zhongmei.bty.cashier.ordercenter.view.OrderCenterOperateDialogFragment.OperateListener;
import com.zhongmei.bty.cashier.ordercenter.view.OrderCenterOperateDialogFragment.OperateResult;
import com.zhongmei.bty.cashier.util.TradeSourceUtils;
import com.zhongmei.bty.cashier.util.TradeStatusUtil;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.database.entity.local.AsyncHttpRecord;
import com.zhongmei.bty.commonmodule.database.enums.AsyncHttpState;
import com.zhongmei.bty.commonmodule.database.enums.OrderActionEnum;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.bty.commonmodule.util.BuffetMobClickAgentEvent;
import com.zhongmei.bty.commonmodule.util.DateUtil;
import com.zhongmei.bty.commonmodule.util.DinnerMobClickAgentEvent;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.dinner.Listener.DishOptListener;
import com.zhongmei.bty.dinner.action.ActionSaveDishService;
import com.zhongmei.bty.dinner.manager.DinnerTradeItemManager;
import com.zhongmei.bty.dinner.ordercenter.view.DinnerChooseTableDialogFragment;
import com.zhongmei.bty.dinner.ordercenter.view.DinnerChooseTableDialogFragment_;
import com.zhongmei.bty.dinner.orderdish.DinnerDishTradeInfoFragment;
import com.zhongmei.bty.dinner.orderdish.DinnerDishTradeInfoFragment_;
import com.zhongmei.bty.dinner.orderdish.manager.DinnerDishManager;
import com.zhongmei.bty.dinner.shopcart.adapter.DinnerShopCartAdapter;
import com.zhongmei.bty.dinner.table.event.EventInsertDinnerNotice;
import com.zhongmei.bty.dinner.table.event.EventTradeAmount;
import com.zhongmei.bty.dinner.table.manager.DinnertableManager;
import com.zhongmei.bty.dinner.table.view.MoreMenuPopWindow;
import com.zhongmei.bty.dinner.table.view.OpentablePopWindow;
import com.zhongmei.bty.dinner.table.view.TableFragmentBase;
import com.zhongmei.bty.pay.manager.DoPayManager;
import com.zhongmei.bty.takeout.ordercenter.ui.RefuseDialog;
import com.zhongmei.bty.takeout.ordercenter.ui.RefuseDialog.RefuseLisetner;
import com.zhongmei.yunfu.Constant;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.context.util.helper.SpHelper;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.PrintOperationOpType;
import com.zhongmei.yunfu.db.enums.SourceId;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.db.enums.TableStatus;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.resp.EventResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.yunfu.util.DialogUtil;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.util.ToastUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * @Date：2015-9-6
 * @Description: 商品信息和开台信息标题和内容封装
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
@EBean
public class TableInfoContentBean {
    private List<View> listViews; // Tab页面列表

    private int offset = 0;// 动画图片偏移量

    private int bmpW;// 动画图片宽度

//    public static boolean needJumpToDishWindow = false;//是否需要跳转到点菜界面

    @ViewById(R.id.newtrade_operate_ll)
    LinearLayout newTradeOperateLL;//微信订单操作栏

    @ViewById(R.id.accept_btn)
    Button acceptBtn;

    @ViewById(R.id.refuse_btn)
    Button refuseBtn;

    @ViewById(R.id.operate_ll)
    LinearLayout bottomOperateLL;

    @ViewById(R.id.delete_trade_ll)
    LinearLayout delteTradeLL;

    @ViewById(R.id.dish_service_ll)
    LinearLayout dishServiceLL;

    @ViewById(R.id.trade_operate_ll)
    LinearLayout tradeOperateLL;

    @ViewById(R.id.open_table_ll)
    LinearLayout openTableLL;

    @ViewById(R.id.operate_seperate_line)
    View operateSeperateLine;

    @ViewById(R.id.finish_trade_ll)
    LinearLayout finishTradeLL;

    @ViewById(R.id.move_dish_operate_ll)
    LinearLayout moveDishOperateLL;//移菜选择按钮

    @ViewById(R.id.cancel_move_dish_ll)
    LinearLayout cancelMoveDishLL;//移菜拖动界面取消按钮

    @ViewById(R.id.settle_btn)
    Button settleBtn;//结算按钮

    @ViewById(R.id.order_dish_btn)
    Button mBtnOrderDish;

    @ViewById(R.id.change_table_ll)
    LinearLayout changeTableLL;//该单保存

    @ViewById(R.id.cancel_move_dish_btn)
    Button cancelMoveDishBtn;

    //起菜、崔菜底部栏
    @ViewById(R.id.dish_operate_bottombar_ll)
    LinearLayout dishOperateLL;

    @ViewById(R.id.cancel_btn_dishoperate)
    Button dishOperateCancelBtn;

    @RootContext
    Context context;

    @RootContext
    FragmentActivity activity;

    private DinnertableTradeVo dinnerTableTradeVo;

    TableInfoFragment tableInfoFragment;

    public DinnerDishTradeInfoFragment tradeInfoFragment;//商品页

    private TableFragmentBase tableFragment;


    @AfterViews
    public void initialViews() {
        tableFragment = (TableFragmentBase) activity.getSupportFragmentManager().findFragmentByTag(TableFragmentBase.TAG_TABLE_FRAGMENT);
        initialContent();
        registerEventBus();
    }

    @Click({R.id.accept_btn, R.id.refuse_btn, R.id.done_btn, R.id.save_btn,
            R.id.cancel_btn, R.id.cancel_btn, R.id.pay_btn, R.id.settle_btn, R.id.order_dish_btn, R.id.finish_trade_btn,
            R.id.cancel_choose_dish_btn, R.id.start_move_dish_btn, R.id.cancel_move_dish_btn, R.id.open_table_btn,
            R.id.save_change_table_btn, R.id.cancel_btn_dishoperate, R.id.done_btn_dishoperate})
    void onclick(View v) {
        switch (v.getId()) {
            case R.id.accept_btn://接受
                if (!ClickManager.getInstance().isClicked()) {
                    MobclickAgentEvent.onEvent(context, DinnerMobClickAgentEvent.tableDetailsAccept);
                    if (newTradeOperateLL.getTag() == null) {
                        verifyAccept();
                    } else {
                        //判断是否有订单是否可操作
                        if (AsyncNetworkManager.getInstance().queryNotSuccess(context, dinnerTableTradeVo.getTradeVo().getTrade().getId())) {
                            return;
                        }
                        //微信加菜，要先接受原订单
                        if (dinnerTableTradeVo.getTradeVo().getTrade().getTradeStatus() == TradeStatus.UNPROCESSED) {
                            ToastUtil.showShortToast(R.string.dinner_table_need_accept_original);
                            return;
                        }
                        acceptAddItem();
                    }

                }

                break;
            case R.id.refuse_btn://拒绝
                if (!ClickManager.getInstance().isClicked()) {
                    MobclickAgentEvent.onEvent(context, DinnerMobClickAgentEvent.tableDetailsRefuse);
                    if (newTradeOperateLL.getTag() == null) {
                        verifyRefuse();
                    } else {
                        //判断是否有订单是否可操作
                        if (AsyncNetworkManager.getInstance().queryNotSuccess(context, dinnerTableTradeVo.getTradeVo().getTrade().getId())) {
                            return;
                        }
                        refuseAddItem();
                    }

                }

                break;
            case R.id.done_btn:
                if (!ClickManager.getInstance().isClicked()) {
//                    TableInfoContentBean.needJumpToDishWindow = true;
                    post(new ActionOpenTable());//开台

                }
                break;

            case R.id.save_btn:
                if (!ClickManager.getInstance().isClicked()) {
                    MobclickAgentEvent.onEvent(context, DinnerMobClickAgentEvent.tableDetailsSure);
                    //判断是否有订单是否可操作
                    if (AsyncNetworkManager.getInstance().queryNotSuccess(context, dinnerTableTradeVo.getTradeVo().getTrade().getId())) {
                        return;
                    }
                    post(new ActionSaveDishService());//保存菜品状态
                }
                break;
            case R.id.cancel_btn://取消滑菜操作
                if (dinnerTableTradeVo != null) {
                    //百糯先付订单未接受时,百糯先付订单接受后 不改变按钮状态
                    if (TradeSourceUtils.isTradePayedUnAcceptFromBAIDURICE(dinnerTableTradeVo)) {
                        MobclickAgentEvent.onEvent(context, DinnerMobClickAgentEvent.tableDetailsCancel);
                        post(new ActionTableInfoBottomBarStatus(BottomBarStatus.NEW_TRADE_OPERATE));
                        break;
                    } else if (TradeSourceUtils.isTradePayedAcceptedFromBAIDURICE(dinnerTableTradeVo)) {
                        post(new ActionTableInfoBottomBarStatus(BottomBarStatus.TRADE_FINISH));
                        break;
                    }
                }
                post(new ActionTableInfoBottomBarStatus(BottomBarStatus.TRADE_OPERATE));
                break;
            case R.id.settle_btn://结账
                //判断是否有订单是否可操作
                if (AsyncNetworkManager.getInstance().queryNotSuccess(context, dinnerTableTradeVo.getTradeVo().getTrade().getId())) {
                    return;
                }

                if (dinnerTableTradeVo.getTradeVo() != null) {
                    if (!dinnerTableTradeVo.getTradeVo().buffetOrderOperationLimit()) {
                        ToastUtil.showShortToast(context.getString(R.string.buffet_order_op_limit_toast));
                        return;
                    }
                }

//                TradeVo tradeVo = dinnerTableTradeVo.getTradeVo();
//                if(tradeVo != null && tradeVo.getTradePrivileges() != null && tradeVo.getTradePrivileges().size() >0
//                        || tradeVo.getTrade().getPrivilegeAmount().compareTo(BigDecimal.ZERO) != 0
//                        || (tradeVo.getExtraChargeMap() != null && tradeVo.getExtraChargeMap().size() != 0)){
//
//                    new CommonDialogFragment.CommonDialogFragmentBuilder().title(activity.getResources().getString(R.string.can_not_into_pay))
//                            .iconType(R.drawable.common_dialog_icon_warning)
//                            .negativeText(R.string.know)
//                            .negativeLisnter(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View arg0) {
//                                    return;
//                                }
//                            })
//                            .build()
//                            .show(activity.getSupportFragmentManager(), activity.getClass().getSimpleName());
//                }else{
                if (!ClickManager.getInstance().isClicked()) {
                    MobclickAgentEvent.onEvent(activity, MobclickAgentEvent.dinnerTableClickPayButton);
                    verifyPay();
                }

//                }

                break;
            case R.id.pay_btn://结算（没用）
                if (!ClickManager.getInstance().isClicked()) {
                    //判断是否有订单是否可操作
                    if (AsyncNetworkManager.getInstance().queryNotSuccess(context, dinnerTableTradeVo.getTradeVo().getTrade().getId())) {
                        return;
                    }

                    MobclickAgentEvent.onEvent(activity, MobclickAgentEvent.dinnerTableClickSettleButton);
                    goSettlePage();
                }

                break;
            case R.id.order_dish_btn://点菜
                if (!TradeStatusUtil.checkPayStatus(tableInfoFragment.getDinnerTableTradeVo()) && tableInfoFragment.getBusinessType() != BusinessType.BUFFET) {
                    ToastUtil.showShortToast(context.getString(R.string.dinner_paying));
                    return;
                }

                if (dinnerTableTradeVo.getTradeVo() != null) {
                    if (!dinnerTableTradeVo.getTradeVo().buffetOrderOperationLimit()) {
                        ToastUtil.showShortToast(context.getString(R.string.buffet_order_op_limit_toast));
                        return;
                    }
                }

                if (!ClickManager.getInstance().isClicked()) {
                    //判断是否有订单是否可操作
                    if (AsyncNetworkManager.getInstance().queryNotSuccess(context, dinnerTableTradeVo.getTradeVo().getTrade().getId())) {
                        return;
                    }

                    MobclickAgentEvent.onEvent(activity, DinnerMobClickAgentEvent.tableDetailsOrderdish);
                    OpentablePopWindow.isQuickOpentable = false;
                    goToDishWindow();
                }

                break;
            case R.id.finish_trade_btn:
                MobclickAgentEvent.onEvent(activity, DinnerMobClickAgentEvent.tableDetailsFinish);
                finishTrade(dinnerTableTradeVo.getTradeVo().getTrade().getId());
                break;
            case R.id.cancel_choose_dish_btn://取消移菜
                if (!ClickManager.getInstance().isClicked()) {
                    post(new ActionTableInfoBottomBarStatus(BottomBarStatus.TRADE_OPERATE));
                }
                break;
            case R.id.start_move_dish_btn://开始移菜
                if (!ClickManager.getInstance().isClicked()) {
                    if (tradeInfoFragment.getSelectedDishAdapter().getCheckedNumber() == 0) {
                        ToastUtil.showShortToast(R.string.dinner_move_dish_not_choosedish);
                        return;
                    }
                    if (tableInfoFragment != null) {
                        tableInfoFragment.showMoveDishDragMode(true);
                    }
                }
                break;
            case R.id.cancel_move_dish_btn:
                if (!ClickManager.getInstance().isClicked()) {
                    DinnerShopCartAdapter.setDishCheckMode(false);
                    post(new ActionTableInfoBottomBarStatus(BottomBarStatus.TRADE_OPERATE));
                    if (tableInfoFragment.event != null) {
                        post(tableInfoFragment.event);
                    }
                }
                break;
            case R.id.open_table_btn:
                if (!ClickManager.getInstance().isClicked()) {
//                    TableInfoContentBean.needJumpToDishWindow = false;
                    post(new ActionOpenTable());//只开台不跳转

                }

                break;
            case R.id.save_change_table_btn:
                if (!ClickManager.getInstance().isClicked()) {
                    post(new ActionOpenTable());//改台（修改人数）
                }
                break;
            case R.id.cancel_btn_dishoperate://取消起菜或者崔菜
                cancelDishOperate();
                break;
            case R.id.done_btn_dishoperate://完成起菜或者崔菜
                finishDishOperate();
                break;
            default:
                break;
        }
    }

    public void initialContent() {
        FragmentManager manager = activity.getSupportFragmentManager();
//        tableInfoFragment = (TableInfoFragment) activity.getSupportFragmentManager().findFragmentByTag(DinnertableFragment.CONTROL_FRAGMENT_TAG);
        tableInfoFragment = tableFragment.getInfoFragment();
        tradeInfoFragment = new DinnerDishTradeInfoFragment_();
        // 初始化参数
        Bundle bundle = new Bundle();
        bundle.putInt(DinnerDishTradeInfoFragment.DISHSHOPCART_PAGE, DinnerDishTradeInfoFragment.PAGE_TABLE);
        tradeInfoFragment.setArguments(bundle);
        tradeInfoFragment.setBusinessType(tableFragment.getBussinessType());
        tableInfoFragment.addFragment(R.id.content2_ll, tradeInfoFragment, "dinner_table_page0");

        Log.i("zhubo", "初始化加入frament");
    }

    public void refreshView(final DinnertableTradeVo tradeVo) {
        dinnerTableTradeVo = tradeVo;
       /* // 开台后刷新时需要跳转
        if(needJumpToDishWindow){
            goToDishWindow();
            needJumpToDishWindow=false;

            OpentablePopWindow.getInstance(activity).hide();
            tableInfoFragment.enableAddOrderBtn(true);
            return;
        }*/

        refreshGoodsInfo(tradeVo);
        showOperateButton(tradeVo);
        if (tradeVo != null && tradeVo.getTradeVo() != null && tradeVo.getTradeVo().isBuffetUnionMainTrade()) {
            mBtnOrderDish.setVisibility(View.GONE);
            settleBtn.setVisibility(View.VISIBLE);
        } else if (tradeVo != null && tradeVo.getTradeVo() != null && tradeVo.getTradeVo().isBuffetUnionSubTrade()) {
            mBtnOrderDish.setVisibility(View.VISIBLE);
            settleBtn.setVisibility(View.GONE);
        } else {
            mBtnOrderDish.setVisibility(View.VISIBLE);
            settleBtn.setVisibility(View.VISIBLE);
        }
    }

    /**
     * @param tradeVo
     * @Date 2015年9月25日
     * @Description: 刷新商品信息列表
     * @Return void
     */
    private void refreshGoodsInfo(DinnertableTradeVo tradeVo) {
        if (tradeInfoFragment != null) {
            if (tradeVo != null && tradeVo.getItems() != null) {
                DinnerShoppingCart.getInstance().initTableTradeInfo(tradeVo.getUnionMainTradeInfo(), tradeVo.getInfo());
                tradeInfoFragment.updateOrderDishList(tradeVo.getItems(), tradeVo.getTradeVo());
            } else {
                tradeInfoFragment.updateOrderDishList(null, null);
            }
        }
    }

    /**
     * @Date 2015年11月2日
     * @Description: 显示微信操作栏
     * @Return void
     */
    public void showOperateButton(DinnertableTradeVo tradeVo) {
        if (isTradeEmpty(tradeVo)) {
            post(new ActionTableInfoBottomBarStatus(BottomBarStatus.OPENTABLE));
        } else {
            if (TradeSourceUtils.isOtherPlatformTrade(tradeVo)) {
                post(new ActionTableInfoBottomBarStatus(BottomBarStatus.NEW_TRADE_OPERATE));
            } else if (TradeSourceUtils.isTradePayedAndConfirmed(tradeVo, SourceId.DIANPING)
                    || TradeSourceUtils.isTradePayedAndConfirmed(tradeVo, SourceId.XIN_MEI_DA)
                    || TradeSourceUtils.isTradePayedAndConfirmed(tradeVo, SourceId.KOU_BEI)
                    || TradeSourceUtils.isTradePayedAcceptedFromBAIDURICE(tradeVo)
                    || TradeSourceUtils.isTradePayedFromShuKe(tradeVo)
                    || TradeSourceUtils.isTradePayedFromOpenFlatform(tradeVo)) {//展示完成交易按钮
                post(new ActionTableInfoBottomBarStatus(BottomBarStatus.TRADE_FINISH));
            } else {
                if (tradeInfoFragment.isDishServingModify()) {
                    post(new ActionTableInfoBottomBarStatus(BottomBarStatus.DISH_SERVING_MODIFY));
                } else {
                    post(new ActionTableInfoBottomBarStatus(BottomBarStatus.TRADE_OPERATE));
                }
            }
        }

    }

    private void verifyAccept() {
        VerifyHelper.verifyAlert(activity, DinnerApplication.PERMISSION_DINNER_ACCEPT, new VerifyHelper.Callback() {
            @Override
            public void onPositive(User user, String code, Auth.Filter filter) {
                super.onPositive(user, code, filter);
                accept();
            }
        });
    }

    void accept() {
        if (dinnerTableTradeVo != null && dinnerTableTradeVo.getTradeVo() != null) {
            if (dinnerTableTradeVo.getTradeVo().getTrade().getSource() == SourceId.KOU_BEI) {
                final TradeVo tradeVo = dinnerTableTradeVo.getTradeVo();
                TradeExtra tradeExtra = dinnerTableTradeVo.getTradeVo().getTradeExtra();
                if (!tradeVo.isAppointmentOrder()) {//无预约时间
                    acceptKouBeiOrder(tradeVo);
                } else {//预约时间
                    boolean allowMultTrades = ServerSettingManager.allowMultiTradesOnTable();
                    String expectTime = DateUtil.format(tradeExtra.getExpectTime());
                    String hint = allowMultTrades ?
                            String.format(activity.getString(R.string.koubei_accept_order_appointment_hint2), expectTime) : String.format(activity.getString(R.string.koubei_accept_order_appointment_hint), expectTime);
                    DialogUtil.showHintConfirmDialog(activity.getSupportFragmentManager(),
                            hint,
                            R.string.common_cancel,
                            R.string.order_center_detail_confirm_accept_order,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            },
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    acceptKouBeiOrder(tradeVo);
                                }
                            }, "");
                }
            } else {
                acceptOrder();
            }
        }
    }

    void acceptOrder() {
        if (dinnerTableTradeVo != null && dinnerTableTradeVo.getTradeVo() != null) {
            final boolean isAcceptAutoTransferOpen = TradeSourceUtils.isTradeUnProcessed(dinnerTableTradeVo, SourceId.WECHAT) ? SpHelper.getDefault().getBoolean(Constant.WEIXIN_ACCEPT_TRANSFER_KITCHEN) : true;
            final boolean isPaid = dinnerTableTradeVo.getTradeVo().getTrade().getTradePayStatus() == TradePayStatus.PAID;
            final boolean isSendKitchen = isAcceptAutoTransferOpen || isPaid;
            final Long tableId = TableInfoFragment.getDataManager().getDinnerTableVo().getTableId();
            final String tradeUuid = dinnerTableTradeVo.getTradeVo().getTrade().getUuid();
            TradeOperates tradeOperates = OperatesFactory.create(TradeOperates.class);
            tradeOperates.acceptDinner(dinnerTableTradeVo.getTradeVo().getTrade(), isSendKitchen,
                    LoadingResponseListener.ensure(new EventResponseListener<TradeResp>(UserActionEvent.DINNER_TRADE_ACCEPT) {

                        @Override
                        public void onResponse(ResponseObject<TradeResp> response) {
                            ToastUtil.showLongToast(response.getMessage());
                            if (ResponseObject.isOk(response)) {
                                if (!isPaid) {
                                    EventBus.getDefault().post(new EventInsertDinnerNotice(tradeUuid, tableId));
                                }

                                TradeVo tradeVo = dinnerTableTradeVo.getTradeVo();
                                Long expectTime = tradeVo.getTradeExtra() == null ? null : tradeVo.getTradeExtra().getExpectTime();
                                //AlarmService.setDinnerAlarmPrint(context, tradeVo, isPaid, isSendKitchen, expectTime);

                                Trade trade = tradeVo.getTrade();
                                AuthLogManager.getInstance().flush(OrderActionEnum.ACTION_ACCPET_ORDER, trade.getId(), tradeUuid, trade.getClientUpdateTime());
                            } else {
                                AuthLogManager.getInstance().clear();
                                ToastUtil.showLongToast(response.getMessage());
                            }
                        }

                        @Override
                        public void onError(VolleyError error) {
                            AuthLogManager.getInstance().clear();
                            ToastUtil.showLongToast(error.getMessage());
                            // 测试
                            // EventBus.getDefault().post(new
                            // EventInsertDinnerNotice(tradeUuid,
                            // tableId));
                        }

                    }, activity.getSupportFragmentManager()));
        }
    }

    private void acceptKouBeiOrder(TradeVo tradeVo) {
        try {
            Tables table = DBHelperManager.queryById(Tables.class, tradeVo.getTradeTableList().get(0).getTableId());
            if (table.getTableStatus() == TableStatus.EMPTY || (ServerSettingManager.allowMultiTradesOnTable() && table.getTableStatus() == TableStatus.OCCUPIED)) {
                acceptOrder();
            } else {
                setTableAndAcceptDinner(tradeVo, table);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setTableAndAcceptDinner(final TradeVo tradeVo, Tables orderTable) {
        final boolean isAcceptAutoTransferOpen = TradeSourceUtils.isTradeFromWeixinUnproccess(tradeVo) ? SpHelper.getDefault().getBoolean(Constant.WEIXIN_ACCEPT_TRANSFER_KITCHEN) : true;
        final boolean isPaid = tradeVo.getTrade().getTradePayStatus() == TradePayStatus.PAID;
        final boolean isSendKitchen = isAcceptAutoTransferOpen || isPaid;

        DinnerChooseTableDialogFragment chooseTableDialogFragment = new DinnerChooseTableDialogFragment_();
        chooseTableDialogFragment.setTables(orderTable);
        chooseTableDialogFragment.setOnTableSelectedConfirmListener(new DinnerChooseTableDialogFragment.OnTableSelectedConfirmListener() {
            @Override
            public void onConfirm(Tables tables) {
                TradeOperates tradeOperates = OperatesFactory.create(TradeOperates.class);
                tradeOperates.dinnerSetTableAndAccept(tradeVo,
                        tables,
                        Bool.YES,
                        LoadingResponseListener.ensure(new ResponseListener<TradeResp>() {

                            @Override
                            public void onResponse(ResponseObject<TradeResp> response) {
                                ToastUtil.showLongToast(response.getMessage());
                                if (ResponseObject.isOk(response)) {
                                    Long expectTime = tradeVo.getTradeExtra() == null ? null : tradeVo.getTradeExtra().getExpectTime();
                                    //AlarmService.setDinnerAlarmPrint(context, tradeVo, isPaid, isSendKitchen, expectTime);

                                    Trade trade = tradeVo.getTrade();
                                    AuthLogManager.getInstance().flush(OrderActionEnum.ACTION_ACCPET_ORDER, trade.getId(), trade.getUuid(), trade.getClientUpdateTime());
                                } else {
                                    AuthLogManager.getInstance().clear();
                                    ToastUtil.showLongToast(response.getMessage());
                                }
                            }

                            @Override
                            public void onError(VolleyError error) {
                                AuthLogManager.getInstance().clear();
                                ToastUtil.showLongToast(error.getMessage());
                            }

                        }, activity.getSupportFragmentManager()));
            }
        });
        chooseTableDialogFragment.show(activity.getSupportFragmentManager(), "dinner_choose_table");
    }

    private void verifyRefuse() {
        VerifyHelper.verifyAlert(activity, DinnerApplication.PERMISSION_DINNER_ACCEPT, new VerifyHelper.Callback() {
            @Override
            public void onPositive(User user, String code, Auth.Filter filter) {
                super.onPositive(user, code, filter);
                doDialogByType("", RejectReason.REASON_TYPE_REFUSE);
            }
        });
    }

    private void doDialogByType(final String uuid, final int reasonType) {
        final SourceId sourceId = dinnerTableTradeVo.getTradeVo().getTrade().getSource();
        ReasonSource reasonSource = getReasonSourceWithKouBei(sourceId);
        OrderCenterOperateDialogFragment dialog = new OrderCenterOperateDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", reasonType);
        bundle.putInt(OrderCenterOperateDialogFragment.EXTRA_SOURCE, reasonSource.value());
        dialog.setArguments(bundle);
        dialog.registerListener(new OperateListener() {
            @Override
            public boolean onSuccess(OperateResult result) {
                return disposeTheRequest(result, reasonType);
            }

        });

        String tradeUuid = dinnerTableTradeVo.getTradeVo().getTrade().getUuid();
        dialog.setPayment(new DinnertableManager().getPaymentVo(tradeUuid));
        if (reasonType == RejectReason.REASON_TYPE_DELETE_DISH) {
            dialog.show(activity.getSupportFragmentManager(), "refund");
        } else if (reasonType == RejectReason.REASON_TYPE_REFUSE) {
            dialog.show(activity.getSupportFragmentManager(), "refuse");
        } else if (reasonType == RejectReason.REASON_TYPE_DESTROY) {
            dialog.show(activity.getSupportFragmentManager(), "destroy");
        } else if (reasonType == RejectReason.REASON_TYPE_REPAY) {
            dialog.show(activity.getSupportFragmentManager(), "repay");
        }

    }

    private ReasonSource getReasonSourceWithKouBei(SourceId sourceId) {
        return sourceId == SourceId.BAIDU_TAKEOUT ? ReasonSource.BAIDU_TAKEOUT
                : sourceId == SourceId.KOU_BEI ? ReasonSource.KOUBEI : ReasonSource.ZHONGMEI;
    }

    private boolean disposeTheRequest(OperateResult result, int reasonType) {
        TradeVo tradeVo;
        TradeOperates httpImpl = OperatesFactory.create(TradeOperates.class);
        Reason reason = result.reason;
        final boolean isPrintChecked = result.isPrintChecked;
        //String uuid = mTradePaymentVo.getTradeVo().getTrade().getUuid();
        switch (reasonType) {
            case RejectReason.REASON_TYPE_REFUSE:
                tradeVo = dinnerTableTradeVo.getTradeVo();
                if (tradeVo == null) {
                    return false;
                }
                tradeVo.getTrade().validateUpdate();
                httpImpl = OperatesFactory.create(TradeOperates.class);
                httpImpl.refuseDinner(tradeVo,
                        reason,
                        LoadingResponseListener.ensure(new EventResponseListener<TradeResp>(UserActionEvent.DINNER_TRADE_REFUSE) {

                            @Override
                            public void onResponse(ResponseObject<TradeResp> response) {
                                ToastUtil.showLongToast(response.getMessage());
                                if (ResponseObject.isOk(response)) {
                                    Trade trade = dinnerTableTradeVo.getTradeVo().getTrade();
                                    AuthLogManager.getInstance().flush(OrderActionEnum.ACTION_REJECT_ORDER, trade.getId(), trade.getUuid(), trade.getClientUpdateTime());
                                } else {
                                    AuthLogManager.getInstance().clear();
                                }
                            }

                            @Override
                            public void onError(VolleyError error) {
                                AuthLogManager.getInstance().clear();
                                ToastUtil.showLongToast(error.getMessage());
                            }
                        }, activity.getSupportFragmentManager()));
                break;
        }
        return false;
    }

    // 拒绝
    void refuse() {
        if (dinnerTableTradeVo != null && dinnerTableTradeVo.getTradeVo() != null) {
            final String tradeUuid = dinnerTableTradeVo.getTradeVo().getTrade().getUuid();

            final RefuseDialog mRefuseDialog = new RefuseDialog(activity);
            mRefuseDialog.setLisetner(new RefuseLisetner() {

                @Override
                public void refuse(String reason) {
                    mRefuseDialog.dismiss();
                    Reason refuseReason = new Reason();
                    refuseReason.setContent(reason);

                    TradeVo tradeVo = dinnerTableTradeVo.getTradeVo();
                    if (tradeVo == null) {
                        return;
                    }
                    tradeVo.getTrade().validateUpdate();
                    TradeOperates httpImpl = OperatesFactory.create(TradeOperates.class);
                    httpImpl.refuseDinner(tradeVo,
                            refuseReason,
                            LoadingResponseListener.ensure(new ResponseListener<TradeResp>() {

                                @Override
                                public void onResponse(ResponseObject<TradeResp> response) {
                                    ToastUtil.showLongToast(response.getMessage());
                                    Trade trade = dinnerTableTradeVo.getTradeVo().getTrade();
                                    AuthLogManager.getInstance().flush(OrderActionEnum.ACTION_REJECT_ORDER, trade.getId(), trade.getUuid(), trade.getClientUpdateTime());
                                }

                                @Override
                                public void onError(VolleyError error) {
                                    ToastUtil.showLongToast(error.getMessage());
                                }
                            }, activity.getSupportFragmentManager()));
                }
            });
            mRefuseDialog.show();
        }

    }

    protected void registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    protected void unregisterEventBus() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * 用EventBus发送一个事件
     *
     * @param obj
     */
    public void post(Object obj) {
        EventBus.getDefault().post(obj);
    }

    public void onEventMainThread(EventTradeAmount event) {
        int smallSize = context.getResources().getInteger(R.integer.shopcart_bottom_pay_btn_small_size);
        int bigSize = context.getResources().getInteger(R.integer.shopcart_bottom_pay_btn_big_size);
        if (event != null && !TextUtils.isEmpty(event.getTradeAmount())) {
            if (event.isPaid()) {
                SpannableString tradeStr = null;
                if (event.isNeedDeposit()) {
                    tradeStr = new SpannableString(context.getString(R.string.buffet_paid_all) + "\n" + context.getString(R.string.buffet_dinner_finish_return_deposit));
                } else {
                    tradeStr = new SpannableString(context.getString(R.string.buffet_paid_all) + "\n" + context.getString(R.string.buffet_dinner_finish));
                }
                tradeStr.setSpan(new AbsoluteSizeSpan(smallSize, true), 0, context.getString(R.string.buffet_paid_all).length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tradeStr.setSpan(new AbsoluteSizeSpan(bigSize, true), context.getString(R.string.buffet_paid_all).length(), tradeStr.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                settleBtn.setText(tradeStr);
            } else {
                SpannableString tradeStr = new SpannableString(context.getString(R.string.dinner_checkout) + "\n" + event.getTradeAmount());
                tradeStr.setSpan(new AbsoluteSizeSpan(bigSize, true), 0, context.getString(R.string.dinner_checkout).length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tradeStr.setSpan(new AbsoluteSizeSpan(smallSize, true), context.getString(R.string.dinner_checkout).length(), tradeStr.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                settleBtn.setText(tradeStr);
            }

        } else {
            settleBtn.setText(context.getString(R.string.dinner_checkout));
        }
    }

    public void onEventMainThread(ActionTableInfoBottomBarStatus event) {
        switch (event.getStatus()) {
            case TRADE_OPERATE:
                showOperateView(tradeOperateLL);
                showMoveDishCheckMode(false);
//              settleBtn.setText(R.string.dinner_table_settlebutton);

                break;
            case DISH_SERVING_MODIFY:
                showOperateView(dishServiceLL);
                break;
            case OPENTABLE:
                showOperateView(openTableLL);
                break;
            case DELETE_TRADE:
                showOperateView(delteTradeLL);
                break;
            case NEW_TRADE_OPERATE:
                showOperateView(newTradeOperateLL);
                newTradeOperateLL.setTag(null);
                break;
            case NO_BOTTOMBAR:
                showOperateView();
                break;
            case TRADE_FINISH:
                showOperateView(finishTradeLL);
                break;
            case MOVE_DISH_CHOOSE_MODE:
                showMoveDishCheckMode(true);
                break;
            case CHANGE_TABLE:
                showOperateView(changeTableLL);
                break;
            case CHOOSE_ADD_DISH_ITEM:
                showOperateView(newTradeOperateLL);
                newTradeOperateLL.setTag("add_dish");
                break;
            default:
                break;
        }

    }

    /**
     * @param views
     * @Date 2015年11月20日
     * @Description: 显示操作栏
     * @Return void
     */
    void showOperateView(View... views) {
        for (int i = 0; i < bottomOperateLL.getChildCount(); i++) {
            bottomOperateLL.getChildAt(i).setVisibility(View.GONE);
        }

        if (views == null || views.length == 0) {
            operateSeperateLine.setVisibility(View.GONE);
            return;
        } else {
            operateSeperateLine.setVisibility(View.VISIBLE);
        }

        for (View view : views) {
            view.setVisibility(View.VISIBLE);
        }
    }

    /**
     * @Date 2015年11月20日
     * @Description: 点菜响应
     * @Return void
     */
    private void goToDishWindow() {
        // 将订单数据写入购物车
        if (dinnerTableTradeVo == null || dinnerTableTradeVo.getStatus() == DinnertableStatus.EMPTY) {

        } else {
            //modify by zhubo 2015-10-30 resetOrderFromTable方法参数调整
            DinnertableTradeInfo tradeInfo = DinnertableTradeInfo.create(dinnerTableTradeVo.getDinnertableTrade(), dinnerTableTradeVo.getTradeVo());
            DinnerShoppingCart.getInstance().resetOrderFromTable(dinnerTableTradeVo.getUnionMainTradeInfo(), dinnerTableTradeVo.getInfo(), true);
            // 显示点菜界面
            //((TableMainActivity) activity).showOrderDish();

        }
    }


    private void verifyPay() {
//        boolean isOk = VerifyPermissionUtils.alertVerifyPermissInputDialog(activity,
//                DinnerApplication.PERMISSION_DINNER_CASH,
//                new PermissionVerify() {
//                    @Override
//                    public void verify(String permission, boolean success) {
//                        if (success && permission.equals(DinnerApplication.PERMISSION_DINNER_CASH)) {
//                            pay();
//                        }
//
//                    }
//                });
//        if (isOk)
//        VerifyPermissionUtils.verifyRun(activity, DinnerApplication.PERMISSION_DINNER_CASH, new Runnable() {
//            @Override
//            public void run() {
//                pay();
//            }
//        });
        if (dinnerTableTradeVo.isNeedFinishTrade()) {//如果订单支付完成调用完结操作
            if (dinnerTableTradeVo.isNeedReturnDeposit()) {
                MobclickAgentEvent.onEvent(activity, BuffetMobClickAgentEvent.buffetTableDetailsFinishReturndeposit);
                DepositInfoDialog.show(activity, dinnerTableTradeVo.getTradeVo());
            } else {
                MobclickAgentEvent.onEvent(activity, BuffetMobClickAgentEvent.buffetTradeDetailsBuffetFinish);
                //如果是联台主单完成，调用主单合单接口 add v9.0
                if (dinnerTableTradeVo.getTradeVo().getTrade().getTradeType() == TradeType.UNOIN_TABLE_MAIN) {
                    finishBuffetUnitTrade(dinnerTableTradeVo);
                } else {
                    finishTrade(dinnerTableTradeVo.getTradeVo().getTrade().getId());
                }
            }
        } else {
            pay();
        }
    }

    /**
     * @Date 2015年11月26日
     * @Description: 收银
     * @Return void
     */
    private void pay() {
        MobclickAgentEvent.onEvent(context, DinnerMobClickAgentEvent.tableDetailsCheckout);
        TradeVo tradeVo = dinnerTableTradeVo.getTradeVo();

        if (tradeVo != null && tradeVo.getTrade() != null && tradeVo.getTrade().getTradeType() == TradeType.UNOIN_TABLE_SUB) {
            ToastUtil.showShortToast(context.getString(R.string.dinner_union_sub_cannot_pay));
            return;
        }
        List<IShopcartItem> shopcartItemList = getShopCartDishItems();
        //无品项的情况
        if ((tradeVo == null || tradeVo.getTradeItemList() == null || tradeVo.getTradeItemList().size() == 0)
                && (Utils.isEmpty(tradeVo.getCouponPrivilegeVoList()) || tradeVo.getIntegralCashPrivilegeVo() == null || tradeVo.getTradePrivilege() == null)
                && Utils.isEmpty(tradeVo.getTradeBuffetPeoples()) && Utils.isEmpty(shopcartItemList)) {
            ToastUtil.showShortToast(context.getString(R.string.dinner_no_unpain_dish));
            return;
        }
        //排除已提交或者已打印的删除项
        boolean canPay = false;
        for (IShopcartItem shopcartItem : shopcartItemList) {
            if (shopcartItem.getStatusFlag() == StatusFlag.VALID && shopcartItem.getTotalQty().doubleValue() != 0) {
                canPay = true;
                break;
            }
        }

        if (Utils.isNotEmpty(tradeVo.getTradeBuffetPeoples())) {
            canPay = true;
        }

        if (!canPay) {
            ToastUtil.showShortToast(context.getString(R.string.dinner_no_unpain_dish_or_zeronumber));
            return;
        }

//        if (tradeVo.getTrade().getTradeAmount().compareTo(BigDecimal.ZERO) < 0) {
//            ToastUtil.showShortToast("收银金额不能为负，请重新结算!");
//        } else {
        //解决跳转闪烁问题
        //modify 20161213 start
        if (tradeVo.getTrade().getTradePayStatus() == TradePayStatus.PAYING && tradeVo.getTrade().getBusinessType() != BusinessType.BUFFET) {
            DoPayManager.gotoPayActivity(activity, tradeVo, true);
        } else {
            //正餐收银界面
            DinnerShoppingCart.getInstance().resetOrderFromTable(dinnerTableTradeVo.getUnionMainTradeInfo(), dinnerTableTradeVo.getInfo(), true);
            //PayManager.gotoDinnerPayUI(activity, null, tradeVo, false, false);
        }
        //modify 20161213 end
//        }
    }

    private void goSettlePage() {
        if (dinnerTableTradeVo == null || dinnerTableTradeVo.getStatus() == DinnertableStatus.EMPTY) {

        } else {
            DinnertableTradeInfo tradeInfo = DinnertableTradeInfo.create(dinnerTableTradeVo.getDinnertableTrade(), dinnerTableTradeVo.getTradeVo());
            DinnerShoppingCart.getInstance().resetOrderFromTable(tradeInfo, true);
            //Intent intent = new Intent(activity, DinnerOrderDishMainActivity.class);
            //intent.putExtra(DinnerOrderDishMainActivity.IS_DEFAULT_SETTLE, true);
            //activity.startActivity(intent);
        }
    }

    /**
     * 异步操作进行中
     *
     * @return
     */
    public static boolean isAsyncHttpExcuting(DinnertableTradeVo dinnertableTradeVo) {
        boolean excuting = false;

        AsyncHttpRecord record = ((DinnertableTradeModel) dinnertableTradeVo.getDinnertableTrade()).getAsyncHttpRecord();

        if (record != null && record.getStatus() != AsyncHttpState.SUCCESS) {
            excuting = true;
        }

        return excuting;
    }

    //联台主单完成接口 add v9.0
    private void finishBuffetUnitTrade(DinnertableTradeVo tableTradeVo) {
        TradeOperates tradeOperates = OperatesFactory.create(TradeOperates.class);
        ResponseListener<TradeResp> listener = new ResponseListener<TradeResp>() {

            @Override
            public void onResponse(ResponseObject<TradeResp> response) {
                if (ResponseObject.isOk(response)) {
                    ToastUtil.showLongToast(response.getMessage());

                } else {
                    ToastUtil.showLongToast(response.getMessage());
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showLongToast(error.getMessage());
            }
        };
        /*try {
            tradeOperates.buffetUnionFinish(BuffetMergeUnionManager.createBuffetMergeUnionReq(tableTradeVo.getTradeVo()), LoadingResponseListener.ensure(listener, activity.getSupportFragmentManager()));
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    /**
     * @Date 2016年4月28日
     * @Description: 完成大众点评已支付订单
     * @Return void
     */
    private void finishTrade(Long tradeId) {
        TradeOperates mTradeOperates = OperatesFactory.create(TradeOperates.class);
        ResponseListener<TradeFinishResp> listener = new ResponseListener<TradeFinishResp>() {

            @Override
            public void onResponse(ResponseObject<TradeFinishResp> response) {
                if (ResponseObject.isOk(response)) {
                    ToastUtil.showLongToast(response.getMessage());
                    //modify by zhubo 2016-5-18 解决完成订单后，点击开台按钮提示未清台的bug
                    TableInfoFragment fragment = (TableInfoFragment) activity.getSupportFragmentManager().findFragmentByTag(DinnerTableConstant.CONTROL_FRAGMENT_TAG);
                    if (fragment != null) {
                        fragment.onClick(fragment.closeBtn);
                    }
                } else {
                    ToastUtil.showLongToast(response.getMessage());
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showLongToast(error.getMessage());
            }
        };

        mTradeOperates.tradeFinish(tradeId, LoadingResponseListener.ensure(listener, activity.getSupportFragmentManager()));
    }

    /**
     * @Date 2016/6/8
     * @Description:是否进入移菜选择模式
     * @Param
     * @Return
     */
    public void showMoveDishCheckMode(boolean show) {
        DinnerDishTradeInfoFragment tradeInfoFragment = (DinnerDishTradeInfoFragment) activity.getSupportFragmentManager().findFragmentByTag("dinner_table_page0");
        if (tradeInfoFragment == null) return;
        DinnerShopCartAdapter adapter = tradeInfoFragment.getSelectedDishAdapter();

        if (show) {
            adapter.setDishCheckMode(true);
            tradeInfoFragment.setMoveDishDragMode(true);
            tradeInfoFragment.enableSlideDishItem();

            if (MoreMenuPopWindow.operateType == OperateType.MOVE_DISH) {
                updateCancelMoveDishBtn((byte) 0);
            } else {
                updateCancelMoveDishBtn((byte) 1);
            }
            showOperateView(cancelMoveDishLL);//moveDishOperateLL

            tableInfoFragment.addItemBatchView.setVisibility(View.GONE);

            tableInfoFragment.llNumberAndWaiter.setVisibility(View.GONE);
            tableInfoFragment.titleRL.setVisibility(View.GONE);
            tableInfoFragment.moveDishTopbarRL.setVisibility(View.VISIBLE);
            tableFragment.enableDinnertableClick(false);//禁止点击桌台

            tableInfoFragment.refreshDishCheckTitleBar();//刷新选择菜品标题栏
            tableInfoFragment.initialDishAllCheckBar(true);
        } else {
            adapter.setDishCheckMode(false);
            tradeInfoFragment.setMoveDishDragMode(false);
            tradeInfoFragment.enableSlideDishItem();

            //判断单子是否有加菜单
            tableInfoFragment.refreshAddItemBatchView();

            tableInfoFragment.llNumberAndWaiter.setVisibility(View.VISIBLE);
            tableInfoFragment.titleRL.setVisibility(View.VISIBLE);
            tableInfoFragment.moveDishTopbarRL.setVisibility(View.GONE);

            tableFragment.enableDinnertableClick(true);//允许点击桌台
            tableInfoFragment.initialDishAllCheckBar(false);
        }
        tradeInfoFragment.updateOrderDishList(dinnerTableTradeVo.getItems(), dinnerTableTradeVo.getTradeVo());


    }

    /**
     * 获取购物车中数据
     *
     * @return
     */
    private List<IShopcartItem> getShopCartDishItems() {
        DinnerDishTradeInfoFragment tradeInfoFragment = (DinnerDishTradeInfoFragment) activity.getSupportFragmentManager().findFragmentByTag("dinner_table_page0");
        return tradeInfoFragment.getSelectedDishAdapter().getAllIShopcartItems();
    }

    /**
     * @Date 2016/6/12
     * @Description:移菜拖动界面展示
     * @Param
     * @Return
     */
    public void showMoveDishDragMode(boolean show) {
        DinnerDishTradeInfoFragment tradeInfoFragment = (DinnerDishTradeInfoFragment) activity.getSupportFragmentManager().findFragmentByTag("dinner_table_page0");
        DinnerShopCartAdapter adapter = tradeInfoFragment.getSelectedDishAdapter();

        if (show) {
            adapter.setDishCheckMode(true);
            List<IShopcartItem> items = adapter.initialMoveDishDragModeCheckStatus2();
            tradeInfoFragment.setMoveDishDragMode(true);

            showOperateView(cancelMoveDishLL);
            tradeInfoFragment.updateOrderDishList(items, dinnerTableTradeVo.getTradeVo());
        } else {
            adapter.setDishCheckMode(false);
            tradeInfoFragment.setMoveDishDragMode(false);
            refreshView(dinnerTableTradeVo);

            showOperateView(tradeOperateLL);
        }


    }

    public DinnertableTradeVo getDinnerTableTradeVo() {
        return dinnerTableTradeVo;
    }

    /**
     * @Date 2016/7/27
     * @Description:更新取消移菜/取消复制/完成复制按钮
     * @Param
     * @Return
     */
    public void updateCancelMoveDishBtn(byte type) {
        if (type == 0) {
            cancelMoveDishBtn.setText(R.string.dinner_move_dish_cancel_button);
        } else if (type == 1) {
            cancelMoveDishBtn.setText(R.string.dinner_copy_dish_cancel_button);
        } else {
            cancelMoveDishBtn.setText(R.string.dinner_copy_dish_finish_button);
        }

    }

    private Handler handler = new Handler();

    public static boolean isTradeEmpty(DinnertableTradeVo tradeVo) {
        if (tradeVo == null || tradeVo.getStatus() == DinnertableStatus.EMPTY) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @Date 2016/10/9
     * @Description:等叫起菜选择模式
     * @Param
     * @Return
     */
    public void showDishOperateCheckMode(boolean show, PrintOperationOpType opType) {
        DinnerDishTradeInfoFragment tradeInfoFragment = (DinnerDishTradeInfoFragment) activity.getSupportFragmentManager().findFragmentByTag("dinner_table_page0");
        if (tradeInfoFragment == null) return;
        DinnerShopCartAdapter adapter = tradeInfoFragment.getSelectedDishAdapter();
        tradeInfoFragment.setEnableSlide(!show);//如果是等叫模式,不允许滑动
        if (show) {
            adapter.setDishCheckMode(true);
            adapter.setOpType(opType);
            tradeInfoFragment.setMoveDishDragMode(false);
            adapter.initialDishCheckStatus();


            showOperateView(dishOperateLL);

            tableInfoFragment.addItemBatchView.setVisibility(View.GONE);
            tableInfoFragment.llNumberAndWaiter.setVisibility(View.GONE);
            tableInfoFragment.titleRL.setVisibility(View.GONE);
            tableInfoFragment.dishOperateTitleLL.setVisibility(View.VISIBLE);
            tableFragment.enableDinnertableClick(false);//禁止点击桌台

//            tableInfoFragment.refreshDishCheckTitleBar();//刷新选择菜品标题栏
            tableInfoFragment.initialDishAllCheckBar(true);

            // 等叫时，不需要取消按钮
            if (opType == PrintOperationOpType.WAKE_UP) {
                dishOperateCancelBtn.setVisibility(View.GONE);
                tableInfoFragment.operateNameTv.setText(context.getString(R.string.dinner_orderdish_dish_prepare));
            } else if (opType == PrintOperationOpType.RISE_DISH) {
                dishOperateCancelBtn.setVisibility(View.VISIBLE);
                tableInfoFragment.operateNameTv.setText(context.getString(R.string.dinner_orderdish_dish_make));
            } else {
                dishOperateCancelBtn.setVisibility(View.VISIBLE);
                tableInfoFragment.operateNameTv.setText(context.getString(R.string.dinner_orderdish_dish_urge));
            }
            tradeInfoFragment.updateOrderDishList(dinnerTableTradeVo.getItems(), dinnerTableTradeVo.getTradeVo(), true, true, true);
        } else {
            adapter.setDishCheckMode(false);
            tradeInfoFragment.setMoveDishDragMode(false);
            showOperateView(tradeOperateLL);

            tableInfoFragment.refreshAddItemBatchView();
            tableInfoFragment.llNumberAndWaiter.setVisibility(View.VISIBLE);
            tableInfoFragment.titleRL.setVisibility(View.VISIBLE);
            tableInfoFragment.dishOperateTitleLL.setVisibility(View.GONE);

            tableFragment.enableDinnertableClick(true);//允许点击桌台
            tableInfoFragment.initialDishAllCheckBar(false);
            tradeInfoFragment.updateOrderDishList(dinnerTableTradeVo.getItems(), dinnerTableTradeVo.getTradeVo());
        }
        tradeInfoFragment.enableSlideDishItem();
//        adapter.notifyDataSetChanged();
//        tradeInfoFragment.updateOrderDishList(dinnerTableTradeVo.getItems(), dinnerTableTradeVo.getTradeVo());
    }

    /**
     * @Date 2016/10/9
     * @Description:取消起菜或者崔菜
     * @Param
     * @Return
     */
    private void cancelDishOperate() {
        final DinnerShopCartAdapter adapter = tradeInfoFragment.getSelectedDishAdapter();
        final PrintOperationOpType opType = adapter.getOpType();

        if (opType == null) {
            return;
        }
        // 催菜和起菜点击取消按钮时，移除已选择条目中添加在内存中的起菜和催菜
        switch (opType) {
            case RISE_DISH:
            case REMIND_DISH:
                List<DishDataItem> selectedItems = adapter.getCheckItems();
                if (DinnerDishManager.getInstance().removeSelectedTradeItemOperations(selectedItems, opType)) {
                    adapter.notifyDataSetChanged();
                }
                break;
            default:
                break;
        }

        showDishOperateCheckMode(false, null);
    }

    /**
     * @Date 2016/10/9
     * @Description:
     * @Param
     * @Return
     */
    private void finishDishOperate() {
        final DinnerShopCartAdapter adapter = tradeInfoFragment.getSelectedDishAdapter();
        final PrintOperationOpType opType = adapter.getOpType();
        if (opType == null) {
            return;
        }
        switch (opType) {
            case WAKE_UP:
            case RISE_DISH:
            case REMIND_DISH:
                TradeVo tradeVo = dinnerTableTradeVo.getTradeVo();
                if (tradeVo == null || tradeVo.getTrade() == null) {
                    return;
                }

                final List<DishDataItem> selectedItems = adapter.getCheckItems();
                if (Utils.isEmpty(selectedItems)) {
                    ToastUtil.showShortToast(R.string.please_select_dish);
                    return;
                }

                List<Long> selectedItemIds = DinnerDishManager.getInstance().getSingleAndComboIds(selectedItems);
                if (selectedItemIds == null || selectedItemIds.isEmpty()) {
                    ToastUtil.showShortToast(R.string.please_select_dish);
                    return;
                }

                DinnerTradeItemManager.dishOperationfun(tradeVo, opType, selectedItems, tradeInfoFragment.getFragmentManager(), adapter, new DishOperationListener());

                break;
            default:
                break;
        }
        showDishOperateCheckMode(false, null);
    }

    private final class DishOperationListener implements DishOptListener {
        @Override
        public void onSuccess(PrintOperationOpType type, List<DishDataItem> dataItems) {

        }

        @Override
        public void onFail(PrintOperationOpType type, List<DishDataItem> dataItems) {
            if (tradeInfoFragment.removeSelectedTradeItemOperations(dataItems, type)) {
                tradeInfoFragment.getSelectedDishAdapter().notifyDataSetChanged();
            }
        }
    }


    /**
     * @Date 2016/10/20
     * @Description:接受加菜
     * @Param
     * @Return
     */
    @Background
    protected void acceptAddItem() {
        List<TradeItemVo> tradeItemVos = dinnerTableTradeVo.getTradeVo().getTradeItemList();
        if (tradeItemVos != null) {
            for (TradeItemVo tradeItemVo : tradeItemVos) {
                if (tradeItemVo.getTradeItem().getId() != null) {
                    tradeItemVo.getTradeItem().setChanged(false);
                }
            }
        }

        final boolean isAcceptAutoTransferOpen = TradeSourceUtils.isTradeFromWeixin(dinnerTableTradeVo) ? SpHelper.getDefault().getBoolean(Constant.WEIXIN_ACCEPT_TRANSFER_KITCHEN) : true;

        DinnertableTradeInfo tradeInfo = DinnertableTradeInfo.create(dinnerTableTradeVo.getDinnertableTrade(), dinnerTableTradeVo.getTradeVo());
        DinnerShoppingCart shoppingCart = DinnerShoppingCart.getInstance();
        shoppingCart.resetOrderFromTable(tradeInfo, true);

        //循环加菜
        List<IShopcartItem> items = tableInfoFragment.getShopcartItemsAddDish();
        if (items != null) {
            for (IShopcartItem item : items) {
                shoppingCart.addReadOnlyShippingToCart(shoppingCart.getShoppingCartVo(), item, false);
            }
        }

        final TradeVo tradeVo = shoppingCart.createOrder(shoppingCart.getShoppingCartVo(), false);

        // 获取tradeid
        Long tradeId = null;
        if (tradeVo.getTrade() != null) {
            tradeId = tradeVo.getTrade().getId();
        }

        // 会员绑定tradeid
        List<TradeCustomer> customerList = tradeVo.getTradeCustomerList();
        if (customerList != null) {
            for (TradeCustomer tradeCustomer : customerList) {
                tradeCustomer.setTradeId(tradeId);
            }
        }

        // 菜品折扣绑定tradeid
        for (TradeItemVo tradeItemVo : tradeVo.getTradeItemList()) {
            if (tradeItemVo.getTradeItemPrivilege() != null
                    && tradeItemVo.getTradeItemPrivilege().getTradeId() != tradeId) {
                tradeItemVo.getTradeItemPrivilege().setTradeId(tradeId);
                tradeItemVo.getTradeItemPrivilege().setChanged(true);
            }
        }

        TradeOperates tradeOperates = OperatesFactory.create(TradeOperates.class);

        ResponseListener<TradeResp> listener = new EventResponseListener<TradeResp>(UserActionEvent.DINNER_TRADE_ITEM_ADD_ACCEPT) {
            @Override
            public void onResponse(ResponseObject<TradeResp> response) {
                ToastUtil.showShortToast(response.getMessage());

                if (!ResponseObject.isOk(response) || response.getContent() == null) {
                    return;
                }

                List<TradeItem> listTradeItems = response.getContent().getTradeItems();
                if (Utils.isNotEmpty(listTradeItems)) {
                    List<String> listAddItemUUIDs = new ArrayList<>();
                    String tradeUUid = "";
                    for (TradeItem tradeItem : listTradeItems) {
                        tradeUUid = tradeItem.getTradeUuid();
                        listAddItemUUIDs.add(tradeItem.getUuid());
                    }

                    //打印客看单
                    //boolean isFirstPrintCustom = DinnerPrintUtil.isFirstPrintCustom(tradeVo);
                    //IPrintHelper.Holder.getInstance().printCustomerTicket(tradeUUid, listAddItemUUIDs, null, null, isFirstPrintCustom, new PRTBatchOnSimplePrintListener(PrintTicketTypeEnum.CUSTOMER));
                    //打印后厨单
                    if (isAcceptAutoTransferOpen) {
                        //boolean isFirstPrintKitchen = DinnerPrintUtil.isFirstPrintKitchen(tradeVo);
                        //DinnerPrintUtil.printKitchenTicketAfterAcceptOrder(tradeVo.getTrade().getUuid(), listAddItemUUIDs, isFirstPrintKitchen);
                    }
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showShortToast(error.getMessage());
            }
        };

        //重新计算价格
        MathShoppingCartTool.mathTotalPrice(DinnerShoppingCart.getInstance().getShoppingCartDish(), tradeVo);

        tradeOperates.acceptAddItem(tradeVo, tableInfoFragment.getChooseAddItemVo(), isAcceptAutoTransferOpen,
                LoadingResponseListener.ensure(listener, tableInfoFragment.getFragmentManager()));
    }

    /**
     * @Date 2016/10/20
     * @Description:拒绝加菜
     * @Param
     * @Return
     */
    private void refuseAddItem() {
        TradeOperates tradeOperates = OperatesFactory.create(TradeOperates.class);
        ResponseListener<TradeResp> listener = new EventResponseListener<TradeResp>(UserActionEvent.DINNER_TRADE_ITEM_ADD_REFUSE) {
            @Override
            public void onResponse(ResponseObject<TradeResp> response) {
                ToastUtil.showShortToast(response.getMessage());
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showShortToast(error.getMessage());
            }
        };
        tradeOperates.refuseAddItem(tableInfoFragment.getChooseAddItemVo(),
                LoadingResponseListener.ensure(listener, tableInfoFragment.getFragmentManager()));
    }

    /**
     * @Date 2016/10/24
     * @Description:是否是加菜模式
     * @Param
     * @Return
     */
    public boolean isAddDishMode() {
        String tag = (String) newTradeOperateLL.getTag();
        if (newTradeOperateLL.getVisibility() == View.VISIBLE && tag.equals("add_dish")) {
            return true;
        }
        return false;
    }

    public void resetView() {
        tableInfoFragment.addItemBatchView.setVisibility(View.GONE);
        tableInfoFragment.llNumberAndWaiter.setVisibility(View.VISIBLE);
        tableInfoFragment.titleRL.setVisibility(View.VISIBLE);
        tableInfoFragment.dishOperateTitleLL.setVisibility(View.GONE);

        tableInfoFragment.initialDishAllCheckBar(false);

        DinnerDishTradeInfoFragment tradeInfoFragment = (DinnerDishTradeInfoFragment) activity.getSupportFragmentManager().findFragmentByTag("dinner_table_page0");
        if (tradeInfoFragment == null) return;
        DinnerShopCartAdapter adapter = tradeInfoFragment.getSelectedDishAdapter();
        tradeInfoFragment.setEnableSlide(true);//如果是等叫模式,不允许滑动
        adapter.setDishCheckMode(false);
        tradeInfoFragment.setMoveDishDragMode(false);
        showOperateView(tradeOperateLL);

//        tableInfoFragment.refreshAddItemBatchView();

        tradeInfoFragment.enableSlideDishItem();
        adapter.notifyDataSetChanged();
    }
}
