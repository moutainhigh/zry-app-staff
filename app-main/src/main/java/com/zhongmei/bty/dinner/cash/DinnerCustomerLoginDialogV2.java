package com.zhongmei.bty.dinner.cash;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.zhongmei.bty.basemodule.auth.application.BeautyApplication;
import com.zhongmei.bty.basemodule.auth.application.CustomerApplication;
import com.zhongmei.bty.basemodule.commonbusiness.utils.ServerAddressUtil;
import com.zhongmei.bty.basemodule.customer.CustomerLogin;
import com.zhongmei.bty.basemodule.customer.dialog.PasswordDialog;
import com.zhongmei.bty.basemodule.customer.dialog.country.CountryDialog;
import com.zhongmei.bty.basemodule.customer.dialog.country.CountryGridAdapter;
import com.zhongmei.bty.basemodule.customer.enums.CustomerLoginType;
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.basemodule.customer.message.MemberCreateResp;
import com.zhongmei.bty.basemodule.customer.message.MemberLoginVoResp;
import com.zhongmei.bty.basemodule.customer.operates.CustomerOperates;
import com.zhongmei.bty.basemodule.customer.operates.interfaces.CustomerDal;
import com.zhongmei.bty.basemodule.database.entity.customer.CommCustomer;
import com.zhongmei.bty.basemodule.database.entity.customer.CustomerV5;
import com.zhongmei.bty.basemodule.database.entity.customer.EventOpenIdLoginInfo;
import com.zhongmei.bty.basemodule.database.entity.customer.OpenIdLoginInfo;
import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCard;
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
import com.zhongmei.bty.basemodule.erp.operates.ErpCommercialRelationDal;
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
import com.zhongmei.bty.customer.event.EventLoginView;
import com.zhongmei.bty.snack.orderdish.selftimepicker.SelfRadioButton;
import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.bean.req.CustomerLoginResp;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.context.util.EncodingHandler;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.CustomerType;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.EventResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.util.DialogUtil;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.util.UserActionCode;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

import de.greenrobot.event.EventBus;

/**
 * @Date： 16/10/18
 * @Description:
 * @Version: 1.0
 */
public class DinnerCustomerLoginDialogV2 extends BasicDialogFragment implements View.OnClickListener {

    private static final String TAG = DinnerCustomerLoginDialogV2.class.getSimpleName();

    public static final String BIRTHDAY_DEFAULT = "1990-9-1";

    private int barcodeWH = 180;// 微信二维码宽度

    private EditTextWithDeleteIcon mShowValue;

    private ImageView mCardLogin;

    private ImageView mFaceLogin;

    private Button mLoginBtn;

    private LinearLayout mLoginLayout;

    private LinearLayout mInputFace;

    private RelativeLayout mRegisterLyaout;

    private EditTextWithDeleteIcon mInputNumber;

    private EditTextWithDeleteIcon mPassWord;

    private EditTextWithDeleteIcon mPassWordSecond;

    private SelfRadioButton mMale;

    private TextView mTvScanDesc;

    private Button mRegisterBtn;

    private Button mCloseBtn;

    private ImageButton mBackBtn;

    private ImageView mRegister;

    private TextView mTitle;

    private RadioGroup mSexGroup;

    private TextView mTvFaceDesc;

    private TextView mTvAreaCode;

    private RelativeLayout mRlAreaCode;

    private EditTextWithDeleteIcon mName;

    private boolean[] settings;

    private String requestUuid;

    private boolean mIsRegister;//是否在注册页面

    private boolean isLogin = false;//判断是否已经登录避免重复登录

    private ScanCodeManager mScanCodeManager;
    private LinearLayout mllErrorView;

    private Button mBtnRetry;


    // 生成二维码
    Bitmap mBitmap = null;

    // 人脸ID
    private String mFaceCode;

    private Integer mSex = CustomerResp.SEX_FEMALE;

    //private FaceFeature mFaceFeature;

    /**
     * 当前商户国籍
     */
    private ErpCurrency mErpCurrency;

    private List<ErpCurrency> mErpCurrencyList;

    private ErpCommercialRelationDal mErpDal;


    public void show(FragmentManager manager, String tag) {
        if (manager != null && !manager.isDestroyed()) {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        }
    }

    private void assignViews(View view) {
        mShowValue = (EditTextWithDeleteIcon) view.findViewById(R.id.phone_number);
        InputFilter[] filters = {new InputFilter.LengthFilter(20)};
        mShowValue.setFilters(filters);
        mShowValue.setHint(getString(R.string.customer_login_hint_enter_phone_or_cardnum));
        mLoginBtn = (Button) view.findViewById(R.id.customer_verification);
        mTvScanDesc = (TextView) view.findViewById(R.id.tvScanDesc_Customer);
        mCardLogin = (ImageView) view.findViewById(R.id.card_login);
        mFaceLogin = (ImageView) view.findViewById(R.id.face_login);
        mLoginLayout = (LinearLayout) view.findViewById(R.id.login_layout);
        mInputFace = (LinearLayout) view.findViewById(R.id.input_face);
        mRegisterLyaout = (RelativeLayout) view.findViewById(R.id.register_layout);
        mInputNumber = (EditTextWithDeleteIcon) view.findViewById(R.id.input_phone_number);
        mPassWord = (EditTextWithDeleteIcon) view.findViewById(R.id.input_password);
        mPassWordSecond = (EditTextWithDeleteIcon) view.findViewById(R.id.input_password_again);
        mRegisterBtn = (Button) view.findViewById(R.id.register_btn);
        mRegister = (ImageView) view.findViewById(R.id.member_register);
        mTitle = (TextView) view.findViewById(R.id.customer_title);
        mSexGroup = (RadioGroup) view.findViewById(R.id.customer_edit_sex_rg);
        mName = (EditTextWithDeleteIcon) view.findViewById(R.id.input_name);
        mMale = (SelfRadioButton) view.findViewById(R.id.male);
        mSexGroup.setOnCheckedChangeListener(onCheckedChangeListener);
        mCloseBtn = (Button) view.findViewById(R.id.clost_btn);
        mllErrorView = (LinearLayout) view.findViewById(R.id.ll_error_view);
        mTvFaceDesc = (TextView) view.findViewById(R.id.tv_face_desc);
        mCloseBtn.setOnClickListener(this);
        mBackBtn = (ImageButton) view.findViewById(R.id.back);
        mBtnRetry = (Button) view.findViewById(R.id.btn_retry);
        mTvAreaCode = (TextView) view.findViewById(R.id.tvAreaCodes);
        mRlAreaCode = (RelativeLayout) view.findViewById(R.id.rlAreaCode);
        mBackBtn.setOnClickListener(this);
        mRegister.setOnClickListener(this);
        mRegisterBtn.setOnClickListener(this);
        mLoginBtn.setOnClickListener(this);
        mCardLogin.setOnClickListener(this);
        mFaceLogin.setOnClickListener(this);
        mBtnRetry.setOnClickListener(this);
        mInputFace.setOnClickListener(this);
        mRlAreaCode.setOnClickListener(this);

    }

    private RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.male:
                    mSex = CustomerResp.SEX_MALE;
                    break;
                case R.id.female:
                    mSex = CustomerResp.SEX_FEMALE;
                    break;
            }
        }
    };

    /**
     * 修改人脸标记
     *
     * @param hasFaceCode
     */
    private void chooseFaceDesc(boolean hasFaceCode) {
        if (hasFaceCode) {
            mTvFaceDesc.setTextColor(getResources().getColor(R.color.color_32ADF6));
            /*if (mFaceFeature != null){
                mTvFaceDesc.setText(getString(R.string.customer_face_approve_on) + (int)mFaceFeature.getBeauty() + getString(R.string.cent));
            } else {
                mTvFaceDesc.setText(R.string.customer_face_approve_on);
            }*/
        } else {
            mTvFaceDesc.setTextColor(getResources().getColor(R.color.color_bcbcbc));
            mTvFaceDesc.setText(R.string.customer_face_approve_off);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queryErpCurrenctList();
    }

    private void queryErpCurrenctList() {
        mErpDal = OperatesFactory.create(ErpCommercialRelationDal.class);
        mErpCurrencyList = mErpDal.queryErpCurrenctList();
        String areaCode = ShopInfoCfg.getInstance().getCurrency().getAreaCode();
        if (!TextUtils.isEmpty(areaCode)) {
            mErpCurrency = mErpDal.queryErpCurrenctByAreaCode(areaCode);
        }
    }

    private void setupCountryView() {
        if (mErpCurrency != null) {
            mTvAreaCode.setText(mErpCurrency.getAreaCode());
        }
    }

    /**
     * 显示国籍dialog
     *
     * @param erpCurrencyList
     * @param currentErpCurrency
     */
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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.comm_customer_login2, container);
        getDialog().setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        assignViews(view);
        setupCountryView();
        mInputNumber.addTextChangedListener(mRegisterTextWatcher);
        mPassWord.addTextChangedListener(mRegisterTextWatcher);
        mPassWordSecond.addTextChangedListener(mRegisterTextWatcher);
        mShowValue.addTextChangedListener(mTextWatcher);
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
        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new EventLoginView(EventLoginView.TYPE_LOGIN_VIEW_START));//add v8.4
        isShowQrCode();
//        startScanQc();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        startScanQc();
    }

    @Override
    public void onPause() {
        if (mScanCodeManager != null) { // 关闭扫码枪
            mScanCodeManager.stop();
        }
        super.onPause();
    }

    /**
     * 判断是否显示二维码
     */
    private void isShowQrCode() {
        CustomerDal dal = OperatesFactory.create(CustomerDal.class);
        settings = new boolean[2];
        try {
            settings[0] = dal.getLoginQrCodeSetting();
            settings[1] = dal.getopenIdRegisterSetting();
        } catch (SQLException e) {
            Log.e(TAG, "", e);
        }
        if (settings[0]) {//没有开通微信公众号
            if (mIsRegister) {
                if (settings[1]) {//打开了关注即会员开关
                    mTvScanDesc.setText(getString(R.string.customer_login_desc_2));
                } else {
                    mTvScanDesc.setText(getString(R.string.customer_login_desc_1));
                }
            } else {
                mTvScanDesc.setText(getString(R.string.customer_login_desc_2));
            }
        } else {
            mTvScanDesc.setText(getString(R.string.customer_login_desc_1));
        }
        showSecondDisPlay();
    }

    /**
     * 控制副二屏的显示
     */
    private void showSecondDisPlay() {
        if (settings == null) {
            /*DisplayUserInfo dUserInfo =
                    DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_ACCOUNT_INPUT, "", null, 0, true, 0);
            DisplayServiceManager.updateDisplay(getActivity(), dUserInfo);*/
            return;
        }
        boolean secondDisPlayQrCode = true;
        if (settings[0]) {//开通微信公众号
            if (mIsRegister && !settings[1]) {//关注即会员开关关闭
                secondDisPlayQrCode = false;
            }
        } else {
            secondDisPlayQrCode = false;
        }
        try {
            if (secondDisPlayQrCode) {
                //扫描二维码后请求地址
                if (mBitmap == null) {
                    requestUuid = SystemUtils.genOnlyIdentifier();
                    String url = ServerAddressUtil.getInstance().getOpenIdUrl() + requestUuid;
                    mBitmap = EncodingHandler.createQRCode(url, barcodeWH);
                }
            }

        } catch (WriterException e) {
            Log.e(TAG, "", e);
        }
        String phone = mShowValue.getText().toString();
        if (secondDisPlayQrCode) {//有二维码
            //DisplayServiceManager.doUpdateLoginInfo(getActivity(), DisPlayLoginInfo.COMMAND_NORMAL, phone, mBitmap, mIsRegister);
        } else {
            if (mIsRegister) {//注册页没有二维码展示广告
                DisplayServiceManager.doCancel(getActivity());
            } else {//展示手机号输入
                /*DisplayUserInfo dUserInfo =
                        DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_ACCOUNT_INPUT, phone, null, 0, true, 0);
                DisplayServiceManager.updateDisplay(getActivity(), dUserInfo);*/
            }

        }
    }

    /**
     * OpenId登录
     */
    public void onEventMainThread(EventOpenIdLoginInfo event) {
        if (event != null) {
            OpenIdLoginInfo openIdLoginInfo = event.getmOpenIdLoginInfo();
            if (openIdLoginInfo.getResult()) {
//                Customer customer = saveCustomer(openIdLoginInfo.getCustomer());
                if (requestUuid.equals(openIdLoginInfo.getUuid())) {//同一次登录请求
                    CommCustomer customer = openIdLoginInfo.getCustomer();
                    if (customer != null) {
                        loginByPhoneNo(customer.getMobile(), customer.getId());
                    }
                }
            }
        }
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
            if (inputStr.endsWith("\n")) {//处理回车事件
                verification();
                return;
            }
            if (!TextUtils.isEmpty(mShowValue.getText())) {
                mLoginBtn.setEnabled(true);
                mLoginBtn.setBackgroundResource(R.drawable.orderdish_clear_status_bottom_button_bg_selector);
            } else {
                mShowValue.setHint(R.string.customer_login_hint_enter_phone_or_cardnum);
                mLoginBtn.setEnabled(false);
                mLoginBtn.setBackgroundResource(R.drawable.orderdish_clear_status_select_all_not_enabled);
            }
            showSecondDisPlay();
        }
    };

    private TextWatcher mRegisterTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(mInputNumber.getText().toString().trim())
                    && !TextUtils.isEmpty(mPassWord.getText().toString().trim())
                    && !TextUtils.isEmpty(mPassWordSecond.getText().toString().trim())) {
                mRegisterBtn.setEnabled(true);
                mRegisterBtn.setBackgroundResource(R.drawable.orderdish_clear_status_bottom_button_bg_selector);
            } else {
                mRegisterBtn.setEnabled(false);
                mRegisterBtn.setBackgroundResource(R.drawable.orderdish_clear_status_select_all_not_enabled);
            }

        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
            //设置宽高
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.width = DensityUtil.dip2px(MainApplication.getInstance(), MainApplication.getInstance().getResources().getInteger(R.integer.customer_comm_weight));
            window.setAttributes(attributes);
        }
    }

    @Override
    public void onClick(View v) {
        if (ClickManager.getInstance().isClicked()) {
            return;
        }
        switch (v.getId()) {
            case R.id.register_btn:
                MobclickAgentEvent.onEvent(UserActionCode.ZC030019);
                if (checkRegisterInfo()) {
                    CustomerResp customer = createCustomer();
                    doCreateCustomer(customer);
                }
                break;
            case R.id.member_register:
                memberRegiest();
                break;
            case R.id.customer_verification:
                VerifyHelper.verifyAlert(getActivity(), BeautyApplication.PERMISSION_CUSTOMER_LOGIN,
                        new VerifyHelper.Callback() {
                            @Override
                            public void onPositive(User user, String code, Auth.Filter filter) {
                                super.onPositive(user, code, filter);
                                verification();
                            }
                        });
                break;
            case R.id.clost_btn:
                mShowValue.setText("");
                View view = getDialog().getWindow().peekDecorView();
                if (view != null) {
                    InputMethodManager inputmanger = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                dismiss();
                break;
            case R.id.back:
                back();
                break;
            case R.id.card_login:
                MobclickAgentEvent.onEvent(UserActionCode.ZC030017);
                cardLogin();
                break;
            case R.id.scan_login:
                startScanQc();
                break;
            case R.id.face_login:
                MobclickAgentEvent.onEvent(UserActionCode.ZC030018);
                inputFace(R.id.face_login);
                break;
            case R.id.input_face:
                inputFace(R.id.input_face); // 录入人脸
                break;
            case R.id.btn_retry:
                inputFace(R.id.btn_retry); // 录入人脸
                break;
            case R.id.rlAreaCode:
                showCountryDialog(mErpCurrencyList, mErpCurrency);
                break;
            default:
                break;
        }
    }

    private void inputFace(int id) {
        /*boolean available= BaiduFaceRecognition.getInstance().checkFaceServer();
        if(!available){
            FacecognitionActivity.showFaceServerWarmDialog(getContext(),getChildFragmentManager());
            return;
        }

        if (id == R.id.face_login || id == R.id.btn_retry){
            startActivityForResult(BaiduFaceRecognition.getInstance().getRecognitionFaceIntent(true),FaceRequestCodeConstant.RC_DINNER_CUSTOMER_LOGIN);
        } else if (id == R.id.input_face){
            startActivityForResult(BaiduFaceRecognition.getInstance().getRegistFaceIntent(true),FaceRequestCodeConstant.RC_DINNER_CUSTOMER_REGIEST);
        }*/
    }

    private void faceLogin() {
        // 人脸登录成功回调调用接口
        loginCustomerByFaceCode(mFaceCode, getResponseMemberLogin(0, null));
    }

    /**
     * 会员注册
     */
    private void memberRegiest() {
        VerifyHelper.verifyAlert(getActivity(), CustomerApplication.PERMISSION_CUSTOMER_CREATE,
                new VerifyHelper.Callback() {
                    @Override
                    public void onPositive(User user, String code, Auth.Filter filter) {
                        super.onPositive(user, code, filter);
                        mIsRegister = true;
                        isShowQrCode();
                        showRegiestView();
                    }
                });
        return;
    }

    /**
     * 显示注册的view
     */
    private void showRegiestView() {
        mllErrorView.setVisibility(View.GONE);
        mTitle.setText(getString(R.string.member_register));
        mTvScanDesc.setVisibility(View.GONE);
        mLoginLayout.setVisibility(View.GONE);
        mCloseBtn.setVisibility(View.GONE);
        mRegisterLyaout.setVisibility(View.VISIBLE);
        mBackBtn.setVisibility(View.VISIBLE);
    }

    /**
     * 显示登录的view
     */
    private void showLoginView() {
        mllErrorView.setVisibility(View.GONE);
        mTitle.setText(getString(R.string.customer_menber_login));
        mRegisterLyaout.setVisibility(View.GONE);
        mBackBtn.setVisibility(View.GONE);
        mLoginLayout.setVisibility(View.VISIBLE);
        mCloseBtn.setVisibility(View.VISIBLE);
        mTvScanDesc.setVisibility(View.VISIBLE);
        mInputNumber.setText("");
        mPassWord.setText("");
        mPassWordSecond.setText("");
        mName.setText("");
        mMale.setChecked(true);
        chooseFaceDesc(false);
    }

    /**
     * 加载人脸登录错误view
     */
    private void showFaceLoginErrorView() {
        mLoginLayout.setVisibility(View.GONE);
        mTvScanDesc.setVisibility(View.GONE);
        mllErrorView.setVisibility(View.VISIBLE);
        mTitle.setText(R.string.customer_login_face_title);
        mBackBtn.setVisibility(View.VISIBLE);
        mCloseBtn.setVisibility(View.VISIBLE);
    }


    /**
     * 返回
     */
    private void back() {
        mFaceCode = "";
        mIsRegister = false;
        isShowQrCode();
        showLoginView();
    }

    /**
     * 刷卡登录
     */
    private void cardLogin() {
        mShowValue.requestFocus();
        DisplayServiceManager.doCancel(getActivity());
        /*ReadCardDialogFragment readCardDialogFragment = new ReadCardDialogFragment.UionCardDialogFragmentBuilder()
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
                        });
        readCardDialogFragment.show(getChildFragmentManager(), "get_cardno");
        readCardDialogFragment.setCloseListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSecondDisPlay();
            }
        });*/
    }

    /**
     * 开启扫码
     */
    private void startScanQc() {
        mShowValue.requestFocus();
        startScan();
    }

    /**
     * 检验注册信息
     */
    private boolean checkRegisterInfo() {
        if (mErpCurrency == null) {
            ToastUtil.showShortToast(getString(R.string.toast_customer_create_tel_limit));
            return false;
        }
        String number = mInputNumber.getText().toString().trim(); // 手机
        if (TextUtils.isEmpty(number)) {
            ToastUtil.showShortToast(R.string.customer_no_phone);
            return false;
        }
        if (!TextUtils.isEmpty(mErpCurrency.getPhoneRegulation()) && !Pattern.matches(mErpCurrency.getPhoneRegulation(), number)) {
            ToastUtil.showShortToast(getString(R.string.customer_mobile_regulation_error));
            return false;
        }
        String passwrod = mPassWord.getText().toString();
        String passwrodSecond = mPassWordSecond.getText().toString();
        if (TextUtils.isEmpty(passwrod)) {
            ToastUtil.showShortToast(R.string.customer_no_password);
            return false;
        } else if (passwrod.length() != 6) {
            ToastUtil.showShortToast(R.string.customer_password_length_6);
            return false;
        } else if (!passwrod.equals(passwrodSecond)) {
            ToastUtil.showShortToast(R.string.customer_password_not_match);
            return false;
        }
        return true;
    }

    /**
     * 生成customer
     */
    private CustomerResp createCustomer() {
        CustomerResp customer = new CustomerResp();
        String number = mInputNumber.getText().toString().trim();
        customer.mobile = number;
        String passwrod = mPassWord.getText().toString();
        customer.password = passwrod;
        customer.birthday = BIRTHDAY_DEFAULT;
        String name = mName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            customer.customerName = getString(R.string.phone_no_name);
        } else {
            customer.customerName = name;
        }
        customer.sex = mSex;
        if (!TextUtils.isEmpty(mFaceCode)) {
            customer.faceCode = mFaceCode;
        }
        if (mErpCurrency != null) {
            customer.nation = mErpCurrency.getCountryEn();
            customer.country = mErpCurrency.getCountryZh();
            customer.nationalTelCode = mErpCurrency.getAreaCode();
        }
        return customer;
    }

    /**
     * 新增会员
     *
     * @param
     */
    private void doCreateCustomer(final CustomerResp customer) {
        CustomerOperates oper = OperatesFactory.create(CustomerOperates.class);
        ResponseListener<MemberCreateResp> listener = new ResponseListener<MemberCreateResp>() {

            @Override
            public void onResponse(ResponseObject<MemberCreateResp> response) {
                if (ResponseObject.isOk(response)) {
                    if (response.getContent().isOk()) {
                        ToastUtil.showShortToast(getString(R.string.register_success));
                        final CustomerResp customerNew = response.getContent().getResult().getCustomer();
                        new AsyncTask<Void, Void, Void>() {

                            @Override
                            protected Void doInBackground(Void... params) {
                                customerNew.queryLevelRightInfos();
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void params) {
                                customerNew.setInitialValue();
                                customerNew.queryLevelRightInfos();
                                customerNew.needRefresh = false;
                                TradeCustomer tradeCustomer = CustomerManager.getInstance().getTradeCustomer(customerNew);
                                if (!customerNew.isMember()) {
                                    tradeCustomer.setCustomerType(CustomerType.CUSTOMER);
                                } else if (customerNew.card == null) {
                                    tradeCustomer.setCustomerType(CustomerType.MEMBER);
                                } else {
                                    tradeCustomer.setCustomerType(CustomerType.CARD);
                                    tradeCustomer.setEntitycardNum(customerNew.card.getCardNum());
                                }
                                mShowValue.setText("");
                                showRegisterSuccessTips(customerNew);
                                // 登录成功后第二屏显示用户信息
                                /*DisplayUserInfo dUserInfo = DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_USERINFO_SHOW, customerNew, customerNew.integral, false, 0);
                                DisplayServiceManager.updateDisplay(getActivity(), dUserInfo);*/
                                EventBus.getDefault().post(new EventReadKeyboard(true, ""));// 发送成功到ReadKeyboardDialogFragment
                                DinnerPriviligeItemsFragment.showDisplayUserInfo(getActivity());
                                new DinnerCashManager().jumpAfterLogin("", customerNew, null);
                                /*if (mFaceFeature != null){
                                    DinnerShopManager.getInstance().getLoginCustomer().faceGrade = (int)mFaceFeature.getBeauty();
                                }*/
                            }
                        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        ToastUtil.showShortToast(response.getContent().getErrorMessage());
                    }
                } else {
                    ToastUtil.showShortToast(response.getMessage());
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showShortToast(error.getMessage());
            }

        };
        oper.createCustomerV2(customer, "", LoadingResponseListener.ensure(listener, getChildFragmentManager()));

    }

    /**
     * 开始扫码并获取焦点
     */
    private void startScan() {
        mScanCodeManager = new ScanCodeManager(getActivity(), mShowValue, true);
        mScanCodeManager.start(new ScanCode.ScanCodeReceivedListener() {
            @Override
            public void onScanCodeReceived(String data) {
                if (!TextUtils.isEmpty(data)) {
                    UserActionEvent.start(UserActionEvent.DINNER_PAY_LOGIN_SCAN_CODE);
                    loginByWeixin(data);        //微信登录
                }
            }
        });
        // add v8.8 begin
        DeWoScanCode.getInstance().registerReceiveDataListener(new DeWoScanCode.OnReceiveDataListener() {
            @Override
            public void onReceiveData(String data) {
                UserActionEvent.start(UserActionEvent.DINNER_PAY_LOGIN_SCAN_CODE);
                loginByWeixin(data);        //微信登录
            }
        });
        // add v8.8 end
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
                    if (resp.customerIsDisable()) {//当前账号冻结
                        ToastUtil.showShortToast(R.string.order_dish_member_disabled);
                    } else {
                        isLogin = true;
                        CustomerResp customerNew = resp.getCustomer();
                        customerNew.setInitialValue();
                        customerNew.queryLevelRightInfos();
                        CustomerManager.getInstance().setLoginCustomer(customerNew);
                        TradeCustomer tradeCustomer = CustomerManager.getInstance().getTradeCustomer(customerNew);
                        tradeCustomer.setCustomerType(CustomerType.MEMBER);
                        ToastUtil.showShortToast(R.string.customer_login);
                        // 登录成功后第二屏显示用户信息
                        /*DisplayUserInfo dUserInfo = DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_USERINFO_SHOW, customerNew, customerNew.integral, false, 0);
                        DisplayServiceManager.updateDisplay(getActivity(), dUserInfo);*/
                        //数据设置
                        EventBus.getDefault().post(new EventReadKeyboard(true, ""));// 发送成功到ReadKeyboardDialogFragment
                        DinnerPriviligeItemsFragment.showDisplayUserInfo(getActivity());
                        new DinnerCashManager().jumpAfterLogin("", customerNew, null);
                        DinnerShopManager.getInstance().getShoppingCart().setOpenIdenty(resp.getOpenId());
                        dismiss();
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

    /**
     * 校验
     */
    private void verification() {
        final String inputNo = mShowValue.getText().toString().replace("\n", "").trim();
        if (TextUtils.isEmpty(inputNo)) {
            ToastUtil.showShortToast(R.string.customer_login_hint);
            return;
        }
        getCardBaseInfo(inputNo); // v8.2支持刷卡登录会员，先校验卡，在校验手机
    }

    @Override
    public void onDestroy() {
        if (DinnerShopManager.getInstance().getLoginCustomer() == null) {
            DisplayServiceManager.doCancel(getActivity());
        }
        EventBus.getDefault().post(new EventLoginView(EventLoginView.TYPE_LOGIN_VIEW_END));//add v8.4
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 使用会员卡号登录
     *
     * @Title: loginByCardNo
     * @Return void 返回类型
     */
    private void loginByCardNo(final String inputNo) {
        CustomerOperates operates = OperatesFactory.create(CustomerOperates.class);
        ResponseListener<CardLoginResp> listener = new EventResponseListener<CardLoginResp>(UserActionEvent.DINNER_PAY_LOGIN_SWIPE_CARD) {

            @Override
            public void onResponse(ResponseObject<CardLoginResp> response) {
                if (ResponseObject.isOk(response)) {
                    final CardLoginResp resp = response.getContent();
                    // 设置card的名称，从customer中获得
                    final EcCard card = resp.getResult().getCardInstance();
                    if (card.getCardType() == EntityCardType.ANONYMOUS_ENTITY_CARD) {
                        ToastUtil.showShortToast(R.string.customer_login_not_member);
                        mShowValue.setHint(R.string.customer_login_not_member);
                        mShowValue.setBackgroundResource(R.drawable.customer_edit_error_bg);
                        /*DisplayUserInfo dUserInfo = DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_VALIDATE_USER_FAIL, "", null, 0, true, 0); // 副屏显示
                        DisplayServiceManager.updateDisplay(getActivity(), dUserInfo);*/
                        mShowValue.requestFocus();
                        mShowValue.setText("");
                        mShowValue.startAnimation(shakeAnimation(6));
                    } else {
                        if (isLogin) {
                            return;
                        }
                        if (card.getCardStatus() != CardStatus.ACTIVATED) {
                            ToastUtil.showShortToast(R.string.card_disable);
                        } else {
                            EcCardKind ecCardKind = resp.getResult().getCardKind();
                            if (ecCardKind.getIsNeedPwd() == Bool.YES && ServerSettingManager.isCommercialNeedVerifPassword()) {
                                CustomerLogin.showMemberPasswordDialog(getActivity(), inputNo, new CustomerLogin.DinnerLoginListener() {
                                    @Override
                                    public void login(final PasswordDialog dialog, int needPswd, String password) {
                                        loginByPhoneNoOrId(null, resp.getResult().getCustomer().getCustomerid(), needPswd, password, new ResponseListener<MemberLoginVoResp>() {
                                            @Override
                                            public void onResponse(ResponseObject<MemberLoginVoResp> response) {
                                                if (ResponseObject.isOk(response) && MemberLoginVoResp.isOk(response.getContent())) {
                                                    dialog.dismiss();
                                                    setLoginByCardNo(resp, card);
                                                } else {
                                                    String msg;
                                                    if (response.getStatusCode() == 1126) {
                                                        msg = getString(R.string.order_dish_member_disabled);
                                                    } else {
                                                        msg = response.getContent() == null ? getString(R.string.display_login_error) : response.getContent().getErrorMessage();
                                                    }

                                                    loginFail(msg, false);//不需要验证密码的模式下，要清空输入框
                                                }
                                            }

                                            @Override
                                            public void onError(VolleyError error) {
                                                ToastUtil.showShortToast(error.getMessage());
                                            }
                                        });
                                    }
                                });
                            } else {
                                setLoginByCardNo(resp, card);
                            }
                        }
                    }
                } else {
                    String message = response.getMessage();
                    // 副屏显示
                    loginFail(message, false);
                }
                UserActionEvent.end(getEventName());
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showShortToast(error.getMessage());
            }
        };
        operates.cardLogin(inputNo, LoadingResponseListener.ensure(listener, getChildFragmentManager()));
    }

    private void setLoginByCardNo(CardLoginResp resp, EcCard card) {
        isLogin = true;
        //add v8.2 start 获取当前登录的卡是否开启会员价限制开关
        EcCardKind cardKind = resp.getResult().getCardKind();
        if (cardKind != null && cardKind.priceLimit != null) {
            CustomerManager.getInstance().setCurrentCardIsPriceLimit(cardKind.priceLimit == 2 ? true : false);
        }
        //add v8.2 end  获取当前登录的卡是否开启会员价限制开关
        CustomerV5 customerV5 = resp.getResult().getCustomer();
        card.setName(customerV5.getName());
        card.setCardLevel(resp.getResult().getCardLevel());
        card.setCardLevelSetting(resp.getResult().getCardLevelSetting());
        card.setCardSettingDetails(resp.getResult().getCardSettingDetails());
        card.setIntegralAccount(resp.getResult().getIntegralAccount());
        card.setValueCardAccount(resp.getResult().getValueCardAccount());
        card.setCardKind(cardKind); // 8.3添加  结账界面 储值屏蔽售卡即激活的卡片 的 充值
        card.setCustomer(customerV5);
        CustomerResp customerNew = resp.getCustomer();
        customerNew.queryLevelRightInfos();
        TradeCustomer tradeCustomer = CustomerManager.getInstance().getTradeCustomer(card.getCustomer());
        tradeCustomer.setCustomerType(CustomerType.CARD);
        tradeCustomer.setEntitycardNum(card.getCardNum());
        ToastUtil.showShortToast(R.string.customer_login);
        mShowValue.setText("");
        // 如果有积分抵现
        BigDecimal integral = BigDecimal.ZERO;
        if (card.getIntegralAccount() != null && card.getIntegralAccount().getIntegral() != null) {
            integral = BigDecimal.valueOf(card.getIntegralAccount().getIntegral());
        }
        /*DisplayUserInfo dUserInfo = DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_USERINFO_SHOW, customerNew, integral.longValue(), false, 0);// 登录成功后第二屏显示用户信息
        DisplayServiceManager.updateDisplay(getActivity(), dUserInfo);*/
        //跳转
        new DinnerCashManager().jumpAfterLogin("", customerNew, null);
        dismiss();
    }

    /**
     * 使用手机号登录,默认不需要密码验证
     *
     * @Title: loginByPhoneNo
     * @Return void 返回类型
     */
    private void loginByPhoneNo(final String inputNo, final Long customerId) {
        loginByPhoneNoOrId(inputNo, customerId, CustomerManager.NOT_NEED_PSWD, null, (PasswordDialog) null);
    }

    private void loginByPhoneNoOrId(final String inputNo, final Long customerId, final int needPswd, final String pswd, final PasswordDialog dialog) {
        loginByPhoneNoOrId(inputNo, customerId, needPswd, pswd, getResponseMemberLogin(needPswd, dialog));
    }

    /**
     * 登录成功回调
     *
     * @param needPswd
     * @param dialog
     * @return
     */
    private ResponseListener<MemberLoginVoResp> getResponseMemberLogin(final int needPswd, final PasswordDialog dialog) {
        ResponseListener<MemberLoginVoResp> response = new ResponseListener<MemberLoginVoResp>() {
            @Override
            public void onResponse(ResponseObject<MemberLoginVoResp> response) {
                try {
                    if (ResponseObject.isOk(response) && MemberLoginVoResp.isOk(response.getContent())) {
                        if (isLogin) {
                            return;
                        }
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        CustomerLoginResp resp = response.getContent().getResult();
                        if (resp.customerIsDisable()) {//当前账号冻结
                            ToastUtil.showShortToast(R.string.order_dish_member_disabled);
                        } else {
                            isLogin = true;
                            CustomerResp customerNew = resp.getCustomer();
                            customerNew.setInitialValue();
                            customerNew.queryLevelRightInfos();
                            customerNew.needRefresh = false;
                            customerNew.customerLoginType = response.getContent().getCustomerLoginType();
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
                            // 登录成功后第二屏显示用户信息
                            /*DisplayUserInfo dUserInfo = DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_USERINFO_SHOW, customerNew, customerNew.integral, false, 0);
                            if(mFaceFeature!=null) {
                                dUserInfo.setFaceFeature(mFaceFeature.getFaceType(), mFaceFeature.getFaceTypeDescribe(), mFaceFeature.getFaceScoreDescribe(), mFaceFeature.getGender());
                                customerNew.mFaceFeatureVo=mFaceFeature.getFaceFeatureVo();
                            }
                            DisplayServiceManager.updateDisplay(getActivity(), dUserInfo);*/
                            EventBus.getDefault().post(new EventReadKeyboard(true, ""));// 发送成功到ReadKeyboardDialogFragment
                            DinnerPriviligeItemsFragment.showDisplayUserInfo(getActivity());
                            new DinnerCashManager().jumpAfterLogin("", customerNew, null);
                            DinnerShopManager.getInstance().getShoppingCart().setOpenIdenty(resp.getOpenId());
                            if (response.getContent().getCustomerLoginType() == CustomerLoginType.FACE_CODE) {
                                //DinnerShopManager.getInstance().getLoginCustomer().faceGrade = (int)mFaceFeature.getBeauty();
                                showFaceGrade();
                            } else {
                                dismiss();
                            }
                        }
                    } else {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.clean();
                        }
                        String msg;
                        if (response.getStatusCode() == 1126) {
                            msg = getString(R.string.order_dish_member_disabled);
                        } else {
//                            msg = response.getContent() == null ? getString(R.string.display_login_error) : response.getContent().getErrorMessage();
                            msg = response.getContent().getErrorMessage();
                        }
                        EventBus.getDefault().post(new EventReadKeyboard(false, msg));// 发送失败到ReadKeyboardDialogFragment
                        loginFail(msg, needPswd != 1);//不需要验证密码的模式下，要清空输入框
                        if (response.getContent().getCustomerLoginType() == CustomerLoginType.FACE_CODE) {
                            showFaceLoginErrorView();
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showLongToast(error.getMessage());
                EventBus.getDefault().post(new EventReadKeyboard(false, error.getMessage()));// 发送失败到ReadKeyboardDialogFragment
            }
        };
        return response;
    }

    /**
     * 人脸评分
     */
    private void showFaceGrade() {
        /*if (mFaceFeature == null){
            return;
        }
        FaceGradeDialogFragment dialogFragment = new FaceGradeDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(FaceGradeDialogFragment.KEY_FACE_FEATURE , mFaceFeature);
        dialogFragment.setArguments(bundle);
        dialogFragment.setOnFaceGradeListener(new FaceGradeDialogFragment.OnFaceGradeListener() {
            @Override
            public void onDismissListener() {
                dismiss();
            }
        });
        dialogFragment.show(getChildFragmentManager() , "showFaceGrade");*/
    }

    /**
     * 登录通过手机号或者CustomerId
     *
     * @param inputNo
     * @param customerId
     * @param needPwd
     * @param pswd
     * @param callback
     */
    private void loginByPhoneNoOrId(final String inputNo, final Long customerId, final int needPwd, final String pswd, final ResponseListener<MemberLoginVoResp> callback) {
        CustomerLoginType loginType;
        String loginId;
        if (customerId != null && customerId != 0) {
            loginType = CustomerLoginType.MEMBER_ID;
            loginId = customerId.toString();
        } else {
            loginType = CustomerLoginType.MOBILE;
            loginId = inputNo;
        }
        loginCustomer(loginType, loginId, needPwd, pswd, callback, UserActionEvent.DINNER_PAY_LOGIN_INPUT_MOBILE);
    }

    /**
     * 人脸登录
     *
     * @param faceCode 人脸ID
     * @param callback
     */
    private void loginCustomerByFaceCode(String faceCode, final ResponseListener<MemberLoginVoResp> callback) {
        CustomerManager customerManager = CustomerManager.getInstance();
        customerManager.customerLogin(CustomerLoginType.FACE_CODE, faceCode, "", false, false, true, LoadingResponseListener.ensure(getResponseListener(CustomerLoginType.FACE_CODE, callback, UserActionEvent.DINNER_PAY_LOGIN_INPUT_FACE), getChildFragmentManager()));
    }

    /**
     * 顾客登录
     *
     * @param loginType
     * @param loginId
     * @param needPwd
     * @param pswd
     * @param callback
     * @param eventName
     */
    private void loginCustomer(CustomerLoginType loginType, String loginId, int needPwd, String pswd, final ResponseListener<MemberLoginVoResp> callback, final UserActionEvent eventName) {
        CustomerManager customerManager = CustomerManager.getInstance();
        customerManager.customerLogin(loginType, loginId, pswd, needPwd == 1, false, true, LoadingResponseListener.ensure(getResponseListener(loginType, callback, eventName), getChildFragmentManager()));
    }

    private ResponseListener<MemberLoginVoResp> getResponseListener(final CustomerLoginType customerLoginType, final ResponseListener<MemberLoginVoResp> callback, UserActionEvent eventName) {
        ResponseListener<MemberLoginVoResp> listener = new EventResponseListener<MemberLoginVoResp>(eventName) {
            @Override
            public void onResponse(ResponseObject<MemberLoginVoResp> response) {
                if (callback != null) {
                    response.getContent().setCustomerLoginType(customerLoginType);
                    callback.onResponse(response);
                }
                UserActionEvent.end(getEventName());
            }

            @Override
            public void onError(VolleyError error) {
                if (callback != null) {
                    callback.onError(error);
                }
            }
        };
        return listener;
    }

    public static Animation shakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(0, 15, 0, 0);
        // 设置一个循环加速器，使用传入的次数就会出现摆动的效果。
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(500);
        return translateAnimation;
    }

    /**
     * 登录失败的响应
     */
    private void loginFail(String message, boolean clearInput) {
        ToastUtil.showShortToast(message);
        if (settings != null && settings[0]) {
            /*if (clearInput) {
                DisplayServiceManager.doUpdateLoginInfo(getActivity(), DisPlayLoginInfo.COMMAND_ERROR, "", mBitmap, mIsRegister);
            } else {
                String phone = mShowValue.getText().toString();
                DisplayServiceManager.doUpdateLoginInfo(getActivity(), DisPlayLoginInfo.COMMAND_ERROR, phone, mBitmap, mIsRegister);
            }*/
        } else {
            /*DisplayUserInfo dUserInfo = DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_VALIDATE_USER_FAIL, "", null, 0, true, 0);
            DisplayServiceManager.updateDisplay(getActivity(), dUserInfo);*/
        }
        if (clearInput) {
            mShowValue.setBackgroundResource(R.drawable.customer_edit_error_bg);
            mShowValue.requestFocus();
            mShowValue.setText("");
            mShowValue.startAnimation(shakeAnimation(6));
        }
    }


    @Override
    public void dismiss() {
        if (getChildFragmentManager() != null && this.isVisible()) {//修复线上bug，apm线上bug：getFragmentManager返回null.
            super.dismiss();
        } else {
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
        }
    }

    /**
     * 获取卡的基本信息
     */
    private void getCardBaseInfo(final String inputNo) {
        UserActionEvent.start(UserActionEvent.DINNER_PAY_LOGIN_SWIPE_CARD);
        CustomerOperates operates = OperatesFactory.create(CustomerOperates.class);
        ResponseListener<CardBaseInfoResp> listener =
                LoadingResponseListener.ensure(new EventResponseListener<CardBaseInfoResp>(UserActionEvent.DINNER_PAY_LOGIN_SWIPE_CARD) {

                                                   @Override
                                                   public void onResponse(ResponseObject<CardBaseInfoResp> response) {
                                                       if (ResponseObject.isOk(response)) {
                                                           CardBaseInfoResp resp = response.getContent();
                                                           final CardBaseInfoResp.BaseCardInfo baseCardInfo = resp.getResult();
                                                           if (baseCardInfo != null) {
                                                               if (baseCardInfo.getCardStatus() == CardStatus.ACTIVATED) {
                                                                   if (baseCardInfo.getCardType() == EntityCardType.GENERAL_CUSTOMER_CARD) {
                                                                       if (baseCardInfo.getCustomerId() != null && !TextUtils.isEmpty(baseCardInfo.getCustomerId().toString())) {
                                                                           showPasswordDialog(baseCardInfo.getCardNum(), baseCardInfo.getCustomerId());
                                                                       } else {
                                                                           UserActionEvent.end(UserActionEvent.DINNER_PAY_LOGIN_SWIPE_CARD);
                                                                       }
                                                                   } else {
                                                                       loginByCardNo(baseCardInfo.getCardNum());
                                                                   }
                                                               } else {
                                                                   String tips = CustomerManager.getInstance().getStatusName(baseCardInfo.getCardStatus());
                                                                   ToastUtil.showShortToast(getString(R.string.card_error_str, tips));
                                                               }
                                                           } else {
                                                               UserActionEvent.end(UserActionEvent.DINNER_PAY_LOGIN_SWIPE_CARD);
                                                               showPasswordDialog(inputNo, null);
                                                           }
                                                       } else {
                                                           UserActionEvent.end(UserActionEvent.DINNER_PAY_LOGIN_SWIPE_CARD);
                                                           showPasswordDialog(inputNo, null);
                                                       }
                                                   }

                                                   @Override
                                                   public void onError(VolleyError error) {
                                                       ToastUtil.showShortToast(error.getMessage());
                                                   }
                                               },
                        getChildFragmentManager());
        operates.getCardBaseInfo(inputNo, listener);
    }

    private void showPasswordDialog(final String inputNo, final Long customerId) {
        CustomerLogin.dinnerLoginByPhoneNo(getActivity(), inputNo, new CustomerLogin.DinnerLoginListener() {
            @Override
            public void login(PasswordDialog dialog, int needPswd, String password) {
                loginByPhoneNoOrId(inputNo, customerId, needPswd, password, dialog);
            }
        });
    }

    /**
     * 注册成功提示框
     */
    private void showRegisterSuccessTips(final CustomerResp customerNew) {
        DinnerCustomerLoginSaveDialog dialog = new DinnerCustomerLoginSaveDialog_();
        dialog.setCustomer(customerNew);
        dialog.setOnClick(new DinnerCustomerLoginSaveDialog.OnDialogBtnClickListener() {
            @Override
            public void cardStoreBtnClick() {
                showChargingDialog(customerNew, String.valueOf(customerNew.remainValue));
                dismiss();
            }

            @Override
            public void entityCardBtnClick() {
                /*Intent intent = new Intent(getContext(), CumtomerSaleCardsActivity_.class);
                intent.putExtra("customer", customerNew);
                intent.putExtra("customer_flag", CustomerContants.FLAG_CUSTOMER_BAND);
                startActivity(intent);*/
                dismiss();
            }

            @Override
            public void closeBtnClick() {
                dismiss();
                DialogUtil.dismissLoadingDialog();
            }
        });
        dialog.show(getChildFragmentManager(), "showRegisterSuccessTips");
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FaceRequestCodeConstant.RC_DINNER_CUSTOMER_LOGIN && resultCode == Activity.RESULT_OK) {
            mFaceCode = data.getStringExtra(BaiduFaceRecognition.KEY_FACE_CODE);
            mFaceFeature = (FaceFeature) data.getSerializableExtra(BaiduFaceRecognition.KEY_FACE_FEATURE);
            faceLogin();
        } else if (requestCode == FaceRequestCodeConstant.RC_DINNER_CUSTOMER_REGIEST && resultCode == Activity.RESULT_OK) {
            mFaceCode = data.getStringExtra(BaiduFaceRecognition.KEY_FACE_CODE);
            mFaceFeature = (FaceFeature) data.getSerializableExtra(BaiduFaceRecognition.KEY_FACE_FEATURE);
            chooseFaceDesc(true);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/

    /**
     * 会员充值界面
     *
     * @param customer 顾客信息
     * @param balance  余额，实体卡充值时该值 传 null
     */
    private void showChargingDialog(CustomerResp customer, String balance) {
        CustomerChargingDialogFragment dialogFragment = new CustomerChargingDialogFragment_();
        Bundle args = new Bundle();
        args.putInt(CustomerChargingDialogFragment.KEY_FROM, CustomerChargingDialogFragment.FROM_CREATE_CUSTOMER);//来自顾客界面
        args.putSerializable(CustomerChargingDialogFragment.KEY_CUSTOMER, customer);
        args.putString(CustomerChargingDialogFragment.KEY_BALANCE, balance);
        dialogFragment.setArguments(args);
        dialogFragment.show(getActivity().getSupportFragmentManager(), "ecCardCharging");
        dismiss();
    }
}
