package com.zhongmei.bty.mobilepay;

import android.text.TextUtils;

import com.zhongmei.bty.basemodule.commonbusiness.utils.BusinessTypeUtils;
import com.zhongmei.bty.basemodule.discount.message.CouponUrlResp;
import com.zhongmei.bty.basemodule.trade.operates.TradeOperates;
import com.zhongmei.OSLog;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.data.operate.OperatesRetailFactory;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.bty.mobilepay.v1.event.ShowCouponUrlEvent;

import de.greenrobot.event.EventBus;

/**
 * @Date： 16/9/26
 * @Description:
 * @Version: 1.0
 */
public class GetCouponUrlThread extends Thread {

    @Override
    public void run() {
        super.run();
        TradeOperates tradeOperate = BusinessTypeUtils.isRetail() ? OperatesRetailFactory.create(TradeOperates.class) : OperatesFactory.create(TradeOperates.class);
        tradeOperate.getSendCouponUrl(new ResponseListener<CouponUrlResp>() {
            @Override
            public void onResponse(ResponseObject<CouponUrlResp> response) {
                if (ResponseObject.isOk(response) && !TextUtils.isEmpty(response.getContent().getUrl())) {
                    EventBus.getDefault().post(new ShowCouponUrlEvent(response.getContent().getUrl()));
                } else {
                    OSLog.info(response.getMessage());
                }
            }

            @Override
            public void onError(VolleyError error) {
                OSLog.error(error.getMessage());
            }
        });

    }
}
