package com.zhongmei.bty.dinner.cash;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import com.zhongmei.bty.basemodule.commonbusiness.listener.SimpleResponseListener;
import com.zhongmei.bty.basemodule.commonbusiness.view.ScanPopupWindow;
import com.zhongmei.bty.basemodule.customer.bean.CustomerMobileVo;
import com.zhongmei.bty.basemodule.customer.dialog.PasswordDialog;
import com.zhongmei.bty.basemodule.customer.enums.CustomerLoginType;
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.basemodule.customer.message.MemberLoginVoResp;
import com.zhongmei.bty.basemodule.customer.operates.CustomerOperates;
import com.zhongmei.bty.basemodule.database.entity.customer.CustomerV5;
import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCard;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardKind;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.NewCardLoginResp;
import com.zhongmei.bty.basemodule.devices.mispos.enums.EntityCardType;
import com.zhongmei.bty.basemodule.devices.mispos.event.EventReadKeyboard;
import com.zhongmei.bty.basemodule.input.NumberKeyBoardUtils;
import com.zhongmei.bty.basemodule.shopmanager.interfaces.ChangePageListener;
import com.zhongmei.bty.basemodule.trade.manager.DinnerCashManager;
import com.zhongmei.bty.common.util.CommonKeyBorad;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.bean.req.CustomerLoginResp;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.resp.data.LoyaltyMindTransferResp;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.util.ToastUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import de.greenrobot.event.EventBus;

@EFragment(R.layout.dinner_dish_customer_login)
public class DinnerDishCustomerLogin extends BasicDialogFragment implements OnClickListener {
    private static final String TAG = DinnerDishCustomerLogin.class.getSimpleName();


    private String source;

    @Bean(CommonKeyBorad.class)
    CommonKeyBorad mNumberKeyBorad;

    @ViewById(R.id.show_value)
    EditText show_value;

    @ViewById(R.id.customer_verification)
    Button customer_verification;

    @ViewById(R.id.btn_scan_code)
    ImageButton btnScanCode;

    boolean mLoginForPay = false;

    private ChangePageListener mChangePageListener;

    private boolean isShowAccount = true;

    private ScanPopupWindow scanPopupWindow;

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(DensityUtil.dip2px(MainApplication.getInstance(), 460), DensityUtil.dip2px(MainApplication.getInstance(), 600));
    }

    @Click({R.id.customer_verification, R.id.back, R.id.btn_card_login, R.id.btn_scan_code})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.customer_verification:
                if (!ClickManager.getInstance().isClicked()) {
                    verification();
                }
                break;
            case R.id.back:
                DisplayServiceManager.doCancel(getActivity());
                isShowAccount = false;
                show_value.setText("");
                close();
                break;
            case R.id.btn_card_login:
                if (ClickManager.getInstance().isClicked()) {
                    return;
                }

                break;
            case R.id.btn_scan_code:
                if (!ClickManager.getInstance().isClicked()) {
                    startScan();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        show_value.setText("");
        super.onHiddenChanged(hidden);
    }

    @AfterViews
    protected void init() {
                getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getDialog().getWindow().setFlags(CommonDialogFragment.FLAG_HOMEKEY_DISPATCHED,
                CommonDialogFragment.FLAG_HOMEKEY_DISPATCHED);
        getDialog().getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (getArguments() != null) {
            source = getArguments().getString(DinnerCashManager.SOURCE);
        }

        if (getActivity().getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
                        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
        show_value.requestFocus();
        show_value.setTag(DinnerCashManager.CUSTOMERLOGIN);
        show_value.addTextChangedListener(textWatcher);
        mNumberKeyBorad.setCurrentInput(show_value);
        NumberKeyBoardUtils.setTouchListener(show_value);


        show_value.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isShowAccount) {

                } else {
                    isShowAccount = true;
                }
            }
        });

        show_value.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN)
                        && (keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER || keyCode == KeyEvent.KEYCODE_ENTER)) {
                    verification();
                    return true;
                }
                return false;
            }
        });
            }

    public void registerListener(ChangePageListener mChangePageListener) {
        this.mChangePageListener = mChangePageListener;
    }

    private void verification() {

        final String inputNo = mNumberKeyBorad.getValue();
        if (TextUtils.isEmpty(inputNo)) {
            ToastUtil.showShortToast(R.string.customer_login_hint);
            return;
        }



        CustomerOperates customerOperates = OperatesFactory.create(CustomerOperates.class);
        customerOperates.findCustomerByPhone(inputNo, new SimpleResponseListener<CustomerMobileVo>() {

            @Override
            public void onSuccess(ResponseObject<CustomerMobileVo> response) {
                loginByPhoneNo(inputNo, null);

            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showLongToast(error.getMessage());
            }
        });
    }


    private void loginByCardNo(final String inputNo) {
        ResponseListener<LoyaltyMindTransferResp<NewCardLoginResp>> listener = LoadingResponseListener.ensure(new ResponseListener<LoyaltyMindTransferResp<NewCardLoginResp>>() {
            @Override
            public void onResponse(ResponseObject<LoyaltyMindTransferResp<NewCardLoginResp>> response) {
                if (ResponseObject.isOk(response)) {
                    NewCardLoginResp resp = response.getContent().getData();
                                        if (response.getContent().isOk() && resp != null) {
                        EcCard card = resp.getCardInstance();
                        card.queryLevelSettingInfo();
                        try {
                            EcCardKind cardKind = card.getCardKind();
                            if (cardKind != null && cardKind.getCardType() != EntityCardType.CUSTOMER_ENTITY_CARD) {
                                loginFail(getString(R.string.the_card_is_not_member_card));
                                return;
                            }
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage(), e);
                        }

                                                CustomerV5 customerV5 = resp.getCustomerV5();
                        card.setName(customerV5.getName());
                        card.setCustomer(customerV5);


                        CustomerResp customer = resp.getCustomer();
                        customer.queryLevelRightInfos();

                                                ToastUtil.showShortToast(R.string.customer_login);
                        isShowAccount = false;
                        show_value.setText("");

                                                new DinnerCashManager().jumpAfterLogin(source, customer, null);
                        dismiss();
                    } else {
                        loginFail(response.getContent().getMessage());
                    }
                } else {
                    loginFail(response.getMessage());
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showShortToast(error.getMessage());
            }
        }, getFragmentManager());
        CustomerOperates operates = OperatesFactory.create(CustomerOperates.class);
        operates.cardLoginNew(inputNo, listener);
    }


    private void loginByPhoneNo(final String inputNo, String wxNo) {
        loginByPhoneNo(inputNo, wxNo, CustomerManager.NOT_NEED_PSWD, null, null);
    }

    private void loginByPhoneNo(final String inputNo, String wxNo, final int needPswd, String pswd, final PasswordDialog dialog) {
        ResponseListener<MemberLoginVoResp> listener = new ResponseListener<MemberLoginVoResp>() {

            @Override
            public void onResponse(ResponseObject<MemberLoginVoResp> response) {
                try {
                    if (ResponseObject.isOk(response)) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        CustomerLoginResp resp = response.getContent().getResult();

                        if (resp.customerIsDisable()) {                            ToastUtil.showShortToast(R.string.order_dish_member_disabled);
                            return;
                        }

                        CustomerResp customer = resp.getCustomer();
                        customer.setInitialValue();
                        customer.needRefresh = false;

                        if (!mLoginForPay) {
                            customer.queryLevelRightInfos();
                            ToastUtil.showShortToast(R.string.customer_login);
                        }
                        isShowAccount = false;
                        show_value.setText("");

                        EventBus.getDefault().post(new EventReadKeyboard(true, ""));
                        DinnerPriviligeItemsFragment.showDisplayUserInfo(getActivity());

                        new DinnerCashManager().jumpAfterLogin(source, customer, null);
                        dismiss();
                    } else {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.clean();
                        }

                        String msg;
                        if (response.getStatusCode() == 1126) {
                            msg = getString(R.string.order_dish_member_disabled);
                        } else {
                            msg = response.getMessage();
                        }
                        EventBus.getDefault().post(new EventReadKeyboard(false, msg));
                        loginFail(response.getMessage(), needPswd != CustomerManager.NEED_PSWD);                    }
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showShortToast(error.getMessage());

                EventBus.getDefault().post(new EventReadKeyboard(false, error.getMessage()));            }
        };

        CustomerManager customerManager = CustomerManager.getInstance();
        if (!TextUtils.isEmpty(inputNo)) {
            customerManager.customerLogin(CustomerLoginType.MOBILE, inputNo, pswd, needPswd == 1, false,
                    true, LoadingResponseListener.ensure(listener, getFragmentManager()));
        } else if (!TextUtils.isEmpty(wxNo)) {
            customerManager.customerLogin(CustomerLoginType.WECHAT_MEMBERCARD_ID, wxNo, pswd, needPswd == 1, false,
                    true, LoadingResponseListener.ensure(listener, getFragmentManager()));
        }
    }


    private void loginFail(String message) {
        loginFail(message, true);
    }


    private void loginFail(String message, boolean isNoPswd) {
        ToastUtil.showShortToast(message);

        if (isNoPswd) {


            show_value.setBackgroundResource(R.drawable.customer_edit_error_bg);
            show_value.requestFocus();
            isShowAccount = false;
            show_value.setText("");
            show_value.startAnimation(shakeAnimation(6));
        } else {

        }
    }



    private void close() {
        dismiss();
    }

    public static Animation shakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(0, 15, 0, 0);
                translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(500);

        return translateAnimation;
    }

    @Override
    public void onDestroy() {
                super.onDestroy();
    }


    private void startScan() {
        scanPopupWindow = new ScanPopupWindow(getActivity(), getString(R.string.sacn_customer_number_desc));
        scanPopupWindow.showAtLocation(btnScanCode, Gravity.NO_GRAVITY, 0, 0);
        scanPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });

        scanPopupWindow.setOnScanBarcodeCallback(new ScanPopupWindow.ScanBarcodeCallback() {
            @Override
            public void onScanBarcode(String data) {
                loginByPhoneNo(null, data);
            }
        });
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (scanPopupWindow != null && scanPopupWindow.isShowing()) {
                scanPopupWindow.dismiss();
            }
        }
    };

    @Override
    public void dismiss() {
        if (isVisible()) {
            super.dismiss();
        }
    }

}
