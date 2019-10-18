package com.zhongmei.bty.dinner.cash;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.beauty.customer.constants.BeautyCustomerConstants;
import com.zhongmei.bty.basemodule.auth.application.BeautyApplication;
import com.zhongmei.bty.basemodule.auth.application.CustomerApplication;
import com.zhongmei.bty.basemodule.customer.CustomerLogin;
import com.zhongmei.bty.basemodule.customer.dialog.PasswordDialog;
import com.zhongmei.bty.basemodule.customer.dialog.country.CountryDialog;
import com.zhongmei.bty.basemodule.customer.dialog.country.CountryGridAdapter;
import com.zhongmei.bty.basemodule.customer.enums.CustomerAppConfig;
import com.zhongmei.bty.basemodule.customer.enums.CustomerLoginType;
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.basemodule.customer.message.MemberLoginVoResp;
import com.zhongmei.bty.basemodule.customer.message.MemberVerifyPwdReq;
import com.zhongmei.bty.basemodule.customer.operates.CustomerOperates;
import com.zhongmei.bty.basemodule.database.entity.customer.CommCustomer;
import com.zhongmei.bty.basemodule.database.entity.customer.CustomerV5;
import com.zhongmei.bty.basemodule.database.entity.customer.EventOpenIdLoginInfo;
import com.zhongmei.bty.basemodule.database.entity.customer.OpenIdLoginInfo;
import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCard;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardInfo;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardKind;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardBaseInfoResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardLoginResp;
import com.zhongmei.bty.basemodule.devices.mispos.enums.CardStatus;
import com.zhongmei.bty.basemodule.devices.mispos.enums.EntityCardType;
import com.zhongmei.bty.basemodule.devices.mispos.event.EventReadKeyboard;
import com.zhongmei.bty.basemodule.devices.scaner.DeWoScanCode;
import com.zhongmei.bty.basemodule.devices.scaner.ScanCode;
import com.zhongmei.bty.basemodule.devices.scaner.ScanCodeManager;
import com.zhongmei.bty.basemodule.erp.bean.ErpCurrency;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.bty.basemodule.shopmanager.handover.manager.ServerSettingManager;
import com.zhongmei.bty.basemodule.trade.manager.DinnerCashManager;
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.commonmodule.view.EditTextWithDeleteIcon;
import com.zhongmei.bty.customer.CustomerChargingDialogFragment;
import com.zhongmei.bty.customer.CustomerChargingDialogFragment_;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.bean.YFResponse;
import com.zhongmei.yunfu.bean.req.CustomerLoginResp;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.data.LoadingYFResponseListener;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.CustomerType;
import com.zhongmei.yunfu.monitor.CalmResponseListener;
import com.zhongmei.yunfu.net.builder.NetError;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.EventResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.yunfu.resp.YFResponseListener;
import com.zhongmei.yunfu.resp.data.LoyaltyTransferResp;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.util.UserActionCode;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import de.greenrobot.event.EventBus;


@EFragment(R.layout.dinner_customer_login_fragment_layout)
public class DinnerCustomerLoginFragment extends BasicFragment {
    public static final String TAG = DinnerCustomerLoginFragment.class.getSimpleName();

    public static final int LOGIN_MOBILE = 1;

    private static final int LOGIN_CARD = 2;

    @ViewById(R.id.login_layout)
    LinearLayout mLlLoginLayout;

    @ViewById(R.id.tvScanDesc_Customer)
    TextView mTvScanDesc;

    @ViewById(R.id.ll_login_mobile)
    LinearLayout mLlLoginMobile;

    @ViewById(R.id.tv_login_mobile)
    TextView mTvLoginMobile;

    @ViewById(R.id.v_login_mobile)
    View mVLoginMobile;

    @ViewById(R.id.ll_login_card)
    LinearLayout mLlLoginCard;

    @ViewById(R.id.tv_login_card)
    TextView mTvLoginCard;

    @ViewById(R.id.v_login_card)
    View mVLoginCard;

    @ViewById(R.id.rl_area_code)
    LinearLayout mLlCountry;

    @ViewById(R.id.tv_area_codes)
    TextView mTvCountry;

    @ViewById(R.id.phone_number)
    EditTextWithDeleteIcon mShowValue;

    @ViewById(R.id.customer_verification)
    Button mLoginBtn;

    private ErpCurrency mErpCurrency;

    private Map<String, ErpCurrency> erpCurrencyMap;

    boolean isOpenWeChat, isNeedOpenId;

        private String mFaceCode;


    private boolean isLogin = false;
    private ScanCodeManager mScanCodeManager;

    private int loginType = LOGIN_MOBILE;


    private int mLaunchMode;

    private DinnerCustomerLoginDialogFragment.CallbackListener listener;

    public void setListener(DinnerCustomerLoginDialogFragment.CallbackListener listener) {
        this.listener = listener;
    }

    public void setErpCurrency(Map<String, ErpCurrency> erpCurrencyMap) {
        this.erpCurrencyMap = erpCurrencyMap;
        if (erpCurrencyMap != null && ShopInfoCfg.getInstance().getCurrency() != null) {
            String areaCode = ShopInfoCfg.getInstance().getCurrency().getAreaCode();
            if (!TextUtils.isEmpty(areaCode)) {
                mErpCurrency = erpCurrencyMap.get(areaCode);
            } else {
                mErpCurrency = erpCurrencyMap.get("86");
            }
        }
    }

    public void setShowLoginView() {
        if (mLlLoginLayout != null) {
            mLlLoginLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getArguments() != null) {
            mLaunchMode = getArguments().getInt(BeautyCustomerConstants.KEY_CUSTOMER_LOGIN_FLAG, BeautyCustomerConstants.CustomerLoginLaunchMode.LOGIN);
        }
    }

    @AfterViews
    void init() {
        isOpenWeChat = getParentDialogFragment().settings[0];
        isNeedOpenId = getParentDialogFragment().settings[1];
        mShowValue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.e("loginFragment",v+";hasFocus+"+hasFocus);
            }
        });
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        switchTitle();
        setupCountryView();
        isShowQrCode();
        mShowValue.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER)) {
                    VerifyHelper.verifyAlert(getActivity(), BeautyApplication.PERMISSION_CUSTOMER_LOGIN,
                            new VerifyHelper.Callback() {
                                @Override
                                public void onPositive(User user, String code, Auth.Filter filter) {
                                    super.onPositive(user, code, filter);
                                    verification();
                                }
                            });
                    return true;
                }
                return false;
            }
        });
    }

    private void switchTitle() {
        switch (loginType) {
            case LOGIN_MOBILE:
                mShowValue.setText("");
                mShowValue.setHint(getString(R.string.customer_login_hint));
                mVLoginMobile.setVisibility(View.VISIBLE);
                mTvLoginCard.setTextColor(getResources().getColor(R.color.color_999999));
                mLlLoginCard.setBackgroundColor(getResources().getColor(R.color.bg_white));
                mVLoginCard.setVisibility(View.INVISIBLE);
                break;
            case LOGIN_CARD:
                mShowValue.setText("");
                mShowValue.setHint(getString(R.string.customer_ordercenter_search_hint0));
                mTvLoginMobile.setTextColor(getResources().getColor(R.color.color_999999));
                mLlLoginMobile.setBackgroundColor(getResources().getColor(R.color.bg_white));
                mVLoginMobile.setVisibility(View.INVISIBLE);
                mVLoginCard.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        isShowQrCode();
    }

    void setupCountryView() {
        if (mErpCurrency != null) {
            mTvCountry.setText(mErpCurrency.getCountryAreaCode());
        }
    }

    private void isShowQrCode() {
    }

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
            Log.e("loginFragment","inputStr:"+inputStr);
            if (inputStr.endsWith("\n")) {                verification();
                return;
            }
            if (!TextUtils.isEmpty(mShowValue.getText())) {
                mLoginBtn.setEnabled(true);
                if (CustomerApplication.mCustomerBussinessType == CustomerAppConfig.CustomerBussinessType.BEAUTY) {
                    mLoginBtn.setBackgroundResource(R.drawable.beauty_login_btn_selector);
                } else {
                    mLoginBtn.setBackgroundResource(R.drawable.orderdish_clear_status_bottom_button_bg_selector);
                }
            } else {
                if (loginType == LOGIN_MOBILE)
                    mShowValue.setHint(loginType == LOGIN_MOBILE ? R.string.customer_login_hint : R.string.customer_ordercenter_search_hint0);
                mLoginBtn.setEnabled(false);
                if (CustomerApplication.mCustomerBussinessType == CustomerAppConfig.CustomerBussinessType.BEAUTY) {
                    mLoginBtn.setBackgroundResource(R.drawable.beauty_login_btn_selector);
                } else {
                    mLoginBtn.setBackgroundResource(R.drawable.orderdish_clear_status_select_all_not_enabled);
                }
            }
        }
    };


    private void verification() {
        final String inputNo = mShowValue.getText().toString().replace("\n", "").trim();
        if (TextUtils.isEmpty(inputNo)) {
            ToastUtil.showShortToast(loginType == LOGIN_MOBILE ? R.string.customer_login_hint : R.string.customer_ordercenter_search_hint0);
            mShowValue.requestFocus();
            return;
        }
        switchLogin(inputNo);
    }

    private void switchLogin(String inputNo) {
        switch (loginType) {
            case LOGIN_MOBILE:
                showPasswordDialog(inputNo, null);
                break;
            case LOGIN_CARD:
                showPasswordDialog(inputNo);
                break;
            default:
                break;
        }
    }

    private void showPasswordDialog(final String inputNo, final Long customerId) {
        if (customerId == null && !TextUtils.isEmpty(inputNo) && mErpCurrency != null && !TextUtils.isEmpty(mErpCurrency.getPhoneRegulation()) && !Pattern.matches(mErpCurrency.getPhoneRegulation(), inputNo)) {
            ToastUtil.showShortToast(getString(R.string.customer_mobile_regulation_error));
            return;
        }
        CustomerLogin.dinnerLoginByPhoneNo(getActivity(), inputNo, new CustomerLogin.DinnerLoginListener() {
            @Override
            public void login(PasswordDialog dialog, int needPswd, String password) {
                boolean needPwd = needPswd == CustomerManager.NEED_PSWD;
                if (customerId != null) {
                    loginByCustomerId(customerId, needPwd, password, dialog);
                } else if (inputNo != null) {
                    loginByMobile(inputNo, needPwd, password, dialog);
                }
            }
        });
    }

    private void showPasswordDialog(final String cardNO){
        CustomerLogin.dinnerLoginByPhoneNo(getActivity(), cardNO, new CustomerLogin.DinnerLoginListener() {
            @Override
            public void login(PasswordDialog dialog, int needPswd, String password) {
                boolean needPwd = needPswd == CustomerManager.NEED_PSWD;
                    loginByCardNo(cardNO, needPwd, password, dialog);
            }
        });
    }


    private void getCardBaseInfo(final String inputNo) {
        CustomerOperates operates = OperatesFactory.create(CustomerOperates.class);
        ResponseListener<CardBaseInfoResp> listener =
                LoadingResponseListener.ensure(new EventResponseListener<CardBaseInfoResp>(UserActionEvent.DINNER_PAY_LOGIN_SWIPE_CARD) {

                                                   @Override
                                                   public void onResponse(ResponseObject<CardBaseInfoResp> response) {
                                                       if (ResponseObject.isOk(response)) {
                                                           UserActionEvent.end(getEventName());
                                                           CardBaseInfoResp resp = response.getContent();
                                                           final CardBaseInfoResp.BaseCardInfo baseCardInfo = resp.getResult();
                                                           if (baseCardInfo != null) {
                                                               if (baseCardInfo.getCardStatus() == CardStatus.ACTIVATED) {
                                                                   if (baseCardInfo.getCardType() == EntityCardType.GENERAL_CUSTOMER_CARD) {
                                                                       if (baseCardInfo.getCustomerId() != null && !TextUtils.isEmpty(baseCardInfo.getCustomerId().toString())) {
                                                                           showPasswordDialog(baseCardInfo.getCardNum(), baseCardInfo.getCustomerId());
                                                                       }
                                                                   } else {
                                                                       showPasswordDialog(baseCardInfo.getCardNum());
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



    private void loginByCardNo(String input, boolean needPswd, final String pswd, final PasswordDialog dialog) {
        CustomerManager.getInstance().customerLogin(CustomerLoginType.CARD_NO_ENTITY, input, pswd, needPswd, false, true,
                getResponseMemberLogin(needPswd, dialog, UserActionEvent.DINNER_PAY_LOGIN_INPUT_MOBILE, CustomerLoginType.MOBILE));
    }

    private void verifyPwd(Long customerId, String pwd, CalmResponseListener listener) {
        CustomerOperates operates = OperatesFactory.create(CustomerOperates.class);
        MemberVerifyPwdReq req = new MemberVerifyPwdReq();
        req.setCustomerId(customerId);
        req.setType(2);
        req.setPassword(pwd);
        req.setUserId(Session.getAuthUser() == null ? null : Session.getAuthUser().getId());
        operates.validationMemberPwd(getActivity(), req, listener);
    }


    private void loginByWeChat(Long customerId) {
        CustomerManager.getInstance().customerLogin(CustomerLoginType.MEMBER_ID, customerId.toString(), null, false, true, getResponseMemberLogin(false, null, UserActionEvent.DINNER_PAY_LOGIN_SCAN_WECHAT_CODE, CustomerLoginType.MEMBER_ID));
    }


    private void loginByMobile(String input, boolean needPswd, final String pswd, final PasswordDialog dialog) {
        String telCode = mErpCurrency != null && mErpCurrency.getAreaCode() != null ? mErpCurrency.getAreaCode() : null;
        String country = mErpCurrency != null && mErpCurrency.getCountryZh() != null ? mErpCurrency.getCountryZh() : null;
        String nation = mErpCurrency != null && mErpCurrency.getCountryEn() != null ? mErpCurrency.getCountryEn() : null;
        CustomerManager.getInstance().customerLogin(CustomerLoginType.MOBILE, input, pswd, needPswd, false, true,
                getResponseMemberLogin(needPswd, dialog, UserActionEvent.DINNER_PAY_LOGIN_INPUT_MOBILE, CustomerLoginType.MOBILE));
    }


    private void loginByCustomerId(Long customerId, boolean needPswd, final String pswd, final PasswordDialog dialog) {
        CustomerManager.getInstance().customerLogin(CustomerLoginType.MEMBER_ID, customerId.toString(), pswd, needPswd, false, true,
                getResponseMemberLogin(needPswd, dialog, UserActionEvent.DINNER_PAY_LOGIN_SCAN_CODE, CustomerLoginType.MEMBER_ID));
    }





    private void loginByWeixin(final String number) {
        ResponseListener<MemberLoginVoResp> listener = new EventResponseListener<MemberLoginVoResp>(UserActionEvent.DINNER_PAY_LOGIN_SCAN_CODE) {
            @Override
            public void onResponse(ResponseObject<MemberLoginVoResp> response) {
                if (ResponseObject.isOk(response) && MemberLoginVoResp.isOk(response.getContent())) {
                    if (isLogin) {
                        return;
                    }
                    CustomerLoginResp resp = response.getContent().getResult();
                    if (resp.customerIsDisable()) {                        ToastUtil.showShortToast(R.string.order_dish_member_disabled);
                    } else {
                        isLogin = true;
                        CustomerResp customerNew = resp.getCustomer();
                        customerNew.setInitialValue();
                        customerNew.queryLevelRightInfos();
                        CustomerManager.getInstance().setLoginCustomer(customerNew);
                        TradeCustomer tradeCustomer = CustomerManager.getInstance().getTradeCustomer(customerNew);
                        tradeCustomer.setCustomerType(CustomerType.MEMBER);
                        ToastUtil.showShortToast(R.string.customer_login);

                                                EventBus.getDefault().post(new EventReadKeyboard(true, ""));                        DinnerPriviligeItemsFragment.showDisplayUserInfo(getActivity());
                        if (mLaunchMode == BeautyCustomerConstants.CustomerLoginLaunchMode.RECHARGE) {
                                                        showCustomerCardDialoig(customerNew);
                        } else {
                            new DinnerCashManager().jumpAfterLogin("", customerNew, null);
                        }
                        DinnerShopManager.getInstance().getShoppingCart().setOpenIdenty(resp.getOpenId());
                        getParentDialogFragment().dismiss();
                    }
                } else {
                    getCardBaseInfo(number);
                }
                UserActionEvent.end(UserActionEvent.DINNER_PAY_LOGIN_SCAN_CODE);
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showLongToast(error.getMessage());
            }
        };

        CustomerManager.getInstance().customerLogin(CustomerLoginType.WECHAT_MEMBERCARD_ID, number, "", false, false, true, LoadingResponseListener.ensure(listener, getChildFragmentManager()));
    }


    private YFResponseListener<YFResponse<CustomerLoginResp>> getResponseMemberLogin(final boolean needPswd, final PasswordDialog dialog, UserActionEvent event, final CustomerLoginType customerLoginType) {
        YFResponseListener<YFResponse<CustomerLoginResp>> response = new YFResponseListener<YFResponse<CustomerLoginResp>>() {
            @Override
            public void onResponse(YFResponse<CustomerLoginResp> response) {
                try {
                    if (YFResponse.isOk(response)) {
                        if (isLogin) {
                            return;
                        }
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                                                CustomerLoginResp resp = response.getContent();
                        if (resp.customerIsDisable()) {                            ToastUtil.showShortToast(R.string.order_dish_member_disabled);
                        } else {
                            isLogin = true;
                            CustomerResp customerNew = resp.getCustomer();
                            customerNew.setInitialValue();
                            customerNew.queryLevelRightInfos();
                            customerNew.setCustomerType(CustomerType.MEMBER);
                            customerNew.needRefresh = false;
                            customerNew.customerLoginType = customerLoginType;
                            TradeCustomer tradeCustomer = CustomerManager.getInstance().getTradeCustomer(customerNew);
                            if (!customerNew.isMember()) {
                                tradeCustomer.setCustomerType(CustomerType.CUSTOMER);
                            } else if (customerNew.card == null) {
                                tradeCustomer.setCustomerType(CustomerType.MEMBER);
                            } else {
                                tradeCustomer.setCustomerType(CustomerType.CARD);
                                tradeCustomer.setEntitycardNum(customerNew.card.getCardNum());
                            }
                            ToastUtil.showShortToast(R.string.customer_login);
                            mShowValue.setText("");

                            EventBus.getDefault().post(new EventReadKeyboard(true, ""));                            if (mLaunchMode == BeautyCustomerConstants.CustomerLoginLaunchMode.RECHARGE) {
                                                                showCustomerCardDialoig(customerNew);
                            } else {
                                new DinnerCashManager().jumpAfterLogin("", customerNew, null);
                            }
                            DinnerShopManager.getInstance().getShoppingCart().setOpenIdenty(resp.getOpenId());
                            if (customerLoginType == CustomerLoginType.FACE_CODE) {

                            } else {
                                getParentDialogFragment().dismiss();
                            }
                        }
                    } else {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.clean();
                        }
                        String msg;
                        if (response.getStatus() == 1126) {
                            msg = getString(R.string.order_dish_member_disabled);
                        } else {
                            msg = response.getMessage();
                        }
                        EventBus.getDefault().post(new EventReadKeyboard(false, msg));                        loginFail(msg, !needPswd);                    }
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showLongToast(error.getMessage());
                EventBus.getDefault().post(new EventReadKeyboard(false, error.getMessage()));            }
        };
        return LoadingYFResponseListener.ensure(response, getSupportFragmentManager());
    }


    private void setLoginByCardNo(CardLoginResp resp, EcCard card) {
        isLogin = true;
                EcCardKind cardKind = resp.getResult().getCardKind();
        if (cardKind != null && cardKind.priceLimit != null) {
            CustomerManager.getInstance().setCurrentCardIsPriceLimit(cardKind.priceLimit == 2 ? true : false);
        }
                CustomerV5 customerV5 = resp.getResult().getCustomer();
        card.setName(customerV5.getName());
        card.setCardLevel(resp.getResult().getCardLevel());
        card.setCardLevelSetting(resp.getResult().getCardLevelSetting());
        card.setCardSettingDetails(resp.getResult().getCardSettingDetails());
        card.setIntegralAccount(resp.getResult().getIntegralAccount());
        card.setValueCardAccount(resp.getResult().getValueCardAccount());
        card.setCardKind(cardKind);         card.setCustomer(customerV5);
        CustomerResp customerNew = resp.getCustomer();
        customerNew.queryLevelRightInfos();
        TradeCustomer tradeCustomer = CustomerManager.getInstance().getTradeCustomer(card.getCustomer());
        tradeCustomer.setCustomerType(CustomerType.CARD);
        tradeCustomer.setEntitycardNum(card.getCardNum());
        ToastUtil.showShortToast(R.string.customer_login);
        mShowValue.setText("");
                BigDecimal integral = BigDecimal.ZERO;
        if (card.getIntegralAccount() != null && card.getIntegralAccount().getIntegral() != null) {
            integral = BigDecimal.valueOf(card.getIntegralAccount().getIntegral());
        }

                if (mLaunchMode == BeautyCustomerConstants.CustomerLoginLaunchMode.RECHARGE) {
                        showChargingDialog(null, card.getEcCardInfo());
        } else {
            new DinnerCashManager().jumpAfterLogin("", customerNew, null);
        }
        getParentDialogFragment().dismiss();
    }


    private void loginFail(String message, boolean clearInput) {
        ToastUtil.showShortToast(message);
        mShowValue.clearFocus();
        mShowValue.setText("");
        mShowValue.requestFocus();
        mShowValue.findFocus();
    }

    public static Animation shakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(0, 15, 0, 0);
                translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(500);
        return translateAnimation;
    }



    DinnerCustomerLoginBasicDialogFragment getParentDialogFragment() {
        return (DinnerCustomerLoginBasicDialogFragment) getParentFragment();
    }



    private void showFaceGrade() {

    }


    private void showFaceLoginErrorView() {
        mLlLoginLayout.setVisibility(View.GONE);
        if (listener != null) {
            listener.setType(DinnerCustomerLoginDialogFragment.UI_TYPE_FACE_ERROR);
        }
    }



    private void startScanQc() {
        mShowValue.requestFocus();
    }



    public void onEventMainThread(EventOpenIdLoginInfo event) {
        if (event != null) {
            OpenIdLoginInfo openIdLoginInfo = event.getmOpenIdLoginInfo();
            if (openIdLoginInfo.getResult()) {
                if (getParentDialogFragment().requestUuid.equals(openIdLoginInfo.getUuid())) {                    CommCustomer customer = openIdLoginInfo.getCustomer();
                    if (customer != null) {
                        loginByWeChat(customer.getId());
                    }
                }
            }
        }
    }

    public void showCountryDialog(List<ErpCurrency> erpCurrencyList, ErpCurrency currentErpCurrency) {
        if (erpCurrencyList == null || erpCurrencyList.size() == 0) {
            ToastUtil.showShortToast(getString(R.string.customer_erpcurrency_empty));
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
    public void onResume() {
        super.onResume();
        startScanQc();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Click({R.id.card_login, R.id.face_login, R.id.member_register, R.id.btn_retry})
    void onClickOtherType(View view) {
        if (ClickManager.getInstance().isClicked()) {
            return;
        }
        switch (view.getId()) {
            case R.id.card_login:
                MobclickAgentEvent.onEvent(UserActionCode.ZC030017);
                cardLogin();
                break;
            case R.id.face_login:
            case R.id.btn_retry:
                MobclickAgentEvent.onEvent(UserActionCode.ZC030018);
                inputFace();
                break;
            case R.id.member_register:
                if (listener != null) {
                    listener.setType(DinnerCustomerLoginDialogFragment.UI_TYPE_REGISTER);
                }
                break;

            default:
                break;
        }
    }

    @Click({R.id.ll_login_mobile, R.id.ll_login_card})
    void onClickTitle(View view) {
        if (ClickManager.getInstance().isClicked()) {
            return;
        }
        switch (view.getId()) {
            case R.id.ll_login_mobile:
                loginType = LOGIN_MOBILE;
                switchTitle();
                break;
            case R.id.ll_login_card:
                loginType = LOGIN_CARD;
                switchTitle();
                break;
            default:
                break;
        }
    }

    @Click(R.id.rl_area_code)
    void onClickCountry() {
        if (ClickManager.getInstance().isClicked()) {
            return;
        }
        showCountryDialog(new ArrayList<ErpCurrency>(erpCurrencyMap.values()), mErpCurrency);
    }

    @Click(R.id.customer_verification)
    void onClickSubmit() {
        if (ClickManager.getInstance().isClicked()) {
            return;
        }
        VerifyHelper.verifyAlert(getActivity(), BeautyApplication.PERMISSION_CUSTOMER_LOGIN,
                new VerifyHelper.Callback() {
                    @Override
                    public void onPositive(User user, String code, Auth.Filter filter) {
                        super.onPositive(user, code, filter);
                        verification();
                    }
                });
    }


    private void cardLogin() {
        mShowValue.requestFocus();
        DisplayServiceManager.doCancel(getActivity());

    }


    private void inputFace() {

    }




    private void showChargingDialog(CustomerResp customer, EcCardInfo ecCard) {
        CustomerChargingDialogFragment dialogFragment = new CustomerChargingDialogFragment_();
        Bundle args = new Bundle();
        args.putInt(CustomerChargingDialogFragment.KEY_FROM, CustomerChargingDialogFragment.FROM_CARD_BEATY_MAIN);        args.putSerializable(CustomerChargingDialogFragment.KEY_CUSTOMER, customer);
        if (ecCard != null) {
            args.putSerializable(CustomerChargingDialogFragment.KEY_ECCARD, ecCard);
            args.putString(CustomerChargingDialogFragment.KEY_BALANCE, ecCard.getRemainValue() != null ? ecCard.getRemainValue().toString() : "0");
        } else {
            args.putString(CustomerChargingDialogFragment.KEY_BALANCE, customer.remainValue != null ? customer.remainValue.toString() : "0");
        }
        dialogFragment.setArguments(args);
        dialogFragment.show(getParentDialogFragment().getFragmentManager(), "ecCardCharging");
    }


    private void showCustomerCardDialoig(CustomerResp customer) {


        CustomerChargingDialogFragment dialogFragment = new CustomerChargingDialogFragment_();
        Bundle args = new Bundle();
        args.putInt(CustomerChargingDialogFragment.KEY_FROM, CustomerChargingDialogFragment.FROM_CARD_BEATY_MAIN);        args.putSerializable(CustomerChargingDialogFragment.KEY_CUSTOMER, customer);
        args.putString(CustomerChargingDialogFragment.KEY_BALANCE, customer.remainValue != null ? customer.remainValue.toString() : "0");
        dialogFragment.setArguments(args);
        dialogFragment.show(getParentDialogFragment().getFragmentManager(), "ecCardCharging");
    }
}
