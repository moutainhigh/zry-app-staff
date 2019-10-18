package com.zhongmei.bty.customer.views;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.bty.basemodule.customer.message.MemberUpgradeReq;
import com.zhongmei.bty.basemodule.customer.message.MemberUpgradeResp;
import com.zhongmei.bty.basemodule.customer.operates.CustomerOperates;
import com.zhongmei.bty.basemodule.erp.bean.ErpCurrency;
import com.zhongmei.bty.basemodule.erp.operates.ErpCommercialRelationDal;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.context.util.DateTimeUtils;
import com.zhongmei.bty.common.view.DatePickerDialogFragment;
import com.zhongmei.bty.common.view.DatePickerDialogFragment.DateSetListener;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.bty.commonmodule.util.MD5;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.bty.commonmodule.view.EditTextWithDeleteIcon;
import com.zhongmei.bty.customer.CustomerActivity;
import com.zhongmei.bty.customer.event.DetailRefreshEvent;
import com.zhongmei.bty.basemodule.customer.dialog.country.CountryDialog;
import com.zhongmei.bty.basemodule.customer.dialog.country.CountryGridAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.List;
import java.util.regex.Pattern;

import de.greenrobot.event.EventBus;

@EFragment(R.layout.customer_levelup_dialog)
public class CustomerLevelUpDialogFragment extends BasicDialogFragment {

    @ViewById(R.id.male)
    protected RadioButton mMale;

    @ViewById(R.id.female)
    protected RadioButton mFemale;

    @ViewById(R.id.customer_name)
    protected EditTextWithDeleteIcon mName;

    @ViewById(R.id.customer_mobile)
    protected EditTextWithDeleteIcon mMobile;

    @ViewById(R.id.customer_country)
    protected EditText mCountry;

    @ViewById(R.id.customer_country_btn)
    protected ImageButton mCountryBtn;




    @ViewById(R.id.pass_edit)
    protected EditTextWithDeleteIcon mPass;

    @ViewById(R.id.pass_edit_confirm)
    protected EditTextWithDeleteIcon mPassConfirm;

    @ViewById(R.id.customer_birthdate)
    protected TextView mBirthday;

    @FragmentArg(CustomerActivity.PARAMKEY_CUSTOMER)
    protected CustomerResp mCustomer;

    private CustomerResp mUpdateCustomer;


    private ErpCurrency mErpCurrency;

    private List<ErpCurrency> mErpCurrencyList;

    private ErpCommercialRelationDal mErpDal;

    @SuppressLint("WrongConstant")
    public static void show(CustomerResp customer, FragmentManager fm) {
        CustomerLevelUpDialogFragment dialogFragment =
                CustomerLevelUpDialogFragment_.builder().mCustomer(customer).build();
        dialogFragment.setCancelable(false);
        dialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        dialogFragment.show(fm, CustomerLevelUpDialogFragment.class.getSimpleName());
    }

    @AfterViews
    protected void initData() {
        mUpdateCustomer = mCustomer;             }

    @AfterViews
    protected void initViews() {
        queryErpCurrenctList();
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        initCustomerView();
    }

    private void queryErpCurrenctList() {
        mErpDal = OperatesFactory.create(ErpCommercialRelationDal.class);
        mErpCurrencyList = mErpDal.queryErpCurrenctList();
    }

    private void initCustomerView() {
        if (TextUtils.isEmpty(mCustomer.nationalTelCode)) {
            String areaCode = ShopInfoCfg.getInstance().getCurrency().getAreaCode();
            if (!TextUtils.isEmpty(areaCode)) {
                mErpCurrency = mErpDal.queryErpCurrenctByAreaCode(areaCode);
            }
        } else {
            mErpCurrency = mErpDal.queryErpCurrenctByAreaCode(mCustomer.nationalTelCode);
        }
        if ("0".equals(String.valueOf(mCustomer.sex))) {
            mFemale.setSelected(true);
            mMale.setSelected(false);
        } else {
            mFemale.setSelected(false);
            mMale.setSelected(true);
        }
        mName.setText(mCustomer.customerName);
        mMobile.setText(mCustomer.mobile);

                String brithdayStr = "";
        if (!TextUtils.isEmpty(mCustomer.birthday)) {
            brithdayStr =
                    DateTimeUtils.formatDateTime(DateTimeUtils.formatDate(mCustomer.birthday), "yyyy-M-d");
        }
        if (!TextUtils.isEmpty(brithdayStr)) {
            mBirthday.setText(brithdayStr);
        } else {
            mBirthday.setText(CustomerActivity.BIRTHDAY_DEFAULT);
        }
        setupCountryView();
    }

    private void setupCountryView() {
        if (mErpCurrency != null) {
            mCountry.setText(mErpCurrency.getAreaCode());
        }
    }

    @Click({R.id.cancel, R.id.level_up, R.id.male, R.id.female,
            R.id.customer_birthdate, R.id.customer_country_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.customer_country_btn:
                showCountryDialog(mErpCurrencyList, mErpCurrency);
                break;
            case R.id.cancel:
                dismiss();
                break;
            case R.id.level_up:
                if (!inputCheckPass()) {
                    return;
                }
                levelUp();
                break;
            case R.id.male:
                mFemale.setSelected(false);
                mMale.setSelected(true);
                break;
            case R.id.female:
                mFemale.setSelected(true);
                mMale.setSelected(false);
                break;

            case R.id.customer_birthdate:
                DatePickerDialogFragment.show(getFragmentManager(),
                        TextUtils.isEmpty(mBirthday.getText().toString()) ? "1990-1-1" : mBirthday.getText().toString(),
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

    boolean inputCheckPass() {
        if (TextUtils.isEmpty(mName.getText().toString())) {
            ToastUtil.showShortToast(R.string.customer_no_name);
            return false;
        }
        if (TextUtils.isEmpty(mMobile.getText().toString())) {
            ToastUtil.showShortToast(R.string.customer_no_phone);
            return false;
        } else {
            if (mErpCurrency != null && !TextUtils.isEmpty(mErpCurrency.getPhoneRegulation()) && !Pattern.matches(mErpCurrency.getPhoneRegulation(), mCustomer.mobile)) {
                ToastUtil.showShortToast(getString(R.string.customer_mobile_regulation_error));
                return false;
            }
        }
        if (!mFemale.isSelected() && !mMale.isSelected()) {
            ToastUtil.showShortToast(R.string.customer_no_sex);
            return false;
        }
        if (TextUtils.isEmpty(mPass.getText().toString())) {
            ToastUtil.showShortToast(R.string.customer_no_password);
            return false;
        }
        if (TextUtils.isEmpty(mPassConfirm.getText().toString())) {
            ToastUtil.showShortToast(R.string.customer_no_confirm_password);
            return false;
        }
        if (!mPassConfirm.getText().toString().equals(mPass.getText().toString())) {
            ToastUtil.showShortToast(R.string.customer_password_not_match);
            return false;
        }
        if (mPass.getText().length() != 6) {
            ToastUtil.showShortToast(R.string.customer_pswd_length_must_be_6);
            return false;
        }
        return true;
    }


    private void levelUp() {
        mUpdateCustomer.mobile = mMobile.getText().toString();
        mUpdateCustomer.customerName = mName.getText().toString();
        mUpdateCustomer.sex = mFemale.isSelected() ? 0 : 1;
        mUpdateCustomer.birthday = String.valueOf(DateTimeUtils.formatDate(getDate()));
        MD5 md5 = new MD5();
        String customerSyncFlag = mUpdateCustomer.synFlag;
        String sex = mFemale.isSelected() ? "0" : "1";
        String birthday = DateTimeUtils.formatDate(Utils.toLong(mUpdateCustomer.birthday));
        String name = mUpdateCustomer.customerName;
        String userId = "" + Session.getAuthUser().getId();
        String commercialGroupId = ShopInfoCfg.getInstance().commercialGroupId;
        String beforePassword = mPassConfirm.getText().toString();
        String password = md5.getMD5ofStr(beforePassword);

        MemberUpgradeReq req = new MemberUpgradeReq(mMobile.getText().toString(), customerSyncFlag, sex, birthday, "1",
                name, userId, commercialGroupId, "", password);
        if (mErpCurrency != null) {
            req.setNation(mErpCurrency.getCountryEn());
            req.setCountry(mErpCurrency.getCountryZh());
            req.setNationalTelCode(mErpCurrency.getAreaCode());
            mUpdateCustomer.nation = mErpCurrency.getCountryEn();
            mUpdateCustomer.country = mErpCurrency.getCountryZh();
            mUpdateCustomer.nationalTelCode = mErpCurrency.getAreaCode();
        }
        CustomerOperates op = OperatesFactory.create(CustomerOperates.class);
        ResponseListener<MemberUpgradeResp> responseListener =
                LoadingResponseListener.ensure(new ResponseListener<MemberUpgradeResp>() {
                    @Override
                    public void onResponse(ResponseObject<MemberUpgradeResp> response) {
                        if (ResponseObject.isOk(response)) {
                            String Message = response.getMessage();
                            if (response.getContent() != null) {
                                String levelId = response.getContent().getLevelId();
                                mUpdateCustomer.levelId = TextUtils.isEmpty(levelId) ? null : Long.parseLong(levelId);
                                ToastUtil.showShortToast(R.string.customer_levelup_success);
                                EventBus.getDefault().post(new DetailRefreshEvent(mUpdateCustomer));
                            } else {
                                ToastUtil.showShortToast(response.getMessage());
                            }

                        } else {
                            ToastUtil.showShortToast(response.getMessage());
                        }
                        dismiss();
                    }

                    @Override
                    public void onError(VolleyError error) {
                        ToastUtil.showShortToast(error.getMessage());
                    }
                }, getFragmentManager());
        op.upgradeCustomer(req, mUpdateCustomer, responseListener);
    }

    private String getDate() {
        if (TextUtils.isEmpty(mBirthday.getText())) {
            return CustomerActivity.BIRTHDAY_DEFAULT;
        } else {
            return mBirthday.getText().toString();
        }
    }
}
