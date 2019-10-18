package com.zhongmei.bty.pay.fragment;

import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhongmei.bty.basemodule.auth.application.BeautyApplication;
import com.zhongmei.bty.basemodule.auth.application.DinnerApplication;
import com.zhongmei.bty.basemodule.auth.application.FastFoodApplication;
import com.zhongmei.bty.basemodule.auth.permission.manager.AuthLogManager;
import com.zhongmei.bty.basemodule.commonbusiness.cache.PaySettingCache;
import com.zhongmei.bty.basemodule.commonbusiness.cache.ServerSettingCache;
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;
import com.zhongmei.bty.basemodule.pay.enums.PayScene;
import com.zhongmei.bty.basemodule.pay.event.PushPayRespEvent;
import com.zhongmei.bty.basemodule.pay.event.RegisterDeWoCustomerLoginScanEvent;
import com.zhongmei.bty.basemodule.pay.event.RegisterDeWoOnlinePayScanEvent;
import com.zhongmei.bty.basemodule.pay.message.PayResp;
import com.zhongmei.bty.basemodule.print.entity.PrintOperation;
import com.zhongmei.bty.basemodule.session.core.user.UserFunc;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.shoppingcart.SeparateShoppingCart;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.event.ActionCloseOrderDishActivity;
import com.zhongmei.bty.basemodule.trade.manager.DinnerCashManager;
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.bty.cashier.shoppingcart.ShoppingCart;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.database.entity.TradeEarnestMoney;
import com.zhongmei.bty.commonmodule.database.enums.OrderActionEnum;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.commonmodule.view.AutofitTextView;
import com.zhongmei.bty.commonmodule.view.NumberInputdialog;
import com.zhongmei.bty.mobilepay.IDoLag;
import com.zhongmei.bty.mobilepay.IFinishPayCallback;
import com.zhongmei.bty.mobilepay.IOnlinePayOverCallback;
import com.zhongmei.bty.mobilepay.IPayConstParame;
import com.zhongmei.bty.mobilepay.IPaymentMenuType;
import com.zhongmei.bty.mobilepay.IUpdateUICallback;
import com.zhongmei.bty.mobilepay.adapter.PayMethodIndicatorAdapter;
import com.zhongmei.bty.mobilepay.bean.PayMethodItem;
import com.zhongmei.bty.mobilepay.bean.PaymentMenuTool;
import com.zhongmei.bty.mobilepay.core.DoPayApi;
import com.zhongmei.bty.mobilepay.enums.PayActionPage;
import com.zhongmei.bty.mobilepay.event.AmountEditedEvent;
import com.zhongmei.bty.mobilepay.event.DepositPayOver;
import com.zhongmei.bty.mobilepay.event.ExemptEvent;
import com.zhongmei.bty.mobilepay.event.ExemptEventUpdate;
import com.zhongmei.bty.mobilepay.event.MemberPayChargeEvent;
import com.zhongmei.bty.mobilepay.event.RefreshTradeVoEvent;
import com.zhongmei.bty.mobilepay.event.SellForRepeatEvent;
import com.zhongmei.bty.mobilepay.event.StopPayStatusTimer;
import com.zhongmei.bty.mobilepay.fragment.CashPayFragment;
import com.zhongmei.bty.mobilepay.fragment.MeiTuanCouponFragment;
import com.zhongmei.bty.mobilepay.fragment.OthersFragment;
import com.zhongmei.bty.mobilepay.fragment.UnionFragment;
import com.zhongmei.bty.mobilepay.fragment.lag.LagMainFragment;
import com.zhongmei.bty.mobilepay.fragment.onlinepay.OnlinePayFragment;
import com.zhongmei.bty.mobilepay.manager.CashInfoManager;
import com.zhongmei.bty.mobilepay.manager.PaymentInfoManager;
import com.zhongmei.bty.mobilepay.popup.ErasePopupHelper;
import com.zhongmei.bty.mobilepay.popup.InvoicePopupHelper;
import com.zhongmei.bty.mobilepay.utils.DoPayUtils;
import com.zhongmei.bty.mobilepay.utils.PayDisplayUtil;
import com.zhongmei.bty.mobilepay.views.indicator.Indicator;
import com.zhongmei.bty.mobilepay.views.indicator.ViewPagerIndicator;
import com.zhongmei.bty.pay.fragment.memberpay.MemberMainFragment;
import com.zhongmei.bty.pay.fragment.memberpay.MemberMainFragment_;
import com.zhongmei.bty.pay.manager.DoPayManager;
import com.zhongmei.bty.pay.utils.PayUtils;
import com.zhongmei.bty.snack.orderdish.data.SDConstant.MobClick;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.DeliveryType;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.db.enums.PayModelGroup;
import com.zhongmei.yunfu.db.enums.PaymentType;
import com.zhongmei.yunfu.db.enums.SourceChild;
import com.zhongmei.yunfu.db.enums.SourceId;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.yunfu.ui.base.MobclickAgentFragment;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.util.UserActionCode;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.greenrobot.event.EventBus;


@EFragment(R.layout.pay_main_container_fragment)
public class MainPayFragment extends MobclickAgentFragment implements IPaymentMenuType, IUpdateUICallback, IDoLag, IFinishPayCallback {
    private final String TAG = "MainPayFragment";

    @ViewById(R.id.tv_total_fee)
    protected AutofitTextView tv_totalFee;
    @ViewById(R.id.pay_main_container_fragment_invoice)
    protected CheckBox invoiceCbox;
    @ViewById(R.id.pay_main_container_fragment_amount_unreceived)
    protected TextView unReceivedTxv;
    @ViewById(R.id.pay_main_container_fragment_btn_erase)
    protected Button mEraseBT;
    @ViewById(R.id.pay_main_container_fragment_amount_actual)
    protected TextView tradeAmountTV;
    @ViewById(R.id.pay_main_container_fragment_amount)
    protected TextView tradeAmountTVTotal;
    @ViewById(R.id.pay_main_container_fragment_tag)
    protected LinearLayout tagLlyt;
    @ViewById(R.id.pay_main_container_fragment_amount_erase)
    protected TextView eraseTxv;
    @ViewById(R.id.pay_main_container_fragment_amount_received)
    protected TextView receivedTxv;
    @ViewById(R.id.pay_main_container_fragment_indicator)
    protected ViewPagerIndicator payMethodIndicator;
    @ViewById(R.id.pager)
    protected FrameLayout mPager;
    @ViewById(R.id.iv_close)
    protected View mCloseBT;
    @ViewById(R.id.pay_main_container_fragment_paid_list_icon)
    protected ImageView mPaidListIcon;
    @ViewById(R.id.pay_main_container_fragment_btn_pay_deposit)
    protected Button mPayDepositBT;
    transient private PayMethodIndicatorAdapter mPayMethodAdapter;

    private List<PayMethodItem> methodList;



    private CashPayFragment mCashPayFragment;

    private UnionFragment mUnionPayFragment;

    private OnlinePayFragment mAliPayFragment, mWxPayFragment;

    private MemberMainFragment mMemberMainFragment;

    private OthersFragment mOtherPayFragment;

    private LagMainFragment mLagMainFragment;

    private MeiTuanCouponFragment mCouponPayFragment;
    private PayMethodItem mCurrentMethod;

    private TradeVo mTradeVo;

    private TradeVo mSourceTradeVo;

    private boolean isOrderCenter;

    private boolean isSplit;

    private boolean isOrdered;
    private PaymentInfoManager mPaymentInfo;

    private DoPayApi mDoPayApi;

    private BigDecimal mCurrentTradeAmount;

        private PayScene mPayScene = PayScene.SCENE_CODE_SHOP;
    private int defautlPaymentMenuType = -1;

        private int quickPayType = -1;

        private int paymentMenuType;

        private boolean isSupportYIPay;

    boolean isFromOpenPlatForm = false;    boolean isSupportOnline = true;    boolean isSupportMobilePay = true;    private boolean isGroupPay = true;    private User mExemptUser;
    public void setDoPayApi(DoPayApi doPayApi) {        this.mDoPayApi = doPayApi;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPaymentInfo = PaymentInfoManager.getNewInstance(this);        if (mDoPayApi == null)
            mDoPayApi = DoPayManager.getInstance();        mDoPayApi.setOnlinePaymentItemUuid(null);        mDoPayApi.setCurrentPaymentInfoId(mPaymentInfo.getId());        if (getArguments() != null) {
            mTradeVo = (TradeVo) getArguments().getSerializable("tradeVo");
            mSourceTradeVo = (TradeVo) getArguments().getSerializable("source");
            PayScene PayScene = (PayScene) getArguments().getSerializable("payScene");
            PayActionPage payActionPage = (PayActionPage) getArguments().getSerializable(IPayConstParame.EXTRA_PAY_ACTION_PAGE);
            if (PayScene != null) {
                mPayScene = PayScene;
            }
            if (payActionPage != null) {
                mPaymentInfo.setPayActionPage(payActionPage);            }
            defautlPaymentMenuType = getArguments().getInt("paymenutype", -1);
            quickPayType = getArguments().getInt(IPayConstParame.EXTRA_QUICK_PAY_TYPE, -1);
            isOrderCenter = getArguments().getBoolean("isOrderCenter");
            isOrdered = getArguments().getBoolean("isOrdered", false);
            isSplit = getArguments().getBoolean("isSplit");
            isGroupPay = getArguments().getBoolean("isGroupPay", true);
            isFromOpenPlatForm = getArguments().getBoolean("isFromOpenPlatform", false);
            isSupportOnline = getArguments().getBoolean("isSupportOnline", true);
            isSupportMobilePay = getArguments().getBoolean("isSupportMobilePay", true);
            isSupportYIPay = getArguments().getBoolean("isSupportYIPay", false);
                        paymentMenuType = getArguments().getInt(IPayConstParame.EXTRA_MENU_DISPLAY_TYPE, 0);
            if (mTradeVo != null && mTradeVo.getTrade() != null) {
                mCurrentTradeAmount = mTradeVo.getTrade().getTradeAmount();
                if (mTradeVo.getTrade().getTradePayStatus() == TradePayStatus.PAYING) {
                    isOrdered = true;
                }
            }
        }
        mPaymentInfo.setFinishPayCallback(this);                mPaymentInfo.setDefaultPaymentMenuType(defautlPaymentMenuType);
                mPaymentInfo.setQuickPayType(quickPayType);
        mPaymentInfo.setTradeVo(mTradeVo);        mPaymentInfo.setSplit(isSplit);        mPaymentInfo.setSourceTradeVo(mSourceTradeVo);        mPaymentInfo.setOrderCenter(isOrderCenter);        mPaymentInfo.setOrdered(isOrdered);        mPaymentInfo.setPayScene(mPayScene);        mPaymentInfo.setIsGroupPay(isGroupPay);        if (isFromOpenPlatForm) {            mPaymentInfo.setPrintedOk(true);         }
    }


    @AfterViews
    void init() {
        registerEventBus();

        if (getActivity().getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
                        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }

                        if ((mPaymentInfo.isDinner() && !mPaymentInfo.isOrderCenter()) && mPaymentInfo.getPayScene() == PayScene.SCENE_CODE_SHOP) {
            mCloseBT.setVisibility(View.INVISIBLE);
        }
                if (PaySettingCache.isElectronicInvoiceOpen() || ShopInfoCfg.getInstance().isMonitorCode(ShopInfoCfg.ShopMonitorCode.MONITOR_CODE_FHXM)) {
            invoiceCbox.setVisibility(View.VISIBLE);
                        if (ShopInfoCfg.getInstance().isMonitorCode(ShopInfoCfg.ShopMonitorCode.MONITOR_CODE_FHXM)) {
                invoiceCbox.setChecked(true);
                invoiceCbox.setEnabled(false);
                mPaymentInfo.setOpenElectronicInvoice(true);
            }
        } else {
            invoiceCbox.setVisibility(View.INVISIBLE);
        }
                BusinessType businessType = mTradeVo.getTrade().getBusinessType();
        if (BusinessType.BOOKING_LIST.equals(businessType) || BusinessType.TAILING_LIST.equals(businessType) || mPaymentInfo.getPayScene() == PayScene.SCENE_CODE_WRITEOFF || mPaymentInfo.getPayScene() == PayScene.SCENE_CODE_BOOKING_DEPOSIT) {
            invoiceCbox.setVisibility(View.INVISIBLE);
        }
                        if (mTradeVo.getTrade() != null && (mTradeVo.getTrade().getId() != null || isOrdered)) {

            new AsyncTask<Void, Void, List<PaymentVo>>() {

                @Override
                protected List<PaymentVo> doInBackground(Void... params) {
                    TradeDal tradeDal = OperatesFactory.create(TradeDal.class);
                    List<PaymentVo> list = null;
                    try {
                        list = tradeDal.listPayment(mTradeVo.getTrade().getUuid(), PaymentType.TRADE_SELL);
                    } catch (Exception e) {
                        Log.e(TAG, "", e);
                    }
                    return list;
                }

                protected void onPostExecute(List<PaymentVo> data) {
                    try {                        mPaymentInfo.setPaidPaymentRecords(data);
                                                loadViews();
                    } catch (Exception e) {
                        Log.e(TAG, "", e);
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } else {
                        loadViews();
        }
    }

    private void loadViews() {
                        initMenus();
                if (mTradeVo.getTrade().getId() == null || mPaymentInfo.getPayScene() == PayScene.SCENE_CODE_BUFFET_DEPOSIT) {
            notifyDataSetChange();
        }
    }

        @Override
    protected void onInit() {
        if (ServerSettingCache.getInstance().isJinChBusiness()) {
            return;
        }
        PayDisplayUtil.doUpdateScreen(this.getActivity().getApplicationContext(), mCurrentMethod, mPaymentInfo);
    }

    private void initMenus() {
                PaymentMenuTool menuTool = new PaymentMenuTool(this.getActivity(), mPaymentInfo.getPayScene(), mPaymentInfo.getTradeBusinessType());
        menuTool.setDefaultPaymentMenuType(mPaymentInfo.getDefaultPaymentMenuType());        menuTool.setSupportLag(PayUtils.isSupportLagPay(mPaymentInfo, !this.isFromOpenPlatForm));        menuTool.setSupportOnline(isSupportOnline);
        menuTool.setSuportMobilePay(isSupportMobilePay);
        menuTool.setSupportYiPay(isSupportYIPay);        menuTool.setColumns(getResources().getInteger(R.integer.zm_pay_menu_count));
                switch (paymentMenuType) {
            case IPayConstParame.MENU_DISPLAY_TYPE_MEMBER_ENABLE:                 menuTool.setThirdCoustomer(true);
                menuTool.setMemberPay(true);
                break;
            case IPayConstParame.MENU_DISPLAY_TYPE_MEMBER_DISABLE:                 menuTool.setThirdCoustomer(true);
                menuTool.setMemberPay(false);
                break;
            case IPayConstParame.MENU_DISPLAY_TYPE_NOMAL:             default:
                break;
        }
        methodList = menuTool.initMethodList();
        mPayMethodAdapter = new PayMethodIndicatorAdapter(this.getActivity());
        mPayMethodAdapter.addData(methodList);
        payMethodIndicator.setAdapter(mPayMethodAdapter);
                payMethodIndicator.setOnItemClickListener(new Indicator.OnItemClickListener() {
            @Override
            public void onItemClick(Indicator indicator, Indicator.Adapter adapter, int position) {
                if (!ClickManager.getInstance().isClicked()) {
                    PayMethodItem item = (PayMethodItem) adapter.getItem(position);
                    if (mCurrentMethod != item) {
                                                if (mPaymentInfo.getTradeBusinessType() != BusinessType.BUFFET) {                                                        if (mCurrentMethod != null && mCurrentMethod.methodId == IPaymentMenuType.PAY_MENU_TYPE_MEMBER) {
                                                                if (isNeedToDealMemberPrice(mPaymentInfo.getTradeVo())) {
                                                                        showRemoveMemberPriceDialog(item);
                                    return;
                                }
                            } else {
                                                                if (isRemovedMemberPrice && item.methodId == IPaymentMenuType.PAY_MENU_TYPE_MEMBER && mPaymentInfo.getPaidAmount() <= 0) {
                                    showPayMethodFragment(item, false, false);
                                    if (mPaymentInfo.isSplit()) {
                                        SeparateShoppingCart.getInstance().memberPrivilege(true, true);
                                    } else {
                                        DinnerShoppingCart.getInstance().memberPrivilege(true, true);
                                    }
                                    isRemovedMemberPrice = false;
                                    return;
                                }
                            }
                        }
                                                showPayMethodFragment(item, false, true);
                    }
                }
            }
        });
                setDefaultPayMethod(menuTool.getDefaultPayMethod());
    }


    private void setDefaultPayMethod(PayMethodItem defaultPayMethodItem) {
                if (defaultPayMethodItem != null) {
            showPayMethodFragment(defaultPayMethodItem, true, true);
            mCurrentMethod = defaultPayMethodItem;
        }
    }

    private void showPayMethodFragment(PayMethodItem payMethodItem, boolean isDefault, boolean isSuportGroupPay) {
                if (payMethodItem != null) {
                        if (mPaymentInfo.enableExempt()) {
                this.mEraseBT.setVisibility(View.VISIBLE);
                if (ServerSettingCache.getInstance().isJinChBusiness()
                        && mPayScene == PayScene.SCENE_CODE_CHARGE) {
                    this.mEraseBT.setVisibility(View.INVISIBLE);
                }
            } else {
                this.mEraseBT.setVisibility(View.INVISIBLE);
            }
            switch (payMethodItem.methodId) {
                case PAY_MENU_TYPE_CASH:                    updateLastFragment(mCurrentMethod);
                    payMethodItem.isSelected = true;
                    mCurrentMethod = payMethodItem;
                    if (mCashPayFragment != null) {
                        this.showFragment(mCashPayFragment, false);
                    } else {
                        mCashPayFragment = CashPayFragment.newInstance(mPaymentInfo, mDoPayApi);                        this.addFragment(R.id.pager, mCashPayFragment, "PayCashPayFragment");
                    }
                    break;
                case PAY_MENU_TYPE_ALIPAY:                    if (!checkEnable(isDefault)) {
                        return;
                    }

                    EventBus.getDefault().post(new RegisterDeWoOnlinePayScanEvent());
                                        updateLastFragment(mCurrentMethod);
                    payMethodItem.isSelected = true;
                    mCurrentMethod = payMethodItem;
                    if (mAliPayFragment != null) {
                        this.showFragment(mAliPayFragment, false);
                    } else {
                        mAliPayFragment = OnlinePayFragment.newInstance(PAY_MENU_TYPE_ALIPAY, mPaymentInfo, mDoPayApi);
                        if (ServerSettingCache.getInstance().isJinChBusiness()) {                             mAliPayFragment.setDefaultShowDialog(true);
                        } else {
                            if (isDefault && defautlPaymentMenuType == -1) {
                                mAliPayFragment.setDefaultShowDialog(false);
                            }
                        }
                        this.addFragment(R.id.pager, mAliPayFragment, "PayOnlinePayFragment");
                    }
                    break;

                case PAY_MENU_TYPE_WEIXIN:                    if (!checkEnable(isDefault)) {
                        return;
                    }

                    EventBus.getDefault().post(new RegisterDeWoOnlinePayScanEvent());
                                        updateLastFragment(mCurrentMethod);
                    payMethodItem.isSelected = true;
                    mCurrentMethod = payMethodItem;
                    if (mWxPayFragment != null) {
                        this.showFragment(mWxPayFragment, false);
                    } else {
                        mWxPayFragment = OnlinePayFragment.newInstance(PAY_MENU_TYPE_WEIXIN, mPaymentInfo, mDoPayApi);
                        if (ServerSettingCache.getInstance().isJinChBusiness()) {                             mWxPayFragment.setDefaultShowDialog(true);
                        } else {
                            if (isDefault && defautlPaymentMenuType == -1) {
                                mWxPayFragment.setDefaultShowDialog(false);
                            }
                        }
                        this.addFragment(R.id.pager, mWxPayFragment, "PayOnlinePayFragment");
                    }
                    break;
                case PAY_MENU_TYPE_UNION:                    if (!checkEnable(isDefault)) {
                        return;
                    }
                    updateLastFragment(mCurrentMethod);
                    payMethodItem.isSelected = true;
                    mCurrentMethod = payMethodItem;
                    if (mUnionPayFragment != null) {
                        this.showFragment(mUnionPayFragment, false);
                    } else {
                        mUnionPayFragment = UnionFragment.newInstance(mPaymentInfo, mDoPayApi);
                        this.addFragment(R.id.pager, mUnionPayFragment, "PayUnionPayFragment");
                    }
                    break;
                case PAY_MENU_TYPE_MEMBER:                    if (!checkEnable(isDefault)) {
                        return;
                    }

                    EventBus.getDefault().post(new RegisterDeWoCustomerLoginScanEvent());
                                        updateLastFragment(mCurrentMethod);
                    payMethodItem.isSelected = true;
                    mCurrentMethod = payMethodItem;
                                        MobclickAgentEvent.onEvent(getActivity(), MobClick.SNACK_PAY_CHARGE);
                    if (mMemberMainFragment != null) {
                        mMemberMainFragment.setSuportGroupPay(isSuportGroupPay);                         this.showFragment(mMemberMainFragment, false);
                    } else {
                        mMemberMainFragment = new MemberMainFragment_();
                        mMemberMainFragment.setSuportGroupPay(isSuportGroupPay);                         mMemberMainFragment.setPaymentInfo(mPaymentInfo);
                        mMemberMainFragment.setDoPayApi(mDoPayApi);                        this.addFragment(R.id.pager, mMemberMainFragment, "PayCustomerMainFragment");
                    }
                    break;

                case PAY_MENU_TYPE_OTHERS:                    if (!checkEnable(isDefault)) {
                        return;
                    }
                    updateLastFragment(mCurrentMethod);
                    payMethodItem.isSelected = true;
                    mCurrentMethod = payMethodItem;
                    if (mOtherPayFragment != null) {
                        this.showFragment(mOtherPayFragment, false);
                    } else {
                        mOtherPayFragment = OthersFragment.newInstance(mPaymentInfo, mDoPayApi);                        this.addFragment(R.id.pager, mOtherPayFragment, "PayOtherPayFragment");
                    }
                    break;
                case PAY_MENU_TYPE_LAGPAY:                    if (!checkEnable(isDefault)) {
                        return;
                    }
                    if (this.mPaymentInfo.getPaidAmount() > 0 && !isDefault) {
                        ToastUtil.showShortToast(R.string.pay_lagpay_cannot_use);
                        return;
                    }
                                        if (mPaymentInfo.getExemptAmount() > 0) {
                        EventBus.getDefault().post(new ExemptEvent(ErasePopupHelper.REMOVE_NOTHING));
                    }
                    this.mEraseBT.setVisibility(View.INVISIBLE);
                    updateLastFragment(mCurrentMethod);
                    payMethodItem.isSelected = true;
                    mCurrentMethod = payMethodItem;
                    if (mLagMainFragment != null) {
                        this.showFragment(mLagMainFragment, false);
                    } else {
                                                mLagMainFragment = LagMainFragment.newInstance(mPaymentInfo, mDoPayApi);
                        mLagMainFragment.setIDoLag(this);
                        this.addFragment(R.id.pager, mLagMainFragment, "PaymLagMainFragment");
                    }
                    break;
                default:
                    break;
            }
            mPayMethodAdapter.notifyDataSetChanged();
        }
    }

    private boolean checkEnable(boolean isDefault) {
        if (this.mPaymentInfo.getActualAmount() == 0 && !isDefault) {
            ToastUtil.showShortToast(R.string.pay_zero_cannot_use);
            return false;
        }
        if (this.mPaymentInfo.getActualAmount() < 0 && !isDefault) {
            ToastUtil.showShortToast(R.string.pay_negative_not_pay);
            return false;
        }
        return true;
    }


    private void updateLastFragment(PayMethodItem lastMethodItem) {
        if (lastMethodItem != null) {
            lastMethodItem.isSelected = false;
            lastMethodItem.isFocused = false;
            switch (lastMethodItem.methodId) {
                case PAY_MENU_TYPE_CASH:                    if (mCashPayFragment != null) {
                        this.hideFragment(mCashPayFragment);
                    }
                    break;
                case PAY_MENU_TYPE_ALIPAY:                    if (mAliPayFragment != null) {
                        this.hideFragment(mAliPayFragment);
                    }
                    break;

                case PAY_MENU_TYPE_WEIXIN:                    if (mWxPayFragment != null) {
                        this.hideFragment(mWxPayFragment);
                    }
                    break;
                case PAY_MENU_TYPE_UNION:                    if (mUnionPayFragment != null) {
                        this.hideFragment(mUnionPayFragment);
                    }
                    break;
                case PAY_MENU_TYPE_MEITUANCOUPON:
                    if (mCouponPayFragment != null) {
                        this.hideFragment(mCouponPayFragment);
                    }
                    break;
                case PAY_MENU_TYPE_MEMBER:                    if (ServerSettingCache.getInstance().isJinChBusiness()) {

                    } else {
                        if (mMemberMainFragment != null) {
                            this.hideFragment(mMemberMainFragment);
                        }
                    }
                    break;

                case PAY_MENU_TYPE_OTHERS:                    if (mOtherPayFragment != null) {
                        this.hideFragment(mOtherPayFragment);
                    }
                    break;
                case PAY_MENU_TYPE_LAGPAY:                    if (mLagMainFragment != null) {
                        this.hideFragment(mLagMainFragment);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Click({R.id.pay_main_container_fragment_btn_erase, R.id.pay_main_container_fragment_tag, R.id.pay_main_container_fragment_invoice, R.id.iv_close, R.id.pay_main_container_fragment_btn_pay_deposit})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay_main_container_fragment_btn_erase:
                MobclickAgentEvent.onEvent(UserActionCode.ZC030010);
                                MobclickAgentEvent.onEvent(getActivity(), MobClick.SNACK_PAY_WIPE_ZERO);
                String permissionCode;
                if (mPaymentInfo.isDinner()) {
                    permissionCode = DinnerApplication.PERMISSION_DINNER_MALING;
                } else if (mPaymentInfo.isBeauty()) {
                    permissionCode = BeautyApplication.PERMISSION_BEAUTY_MALING;
                } else {
                    permissionCode = FastFoodApplication.PERMISSION_FASTFOOD_MALING;
                }
                VerifyHelper.verifyAlert(getActivity(), permissionCode,
                        new VerifyHelper.Callback() {
                            @Override
                            public void onPositive(User user, String code, Auth.Filter filter) {
                                mExemptUser = user;                                doExempt();
                            }
                        });
                break;
            case R.id.pay_main_container_fragment_tag:
                openPaymentItemList(mPaymentInfo.getPaidPaymentItems());
                break;
            case R.id.pay_main_container_fragment_invoice:                MobclickAgentEvent.onEvent(UserActionCode.ZC030011);
                mPaymentInfo.setOpenElectronicInvoice(invoiceCbox.isChecked());
                break;
            case R.id.iv_close:
                CustomerManager.getInstance().setLoginSource(0);
                getActivity().finish();
                break;
            case R.id.pay_main_container_fragment_btn_pay_deposit:
                DoPayManager.gotoPayActivity(getActivity(), mPaymentInfo.getTradeVo(), mPaymentInfo.isOrderCenter(), PayScene.SCENE_CODE_BUFFET_DEPOSIT);
                break;
            default:
                break;
        }
    }

    private void doExempt() {
        if (!ClickManager.getInstance().isClicked()) {
            ErasePopupHelper.getInstance().initPopwindow(getActivity(), mEraseBT, mPaymentInfo);
        }
    }

        public void updatePayUI(final TradeVo source, final TradeVo tradeVo, boolean isSplit) {
                if (isSplit == false && mPaymentInfo.isSplit()) {
            mPaymentInfo.clearPaymentUUids();
            DoPayUtils.findSourceTradePaymentVos(mPaymentInfo);        }
        mPaymentInfo.setSourceTradeVo(source);
        mPaymentInfo.setSplit(isSplit);
        mPaymentInfo.setPrintedOk(false);
        if (tradeVo.getTrade().getBusinessType() == BusinessType.BUFFET || tradeVo.getTrade().getTradePayStatus() != TradePayStatus.PAYING && mPaymentInfo.getPaidAmount() <= 0) {
            mPaymentInfo.setOrdered(false);
        }
                if (isSplit && tradeVo.getTrade().getTradePayStatus() != TradePayStatus.PAYING && mPaymentInfo.getPaidAmount() <= 0) {
            mPaymentInfo.setPaidPaymentRecords(null);
            mPaymentInfo.clearPaymentUUids();
        }
        if (this.isFromOpenPlatForm) {            mPaymentInfo.setPrintedOk(true);             tradeVo.setTradeExtra(null);
        }
        updateTradeVo(tradeVo);
        checkMemberPrice(tradeVo);
    }

    private void updateTradeVo(TradeVo tradeVo) {
        boolean isClearExempt = true;
        double exemptAmount = mPaymentInfo.getExemptAmount();        boolean isSameTradeAmount = tradeVo.getTrade().getTradeAmount().equals(mCurrentTradeAmount);        mPaymentInfo.setTradeVo(tradeVo);
                if (isSameTradeAmount || (exemptAmount <= 0 && mPaymentInfo.getEraseType() == ErasePopupHelper.REMOVE_NOTHING)) {
            isClearExempt = false;
        }
                if (isClearExempt) {
            mPaymentInfo.setEraseType(ErasePopupHelper.REMOVE_NOTHING);
            mPaymentInfo.setExemptAmount(0);
            EventBus.getDefault().post(new ExemptEventUpdate(ErasePopupHelper.REMOVE_NOTHING));
            setExemptBTEraseType(mEraseBT, ErasePopupHelper.REMOVE_NOTHING);        } else {             if (!isSameTradeAmount) {
                EventBus.getDefault().post(new ExemptEventUpdate(mPaymentInfo.getEraseType()));
            }
        }
                if (!isSameTradeAmount) {
            this.mCurrentTradeAmount = tradeVo.getTrade().getTradeAmount();
            this.notifyDataSetChange();
            PayDisplayUtil.doUpdateScreen(this.getActivity().getApplicationContext(), mCurrentMethod, mPaymentInfo);
        }
    }

        transient NumberInputdialog.InputOverListener mInputOverListener = new NumberInputdialog.InputOverListener() {
        @Override
        public void afterInputOver(String inputContent) {
            if (inputContent != null) {
                Double exmpent = Double.valueOf(inputContent);
                if (exmpent != null) {
                    int exemptType = ErasePopupHelper.REMOVE_AUTO;
                    if (exmpent <= 0) {
                        exemptType = ErasePopupHelper.REMOVE_NOTHING;
                    }
                                        setExemptResult(exmpent, exemptType);
                }
            }
        }
    };

        private void showNumberInputDialog() {
                double maxValue = CashInfoManager.floatSubtract(mPaymentInfo.getTradeAmount(), mPaymentInfo.getPaidAmount());
        String defaultInput = null;
        if (mPaymentInfo.getExemptAmount() > 0) {
            defaultInput = MathDecimal.trimZero(BigDecimal.valueOf(mPaymentInfo.getExemptAmount())) + "";
        }
        NumberInputdialog numberDialog =
                new NumberInputdialog(this.getActivity(), getString(R.string.pay_auto_exempty_title), getString(R.string.pay_auto_exempty_edit_hint), defaultInput, maxValue, mInputOverListener);
        numberDialog.show();
    }


    private void updateExempt() {
                if (mPaymentInfo.enableExempt()) {
                        if (mCurrentMethod != null && mCurrentMethod.methodId != PAY_MENU_TYPE_LAGPAY) {
                this.mEraseBT.setVisibility(View.VISIBLE);
                if (ServerSettingCache.getInstance().isJinChBusiness()
                        && mPayScene == PayScene.SCENE_CODE_CHARGE) {
                    this.mEraseBT.setVisibility(View.INVISIBLE);
                }
            }
        } else {
            this.mEraseBT.setVisibility(View.INVISIBLE);
        }

        double exemptAmount = mPaymentInfo.getExemptAmount();
        if (exemptAmount > 0) {
            eraseTxv.setText(getString(R.string.erase_small_change) + "：-" + CashInfoManager.formatCash(exemptAmount));
        } else {
            eraseTxv.setText("");
        }
                if (mPaymentInfo.enableExempt() && exemptAmount > 0) {
            double receivableAmount = mPaymentInfo.getReceivableAmount();
            tv_totalFee.setText(CashInfoManager.formatCash(receivableAmount));
            tv_totalFee.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tv_totalFee.setVisibility(View.VISIBLE);

        } else {
            tv_totalFee.setVisibility(View.GONE);
        }
    }


    public void onEventMainThread(ExemptEvent event) {
        String afterEraseOrderCash = null;
        String receivableAmount = CashInfoManager.formatCashAmount(this.mPaymentInfo.getReceivableAmount());
        double eraseAmount = 0;
        switch (event.getEraseType()) {

            case ErasePopupHelper.REMOVE_CENT:                                MobclickAgentEvent.onEvent(getActivity(), MobClick.SNACK_PAY_WIPE_ZERO_FEN);
                afterEraseOrderCash = receivableAmount.substring(0, receivableAmount.length() - 1);
                eraseAmount = CashInfoManager.floatSubtract(Double.valueOf(receivableAmount), Double.valueOf(afterEraseOrderCash));

                break;

            case ErasePopupHelper.REMOVE_DIME:                                MobclickAgentEvent.onEvent(getActivity(), MobClick.SNACK_PAY_WIPE_ZERO_JIAO);
                afterEraseOrderCash = receivableAmount.substring(0, receivableAmount.length() - 3);
                eraseAmount = CashInfoManager.floatSubtract(Double.valueOf(receivableAmount), Double.valueOf(afterEraseOrderCash));

                break;

            case ErasePopupHelper.REMOVE_YUAN:                                MobclickAgentEvent.onEvent(getActivity(), MobClick.SNACK_PAY_WIPE_ZERO_YUAN);
                afterEraseOrderCash = receivableAmount.substring(0, receivableAmount.length() - 4) + "0";
                eraseAmount = CashInfoManager.floatSubtract(Double.valueOf(receivableAmount), Double.valueOf(afterEraseOrderCash));
                break;
            case ErasePopupHelper.REMOVE_AUTO:
                                MobclickAgentEvent.onEvent(getActivity(), MobClick.SNACK_PAY_WIPE_ZERO_CUSTOMER);
                String permissionCode;
                if (mPaymentInfo.isDinner()) {
                    permissionCode = DinnerApplication.PERMISSION_DINNER_AUTO_MALING;
                } else if (mPaymentInfo.isBeauty()) {
                    permissionCode = BeautyApplication.PERMISSION_BEAUTY_AUTO_MALING;
                } else {
                    permissionCode = FastFoodApplication.PERMISSION_FASTFOOD_AUTO_MALING;
                }
                VerifyHelper.verifyAlert(getActivity(), permissionCode,
                        new VerifyHelper.Callback() {
                            @Override
                            public void onPositive(User user, String code, Auth.Filter filter) {
                                super.onPositive(user, code, filter);
                                showNumberInputDialog();
                            }
                        });
                return;
            case ErasePopupHelper.REMOVE_NOTHING:                eraseAmount = 0;
                break;
            default:
                break;

        }
        setExemptResult(eraseAmount, event.getEraseType());
            }

            private static boolean isExemptUserHasPermission(User user, String permissionCode) {
                if (user != null && user.equals(Session.getAuthUser())) {
            return Session.getAuthUser().getAuth().hasAuth(permissionCode);
        }

        List<User> users = Session.getFunc(UserFunc.class).getUsers(permissionCode);        if (users != null && users.contains(user)) {
            return true;
        }
        return false;
    }

    private void checkExemptAmountLimit(final double exemptAmount, final int exemptType) {
        String exemptAmountPermission = mPaymentInfo.isDinner() ? DinnerApplication.PERMISSION_DINNER_MALING_AMOUNT_LIMIT : FastFoodApplication.PERMISSION_FASTFOOD_MALING_AMOUNT_LIMIT;
                if (isExemptUserHasPermission(mExemptUser, exemptAmountPermission)) {
            Auth.Filter filter = VerifyHelper.createMax(exemptAmountPermission, BigDecimal.valueOf(exemptAmount));
            VerifyHelper.verifyAlert(getActivity(), exemptAmountPermission, filter, new VerifyHelper.Callback() {
                @Override
                public void onPositive(User user, String code, Auth.Filter filter) {
                    super.onPositive(user, code, filter);
                    setExemptResult(exemptAmount, exemptType);
                }
            });
        } else {
            setExemptResult(exemptAmount, exemptType);
        }
    }

        private void setExemptResult(double exemptAmount, int exemptType) {
        mPaymentInfo.setExemptAmount(exemptAmount);

        mPaymentInfo.setEraseType(exemptType);

        notifyDataSetChange();
        setExemptBTEraseType(mEraseBT, exemptType);
        EventBus.getDefault().post(new ExemptEventUpdate(exemptType, true));

        PayDisplayUtil.doUpdateScreen(getActivity().getApplicationContext(), mCurrentMethod, mPaymentInfo);
    }


    public void onEventMainThread(AmountEditedEvent event) {
        if (!Utils.isEmpty(methodList)) {
            for (PayMethodItem menu : methodList) {
                if (menu.methodId == event.getPayMethodId()) {
                    menu.methodName = event.getMethodName();
                    break;
                }
            }
            mPayMethodAdapter.notifyDataSetChanged();
        }
    }




    private void setExemptBTEraseType(Button exemptbutton, int eraseType) {
        ErasePopupHelper.setExemptButtonEraseType(getActivity(), exemptbutton, eraseType);
    }


    private void notifyDataSetChange() {
        tradeAmountTVTotal.setVisibility(View.GONE);
        if (mPaymentInfo.getPayScene() == PayScene.SCENE_CODE_BUFFET_DEPOSIT) {            this.mEraseBT.setVisibility(View.INVISIBLE);
            this.invoiceCbox.setVisibility(View.INVISIBLE);
            this.mPayDepositBT.setVisibility(View.GONE);
            this.tradeAmountTV.setVisibility(View.GONE);
            this.eraseTxv.setVisibility(View.GONE);
                        updatePaidAmount(mPaymentInfo.getPaidPaymentItems());
                        unReceivedTxv.setText(String.format(getString(R.string.pay_cash_text_deposit), CashInfoManager.formatCash(mPaymentInfo.getActualAmount())));
        } else {
                        unReceivedTxv.setText(String.format(getString(R.string.pay_cash_text_unreceived), CashInfoManager.formatCash(mPaymentInfo.getActualAmount())));
                        if (mPaymentInfo.getTradeBusinessType() == BusinessType.BUFFET && mPaymentInfo.getTradeVo().getTradeDeposit() != null && mPaymentInfo.getTradeVo().getTradeDeposit().isValid()) {
                tradeAmountTV.setText(String.format(getString(R.string.pay_cash_text), CashInfoManager.formatCash(mPaymentInfo.getTradeAmount())) + " " + String.format(getString(R.string.pay_cash_text_include_deposit), CashInfoManager.formatCash(mPaymentInfo.getTradeVo().getTradeDeposit().getDepositPay().doubleValue())));
            } else {
                boolean isSelected = SharedPreferenceUtil.getSpUtil().getBoolean("auto_privilege", false);
                if (CustomerManager.getInstance().getLoginCustomer() != null
                        && mCurrentMethod != null && mCurrentMethod.methodId == PAY_MENU_TYPE_MEMBER
                        && (!mPaymentInfo.isDinner() && !mPaymentInfo.isOrdered() && isSelected)
                        && ShoppingCart.getInstance().isHaveMemberPrivilege(mPaymentInfo.isDinner())) {
                    tradeAmountTV.setText(String.format(getString(R.string.pay_cash_privilege_text_2), CashInfoManager.formatCash(mPaymentInfo.getTradeAmount())));
                    tradeAmountTV.setCompoundDrawablesWithIntrinsicBounds(R.drawable.custom_allowance, 0, 0, 0);
                    tradeAmountTVTotal.setText(CashInfoManager.formatCash(mPaymentInfo.getSaleAmount()));
                    tradeAmountTVTotal.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    tradeAmountTVTotal.setVisibility(View.VISIBLE);
                } else {
                    tradeAmountTV.setText(String.format(getString(R.string.pay_cash_text), CashInfoManager.formatCash(mPaymentInfo.getTradeAmount())));
                    tradeAmountTV.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
                        updatePaidAmount(mPaymentInfo.getPaidPaymentItems());
                        updateExempt();
                        if (mPaymentInfo.isNeedToPayDeposit()) {
                this.mPayDepositBT.setVisibility(View.VISIBLE);
            } else {
                this.mPayDepositBT.setVisibility(View.GONE);
            }
        }
    }

    PopupWindow.OnDismissListener listener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            try {
                mPaidListIcon.setSelected(false);

            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
        }
    };


    private void openPaymentItemList(final List<PaymentItem> paymentItems) {
        if (Utils.isEmpty(paymentItems)) {
            return;
        }
        if (paymentItems.size() > 1) {
            sortPaymentItems(paymentItems);
            mPaidListIcon.setSelected(true);
            InvoicePopupHelper.showPaymentItemList(getActivity(), mPaymentInfo, paymentItems, tagLlyt,
                    new InvoicePopupHelper.OnPaymentItemCallback() {
                        @Override
                        public boolean onDelete(PaymentItem paymentItem) {
                            return true;
                        }
                    }, listener);
        }
    }

        private void showEarnestAmount() {
        if (mPaymentInfo.getTradeVo().getTradeEarnestMoneys() != null && mPaymentInfo.getTradeVo().getTradeEarnestMoneys().size() > 0) {
            tagLlyt.setVisibility(View.VISIBLE);
            mPaidListIcon.setVisibility(View.INVISIBLE);
            TradeEarnestMoney tradeEarnestMoney = mPaymentInfo.getTradeVo().getTradeEarnestMoneys().get(0);
            StringBuilder stringBuild = new StringBuilder();
            String textPayName = PaySettingCache.getPayModeNameByModeId(tradeEarnestMoney.getPayModeId());
            stringBuild.append(textPayName);
                        stringBuild.append("(" + getString(R.string.pay_earnest_text) + ")");
            stringBuild.append(CashInfoManager.formatCash(mPaymentInfo.getTradeVo().getTradeEarnestMoney()));

            String text = stringBuild.toString();
            SpannableStringBuilder builder = new SpannableStringBuilder(text);
            builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_red_2)),
                    text.indexOf("("),
                    text.indexOf(")") + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            receivedTxv.setText(builder);
        }
    }


    private void updatePaidAmount(List<PaymentItem> paymentItems) {
        if (paymentItems == null || paymentItems.isEmpty()) {
                        if (mPaymentInfo.getTradeVo().getTradeEarnestMoney() > 0) {
                showEarnestAmount();
            } else {
                tagLlyt.setVisibility(View.GONE);
            }
                        return;
        }
        tagLlyt.setVisibility(View.VISIBLE);
                if (paymentItems.size() == 1) {
            mPaidListIcon.setVisibility(View.INVISIBLE);
            PaymentItem paymentItem = paymentItems.get(0);
            StringBuilder stringBuild = new StringBuilder();
            String textPayName = TextUtils.isEmpty(paymentItem.getPayModeName()) ? PaySettingCache.getPayModeNameByModeId(paymentItem.getPayModeId()) : paymentItem.getPayModeName();
            stringBuild.append(textPayName);
                        if (mPaymentInfo.getTradeBusinessType() == BusinessType.BUFFET && mPaymentInfo.getTradeVo().getTradeDepositPaymentItem() != null) {
                if (paymentItem.equals(mPaymentInfo.getTradeVo().getTradeDepositPaymentItem())) {
                    stringBuild.append("(" + getString(R.string.pay_deposit_text) + ")");
                }
            }
            stringBuild.append(CashInfoManager.formatCash(paymentItem.getUsefulAmount().doubleValue()));

            if (PayModeId.MEITUAN_FASTPAY.value().equals(paymentItem.getPayModeId())) {
                double youhuiAmount = paymentItem.getUsefulAmount().subtract(paymentItem.getFaceAmount()).doubleValue();
                stringBuild.append("(");
                stringBuild.append(getString(R.string.pay_paid_privilege));
                stringBuild.append(CashInfoManager.formatCash(youhuiAmount));
                stringBuild.append(")");
                String text = stringBuild.toString();
                SpannableStringBuilder builder = new SpannableStringBuilder(text);
                builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_red_2)),
                        text.indexOf("("),
                        text.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                receivedTxv.setText(builder);
            } else {
                receivedTxv.setText(stringBuild.toString());
            }
        } else if (paymentItems.size() > 1) {
            mPaidListIcon.setVisibility(View.VISIBLE);
            double receivedAmount = mPaymentInfo.getPaidAmount();            receivedTxv.setText(CashInfoManager.formatCash(receivedAmount));
        }
    }

    private static void sortPaymentItems(List<PaymentItem> paymentItems) {
        if (paymentItems == null) {
            return;
        }
        Collections.sort(paymentItems, new Comparator<PaymentItem>() {
            @Override
            public int compare(PaymentItem lhs, PaymentItem rhs) {
                Long lct = lhs.getClientCreateTime();
                Long rct = rhs.getClientCreateTime();
                if (lct == null || rct == null) {
                    return 0;
                }
                if (lct.longValue() > rct.longValue()) {
                    return -1;
                }
                return 1;
            }
        });
    }

    @Override
    public void updatePaymentInfo() {
        try {
            if (this.isAdded()) {
                this.notifyDataSetChange();                            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }

    public void onEventMainThread(RefreshTradeVoEvent event) {
        mTradeVo = event.getTradeVo();
        mPaymentInfo.setTradeVo(mTradeVo);        updatePaymentInfo();
        EventBus.getDefault().post(new ExemptEventUpdate(mPaymentInfo.getEraseType()));
    }

        @Override
    public void onStart() {
                super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        unregisterEventBus();
        ErasePopupHelper.getInstance().disposePopuWindow();        super.onDestroy();
    }

    @Override
    public void lagOver(int payType) {
                EventBus.getDefault().post(new ActionCloseOrderDishActivity());
        DoPayUtils.showPayOkDialog(getActivity(), mDoPayApi, mPaymentInfo, false, 2);
    }

        public void onEventMainThread(DepositPayOver depositPayOver) {
        if (mPaymentInfo.getTradeBusinessType() == BusinessType.BUFFET
                && mPaymentInfo.getPayScene() == PayScene.SCENE_CODE_SHOP) {
            if (depositPayOver.getPayResp() != null) {                if (!Utils.isEmpty(depositPayOver.getPayResp().getTrades())) {
                    this.mPaymentInfo.getTradeVo().setTrade(depositPayOver.getPayResp().getTrades().get(0));
                }
                if (!Utils.isEmpty(depositPayOver.getPayResp().getPaymentItems()))
                    this.mPaymentInfo.getTradeVo().setTradeDepositPaymentItem(depositPayOver.getPayResp().getPaymentItems().get(0));
                if (!Utils.isEmpty(depositPayOver.getPayResp().getTradeDepositPayRelations()))
                    this.mPaymentInfo.getTradeVo().setTradeDepositPayRelation(depositPayOver.getPayResp().getTradeDepositPayRelations().get(0));
                DoPayUtils.findPaymentVoAsync(this.mPaymentInfo, true);
                                CustomerResp customer = CustomerManager.getInstance().getDinnerLoginCustomer();
                DinnerShoppingCart.getInstance().updateDataFromTradeVo(this.mPaymentInfo.getTradeVo());
                                if (customer != null) {
                    CustomerManager.getInstance().setDinnerLoginCustomer(customer);
                                        DinnerCashManager cashManager = new DinnerCashManager();
                    if (customer.card == null) {
                        cashManager.updateIntegralCash(customer);
                    } else {
                        cashManager.updateIntegralCash(customer.card);
                    }
                }
                mPaymentInfo.setOrdered(true);            }
        }
    }


    public void onEventMainThread(PushPayRespEvent event) {
        try {

            PayResp result = event.getPushPayMent().getContent();
                        if (!DoPayApi.OnlineDialogShowing && mPaymentInfo.getId().equals(mDoPayApi.getCurrentPaymentInfoId())) {
                doVerifyPayResp(result);            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        IOnlinePayOverCallback mOnlinePayOverCallback = new IOnlinePayOverCallback() {

        @Override
        public void onPayResult(Long paymentItemId, int payStatus) {
                    }
    };

    private void doVerifyPayResp(final PayResp result) {
        if (result == null) {
            return;
        }
                if (TextUtils.isEmpty(mDoPayApi.getOnlinePaymentItemUuid())) {             Trade pushTrade = result.getTrades().get(0);
            if (pushTrade != null && mPaymentInfo.getTradeVo().getTrade().getUuid().equals(pushTrade.getUuid()) && DoPayUtils.isTradePaidOver(pushTrade)) {
                                if (!mPaymentInfo.isPrintedOk()) {
                    mPaymentInfo.getTradeVo().setTrade(pushTrade);
                    onFinishedDoPrint(result);                    mPaymentInfo.doFinish();                }
            }
        } else {
                        mDoPayApi.doVerifyPayResp(getActivity(), mPaymentInfo, result, mOnlinePayOverCallback);
        }
    }


        private void onFinishedDoPrint(PayResp result) {
        if (mPaymentInfo.getCustomer() == null && mPaymentInfo.getEcCard() != null) {
            mPaymentInfo.setPrintMemeberInfoByCard();
        }
        if (!mPaymentInfo.isDinner() && mPaymentInfo.getTradeVo().getTrade().getTradeType() == TradeType.SELL_FOR_REPEAT) {
            EventBus.getDefault().post(new SellForRepeatEvent(mPaymentInfo.getTradeVo().getTrade().getUuid()));
        }
        if (mPaymentInfo.isSplit() && (mPaymentInfo.getOtherPay().isContainsPayModel(PayModeId.MEMBER_CARD)
                || mPaymentInfo.getOtherPay().isContainsPayModel(PayModeId.ENTITY_CARD)
                || mPaymentInfo.getOtherPay().isContainsPayModel(PayModeId.ANONYMOUS_ENTITY_CARD)
        )) {
            MemberPayChargeEvent memberPayChargeEvent = new MemberPayChargeEvent();
            memberPayChargeEvent.setmValueCardBalance(BigDecimal.valueOf(CashInfoManager.floatSubtract(mPaymentInfo.getMemberCardBalance(), mPaymentInfo.getOtherPay().getGroupActualAmount())));
            EventBus.getDefault().post(memberPayChargeEvent);        }

                if (!mPaymentInfo.isPrintedOk()) {
            EventBus.getDefault().post(new StopPayStatusTimer(true));
            Trade trade = mPaymentInfo.getTradeVo().getTrade();
            List<PrintOperation> printOperations = result.getPrintOperations();
            if (printOperations != null && !printOperations.isEmpty()
                    && mPaymentInfo.getMemberResp() != null) {
                try {
                    String tmp = printOperations.get(0).getExtendsStr();
                    JSONObject extendsStr = new JSONObject(tmp);
                    double beforeActualvalue = extendsStr.optDouble("beforeActualvalue", 0);
                    double beforeSendValue = extendsStr.optDouble("beforeSendValue", 0);
                    mPaymentInfo.getMemberResp().setValueCardBalance(BigDecimal.valueOf(beforeActualvalue + beforeSendValue));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
                        if (mPaymentInfo.isOrderCenter() && trade.getDeliveryType() == DeliveryType.HERE
                    && trade.getSource() == SourceId.POS && trade.getSourceChild() == SourceChild.ANDROID) {
                mDoPayApi.doPrint(mPaymentInfo, mPaymentInfo.getTradeVo().getTrade().getUuid(), true, true, true, true);
                mPaymentInfo.setPrintedOk(true);
            } else {
                                if (ServerSettingCache.getInstance().isJinChBusiness()) {
                    if (mPaymentInfo.getTradeBusinessType() != BusinessType.CARD
                            && mPaymentInfo.getTradeBusinessType() != BusinessType.ONLINE_RECHARGE
                            && mPaymentInfo.getTradeBusinessType() != BusinessType.ANONYMOUS_ENTITY_CARD_SELL
                            && mPaymentInfo.getTradeBusinessType() != BusinessType.ENTITY_CARD_CHANGE
                            && mPaymentInfo.getTradeBusinessType() != BusinessType.ANONYMOUS_ENTITY_CARD_RECHARGE) {
                        mDoPayApi.doPrint(mPaymentInfo, mPaymentInfo.getTradeVo().getTrade().getUuid(), !mPaymentInfo.isOrderCenter(),
                                !mPaymentInfo.isOrderCenter(), true, true);
                        mPaymentInfo.setPrintedOk(true);
                    }
                } else {
                                        if (mPaymentInfo.getTradeBusinessType() != BusinessType.ENTITY_CARD_CHANGE && mPaymentInfo.getPayScene() != PayScene.SCENE_CODE_BOOKING_DEPOSIT) {
                        mDoPayApi.doPrint(mPaymentInfo, mPaymentInfo.getTradeVo().getTrade().getUuid(), !mPaymentInfo.isOrderCenter(),
                                !mPaymentInfo.isOrderCenter(), true, true);
                        mPaymentInfo.setPrintedOk(true);
                    }
                }
            }
            AuthLogManager.getInstance().flush(OrderActionEnum.ACTION_CHECK_OUT, trade.getId(), trade.getUuid(), trade.getClientUpdateTime());
        }
    }

        private void checkMemberPrice(TradeVo tradeVo) {
                if (this.mPaymentInfo.getTradeBusinessType() == BusinessType.BUFFET) {
            return;
        }
        if (this.mCurrentMethod != null && PaySettingCache.isSetPayModeGroup(PayScene.SCENE_CODE_SHOP.value(), PayModelGroup.VALUE_CARD) && this.mCurrentMethod.methodId != IPaymentMenuType.PAY_MENU_TYPE_MEMBER) {
            if (isNeedToDealMemberPrice(tradeVo)) {
                                if (mPaymentInfo.getPaidAmount() > 0 && !PayUtils.isMemberPay(mPaymentInfo.getPaidPaymentItems())) {
                    if (mPaymentInfo.isSplit()) {
                        SeparateShoppingCart.getInstance().removeAllMemberPrivileges();
                    } else {
                        DinnerShoppingCart.getInstance().removeAllMemberPrivileges();
                    }
                } else {
                                        showPayMethodFragment(getPayMethodItemById(IPaymentMenuType.PAY_MENU_TYPE_MEMBER), true, false);
                }
            }
        }
    }

    private PayMethodItem getPayMethodItemById(int method) {
        if (this.methodList != null && !this.methodList.isEmpty()) {
            for (PayMethodItem it : this.methodList) {
                if (it.methodId == method) {
                    return it;
                }
            }
        }
        return null;
    }

        private boolean isNeedToDealMemberPrice(TradeVo tradeVo) {
                if (tradeVo != null && getCurrentLoginCustomer() != null) {
            TradeCustomer tradeCustomer = PayUtils.getTradeCustomer(tradeVo);
            if (tradeCustomer != null) {
                return (CustomerManager.getInstance().isOpenPriceLimit(tradeCustomer.getCustomerType()) && PayUtils.isTradeWithMemberPrice(tradeVo));
            }
        }
        return false;
    }

    private CustomerResp getCurrentLoginCustomer() {
                return mPaymentInfo.isDinner() ? DinnerShopManager.getInstance().getLoginCustomer() : CustomerManager.getInstance().getLoginCustomer();
    }

    private void showRemoveMemberPriceDialog(final PayMethodItem payMethodItem) {
        new CommonDialogFragment.CommonDialogFragmentBuilder(this.getActivity())
                .title(R.string.pay_remove_member_price_alter)
                .iconType(CommonDialogFragment.ICON_ASK)
                .positiveText(R.string.invoice_btn_ok)
                .positiveLinstner(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                                                try {
                            if (mPaymentInfo.isSplit()) {
                                SeparateShoppingCart.getInstance().removeAllMemberPrivileges();
                            } else {
                                DinnerShoppingCart.getInstance().removeAllMemberPrivileges();
                            }
                                                        if (!mPaymentInfo.isDinner()) {
                                ShoppingCart.getInstance().removeMemberPrivilege();
                                                                mPaymentInfo.setIsGroupPay(true);
                                                                mPaymentInfo.setTradeVo(ShoppingCart.getInstance().createFastFoodOrder(false));
                                notifyDataSetChange();
                            }
                            isRemovedMemberPrice = true;
                            showPayMethodFragment(payMethodItem, false, true);
                        } catch (Exception e) {
                            Log.e(TAG, "", e);
                        }
                    }
                })
                .negativeText(R.string.invoice_btn_cancel)
                .build()
                .show(getFragmentManager(), "removeMemberPrice ");
    }

    private boolean isRemovedMemberPrice = false;

        public int getPayResult() {
        if (this.mPaymentInfo != null && this.mPaymentInfo.getTradeVo() != null && this.mPaymentInfo.getTradeVo().getTrade() != null && this.mPaymentInfo.getTradeVo().getTrade().getTradePayStatus() != null) {
            return this.mPaymentInfo.getTradeVo().getTrade().getTradePayStatus().value();
        }
        return 1;    }

    @Override
    public void finishPay() {        if (this.getActivity() != null) {
            this.getActivity().finish();
        }
    }
}
