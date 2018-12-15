package com.zhongmei.bty.basemodule.customer.manager;

import com.j256.ormlite.dao.Dao;
import com.zhongmei.bty.basemodule.customer.action.EventMemeberIsLogin;
import com.zhongmei.bty.basemodule.customer.bean.CustomerMobile;
import com.zhongmei.bty.basemodule.customer.enums.CustomerLoginType;
import com.zhongmei.bty.basemodule.customer.message.MemberCouponsReq;
import com.zhongmei.bty.basemodule.customer.message.MemberCouponsVoResp;
import com.zhongmei.bty.basemodule.customer.message.MemberLoginVoResp;
import com.zhongmei.bty.basemodule.customer.operates.CustomerOperates;
import com.zhongmei.bty.basemodule.customer.util.CustomerUtil;
import com.zhongmei.bty.basemodule.database.entity.customer.CustomerV5;
import com.zhongmei.bty.basemodule.devices.mispos.enums.CardStatus;
import com.zhongmei.bty.basemodule.discount.cache.MarketRuleCache;
import com.zhongmei.yunfu.db.entity.discount.CustomerScoreRule;
import com.zhongmei.bty.basemodule.session.core.auth.Code;
import com.zhongmei.yunfu.bean.YFResponseList;
import com.zhongmei.yunfu.bean.req.CustomerCouponResp;
import com.zhongmei.yunfu.db.entity.booking.Booking;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.db.enums.CustomerType;
import com.zhongmei.yunfu.db.enums.Sex;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.bean.YFResponse;
import com.zhongmei.yunfu.bean.req.CustomerLoginReq;
import com.zhongmei.yunfu.bean.req.CustomerLoginResp;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.YFResponseListener;

import java.util.List;

import de.greenrobot.event.EventBus;

public class CustomerManager {
    private static final String TAG = CustomerManager.class.getSimpleName();

    public static final int NEED_PSWD = 1;

    public static final int NOT_NEED_PSWD = 2;

    private CustomerResp mLoginCustomer = null;// 快餐业务会员

    private CustomerResp mDinnerLoginCustomer = null;// 正餐业务会员

    private CustomerResp mSeparatLoginCustomer = null;// 拆单业务会员

    private static CustomerManager sCustomerManager;

    private String currentPhone;

    public List<String> getAccounts() {
        return mAccounts;
    }

    public void setAccounts(List<String> mAccounts) {
        this.mAccounts = mAccounts;
    }

    private List<String> mAccounts;

    //1表示从支付页登录,0表示快餐点餐页
    private int loginSource = 0;

    private CustomerScoreRule mCustomerScoreRule;


    public int getLoginSource() {
        return loginSource;
    }

    public void setLoginSource(int loginSource) {
        this.loginSource = loginSource;
    }

    public void setCurrentPhone(String currentPhone) {
        this.currentPhone = currentPhone;
    }

    public String getCurrentPhone() {
        return currentPhone;
    }

    private CustomerResp currentCustomer = null;// 当前呼入电话的Customer

    public CustomerResp getCurrentCustomer() {
        return currentCustomer;
    }

    public void setCurrentCustomer(CustomerResp currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

    private CustomerManager() {
    }

    public static CustomerManager getInstance() {
        if (sCustomerManager == null) {
            sCustomerManager = new CustomerManager();
        }
        return sCustomerManager;
    }

    public CustomerResp getLoginCustomer() {
        return mLoginCustomer;
    }

    public void setLoginCustomer(CustomerResp customer) {
        mLoginCustomer = customer;
        EventBus.getDefault().post(new EventMemeberIsLogin(mLoginCustomer == null ? false : true));
    }

    public TradeCustomer getTradeCustomer(CustomerMobile customerMobile) {
        if (customerMobile != null) {
            TradeCustomer tradeCustomer = new TradeCustomer();
            tradeCustomer.validateCreate();
            tradeCustomer.setUuid(SystemUtils.genOnlyIdentifier());
            tradeCustomer.setCustomerName(customerMobile.customerName);
            tradeCustomer.setCustomerPhone(customerMobile.mobile);
            tradeCustomer.setCustomerId(customerMobile.customerId);
            Integer sexInt = customerMobile.sex;
            if (sexInt != null) {
                tradeCustomer.setCustomerSex(Utils.equals(sexInt, 0) ? Sex.FEMALE : Sex.MALE);
            }
            return tradeCustomer;
        }
        return null;
    }

    public TradeCustomer getTradeCustomer(CustomerResp currentCustomer) {
        if (currentCustomer != null) {
            TradeCustomer customer = new TradeCustomer();
            customer.validateCreate();
            customer.setUuid(SystemUtils.genOnlyIdentifier());
            customer.setCustomerName(currentCustomer.customerName);
            customer.setCustomerPhone(currentCustomer.mobile);
            customer.setCustomerId(currentCustomer.customerId);
            Integer sexInt = currentCustomer.sex;
            if (sexInt != null) {
                customer.setCustomerSex(Utils.equals(sexInt, 0) ? Sex.FEMALE : Sex.MALE);
            }
            return customer;
        }
        return null;
    }

    public TradeCustomer getTradeCustomer(CustomerV5 customerV5) {
        if (customerV5 != null) {
            TradeCustomer customer = new TradeCustomer();
            customer.validateCreate();
            customer.setCustomerName(customerV5.getName());
            customer.setCustomerPhone(customerV5.getMobile());
            customer.setCustomerId(customerV5.getCustomerid());
            customer.setCustomerSex(customerV5.getSex());

            return customer;
        }
        return null;
    }

    public CustomerV5 getCustomerV5(TradeCustomer tradeCustomer) {
        if (tradeCustomer != null) {
            CustomerV5 customerV5 = new CustomerV5();
            customerV5.setName(tradeCustomer.getCustomerName());
            customerV5.setMobile(tradeCustomer.getCustomerPhone());
            customerV5.setCustomerid(tradeCustomer.getCustomerId());
            customerV5.setSex(tradeCustomer.getCustomerSex());

            return customerV5;
        }

        return null;
    }

    public CustomerResp getCustomer(TradeCustomer tradeCustomer) {
        if (tradeCustomer != null) {
            CustomerResp customerNew = new CustomerResp();
            customerNew.customerName = tradeCustomer.getCustomerName();
            customerNew.mobile = tradeCustomer.getCustomerPhone();
            customerNew.customerId = tradeCustomer.getCustomerId();
            customerNew.levelId = tradeCustomer.levelId;
            customerNew.sex = tradeCustomer.getCustomerSex() == Sex.MALE ? 1 : 0;

            return customerNew;
        }

        return null;
    }

    public void clearCurrentCustomer() {
        CustomerManager.getInstance().setCurrentCustomer(null);
        setCurrentPhone(null);
    }

    // 正餐会员
    public CustomerResp getDinnerLoginCustomer() {
        return mDinnerLoginCustomer;
    }

    // 正餐会员
    public void setDinnerLoginCustomer(CustomerResp dinnerLoginCustomer) {
        this.mDinnerLoginCustomer = dinnerLoginCustomer;
    }

    public CustomerResp getSeparateLoginCustomer() {
        return mSeparatLoginCustomer;
    }

    public void setSeparateLoginCustomer(CustomerResp separatLoginCustomer) {
        this.mSeparatLoginCustomer = separatLoginCustomer;
    }

    /*public PasswordDialog dinnerLoginByPhoneNo(final FragmentActivity context, String input, final DinnerLoginListener listener) {
        final PasswordDialog dialog;
        if (ServerSettingManager.isCommercialNeedVerifPassword()) {
            return showMemberPasswordDialog(context, input, listener);
        } else {
            if (listener != null) {
                listener.login(null, CustomerManager.NOT_NEED_PSWD, "");
            }
        }
        return null;
    }*/

    /*public PasswordDialog showMemberPasswordDialog(final FragmentActivity context, String input, final DinnerLoginListener listener) {
        final PasswordDialog dialog;
        DisplayUserInfo dUserInfo = DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_PASSWORD_INPUT,
                        "",
                        null,
                        0,
                        true, 0);
        DisplayServiceManager.updateDisplay(context, dUserInfo);

        dialog = new PasswordDialog(context) {
            @Override
            public void close() {
                dismiss();
                DisplayServiceManager.doCancel(context);
            }
        };

        //名字为空时，显示电话号码
//            if (!TextUtils.isEmpty(input)) {
//                dialog.setMembeName(input);
//            } else {
//                dialog.setMembeName(context.getString(R.string.customer_sex_unknown));
//            }
        dialog.setMembeName(input);
        dialog.setLisetner(new PasswordDialog.PasswordCheckLisetner() {
            @Override
            public void checkPassWord(String password) {
                password = new MD5().getMD5ofStr(password);
                if (listener != null) {
                    listener.login(dialog, CustomerManager.NEED_PSWD, password);
                }
            }

            @Override
            public void showPassWord(String password) {
                DisplayUserInfo dUserInfo = DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_PASSWORD_INPUT,
                        password,
                        null,
                        0,
                        false, 0);
                DisplayServiceManager.updateDisplay(context, dUserInfo);
            }

            @Override
            public void showReadKeyBord() {
                if (!PosConnectManager.isPosConnected()) {
                    ToastUtil.showLongToastCenter(context, context.getString(R.string.customer_pos_connection_closed));
                    return;
                }

                final ReadKeyboardDialogFragment dialogFragment =
                        new ReadKeyboardDialogFragment.ReadKeyboardFragmentBuilder().build();
                ReadKeyboardDialogFragment.CardOvereCallback cardOvereCallback = new ReadKeyboardDialogFragment.CardOvereCallback() {

                    @Override
                    public void onSuccess(String keybord) {
                        String password = keybord.toUpperCase(Locale.getDefault());
                        if (listener != null) {
                            listener.login(dialog, CustomerManager.NEED_PSWD, password);
                        }
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
        return dialog;
    }*/

    /**
     * 正餐微信扫码登录
     *
     * @param context
     * @param listener
     */
    /*@Deprecated
    public void dinnerLoginWxNo(final FragmentActivity context, final DinnerLoginListener listener) {
        if (ServerSettingManager.isCommercialNeedVerifPassword()) {
            DisplayUserInfo dUserInfo =
                    DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_PASSWORD_INPUT,
                            "",
                            null,
                            0,
                            true, 0);
            DisplayServiceManager.updateDisplay(context, dUserInfo);

            final PasswordDialog dialog = new PasswordDialog(context) {
                @Override
                public void close() {
                    dismiss();
                    DisplayServiceManager.doCancel(context);
                }
            };

            //名字处显示微信扫码登录
            dialog.setMemberContent(context.getString(R.string.customer_login_through_wechat_scan_code));
            dialog.setLisetner(new PasswordDialog.PasswordCheckLisetner() {
                @Override
                public void checkPassWord(String password) {
                    password = new MD5().getMD5ofStr(password);
                    if (listener != null) {
                        listener.login(dialog, CustomerManager.NEED_PSWD, password);
                    }
                }

                @Override
                public void showPassWord(String password) {
                    DisplayUserInfo dUserInfo = DisplayServiceManager.buildDUserInfo(DisplayUserInfo.COMMAND_PASSWORD_INPUT,
                            password,
                            null,
                            0,
                            false, 0);
                    DisplayServiceManager.updateDisplay(context, dUserInfo);
                }

                @Override
                public void showReadKeyBord() {
                    if (!PosConnectManager.isPosConnected()) {
                        ToastUtil.showLongToastCenter(context, context.getString(R.string.customer_pos_connection_closed));
                        return;
                    }

                    final ReadKeyboardDialogFragment dialogFragment =
                            new ReadKeyboardDialogFragment.ReadKeyboardFragmentBuilder().build();
                    ReadKeyboardDialogFragment.CardOvereCallback cardOvereCallback = new ReadKeyboardDialogFragment.CardOvereCallback() {

                        @Override
                        public void onSuccess(String keybord) {
                            String password = keybord.toUpperCase(Locale.getDefault());
                            if (listener != null) {
                                listener.login(dialog, CustomerManager.NEED_PSWD, password);
                            }
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
        } else {
            if (listener != null) {
                listener.login(null, CustomerManager.NOT_NEED_PSWD, "");
            }
        }
    }

    public interface DinnerLoginListener {
        void login(PasswordDialog dialog, int needPswd, String password);
    }*/
    /*public void customerLogin(CustomerLoginType loginType, String loginId, String pwd, boolean isNeedPwd, boolean isNeedCredit, boolean isNeedCard, ResponseListener<MemberLoginVoResp> listener, CustomerOperates customerOperate) {
        customerLogin(loginType, loginId, pwd, null, null, null, isNeedPwd, isNeedCredit, isNeedCard, listener, customerOperate);
    }*/

    /*public void customerLogin(CustomerLoginType loginType, String loginId, String pwd, boolean isNeedPwd, boolean isNeedCredit, boolean isNeedCard, ResponseListener<MemberLoginVoResp> listener) {
        customerLogin(loginType, loginId, pwd, null, null, null, isNeedPwd, isNeedCredit, isNeedCard, listener, null);
    }*/

    /*public void customerLogin(CustomerLoginType loginType, String loginId, String pwd, boolean isNeedPwd, boolean isNeedCredit, boolean isNeedCard, CustomerOperates operates, ResponseListener<MemberLoginVoResp> listener) {
        customerLogin(loginType, loginId, pwd, null, null, null, isNeedPwd, isNeedCredit, isNeedCard, listener, operates);
    }*/

    /*public void customerLoginByMobile(String mobile, String nationalTelCode, String country, String nation, String pwd, boolean isNeedPwd, boolean isNeedCredit, boolean isNeedCard, YFResponseListener<YFResponse<CustomerLoginResp>> listener) {
        customerLoginByMobile(mobile, nationalTelCode, country, nation, pwd, isNeedPwd, isNeedCredit, isNeedCard, listener, null);
    }

    public void customerLoginByMobile(String mobile, String nationalTelCode, String country, String nation, String pwd, boolean isNeedPwd, boolean isNeedCredit, boolean isNeedCard, YFResponseListener<YFResponse<CustomerLoginResp>> listener, CustomerOperates customerOperates) {
        customerLogin(CustomerLoginType.MOBILE, mobile, pwd, nationalTelCode, country, nation, isNeedPwd, isNeedCredit, isNeedCard, listener, customerOperates);
    }

    public void customerLoginByCustomerId(String customerId, String pwd, boolean isNeedPwd, boolean isNeedCredit, boolean isNeedCard, ResponseListener<MemberLoginVoResp> listener) {
        customerLogin(CustomerLoginType.MEMBER_ID, customerId, pwd, null, null, null, isNeedPwd, isNeedCredit, isNeedCard, listener, null);
    }

    public void customerLoginByFace(String faceCode, boolean isNeedCredit, boolean isNeedCard, ResponseListener<MemberLoginVoResp> listener) {
        customerLogin(CustomerLoginType.FACE_CODE, faceCode, null, null, null, null, false, isNeedCredit, isNeedCard, listener, null);
    }

    public void customerLoginByWeChat(String customerId, boolean isNeedCredit, boolean isNeedCard, ResponseListener<MemberLoginVoResp> listener) {
        customerLogin(CustomerLoginType.MEMBER_ID, customerId, null, null, null, null, false, isNeedCredit, isNeedCard, listener, null);
    }

    public void customerLoginByWeChatCardNum(String weChatCardNum, boolean isNeedCredit, boolean isNeedCard, ResponseListener<MemberLoginVoResp> listener) {
        customerLogin(CustomerLoginType.WECHAT_MEMBERCARD_ID, weChatCardNum, null, null, null, null, false, isNeedCredit, isNeedCard, listener, null);
    }*/
    @Deprecated
    public static void customerLogin(CustomerLoginType loginType,
                                     String loginId,
                                     String pwd,
                                     boolean isNeedCredit,
                                     boolean isNeedCard,
                                     ResponseListener<MemberLoginVoResp> listener) {
        customerLogin(loginType, loginId, pwd, pwd != null, isNeedCredit, isNeedCard, listener);
    }

    @Deprecated
    public static void customerLogin(CustomerLoginType loginType,
                                     String loginId,
                                     String pwd,
                                     boolean isNeedPwd,
                                     boolean isNeedCredit,
                                     boolean isNeedCard,
                                     ResponseListener<MemberLoginVoResp> listener) {
        customerLogin(loginType, loginId, pwd, null, null, null, isNeedPwd, isNeedCredit, isNeedCard, listener, null);
    }

    /**
     * @param loginType       登录方式：0、手机号码；1、微信OPENID；2、座机号；101、微信会员卡卡号；102、顾客customerId; 103、免密会员登陆 104、人脸faceCode
     * @param loginId         手机号码\微信openId\座机号码\顾客ID\动态会员码（customerId:token）\faceCode值
     * @param pwd             密码(大写MD5密文)
     * @param nationalTelCode 电话国际区码(为空默认中国)
     * @param country         国家中文名称(为空默认中国)
     * @param nation          国家英文名称(为空默认中国)
     * @param isNeedPwd       是否需要密码(1:需要，其他不需要)
     * @param isNeedCredit    是否挂账查询（1:需要，其他不需要）
     * @param isNeedCard      是否查询实体卡列表（1:需要，其他不需要）
     * @param listener
     */
    @Deprecated
    public static void customerLogin(CustomerLoginType loginType,
                                     String loginId,
                                     String pwd,
                                     String nationalTelCode,
                                     String country,
                                     String nation,
                                     boolean isNeedPwd,
                                     boolean isNeedCredit,
                                     boolean isNeedCard,
                                     ResponseListener<MemberLoginVoResp> listener, CustomerOperates operates) {
        CustomerLoginReq loginReq = new CustomerLoginReq();
        loginReq.setBrandId(BaseApplication.sInstance.getBrandIdenty());//品牌id
        loginReq.setShopId(BaseApplication.sInstance.getShopIdenty());//门店id
        loginReq.setLoginType(loginType);
        loginReq.setLoginId(loginId);
        loginReq.setPassword(pwd);
        loginReq.setIsNeedPwd(isNeedPwd);
        loginReq.setIsNeedCredit(isNeedCredit);//是否挂账查询
        loginReq.setIsNeedCard(isNeedCard);//是否查询实体卡
        loginReq.nationalTelCode = nationalTelCode;
        loginReq.nation = nation;
        loginReq.country = country;
        if (operates == null) operates = OperatesFactory.create(CustomerOperates.class);
        operates.customerLogin(loginReq, listener);
    }

    public static void customerLogin(CustomerLoginType loginType,
                                     String loginId,
                                     String pwd,
                                     boolean isNeedCredit,
                                     boolean isNeedCard,
                                     YFResponseListener<YFResponse<CustomerLoginResp>> listener) {
        customerLogin(loginType, loginId, pwd, null, null, null, pwd != null, isNeedCredit, isNeedCard, listener);
    }

    public static void customerLogin(CustomerLoginType loginType,
                                     String loginId,
                                     String pwd,
                                     boolean isNeedPwd,
                                     boolean isNeedCredit,
                                     boolean isNeedCard,
                                     YFResponseListener<YFResponse<CustomerLoginResp>> listener) {
        customerLogin(loginType, loginId, pwd, null, null, null, isNeedPwd, isNeedCredit, isNeedCard, listener);
    }

    public static void customerLogin(CustomerLoginType loginType,
                                     String loginId,
                                     String pwd,
                                     String nationalTelCode,
                                     String country,
                                     String nation,
                                     boolean isNeedPwd,
                                     boolean isNeedCredit,
                                     boolean isNeedCard,
                                     YFResponseListener<YFResponse<CustomerLoginResp>> listener) {
        CustomerLoginReq loginReq = new CustomerLoginReq();
        loginReq.setBrandId(BaseApplication.sInstance.getBrandIdenty());//品牌id
        loginReq.setShopId(BaseApplication.sInstance.getShopIdenty());//门店id
        loginReq.setLoginType(loginType);
        loginReq.setLoginId(loginId);
        loginReq.setPassword(pwd);
        loginReq.setIsNeedPwd(isNeedPwd);
        loginReq.setIsNeedCredit(isNeedCredit);//是否挂账查询
        loginReq.setIsNeedCard(isNeedCard);//是否查询实体卡
        loginReq.nationalTelCode = nationalTelCode;
        loginReq.nation = nation;
        loginReq.country = country;
        CustomerOperates operates = OperatesFactory.create(CustomerOperates.class);
        operates.customerLogin(loginReq, listener);
    }

    /**
     * 获取优惠卷
     *
     * @param customerId 会员id
     * @param curPage    当前页
     * @param size       每页显示的长度
     * @param listener   回调监听
     */
    public void getCustomerCoupons(Long customerId, int curPage, int size, ResponseListener<MemberCouponsVoResp> listener) {
        MemberCouponsReq couponsReq = new MemberCouponsReq();
        couponsReq.setClientType("pos");
        couponsReq.setBrandId(BaseApplication.sInstance.getBrandIdenty());//品牌id
        couponsReq.setCommercialId(BaseApplication.sInstance.getShopIdenty());//门店id
        couponsReq.setCustomerId(customerId);
        couponsReq.setPageNo(curPage);
        couponsReq.setPageSize(size);

        CustomerOperates customerOperate = OperatesFactory.create(CustomerOperates.class);
        customerOperate.getCustomerCoupons(couponsReq, listener);
    }


    public void getCustomerCoupons(Long customerId, int curPage, int size, YFResponseListener<YFResponseList<CustomerCouponResp>> listener) {
        MemberCouponsReq couponsReq = new MemberCouponsReq();
        couponsReq.setClientType("pos");
        couponsReq.setBrandId(BaseApplication.sInstance.getBrandIdenty());//品牌id
        couponsReq.setCommercialId(BaseApplication.sInstance.getShopIdenty());//门店id
        couponsReq.setCustomerId(customerId);
        couponsReq.setPageNo(curPage);
        couponsReq.setPageSize(size);

        CustomerOperates customerOperate = OperatesFactory.create(CustomerOperates.class);
        customerOperate.getCustomerCoupons(couponsReq, listener);
    }

    /**
     * 获取订单中的会员或者实体卡
     *
     * @param listCustomer
     * @return
     */
    public TradeCustomer getValidMemberOrCardCustomer(List<TradeCustomer> listCustomer) {
        if (Utils.isNotEmpty(listCustomer)) {
            for (TradeCustomer tradeCustomer : listCustomer) {
                if (tradeCustomer.getStatusFlag() == StatusFlag.VALID
                        && (tradeCustomer.getCustomerType() == CustomerType.MEMBER
                        || tradeCustomer.getCustomerType() == CustomerType.CARD)) {
                    return tradeCustomer;
                }
            }
        }
        return null;
    }

    /**
     * 状态名称
     *
     * @param cardStatus
     * @return
     */
    public String getStatusName(CardStatus cardStatus) {
        return CustomerUtil.getStatusName(cardStatus);
    }

    //add v8.2 添加会员权益卡权益开关 start
    private boolean mCurrentCardIsPriceLimit = false;

    public boolean isOpenPriceLimit(CustomerType type) {
        if (type == CustomerType.CARD) { //权益卡登录
            return mCurrentCardIsPriceLimit;
        } else if (type == CustomerType.MEMBER) {//虚拟会员登录
            return MarketRuleCache.isOpenCustomerPriceLimit();
        }
        return false;
    }

    // 卡登录时设置该参数
    public void setCurrentCardIsPriceLimit(boolean currentCardIsPriceLimit) {
        this.mCurrentCardIsPriceLimit = currentCardIsPriceLimit;
    }

    //add v8.2 添加会员权益卡权益开关 end

    public CustomerScoreRule getIntegerRule() {
//        mCustomerScoreRule=new CustomerScoreRule();
//        mCustomerScoreRule.setConvertValue(10);
//        mCustomerScoreRule.setId(1L);
//        return mCustomerScoreRule;

        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            if (mCustomerScoreRule != null) {
                return mCustomerScoreRule;
            }

            Dao<CustomerScoreRule, Long> customerScoreRule = helper.getDao(CustomerScoreRule.class);

            mCustomerScoreRule = customerScoreRule.queryBuilder().where().eq(CustomerScoreRule.$.statusFlag, StatusFlag.VALID).and().eq(CustomerScoreRule.$.type, 1).queryForFirst();

            return mCustomerScoreRule;
        } catch (Exception ex) {

        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return null;
    }
}
