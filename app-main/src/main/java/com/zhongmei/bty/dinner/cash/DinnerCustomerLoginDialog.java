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
import android.view.Gravity;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.zhongmei.bty.basemodule.auth.application.BeautyApplication;
import com.zhongmei.bty.basemodule.auth.application.CustomerApplication;
import com.zhongmei.bty.basemodule.commonbusiness.utils.ServerAddressUtil;
import com.zhongmei.bty.basemodule.commonbusiness.view.ScanPopupWindow;
import com.zhongmei.bty.basemodule.customer.dialog.PasswordDialog;
import com.zhongmei.bty.basemodule.customer.enums.CustomerLoginType;
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.basemodule.customer.message.MemberCreateResp;
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
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.bty.basemodule.trade.manager.DinnerCashManager;
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.commonmodule.view.EditTextWithDeleteIcon;
import com.zhongmei.bty.customer.CustomerChargingDialogFragment;
import com.zhongmei.bty.customer.CustomerChargingDialogFragment_;
import com.zhongmei.bty.snack.orderdish.selftimepicker.SelfRadioButton;
import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.bean.YFResponse;
import com.zhongmei.yunfu.bean.req.CustomerLoginResp;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.context.util.EncodingHandler;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.data.LoadingYFResponseListener;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.enums.CustomerType;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.EventResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.yunfu.resp.YFResponseListener;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.util.DialogUtil;
import com.zhongmei.yunfu.util.ToastUtil;

import java.math.BigDecimal;
import java.sql.SQLException;

import de.greenrobot.event.EventBus;

/**
 * @Date： 16/10/18
 * @Description:
 * @Version: 1.0
 */
public class DinnerCustomerLoginDialog extends BasicDialogFragment implements View.OnClickListener {

    private static final String TAG = DinnerCustomerLoginDialog.class.getSimpleName();

    public static final String BIRTHDAY_DEFAULT = "1990-9-1";

    private int barcodeWH = 180;// 微信二维码宽度

    private EditTextWithDeleteIcon mShowValue;

    private TextView mBtnScanLogin;

    private TextView mCardLogin;

    private ImageView mQrCodeImg;

    private EditText mHideText;

    private Button mLoginBtn;

    private LinearLayout mLoginLayout;

    private RelativeLayout mRegisterLyaout;

    private LinearLayout mQrCodeLayout;

    private EditTextWithDeleteIcon mInputNumber;

    private EditTextWithDeleteIcon mPassWord;

    private EditTextWithDeleteIcon mPassWordSecond;

    private SelfRadioButton mMale;

    private Button mRegisterBtn;

    private Button mCloseBtn;

    private ImageButton mBackBtn;

    private TextView mScanTips;

    private TextView mRegisterTxt;

    private ImageView mDefaultIcon;

    private TextView mTitle;

    private View mTempView;

    private RadioGroup mSexGroup;

    private EditTextWithDeleteIcon mName;

    private ScanPopupWindow scanPopupWindow;

    private boolean[] settings;

    private String requestUuid;

    private boolean mIsRegister;//是否在注册页面

    private boolean isLogin = false;//判断是否已经登录避免重复登录

    private PasswordDialog mPassWordDialog;

    // 生成二维码
    Bitmap mBitmap = null;

    private Integer mSex = CustomerResp.SEX_MALE;

    public void show(FragmentManager manager, String tag) {
        if (manager != null && !manager.isDestroyed()) {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        }
    }

    private void assignViews(View view) {
        mHideText = (EditTextWithDeleteIcon) view.findViewById(R.id.phone_number);
        mShowValue = (EditTextWithDeleteIcon) view.findViewById(R.id.phone_number);
        InputFilter[] filters = {new InputFilter.LengthFilter(20)};
        mShowValue.setFilters(filters);
        mShowValue.setHint(getString(R.string.customer_login_hint_enter_phone_or_cardnum));
        mBtnScanLogin = (TextView) view.findViewById(R.id.scan_login);
        mLoginBtn = (Button) view.findViewById(R.id.customer_verification);
        mCardLogin = (TextView) view.findViewById(R.id.card_login);
        mLoginLayout = (LinearLayout) view.findViewById(R.id.login_layout);
        mRegisterLyaout = (RelativeLayout) view.findViewById(R.id.register_layout);
        mQrCodeLayout = (LinearLayout) view.findViewById(R.id.qrcode_layout);
        mInputNumber = (EditTextWithDeleteIcon) view.findViewById(R.id.input_phone_number);
        mPassWord = (EditTextWithDeleteIcon) view.findViewById(R.id.input_password);
        mPassWordSecond = (EditTextWithDeleteIcon) view.findViewById(R.id.input_password_again);
        mRegisterBtn = (Button) view.findViewById(R.id.register_btn);
        mRegisterTxt = (TextView) view.findViewById(R.id.member_register);
        mQrCodeImg = (ImageView) view.findViewById(R.id.qrcode_img);
        mScanTips = (TextView) view.findViewById(R.id.register_scan_tips);
        mDefaultIcon = (ImageView) view.findViewById(R.id.defalut_icon);
        mTitle = (TextView) view.findViewById(R.id.customer_title);
        mTempView = view.findViewById(R.id.temp_view);
        mSexGroup = (RadioGroup) view.findViewById(R.id.customer_edit_sex_rg);
        mName = (EditTextWithDeleteIcon) view.findViewById(R.id.input_name);
        mMale = (SelfRadioButton) view.findViewById(R.id.male);
        mSexGroup.setOnCheckedChangeListener(onCheckedChangeListener);
        mCloseBtn = (Button) view.findViewById(R.id.clost_btn);
        mCloseBtn.setOnClickListener(this);
        mBackBtn = (ImageButton) view.findViewById(R.id.back);
        mBackBtn.setOnClickListener(this);
        mRegisterTxt.setOnClickListener(this);
        mRegisterBtn.setOnClickListener(this);
        mBtnScanLogin.setOnClickListener(this);
        mLoginBtn.setOnClickListener(this);
        mCardLogin.setOnClickListener(this);

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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.comm_customer_login, container);
        getDialog().setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        assignViews(view);

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
        isShowQrCode();

        return view;
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

        if (!settings[0]) {//没有开通微信公众号
            mQrCodeLayout.setVisibility(View.GONE);
            if (mIsRegister) {
//                mDefaultIcon.setBackgroundResource(R.drawable.register_defalut_icon);
                mTempView.setVisibility(View.VISIBLE);
                mDefaultIcon.setVisibility(View.GONE);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mRegisterBtn.getLayoutParams();
                params.setMargins(0, 200, 0, 10);
                mRegisterBtn.setLayoutParams(params);
            } else {
                mDefaultIcon.setBackgroundResource(R.drawable.login_defalut_icon);
                mDefaultIcon.setVisibility(View.VISIBLE);

            }
        } else {
            if (mIsRegister) {
                if (settings[1]) {//打开了关注即会员开关
                    mQrCodeLayout.setVisibility(View.VISIBLE);
                    mDefaultIcon.setVisibility(View.GONE);
                    mTempView.setVisibility(View.GONE);
                    mScanTips.setText(getString(R.string.input_scan_register_member));
                } else {
                    mTempView.setVisibility(View.VISIBLE);
                    mQrCodeLayout.setVisibility(View.GONE);
                    mDefaultIcon.setVisibility(View.GONE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mRegisterBtn.getLayoutParams();
                    params.setMargins(0, 200, 0, 10);
                    mRegisterBtn.setLayoutParams(params);
                }
            } else {
                mDefaultIcon.setVisibility(View.GONE);
                mQrCodeLayout.setVisibility(View.VISIBLE);
                mScanTips.setText(getString(R.string.input_scan_qrcode));
            }
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
                    mQrCodeImg.setImageBitmap(mBitmap);
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
                        loginByPhoneNo(customer.getMobile(), customer.getId(), UserActionEvent.DINNER_PAY_LOGIN_INPUT_MOBILE);
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
            if (scanPopupWindow != null && scanPopupWindow.isShowing()) {
                scanPopupWindow.dismiss();
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
                if (checkRegisterInfo()) {
                    CustomerResp customer = createCustomer();
                    doCreateCustomer(customer);
                }
                break;
            case R.id.member_register:
                VerifyHelper.verifyAlert(getActivity(), CustomerApplication.PERMISSION_CUSTOMER_CREATE,
                        new VerifyHelper.Callback() {
                            @Override
                            public void onPositive(User user, String code, Auth.Filter filter) {
                                super.onPositive(user, code, filter);
                                mIsRegister = true;
                                isShowQrCode();
                                mTitle.setText(getString(R.string.member_register));
                                mLoginLayout.setVisibility(View.GONE);
                                mRegisterLyaout.setVisibility(View.VISIBLE);
                                mBackBtn.setVisibility(View.VISIBLE);
                                mCloseBtn.setVisibility(View.GONE);
                            }
                        });
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
                mIsRegister = false;
                isShowQrCode();
                mRegisterLyaout.setVisibility(View.GONE);
                mBackBtn.setVisibility(View.GONE);
                mLoginLayout.setVisibility(View.VISIBLE);
                mCloseBtn.setVisibility(View.VISIBLE);
                mInputNumber.setText("");
                mPassWord.setText("");
                mPassWordSecond.setText("");
                mName.setText("");
                mMale.setChecked(true);
                mTitle.setText(getString(R.string.customer_menber_login));
                break;
            case R.id.card_login:
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
                readCardDialogFragment.show(getFragmentManager(), "get_cardno");
                readCardDialogFragment.setCloseListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showSecondDisPlay();
                    }
                });*/
                break;
            case R.id.scan_login:
                DisplayServiceManager.doCancel(getActivity());
                mShowValue.requestFocus();
                startScan();
                break;
            default:
                break;
        }
    }

    /**
     * 检验注册信息
     */
    private boolean checkRegisterInfo() {

        // 手机
        String number = mInputNumber.getText().toString().trim();
        if (TextUtils.isEmpty(number)) {
            ToastUtil.showShortToast(R.string.customer_no_phone);
            return false;
        }

        String passwrod = mPassWord.getText().toString();
        String passwrodSecond = mPassWordSecond.getText().toString();
        if (TextUtils.isEmpty(passwrod)) {
            ToastUtil.showShortToast(R.string.customer_no_password);
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
                    ToastUtil.showShortToast(response.getMessage());
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

                            TradeCustomer tradeCustomer = CustomerManager.getInstance()
                                    .getTradeCustomer(customerNew);
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
                            /*DisplayUserInfo dUserInfo =
                                    DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_USERINFO_SHOW,
                                            customerNew, customerNew.integral, false, 0);
                            DisplayServiceManager.updateDisplay(getActivity(), dUserInfo);*/

                            // start
                            EventBus.getDefault().post(new EventReadKeyboard(true, ""));// 发送成功到ReadKeyboardDialogFragment

                            DinnerPriviligeItemsFragment.showDisplayUserInfo(getActivity());
                            new DinnerCashManager().jumpAfterLogin("", customerNew, null);
                        }
                    }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                } else {
                    ToastUtil.showShortToast(response.getMessage());
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showShortToast(error.getMessage());
            }

        };
        oper.createCustomer(customer, "", LoadingResponseListener.ensure(listener, getFragmentManager()));

    }


    /**
     * 开启扫描
     */
    private void startScan() {
        scanPopupWindow = new ScanPopupWindow(getActivity(), getString(R.string.sacn_customer_number_desc));
        scanPopupWindow.showAtLocation(mBtnScanLogin, Gravity.NO_GRAVITY, 0, 0);
        scanPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                showSecondDisPlay();
            }
        });

        scanPopupWindow.setOnScanBarcodeCallback(new ScanPopupWindow.ScanBarcodeCallback() {
            @Override
            public void onScanBarcode(String data) {
                if (!TextUtils.isEmpty(data)) {
                    UserActionEvent.start(UserActionEvent.DINNER_PAY_LOGIN_SCAN_CODE);
                    loginByWeixin(data);        //微信登录
                }
            }
        });
    }

    private void loginByWeixin(String number) {
        YFResponseListener<YFResponse<CustomerLoginResp>> listener = new YFResponseListener<YFResponse<CustomerLoginResp>>() {
            @Override
            public void onResponse(YFResponse<CustomerLoginResp> response) {
                if (YFResponse.isOk(response)) {
                    if (isLogin) {
                        return;
                    }
                    CustomerLoginResp resp = response.getContent();

                    if (resp.customerIsDisable()) {//当前账号冻结
                        ToastUtil.showShortToast(R.string.order_dish_member_disabled);
                    } else {
                        isLogin = true;

                        CustomerResp customerNew = resp.getCustomer();
                        customerNew.setInitialValue();
                        customerNew.queryLevelRightInfos();
                        CustomerManager.getInstance().setLoginCustomer(customerNew);

                        TradeCustomer tradeCustomer = CustomerManager.getInstance()
                                .getTradeCustomer(customerNew);

                        tradeCustomer.setCustomerType(CustomerType.MEMBER);
                        ToastUtil.showShortToast(R.string.customer_login);

                        // 登录成功后第二屏显示用户信息
                        /*DisplayUserInfo dUserInfo =
                                DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_USERINFO_SHOW,
                                        customerNew, customerNew.integral, false, 0);
                        DisplayServiceManager.updateDisplay(getActivity(), dUserInfo);*/

                        //数据设置
                        EventBus.getDefault().post(new EventReadKeyboard(true, ""));// 发送成功到ReadKeyboardDialogFragment

                        DinnerPriviligeItemsFragment.showDisplayUserInfo(getActivity());

                        new DinnerCashManager().jumpAfterLogin("", customerNew, null);

                        DinnerShopManager.getInstance().getShoppingCart().setOpenIdenty(resp.getOpenId());

                        dismiss();
                    }
                } else {
                    String msg;
                    if (response.getStatus() == 1126) {
                        msg = getString(R.string.order_dish_member_disabled);
                    } else {
                        msg = response.getContent() == null ? getString(R.string.display_login_error) : response.getMessage();
                    }
                    loginFail(msg, true);//不需要验证密码的模式下，要清空输入框
                }
                UserActionEvent.end(UserActionEvent.DINNER_PAY_LOGIN_SCAN_CODE);
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showLongToast(error.getMessage());
            }
        };


        CustomerManager.getInstance().customerLogin(CustomerLoginType.WECHAT_MEMBERCARD_ID, number, "", false, false, true, LoadingYFResponseListener.ensure(listener, getFragmentManager()));


    }

    // 品牌编号长度
    private static final int BRAND_LENGTH = 7;

    private void verification() {
        final String inputNo = mShowValue.getText().toString().trim();
        if (TextUtils.isEmpty(inputNo)) {
            ToastUtil.showShortToast(R.string.customer_login_hint);
            return;
        }
        getCardBaseInfo(inputNo); // v8.2支持刷卡登录会员，先校验卡，在校验手机

//        loginByPhoneNo(inputNo, null, UserActionEvent.DINNER_PAY_LOGIN_INPUT_MOBILE);

//        Customer customer = CustomerManager.getInstance().getCustomerByPhone(inputNo);
//        if (customer == null) {
//            ToastUtil.showShortToast(R.string.customer_not_exist);
//            return;
//        }
//
//        if (!customer.isMember()) {
//            loginByPhoneNo(inputNo, null);
//            return;
//        }
//
//        mPassWordDialog = CustomerManager.getInstance().dinnerLoginByPhoneNo(getActivity(), inputNo, new CustomerManager.DinnerLoginListener() {
//            @Override
//            public void login(PasswordDialog dialog, int needPswd, String password) {
//                if (needPswd == 1) {
//                    //需要密码
//                    loginByPhoneNoOrId(inputNo, null, 1, password, dialog);
//                } else {
//                    //不需要密码
//                    loginByPhoneNo(inputNo, null);
//                }
//            }
//        });

    }

    @Override
    public void onDestroy() {
        if (DinnerShopManager.getInstance().getLoginCustomer() == null) {
            DisplayServiceManager.doCancel(getActivity());
        }

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
                    CardLoginResp resp = response.getContent();
                    // 设置card的名称，从customer中获得
                    EcCard card = resp.getResult().getCardInstance();
                    if (card.getCardType() == EntityCardType.ANONYMOUS_ENTITY_CARD) {
                        ToastUtil.showShortToast(R.string.customer_login_not_member);
                        mShowValue.setHint(R.string.customer_login_not_member);
                        mShowValue.setBackgroundResource(R.drawable.customer_edit_error_bg);

                        // 副屏显示
                        /*DisplayUserInfo dUserInfo =
                                DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_VALIDATE_USER_FAIL,
                                        "",
                                        null,
                                        0,
                                        true, 0);
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
                            // 登录成功后第二屏显示用户信息
                            /*DisplayUserInfo dUserInfo =
                                    DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_USERINFO_SHOW,
                                            customerNew,
                                            integral.longValue(), false, 0);
                            DisplayServiceManager.updateDisplay(getActivity(), dUserInfo);*/

                            //跳转
                            new DinnerCashManager().jumpAfterLogin("", customerNew, null);

                            dismiss();
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
        operates.cardLogin(inputNo, LoadingResponseListener.ensure(listener, getFragmentManager()));
    }

    /**
     * 使用手机号登录,默认不需要密码验证
     *
     * @Title: loginByPhoneNo
     * @Return void 返回类型
     */
    private void loginByPhoneNo(final String inputNo, final Long customerId, UserActionEvent eventName) {
//        loginByPhoneNo(inputNo, customerId, DinnerDishCustomerLogin.NOT_NEED_PSWD, null, null);
        loginByPhoneNoOrId(inputNo, customerId, CustomerManager.NOT_NEED_PSWD, null, null, eventName);
    }

    private void loginByPhoneNoOrId(final String inputNo, final Long customerId, final int needPswd, final String pswd, final PasswordDialog dialog, UserActionEvent eventName) {
        YFResponseListener<YFResponse<CustomerLoginResp>> listener = new YFResponseListener<YFResponse<CustomerLoginResp>>() {
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
                        if (mPassWordDialog != null && mPassWordDialog.isShowing()) {
                            mPassWordDialog.dismiss();
                        }

                        CustomerLoginResp resp = response.getContent();

                        if (resp.customerIsDisable()) {//当前账号冻结
                            ToastUtil.showShortToast(R.string.order_dish_member_disabled);
                        } else {
                            isLogin = true;

                            CustomerResp customerNew = resp.getCustomer();
                            customerNew.setInitialValue();
                            customerNew.queryLevelRightInfos();
                            customerNew.needRefresh = false;

                            TradeCustomer tradeCustomer = CustomerManager.getInstance()
                                    .getTradeCustomer(customerNew);
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
                            /*DisplayUserInfo dUserInfo =
                                    DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_USERINFO_SHOW,
                                            customerNew, customerNew.integral, false, 0);
                            DisplayServiceManager.updateDisplay(getActivity(), dUserInfo);*/

                            // start
                            EventBus.getDefault().post(new EventReadKeyboard(true, ""));// 发送成功到ReadKeyboardDialogFragment

                            DinnerPriviligeItemsFragment.showDisplayUserInfo(getActivity());

                            new DinnerCashManager().jumpAfterLogin("", customerNew, null);

                            DinnerShopManager.getInstance().getShoppingCart().setOpenIdenty(resp.getOpenId());
                            //end

                            dismiss();
                        }
                    } else {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.clean();
                        }

                        String msg;
                        if (response.getStatus() == 1126) {
                            msg = getString(R.string.order_dish_member_disabled);
                        } else {
                            msg = response.getContent() == null ? getString(R.string.display_login_error) : response.getMessage();
                        }

                        EventBus.getDefault().post(new EventReadKeyboard(false, msg));// 发送失败到ReadKeyboardDialogFragment
                        loginFail(msg, needPswd != 1);//不需要验证密码的模式下，要清空输入框
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

        CustomerManager customerManager = CustomerManager.getInstance();
        if (!TextUtils.isEmpty(inputNo)) {
            customerManager.customerLogin(CustomerLoginType.MOBILE, inputNo, pswd, needPswd == 1, false, true, LoadingYFResponseListener.ensure(listener, getFragmentManager()));
        } else if (customerId != null && customerId != 0) {
            customerManager.customerLogin(CustomerLoginType.MEMBER_ID, customerId.toString(), pswd, needPswd == 1, false, true, LoadingYFResponseListener.ensure(listener, getFragmentManager()));
        }
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
            if (clearInput) {
                //DisplayServiceManager.doUpdateLoginInfo(getActivity(), DisPlayLoginInfo.COMMAND_ERROR, "", mBitmap, mIsRegister);
            } else {
                /*String phone = mShowValue.getText().toString();
                DisplayServiceManager.doUpdateLoginInfo(getActivity(), DisPlayLoginInfo.COMMAND_ERROR, phone, mBitmap, mIsRegister);*/
            }
        } else {
            /*DisplayUserInfo dUserInfo =
                    DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_VALIDATE_USER_FAIL,
                            "",
                            null,
                            0,
                            true, 0);
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
        if (getFragmentManager() != null && this.isVisible()) {//修复线上bug，apm线上bug：getFragmentManager返回null.
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
                                                           CardBaseInfoResp.BaseCardInfo baseCardInfo = resp.getResult();
                                                           if (baseCardInfo != null) {
                                                               if (baseCardInfo.getCardStatus() == CardStatus.ACTIVATED) {
                                                                   if (baseCardInfo.getCardType() == EntityCardType.GENERAL_CUSTOMER_CARD) {
                                                                       if (baseCardInfo.getCustomerId() != null
                                                                               && !TextUtils.isEmpty(baseCardInfo.getCustomerId().toString())) {
                                                                           loginByPhoneNo(null, baseCardInfo.getCustomerId(), UserActionEvent.DINNER_PAY_LOGIN_SWIPE_CARD);
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
                                                               loginByPhoneNo(inputNo, null, UserActionEvent.DINNER_PAY_LOGIN_INPUT_MOBILE);
//                                                               ToastUtil.showShortToast(R.string.no_card_info);
//                                                               UserActionEvent.end(UserActionEvent.DINNER_PAY_LOGIN_SWIPE_CARD);
                                                           }
                                                       } else {
                                                           UserActionEvent.end(UserActionEvent.DINNER_PAY_LOGIN_SWIPE_CARD);
                                                           loginByPhoneNo(inputNo, null, UserActionEvent.DINNER_PAY_LOGIN_INPUT_MOBILE);
//                                                           String message = response.getMessage();
//                                                           loginFail(message, false);
//                                                           UserActionEvent.end(UserActionEvent.DINNER_PAY_LOGIN_SWIPE_CARD);
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

    /**
     * 注册成功提示框
     */
    private void showRegisterSuccessTips(final CustomerResp customerNew) {

        CommonDialogFragment.CommonDialogFragmentBuilder cb = new CommonDialogFragment.CommonDialogFragmentBuilder(MainApplication.getInstance());
        cb.iconType(CommonDialogFragment.ICON_SUCCESS)
                .title(R.string.register_success)
                .positiveText(R.string.close)
                .negativeText(R.string.customer_charing_str)
                .positiveLinstner(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        dismiss();
                        DialogUtil.dismissLoadingDialog();
                    }
                })
                .negativeLisnter(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        showChargingDialog(customerNew, String.valueOf(customerNew.remainValue));
                    }
                })
                .build()
                .show(getFragmentManager(), "showRegisterSuccessTips");
    }

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
        dialogFragment.show(getFragmentManager(), "ecCardCharging");
        dismiss();
    }
}
