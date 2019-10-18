package com.zhongmei.bty.customer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.auth.application.CustomerApplication;
import com.zhongmei.bty.basemodule.commonbusiness.enums.ReasonType;
import com.zhongmei.yunfu.bean.req.CustomerCouponResp;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.http.CalmNetWorkRequest;
import com.zhongmei.bty.basemodule.commonbusiness.utils.ServerAddressUtil;
import com.zhongmei.bty.basemodule.customer.bean.IntegralRecord;
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.basemodule.customer.message.MemberCouponsVoResp;
import com.zhongmei.bty.basemodule.customer.message.MemberIntegralModificationReq;
import com.zhongmei.bty.basemodule.customer.message.MemberIntegralModificationResp;
import com.zhongmei.yunfu.bean.req.CustomerStoredBalanceResp;
import com.zhongmei.yunfu.bean.req.CustomerStoredBalanceReq;
import com.zhongmei.bty.basemodule.customer.message.MemberValuecardHistoryResp;
import com.zhongmei.bty.basemodule.customer.message.NewMemberIntegralInfoResp;
import com.zhongmei.bty.basemodule.customer.operates.CustomerOperates;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardInfo;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardIntegralInfoReq;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardIntegralInfoResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardIntegralInfoResp.CardIntegralInfo;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CustomerCardStoreValueReq;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CustomerCardStoreValueResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CustomerCardStoreValueResp.CardStoreValueItem;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.bty.basemodule.session.core.user.UserFunc;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.bty.basemodule.trade.bean.Reason;
import com.zhongmei.atask.SimpleAsyncTask;
import com.zhongmei.atask.TaskContext;
import com.zhongmei.yunfu.net.builder.NetError;
import com.zhongmei.yunfu.net.builder.NetworkRequest;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.bty.cashier.ordercenter.view.OrderCenterOperateDialogFragment;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.resp.data.TransferReq;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.commonmodule.view.NumberInputdialog;
import com.zhongmei.bty.customer.adapter.ChargingRecordAdapter;
import com.zhongmei.bty.customer.adapter.CouponAdapter;
import com.zhongmei.bty.customer.adapter.IntegralAdapter;
import com.zhongmei.bty.customer.event.EventRefreshBalance;
import com.zhongmei.bty.customer.event.EventRefreshDetail;
import com.zhongmei.bty.customer.util.CustomerContants;
import com.zhongmei.bty.customer.util.CustomerPrintManager;
import com.zhongmei.bty.basemodule.customer.operates.CouponDal;
import com.zhongmei.bty.basemodule.customer.bean.coupon.CouponVo;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;


@EFragment(R.layout.customer_balance_layout)
public class CustomerBalanceFragment extends BasicFragment implements OnClickListener {

    private static final String TAG = CustomerBalanceFragment.class.getSimpleName();

    private static final Integer ADD_INTEGRAL = 1;

    private static final Integer SUBTRACT_INTEGRAL = 2;

    private int operateType = 0;

    @ViewById(R.id.customer_banlance_radio_group)
    RadioGroup balance_radioGroup;

    @ViewById(R.id.customer_banlance_rb)
    RadioButton banlance_rb;

    @ViewById(R.id.customer_integral_canuse_rb)
    RadioButton integral_rb;

    @ViewById(R.id.customer_coupon_use_rb)
    RadioButton coupon_user_rb;

        @ViewById(R.id.banlace_layout)
    LinearLayout mBalanceLayout;

        @ViewById(R.id.integral_layout)
    LinearLayout mIntegralLayout;

    @ViewById(R.id.customer_canuse_intergral_value)
    TextView canuseIntergral;

    @ViewById(R.id.count_integral_tv)
    TextView TvCountIntergral;

    @ViewById(R.id.customer_total_intergral_value)
    TextView totalIntergral;

        @ViewById(R.id.customer_coupon_gridview)
    GridView gridView;

        @ViewById(R.id.customer_balance_lv)
    protected ListView mBalanceLv;

        @ViewById(R.id.customer_integral_lv)
    protected ListView mIntegralLv;

        @ViewById(R.id.card_integral_lv)
    protected ListView mCardIntegralLv;

        @ViewById(R.id.balance_empty_layout)
    protected ImageView mBalanceEmptyLayout;

        @ViewById(R.id.integral_empty_layout)
    protected ImageView mIntegralEmptyLayout;

        @ViewById(R.id.coupon_empty_layout)
    protected RelativeLayout mCouponEmptyLayout;

    @ViewById(R.id.integral_layout_btn)
    protected LinearLayout mLayoutBtn;

    @ViewById(R.id.btn_add_integral)
    protected Button mBtnAddIntegral;

    @ViewById(R.id.btn_subtract_integral)
    protected Button mBtnSubIntegral;

    @ViewById(R.id.mPtr)
    protected PtrClassicFrameLayout mPtr;

    private int currentPage = 1;

        private List<CustomerStoredBalanceResp> mChargingRecords = new ArrayList<CustomerStoredBalanceResp>();

        private List<CouponVo> mCouponVoList = new ArrayList<CouponVo>();

        private List<IntegralRecord> mIntegralList = new ArrayList<IntegralRecord>();

        private HashMap<String, String> mAllUser;

    private CustomerResp mCustomer;

    private CouponAdapter mCouponAdater;

    private ChargingRecordAdapter mChargingAdapter;

    private IntegralAdapter mIntegralAdapter;


    private EcCardInfo ecCard;

        private int curCheck = CustomerContants.TYPE_BALANCE;

        private String integarl = "0";

    private Reason mReason;

    private Long lastId = null;

    @AfterViews
    void initView() {
        EventBus.getDefault().register(this);
        getAllUserTask();
    }


    public void bindData(CustomerResp customer, int checkType, String integarl, EcCardInfo ecCard) {
        this.mCustomer = customer;
        this.curCheck = checkType;
        this.integarl = integarl;
        this.ecCard = ecCard;
        refreshInfo();
    }

    @UiThread
    void refreshInfo() {
        if (ecCard != null) {
            coupon_user_rb.setVisibility(View.GONE);
            mLayoutBtn.setVisibility(View.GONE);
        } else {
            mLayoutBtn.setVisibility(View.VISIBLE);
        }
    }


    @UiThread(delay = 100)
    void defalutCheck() {
        switch (curCheck) {
            case CustomerContants.TYPE_BALANCE:
                banlance_rb.setChecked(true);
                break;
            case CustomerContants.TYPE_INTEGRAL:
                integral_rb.setChecked(true);
                break;
            case CustomerContants.TYPE_COUPON:
                coupon_user_rb.setChecked(true);
                break;
            default:
                break;
        }
    }

    @Click({R.id.customer_charging_balance_back_btn, R.id.btn_add_integral, R.id.btn_subtract_integral})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.customer_charging_balance_back_btn:
                getActivity().finish();
                break;
            case R.id.btn_add_integral:
                showDialog(getString(R.string.member_add_Integral), getString(R.string.member_input_Integral), ADD_INTEGRAL);
                break;
            case R.id.btn_subtract_integral:
                showDialog(getString(R.string.member_subtract_Integral), getString(R.string.member_input_Integral), SUBTRACT_INTEGRAL);
                break;
            default:
                break;
        }

    }


    private int integralNumber = 0;

    private void showDialog(String title, String hint, int operateType) {
        double max = 99999999d;
        this.operateType = operateType;
        NumberInputdialog inputdialog = new NumberInputdialog(getActivity(), title, hint, "", max, listener);
        inputdialog.setNumberType(NumberInputdialog.NUMBER_TYPE_INT).show();
    }

    NumberInputdialog.InputOverListener listener = new NumberInputdialog.InputOverListener() {
        @Override
        public void afterInputOver(String inputContent) {
            integralNumber = Integer.valueOf(inputContent);
            VerifyHelper.verifyAlert(getActivity(), CustomerApplication.PERMISSION_MEMBER_INTEGRAL_MODIFY,
                    new VerifyHelper.Callback() {
                        @Override
                        public void onPositive(User user, String code, Auth.Filter filter) {
                            super.onPositive(user, code, filter);
                            showReason();
                        }
                    });
        }
    };

    private void showReason() {
        OrderCenterOperateDialogFragment dialog = new OrderCenterOperateDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", ReasonType.INTEGRAL_MODIFY.value());
        dialog.setArguments(bundle);
        dialog.registerListener(new OrderCenterOperateDialogFragment.OperateListener() {
            @Override
            public boolean onSuccess(OrderCenterOperateDialogFragment.OperateResult result) {
                String reason = "";
                mReason = result.reason;
                if (mReason != null) {
                    reason = mReason.getContent();
                } else {
                    reason = "";
                }
                if (operateType == ADD_INTEGRAL) {
                    memberIntegralModification(integralNumber, operateType, reason);
                } else if (operateType == SUBTRACT_INTEGRAL) {
                    memberIntegralModification(integralNumber, operateType, reason);
                }
                return true;
            }
        });
        dialog.registerCloseListener(new OrderCenterOperateDialogFragment.OperateCloseListener() {

            @Override
            public void onClose(OrderCenterOperateDialogFragment.OperateResult result) {

            }
        });
        dialog.show(getFragmentManager(), TAG);
    }

    @Override
    public void onDestroyView() {
        if (mCustomer != null && EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().post(new EventRefreshDetail(mCustomer.customerId));
        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    private void memberIntegralModification(int integral, int operateType, String reason) {



    }

    private void updateUI(List<IntegralRecord> mIntegralList) {
        mIntegralEmptyLayout.setVisibility(View.GONE);
        mPtr.setVisibility(View.VISIBLE);
        mIntegralAdapter.notifyDataSetChanged();
        countIntergal(mIntegralList);
    }


    private class MyCheckListener implements OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            changeLayout(checkedId);
        }
    }


    private void changeLayout(int checkedId) {
        switch (checkedId) {
            case R.id.customer_banlance_rb:                mIntegralLayout.setVisibility(View.GONE);
                gridView.setVisibility(View.GONE);
                mBalanceLayout.setVisibility(View.VISIBLE);
                mBalanceLv.setAdapter(mChargingAdapter);
                curCheck = CustomerContants.TYPE_BALANCE;
                loadChargeRecord();
                break;
            case R.id.customer_integral_canuse_rb:                if (ecCard == null) {
                    mLayoutBtn.setVisibility(View.VISIBLE);
                    mCardIntegralLv.setVisibility(View.GONE);
                    mPtr.setVisibility(View.VISIBLE);
                    mIntegralLv.setAdapter(mIntegralAdapter);
                    TvCountIntergral.setVisibility(View.VISIBLE);
                    totalIntergral.setVisibility(View.VISIBLE);
                } else {
                    mLayoutBtn.setVisibility(View.GONE);
                    TvCountIntergral.setVisibility(View.GONE);
                    totalIntergral.setVisibility(View.GONE);
                    mCardIntegralLv.setVisibility(View.VISIBLE);
                    mPtr.setVisibility(View.GONE);
                    mCardIntegralLv.setAdapter(mIntegralAdapter);
                }
                mBalanceLayout.setVisibility(View.GONE);
                gridView.setVisibility(View.GONE);
                mIntegralLayout.setVisibility(View.VISIBLE);
                curCheck = CustomerContants.TYPE_INTEGRAL;
                loadIntegral();
                break;
            case R.id.customer_coupon_use_rb:                mIntegralLayout.setVisibility(View.GONE);
                mBalanceLayout.setVisibility(View.GONE);
                gridView.setVisibility(View.VISIBLE);
                curCheck = CustomerContants.TYPE_COUPON;
                getCouponInfos();
                break;
            default:
                break;
        }
    }


    public void onEventMainThread(EventRefreshBalance event) {
        if (mChargingAdapter != null && curCheck == CustomerContants.TYPE_BALANCE) {
            loadChargeRecord();
        }
    }


    private void getAllUserTask() {
        TaskContext.bindExecute(this, new SimpleAsyncTask<Void>() {

            @Override
            public Void doInBackground(Void... params) {
                mAllUser = getAllUser();
                return null;
            }

            @Override
            public void onPostExecute(Void result) {
                super.onPostExecute(result);
                balance_radioGroup.setOnCheckedChangeListener(new MyCheckListener());
                mChargingAdapter = new ChargingRecordAdapter(getActivity(), mChargingRecords, mAllUser);
                mChargingAdapter.setOnRecordItemClickListener(new ChargingRecordAdapter.OnRecordItemClickListener() {
                    @Override
                    public void onRePrint(CustomerStoredBalanceResp history, String authUser) {
                        if (mCustomer != null && history != null) {
                            new CustomerPrintManager().rePrinCardOrtMemberConsume(mCustomer.customerName, mCustomer.sex + "", mCustomer.mobile,
                                    history.getCardNum(), authUser, history.getCreateDateTime(), history.getBeforeRealValue(), history.getBeforeSendValue(), history.getCurrentRealValue(), history.getCurrentSendValue(),
                                    history.getEndRealValue(), history.getEndSendValue());
                        }
                    }
                });
                mCouponAdater = new CouponAdapter(getActivity(), mCouponVoList);
                mIntegralAdapter = new IntegralAdapter(getActivity(), mIntegralList, mAllUser);
                defalutCheck();
            }
        });
    }


    private HashMap<String, String> getAllUser() {
        HashMap<String, String> authUserMap = new HashMap<String, String>();

        try {
            List<User> authUserList = Session.getFunc(UserFunc.class).getUsers();
            if (Utils.isNotEmpty(authUserList)) {
                for (User authUser : authUserList) {
                    authUserMap.put(authUser.getAccount(), authUser.getName());
                    authUserMap.put(authUser.getId().toString(), authUser.getName());
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "" + e);
        }


        return authUserMap;
    }


    private void loadChargeRecord() {
        CustomerOperates op = OperatesFactory.create(CustomerOperates.class);
        if (ecCard != null) {
            getCardStoreValueList(ecCard.getCardNum());
        } else if (mCustomer != null) {
            String phoneNum = "";
            if (TextUtils.isEmpty(mCustomer.mobile)) {
                                setBalanceIntegralGone();
                return;
            }

            phoneNum = mCustomer.mobile;            CustomerStoredBalanceReq req = new CustomerStoredBalanceReq(1, mCustomer.id);

            op.getValuecardHistory(req,
                    LoadingResponseListener.ensure(new ResponseListener<MemberValuecardHistoryResp>() {

                        @Override
                        public void onResponse(ResponseObject<MemberValuecardHistoryResp> response) {
                            if (ResponseObject.isOk(response) || ResponseObject.MEMBER_REJECT == response.getStatusCode()) {
                                if (response.getContent() != null) {
                                    mChargingRecords.clear();
                                    MemberValuecardHistoryResp resp = response.getContent();
                                    List<CustomerStoredBalanceResp> historys = new ArrayList<>();
                                    for (CustomerStoredBalanceResp historyBean : resp.getValuecardHistorys()) {
                                        if (historyBean.filterDataByType()) {
                                            historys.add(historyBean);
                                        }
                                    }
                                    if (historys.size() == 0) {
                                        setBalanceIntegralGone();
                                    } else {
                                        setBalanceIntegralVisible();
                                        refreshChargeRecordeView(historys);
                                    }
                                    if (ResponseObject.MEMBER_REJECT == response.getStatusCode()) {
                                        setBalanceIntegralGone();
                                        ToastUtil.showShortToast(response.getMessage());
                                    }
                                }

                            } else {
                                                            }
                        }

                        @Override
                        public void onError(VolleyError error) {
                            ToastUtil.showShortToast(error.getMessage());
                        }
                    }, getChildFragmentManager()));
        }
    }


    private void getCardStoreValueList(String cardNumber) {
                CustomerCardStoreValueReq cardStoreValueReq = new CustomerCardStoreValueReq();
        cardStoreValueReq.setTradeStatus(4);
        cardStoreValueReq.setSource(1);
        cardStoreValueReq.setPageSize(Long.valueOf(50));
        cardStoreValueReq.setUserId(Session.getAuthUser().getId());
        if (cardNumber != null) {
            cardStoreValueReq.setQueryParam(cardNumber);
        }
        CustomerOperates customerOperates = OperatesFactory.create(CustomerOperates.class);
        ResponseListener<CustomerCardStoreValueResp> listener = new ResponseListener<CustomerCardStoreValueResp>() {

            @Override
            public void onResponse(ResponseObject<CustomerCardStoreValueResp> response) {
                if (ResponseObject.isOk(response)) {
                    List<CardStoreValueItem> items = response.getContent().getResult();
                    List<CustomerStoredBalanceResp> historys = new ArrayList<CustomerStoredBalanceResp>();
                    if (items != null && items.size() > 0) {
                        Collections.sort(items, new Comparator<CardStoreValueItem>() {
                            @Override
                            public int compare(CardStoreValueItem lhs, CardStoreValueItem rhs) {
                                return rhs.getStoreDate().compareTo(lhs.getStoreDate());
                            }
                        });
                        for (CardStoreValueItem item : items) {
                            historys.add(item.getMemberValuecardHistory());
                        }
                    }
                    refreshChargeRecordeView(historys);
                } else {
                    ToastUtil.showLongToast(response.getMessage());
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showLongToast(error.getMessage());
            }
        };
        customerOperates.getCardStoreValueList(cardStoreValueReq,
                LoadingResponseListener.ensure(listener, getFragmentManager()));
    }


    private void refreshChargeRecordeView(List<CustomerStoredBalanceResp> historys) {
        Collections.sort(historys, new Comparator<CustomerStoredBalanceResp>() {

            @Override
            public int compare(CustomerStoredBalanceResp arg0, CustomerStoredBalanceResp arg1) {
                return arg1.getCreateDateTime().compareTo(arg0.getCreateDateTime());
            }
        });
        mChargingRecords.clear();
        mChargingRecords.addAll(historys);
        mBalanceLv.setAdapter(mChargingAdapter);
        mChargingAdapter.notifyDataSetChanged();
        if (mChargingRecords.size() == 0) {
            setBalanceIntegralGone();
        } else {
            setBalanceIntegralVisible();
        }
    }


    private void loadIntegral() {
        CustomerOperates op = OperatesFactory.create(CustomerOperates.class);
        if (ecCard != null) {
            CardIntegralInfoReq req = new CardIntegralInfoReq();
            req.setEntityCardNo(ecCard.getCardNum());
            req.setPageSize(50);
            req.setUserId(Session.getAuthUser().getId());
            req.setSource(1);
            op.getCardIntegralHistory(req, LoadingResponseListener.ensure(new ResponseListener<CardIntegralInfoResp>() {

                @SuppressLint("SimpleDateFormat")
                @Override
                public void onResponse(ResponseObject<CardIntegralInfoResp> response) {
                    if (ResponseObject.isOk(response)) {
                        CardIntegralInfoResp resp = response.getContent();
                        List<CardIntegralInfo> cardIntegrals = resp.getResult();
                        if (cardIntegrals != null && cardIntegrals.size() > 0) {
                            setBalanceIntegralVisible();
                            Collections.sort(cardIntegrals, new Comparator<CardIntegralInfo>() {
                                @Override
                                public int compare(CardIntegralInfo lhs, CardIntegralInfo rhs) {
                                    if (rhs.getServerCreateTime().compareTo(lhs.getServerCreateTime()) > 0) {
                                        return 1;
                                    } else if (rhs.getServerCreateTime().compareTo(lhs.getServerCreateTime()) == 0) {                                        if (rhs.getAddIntegral().compareTo(BigDecimal.ZERO) < 0) {
                                            return -1;
                                        } else {
                                            return 1;
                                        }
                                    } else {
                                        return -1;
                                    }
                                }
                            });
                            List<IntegralRecord> memberIntegral = new ArrayList<IntegralRecord>();
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                            for (CardIntegralInfo info : cardIntegrals) {
                                IntegralRecord record = new IntegralRecord();
                                record.setAddIntegral(info.getAddIntegral().toPlainString());
                                record.setId(info.getId());
                                record.setEndIntegral(info.getEndIntegral().toPlainString());
                                record.setUserId(info.getCreatorId());
                                try {
                                    record.setModifyDateTime(format.parse(info.getServerCreateTime()).getTime());
                                } catch (ParseException e) {
                                    Log.e(TAG, "", e);
                                }
                                memberIntegral.add(record);
                            }
                                                        refreshIntegralView(memberIntegral);
                        } else {
                            setBalanceIntegralGone();
                        }
                    } else {
                        ToastUtil.showShortToast(response.getMessage());
                    }
                }

                @Override
                public void onError(VolleyError error) {
                    ToastUtil.showShortToast(error.getMessage());
                }

            }, getFragmentManager()));

        } else if (mCustomer != null) {
            op.newMemberIntegralInfo(mCustomer.customerId, 1, null, LoadingResponseListener.ensure(new ResponseListener<NewMemberIntegralInfoResp>() {
                @Override
                public void onResponse(ResponseObject<NewMemberIntegralInfoResp> response) {
                    if (response.getContent() != null && response.getContent().getResult() != null) {
                        List<IntegralRecord> memberIntegral =
                                response.getContent().getMemberIntegral(response.getContent().getResult().getItems());
                        if (memberIntegral.size() == 0) {
                            ToastUtil.showShortToast(getString(R.string.toast_customer_not_intrgral));
                        } else {
                            lastId = memberIntegral.get(memberIntegral.size() - 1).getId();
                        }
                        refreshIntegralView(memberIntegral);


                    } else {
                        ToastUtil.showShortToast(response.getContent().getErrorMessage());
                        setBalanceIntegralGone();
                    }
                    if (mIntegralAdapter != null && mIntegralAdapter.getCount() > 0) {
                        setBalanceIntegralVisible();
                    }
                }

                @Override
                public void onError(VolleyError error) {
                    ToastUtil.showShortToast(error.getMessage());
                    setBalanceIntegralGone();
                    if (mIntegralAdapter != null && mIntegralAdapter.getCount() > 0) {
                        setBalanceIntegralVisible();
                    }

                }
            }, getFragmentManager()));

            mPtr.setPtrHandler(new PtrDefaultHandler2() {
                @Override
                public void onLoadMoreBegin(PtrFrameLayout frame) {
                    currentPage++;
                    getMemberIntegralDetail(currentPage, lastId);
                }

                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                    currentPage = 1;
                    lastId = null;
                    getMemberIntegralDetail(currentPage, lastId);
                }
            });
        }
    }


    private void refreshIntegralView(List<IntegralRecord> memberIntegral) {
        mIntegralList.clear();
        mIntegralList.addAll(memberIntegral);
                Collections.sort(mIntegralList, new IntegralComparator());
        mIntegralAdapter.notifyDataSetChanged();
                countIntergal(mIntegralList);
        if (mIntegralList.size() == 0) {
            setBalanceIntegralGone();
        } else {
            setBalanceIntegralVisible();
        }
    }


    private void getCouponInfos() {
        if (mCustomer != null) {
            ResponseListener listener = LoadingResponseListener.ensure(new ResponseListener<MemberCouponsVoResp>() {

                @Override
                public void onResponse(ResponseObject<MemberCouponsVoResp> response) {
                    if (ResponseObject.isOk(response) && MemberCouponsVoResp.isOk(response.getContent())) {
                        MemberCouponsVoResp resp = response.getContent();
                        filterData(resp.getResult().getItems());
                    } else {
                        ToastUtil.showShortToast(response.getMessage());
                    }
                }

                @Override
                public void onError(VolleyError error) {
                    ToastUtil.showShortToast(error.getMessage());
                }
            }, getFragmentManager());

            CustomerManager.getInstance().getCustomerCoupons(mCustomer.customerId, 1, Integer.MAX_VALUE, listener);
        }
    }


    private void setBalanceIntegralVisible() {
        mBalanceEmptyLayout.setVisibility(View.GONE);
        mIntegralEmptyLayout.setVisibility(View.GONE);
        mBalanceLv.setVisibility(View.VISIBLE);

        if (ecCard != null) {
            mCardIntegralLv.setVisibility(View.VISIBLE);
            mPtr.setVisibility(View.GONE);
        } else {
            mCardIntegralLv.setVisibility(View.GONE);
            mPtr.setVisibility(View.VISIBLE);
        }
    }


    private void setBalanceIntegralGone() {
        mBalanceEmptyLayout.setVisibility(View.VISIBLE);
        mIntegralEmptyLayout.setVisibility(View.VISIBLE);
        mCardIntegralLv.setVisibility(View.GONE);
        mBalanceLv.setVisibility(View.GONE);
        if (ecCard != null) {
            mPtr.setVisibility(View.GONE);
        } else {
            mPtr.setVisibility(View.VISIBLE);
        }
    }


    private void setCouponVisible() {

        mCouponEmptyLayout.setVisibility(View.GONE);
        gridView.setVisibility(View.VISIBLE);
    }


    private void setCouponGone() {
        mCouponEmptyLayout.setVisibility(View.VISIBLE);
        gridView.setVisibility(View.GONE);
    }


    @UiThread
    void countIntergal(List<IntegralRecord> records) {
        long allCount = 0;
        long usable = 0;
        if (records != null && records.size() > 0) {
            if (ecCard != null) {
                for (IntegralRecord re : records) {
                    if (Long.parseLong(re.getAddIntegral()) > 0) {
                        allCount += Long.parseLong(re.getAddIntegral());
                    }
                }
                usable = Long.parseLong(records.get(0).getEndIntegral());
            } else {
                allCount = Long.parseLong(records.get(0).getAggregateCount());
                usable = Long.parseLong(records.get(0).getEndIntegral());
            }
        }
        canuseIntergral.setText(String.format(getActivity().getString(R.string.customer_cent_str), String.valueOf(usable)));
        totalIntergral.setText((String.format(getActivity().getString(R.string.customer_cent_str), String.valueOf(allCount))));

    }


    public void filterData(final List<CustomerCouponResp> list) {
        if (list != null && list.size() > 0) {
            final CouponDal mCouponDal = OperatesFactory.create(CouponDal.class);
            TaskContext.bindExecute(this, new SimpleAsyncTask<List<CouponVo>>() {
                @Override
                public List<CouponVo> doInBackground(Void... params) {

                    List<CouponVo> couponVoList = null;
                    try {
                        couponVoList = mCouponDal.findCouponVoListByCouponInfos(list);
                        if (couponVoList != null && !couponVoList.isEmpty()) {
                            Collections.sort(couponVoList, new CouponeComparator());
                        }

                    } catch (Exception e) {
                        Log.e(TAG, "", e);
                    }
                    return couponVoList;
                }

                public void onPostExecute(List<CouponVo> data) {
                    mCouponVoList.clear();
                    mCouponVoList.addAll(data);
                    gridView.setAdapter(mCouponAdater);
                    mCouponAdater.notifyDataSetChanged();
                    if (mCouponVoList.size() == 0) {
                        setCouponGone();
                    } else {
                        setCouponVisible();
                    }
                }

                ;
            });
        } else {
            setCouponGone();
        }
    }


    private class CouponeComparator implements Comparator<CouponVo> {

        @Override
        public int compare(CouponVo o1, CouponVo o2) {
            int t1 = o1.getCouponInfo().getCouponType().value();
            int t2 = o2.getCouponInfo().getCouponType().value();
            if (t1 > t2) {
                return -1;
            } else {
                return 1;
            }
        }
    }


    private class IntegralComparator implements Comparator<IntegralRecord> {

        @Override
        public int compare(IntegralRecord o1, IntegralRecord o2) {
            Long t1 = o1.getId();
            Long t2 = o2.getId();
            return t2.compareTo(t1);
        }
    }

    private void getMemberIntegralDetail(final int mCurrentPage, Long mLastId) {
        CustomerOperates op = OperatesFactory.create(CustomerOperates.class);
        op.newMemberIntegralInfo(mCustomer.customerId, mCurrentPage, mLastId, new ResponseListener<NewMemberIntegralInfoResp>() {
            @Override
            public void onResponse(ResponseObject<NewMemberIntegralInfoResp> response) {
                if (response.getContent() != null && response.getContent().getResult() != null) {
                    List<IntegralRecord> memberIntegral =
                            response.getContent().getMemberIntegral(response.getContent().getResult().getItems());
                    if (mCurrentPage > 1) {
                        if (memberIntegral.size() == 0) {
                            ToastUtil.showShortToast(getString(R.string.toast_not_more));
                        } else {
                            mIntegralList.addAll(memberIntegral);
                            memberIntegral.clear();
                            memberIntegral.addAll(mIntegralList);
                            lastId = memberIntegral.get(memberIntegral.size() - 1).getId();
                            refreshIntegralView(memberIntegral);
                        }
                    } else {
                        if (memberIntegral.size() == 0) {
                            ToastUtil.showShortToast(getString(R.string.toast_customer_not_intrgral));
                        } else {
                            lastId = memberIntegral.get(memberIntegral.size() - 1).getId();
                        }
                        refreshIntegralView(memberIntegral);

                    }
                } else {
                    ToastUtil.showShortToast(response.getContent().getErrorMessage());
                    currentPage--;
                    setBalanceIntegralGone();
                }
                if (mIntegralAdapter.getCount() > 0) {
                    setBalanceIntegralVisible();
                }
                mPtr.refreshComplete();
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showShortToast(error.getMessage());
                currentPage--;
                setBalanceIntegralGone();
                if (mIntegralAdapter.getCount() > 0) {
                    setBalanceIntegralVisible();
                }
                mPtr.refreshComplete();
            }
        });
    }
}
