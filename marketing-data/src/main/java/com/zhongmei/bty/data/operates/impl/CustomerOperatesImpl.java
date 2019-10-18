package com.zhongmei.bty.data.operates.impl;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.zhongmei.bty.basemodule.commonbusiness.utils.ServerAddressUtil;
import com.zhongmei.bty.basemodule.customer.bean.CustomerMobileVo;
import com.zhongmei.bty.basemodule.customer.bean.CustomerOrderBean;
import com.zhongmei.bty.basemodule.customer.message.BindCustomerFaceCodeReq;
import com.zhongmei.bty.basemodule.customer.message.BindCustomerFaceCodeResp;
import com.zhongmei.bty.basemodule.customer.message.CustomTimesReq;
import com.zhongmei.bty.basemodule.customer.message.CustomTimesResp;
import com.zhongmei.bty.basemodule.customer.message.CustomerCardInfoResp;
import com.zhongmei.bty.basemodule.customer.message.CustomerDirectCouponListV2Resp;
import com.zhongmei.bty.basemodule.customer.message.CustomerEditReq;
import com.zhongmei.bty.basemodule.customer.message.CustomerEditResp;
import com.zhongmei.bty.basemodule.customer.message.CustomerInfoReq;
import com.zhongmei.bty.basemodule.customer.message.CustomerInfoResp;
import com.zhongmei.bty.basemodule.customer.message.CustomerListVoResp;
import com.zhongmei.bty.basemodule.customer.message.CustomerMemberStoreValueReq;
import com.zhongmei.bty.basemodule.customer.message.CustomerMemberStoreValueResp;
import com.zhongmei.bty.basemodule.customer.message.CustomerMemberStoreValueRevokeReq;
import com.zhongmei.bty.basemodule.customer.message.CustomerMemberStoreValueRevokeResp;
import com.zhongmei.bty.basemodule.customer.message.MemberAddressResp;
import com.zhongmei.bty.basemodule.customer.message.MemberCheckCodeReq;
import com.zhongmei.bty.basemodule.customer.message.MemberCheckCodeResp;
import com.zhongmei.bty.basemodule.customer.message.MemberCouponsReq;
import com.zhongmei.bty.basemodule.customer.message.MemberCouponsVoResp;
import com.zhongmei.bty.basemodule.customer.message.MemberCreateResp;
import com.zhongmei.bty.basemodule.customer.message.MemberCreditListReq;
import com.zhongmei.bty.basemodule.customer.message.MemberCreditListResp;
import com.zhongmei.bty.basemodule.customer.message.MemberIntegralInfoReq;
import com.zhongmei.bty.basemodule.customer.message.MemberIntegralInfoResp;
import com.zhongmei.bty.basemodule.customer.message.MemberIntegralModificationReq;
import com.zhongmei.bty.basemodule.customer.message.MemberIntegralModificationResp;
import com.zhongmei.bty.basemodule.customer.message.MemberLoginVoResp;
import com.zhongmei.bty.basemodule.customer.message.MemberModifyLevelReq;
import com.zhongmei.bty.basemodule.customer.message.MemberModifyMobileReq;
import com.zhongmei.bty.basemodule.customer.message.MemberRechargeReq;
import com.zhongmei.bty.basemodule.customer.message.MemberRechargeResp;
import com.zhongmei.bty.basemodule.customer.message.MemberResetPswdReq;
import com.zhongmei.bty.basemodule.customer.message.MemberResetPswdResp;
import com.zhongmei.bty.basemodule.customer.message.MemberUpgradeReq;
import com.zhongmei.bty.basemodule.customer.message.MemberUpgradeResp;
import com.zhongmei.bty.basemodule.customer.message.MemberValidateCheckCodeReq;
import com.zhongmei.bty.basemodule.customer.message.MemberValidateCheckCodeResp;
import com.zhongmei.bty.basemodule.customer.message.MemberValuecardHistoryResp;
import com.zhongmei.bty.basemodule.customer.message.MemberVerifyPwdReq;
import com.zhongmei.bty.basemodule.customer.message.NewMemberIntegralInfoResp;
import com.zhongmei.bty.basemodule.customer.message.PaymentAndMemberReq;
import com.zhongmei.bty.basemodule.customer.message.PaymentAndMemberResp;
import com.zhongmei.bty.basemodule.customer.operates.CustomerOperates;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.AnonymousCardStorePayModeResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.AnonymousCardTradeReq;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.AnonymousCardTradeResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardAccountReq;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardAccountResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardActivateReq;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardActivateResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardActiveReqV2;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardActiveRespV2;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardBaseInfoReq;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardBaseInfoResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardIntegralInfoReq;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardIntegralInfoResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardLoginReq;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardLoginResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardRangeSearchReq;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardRangeSearchResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardRechargeReq;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardRechargeResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardSingleSearchByTransReq;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardSingleSearchReq;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardSingleSearchResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CustomerCardStoreValueReq;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CustomerCardStoreValueResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CustomerSaleCardInvalidReq;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CustomerSaleCardInvalidResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CustomerSellcardDetailReq;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CustomerSellcardDetailResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CustomerSellcardReq;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CustomerSellcardResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.EntityCardChangeDetailResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.JCReturnAnonymousCardReq;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.JCReturnAnonymousCardResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.MemberCardReq;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.MemberCardResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.MemberCardsReq;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.MemberCardsResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.NewCardLoginResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.ReturnAnonymousCardReq;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.ReturnAnonymousCardResp;
import com.zhongmei.bty.basemodule.erp.bean.ErpCurrency;
import com.zhongmei.bty.basemodule.trade.bean.Reason;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.bty.customer.util.JCAddressUtil;
import com.zhongmei.bty.data.operates.message.content.CustomerCardStoreValueRevokeReq;
import com.zhongmei.bty.data.operates.message.content.CustomerRequest;
import com.zhongmei.bty.data.operates.message.content.MemberCreateOldCustomerV2Req;
import com.zhongmei.yunfu.bean.YFResponse;
import com.zhongmei.yunfu.bean.YFResponseList;
import com.zhongmei.yunfu.bean.req.CustomerCardTimeStoreReq;
import com.zhongmei.yunfu.bean.req.CustomerCardTimeStoreResp;
import com.zhongmei.yunfu.bean.req.CustomerCouponResp;
import com.zhongmei.yunfu.bean.req.CustomerCreateReq;
import com.zhongmei.yunfu.bean.req.CustomerCreateResp;
import com.zhongmei.yunfu.bean.req.CustomerIntegralReq;
import com.zhongmei.yunfu.bean.req.CustomerIntegralResp;
import com.zhongmei.yunfu.bean.req.CustomerListReq;
import com.zhongmei.yunfu.bean.req.CustomerListResp;
import com.zhongmei.yunfu.bean.req.CustomerLoginReq;
import com.zhongmei.yunfu.bean.req.CustomerLoginResp;
import com.zhongmei.yunfu.bean.req.CustomerMemberStoreReq;
import com.zhongmei.yunfu.bean.req.CustomerMemberStoreResp;
import com.zhongmei.yunfu.bean.req.CustomerReq;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.bean.req.CustomerStoredBalanceReq;
import com.zhongmei.yunfu.bean.req.CustomerStoredBalanceResp;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.http.CalmNetWorkRequest;
import com.zhongmei.yunfu.http.JFRequest;
import com.zhongmei.yunfu.http.OpsRequest;
import com.zhongmei.yunfu.monitor.CalmResponseListener;
import com.zhongmei.yunfu.net.RequestManager;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.resp.YFResponseListener;
import com.zhongmei.yunfu.resp.data.LoyaltyMindTransferResp;
import com.zhongmei.yunfu.resp.data.LoyaltyTransferResp;
import com.zhongmei.yunfu.resp.data.TransferReq;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.Callable;


@SuppressLint("SimpleDateFormat")
public class CustomerOperatesImpl extends AbstractOpeartesImpl implements CustomerOperates {

    private static final String TAG = CustomerOperatesImpl.class.getSimpleName();

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public CustomerOperatesImpl(ImplContext context) {
        super(context);
    }

    @Override
    public void editCustomer(CustomerResp customerNew, ResponseListener<CustomerEditResp> listener) throws JSONException {

    }

    @Override
    public void login(CustomerLoginReq loginReq, ResponseListener<CustomerLoginResp> listener) {

    }

    @Override
    public void customerLogin(CustomerLoginReq loginReq, ResponseListener<MemberLoginVoResp> listener) {

    }

    @Override
    public void customerLogin(CustomerLoginReq loginReq, YFResponseListener<YFResponse<CustomerLoginResp>> listener) {
        String url = ServerAddressUtil.getInstance().customerLogin();
        JFRequest.Executor executor = JFRequest.create(url);
        executor.requestValue(loginReq)
                .execute(listener, "customerLoginNew");
    }

    @Override
    public void getCustomerCoupons(MemberCouponsReq couponsReq, ResponseListener<MemberCouponsVoResp> listener) {

    }

    @Override
    public void getCustomerCoupons(MemberCouponsReq couponsReq, YFResponseListener<YFResponseList<CustomerCouponResp>> listener) {
        String url = ServerAddressUtil.getInstance().memberCoupons();
        JFRequest.Executor executor = JFRequest.create(url);
        executor.requestValue(couponsReq).execute(listener, "customerCoupons");
    }

    @Override
    public void getCustomerCards(MemberCardsReq cardsReq, ResponseListener<LoyaltyTransferResp<MemberCardsResp>> listener) {

    }


    @Override
    public void memberRecharge(MemberRechargeReq rechargeReq, ResponseListener<MemberRechargeResp> listener) {

    }

    @Override
    public void cardRecharge(CardRechargeReq rechargeReq, ResponseListener<CardRechargeResp> listener) {

    }

    @Override
    public void findCustomerInfo(CustomerInfoReq couponinfoReq, ResponseListener<CustomerInfoResp> listener) {

    }

    @Override
    public void getCheckCode(MemberCheckCodeReq checkCodeReq, ResponseListener<MemberCheckCodeResp> listener) {

    }

    @Override
    public void validateCheckCode(MemberValidateCheckCodeReq validateCheckCodeReq,
                                  ResponseListener<MemberValidateCheckCodeResp> listener) {

    }

    @Override
    public void modifyPassword(MemberResetPswdReq resetPswdReq, ResponseListener<MemberResetPswdResp> listener) {

    }

    @Override
    public void getValuecardHistory(CustomerStoredBalanceReq req, ResponseListener<MemberValuecardHistoryResp> listener) {
        String url = ServerAddressUtil.getInstance().memberValuecardHistory();
        OpsRequest.Executor<CustomerStoredBalanceReq, MemberValuecardHistoryResp> executor =
                OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(MemberValuecardHistoryResp.class)
                .execute(listener, "memberValuecardHistory");
    }

    @Override
    public void getValuecardHistory(CustomerStoredBalanceReq req, YFResponseListener<YFResponseList<CustomerStoredBalanceResp>> listener) {
        String url = ServerAddressUtil.getInstance().memberValuecardHistory();
        JFRequest.Executor executor = JFRequest.create(url);
        executor.requestValue(req)
                .execute(listener, "memberValuecardHistory");
    }

    @Override
    public void upgradeCustomer(MemberUpgradeReq req, CustomerResp customer, ResponseListener<MemberUpgradeResp> listener) {

    }

    @Override
    public void createCustomer(CustomerResp customer, String uniqueCode, ResponseListener<MemberCreateResp> listener) {

    }

    @Override
    public void queryMemberByUniqueCode(String uniqueCode, ResponseListener<MemberCreateResp> listener) {

    }

    @Override
    public void cardLogin(String cardNo, ResponseListener<CardLoginResp> listener) {

    }

    private CardLoginReq toCardLoginReq(String cardNo) {
        CardLoginReq req = new CardLoginReq(cardNo);
        req.setSource(1);        AuthUser user = Session.getAuthUser();
        if (user != null) {
            req.setUserId(user.getId());
        }
        return req;
    }

    @Override
    public void cardLoginNew(String cardNum, ResponseListener<LoyaltyMindTransferResp<NewCardLoginResp>> listener) {


                    }

    @Override
    public void getMemberCards(Long customerId, String mobile, ResponseListener<MemberCardResp> listener) {

    }


    @Override
    public void cancelGetMemberCards() {
        RequestManager.cancelAll("memberCreate");
    }

    @Override
    public void rangeSearchCards(CardRangeSearchReq req, ResponseListener<CardRangeSearchResp> listener) {


    }

    @Override
    public void singleSearchCards(CardSingleSearchReq req, ResponseListener<CardRangeSearchResp> listener) {

    }

    @Override
    public void singleSearchCardsByTrans(CardSingleSearchByTransReq req, ResponseListener<CardSingleSearchResp> listener) {

    }

    @Override
    public void activateCards(CardActivateReq req, ResponseListener<CardActivateResp> listener) {


    }

    @SuppressLint("SimpleDateFormat")
    private MemberCreateReq toMemberCreateReq(CustomerResp customer, String uniqueCode) {
        MemberCreateReq req = new MemberCreateReq();
        if (uniqueCode != null) {
            req.setUniqueCode(uniqueCode);
        }
        req.setLogId(SystemUtils.genOnlyIdentifier());

        req.setSex(customer.sex + "");
        req.setAddress(customer.address);
        req.setBirthday(customer.birthday);
        req.setConsumePwd(customer.password);
        req.setEnvironmentHobby(customer.hobby);
        req.setGroupId(customer.groupId);
        req.setInvoiceTitle(customer.invoice);
        req.setMemo(customer.memo);
        req.setMobile(customer.mobile);
        req.setName(customer.customerName);
        req.setNation(customer.nation);
        req.setCountry(customer.country);
        req.setNationalTelCode(customer.nationalTelCode);
        req.setUserId(Session.getAuthUser().getId());
        if (TextUtils.isEmpty(customer.faceCode)) {
            req.setFaceCode(customer.faceCode);
        }
        return req;
    }

    @Override
    public void getValueIntegralHistory(MemberIntegralInfoReq req, ResponseListener<MemberIntegralInfoResp> listener) {


    }


    @Override
    public void getCardIntegralHistory(CardIntegralInfoReq req, ResponseListener<CardIntegralInfoResp> listener) {

    }


    static class MemberQueueReq {

        private String uniqueCode;

        public String getUniqueCode() {
            return uniqueCode;
        }

        public void setUniqueCode(String uniqueCode) {
            this.uniqueCode = uniqueCode;
        }
    }


    class MemberAddressReq {

        private Long memberID;

        public Long getMemberID() {
            return memberID;
        }

        public void setMemberID(Long memberID) {
            this.memberID = memberID;
        }
    }


    static class MemberCreateReq {

        private String uniqueCode;

        private String mobile;

        private Long userId;

                private String birthday;

        private String sex;

        private String logId;

        private String consumePwd;

        private String name;

        private String environmentHobby;

        private String memo;

        private String invoiceTitle;

        private String address;

        private Long groupId;


        private String nation;

        private String country;

        private String nationalTelCode;

        private String faceCode;

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getLogId() {
            return logId;
        }

        public void setLogId(String logId) {
            this.logId = logId;
        }

        public String getConsumePwd() {
            return consumePwd;
        }

        public void setConsumePwd(String consumePwd) {
            this.consumePwd = consumePwd;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEnvironmentHobby() {
            return environmentHobby;
        }

        public void setEnvironmentHobby(String environmentHobby) {
            this.environmentHobby = environmentHobby;
        }

        public String getMemo() {
            return memo;
        }

        public void setMemo(String memo) {
            this.memo = memo;
        }

        public String getInvoiceTitle() {
            return invoiceTitle;
        }

        public void setInvoiceTitle(String invoiceTitle) {
            this.invoiceTitle = invoiceTitle;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Long getGroupId() {
            return groupId;
        }

        public void setGroupId(Long groupId) {
            this.groupId = groupId;
        }

        public String getUniqueCode() {
            return uniqueCode;
        }

        public void setUniqueCode(String uniqueCode) {
            this.uniqueCode = uniqueCode;
        }

        public String getNation() {
            return nation;
        }

        public void setNation(String nation) {
            this.nation = nation;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getNationalTelCode() {
            return nationalTelCode;
        }

        public void setNationalTelCode(String nationalTelCode) {
            this.nationalTelCode = nationalTelCode;
        }

        public String getFaceCode() {
            return faceCode;
        }

        public void setFaceCode(String faceCode) {
            this.faceCode = faceCode;
        }
    }


    @Override
    public void getCustomerSellCardOrders(CustomerSellcardReq sellcardReq,
                                          ResponseListener<List<CustomerSellcardResp>> listener) {


    }

    @Override
    public void getEntityCardChangeOrders(CustomerSellcardReq entityCardChangeReq, ResponseListener<List<CustomerSellcardResp>> listener
    ) {

    }


    @Override
    public void getCustomerSellCardDetailInfo(CustomerSellcardDetailReq sellcardReq,
                                              ResponseListener<CustomerSellcardDetailResp> listener) {


    }

    @Override
    public void getEntityCardChangeDetailInfo(Long tradeId, ResponseListener<EntityCardChangeDetailResp> listener
    ) {

    }

    private CustomerSellcardDetailReq toEntityCardChangeDetailReq(Long tradeId) {
        CustomerSellcardDetailReq req = new CustomerSellcardDetailReq();
        if (Session.getAuthUser() != null) {
            req.setUserId(Session.getAuthUser().getId());
        }
        req.setTradeId(tradeId);
        return req;
    }

    @Override
    public void getAnonymousEntityCardSellDetailInfo(Long tradeId, ResponseListener<CustomerSellcardDetailResp> listener) {

    }


    @Override
    public void getCardStoreValueList(CustomerCardStoreValueReq cardStoreValueReq,
                                      ResponseListener<CustomerCardStoreValueResp> listener) {

    }

    @Override
    public void getEntityCardStoreHistory(CustomerCardTimeStoreReq cardStoreValueReq, ResponseListener<List<CustomerCardTimeStoreResp>> listener) {
        String url = ServerAddressUtil.getInstance().getEntityCardStoreHistory();
        OpsRequest.Executor<CustomerCardTimeStoreReq, List<CustomerCardTimeStoreResp>> executor = OpsRequest.Executor.create(url);
        executor.requestValue(cardStoreValueReq)
                .responseType(OpsRequest.getListContentResponseType(CustomerCardTimeStoreResp.class))
                .execute(listener, "getEntityCardStoreHistory");
    }

    @Override
    public void getEntityCardStoreHistory(CustomerCardTimeStoreReq cardStoreValueReq, YFResponseListener<YFResponseList<CustomerCardTimeStoreResp>> listener) {
        String url = ServerAddressUtil.getInstance().getEntityCardStoreHistory();
        JFRequest.Executor executor = JFRequest.create(url);
        executor.requestValue(cardStoreValueReq)
                .execute(listener, "getEntityCardStoreHistory");
    }


    @Override
    public void queryPaymentAndMember(PaymentAndMemberReq req,
                                      ResponseListener<PaymentAndMemberResp> listener) {

    }

    @Override
    public void queryPayment(String paymentUuid, ResponseListener<PaymentAndMemberResp> listener) {

    }


    @Override
    public void getMemberStoreValueList(CustomerMemberStoreValueReq req,
                                        ResponseListener<CustomerMemberStoreValueResp> listener) {

    }

    @Override
    public void getMemberStoreHistory(CustomerMemberStoreReq req,
                                      ResponseListener<List<CustomerMemberStoreResp>> listener) {
        String url = ServerAddressUtil.getInstance().getMemberStoreHistory();
        OpsRequest.Executor<CustomerMemberStoreReq, List<CustomerMemberStoreResp>> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseType(OpsRequest.getListContentResponseType(CustomerMemberStoreResp.class))
                .execute(listener, "getMemberStoreHistory");
    }

    @Override
    public void getMemberStoreHistory(CustomerMemberStoreReq req, YFResponseListener<YFResponseList<CustomerMemberStoreResp>> listener) {
        String url = ServerAddressUtil.getInstance().getMemberStoreHistory();
        JFRequest.Executor executor = JFRequest.create(url);
        executor.requestValue(req)
                .execute(listener, "getMemberStoreHistory");
    }


    @Override
    public void saleCardRecordInvaliate(CustomerSaleCardInvalidReq req,
                                        ResponseListener<CustomerSaleCardInvalidResp> listener) {

    }


    @Override
    public void cardStoreValueRevokeReq(CustomerOrderBean orderBean, Reason reason, ResponseListener<Object> listener) {


    }

    private CustomerCardStoreValueRevokeReq toCustomerCardStoreValueRevokeReq(CustomerOrderBean orderBean, Reason reason) {
        CustomerCardStoreValueRevokeReq req = new CustomerCardStoreValueRevokeReq();
        if (orderBean != null) {
            req.setTradeId(orderBean.getTradeId());
            req.setServerUpdateTime(orderBean.getServerUpdateTime());
            req.setClientCreateTime(orderBean.getClientCreateTime());
            req.setType(orderBean.getCardKindId());
            req.setCustomerId(orderBean.getCustomerId());
            req.setPaymentUuid(orderBean.getPaymentUuid());
            req.setEntityCardNo(orderBean.getCardNo());
            req.setAddValue(orderBean.getAddValue());
            req.setSendValue(orderBean.getSendValue());
            req.setIsCash(orderBean.getAddValueType());
            req.setStoreId(orderBean.getStoreId());
            req.setBizDate(orderBean.getBizDate());
        }

        if (reason != null) {
            req.setReasonId(reason.getId());
            req.setReasonContent(reason.getContent());
        }

        AuthUser user = Session.getAuthUser();
        if (user != null) {
            req.setUpdatorId(user.getId());
            req.setUpdatorName(user.getName());
        }

        return req;
    }


    @Override
    public void memberStoreValueRevokeReq(CustomerMemberStoreValueRevokeReq req,
                                          ResponseListener<CustomerMemberStoreValueRevokeResp> listener) {


    }

    private static class CustomerMemberStoreRevokeRespProcessor extends OpsRequest.SaveDatabaseResponseProcessor<CustomerMemberStoreValueRevokeResp> {
        @Override
        protected Callable<Void> getCallable(final DatabaseHelper helper, final CustomerMemberStoreValueRevokeResp resp) throws Exception {
            return new Callable<Void>() {

                @Override
                public Void call()
                        throws Exception {
                    DBHelperManager.saveEntities(helper, Trade.class, resp.getRelDatas().getTrades());
                    DBHelperManager.saveEntities(helper, Payment.class, resp.getRelDatas().getPayments());
                    DBHelperManager.saveEntities(helper, PaymentItem.class, resp.getRelDatas().getPaymentItems());
                    return null;
                }
            };
        }
    }


    @Override
    public void anonymousEntityCardSaleRecordInvalid(Long tradeId, ResponseListener<CustomerSaleCardInvalidResp> listener) {

    }

    @Override
    public void getAnoymousCardTradeInfo(AnonymousCardTradeReq req, ResponseListener<AnonymousCardTradeResp> listener) {

    }

    @Override
    public void queryAnoymousCardStorePayMode(@NonNull String cardNo, ResponseListener<AnonymousCardStorePayModeResp> listener) {

    }

    private AnonymousCardTradeReq toAnonymousCardTradeReq(String cardNo) {
        AnonymousCardTradeReq anonymousCardTradeReq = new AnonymousCardTradeReq();
        anonymousCardTradeReq.setCardNo(cardNo);

        return anonymousCardTradeReq;
    }

    @Override
    public void returnAnoymousCard(ReturnAnonymousCardReq req, ResponseListener<ReturnAnonymousCardResp> listener) {

    }

    @Override
    public void returnJCAnoymousCard(JCReturnAnonymousCardReq req, ResponseListener<JCReturnAnonymousCardResp> listener) {
        String url = JCAddressUtil.getJcRefundCardUrl();
        OpsRequest.Executor<JCReturnAnonymousCardReq, JCReturnAnonymousCardResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(JCReturnAnonymousCardResp.class)
                .execute(listener, "returnJCAnonymousCard");
    }

    @Override
    public void getCustomerById(Long customerId, boolean isNeedCredit, ResponseListener<com.zhongmei.bty.basemodule.customer.message.CustomerResp> listener) {

    }

    @Override
    public void getCustomerById(Long customerId, boolean isNeedCredit, YFResponseListener<YFResponse<CustomerResp>> listener) {
        CustomerReq req = new CustomerReq();
        req.brandId = BaseApplication.getInstance().getBrandIdenty();        req.commercialId = BaseApplication.getInstance().getShopIdenty();
        req.clientType = "pos";
        req.customerId = customerId;
        if (isNeedCredit) {
            req.isNeedCredit = 1;
        }
        String url = ServerAddressUtil.getInstance().getCustomerDetailById();
        JFRequest.Executor executor = JFRequest.create(url);
        executor.requestValue(req)
                .execute(listener, "getCustomerById");
    }

    @Override
    public void getCustomerByCardNo(String cardNo, boolean isNeedCredit, YFResponseListener<YFResponse<CustomerResp>> listener) {

    }

    @Override
    public void customerBindCard(String cardNo, Long customerId, YFResponseListener<YFResponse<CustomerCreateResp>> listener) {
        CustomerCreateReq req = toBindCardReq(customerId,cardNo);
        String url = ServerAddressUtil.getInstance().customerBindCard();
        JFRequest.Executor executor = JFRequest.create(url);
        executor.requestValue(req)
                .execute(listener, "saveCustomer");
    }

    @Override
    public void cancelGetCustomerById() {
        RequestManager.cancelAll("getCustomerById");
    }

    @Override
    public void findCustomerByPhone(String phone, ResponseListener<CustomerMobileVo> listener) {
        getCustomerByType(CustomerRequest.PHONE, phone, listener);
    }

    @Override
    public void getCustomerByType(Long loginType, String loginId, ResponseListener<CustomerMobileVo> listener) {

    }


    @Override
    public void queryCustomerListByCondition(CustomerListReq req, ResponseListener<CustomerListVoResp> listener) {

    }

    @Override
    public void queryCustomerListByCondition(CustomerListReq req, YFResponseListener<YFResponseList<CustomerListResp>> listener) {
        String url = ServerAddressUtil.getInstance().queryCustomerList();
        JFRequest.Executor executor = JFRequest.create(url);
        executor.requestValue(req)
                .execute(listener, "queryCustomerListByCondition");
    }

    @Override
    public void cancelQueryCustomerListByCondition() {
        RequestManager.cancelAll("queryCustomerListByCondition");
    }

    @Override
    public void queryMemberAddressById(Long memberId, ResponseListener<MemberAddressResp> listener) {

    }

    @Override
    public void getCardBaseInfo(String cardNum, ResponseListener<CardBaseInfoResp> listener) {

    }

    @Override
    public void getCardAccount(String cardNum, ResponseListener<CardAccountResp> listener) {

    }

    @Override
    public void getCardInfoByNum(CardSingleSearchByTransReq req, ResponseListener<CustomerCardInfoResp> listener) {

    }

    @Override
    public void memberIntegralModification(Long customerId, int integral, Integer operateType, ResponseListener<MemberIntegralModificationResp> listener) {

    }

    @Override
    public void newMemberIntegralInfo(Long customerId, Integer currentPage, Long lastId, ResponseListener<NewMemberIntegralInfoResp> listener) {

    }

    @Override
    public void newMemberIntegralInfo(Long customerId, Integer currentPage, Long lastId, YFResponseListener<YFResponse<CustomerIntegralResp>> listener) {
        String url = ServerAddressUtil.getInstance().newQueryIntegralDetail();
        CustomerIntegralReq customerIntegralReq = new CustomerIntegralReq();
        customerIntegralReq.setCustomerId(customerId);
        customerIntegralReq.setPageNo(currentPage);
        customerIntegralReq.setLastId(lastId);
        JFRequest.Executor executor = JFRequest.create(url);
        executor.requestValue(customerIntegralReq).execute(listener, "memberIntegralModification");
    }

    @Override
    public void bindCardInstance(CardActivateReq req, ResponseListener<CardActivateResp> listener) {

    }

    @Override
    public void activateCardsV2(CardActiveReqV2 req, ResponseListener<CardActiveRespV2> listener) {

    }

    @Override
    public void createCustomerV2(CustomerResp customer, String uniqueCode, ResponseListener<MemberCreateResp> listener) {

    }

    @Override
    public void saveCustomer(CustomerResp customer, YFResponseListener<YFResponse<CustomerCreateResp>> listener) {
        CustomerCreateReq req = toMemberCreateV2Req(customer);
        String url = ServerAddressUtil.getInstance().createMemberByMobile();
        JFRequest.Executor executor = JFRequest.create(url);
        executor.requestValue(req)
                .execute(listener, "saveCustomer");
    }

    @Override
    public void createMemberByPresetCustomer(CustomerResp customer, String uniqueCode, ResponseListener<MemberCreateResp> listener) {

    }

    @Override
    public void createMemberByPresetCustomer(CustomerResp customer, String uniqueCode, YFResponseListener<YFResponse<CustomerCreateResp>> listener) {

    }


    private MemberCreateOldCustomerV2Req toMemberCreateOldCustomerV2Req(CustomerResp customer, String uniqueCode) {
        MemberCreateOldCustomerV2Req req = new MemberCreateOldCustomerV2Req();
        req.cloneReq(toMemberCreateV2Req(customer));
        req.uniqueCode = uniqueCode;
        return req;
    }


    private CustomerCreateReq toMemberCreateV2Req(CustomerResp customer) {
        CustomerCreateReq req = new CustomerCreateReq();
        req.customerId = customer.customerId;
        req.name = customer.customerName;
        req.sex = customer.sex + "";
        req.address = customer.address;
        req.birthday = customer.birthday;
        req.consumePassword = customer.password;
        req.environmentHobby = customer.hobby;
        req.groupId = customer.groupId;
        req.groupName = customer.groupName;
        req.invoiceTitle = customer.invoice;
        req.memo = customer.memo;
        req.mobile = customer.mobile;
        req.nation = customer.nation;
        req.country = customer.country;
        req.nationalTelCode = customer.nationalTelCode;
        req.userId = Session.getAuthUser().getId();
        req.userName = Session.getAuthUser().getName();
        req.cardNo=customer.cardNo;
        if (!TextUtils.isEmpty(customer.faceCode)) {
            req.faceCode = customer.faceCode;
        }
        return req;
    }

    private CustomerCreateReq toBindCardReq(Long customerId,String cardNo){
        CustomerCreateReq req = new CustomerCreateReq();
        req.customerId=customerId;
        req.cardNo=cardNo;
        req.userId = Session.getAuthUser().getId();
        req.userName = Session.getAuthUser().getName();
        req.source=null;
        return req;
    }

    @Override
    public void bindCustomerFaceCode(Long customerId, String faceId, ResponseListener<BindCustomerFaceCodeResp> listener) {

    }

    @Override
    public void memberModifyLevel(FragmentActivity fragment, Long customerId, Long levelId, CalmResponseListener<ResponseObject<LoyaltyTransferResp>> listener) {

    }

    @Override
    public void validationMemberPwd(FragmentActivity fragment, MemberVerifyPwdReq req, CalmResponseListener<ResponseObject<LoyaltyTransferResp>> listener) {

    }

    @Override
    public void memberModifyMobile(FragmentActivity fragment, Long customerId, String oldMobile, String newMobile, ErpCurrency currency, CalmResponseListener<ResponseObject<LoyaltyTransferResp>> listener) {

    }

    @Override
    public void memberCreditList(FragmentActivity fragment, MemberCreditListReq req, boolean isShowLoading, CalmResponseListener<ResponseObject<MemberCreditListResp>> listener) {


    }

    @Override
    public void getSendCouponList(FragmentActivity fragment, CalmResponseListener<ResponseObject<CustomerDirectCouponListV2Resp>> listener) {

    }

    @Override
    public void getCustomTimes(CustomTimesReq req, boolean isShowLoading, CalmResponseListener<ResponseObject<CustomTimesResp>> listener) {

    }
}
