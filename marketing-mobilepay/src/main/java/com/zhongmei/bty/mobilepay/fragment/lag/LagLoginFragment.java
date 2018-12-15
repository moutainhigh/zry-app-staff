package com.zhongmei.bty.mobilepay.fragment.lag;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zhongmei.bty.basemodule.auth.application.BeautyApplication;
import com.zhongmei.bty.mobilepay.PayKeyPanel;
import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.bty.mobilepay.bean.IPaymentInfo;
import com.zhongmei.bty.mobilepay.core.DoPayApi;
import com.zhongmei.bty.mobilepay.event.DisplayUserInfoEvent;
import com.zhongmei.bty.basemodule.auth.application.DinnerApplication;
import com.zhongmei.bty.basemodule.customer.dialog.country.CountryDialog;
import com.zhongmei.bty.basemodule.customer.dialog.country.CountryGridAdapter;
import com.zhongmei.bty.basemodule.customer.enums.CustomerLoginType;
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.basemodule.erp.bean.ErpCurrency;
import com.zhongmei.bty.basemodule.erp.operates.ErpCommercialRelationDal;
import com.zhongmei.bty.basemodule.input.NumberKeyBoardUtils;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.atask.SimpleAsyncTask;
import com.zhongmei.atask.TaskContext;
import com.zhongmei.yunfu.bean.YFResponse;
import com.zhongmei.yunfu.bean.req.CustomerLoginResp;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.data.LoadingYFResponseListener;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.YFResponseListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import de.greenrobot.event.EventBus;

/**
 * Created by demo on 2018/12/15
 */
public class LagLoginFragment extends BasicFragment implements View.OnClickListener {

    private final String TAG = LagLoginFragment.class.getSimpleName();
    PayKeyPanel mNumberKeyBorad;

    EditText show_value;

    Button mLoginBT;

    TextView mTvCountry;

    boolean mLoginForPay = false;

    private boolean isShowAccount = true;

    private String mCurrentPhone;

    private Long mCustomerId;

    private LoginListener mListener;

    private DoPayApi mDoPayApi;//add v8.9
    private IPaymentInfo mPaymentInfo;

    /**
     * 当前商户国籍
     */
    private ErpCurrency mErpCurrency;

    private Map<String, ErpCurrency> erpCurrencyMap;

    private ErpCommercialRelationDal mErpDal;

    public static LagLoginFragment newInstance(IPaymentInfo info, DoPayApi doPayApi) {
        LagLoginFragment f = new LagLoginFragment();
        f.setPaymentInfo(info);
        f.setDoPayApi(doPayApi);
        f.setArguments(new Bundle());
        return f;
    }

    public void setPaymentInfo(IPaymentInfo iPaymentInfo) {
        this.mPaymentInfo = iPaymentInfo;
    }

    private void setDoPayApi(DoPayApi doPayApi) {
        this.mDoPayApi = doPayApi;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view == null) {
            view = inflater.inflate(R.layout.pay_lag_login_layout, container, false);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assignViews(view);
        initAreaCode();

    }

    private void assignViews(View view) {
        show_value = (EditText) view.findViewById(R.id.show_value);
        mLoginBT = (Button) view.findViewById(R.id.customer_verification);
        mNumberKeyBorad = new PayKeyPanel(view);
        mTvCountry = (TextView) view.findViewById(R.id.tvAreaCodes);
        mLoginBT.setOnClickListener(this);
        view.findViewById(R.id.rlAreaCode).setOnClickListener(this);
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
                String areaCode = ShopInfoCfg.getInstance().getCurrency().getAreaCode();
                if (!TextUtils.isEmpty(areaCode)) {
                    mErpCurrency = erpCurrencyMap.get(areaCode);
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

    protected void initView() {
        if (getActivity().getWindow()
                .getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
            // 隐藏软键盘
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
        mNumberKeyBorad.setCurrentInput(show_value);
        NumberKeyBoardUtils.setTouchListener(show_value);
        /*DisplayUserInfo dUserInfo =
                DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_ACCOUNT_INPUT, "", null, 0, true, 0);
        DisplayServiceManager.updateDisplay(getActivity(), dUserInfo);*/
        show_value.requestFocus();
        show_value.setFocusable(true);
        submitBtnClickable();
        show_value.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                submitBtnClickable();
                if (isShowAccount) {
                    /*DisplayUserInfo dUserInfo = DisplayServiceManager
                            .buildDUserInfo(DisplayUserInfo.COMMAND_ACCOUNT_INPUT, s.toString(), null, 0, false, 0);
                    DisplayServiceManager.updateDisplay(getActivity(), dUserInfo);*/
                } else {
                    isShowAccount = true;
                }
            }
        });

        show_value.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN)
                        && (keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER || keyCode == KeyEvent.KEYCODE_ENTER)) {
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
        if (!TextUtils.isEmpty(mCurrentPhone)) {
            show_value.setText(mCurrentPhone);
        }
        if (!TextUtils.isEmpty(mCurrentPhone)) {
            loginByPhoneNo(mCurrentPhone);
        }
    }

    void submitBtnClickable() {
        String input = show_value != null ? show_value.getText().toString().trim() : "";
        if (TextUtils.isEmpty(input)) {
            mLoginBT.setEnabled(false);
            mLoginBT.setTextColor(Color.parseColor("#BCBCBC"));
        } else {
            mLoginBT.setEnabled(true);
            mLoginBT.setTextColor(Color.parseColor("#03A9F4"));

        }
    }

    @Override
    public void onClick(View v) {
        if (ClickManager.getInstance().isClicked()) return;
        if (v.getId() == R.id.customer_verification) {
            VerifyHelper.verifyAlert(getActivity(), BeautyApplication.PERMISSION_CUSTOMER_LOGIN,
                    new VerifyHelper.Callback() {
                        @Override
                        public void onPositive(User user, String code, Auth.Filter filter) {
                            super.onPositive(user, code, filter);
                            verification();
                        }
                    });
        } else if (v.getId() == R.id.rlAreaCode) {
            showCountryDialog(new ArrayList<>(erpCurrencyMap.values()), mErpCurrency);
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
            ToastUtil.showShortToast(getString(R.string.pay_erpcurrency_enpty_hint));
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
        show_value.setText("");
        super.onHiddenChanged(hidden);
    }


    protected void verification() {
        final String inputNo = mNumberKeyBorad.getValue();
        if (TextUtils.isEmpty(inputNo)) {
            ToastUtil.showShortToast(R.string.customer_login_hint);
            return;
        }
        // 直接使用手机号登录
        loginByPhoneNo(inputNo);
    }


    //新登录接口
    protected void loginByPhoneNo(final String inputNo) {
        if (!TextUtils.isEmpty(inputNo)
                && mErpCurrency != null
                && !TextUtils.isEmpty(mErpCurrency.getPhoneRegulation())
                && !Pattern.matches(mErpCurrency.getPhoneRegulation(), inputNo)) {
            ToastUtil.showShortToast(getString(R.string.customer_mobile_regulation_error));
            return;
        }
        YFResponseListener<YFResponse<CustomerLoginResp>> listener = new YFResponseListener<YFResponse<CustomerLoginResp>>() {
            @Override
            public void onResponse(YFResponse<CustomerLoginResp> response) {
                try {
                    if (YFResponse.isOk(response)) {
                        CustomerLoginResp resp = response.getContent();

                        if (resp == null) {
                            return;
                        }

                        if (resp.customerIsDisable()) {//当前账号冻结
                            ToastUtil.showShortToast(R.string.order_dish_member_disabled);
                            return;
                        }

                        CustomerResp customer = resp.getCustomer();
                        //customer.setCoupons(infoResp.getCoupons());
                        if (!customer.isMember()) {
                            ToastUtil.showShortToast(R.string.customer_not_member);
                            return;
                        }
                        if (!mLoginForPay) {
                            customer.queryLevelRightInfos();
                            ToastUtil.showShortToast(R.string.customer_login);
                        }
                        isShowAccount = false;
                        show_value.setText("");

                        //DinnerPriviligeItemsFragment.showDisplayUserInfo(getActivity());
                        EventBus.getDefault().post(new DisplayUserInfoEvent());//modify v8.9
                        //List<CustomerInfoResp.Card> cards = customer.getOtherCardList();
                        if (mListener != null) {
                            mListener.onPhoneSuccess(customer);
                        }
                    } else {
                        ToastUtil.showShortToast(response.getMessage());
                        show_value.setBackgroundResource(R.drawable.customer_edit_error_bg);

                            /*DisplayUserInfo dUserInfo = DisplayServiceManager
                                    .buildDUserInfo(DisplayUserInfo.COMMAND_VALIDATE_USER_FAIL, "", null, 0, true, 0);

                            DisplayServiceManager.updateDisplay(getActivity(), dUserInfo);*/

                        show_value.requestFocus();
                        isShowAccount = false;
                        show_value.setText("");
                        show_value.startAnimation(shakeAnimation(6));
                    }
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showShortToast(error.getMessage());
            }
        };
        String telCode = mErpCurrency != null && mErpCurrency.getAreaCode() != null ? mErpCurrency.getAreaCode() : null;
        String country = mErpCurrency != null && mErpCurrency.getCountryZh() != null ? mErpCurrency.getCountryZh() : null;
        String nation = mErpCurrency != null && mErpCurrency.getCountryEn() != null ? mErpCurrency.getCountryEn() : null;
        CustomerManager.getInstance().customerLogin(CustomerLoginType.MOBILE, inputNo, null, true, false,
                LoadingYFResponseListener.ensure(listener, getFragmentManager()));
    }


    public static Animation shakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(0, 15, 0, 0);
        // 设置一个循环加速器，使用传入的次数就会出现摆动的效果。
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(500);

        return translateAnimation;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCurrentPhone = null;
        mCustomerId = null;
    }

    public void setPhone(String phone, Long customerId) {
        mCurrentPhone = phone;
        mCustomerId = customerId;
    }

    public void setListener(LoginListener listener) {
        mListener = listener;
    }
}
