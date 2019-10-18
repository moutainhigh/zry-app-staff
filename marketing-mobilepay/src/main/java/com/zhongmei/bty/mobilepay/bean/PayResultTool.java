package com.zhongmei.bty.mobilepay.bean;

import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.bty.basemodule.pay.message.PayResp;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.basemodule.pay.bean.PaymentVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class PayResultTool {
    private PaymentVo mPaymentVo;    private PayResp mPayResp;    private List<PaymentItem> okList;
    private List<PaymentItem> errorList;
    private Map<String, PayResp.PItemResults> resultsMap;
    private double payOkAmount = 0;

    public PayResultTool(PaymentVo paymentVo, PayResp payResp) {
        mPaymentVo = paymentVo;
        mPayResp = payResp;
        dofillter();
    }

    public double getPayOkAmount() {
        return payOkAmount;
    }

    public List<PaymentItem> getPaymentItemList(boolean isPayOk) {
        if (isPayOk)
            return okList;
        else
            return errorList;
    }

    public PayResp.PItemResults getPItemResultsByPaymentItemUUid(String paymentItemUUid) {
        if (resultsMap != null) {
            return resultsMap.get(paymentItemUUid);
        } else
            return null;
    }

    private void dofillter() {
        if (mPaymentVo != null && mPayResp != null) {
            okList = new ArrayList<PaymentItem>(2);
            errorList = new ArrayList<PaymentItem>(2);
            resultsMap = new HashMap<String, PayResp.PItemResults>();
            payOkAmount = 0;
            if (!Utils.isEmpty(mPaymentVo.getPaymentItemList()) && !Utils.isEmpty(mPayResp.getPaymentItemResults())) {
                for (PaymentItem item : mPaymentVo.getPaymentItemList()) {
                    for (PayResp.PItemResults result : mPayResp.getPaymentItemResults()) {
                        if (item.getUuid().equals(result.getPaymentItemUuid())) {
                            resultsMap.put(item.getUuid(), result);
                            if (result.getResultStatus() == 1000) {
                                okList.add(item);
                                payOkAmount += item.getUsefulAmount().doubleValue();
                            } else {
                                errorList.add(item);
                            }
                            break;
                        }
                    }
                }
            }
        } else {
            okList = null;
            errorList = null;
            resultsMap = null;
            payOkAmount = 0;
        }
    }
}
