package com.zhongmei.bty.manager;

import android.content.res.Resources;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

import com.j256.ormlite.dao.Dao;
import com.zhongmei.bty.basemodule.commonbusiness.cache.PaySettingCache;
import com.zhongmei.bty.basemodule.commonbusiness.listener.SimpleResponseListener;
import com.zhongmei.bty.basemodule.customer.bean.ChargingPrint;
import com.zhongmei.bty.basemodule.customer.bean.CustomerOrderBean;
import com.zhongmei.bty.basemodule.customer.bean.PayMethod;
import com.zhongmei.bty.basemodule.customer.message.CustomerMemberStoreValueRevokeReq;
import com.zhongmei.bty.basemodule.customer.message.CustomerMemberStoreValueRevokeResp;
import com.zhongmei.bty.basemodule.customer.operates.CustomerOperates;
import com.zhongmei.bty.basemodule.devices.mispos.data.CardSaleInfo;
import com.zhongmei.bty.basemodule.devices.mispos.data.bean.CustomerSaleCardInfo;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CustomerSaleCardInvalidResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CustomerSellcardDetailResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.SalesCardReturnResp;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;
import com.zhongmei.bty.basemodule.print.entity.PrintOperation;
import com.zhongmei.bty.basemodule.trade.bean.Reason;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.db.entity.trade.Payment;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.enums.PrintOperationOpType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class EntityCardMananger {


    public static void memberStoreRevoke(final CustomerOrderBean mOrderBean, Reason reason,
                                         ResponseListener<CustomerMemberStoreValueRevokeResp> listener, FragmentManager fragmentManager) {
        if (mOrderBean == null) {
            return;
        }
        CustomerMemberStoreValueRevokeReq req = new CustomerMemberStoreValueRevokeReq();
        req.setClientCreateTime(System.currentTimeMillis());
        req.setCustomerId(mOrderBean.getCustomerId());
                req.setTradeId(mOrderBean.getTradeId());
        if (mOrderBean.getAddValue() != null) {
            req.setValue(mOrderBean.getAddValue().doubleValue());
        } else {
            req.setValue(0.00);
        }
        if (Session.getAuthUser() != null) {
            req.setUserId(Session.getAuthUser().getId());
        }
        if (reason != null) {
            req.setReasonId(reason.getId());
            req.setReasonContent(reason.getContent());
        }
        req.setServerUpdateTime(mOrderBean.getServerUpdateTime());
        req.setHistoryId(mOrderBean.getHistoryId());
        CustomerOperates customerOperates = OperatesFactory.create(CustomerOperates.class);
        customerOperates.memberStoreValueRevokeReq(req, LoadingResponseListener.ensure(listener, fragmentManager));
    }


    public static void printStoreValue(final CustomerOrderBean orderBean, final List<PaymentItem> payments, final int chargingType, final boolean isReprint) {
        if (orderBean == null) {
            return;
        }

        CustomerOperates customerOperates = OperatesFactory.create(CustomerOperates.class);
        customerOperates.getCustomerById(orderBean.getCustomerId(), false, new SimpleResponseListener<com.zhongmei.bty.basemodule.customer.message.CustomerResp>() {

            @Override
            public void onSuccess(ResponseObject<com.zhongmei.bty.basemodule.customer.message.CustomerResp> response) {
                CustomerResp customer = response.getContent().result;
                printStoreValue(customer, orderBean, payments, chargingType, isReprint);
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showLongToast(error.getMessage());
            }
        });
    }

    private static void printStoreValue(CustomerResp customer, CustomerOrderBean orderBean, List<PaymentItem> payments, int chargingType, boolean isReprint) {
        ChargingPrint print = new ChargingPrint();
        print.setCustomerName(customer.customerName);
        print.setCustomerSex(customer.sex + "");
        print.setPhoneNo(orderBean.getMobile());
        print.setChargingType(chargingType);
        DatabaseHelper helper = DBHelperManager.getHelper();
                List<PrintOperation> printOperationsListAll = null;
        try {
            Dao<PrintOperation, String> printOperationsDao = helper.getDao(PrintOperation.class);
            printOperationsListAll = printOperationsDao.queryBuilder()
                    .selectColumns(PrintOperation.$.extendsStr, PrintOperation.$.opType)
                    .where()
                    .eq(PrintOperation.$.opType, PrintOperationOpType.CUSTOMER_CHARGING)
                    .and().eq(PrintOperation.$.sourceTradeId, orderBean.getTradeId())
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (chargingType == 1) {
            if (isReprint && (printOperationsListAll == null || printOperationsListAll.isEmpty())) {
                print.setPresentEnd(orderBean.getEndSendValue());
                print.setPresentStart(orderBean.getBeforeSendValue());
                print.setCapitalEnd(orderBean.getEndRealValue());
                print.setCapitalStart(orderBean.getBeforeRealValue());

                BigDecimal currentRealValue = orderBean.getCurrentRealValue();
                BigDecimal currentSendValue = orderBean.getCurrentSendValue();

                currentRealValue = currentRealValue != null ? currentRealValue.abs() : new BigDecimal(BigInteger.ZERO);                 currentSendValue = currentSendValue != null ? currentSendValue.abs() : new BigDecimal(BigInteger.ZERO);
                print.setTrueIncomeValuecard(currentRealValue);                 print.setChargeValuecard(currentRealValue.add(currentSendValue));
                print.setEndValuecard(orderBean.getEndValue());
                print.setBeforeValuecard(orderBean.getBeforeValue());
            } else {
                try {
                    if (printOperationsListAll != null && !printOperationsListAll.isEmpty()) {
                        String tmp = printOperationsListAll.get(0).getExtendsStr();
                        JSONObject extendsStr = new JSONObject(tmp);
                        print.setPresentEnd(BigDecimal.valueOf(extendsStr.optDouble("beforeSendValue", 0)));
                        print.setPresentStart((BigDecimal.valueOf(extendsStr.optDouble("beforeSendValue", 0) + extendsStr.optDouble("currentSendValue", 0))));
                        print.setCapitalEnd(orderBean.getBeforeValue().subtract(BigDecimal.valueOf(extendsStr.optDouble("beforeSendValue", 0))));
                        print.setCapitalStart(orderBean.getEndValue().subtract(print.getPresentStart()));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                                print.setEndValuecard(orderBean.getBeforeValue());
                print.setBeforeValuecard(orderBean.getEndValue());
                print.setTrueIncomeValuecard(orderBean.getAddValue());
                print.setChargeValuecard(orderBean.getAddValue()
                        .add(orderBean.getSendValue() != null ? orderBean.getSendValue() : BigDecimal.ZERO));
            }


        } else {
            try {
                if (printOperationsListAll != null && !printOperationsListAll.isEmpty()) {
                    String tmp = printOperationsListAll.get(0).getExtendsStr();
                    JSONObject extendsStr = new JSONObject(tmp);
                    print.setPresentStart(BigDecimal.valueOf(extendsStr.optDouble("beforeSendValue", 0)));
                    print.setPresentEnd((BigDecimal.valueOf(extendsStr.optDouble("beforeSendValue", 0) + extendsStr.optDouble("currentSendValue", 0))));
                    print.setCapitalStart(orderBean.getBeforeValue().subtract(BigDecimal.valueOf(extendsStr.optDouble("beforeSendValue", 0))));
                    print.setCapitalEnd(orderBean.getEndValue().subtract(print.getPresentEnd()));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            print.setBeforeValuecard(orderBean.getBeforeValue());
            print.setEndValuecard(orderBean.getEndValue());

            print.setTrueIncomeValuecard(orderBean.getAddValue());
            print.setChargeValuecard(orderBean.getAddValue()
                    .add(orderBean.getSendValue() != null ? orderBean.getSendValue() : BigDecimal.ZERO));

        }
        if (orderBean.getServerUpdateTime() != null) {
            print.setChargingTime(orderBean.getServerUpdateTime());
        }


        if (isReprint) {
            print.setUserId(orderBean.getOperater());
        } else {
            print.setUserId(Session.getAuthUser().getName());
        }
        print.setCommercialName(ShopInfoCfg.getInstance().commercialName);
        print.setCustomerIntegral(orderBean.getIntegral() == null ? "0" : orderBean.getIntegral().toString());
        if (!TextUtils.isEmpty(orderBean.getCardNo())) {
            print.setCardNum(orderBean.getCardNo());
        }

        Resources res = MainApplication.getInstance().getResources();
        List<PayMethod> list = new ArrayList<PayMethod>();
        if (payments != null && !payments.isEmpty()) {
            for (PaymentItem item : payments) {
                if (item.getStatusFlag() == StatusFlag.VALID) {
                    String name = TextUtils.isEmpty(item.getPayModeName()) ? PaySettingCache.getPayModeNameByModeId(item.getPayModeId()) : item.getPayModeName();
                                        PayMethod payMethod = new PayMethod(name, item.getUsefulAmount());
                    if (chargingType == 1) {
                        payMethod.setChangeAmount(item.getChangeAmount().abs());
                        payMethod.setFaceAmount(item.getFaceAmount().abs());
                        payMethod.setUsefulAmount(item.getUsefulAmount().abs());
                        payMethod.setValue(item.getUsefulAmount().abs());
                    } else {
                        payMethod.setChangeAmount(item.getChangeAmount());
                        payMethod.setFaceAmount(item.getFaceAmount());
                        payMethod.setUsefulAmount(item.getUsefulAmount());
                    }
                    payMethod.setPaymentType(item.getPayModeId());
                    list.add(payMethod);
                }
            }
        } else {
            if (orderBean.getAddValueType() == 1) {
                String name = res.getString(R.string.cash_charge);
                PayMethod payMethod = new PayMethod(name, orderBean.getAddValue());
                list.add(payMethod);
            } else {
                String name = res.getString(R.string.card_charge);
                PayMethod payMethod = new PayMethod(name, orderBean.getAddValue());
                list.add(payMethod);
            }
        }
        print.setPayMethods(list);
                    }


    public static void printSaleCard(CustomerSellcardDetailResp resp) {
        if (resp == null || resp.getTradeItems() == null) {
            return;
        }
        List<TradeItemVo> tradeItemVos = new ArrayList<TradeItemVo>();
        for (TradeItem tradeItem : resp.getTradeItems()) {
            TradeItemVo tradeItemVo = new TradeItemVo();
            List<CardSaleInfo> temp = new ArrayList<CardSaleInfo>();
            tradeItemVo.setTradeItem(tradeItem);
            for (CustomerSaleCardInfo cardSaleInfo : resp.getCardSaleInfos()) {
                if (cardSaleInfo.getTradeItemId() == tradeItem.getId()) {
                    CardSaleInfo info = new CardSaleInfo();
                    info.setCardKindId(cardSaleInfo.getCardId());
                    info.setCardKindName(cardSaleInfo.getCardKind());
                    info.setCardNum(cardSaleInfo.getCardNo());
                    info.setCardCost(cardSaleInfo.getCardCost());
                    temp.add(info);
                }
            }
            tradeItemVo.setCardSaleInfos(temp);
            tradeItemVos.add(tradeItemVo);
        }
        printSaleCard(resp.getTrades(), tradeItemVos, resp.getPayments(), resp.getPaymentItems(), true);
    }


    public static void printSaleCard(SalesCardReturnResp resp) {
        if (resp == null) {
            return;
        }

        List<TradeItemVo> tradeItemVos = new ArrayList<TradeItemVo>();
        for (TradeItem tradeItem : resp.getTradeItems()) {
            TradeItemVo tradeItemVo = new TradeItemVo();
            List<CardSaleInfo> temp = new ArrayList<CardSaleInfo>();
            tradeItemVo.setTradeItem(tradeItem);
            for (CustomerSaleCardInfo cardSaleInfo : resp.getCardSaleInfos()) {
                if (cardSaleInfo.getTradeItemId() == tradeItem.getId()) {
                    CardSaleInfo info = new CardSaleInfo();
                    info.setCardKindId(cardSaleInfo.getCardId());
                    info.setCardKindName(cardSaleInfo.getCardKind());
                    info.setCardNum(cardSaleInfo.getCardNo());
                    info.setCardCost(cardSaleInfo.getCardCost());
                    temp.add(info);
                }
            }
            tradeItemVo.setCardSaleInfos(temp);
            tradeItemVos.add(tradeItemVo);
        }
        printSaleCard(resp.getTrades(), tradeItemVos, resp.getPayments(), resp.getPaymentItems(), false);
    }

    public static void printSaleCard(CustomerSaleCardInvalidResp resp) {
        if (resp == null) {
            return;
        }
        List<TradeItemVo> tradeItemVos = new ArrayList<TradeItemVo>();
        for (TradeItem tradeItem : resp.getTradeItems()) {
            TradeItemVo tradeItemVo = new TradeItemVo();
            List<CardSaleInfo> temp = new ArrayList<CardSaleInfo>();
            tradeItemVo.setTradeItem(tradeItem);
            for (CustomerSaleCardInfo cardSaleInfo : resp.getCardSaleInfos()) {
                if (cardSaleInfo.getTradeItemId() == tradeItem.getId()) {
                    CardSaleInfo info = new CardSaleInfo();
                    info.setCardKindId(cardSaleInfo.getCardId());
                    info.setCardKindName(cardSaleInfo.getCardKind());
                    info.setCardNum(cardSaleInfo.getCardNo());
                    info.setCardCost(cardSaleInfo.getCardCost());
                    temp.add(info);
                }
            }
            tradeItemVo.setCardSaleInfos(temp);
            tradeItemVos.add(tradeItemVo);
        }
        printSaleCard(resp.getTrades(), tradeItemVos, null, null, false);
    }


    public static void printSaleCard(List<Trade> trades, List<TradeItemVo> tradeItemVos, List<Payment> payments,
                                     List<PaymentItem> paymentItems, boolean isReprint) {
        TradeVo tradeVo = new TradeVo();
        if (trades != null && !trades.isEmpty()) {
            tradeVo.setTrade(trades.get(0));
        }
        tradeVo.setTradeItemList(tradeItemVos);
        List<PaymentVo> paymentVos = new ArrayList<PaymentVo>();
        PaymentVo paymentVo = new PaymentVo();
        if (payments != null && !payments.isEmpty()) {
            paymentVo.setPayment(payments.get(0));
        }

        List<PaymentItem> items = new ArrayList<PaymentItem>();
                if (tradeVo.getTrade() != null && paymentItems != null) {
            switch (tradeVo.getTrade().getTradeStatus()) {
                case CONFIRMED:
                case INVALID:
                    items = paymentItems;
                    break;
                case FINISH:
                    items = filterPaymentItems(paymentItems, TradePayStatus.PAID);
                    break;
                case RETURNED:
                    items =
                            filterPaymentItems(paymentItems,
                                    TradePayStatus.REFUND_FAILED,
                                    TradePayStatus.REFUNDED,
                                    TradePayStatus.REFUNDING);
                    break;
                default:
                    break;
            }
        }
        paymentVo.setPaymentItemList(items);
        paymentVos.add(paymentVo);


    }

    private static List<PaymentItem> filterPaymentItems(List<PaymentItem> paymentItems,
                                                        TradePayStatus... tradePayStatuses) {
        List<PaymentItem> items = new ArrayList<PaymentItem>();
        for (PaymentItem paymentItem : paymentItems) {
            for (TradePayStatus tradePayStatus : tradePayStatuses) {
                if (paymentItem.getPayStatus() == tradePayStatus) {
                    items.add(paymentItem);
                    break;
                }
            }
        }

        return items;
    }

}
