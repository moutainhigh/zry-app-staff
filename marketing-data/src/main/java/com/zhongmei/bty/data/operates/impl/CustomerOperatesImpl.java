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

/**
 * @version: 1.0
 * @date 2015年5月8日
 */
@SuppressLint("SimpleDateFormat")
public class CustomerOperatesImpl extends AbstractOpeartesImpl implements CustomerOperates {

    private static final String TAG = CustomerOperatesImpl.class.getSimpleName();

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public CustomerOperatesImpl(ImplContext context) {
        super(context);
    }

    @Override
    public void editCustomer(CustomerResp customerNew, ResponseListener<CustomerEditResp> listener) throws JSONException {
        String url = ServerAddressUtil.getInstance().editCustomer();
        OpsRequest.Executor<CustomerEditReq, CustomerEditResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(new CustomerEditReq().fromCustomer(customerNew)).responseClass(CustomerEditResp.class).execute(listener, "editCustomer");
    }

    @Override
    public void login(CustomerLoginReq loginReq, ResponseListener<CustomerLoginResp> listener) {
        String url = ServerAddressUtil.getInstance().memberLogin();
        OpsRequest.Executor<CustomerLoginReq, CustomerLoginResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(loginReq).responseClass(CustomerLoginResp.class).execute(listener, "memberLogin");
    }

    @Override
    public void customerLogin(CustomerLoginReq loginReq, ResponseListener<MemberLoginVoResp> listener) {
        //使用透传接口
        String url = ServerAddressUtil.getInstance().loyaltyTransfer();
        TransferReq<CustomerLoginReq> transferReq = new TransferReq<>();
        transferReq.setUrl(ServerAddressUtil.getInstance().customerLogin());
        transferReq.setPostData(loginReq);
        OpsRequest.Executor<TransferReq<CustomerLoginReq>, MemberLoginVoResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(transferReq).responseClass(MemberLoginVoResp.class).execute(listener, "customerLoginNew");
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
        //使用透传接口
        String url = ServerAddressUtil.getInstance().loyaltyTransfer();
        TransferReq<MemberCouponsReq> transferReq = new TransferReq<>();
        transferReq.setUrl(ServerAddressUtil.getInstance().memberCoupons());
        transferReq.setPostData(couponsReq);
        OpsRequest.Executor<TransferReq<MemberCouponsReq>, MemberCouponsVoResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(transferReq).responseClass(MemberCouponsVoResp.class).execute(listener, "customerCoupons");
    }

    @Override
    public void getCustomerCoupons(MemberCouponsReq couponsReq, YFResponseListener<YFResponseList<CustomerCouponResp>> listener) {
        String url = ServerAddressUtil.getInstance().memberCoupons();
        JFRequest.Executor executor = JFRequest.create(url);
        executor.requestValue(couponsReq).execute(listener, "customerCoupons");
    }

    @Override
    public void getCustomerCards(MemberCardsReq cardsReq, ResponseListener<LoyaltyTransferResp<MemberCardsResp>> listener) {
        String url = ServerAddressUtil.getInstance().loyaltyTransfer();
        TransferReq<MemberCardsReq> transferReq = new TransferReq<>();
        transferReq.setUrl(ServerAddressUtil.getInstance().memberCards());
        transferReq.setPostData(cardsReq);
        OpsRequest.Executor<TransferReq<MemberCardsReq>, LoyaltyTransferResp<MemberCardsResp>> executor = OpsRequest.Executor.create(url);
        executor.requestValue(transferReq).responseType(OpsRequest.getContentResponseType(LoyaltyTransferResp.class, MemberCardsResp.class)).execute(listener, "customerCards");
    }


    @Override
    public void memberRecharge(MemberRechargeReq rechargeReq, ResponseListener<MemberRechargeResp> listener) {
        String url = ServerAddressUtil.getInstance().memberRecharge();
        rechargeReq.setSource(1);
        OpsRequest.Executor<MemberRechargeReq, MemberRechargeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(rechargeReq).responseClass(MemberRechargeResp.class).execute(listener, "memberRecharge");
    }

    @Override
    public void cardRecharge(CardRechargeReq rechargeReq, ResponseListener<CardRechargeResp> listener) {
        String url = ServerAddressUtil.getInstance().cardRecharge();
        OpsRequest.Executor<CardRechargeReq, CardRechargeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(rechargeReq).responseClass(CardRechargeResp.class).execute(listener, "cardRecharge");
    }

    @Override
    public void findCustomerInfo(CustomerInfoReq couponinfoReq, ResponseListener<CustomerInfoResp> listener) {
        String url = ServerAddressUtil.getInstance().customerInfo();
        OpsRequest.Executor<CustomerInfoReq, CustomerInfoResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(couponinfoReq)
                .responseClass(CustomerInfoResp.class)
                .execute(listener, "findCouponInfo");
    }

    @Override
    public void getCheckCode(MemberCheckCodeReq checkCodeReq, ResponseListener<MemberCheckCodeResp> listener) {
        String url = ServerAddressUtil.getInstance().memberCheckCode();
        OpsRequest.Executor<MemberCheckCodeReq, MemberCheckCodeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(checkCodeReq)
                .responseClass(MemberCheckCodeResp.class)
                .execute(listener, "memberCheckCode");
    }

    @Override
    public void validateCheckCode(MemberValidateCheckCodeReq validateCheckCodeReq,
                                  ResponseListener<MemberValidateCheckCodeResp> listener) {
        String url = ServerAddressUtil.getInstance().memberValidateCheckCode();
        OpsRequest.Executor<MemberValidateCheckCodeReq, MemberValidateCheckCodeResp> executor =
                OpsRequest.Executor.create(url);
        executor.requestValue(validateCheckCodeReq)
                .responseClass(MemberValidateCheckCodeResp.class)
                .execute(listener, "memberValidateCheckCode");
    }

    @Override
    public void modifyPassword(MemberResetPswdReq resetPswdReq, ResponseListener<MemberResetPswdResp> listener) {
        String url = ServerAddressUtil.getInstance().memberModifyPswd();
        OpsRequest.Executor<MemberResetPswdReq, MemberResetPswdResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(resetPswdReq)
                .responseClass(MemberResetPswdResp.class)
                .execute(listener, "memberResetPswd");
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
        String url = ServerAddressUtil.getInstance().upgradeMemberUrl();
        OpsRequest.Executor<MemberUpgradeReq, MemberUpgradeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(MemberUpgradeResp.class)
                //.responseProcessor(new UpgradeCustomerRespProcessor(customer))
                .execute(listener, "memberUpgrade");
    }

    @Override
    public void createCustomer(CustomerResp customer, String uniqueCode, ResponseListener<MemberCreateResp> listener) {
        String url = ServerAddressUtil.getInstance().creatMemeberUrl();
        MemberCreateReq req = toMemberCreateReq(customer, uniqueCode);
        OpsRequest.Executor<MemberCreateReq, MemberCreateResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(MemberCreateResp.class)
                .execute(listener, "memberCreate");
    }

    @Override
    public void queryMemberByUniqueCode(String uniqueCode, ResponseListener<MemberCreateResp> listener) {
        String url = ServerAddressUtil.getInstance().queueMemeberUrl();
        MemberQueueReq req = new MemberQueueReq();
        req.setUniqueCode(uniqueCode);
        OpsRequest.Executor<MemberQueueReq, MemberCreateResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req).responseClass(MemberCreateResp.class).execute(listener, "memberQueue");
    }

    @Override
    public void cardLogin(String cardNo, ResponseListener<CardLoginResp> listener) {
        String url = ServerAddressUtil.getInstance().cardLogin();
        CardLoginReq req = toCardLoginReq(cardNo);
        OpsRequest.Executor<CardLoginReq, CardLoginResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req).responseClass(CardLoginResp.class).execute(listener, "cardLogin");
    }

    private CardLoginReq toCardLoginReq(String cardNo) {
        CardLoginReq req = new CardLoginReq(cardNo);
        req.setSource(1);//1表示来自on os
        AuthUser user = Session.getAuthUser();
        if (user != null) {
            req.setUserId(user.getId());
        }
        return req;
    }

    @Override
    public void cardLoginNew(String cardNum, ResponseListener<LoyaltyMindTransferResp<NewCardLoginResp>> listener) {
        Long brandId = BaseApplication.getInstance().getBrandIdenty();
        Long commercialId = BaseApplication.getInstance().getShopIdenty();
        Long userId = -1L;
        AuthUser user = Session.getAuthUser();
        if (user != null) {
            userId = user.getId();
        }

        String transferUrl = ServerAddressUtil.getInstance().newCardLogin() + "?brandId=" + brandId +
                "&commercialId=" + commercialId + "&userId=" + userId + "&appType=1&cardNum=" + cardNum;

        //PrintSettingDal printSettingDal = OperatesFactory.create(PrintSettingDal.class);
        //printSettingDal.loyaltyMindTransfer(transferUrl, new Object(), NewCardLoginResp.class, listener);
    }

    @Override
    public void getMemberCards(Long customerId, String mobile, ResponseListener<MemberCardResp> listener) {
        String url = ServerAddressUtil.getInstance().getMemeberCardUrl();
        MemberCardReq req = new MemberCardReq();
        req.setCreateId(Session.getAuthUser().getId());
        req.setCustomerId(customerId);
        req.setMobile(mobile);
        OpsRequest.Executor<MemberCardReq, MemberCardResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req).responseClass(MemberCardResp.class).execute(listener, "memberCreate");
    }


    @Override
    public void cancelGetMemberCards() {
        RequestManager.cancelAll("memberCreate");
    }

    @Override
    public void rangeSearchCards(CardRangeSearchReq req, ResponseListener<CardRangeSearchResp> listener) {
        String url = ServerAddressUtil.getInstance().getRangeSearchCardsUrl();

        OpsRequest.Executor<CardRangeSearchReq, CardRangeSearchResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req).responseClass(CardRangeSearchResp.class).execute(listener, "rangeSearchCards");

    }

    @Override
    public void singleSearchCards(CardSingleSearchReq req, ResponseListener<CardRangeSearchResp> listener) {
        String url = ServerAddressUtil.getInstance().getSingleSearchCardsUrl();

        OpsRequest.Executor<CardSingleSearchReq, CardRangeSearchResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req).responseClass(CardRangeSearchResp.class).execute(listener, "singleSearchCards");
    }

    @Override
    public void singleSearchCardsByTrans(CardSingleSearchByTransReq req, ResponseListener<CardSingleSearchResp> listener) {
        String url = ServerAddressUtil.getInstance().loyaltyTransfer();
        TransferReq<CardSingleSearchByTransReq> transferReq = new TransferReq<>();
        transferReq.setUrl(ServerAddressUtil.getInstance().getSingleSearchCardsUrlByTrans());
        transferReq.setPostData(req);
        OpsRequest.Executor<TransferReq<CardSingleSearchByTransReq>, CardSingleSearchResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(transferReq).responseClass(CardSingleSearchResp.class).execute(listener, "singleSearchCardsByTrans");
    }

    @Override
    public void activateCards(CardActivateReq req, ResponseListener<CardActivateResp> listener) {
        String url = ServerAddressUtil.getInstance().getActiviteCardsUrl();

        OpsRequest.Executor<CardActivateReq, CardActivateResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req).responseClass(CardActivateResp.class).execute(listener, "activateCards");

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
//		String birthday = customer.birthday;
//		if (!TextUtils.isEmpty(birthday)) {
//			Date time = new Date(Long.valueOf(birthday));
        req.setBirthday(customer.birthday);
//		}
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

        String url = ServerAddressUtil.getInstance().memberIntegralHistory();
        OpsRequest.Executor<MemberIntegralInfoReq, MemberIntegralInfoResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(MemberIntegralInfoResp.class)
                .execute(listener, "memberIntegralHistory");
    }


    @Override
    public void getCardIntegralHistory(CardIntegralInfoReq req, ResponseListener<CardIntegralInfoResp> listener) {
        String url = ServerAddressUtil.getInstance().cardIntegralHistory();
        OpsRequest.Executor<CardIntegralInfoReq, CardIntegralInfoResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(CardIntegralInfoResp.class)
                .execute(listener, "getCardIntegralHistory");
    }

    /**
     * 查询会员请求
     */
    static class MemberQueueReq {
        /**
         * 旧会员唯一标识
         */
        private String uniqueCode;

        public String getUniqueCode() {
            return uniqueCode;
        }

        public void setUniqueCode(String uniqueCode) {
            this.uniqueCode = uniqueCode;
        }
    }

    /**
     * 会员地址请求
     */
    class MemberAddressReq {

        private Long memberID;

        public Long getMemberID() {
            return memberID;
        }

        public void setMemberID(Long memberID) {
            this.memberID = memberID;
        }
    }

    /**
     * 创建会员请求类
     */
    static class MemberCreateReq {
        /**
         * 旧会员唯一标识
         */
        private String uniqueCode;

        private String mobile;

        private Long userId;

        // 1988/03/01
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

        /**
         * 国家英文名称(为空默认中国) = countryEN
         */
        private String nation;
        /**
         * 国家中文名称(为空默认中国) = countryZH
         */
        private String country;
        /**
         * 电话国际区码(为空默认中国) = AreaCode
         */
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

    /**
     * @Date 2016年3月16日
     * @Description: 获取售卡记录
     * @Param
     */
    @Override
    public void getCustomerSellCardOrders(CustomerSellcardReq sellcardReq,
                                          ResponseListener<List<CustomerSellcardResp>> listener) {
        String url = ServerAddressUtil.getInstance().getCustomerSellCardOrders();
        OpsRequest.Executor<CustomerSellcardReq, List<CustomerSellcardResp>> executor = OpsRequest.Executor.create(url);
        executor.requestValue(sellcardReq)
                .responseType(OpsRequest.getListContentResponseType(CustomerSellcardResp.class))
                .execute(listener, "getCustomerSellCardOrders");

    }

    @Override
    public void getEntityCardChangeOrders(CustomerSellcardReq entityCardChangeReq, ResponseListener<List<CustomerSellcardResp>> listener
    ) {
        String url = ServerAddressUtil.getInstance().getEntityCardChangeOrders();
        OpsRequest.Executor<CustomerSellcardReq, List<CustomerSellcardResp>> executor = OpsRequest.Executor.create(url);
        executor.requestValue(entityCardChangeReq)
                .responseType(OpsRequest.getListContentResponseType(CustomerSellcardResp.class))
                .execute(listener, "getEntityCardChangeOrders");
    }

    /**
     * @Date 2016年3月16日
     * @Description: 获取售卡详情
     * @Param
     */
    @Override
    public void getCustomerSellCardDetailInfo(CustomerSellcardDetailReq sellcardReq,
                                              ResponseListener<CustomerSellcardDetailResp> listener) {
        String url = ServerAddressUtil.getInstance().getCustomerSellCardDetailInfo();
        OpsRequest.Executor<CustomerSellcardDetailReq, CustomerSellcardDetailResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(sellcardReq)
                .responseClass(CustomerSellcardDetailResp.class)
                .execute(listener, "getCustomerSellCardDetailInfo");

    }

    @Override
    public void getEntityCardChangeDetailInfo(Long tradeId, ResponseListener<EntityCardChangeDetailResp> listener
    ) {
        String url = ServerAddressUtil.getInstance().getEntityCardChangeDetailInfo();
        OpsRequest.Executor<CustomerSellcardDetailReq, EntityCardChangeDetailResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(toEntityCardChangeDetailReq(tradeId))
                .responseClass(EntityCardChangeDetailResp.class)
                .execute(listener, "getEntityCardChangeDetailInfo");
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
        CustomerSellcardDetailReq requestContent = new CustomerSellcardDetailReq();
        requestContent.setTradeId(tradeId);

        String url = ServerAddressUtil.getInstance().getAnonymousEntityCardSellDetailInfo();
        OpsRequest.Executor<CustomerSellcardDetailReq, CustomerSellcardDetailResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(requestContent)
                .responseClass(CustomerSellcardDetailResp.class)
                .execute(listener, "getAnonymousEntityCardDetailInfo");
    }

    /**
     * @Date 2016年3月22日
     * @Description: 查询实体卡储值记录
     * @Param
     */
    @Override
    public void getCardStoreValueList(CustomerCardStoreValueReq cardStoreValueReq,
                                      ResponseListener<CustomerCardStoreValueResp> listener) {
        String url = ServerAddressUtil.getInstance().getCardStoreValueList();
        OpsRequest.Executor<CustomerCardStoreValueReq, CustomerCardStoreValueResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(cardStoreValueReq)
                .responseClass(CustomerCardStoreValueResp.class)
                .execute(listener, "getCardStoreValueList");
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

    /**
     * @Date 2016年3月22日
     * @Description: 查询支付和会员信息
     * @Param
     */
    @Override
    public void queryPaymentAndMember(PaymentAndMemberReq req,
                                      ResponseListener<PaymentAndMemberResp> listener) {
        String url = ServerAddressUtil.getInstance().queryPaymentAndMember();
        OpsRequest.Executor<PaymentAndMemberReq, PaymentAndMemberResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(PaymentAndMemberResp.class)
                .execute(listener, "queryPaymentAndMember");
    }

    @Override
    public void queryPayment(String paymentUuid, ResponseListener<PaymentAndMemberResp> listener) {
        PaymentAndMemberReq requestContent = new PaymentAndMemberReq();
        requestContent.setPaymentUuid(paymentUuid);

        String url = ServerAddressUtil.getInstance().queryPayment();
        OpsRequest.Executor<PaymentAndMemberReq, PaymentAndMemberResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(requestContent)
                .responseClass(PaymentAndMemberResp.class)
                .execute(listener, "queryPayment");
    }

    /**
     * @Date 2016年3月22日
     * @Description:查询会员储值记录
     * @Param
     */
    @Override
    public void getMemberStoreValueList(CustomerMemberStoreValueReq req,
                                        ResponseListener<CustomerMemberStoreValueResp> listener) {
        String url = ServerAddressUtil.getInstance().getMemberStoreValueList();
        OpsRequest.Executor<CustomerMemberStoreValueReq, CustomerMemberStoreValueResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(CustomerMemberStoreValueResp.class)
                .execute(listener, "getMemberStoreValueList");
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

    /**
     * @Date 2016年4月5日
     * @Description: 售卡记录作废
     * @Param
     */
    @Override
    public void saleCardRecordInvaliate(CustomerSaleCardInvalidReq req,
                                        ResponseListener<CustomerSaleCardInvalidResp> listener) {
        String url = ServerAddressUtil.getInstance().saleCardRecordInvaliate();
        OpsRequest.Executor<CustomerSaleCardInvalidReq, CustomerSaleCardInvalidResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(CustomerSaleCardInvalidResp.class)
                .execute(listener, "saleCardRecordInvaliate");
    }

    /**
     * @Date 2016年4月5日
     * @Description: 实体卡储值撤销
     * @Param
     */
    @Override
    public void cardStoreValueRevokeReq(CustomerOrderBean orderBean, Reason reason, ResponseListener<Object> listener) {
        String url = ServerAddressUtil.getInstance().cardStoreValueRevoke();
        OpsRequest.Executor<CustomerCardStoreValueRevokeReq, Object> executor = OpsRequest.Executor.create(url);
        executor.requestValue(toCustomerCardStoreValueRevokeReq(orderBean, reason))
                .responseClass(Object.class)
                .execute(listener, "cardStoreValueRevokeReq");

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

    /**
     * @Date 2016年4月5日
     * @Description: 会员储值撤销
     * @Param
     */
    @Override
    public void memberStoreValueRevokeReq(CustomerMemberStoreValueRevokeReq req,
                                          ResponseListener<CustomerMemberStoreValueRevokeResp> listener) {

        String url = ServerAddressUtil.getInstance().memberStoreValueRevoke();
        OpsRequest.Executor<CustomerMemberStoreValueRevokeReq, CustomerMemberStoreValueRevokeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseProcessor(new CustomerMemberStoreRevokeRespProcessor())
                .responseClass(CustomerMemberStoreValueRevokeResp.class)
                .execute(listener, "memberStoreValueRevokeReq");
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
        CustomerSaleCardInvalidReq req = new CustomerSaleCardInvalidReq();
        req.setTradeId(tradeId);

        String url = ServerAddressUtil.getInstance().anonymousEntityCardSaleRecordInvalid();
        OpsRequest.Executor<CustomerSaleCardInvalidReq, CustomerSaleCardInvalidResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(CustomerSaleCardInvalidResp.class)
                .execute(listener, "anonymousEntityCardSaleRecordInvalid");
    }

    @Override
    public void getAnoymousCardTradeInfo(AnonymousCardTradeReq req, ResponseListener<AnonymousCardTradeResp> listener) {
        String url = ServerAddressUtil.getInstance().getAnoymousCardTradeUrl();
        OpsRequest.Executor<AnonymousCardTradeReq, AnonymousCardTradeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(AnonymousCardTradeResp.class)
                .execute(listener, "anonymousCardTrade");
    }

    @Override
    public void queryAnoymousCardStorePayMode(@NonNull String cardNo, ResponseListener<AnonymousCardStorePayModeResp> listener) {
        String url = ServerAddressUtil.getInstance().getQueryTempCardStorePayModeUrl();
        OpsRequest.Executor<AnonymousCardTradeReq, AnonymousCardStorePayModeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(toAnonymousCardTradeReq(cardNo))
                .responseClass(AnonymousCardStorePayModeResp.class)
                .execute(listener, "queryAnoymousCardStorePayMode");
    }

    private AnonymousCardTradeReq toAnonymousCardTradeReq(String cardNo) {
        AnonymousCardTradeReq anonymousCardTradeReq = new AnonymousCardTradeReq();
        anonymousCardTradeReq.setCardNo(cardNo);

        return anonymousCardTradeReq;
    }

    @Override
    public void returnAnoymousCard(ReturnAnonymousCardReq req, ResponseListener<ReturnAnonymousCardResp> listener) {
        String url = ServerAddressUtil.getInstance().returnAnoymousCardUrl();
        OpsRequest.Executor<ReturnAnonymousCardReq, ReturnAnonymousCardResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(req)
                .responseClass(ReturnAnonymousCardResp.class)
                .execute(listener, "returnAnonymousCard");
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
        CustomerReq req = new CustomerReq();
        req.brandId = BaseApplication.getInstance().getBrandIdenty();//品牌id
        req.commercialId = BaseApplication.getInstance().getShopIdenty();
        req.clientType = "pos";
        req.customerId = customerId;
        if (isNeedCredit) {
            req.isNeedCredit = 1;
        }
        String url = ServerAddressUtil.getInstance().loyaltyTransfer();
        TransferReq<CustomerReq> transferReq = new TransferReq<>();
        transferReq.setUrl(ServerAddressUtil.getInstance().getCustomerDetailById());
        transferReq.setPostData(req);
        OpsRequest.Executor<TransferReq<CustomerReq>, com.zhongmei.bty.basemodule.customer.message.CustomerResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(transferReq).responseClass(com.zhongmei.bty.basemodule.customer.message.CustomerResp.class).execute(listener, "getCustomerById");
    }

    @Override
    public void getCustomerById(Long customerId, boolean isNeedCredit, YFResponseListener<YFResponse<CustomerResp>> listener) {
        CustomerReq req = new CustomerReq();
        req.brandId = BaseApplication.getInstance().getBrandIdenty();//品牌id
        req.commercialId = BaseApplication.getInstance().getShopIdenty();
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
    public void cancelGetCustomerById() {
        RequestManager.cancelAll("getCustomerById");
    }

    @Override
    public void findCustomerByPhone(String phone, ResponseListener<CustomerMobileVo> listener) {
        getCustomerByType(CustomerRequest.PHONE, phone, listener);
    }

    @Override
    public void getCustomerByType(Long loginType, String loginId, ResponseListener<CustomerMobileVo> listener) {
        String url = ServerAddressUtil.getInstance().loyaltyTransfer();
        CustomerRequest req = new CustomerRequest(loginType, loginId);
        TransferReq request = new TransferReq(ServerAddressUtil.getInstance().getCustomerByType(), req);
        OpsRequest.Executor<TransferReq, CustomerMobileVo> executor = OpsRequest.Executor.create(url);
        executor.requestValue(request)
                .responseClass(CustomerMobileVo.class)
                .execute(listener, "findCustomerByPhone");
    }

//    @Override
//    public void queryCustomerListByCondition(String nameOrMobile, String cardNum, String openId, String groupId, String customerType, String levelId, int pageSize, int currentPage,boolean isCache, Long minCustomerId,ResponseListener<CustomerListVoResp> listener) {
//        //使用透传接口
//        CustomerListReq req = new CustomerListReq();
//        CustomerListReq.CustomerListReqBean bean = req.new CustomerListReqBean();
//        bean.brandId = BaseApplication.getInstance().getBrandIdenty();//品牌id
//        if (!TextUtils.isEmpty(nameOrMobile)) {
//            bean.nameOrMobile = nameOrMobile;
//        }
//        if (!TextUtils.isEmpty(cardNum)) {
//            bean.cardNum = cardNum;
//        }
//        if (!TextUtils.isEmpty(openId)) {
//            bean.openId = openId;
//        }
//        if (!TextUtils.isEmpty(groupId)) {
//            bean.groupId = groupId;
//        }
//        if (!TextUtils.isEmpty(customerType)) {
//            bean.customerType = customerType;
//        }
//        if (!TextUtils.isEmpty(levelId)) {
//            bean.levelId = levelId;
//        }
//        if (!isCache){ // 是否使用缓存,不使用缓存 需要传该字段
//            bean.refresh = "Y";
//        }
//        bean.minCustomerId = minCustomerId;
//        req.pageSize = 10;
//        req.currentPage = currentPage;
//        req.bean = bean;
//        String url = ServerAddressUtil.getInstance().loyaltyTransfer();
//        TransferReq<CustomerListReq> transferReq = new TransferReq<>();
//        transferReq.setUrl(ServerAddressUtil.getInstance().queryCustomerList());
//        transferReq.setPostData(req);
//        OpsRequest.Executor<TransferReq<CustomerListReq>, CustomerListVoResp> executor = OpsRequest.Executor.create(url);
//        executor.requestValue(transferReq).responseClass(CustomerListVoResp.class).execute(listener, "queryCustomerListByCondition");
//    }

    @Override
    public void queryCustomerListByCondition(CustomerListReq req, ResponseListener<CustomerListVoResp> listener) {
        String url = ServerAddressUtil.getInstance().loyaltyTransfer();
        TransferReq<CustomerListReq> transferReq = new TransferReq<>();
        transferReq.setUrl(ServerAddressUtil.getInstance().queryCustomerList());
        transferReq.setPostData(req);
        OpsRequest.Executor<TransferReq<CustomerListReq>, CustomerListVoResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(transferReq).responseClass(CustomerListVoResp.class).execute(listener, "queryCustomerListByCondition");
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
        String url = ServerAddressUtil.getInstance().getMemberAddressUrl();
        OpsRequest.Executor<MemberAddressReq, MemberAddressResp> executor = OpsRequest.Executor.create(url);
        MemberAddressReq req = new MemberAddressReq();
        req.setMemberID(memberId);
        executor.requestValue(req)
                .responseClass(MemberAddressResp.class)
                .execute(listener, "queryMemberAddressById");
    }

    @Override
    public void getCardBaseInfo(String cardNum, ResponseListener<CardBaseInfoResp> listener) {
        String url = ServerAddressUtil.getInstance().loyaltyTransfer();
        CardBaseInfoReq req = new CardBaseInfoReq();
        req.setCardNum(cardNum);
        TransferReq<CardBaseInfoReq> transferReq = new TransferReq<>();
        transferReq.setUrl(ServerAddressUtil.getInstance().getCardBaseInfoUrl());
        transferReq.setPostData(req);
        OpsRequest.Executor<TransferReq<CardBaseInfoReq>, CardBaseInfoResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(transferReq).responseClass(CardBaseInfoResp.class).execute(listener, "getCardBaseInfo");
    }

    @Override
    public void getCardAccount(String cardNum, ResponseListener<CardAccountResp> listener) {
        String url = ServerAddressUtil.getInstance().loyaltyTransfer();
        CardAccountReq req = new CardAccountReq();
        req.setCardNum(cardNum);
        TransferReq<CardAccountReq> transferReq = new TransferReq<>();
        transferReq.setUrl(ServerAddressUtil.getInstance().getCardAccount());
        transferReq.setPostData(req);
        OpsRequest.Executor<TransferReq<CardAccountReq>, CardAccountResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(transferReq).responseClass(CardAccountResp.class).execute(listener, "getCardAccount");
    }

    @Override
    public void getCardInfoByNum(CardSingleSearchByTransReq req, ResponseListener<CustomerCardInfoResp> listener) {
        String url = ServerAddressUtil.getInstance().loyaltyTransfer();
        TransferReq<CardSingleSearchByTransReq> transferReq = new TransferReq<>();
        transferReq.setUrl(ServerAddressUtil.getInstance().getCardInfoUrlByNum());
        transferReq.setPostData(req);
        OpsRequest.Executor<TransferReq<CardSingleSearchByTransReq>, CustomerCardInfoResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(transferReq).responseClass(CustomerCardInfoResp.class).execute(listener, "getCardInfoByNum");
    }

    @Override
    public void memberIntegralModification(Long customerId, int integral, Integer operateType, ResponseListener<MemberIntegralModificationResp> listener) {
        String url = ServerAddressUtil.getInstance().loyaltyTransfer();
        MemberIntegralModificationReq req = new MemberIntegralModificationReq();
        req.setCustomerId(customerId);
        req.setIntegral(integral);
        req.setOperateType(operateType);
        req.setSource(1);
        req.setUserId(Session.getAuthUser().getId());
        TransferReq<MemberIntegralModificationReq> transferReq = new TransferReq<>();
        transferReq.setUrl(ServerAddressUtil.getInstance().integralModification());
        transferReq.setPostData(req);
        OpsRequest.Executor<TransferReq<MemberIntegralModificationReq>, MemberIntegralModificationResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(transferReq).responseClass(MemberIntegralModificationResp.class).execute(listener, "memberIntegralModification");
    }

    @Override
    public void newMemberIntegralInfo(Long customerId, Integer currentPage, Long lastId, ResponseListener<NewMemberIntegralInfoResp> listener) {
        String url = ServerAddressUtil.getInstance().loyaltyTransfer();
        String url2 = ServerAddressUtil.getInstance().newQueryIntegralDetail() + "&customerId=" + customerId + "&currentPage=" + currentPage;
        if (lastId != null) {
            url2 += "&lastId=" + lastId;
        }
        TransferReq<Object> transferReq = new TransferReq<>();
        transferReq.setUrl(url2);
        OpsRequest.Executor<TransferReq, NewMemberIntegralInfoResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(transferReq).responseClass(NewMemberIntegralInfoResp.class).execute(listener, "memberIntegralModification");
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
        String url = ServerAddressUtil.getInstance().loyaltyTransfer();
        TransferReq<CardActivateReq> transferReq = new TransferReq<>();
        transferReq.setUrl(ServerAddressUtil.getInstance().getBindCardInstance());
        transferReq.setPostData(req);
        OpsRequest.Executor<TransferReq<CardActivateReq>, CardActivateResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(transferReq).responseClass(CardActivateResp.class).execute(listener, "activateCards");
    }

    @Override
    public void activateCardsV2(CardActiveReqV2 req, ResponseListener<CardActiveRespV2> listener) {
        String url = ServerAddressUtil.getInstance().loyaltyTransfer();
        TransferReq<CardActiveReqV2> transferReq = new TransferReq<>();
        transferReq.setUrl(ServerAddressUtil.getInstance().getCardBindCardInstance());
        transferReq.setPostData(req);
        OpsRequest.Executor<TransferReq<CardActiveReqV2>, CardActiveRespV2> executor = OpsRequest.Executor.create(url);
        executor.requestValue(transferReq).responseClass(CardActiveRespV2.class).execute(listener, "activateCardsV2");
    }

    @Override
    public void createCustomerV2(CustomerResp customer, String uniqueCode, ResponseListener<MemberCreateResp> listener) {
        CustomerCreateReq req = toMemberCreateV2Req(customer);
        String url = ServerAddressUtil.getInstance().loyaltyTransfer();
        TransferReq<CustomerCreateReq> transferReq = new TransferReq<>();
        transferReq.setUrl(ServerAddressUtil.getInstance().createMemberByMobile());
        transferReq.setPostData(req);
        OpsRequest.Executor<TransferReq<CustomerCreateReq>, MemberCreateResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(transferReq).responseClass(MemberCreateResp.class).execute(listener, "createCustomerV2");
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
        MemberCreateOldCustomerV2Req req = toMemberCreateOldCustomerV2Req(customer, uniqueCode);
        String url = ServerAddressUtil.getInstance().loyaltyTransfer();
        TransferReq<MemberCreateOldCustomerV2Req> transferReq = new TransferReq<>();
        transferReq.setUrl(ServerAddressUtil.getInstance().createMemberByPresetCustomer());
        transferReq.setPostData(req);
        OpsRequest.Executor<TransferReq<MemberCreateOldCustomerV2Req>, MemberCreateResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(transferReq).responseClass(MemberCreateResp.class).execute(listener, "createMemberByPresetCustomer");
    }

    @Override
    public void createMemberByPresetCustomer(CustomerResp customer, String uniqueCode, YFResponseListener<YFResponse<CustomerCreateResp>> listener) {
        MemberCreateOldCustomerV2Req req = toMemberCreateOldCustomerV2Req(customer, uniqueCode);
        String url = ServerAddressUtil.getInstance().createMemberByPresetCustomer();
        JFRequest.Executor executor = JFRequest.create(url);
        executor.requestValue(req)
                .execute(listener, "saveCustomer");
    }

    /**
     * 通过预制顾客创建会员 实体
     *
     * @param customer
     * @param uniqueCode
     * @return
     */
    private MemberCreateOldCustomerV2Req toMemberCreateOldCustomerV2Req(CustomerResp customer, String uniqueCode) {
        MemberCreateOldCustomerV2Req req = new MemberCreateOldCustomerV2Req();
        req.cloneReq(toMemberCreateV2Req(customer));
        req.uniqueCode = uniqueCode;
        return req;
    }

    /**
     * v2 创建会员请求参数
     *
     * @param customer
     * @return
     */
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
        if (!TextUtils.isEmpty(customer.faceCode)) {
            req.faceCode = customer.faceCode;
        }
        return req;
    }

    @Override
    public void bindCustomerFaceCode(Long customerId, String faceId, ResponseListener<BindCustomerFaceCodeResp> listener) {
        String url = ServerAddressUtil.getInstance().loyaltyTransfer();
        BindCustomerFaceCodeReq req = new BindCustomerFaceCodeReq();
        req.setUserId(Session.getAuthUser().getId());
        req.setCustomerId(customerId);
        req.setFaceCode(faceId);
        TransferReq<BindCustomerFaceCodeReq> transferReq = new TransferReq<>();
        transferReq.setUrl(ServerAddressUtil.getInstance().saveCustomerFaceCode());
        transferReq.setPostData(req);
        OpsRequest.Executor<TransferReq<BindCustomerFaceCodeReq>, BindCustomerFaceCodeResp> executor = OpsRequest.Executor.create(url);
        executor.requestValue(transferReq).responseClass(BindCustomerFaceCodeResp.class).execute(listener, "createCustomerV2");
    }

    @Override
    public void memberModifyLevel(FragmentActivity fragment, Long customerId, Long levelId, CalmResponseListener<ResponseObject<LoyaltyTransferResp>> listener) {
        String transferUrl = ServerAddressUtil.getInstance().loyaltyTransfer();
        String url = ServerAddressUtil.getInstance().memberModifyLevel();
        MemberModifyLevelReq req = new MemberModifyLevelReq();
        req.setCustomerId(customerId);
        req.setLevelId(levelId);
        req.setUserId(Session.getAuthUser() == null ? null : Session.getAuthUser().getId());
        req.setName(Session.getAuthUser() == null ? null : Session.getAuthUser().getName());

        TransferReq<MemberModifyLevelReq> transferReq = new TransferReq<>();
        transferReq.setUrl(url);
        transferReq.setPostData(req);
        CalmNetWorkRequest.with(fragment)
                .url(transferUrl)
                .requestContent(transferReq)
                .responseClass(LoyaltyTransferResp.class)
                .showLoading()
                .errorListener(listener)
                .successListener(listener)
                .tag("memberModifyLevel")
                .create();
    }

    @Override
    public void validationMemberPwd(FragmentActivity fragment, MemberVerifyPwdReq req, CalmResponseListener<ResponseObject<LoyaltyTransferResp>> listener) {
        String transferUrl = ServerAddressUtil.getInstance().loyaltyTransfer();
        String url = ServerAddressUtil.getInstance().verifyMemberPassword()
                + req.toString();
        TransferReq<MemberVerifyPwdReq> transferReq = new TransferReq<>();
        transferReq.setUrl(url);
        CalmNetWorkRequest.with(fragment)
                .url(transferUrl)
                .requestContent(transferReq)
                .responseClass(LoyaltyTransferResp.class)
                .showLoading()
                .errorListener(listener)
                .successListener(listener)
                .tag("validationMemberPwd")
                .create();
    }

    @Override
    public void memberModifyMobile(FragmentActivity fragment, Long customerId, String oldMobile, String newMobile, ErpCurrency currency, CalmResponseListener<ResponseObject<LoyaltyTransferResp>> listener) {
        String transferUrl = ServerAddressUtil.getInstance().loyaltyTransfer();
        String url = ServerAddressUtil.getInstance().memberModifyMobile();
        MemberModifyMobileReq req = new MemberModifyMobileReq();
        req.setCustomerId(customerId);
        req.setNewMobile(newMobile);
        req.setOldMobile(oldMobile);
        req.setCountry(currency.getCountryZh());
        req.setNation(currency.getCountryEn());
        req.setNationalTelCode(currency.getAreaCode());
        req.setUserId(Session.getAuthUser() == null ? null : Session.getAuthUser().getId());

        TransferReq<MemberModifyMobileReq> transferReq = new TransferReq<>();
        transferReq.setUrl(url);
        transferReq.setPostData(req);
        CalmNetWorkRequest.with(fragment)
                .url(transferUrl)
                .requestContent(transferReq)
                .responseClass(LoyaltyTransferResp.class)
                .showLoading()
                .errorListener(listener)
                .successListener(listener)
                .tag("memberModifyMobile")
                .create();
    }

    @Override
    public void memberCreditList(FragmentActivity fragment, MemberCreditListReq req, boolean isShowLoading, CalmResponseListener<ResponseObject<MemberCreditListResp>> listener) {
        String transferUrl = ServerAddressUtil.getInstance().loyaltyTransfer();
        String url = ServerAddressUtil.getInstance().memberCreditList();
        TransferReq<MemberCreditListReq> transferReq = new TransferReq<>();
        transferReq.setUrl(url);
        transferReq.setPostData(req);
        CalmNetWorkRequest.Builder builder = CalmNetWorkRequest.with(fragment)
                .url(transferUrl)
                .requestContent(transferReq)
                .responseClass(MemberCreditListResp.class)
                .errorListener(listener)
                .successListener(listener)
                .tag("memberCreditList");
        if (isShowLoading) {
            builder.showLoading();
        }
        builder.create();

    }

    @Override
    public void getSendCouponList(FragmentActivity fragment, CalmResponseListener<ResponseObject<CustomerDirectCouponListV2Resp>> listener) {
        TransferReq<Object> transferReq = new TransferReq<>();
        transferReq.setUrl(ServerAddressUtil.getInstance().customerDirectCouponListV2());
        new CalmNetWorkRequest.Builder<TransferReq<Object>, ResponseObject<CustomerDirectCouponListV2Resp>>()
                .with(fragment)
                .url(ServerAddressUtil.getInstance().loyaltyTransfer())
                .requestContent(transferReq)
                .responseClass(CustomerDirectCouponListV2Resp.class)
                .showLoading()
                .tag("getDirectCouponList")
                .successListener(listener)
                .errorListener(listener)
                .create();
    }

    @Override
    public void getCustomTimes(CustomTimesReq req, boolean isShowLoading, CalmResponseListener<ResponseObject<CustomTimesResp>> listener) {
        String transferUrl = ServerAddressUtil.getInstance().loyaltyTransfer();
        String url = ServerAddressUtil.getInstance().customTimes();
        TransferReq<CustomTimesReq> transferReq = new TransferReq<>();
        transferReq.setUrl(url);
        transferReq.setPostData(req);
        CalmNetWorkRequest.Builder builder = CalmNetWorkRequest.with(getContext())
                .url(transferUrl)
                .timeout(2000)
                .requestContent(transferReq)
                .responseClass(CustomTimesResp.class)
                .errorListener(listener)
                .successListener(listener)
                .tag(url);
        if (isShowLoading) {
            builder.showLoading();
        }
        builder.create();
    }
}
