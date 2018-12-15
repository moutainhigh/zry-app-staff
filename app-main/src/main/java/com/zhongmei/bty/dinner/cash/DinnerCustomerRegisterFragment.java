package com.zhongmei.bty.dinner.cash;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zhongmei.bty.basemodule.customer.dialog.country.CountryDialog;
import com.zhongmei.bty.basemodule.customer.dialog.country.CountryGridAdapter;
import com.zhongmei.bty.basemodule.customer.enums.CustomerLoginType;
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.basemodule.customer.message.MemberCreateResp;
import com.zhongmei.bty.basemodule.customer.message.MemberLoginVoResp;
import com.zhongmei.bty.basemodule.customer.operates.CustomerOperates;
import com.zhongmei.bty.basemodule.customer.util.CustomerUtil;
import com.zhongmei.bty.basemodule.database.entity.customer.CommCustomer;
import com.zhongmei.bty.basemodule.database.entity.customer.EventOpenIdLoginInfo;
import com.zhongmei.bty.basemodule.database.entity.customer.OpenIdLoginInfo;
import com.zhongmei.bty.basemodule.devices.mispos.event.EventReadKeyboard;
import com.zhongmei.bty.basemodule.erp.bean.ErpCurrency;
import com.zhongmei.bty.basemodule.trade.manager.DinnerCashManager;
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.commonmodule.view.EditTextWithDeleteIcon;
import com.zhongmei.bty.customer.CustomerChargingDialogFragment;
import com.zhongmei.bty.customer.CustomerChargingDialogFragment_;
import com.zhongmei.bty.snack.orderdish.selftimepicker.SelfRadioButton;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.bean.req.CustomerLoginResp;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.enums.CustomerType;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.EventResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.yunfu.util.DialogUtil;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.util.UserActionCode;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import de.greenrobot.event.EventBus;

/**
 * Created by demo on 2018/12/15
 */
@EFragment(R.layout.dinner_customer_register_fragment_layout)
public class DinnerCustomerRegisterFragment extends BasicFragment {
    public static final String TAG = DinnerCustomerRegisterFragment.class.getSimpleName();

    @ViewById(R.id.input_name)
    EditTextWithDeleteIcon mName;

    @ViewById(R.id.customer_edit_sex_rg)
    RadioGroup mSexGroup;

    @ViewById(R.id.male)
    SelfRadioButton mMale;

    @ViewById(R.id.tvAreaCodes)
    TextView mTvAreaCode;

    @ViewById(R.id.input_phone_number)
    EditTextWithDeleteIcon mInputNumber;

    @ViewById(R.id.input_password)
    EditTextWithDeleteIcon mPassWord;

    @ViewById(R.id.input_password_again)
    EditTextWithDeleteIcon mPassWordSecond;

    @ViewById(R.id.tv_face_desc)
    TextView mTvFaceDesc;

    @ViewById(R.id.register_btn)
    Button mRegisterBtn;

    private Integer mSex = CustomerResp.SEX_MALE;

    // 人脸ID
    private String mFaceCode;

    //private FaceFeature mFaceFeature;

    public static final String BIRTHDAY_DEFAULT = "1990-9-1";

    private ErpCurrency mErpCurrency;

    private Map<String, ErpCurrency> erpCurrencyMap;


//    /**
//     * 标记进入模式
//     *
//     * @see com.zhongmei.beauty.customer.constants.BeautyCustomerConstants.CustomerLoginLaunchMode
//     */
//    private int mLaunchMode = BeautyCustomerConstants.CustomerReiestLaunchMode.NORMAL_REGIEST;

    public void setErpCurrency(Map<String, ErpCurrency> erpCurrencyMap) {
        this.erpCurrencyMap = erpCurrencyMap;
        String areaCode = ShopInfoCfg.getInstance().getCurrency().getAreaCode();
        if (!TextUtils.isEmpty(areaCode)) {
            mErpCurrency = erpCurrencyMap.get(areaCode);
        } else {
            mErpCurrency = erpCurrencyMap.get("86");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (getArguments() != null){
//            mLaunchMode = getArguments().getInt(BeautyCustomerConstants.KEY_CUSTOMER_REGIEST_FLAG , BeautyCustomerConstants.CustomerReiestLaunchMode.NORMAL_REGIEST);
//        }
    }

    @AfterViews
    void initView() {
        initRadioGroup();
        setupCountryView();
        mInputNumber.addTextChangedListener(mRegisterTextWatcher);
        mPassWord.addTextChangedListener(mRegisterTextWatcher);
        mPassWordSecond.addTextChangedListener(mRegisterTextWatcher);
        EventBus.getDefault().register(this);
    }

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

    private void initRadioGroup() {
        mMale.setChecked(true);
        mSexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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
        });
    }

    void setupCountryView() {
        if (mErpCurrency != null) {
            mTvAreaCode.setText(mErpCurrency.getCountryAreaCode());
        }
    }

    @Click({R.id.rlAreaCode, R.id.input_face, R.id.register_btn})
    void onClick(View view) {
        if (ClickManager.getInstance().isClicked())
            return;
        switch (view.getId()) {
            case R.id.rlAreaCode:
                showCountryDialog(new ArrayList<ErpCurrency>(erpCurrencyMap.values()), mErpCurrency);
                break;
            case R.id.input_face:
                MobclickAgentEvent.onEvent(UserActionCode.ZC030018);
                inputFace();
                break;
            case R.id.register_btn:
                MobclickAgentEvent.onEvent(UserActionCode.ZC030019);
                if (checkRegisterInfo()) {
                    CustomerResp customer = createCustomer();
                    doCreateCustomer(customer);
                }
                break;
            default:
                break;
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

    private void inputFace() {
       /* boolean available = BaiduFaceRecognition.getInstance().checkFaceServer();
        if (!available) {
            FacecognitionActivity.showFaceServerWarmDialog(getContext(), getChildFragmentManager());
            return;
        }
        startActivityForResult(BaiduFaceRecognition.getInstance().getRegistFaceIntent(true), FaceRequestCodeConstant.RC_DINNER_CUSTOMER_REGIEST);*/

    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FaceRequestCodeConstant.RC_DINNER_CUSTOMER_REGIEST && resultCode == Activity.RESULT_OK) {
            mFaceCode = data.getStringExtra(BaiduFaceRecognition.KEY_FACE_CODE);
            mFaceFeature = (FaceFeature) data.getSerializableExtra(BaiduFaceRecognition.KEY_FACE_FEATURE);
            chooseFaceDesc(true);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/

    /**
     * 修改人脸标记
     *
     * @param hasFaceCode
     */
    private void chooseFaceDesc(boolean hasFaceCode) {
        if (hasFaceCode) {
            mTvFaceDesc.setTextColor(getResources().getColor(R.color.color_32ADF6));
            /*if (mFaceFeature != null) {
                mTvFaceDesc.setText(getString(R.string.customer_face_approve_on) + (int) mFaceFeature.getBeauty() + getString(R.string.cent));
            } else {
                mTvFaceDesc.setText(R.string.customer_face_approve_on);
            }*/
        } else {
            mTvFaceDesc.setTextColor(getResources().getColor(R.color.color_bcbcbc));
            mTvFaceDesc.setText(R.string.customer_face_approve_off);
        }
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
                                //新增会员人数
                                CustomerUtil.addRegistMemberNumber(1);
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
                                showRegisterSuccessTips(customerNew);
                                // 登录成功后第二屏显示用户信息
                                /*DisplayUserInfo dUserInfo = DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_USERINFO_SHOW, customerNew, customerNew.integral, false, 0);
                                DisplayServiceManager.updateDisplay(getActivity(), dUserInfo);*/
                                EventBus.getDefault().post(new EventReadKeyboard(true, ""));// 发送成功到ReadKeyboardDialogFragment
                                DinnerPriviligeItemsFragment.showDisplayUserInfo(getActivity());
                                new DinnerCashManager().jumpAfterLogin("", customerNew, null);
                                /*if (mFaceFeature != null) {
                                    DinnerShopManager.getInstance().getLoginCustomer().faceGrade = (int) mFaceFeature.getBeauty();
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
     * 注册成功提示框
     */
    private void showRegisterSuccessTips(final CustomerResp customerNew) {
        DinnerCustomerLoginSaveDialog dialog = new DinnerCustomerLoginSaveDialog_();
        dialog.setCustomer(customerNew);
        dialog.setOnClick(new DinnerCustomerLoginSaveDialog.OnDialogBtnClickListener() {
            @Override
            public void cardStoreBtnClick() {
                showChargingDialog(customerNew, String.valueOf(customerNew.remainValue));
                getParentDialogFragment().dismiss();
            }

            @Override
            public void entityCardBtnClick() {
                /*Intent intent = new Intent(getContext(), CumtomerSaleCardsActivity_.class);
                intent.putExtra("customer", customerNew);
                intent.putExtra("customer_flag", CustomerContants.FLAG_CUSTOMER_BAND);
                startActivity(intent);*/
                getParentDialogFragment().dismiss();
            }

            @Override
            public void closeBtnClick() {
                getParentDialogFragment().dismiss();
                DialogUtil.dismissLoadingDialog();
            }
        });
        dialog.show(getChildFragmentManager(), "showRegisterSuccessTips");
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
        dialogFragment.show(getActivity().getSupportFragmentManager(), "ecCardCharging");
        getParentDialogFragment().dismiss();
    }

    /**
     * 获取父实例
     *
     * @return
     */
    DinnerCustomerLoginBasicDialogFragment getParentDialogFragment() {
        return (DinnerCustomerLoginBasicDialogFragment) getParentFragment();
    }

    /**
     * OpenId登录
     */
    public void onEventMainThread(EventOpenIdLoginInfo event) {
        if (event != null) {
            OpenIdLoginInfo openIdLoginInfo = event.getmOpenIdLoginInfo();
            if (openIdLoginInfo.getResult()) {
                if (getParentDialogFragment().requestUuid.equals(openIdLoginInfo.getUuid())) {//同一次登录请求
                    CommCustomer customer = openIdLoginInfo.getCustomer();
                    if (customer != null) {
                        loginByWeChat(customer.getId());
                    }
                }
            }
        }
    }

    /**
     * 使用微信扫码登录
     */
    private void loginByWeChat(Long customerId) {
        CustomerManager.getInstance().customerLogin(CustomerLoginType.MEMBER_ID, customerId.toString(), null, false, true, getResponseMemberLogin());
    }


    /**
     * 登录成功回调
     *
     * @return
     */
    private ResponseListener<MemberLoginVoResp> getResponseMemberLogin() {
        ResponseListener<MemberLoginVoResp> response = new EventResponseListener<MemberLoginVoResp>(UserActionEvent.DINNER_PAY_LOGIN_SCAN_WECHAT_CODE) {
            @Override
            public void onResponse(ResponseObject<MemberLoginVoResp> response) {
                try {
                    if (ResponseObject.isOk(response) && MemberLoginVoResp.isOk(response.getContent())) {
                        UserActionEvent.end(getEventName());
                        response.getContent().setCustomerLoginType(CustomerLoginType.MEMBER_ID);
                        CustomerLoginResp resp = response.getContent().getResult();
                        if (resp.customerIsDisable()) {//当前账号冻结
                            ToastUtil.showShortToast(R.string.order_dish_member_disabled);
                        } else {
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
                            // 登录成功后第二屏显示用户信息
                            /*DisplayUserInfo dUserInfo = DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_USERINFO_SHOW, customerNew, customerNew.integral, false, 0);
                            DisplayServiceManager.updateDisplay(getActivity(), dUserInfo);*/
                            EventBus.getDefault().post(new EventReadKeyboard(true, ""));// 发送成功到ReadKeyboardDialogFragment
                            DinnerPriviligeItemsFragment.showDisplayUserInfo(getActivity());
                            new DinnerCashManager().jumpAfterLogin("", customerNew, null);
                            DinnerShopManager.getInstance().getShoppingCart().setOpenIdenty(resp.getOpenId());
                            getParentDialogFragment().dismiss();
                        }
                    } else {
                        String msg;
                        if (response.getStatusCode() == 1126) {
                            msg = getString(R.string.order_dish_member_disabled);
                        } else {
                            msg = response.getContent().getErrorMessage();
                        }
                        ToastUtil.showShortToast(msg);
                        EventBus.getDefault().post(new EventReadKeyboard(false, msg));// 发送失败到ReadKeyboardDialogFragment
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
        return LoadingResponseListener.ensure(response, getFragmentManager());
    }
}
