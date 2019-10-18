package com.zhongmei.bty.customer.util;

import android.text.TextUtils;

import com.zhongmei.bty.basemodule.customer.bean.ChargingPrint;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;

import java.math.BigDecimal;
import java.math.BigInteger;


public class CustomerPrintManager {


    public void rePrinCardOrtMemberConsume(String customerName, String customerSex, String customerMoblie, String ecCardNum, String userName,
                                           Long chargingTime,
                                           BigDecimal beforeRealValue,
                                           BigDecimal beforeSendValue,
                                           BigDecimal currentRealValue,
                                           BigDecimal currentSendValue,
                                           BigDecimal endRealValue,
                                           BigDecimal endSendValue) {
        currentRealValue = currentRealValue != null ? currentRealValue.abs() : new BigDecimal(BigInteger.ZERO);         currentSendValue = currentSendValue != null ? currentSendValue.abs() : new BigDecimal(BigInteger.ZERO);         ChargingPrint print = new ChargingPrint();
        print.setCustomerName(customerName);
        print.setCustomerSex(customerSex);
        print.setPhoneNo(customerMoblie);
        if (!TextUtils.isEmpty(ecCardNum)) {
            print.setCardNum(ecCardNum);
        }
        print.setChargingType(2);
        print.setCapitalStart(beforeRealValue);        print.setPresentStart(beforeSendValue);        print.setBeforeValuecard(beforeRealValue.add(beforeSendValue));
        print.setTrueIncomeValuecard(currentRealValue);         print.setChargeValuecard(currentRealValue.add(currentSendValue));
        print.setCapitalEnd(endRealValue);        print.setPresentEnd(endSendValue);         print.setEndValuecard(endRealValue.add(endSendValue));
        print.setCommercialName(ShopInfoCfg.getInstance().commercialName);
        print.setUserId(userName);         print.setCustomerIntegral(null);
        print.setChargingTime(chargingTime);

            }
}
