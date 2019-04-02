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

    // 标题名称
    @ViewById(R.id.title_name)
    TextView mTitleName;

    // 生日
    @ViewById(R.id.birthday)
    protected TextView mBirthday;

    // 生日Button
    @ViewById(R.id.birthday_btn)
    protected ImageButton mBirthdayBtn;

    // 分组
    @ViewById(R.id.group)
    protected TextView mGroup;

    // 分组Button
    @ViewById(R.id.group_btn)
    protected ImageButton mGroupBtn;

    // 备注
    @ViewById(R.id.customer_memo)
    protected EditText mMemo;

    // 发票
    @ViewById(R.id.cusetomer_invoice)
    protected EditText mInvoice;

    // 地址
    @ViewById(R.id.customer_addr)
    protected EditText mAddress;

    // 名字
    @ViewById(R.id.customer_name)
    protected EditText mName;

    // 男
    @ViewById(R.id.customer_male)
    protected ImageView mMale;

    // 女
    @ViewById(R.id.customer_female)
    protected ImageView mFemale;

    // 电话
    @ViewById(R.id.customer_phonenum)
    protected EditText mPhonenum;

    // 密码
    @ViewById(R.id.customer_password_first)
    protected EditText mPassword;

    // 密码确认
    @ViewById(R.id.customer_password_second)
    protected EditText mPasswordSecond;

    // 喜好
    @ViewById(R.id.enjoy)
    LableTextEdit mEnjoy;

    @ViewById(R.id.customer_enjoy_btn)
    ImageButton mEnjoyBtn;

    @ViewById(R.id.enjoy_layout)
    LinearLayout mEnjoyLayout;

    // 导入
    @ViewById(R.id.customer_import)
    ImageView mImport;

    @ViewById(R.id.card_store)
    Button mCardStoreBtn;

    /**
     * 人脸识别
     */
    @ViewById(R.id.tvFace)
    TextView mTvFace;

    /**
     * 人脸识别 按钮
     */
    @ViewById(R.id.llFace)
    LinearLayout mLlFace;

    /**
     * 国籍
     */
    @ViewById(R.id.etCountry_CustomerInfo)
    EditText mEtCountry;

    /**
     * 国籍
     */
    @ViewById(R.id.ivCountry_CustomerInfo)
    ImageButton mIvCountry;

    /**
     *
     */
    @ViewById(R.id.entity_card_store)
    Button mEntityCardActivate;

    // 选中的喜好
    private ArrayList<String> mSelectSet = new ArrayList<String>();

    // 所有的喜好
    private List<String> enjoyValue = null;

    private CustomerEditPopWindow mEditPopWindow;

    private CustomerGroupPopWindowAdapter mGroupAdapter;

    private CustomerEnjoyPopWindowAdapter mEnjoyAdapter;

    private List<CustomerGroupLevel> list;

    private Long groupId;

    private Sex mSex = Sex.MALE;

    private boolean mMisPosInput = false;

    /**
     * 顾客分组
     */
    private CustomerGroupLevel customerGroup;

    /**
     * 业务类型
     */
    private int type = CustomerActivity.PARAM_ADD;

    /**
     * 旧会员唯一识别号
     */
    //private String uniqueCode = null;

    /**
     * 是否激活实体卡创建会员
     */
    private int activityType;

    private Drawable drawableWrong, drawableCorrect;
    private SaveCustomerDailog dialog;

    private String serviceId;
    private String customerId;

    private CustomerResp mCustomer;

    private ArrayList<EcCardInfo> mActiveList;

    /**
     * 当前商户国籍
     */
    private ErpCurrency mErpCurrency;

    private List<ErpCurrency> mErpCurrencyList;

    /**
     * faceCode
     */
    private String mFaceCode;

    /**
     * 跳转到 card的状态
     */
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
//        jinChInit();
    }

//    /**
//     * 金城初始化
//     */
//    private void jinChInit() {
//        isJinCh = ServerSettingCache.getInstance().isJinChBusiness();
//        if (isJinCh) {
//            String phone = getIntent().getStringExtra(EXTRA_JC_PHONE);
//            mPhonenum.setEnabled(false);
//            mPhonenum.setText(phone);
//        }
//    }

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

    // 验证密码的有效性
    private TextWatcher mPasswordSecondWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub

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

    /**
     * 根据intent获取customer
     */
    private void refreshInfo() {
        Intent intent = getIntent();
        if (intent != null) {
            type = intent.getIntExtra("type", CustomerActivity.PARAM_ADD);
            String areaCode = ShopInfoCfg.getInstance().getCurrency().getAreaCode();
            if (CustomerActivity.PARAM_ADD == type) {// 新增
                chooseFaceDesc(false);
                if (!TextUtils.isEmpty(areaCode)) {
                    mErpCurrency = mErpDal.queryErpCurrenctByAreaCode(areaCode);
                }
                mCustomer = new CustomerResp();
                mCustomer.groupId = Long.valueOf(999999);
                mImport.setVisibility(View.GONE);
                mLlFace.setVisibility(View.GONE);
                setCardVisble(false);
            } else if (CustomerActivity.PARAM_EDIT == type) {// 编辑
                mLlFace.setVisibility(View.GONE);
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
                mPhonenum.setEnabled(true); //编辑的时候也可以更改电话号码
                mPhonenum.setFocusable(true);
                mIvCountry.setEnabled(false);
                setPasswordDisable(true);
                mImport.setVisibility(View.GONE);
                //非会员不显示储值按钮
                //非会员不显示密码
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

    /**
     * 通过customer更新界面
     */
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
        // 设置分组
        if (customer.groupId != null) {
            groupId = customer.groupId;
            mGroup.setText(getGroup(groupId));
        }
        if (!TextUtils.isEmpty(customer.birthday)) {
            mBirthday.setText(customer.birthday);
        } else {
            mBirthday.setText(CustomerActivity.BIRTHDAY_DEFAULT);
        }

        // 设置喜好
        String enjoyStr = EnjoyManager.getInstance().getDtailEnjoyString(customer.interest);
        if (!TextUtils.isEmpty(enjoyStr)) {
            mEnjoy.setText(enjoyStr);
        }
        mSelectSet.clear();
        List<String> tempSelect = EnjoyManager.getInstance().getEnjoylist(customer.interest);
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

    /**
     * 设置选中的 sex
     */
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

    /**
     * 禁止密码框
     */
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
                    // 选择国籍
                    MobclickAgentEvent.onEvent(UserActionCode.GK020003);
                    showCountryDialog(mErpCurrencyList, mErpCurrency);
                    break;
                case R.id.card_store://虚拟卡储值
                    MobclickAgentEvent.onEvent(UserActionCode.GK020005);
                    if (mCustomer != null && !mCustomer.isDisabled()) {
                        cardStore();
                    }
                    break;
                case R.id.entity_card_store://实体卡激活
                    MobclickAgentEvent.onEvent(UserActionCode.GK020006);
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

    /**
     * 录入人脸
     */
    private void inputFace() {
        /*boolean available = BaiduFaceRecognition.getInstance().checkFaceServer();
        if (!available) {
            FacecognitionActivity.showFaceServerWarmDialog(getBaseContext(), getSupportFragmentManager());
            return;
        }
        startActivityForResult(BaiduFaceRecognition.getInstance().getRegistFaceIntent(), FaceRequestCodeConstant.RC_CUSTOMER_CREATE_REGIEST);*/
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
        CountryDialog dialog = new CountryDialog(this, erpCurrencyList, currentErpCurrency, new CountryGridAdapter.OnItemSelectedListener() {
            @Override
            public void onSelected(Long countryId, String name, ErpCurrency erpCurrency) {
                mErpCurrency = erpCurrency;
                setupCountryView();
            }
        });
        dialog.show();
    }

    /**
     * 转到会员存储界面
     */
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

    /**
     * 实体卡会员充值界面
     *
     * @param customer 顾客信息
     * @param balance  余额，实体卡充值时该值 传 null
     */
    private void showChargingDialog(CustomerResp customer, String balance) {
        CustomerChargingDialogFragment dialogFragment = new CustomerChargingDialogFragment_();
        Bundle args = new Bundle();
        args.putInt(CustomerChargingDialogFragment.KEY_FROM, CustomerChargingDialogFragment.FROM_MEMBER_CUSTOMER);//来自顾客界面
        args.putSerializable(CustomerChargingDialogFragment.KEY_CUSTOMER, customer);
        args.putString(CustomerChargingDialogFragment.KEY_BALANCE, balance);
        dialogFragment.setArguments(args);
        dialogFragment.setOnClickListener(new CustomerChargingDialogFragment.OnChargingClickListener() {
            @Override
            public void onClickClose() {
                // 新增时点x返回 退出
                if (type == CustomerActivity.PARAM_ADD) {
                    finish();
                }
            }
        });
        dialogFragment.show(getSupportFragmentManager(), "ecCardCharging");
    }


    /**
     * 显示会员导入窗口
     */
    @SuppressLint("SimpleDateFormat")
    private void showMemberImportDialog() {
        /*MemberImportDialog dialog = new MemberImportDialog(this, this.getSupportFragmentManager()) {
            @Override
            protected void doImport(MemberInfo member) {
                //uniqueCode = member.getUniqueCode();
                //setTitleText(uniqueCode);
                CustomerResp customer = new CustomerResp();
                customer.customerName = member.getName();
                customer.sex = member.getGender().value();
                customer.birthday = member.getBirthday();
                customer.groupId = TextUtils.isEmpty(member.getGroupId()) ? null : Long.parseLong(member.getGroupId());
                customer.interest = member.getEnvironmentHobby();
                customer.memo = member.getProfile();
                customer.invoice = member.getInvoice();
                customer.address = member.getAddress();
                customer.mobile = member.getMobile();
                updateUI(customer);
            }
        };
        dialog.show();*/
    }

    /**
     * 修改标题
     */
    private void setTitleText(String uniqueCode) {
        mTitleName.setText(getString(R.string.customer_import_edit_title, uniqueCode));
    }

    View.OnFocusChangeListener focusListener = new View.OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            // TODO Auto-generated method stub

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

    /**
     * 根据groupId 获取GroupName
     */
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
    /**
     * 为customer赋值
     */
    /**
     * @return
     */
    private boolean setUpdateCustomerValue() {
        // 用户名
        String name = mName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.showShortToast(R.string.customer_no_name);
            mName.requestFocus();
            return false;
        }
        mCustomer.customerName = name;
        // 生日
        String birthDate = mBirthday.getText().toString();
        if (TextUtils.isEmpty(birthDate)) {
            birthDate = CustomerActivity.BIRTHDAY_DEFAULT;
        }
        mCustomer.birthday = birthDate;
        // 手机
        if (CustomerActivity.PARAM_ADD == type) {
            // 是否开启隐私保护
            mCustomer.mobile = mPhonenum.getText().toString();
        } else {
            //if (!SpHelper.getDefault().getBoolean(QueueSettingSwitchFragment.MOBILE_PRIVACY, false)) {
                mCustomer.mobile = mPhonenum.getText().toString();
            //}
        }
        if (type == CustomerActivity.PARAM_ADD && mMisPosInput == false) {
            String passwrod = mPassword.getText().toString();
            String passwrodSed = mPasswordSecond.getText().toString();

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
            mCustomer.password = passwrod;
        }
        SexUtils.setSex(mCustomer, mSex);
        if (groupId != null) {
            mCustomer.groupId = groupId;
        }
        mCustomer.address = mAddress.getText().toString();
        mCustomer.invoice = mInvoice.getText().toString();
        mCustomer.memo = mMemo.getText().toString();
        mCustomer.interest = EnjoyManager.getInstance().tohoddyString(mSelectSet);
        if (mErpCurrency != null) {
            mCustomer.nation = mErpCurrency.getCountryEn();
            mCustomer.country = mErpCurrency.getCountryZh();
            mCustomer.nationalTelCode = mErpCurrency.getAreaCode();
        }
        if (!TextUtils.isEmpty(mFaceCode)) {
            mCustomer.faceCode = mFaceCode;
        }
        return true;
    }

    /**
     * 执行添加或更新
     */
    private void insertOrUpdate() {
        if (setUpdateCustomerValue()) {
            mCustomer.localModifyDateTime = System.currentTimeMillis();
            if (TextUtils.isEmpty(mCustomer.mobile) || !NumberUtil.isCellPhone(mCustomer.mobile)) {
                ToastUtil.showShortToast(getString(R.string.customer_mobile_regulation_error));
                return;
            }
            /*switch (type) {
                case CustomerActivity.PARAM_ADD:// 使用新接口,添加国籍
                    doCreateCustomer(mCustomer, uniqueCode);
                    break;
                case CustomerActivity.PARAM_EDIT:
                    CustomerOperates customerOperates = OperatesFactory.create(CustomerOperates.class);
                    try {
                        customerOperates.editCustomer(mCustomer, LoadingResponseListener.ensure(listener, getSupportFragmentManager()));
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                    break;
            }*/
            doCreateCustomer(mCustomer);
//            checkPassword(mCustomer.mobile,mCustomer.name,getContext()); //密码验证
        }
    }

    private void checkPassword(final String mobile, final String inputNo, final Context context) {
        //密码登陆
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

    /**
     * 新增会员
     */
    private void doCreateCustomer(final CustomerResp customer) {
        CustomerOperates oper = OperatesFactory.create(CustomerOperates.class);
        YFResponseListener<YFResponse<CustomerCreateResp>> listener = new YFResponseListener<YFResponse<CustomerCreateResp>>() {

            @Override
            public void onResponse(YFResponse<CustomerCreateResp> response) {
                try {
                    if (YFResponse.isOk(response)) {
                        if (customer.customerId == null) {//新增会员，会员数量+1
                            CustomerUtil.addRegistMemberNumber(1);
                        }
                        CustomerCreateResp info = response.getContent();
                        serviceId = info.getUuid();
                        customerId = info.getCustomerID();
                        mCustomer = info.getCustomer();
                        if (activityType == CustomerContants.CARD_ACTIVITY_CREATE_TYPE) {// 绑定实体卡创建会员
                            showBandCustomerDialog(info.getCustomer());
                        } else { // 创建会员
//							 发送eventbus 通知 列表界面刷新数据
                            EventBus.getDefault().post(new EventCreateOrEditCustomer(type, info.getCustomerListBean()));
                            //dialogShow();
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
        //if (TextUtils.isEmpty(uniqueCode)) {
        oper.saveCustomer(customer, LoadingYFResponseListener.ensure(listener, getSupportFragmentManager()));
        /*} else {
            oper.createMemberByPresetCustomer(customer, uniqueCode, LoadingYFResponseListener.ensure(listener, getSupportFragmentManager()));
        }*/
    }

    /**
     * 绑定会员
     */
    private void showBandCustomerDialog(CustomerResp customer) {
        /*if (mActiveList == null || mActiveList.size() == 0) {
            ToastUtil.showShortToast(getString(R.string.toast_customer_need_choose_card));
            return;
        }
        UionBindCardToCustomerDialogFragment dialogFragment = new UionBindCardToCustomerDialogFragment_();
        Bundle bundle = new Bundle();
        bundle.putSerializable("customer", customer);
        bundle.putSerializable("flag", UionBindCardToCustomerDialogFragment.FlagType.NEW_CUSTOMER_BIND);
        bundle.putSerializable("activeList", mActiveList);
        dialogFragment.setArguments(bundle);
        dialogFragment.setOnUionBindClickListener(new UionBindCardToCustomerDialogFragment.OnUionBindClickListener() {
            @Override
            public void onClose() {
            }

            @Override
            public void onActiveSuccessCallback(CumtomerSaleCardsActivity.ActiveStatus status) {
                finish();
            }

        });
        dialogFragment.show(getSupportFragmentManager(), "uionBandCustomerDialog");*/
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
                // 更新成功后，更新顾客详情信息
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


    /**
     * 编辑界面返回
     */
    protected void close() {
        /*if (!TextUtils.isEmpty(mName.getText().toString().trim())
                && !TextUtils.isEmpty(mPhonenum.getText().toString().trim())) {
            new CommonDialogFragmentBuilder(MainApplication.getInstance()).title(R.string.customer_giveup_save)
                    .negativeText(R.string.common_cancel)
                    .negativeLisnter(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                        }
                    })
                    .positiveText(R.string.common_submit)
                    .positiveLinstner(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            *//*if (mPageFrom == BeautyCustomerConstants.CustomerEditPage.OTHER && activityType != CustomerContants.CARD_ACTIVITY_CREATE_TYPE) {// 绑定实体卡创建会员
                                Intent intent = new Intent(BeautyCustomerEditActivity.this, BeautyCustomerActivity_.class);
                                startActivity(intent);
                            }*//*
                            finish();
                        }
                    })
                    .build()
                    .show(getSupportFragmentManager(), "cancelBeautyEditCustomer");
        } else {*/
            /*if (mPageFrom == BeautyCustomerConstants.CustomerEditPage.OTHER && activityType != CustomerContants.CARD_ACTIVITY_CREATE_TYPE) {// 绑定实体卡创建会员
                Intent intent = new Intent(BeautyCustomerEditActivity.this, BeautyCustomerActivity_.class);
                startActivity(intent);
            }*/
        finish();
        //}
    }

    /**
     * 分组popWindow
     */
    private void initPopwindow() {
        // 查询分组数据
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

    /**
     * 喜好popwindow
     */
    private void enjoyPopwindow() {
        enjoyValue = EnjoyManager.getInstance().getAllList();
        mEnjoyAdapter = new CustomerEnjoyPopWindowAdapter(this, enjoyValue);
        mEditPopWindow = new CustomerEditPopWindow(this, mEnjoyAdapter, mEnjoyBtn, enjoyItemClick);
    }

    /**
     * 分组Item事件
     */
    private OnItemClickListener groupItemClick = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            groupId = list.get(position).getId();
            mGroup.setText(list.get(position).getGroupName());
            mEditPopWindow.dismiss();
        }
    };

    /**
     * 喜好Item事件
     */
    private OnItemClickListener enjoyItemClick = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
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
        /*Intent intent = new Intent(BeautyCustomerEditActivity.this, CumtomerSaleCardsActivity_.class);
        intent.putExtra("customer", mCustomer);
        intent.putExtra("customer_flag", CustomerContants.FLAG_CUSTOMER_BAND);
        startActivity(intent);*/
        if (type == TO_CARD_TYPE.NEW) {
            finish();
        }
    }

    private void returnList() {
        Intent intent;
        //if (CustomerApplication.mCustomerBussinessType == CustomerAppConfig.CustomerBussinessType.BEAUTY) {
        intent = new Intent(getBaseContext(), BeautyCustomerActivity_.class);
        /*} else {
            intent = new Intent(getBaseContext(), CustomerActivity_.class);
        }*/
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

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FaceRequestCodeConstant.RC_CUSTOMER_CREATE_REGIEST && resultCode == Activity.RESULT_OK) {
            // FIXME 保存人脸认证数据
            mFaceCode = data.getStringExtra(BaiduFaceRecognition.KEY_FACE_CODE);
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
            mTvFace.setTextColor(getResources().getColor(R.color.color_32ADF6));
            mTvFace.setText(String.format(getString(R.string.customer_edit_face), getString(R.string.customer_face_approve_on)));
        } else {
            mTvFace.setTextColor(getResources().getColor(R.color.color_999999));
            mTvFace.setText(String.format(getString(R.string.customer_edit_face), getString(R.string.customer_face_approve_off)));
        }
    }
}
