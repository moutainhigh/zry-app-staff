package com.zhongmei.bty.cashier.ordercenter.view;

import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.bty.basemodule.trade.message.AddFeeResp;
import com.zhongmei.bty.snack.orderdish.view.MvpView;

/**
 * Created by demo on 2018/12/15
 */

public interface AddFeeView extends MvpView {

    void onSuccess(AddFeeResp addFeeResp);

    void onError(VolleyError error);
}
