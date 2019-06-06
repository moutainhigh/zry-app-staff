package com.zhongmei.bty.pay.fragment.memberpay;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.atask.SimpleAsyncTask;
import com.zhongmei.atask.TaskContext;
import com.zhongmei.bty.basemodule.auth.application.BeautyApplication;
import com.zhongmei.bty.basemodule.commonbusiness.cache.PaySettingCache;
import com.zhongmei.bty.basemodule.commonbusiness.cache.ServerSettingCache;
import com.zhongmei.bty.basemodule.commonbusiness.constants.SettingConstant;
import com.zhongmei.bty.basemodule.customer.dialog.PasswordDialog;
import com.zhongmei.bty.basemodule.customer.dialog.country.CountryDialog;
import com.zhongmei.bty.basemodule.customer.dialog.country.CountryGridAdapter;
import com.zhongmei.bty.basemodule.customer.entity.EcCardKindCommercialRel;
import com.zhongmei.bty.basemodule.customer.enums.CustomerLoginType;
import com.zhongmei.bty.basemodule.customer.enums.EntityCardCommercialType;
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.basemodule.customer.operates.CustomerOperates;
import com.zhongmei.bty.basemodule.customer.operates.interfaces.CustomerDal;
import com.zhongmei.bty.basemodule.database.entity.customer.CustomerV5;
import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCard;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardKind;
import com.zhongmei.bty.basemodule.devices.mispos.data.EctempAccount;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardBaseInfoResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardLoginResp;
import com.zhongmei.bty.basemodule.devices.mispos.enums.CardStatus;
import com.zhongmei.bty.basemodule.devices.mispos.enums.EntityCardType;
import com.zhongmei.bty.basemodule.devices.scaner.DeWoScanCode;
import com.zhongmei.bty.basemodule.devices.scaner.ScanCode;
import com.zhongmei.bty.basemodule.devices.scaner.ScanCodeManager;
import com.zhongmei.bty.basemodule.erp.bean.ErpCurrency;
import com.zhongmei.bty.basemodule.erp.operates.ErpCommercialRelationDal;
import com.zhongmei.bty.basemodule.pay.enums.PayScene;
import com.zhongmei.bty.basemodule.pay.event.RegisterDeWoCustomerLoginScanEvent;
import com.zhongmei.bty.basemodule.pay.message.PayResp;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.shoppingcart.SeparateShoppingCart;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager;
import com.zhongmei.bty.cashier.shoppingcart.ShoppingCart;
import com.zhongmei.bty.commonmodule.adapter.AbstractSpinerAdapter;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.customer.event.EventLoginView;
import com.zhongmei.bty.data.operates.impl.NewTradeOperatesImpl;
import com.zhongmei.bty.mobilepay.IOnlinePayOverCallback;
import com.zhongmei.bty.mobilepay.IPayConstParame;
import com.zhongmei.bty.mobilepay.ISavedCallback;
import com.zhongmei.bty.mobilepay.bean.IPaymentInfo;
import com.zhongmei.bty.mobilepay.bean.MemberLoginTypeData;
import com.zhongmei.bty.mobilepay.bean.PayModelItem;
import com.zhongmei.bty.mobilepay.bean.PaymentReqTool;
import com.zhongmei.bty.mobilepay.core.DoPayApi;
import com.zhongmei.bty.mobilepay.dialog.BookingDeductionRefundDialog;
import com.zhongmei.bty.mobilepay.dialog.PayDepositPromptDialog;
import com.zhongmei.bty.mobilepay.event.ExemptEventUpdate;
import com.zhongmei.bty.mobilepay.event.RefreshTradeVoEvent;
import com.zhongmei.bty.mobilepay.popup.MemberChooseTypePopWindow;
import com.zhongmei.bty.mobilepay.v1.event.MemberLoginEvent;
import com.zhongmei.bty.snack.offline.Snack;
import com.zhongmei.bty.snack.orderdish.CustomerSelectDialog;
import com.zhongmei.bty.snack.orderdish.data.SDConstant;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.bean.YFResponse;
import com.zhongmei.yunfu.bean.req.CustomerLoginResp;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.context.util.EncodingHandler;
import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.data.LoadingYFResponseListener;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.CustomerType;
import com.zhongmei.yunfu.db.enums.PayModeId;
import com.zhongmei.yunfu.db.enums.PayType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.net.RequestManager;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.EventResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.resp.YFResponseListener;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.util.EmptyUtils;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.util.ToastUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import de.greenrobot.event.EventBus;

import static com.zhongmei.bty.mobilepay.bean.MemberLoginTypeData.UI_TYPE_CARD;
import static com.zhongmei.bty.mobilepay.bean.MemberLoginTypeData.UI_TYPE_MOBILE;


@EFragment(R.layout.pay_member_login_fragment_layout)
public class MemberLoginFragment extends BasicFragment {

    private final String TAG = MemberLoginFragment.class.getSimpleName();

    private int barcodeWH = 180;// 微信二维码宽度

    private static final int WHAT_PAYSTATUS = 1;
    private static final int WHAT_SHOW_PAYSTATUS_BUTTON = 5;//显示获取支付状态按钮
   /* @ViewById(R.id.scan_barcode_input_et)
    EditText mScanInputET;//扫码输入框*/

    @ViewById(R.id.show_value)
    EditText show_value;//账号输入框

    @ViewById(R.id.customer_verification)
    Button customer_verification;

    @ViewById(R.id.pay_member_login_openscanner_button)
    TextView mOpenScannerBT;

    @ViewById(R.id.member_login_paycoder_button)
    TextView mFreshCodeBT;

    @ViewById(R.id.member_login_paycoder_label)
    TextView mScanCodeLBTV;//状态提示语

    @ViewById(R.id.pay_member_rlt)
    RelativeLayout mMemberRlt;

    @ViewById(R.id.iv_login_type)
    protected ImageView mIvLoginType;

    @ViewById(R.id.ll_login_type)
    LinearLayout mLlLoginType;

    @ViewById(R.id.rlAreaCode)
    LinearLayout mLlAreaCode;

    @ViewById(R.id.tvAreaCodes)
    TextView mTvCountry;

    private double mValueCardBalance = 0;// 会员余额

    private long mIntegralBalance;// 会员积分
    //add 20160714 start
    private IPaymentInfo mPaymentInfo;
    private DoPayApi mDoPayApi;//add v8.11
    private String mActualValue = "";//实际数据

    private String mDisplayValue = "";//显示的数据

    private boolean isShowAccount = true;

    private boolean isAutoLogin = false;//是否需要自动登录

    private boolean isDefaultShowCode = false;//false默认初始化就显示付款码

    private boolean isShow = false;

    //限制最大输入字符数
    private final int maxTextLength = 20;
    private ScanCodeManager mScanCodeManager;
    private long mCurrentPaymentItemId;
    private String mCurrentPaymentItemUuid = null;
    private Double mLastAmount;//当前金额
    private Bitmap bitmap = null;//当前二维码
    private String codeUrl = null;//当前二维码Url
    private DeWoScanCode mDeWoScanCode;
    private NewTradeOperatesImpl tradeOperates = OperatesFactory.create(NewTradeOperatesImpl.class);

    /**
     * 当前商户国籍
     */
    private ErpCurrency mErpCurrency;

    private Map<String, ErpCurrency> erpCurrencyMap;

    private ErpCommercialRelationDal mErpDal;

    private int uiType = UI_TYPE_MOBILE;

    MemberChooseTypePopWindow popWindow;

    List<MemberLoginTypeData> loginTypeDataList;

    public void setDefaultShowCode(boolean defaultShowCode) {
        isDefaultShowCode = defaultShowCode;
    }

    public void setCashInfoManager(IPaymentInfo cashInfoManager) {
        this.mPaymentInfo = cashInfoManager;
    }

    public void setDoPayApi(DoPayApi doPayApi) {
        this.mDoPayApi = doPayApi;
    }

    @Click({R.id.customer_verification, R.id.back, R.id.btn_card_login, R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven, R.id.eight, R.id.nine,
            R.id.zero, R.id.delete, R.id.clean, R.id.btn_face_login, R.id.pay_member_login_openscanner_button, R.id.member_login_paycoder_button,
            R.id.rlAreaCode, R.id.ll_login_type})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.customer_verification:
                if (!ClickManager.getInstance().isClicked()) {
                    // 收银台会员手机登录量
                    doLogin();//modif  v8.7
                }
                break;
            case R.id.back:
                DisplayServiceManager.doCancel(getActivity());
                isShowAccount = false;
                show_value.setText("");
                mActualValue = "";
                break;
            case R.id.btn_card_login:
                if (ClickManager.getInstance().isClicked()) {
                    return;
                }
                onCardLoginClick();
                break;
            case R.id.one:
                append("1");
                break;
            case R.id.two:
                append("2");
                break;
            case R.id.three:
                append("3");
                break;
            case R.id.four:
                append("4");
                break;
            case R.id.five:
                append("5");
                break;
            case R.id.six:
                append("6");
                break;
            case R.id.seven:
                append("7");
                break;
            case R.id.eight:
                append("8");
                break;
            case R.id.nine:
                append("9");
                break;
            case R.id.zero:
                append("0");
                break;
            case R.id.delete:
                deleteTheLast();
                break;
            case R.id.clean:
                clearText();
                break;
            case R.id.pay_member_login_openscanner_button:
                startScanner();//开启扫描枪
                ToastUtil.showShortToast(R.string.pay_member_scanner_have_open);
                break;
            case R.id.member_login_paycoder_button:
                // isDefaultShowCode = true;
                // 收银台会员被扫登录量
                MobclickAgentEvent.onEvent(getActivity(), SDConstant.MobClick.SNACK_PAY_MEMBER_LOGIN_SCANED);
                if (this.mPaymentInfo.isNeedToPayDeposit()) {
                    PayDepositPromptDialog.start(this.getActivity(), mDoPayApi, this.mPaymentInfo);
                } else if (this.isNeedToDeductionEarnest()) {
                    this.showBookingDeductionDialog();//先判断是否抵扣预付金 add 8.14
                } else {
                    if (this.mPaymentInfo.getActualAmount() == 0) {
                        ToastUtil.showShortToast(R.string.pay_zero_cannot_use);
                        return;
                    }
                    if (DensityUtil.isHaveMiniScreen(this.getActivity())) {
                        if (this.mPaymentInfo.isDinner() || this.mPaymentInfo.isBeauty()) {
                            VerifyHelper.verifyAlert(this.getActivity(), mPaymentInfo.getCashPermissionCode(),
                                    new VerifyHelper.Callback() {
                                        @Override
                                        public void onPositive(User user, String code, Auth.Filter filter) {
                                            super.onPositive(user, code, filter);
                                            getMemberPayCode();//生成付款码
                                        }
                                    });
                        } else {
                            getMemberPayCode();//生成付款码
                        }
                    }
                }
                break;
           /* case R.id.check_payresult_bt:
                checkPayResult();//checkout 查询支付结果
                break;*/
            case R.id.btn_face_login://add v8.5 人脸登录
                /*boolean available = BaiduFaceRecognition.getInstance().checkFaceServer();
                if (!available) {
                    FacecognitionActivity.showFaceServerWarmDialog(getContext(), getChildFragmentManager());
                    return;
                }
                // 收银台会员刷脸登录量
                MobclickAgentEvent.onEvent(getActivity(), SDConstant.MobClick.SNACK_PAY_MEMBER_LOGIN_FACE);
                startActivityForResult(BaiduFaceRecognition.getInstance().getRecognitionFaceIntent(true), FaceRequestCodeConstant.RC_PAY_LOGIN);*/
                break;
            case R.id.rlAreaCode:
                showCountryDialog(new ArrayList<>(erpCurrencyMap.values()), mErpCurrency);
                break;
            /*case R.id.ll_login_type:
                if(popWindow != null){
                    popWindow.refreshData(loginTypeDataList, uiType);
                    popWindow.showAsDropDown(mLlLoginType,-10,0);
                }
                break;*/
            default:
                break;
        }
    }

    /**
     * v8.16.0刷卡登录快捷登录提取方法
     */
    private void onCardLoginClick() {
        // 收银台会员刷卡登录量
        /*MobclickAgentEvent.onEvent(getActivity(), SDConstant.MobClick.SNACK_PAY_MEMBER_LOGIN_CARD);
        new ReadCardDialogFragment.UionCardDialogFragmentBuilder()
                .buildReadCardId(ReadCardDialogFragment.UionCardStaus.READ_CARD_ID_SINGLE,
                        new ReadCardDialogFragment.CardOvereCallback() {

                            @Override
                            public void onSuccess(ReadCardDialogFragment.UionCardStaus status, String number) {
                                getCardBaseInfo(number);
                            }

                            @Override
                            public void onFail(ReadCardDialogFragment.UionCardStaus status, String rejCodeExplain) {
                                if (!TextUtils.isEmpty(rejCodeExplain)) {
                                    ToastUtil.showShortToast(rejCodeExplain);
                                }
                            }

                        })
                .show(getFragmentManager(), "get_cardno");*/
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerEventBus();
    }

    @AfterViews
    protected void init() {
        mDoPayApi.setCurrentPaymentInfoId(mPaymentInfo.getId());///当前支付界面id add 20170708
        initAreaCode();
        isShow = true;
    }

    void initAreaCode() {
        mErpDal = OperatesFactory.create(ErpCommercialRelationDal.class);
        TaskContext.bindExecute(getActivity(), new SimpleAsyncTask<Map<String, ErpCurrency>>() {
            @Override
            public Map<String, ErpCurrency> doInBackground(Void... params) {
                if (erpCurrencyMap == null) {
                    erpCurrencyMap = new HashMap<>();
                }
                List<ErpCurrency> mErpCurrencyList = mErpDal.queryErpCurrenctList();
                if (Utils.isNotEmpty(mErpCurrencyList)) {
                    for (ErpCurrency currency : mErpCurrencyList) {
                        erpCurrencyMap.put(currency.getAreaCode(), currency);
                    }
                }
                return erpCurrencyMap;
            }

            @Override
            public void onPostExecute(Map<String, ErpCurrency> erpCurrencyMap) {
                if (erpCurrencyMap != null && ShopInfoCfg.getInstance().getCurrency() != null) {
                    String areaCode = ShopInfoCfg.getInstance().getCurrency().getAreaCode();
                    if (!TextUtils.isEmpty(areaCode)) {
                        mErpCurrency = erpCurrencyMap.get(areaCode);
                    }
                }
                setupCountryView();
                initView();

            }
        });
    }

    void setupCountryView() {
        if (mErpCurrency != null) {
            mTvCountry.setText(mErpCurrency.getCountryAreaCode());
        }
    }

    void initView() {
        if (ServerSettingCache.getInstance().isJinChBusiness()) {
            customer_verification.setVisibility(View.GONE);
            show_value.setVisibility(View.GONE);
        }
        mActualValue = "";//初始化实际值
        disPlayInput("");// modify v8.4
        initKeyBoard();
        initPopup();
        doAutoLogin();
        updateTradeAmount();//add v8.4
        updateFreshCodeBT();//add v8.4
        initJinCheng();
    }

    void initKeyBoard() {
        show_value.addTextChangedListener(mTextWatcher);
        show_value.setOnKeyListener(mShowValueETOnKeyListener);
    }

    void initPopup() {
        loginTypeDataList = new ArrayList<>();
        MemberLoginTypeData data = new MemberLoginTypeData();
        data.setUiType(UI_TYPE_MOBILE);
        data.setMemberTypeImgRes(R.drawable.selector_member_login_type_mobile);
        data.setMemberTypeTextRes(getString(R.string.member_pay_login_type_mobile));
        loginTypeDataList.add(data);
        data = new MemberLoginTypeData();
        data.setUiType(UI_TYPE_CARD);
        data.setMemberTypeImgRes(R.drawable.selector_member_login_type_card);
        data.setMemberTypeTextRes(getString(R.string.member_pay_login_type_card));
        loginTypeDataList.add(data);
        chooseLoginType();
        popWindow = new MemberChooseTypePopWindow(getContext());
        popWindow.setItemListener(new AbstractSpinerAdapter.IOnItemSelectListener() {
            @Override
            public void onItemClick(int pos) {
                MemberLoginTypeData loginTypeData = loginTypeDataList.get(pos);
                uiType = loginTypeData.getUiType();
                chooseLoginType();
            }
        });
        popWindow.setWidth(DensityUtil.dip2px(getContext(), 150));
    }

    public void chooseLoginType() {
        if (uiType == UI_TYPE_MOBILE) {
            mIvLoginType.setImageResource(R.drawable.member_login_type_mobile_selected);
            mLlAreaCode.setVisibility(View.GONE);
            show_value.setHint(R.string.customer_login_hint);
            show_value.setText("");
        } else if (uiType == UI_TYPE_CARD) {
            mIvLoginType.setImageResource(R.drawable.member_login_type_card_selected);
            mLlAreaCode.setVisibility(View.GONE);
            show_value.setHint(R.string.customer_ordercenter_search_hint0);
            show_value.setText("");
        }
    }

    void initJinCheng() {
        if (mPaymentInfo != null
                && Snack.isSnackBusiness(mPaymentInfo.getTradeBusinessType())
                && Snack.netWorkUnavailable()
                && Snack.isOfflineEnable()) {
            mMemberRlt.setVisibility(View.GONE);
        }
        if (mPaymentInfo != null) {
            final int quickPayType = mPaymentInfo.getQuickPayType();
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    View view = new View(getActivity());
                    if (quickPayType == IPayConstParame.MEMBER_SCAN_CARD_LOGIN) {
                        onCardLoginClick();
                    } else if (quickPayType == IPayConstParame.MEMBER_SCAN_FACE_LOGIN) {
                        view.setId(R.id.btn_face_login);
                    } else {
                        view = null;
                    }
                    if (view != null) {
                        onClick(view);
                    }
                }
            });
        }
    }

    //副屏显示空input//add v8.4
    private void disPlayInput(String input) {
        /*DisplayUserInfo dUserInfo =
                DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_ACCOUNT_INPUT, input, null, 0, true, 0);
        DisplayServiceManager.updateDisplay(getActivity(), dUserInfo);*/
    }

    /**
     * 显示国籍dialog
     *
     * @param erpCurrencyList
     * @param currentErpCurrency
     */
    public void showCountryDialog(List<ErpCurrency> erpCurrencyList, ErpCurrency currentErpCurrency) {
        if (erpCurrencyList == null || erpCurrencyList.size() == 0) {
            ToastUtil.showShortToast(getString(com.zhongmei.yunfu.mobilepay.R.string.pay_erpcurrency_enpty_hint));
            return;
        }
        CountryDialog dialog = new CountryDialog(getActivity(), erpCurrencyList, currentErpCurrency, new CountryGridAdapter.OnItemSelectedListener() {
            @Override
            public void onSelected(Long countryId, String name, ErpCurrency erpCurrency) {
                mErpCurrency = erpCurrency;
                setupCountryView();
            }
        });
        dialog.show();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        isShow = !hidden;
        if (!hidden) {
            disPlayInput("");
            startScanner();//开启扫描枪
            if (DensityUtil.isHaveMiniScreen(this.getActivity())) {//add v8.4
                if (this.bitmap != null && codeUrl != null) {
                    if (isAmountChange()) {//如果金额变动，手动生成二维码
                        //getMemberPayCode();//生成新的副屏二维码
                        updateTradeAmount();
                        this.bitmap = null;
                        updateFreshCodeBT();//add v8.4
                    } else {
                        updateMiniDiplayWithUrl(1, codeUrl, bitmap, true);
                    }
                }
            }
            //isDefaultShowCode = true;
        } else {
            stopScanner();
            stopGetPayStatus();
        }
    }

    @Override
    public void onDestroy() {
        unregisterEventBus();
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        stopScanner();  //关闭扫描枪 add v8.5.6
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        startScanner();//开启扫描枪 add v8.5.6
    }

    @Override
    public void onPause() {
        super.onPause();
        stopScanner();//关闭扫描枪 add v8.5.6
    }

    /**
     * 在末尾添加一个字符
     *
     * @param text
     */
    private void append(CharSequence text) {
        if (show_value.getText() != null) {
            show_value.setSelection(show_value.getText().length());
        }
        if (show_value.getText().length() >= maxTextLength) {
            return;
        }
        StringBuilder sb = new StringBuilder(mActualValue);
        sb.append(text);
        mActualValue = sb.toString();
        //mDisplayValue = Utils.useStarReplace(mActualValue, 3, 4);
        mDisplayValue = mActualValue;
        int index = show_value.getSelectionStart();
        show_value.getText().insert(index, text);
//        show_value.setText(mDisplayValue);
        Selection.setSelection(show_value.getText(), show_value.getText().length());
    }

    /**
     * 删除最后一个字符
     */
    private void deleteTheLast() {
        if (mActualValue.length() > 0) {
            StringBuilder sb = new StringBuilder(mActualValue);
            sb.deleteCharAt(sb.length() - 1);
            mActualValue = sb.toString();
            //mDisplayValue = Utils.useStarReplace(mActualValue, 3, 4);
            mDisplayValue = mActualValue;
            show_value.setText(mDisplayValue);
            Selection.setSelection(show_value.getText(), show_value.getText().length());
        }
    }

    /**
     * 清除文本
     */
    private void clearText() {
        mActualValue = "";
        //mDisplayValue = Utils.useStarReplace(mActualValue, 3, 4);
        mDisplayValue = mActualValue;
        show_value.setText(mDisplayValue);
        Selection.setSelection(show_value.getText(), show_value.getText().length());
    }

    //手机号输入框监听器1
    private View.OnKeyListener mShowValueETOnKeyListener = new View.OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if ((event.getAction() == KeyEvent.ACTION_DOWN)
                    && (keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER || keyCode == KeyEvent.KEYCODE_ENTER)) {
                doLogin();
                return true;
            }
            return false;
        }
    };

    //手机号输入框监听器2
    private TextWatcher mTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            //add  v8.7 start
            String inputStr = s.toString();
            if (inputStr.endsWith("\n") && !TextUtils.isEmpty(mActualValue)) {//处理回车事件
                doLogin();
                return;
            }
            //add  v8.7 end
            setButton();
            if (isShowAccount) {
                disPlayInput(s.toString());//modify v8.4
            } else {
                isShowAccount = true;
            }
        }
    };

    //add  v8.7  start
    private void doLogin() {
        // 收银台会员手机登录量
        MobclickAgentEvent.onEvent(getActivity(), SDConstant.MobClick.SNACK_PAY_MEMBER_LOGIN_PHONE);
        if (mPaymentInfo != null && mPaymentInfo.isDinner()) {
            VerifyHelper.verifyAlert(getActivity(), BeautyApplication.PERMISSION_CUSTOMER_LOGIN,
                    new VerifyHelper.Callback() {
                        @Override
                        public void onPositive(User user, String code, Auth.Filter filter) {
                            super.onPositive(user, code, filter);
                            verification(mActualValue, null, false);
                        }
                    });
        } else {
            verification(mActualValue, null, false);
        }
    }
    //add  v8.7 end
    //add v8.4 start

    /***
     * 监听抹零事件
     *
     * @param event
     */
    public void onEventMainThread(ExemptEventUpdate event) {
        if (this.isAdded() && this.isShow && DensityUtil.isHaveMiniScreen(this.getActivity())) {
            if (isAmountChange()) {//金额发生变化
                if (isDefaultShowCode || bitmap != null) {
                    // getMemberPayCode();//生成新的副屏二维码
                    //刷新副屏，显示输入框
                    disPlayInput(show_value.getText().toString());// modify v8.5
                    bitmap = null;
                    updateFreshCodeBT();
                }
                updateTradeAmount();
            }
        }
    }

    //正餐会员登录和退出
    public void onEventMainThread(EventLoginView event) {
        if (this.isAdded() && this.isShow && DensityUtil.isHaveMiniScreen(this.getActivity())) {
            if (event.getOperateType() == EventLoginView.TYPE_LOGIN_VIEW_START) {
                this.stopScanner();
            } else if (event.getOperateType() == EventLoginView.TYPE_LOGIN_VIEW_END) {
                this.disPlayInput("");
                this.startScanner();
                //如果已经生成过二维码，客显直接显示二维码
                if (this.bitmap != null && codeUrl != null) {
                    updateMiniDiplayWithUrl(1, codeUrl, bitmap, true);
                } else {
                    if (this.isDefaultShowCode)
                        getMemberPayCode();//生成新的副屏二维码
                }
            }
        }
    }

    private void updateFreshCodeBT() {
        //add v8.11 如果销账，不生成二维码支付
        if (mPaymentInfo.getPayScene() == PayScene.SCENE_CODE_WRITEOFF) {
            mFreshCodeBT.setVisibility(View.INVISIBLE);
            mScanCodeLBTV.setVisibility(View.INVISIBLE);
            return;
        }
        if (this.bitmap != null) {
            mFreshCodeBT.setText(R.string.pay_member_pay_code_refresh);
            mFreshCodeBT.setTextColor(getResources().getColor(R.color.color_F69227));
            mFreshCodeBT.setBackground(getResources().getDrawable(R.drawable.pay_member_label_bg));

            mScanCodeLBTV.setText(R.string.pay_member_pay_code_is_show);
        } else {
            mFreshCodeBT.setText(R.string.pay_member_pay_code_new);
            mFreshCodeBT.setTextColor(getResources().getColor(R.color.bg_white));
            mFreshCodeBT.setBackground(getResources().getDrawable(R.drawable.pay_member_label_button_bg));
            mScanCodeLBTV.setText(R.string.pay_member_pay_code_show_alter);
        }
    }


    //设置当前应付金额
    private void updateTradeAmount() {
        if (this.mPaymentInfo != null) {
            mLastAmount = this.mPaymentInfo.getActualAmount();
        }
    }

    private boolean isAmountChange() {
        if (this.mPaymentInfo != null && mLastAmount != null) {
            return !mLastAmount.equals(this.mPaymentInfo.getActualAmount());
        }
        return false;
    }

    /**
     * 开始扫码并获取焦点
     */
    private void startScanner() {
        stopScanner();
        mScanCodeManager = new ScanCodeManager(getActivity(), show_value, true);
        mScanCodeManager.start(mScanCodeReceivedListener);
        show_value.requestFocus();

        resisterDeWoScanCode();
    }

    public void onEventMainThread(RegisterDeWoCustomerLoginScanEvent event) {
        resisterDeWoScanCode();
    }

    private void resisterDeWoScanCode() {
        DeWoScanCode.getInstance().registerReceiveDataListener(new DeWoScanCode.OnReceiveDataListener() {
            @Override
            public void onReceiveData(String data) {
                loginByScanCode(data);
            }
        });
    }

    private void unRegisterDewoScan() {
        DeWoScanCode.getInstance().unRegisterReceiveDataListener();
    }

    private void stopScanner() {
        if (mScanCodeManager != null) {
            mScanCodeManager.stop();
            mScanCodeManager = null;
        }
        this.unRegisterDewoScan();
    }

    //扫码回调处理
    ScanCode.ScanCodeReceivedListener mScanCodeReceivedListener = new ScanCode.ScanCodeReceivedListener() {
        @Override
        public void onScanCodeReceived(String data) {
            loginByScanCode(data);
        }
    };

    //modify 20180205  start修改交互，扫码去掉了选择框，直接登录
    private void loginByScanCode(String data) {
        // 收银台会员主扫登录量
        MobclickAgentEvent.onEvent(getActivity(), SDConstant.MobClick.SNACK_PAY_MEMBER_LOGIN_SCAN);
        if (this.getActivity() != null && !this.getActivity().isDestroyed() && this.isAdded()) {
            if (!TextUtils.isEmpty(data)) {
                if (data.contains(":")) { //如果是token，包含有冒号,直接调用token登录接口，
                    loginByWeixin(data);
                } else {
                    verification(data, null, false);
                }
                show_value.setText("");
            }
        }
    }
    //modify 20180205  end修改交互，扫码去掉了选择框，直接登录
   /* ScanMemberCodeDialog.ScanMemberCodeListenner mCodeListenner = new ScanMemberCodeDialog.ScanMemberCodeListenner() {


        @Override
        public void doLogin(String code) {
            UserActionEvent.start(UserActionEvent.DINNER_PAY_LOGIN_SCAN_CODE);
            loginByWeixin(code);
        }

        @Override
        public void doPay(String code) {
            if (mPaymentInfo.isNeedToPayDeposit()) {
                PayDepositPromptDialog.start(getActivity(), mPaymentInfo);
            } else {
                doWeiXinCustomerScanPay(code);
            }
        }

        @Override
        public void onClose() {
            if (show_value != null)
                show_value.requestFocus();
        }
    };
*///modify 20180205 去掉没用的代码

    /**
     * @Description: 如果已经下单，直接获取url，如果没下单，先下单，再生成url
     */
    private void getMemberPayCode() {
        if (mPaymentInfo == null || mPaymentInfo.getTradeVo() == null) {
            return;
        }
        TradeVo tradeVo = mPaymentInfo.getTradeVo();
        // 如果已经下单
        if (mPaymentInfo.isOrdered()) {
            getMemberPayUrl(tradeVo.getTrade());

        } else {
            ISavedCallback savedCallback = new ISavedCallback() {
                @Override
                public void onSaved(boolean isOk) {//保存成功回调
                    try {
                        if (isOk) {
                            mPaymentInfo.setOrdered(true);
                            ToastUtil.showLongToast(R.string.pay_wechat_trade_insert_success);
                            //BakeryPayFactory.newInstance().toNotifyRefreshStatus();
                            ShoppingCart.getInstance().clearShoppingCart();
                            // 获取二维码
                            getMemberPayUrl(mPaymentInfo.getTradeVo().getTrade());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            /*if (!BakeryPayData.getInstance().isUnKnown()) {
                if (BakeryPayData.getInstance().getBakeryForm() == BakeryForm.PAY_ONLY) {
                    getMemberPayUrl(tradeVo.getTrade());
                } else {
                    BakeryPayFactory.newInstance().toSaveOrderForBakery(getActivity(), mPaymentInfo, savedCallback);
                }
            } else {*/
            mDoPayApi.saveTrade(this.getActivity(), mPaymentInfo, null, savedCallback);
            //}
        }
    }

    /**
     * 获取微信支付的url并生成二维码
     */

    private void getMemberPayUrl(Trade trade) {
        if (trade == null) {
            return;
        }
        mCurrentPaymentItemId = 0;
        mCurrentPaymentItemUuid = null;
        ResponseListener<PayResp> listener = new ResponseListener<PayResp>() {

            @Override
            public void onResponse(ResponseObject<PayResp> response) {
                try {
                    PayResp resp = response.getContent();
                    if (ResponseObject.isOk(response) && !Utils.isEmpty(resp.getPaymentItemResults())) {

                        mCurrentPaymentItemId = resp.getPaymentItemResults().get(0).getPaymentItemId();
                        mCurrentPaymentItemUuid = resp.getPaymentItemResults().get(0).getPaymentItemUuid();
                        mDoPayApi.setOnlinePaymentItemUuid(resp.getPaymentItemResults().get(0).getPaymentItemUuid());
                        Map addition = resp.getPaymentItemResults().get(0).getAddition();
                        //判断订单时间戳有没有更新 (生成二维码时间戳会更新)add v8.2
                        if (!Utils.isEmpty(resp.getTrades())) {
                            Trade trade = resp.getTrades().get(0);
                            if (trade.getServerUpdateTime() > mPaymentInfo.getTradeVo().getTrade().getServerUpdateTime()) {
                                mPaymentInfo.getTradeVo().setTrade(trade);
                                //刷新拆单购物车数据
                                if (mPaymentInfo.isSplit()) {
                                    SeparateShoppingCart.getInstance().updateDataWithTrade(trade);
                                } else {
                                    DinnerShoppingCart.getInstance().updateDataWithTrade(trade);
                                }
                            }
                        }
                        codeUrl = (String) addition.get("codeUrl");
                        // 生成副屏二维码
                        bitmap = EncodingHandler.createQRCode(codeUrl, barcodeWH);
                        // 客显显示二维码//
                        updateMiniDiplayWithUrl(1, codeUrl, bitmap, true);
                        // 开始轮训获取支付状态
                        sendGetPayStatusMessage();
                       /* //現在查询结果
                        sendShowCheckPayResultMS();*/

                        updateFreshCodeBT();
                    } else {
                        if (EmptyUtils.isNotEmpty(response.getMessage())) {
                            ToastUtil.showLongToast(response.getMessage());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
                try {
                    ToastUtil.showLongToast(error.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        PayModelItem payModelItem = new PayModelItem(PayModeId.MEMBER_CARD);//
        payModelItem.setUsedValue(BigDecimal.valueOf(this.mPaymentInfo.getActualAmount()));

        payModelItem.setUuid(SystemUtils.genOnlyIdentifier());

        payModelItem.setPayType(PayType.QCODE);
        mPaymentInfo.getOtherPay().clear();
        mPaymentInfo.getOtherPay().addPayModelItem(payModelItem);
        mDoPayApi.setOnlinePaymentItemUuid(payModelItem.getUuid());//add 20170525 默认当前支付的uuid(解决超时情况有推送结果但是没有出票问题)
        /*if (!BakeryPayData.getInstance().isUnKnown()) {
            // 烘焙单独收银（由于listener不公用，所以重新赋值单独支付）
            if (BakeryPayData.getInstance().getBakeryForm() != BakeryForm.PAY_ONLY) {
                PayForm form = new PayForm();
                form.serverUpdateTime = trade.getServerUpdateTime();
                form.uuid = trade.getUuid();
                form.id = trade.getId();
                BakeryPayData.getInstance().setBakeryForm(BakeryForm.PAY_ONLY).setPayForm(form);
            }
            BakeryPayData.getInstance().pay(mPaymentInfo, LoadingResponseListener.ensure(listener, getFragmentManager()));
        } else {*/
        tradeOperates.newpay(trade, new PaymentReqTool(mPaymentInfo).creatPaymentReq(), LoadingResponseListener.ensure(listener, getFragmentManager()));
        //}
    }

    private void updateMiniDiplayWithUrl(int payWay, String codeUrl, Bitmap bitmap, boolean isUseUrl) {
        /*try {
            DisplayBarcode dBarcode =
                    DisplayServiceManager.buildDBarcode(payWay,
                            String.valueOf(this.mPaymentInfo.getActualAmount()),
                            bitmap,
                            PayModeId.MEMBER_CARD.value());
            dBarcode.setCodeUrl(codeUrl);
            dBarcode.setUseUrl(isUseUrl);
            DisplayServiceManager.updateDisplay(getActivity().getApplicationContext(), dBarcode);
        } catch (Exception e) {
            // Log.e(TAG, "", e);
            e.printStackTrace();
        }*/
    }

    //轮训支付结果
    private void getPayStatusForS() {
        ResponseListener<PayResp> listener = new ResponseListener<PayResp>() {

            @Override
            public void onResponse(ResponseObject<PayResp> response) {
                try {
                    if (ResponseObject.isOk(response)) {
                        PayResp result = response.getContent();

                        mDoPayApi.doVerifyPayResp(getActivity(), mPaymentInfo, result, mOnlinePayOverCallback);// 处理收银结果
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sendGetPayStatusMessage();
            }

            @Override
            public void onError(VolleyError error) {
                sendGetPayStatusMessage();
            }
        };
        if (!TextUtils.isEmpty(mCurrentPaymentItemUuid) && mCurrentPaymentItemId != 0) {
            tradeOperates.getOnlinePayStatus(mPaymentInfo.getTradeVo(), mCurrentPaymentItemId, mCurrentPaymentItemUuid, listener);
        }
    }

    /**
     * 发送获取订单状态消息
     */
    private void sendGetPayStatusMessage() {
        Message message = new Message();
        message.what = WHAT_PAYSTATUS;
        mHandler.removeMessages(WHAT_PAYSTATUS);
        mHandler.sendMessageDelayed(message, 3000);
    }

    /**
     * 取消查询订单状态
     */
    private void stopGetPayStatus() {
        RequestManager.cancelAll("paystatus");// 移除网络请求
        if (mHandler != null) {
            mHandler.removeMessages(WHAT_PAYSTATUS);
        }
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_PAYSTATUS:
                    getPayStatusForS();
                    break;
               /* case WHAT_SHOW_PAYSTATUS_BUTTON:
                    showCheckPayResultBT();
                    break;*/
                default:
                    break;
            }
        }
    };
    // add v8.4 在线支付回调处理
    IOnlinePayOverCallback mOnlinePayOverCallback = new IOnlinePayOverCallback() {

        @Override
        public void onPayResult(Long paymentItemId, int payStatus) {
            if (TradePayStatus.PAID.value() == payStatus) {
                stopGetPayStatus();// 取消查询订单状态
                //BakeryPayFactory.newInstance().toNotifyRefreshAndPrint(mPaymentInfo);
            } else if (TradePayStatus.PAID_FAIL.value() == payStatus) {
                stopGetPayStatus();// 取消查询订单状态
                //BakeryPayFactory.newInstance().toNotifyRefreshStatus();
            } else {
                sendGetPayStatusMessage();
            }
        }
    };
    /*IPayOverCallback mPayOverCallback = new IPayOverCallback() {

        @Override
        public void onFinished(boolean isOk) {
            try {
                if (isOk) {

                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    };

    private void doWeiXinCustomerScanPay(String tokenJson) {
        if (!TextUtils.isEmpty(tokenJson) && tokenJson.contains(":") && tokenJson.length() > 5) {
            String tokens[] = tokenJson.split(":");
            if (tokens != null) {
                String customerId = tokens[0];
                String token = tokens[1];
                if (!TextUtils.isEmpty(customerId) && Utils.isNum(customerId) && !TextUtils.isEmpty(token)) {
                    UserActionEvent.start(UserActionEvent.DINNER_PAY_SETTLE_STORE);
                    PayModelItem payModelItem = new PayModelItem(PayModeId.MEMBER_CARD);//
                    payModelItem.setUsedValue(BigDecimal.valueOf(this.mPaymentInfo.getActualAmount()));
                    payModelItem.setPasswordType(PasswordType.TOKEN_CODE);
                    payModelItem.setPayType(null);
                    payModelItem.setUuid(SystemUtils.genOnlyIdentifier());
                    try {
                        this.mPaymentInfo.setCustomerId(Long.valueOf(customerId).longValue());
                    } catch (Exception e) {
                        Log.e(TAG, "", e);
                    }
                    this.mPaymentInfo.setMemberPassword(token);
                    this.mPaymentInfo.getOtherPay().clear();
                    this.mPaymentInfo.getOtherPay().addPayModelItem(payModelItem);//添加会员储值支付方式

                    DoPayManager.getInstance().doPay(this.getActivity(), this.mPaymentInfo, mPayOverCallback);
                } else {
                    ToastUtil.showShortToast(R.string.pay_data_format_not_ok);
                }
            }
        } else {
            ToastUtil.showShortToast(R.string.pay_data_format_not_ok);
        }
    }*///modify 20180205 去掉没用的代码
//add v8.4 end

    private void loginCard(String cardNo) {
        // 大客户雅座拦截
        /*if (KeyAt.getKeyAtType() == KeyAtType.YAZUO) {
            requestCardLogin(cardNo);
            return;
        }*/
        CustomerOperates operates = OperatesFactory.create(CustomerOperates.class);
        ResponseListener<CardLoginResp> listener = LoadingResponseListener.ensure(new CardResponseListener(), getFragmentManager());
        operates.cardLogin(cardNo, listener);
    }

    private class CardResponseListener implements ResponseListener<CardLoginResp> {
        @Override
        public void onResponse(ResponseObject<CardLoginResp> response) {
            try {
                if (ResponseObject.isOk(response)) {
                    CardLoginResp resp = response.getContent();
                    // 设置card的名称，从customer中获得
                    EcCard card = resp.getResult().getCardInstance();
                    if (card.getCardType() == EntityCardType.CUSTOMER_ENTITY_CARD) {
                        if (!PaySettingCache.isErpModeID(mPaymentInfo.getPayScene().value(), PayModeId.ENTITY_CARD.value())) {
                            // 如果商户没有配置实体卡支付就直接返回
                            ToastUtil.showShortToast(R.string.card_pay_not_set);
                            return;
                        }
                        // ToastUtil.showShortToast(R.string.no_right_login_customer_entity_card);放开登录功能
                        CustomerV5 customerv5 = resp.getResult().getCustomer();
                        card.setName(customerv5.getName());
                        card.setCardLevel(resp.getResult().getCardLevel());
                        card.setCardLevelSetting(resp.getResult().getCardLevelSetting());
                        card.setCardSettingDetails(resp.getResult().getCardSettingDetails());
                        card.setIntegralAccount(resp.getResult().getIntegralAccount());
                        card.setValueCardAccount(resp.getResult().getValueCardAccount());
                        card.setCardKind(resp.getResult().getCardKind());
                        card.setCustomer(customerv5);

                        // 余额
                        if (card.getValueCardAccount() != null
                                && card.getValueCardAccount().getRemainValue() != null) {
                            mValueCardBalance = card.getValueCardAccount().getRemainValue();
                        } else {
                            mValueCardBalance = 0;
                        }
                        if (card.getIntegralAccount() != null && card.getIntegralAccount().getIntegral() != null) {
                            // 积分
                            mIntegralBalance = card.getIntegralAccount().getIntegral();
                        } else {
                            mIntegralBalance = 0;
                        }

                        mPaymentInfo.setEcCard(card);
                        mPaymentInfo.setCustomer(null);
                        CustomerResp cardCustomer = resp.getCustomer();
                        cardCustomer.card = null;
                        mPaymentInfo.setCardCustomer(cardCustomer);
                        mPaymentInfo.setMemberCardBalance(mValueCardBalance);
                        mPaymentInfo.setMemberIntegral(mIntegralBalance);

                        ToastUtil.showShortToast(R.string.card_login_success);
                        if (mSelectDialog != null)
                            mSelectDialog.dismiss();

                        setLoginCustomer(resp.getCustomer());
                        postLoginSuccess();

                    } else if (card.getCardType() == EntityCardType.ANONYMOUS_ENTITY_CARD) {

                        /*
                        if (mPaymentInfo.isDinner()) {
                            ToastUtil.showShortToast(R.string.the_card_is_not_member_card);
                            return;
                        }
                        */

                        EcCardKind ecCardKind = resp.getResult().getCardKind();
                        if (ecCardKind != null && !isConsumeShop(ecCardKind.getId())) {
                            ToastUtil.showShortToast(R.string.ananonymous_card_not_consume_shop);
                            return;
                        }

                        EctempAccount ectempAccount = resp.getResult().getTempAccount();
                        if (ectempAccount != null && ectempAccount.getRemainValue() != null) {
                            mValueCardBalance = ectempAccount.getRemainValue();
                        } else {
                            mValueCardBalance = 0;
                        }
                        mIntegralBalance = 0;// 临时卡没有积分。

                        card.setEctempAccount(ectempAccount);
                        card.setName(getString(R.string.anonymous_entity));
                        card.setCardLevel(resp.getResult().getCardLevel());
                        card.setCardLevelSetting(resp.getResult().getCardLevelSetting());
                        card.setCardSettingDetails(resp.getResult().getCardSettingDetails());
                        card.setIntegralAccount(resp.getResult().getIntegralAccount());
                        card.setValueCardAccount(resp.getResult().getValueCardAccount());
                        card.setCardKind(resp.getResult().getCardKind());
                        card.setCustomer(null);

                        mPaymentInfo.setEcCard(card);
                        mPaymentInfo.setCustomer(null);
                        mPaymentInfo.setMemberCardBalance(mValueCardBalance);
                        mPaymentInfo.setMemberIntegral(mIntegralBalance);

                        ToastUtil.showShortToast(R.string.ananonymous_card_login);
                        setLoginCustomer(resp.getCustomer());
                        postLoginSuccess();
                        if (mSelectDialog != null)
                            mSelectDialog.dismiss();
                    } else {
                        ToastUtil.showShortToast(R.string.unknow_card_type);
                    }

                } else {
                    String message = response.getMessage();
                    ToastUtil.showShortToast(message);
                }
            } catch (Exception e) {
                Log.e(TAG, "" + e);
            }
        }

        @Override
        public void onError(VolleyError error) {
            ToastUtil.showShortToast(error.getMessage());

        }
    }

    /**
     * 判断当前门店是否消费门店
     *
     * @param cardKindId
     * @return
     */
    private boolean isConsumeShop(Long cardKindId) {
        try {
            CustomerDal customerDal = OperatesFactory.create(CustomerDal.class);
            List<EcCardKindCommercialRel> ecCardKindCommercialRels = customerDal.findEcCardKindCommercialRel(cardKindId);
            for (EcCardKindCommercialRel ecCardKindCommercialRel : ecCardKindCommercialRels) {
                if (ecCardKindCommercialRel.getCommercialType() == EntityCardCommercialType.CONSUME_SHOP) {
                    return true;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return false;
    }

    /**
     * 会员登录
     *
     * @param mobile 手机号
     */
    private void verification(final String mobile, final Long customerId, boolean auto) {
        if (customerId == null) {
            if (TextUtils.isEmpty(mobile)) {
                ToastUtil.showShortToast(uiType == UI_TYPE_MOBILE ? R.string.customer_login_hint : R.string.customer_ordercenter_search_hint0);
                return;
            }
        }
        loginByPhoneNo(mobile, customerId, auto);
    }

    /**
     * 使用手机号登录,默认不需要密码验证
     *
     * @Title: loginByPhoneNo
     * @Return void 返回类型
     */
    private void loginByPhoneNo(final String inputNo, final Long customerId, boolean auto) {
        if (auto) {
            loginByPhoneNo(inputNo, customerId, 2, null, null);
        } else {
            if (uiType == UI_TYPE_MOBILE) {
                if (customerId == null && !TextUtils.isEmpty(inputNo) && mErpCurrency != null && !TextUtils.isEmpty(mErpCurrency.getPhoneRegulation()) && !Pattern.matches(mErpCurrency.getPhoneRegulation(), inputNo)) {
                    ToastUtil.showShortToast(getString(R.string.customer_mobile_regulation_error));
                    return;
                }
                loginByPhoneNo(inputNo, customerId, 2, null, null);
            } else {
                getLoginInfoByInput(inputNo);//一个接口一个接口尝试
            }
        }
    }

    private CustomerSelectDialog mSelectDialog = null;

    //将输入当成卡号去查询 查询不到卡信息时，将输入当成手机号去查询
    private void getLoginInfoByInput(final String inputNo) {
        CustomerOperates operates = OperatesFactory.create(CustomerOperates.class);
        ResponseListener<CardBaseInfoResp> listener =
                LoadingResponseListener.ensure(new EventResponseListener<CardBaseInfoResp>() {

                                                   @Override
                                                   public void onResponse(ResponseObject<CardBaseInfoResp> response) {
                                                       if (ResponseObject.isOk(response)) {
                                                           CardBaseInfoResp resp = response.getContent();
                                                           CardBaseInfoResp.BaseCardInfo baseCardInfo = resp.getResult();
                                                           if (baseCardInfo != null) {
                                                               if (baseCardInfo.getCardStatus() == CardStatus.ACTIVATED) {
                                                                   if (baseCardInfo.getCardType() == EntityCardType.GENERAL_CUSTOMER_CARD) {
                                                                       if (baseCardInfo.getCustomerId() != null
                                                                               && !TextUtils.isEmpty(baseCardInfo.getCustomerId().toString())) {
                                                                           loginByCustomerId(baseCardInfo.getCustomerId(), true);
                                                                       } else {
                                                                           //do nothing
                                                                       }
                                                                   } else {
                                                                       loginCard(baseCardInfo.getCardNum());
                                                                   }
                                                               } else {
                                                                   String tips = CustomerManager.getInstance().getStatusName(baseCardInfo.getCardStatus());
                                                                   ToastUtil.showShortToast(getString(R.string.card_error_str, tips));
                                                               }
                                                           } else {
                                                               ToastUtil.showShortToast(getString(R.string.no_card_info));
                                                           }
                                                       } else {
                                                           ToastUtil.showShortToast(response.getMessage());
                                                       }
                                                   }

                                                   @Override
                                                   public void onError(VolleyError error) {
                                                       ToastUtil.showShortToast(error.getMessage());
                                                   }
                                               },
                        getFragmentManager());
        operates.getCardBaseInfo(inputNo, listener);
    }

   /* private void checkMemeber(final String inputNo) {
        ResponseListener<MemberLoginVoResp> listener = new ResponseListener<MemberLoginVoResp>() {
            @Override
            public void onResponse(ResponseObject<MemberLoginVoResp> response) {
                try {
                    if (ResponseObject.isOk(response) && MemberLoginVoResp.isOk(response.getContent())) {
                        MemberLoginResp resp = response.getContent().getResult();
                        if (resp.customerIsDisable()) {//当前账号冻结
                            ToastUtil.showShortToast(R.string.order_dish_member_disabled);
                            return;
                        }
                        if (!resp.getCustomer().isMember()) {
                            ToastUtil.showShortToast(R.string.customer_not_member);
                            return;
                        }
                        List<CustomerInfoResp.Card> cards = resp.getCardList();

                        //权益卡
                        List<CustomerInfoResp.Card> temp = new ArrayList<CustomerInfoResp.Card>();
                        if (!Utils.isEmpty(cards)) {
                            for (CustomerInfoResp.Card card : cards) {
                                if (card.getCardType() != EntityCardType.GENERAL_CUSTOMER_CARD) {
                                    temp.add(card);
                                }
                            }
                        }
                        if (Utils.isEmpty(temp)) {
                            //虚拟账号登录
                            loginByPhoneNo(inputNo, null, 2, null, null);
                            return;
                        }
                        if (resp.getIsDisable() == 1 && temp.size() == 1) {
                            loginCard(temp.get(0).getCardNum());
                            return;
                        }

                        List<String> accounts = new ArrayList<>();
                        if (resp.getIsDisable() != 1) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("name", getString(R.string.customer_login_virtual_card));
                            jsonObject.put("type", 1);
                            jsonObject.put("num", inputNo);
                            accounts.add(jsonObject.toString());
                        }
                        for (CustomerInfoResp.Card card : cards) {
                            if (card.getCardStatus() == CardStatus.ACTIVATED
                                    && card.getCardType() != EntityCardType.GENERAL_CUSTOMER_CARD) {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("mobile", resp.getMobile());
                                jsonObject.put("name", card.getCardKindName());
                                jsonObject.put("type", 2);
                                jsonObject.put("num", card.getCardNum());
                                accounts.add(jsonObject.toString());
                            }
                        }

                        //setLoginCustomer(resp.getCustomer());
                        CustomerManager.getInstance().setAccounts(accounts);
                        if (mSelectDialog == null) {
                            mSelectDialog = new CustomerSelectDialog();
                            mSelectDialog.setResponseListener(new PhoneResponseListener(null), new CardResponseListener());
                        }
                        if (!mSelectDialog.isAdded())
                            mSelectDialog.show(getFragmentManager(), "CustomerSelectDialog");
                    } else {
                        String msg;
                        if (response.getContent() != null) {
                            msg = response.getContent().getErrorMessage();
                        } else {
                            msg = getString(R.string.display_login_error);
                        }
                        ToastUtil.showLongToast(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "", e);
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showLongToast(error.getMessage());
            }
        };
        if (getFragmentManager() != null)
            CustomerManager.getInstance().customerLogin(CustomerLoginType.MOBILE, inputNo, null, false, false, true, LoadingResponseListener.ensure(listener, getFragmentManager()));
    }*///modify 20180205 去掉没用的代码

    /**
     * 使用手机号登录
     *
     * @Title: loginByPhoneNo
     * @Return void 返回类型
     */
    private void loginByPhoneNo(final String inputNo, final Long customerId, final int needPswd, final String pswd, final PasswordDialog dialog) {
        // 大客户雅座拦截
        /*if (KeyAt.getKeyAtType() == KeyAtType.YAZUO) {
            requestCheckMember(inputNo, String.valueOf(customerId));
            return;
        }*/
        PhoneResponseListener listener = new PhoneResponseListener(dialog);
        CustomerManager customerManager = CustomerManager.getInstance();
        if (customerId != null && customerId != 0) {
            customerManager.customerLogin(CustomerLoginType.MEMBER_ID, customerId.toString(), pswd, needPswd == 1, false, true, LoadingYFResponseListener.ensure(listener, getFragmentManager()));
        } else if (!TextUtils.isEmpty(inputNo)) {
//            customerManager.customerLogin(CustomerLoginType.MOBILE, inputNo, pswd, needPswd == 1, false, true, LoadingResponseListener.ensure(listener, getFragmentManager()));
            String telCode = mErpCurrency != null && mErpCurrency.getAreaCode() != null ? mErpCurrency.getAreaCode() : null;
            String country = mErpCurrency != null && mErpCurrency.getCountryZh() != null ? mErpCurrency.getCountryZh() : null;
            String nation = mErpCurrency != null && mErpCurrency.getCountryEn() != null ? mErpCurrency.getCountryEn() : null;
            CustomerManager.getInstance().customerLogin(CustomerLoginType.MOBILE, inputNo, pswd, needPswd == 1, false, true,
                    LoadingYFResponseListener.ensure(listener, getFragmentManager()));
        }
    }


    //使用手机号登录成功处理
    /*private void loginByPhoneNoSuccess(CustomerResp customer) {
        if (customer != null) {
            if (!customer.isMember()) {
                ToastUtil.showShortToast(R.string.customer_not_member);
                return;
            }
            setLoginCustomer(customer);
            if (mPaymentInfo.getTradeBusinessType() == BusinessType.SNACK && mPaymentInfo.getTradeVo().getTrade().getTradePayStatus() != TradePayStatus.PAYING
                    && mPaymentInfo.getTradeVo() != null
                    && mPaymentInfo.getTradeVo().getTradeExtra() != null
                    && TextUtils.isEmpty(mPaymentInfo.getTradeVo().getTradeExtra().getOpenIdenty())) {//快餐并且没有openIdenty
                if (!TextUtils.isEmpty(customer.openId)) {//顾客有openId
                    mPaymentInfo.getTradeVo().getTradeExtra().setChanged(true);
                    mPaymentInfo.getTradeVo().getTradeExtra().setOpenIdenty(customer.openId);
                }
            }
            mValueCardBalance = customer.remainValue;
            if (customer.integral == null) {
                mIntegralBalance = 0;
            } else {
                mIntegralBalance = customer.integral;
            }

            mPaymentInfo.setEcCard(null);
            mPaymentInfo.setCustomer(customer);
            mPaymentInfo.setMemberCardBalance(mValueCardBalance);
            mPaymentInfo.setMemberIntegral(mIntegralBalance);
            if (mSelectDialog != null)
                mSelectDialog.dismiss();
            //这行需在后面
            postLoginSuccess();
        }
    }*/

    /**
     * 使用手机号登录
     * 1.只有手机号
     * 2.手机号下有卡号（弹出选择框选择使用什么登录）
     *
     * @Title: loginByPhoneNo
     * @Return void 返回类型
     */
    /*private void loginByPhoneNo(final String inputNo) {
        ResponseListener<MemberLoginVoResp> listener = new ResponseListener<MemberLoginVoResp>() {
            @Override
            public void onResponse(ResponseObject<MemberLoginVoResp> response) {
                if (ResponseObject.isOk(response) && MemberLoginVoResp.isOk(response.getContent())) {
                    final CustomerLoginResp resp = response.getContent().getResult();
                    final List<CustomerInfoResp.Card> cards = resp.getCardList();
                    if (resp.customerIsDisable() && cards == null) {//当前账号冻结
                        ToastUtil.showShortToast(R.string.order_dish_member_disabled);
                        return;
                    } else if (resp.customerIsDisable()) {
                        if (cards != null && cards.size() > 0) {
                            List<String> accounts = new ArrayList<>();
                            for (CustomerInfoResp.Card card : cards) {
                                if (card.getCardStatus() == CardStatus.ACTIVATED
                                        && card.getCardType() != EntityCardType.GENERAL_CUSTOMER_CARD) {
                                    try {
                                        JSONObject jsonObject = new JSONObject();
                                        jsonObject.put("mobile", resp.getMobile());
                                        jsonObject.put("name", card.getCardKindName());
                                        jsonObject.put("type", 2);
                                        jsonObject.put("num", card.getCardNum());
                                        accounts.add(jsonObject.toString());
                                    } catch (Exception e) {
                                        Log.e(TAG, "", e);
                                    }
                                }
                            }
                            if (accounts.size() > 1) {
                                CustomerManager.getInstance().setAccounts(accounts);
                                if (mSelectDialog == null) {
                                    mSelectDialog = new CustomerSelectDialog();
                                    mSelectDialog.setResponseListener(new PhoneResponseListener(null), new CardResponseListener());
                                }
                                if (!mSelectDialog.isAdded())
                                    mSelectDialog.show(getFragmentManager(), "CustomerSelectDialog");
                            } else if (accounts.size() == 1) {
                                for (CustomerInfoResp.Card card : cards) {
                                    if (card.getCardStatus() == CardStatus.ACTIVATED) {
                                        loginCard(card.getCardNum());
                                        break;
                                    }
                                }
                            } else {
                                ToastUtil.showShortToast(R.string.all_account_disable);
                            }
                            return;
                        }
                    }
                    new AsyncTask<Void, Void, CustomerResp>() {

                        @Override
                        protected CustomerResp doInBackground(Void... params) {
                            CustomerResp customerNew = resp.getCustomer();
                            customerNew.queryLevelRightInfos();
                            return customerNew;
                        }

                        @Override
                        protected void onPostExecute(CustomerResp customer) {
                            super.onPostExecute(customer);
                            customer.integral = resp.getIntegral() == null ? 0 : resp.getIntegral();
                            if (cards != null && cards.size() > 0) {
                                List<String> accounts = new ArrayList<>();
                                try {
                                    if (!resp.customerIsDisable()) {
                                        JSONObject jsonObject = new JSONObject();
                                        jsonObject.put("name", getString(R.string.customer_login_virtual_card));
                                        jsonObject.put("type", 1);
                                        jsonObject.put("num", customer.mobile);
                                        accounts.add(jsonObject.toString());
                                    }
                                    for (CustomerInfoResp.Card card : cards) {
                                        if (card.getCardType() != EntityCardType.GENERAL_CUSTOMER_CARD) {
                                            JSONObject jsonObject = new JSONObject();
                                            jsonObject.put("mobile", resp.getMobile());
                                            jsonObject.put("name", card.getCardKindName());
                                            jsonObject.put("type", 2);
                                            jsonObject.put("num", card.getCardNum());
                                            accounts.add(jsonObject.toString());
                                        }
                                    }
                                    if (accounts.size() > 1) {
                                        CustomerManager.getInstance().setAccounts(accounts);
                                        if (mSelectDialog == null) {
                                            mSelectDialog = new CustomerSelectDialog();
                                            mSelectDialog.setResponseListener(new PhoneResponseListener(null), new CardResponseListener());
                                        }
                                        if (!mSelectDialog.isAdded()) {
                                            mSelectDialog.show(getFragmentManager(), "CustomerSelectDialog");
                                        }
                                        return;
                                    }
                                } catch (Exception e) {
                                    Log.e(TAG, "", e);
                                }
                            }
                            loginByPhoneNoSuccess(customer);
                        }
                    }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    String msg;
                    if (response.getContent() != null) {
                        msg = response.getContent().getErrorMessage();
                    } else {
                        msg = getString(R.string.display_login_error);
                    }
                    ToastUtil.showLongToast(msg);
                    EventBus.getDefault().post(new EventReadKeyboard(false, msg));// 发送失败到ReadKeyboardDialogFragment
                }

            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showLongToast(error.getMessage());
                EventBus.getDefault().post(new EventReadKeyboard(false, error.getMessage()));// 发送失败到ReadKeyboardDialogFragment
            }
        };

        CustomerManager customerManager = CustomerManager.getInstance();

//        customerManager.customerLogin(CustomerLoginType.MOBILE, inputNo, null, false, false, true, LoadingResponseListener.ensure(listener, getFragmentManager()));
        String telCode = mErpCurrency != null && mErpCurrency.getAreaCode() != null ? mErpCurrency.getAreaCode() : null;
        String country = mErpCurrency != null && mErpCurrency.getCountryZh() != null ? mErpCurrency.getCountryZh() : null;
        String nation = mErpCurrency != null && mErpCurrency.getCountryEn() != null ? mErpCurrency.getCountryEn() : null;
        CustomerManager.getInstance().customerLoginByMobile(inputNo, telCode, country, nation, null, false, false, true,
                LoadingYFResponseListener.ensure(listener, getFragmentManager()));
    }*/
    private void loginByCustomerId(Long customerId, boolean isloginByCard) {
        PhoneResponseListener listener = new PhoneResponseListener(null, isloginByCard);
        CustomerManager customerManager = CustomerManager.getInstance();
        customerManager.customerLogin(CustomerLoginType.MEMBER_ID, customerId.toString(), null, false, false, true, LoadingYFResponseListener.ensure(listener, getFragmentManager()));
    }

    /**
     * 微信登录
     *
     * @param number
     */
    private void loginByWeixin(String number) {
        YFResponseListener<YFResponse<CustomerLoginResp>> listener = new PhoneResponseListener(null).setCustomerLoginType(CustomerLoginType.WECHAT_MEMBERCARD_ID);
        CustomerManager customerManager = CustomerManager.getInstance();
        customerManager.customerLogin(CustomerLoginType.WECHAT_MEMBERCARD_ID, number, null, false, false, true, LoadingYFResponseListener.ensure(listener, getFragmentManager()));
    }


    private class PhoneResponseListener implements YFResponseListener<YFResponse<CustomerLoginResp>> {
        private PasswordDialog dialog;
        private boolean isLoginByCard;

        public PhoneResponseListener setCustomerLoginType(CustomerLoginType customerLoginType) {
            this.customerLoginType = customerLoginType;
            return this;
        }

        private CustomerLoginType customerLoginType;//add v8.5

        public PhoneResponseListener(PasswordDialog dialog) {
            this.dialog = dialog;
        }

        public PhoneResponseListener(PasswordDialog dialog, boolean isCardLogin) {
            this.dialog = dialog;
            this.isLoginByCard = isCardLogin;
        }

        @Override
        public void onResponse(YFResponse<CustomerLoginResp> response) {
            if (getActivity() == null || getActivity().isDestroyed()) {
                return;
            }
            try {
                if (YFResponse.isOk(response)) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    final CustomerLoginResp resp = response.getContent();
                    if (resp.customerIsDisable()) {//当前账号冻结
                        ToastUtil.showShortToast(R.string.order_dish_member_disabled);
                        return;
                    }

                    CustomerResp customer = resp.getCustomer();
                    if (customer != null) {
                        if (this.customerLoginType != null) {
                            customer.customerLoginType = this.customerLoginType;
                        }
                        //如果结算时会员已经登录，判断是不是人脸登录,如果是要标记
                        if (customer.customerId != null && DinnerShopManager.getInstance().getLoginCustomer() != null && customer.customerId.equals(DinnerShopManager.getInstance().getLoginCustomer().customerId)) {
                            if (DinnerShopManager.getInstance().getLoginCustomer().customerLoginType == CustomerLoginType.FACE_CODE) {
                                customer.customerLoginType = CustomerLoginType.FACE_CODE;
                            }
                        }
                        customer.isLoginByCard = isLoginByCard;
                        if (!customer.isMember()) {
                            ToastUtil.showShortToast(R.string.customer_not_member);
                            return;
                        }
                        setLoginCustomer(customer);
                        //customer.queryLevelRightInfos();
                        if (mPaymentInfo.getTradeBusinessType() == BusinessType.SNACK && mPaymentInfo.getTradeVo().getTrade().getTradePayStatus() != TradePayStatus.PAYING
                                && mPaymentInfo.getTradeVo() != null
                                && mPaymentInfo.getTradeVo().getTradeExtra() != null
                                && TextUtils.isEmpty(mPaymentInfo.getTradeVo().getTradeExtra().getOpenIdenty())) {//快餐并且没有openIdenty
                            if (!TextUtils.isEmpty(customer.openId)) {//顾客有openId
                                mPaymentInfo.setOrdered(false);
                                mPaymentInfo.getTradeVo().getTradeExtra().setChanged(true);
                                mPaymentInfo.getTradeVo().getTradeExtra().setOpenIdenty(customer.openId);
                            }
                        }
                        mValueCardBalance = customer.remainValue;
                        if (customer.integral == null) {
                            mIntegralBalance = 0;
                        } else {
                            mIntegralBalance = customer.integral;
                        }

                        mPaymentInfo.setEcCard(null);
                        mPaymentInfo.setCustomer(customer);
                        mPaymentInfo.setMemberCardBalance(mValueCardBalance);
                        mPaymentInfo.setMemberIntegral(mIntegralBalance);
                        if (mSelectDialog != null)
                            mSelectDialog.dismiss();
                        //这行需在后面
                        postLoginSuccess();
                    }
                } else {

                    String msg;
                    if (response.getContent() != null) {
                        msg = response.getMessage();
                    } else {
                        msg = getString(R.string.display_login_error);
                    }
                    ToastUtil.showLongToast(msg);
                }
            } catch (Exception e) {
                Log.e(TAG, "" + e);
            }
        }

        @Override
        public void onError(VolleyError error) {
            try {
                ToastUtil.showShortToast(error.getMessage());
            } catch (Exception e) {
                Log.e(TAG, "" + e);
            }
        }
    }

    public static Animation shakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(0, 15, 0, 0);
        // 设置一个循环加速器，使用传入的次数就会出现摆动的效果。
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(500);

        return translateAnimation;
    }


    private void setButton() {
        if (customer_verification != null) {
            if (!TextUtils.isEmpty(show_value.getText())) {
                customer_verification.setEnabled(true);
                customer_verification.setTextColor(getActivity().getResources().getColor(R.color.text_white));
                customer_verification.setBackgroundResource(R.drawable.btn_blue_selector);
//                if (show_value.getTag().equals(CustomerLogin.CUSTOMERLOGIN)) {
//                    show_value.setBackgroundResource(R.drawable.customer_edit_bg);
//                    show_value.setHint(R.string.customer_login_hint);
//                }
                mActualValue = show_value.getText().toString().replace("\n", "").trim();//add 20170223 外接键盘输入
            } else {
                customer_verification.setEnabled(false);
                customer_verification.setTextColor(getActivity().getResources().getColor(R.color.text_black));
                customer_verification.setBackgroundResource(R.drawable.pay_dopay_btn_bg);
            }
        }
    }

    /**
     * 发送替换会员登录界面的成功的EventBus
     */
    public void postLoginSuccess() {
        EventBus.getDefault().post(new MemberLoginEvent(false));
    }


    /**
     * 自动登录
     */
    private void doAutoLogin() {
        if (mPaymentInfo == null) {
            return;
        }
        CustomerResp mCustomer = mPaymentInfo.getCustomer();//mCustomer和mecCard不能并存
        EcCard mecCard = mPaymentInfo.getEcCard();//mCustomer和mecCard不能并存
        TradeCustomer memberCustomer = null;
        TradeCustomer memberCustomerCard = null;
        if (mecCard == null && mCustomer == null && mPaymentInfo.getTradeVo() != null) {
            // 没有登录会员,看看单据里面是否有会员
            List<TradeCustomer> tradeCustomerList = mPaymentInfo.getTradeVo().getTradeCustomerList();

            if (tradeCustomerList != null && tradeCustomerList.size() > 0) {
                for (TradeCustomer tadeCustomer : tradeCustomerList) {
                    if (CustomerType.MEMBER == tadeCustomer.getCustomerType()
                            && tadeCustomer.getStatusFlag() == StatusFlag.VALID) {
                        memberCustomer = tadeCustomer;
                        continue;
                    }
                    if (CustomerType.CARD == tadeCustomer.getCustomerType()
                            && tadeCustomer.getStatusFlag() == StatusFlag.VALID) {
                        memberCustomerCard = tadeCustomer;
                        continue;
                    }
                }
            }
        }
        if (mCustomer != null) {// 标识已登录会员 直接显示会员余额信息
        } else if (mecCard != null) {
        } else if (memberCustomer != null || memberCustomerCard != null) {
            //需要自动登录
            if (isAutoLogin()) {
                if (memberCustomer != null && memberCustomerCard == null) {//虚拟会员登录
                    verification(memberCustomer.getCustomerPhone(), memberCustomer.getCustomerId(), true);
                } else if (memberCustomerCard != null) {//实体卡登录
                    loginCard(memberCustomerCard.getEntitycardNum());
                } else {
                    ToastUtil.showLongToast(R.string.cannot_auto_login_unknow_type);
                }
            }
        } else {// 需要登录 显示登录界面
        }
    }

    public boolean isAutoLogin() {
        return isAutoLogin;
    }

    public void setAutoLogin(boolean autoLogin) {
        isAutoLogin = autoLogin;
    }

    /**
     * 获取卡的基本信息
     */
    private void getCardBaseInfo(String cardNum) {
        CustomerOperates operates = OperatesFactory.create(CustomerOperates.class);
        ResponseListener<CardBaseInfoResp> listener =
                LoadingResponseListener.ensure(new ResponseListener<CardBaseInfoResp>() {

                                                   @Override
                                                   public void onResponse(ResponseObject<CardBaseInfoResp> response) {
                                                       if (ResponseObject.isOk(response)) {
                                                           CardBaseInfoResp resp = response.getContent();
                                                           CardBaseInfoResp.BaseCardInfo baseCardInfo = resp.getResult();
                                                           if (baseCardInfo != null) {
                                                               if (baseCardInfo.getCardStatus() == CardStatus.ACTIVATED) {
                                                                   if (baseCardInfo.getCardType() == EntityCardType.GENERAL_CUSTOMER_CARD) {
                                                                       if (baseCardInfo.getCustomerId() != null
                                                                               && !TextUtils.isEmpty(baseCardInfo.getCustomerId().toString())) {
                                                                           // loginByPhoneNo(null, baseCardInfo.getCustomerId(), true);
                                                                           loginByCustomerId(baseCardInfo.getCustomerId(), true);//modify 20170913
                                                                       }
                                                                   } else {
                                                                       loginCard(baseCardInfo.getCardNum());
                                                                   }
                                                               } else {
                                                                   String tips = CustomerManager.getInstance().getStatusName(baseCardInfo.getCardStatus());
                                                                   ToastUtil.showShortToast(getString(R.string.card_error_str, tips));
                                                               }
                                                           } else {
                                                               ToastUtil.showShortToast(R.string.no_card_info);
                                                           }
                                                       } else {
                                                           String message = response.getMessage();
                                                           ToastUtil.showShortToast(message);
                                                       }
                                                   }

                                                   @Override
                                                   public void onError(VolleyError error) {
                                                       ToastUtil.showShortToast(error.getMessage());
                                                   }
                                               },
                        getFragmentManager());
        operates.getCardBaseInfo(cardNum, listener);
    }

    private void setLoginCustomer(CustomerResp customerNew) {
        if (mPaymentInfo.getTradeBusinessType() == BusinessType.SNACK || mPaymentInfo.getTradeBusinessType() == BusinessType.TAKEAWAY) {
            boolean isSelected = SharedPreferenceUtil.getSpUtil().getBoolean(SettingConstant.MEMBER_AUTO_PRIVILEGE, false);
            if (mPaymentInfo.getPaidPayment() != null && isSelected) {
                ToastUtil.showLongToast(R.string.no_support_privilege);
                return;
            }

            if (mPaymentInfo.isOrdered()) {
                return;
            }

            if (isSelected) {
                customerNew.queryLevelRightInfos();
                CustomerManager.getInstance().setLoginCustomer(customerNew);
                TradeCustomer tradeCustomer = CustomerManager.getInstance().getTradeCustomer(customerNew);
                if (!customerNew.isMember()) {
                    tradeCustomer.setCustomerType(CustomerType.CUSTOMER);
                } else if (customerNew.card == null) {
                    tradeCustomer.setCustomerType(CustomerType.MEMBER);
                } else {
                    tradeCustomer.setCustomerType(CustomerType.CARD);
                    tradeCustomer.setEntitycardNum(customerNew.card.getCardNum());
                }

                ShoppingCart.getInstance().setFastFoodCustomer(tradeCustomer);
                ShoppingCart.getInstance().memberPrivilege();// 设置购物车会员折扣
            }

            CustomerManager.getInstance().setLoginSource(1);
            RefreshTradeVoEvent event = new RefreshTradeVoEvent();
            event.setLogin(true);
            event.setTradeVo(ShoppingCart.getInstance().createFastFoodOrder(false));
            EventBus.getDefault().post(event);
        }
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FaceRequestCodeConstant.RC_PAY_LOGIN && resultCode == Activity.RESULT_OK) {
            String faceCode = data.getStringExtra(BaiduFaceRecognition.KEY_FACE_CODE);
            ResponseListener<MemberLoginVoResp> listener = new PhoneResponseListener(null).setCustomerLoginType(CustomerLoginType.FACE_CODE);
            CustomerManager.getInstance().customerLogin(CustomerLoginType.FACE_CODE, faceCode, "", false, false, true, LoadingResponseListener.ensure(listener, getFragmentManager()));
            FaceFeature faceFeature = (FaceFeature) data.getSerializableExtra(BaiduFaceRecognition.KEY_FACE_FEATURE);
            if (faceFeature != null) {
                ToastUtil.showLongToast("Beauty:" + faceFeature.getFaceScoreDescribe() + "\nAge:" + faceFeature.getAge() + "\nGender:" + faceFeature.getGender() + "\nFaceType:" + faceFeature.getFaceType() + "-" + faceFeature.getFaceTypeDescribe());
            }
        }
    }*/

    //判断是否要做预付金抵扣
    private boolean isNeedToDeductionEarnest() {
        if (this.mPaymentInfo.getPaidAmount() <= 0 && this.mPaymentInfo.getTradeVo().getTradeEarnestMoney() > 0) {
            return true;
        }
        return false;
    }

    //显示预付金抵扣界面
    public void showBookingDeductionDialog() {
        int optype = this.mPaymentInfo.getTradeVo().getTradeEarnestMoney() > this.mPaymentInfo.getActualAmount() ? 2 : 1;
        BookingDeductionRefundDialog.start(getFragmentManager(), mPaymentInfo, mDoPayApi, optype);
    }

    /**
     * 大客户loyalty登录
     */
    /*private void requestCheckMember(String phone, String memberId) {
        KeyAtCheckMemberListener keyAtCheckMemberListener = new KeyAtCheckMemberListener(getActivity(), null, new KeyAtCardLoginCallBackImpl() {
            @Override
            public void onRespCallBack(KeyAtLoginResp keyAtLoginResp) {
                requestSuccess(keyAtLoginResp);
            }
        });
        KeyAtDispatcher.getInstance().keyAtCheckMember(phone, memberId, LoadingResponseListener.ensure(keyAtCheckMemberListener, getSupportFragmentManager()));
    }*/

    /**
     * 大客户卡登陆
     */
    /*private void requestCardLogin(String cardNo) {
        KeyAtCardLoginListener keyAtCardLoginListener = new KeyAtCardLoginListener(getActivity(), new KeyAtRespCallBackImpl<KeyAtLoginResp>() {
            @Override
            public void onRespCallBack(KeyAtLoginResp keyAtLoginResp) {
                requestSuccess(keyAtLoginResp);
            }
        });
        KeyAtDispatcher.getInstance().keyAtCardLogin(cardNo, null, LoadingResponseListener.ensure(keyAtCardLoginListener, getSupportFragmentManager()));
    }*/

    /**
     * 回调处理
     */
    /*private void requestSuccess(KeyAtLoginResp keyAtLoginResp) {
        if (!PaySettingCache.isErpModeID(mPaymentInfo.getPayScene().value(), PayModeId.ENTITY_CARD.value())) {
            // 如果商户没有配置实体卡支付就直接返回
            ToastUtil.showShortToast(R.string.card_pay_not_set);
            return;
        }
        CustomerResp customerNew = keyAtLoginResp.customer;
        KeyAtCard keyAtCard = keyAtLoginResp.memberCard;
        KeyAtLoginConvert convert = KeyAtLoginConvert.newInstance();
        // 卡
        EcCard ecCard = convert.convertEcCard(keyAtCard, customerNew);
        // 余额
        if (ecCard.getValueCardAccount() != null
                && ecCard.getValueCardAccount().getRemainValue() != null) {
            mValueCardBalance = ecCard.getValueCardAccount().getRemainValue();
        } else {
            mValueCardBalance = 0;
        }
        if (ecCard.getIntegralAccount() != null && ecCard.getIntegralAccount().getIntegral() != null) {
            // 积分
            mIntegralBalance = ecCard.getIntegralAccount().getIntegral();
        } else {
            mIntegralBalance = 0;
        }

        mPaymentInfo.setEcCard(ecCard);
        mPaymentInfo.setCustomer(null);
        customerNew.card = null;
        mPaymentInfo.setCardCustomer(customerNew);
        mPaymentInfo.setMemberCardBalance(mValueCardBalance);
        mPaymentInfo.setMemberIntegral(mIntegralBalance);

        ToastUtil.showShortToast(R.string.card_login_success);
        if (mSelectDialog != null)
            mSelectDialog.dismiss();

        setLoginCustomer(customerNew);
        postLoginSuccess();
    }*/
}
