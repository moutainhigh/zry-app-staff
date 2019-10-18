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
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeInfo;
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

    private int barcodeWH = 180;
    private static final int WHAT_PAYSTATUS = 1;
    private static final int WHAT_SHOW_PAYSTATUS_BUTTON = 5;

    @ViewById(R.id.show_value)
    EditText show_value;
    @ViewById(R.id.customer_verification)
    Button customer_verification;

    @ViewById(R.id.pay_member_login_openscanner_button)
    TextView mOpenScannerBT;

    @ViewById(R.id.member_login_paycoder_button)
    TextView mFreshCodeBT;

    @ViewById(R.id.member_login_paycoder_label)
    TextView mScanCodeLBTV;
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

    private double mValueCardBalance = 0;
    private long mIntegralBalance;        private IPaymentInfo mPaymentInfo;
    private DoPayApi mDoPayApi;    private String mActualValue = "";
    private String mDisplayValue = "";
    private boolean isShowAccount = true;

    private boolean isAutoLogin = false;
    private boolean isDefaultShowCode = false;
    private boolean isShow = false;

        private final int maxTextLength = 20;
    private ScanCodeManager mScanCodeManager;
    private long mCurrentPaymentItemId;
    private String mCurrentPaymentItemUuid = null;
    private Double mLastAmount;    private Bitmap bitmap = null;    private String codeUrl = null;    private DeWoScanCode mDeWoScanCode;
    private NewTradeOperatesImpl tradeOperates = OperatesFactory.create(NewTradeOperatesImpl.class);


    private ErpCurrency mErpCurrency;

    private Map<String, ErpCurrency> erpCurrencyMap;

    private ErpCommercialRelationDal mErpDal;

    private int uiType = UI_TYPE_MOBILE;

    MemberChooseTypePopWindow popWindow;

    List<MemberLoginTypeData> loginTypeDataList;

    private CustomerType mCustomerType=CustomerType.MEMBER;

    public CustomerType getmCustomerType() {
        return mCustomerType;
    }

    public void setmCustomerType(CustomerType mCustomerType) {
        this.mCustomerType = mCustomerType;
    }

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
                                        doLogin();                }
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
                startScanner();                ToastUtil.showShortToast(R.string.pay_member_scanner_have_open);
                break;
            case R.id.member_login_paycoder_button:
                                                MobclickAgentEvent.onEvent(getActivity(), SDConstant.MobClick.SNACK_PAY_MEMBER_LOGIN_SCANED);
                if (this.mPaymentInfo.isNeedToPayDeposit()) {
                    PayDepositPromptDialog.start(this.getActivity(), mDoPayApi, this.mPaymentInfo);
                } else if (this.isNeedToDeductionEarnest()) {
                    this.showBookingDeductionDialog();                } else {
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
                                            getMemberPayCode();                                        }
                                    });
                        } else {
                            getMemberPayCode();                        }
                    }
                }
                break;

            case R.id.btn_face_login:
                break;
            case R.id.rlAreaCode:
                showCountryDialog(new ArrayList<>(erpCurrencyMap.values()), mErpCurrency);
                break;

            default:
                break;
        }
    }


    private void onCardLoginClick() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerEventBus();
    }

    @AfterViews
    protected void init() {
        mDoPayApi.setCurrentPaymentInfoId(mPaymentInfo.getId());        initAreaCode();
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
        mActualValue = "";        disPlayInput("");        initKeyBoard();
        initPopup();
        doAutoLogin();
        updateTradeAmount();        updateFreshCodeBT();        initJinCheng();
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

        private void disPlayInput(String input) {

    }


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
            startScanner();            if (DensityUtil.isHaveMiniScreen(this.getActivity())) {                if (this.bitmap != null && codeUrl != null) {
                    if (isAmountChange()) {                                                updateTradeAmount();
                        this.bitmap = null;
                        updateFreshCodeBT();                    } else {
                        updateMiniDiplayWithUrl(1, codeUrl, bitmap, true);
                    }
                }
            }
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
        stopScanner();          mHandler.removeCallbacksAndMessages(null);
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        startScanner();    }

    @Override
    public void onPause() {
        super.onPause();
        stopScanner();    }


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
                mDisplayValue = mActualValue;
        int index = show_value.getSelectionStart();
        show_value.getText().insert(index, text);
        Selection.setSelection(show_value.getText(), show_value.getText().length());
    }


    private void deleteTheLast() {
        if (mActualValue.length() > 0) {
            StringBuilder sb = new StringBuilder(mActualValue);
            sb.deleteCharAt(sb.length() - 1);
            mActualValue = sb.toString();
                        mDisplayValue = mActualValue;
            show_value.setText(mDisplayValue);
            Selection.setSelection(show_value.getText(), show_value.getText().length());
        }
    }


    private void clearText() {
        mActualValue = "";
                mDisplayValue = mActualValue;
        show_value.setText(mDisplayValue);
        Selection.setSelection(show_value.getText(), show_value.getText().length());
    }

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

        private TextWatcher mTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
                        String inputStr = s.toString();
            if (inputStr.endsWith("\n") && !TextUtils.isEmpty(mActualValue)) {                doLogin();
                return;
            }
                        setButton();
            if (isShowAccount) {
                disPlayInput(s.toString());            } else {
                isShowAccount = true;
            }
        }
    };

        private void doLogin() {
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


    public void onEventMainThread(ExemptEventUpdate event) {
        if (this.isAdded() && this.isShow && DensityUtil.isHaveMiniScreen(this.getActivity())) {
            if (isAmountChange()) {                if (isDefaultShowCode || bitmap != null) {
                                                            disPlayInput(show_value.getText().toString());                    bitmap = null;
                    updateFreshCodeBT();
                }
                updateTradeAmount();
            }
        }
    }

        public void onEventMainThread(EventLoginView event) {
        if (this.isAdded() && this.isShow && DensityUtil.isHaveMiniScreen(this.getActivity())) {
            if (event.getOperateType() == EventLoginView.TYPE_LOGIN_VIEW_START) {
                this.stopScanner();
            } else if (event.getOperateType() == EventLoginView.TYPE_LOGIN_VIEW_END) {
                this.disPlayInput("");
                this.startScanner();
                                if (this.bitmap != null && codeUrl != null) {
                    updateMiniDiplayWithUrl(1, codeUrl, bitmap, true);
                } else {
                    if (this.isDefaultShowCode)
                        getMemberPayCode();                }
            }
        }
    }

    private void updateFreshCodeBT() {
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

        ScanCode.ScanCodeReceivedListener mScanCodeReceivedListener = new ScanCode.ScanCodeReceivedListener() {
        @Override
        public void onScanCodeReceived(String data) {
            loginByScanCode(data);
        }
    };

        private void loginByScanCode(String data) {
                MobclickAgentEvent.onEvent(getActivity(), SDConstant.MobClick.SNACK_PAY_MEMBER_LOGIN_SCAN);
        if (this.getActivity() != null && !this.getActivity().isDestroyed() && this.isAdded()) {
            if (!TextUtils.isEmpty(data)) {
                if (data.contains(":")) {                     loginByWeixin(data);
                } else {
                    verification(data, null, false);
                }
                show_value.setText("");
            }
        }
    }


    private void getMemberPayCode() {
        if (mPaymentInfo == null || mPaymentInfo.getTradeVo() == null) {
            return;
        }
        TradeVo tradeVo = mPaymentInfo.getTradeVo();
                if (mPaymentInfo.isOrdered()) {
            getMemberPayUrl(tradeVo.getTrade());

        } else {
            ISavedCallback savedCallback = new ISavedCallback() {
                @Override
                public void onSaved(boolean isOk) {                    try {
                        if (isOk) {
                            mPaymentInfo.setOrdered(true);
                            ToastUtil.showLongToast(R.string.pay_wechat_trade_insert_success);
                                                        ShoppingCart.getInstance().clearShoppingCart();
                                                        getMemberPayUrl(mPaymentInfo.getTradeVo().getTrade());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            mDoPayApi.saveTrade(this.getActivity(), mPaymentInfo, null, savedCallback);
                    }
    }



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
                                                if (!Utils.isEmpty(resp.getTrades())) {
                            Trade trade = resp.getTrades().get(0);
                            if (trade.getServerUpdateTime() > mPaymentInfo.getTradeVo().getTrade().getServerUpdateTime()) {
                                mPaymentInfo.getTradeVo().setTrade(trade);
                                                                if (mPaymentInfo.isSplit()) {
                                    SeparateShoppingCart.getInstance().updateDataWithTrade(trade);
                                } else {
                                    DinnerShoppingCart.getInstance().updateDataWithTrade(trade);
                                }
                            }
                        }
                        codeUrl = (String) addition.get("codeUrl");
                                                bitmap = EncodingHandler.createQRCode(codeUrl, barcodeWH);
                                                updateMiniDiplayWithUrl(1, codeUrl, bitmap, true);
                                                sendGetPayStatusMessage();


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
        PayModelItem payModelItem = new PayModelItem(PayModeId.MEMBER_CARD);        payModelItem.setUsedValue(BigDecimal.valueOf(this.mPaymentInfo.getActualAmount()));

        payModelItem.setUuid(SystemUtils.genOnlyIdentifier());

        payModelItem.setPayType(PayType.QCODE);
        mPaymentInfo.getOtherPay().clear();
        mPaymentInfo.getOtherPay().addPayModelItem(payModelItem);
        mDoPayApi.setOnlinePaymentItemUuid(payModelItem.getUuid());
        tradeOperates.newpay(trade, new PaymentReqTool(mPaymentInfo).creatPaymentReq(), LoadingResponseListener.ensure(listener, getFragmentManager()));
            }

    private void updateMiniDiplayWithUrl(int payWay, String codeUrl, Bitmap bitmap, boolean isUseUrl) {

    }

        private void getPayStatusForS() {
        ResponseListener<PayResp> listener = new ResponseListener<PayResp>() {

            @Override
            public void onResponse(ResponseObject<PayResp> response) {
                try {
                    if (ResponseObject.isOk(response)) {
                        PayResp result = response.getContent();

                        mDoPayApi.doVerifyPayResp(getActivity(), mPaymentInfo, result, mOnlinePayOverCallback);                        return;
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


    private void sendGetPayStatusMessage() {
        Message message = new Message();
        message.what = WHAT_PAYSTATUS;
        mHandler.removeMessages(WHAT_PAYSTATUS);
        mHandler.sendMessageDelayed(message, 3000);
    }


    private void stopGetPayStatus() {
        RequestManager.cancelAll("paystatus");        if (mHandler != null) {
            mHandler.removeMessages(WHAT_PAYSTATUS);
        }
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_PAYSTATUS:
                    getPayStatusForS();
                    break;

                default:
                    break;
            }
        }
    };
        IOnlinePayOverCallback mOnlinePayOverCallback = new IOnlinePayOverCallback() {

        @Override
        public void onPayResult(Long paymentItemId, int payStatus) {
            if (TradePayStatus.PAID.value() == payStatus) {
                stopGetPayStatus();                            } else if (TradePayStatus.PAID_FAIL.value() == payStatus) {
                stopGetPayStatus();                            } else {
                sendGetPayStatusMessage();
            }
        }
    };

    private void loginCard(String cardNo) {

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
                                        EcCard card = resp.getResult().getCardInstance();
                    if (card.getCardType() == EntityCardType.CUSTOMER_ENTITY_CARD) {
                        if (!PaySettingCache.isErpModeID(mPaymentInfo.getPayScene().value(), PayModeId.ENTITY_CARD.value())) {
                                                        ToastUtil.showShortToast(R.string.card_pay_not_set);
                            return;
                        }
                                                CustomerV5 customerv5 = resp.getResult().getCustomer();
                        card.setName(customerv5.getName());
                        card.setCardLevel(resp.getResult().getCardLevel());
                        card.setCardLevelSetting(resp.getResult().getCardLevelSetting());
                        card.setCardSettingDetails(resp.getResult().getCardSettingDetails());
                        card.setIntegralAccount(resp.getResult().getIntegralAccount());
                        card.setValueCardAccount(resp.getResult().getValueCardAccount());
                        card.setCardKind(resp.getResult().getCardKind());
                        card.setCustomer(customerv5);

                                                if (card.getValueCardAccount() != null
                                && card.getValueCardAccount().getRemainValue() != null) {
                            mValueCardBalance = card.getValueCardAccount().getRemainValue();
                        } else {
                            mValueCardBalance = 0;
                        }
                        if (card.getIntegralAccount() != null && card.getIntegralAccount().getIntegral() != null) {
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
                        mIntegralBalance = 0;
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


    private void verification(final String mobile, final Long customerId, boolean auto) {
        if (customerId == null) {
            if (TextUtils.isEmpty(mobile)) {
                ToastUtil.showShortToast(uiType == UI_TYPE_MOBILE ? R.string.customer_login_hint : R.string.customer_ordercenter_search_hint0);
                return;
            }
        }
        loginByPhoneNo(mobile, customerId, auto);
    }


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
                getLoginInfoByInput(inputNo);            }
        }
    }

    private CustomerSelectDialog mSelectDialog = null;

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



    private void loginByPhoneNo(final String inputNo, final Long customerId, final int needPswd, final String pswd, final PasswordDialog dialog) {

        PhoneResponseListener listener = new PhoneResponseListener(dialog);
        listener.setCustomerType(mCustomerType);
        CustomerManager customerManager = CustomerManager.getInstance();
        if (customerId != null && customerId != 0) {
            customerManager.customerLogin(CustomerLoginType.MEMBER_ID, customerId.toString(), pswd, needPswd == 1, false, true, LoadingYFResponseListener.ensure(listener, getFragmentManager()));
        } else if (!TextUtils.isEmpty(inputNo)) {
            String telCode = mErpCurrency != null && mErpCurrency.getAreaCode() != null ? mErpCurrency.getAreaCode() : null;
            String country = mErpCurrency != null && mErpCurrency.getCountryZh() != null ? mErpCurrency.getCountryZh() : null;
            String nation = mErpCurrency != null && mErpCurrency.getCountryEn() != null ? mErpCurrency.getCountryEn() : null;
            CustomerManager.getInstance().customerLogin(CustomerLoginType.MOBILE, inputNo, pswd, needPswd == 1, false, true,
                    LoadingYFResponseListener.ensure(listener, getFragmentManager()));
        }
    }






    private void loginByCustomerId(Long customerId, boolean isloginByCard) {
        PhoneResponseListener listener = new PhoneResponseListener(null, isloginByCard);
        CustomerManager customerManager = CustomerManager.getInstance();
        customerManager.customerLogin(CustomerLoginType.MEMBER_ID, customerId.toString(), null, false, false, true, LoadingYFResponseListener.ensure(listener, getFragmentManager()));
    }


    private void loginByWeixin(String number) {
        YFResponseListener<YFResponse<CustomerLoginResp>> listener = new PhoneResponseListener(null).setCustomerLoginType(CustomerLoginType.WECHAT_MEMBERCARD_ID);
        CustomerManager customerManager = CustomerManager.getInstance();
        customerManager.customerLogin(CustomerLoginType.WECHAT_MEMBERCARD_ID, number, null, false, false, true, LoadingYFResponseListener.ensure(listener, getFragmentManager()));
    }


    private class PhoneResponseListener implements YFResponseListener<YFResponse<CustomerLoginResp>> {
        private PasswordDialog dialog;
        private boolean isLoginByCard;
        private CustomerType customerType=CustomerType.MEMBER;

        public PhoneResponseListener setCustomerLoginType(CustomerLoginType customerLoginType) {
            this.customerLoginType = customerLoginType;
            return this;
        }

        public void setCustomerType(CustomerType customerType) {
            this.customerType = customerType;
        }

        private CustomerLoginType customerLoginType;
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
                    if (resp.customerIsDisable()) {                        ToastUtil.showShortToast(R.string.order_dish_member_disabled);
                        return;
                    }

                    CustomerResp customer = resp.getCustomer();
                    customer.setCustomerType(this.customerType);
                    if (customer != null) {
                        if (this.customerLoginType != null) {
                            customer.customerLoginType = this.customerLoginType;
                        }
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
                        setLoginCustomer(customer);                                                if (mPaymentInfo.getTradeBusinessType() == BusinessType.SNACK && mPaymentInfo.getTradeVo().getTrade().getTradePayStatus() != TradePayStatus.PAYING
                                && mPaymentInfo.getTradeVo() != null
                                && mPaymentInfo.getTradeVo().getTradeExtra() != null
                                && TextUtils.isEmpty(mPaymentInfo.getTradeVo().getTradeExtra().getOpenIdenty())) {                            if (!TextUtils.isEmpty(customer.openId)) {                                mPaymentInfo.setOrdered(false);
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
                mActualValue = show_value.getText().toString().replace("\n", "").trim();            } else {
                customer_verification.setEnabled(false);
                customer_verification.setTextColor(getActivity().getResources().getColor(R.color.text_black));
                customer_verification.setBackgroundResource(R.drawable.pay_dopay_btn_bg);
            }
        }
    }


    public void postLoginSuccess() {
        EventBus.getDefault().post(new MemberLoginEvent(false));
    }



    private void doAutoLogin() {
        if (mPaymentInfo == null) {
            return;
        }
        CustomerResp mCustomer = mPaymentInfo.getCustomer();        EcCard mecCard = mPaymentInfo.getEcCard();        TradeCustomer memberCustomer = null;
        TradeCustomer memberCustomerCard = null;
        if (mecCard == null && mCustomer == null && mPaymentInfo.getTradeVo() != null) {
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
        if (mCustomer != null) {        } else if (mecCard != null) {
        } else if (memberCustomer != null || memberCustomerCard != null) {
                        if (isAutoLogin()) {
                if (memberCustomer != null && memberCustomerCard == null) {                    verification(memberCustomer.getCustomerPhone(), memberCustomer.getCustomerId(), true);
                } else if (memberCustomerCard != null) {                    loginCard(memberCustomerCard.getEntitycardNum());
                } else {
                    ToastUtil.showLongToast(R.string.cannot_auto_login_unknow_type);
                }
            }
        } else {        }
    }

    public boolean isAutoLogin() {
        return isAutoLogin;
    }

    public void setAutoLogin(boolean autoLogin) {
        isAutoLogin = autoLogin;
    }


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
                                                                                                                                                      loginByCustomerId(baseCardInfo.getCustomerId(), true);                                                                       }
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
        DinnerShopManager.getInstance().setLoginCustomer(customerNew);
        DinnerShoppingCart.getInstance().batchMemberChargePrivilege(true, false);

        RefreshTradeVoEvent event = new RefreshTradeVoEvent();
        event.setLogin(true);
        event.setTradeVo(DinnerShoppingCart.getInstance().getShoppingCartVo().getmTradeVo());
        EventBus.getDefault().post(event);
    }



        private boolean isNeedToDeductionEarnest() {
        if (this.mPaymentInfo.getPaidAmount() <= 0 && this.mPaymentInfo.getTradeVo().getTradeEarnestMoney() > 0) {
            return true;
        }
        return false;
    }

        public void showBookingDeductionDialog() {
        int optype = this.mPaymentInfo.getTradeVo().getTradeEarnestMoney() > this.mPaymentInfo.getActualAmount() ? 2 : 1;
        BookingDeductionRefundDialog.start(getFragmentManager(), mPaymentInfo, mDoPayApi, optype);
    }









}
