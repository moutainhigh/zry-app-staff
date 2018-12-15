package com.zhongmei.beauty.pay;

import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCard;
import com.zhongmei.bty.basemodule.pay.message.PayResp;

/**
 * Created by demo on 2018/12/15
 */

public class BeautyPayPrintUtil {

    /**
     * 会员支付打印
     *
     * @param customer
     * @param ecCard
     * @param resp
     * @throws Exception
     */
    public static void memberPayPrint(CustomerResp customer, EcCard ecCard, PayResp resp) throws Exception {
        /*if (resp.getPrintOperations() == null || resp.getPrintOperations().isEmpty()) {
            return;
        }
        String tmp = resp.getPrintOperations().get(0).getExtendsStr();
        JSONObject extendsStr = new JSONObject(tmp);
        double subValue = extendsStr.optDouble("subValue", 0);
        double beforeActualvalue = extendsStr.optDouble("beforeActualvalue", 0);
        double beforeSendValue = extendsStr.optDouble("beforeSendValue", 0);
        double currentReduceActualValue = extendsStr.optDouble("currentReduceActualValue", 0);
        double currentReduceSendValue = extendsStr.optDouble("currentReduceSendValue", 0);
        ChargingPrint print = new ChargingPrint();
        if (ecCard != null) {
            print.setCardNum(ecCard.getCardNum());
            print.setCustomerName(ecCard.getCustomer().getName());
            print.setCustomerSex(String.valueOf(ecCard.getCustomer().getSex()));
            print.setPhoneNo(ecCard.getCustomer().getMobile());
        } else if (customer != null) {
            print.setCustomerName(customer.customerName);
            print.setCustomerSex(customer.sex + "");
            print.setPhoneNo(customer.mobile);
        }
        print.setChargingType(2);
        print.setTrueIncomeValuecard(BigDecimal.valueOf(currentReduceActualValue));
        print.setBeforeValuecard(BigDecimal.valueOf(beforeActualvalue + beforeSendValue));
        print.setCommercialName(ShopInfoCfg.getInstance().commercialName);
        print.setUserId(Session.getAuthUser().getName());
        print.setChargeValuecard(BigDecimal.valueOf(subValue));
        print.setCustomerIntegral(null);
        print.setEndValuecard(print.getBeforeValuecard().subtract(print.getChargeValuecard()));

        print.setPresentStart(BigDecimal.valueOf(beforeSendValue));
        print.setPresentEnd(BigDecimal.valueOf(beforeSendValue - currentReduceSendValue));
        print.setCapitalStart(BigDecimal.valueOf(beforeActualvalue));
        print.setCapitalEnd(BigDecimal.valueOf(beforeActualvalue - currentReduceActualValue));

        print.setPayMethods(null);
        print.setChargingTime(resp.getTrades().get(0).getServerUpdateTime());
        //  PrintContentQueue.getInstance().printCardOrMemberCharge(print,false,new OnSimplePrintListener(PrintTicketTypeEnum.STORE_OUT));
        PRTPrintContentQueue.getCommonPrintQueue()
                .printStoreTicket(PRTStoreBeanConvertHelper.createStoreBean(print, false), new PRTOnSimplePrintListener(PrintTicketTypeEnum.STORE_OUT));*/
    }

    /**
     * 打印押金单
     */
    public static void printDepositTicket(final String tradeUuid) {
        /*TaskContext.execute(new SimpleAsyncTask<PRTCashierDepositOrder>() {
            @Override
            public PRTCashierDepositOrder doInBackground(Void... params) {
                return PRTCashierDepositOrderHelper.createPRTCashierDepositOrder(tradeUuid);
            }

            @Override
            public void onPostExecute(PRTCashierDepositOrder prtCashierDepositOrder) {
                super.onPostExecute(prtCashierDepositOrder);
                PRTPrintContentQueue.getSnackPrintQueue()
                        .printDepositTicket(prtCashierDepositOrder, false, new PRTOnSimplePrintListener(PrintTicketTypeEnum.DEPOSIT, false));
            }
        });*/
    }
}
