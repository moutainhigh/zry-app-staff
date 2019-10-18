package com.zhongmei.beauty.customer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.beauty.customer.constants.BeautyCustomerConstants;
import com.zhongmei.bty.base.MainBaseActivity;
import com.zhongmei.bty.basemodule.auth.application.CustomerApplication;
import com.zhongmei.bty.basemodule.customer.bean.CustomerStatistic;
import com.zhongmei.bty.basemodule.customer.dialog.PasswordDialog;
import com.zhongmei.bty.basemodule.customer.dialog.country.CountryDialog;
import com.zhongmei.bty.basemodule.customer.dialog.country.CountryGridAdapter;
import com.zhongmei.bty.basemodule.customer.message.CustomerEditResp;
import com.zhongmei.bty.basemodule.customer.operates.CustomerOperates;
import com.zhongmei.bty.basemodule.customer.operates.interfaces.CustomerDal;
import com.zhongmei.bty.basemodule.customer.util.CustomerUtil;
import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.bty.basemodule.devices.liandipos.NewLDResponse;
import com.zhongmei.bty.basemodule.devices.liandipos.PosConnectManager;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardInfo;
import com.zhongmei.bty.basemodule.devices.mispos.dialog.ReadKeyboardDialogFragment;
import com.zhongmei.bty.basemodule.erp.bean.ErpCurrency;
import com.zhongmei.bty.basemodule.erp.operates.ErpCommercialRelationDal;
import com.zhongmei.bty.basemodule.input.NumberKeyBoardUtils;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.bty.common.util.CommonKeyBorad;
import com.zhongmei.bty.common.view.DatePickerDialogFragment;
import com.zhongmei.bty.common.view.DatePickerDialogFragment.DateSetListener;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.util.MD5;
import com.zhongmei.bty.commonmodule.util.NumberUtil;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.customer.CustomerActivity;
import com.zhongmei.bty.customer.CustomerChargingDialogFragment;
import com.zhongmei.bty.customer.CustomerChargingDialogFragment_;
import com.zhongmei.bty.customer.LableTextEdit;
import com.zhongmei.bty.customer.adapter.CustomerEnjoyPopWindowAdapter;
import com.zhongmei.bty.customer.adapter.CustomerGroupPopWindowAdapter;
import com.zhongmei.bty.customer.bean.EnjoyManager;
import com.zhongmei.bty.customer.event.EventCreateOrEditCustomer;
import com.zhongmei.bty.customer.event.EventRefreshBalance;
import com.zhongmei.bty.customer.event.SaveDialogCloseEvent;
import com.zhongmei.bty.customer.util.AppUtil;
import com.zhongmei.bty.customer.util.CustomerContants;
import com.zhongmei.bty.customer.util.CustomerRequestManager;
import com.zhongmei.bty.customer.util.SexUtils;
import com.zhongmei.bty.customer.views.CustomerEditPopWindow;
import com.zhongmei.bty.customer.views.SaveCustomerDailog;
import com.zhongmei.bty.customer.views.SaveCustomerDailog_;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.bean.YFResponse;
import com.zhongmei.yunfu.bean.req.CustomerCreateResp;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.context.util.DateTimeUtils;
import com.zhongmei.yunfu.data.LoadingYFResponseListener;
import com.zhongmei.yunfu.db.entity.crm.CustomerGroupLevel;
import com.zhongmei.yunfu.db.enums.Sex;
import com.zhongmei.yunfu.net.volley.Response.ErrorListener;
import com.zhongmei.yunfu.net.volley.Response.Listener;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.yunfu.resp.YFResponseListener;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.util.UserActionCode;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import de.greenrobot.event.EventBus;

@EActivity(R.layout.beauty_customer_edit)
public class BeautyCustomerEditActivity extends MainBaseActivity {
    private static final String TAG = BeautyCustomerEditActivity.class.getSimpleName();

    @Bean(CommonKeyBorad.class)
    protected CommonKeyBorad mNumberKeyBorad;

        @ViewById(R.id.title_name)
    TextView mTitleName;

        @ViewById(R.id.birthday)
    protected TextView mBirthday;

        @ViewById(R.id.birthday_btn)
    protected ImageButton mBirthdayBtn;

        @ViewById(R.id.group)
    protected TextView mGroup;

        @ViewById(R.id.group_btn)
    protected ImageButton mGroupBtn;

        @ViewById(R.id.customer_memo)
    protected EditText mMemo;

        @ViewById(R.id.cusetomer_invoice)
    protected EditText mInvoice;

        @ViewById(R.id.customer_addr)
    protected EditText mAddress;

        @ViewById(R.id.customer_name)
    protected EditText mName;

        @ViewById(R.id.customer_male)
    protected ImageView mMale;

        @ViewById(R.id.customer_female)
    protected ImageView mFemale;

        @ViewById(R.id.customer_phonenum)
    protected EditText mPhonenum;

        @ViewById(R.id.customer_password_first)
    protected EditText mPassword;

        @ViewById(R.id.customer_password_second)
    protected EditText mPasswordSecond;

        @ViewById(R.id.enjoy)
    LableTextEdit mEnjoy;

    @ViewById(R.id.customer_enjoy_btn)
    ImageButton mEnjoyBtn;

    @ViewById(R.id.enjoy_layout)
    LinearLayout mEnjoyLayout;

        @ViewById(R.id.customer_import)
    ImageView mImport;

    @ViewById(R.id.card_store)
    Button mCardStoreBtn;


    @ViewById(R.id.tvFace)
    TextView mTvFace;


    @ViewById(R.id.llFace)
    LinearLayout mLlFace;


    @ViewById(R.id.etCountry_CustomerInfo)
    EditText mEtCountry;


    @ViewById(R.id.ivCountry_CustomerInfo)
    ImageButton mIvCountry;


    @ViewById(R.id.entity_card_store)
    Button mEntityCardActivate;

        private ArrayList<String> mSelectSet = new ArrayList<String>();

        private List<String> enjoyValue = null;

    private CustomerEditPopWindow mEditPopWindow;

    private CustomerGroupPopWindowAdapter mGroupAdapter;

    private CustomerEnjoyPopWindowAdapter mEnjoyAdapter;

    private List<CustomerGroupLevel> list;

    private Long groupId;

    private Sex mSex = Sex.MALE;

    private boolean mMisPosInput = false;


    private CustomerGroupLevel customerGroup;


    private int type = CustomerActivity.PARAM_ADD;




    private int activityType;

    private Drawable drawableWrong, drawableCorrect;
    private SaveCustomerDailog dialog;

    private String serviceId;
    private String customerId;

    private CustomerResp mCustomer;

    private ArrayList<EcCardInfo> mActiveList;


    private ErpCurrency mErpCurrency;

    private List<ErpCurrency> mErpCurrencyList;


    private String mFaceCode;


    private enum TO_CARD_TYPE {
        EDIT, NEW
    }

    private ErpCommercialRelationDal mErpDal;

    @BeautyCustomerConstants.CustomerEditPage
    private int mPageFrom = BeautyCustomerConstants.CustomerEditPage.OTHER;

    @AfterViews
    void init() {
        getIntentData();
        mErpDal = OperatesFactory.create(ErpCommercialRelationDal.class);
        mErpCurrencyList = mErpDal.queryErpCurrenctList();

        drawableWrong = getResources().getDrawable(R.drawable.wrong);
        drawableWrong.setBounds(0, 0, drawableWrong.getMinimumWidth(), drawableWrong.getMinimumHeight());

        drawableCorrect = getResources().getDrawable(R.drawable.correct);
        drawableCorrect.setBounds(0, 0, drawableCorrect.getMinimumWidth(), drawableCorrect.getMinimumHeight());

        Settings.System.putInt(getContentResolver(), Settings.System.TEXT_SHOW_PASSWORD, 0);

        NumberKeyBoardUtils.setTouchListener(mPhonenum);
        NumberKeyBoardUtils.setTouchListener(mPassword);
        NumberKeyBoardUtils.setTouchListener(mPasswordSecond);
        mPhonenum.setOnFocusChangeListener(focusListener);
        mPassword.setOnFocusChangeListener(focusListener);
        mPasswordSecond.setOnFocusChangeListener(focusListener);
        mPasswordSecond.addTextChangedListener(mPasswordSecondWatcher);
        setupEditTextFocus(mName);
        refreshInfo();
        initDialog();
    }


    private void setupEditTextFocus(final EditText editText) {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    String msg = editText.getText().toString();
                    if (!TextUtils.isEmpty(msg)) {
                        editText.setSelection(msg.length());
                    }
                }
            }
        });
    }

        private TextWatcher mPasswordSecondWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mPassword.getText().toString().equals(s.toString())) {
                mPasswordSecond.setCompoundDrawables(null, null, drawableCorrect, null);
            } else if (!mPassword.getText().toString().startsWith(s.toString())) {
                mPasswordSecond.setCompoundDrawables(null, null, drawableWrong, null);
            } else if (mPassword.getText().toString().startsWith(s.toString())) {
                mPasswordSecond.setCompoundDrawables(null, null, null, null);
            }
        }
    };

    private void getIntentData() {
        mPageFrom = getIntent().getIntExtra(BeautyCustomerConstants.KEY_CUSTOMER_EDIT_PAGE, BeautyCustomerConstants.CustomerEditPage.OTHER);
    }


    private void refreshInfo() {
        Intent intent = getIntent();
        if (intent != null) {
            type = intent.getIntExtra("type", CustomerActivity.PARAM_ADD);
            String areaCode = ShopInfoCfg.getInstance().getCurrency().getAreaCode();
            if (CustomerActivity.PARAM_ADD == type) {                chooseFaceDesc(false);
                if (!TextUtils.isEmpty(areaCode)) {
                    mErpCurrency = mErpDal.queryErpCurrenctByAreaCode(areaCode);
                }
                mCustomer = new CustomerResp();
                mCustomer.groupId = Long.valueOf(999999);
                mImport.setVisibility(View.GONE);
                mLlFace.setVisibility(View.GONE);
                setCardVisble(false);
            } else if (CustomerActivity.PARAM_EDIT == type) {                mLlFace.setVisibility(View.GONE);
                mCustomer = (CustomerResp) intent.getSerializableExtra(CustomerContants.KEY_CUSTOMER_EDIT);
                if (!TextUtils.isEmpty(mCustomer.nationalTelCode)) {
                    mErpCurrency = mErpDal.queryErpCurrenctByAreaCode(mCustomer.nationalTelCode);
                }
                mTitleName.setText(getString(R.string.edit_customer));
                if (mCustomer.isDisabled()) {
                    setCardVisble(false);
                } else {
                    setCardVisble(true);
                }
                mPhonenum.setEnabled(true);                 mPhonenum.setFocusable(true);
                mIvCountry.setEnabled(false);
                setPasswordDisable(true);
                mImport.setVisibility(View.GONE);
                                                if (mCustomer == null || mCustomer.levelId == null) {
                    setCardVisble(false);
                } else {
                    mPassword.setText("******");
                    mPasswordSecond.setText("******");
                }

                if (mCustomer != null) {
                    customerId = String.valueOf(mCustomer.customerId);
                    serviceId = mCustomer.synFlag;
                }
            }
            activityType = intent.getIntExtra(CustomerContants.BUNDLE_KEY_CREATE_MEMBER, 0);
            if (activityType == CustomerContants.CARD_ACTIVITY_CREATE_TYPE) {
                mActiveList = (ArrayList<EcCardInfo>) intent.getSerializableExtra(CustomerContants.BUNDLE_KEY_ACTIVE_LIST);
            }
        }
        updateUI(mCustomer);
    }


    private void updateUI(CustomerResp customer) {
        if (customer == null) {
            return;
        }
        String name = customer.customerName;
        if (!TextUtils.isEmpty(name)) {
            mName.setText(name);
        }
        if (customer.sex != null) {
            checkSex(SexUtils.getSexEnum(String.valueOf(customer.sex)));
        }

        String phone = customer.mobile;
        if (!TextUtils.isEmpty(phone)) {
            mPhonenum.setText(AppUtil.getTel(phone));

        }
                if (customer.groupId != null) {
            groupId = customer.groupId;
            mGroup.setText(getGroup(groupId));
        }
        if (!TextUtils.isEmpty(customer.birthday)) {
            mBirthday.setText(customer.birthday);
        } else {
            mBirthday.setText(CustomerActivity.BIRTHDAY_DEFAULT);
        }

                String enjoyStr = EnjoyManager.getInstance().getDtailEnjoyString(customer.hobby);
        if (!TextUtils.isEmpty(enjoyStr)) {
            mEnjoy.setText(enjoyStr);
        }
        mSelectSet.clear();
        List<String> tempSelect = EnjoyManager.getInstance().getEnjoylist(customer.hobby);
        mSelectSet.addAll(tempSelect);

        mMemo.setText(customer.memo);
        if (!TextUtils.isEmpty(customer.invoice)) {
            mInvoice.setText(customer.invoice);
        } else {
            if (!TextUtils.isEmpty(customer.invoiceTitle)) {
                mInvoice.setText(customer.invoiceTitle);
            }
        }
        mAddress.setText(customer.address);

        setupCountryView();
    }

    private void setupCountryView() {
        if (mErpCurrency != null) {
            mEtCountry.setText(mErpCurrency.getAreaCode());
        }
    }


    private void checkSex(Sex sexValue) {
        if (sexValue == Sex.MALE) {
            mMale.setImageResource(R.drawable.queue_takeno_man_selected);
            mFemale.setImageResource(R.drawable.queue_takeno_woman_notselected);
            mSex = Sex.MALE;
        } else {
            mMale.setImageResource(R.drawable.queue_takeno_man_notselected);
            mFemale.setImageResource(R.drawable.queue_takeno_woman_selected);
            mSex = Sex.FEMALE;
        }
    }

    @Override
    protected void onNewIntent(Intent newIntent) {
        super.onNewIntent(newIntent);
        setIntent(newIntent);
        refreshInfo();
    }


    private void setPasswordDisable(boolean en) {
        if (!en) {
            mPassword.setBackground(getResources().getDrawable(R.drawable.booking_detail_bg));
            mPasswordSecond.setBackground(getResources().getDrawable(R.drawable.booking_detail_bg));
            mPassword.setEnabled(true);
            mPassword.setFocusable(true);
            mPasswordSecond.setEnabled(true);
            mPasswordSecond.setFocusable(true);

            mPassword.setHint(getString(R.string.customer_input_password_first));
            mPasswordSecond.setHint(getString(R.string.customer_input_password_first));
            mPassword.setText("");
            mPasswordSecond.setText("");
        } else {
            mPassword.setBackground(getResources().getDrawable(R.drawable.password_diable_bg));
            mPasswordSecond.setBackground(getResources().getDrawable(R.drawable.password_diable_bg));
            mPassword.setEnabled(false);
            mPassword.setFocusable(false);
            mPasswordSecond.setEnabled(false);
            mPasswordSecond.setFocusable(false);

            mPassword.setHint("");
            mPasswordSecond.setHint("");
        }
    }

    @Click({R.id.customer_male, R.id.customer_female, R.id.group_btn, R.id.birthday_btn, R.id.save_btn, R.id.back_btn,
            R.id.customer_enjoy_btn, R.id.customer_import, R.id.card_store, R.id.entity_card_store, R.id.ivCountry_CustomerInfo, R.id.btnFace})
    void click(View v) {
        if (!ClickManager.getInstance().isClicked()) {
            switch (v.getId()) {
                case R.id.customer_male:
                    mSex = Sex.MALE;
                    checkSex(mSex);
                    break;
                case R.id.customer_female:
                    mSex = Sex.FEMALE;
                    checkSex(mSex);
                    break;
                case R.id.group_btn:
                    MobclickAgentEvent.onEvent(UserActionCode.GK020001);
                    initPopwindow();
                    break;
                case R.id.birthday_btn:
                    MobclickAgentEvent.onEvent(UserActionCode.GK020002);
                    DatePickerDialogFragment.show(getSupportFragmentManager(),
                            TextUtils.isEmpty(mBirthday.getText().toString().trim()) ? "1991-1-1" : mBirthday.getText().toString().trim(),
                            new DateSetListener() {

                                @Override
                                public void OnDateSet(String date) {
                                    if (DateTimeUtils.formatDate(date) >= System.currentTimeMillis()) {
                                        ToastUtil.showShortToast(getString(R.string.timing_is_invalid));
                                    } else {
                                        mBirthday.setText(date);
                                    }
                                }
                            });
                    break;
                case R.id.save_btn:
                    if (type == CustomerActivity.PARAM_ADD) {
                        MobclickAgentEvent.onEvent(UserActionCode.GK020004);
                    } else {
                        MobclickAgentEvent.onEvent(UserActionCode.GK020007);
                    }
                    if (!ClickManager.getInstance().isClicked(R.id.save_btn)) {
                        insertOrUpdate();
                    }
                    break;
                case R.id.back_btn:
                    close();
                    break;
                case R.id.customer_enjoy_btn:
                    enjoyPopwindow();
                    break;
                case R.id.customer_import:
                    showMemberImportDialog();
                    break;
                case R.id.ivCountry_CustomerInfo:
                                        MobclickAgentEvent.onEvent(UserActionCode.GK020003);
                    showCountryDialog(mErpCurrencyList, mErpCurrency);
                    break;
                case R.id.card_store:                    MobclickAgentEvent.onEvent(UserActionCode.GK020005);
                    if (mCustomer != null && !mCustomer.isDisabled()) {
                        cardStore();
                    }
                    break;
                case R.id.entity_card_store:                    MobclickAgentEvent.onEvent(UserActionCode.GK020006);
                    if (mCustomer != null && !mCustomer.isDisabled()) {
                        VerifyHelper.verifyAlert(this, CustomerApplication.PERMISSION_CUSTOMER_CARD_ENABLE,
                                new VerifyHelper.Callback() {
                                    @Override
                                    public void onPositive(User user, String code, Auth.Filter filter) {
                                        super.onPositive(user, code, filter);
                                        toEntityCardSalePage(TO_CARD_TYPE.EDIT);
                                    }
                                });
                    }
                    break;
                case R.id.btnFace:
                    inputFace();
                    break;
                default:
                    break;
            }
        }
    }


    private void inputFace() {

    }



    public void showCountryDialog(List<ErpCurrency> erpCurrencyList, ErpCurrency currentErpCurrency) {
        if (erpCurrencyList == null || erpCurrencyList.size() == 0) {
            ToastUtil.showShortToast(getString(R.string.customer_erpcurrency_empty));
            return;
        }
        CountryDialog dialog = new CountryDialog(this, erpCurrencyList, currentErpCurrency, new CountryGridAdapter.OnItemSelectedListener() {
            @Override
            public void onSelected(Long countryId, String name, ErpCurrency erpCurrency) {
                mErpCurrency = erpCurrency;
                setupCountryView();
            }
        });
        dialog.show();
    }


    private void cardStore() {
        if (TextUtils.isEmpty(serviceId)) {
            ToastUtil.showShortToast(R.string.customer_get_info_fail);
            return;
        }
        if (TextUtils.isEmpty(customerId)) {
            ToastUtil.showShortToast(R.string.customer_get_info_fail);
            return;
        }
        CustomerRequestManager.getInstance().getMemberStatistics(serviceId,
                new Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            CustomerStatistic mCustomerStatistic = CustomerStatistic.initFromJson(response);
                            showChargingDialog(mCustomer, mCustomerStatistic.memberBalance + "");
                        } catch (Exception e) {
                            Log.e(TAG, "", e);
                        }
                    }
                },
                new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastUtil.showShortToast(R.string.customer_get_info_fail);
                    }
                },
                this);
    }


    private void showChargingDialog(CustomerResp customer, String balance) {
        CustomerChargingDialogFragment dialogFragment = new CustomerChargingDialogFragment_();
        Bundle args = new Bundle();
        args.putInt(CustomerChargingDialogFragment.KEY_FROM, CustomerChargingDialogFragment.FROM_MEMBER_CUSTOMER);        args.putSerializable(CustomerChargingDialogFragment.KEY_CUSTOMER, customer);
        args.putString(CustomerChargingDialogFragment.KEY_BALANCE, balance);
        dialogFragment.setArguments(args);
        dialogFragment.setOnClickListener(new CustomerChargingDialogFragment.OnChargingClickListener() {
            @Override
            public void onClickClose() {
                                if (type == CustomerActivity.PARAM_ADD) {
                    finish();
                }
            }
        });
        dialogFragment.show(getSupportFragmentManager(), "ecCardCharging");
    }



    @SuppressLint("SimpleDateFormat")
    private void showMemberImportDialog() {

    }


    private void setTitleText(String uniqueCode) {
        mTitleName.setText(getString(R.string.customer_import_edit_title, uniqueCode));
    }

    View.OnFocusChangeListener focusListener = new View.OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {

            if (!hasFocus) {
                mNumberKeyBorad.setCurrentInput(null);
                switch (v.getId()) {
                    case R.id.customer_password_first:
                        if (mPassword.getText().toString().length() == 6) {
                            mPassword.setCompoundDrawables(null, null, drawableCorrect, null);
                        } else if (TextUtils.isEmpty(mPassword.getText().toString())) {
                            mPassword.setCompoundDrawables(null, null, null, null);
                        } else {
                            mPassword.setCompoundDrawables(null, null, drawableWrong, null);
                        }
                        break;
                    case R.id.customer_password_second:
                        if (TextUtils.isEmpty(mPasswordSecond.getText().toString())) {
                            mPasswordSecond.setCompoundDrawables(null, null, null, null);
                        } else if (mPassword.getText().toString().equals(mPasswordSecond.getText().toString())) {
                            mPasswordSecond.setCompoundDrawables(null, null, drawableCorrect, null);
                        } else if (mPasswordSecond.getText().toString().length() != 6) {
                            mPasswordSecond.setCompoundDrawables(null, null, drawableWrong, null);
                        }
                        break;
                }
                return;
            }
            switch (v.getId()) {
                case R.id.customer_phonenum:
                    mNumberKeyBorad.setDefaultSuffix("");
                    mNumberKeyBorad.setCurrentInput(mPhonenum);
                    break;
                case R.id.customer_password_first:
                    mPassword.setCompoundDrawables(null, null, null, null);
                    mNumberKeyBorad.setDefaultSuffix("");
                    mNumberKeyBorad.setCurrentInput(mPassword);
                    break;
                case R.id.customer_password_second:
                    mPasswordSecond.setCompoundDrawables(null, null, null, null);
                    mNumberKeyBorad.setDefaultSuffix("");
                    mNumberKeyBorad.setCurrentInput(mPasswordSecond);
                    break;
            }
        }

    };


    private String getGroup(Long groupId) {
        try {
            CustomerDal customerDal = OperatesFactory.create(CustomerDal.class);
            customerGroup = customerDal.findCustomerGroupById(groupId);
            if (customerGroup.getGroupName() != null) {
                return customerGroup.getGroupName();
            }
        } catch (Exception e) {
        }
        return getString(R.string.customer_not_set);

    }


    private boolean setUpdateCustomerValue() {
                String name = mName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.showShortToast(R.string.customer_no_name);
            mName.requestFocus();
            return false;
        }
        mCustomer.customerName = name;
                String birthDate = mBirthday.getText().toString();
        if (TextUtils.isEmpty(birthDate)) {
            birthDate = CustomerActivity.BIRTHDAY_DEFAULT;
        }
        mCustomer.birthday = birthDate;
                if (CustomerActivity.PARAM_ADD == type) {
                        mCustomer.mobile = mPhonenum.getText().toString();
        } else {
                            mCustomer.mobile = mPhonenum.getText().toString();
                    }

        if (TextUtils.isEmpty(mCustomer.mobile) || !NumberUtil.isCellPhone(mCustomer.mobile)) {
            ToastUtil.showShortToast(getString(R.string.customer_mobile_regulation_error));
            return false;
        }

        if (type == CustomerActivity.PARAM_ADD && mMisPosInput == false) {
            String passwrod = mPassword.getText().toString();
            String passwrodSed = mPasswordSecond.getText().toString();

            if(TextUtils.isEmpty(passwrod)){
                mCustomer.password = mCustomer.mobile.substring(mCustomer.mobile.length()-6,mCustomer.mobile.length());
            }else{
                if (TextUtils.isEmpty(passwrod)) {
                    ToastUtil.showShortToast(R.string.customer_no_password);
                    return false;
                } else if (passwrod.length() != 6) {
                    ToastUtil.showShortToast(R.string.customer_password_length_6);
                    return false;
                } else if (!passwrod.equals(passwrodSed)) {
                    ToastUtil.showShortToast(R.string.customer_password_not_match);
                    return false;
                }
                mCustomer.password=passwrod;
            }
        }
        SexUtils.setSex(mCustomer, mSex);
        if (groupId != null) {
            mCustomer.groupId = groupId;
        }
        mCustomer.address = mAddress.getText().toString();
        mCustomer.invoice = mInvoice.getText().toString();
        mCustomer.memo = mMemo.getText().toString();
        mCustomer.hobby = mEnjoy.getText().toString();
        if (mErpCurrency != null) {
            mCustomer.nation = mErpCurrency.getCountryEn();
            mCustomer.country = mErpCurrency.getCountryZh();
            mCustomer.nationalTelCode = mErpCurrency.getAreaCode();
        }
        if (!TextUtils.isEmpty(mFaceCode)) {
            mCustomer.faceCode = mFaceCode;
        }
        mCustomer.localModifyDateTime = System.currentTimeMillis();
        return true;
    }


    private void insertOrUpdate() {
        if (setUpdateCustomerValue()) {

            doCreateCustomer(mCustomer);
        }
    }

    private void checkPassword(final String mobile, final String inputNo, final Context context) {
                final PasswordDialog dialog;
        dialog = new PasswordDialog(context) {
            @Override
            public void close() {
                dismiss();
                DisplayServiceManager.doCancel(context);
            }
        };

        dialog.setMembeName(inputNo);
        dialog.setLisetner(new PasswordDialog.PasswordCheckLisetner() {
            @Override
            public void checkPassWord(String password) {
                password = new MD5().getMD5ofStr(password);
                mCustomer.password=password;
                doCreateCustomer(mCustomer);
            }

            @Override
            public void showPassWord(String password) {
            }

            @Override
            public void showReadKeyBord() {
            }
        });
        dialog.show();
    }


    private void doCreateCustomer(final CustomerResp customer) {
        CustomerOperates oper = OperatesFactory.create(CustomerOperates.class);
        YFResponseListener<YFResponse<CustomerCreateResp>> listener = new YFResponseListener<YFResponse<CustomerCreateResp>>() {

            @Override
            public void onResponse(YFResponse<CustomerCreateResp> response) {
                try {
                    if (YFResponse.isOk(response)) {
                        if (customer.customerId == null) {                            CustomerUtil.addRegistMemberNumber(1);
                        }
                        CustomerCreateResp info = response.getContent();
                        serviceId = info.getUuid();
                        customerId = info.getCustomerID();
                        mCustomer = info.getCustomer();
                        if (activityType == CustomerContants.CARD_ACTIVITY_CREATE_TYPE) {                            showBandCustomerDialog(info.getCustomer());
                        } else {                             EventBus.getDefault().post(new EventCreateOrEditCustomer(type, info.getCustomerListBean()));
                                                        setCardVisble(true);
                            finish();
                        }

                    } else {
                        ToastUtil.showShortToast(response.getMessage());
                    }
                    UserActionEvent.end(UserActionEvent.CUSTOMER_CREATE);
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
            }

            @Override
            public void onError(VolleyError error) {
                UserActionEvent.end(UserActionEvent.CUSTOMER_CREATE);
                ToastUtil.showShortToast(error.getMessage());
            }

        };
                oper.saveCustomer(customer, LoadingYFResponseListener.ensure(listener, getSupportFragmentManager()));

    }


    private void showBandCustomerDialog(CustomerResp customer) {

    }

    private ResponseListener<CustomerEditResp> listener = new ResponseListener<CustomerEditResp>() {
        @Override
        public void onResponse(ResponseObject<CustomerEditResp> response) {
            Log.e("response", response.toString());
            if (ResponseObject.isOk(response)) {
                CustomerEditResp resp = response.getContent();
                CustomerResp customer = resp.customers.get(0);
                customer.customerId = customer.id;
                customer.groupId = customer.groupId;
                customer.customerName = customer.name;
                if (mCustomer != null) {
                    customer.levelId = mCustomer.levelId;
                }
                                EventBus.getDefault().post(new EventCreateOrEditCustomer(type, customer.getCustomerListBean()));
                finish();
            }

            if (!TextUtils.isEmpty(response.getMessage())) {
                ToastUtil.showShortToast(response.getMessage());
            } else {
                ToastUtil.showLongToast(R.string.customer_create_fail);
            }

            UserActionEvent.end(UserActionEvent.CUSTOMER_EDIT);
        }

        @Override
        public void onError(VolleyError error) {
            UserActionEvent.end(UserActionEvent.CUSTOMER_EDIT);
            ToastUtil.showShortToast(error.getMessage());
        }
    };



    protected void close() {


        finish();
            }


    private void initPopwindow() {
                try {
            CustomerDal customerDal = OperatesFactory.create(CustomerDal.class);
            list = customerDal.findCustomerGroupAll();
        } catch (Exception e) {

        }
        if (list == null || list.size() <= 0) {
            ToastUtil.showShortToast(R.string.customer_no_group);
            return;
        }
        if (mCustomer != null && mCustomer.groupId != null) {
            for (int i = list.size() - 1; i >= 0; i--) {
                if (list.get(i).getId().equals(mCustomer.groupName)) {
                    list.remove(i);
                }
            }
        } else {
            for (int i = list.size() - 1; i >= 0; i--) {
                if (list.get(i).getId().equals("999999")) {
                    list.remove(i);
                }
            }
        }
        mGroupAdapter = new CustomerGroupPopWindowAdapter(this, list);
        mEditPopWindow = new CustomerEditPopWindow(this, mGroupAdapter, mGroupBtn, groupItemClick);
    }


    private void enjoyPopwindow() {
        enjoyValue = EnjoyManager.getInstance().getAllList();
        mEnjoyAdapter = new CustomerEnjoyPopWindowAdapter(this, enjoyValue);
        mEditPopWindow = new CustomerEditPopWindow(this, mEnjoyAdapter, mEnjoyBtn, enjoyItemClick);
    }


    private OnItemClickListener groupItemClick = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        groupId = list.get(position).getId();
            mGroup.setText(list.get(position).getGroupName());
            mEditPopWindow.dismiss();
        }
    };


    private OnItemClickListener enjoyItemClick = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (mSelectSet.contains(enjoyValue.get(position))) {
                mSelectSet.remove(enjoyValue.get(position));
            } else {
                mSelectSet.add(enjoyValue.get(position));
            }
            mEnjoy.setText(EnjoyManager.getInstance().hoddytoString(mSelectSet));
        }
    };

    private void initDialog() {
        dialog = new SaveCustomerDailog_();
        dialog.setOnClick(new SaveCustomerDailog.DialogBtnClickListener() {

            @Override
            public void cardStoreBtnClick() {
                cardStore();
            }

            @Override
            public void entityCardBtnClick() {
                toEntityCardSalePage(TO_CARD_TYPE.NEW);
            }

            @Override
            public void returnListBtnClick() {
                returnList();
            }
        });
    }

    private void toEntityCardSalePage(TO_CARD_TYPE type) {

        if (type == TO_CARD_TYPE.NEW) {
            finish();
        }
    }

    private void returnList() {
        Intent intent;
                intent = new Intent(getBaseContext(), BeautyCustomerActivity_.class);

        startActivity(intent);
        finish();
    }

    private void dialogShow() {
        if (dialog == null) {
            dialog = new SaveCustomerDailog_();
        }
        dialog.setCustomer(mCustomer);
        dialog.show(getSupportFragmentManager(), "CustomerEditDialog");
    }


    private void setCardVisble(boolean enabled) {
        if (enabled) {
            mCardStoreBtn.setBackgroundResource(R.drawable.beauty_selector_btn_yellow_oval_shadow);
            mEntityCardActivate.setBackgroundResource(R.drawable.beauty_selector_btn_yellow_oval_shadow);
        } else {
            mCardStoreBtn.setBackgroundResource(R.drawable.beauty_btn_yellow_oval_shadow_disable);
            mEntityCardActivate.setBackgroundResource(R.drawable.beauty_btn_yellow_oval_shadow_disable);
        }
        mCardStoreBtn.setEnabled(enabled);
        mEntityCardActivate.setEnabled(enabled);
    }

    public void onEvent(SaveDialogCloseEvent action) {
        if (dialog != null && !dialog.isHidden()) {
            dialog.dismiss();
        }
    }


    public void onEventMainThread(EventRefreshBalance balance) {
        if (CustomerActivity.PARAM_ADD == type) {
            finish();
        }
    }




    private void chooseFaceDesc(boolean hasFaceCode) {
        if (hasFaceCode) {
            mTvFace.setTextColor(getResources().getColor(R.color.color_32ADF6));
            mTvFace.setText(String.format(getString(R.string.customer_edit_face), getString(R.string.customer_face_approve_on)));
        } else {
            mTvFace.setTextColor(getResources().getColor(R.color.color_999999));
            mTvFace.setText(String.format(getString(R.string.customer_edit_face), getString(R.string.customer_face_approve_off)));
        }
    }
}
