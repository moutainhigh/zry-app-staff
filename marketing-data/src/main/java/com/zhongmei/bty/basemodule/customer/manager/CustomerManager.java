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
import com.zhongmei.yunfu.db.entity.discount.CustomerScoreRuleVo;
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

import java.math.BigDecimal;
import java.util.List;

import de.greenrobot.event.EventBus;

public class CustomerManager {
    private static final String TAG = CustomerManager.class.getSimpleName();

    public static final int NEED_PSWD = 1;

    public static final int NOT_NEED_PSWD = 2;

    private CustomerResp mLoginCustomer = null;
    private CustomerResp mDinnerLoginCustomer = null;
    private CustomerResp mSeparatLoginCustomer = null;
    private static CustomerManager sCustomerManager;

    private String currentPhone;

    public List<String> getAccounts() {
        return mAccounts;
    }

    public void setAccounts(List<String> mAccounts) {
        this.mAccounts = mAccounts;
    }

    private List<String> mAccounts;

        private int loginSource = 0;

    private CustomerScoreRuleVo mCustomerScoreRule;


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

    private CustomerResp currentCustomer = null;
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

        public CustomerResp getDinnerLoginCustomer() {
        return mDinnerLoginCustomer;
    }

        public void setDinnerLoginCustomer(CustomerResp dinnerLoginCustomer) {
        this.mDinnerLoginCustomer = dinnerLoginCustomer;
    }

    public CustomerResp getSeparateLoginCustomer() {
        return mSeparatLoginCustomer;
    }

    public void setSeparateLoginCustomer(CustomerResp separatLoginCustomer) {
        this.mSeparatLoginCustomer = separatLoginCustomer;
    }














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
        loginReq.setBrandId(BaseApplication.sInstance.getBrandIdenty());        loginReq.setShopId(BaseApplication.sInstance.getShopIdenty());        loginReq.setLoginType(loginType);
        loginReq.setLoginId(loginId);
        loginReq.setPassword(pwd);
        loginReq.setIsNeedPwd(isNeedPwd);
        loginReq.setIsNeedCredit(isNeedCredit);        loginReq.setIsNeedCard(isNeedCard);        loginReq.nationalTelCode = nationalTelCode;
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
        loginReq.setBrandId(BaseApplication.sInstance.getBrandIdenty());        loginReq.setShopId(BaseApplication.sInstance.getShopIdenty());        loginReq.setLoginType(loginType);
        loginReq.setLoginId(loginId);
        loginReq.setPassword(pwd);
        loginReq.setIsNeedPwd(isNeedPwd);
        loginReq.setIsNeedCredit(isNeedCredit);        loginReq.setIsNeedCard(isNeedCard);        loginReq.nationalTelCode = nationalTelCode;
        loginReq.nation = nation;
        loginReq.country = country;
        CustomerOperates operates = OperatesFactory.create(CustomerOperates.class);
        operates.customerLogin(loginReq, listener);
    }


    public void getCustomerCoupons(Long customerId, int curPage, int size, ResponseListener<MemberCouponsVoResp> listener) {
        MemberCouponsReq couponsReq = new MemberCouponsReq();
        couponsReq.setClientType("pos");
        couponsReq.setBrandId(BaseApplication.sInstance.getBrandIdenty());        couponsReq.setCommercialId(BaseApplication.sInstance.getShopIdenty());        couponsReq.setCustomerId(customerId);
        couponsReq.setPageNo(curPage);
        couponsReq.setPageSize(size);

        CustomerOperates customerOperate = OperatesFactory.create(CustomerOperates.class);
        customerOperate.getCustomerCoupons(couponsReq, listener);
    }


    public void getCustomerCoupons(Long customerId, int curPage, int size, YFResponseListener<YFResponseList<CustomerCouponResp>> listener) {
        MemberCouponsReq couponsReq = new MemberCouponsReq();
        couponsReq.setClientType("pos");
        couponsReq.setBrandId(BaseApplication.sInstance.getBrandIdenty());        couponsReq.setCommercialId(BaseApplication.sInstance.getShopIdenty());        couponsReq.setCustomerId(customerId);
        couponsReq.setPageNo(curPage);
        couponsReq.setPageSize(size);

        CustomerOperates customerOperate = OperatesFactory.create(CustomerOperates.class);
        customerOperate.getCustomerCoupons(couponsReq, listener);
    }


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


    public String getStatusName(CardStatus cardStatus) {
        return CustomerUtil.getStatusName(cardStatus);
    }

        private boolean mCurrentCardIsPriceLimit = false;

    public boolean isOpenPriceLimit(CustomerType type) {
        if (type == CustomerType.CARD) {             return mCurrentCardIsPriceLimit;
        } else if (type == CustomerType.MEMBER) {            return MarketRuleCache.isOpenCustomerPriceLimit();
        }
        return false;
    }

        public void setCurrentCardIsPriceLimit(boolean currentCardIsPriceLimit) {
        this.mCurrentCardIsPriceLimit = currentCardIsPriceLimit;
    }


    public CustomerScoreRuleVo getIntegerRule() {

        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            if (mCustomerScoreRule != null) {
                return mCustomerScoreRule;
            }

            Dao<CustomerScoreRule, Long> customerScoreRule = helper.getDao(CustomerScoreRule.class);

            CustomerScoreRule  rule = customerScoreRule.queryBuilder().where().eq(CustomerScoreRule.$.statusFlag, StatusFlag.VALID).and().eq(CustomerScoreRule.$.type, 2).queryForFirst();

            if(rule!=null){
                mCustomerScoreRule=new CustomerScoreRuleVo();
                mCustomerScoreRule.setId(rule.getId());
                mCustomerScoreRule.setType(rule.getType());
                mCustomerScoreRule.setConvertValue(rule.getConvertValue());
                mCustomerScoreRule.setStatusFlag(rule.getStatusFlag());
            }

            CustomerScoreRule  LimitRule = customerScoreRule.queryBuilder().where().eq(CustomerScoreRule.$.statusFlag, StatusFlag.VALID).and().eq(CustomerScoreRule.$.type, 3).queryForFirst();

            if(LimitRule!=null && LimitRule.getConvertValue()!=null && mCustomerScoreRule!=null){
                mCustomerScoreRule.setMaxUserInteger(new BigDecimal(LimitRule.getConvertValue()));
            }

            return mCustomerScoreRule;
        } catch (Exception ex) {

        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return null;
    }


    public CustomerScoreRule getIntegerLimitRule() {

        DatabaseHelper helper = DBHelperManager.getHelper();
        try{

            Dao<CustomerScoreRule, Long> customerScoreRule = helper.getDao(CustomerScoreRule.class);


            CustomerScoreRule  LimitRule = customerScoreRule.queryBuilder().where().eq(CustomerScoreRule.$.statusFlag, StatusFlag.VALID).and().eq(CustomerScoreRule.$.type, 3).queryForFirst();


            return LimitRule;
        } catch (Exception ex) {

        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return null;
    }
}
