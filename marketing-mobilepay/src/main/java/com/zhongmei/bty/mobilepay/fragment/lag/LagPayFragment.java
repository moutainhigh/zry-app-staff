package com.zhongmei.bty.mobilepay.fragment.lag;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zhongmei.bty.basemodule.auth.application.DinnerApplication;
import com.zhongmei.bty.basemodule.auth.permission.manager.AuthLogManager;
import com.zhongmei.bty.basemodule.commonbusiness.cache.ServerSettingCache;
import com.zhongmei.bty.basemodule.commonbusiness.enums.ReasonType;
import com.zhongmei.bty.basemodule.commonbusiness.utils.BusinessTypeUtils;
import com.zhongmei.bty.basemodule.customer.dialog.PasswordDialog;
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.basemodule.customer.operates.CustomerOperates;
import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.bty.basemodule.devices.liandipos.NewLDResponse;
import com.zhongmei.bty.basemodule.devices.liandipos.PosConnectManager;
import com.zhongmei.bty.basemodule.devices.mispos.dialog.ReadKeyboardDialogFragment;
import com.zhongmei.bty.basemodule.devices.mispos.event.EventReadKeyboard;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.trade.bean.Reason;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.manager.DinnerCashManager;
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager;
import com.zhongmei.bty.basemodule.trade.message.LagReq;
import com.zhongmei.bty.basemodule.trade.message.LagResp;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.bty.basemodule.trade.operates.TradeOperates;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.data.operate.OperatesRetailFactory;
import com.zhongmei.bty.commonmodule.database.enums.OrderActionEnum;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.bty.commonmodule.util.MD5;
import com.zhongmei.bty.mobilepay.IDoLag;
import com.zhongmei.bty.mobilepay.ISavedCallback;
import com.zhongmei.bty.mobilepay.bean.IPaymentInfo;
import com.zhongmei.bty.mobilepay.core.DoPayApi;
import com.zhongmei.bty.mobilepay.dialog.ReasonOptionDialog;
import com.zhongmei.bty.mobilepay.event.ActionClose;
import com.zhongmei.bty.mobilepay.manager.CashInfoManager;
import com.zhongmei.yunfu.bean.req.CustomerLoginReq;
import com.zhongmei.yunfu.bean.req.CustomerLoginResp;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.yunfu.util.ToastUtil;

import java.math.BigDecimal;
import java.util.Locale;

import de.greenrobot.event.EventBus;



public class LagPayFragment extends BasicFragment implements View.OnClickListener {
    private final String TAG = LagPayFragment.class.getSimpleName();

    private TextView tv_name;

    private TextView tv_nickname;

    private TextView tv_remaing_amount;

    private TextView tv_usage_amount;

    private Button mLagBT;

    private Button mExitBT;

    private Button mReasonSelectBT;

    private Reason mReason;

    private CustomerResp mCustomer;

        private Double restAmount = 0.0;

        private Double usageAmount = 0.0;

    private IPaymentInfo mPaymentInfo;

    private DoPayApi mDoPayApi;

    private IDoLag mIDoLag;

    public static LagPayFragment newInstance(IPaymentInfo info, DoPayApi doPayApi) {
        LagPayFragment f = new LagPayFragment();
        f.setPaymentInfo(info);
        f.setDoPayApi(doPayApi);
        f.setArguments(new Bundle());
        return f;
    }

    public void setPaymentInfo(IPaymentInfo iPaymentInfo) {
        this.mPaymentInfo = iPaymentInfo;
    }

    public void setDoPayApi(DoPayApi doPayApi) {
        this.mDoPayApi = doPayApi;
    }

    public void setIDoPay(IDoLag iDoPay) {
        this.mIDoLag = iDoPay;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view == null) {
            view = inflater.inflate(R.layout.lag_pay_fragment_new, container, false);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assignViews(view);
        initView();
    }

    private void assignViews(View view) {
        tv_name = (TextView) view.findViewById(R.id.lag_info_customer_name);
        tv_nickname = (TextView) view.findViewById(R.id.lag_info_nickname);
        tv_remaing_amount = (TextView) view.findViewById(R.id.lag_remaining_amount);
        tv_usage_amount = (TextView) view.findViewById(R.id.lag_usage_amount);
        mReasonSelectBT = (Button) view.findViewById(R.id.btn_reason_select);
        mLagBT = (Button) view.findViewById(R.id.btn_lag);
        mExitBT = (Button) view.findViewById(R.id.btn_quiet);

        mReasonSelectBT.setOnClickListener(this);
        mExitBT.setOnClickListener(this);
        mLagBT.setOnClickListener(this);
    }

    private void initView() {
        if (mCustomer != null) {
            tv_name.setText(mCustomer.customerName);
            if (mCustomer.getCustomerLevel() != null) {
                tv_nickname.setText(mCustomer.getCustomerLevel().getGroupName() + "(" + mCustomer.getCustomerLevel().getId()
                        + getString(R.string.level) + ")");
            }
        }
        if (restAmount == null) {
            restAmount = 0.0;
        }
        tv_remaing_amount.setText(String.format(getString(R.string.lag_remaining_amount), CashInfoManager.formatCash(restAmount)));
        if (usageAmount == null) {
            usageAmount = 0.0;
        }
        tv_usage_amount.setText(String.format(getString(R.string.lag_usage_amount), CashInfoManager.formatCash(usageAmount)));
        if (mPaymentInfo != null)
            DisplayServiceManager.updateDisplayPay(getActivity().getApplicationContext(), mPaymentInfo.getActualAmount());
    }

    public void setCustomerInfo(CustomerResp customer) {
        mCustomer = customer;
        this.restAmount = mCustomer.remainCreditValue;
        this.usageAmount = mCustomer.usedCreditValue;
    }


    public void onClick(View view) {
        if (view.getId() == R.id.btn_quiet) {
            doQuiet();

        } else if (view.getId() == R.id.btn_reason_select) {
            showReason();

        } else if (view.getId() == R.id.btn_lag) {
            UserActionEvent.start(UserActionEvent.DINNER_PAY_SETTLE_LAG_PAY);
            VerifyHelper.verifyAlert(getActivity(), DinnerApplication.PERMISSION_DINNER_CREDIT,
                    new VerifyHelper.Callback() {
                        @Override
                        public void onPositive(User user, String code, Auth.Filter filter) {
                            super.onPositive(user, code, filter);
                            prepareLag();
                        }
                    });
        }
    }

    private void doQuiet() {
        EventBus.getDefault().post(new ActionClose());
    }

    private void showReason() {
        ReasonOptionDialog dialog = new ReasonOptionDialog(getActivity(), getString(R.string.reason_lag_title), getString(R.string.reason_lag_title), ReasonType.LAG_REASON);
        dialog.setOperateListener(new ReasonOptionDialog.OperateListener() {

            @Override
            public boolean onSuccess(Reason reason) {
                mReason = reason;
                if (mReason != null)
                    mReasonSelectBT.setText(mReason.getContent());
                return true;
            }
        });
        dialog.show();
    }

        private void doLag() {
                if (mPaymentInfo.getTradeVo().getTrade().getTradeType() == TradeType.UNOIN_TABLE_MAIN) {
                        ISavedCallback savedCallback = new ISavedCallback() {
                @Override
                public void onSaved(boolean isOk) {
                                        if (isOk) {
                        lag();
                    }
                }
            };
            if (mDoPayApi != null)
                mDoPayApi.saveTrade(this.getActivity(), mPaymentInfo, null, savedCallback);
        } else {
            lag();
        }
    }


    private void lag() {
        TradeOperates tradeOperates = BusinessTypeUtils.isRetail() ? OperatesRetailFactory.create(TradeOperates.class) : OperatesFactory.create(TradeOperates.class);
        LagReq lagReq = new LagReq();
        TradeVo tradeVo = mPaymentInfo.getTradeVo();
        lagReq.setTradeId(tradeVo.getTrade().getId());
        lagReq.setAmount(new BigDecimal(mPaymentInfo.getActualAmount().toString()));
        AuthUser user = Session.getAuthUser();
        lagReq.setCreatorName(user.getName());
        lagReq.setCreatorId(user.getId());
        lagReq.setUuid(SystemUtils.genOnlyIdentifier());
        lagReq.setCustomerId(mCustomer.customerId);
        lagReq.setReasonId(mReason.getId());
        lagReq.setReasonContent(mReason.getContent());
        lagReq.setCustomerName(mCustomer.customerName);
        lagReq.setCustomerPhone(mCustomer.mobile);
        if (mPaymentInfo.isOrdered()) {
            Trade trade = tradeVo.getTrade();
            tradeVo = new TradeVo();
            tradeVo.setTrade(trade);
        }
        tradeVo.getTrade().validateUpdate();
        final Long tradeId = tradeVo.getTrade().getId();
        final String tradeUuid = tradeVo.getTrade().getUuid();
        tradeOperates.doLag(tradeVo, lagReq, LoadingResponseListener.ensure(new ResponseListener<LagResp>() {
            @Override
            public void onResponse(ResponseObject<LagResp> response) {
                try {
                    if (getActivity() != null && !getActivity().isDestroyed()) {
                        if (ResponseObject.isOk(response)) {
                            mPaymentInfo.getTradeVo().setTrade(response.getContent().getModifyResponse().getTrades().get(0));
                            doLagPrint();
                            startLagOk();                            AuthLogManager.getInstance().flush(OrderActionEnum.ACTION_CREDIT, tradeId, tradeUuid, null);
                            getActivity().finish();
                            UserActionEvent.end(UserActionEvent.DINNER_PAY_SETTLE_LAG_PAY);
                        } else {
                                                        if (response.getContent().getModifyResponse() != null && mPaymentInfo.isDinner()) {
                                                                CustomerResp customer = CustomerManager.getInstance().getDinnerLoginCustomer();
                                DinnerShoppingCart.getInstance().resetShopcartItemFromDB(null);
                                CustomerManager.getInstance().setDinnerLoginCustomer(customer);
                                                                DinnerCashManager cashManager = new DinnerCashManager();
                                if (customer != null) {
                                    if (customer.card == null) {
                                        cashManager.updateIntegralCash(customer);
                                    } else {
                                        cashManager.updateIntegralCash(customer.card);
                                    }
                                }
                                TradeDal tradeDal = OperatesFactory.create(TradeDal.class);
                                TradeVo tradeVo = tradeDal.findTrade(mPaymentInfo.getTradeVo().getTrade().getUuid());
                                mPaymentInfo.setTradeVo(tradeVo);
                                mPaymentInfo.setOrdered(true);                            }
                            AuthLogManager.getInstance().clear();
                            ToastUtil.showLongToast(response.getMessage());
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showLongToast(error.getMessage());
                AuthLogManager.getInstance().clear();
            }
        }, getFragmentManager()));
    }

    private void startLagOk() {
        if (mIDoLag != null) {
            mIDoLag.lagOver(2);
        }
    }


    private void doLagPrint() {
        TradeVo tradeVo = mPaymentInfo.getTradeVo();
        Trade trade = tradeVo.getTrade();
        if (trade != null) {
            CustomerLoginResp resp = null;
            if (mCustomer != null) {
                resp = new CustomerLoginResp();
                resp.setCustomerName(mCustomer.customerName);
                resp.setPhoneNumber(mCustomer.mobile);
            }
                                           }
    }

        private void prepareLag() {
                if (mPaymentInfo.getActualAmount() < 0) {
            ToastUtil.showShortToast(R.string.negative_not_pay);
            return;
        }
        if (restAmount < mPaymentInfo.getActualAmount()) {
            ToastUtil.showShortToast(R.string.lag_amount_over_hint);
            return;
        }
        if (mPaymentInfo.getTradeAmount() == 0) {
            ToastUtil.showShortToast(R.string.lag_amount_zero);
            return;
        }
        if (mReason == null || mReason.getContent() == null || TextUtils.isEmpty(mReason.getContent().trim())) {
            ToastUtil.showShortToast(R.string.lag_reason_not_null);
            return;
        }
        if (mPaymentInfo.isSplit() || mPaymentInfo.getTradeVo().getTrade().getTradeType() == TradeType.SPLIT) {
            ToastUtil.showShortToast(R.string.lag_split_can_not);
            return;
        }
        if (this.mPaymentInfo.getPaidAmount() > 0) {
            ToastUtil.showShortToast(R.string.pay_lagpay_cannot_use);
            return;
        }

        if (mPaymentInfo.getTradeVo().getTrade() != null && mPaymentInfo.getTradeVo().getTrade().getId() == null) {
            ToastUtil.showShortToast(R.string.open_table_please);
            return;
        }
                        if (!DinnerShopManager.getInstance().hasValidItems(mPaymentInfo.getTradeVo())) {
            ToastUtil.showShortToast(R.string.dinner_no_item_hint);
            return;
        }
        if (DinnerShopManager.getInstance().hasUnactivedPrivilege(mPaymentInfo.getTradeVo())) {
            ToastUtil.showShortToast(R.string.privilege_ineffective);
            return;
        }
                if (ServerSettingCache.getInstance().isCommercialNeedVerifPassword()) {
            showPasswordDialog(mCustomer.mobile, mPaymentInfo, this.getActivity());
        } else {
            doLag();
        }
    }

        private void showPasswordDialog(final String phonenum, final IPaymentInfo paymentInfo, final FragmentActivity context) {

        final PasswordDialog dialog = new PasswordDialog(context) {
            @Override
            public void close() {
                super.close();

            }
        };

        dialog.setMembeName(mCustomer.getCustomerName(context));
        dialog.setLisetner(new PasswordDialog.PasswordCheckLisetner() {

            @Override
            public void checkPassWord(String password) {
                password = new MD5().getMD5ofStr(password);
                doVerifypassword(phonenum, password, paymentInfo, context, dialog);
            }

            @Override
            public void showPassWord(String password) {

            }

            @Override
            public void showReadKeyBord() {

                if (!PosConnectManager.isPosConnected()) {
                    ToastUtil.showLongToastCenter(context, context.getString(R.string.pay_pos_connection_closed));
                    return;
                }
                final ReadKeyboardDialogFragment dialogFragment =
                        new ReadKeyboardDialogFragment.ReadKeyboardFragmentBuilder().build();
                ReadKeyboardDialogFragment.CardOvereCallback cardOvereCallback = new ReadKeyboardDialogFragment.CardOvereCallback() {

                    @Override
                    public void onSuccess(String keybord) {

                        String password = keybord.toUpperCase(Locale.getDefault());

                        doVerifypassword(phonenum, password.trim(), paymentInfo, context, dialog);
                    }

                    @Override
                    public void onFail(NewLDResponse ldResponse) {
                                            }
                };
                dialogFragment.setPosOvereCallback(cardOvereCallback);

                dialogFragment.show(context.getSupportFragmentManager(), "ReadKeyboardDialog");
            }
        });
        dialog.show();

    }

        private void doVerifypassword(String phonenum, final String password, final IPaymentInfo paymentInfo, final FragmentActivity context,
                                  final PasswordDialog dialog) {
        UserActionEvent.start(UserActionEvent.DINNER_PAY_SETTLE_STORE);
        if (phonenum != null) {
            final String mobile = phonenum;
            CustomerLoginReq loginReq = new CustomerLoginReq();
            loginReq.setMobile(mobile);
            loginReq.setPassword(password);
            loginReq.setNationalTelCode(mCustomer.nationalTelCode);
            loginReq.setNation(mCustomer.nation);
            loginReq.setCountry(mCustomer.country);

            CustomerOperates customerOperate = BusinessTypeUtils.isRetail() ? OperatesRetailFactory.create(CustomerOperates.class) : OperatesFactory.create(CustomerOperates.class);
            customerOperate.login(loginReq, LoadingResponseListener.ensure(new ResponseListener<CustomerLoginResp>() {

                @Override
                public void onResponse(ResponseObject<CustomerLoginResp> response) {
                    if (ResponseObject.isOk(response)) {
                        ToastUtil.showShortToast(R.string.veryfy_password_success);
                        if (dialog != null)
                            dialog.dismiss();
                        EventBus.getDefault().post(new EventReadKeyboard(true, ""));                        doLag();
                    } else {
                        String msg = "";
                        if (response.getStatusCode() == 1126) {
                            msg = context.getString(R.string.order_dish_member_disabled);
                            ToastUtil.showLongToastCenter(context, msg);
                        } else {
                            msg = response.getMessage();
                            ToastUtil.showLongToastCenter(context, msg);
                        }
                        if (dialog != null)
                            dialog.clean();
                        EventBus.getDefault().post(new EventReadKeyboard(false, msg));

                    }
                }

                @Override
                public void onError(VolleyError error) {
                    ToastUtil.showLongToastCenter(context, error.getMessage());
                    EventBus.getDefault().post(new EventReadKeyboard(false, error.getMessage()));                }

            }, context.getSupportFragmentManager()));
        } else {
            ToastUtil.showShortToast(R.string.pay_member_login_please);
        }
    }
    }
