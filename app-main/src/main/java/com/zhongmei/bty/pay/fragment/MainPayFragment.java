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
import com.zhongmei.bty.basemodule.devices.display.ShowPayManagerEvent;
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

/**
 * Created by demo on 2018/12/15
 * 支付主界面，该界面可以独立嵌套到activity里
 */
@EFragment(R.layout.pay_main_container_fragment)
public class MainPayFragment extends MobclickAgentFragment implements IPaymentMenuType, IUpdateUICallback, IDoLag, IFinishPayCallback {
    private final String TAG = "MainPayFragment";

    @ViewById(R.id.tv_total_fee)
    protected AutofitTextView tv_totalFee;//抹零前价格,跟未收平行

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

    //private FragmentTransaction ft;

    // private FragmentManager fragmentManager;

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

    private boolean isOrdered;//是否已经保存订单

    private PaymentInfoManager mPaymentInfo;

    private DoPayApi mDoPayApi;//add v8.9

//    private LoadingFinish mLoadingFinish;

    private BigDecimal mCurrentTradeAmount;

    //    public void registerLoadingListener(LoadingFinish mLoadingFinish) {
//        this.mLoadingFinish = mLoadingFinish;
//    }
    private PayScene mPayScene = PayScene.SCENE_CODE_SHOP;//支付场景 add 20170704

    private int defautlPaymentMenuType = -1;

    // v8.6.0 增加快捷支付字段
    private int quickPayType = -1;

    // v8.15.0 增加雅座储值方式字段
    private int paymentMenuType;

    // v8.16.0 是否支持翼支付
    private boolean isSupportYIPay;

    boolean isFromOpenPlatForm = false;//是否来自开放平台 add v8.5
    boolean isSupportOnline = true;//是否支持在线支付 add 8.9异步
    boolean isSupportMobilePay = true;// 是否支持一码付，离线订单暂时不支持一码付
    private boolean isGroupPay = true;//add v8.9
    private User mExemptUser;//抹零用户

    public void setDoPayApi(DoPayApi doPayApi) {//add v8.9
        this.mDoPayApi = doPayApi;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPaymentInfo = PaymentInfoManager.getNewInstance(this);//初始化对象
        if (mDoPayApi == null)
            mDoPayApi = DoPayManager.getInstance();//add v8.9 解耦临时方案，以后会通过外部传入
        mDoPayApi.setOnlinePaymentItemUuid(null);//清空上次在线支付的的paymentItemUuid
        mDoPayApi.setCurrentPaymentInfoId(mPaymentInfo.getId());//当前支付界面id add 20170708
        if (getArguments() != null) {
            mTradeVo = (TradeVo) getArguments().getSerializable("tradeVo");
            mSourceTradeVo = (TradeVo) getArguments().getSerializable("source");
            PayScene PayScene = (PayScene) getArguments().getSerializable("payScene");
            PayActionPage payActionPage = (PayActionPage) getArguments().getSerializable(IPayConstParame.EXTRA_PAY_ACTION_PAGE);
            if (PayScene != null) {
                mPayScene = PayScene;
            }
            if (payActionPage != null) {
                mPaymentInfo.setPayActionPage(payActionPage);//支付页面
            }
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
            // v8.15.0 雅座储值显示
            paymentMenuType = getArguments().getInt(IPayConstParame.EXTRA_MENU_DISPLAY_TYPE, 0);
            if (mTradeVo != null && mTradeVo.getTrade() != null) {
                mCurrentTradeAmount = mTradeVo.getTrade().getTradeAmount();
                if (mTradeVo.getTrade().getTradePayStatus() == TradePayStatus.PAYING) {
                    isOrdered = true;
                }
            }
        }
        mPaymentInfo.setFinishPayCallback(this);//add 20180123
        // 组合付款管理
        mPaymentInfo.setDefaultPaymentMenuType(defautlPaymentMenuType);
        // 快捷支付
        mPaymentInfo.setQuickPayType(quickPayType);
        mPaymentInfo.setTradeVo(mTradeVo);//要支付的订单
        mPaymentInfo.setSplit(isSplit);//是否拆单
        mPaymentInfo.setSourceTradeVo(mSourceTradeVo);// 原单
        mPaymentInfo.setOrderCenter(isOrderCenter);// 设置订单中心属性
        mPaymentInfo.setOrdered(isOrdered);// 正餐已经下单
        mPaymentInfo.setPayScene(mPayScene);//支付场景
        mPaymentInfo.setIsGroupPay(isGroupPay);//add v8.9
        if (isFromOpenPlatForm) {//是否来自开放平台 add v8.5
            mPaymentInfo.setPrintedOk(true); //开放平台来的订单默认已经打印
        }
    }


    @AfterViews
    void init() {
        registerEventBus();

        if (getActivity().getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
            // 隐藏软键盘
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }

        //fragmentManager = getFragmentManager();
        //隐藏关闭按钮
        if ((mPaymentInfo.isDinner() && !mPaymentInfo.isOrderCenter()) && mPaymentInfo.getPayScene() == PayScene.SCENE_CODE_SHOP) {
            mCloseBT.setVisibility(View.INVISIBLE);
        }
        //电子发票开关,如果是烽火科技默认开启电子发票  modify 20180309
        if (PaySettingCache.isElectronicInvoiceOpen() || ShopInfoCfg.getInstance().isMonitorCode(ShopInfoCfg.ShopMonitorCode.MONITOR_CODE_FHXM)) {
            invoiceCbox.setVisibility(View.VISIBLE);
            //如果是烽火科技默认开启电子发票 add 20180309
            if (ShopInfoCfg.getInstance().isMonitorCode(ShopInfoCfg.ShopMonitorCode.MONITOR_CODE_FHXM)) {
                invoiceCbox.setChecked(true);
                invoiceCbox.setEnabled(false);
                mPaymentInfo.setOpenElectronicInvoice(true);
            }
        } else {
            invoiceCbox.setVisibility(View.INVISIBLE);
        }
        // 如果是烘焙的业务类型,暂时屏蔽掉电子发票入口,销账,预定交订金不支持发票
        BusinessType businessType = mTradeVo.getTrade().getBusinessType();
        if (BusinessType.BOOKING_LIST.equals(businessType) || BusinessType.TAILING_LIST.equals(businessType) || mPaymentInfo.getPayScene() == PayScene.SCENE_CODE_WRITEOFF || mPaymentInfo.getPayScene() == PayScene.SCENE_CODE_BOOKING_DEPOSIT) {
            invoiceCbox.setVisibility(View.INVISIBLE);
        }
        //modify 20170206 start
        //如果已经下单先异步查询支付信息
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
                    try {// 通知ui更新
                        mPaymentInfo.setPaidPaymentRecords(data);
                        //加载Ui
                        loadViews();
                    } catch (Exception e) {
                        Log.e(TAG, "", e);
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } else {
            //加载Ui
            loadViews();
        }
    }

    private void loadViews() {
        //关闭加载页
//        if (mLoadingFinish != null) {
//            mLoadingFinish.loadingFinish();
//        }
        //初始化菜单
        initMenus();
        //刷新新单支付信息
        if (mTradeVo.getTrade().getId() == null || mPaymentInfo.getPayScene() == PayScene.SCENE_CODE_BUFFET_DEPOSIT) {
            notifyDataSetChange();
        }
    }

    //modify 20170206 end
    @Override
    protected void onInit() {
        if (ServerSettingCache.getInstance().isJinChBusiness()) {
            return;
        }
        PayDisplayUtil.doUpdateScreen(this.getActivity().getApplicationContext(), mCurrentMethod, mPaymentInfo);
    }

    private void initMenus() {
        // 初始化菜单及默认
        PaymentMenuTool menuTool = new PaymentMenuTool(this.getActivity(), mPaymentInfo.getPayScene(), mPaymentInfo.getTradeBusinessType());
        menuTool.setDefaultPaymentMenuType(mPaymentInfo.getDefaultPaymentMenuType());//add v8.4
        menuTool.setSupportLag(PayUtils.isSupportLagPay(mPaymentInfo, !this.isFromOpenPlatForm));//add v8.5 开放平台不支持挂账
        menuTool.setSupportOnline(isSupportOnline);
        menuTool.setSuportMobilePay(isSupportMobilePay);
        menuTool.setSupportYiPay(isSupportYIPay);//是否支持翼支付addv8.16
        menuTool.setColumns(getResources().getInteger(R.integer.zm_pay_menu_count));
        // v8.15.0 雅座储值显示
        switch (paymentMenuType) {
            case IPayConstParame.MENU_DISPLAY_TYPE_MEMBER_ENABLE: // 其它禁用，储值可用1
                menuTool.setThirdCoustomer(true);
                menuTool.setMemberPay(true);
                break;
            case IPayConstParame.MENU_DISPLAY_TYPE_MEMBER_DISABLE: // 储值禁用2
                menuTool.setThirdCoustomer(true);
                menuTool.setMemberPay(false);
                break;
            case IPayConstParame.MENU_DISPLAY_TYPE_NOMAL: // 全部正常0
            default:
                break;
        }
        methodList = menuTool.initMethodList();
        mPayMethodAdapter = new PayMethodIndicatorAdapter(this.getActivity());
        mPayMethodAdapter.addData(methodList);
        payMethodIndicator.setAdapter(mPayMethodAdapter);
        //设置菜单点击效果
        payMethodIndicator.setOnItemClickListener(new Indicator.OnItemClickListener() {
            @Override
            public void onItemClick(Indicator indicator, Indicator.Adapter adapter, int position) {
                if (!ClickManager.getInstance().isClicked()) {
                    PayMethodItem item = (PayMethodItem) adapter.getItem(position);
                    if (mCurrentMethod != item) {
                        //add v8.2 添加会员权益判断 start
                        if (mPaymentInfo.getTradeBusinessType() != BusinessType.BUFFET) {//自助餐不参与会员价限制
                            //如果当前是储值切换到其它,需要处理会员价权益
                            if (mCurrentMethod != null && mCurrentMethod.methodId == IPaymentMenuType.PAY_MENU_TYPE_MEMBER) {
                                //如果需要处理会员权益
                                if (isNeedToDealMemberPrice(mPaymentInfo.getTradeVo())) {
                                    //弹出移除会员价框
                                    showRemoveMemberPriceDialog(item);
                                    return;
                                }
                            } else {
                                //如果是其它切换到储值，要恢复会员价
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
                        //add v8.2 添加会员权益判断 end
                        showPayMethodFragment(item, false, true);
                    }
                }
            }
        });
        // 展示默认界面
        setDefaultPayMethod(menuTool.getDefaultPayMethod());
    }

    /*
     * 默认现金支付、银联、储值在线，其它，挂账
     */
    private void setDefaultPayMethod(PayMethodItem defaultPayMethodItem) {
        //如果有现金默认现金
        if (defaultPayMethodItem != null) {
            showPayMethodFragment(defaultPayMethodItem, true, true);
            mCurrentMethod = defaultPayMethodItem;
        }
    }

    private void showPayMethodFragment(PayMethodItem payMethodItem, boolean isDefault, boolean isSuportGroupPay) {
        //如果有现金默认现金
        if (payMethodItem != null) {
            //抹零按钮是否可以用
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
                case PAY_MENU_TYPE_CASH://现金
                    updateLastFragment(mCurrentMethod);
                    payMethodItem.isSelected = true;
                    mCurrentMethod = payMethodItem;
                    if (mCashPayFragment != null) {
                        this.showFragment(mCashPayFragment, false);
                    } else {
                        mCashPayFragment = CashPayFragment.newInstance(mPaymentInfo, mDoPayApi);//add v8.9
                        this.addFragment(R.id.pager, mCashPayFragment, "PayCashPayFragment");
                    }
                    break;
                case PAY_MENU_TYPE_ALIPAY:// 支付宝
                    if (!checkEnable(isDefault)) {
                        return;
                    }

                    EventBus.getDefault().post(new RegisterDeWoOnlinePayScanEvent());
                    EventBus.getDefault().post(new ShowPayManagerEvent());
                    updateLastFragment(mCurrentMethod);
                    payMethodItem.isSelected = true;
                    mCurrentMethod = payMethodItem;
                    if (mAliPayFragment != null) {
                        this.showFragment(mAliPayFragment, false);
                    } else {
                        mAliPayFragment = OnlinePayFragment.newInstance(PAY_MENU_TYPE_ALIPAY, mPaymentInfo, mDoPayApi);
                        if (ServerSettingCache.getInstance().isJinChBusiness()) { //如果是金城默认弹出主扫被扫dialog
                            mAliPayFragment.setDefaultShowDialog(true);
                        } else {
                            if (isDefault && defautlPaymentMenuType == -1) {
                                mAliPayFragment.setDefaultShowDialog(false);
                            }
                        }
                        this.addFragment(R.id.pager, mAliPayFragment, "PayOnlinePayFragment");
                    }
                    break;

                case PAY_MENU_TYPE_WEIXIN://微信支付
                    if (!checkEnable(isDefault)) {
                        return;
                    }

                    EventBus.getDefault().post(new RegisterDeWoOnlinePayScanEvent());
                    EventBus.getDefault().post(new ShowPayManagerEvent());
                    updateLastFragment(mCurrentMethod);
                    payMethodItem.isSelected = true;
                    mCurrentMethod = payMethodItem;
                    if (mWxPayFragment != null) {
                        this.showFragment(mWxPayFragment, false);
                    } else {
                        mWxPayFragment = OnlinePayFragment.newInstance(PAY_MENU_TYPE_WEIXIN, mPaymentInfo, mDoPayApi);
                        if (ServerSettingCache.getInstance().isJinChBusiness()) { //如果是金城默认弹出主扫被扫dialog
                            mWxPayFragment.setDefaultShowDialog(true);
                        } else {
                            if (isDefault && defautlPaymentMenuType == -1) {
                                mWxPayFragment.setDefaultShowDialog(false);
                            }
                        }
                        this.addFragment(R.id.pager, mWxPayFragment, "PayOnlinePayFragment");
                    }
                    break;
                case PAY_MENU_TYPE_UNION:// 银联
                    if (!checkEnable(isDefault)) {
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
                case PAY_MENU_TYPE_MEMBER://储值(正餐快餐外卖支持储值支付)
                    if (!checkEnable(isDefault)) {
                        return;
                    }

                    EventBus.getDefault().post(new RegisterDeWoCustomerLoginScanEvent());
                    EventBus.getDefault().post(new ShowPayManagerEvent());
                    updateLastFragment(mCurrentMethod);
                    payMethodItem.isSelected = true;
                    mCurrentMethod = payMethodItem;
                    // 收银台储值支付点击量
                    MobclickAgentEvent.onEvent(getActivity(), MobClick.SNACK_PAY_CHARGE);
                    if (mMemberMainFragment != null) {
                        mMemberMainFragment.setSuportGroupPay(isSuportGroupPay); //add v8.2 添加会员权益判断
                        this.showFragment(mMemberMainFragment, false);
                    } else {
                        mMemberMainFragment = new MemberMainFragment_();
                        mMemberMainFragment.setSuportGroupPay(isSuportGroupPay); //add v8.2 添加会员权益判断
                        mMemberMainFragment.setPaymentInfo(mPaymentInfo);
                        mMemberMainFragment.setDoPayApi(mDoPayApi);//add v8.11
                        this.addFragment(R.id.pager, mMemberMainFragment, "PayCustomerMainFragment");
                    }
                    break;

                case PAY_MENU_TYPE_OTHERS://其它
                    if (!checkEnable(isDefault)) {
                        return;
                    }
                    updateLastFragment(mCurrentMethod);
                    payMethodItem.isSelected = true;
                    mCurrentMethod = payMethodItem;
                    if (mOtherPayFragment != null) {
                        this.showFragment(mOtherPayFragment, false);
                    } else {
                        mOtherPayFragment = OthersFragment.newInstance(mPaymentInfo, mDoPayApi);//modify v8.9
                        this.addFragment(R.id.pager, mOtherPayFragment, "PayOtherPayFragment");
                    }
                    break;
                case PAY_MENU_TYPE_LAGPAY://挂账
                    if (!checkEnable(isDefault)) {
                        return;
                    }
                    if (this.mPaymentInfo.getPaidAmount() > 0 && !isDefault) {
                        ToastUtil.showShortToast(R.string.pay_lagpay_cannot_use);
                        return;
                    }
                    // 挂账不允许抹零
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
                        //modify v8.10
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

    /**
     * @Title: updateLastFragment
     * @Description: 更新原来漏洞界面
     * @Param mCurrentMethod
     * @Return void 返回类型
     */
    private void updateLastFragment(PayMethodItem lastMethodItem) {
        if (lastMethodItem != null) {
            lastMethodItem.isSelected = false;
            lastMethodItem.isFocused = false;
            switch (lastMethodItem.methodId) {
                case PAY_MENU_TYPE_CASH://现金
                    if (mCashPayFragment != null) {
                        this.hideFragment(mCashPayFragment);
                    }
                    break;
                case PAY_MENU_TYPE_ALIPAY:// 支付宝
                    if (mAliPayFragment != null) {
                        this.hideFragment(mAliPayFragment);
                    }
                    break;

                case PAY_MENU_TYPE_WEIXIN://微信支付
                    if (mWxPayFragment != null) {
                        this.hideFragment(mWxPayFragment);
                    }
                    break;
                case PAY_MENU_TYPE_UNION:// 银联
                    if (mUnionPayFragment != null) {
                        this.hideFragment(mUnionPayFragment);
                    }
                    break;
                case PAY_MENU_TYPE_MEITUANCOUPON:// 美团券

                    if (mCouponPayFragment != null) {
                        this.hideFragment(mCouponPayFragment);
                    }
                    break;
                case PAY_MENU_TYPE_MEMBER://储值(正餐快餐外卖支持储值支付)
                    if (ServerSettingCache.getInstance().isJinChBusiness()) {
                        /*if (mJCCustomerCardPayFragment != null) {
                            this.hideFragment(mJCCustomerCardPayFragment);
                        }*/
                    } else {
                        if (mMemberMainFragment != null) {
                            this.hideFragment(mMemberMainFragment);
                        }
                    }
                    break;

                case PAY_MENU_TYPE_OTHERS://其它
                    if (mOtherPayFragment != null) {
                        this.hideFragment(mOtherPayFragment);
                    }
                    break;
                case PAY_MENU_TYPE_LAGPAY://挂账
                    if (mLagMainFragment != null) {
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
                // 抹零点击量
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
                                mExemptUser = user;//add v8.14
                                doExempt();
                            }
                        });
                break;
            case R.id.pay_main_container_fragment_tag:
                openPaymentItemList(mPaymentInfo.getPaidPaymentItems());
                break;
            case R.id.pay_main_container_fragment_invoice://电子票
                MobclickAgentEvent.onEvent(UserActionCode.ZC030011);
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

    //更新tradeVo add 20161009 begin
    public void updatePayUI(final TradeVo source, final TradeVo tradeVo, boolean isSplit) {
        //如果拆单模式还原到原单模式，还原原单的payment
        if (isSplit == false && mPaymentInfo.isSplit()) {
            mPaymentInfo.clearPaymentUUids();
            DoPayUtils.findSourceTradePaymentVos(mPaymentInfo);//查询原单payment记录
        }
        mPaymentInfo.setSourceTradeVo(source);
        mPaymentInfo.setSplit(isSplit);
        mPaymentInfo.setPrintedOk(false);
        if (tradeVo.getTrade().getBusinessType() == BusinessType.BUFFET || tradeVo.getTrade().getTradePayStatus() != TradePayStatus.PAYING && mPaymentInfo.getPaidAmount() <= 0) {
            mPaymentInfo.setOrdered(false);
        }
        //如果原单有支付记录，先清空
        if (isSplit && tradeVo.getTrade().getTradePayStatus() != TradePayStatus.PAYING && mPaymentInfo.getPaidAmount() <= 0) {
            mPaymentInfo.setPaidPaymentRecords(null);
            mPaymentInfo.clearPaymentUUids();
        }
        if (this.isFromOpenPlatForm) {//add v8.5 开放平台顾虑tradeExtra
            mPaymentInfo.setPrintedOk(true); //开放平台来的订单默认已经打印
            tradeVo.setTradeExtra(null);
        }
        updateTradeVo(tradeVo);
        checkMemberPrice(tradeVo);
    }

    private void updateTradeVo(TradeVo tradeVo) {
        boolean isClearExempt = true;
        double exemptAmount = mPaymentInfo.getExemptAmount();//当前抹零金额
        boolean isSameTradeAmount = tradeVo.getTrade().getTradeAmount().equals(mCurrentTradeAmount);//true没改变
        mPaymentInfo.setTradeVo(tradeVo);
        //如果没有抹零金额或者订单金额不变，不用清空抹零
        if (isSameTradeAmount || (exemptAmount <= 0 && mPaymentInfo.getEraseType() == ErasePopupHelper.REMOVE_NOTHING)) {
            isClearExempt = false;
        }
        //清空抹零
        if (isClearExempt) {
            mPaymentInfo.setEraseType(ErasePopupHelper.REMOVE_NOTHING);
            mPaymentInfo.setExemptAmount(0);
            EventBus.getDefault().post(new ExemptEventUpdate(ErasePopupHelper.REMOVE_NOTHING));
            setExemptBTEraseType(mEraseBT, ErasePopupHelper.REMOVE_NOTHING);//刷新抹零按钮样式
        } else { //保留抹零
            if (!isSameTradeAmount) {
                EventBus.getDefault().post(new ExemptEventUpdate(mPaymentInfo.getEraseType()));
            }
        }
        //如果金额变化，刷新应收数据
        if (!isSameTradeAmount) {
            this.mCurrentTradeAmount = tradeVo.getTrade().getTradeAmount();
            this.notifyDataSetChange();
            PayDisplayUtil.doUpdateScreen(this.getActivity().getApplicationContext(), mCurrentMethod, mPaymentInfo);
        }
    }

    //更新tradeVo add 20161009 end
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
                    //checkExemptAmountLimit(exmpent, exemptType);//add v8.14
                    setExemptResult(exmpent, exemptType);
                }
            }
        }
    };

    //显示自定义抹零对话框
    private void showNumberInputDialog() {
        //最大抹零金额为未付金额
        double maxValue = CashInfoManager.floatSubtract(mPaymentInfo.getTradeAmount(), mPaymentInfo.getPaidAmount());
        String defaultInput = null;
        if (mPaymentInfo.getExemptAmount() > 0) {
            defaultInput = MathDecimal.trimZero(BigDecimal.valueOf(mPaymentInfo.getExemptAmount())) + "";
        }
        NumberInputdialog numberDialog =
                new NumberInputdialog(this.getActivity(), getString(R.string.pay_auto_exempty_title), getString(R.string.pay_auto_exempty_edit_hint), defaultInput, maxValue, mInputOverListener);
        numberDialog.show();
    }

    /**
     * @Description: 刷新找零
     * @Return void 返回类型
     */
    private void updateExempt() {
        //抹零按钮是否可以用
        if (mPaymentInfo.enableExempt()) {
            //如果不是挂账显示抹零按钮
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
        //如果首次抹零
        if (mPaymentInfo.enableExempt() && exemptAmount > 0) {
            double receivableAmount = mPaymentInfo.getReceivableAmount();
            tv_totalFee.setText(CashInfoManager.formatCash(receivableAmount));
            tv_totalFee.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tv_totalFee.setVisibility(View.VISIBLE);

        } else {
            tv_totalFee.setVisibility(View.GONE);
        }
    }

    /**
     * 抹零触发事件
     *
     * @param event
     */
    public void onEventMainThread(ExemptEvent event) {
        String afterEraseOrderCash = null;
        String receivableAmount = CashInfoManager.formatCashAmount(this.mPaymentInfo.getReceivableAmount());
        double eraseAmount = 0;
        switch (event.getEraseType()) {

            case ErasePopupHelper.REMOVE_CENT:// 抹分
                // 抹分点击量
                MobclickAgentEvent.onEvent(getActivity(), MobClick.SNACK_PAY_WIPE_ZERO_FEN);
                afterEraseOrderCash = receivableAmount.substring(0, receivableAmount.length() - 1);
                eraseAmount = CashInfoManager.floatSubtract(Double.valueOf(receivableAmount), Double.valueOf(afterEraseOrderCash));

                break;

            case ErasePopupHelper.REMOVE_DIME:// 抹角
                // 抹角点击量
                MobclickAgentEvent.onEvent(getActivity(), MobClick.SNACK_PAY_WIPE_ZERO_JIAO);
                afterEraseOrderCash = receivableAmount.substring(0, receivableAmount.length() - 3);
                eraseAmount = CashInfoManager.floatSubtract(Double.valueOf(receivableAmount), Double.valueOf(afterEraseOrderCash));

                break;

            case ErasePopupHelper.REMOVE_YUAN:// 抹元
                // 抹元点击量
                MobclickAgentEvent.onEvent(getActivity(), MobClick.SNACK_PAY_WIPE_ZERO_YUAN);
                afterEraseOrderCash = receivableAmount.substring(0, receivableAmount.length() - 4) + "0";
                eraseAmount = CashInfoManager.floatSubtract(Double.valueOf(receivableAmount), Double.valueOf(afterEraseOrderCash));
                break;
            case ErasePopupHelper.REMOVE_AUTO:
                // 自定义抹零点击量
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
            case ErasePopupHelper.REMOVE_NOTHING:// 取消抹零
                eraseAmount = 0;
                break;
            default:
                break;

        }
        setExemptResult(eraseAmount, event.getEraseType());
        // checkExemptAmountLimit(eraseAmount,event.getEraseType());//add v8.14
    }

    //add v8.14 start
    //判断抹零用户有没有金额限制权限
    private static boolean isExemptUserHasPermission(User user, String permissionCode) {
        //抹零授权人就是当前登录用户
        if (user != null && user.equals(Session.getAuthUser())) {
            return Session.getAuthUser().getAuth().hasAuth(permissionCode);
        }

        List<User> users = Session.getFunc(UserFunc.class).getUsers(permissionCode);//该方法阻塞线程，暂时调用
        if (users != null && users.contains(user)) {
            return true;
        }
        return false;
    }

    private void checkExemptAmountLimit(final double exemptAmount, final int exemptType) {
        String exemptAmountPermission = mPaymentInfo.isDinner() ? DinnerApplication.PERMISSION_DINNER_MALING_AMOUNT_LIMIT : FastFoodApplication.PERMISSION_FASTFOOD_MALING_AMOUNT_LIMIT;
        //如果抹零授权用户配了金额限制权限就验证金额，否则就不做任何限制
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

    //设置抹零数据 add v8.14
    private void setExemptResult(double exemptAmount, int exemptType) {
        mPaymentInfo.setExemptAmount(exemptAmount);

        mPaymentInfo.setEraseType(exemptType);

        notifyDataSetChange(); //刷新应收数据

        setExemptBTEraseType(mEraseBT, exemptType); //修复抹零按钮样式

        EventBus.getDefault().post(new ExemptEventUpdate(exemptType, true));

        PayDisplayUtil.doUpdateScreen(getActivity().getApplicationContext(), mCurrentMethod, mPaymentInfo);
    }
    //add v8.14 end

    /**
     * 编辑金额触发事件
     * 修改支付菜单名称
     */
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
/*
    private void findPaymentVos(Trade trade) {
        //已经下单的要先查询支付记录
        if (trade.getId() != null) {
            PayUtils.findPaymentVos(mPaymentInfo);
        }
    }*/

    /**
     * 修改抹零按钮样式
     */

    private void setExemptBTEraseType(Button exemptbutton, int eraseType) {
        ErasePopupHelper.setExemptButtonEraseType(getActivity(), exemptbutton, eraseType);
    }
//-----------------------------by yanm[start]-------------------------------------------------------------

    /**
     * 当mPaymentInfo有改变后，通知刷新对应的UI
     */
    private void notifyDataSetChange() {
        tradeAmountTVTotal.setVisibility(View.GONE);
        if (mPaymentInfo.getPayScene() == PayScene.SCENE_CODE_BUFFET_DEPOSIT) {//押金模式
            this.mEraseBT.setVisibility(View.INVISIBLE);
            this.invoiceCbox.setVisibility(View.INVISIBLE);
            this.mPayDepositBT.setVisibility(View.GONE);
            this.tradeAmountTV.setVisibility(View.GONE);
            this.eraseTxv.setVisibility(View.GONE);
            //刷新已支付信息
            updatePaidAmount(mPaymentInfo.getPaidPaymentItems());
            //获取押金的金额
            unReceivedTxv.setText(String.format(getString(R.string.pay_cash_text_deposit), CashInfoManager.formatCash(mPaymentInfo.getActualAmount())));
        } else {
            //获取未收的金额
            unReceivedTxv.setText(String.format(getString(R.string.pay_cash_text_unreceived), CashInfoManager.formatCash(mPaymentInfo.getActualAmount())));
            //获取应收的金额(包含押金)
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
            //刷新已支付信息
            updatePaidAmount(mPaymentInfo.getPaidPaymentItems());
            //刷新抹零金额
            updateExempt();
            //如果需要付押金,显示付押金按钮
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

    /**
     * 打开支付列表视图
     *
     * @param paymentItems 支付列表数据
     */
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

    //add v8.13 显示预订金
    private void showEarnestAmount() {
        if (mPaymentInfo.getTradeVo().getTradeEarnestMoneys() != null && mPaymentInfo.getTradeVo().getTradeEarnestMoneys().size() > 0) {
            tagLlyt.setVisibility(View.VISIBLE);
            mPaidListIcon.setVisibility(View.INVISIBLE);
            TradeEarnestMoney tradeEarnestMoney = mPaymentInfo.getTradeVo().getTradeEarnestMoneys().get(0);
            StringBuilder stringBuild = new StringBuilder();
            String textPayName = PaySettingCache.getPayModeNameByModeId(tradeEarnestMoney.getPayModeId());
            stringBuild.append(textPayName);
            //显示预付金标识
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

    /**
     * 刷新已收金额UI
     *
     * @param paymentItems 支付列表数据,(该数据请按时间先后倒序排列)
     */
    private void updatePaidAmount(List<PaymentItem> paymentItems) {
        if (paymentItems == null || paymentItems.isEmpty()) {
            //modify v8.13 显示预订金 start
            if (mPaymentInfo.getTradeVo().getTradeEarnestMoney() > 0) {
                showEarnestAmount();
            } else {
                tagLlyt.setVisibility(View.GONE);
            }
            //modify v8.13 显示预订金 end
            return;
        }
        tagLlyt.setVisibility(View.VISIBLE);
        //如果只有一条支付记录，显示具体支付方式
        if (paymentItems.size() == 1) {
            mPaidListIcon.setVisibility(View.INVISIBLE);
            PaymentItem paymentItem = paymentItems.get(0);
            StringBuilder stringBuild = new StringBuilder();
            String textPayName = TextUtils.isEmpty(paymentItem.getPayModeName()) ? PaySettingCache.getPayModeNameByModeId(paymentItem.getPayModeId()) : paymentItem.getPayModeName();
            stringBuild.append(textPayName);
            //显示押金标识
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
            double receivedAmount = mPaymentInfo.getPaidAmount();//获取已付金额
            receivedTxv.setText(CashInfoManager.formatCash(receivedAmount));
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
                this.notifyDataSetChange();//刷新支付信息
                //  EventBus.getDefault().post(new ExemptEventUpdate(mPaymentInfo.getEraseType()));
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }

    public void onEventMainThread(RefreshTradeVoEvent event) {
        mTradeVo = event.getTradeVo();
        mPaymentInfo.setTradeVo(mTradeVo);//要支付的订单
        updatePaymentInfo();
        EventBus.getDefault().post(new ExemptEventUpdate(mPaymentInfo.getEraseType()));
    }

    //-----------------------------by yanm[end]---------------------------------------------------------------
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        unregisterEventBus();
        ErasePopupHelper.getInstance().disposePopuWindow();// 清空数据
        super.onDestroy();
    }

    @Override
    public void lagOver(int payType) {
        //显示挂账成功结果
        EventBus.getDefault().post(new ActionCloseOrderDishActivity());
        DoPayUtils.showPayOkDialog(getActivity(), mDoPayApi, mPaymentInfo, false, 2);
    }

    //押金支付后刷新支付界面数据
    public void onEventMainThread(DepositPayOver depositPayOver) {
        if (mPaymentInfo.getTradeBusinessType() == BusinessType.BUFFET
                && mPaymentInfo.getPayScene() == PayScene.SCENE_CODE_SHOP) {
            if (depositPayOver.getPayResp() != null) {//押金支付成功触发
                if (!Utils.isEmpty(depositPayOver.getPayResp().getTrades())) {
                    this.mPaymentInfo.getTradeVo().setTrade(depositPayOver.getPayResp().getTrades().get(0));
                }
                if (!Utils.isEmpty(depositPayOver.getPayResp().getPaymentItems()))
                    this.mPaymentInfo.getTradeVo().setTradeDepositPaymentItem(depositPayOver.getPayResp().getPaymentItems().get(0));
                if (!Utils.isEmpty(depositPayOver.getPayResp().getTradeDepositPayRelations()))
                    this.mPaymentInfo.getTradeVo().setTradeDepositPayRelation(depositPayOver.getPayResp().getTradeDepositPayRelations().get(0));
                DoPayUtils.findPaymentVoAsync(this.mPaymentInfo, true);
                //先把原单会员拿出来，等reset方法玩了以后再放回去（临时方案，后续需要优化）
                CustomerResp customer = CustomerManager.getInstance().getDinnerLoginCustomer();
                DinnerShoppingCart.getInstance().updateDataFromTradeVo(this.mPaymentInfo.getTradeVo());
                //如果会员不为空
                if (customer != null) {
                    CustomerManager.getInstance().setDinnerLoginCustomer(customer);
                    //刷新积分抵现
                    DinnerCashManager cashManager = new DinnerCashManager();
                    if (customer.card == null) {
                        cashManager.updateIntegralCash(customer);
                    } else {
                        cashManager.updateIntegralCash(customer.card);
                    }
                }
                mPaymentInfo.setOrdered(true);// 标识已经下单
            }
        }
    }

    /**
     * 接收在线支付推送消息
     */
    public void onEventMainThread(PushPayRespEvent event) {
        try {
            //PLog.d(PLog.TAG_CALLPRINT_KEY, "info:MainPayFragment接收推送v3第三方支付:时间戳:" + System.currentTimeMillis() + ",TradeUuid:" + event.getTradeUuid() + ",position:" + TAG + "->onEventMainThread()");

            PayResp result = event.getPushPayMent().getContent();
            //如果是当前支付界面发起的支付，且在线支付对话框已经关闭  add 20170708
            if (!DoPayApi.OnlineDialogShowing && mPaymentInfo.getId().equals(mDoPayApi.getCurrentPaymentInfoId())) {
                doVerifyPayResp(result);// 处理收银结果
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //moidy  20180129 start 统一处理在线支付结果
    IOnlinePayOverCallback mOnlinePayOverCallback = new IOnlinePayOverCallback() {

        @Override
        public void onPayResult(Long paymentItemId, int payStatus) {
            //do nothing
        }
    };

    private void doVerifyPayResp(final PayResp result) {
        if (result == null) {
            return;
        }
        //add start 20170914 处理先轮询到支付结果，但是订单状态还没及时改到
        if (TextUtils.isEmpty(mDoPayApi.getOnlinePaymentItemUuid())) { //如果支付方式已经清空，比较订单，如果是同一个订单已完成，则处理支付结果
            Trade pushTrade = result.getTrades().get(0);
            if (pushTrade != null && mPaymentInfo.getTradeVo().getTrade().getUuid().equals(pushTrade.getUuid()) && DoPayUtils.isTradePaidOver(pushTrade)) {
                //如果还没有打单
                if (!mPaymentInfo.isPrintedOk()) {
                    mPaymentInfo.getTradeVo().setTrade(pushTrade);
                    onFinishedDoPrint(result);//打印结账单，
                    mPaymentInfo.doFinish();//关闭支付界面
                }
            }
        } else {
            //add end  20170914 处理先轮询但是订单未完成导致问题
            mDoPayApi.doVerifyPayResp(getActivity(), mPaymentInfo, result, mOnlinePayOverCallback);
        }
    }


    //moidy  20180129 end
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
            EventBus.getDefault().post(memberPayChargeEvent);//发送EVENTBUS到会员支付界面MemberPayFragment
        }
        //new GetCouponUrlThread().start();

        // 打印
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
            // 如果是订单中心的内用且是Android端下的订单，要打印厨房单
            if (mPaymentInfo.isOrderCenter() && trade.getDeliveryType() == DeliveryType.HERE
                    && trade.getSource() == SourceId.POS && trade.getSourceChild() == SourceChild.ANDROID) {
                mDoPayApi.doPrint(mPaymentInfo, mPaymentInfo.getTradeVo().getTrade().getUuid(), true, true, true, true);
                mPaymentInfo.setPrintedOk(true);
            } else {
                //金诚绑卡,换卡,金诚充值不打印
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
                    //换卡收银不打印,交预付金也不打印（add v8.14）
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

    //add v8.2 添加会员权益判断 start
    private void checkMemberPrice(TradeVo tradeVo) {
        //正餐自助餐暂不对接
        if (this.mPaymentInfo.getTradeBusinessType() == BusinessType.BUFFET) {
            return;
        }
        if (this.mCurrentMethod != null && PaySettingCache.isSetPayModeGroup(PayScene.SCENE_CODE_SHOP.value(), PayModelGroup.VALUE_CARD) && this.mCurrentMethod.methodId != IPaymentMenuType.PAY_MENU_TYPE_MEMBER) {
            if (isNeedToDealMemberPrice(tradeVo)) {
                //如果已经支付且不是储值支付，移除会员价
                if (mPaymentInfo.getPaidAmount() > 0 && !PayUtils.isMemberPay(mPaymentInfo.getPaidPaymentItems())) {
                    if (mPaymentInfo.isSplit()) {
                        SeparateShoppingCart.getInstance().removeAllMemberPrivileges();
                    } else {
                        DinnerShoppingCart.getInstance().removeAllMemberPrivileges();
                    }
                } else {
                    //如果没有支付过或者已经用过储值支付，跳转到储值支付
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

    //判断是否需要处理会员价
    private boolean isNeedToDealMemberPrice(TradeVo tradeVo) {
        //先判断会员\卡 是否已经登录,然后根据登录的身份区分开关
        if (tradeVo != null && getCurrentLoginCustomer() != null) {
            TradeCustomer tradeCustomer = PayUtils.getTradeCustomer(tradeVo);
            if (tradeCustomer != null) {
                return (CustomerManager.getInstance().isOpenPriceLimit(tradeCustomer.getCustomerType()) && PayUtils.isTradeWithMemberPrice(tradeVo));
            }
        }
        return false;
    }

    private CustomerResp getCurrentLoginCustomer() {
        // v8.10.0 快餐移出会员价格
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
                        // 移除会员价
                        try {
                            if (mPaymentInfo.isSplit()) {
                                SeparateShoppingCart.getInstance().removeAllMemberPrivileges();
                            } else {
                                DinnerShoppingCart.getInstance().removeAllMemberPrivileges();
                            }
                            // v8.10.0 快餐移出会员价格
                            if (!mPaymentInfo.isDinner()) {
                                ShoppingCart.getInstance().removeMemberPrivilege();
                                // 移除会员价后打开分步支付
                                mPaymentInfo.setIsGroupPay(true);
                                // 移出tradeVo中的会员价
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

    //add v8.2 添加会员权益判断 end
    public int getPayResult() {
        if (this.mPaymentInfo != null && this.mPaymentInfo.getTradeVo() != null && this.mPaymentInfo.getTradeVo().getTrade() != null && this.mPaymentInfo.getTradeVo().getTrade().getTradePayStatus() != null) {
            return this.mPaymentInfo.getTradeVo().getTrade().getTradePayStatus().value();
        }
        return 1;//未支付
    }

    @Override
    public void finishPay() {//add 20180123
        if (this.getActivity() != null) {
            this.getActivity().finish();
        }
    }
}
