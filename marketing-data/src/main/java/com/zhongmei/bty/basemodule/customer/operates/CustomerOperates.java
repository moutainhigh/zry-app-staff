package com.zhongmei.bty.basemodule.customer.operates;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.zhongmei.bty.basemodule.customer.bean.CustomerMobileVo;
import com.zhongmei.yunfu.bean.req.CustomerCouponResp;
import com.zhongmei.yunfu.bean.req.CustomerIntegralResp;
import com.zhongmei.yunfu.bean.req.CustomerStoredBalanceResp;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.bty.basemodule.customer.bean.CustomerOrderBean;
import com.zhongmei.bty.basemodule.customer.message.BindCustomerFaceCodeResp;
import com.zhongmei.bty.basemodule.customer.message.CustomTimesReq;
import com.zhongmei.bty.basemodule.customer.message.CustomTimesResp;
import com.zhongmei.bty.basemodule.customer.message.CustomerCardInfoResp;
import com.zhongmei.bty.basemodule.customer.message.CustomerDirectCouponListV2Resp;
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
import com.zhongmei.bty.basemodule.customer.message.MemberIntegralModificationResp;
import com.zhongmei.yunfu.bean.req.CustomerLoginReq;
import com.zhongmei.yunfu.bean.req.CustomerLoginResp;
import com.zhongmei.bty.basemodule.customer.message.MemberLoginVoResp;
import com.zhongmei.bty.basemodule.customer.message.MemberRechargeReq;
import com.zhongmei.bty.basemodule.customer.message.MemberRechargeResp;
import com.zhongmei.bty.basemodule.customer.message.MemberResetPswdReq;
import com.zhongmei.bty.basemodule.customer.message.MemberResetPswdResp;
import com.zhongmei.bty.basemodule.customer.message.MemberUpgradeReq;
import com.zhongmei.bty.basemodule.customer.message.MemberUpgradeResp;
import com.zhongmei.bty.basemodule.customer.message.MemberValidateCheckCodeReq;
import com.zhongmei.bty.basemodule.customer.message.MemberValidateCheckCodeResp;
import com.zhongmei.yunfu.bean.req.CustomerStoredBalanceReq;
import com.zhongmei.bty.basemodule.customer.message.MemberValuecardHistoryResp;
import com.zhongmei.bty.basemodule.customer.message.MemberVerifyPwdReq;
import com.zhongmei.bty.basemodule.customer.message.NewMemberIntegralInfoResp;
import com.zhongmei.bty.basemodule.customer.message.PaymentAndMemberReq;
import com.zhongmei.bty.basemodule.customer.message.PaymentAndMemberResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.AnonymousCardStorePayModeResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.AnonymousCardTradeReq;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.AnonymousCardTradeResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardAccountResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardActivateReq;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardActivateResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardActiveReqV2;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardActiveRespV2;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardBaseInfoResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardIntegralInfoReq;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardIntegralInfoResp;
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
import com.zhongmei.yunfu.bean.req.CustomerCardTimeStoreReq;
import com.zhongmei.yunfu.bean.req.CustomerCardTimeStoreResp;
import com.zhongmei.yunfu.bean.req.CustomerMemberStoreReq;
import com.zhongmei.yunfu.bean.req.CustomerMemberStoreResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CustomerSaleCardInvalidReq;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CustomerSaleCardInvalidResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CustomerSellcardDetailReq;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CustomerSellcardDetailResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CustomerSellcardReq;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CustomerSellcardResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.EntityCardChangeDetailResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.JCReturnAnonymousCardReq;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.JCReturnAnonymousCardResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.MemberCardResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.MemberCardsReq;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.MemberCardsResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.NewCardLoginResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.ReturnAnonymousCardReq;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.ReturnAnonymousCardResp;
import com.zhongmei.bty.basemodule.erp.bean.ErpCurrency;
import com.zhongmei.bty.basemodule.trade.bean.Reason;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.yunfu.bean.YFResponse;
import com.zhongmei.yunfu.bean.YFResponseList;
import com.zhongmei.yunfu.bean.req.CustomerListReq;
import com.zhongmei.yunfu.bean.req.CustomerListResp;
import com.zhongmei.yunfu.bean.req.CustomerCreateResp;
import com.zhongmei.yunfu.monitor.CalmResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.resp.YFResponseListener;
import com.zhongmei.yunfu.resp.data.LoyaltyMindTransferResp;
import com.zhongmei.yunfu.resp.data.LoyaltyTransferResp;

import org.json.JSONException;

import java.util.List;


public interface CustomerOperates extends IOperates {


    void editCustomer(CustomerResp customerNew, ResponseListener<CustomerEditResp> listener) throws JSONException;


    void login(CustomerLoginReq loginReq, ResponseListener<CustomerLoginResp> listener);


    void customerLogin(CustomerLoginReq loginReq, ResponseListener<MemberLoginVoResp> listener);

    void customerLogin(CustomerLoginReq loginReq, YFResponseListener<YFResponse<CustomerLoginResp>> listener);


    void getCustomerCoupons(MemberCouponsReq couponsReq, ResponseListener<MemberCouponsVoResp> listener);

    void getCustomerCoupons(MemberCouponsReq couponsReq, YFResponseListener<YFResponseList<CustomerCouponResp>> listener);


    void getCustomerCards(MemberCardsReq cardsReq, ResponseListener<LoyaltyTransferResp<MemberCardsResp>> listener);


    void memberRecharge(MemberRechargeReq rechargeReq, ResponseListener<MemberRechargeResp> listener);


    void cardRecharge(CardRechargeReq rechargeReq, ResponseListener<CardRechargeResp> listener);


    void findCustomerInfo(CustomerInfoReq couponinfoReq, ResponseListener<CustomerInfoResp> listener);


    void getCheckCode(MemberCheckCodeReq checkCodeReq, ResponseListener<MemberCheckCodeResp> listener);


    void validateCheckCode(MemberValidateCheckCodeReq validateCheckCodeReq,
                           ResponseListener<MemberValidateCheckCodeResp> listener);


    void modifyPassword(MemberResetPswdReq resetPswdReq, ResponseListener<MemberResetPswdResp> listener);


    void getValuecardHistory(CustomerStoredBalanceReq req, ResponseListener<MemberValuecardHistoryResp> listener);

    void getValuecardHistory(CustomerStoredBalanceReq req, YFResponseListener<YFResponseList<CustomerStoredBalanceResp>> listener);


    void upgradeCustomer(MemberUpgradeReq req, CustomerResp customer, ResponseListener<MemberUpgradeResp> listener);


    void createCustomer(CustomerResp customer, String uniqueCode, ResponseListener<MemberCreateResp> listener);




    void getValueIntegralHistory(MemberIntegralInfoReq req, ResponseListener<MemberIntegralInfoResp> listener);


    void getCardIntegralHistory(CardIntegralInfoReq req, ResponseListener<CardIntegralInfoResp> ensure);


    void cardLogin(String cardNo, ResponseListener<CardLoginResp> listener);


    void cardLoginNew(String cardNum, ResponseListener<LoyaltyMindTransferResp<NewCardLoginResp>> listener);


    void getMemberCards(Long customerId, String mobile, ResponseListener<MemberCardResp> listener);


    void cancelGetMemberCards();


    void getCustomerSellCardOrders(CustomerSellcardReq sellcardReq, ResponseListener<List<CustomerSellcardResp>> listener);


    void getEntityCardChangeOrders(CustomerSellcardReq entityCardChangeReq, ResponseListener<List<CustomerSellcardResp>> listener);

    void getCustomerSellCardDetailInfo(CustomerSellcardDetailReq sellcardReq, ResponseListener<CustomerSellcardDetailResp> listener);


    void getEntityCardChangeDetailInfo(Long tradeId, ResponseListener<EntityCardChangeDetailResp> listener);


    void getAnonymousEntityCardSellDetailInfo(Long tradeId, ResponseListener<CustomerSellcardDetailResp> listener);


    void rangeSearchCards(CardRangeSearchReq req, ResponseListener<CardRangeSearchResp> listener);




    void queryMemberByUniqueCode(String uniqueCode, ResponseListener<MemberCreateResp> listener);

    void singleSearchCards(CardSingleSearchReq req, ResponseListener<CardRangeSearchResp> listener);


    void singleSearchCardsByTrans(CardSingleSearchByTransReq req, ResponseListener<CardSingleSearchResp> listener);


    void activateCards(CardActivateReq req, ResponseListener<CardActivateResp> listener);


    void getCardStoreValueList(CustomerCardStoreValueReq cardStoreValueReq,
                               ResponseListener<CustomerCardStoreValueResp> listener);


    void getEntityCardStoreHistory(CustomerCardTimeStoreReq cardStoreValueReq,
                                   ResponseListener<List<CustomerCardTimeStoreResp>> listener);

    void getEntityCardStoreHistory(CustomerCardTimeStoreReq cardStoreValueReq,
                                   YFResponseListener<YFResponseList<CustomerCardTimeStoreResp>> listener);

    void queryPaymentAndMember(PaymentAndMemberReq cardStoreValueReq,
                               ResponseListener<PaymentAndMemberResp> listener);


    void queryPayment(String paymentUuid, ResponseListener<PaymentAndMemberResp> listener);

    void getMemberStoreValueList(CustomerMemberStoreValueReq req,
                                 ResponseListener<CustomerMemberStoreValueResp> listener);


    void getMemberStoreHistory(CustomerMemberStoreReq req,
                               ResponseListener<List<CustomerMemberStoreResp>> listener);

    void getMemberStoreHistory(CustomerMemberStoreReq req, YFResponseListener<YFResponseList<CustomerMemberStoreResp>> listener);

    void saleCardRecordInvaliate(CustomerSaleCardInvalidReq req,
                                 ResponseListener<CustomerSaleCardInvalidResp> listener);

    void cardStoreValueRevokeReq(CustomerOrderBean orderBean, Reason reason, ResponseListener<Object> listener);

    void memberStoreValueRevokeReq(CustomerMemberStoreValueRevokeReq req, ResponseListener<CustomerMemberStoreValueRevokeResp> listener);


    void anonymousEntityCardSaleRecordInvalid(Long tradeId, ResponseListener<CustomerSaleCardInvalidResp> listener);


    void getAnoymousCardTradeInfo(AnonymousCardTradeReq req, ResponseListener<AnonymousCardTradeResp> listener);


    void queryAnoymousCardStorePayMode(@NonNull String cardNo, ResponseListener<AnonymousCardStorePayModeResp> listener);


    void returnAnoymousCard(ReturnAnonymousCardReq req, ResponseListener<ReturnAnonymousCardResp> listener);


    void returnJCAnoymousCard(JCReturnAnonymousCardReq returnAnonymousCardReq, ResponseListener<JCReturnAnonymousCardResp> listener);


    void getCustomerById(Long customerId, boolean isNeedCredit, ResponseListener<com.zhongmei.bty.basemodule.customer.message.CustomerResp> listener);

    void getCustomerById(Long customerId, boolean isNeedCredit, YFResponseListener<YFResponse<CustomerResp>> listener);


    void getCustomerByCardNo(String cardNo, boolean isNeedCredit, YFResponseListener<YFResponse<CustomerResp>> listener);


    void customerBindCard(String cardNo,Long customerId, YFResponseListener<YFResponse<CustomerCreateResp>> listener);


    void cancelGetCustomerById();


    void findCustomerByPhone(String phone, ResponseListener<CustomerMobileVo> listener);


    void getCustomerByType(Long loginType, String loginId, ResponseListener<CustomerMobileVo> listener);



    void queryCustomerListByCondition(CustomerListReq req, ResponseListener<CustomerListVoResp> listener);

    void queryCustomerListByCondition(CustomerListReq req, YFResponseListener<YFResponseList<CustomerListResp>> listener);


    void cancelQueryCustomerListByCondition();


    void queryMemberAddressById(Long memberId, ResponseListener<MemberAddressResp> listener);


    void getCardBaseInfo(String cardNum, ResponseListener<CardBaseInfoResp> listener);


    void getCardAccount(String cardNum, ResponseListener<CardAccountResp> listener);


    void getCardInfoByNum(CardSingleSearchByTransReq req, ResponseListener<CustomerCardInfoResp> listener);


    void memberIntegralModification(Long customerId, int integral, Integer operateType, ResponseListener<MemberIntegralModificationResp> listener);


    void newMemberIntegralInfo(Long customerId, Integer currentPage, Long lastId, ResponseListener<NewMemberIntegralInfoResp> listener);

    void newMemberIntegralInfo(Long customerId, Integer currentPage, Long lastId, YFResponseListener<YFResponse<CustomerIntegralResp>> listener);


    void bindCardInstance(CardActivateReq req, ResponseListener<CardActivateResp> listener);


    void activateCardsV2(CardActiveReqV2 req, ResponseListener<CardActiveRespV2> listener);


    void createCustomerV2(CustomerResp customer, String uniqueCode, ResponseListener<MemberCreateResp> listener);

    void saveCustomer(CustomerResp customer, YFResponseListener<YFResponse<CustomerCreateResp>> listener);


    void createMemberByPresetCustomer(CustomerResp customer, String uniqueCode, ResponseListener<MemberCreateResp> listener);

    void createMemberByPresetCustomer(CustomerResp customer, String uniqueCode, YFResponseListener<YFResponse<CustomerCreateResp>> listener);



    void bindCustomerFaceCode(Long customerId, String faceId, ResponseListener<BindCustomerFaceCodeResp> listener);


    void memberModifyLevel(FragmentActivity fragment, Long customerId, Long levelId, CalmResponseListener<ResponseObject<LoyaltyTransferResp>> listener);


    void validationMemberPwd(FragmentActivity fragment, MemberVerifyPwdReq req, CalmResponseListener<ResponseObject<LoyaltyTransferResp>> listener);


    void memberModifyMobile(FragmentActivity fragment, Long customerId, String oldMobile, String newMobile, ErpCurrency currency, CalmResponseListener<ResponseObject<LoyaltyTransferResp>> listener);


    void memberCreditList(FragmentActivity fragment, MemberCreditListReq req, boolean isShowLoading, CalmResponseListener<ResponseObject<MemberCreditListResp>> listener);

    void getSendCouponList(FragmentActivity fragment, CalmResponseListener<ResponseObject<CustomerDirectCouponListV2Resp>> listener);

    void getCustomTimes(CustomTimesReq req, boolean isShowLoading, CalmResponseListener<ResponseObject<CustomTimesResp>> listener);
}
