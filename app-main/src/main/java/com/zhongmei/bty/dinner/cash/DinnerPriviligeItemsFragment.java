package com.zhongmei.bty.dinner.cash;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.bty.mobilepay.event.DisplayUserInfoEvent;
import com.zhongmei.bty.mobilepay.manager.CashInfoManager;
import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.db.entity.crm.CustomerGroupLevel;
import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.auth.application.DinnerApplication;
import com.zhongmei.bty.basemodule.commonbusiness.cache.ServerSettingCache;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.bty.basemodule.customer.enums.CustomerLoginType;
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.basemodule.customer.message.BindCustomerFaceCodeResp;
import com.zhongmei.yunfu.bean.req.CustomerLoginResp;
import com.zhongmei.bty.basemodule.customer.message.MemberLoginVoResp;
import com.zhongmei.bty.basemodule.customer.operates.CustomerOperates;
import com.zhongmei.bty.basemodule.database.entity.customer.CustomerV5;
import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCard;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardLevel;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcValueCardAccount;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.NewCardLoginResp;
import com.zhongmei.bty.basemodule.discount.event.ActionDinnerPrilivige;
import com.zhongmei.bty.basemodule.discount.event.ActionDinnerPrilivige.DinnerPriviligeType;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.shoppingcart.SeparateShoppingCart;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.bty.basemodule.trade.manager.DinnerCashManager;
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.ui.base.MobclickAgentFragment;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.resp.data.LoyaltyMindTransferResp;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.CustomerType;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.util.UserActionCode;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.context.util.helper.SpHelper;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;
import com.zhongmei.bty.customer.event.EventDinnerCustomerRegisterRecharge;
import com.zhongmei.bty.dinner.action.ActionRefreshDinnerCustomer;
import com.zhongmei.bty.dinner.action.ActionSaveData;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import de.greenrobot.event.EventBus;

/**
 * 结算项主界面，展示会员登录信息，积分和优惠卷等按钮
 */
@EFragment(R.layout.fragment_dinner_priviliges)
public class DinnerPriviligeItemsFragment extends MobclickAgentFragment {
    private static final String TAG = DinnerPriviligeItemsFragment.class.getSimpleName();

    @ViewById(R.id.rl_customer_login)
    RelativeLayout rlCustomerLogin;

    @ViewById(R.id.rl_customer_info)
    RelativeLayout rlCustomerInfo;

    @ViewById(R.id.tv_customer_name)
    TextView tvCustomerName;

    @ViewById(R.id.tv_card_no)
    TextView tvCardNo;

    @ViewById(R.id.btn_check)
    Button btnCheck;//切换按钮

    @ViewById(R.id.tv_customer_info)
    TextView tvCustomerInfo;

    @ViewById(R.id.btn_exit_customer)
    Button btnExitCustomer;

    @ViewById(R.id.view_top_seprator)
    View viewTopSeparator;

    @ViewById(R.id.view_privilege_seperator)
    View viewPrivilegeSeperator;

    @ViewById(R.id.ll_customer_privilege)
    LinearLayout llCustomerPrivilege;

    @ViewById(R.id.rl_integral)
    ViewGroup vgIntegral;

    @ViewById(R.id.tv_integral)
    TextView tvIntegral;

    @ViewById(R.id.view_face_separator)
    View viewFaceSeparator;

    @ViewById(R.id.layout_market)
    LinearLayout llLayoutMarket;

    @ViewById(R.id.tv_coupon)
    TextView tvCoupon;

    @ViewById(R.id.rl_face)
    ViewGroup rlFace;

    @ViewById(R.id.tv_face)
    TextView tvFace;

    @ViewById(R.id.tv_market_activity)
    TextView tv_market;

    @ViewById(R.id.rl_market_activity)
    RelativeLayout rl_market;

    @ViewById(R.id.layout_save)
    LinearLayout layout_save;

    @ViewById(R.id.layout_extra)
    LinearLayout layout_extra;

    @ViewById(R.id.line)
    View mViewLine;

    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = MainApplication.getInstance();

        this.registerEventBus();
    }

    @AfterViews
    void init() {
        controlShow();
    }

    @Override
    protected void onInit() {
        initCustomer(false);
    }

    private void initCustomer() {
        Log.e(TAG, "initCustomer");
        initCustomer(true);
    }

    /**
     * 根据业务类型，控制按钮显示
     */
    private void controlShow() {
        //联台主单不提供保存结算信息的功能
        if (DinnerShopManager.getInstance().getShoppingCart().getOrder().isUnionMainTrade()) {
            layout_save.setVisibility(View.GONE);
        }
        BusinessType type = DinnerShopManager.getInstance().getShoppingCart().getOrder().getTrade().getBusinessType();
        if (type == BusinessType.GROUP || type == BusinessType.BUFFET) {
            //团餐控制没有营销活动功能
            layout_extra.setVisibility(View.GONE);
            mViewLine.setVisibility(View.GONE);
        }
        //金诚没有会员登录
        if (ServerSettingCache.getInstance().isJinChBusiness()) {
            viewPrivilegeSeperator.setVisibility(View.GONE);
            rlCustomerLogin.setVisibility(View.GONE);
        }
    }

    private void initCustomer(boolean isCancelMiniDisplay) {
        //自助餐暂时不刷新会员信息
//        BusinessType type = DinnerShopManager.getInstance().getShoppingCart().getOrder().getTrade().getBusinessType();
        CustomerResp customer = DinnerShopManager.getInstance().getLoginCustomer();
        if (customer != null) {
            if (customer.card == null) {
                initPhoneCustomer(customer);
            } else {
                initCardCustomer(customer);
            }
        } else {
            removeCustomerInfo(isCancelMiniDisplay);
        }
    }

    /**
     * 显示提示dialog
     */
    private void showInputFaceDialog(final CustomerResp customer) {
        boolean isOpen = SpHelper.getDefault().getBoolean(SpHelper.IBEACON_REMIND, false);
        if (!isOpen || customer.hasFaceCode()) {
            return;
        }
        final CommonDialogFragment.CommonDialogFragmentBuilder builder = new CommonDialogFragment.CommonDialogFragmentBuilder(MainApplication.getInstance());
        builder.message(getString(R.string.customer_login_face_desc));
        builder.iconType(CommonDialogFragment.ICON_ASK);
        builder.positiveText(getString(R.string.customer_login_face_goto_input)).positiveLinstner(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputFace();
            }
        });
        builder.negativeText(getString(R.string.customer_login_face_close));
        builder.build().setCancelable(false);
        builder.build().setCancelWithHomeKey(false);
        builder.build().show(getFragmentManager(), "");
    }

    /**
     * 密码输入框
     *
     * @param inputNo
     * @param customerId
     */
    /*
    private void showPasswordDialog(final String mobile, final String inputNo, final Long customerId) {
        CustomerManager.getInstance().showMemberPasswordDialog(getActivity(), inputNo, new CustomerManager.DinnerLoginListener() {
            @Override
            public void login(PasswordDialog dialog, int needPswd, String password) {
                doVerifypassword(mobile, customerId + "", password, dialog);
            }
        });
    }*/

    /**
     * 验证密码
     */
    /*
    private void doVerifypassword(String mobile, String customerId, final String password, final PasswordDialog dialog) {
        if (customerId != null) {
            MemberLoginReq loginReq = new MemberLoginReq();
            loginReq.setLoginId(customerId);
            loginReq.setMobile(mobile);
            loginReq.setPassword(password);
            CustomerOperates customerOperate = OperatesFactory.create(CustomerOperates.class);
            customerOperate.login(loginReq, LoadingResponseListener.ensure(new ResponseListener<MemberLoginResp>() {

                @Override
                public void onResponse(ResponseObject<MemberLoginResp> response) {
                    if (ResponseObject.isOk(response)) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        inputFace();
                    } else {
                        if (dialog != null) {
                            dialog.clean();
                        }
                        ToastUtil.showShortToast(response.getMessage());
                    }
                }

                @Override
                public void onError(VolleyError error) {
                    ToastUtil.showShortToast(error.getMessage());
                }

            }, getFragmentManager()));
        } else {
            ToastUtil.showShortToast(R.string.pay_member_login_please);
        }
    }
    */


    /**
     * 绑定顾客
     */
    private void inputFace() {
        /*boolean available= BaiduFaceRecognition.getInstance().checkFaceServer();
        if(!available){
            FacecognitionActivity.showFaceServerWarmDialog(getContext(),getChildFragmentManager());
            return;
        }
        startActivityForResult(BaiduFaceRecognition.getInstance().getRegistFaceIntent(), FaceRequestCodeConstant.RC_PAY_BIND_FACE);*/
    }

    /**
     * 绑定顾客
     *
     * @param customer
     * @param faceCode
     */
    private void bindFace(CustomerResp customer, final String faceCode) {
        CustomerOperates operates = OperatesFactory.create(CustomerOperates.class);
        operates.bindCustomerFaceCode(customer.customerId, faceCode, new ResponseListener<BindCustomerFaceCodeResp>() {
            @Override
            public void onResponse(ResponseObject<BindCustomerFaceCodeResp> response) {
                if (ResponseObject.isOk(response)) {
                    if (response.getContent() != null) {
                        if (response.getContent().isOk()) {
                            DinnerShopManager.getInstance().getLoginCustomer().faceCode = faceCode;
                            DinnerShopManager.getInstance().getLoginCustomer().hasFaceCode = 1;
                            initCustomer(false);
                        } else {
                            ToastUtil.showLongToast(response.getContent().getErrorMessage());
                        }
                    } else {
                        ToastUtil.showLongToast(response.getMessage());
                    }
                } else {
                    ToastUtil.showLongToast(response.getMessage());
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showLongToast(error.getMessage());
            }
        });
    }

    /**
     * 加载手机号会员信息
     *
     * @Title: initPhoneCustomer
     * @Return void 返回类型
     */
    private void initPhoneCustomer(CustomerResp customer) {
        rlCustomerLogin.setVisibility(View.GONE);
        rlCustomerInfo.setVisibility(View.VISIBLE);
        String name = customer.customerName;
        if (!TextUtils.isEmpty(name)) {
            tvCustomerName.setText(Utils.getDisplayName(name));
        } else {
            tvCustomerName.setText(R.string.customer_no_name2);
        }
        if (customer.isMember()) {
//            showInputFaceDialog(customer); // 屏蔽会员认证弹框
            tvCardNo.setText("");
            tvCardNo.setVisibility(View.GONE);
            btnCheck.setVisibility(View.VISIBLE);
            vgIntegral.setVisibility(View.VISIBLE);
            viewFaceSeparator.setVisibility(View.VISIBLE);
            if (customer.needRefresh || customer.coupCount == null || customer.integral == null) {
                loginByPhoneOrId(customer);
            } else {
                CustomerGroupLevel customerLevel = customer.customerLevel;
                if (customerLevel == null) {
                    showValuecardAndIntegral(customer.remainValue, customer.integral, null, null);
                } else {
                    showValuecardAndIntegral(customer.remainValue, customer.integral, customerLevel.getGroupName(), customerLevel.getId());
                }
                showIntegralAndCoupon(customer.integral, customer.coupCount, customer.hasFaceCode(), false, customer.faceGrade);
                new DinnerCashManager().updateIntegralCash(customer);
            }
        } else {
            if (customer.needRefresh || customer.coupCount == null || customer.integral == null) {
                loginByPhoneOrId(customer);
            } else {
                String number = customer.mobile;
                tvCardNo.setText(number);
                tvCardNo.setVisibility(View.VISIBLE);
                btnCheck.setVisibility(View.GONE);
                tvCustomerInfo.setText("");
                viewTopSeparator.setVisibility(View.VISIBLE);
                llCustomerPrivilege.setVisibility(View.VISIBLE);
                vgIntegral.setVisibility(View.GONE);
                viewFaceSeparator.setVisibility(View.GONE);
                rlFace.setVisibility(View.GONE);
                int couponCount = 0;
                if (customer.coupCount != null) {
                    couponCount = customer.coupCount;
                }
                if (couponCount == 0) {
                    tvCoupon.setTextColor(getResources().getColor(R.color.color_bcbcbc));
                    tvCoupon.setText(R.string.pay_coupon_null);
                } else {
                    tvCoupon.setTextColor(getResources().getColor(R.color.notice_warm));
                    tvCoupon.setText(couponCount + mContext.getString(R.string.sheet));
                }
            }
        }
    }

    /**
     * @Title: initCardCustomer
     * @Return void 返回类型
     */
    private void initCardCustomer(CustomerResp customer) {
        rlCustomerLogin.setVisibility(View.GONE);
        rlCustomerInfo.setVisibility(View.VISIBLE);

        EcCard card = customer.card;
        boolean isNeedRefresh = customer.needRefresh;

        String name = card.getName();
        tvCustomerName.setText(Utils.getDisplayName(name));
        tvCardNo.setText("(" + card.getCardNum() + ")");
        tvCardNo.setVisibility(View.VISIBLE);
        btnCheck.setVisibility(View.GONE);

        if (!isNeedRefresh && card.getValueCardAccount() != null) {
            Double value = null;// 余额
            Long integral = null;// 积分
            if (card.getValueCardAccount() != null) {
                value = card.getValueCardAccount().getRemainValue();
            }
            if (card.getIntegralAccount() != null) {
                integral = card.getIntegralAccount().getIntegral();
            }

            EcCardLevel cardLevel = card.getCardLevel();
            if (cardLevel == null) {
                showValuecardAndIntegral(value, integral, null, null);
            } else {
                showValuecardAndIntegral(value, integral, cardLevel.getCardLevelName(), null);
            }
            showIntegralAndCoupon(integral, 0, false, true, 0);
            new DinnerCashManager().updateIntegralCash(card);
        } else {
            loginByCardNo(card.getCardNum());
        }
    }

    /**
     * 移除会员信息，并清空相关的显示
     */
    private void removeCustomerInfo() {
        removeCustomerInfo(true);
    }

    /**
     * 移除会员信息，并清空相关的显示
     *
     * @param isCancelMiniDisplay 是否清空副屏
     */
    private void removeCustomerInfo(boolean isCancelMiniDisplay) {
        if (ServerSettingCache.getInstance().isJinChBusiness()) {
            return; //金诚没有会员登录功能，直接返回。
        }
        if (isCancelMiniDisplay) {
            DisplayServiceManager.doCancel(mContext);
        }
        DinnerShopManager.getInstance().setLoginCustomer(null);
        DinnerShopManager.getInstance().getShoppingCart().setOpenIdenty(null);

        if (DinnerShopManager.getInstance().isSepartShopCart()) {
            SeparateShoppingCart.getInstance().setSeparateCustomer(null);
        } else {
            DinnerShoppingCart.getInstance().setDinnerCustomer(null);
        }
        DinnerShopManager.getInstance().getShoppingCart().removeAllPrivilegeForCustomer(true, false);

        rlCustomerLogin.setVisibility(View.VISIBLE);
        rlCustomerInfo.setVisibility(View.GONE);
        tvCustomerName.setText("");
        tvCardNo.setText("");
        tvCardNo.setVisibility(View.GONE);
        tvCustomerInfo.setText("");
        viewTopSeparator.setVisibility(View.GONE);
        llCustomerPrivilege.setVisibility(View.GONE);
    }

    /**
     * 根据手机号查询会员信息
     */
    private void loginByPhoneOrId(final CustomerResp customer) {
        ResponseListener<MemberLoginVoResp> listener = new ResponseListener<MemberLoginVoResp>() {

            @Override
            public void onResponse(ResponseObject<MemberLoginVoResp> response) {
                try {
                    if (ResponseObject.isOk(response) && MemberLoginVoResp.isOk(response.getContent())) {
                        CustomerLoginResp resp = response.getContent().getResult();
                        if (resp.customerIsDisable()) {//当前账号冻结
                            ToastUtil.showShortToast(R.string.order_dish_member_disabled);
                            return;
                        }
                        CustomerResp customerNew = resp.getCustomer();
                        customerNew.setInitialValue();
                        customerNew.queryLevelRightInfos();
                        customerNew.needRefresh = false;
                        DinnerShopManager.getInstance().setLoginCustomer(customerNew);// 设置会员
                        DinnerShopManager.getInstance().getShoppingCart().memberPrivilege(true, true);//设置会员折扣／会员价
                        //构建并设置TradeCustomer
                        TradeCustomer tradeCustomer = CustomerManager.getInstance().getTradeCustomer(customerNew);
                        if (customerNew.card == null) {
                            if (customerNew.isMember()) {
                                tradeCustomer.setCustomerType(CustomerType.MEMBER);
                            } else {
                                tradeCustomer.setCustomerType(CustomerType.CUSTOMER);
                            }
                        } else {
                            tradeCustomer.setCustomerType(CustomerType.CARD);
                            tradeCustomer.setEntitycardNum(customerNew.card.getCardNum());
                        }
                        //获取之前的tradecustomer，把id和uuid等值赋值给新的tradecustomer，保证服务器不会重复存储同类型的tradecustomer
                        TradeVo tradeVo = DinnerShopManager.getInstance().getShoppingCart().getOrder();
                        TradeCustomer oldTradeCustomer = new DinnerCashManager().getTradeCustomer(tradeVo, tradeCustomer.getCustomerType());
                        if (oldTradeCustomer != null) {
                            tradeCustomer.setId(oldTradeCustomer.getId());
                            tradeCustomer.setUuid(oldTradeCustomer.getUuid());
                            tradeCustomer.setTradeId(oldTradeCustomer.getTradeId());
                            tradeCustomer.setTradeUuid(oldTradeCustomer.getTradeUuid());
                            tradeCustomer.setServerUpdateTime(oldTradeCustomer.getServerUpdateTime());
                        }
                        if (DinnerShopManager.getInstance().isSepartShopCart()) {
                            SeparateShoppingCart.getInstance().setSeparateCustomer(tradeCustomer);
                        } else {
                            DinnerShoppingCart.getInstance().setDinnerCustomer(tradeCustomer);
                        }
                        DinnerShopManager.getInstance().getShoppingCart().setOpenIdenty(resp.getOpenId());
                        //刷新会员信息的展示
                        initPhoneCustomer(customerNew);
                    } else {
                        ToastUtil.showShortToast(response.getMessage());
                        removeCustomerInfo();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                    removeCustomerInfo();
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showShortToast(error.getMessage());
                removeCustomerInfo();
            }
        };
        CustomerManager customerManager = CustomerManager.getInstance();
        if (!TextUtils.isEmpty(customer.mobile)) {
            customerManager.customerLogin(CustomerLoginType.MOBILE, customer.mobile, "", false, true, true, LoadingResponseListener.ensure(listener, getFragmentManager()));
        } else if (customer.customerId != null && customer.customerId != 0) {
            CustomerManager.getInstance().customerLogin(CustomerLoginType.MEMBER_ID, customer.customerId + "", "", false, true, true, LoadingResponseListener.ensure(listener, getFragmentManager()));
        }

    }

    /**
     * 使用实体卡号登录
     *
     * @Title: loginByCardNo
     * @Return void 返回类型
     */
    private void loginByCardNo(final String cardNo) {
        CustomerOperates operates = OperatesFactory.create(CustomerOperates.class);
        ResponseListener<LoyaltyMindTransferResp<NewCardLoginResp>> listener =
                LoadingResponseListener.ensure(new ResponseListener<LoyaltyMindTransferResp<NewCardLoginResp>>() {

                    @Override
                    public void onResponse(ResponseObject<LoyaltyMindTransferResp<NewCardLoginResp>> response) {
                        try {
                            if (ResponseObject.isOk(response)) {
                                NewCardLoginResp resp = response.getContent().getData();
                                //1表示loyaty登录成功
                                if (response.getContent().isOk() && resp != null) {
                                    // 设置card的名称，从customer中获得
                                    EcCard card = resp.getCardInstance();
                                    CustomerV5 customerV5 = resp.getCustomerV5();
                                    card.setName(customerV5.getName());
                                    card.queryLevelSettingInfo();
                                    // 如果valuecard为null时，给一个余额为0的对象，避免切换该界面时总是加载网络获取信息（只有新卡会为null）
                                    EcValueCardAccount valueCardAccount = card.getValueCardAccount();
                                    if (valueCardAccount == null) {
                                        valueCardAccount = new EcValueCardAccount();
                                        valueCardAccount.setRemainValue(0D);
                                        card.setValueCardAccount(valueCardAccount);
                                    }
                                    card.setCustomer(customerV5);
                                    if (card != null && card.priceLimit != null) {
                                        CustomerManager.getInstance().setCurrentCardIsPriceLimit(card.priceLimit == 2 ? true : false);
                                    }
                                    CustomerResp customerNew = resp.getCustomer();
                                    // 重新设置拆单购物车实体卡登录对象，更新内容
                                    DinnerShopManager.getInstance().setLoginCustomer(customerNew);
                                    DinnerShopManager.getInstance().getShoppingCart().memberPrivilege(true, true);
                                    initCardCustomer(customerNew);
                                } else {
                                    ToastUtil.showShortToast(response.getContent().getMessage());
                                }
                            } else {
                                ToastUtil.showShortToast(response.getMessage());
                                removeCustomerInfo();
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "", e);
                            removeCustomerInfo();
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        ToastUtil.showShortToast(error.getMessage());
                        removeCustomerInfo();
                    }
                }, getFragmentManager());
        operates.cardLoginNew(cardNo, listener);
    }

    /**
     * 显示积分和储值
     *
     * @param valuecard 余额
     * @param integral  积分
     * @param levelName 等级名称
     * @param level     等级
     */
    private void showValuecardAndIntegral(Double valuecard, Long integral, String levelName, Long level) {
        if (valuecard == null) {
            valuecard = 0.0;
        }
        if (integral == null) {
            integral = 0L;
        }
        if (TextUtils.isEmpty(levelName)) {
            levelName = "";
        }
        String levelStr = "";
        if (level != null) {
            levelStr = "(" + level + mContext.getString(R.string.level) + ")";
        }
        tvCustomerInfo.setText(levelName + levelStr + "  " + mContext.getString(R.string.charging) + "：" + CashInfoManager.formatCash(valuecard));
        showDisplayUserInfo(mContext);
    }

    /**
     * 显示积分和优惠券张数
     *
     * @param integral
     * @param couponCount
     */
    private void showIntegralAndCoupon(Long integral, int couponCount, boolean hasFaceCode, boolean isCard, int faceGrade) {
        viewTopSeparator.setVisibility(View.VISIBLE);
        llCustomerPrivilege.setVisibility(View.VISIBLE);
        if (integral == null) {
            integral = 0L;
        }
        if (integral == 0L) {
            tvIntegral.setTextColor(getResources().getColor(R.color.color_bcbcbc));
        } else {
            tvIntegral.setTextColor(getResources().getColor(R.color.notice_warm));

        }
        tvIntegral.setText(integral + mContext.getString(R.string.cent));
        if (isCard) {
            tvCoupon.setTextColor(getResources().getColor(R.color.color_bcbcbc));
            tvCoupon.setText(R.string.pay_coupon_null);
            rlFace.setVisibility(View.GONE);
            viewFaceSeparator.setVisibility(View.GONE);
        } else {
            if (couponCount == 0) {
                tvCoupon.setTextColor(getResources().getColor(R.color.color_bcbcbc));
                tvCoupon.setText(R.string.pay_coupon_null);
            } else {
                tvCoupon.setTextColor(getResources().getColor(R.color.notice_warm));
                tvCoupon.setText(couponCount + mContext.getString(R.string.sheet));
            }
            rlFace.setVisibility(View.VISIBLE);
            viewFaceSeparator.setVisibility(View.VISIBLE);
            if (!hasFaceCode) {
                tvFace.setTextColor(getResources().getColor(R.color.color_bcbcbc));
                tvFace.setText(R.string.customer_face_regiest_off);
//            rlFace.setEnabled(true);
            } else {
                tvFace.setTextColor(getResources().getColor(R.color.coloc_FF6B6A));
                if (faceGrade == 0) {
                    tvFace.setText(getString(R.string.customer_face_regiest_on));
                } else {
                    tvFace.setText(getString(R.string.customer_face_regiest_on) + faceGrade + getString(R.string.cent));
                }
//            rlFace.setEnabled(false);
            }
        }

    }

    @Click({R.id.btn_customer_login, R.id.btn_check, R.id.btn_exit_customer, R.id.rl_discount, R.id.rl_integral, R.id.rl_coupon,
            R.id.rl_extra_charge, R.id.rl_coupon_code, R.id.rl_market_activity, R.id.tv_fete, R.id.done_save_bt, R.id.rl_face, R.id.rl_memo, R.id.rl_customer_coupon})
    void click(View v) {
//        if(ClickManager.getInstance().isClicked()){
//            return;
//        }
        CustomerResp customer;
        switch (v.getId()) {
            case R.id.btn_customer_login:
                if (ClickManager.getInstance().isClicked(R.id.btn_customer_login)) {
                    return;
                }
                EventBus.getDefault().post(new ActionDinnerPrilivige(DinnerPriviligeType.LOGIN));
                MobclickAgentEvent.onEvent(UserActionCode.ZC030004);
                break;
            case R.id.btn_check:
                MobclickAgentEvent.onEvent(UserActionCode.ZC030020);
                customer = DinnerShopManager.getInstance().getLoginCustomer();
                if (customer.card != null || Utils.isNotEmpty(customer.otherCardList)) {
                    ActionDinnerPrilivige action = new ActionDinnerPrilivige(DinnerPriviligeType.SWITCH_CARD);
                    action.setCards(customer.otherCardList);
                    action.setSource(DinnerCashManager.ITEMS);
                    EventBus.getDefault().post(action);
                } else {
                    ToastUtil.showShortToast(mContext.getString(R.string.no_card_to_switch));
                }
                break;
            case R.id.btn_exit_customer:
                MobclickAgentEvent.onEvent(UserActionCode.ZC030021);
                removeCustomerInfo();
                break;
            case R.id.rl_discount:
                MobclickAgentEvent.onEvent(UserActionCode.ZC030005);
                VerifyHelper.verifyAlert(getActivity(), DinnerApplication.PERMISSION_DINNER_PRIVILEDGE,
                        new VerifyHelper.Callback() {
                            @Override
                            public void onPositive(User user, String code, Auth.Filter filter) {
                                super.onPositive(user, code, filter);
                                EventBus.getDefault().post(new ActionDinnerPrilivige(DinnerPriviligeType.DISCOUNT));
                            }
                        });
                break;
            case R.id.rl_integral:
                MobclickAgentEvent.onEvent(UserActionCode.ZC030022);
                EventBus.getDefault().post(new ActionDinnerPrilivige(DinnerPriviligeType.INTEGRAL));
                break;
            case R.id.rl_coupon:
                MobclickAgentEvent.onEvent(UserActionCode.ZC030023);
                EventBus.getDefault().post(new ActionDinnerPrilivige(DinnerPriviligeType.COUPON));
                break;
            case R.id.rl_face:
                MobclickAgentEvent.onEvent(UserActionCode.ZC030024);
//                customer = DinnerShopManager.getInstance().getLoginCustomer();
//                if (!customer.hasFaceCode() && !TextUtils.isEmpty(customer.mobile)){
                inputFace();
//                    showPasswordDialog(customer.mobile , customer.customerName , customer.customerId);
//                }
                break;
            case R.id.rl_extra_charge:
                MobclickAgentEvent.onEvent(UserActionCode.ZC030006);
                EventBus.getDefault().post(new ActionDinnerPrilivige(DinnerPriviligeType.EXTRA_CHARGE));
                break;
            case R.id.rl_coupon_code:
                MobclickAgentEvent.onEvent(UserActionCode.ZC030007);
                EventBus.getDefault().post(new ActionDinnerPrilivige(DinnerPriviligeType.COUPON_CODE));
                break;
            case R.id.rl_market_activity:
                MobclickAgentEvent.onEvent(UserActionCode.ZC030008);
                EventBus.getDefault().post(new ActionDinnerPrilivige(DinnerPriviligeType.MARKET_ACTIVITY));
                break;
            case R.id.done_save_bt:
                // 没有可结算品项不能跳转
                MobclickAgentEvent.onEvent(UserActionCode.ZC030009);
                UserActionEvent.start(UserActionEvent.DINNER_PAY_ORDER_SAVE);
                EventBus.getDefault().post(new ActionSaveData());
                break;
            case R.id.rl_customer_coupon:
                // 没有可结算品项不能跳转
                customer = DinnerShopManager.getInstance().getLoginCustomer();
                if (customer != null && customer.customerId != null) {
                    EventBus.getDefault().post(new ActionDinnerPrilivige(DinnerPriviligeType.CUSTOMER_COUPON, customer));
                } else {
                    ToastUtil.showShortToast(getString(R.string.dinner_privilege_send_coupon_hint));
                }
                break;
            case R.id.rl_memo:
                // 喜欢/备注
                EventBus.getDefault().post(new ActionDinnerPrilivige(DinnerPriviligeType.CUSTOMER_LIKE_REMARK));
                break;
            default:
                break;
        }
    }

    public void onEventMainThread(ActionRefreshDinnerCustomer action) {
        Log.e(TAG, "RefreshDinnerCustomer");
        if (isVisible() && !isRemoving() && !isDetached()) {
            initCustomer();
        }
    }

    public void onEventMainThread(EventDinnerCustomerRegisterRecharge event) {
        if (event.getmCustomer() != null) {
            loginByPhoneOrId(event.getmCustomer());
        }
    }

    public void onEventMainThread(DisplayUserInfoEvent displayUserInfoEvent) {//add v8.9 解耦
        showDisplayUserInfo(getActivity());
    }

    // 第二屏显示用户信息
    public static void showDisplayUserInfo(Context context) {
        CustomerResp customer = DinnerShopManager.getInstance().getLoginCustomer();
        if (customer != null) {
            if (customer.card == null) {
                /*DisplayUserInfo userInfo =
                        DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_USERINFO_SHOW,
                                customer,
                                customer.integral == null ? 0 : customer.integral,
                                false, 0);
                if(customer.mFaceFeatureVo!=null){
                    userInfo.setFaceFeature(customer.mFaceFeatureVo.face,customer.mFaceFeatureVo.faceDesc
                    ,customer.mFaceFeatureVo.faceScoreDesc,customer.mFaceFeatureVo.sex
                    );
                }
                DisplayServiceManager.updateDisplay(context, userInfo);*/
            } else {
                EcCard card = customer.card;
                long integral = 0;
                if (card.getIntegralAccount() != null && card.getIntegralAccount().getIntegral() != null) {
                    integral = card.getIntegralAccount().getIntegral();
                }
                /*DisplayUserInfo userInfo =
                        DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_USERINFO_SHOW, card, integral, false, 0);
                if(customer.mFaceFeatureVo!=null){
                    userInfo.setFaceFeature(customer.mFaceFeatureVo.face,customer.mFaceFeatureVo.faceDesc
                            ,customer.mFaceFeatureVo.faceScoreDesc,customer.mFaceFeatureVo.sex
                    );
                }
                DisplayServiceManager.updateDisplay(context, userInfo);*/
            }
        }
    }

    @Override
    public void onDestroy() {
        this.unregisterEventBus();
        super.onDestroy();
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FaceRequestCodeConstant.RC_PAY_BIND_FACE && resultCode == Activity.RESULT_OK) {
            bindFace(DinnerShopManager.getInstance().getLoginCustomer(), data.getStringExtra(BaiduFaceRecognition.KEY_FACE_CODE));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/

}
