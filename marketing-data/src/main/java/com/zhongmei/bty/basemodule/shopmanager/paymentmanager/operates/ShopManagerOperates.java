package com.zhongmei.bty.basemodule.shopmanager.paymentmanager.operates;

import com.zhongmei.bty.basemodule.pay.message.PaymentsInputReq;
import com.zhongmei.bty.basemodule.pay.message.PaymentsInputResp;
import com.zhongmei.bty.basemodule.shopmanager.paymentmanager.operators.PaymentsDetailResp;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.yunfu.resp.ResponseListener;

import java.util.List;

/**
 * @Date： 2016/7/21
 * @Description:门店管理相关操作
 * @Version: 1.0
 */
public interface ShopManagerOperates extends IOperates {

    /**
     * 收支录入
     */
    void addPayments(List<PaymentsInputReq> req, ResponseListener<PaymentsInputResp> listener);

    /**
     * 收支详情
     */
    void getPaymentsDetail(String startDate, String endDate, ResponseListener<PaymentsDetailResp> listener);
}
