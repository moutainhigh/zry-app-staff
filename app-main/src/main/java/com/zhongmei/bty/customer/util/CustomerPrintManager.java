package com.zhongmei.bty.customer.util;

import android.text.TextUtils;

import com.zhongmei.bty.basemodule.customer.bean.ChargingPrint;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 顾客打印相关
 * <p>
 * Created by demo on 2018/12/15
 */
public class CustomerPrintManager {

    /**
     * 补打消费储值单
     *
     * @param customerName     顾客姓名
     * @param customerSex      顾客性别
     * @param customerMoblie   手机号
     * @param ecCardNum        实体卡
     * @param userName         经手人
     * @param chargingTime     充值时间
     * @param beforeRealValue  操作前余额实储金额
     * @param beforeSendValue  操作前余额赠送金额
     * @param currentRealValue 本次操作实储金额
     * @param currentSendValue 本次操作赠送金额
     * @param endRealValue     操作后实储金额
     * @param endSendValue     操作后赠送金额
     */
    public void rePrinCardOrtMemberConsume(String customerName, String customerSex, String customerMoblie, String ecCardNum, String userName,
                                           Long chargingTime,
                                           BigDecimal beforeRealValue,
                                           BigDecimal beforeSendValue,
                                           BigDecimal currentRealValue,
                                           BigDecimal currentSendValue,
                                           BigDecimal endRealValue,
                                           BigDecimal endSendValue) {
        currentRealValue = currentRealValue != null ? currentRealValue.abs() : new BigDecimal(BigInteger.ZERO); // 负数转正数
        currentSendValue = currentSendValue != null ? currentSendValue.abs() : new BigDecimal(BigInteger.ZERO); // 负数转正数
        ChargingPrint print = new ChargingPrint();
        print.setCustomerName(customerName);
        print.setCustomerSex(customerSex);
        print.setPhoneNo(customerMoblie);
        if (!TextUtils.isEmpty(ecCardNum)) {
            print.setCardNum(ecCardNum);
        }
        print.setChargingType(2);
        print.setCapitalStart(beforeRealValue);// 消费前  储值本金
        print.setPresentStart(beforeSendValue);// 消费前  赠送
        print.setBeforeValuecard(beforeRealValue.add(beforeSendValue));// 消费前  合计

        print.setTrueIncomeValuecard(currentRealValue); // 本次消费  储值本金
        print.setChargeValuecard(currentRealValue.add(currentSendValue)); // 本次消费  合计

        print.setCapitalEnd(endRealValue);// 消费后  本金
        print.setPresentEnd(endSendValue); // 消费后  赠送
        print.setEndValuecard(endRealValue.add(endSendValue)); // 消费后  合计

        print.setCommercialName(ShopInfoCfg.getInstance().commercialName);
        print.setUserId(userName); // 收银员name
        print.setCustomerIntegral(null);
        print.setChargingTime(chargingTime);

        //PrintTool.printCardOrMemberCharge(print, true, new PRTOnSimplePrintListener(PrintTicketTypeEnum.STORE_OUT));
    }
}
