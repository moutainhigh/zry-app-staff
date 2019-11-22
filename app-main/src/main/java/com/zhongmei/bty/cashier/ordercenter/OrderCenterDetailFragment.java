package com.zhongmei.bty.cashier.ordercenter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zhongmei.beauty.dialog.BeautyCreateOrEditMemberDocDialog;
import com.zhongmei.beauty.dialog.BeautyCreateOrEditTaskDialog;
import com.zhongmei.beauty.dialog.BeautyResultTaskDialog;
import com.zhongmei.beauty.ordercenter.BeautyOrderCenterDetailPresenter;
import com.zhongmei.bty.basemodule.auth.application.FastFoodApplication;
import com.zhongmei.bty.basemodule.commonbusiness.cache.ServerSettingCache;
import com.zhongmei.bty.basemodule.commonbusiness.entity.PartnerDeliveryPlatformConfig;
import com.zhongmei.bty.basemodule.commonbusiness.entity.PartnerShopBiz;
import com.zhongmei.bty.basemodule.commonbusiness.listener.SimpleResponseListener;
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.basemodule.discount.entity.ExtraCharge;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.bty.basemodule.pay.bean.ElectronicInvoiceVo;
import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;
import com.zhongmei.bty.basemodule.pay.message.PayResp;
import com.zhongmei.bty.basemodule.session.core.user.UserFunc;
import com.zhongmei.bty.basemodule.shopmanager.handover.manager.ServerSettingManager;
import com.zhongmei.bty.basemodule.trade.bean.DeliveryOrderVo;
import com.zhongmei.bty.basemodule.trade.bean.TradePaymentVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.entity.DeliveryOrderRecord;
import com.zhongmei.bty.basemodule.trade.entity.TradeBuffetPeople;
import com.zhongmei.bty.basemodule.trade.entity.TradeDeposit;
import com.zhongmei.bty.basemodule.trade.entity.TradeExtraSecrecyPhone;
import com.zhongmei.bty.basemodule.trade.entity.TradeStatusLog;
import com.zhongmei.bty.basemodule.trade.message.InvoiceQrcodeReq;
import com.zhongmei.bty.basemodule.trade.message.ModifyTradeMemoResp;
import com.zhongmei.bty.basemodule.trade.operates.TradeOperates;
import com.zhongmei.bty.basemodule.trade.settings.IPanelItemSettings;
import com.zhongmei.bty.buffet.orderdish.DepositInfoDialog;
import com.zhongmei.bty.cashier.ordercenter.adapter.OrderDeliveryFeeAdapter;
import com.zhongmei.bty.cashier.ordercenter.bean.DispatchFailOrder;
import com.zhongmei.bty.cashier.ordercenter.presenter.BuffetOrderCenterDetailPresenter;
import com.zhongmei.bty.cashier.ordercenter.presenter.DinnerOrderCenterDetailPresenter;
import com.zhongmei.bty.cashier.ordercenter.presenter.GroupOrderCenterDetailPresenter;
import com.zhongmei.bty.cashier.ordercenter.presenter.IOrderCenterDetailPresenter;
import com.zhongmei.bty.cashier.ordercenter.view.AddFeeDialog;
import com.zhongmei.bty.cashier.ordercenter.view.DeliveryPlatformPopupWindow;
import com.zhongmei.bty.cashier.ordercenter.view.DispatchFailOrderListFragment;
import com.zhongmei.bty.cashier.ordercenter.view.IOrderCenterDetailView;
import com.zhongmei.bty.cashier.ordercenter.view.OrderCenterRePayLayout;
import com.zhongmei.bty.cashier.ordercenter.view.OrderCenterRePayLayout_;
import com.zhongmei.bty.cashier.ordercenter.view.SelectSenderDialogFragment;
import com.zhongmei.bty.common.view.OrderReprintDialog;
import com.zhongmei.bty.common.view.OrderReprintDialog_;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.database.entity.DeliveryOrder;
import com.zhongmei.bty.commonmodule.database.enums.DeliveryOrderRecordOpType;
import com.zhongmei.bty.commonmodule.database.enums.DeliveryOrderStatus;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.bty.commonmodule.util.ChangeViewStateUtil;
import com.zhongmei.bty.commonmodule.util.EventServerAvailableStateChange;
import com.zhongmei.bty.commonmodule.util.ServerHeartbeat;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.constants.OCConstant;
import com.zhongmei.bty.db.entity.VerifyKoubeiOrder;
import com.zhongmei.bty.dinner.action.ActionReprintType;
import com.zhongmei.bty.dinner.adapter.DinnerBillCenterRejectDishAdapter;
import com.zhongmei.bty.dinner.adapter.OrderCenterDishAdapter;
import com.zhongmei.bty.dinner.ordercenter.view.ColumnLayout;
import com.zhongmei.bty.dinner.ordercenter.view.OrderCenterDetailView;
import com.zhongmei.bty.mobilepay.fragment.InvoiceFragment;
import com.zhongmei.bty.settings.view.XInnerListView;
import com.zhongmei.bty.snack.event.EventSelectOrder;
import com.zhongmei.bty.snack.event.EventSelectOrderRefresh;
import com.zhongmei.bty.snack.offline.Snack;
import com.zhongmei.util.SettingManager;
import com.zhongmei.yunfu.Constant;
import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.context.util.DateTimeUtils;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeCreditLog;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.entity.trade.TradeUser;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.CreditType;
import com.zhongmei.yunfu.db.enums.CustomerType;
import com.zhongmei.yunfu.db.enums.DeliveryPlatform;
import com.zhongmei.yunfu.db.enums.DeliveryStatus;
import com.zhongmei.yunfu.db.enums.DeliveryType;
import com.zhongmei.yunfu.db.enums.DishType;
import com.zhongmei.yunfu.db.enums.OperateType;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.db.enums.PaymentType;
import com.zhongmei.yunfu.db.enums.PrivilegeType;
import com.zhongmei.yunfu.db.enums.SaleType;
import com.zhongmei.yunfu.db.enums.SourceId;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.db.enums.TakeDishStatus;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;
import com.zhongmei.yunfu.util.DialogUtil;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.util.ResourceUtils;
import com.zhongmei.yunfu.util.ToastUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;

import static com.zhongmei.bty.basemodule.trade.settings.IPanelItemSettings.SERIAL_DESK_MODE;
import static com.zhongmei.bty.commonmodule.util.ServerHeartbeat.NetworkState.NetworkAvailable;
import static com.zhongmei.bty.commonmodule.util.ServerHeartbeat.NetworkState.NetworkUnavailable;


@EFragment(R.layout.frg_order_center_detail)
public class OrderCenterDetailFragment extends BasicFragment implements IOrderCenterDetailView {
    private static final String TAG = OrderCenterDetailFragment.class.getSimpleName();
    @ViewById(R.id.layout_titlebar)
    protected LinearLayout layout_titlebar;

    @ViewById(R.id.layout_loading_title_bar)
    protected LinearLayout layout_LoadingTitleBar;

    @ViewById(R.id.detail_scrollview)
    ScrollView scrollView;

    @ViewById(R.id.tv_empty_trade_details)
    protected TextView tv_emptyTradeDetails;

    @ViewById(R.id.v_empty)
    View vEmpty;

    @ViewById(R.id.v_content)
    View vContent;

    @ViewById(R.id.loading_view)
    View mLoadingView;
    @ViewById(R.id.detail_trade_no)
    TextView trade_no;
    @ViewById(R.id.detail_delivery_type)
    TextView delivery_type;
    @ViewById(R.id.detail_take_dish_status)
    TextView take_dish_status;

    @ViewById(R.id.order_center_detail_delivery_status_tv)
    TextView tvDeliveryStatus;

    @ViewById(R.id.detail_delivery_name)
    TextView detail_delivery_name;

    @ViewById(R.id.delivery_info)
    View viewDelivery;

    @ViewById(R.id.receiver_info)
    View delivery_info;

    @ViewById(R.id.order_center_detail_delivery_info_divide_line_v)
    View vDeliveryInfoDivideLine;

    @ViewById(R.id.order_center_detail_delivery_info_tip_v)
    View vTip;

    @ViewById(R.id.order_center_detail_delivery_info_passive_add_tip_tv)
    TextView tvPassiveAddTip;

    @ViewById(R.id.order_center_detail_delivery_user_name_tv)
    TextView tvDeliveryUserName;

    @ViewById(R.id.order_center_detail_delivery_user_phone_tv)
    TextView tvDeliveryUserPhone;

    @ViewById(R.id.order_center_detail_delivery_platform_tv)
    TextView tvDeliveryPlatform;

    @ViewById(R.id.order_center_detail_delivery_info_tip_hint_tv)
    TextView tvFeeHint;

    @ViewById(R.id.order_center_detail_delivery_info_tip_total_amount_tv)
    TextView tvFeeTotal;

    @ViewById(R.id.order_center_detail_delivery_info_tip_record_lv)
    XInnerListView mListViewFee;

    @ViewById(R.id.tv_delivery_invoice_layout)
    View delivery_invoice_layout;

    @ViewById(R.id.tv_delivery_address_layout)
    View delivery_address_layout;

    @ViewById(R.id.tv_delivery_type)
    TextView tv_delivery_type;

    @ViewById(R.id.tv_delivery_invoice_title)
    TextView delivery_invoice_title;

    @ViewById(R.id.tv_delivery_name)
    TextView delivery_name;

    @ViewById(R.id.tv_delivery_phone)
    TextView delivery_phone;

    @ViewById(R.id.tv_delivery_time)
    TextView delivery_time;

    @ViewById(R.id.tv_delivery_address)
    TextView delivery_address;

    @ViewById(R.id.tv_delivery_invoice_code)
    TextView delivery_invoice_code;

    @ViewById(R.id.pay_info)
    View vPayInfo;

    @ViewById(R.id.ll_payinfo_content)
    LinearLayout llPayInfoContent;

    @ViewById(R.id.tv_payment_time)
    TextView tvPaymentTime;

    @ViewById(R.id.v_trade_handle)
    LinearLayout vTradeHandle;

    @ViewById(R.id.btn_refuse)
    Button btnRefuse;

    @ViewById(R.id.btn_accept)
    Button btnAccept;

    @ViewById(R.id.btn_call)
    ImageView btnCall;

    @ViewById(R.id.btn_pay)
    Button btnPay;

    @ViewById(R.id.btn_recision)
    Button btnRecision;

    @ViewById(R.id.btn_refund)
    Button btnRefund;

    @ViewById(R.id.btn_print)
    Button btnPrint;

    @ViewById(R.id.btn_retry_refund)
    Button btnRetryRefund;

    @ViewById(R.id.btn_repay)
    Button btnRepay;

    @ViewById(R.id.btn_continue_repay)
    Button btnContinueRepay;

    @ViewById(R.id.fl_send_order)
    FrameLayout flSendOrder;

    @ViewById(R.id.tv_send_order)
    TextView tvSendOrder;

    @ViewById(R.id.btn_cancel_order)
    Button btnCancelOrder;

    @ViewById(R.id.btn_accept_return)
    Button btnAcceptReturn;

    @ViewById(R.id.btn_refuse_return)
    Button btnRefuseReturn;

    @ViewById(R.id.btn_deposit_refund)
    Button btnDepositRefund;

    @ViewById(R.id.btn_continue_pay)
    Button btnContinuePay;

    @ViewById(R.id.btn_invoice)
    Button btnInvoice;

    @ViewById(R.id.btn_invoice_revoke)
    Button btnInvoiceRevoke;

    @ViewById(R.id.btn_take_dish)
    Button btnTakeDish;

    @ViewById(R.id.btn_rebind_delivery_user)
    Button btnRebindDeliveryUser;

    @ViewById(R.id.btn_create_doc)
    Button btnCreateDoc;

    @ViewById(R.id.btn_create_task)
    Button btnCreateTask;

    @ViewById(R.id.goods_info)
    LinearLayout goods_info;

    @ViewById(R.id.tv_goods_number)
    TextView mTvNumber;

    @ViewById(R.id.tv_amount)
    TextView mTvAmount;

    @ViewById(R.id.dinner_billcenter_detail_goodslistview)
    XInnerListView goodsListView;

    @ViewById(R.id.v_table_group)
    View vTableGroup;

    @ViewById(R.id.bill_info)
    View vBillInfo;

    @ViewById(R.id.tv_billtime)
    TextView mTvBillTime;

    @ViewById(R.id.group_name_layout)
    ViewGroup groupNameLayout;

    @ViewById(R.id.tv_group_name)
    TextView tvGroupName;

    @ViewById(R.id.tv_tablenumber)
    TextView mTvTableNumber;

    @ViewById(R.id.tv_billserialnumber)
    TextView mTvBillSerialNumber;

    @ViewById(R.id.tv_peoplecount)
    TextView mTvPeopleCount;

    @ViewById(R.id.tv_dinnertime)
    TextView mTvDinnerTime;

    @ViewById(R.id.cl_customer)
    ColumnLayout clCustomer;

    @ViewById(R.id.v_line_customer)
    View vLineCustomer;

    @ViewById(R.id.tv_billtablememo)
    TextView mTvBillTableMemo;

    @ViewById(R.id.tv_editable_memo)
    TextView tvEditableMemo;


    @ViewById(R.id.tv_dinner_verify_status)
    TextView tvVerifyStatus;

    @ViewById(R.id.v_memo_group)
    LinearLayout vMemoGroup;

    @ViewById(R.id.v_people_group)
    LinearLayout vPeopleGroup;

    @ViewById(R.id.tv_operationbillpeople)
    TextView mTvOperationBillPeople;

    @ViewById(R.id.v_shopper_group)
    LinearLayout vShopperGroup;

    @ViewById(R.id.line_shopper)
    View mViewLineShopper;

    @ViewById(R.id.tv_shopper)
    TextView mTvShopper;

    @ViewById(R.id.tv_counselor)
    TextView mTvCounselor;

    @ViewById(R.id.line_operation_people_center_detail)
    View mLineOperationPeople;

    @ViewById(R.id.tv_table_serverpoeple)
    TextView mTvTableServerPeople;

    @ViewById(R.id.tv_trade_source)
    TextView tvTradeSource;

    @ViewById(R.id.tv_accept_source)
    TextView tvAcceptSource;

    @ViewById(R.id.tv_origontrade_number)
    TextView tvOrigonTradeNumber;

    @ViewById(R.id.operating_info_view)
    OrderCenterDetailView operating_info_view1;

    @ViewById(R.id.rePay_list)
    LinearLayout llRepayList;

    @ViewById(R.id.split_list)
    LinearLayout llSplitList;

    @ViewById(R.id.reject_goods_info)
    View vRejectGoodsInfo;

    @ViewById(R.id.dinner_billcenter_detail_reject_goodslistview)
    XInnerListView rejectGoodsListView;

    @ViewById(R.id.additional_privilege_view)
    OrderCenterDetailView additional_privilege_view;

    @ViewById(R.id.service_privilege_view)
    OrderCenterDetailView service_privilege_view;

    @ViewById(R.id.tax_view)
    OrderCenterDetailView tax_view;


    @ViewById(R.id.privilege_info)
    View vPrivilegeInfo;

    @ViewById(R.id.ll_privilegeinfo_content)
    LinearLayout llPrivilegeInfoContent;

    @ViewById(R.id.tv_privilege_no)
    TextView tvPrivilegeNo;

    @ViewById(R.id.order_center_detail_delivery_cancel_alert_ll)
    LinearLayout llDeliveryCancelAlert;

    @ViewById(R.id.order_center_detail_delivery_cancel_alert_tv)
    TextView tvDeliveryCancelAlert;

    private DinnerBillCenterRejectDishAdapter mRejectAdapter;

    private OrderCenterDishAdapter mAdapter;

    private IOrderCenterDetailPresenter mPresenter;


    private DeliveryPlatformPopupWindow mDeliveryPlatformPopupWindow;
    private TradeVo mTradeVo;

    private int mFromType = OCConstant.FromType.FROM_TYPE_SNACK;

    private OnRepayListener mOnRepayListener;

    public interface OnRepayListener {
        void onRepayCompleted(TradeVo tradeVo);
    }


    public static OrderCenterDetailFragment newInstance(int fromType) {
        Bundle args = new Bundle();
        args.putInt(Constant.EXTRA_FROM_TYPE, fromType);
        OrderCenterDetailFragment orderCenterDetailFragment = new OrderCenterDetailFragment_();
        orderCenterDetailFragment.setArguments(args);
        return orderCenterDetailFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnRepayListener) {
            mOnRepayListener = (OnRepayListener) activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mOnRepayListener != null) {
            mOnRepayListener = null;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mFromType = arguments.getInt(Constant.EXTRA_FROM_TYPE);
        }
        initPresenter();
        registerEventBus();
    }

    private void initTitleBarBg() {
        switch (mFromType) {
            case OCConstant.FromType.FROM_TYPE_SNACK:
            case OCConstant.FromType.FROM_TYPE_DINNER:
            case OCConstant.FromType.FROM_TYPE_BUFFET:
            case OCConstant.FromType.FROM_TYPE_GROUP:
            case OCConstant.FromType.FROM_TYPE_RETAIL:
                layout_titlebar.setBackgroundColor(getResources().getColor(R.color.snack_title_bg));
                layout_LoadingTitleBar.setBackgroundColor(getResources().getColor(R.color.snack_title_bg));
                setrChildTvColor(layout_titlebar, getResources().getColor(R.color.text_color_white));
                tv_emptyTradeDetails.setTextColor(getResources().getColor(R.color.text_color_white));
                break;
            case OCConstant.FromType.FROM_TYPE_BEAUTY:
                layout_titlebar.setBackgroundColor(getResources().getColor(R.color.beauty_bg_white));
                layout_LoadingTitleBar.setBackgroundColor(getResources().getColor(R.color.beauty_bg_white));
                setrChildTvColor(layout_titlebar, getResources().getColor(R.color.beauty_color_333333));
                tv_emptyTradeDetails.setTextColor(getResources().getColor(R.color.beauty_color_333333));
                break;
        }
    }


    private void setrChildTvColor(ViewGroup viewGroup, int color) {
        if (viewGroup == null) {
            return;
        }

        int childCount = viewGroup.getChildCount();
        if (childCount <= 0) {
            return;
        }

        for (int childIndex = 0; childIndex < childCount; childIndex++) {
            View childView = viewGroup.getChildAt(childIndex);
            if (childView instanceof TextView) {
                ((TextView) childView).setTextColor(color);
            }
        }
    }

    private void initPresenter() {
        switch (mFromType) {
            case OCConstant.FromType.FROM_TYPE_SNACK:
                break;
            case OCConstant.FromType.FROM_TYPE_DINNER:
                mPresenter = new DinnerOrderCenterDetailPresenter(this);
                break;
            case OCConstant.FromType.FROM_TYPE_BUFFET:
                mPresenter = new BuffetOrderCenterDetailPresenter(this);
                break;
            case OCConstant.FromType.FROM_TYPE_GROUP:
                mPresenter = new GroupOrderCenterDetailPresenter(this);
                break;
            case OCConstant.FromType.FROM_TYPE_RETAIL:
                break;
            case OCConstant.FromType.FROM_TYPE_BEAUTY:
                mPresenter = new BeautyOrderCenterDetailPresenter(this);
                break;
        }
    }


    @Override
    public boolean isFromSnack() {
        return mFromType == OCConstant.FromType.FROM_TYPE_SNACK;
    }

    public boolean isFromRetail() {
        return mFromType == OCConstant.FromType.FROM_TYPE_RETAIL;
    }

    @Override
    public void hideEmptyViewAndContent() {
        vEmpty.setVisibility(View.GONE);
        vContent.setVisibility(View.GONE);
    }

    @Override
    public void showLoadingView() {
        mLoadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissLoadingView() {
        mLoadingView.setVisibility(View.GONE);
    }

    @AfterViews
    protected void initView() {
        showEmptyView(true);
        initTitleBarBg();
    }

    @Override
    public void onDestroy() {
        unregisterEventBus();
        destroyPresenter();
        super.onDestroy();
    }

    private void destroyPresenter() {
        if (mPresenter != null) {
            mPresenter.destroy();
            mPresenter = null;
        }
    }

    @Override
    public FragmentManager getViewFragmentManager() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager == null && getActivity() != null) {
            fragmentManager = getActivity().getSupportFragmentManager();
        }

        return fragmentManager;
    }

    @Override
    public FragmentActivity getViewActivity() {
        return getActivity();
    }

    @Override
    public void showToast(String message) {
        ToastUtil.showShortToast(message);
    }

    @Override
    public void showToast(int strId) {
        ToastUtil.showShortToast(getString(strId));
    }

    @Override
    public void startActivity(Intent intent) {
        getActivity().startActivity(intent);
    }

    @Override
    public void showDialog(Trade trade) {
        OrderReprintDialog dialog = new OrderReprintDialog_();
        Bundle bundle = new Bundle();
        bundle.putSerializable("trade", trade);
        bundle.putString("printTag", TAG);
        bundle.putInt("from_type", mFromType);
        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), TAG);
    }

    @Override
    public void showEmptyView(boolean show) {
        if (show) {
            vEmpty.setVisibility(View.VISIBLE);
            vContent.setVisibility(View.GONE);
        } else {
            vEmpty.setVisibility(View.GONE);
            vContent.setVisibility(View.VISIBLE);
        }
    }

    @Click(R.id.order_center_detail_delivery_info_passive_add_tip_tv)
    void addFee() {
        if (!ClickManager.getInstance().isClicked()) {
            mPresenter.passiveAddTip();
        }
    }

    @Click(R.id.btn_refuse)
    void clickRefuse() {
        if (!ClickManager.getInstance().isClicked()) {
            mPresenter.doRefuse();
        }
    }

    @Click(R.id.btn_accept)
    void clickAccept() {
        if (!ClickManager.getInstance().isClicked()) {
            mPresenter.doAccept();
        }
    }

    @Click(R.id.btn_call)
    void clickCall() {
        if (!ClickManager.getInstance().isClicked()) {
            mPresenter.doCall();
        }
    }

    @Click(R.id.btn_pay)
    void clickPay() {
        if (!ClickManager.getInstance().isClicked()) {
            mPresenter.doPay();
        }
    }

    @Click(R.id.btn_recision)
    void clickRecision() {
        if (!ClickManager.getInstance().isClicked()) {
            mPresenter.doRecision();
        }
    }

    @Click(R.id.btn_refund)
    void clickRefund() {
        if (!ClickManager.getInstance().isClicked()) {
            mPresenter.doRefund();
        }
    }

    @Click(R.id.btn_print)
    void clickPrint() {
        if (!ClickManager.getInstance().isClicked()) {
            mPresenter.doPrint(TAG);
        }
    }

    @Click(R.id.btn_repay)
    void clickRepay() {
        if (!ClickManager.getInstance().isClicked()) {
            mPresenter.doRepay();
        }
    }

    @Click(R.id.btn_continue_repay)
    void clickContinueRepay() {
        if (!ClickManager.getInstance().isClicked()) {
            mPresenter.doContinueRepay();
        }
    }

    @Click(R.id.btn_create_doc)
    void clickCreateDoc() {
        TradeCustomer tradeCustomer = mPresenter.getTradeCustomer();
        if (tradeCustomer != null) {
            BeautyCreateOrEditMemberDocDialog dialog = new BeautyCreateOrEditMemberDocDialog();
            dialog.setCustomerInfo(CustomerManager.getInstance().getCustomer(tradeCustomer));
            dialog.show(getChildFragmentManager(), "BeautyCreateOrEditMemberDocDialog");
        }
    }

    @Click(R.id.btn_create_task)
    void clickCreateTask() {
        BeautyCreateOrEditTaskDialog dialog = new BeautyCreateOrEditTaskDialog();
        dialog.show(getChildFragmentManager(), "BeautyResultTaskDialog");
    }

    @Click(R.id.fl_send_order)
    void clickSendOrderButton() {
        if (!ClickManager.getInstance().isClicked()) {
            List<PartnerShopBiz> deliveryPlatformPartnerShopBizs = new ArrayList<PartnerShopBiz>(mPresenter.getDeliveryPlatformPartnerShopBizMap().values());
            if (Utils.isNotEmpty(deliveryPlatformPartnerShopBizs)) {
                if (deliveryPlatformPartnerShopBizs.size() == 1) {
                    mPresenter.sendOrder(deliveryPlatformPartnerShopBizs.get(0), null);
                } else {
                    showDeliveryPlatformPopupWindow(flSendOrder, deliveryPlatformPartnerShopBizs);
                }
            }
        }
    }


    private void showDeliveryPlatformPopupWindow(View anchor, List<PartnerShopBiz> deliveryPlatformPartnerShopBizs) {
        mDeliveryPlatformPopupWindow = new DeliveryPlatformPopupWindow(getActivity(), anchor.getWidth(), deliveryPlatformPartnerShopBizs, new DeliveryPlatformPopupWindow.OnDeliveryPlatformSelectedListener() {
            @Override
            public void OnDeliveryPlatformSelected(PartnerShopBiz partnerShopBiz) {
                mPresenter.sendOrder(partnerShopBiz, null);
            }
        });
        mDeliveryPlatformPopupWindow.show(anchor);
    }


    private void dismissDeliveryPlatformPopupWindow() {
        if (mDeliveryPlatformPopupWindow != null && mDeliveryPlatformPopupWindow.isShowing()) {
            mDeliveryPlatformPopupWindow.dismiss();
            mDeliveryPlatformPopupWindow = null;
        }
    }

    @Click(R.id.btn_cancel_order)
    void clickCancelOrderButton() {
        if (!ClickManager.getInstance().isClicked()) {
            mPresenter.cancelDeliveryOrder();
        }
    }

    @Click(R.id.btn_accept_return)
    void clickAcceptReturnButton() {
        if (!ClickManager.getInstance().isClicked()) {
            mPresenter.doAcceptReturn();
        }
    }

    @Click(R.id.btn_refuse_return)
    void clickRefuseReturnButton() {
        if (!ClickManager.getInstance().isClicked()) {
            mPresenter.doRefuseReturn();
        }
    }

    @Click(R.id.btn_retry_refund)
    void clickRetryRefundButton() {
        if (!ClickManager.getInstance().isClicked()) {
            mPresenter.retryRefund();
        }
    }

    @Click(R.id.btn_take_dish)
    void clickTakeDishButton() {
        if (!ClickManager.getInstance().isClicked()) {
            mPresenter.doTakeDish();
        }
    }

    @Click(R.id.btn_continue_pay)
    void clickContinuePay() {
        if (!ClickManager.getInstance().isClicked()) {
            mPresenter.doContinuePay();
        }
    }

    @Click(R.id.btn_deposit_refund)
    void clickDepositRefundButton() {
        if (!ClickManager.getInstance().isClicked()) {
            if (mTradeVo.getTrade().getBusinessType() == BusinessType.BUFFET) {
                showBuffetRefundDepositWindow();
            } else {
                showRefundDepositWindow();
            }
        }
    }

    @Click(R.id.btn_invoice)
    void clickInvoiceButton() {
        if (!ClickManager.getInstance().isClicked()) {
            mPresenter.invoiceQrcode();
        }
    }

    @Click(R.id.btn_invoice_revoke)
    void clickInvoiceRedButton() {
        if (!ClickManager.getInstance().isClicked()) {
            mPresenter.invoiceRevoke();
        }
    }

    @Click(R.id.btn_rebind_delivery_user)
    void clickRebindDeliveryUserButton() {
        if (!ClickManager.getInstance().isClicked()) {
            mPresenter.doRebindDeliveryUser();
        }
    }

    EventSelectOrder eventSelectOrder;

    public void onEventMainThread(EventSelectOrder event) {
        eventSelectOrder = event;
        dismissDeliveryPlatformPopupWindow();
        if (event.getFromType() == mFromType) {
            if (TextUtils.isEmpty(event.getTradeUuid())) {
                showEmptyView(true);
            } else {
                showLoadingView();
                hideEmptyViewAndContent();
                mPresenter.setIsSquareAccountMode(event.isSquareAccountMode());
                mPresenter.setIsBindDeliveryUserMode(event.isBindDeliveryUserMode());
                mPresenter.setCurrentTab(event.getCurrentTab());
                mPresenter.loadData(event.getTradeUuid());
            }
        }
    }

    public void onEventMainThread(EventSelectOrderRefresh event) {
        if (eventSelectOrder != null) {
            onEventMainThread(eventSelectOrder);
        }
    }


    public void onEventMainThread(ActionReprintType eventReprintType) {

    }

    private void reprint(ActionReprintType action) {

    }

    @Override
    public void goToDishWindow(TradeVo tradeVo) {
        if (mOnRepayListener != null) {
            mOnRepayListener.onRepayCompleted(tradeVo);
        }
    }

    @Override
    public void showPrivilegeInfo(TradePaymentVo tradePaymentVo, boolean isRefund) {
        llPrivilegeInfoContent.removeAllViews();
        if (mPresenter.showPrivilegeInfo(tradePaymentVo)) {
            View view = mPresenter.createPrivilegeInfoItem(getActivity(), tradePaymentVo, isRefund);
            llPrivilegeInfoContent.addView(view);
            tvPrivilegeNo.setText(R.string.dinner_order_center_privilegement_no);
            vPrivilegeInfo.setVisibility(View.VISIBLE);
            tvPrivilegeNo.setVisibility(View.VISIBLE);
        } else {
            vPrivilegeInfo.setVisibility(View.GONE);
            tvPrivilegeNo.setVisibility(View.GONE);
        }
    }

    @Override
    public void showAdditionaltPrivilegeInfo(TradeVo tradeVo) {
        additional_privilege_view.removeAllItemView();
        List<TradePrivilege> tradePrivileges = mPresenter.getOrderPrivilegeList(tradeVo, PrivilegeType.ADDITIONAL);
        if (tradePrivileges.isEmpty()) {
            additional_privilege_view.setVisibility(View.GONE);
            return;
        }
        additional_privilege_view.setVisibility(View.VISIBLE);
        additional_privilege_view.setTitle(getResources().getString(R.string.extra_charge));
        BigDecimal totalBigDecimal = BigDecimal.ZERO;
        ExtraCharge outTimeFeeExtra = ServerSettingCache.getInstance().getmOutTimeRule();
        BigDecimal paidOutTimeFee = BigDecimal.ZERO;
        for (TradePrivilege tradePrivilege : tradePrivileges) {
            ExtraCharge extraCharge = null;
            Map<Long, ExtraCharge> extraChargeMap = tradeVo.getExtraChargeMap();
            if (extraChargeMap != null) {
                extraCharge = extraChargeMap.get(tradePrivilege.getPromoId());
            }
            BigDecimal amount = tradePrivilege.getPrivilegeAmount();
            totalBigDecimal = totalBigDecimal.add(amount);
            if (tradePrivilege.getPrivilegeType() != PrivilegeType.ADDITIONAL) {
                continue;
            }
            if (outTimeFeeExtra != null && MathDecimal.isLongEqual(tradePrivilege.getPromoId(), outTimeFeeExtra.getId())) {
                paidOutTimeFee = paidOutTimeFee.add(tradePrivilege.getPrivilegeAmount());
                continue;
            }
            additional_privilege_view.addItemNormalView(tradePrivilege.getPrivilegeName(),
                    null,
                    Utils.formatPrice(amount.abs().doubleValue()),
                    true);

        }
        if (ServerSettingCache.getInstance().getBuffetOutTimeFeeEnable()) {
            if (paidOutTimeFee.compareTo(BigDecimal.ZERO) > 0) {
                additional_privilege_view.addItemNormalView(outTimeFeeExtra.getName(),
                        null,
                        Utils.formatPrice(paidOutTimeFee.doubleValue()),
                        true);
            }
        }

        additional_privilege_view.addItemTotallView(null,
                null,
                Utils.formatPrice(totalBigDecimal.abs().doubleValue()),
                false);
    }

    @Override
    public void showServicePrivilegeInfo(TradeVo tradeVo) {
        service_privilege_view.removeAllItemView();
        List<TradePrivilege> tradePrivileges = mPresenter.getOrderPrivilegeList(tradeVo, PrivilegeType.SERVICE);
        if (tradePrivileges.isEmpty()) {
            service_privilege_view.setVisibility(View.GONE);
            return;
        }
        service_privilege_view.setVisibility(View.VISIBLE);
        service_privilege_view.setTitle(tradePrivileges.get(0).getPrivilegeName());

        service_privilege_view.addItemNormalView(tradePrivileges.get(0).getPrivilegeName(),
                null,
                Utils.formatPrice(tradePrivileges.get(0).getPrivilegeAmount().abs().doubleValue()),
                false);
    }


    @Override
    public void showTaxInfo(TradeVo tradeVo) {
        tax_view.setVisibility(View.GONE);
    }

    private void showTaxCode(TradeVo tradeVo) {
        if (Utils.isEmpty(tradeVo.getTradeTaxs())) {
            tax_view.setTime("");
        } else {
            if (tradeVo.getTradeInvoiceNo() != null && !TextUtils.isEmpty(tradeVo.getTradeInvoiceNo().getCode())) {
                tax_view.setTime(getString(R.string.invoice_no) + ":" + tradeVo.getTradeInvoiceNo().getCode());
            } else {
                if (tradeVo.getTrade().getTradePayStatus() == TradePayStatus.PAID || tradeVo.getTrade().getTradePayStatus() == TradePayStatus.PREPAID || tradeVo.getTrade().getTradeType() == TradeType.REFUND) {
                    String alterText = getString(R.string.invoice_no) + ":" + getString(R.string.tax_code_not_find);
                    SpannableStringBuilder builder =
                            new SpannableStringBuilder(alterText);
                    builder.setSpan(new ForegroundColorSpan(Color.RED),
                            0,
                            alterText.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tax_view.setTime(builder);
                    mPresenter.getTaxNoByTradeId(tradeVo.getTrade().getId());
                } else {
                    tax_view.setTime("");
                }
            }
        }
    }

    @Override
    public void showRejectGoodsInfo(TradeVo tradeVo) {
        vRejectGoodsInfo.setVisibility(View.GONE);
        if (tradeVo != null) {
            mRejectAdapter = new DinnerBillCenterRejectDishAdapter(getActivity());
            mRejectAdapter.setDataSet(tradeVo);
            rejectGoodsListView.setAdapter(mRejectAdapter);
            if (mRejectAdapter.getCount() > 0) {
                vRejectGoodsInfo.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void showRePayedTrades(List<TradePaymentVo> rePayedList) {
        llRepayList.removeAllViews();
        if (Utils.isNotEmpty(rePayedList)) {
            int count = rePayedList.size();
            for (int i = count - 1; i >= 0; i--) {
                OrderCenterRePayLayout infoLayout = OrderCenterRePayLayout_.build(getActivity(), rePayedList.get(i), i + 1, mPresenter);
                llRepayList.addView(infoLayout);
            }
        }
    }

    @Override
    public void showOperateReasonInfo(TradeVo tradeVo) {
        operating_info_view1.removeAllItemView();
        operating_info_view1.setVisibility(View.GONE);
        TradeStatus tradeStatus = tradeVo.getTrade().getTradeStatus();
        OperateType operateType = null;
        int resId = 0;
        int titleResId = 0;
        int operatResId = 0;
        switch (tradeStatus) {
            case RETURNED:
                TradeType tradeType = tradeVo.getTrade().getTradeType();
                if (tradeType == TradeType.REFUND) {
                    if (isNoOrderReturn(tradeVo)) {
                        operateType = OperateType.TRADE_HAVENOORDER_RETURN;
                    } else {
                        operateType = OperateType.TRADE_RETURNED;
                    }
                } else if (tradeType == TradeType.REFUND_FOR_REPEAT) {
                    operateType = OperateType.TRADE_REPEATED;
                }
                resId = R.string.order_center_detail_reason_return;
                titleResId = R.string.order_center_detail_info_return;
                operatResId = R.string.order_center_detail_operat_return;
                break;
            case INVALID:
                if (mFromType == OCConstant.FromType.FROM_TYPE_SNACK || mFromType == OCConstant.FromType.FROM_TYPE_RETAIL) {
                    operateType = OperateType.TRADE_FASTFOOD_INVALID;
                } else {
                    operateType = OperateType.TRADE_DINNER_INVALID;
                }
                resId = R.string.order_center_detail_reason_invalid;
                titleResId = R.string.order_center_detail_info_invalid;
                operatResId = R.string.order_center_detail_operat_invalid;
                break;
            case REFUSED:
                if (mFromType == OCConstant.FromType.FROM_TYPE_SNACK || mFromType == OCConstant.FromType.FROM_TYPE_RETAIL) {
                    operateType = OperateType.TRADE_FASTFOOD_REFUSE;
                } else {
                    operateType = OperateType.TRADE_DINNER_REFUSE;
                }
                resId = R.string.order_center_detail_reason_refuse;
                titleResId = R.string.order_center_detail_info_refuse;
                operatResId = R.string.order_center_detail_operat_refuse;
                break;
            case REPEATED:
                operateType = OperateType.TRADE_REPEATED;
                resId = R.string.order_center_detail_reason_repeated;
                titleResId = R.string.order_center_detail_info_repeated;
                operatResId = R.string.order_center_detail_operat_repeated;
                break;
            case CREDIT:
            case WRITEOFF:
                operateType = OperateType.TRADE_CREDIT;
                resId = R.string.order_center_detail_reason_credit;
                titleResId = R.string.order_center_detail_info_credit;
                operatResId = R.string.order_center_detail_operat_credit;
                break;
            case FINISH:
                if (null != tradeVo.getTradeReasonRelList() && !tradeVo.getTradeReasonRelList().isEmpty()) {
                    operateType = OperateType.TRADE_CREDIT;
                    resId = R.string.order_center_detail_reason_credit;
                    titleResId = R.string.order_center_detail_info_credit;
                    operatResId = R.string.order_center_detail_operat_credit;
                }
                break;
            case CANCELLED:
                if (tradeVo.getTrade().getSource() == SourceId.KOU_BEI
                        && !tradeVo.getTradeReasonRelList().isEmpty()) {
                    operateType = OperateType.TRADE_CANCELLED;
                    resId = R.string.order_center_detail_reason_cancel;
                    titleResId = R.string.order_center_detail_info_cancel;
                    operatResId = R.string.order_center_detail_operat_cancel;
                }
                break;
            default:
                if (null != tradeVo.getTradeReasonRelList() && !tradeVo.getTradeReasonRelList().isEmpty()) {
                    operateType = OperateType.TRADE_CREDIT;
                    resId = R.string.order_center_detail_reason_unknow;
                    titleResId = R.string.order_center_detail_info_unknow;
                    operatResId = R.string.order_center_detail_operat_unknow;
                }
                break;
        }
        if (operateType != null) {
            TradeReasonRel operateReason = tradeVo.getOperateReason(operateType);
            String date;
            if (operateType == OperateType.TRADE_RETURNED) {
                date = getString(R.string.order_center_detail_return_time, DateTimeUtils.formatDateTime(tradeVo.getTrade().getServerUpdateTime()));
            } else {
                date = DateTimeUtils.formatDateTime(tradeVo.getTrade().getServerUpdateTime());
            }
            String username = tradeVo.getTrade().getUpdatorName();
            operating_info_view1.setVisibility(View.VISIBLE);
            operating_info_view1.setTitle(getString(titleResId));
            operating_info_view1.setTime(date);
            if (operateType == OperateType.TRADE_CREDIT) {
                addCreditInfo(tradeVo, operating_info_view1);
            }
            if (!TextUtils.isEmpty(username)) {
                operating_info_view1.addItemNormalView(getString(operatResId) + username, null, null, false);
            }
        }
    }


    private void addCreditInfo(TradeVo tradeVo, OrderCenterDetailView credit_detailview) {
        List<TradeCreditLog> tradeCreditLogList = tradeVo.getTradeCreditLogList();
        if (null != tradeCreditLogList && !tradeCreditLogList.isEmpty()) {
            String tite = "";
            TradeCreditLog currentTradeCreditLog = null;
            for (TradeCreditLog tradeCreditLog : tradeCreditLogList) {
                if (tradeCreditLog.getCreditType() == CreditType.WRITEOFF) {
                    currentTradeCreditLog = tradeCreditLog;
                    break;
                }
                currentTradeCreditLog = tradeCreditLog;
            }
            if (currentTradeCreditLog.getCreditType() == CreditType.LEDGER) {
                tite = getString(R.string.order_center_dinner_bill);
            } else if (currentTradeCreditLog.getCreditType() == CreditType.WRITEOFF) {
                tite = getString(R.string.order_center_dinner_book);
            }
            credit_detailview.setTitle(tite + getString(R.string.dinner_order_center_goods_item_info));
            BigDecimal creditAmount = currentTradeCreditLog.getAmount();
            credit_detailview.addItemNormalView(tite, Utils.formatPrice(creditAmount.doubleValue()), "", true);
            String name = "";
            if (null != currentTradeCreditLog && null != currentTradeCreditLog.getCustomerId()) {
                if (currentTradeCreditLog.getCustomerName() != null) {
                    name = currentTradeCreditLog.getCustomerName();
                } else {
                    name = getString(R.string.dinner_order_center_member_id, String.valueOf(currentTradeCreditLog.getCustomerId()));
                }
            }
            credit_detailview.addItemNormalView(tite + getString(R.string.dinner_order_center_target_people) + name, "", "", true);
        } else {
            credit_detailview.setVisibility(View.GONE);
        }
    }


    @Override
    public void showBillInfo(TradeVo tradeVo, TradeVo oriTradeVo) {
        if (tradeVo != null) {
            Trade trade = tradeVo.getTrade();
            if (oriTradeVo.getTrade().getTradeType() == TradeType.SELL
                    || oriTradeVo.getTrade().getTradeType() == TradeType.SPLIT
                    || oriTradeVo.getTrade().getTradeType() == TradeType.SELL_FOR_REPEAT
                    || oriTradeVo.getTrade().getTradeType() == TradeType.SELL_FOR_REVERSAL
                    || oriTradeVo.getTrade().getTradeType() == TradeType.UNOIN_TABLE_MAIN
                    || oriTradeVo.getTrade().getTradeType() == TradeType.UNOIN_TABLE_SUB) {
                Long tradeCreateTime = oriTradeVo.getTrade().getServerCreateTime();
                mTvBillTime.setText(getString(R.string.dinner_order_center_trade_time,
                        DateTimeUtils.formatDateTime(tradeCreateTime)));
            } else {
                mTvBillTime.setText(getString(R.string.dinner_order_center_trade_time, getString(R.string.three_days_ago)));
                if (isNoOrderReturn(oriTradeVo)) {
                    mTvBillTime.setText(getString(R.string.dinner_order_center_trade_time,
                            DateTimeUtils.formatDateTime(oriTradeVo.getTrade().getServerCreateTime())));
                }
            }

            if (tradeVo.isAppointmentOrder()) {
                mTvBillTime.append("        " + getString(R.string.dinner_appointment_order_hint, DateTimeUtils.formatDate(tradeVo.getTradeExtra().getExpectTime(),
                        DateTimeUtils.DATE_TIME_FORMAT4)));
            }

            groupNameLayout.setVisibility(mFromType == OCConstant.FromType.FROM_TYPE_GROUP ? View.VISIBLE : View.GONE);
            tvGroupName.setText(getString(R.string.dinner_order_center_group_name, tradeVo.getTradeGroup() != null ? tradeVo.getTradeGroup().getName() : ""));

            String creatorName = trade.getCreatorName();
            if (!TextUtils.isEmpty(creatorName)) {
                mTvOperationBillPeople
                        .setText(getString(R.string.dinner_order_center_trade_creator, creatorName));
                mTvOperationBillPeople.setVisibility(View.VISIBLE);
                mLineOperationPeople.setVisibility(View.VISIBLE);
            } else {
                mTvOperationBillPeople.setVisibility(View.INVISIBLE);
                mLineOperationPeople.setVisibility(View.INVISIBLE);
            }
            showEditableMemo(trade);


            tvVerifyStatus.setVisibility(View.GONE);
            if (tradeVo.getVerifyKoubeiOrder() != null && tradeVo.getTrade().getTradeStatus() != TradeStatus.UNPROCESSED) {
                tvVerifyStatus.setVisibility(View.VISIBLE);
                tvVerifyStatus.setText(getString(R.string.order_detail_verify_status, getVerifyStatusStr(tradeVo.getVerifyKoubeiOrder())));
            }

            TradeExtra tradeExtra = tradeVo.getTradeExtra();
            if (tradeExtra != null && !TextUtils.isEmpty(tradeExtra.getSerialNumber())) {
                mTvBillSerialNumber
                        .setText(getString(R.string.dinner_order_center_serial_number, tradeExtra.getSerialNumber()));
                mTvBillSerialNumber.setVisibility(View.VISIBLE);
            } else {
                mTvBillSerialNumber.setVisibility(View.GONE);
            }

            List<TradeTable> tradeTableList = tradeVo.getTradeTableList();
            if (tradeTableList != null && tradeTableList.size() > 0) {
                mTvTableNumber.setVisibility(View.VISIBLE);
                BigDecimal deskCount = tradeVo.getDeskCount();
                if (deskCount.compareTo(BigDecimal.ONE) > 0) {
                    String deskCountString = String.format(getString(R.string.group_order_item_desknum), deskCount.toString());
                    mTvTableNumber.setText(deskCountString);
                } else {
                    TradeTable tradeTable = tradeTableList.get(0);
                    String tableName = tradeTable.getTableName();
                    if (!TextUtils.isEmpty(tableName)) {
                        mTvTableNumber.setText(getString(getLeftDisplayForTable(trade.getSource()), tableName));
                    } else {
                        mTvTableNumber.setText(getString(getLeftDisplayForTable(trade.getSource()), getString(R.string.nothing)));
                    }
                }
            } else if (tradeExtra != null && !TextUtils.isEmpty(tradeExtra.getNumberPlate())) {
                mTvTableNumber.setVisibility(View.VISIBLE);
                IPanelItemSettings settings = SettingManager.getSettings(IPanelItemSettings.class);
                if (settings != null) {
                    int serialMode = settings.getSerialMode();
                    switch (serialMode) {
                        case SERIAL_DESK_MODE:
                            mTvTableNumber.setText(getString(getLeftDisplayForTable(trade.getSource()),
                                    tradeExtra.getNumberPlate()));
                            break;
                        default:
                            mTvTableNumber.setText(getString(getLeftDisplayForNumberPlate(trade.getSource()),
                                    tradeExtra.getNumberPlate()));
                            break;
                    }
                } else {
                    mTvTableNumber.setText(tradeExtra.getNumberPlate());
                }
            } else {
                mTvTableNumber.setVisibility(View.GONE);
            }

            if (mFromType != OCConstant.FromType.FROM_TYPE_RETAIL) {
                mTvPeopleCount.setVisibility(View.VISIBLE);
                mTvPeopleCount.setText(getString(R.string.dinner_order_center_people_count, String.valueOf(trade.getTradePeopleCount())) + tradeVo.getTradeBuffetPeopleTip());
            }

            Long mealTime = getMealTime(oriTradeVo);
            if (mealTime != null) {
                long minutes = TimeUnit.MILLISECONDS.toMinutes(mealTime);
                String formatMealTimeStr = String.format("%02d:%02d", minutes / 60, minutes % 60);
                mTvDinnerTime.setText(getActivity().getString(R.string.order_meal_time_st, formatMealTimeStr));
                mTvDinnerTime.setVisibility(View.VISIBLE);
            } else {
                mTvDinnerTime.setVisibility(View.GONE);
            }


            showShopperInfo(tradeVo, vShopperGroup, mTvShopper, mTvCounselor, mViewLineShopper);


            List<String> customerInfo = getCustomerInfo(tradeVo);
            if (Utils.isNotEmpty(customerInfo)) {
                clCustomer.setData(customerInfo);
                vLineCustomer.setVisibility(View.VISIBLE);
            } else {
                clCustomer.setVisibility(View.GONE);
                vLineCustomer.setVisibility(View.GONE);
            }
            String tradeSource = mPresenter.getTradeSource(getActivity(), trade);
            if (!TextUtils.isEmpty(tradeSource)) {
                tvTradeSource.setText(getString(R.string.dinner_order_center_tradesource, tradeSource));
                tvTradeSource.setVisibility(View.VISIBLE);
                mLineOperationPeople.setVisibility(View.VISIBLE);
            } else {
                tvTradeSource.setVisibility(View.INVISIBLE);
                mLineOperationPeople.setVisibility((View.INVISIBLE));
            }

            String tradeAccept = mPresenter.getTradeAcceptSource(tradeVo.getTradeReceiveLog());
            if (!TextUtils.isEmpty(tradeAccept)) {
                tvAcceptSource.setText(getString(R.string.order_center_detail_accept, tradeAccept));
                tvAcceptSource.setVisibility(View.VISIBLE);
            } else {
                tvAcceptSource.setVisibility(View.GONE);
            }
            final String origonTradeNumber = mPresenter.getSplideOrigonTradeNumber(trade);
            if (!TextUtils.isEmpty(origonTradeNumber)) {
                tvOrigonTradeNumber
                        .setText(getString(R.string.dinner_order_center_origontrade_number, origonTradeNumber));
                tvOrigonTradeNumber.setVisibility(View.VISIBLE);
            } else {
                tvOrigonTradeNumber.setVisibility(View.INVISIBLE);
            }
            vBillInfo.setVisibility(View.VISIBLE);
            if (mTvOperationBillPeople.getVisibility() == View.VISIBLE || mTvTableServerPeople.getVisibility() == View.VISIBLE) {
                vPeopleGroup.setVisibility(View.VISIBLE);
            } else {
                vPeopleGroup.setVisibility(View.GONE);
            }
        } else {
            vBillInfo.setVisibility(View.GONE);
        }
    }

    private String getVerifyStatusStr(VerifyKoubeiOrder verifyKoubeiOrder) {
        if (verifyKoubeiOrder.getVerifyStatus() == VerifyKoubeiOrder.VerifyStatus.VERIFY_SUCCESS) {
            return getString(R.string.order_detail_verify_already);
        }
        return getString(R.string.order_detail_verify_waiting);
    }

    private void showShopperInfo(TradeVo tradeVo, LinearLayout shopperGroup, TextView tvShopper, TextView tvCounselor, View line) {
        if (tradeVo == null || Utils.isEmpty(tradeVo.getTradeUsers())) {
            shopperGroup.setVisibility(View.GONE);
            return;
        }

        StringBuffer shoppersBuf = new StringBuffer();
        StringBuffer advisersBuf = new StringBuffer();

        for (TradeUser tradeUser : tradeVo.getTradeUsers()) {
        }

        if (shoppersBuf.length() > 0) {
            line.setVisibility(View.VISIBLE);
            shopperGroup.setVisibility(View.VISIBLE);
            tvShopper.setVisibility(View.VISIBLE);

            tvShopper.setText(R.string.beauty_shopowner);
            tvShopper.append(" " + shoppersBuf.subSequence(0, shoppersBuf.length() - 1));
        }

        if (advisersBuf.length() > 0) {
            line.setVisibility(View.VISIBLE);
            shopperGroup.setVisibility(View.VISIBLE);
            tvCounselor.setVisibility(View.VISIBLE);

            tvCounselor.setText(R.string.beauty_adviser);
            tvCounselor.append(" " + advisersBuf.subSequence(0, advisersBuf.length() - 1));
        }
    }


    private void showEditableMemo(final Trade trade) {
        mTvBillTableMemo.setText(R.string.order_dish_memo_semicolon);
        tvEditableMemo.setText(Utils.trim(trade.getTradeMemo(), "无"));
    }

    private void modifyTradeMemo(Trade trade, String memo) {
        TradeOperates operates = OperatesFactory.create(TradeOperates.class);
        ResponseListener<ModifyTradeMemoResp> listener = new ResponseListener<ModifyTradeMemoResp>() {
            @Override
            public void onResponse(ResponseObject<ModifyTradeMemoResp> response) {
                if (ResponseObject.isOk(response)) {
                    tvEditableMemo.setText(Utils.trim(response.getContent().trade.getTradeMemo(), ""));
                } else {
                    ToastUtil.showShortToast(response.getMessage());
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showShortToast(error.getMessage());
            }
        };
        operates.modifyTradeMemo(trade, memo, LoadingResponseListener.ensure(listener, getFragmentManager()));
    }

    public Long getMealTime(TradeVo tradeVo) {
        Long mealTime = null;
        if (tradeVo != null && tradeVo.getTrade().getBusinessType() == BusinessType.SNACK) {
            long currentTimeMillis = System.currentTimeMillis();
            List<TradeTable> tradeTableList = tradeVo.getTradeTableList();
            List<TradeStatusLog> tradeStatusLogList = tradeVo.getTradeStatusLogList();
            if (Utils.isNotEmpty(tradeTableList) && Utils.isNotEmpty(tradeStatusLogList)) {
                if (tradeStatusLogList.size() > 1) {
                    mealTime = tradeStatusLogList.get(tradeStatusLogList.size() - 1).getServerCreateTime()
                            - tradeStatusLogList.get(0).getServerCreateTime();
                } else if (tradeStatusLogList.size() == 1) {
                    mealTime = currentTimeMillis - tradeVo.getTradeStatusLogList().get(0).getServerCreateTime();
                } else {
                    mealTime = currentTimeMillis - tradeVo.getTrade().getServerCreateTime();
                }
            }

        }

        return mealTime;
    }

    private List<String> getCustomerInfo(TradeVo tradeVo) {
        List<String> customerInfo = new ArrayList<String>();

        List<TradeCustomer> tradeCustomers = tradeVo.getTradeCustomerList();
        if (Utils.isNotEmpty(tradeCustomers)) {
            for (TradeCustomer tradeCustomer : tradeCustomers) {
                customerInfo.clear();
                if (tradeCustomer.getCustomerType() == CustomerType.BOOKING
                        || tradeCustomer.getCustomerType() == CustomerType.CUSTOMER) {
                    if (tradeCustomer.getCustomerName() != null) {
                        customerInfo.add(getActivity().getString(R.string.dinner_order_center_customer_name,
                                tradeCustomer.getCustomerName()));
                    }
                    if (tradeCustomer.getCustomerPhone() != null) {
                        customerInfo.add(getActivity().getString(R.string.dinner_order_center_customer_account,
                                tradeCustomer.getCustomerPhone()));
                    }

                } else if (tradeCustomer.getCustomerType() == CustomerType.CARD) {
                    if (!TextUtils.isEmpty(tradeCustomer.getEntitycardNum())) {
                        customerInfo.add(getActivity().getString(R.string.dinner_order_center_card_account,
                                tradeCustomer.getEntitycardNum()));
                    }
                    break;
                } else if (tradeCustomer.getCustomerType() == CustomerType.MEMBER) {
                    if (!TextUtils.isEmpty(tradeCustomer.getCustomerName())) {
                        customerInfo.add(getActivity().getString(R.string.dinner_order_center_member_name,
                                tradeCustomer.getCustomerName()));
                    }
                    if (!TextUtils.isEmpty(tradeCustomer.getCustomerPhone())) {
                        customerInfo.add(getActivity().getString(R.string.dinner_order_center_customer_account,
                                tradeCustomer.getCustomerPhone()));
                    }
                    break;
                }
            }
        }

        return customerInfo;
    }

    @Override
    public void showGoodsInfo(TradePaymentVo tradePaymentVo, List<TradeItemVo> tradeItemVos, TradePaymentVo oriTradePaymentVo) {
        if (tradePaymentVo.getTradeVo() != null) {
            TradeVo payTradeVo = tradePaymentVo.getTradeVo();
            if (Utils.isNotEmpty(tradeItemVos) || Utils.isNotEmpty(payTradeVo.getTradeBuffetPeoples())) {
                if (payTradeVo.getTrade().getTradeType() == TradeType.REFUND ||
                        payTradeVo.getTrade().getTradeType() == TradeType.REFUND_FOR_REPEAT ||
                        payTradeVo.getTrade().getTradeType() == TradeType.REFUND_FOR_REVERSAL) {
                    mAdapter = new OrderCenterDishAdapter(getActivity(), true);
                } else {
                    mAdapter = new OrderCenterDishAdapter(getActivity(), false);
                }
                mAdapter.setFromType(mFromType);
                mAdapter.setDataSet(payTradeVo);
                goodsListView.setAdapter(mAdapter);
                BigDecimal totalAmount = getGoodsAmount(payTradeVo, oriTradePaymentVo);
                mTvAmount.setText(Utils.formatPrice(totalAmount.doubleValue()));
                mTvNumber.setVisibility(View.VISIBLE);
                mTvNumber.setText(getString(R.string.dinner_order_center_goods_total_number, getAllDishCount(tradeItemVos, payTradeVo.getTrade().getTradeType(), payTradeVo)));
                goods_info.setVisibility(View.VISIBLE);
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (scrollView != null) {
                            scrollView.scrollTo(0, 0);
                        }
                    }
                });
            } else {
                goods_info.setVisibility(View.GONE);
            }
        } else {
            goods_info.setVisibility(View.GONE);
        }
    }

    private BigDecimal getGoodsAmount(TradeVo tradeVo, TradePaymentVo oriTradePaymentVo) {
        if (tradeVo.getTrade().getTradeType() == TradeType.REFUND ||
                tradeVo.getTrade().getTradeType() == TradeType.REFUND_FOR_REPEAT ||
                tradeVo.getTrade().getTradeType() == TradeType.REFUND_FOR_REVERSAL) {
            if (oriTradePaymentVo != null && oriTradePaymentVo.getTradeVo() != null) {
                return getGoodsAmount(oriTradePaymentVo.getTradeVo());
            } else {
                return getGoodsAmount(tradeVo);
            }
        } else {
            return getGoodsAmount(tradeVo);
        }
    }

    public static BigDecimal getGoodsAmount(TradeVo tradeVo) {
        return getGoodsAmountNew(tradeVo);
    }

    private BigDecimal getGoodsAmountOld(TradeVo tradeVo) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal exemptAmount = BigDecimal.ZERO;
        BigDecimal actualAmount = tradeVo.getTrade().getSaleAmount();
        String discountAmount =
                Utils.transferDot2(tradeVo.getTrade().getPrivilegeAmount().add(exemptAmount.negate()).toString());
        if (!TextUtils.isEmpty(discountAmount)) {
            List<TradePrivilege> tradePrivileges = tradeVo.getTradePrivileges();
            if (tradePrivileges != null) {
                for (TradePrivilege tradePrivilege : tradeVo.getTradePrivileges()) {
                    if (tradePrivilege.getPrivilegeType() == PrivilegeType.ADDITIONAL) {
                        BigDecimal privilegeAmount = tradePrivilege.getPrivilegeAmount();
                        actualAmount = actualAmount.subtract(privilegeAmount);
                    }
                }
            }
            totalAmount = actualAmount;
        }
        TradeDeposit tradeDeposit = tradeVo.getTradeDeposit();
        if (tradeDeposit != null && tradeDeposit.getDepositPay() != null) {
            totalAmount = totalAmount.subtract(tradeDeposit.getDepositPay());
        }
        return totalAmount.abs();
    }

    private static BigDecimal getGoodsAmountNew(TradeVo tradeVo) {
        BigDecimal goodsAmount = BigDecimal.ZERO;
        boolean isNegate = false;
        if (tradeVo.getTrade().getTradeType() == TradeType.REFUND ||
                tradeVo.getTrade().getTradeType() == TradeType.REFUND_FOR_REPEAT ||
                tradeVo.getTrade().getTradeType() == TradeType.REFUND_FOR_REVERSAL) {
            isNegate = true;
        }
        List<TradeItemVo> tradeItemVos;
        if ((tradeItemVos = tradeVo.getTradeItemList()) != null) {
            for (TradeItemVo tradeItemVo : tradeItemVos) {
                StatusFlag statusFlag = tradeItemVo.getTradeItem().getStatusFlag();
                if (statusFlag == StatusFlag.VALID) {
                    TradeItem tradeItem = tradeItemVo.getTradeItem();
                    BigDecimal itemAmount = tradeItem.getAmount();
                    BigDecimal itemPropertyAmount = tradeItem.getPropertyAmount();
                    goodsAmount = goodsAmount.add(isNegate ? itemAmount.negate() : itemAmount);
                    goodsAmount = goodsAmount.add(isNegate ? itemPropertyAmount.negate() : itemPropertyAmount);
                }
            }
        }

        if (tradeVo.getMealShellVo() != null && tradeVo.getMealShellVo().getActualAmount() != null) {
            goodsAmount = goodsAmount.add(tradeVo.getMealShellVo().getActualAmount());
        }

        return goodsAmount;
    }


    public String getAllDishCount(List<TradeItemVo> list, TradeType tradeType, TradeVo tradeVo) {
        BigDecimal count = BigDecimal.ZERO;
        if (list != null) {
            String shellUuid = null;
            if (tradeVo.getMealShellVo() != null) {
                shellUuid = tradeVo.getMealShellVo().getUuid();
            }
            for (int i = list.size() - 1; i >= 0; i--) {
                TradeItem tradeItem = list.get(i).getTradeItem();
                if (tradeItem.getType() == DishType.SINGLE && !TextUtils.isEmpty(tradeItem.getParentUuid()) && !(tradeItem.getParentUuid().equals(shellUuid))) {
                    continue;
                }
                if (tradeItem.getType() == DishType.SINGLE || tradeItem.getType() == DishType.COMBO) {
                    if (tradeItem.getSaleType() == SaleType.WEIGHING) {
                        count = count.add(BigDecimal.ONE);
                    } else {
                        count = count.add(new BigDecimal(tradeItem.getQuantity() + "").abs());
                    }
                }
            }
        }

        if (Utils.isNotEmpty(tradeVo.getTradeBuffetPeoples())) {
            for (TradeBuffetPeople tradeBuffetPeople : tradeVo.getTradeBuffetPeoples()) {
                count = count.add(tradeBuffetPeople.getPeopleCount());
            }
        }

        return MathDecimal.trimZero(count).toString();
    }

    @Override
    public void showPayInfo(TradePaymentVo tradePaymentVo, TradePaymentVo oriTradePaymentVo, boolean isRefund) {

        if (oriTradePaymentVo.getTradeVo().getTrade().getTradeType() == TradeType.SELL
                || oriTradePaymentVo.getTradeVo().getTrade().getTradeType() == TradeType.SPLIT
                || oriTradePaymentVo.getTradeVo().getTrade().getTradeType() == TradeType.SELL_FOR_REVERSAL
                || oriTradePaymentVo.getTradeVo().getTrade().getTradeType() == TradeType.SELL_FOR_REPEAT) {
            if (Utils.isNotEmpty(oriTradePaymentVo.getPaymentVoList())) {
                tvPaymentTime.setText(getString(R.string.dinner_order_center_payment_time,
                        DateTimeUtils.formatDateTime(oriTradePaymentVo.getPaymentVoList().get(0).getPayment().getPaymentTime())));
                tvPaymentTime.setVisibility(View.VISIBLE);
            } else {
                tvPaymentTime.setVisibility(View.GONE);
            }
        } else {
            tvPaymentTime.setText(getString(R.string.dinner_order_center_payment_time, getString(R.string.three_days_ago)));
            if (isNoOrderReturn(oriTradePaymentVo.getTradeVo())) {
                tvPaymentTime.setText(getString(R.string.dinner_order_center_trade_time,
                        DateTimeUtils.formatDateTime(oriTradePaymentVo.getPaymentVoList().get(0).getPayment().getPaymentTime())));
            }
            tvPaymentTime.setVisibility(View.VISIBLE);
        }

        llPayInfoContent.removeAllViews();
        vPayInfo.setVisibility(View.GONE);
        TradeVo tradeVo = tradePaymentVo.getTradeVo();
        List<PaymentVo> paymentVoList = tradePaymentVo.getPaymentVoList();
        List<PaymentVo> paymentTempVoList = new ArrayList<PaymentVo>();
        if (Utils.isNotEmpty(paymentVoList)) {
            for (PaymentVo paymentVo : paymentVoList) {
                if (PaymentType.ADJUST != paymentVo.getPayment().getPaymentType()) {
                    paymentTempVoList.add(paymentVo);
                }
            }
        }
        llPayInfoContent.removeAllViews();
        List<PaymentVo> paymentVos = new ArrayList<PaymentVo>();
        if (Utils.isNotEmpty(paymentTempVoList)) {
            paymentVos.addAll(paymentTempVoList);
        }
        if (isNeedShowPayInfo(paymentVos)) {
            View view = mPresenter.createPayInfoItem(getActivity(), tradePaymentVo, oriTradePaymentVo, isRefund);
            llPayInfoContent.addView(view);
            vPayInfo.setVisibility(mPresenter.showPayInfo(tradeVo, paymentTempVoList) ? View.VISIBLE : View.GONE);
        }
    }

    private boolean isNeedShowPayInfo(List<PaymentVo> paymentVos) {
        for (PaymentVo paymentVo : paymentVos) {
            if (paymentVo.getPayment().getPaymentType() == PaymentType.TRADE_SELL) {
                List<PaymentItem> paymentItems = paymentVo.getPaymentItemList();
                for (PaymentItem paymentItem : paymentItems) {
//                    if (paymentItem.getPayModeId() == PayModeId.ALIPAY.value() || paymentItem.getPayModeId() == PayModeId.WEIXIN_PAY.value())
                        return true;
                }
            }
        }
        return false;
    }

    @Override
    public void setTradeVo(TradeVo tradeVo) {
        mTradeVo = tradeVo;
    }

    @Override
    public void showDeliveryInfoView(boolean show) {
        viewDelivery.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void refreshDeliveryUserInfoView(TradePaymentVo tradePaymentVo) {
        tvDeliveryUserName.setVisibility(View.GONE);
        tvDeliveryUserPhone.setVisibility(View.GONE);
        if (Utils.isNotEmpty(tradePaymentVo.getDeliveryOrderVoList())) {
            DeliveryOrderVo deliveryOrderVo = getDeliveryOrderVo(tradePaymentVo.getDeliveryOrderVoList());
            if (deliveryOrderVo != null) {
                DeliveryOrder deliveryOrder = deliveryOrderVo.getDeliveryOrder();
                if (!TextUtils.isEmpty(deliveryOrder.getDelivererName())
                        && !TextUtils.isEmpty(deliveryOrder.getDelivererPhone())) {
                    tvDeliveryUserName.setText(getString(R.string.delivery_user_name, deliveryOrder.getDelivererName()));
                    tvDeliveryUserName.setVisibility(View.VISIBLE);
                    tvDeliveryUserPhone.setText(getString(R.string.order_center_detail_delivery_phone, deliveryOrder.getDelivererPhone()));
                    tvDeliveryUserPhone.setVisibility(View.VISIBLE);
                }
            }
        } else {
            TradeExtra tradeExtra = tradePaymentVo.getTradeVo().getTradeExtra();
            if (tradeExtra != null && !TextUtils.isEmpty(tradeExtra.getDeliveryUserId())) {
                List<User> users = Session.getFunc(UserFunc.class).getUsers(FastFoodApplication.PERMISSION_FASTFOOD_SC);
                if (Utils.isNotEmpty(users)) {
                    for (User user : users) {
                        if (tradeExtra.getDeliveryUserId().equals(user.getAccount())
                                || tradeExtra.getDeliveryUserId().equals(user.getId().toString())) {
                            tvDeliveryUserName.setText(getString(R.string.delivery_user_name, user.getName()));
                            tvDeliveryUserName.setVisibility(View.VISIBLE);
                            tvDeliveryUserPhone.setText(getString(R.string.order_center_detail_delivery_phone, user.getMobile()));
                            tvDeliveryUserPhone.setVisibility(View.VISIBLE);
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void refreshDeliveryPlatformInfoView(TradePaymentVo tradePaymentVo) {
        Trade trade = tradePaymentVo.getTradeVo().getTrade();
        if (trade != null
                && trade.getBusinessType() == BusinessType.TAKEAWAY
                && trade.getDeliveryType() == DeliveryType.SEND
                && (trade.getTradeStatus() == TradeStatus.CONFIRMED || trade.getTradeStatus() == TradeStatus.FINISH)) {
            DeliveryPlatform deliveryPlatform = getDeliveryPlatform(tradePaymentVo);
            String deliveryPlatformName = getDeliveryPlatformName(deliveryPlatform);
            tvDeliveryPlatform.setText(getString(R.string.order_center_detail_delivery_platform, deliveryPlatformName));
            tvDeliveryPlatform.setVisibility(TextUtils.isEmpty(deliveryPlatformName) ? View.GONE : View.VISIBLE);
        } else {
            tvDeliveryPlatform.setVisibility(View.GONE);
        }
    }

    private DeliveryPlatform getDeliveryPlatform(TradePaymentVo tradePaymentVo) {
        DeliveryPlatform deliveryPlatform = null;
        DeliveryOrderVo deliveryOrderVo = getDeliveryOrderVo(tradePaymentVo.getDeliveryOrderVoList());
        if (deliveryOrderVo != null) {
            deliveryPlatform = deliveryOrderVo.getDeliveryOrder().getDeliveryPlatform();
        }

        TradeExtra tradeExtra = tradePaymentVo.getTradeVo().getTradeExtra();
        if (deliveryPlatform == null
                && tradeExtra != null
                && (!TextUtils.isEmpty(tradeExtra.getDeliveryUserId()) || DeliveryPlatform.MERCHANT != tradeExtra.getDeliveryPlatform())) {
            deliveryPlatform = tradeExtra.getDeliveryPlatform();
        }

        return deliveryPlatform;
    }

    @Override
    public void refreshTipInfoView(TradePaymentVo tradePaymentVo) {
        List<DeliveryOrderVo> deliveryOrderVos = tradePaymentVo.getDeliveryOrderVoList();
        DeliveryOrderVo deliveryOrderVo = getDeliveryOrderVo(deliveryOrderVos);
        if (deliveryOrderVo != null) {
            DeliveryOrder deliveryOrder = deliveryOrderVo.getDeliveryOrder();
            if (deliveryOrder != null) {
                DeliveryPlatform deliveryPlatform = deliveryOrder.getDeliveryPlatform();
                DeliveryOrderStatus deliveryOrderStatus = deliveryOrder.getDeliveryStatus();
                PartnerDeliveryPlatformConfig partnerDeliveryPlatformConfig = ServerSettingManager.getPartnerDeliveryPlatformConfig(DeliveryPlatform.DA_DA);
                refreshTipHintView(deliveryPlatform, deliveryOrderStatus, partnerDeliveryPlatformConfig);
                refreshPassiveAddTipView(deliveryPlatform, deliveryOrderStatus, partnerDeliveryPlatformConfig);
            }

            List<DeliveryOrderRecord> tipRecords = getTipRecords(deliveryOrderVo.getDeliveryOrderRecords());
            refreshTipRecordView(tipRecords);
            refreshTipTotalAmountView(tipRecords);

            boolean showTip = tvFeeHint.getVisibility() == View.VISIBLE
                    || mListViewFee.getVisibility() == View.VISIBLE
                    || tvFeeTotal.getVisibility() == View.VISIBLE
                    || tvPassiveAddTip.getVisibility() == View.VISIBLE;
            vTip.setVisibility(showTip ? View.VISIBLE : View.GONE);
            vDeliveryInfoDivideLine.setVisibility(vTip.getVisibility());
        } else {
            vTip.setVisibility(View.GONE);
            vDeliveryInfoDivideLine.setVisibility(View.GONE);
        }
    }

    private void refreshTipHintView(DeliveryPlatform deliveryPlatform, DeliveryOrderStatus deliveryOrderStatus,
                                    PartnerDeliveryPlatformConfig partnerDeliveryPlatformConfig
    ) {
        if (deliveryPlatform != null
                && isSupportTip(deliveryPlatform)
                && deliveryOrderStatus != null
                && deliveryOrderStatus == DeliveryOrderStatus.WAITING_ACCEPT) {
            if (partnerDeliveryPlatformConfig != null && partnerDeliveryPlatformConfig.getIsAutoAddTip() == YesOrNo.YES) {
                tvFeeHint.setText(getString(R.string.order_center_detail_tip_hint_auto, partnerDeliveryPlatformConfig
                        .getIntervalTime(), partnerDeliveryPlatformConfig.getAutoAddMoney()));
            } else {
                tvFeeHint.setText(getString(R.string.order_center_detail_tip_hint_passive));
            }

            tvFeeHint.setVisibility(View.VISIBLE);
        } else {
            tvFeeHint.setVisibility(View.GONE);
        }
    }

    private List<DeliveryOrderRecord> getTipRecords(List<DeliveryOrderRecord> deliveryOrderRecords) {
        if (Utils.isNotEmpty(deliveryOrderRecords)) {
            List<DeliveryOrderRecord> tipRecords = new ArrayList<DeliveryOrderRecord>();

            for (DeliveryOrderRecord deliveryOrderRecord : deliveryOrderRecords) {
                if (DeliveryOrderRecordOpType.ADD_TIP == deliveryOrderRecord.getOpType()) {
                    tipRecords.add(deliveryOrderRecord);
                }
            }

            return tipRecords;
        }

        return Collections.emptyList();
    }

    private void refreshTipRecordView(List<DeliveryOrderRecord> deliveryOrderRecords) {
        if (Utils.isNotEmpty(deliveryOrderRecords)) {
            OrderDeliveryFeeAdapter orderDeliveryFeeAdapter = new OrderDeliveryFeeAdapter(getActivity(), deliveryOrderRecords);
            mListViewFee.setAdapter(orderDeliveryFeeAdapter);
            mListViewFee.setVisibility(View.VISIBLE);
        } else {
            mListViewFee.setVisibility(View.GONE);
        }
    }

    private void refreshTipTotalAmountView(List<DeliveryOrderRecord> deliveryOrderRecords) {
        BigDecimal tipTotalAmount = calculateTipTotalAmount(deliveryOrderRecords);
        if (tipTotalAmount.compareTo(BigDecimal.ZERO) > 0) {
            tvFeeTotal.setText(getString(R.string.order_center_detail_tip_total_amount, Utils.formatPrice(tipTotalAmount.doubleValue())));
            tvFeeTotal.setVisibility(View.VISIBLE);
        } else {
            tvFeeTotal.setVisibility(View.GONE);
        }
    }

    private BigDecimal calculateTipTotalAmount(List<DeliveryOrderRecord> deliveryOrderRecords) {
        BigDecimal totalAmount = BigDecimal.ZERO;

        if (Utils.isNotEmpty(deliveryOrderRecords)) {
            for (DeliveryOrderRecord deliveryOrderRecord : deliveryOrderRecords) {
                if (deliveryOrderRecord != null && deliveryOrderRecord.getAmount() != null) {
                    totalAmount = totalAmount.add(deliveryOrderRecord.getAmount());
                }
            }
        }

        return totalAmount;
    }

    private void refreshPassiveAddTipView(DeliveryPlatform deliveryPlatform, DeliveryOrderStatus deliveryOrderStatus,
                                          PartnerDeliveryPlatformConfig partnerDeliveryPlatformConfig
    ) {
        if (deliveryPlatform != null
                && isSupportTip(deliveryPlatform)
                && deliveryOrderStatus != null
                && deliveryOrderStatus == DeliveryOrderStatus.WAITING_ACCEPT
                && (partnerDeliveryPlatformConfig == null || partnerDeliveryPlatformConfig.getIsAutoAddTip() == YesOrNo.NO)) {
            tvPassiveAddTip.setVisibility(View.VISIBLE);
        } else {
            tvPassiveAddTip.setVisibility(View.GONE);
        }
    }

    private boolean isSupportTip(DeliveryPlatform deliveryPlatform) {
        Map<Integer, PartnerShopBiz> partnerShopBizMap = mPresenter.getDeliveryPlatformPartnerShopBizMap();
        if (partnerShopBizMap != null) {
            PartnerShopBiz partnerShopBiz = partnerShopBizMap.get(deliveryPlatform.value());
            if (partnerShopBiz != null) {
                return partnerShopBiz.isSupportTip();
            }
        }

        return false;
    }

    @Override
    public void showDeliveryInfo(TradePaymentVo tradePaymentVo) {
        delivery_info.setVisibility(View.GONE);

    }


    private String getReceiverPhone(TradeVo tradeVo) {
        if (tradeVo != null) {
            TradeExtraSecrecyPhone tradeExtraSecrecyPhone = tradeVo.getTradeExtraSecrecyPhone();
            TradeExtra tradeExtra = tradeVo.getTradeExtra();
            if (tradeExtraSecrecyPhone != null) {
                return getString(R.string.order_center_receiver_secrecy_phone, tradeExtraSecrecyPhone.getVirtualPhone(), tradeExtraSecrecyPhone.getVirtualPhoneExt());
            } else if (tradeExtra != null) {
                return tradeExtra.getReceiverPhone();
            }
        }

        return "";
    }

    private DeliveryOrderVo getDeliveryOrderVo(List<DeliveryOrderVo> deliveryOrderVos) {
        if (Utils.isNotEmpty(deliveryOrderVos)) {
            for (DeliveryOrderVo deliveryOrderVo : deliveryOrderVos) {
                DeliveryOrder deliveryOrder = deliveryOrderVo.getDeliveryOrder();
                if (deliveryOrder.getEnableFlag() == YesOrNo.YES) {
                    return deliveryOrderVo;
                }
            }
        }

        return null;
    }

    private void refreshTradeInvoiceLayout(String invoiceTitle, String taxpayerId) {
        if (!TextUtils.isEmpty(invoiceTitle) || !TextUtils.isEmpty(taxpayerId)) {
            if (!TextUtils.isEmpty(invoiceTitle)) {
                delivery_invoice_title.setText(getString(R.string.order_center_detail_delivery_invoice, invoiceTitle));
                delivery_invoice_title.setVisibility(View.VISIBLE);
            } else {
                delivery_invoice_title.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(taxpayerId)) {
                delivery_invoice_code.setText(getString(R.string.order_center_detail_delivery_invoice_code, taxpayerId));
                delivery_invoice_code.setVisibility(View.VISIBLE);
            } else {
                delivery_invoice_code.setVisibility(View.GONE);
            }

            delivery_invoice_layout.setVisibility(View.VISIBLE);
        } else {
            delivery_invoice_layout.setVisibility(View.GONE);
        }
    }

    private boolean isShowReceiverInfoView() {
        if (delivery_name != null && delivery_name.getVisibility() == View.VISIBLE) {
            return true;
        }

        if (delivery_phone != null && delivery_phone.getVisibility() == View.VISIBLE) {
            return true;
        }

        if (delivery_address_layout != null && delivery_address_layout.getVisibility() == View.VISIBLE) {
            return true;
        }

        if (delivery_invoice_layout != null && delivery_invoice_layout.getVisibility() == View.VISIBLE) {
            return true;
        }

        if (delivery_time != null && delivery_time.getVisibility() == View.VISIBLE) {
            return true;
        }

        return false;
    }

    @Override
    public void showDetailTitle(TradePaymentVo tradePaymentVo) {
        Trade trade = tradePaymentVo.getTradeVo().getTrade();
        TradeExtra tradeExtra = tradePaymentVo.getTradeVo().getTradeExtra();
        trade_no.setText(getString(R.string.order_number_st,
                trade.getTradeNo()));

        detail_delivery_name.setVisibility(View.GONE);
        refreshDeliveryStatus(tradePaymentVo);
        refreshDeliveryPlatform(tradePaymentVo);
        refreshTakeDishStatus(trade, tradeExtra);


    }

    private void refreshTakeDishStatus(Trade trade, TradeExtra tradeExtra) {
        take_dish_status.setVisibility(View.GONE);
        if (trade == null || tradeExtra == null) {
            return;
        }
        if (SourceId.POS != trade.getSource()
                && SourceId.KOU_BEI != trade.getSource() && TradeStatus.UNPROCESSED != trade.getTradeStatus()
                && TradeStatus.REFUSED != trade.getTradeStatus()
                && TradeStatus.CANCELLED != trade.getTradeStatus()
                && DeliveryType.TAKE == trade.getDeliveryType()
                && TradePayStatus.PAID == trade.getTradePayStatus()) {
            if (TakeDishStatus.NOT_TAKE_DISH == tradeExtra.getCallDishStatus()) {
                take_dish_status.setText(getString(R.string.order_center_detail_take_dish_status, getString(R.string.order_center_not_take_dish)));
            } else if (TakeDishStatus.HAVE_TAKE_DISH == tradeExtra.getCallDishStatus()) {
                take_dish_status.setText(getString(R.string.order_center_detail_take_dish_status, getString(R.string.order_center_have_take_dish)));
            }
            take_dish_status.setVisibility(View.VISIBLE);
        }
    }

    private String getTakeDishStatus(TakeDishStatus takeDishStatus) {
        if (TakeDishStatus.HAVE_TAKE_DISH == takeDishStatus) {
            return getString(R.string.order_detail_task_dish_status_already);
        }
        return getString(R.string.order_detail_task_dish_status_waiting);
    }

    private void refreshDeliveryStatus(TradePaymentVo tradePaymentVo) {
        Trade trade = null;

        if (tradePaymentVo.getTradeVo() != null && tradePaymentVo.getTradeVo().getTrade() != null) {
            trade = tradePaymentVo.getTradeVo().getTrade();
        }

        if (trade != null
                && trade.getBusinessType() == BusinessType.TAKEAWAY
                && trade.getDeliveryType() == DeliveryType.SEND
                && (trade.getTradeStatus() == TradeStatus.CONFIRMED || trade.getTradeStatus() == TradeStatus.FINISH)) {
            DeliveryOrderVo deliveryOrderVo = getDeliveryOrderVo(tradePaymentVo.getDeliveryOrderVoList());
            if (deliveryOrderVo != null) {
                DeliveryOrderStatus deliveryOrderStatus = deliveryOrderVo.getDeliveryOrder().getDeliveryStatus();
                if (DeliveryOrderStatus.WAITING_CREATE == deliveryOrderStatus) {
                    tvDeliveryStatus.setText(R.string.order_center_detail_waiting_create);
                    tvDeliveryStatus.setVisibility(View.VISIBLE);
                } else if (DeliveryOrderStatus.WAITING_ACCEPT == deliveryOrderStatus) {
                    tvDeliveryStatus.setText(R.string.order_center_detail_waiting_accept);
                    tvDeliveryStatus.setVisibility(View.VISIBLE);
                } else if (DeliveryOrderStatus.WAITING_PICK_UP == deliveryOrderStatus) {
                    tvDeliveryStatus.setText(R.string.order_center_detail_waiting_pick_up);
                    tvDeliveryStatus.setVisibility(View.VISIBLE);
                } else if (DeliveryOrderStatus.DELIVERYING == deliveryOrderStatus) {
                    tvDeliveryStatus.setText(R.string.order_center_detail_deliverying);
                    tvDeliveryStatus.setVisibility(View.VISIBLE);
                } else if (DeliveryOrderStatus.REAL_DELIVERY == deliveryOrderStatus) {
                    tvDeliveryStatus.setText(R.string.order_center_detail_real_delivery);
                    tvDeliveryStatus.setVisibility(View.VISIBLE);
                } else if (DeliveryOrderStatus.DELIVERY_CANCEL == deliveryOrderStatus) {
                    tvDeliveryStatus.setText(R.string.order_center_detail_delivery_cancel);
                    tvDeliveryStatus.setVisibility(View.VISIBLE);
                } else {
                    tvDeliveryStatus.setVisibility(View.GONE);
                }
            } else {
                TradeExtra tradeExtra = tradePaymentVo.getTradeVo().getTradeExtra();
                if (tradeExtra != null && DeliveryStatus.WAITINT_DELIVERY == tradeExtra.getDeliveryStatus()
                        && (!TextUtils.isEmpty(tradeExtra.getDeliveryUserId()) || DeliveryPlatform.MERCHANT != tradeExtra.getDeliveryPlatform())) {
                    tvDeliveryStatus.setText(R.string.order_center_detail_waiting_pick_up);
                    tvDeliveryStatus.setVisibility(View.VISIBLE);
                } else if (tradeExtra != null && tradeExtra.getDeliveryStatus() == DeliveryStatus.DELIVERYING) {
                    tvDeliveryStatus.setText(R.string.order_center_detail_delivery_status_deliverying);
                    tvDeliveryStatus.setVisibility(View.VISIBLE);
                } else if (tradeExtra != null
                        && (tradeExtra.getDeliveryStatus() == DeliveryStatus.REAL_DELIVERY || tradeExtra.getDeliveryStatus() == DeliveryStatus.SQUARE_UP)) {
                    tvDeliveryStatus.setText(R.string.order_center_detail_delivery_status_real_delivery);
                    tvDeliveryStatus.setVisibility(View.VISIBLE);
                } else {
                    tvDeliveryStatus.setVisibility(View.GONE);
                }
            }
        } else {
            tvDeliveryStatus.setVisibility(View.GONE);
        }
    }

    private void refreshDeliveryPlatform(TradePaymentVo tradePaymentVo) {
        Trade trade = tradePaymentVo.getTradeVo().getTrade();
        if (trade != null
                && trade.getBusinessType() == BusinessType.TAKEAWAY
                && trade.getDeliveryType() == DeliveryType.SEND
                && (trade.getTradeStatus() == TradeStatus.CONFIRMED || trade.getTradeStatus() == TradeStatus.FINISH)) {
            DeliveryPlatform deliveryPlatform = getDeliveryPlatform(tradePaymentVo);
            String deliveryPlatformName = getDeliveryPlatformName(deliveryPlatform);
            detail_delivery_name.setText(deliveryPlatformName);
            detail_delivery_name.setVisibility(TextUtils.isEmpty(deliveryPlatformName) ? View.GONE : View.VISIBLE);
        } else {
            detail_delivery_name.setVisibility(View.GONE);
        }
    }


    private String getDeliveryPlatformName(DeliveryPlatform deliveryPlatform) {
        String deliveryPlatformName = mPresenter.getDeliveryPlatformName(deliveryPlatform);
        if (TextUtils.isEmpty(deliveryPlatformName) && deliveryPlatform != null) {
            if (deliveryPlatform == DeliveryPlatform.BAIDU_TAKEOUT) {
                deliveryPlatformName = getString(R.string.delivery_platform_baidu_takeout);
            } else if (deliveryPlatform == DeliveryPlatform.MERCHANT
                    || deliveryPlatform == DeliveryPlatform.OPEN_PLATFORM) {
                deliveryPlatformName = getString(R.string.delivery_platform_merchant);
            } else if (deliveryPlatform == DeliveryPlatform.ELEME) {
                deliveryPlatformName = getString(R.string.delivery_platform_eleme);
            } else if (deliveryPlatform == DeliveryPlatform.MEITUAN_TAKEOUT) {
                deliveryPlatformName = getString(R.string.delivery_platform_meituan_takeout);
            } else if (deliveryPlatform == DeliveryPlatform.DA_DA) {
                deliveryPlatformName = getString(R.string.delivery_platform_dada);
            } else if (deliveryPlatform == DeliveryPlatform.SHUN_FENG) {
                deliveryPlatformName = getString(R.string.delivery_platform_shunfeng);
            } else if (deliveryPlatform == DeliveryPlatform.MEITUAN_ZHONGBAO) {
                deliveryPlatformName = getString(R.string.delivery_platform_meituan_zhongbao);
            } else if (deliveryPlatform == DeliveryPlatform.ELEME_ZHONGBAO) {
                deliveryPlatformName = getString(R.string.delivery_platform_eleme_zhongbao);
            } else {
                deliveryPlatformName = getString(R.string.delivery_platform_open_platform);
            }
        }

        return deliveryPlatformName;
    }

    @Override
    public void showSpliteTrades(List<TradePaymentVo> reSpliteList) {
        llSplitList.removeAllViews();
        if (Utils.isNotEmpty(reSpliteList)) {
            int count = reSpliteList.size();
            for (int i = count - 1; i >= 0; i--) {
                OrderCenterRePayLayout infoLayout =
                        OrderCenterRePayLayout_.build(getActivity(), reSpliteList.get(i), i + 1, mPresenter, TradeType.SPLIT);
                llSplitList.addView(infoLayout);
            }
        }
    }

    @Override
    public void showHandlerButton() {
        btnRefuse.setVisibility(View.GONE);
        btnAccept.setVisibility(View.GONE);
        btnCall.setVisibility(View.GONE);
        boolean showPayBtn = mPresenter.showPay();
        btnPay.setVisibility(showPayBtn ? View.VISIBLE : View.GONE);
        boolean recisionTrade = mPresenter.showRecision();
        btnRecision.setVisibility(recisionTrade ? View.VISIBLE : View.GONE);
        boolean refundTrade = mPresenter.showRefund();
        btnRefund.setVisibility(refundTrade ? View.VISIBLE : View.GONE);
        btnCreateDoc.setVisibility(mPresenter.showCreateDoc() ? View.VISIBLE : View.GONE);
        btnCreateTask.setVisibility(mPresenter.showCreateTask() ? View.VISIBLE : View.GONE);
        btnPrint.setVisibility(View.GONE);
        btnRetryRefund.setVisibility(View.GONE);
        btnRepay.setVisibility(View.GONE);
        btnContinueRepay.setVisibility(View.GONE);
        showSendOrderButton();
        btnCancelOrder.setVisibility(View.GONE);
        btnAcceptReturn.setVisibility(View.GONE);
        btnRefuseReturn.setVisibility(View.GONE);
        btnDepositRefund.setVisibility(View.GONE);
        btnContinuePay.setVisibility(View.GONE);
        btnInvoice.setVisibility(View.GONE);
        btnInvoiceRevoke.setVisibility(View.GONE);
        btnTakeDish.setVisibility(View.GONE);
        btnRebindDeliveryUser.setVisibility(View.GONE);
        boolean showBtnBar = showPayBtn || recisionTrade || refundTrade;
        vTradeHandle.setVisibility(showBtnBar ? View.VISIBLE : View.GONE);

        ServerHeartbeat.NetworkState networkState = ServerHeartbeat.getInstance().getNetworkState();
        changeHandlerButtonState(networkState);
    }

    private void showSendOrderButton() {
        List<PartnerShopBiz> deliveryPlatformPartnerShopBizs = new ArrayList<PartnerShopBiz>(mPresenter.getDeliveryPlatformPartnerShopBizMap().values());
        if (Utils.isNotEmpty(deliveryPlatformPartnerShopBizs)) {
            if (deliveryPlatformPartnerShopBizs.size() == 1) {
                tvSendOrder.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                tvSendOrder.setText(deliveryPlatformPartnerShopBizs.get(0).getSourceName());
            } else {
                Drawable rightDrawable = getResources().getDrawable(R.drawable.ic_order_center_send_order);
                tvSendOrder.setCompoundDrawablesWithIntrinsicBounds(null, null, rightDrawable, null);
                tvSendOrder.setText(R.string.order_center_send_order);
            }
        }
        flSendOrder.setVisibility(View.GONE);
    }


    private void showRefundDepositWindow() {

    }


    private void showBuffetRefundDepositWindow() {
        DepositInfoDialog.show(getActivity(), mTradeVo);
    }


    private void dismissRefundDepositWindow() {

    }

    @Override
    public void onPause() {
        super.onPause();
        dismissRefundDepositWindow();
        dismissDeliveryPlatformPopupWindow();
    }

    @Override
    public boolean isAdd() {
        return isAdded();
    }

    @Override
    public void showInvoiceRevokeAlert(final String invoiceUuid) {
        CommonDialogFragment.CommonDialogFragmentBuilder cb = new CommonDialogFragment.CommonDialogFragmentBuilder(MainApplication.getInstance());
        cb.iconType(CommonDialogFragment.ICON_WARNING)
                .title(getActivity().getString(R.string.order_center_invoice_revoke_alert))
                .negativeText(R.string.calm_logout_no)
                .positiveText(R.string.calm_logout_yes)
                .positiveLinstner(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        mPresenter.performInvoiceRevoke(invoiceUuid);
                    }
                })
                .build()
                .show(getViewFragmentManager(), "invoice_revoke_alert");
    }

    @Override
    public void showInvoiceQrcodeDialog(TradeVo tradeVo, ElectronicInvoiceVo electronicInvoiceVo, BigDecimal actualAmount) {
        InvoiceFragment.Callback callback = new InvoiceFragment.Callback() {
            @Override
            public void callback(boolean flag, InvoiceQrcodeReq invoiceQrcodeReq, TradeVo tradeVo1) {
                if (flag) {
                    mPresenter.performInvoiceQrcode(invoiceQrcodeReq, tradeVo1);
                }
            }
        };
        InvoiceFragment.show(getViewFragmentManager(), tradeVo, electronicInvoiceVo, actualAmount, callback);
    }

    @Override
    public void showRefundDialog(final Long tradeId, final Long paymentItemId, BigDecimal usefulAmount) {
        final TradeOperates tradeOperates = OperatesFactory.create(TradeOperates.class);
        String title = String.format(getString(R.string.order_center_refund_money), ShopInfoCfg.formatCurrencySymbol(usefulAmount));


        DialogUtil.showWarnConfirmDialog(getSupportFragmentManager(), title,
                R.string.order_center_refund,
                R.string.cancel,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showLoadingProgressDialog();
                        tradeOperates.refundPayment(tradeId, paymentItemId, new SimpleResponseListener<PayResp>() {
                            @Override
                            public void onSuccess(ResponseObject<PayResp> response) {
                                dismissLoadingProgressDialog();
                                EventBus.getDefault().post(new EventSelectOrderRefresh());
                            }

                            @Override
                            public void onFailure(VolleyError error) {
                                super.onFailure(error);
                                dismissLoadingProgressDialog();
                                ToastUtil.showLongToast(error.getMessage());
                            }
                        });
                    }
                }, null, "order_center_refund");
    }

    @Override
    public void showPassiveAddTipDialog(Long deliveryOrderId, DeliveryPlatform deliveryPlatform) {
        AddFeeDialog dialog = AddFeeDialog.newInstance(deliveryOrderId, deliveryPlatform.value());
        dialog.showDialog(getFragmentManager());
    }

    @Override
    public void showSelectSenderDialog(final List<User> authUsers, String authUserId, SelectSenderDialogFragment.OnSelectAuthUserListener listener) {
        SelectSenderDialogFragment selectSenderDialogFragment = SelectSenderDialogFragment.newInstance(authUsers, authUserId);
        selectSenderDialogFragment.setListener(listener);
        selectSenderDialogFragment.show(getActivity().getFragmentManager(), SelectSenderDialogFragment.class.getSimpleName());
    }

    @Override
    public void showDeliveryFeeAlert(final TradePaymentVo tradePaymentVo, final PartnerShopBiz partnerShopBiz, final BigDecimal fee) {
        CommonDialogFragment.CommonDialogFragmentBuilder cb = new CommonDialogFragment.CommonDialogFragmentBuilder(MainApplication.getInstance());
        cb.iconType(CommonDialogFragment.ICON_HINT)
                .title(Html.fromHtml(ResourceUtils.getString(R.string.order_center_delivery_fee_alert, fee)))
                .negativeText(R.string.calm_logout_no)
                .positiveText(R.string.calm_logout_yes)
                .positiveLinstner(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        mPresenter.deliveryOrderDispatch(tradePaymentVo, fee, partnerShopBiz.getSource());
                    }
                })
                .build()
                .show(getViewFragmentManager(), "delivery_fee_alert");
    }

    @Override
    public void showDeliveryCancelTip(String text) {
        tvDeliveryCancelAlert.setText(text);
        llDeliveryCancelAlert.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideDeliveryCancelTip() {
        llDeliveryCancelAlert.setVisibility(View.GONE);
    }

    @Override
    public void showDispatchFailOrderListAlert(List<DispatchFailOrder> dispatchFailOrders) {
        if (Utils.isNotEmpty(dispatchFailOrders)) {
            DispatchFailOrderListFragment fragment = DispatchFailOrderListFragment.newInstance(dispatchFailOrders);
            fragment.show(getViewFragmentManager(), DispatchFailOrderListFragment.class.getSimpleName());
        }
    }


    private boolean isNoOrderReturn(TradeVo tradeVo) {
        if (tradeVo.getTradeExtra() == null || tradeVo.getTrade() == null) {
            return false;
        }

        String serialNumber = tradeVo.getTradeExtra().getSerialNumber();
        Trade trade = tradeVo.getTrade();
        if (TextUtils.isEmpty(serialNumber) &&
                trade.getTradeStatus() == TradeStatus.RETURNED && TextUtils.isEmpty(trade.getRelateTradeUuid())) {
            return true;
        }

        return false;
    }

    public void onEventMainThread(EventServerAvailableStateChange event) {
        if (event != null) {
            changeHandlerButtonState(event.getState());
        }
    }

    private void changeHandlerButtonState(ServerHeartbeat.NetworkState state) {
        if (Snack.isOfflineTrade(mTradeVo) && isFromSnack()) {
            boolean networkAvailable = (state == NetworkAvailable || state == NetworkUnavailable);
            ChangeViewStateUtil.setViewStateByNetworkState(getActivity(), networkAvailable, R.drawable.btn_gray_disabled,
                    btnPay, btnRecision, btnRefund);
            ChangeViewStateUtil.setViewStateByNetworkState(getActivity(), false, R.drawable.btn_gray_disabled, R.string.feature_not_supported,
                    btnDepositRefund, btnInvoice, btnInvoiceRevoke, btnRebindDeliveryUser);
        } else {
            ChangeViewStateUtil.setViewStateByNetworkState(getActivity(), true, R.drawable.btn_gray_disabled,
                    btnPay, btnRecision, btnRefund, btnContinuePay, btnContinueRepay, btnDepositRefund, btnRepay, btnInvoice, btnInvoiceRevoke, btnRetryRefund, btnRebindDeliveryUser);
        }
    }

    private boolean isPosTrade() {
        if (mTradeVo != null) {
            Trade trade = mTradeVo.getTrade();
            return SourceId.POS == trade.getSource();
        }

        return false;
    }


    private int getLeftDisplayForNumberPlate(SourceId sourceId) {
        switch (sourceId) {
            case POS:
                return R.string.dinner_order_center_numberplate;
            default:
                return R.string.order_center_list_take_number;
        }
    }


    private int getLeftDisplayForTable(SourceId sourceId) {
        switch (sourceId) {
            case POS:
                return R.string.dinner_order_center_tables;
            default:
                return R.string.order_center_list_take_number;
        }
    }
}
