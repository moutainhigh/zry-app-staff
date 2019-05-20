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

/**
 * @version: 1.0
 * @date 2015年5月8日
 */
public interface CustomerOperates extends IOperates {

    /**
     * 编辑会员
     */
    void editCustomer(CustomerResp customerNew, ResponseListener<CustomerEditResp> listener) throws JSONException;

    /**
     * 会员登录。此方法不阻塞调用线程
     *
     * @param loginReq
     * @param listener
     */
    void login(CustomerLoginReq loginReq, ResponseListener<CustomerLoginResp> listener);

    /**
     * 查询会员信息
     *
     * @param loginReq
     * @param listener
     */
    void customerLogin(CustomerLoginReq loginReq, ResponseListener<MemberLoginVoResp> listener);

    void customerLogin(CustomerLoginReq loginReq, YFResponseListener<YFResponse<CustomerLoginResp>> listener);

    /**
     * 查询会员优惠卡卷
     *
     * @param couponsReq
     * @param listener
     */
    void getCustomerCoupons(MemberCouponsReq couponsReq, ResponseListener<MemberCouponsVoResp> listener);

    void getCustomerCoupons(MemberCouponsReq couponsReq, YFResponseListener<YFResponseList<CustomerCouponResp>> listener);

    /**
     * 获取会员实体卡列表
     *
     * @param cardsReq
     * @param listener
     */
    void getCustomerCards(MemberCardsReq cardsReq, ResponseListener<LoyaltyTransferResp<MemberCardsResp>> listener);

    /**
     * 会员充值。此方法不阻塞调用线程
     *
     * @param rechargeReq
     * @param listener
     */
    void memberRecharge(MemberRechargeReq rechargeReq, ResponseListener<MemberRechargeResp> listener);

    /**
     * 实体卡充值。此方法不阻塞调用线程
     *
     * @param rechargeReq
     * @param listener
     */
    void cardRecharge(CardRechargeReq rechargeReq, ResponseListener<CardRechargeResp> listener);

    /**
     * 查询会员优惠券信息。此方法不阻塞调用线程
     *
     * @param couponinfoReq
     * @param listener
     */
    void findCustomerInfo(CustomerInfoReq couponinfoReq, ResponseListener<CustomerInfoResp> listener);

    /**
     * 获取短信验证码
     *
     * @param checkCodeReq
     * @param listener
     */
    void getCheckCode(MemberCheckCodeReq checkCodeReq, ResponseListener<MemberCheckCodeResp> listener);

    /**
     * 校验短信验证码
     *
     * @param validateCheckCodeReq
     * @param listener
     */
    void validateCheckCode(MemberValidateCheckCodeReq validateCheckCodeReq,
                           ResponseListener<MemberValidateCheckCodeResp> listener);

    /**
     * 修改会员密码
     *
     * @param resetPswdReq
     * @param listener
     */
    void modifyPassword(MemberResetPswdReq resetPswdReq, ResponseListener<MemberResetPswdResp> listener);

    /**
     * 获取会员充值列表
     *
     * @param req
     * @param listener
     */
    void getValuecardHistory(CustomerStoredBalanceReq req, ResponseListener<MemberValuecardHistoryResp> listener);

    void getValuecardHistory(CustomerStoredBalanceReq req, YFResponseListener<YFResponseList<CustomerStoredBalanceResp>> listener);

    /**
     * 会员升级
     */
    void upgradeCustomer(MemberUpgradeReq req, CustomerResp customer, ResponseListener<MemberUpgradeResp> listener);

    /**
     * 创建会员
     *
     * @param customer
     * @param uniqueCode 旧会员卡号
     * @param listener
     */
    void createCustomer(CustomerResp customer, String uniqueCode, ResponseListener<MemberCreateResp> listener);

//    /**
//     * 创建会员
//     *
//     * @param customer
//     * @param uniqueCode 旧会员卡号
//     * @param listener
//     */
//    @Deprecated
//    void createCustomer(CustomerNew customer, String uniqueCode, ResponseListener<MemberCreateResp> listener);

    /**
     * 获取会员积分列表
     */

    void getValueIntegralHistory(MemberIntegralInfoReq req, ResponseListener<MemberIntegralInfoResp> listener);

    /**
     * 获取实体卡积分列表
     *
     * @param req
     * @param ensure
     */
    void getCardIntegralHistory(CardIntegralInfoReq req, ResponseListener<CardIntegralInfoResp> ensure);

    /**
     * 会员卡登录
     *
     * @Title: cardLogin
     * @Param @param cardNo
     * @Return void 返回类型
     */
    void cardLogin(String cardNo, ResponseListener<CardLoginResp> listener);

    /**
     * 新的实体卡登录，返回对应会员下其他的实体卡列表
     *
     * @param cardNum
     * @param listener
     */
    void cardLoginNew(String cardNum, ResponseListener<LoyaltyMindTransferResp<NewCardLoginResp>> listener);

    /**
     * 获取会员卡
     *
     * @param mobile   电话
     * @param listener
     */
    void getMemberCards(Long customerId, String mobile, ResponseListener<MemberCardResp> listener);

    /**
     * 取消获取会员卡
     */
    void cancelGetMemberCards();

    /**
     * 获取实体卡(会员实体卡、匿名实体卡)销售记录
     *
     * @param sellcardReq
     * @param listener
     */
    void getCustomerSellCardOrders(CustomerSellcardReq sellcardReq, ResponseListener<List<CustomerSellcardResp>> listener);

    /**
     * 获取实体卡（会员卡、权益卡）换卡记录
     */
    void getEntityCardChangeOrders(CustomerSellcardReq entityCardChangeReq, ResponseListener<List<CustomerSellcardResp>> listener);

    void getCustomerSellCardDetailInfo(CustomerSellcardDetailReq sellcardReq, ResponseListener<CustomerSellcardDetailResp> listener);

    /**
     * 获取实体卡（会员卡、权益卡）换卡详情
     */
    void getEntityCardChangeDetailInfo(Long tradeId, ResponseListener<EntityCardChangeDetailResp> listener);

    /**
     * 获取匿名实体卡销售详细信息
     *
     * @param tradeId  交易Id
     * @param listener
     */
    void getAnonymousEntityCardSellDetailInfo(Long tradeId, ResponseListener<CustomerSellcardDetailResp> listener);

    /**
     * @Description: 根据卡号范围查找卡
     * @Return void 返回类型
     */
    void rangeSearchCards(CardRangeSearchReq req, ResponseListener<CardRangeSearchResp> listener);

    /**

     * @Description: 根据卡号范围查找卡
     * @Return void 返回类型
     */

    /**
     * 查询旧会员信息
     *
     * @param uniqueCode 旧会员唯一识别号
     * @param listener
     */
    void queryMemberByUniqueCode(String uniqueCode, ResponseListener<MemberCreateResp> listener);

    void singleSearchCards(CardSingleSearchReq req, ResponseListener<CardRangeSearchResp> listener);

    /**
     * 会员卡单卡查询，修改上传参数为数组
     *
     * @param req
     * @param listener
     */
    void singleSearchCardsByTrans(CardSingleSearchByTransReq req, ResponseListener<CardSingleSearchResp> listener);

    /**
     * @Description: 激活会员实体卡
     * @Return void 返回类型
     */
    void activateCards(CardActivateReq req, ResponseListener<CardActivateResp> listener);

    /**
     * @param cardStoreValueReq
     * @param listener
     * @Date 2016年3月18日
     * @Description: 查询实体卡(会员实体卡 、 匿名实体卡)储值列表
     * @Return void
     */
    void getCardStoreValueList(CustomerCardStoreValueReq cardStoreValueReq,
                               ResponseListener<CustomerCardStoreValueResp> listener);

    /**
     * 获取门店实体卡/匿名卡储值记录列表
     *
     * @param cardStoreValueReq
     * @param listener
     */
    void getEntityCardStoreHistory(CustomerCardTimeStoreReq cardStoreValueReq,
                                   ResponseListener<List<CustomerCardTimeStoreResp>> listener);

    void getEntityCardStoreHistory(CustomerCardTimeStoreReq cardStoreValueReq,
                                   YFResponseListener<YFResponseList<CustomerCardTimeStoreResp>> listener);

    void queryPaymentAndMember(PaymentAndMemberReq cardStoreValueReq,
                               ResponseListener<PaymentAndMemberResp> listener);

    /**
     * 根据paymentUuid查询订单相关信息
     *
     * @param paymentUuid
     * @param listener
     */
    void queryPayment(String paymentUuid, ResponseListener<PaymentAndMemberResp> listener);

    void getMemberStoreValueList(CustomerMemberStoreValueReq req,
                                 ResponseListener<CustomerMemberStoreValueResp> listener);

    /**
     * 获取门店会员储值记录列表
     *
     * @param req
     * @param listener
     */
    void getMemberStoreHistory(CustomerMemberStoreReq req,
                               ResponseListener<List<CustomerMemberStoreResp>> listener);

    void getMemberStoreHistory(CustomerMemberStoreReq req, YFResponseListener<YFResponseList<CustomerMemberStoreResp>> listener);

    void saleCardRecordInvaliate(CustomerSaleCardInvalidReq req,
                                 ResponseListener<CustomerSaleCardInvalidResp> listener);

    void cardStoreValueRevokeReq(CustomerOrderBean orderBean, Reason reason, ResponseListener<Object> listener);

    void memberStoreValueRevokeReq(CustomerMemberStoreValueRevokeReq req, ResponseListener<CustomerMemberStoreValueRevokeResp> listener);

    /**
     * 匿名实体卡销售记录作废
     *
     * @param tradeId  订单交易Id
     * @param listener
     */
    void anonymousEntityCardSaleRecordInvalid(Long tradeId, ResponseListener<CustomerSaleCardInvalidResp> listener);

    /**
     * 匿名实体卡最近一条交易信息
     *
     * @param req
     * @param listener
     */
    void getAnoymousCardTradeInfo(AnonymousCardTradeReq req, ResponseListener<AnonymousCardTradeResp> listener);

    /**
     * 查询匿名卡储值支付方式
     *
     * @param cardNo   临时卡卡号
     * @param listener 回调listener
     */
    void queryAnoymousCardStorePayMode(@NonNull String cardNo, ResponseListener<AnonymousCardStorePayModeResp> listener);

    /**
     * 匿名卡退卡
     */
    void returnAnoymousCard(ReturnAnonymousCardReq req, ResponseListener<ReturnAnonymousCardResp> listener);

    /**
     * 金城匿名卡退卡
     */
    void returnJCAnoymousCard(JCReturnAnonymousCardReq returnAnonymousCardReq, ResponseListener<JCReturnAnonymousCardResp> listener);

    /**
     * 根据ID查询会员信息
     *
     * @param customerId   用户ID
     * @param isNeedCredit 是否需要挂账信息  true - 需要 ，false - 不需要
     */
    void getCustomerById(Long customerId, boolean isNeedCredit, ResponseListener<com.zhongmei.bty.basemodule.customer.message.CustomerResp> listener);

    void getCustomerById(Long customerId, boolean isNeedCredit, YFResponseListener<YFResponse<CustomerResp>> listener);

    /**
     * 根据会员卡
     * @param cardNo
     * @param isNeedCredit
     * @param listener
     */
    void getCustomerByCardNo(String cardNo, boolean isNeedCredit, YFResponseListener<YFResponse<CustomerResp>> listener);

    /**
     * 绑定会员卡
     * @param cardNo
     * @param customerId
     * @param listener
     */
    void customerBindCard(String cardNo,Long customerId, YFResponseListener<YFResponse<CustomerCreateResp>> listener);

    /**
     * 取消根据ID查询会员信息
     */
    void cancelGetCustomerById();

    /**
     * 根据手机号查找会员信息
     *
     * @param phone
     * @param listener
     */
    void findCustomerByPhone(String phone, ResponseListener<CustomerMobileVo> listener);

    /**
     * 根据类型查询顾客信息
     *
     * @param loginType 0手机号 1微信OPENID 102顾客customerId
     * @param loginId   手机号，微信，顾客ID
     * @param listener
     */
    void getCustomerByType(Long loginType, String loginId, ResponseListener<CustomerMobileVo> listener);

//    /**
//     * 条件查询顾客列表
//     * @param nameOrMobile 姓名或手机号
//     * @param cardNum  实体卡号
//     * @param openId 微信openid
//     * @param groupId 分组id
//     * @param customerType 顾客类型 1 顾客；2 会员
//     * @param levelId 会员等级id
//     * @param pageSize 每页展示条数
//     * @param currentPage 当前页码
//     * @param isCache 是否使用缓存数据 true 使用  false 不使用
//     * @param minCustomerId 最后一条数据的id，用于去重复
//     * @param listener 数据回调监听
//     */
//    void queryCustomerListByCondition(String nameOrMobile, String cardNum, String openId, String groupId, String customerType, String levelId, int pageSize, int currentPage ,boolean isCache , Long minCustomerId, ResponseListener<CustomerListVoResp> listener);

    /**
     * 条件查询顾客列表
     *
     * @param req      查询条件
     * @param listener 数据回调监听
     */
    void queryCustomerListByCondition(CustomerListReq req, ResponseListener<CustomerListVoResp> listener);

    void queryCustomerListByCondition(CustomerListReq req, YFResponseListener<YFResponseList<CustomerListResp>> listener);

    /**
     * 取消条件查询顾客列表
     */
    void cancelQueryCustomerListByCondition();

    /**
     * 通过memberId查询会员常用地址
     */
    void queryMemberAddressById(Long memberId, ResponseListener<MemberAddressResp> listener);

    /**
     * 查询卡基本信息
     */
    void getCardBaseInfo(String cardNum, ResponseListener<CardBaseInfoResp> listener);

    /**
     * 通过实体卡卡号获取卡账户
     *
     * @param cardNum
     * @param listener
     */
    void getCardAccount(String cardNum, ResponseListener<CardAccountResp> listener);

    /**
     * 根据根据卡号精确查询卡信息接口
     */
    void getCardInfoByNum(CardSingleSearchByTransReq req, ResponseListener<CustomerCardInfoResp> listener);

    /**
     * 会员积分的补录/扣除
     */
    void memberIntegralModification(Long customerId, int integral, Integer operateType, ResponseListener<MemberIntegralModificationResp> listener);

    /**
     * 新会员积分流水接口
     */
    void newMemberIntegralInfo(Long customerId, Integer currentPage, Long lastId, ResponseListener<NewMemberIntegralInfoResp> listener);

    void newMemberIntegralInfo(Long customerId, Integer currentPage, Long lastId, YFResponseListener<YFResponse<CustomerIntegralResp>> listener);

    /**
     * 即售即用实体卡绑定已有顾客接口
     */
    void bindCardInstance(CardActivateReq req, ResponseListener<CardActivateResp> listener);

    /**
     * @Description: 激活会员实体卡
     * @Return void 返回类型
     */
    void activateCardsV2(CardActiveReqV2 req, ResponseListener<CardActiveRespV2> listener);

    /**
     * 创建会员
     *
     * @param customer
     * @param uniqueCode 旧会员卡号
     * @param listener
     */
    void createCustomerV2(CustomerResp customer, String uniqueCode, ResponseListener<MemberCreateResp> listener);

    void saveCustomer(CustomerResp customer, YFResponseListener<YFResponse<CustomerCreateResp>> listener);

    /**
     * 通过预制顾客创建会员
     * <p>
     *
     * @param customer
     * @param uniqueCode 旧会员卡号
     * @param listener
     */
    void createMemberByPresetCustomer(CustomerResp customer, String uniqueCode, ResponseListener<MemberCreateResp> listener);

    void createMemberByPresetCustomer(CustomerResp customer, String uniqueCode, YFResponseListener<YFResponse<CustomerCreateResp>> listener);


    /**
     * 绑定人脸
     *
     * @param customerId
     * @param faceId
     */
    void bindCustomerFaceCode(Long customerId, String faceId, ResponseListener<BindCustomerFaceCodeResp> listener);

    /**
     * 修改会员等级
     *
     * @param fragment
     * @param customerId
     * @param levelId
     * @param listener
     */
    void memberModifyLevel(FragmentActivity fragment, Long customerId, Long levelId, CalmResponseListener<ResponseObject<LoyaltyTransferResp>> listener);

    /**
     * 验证会员密码
     *
     * @param fragment
     * @param req
     * @param listener
     */
    void validationMemberPwd(FragmentActivity fragment, MemberVerifyPwdReq req, CalmResponseListener<ResponseObject<LoyaltyTransferResp>> listener);

    /**
     * 修改手机号
     *
     * @param fragment
     * @param
     * @param listener
     */
    void memberModifyMobile(FragmentActivity fragment, Long customerId, String oldMobile, String newMobile, ErpCurrency currency, CalmResponseListener<ResponseObject<LoyaltyTransferResp>> listener);


    void memberCreditList(FragmentActivity fragment, MemberCreditListReq req, boolean isShowLoading, CalmResponseListener<ResponseObject<MemberCreditListResp>> listener);

    void getSendCouponList(FragmentActivity fragment, CalmResponseListener<ResponseObject<CustomerDirectCouponListV2Resp>> listener);

    void getCustomTimes(CustomTimesReq req, boolean isShowLoading, CalmResponseListener<ResponseObject<CustomTimesResp>> listener);
}
