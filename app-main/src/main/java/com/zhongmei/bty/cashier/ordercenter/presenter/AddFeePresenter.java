package com.zhongmei.bty.cashier.ordercenter.presenter;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.bty.basemodule.trade.message.AddFeeResp;
import com.zhongmei.bty.cashier.ordercenter.presenter.IAddFeeContract.IAddFeePresenter;
import com.zhongmei.bty.cashier.ordercenter.presenter.IAddFeeContract.IAddFeeView;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.bty.basemodule.trade.operates.TradeOperates;
import com.zhongmei.yunfu.resp.data.GatewayTransferResp;

import java.util.Locale;


public class AddFeePresenter implements IAddFeePresenter {

    IAddFeeView iView;

    boolean hasPoint = false;

    int total = 0;

    int point = 0;

    int limit = 30;

    TradeOperates operates;

    @Override
    public void attachView(IAddFeeView view) {
        iView = view;
        operates = OperatesFactory.create(TradeOperates.class);
    }

    @Override
    public void detachView() {
        operates = null;
        iView = null;
    }

    @Override
    public void minusClick() {
        total--;
        if (total <= 0) {
            total = 0;
        }
        iView.setMinusEnable(total > 0);
        iView.setPlusEnable(true);
        iView.setFeeValue(feeValue());
    }

    @Override
    public void plusClick() {
        total++;
        if (total >= limit) {
            hasPoint = false;
            total = limit;
            point = 0;
        }
        iView.setMinusEnable(true);
        iView.setPlusEnable(total < limit);
        iView.setFeeValue(feeValue());
    }

    @Override
    public void sureClick(Long deliveryOrderId, Integer deliveryPlatform) {
        if (deliveryOrderId == null || deliveryPlatform == null) {
            return;
        }
        if (total == 0 && point == 0) {
            ToastUtil.showShortToast(R.string.order_center_add_fee_empty_error);
            return;
        }
        if (total > limit || (total == limit && point > 0)) {
            ToastUtil.showShortToast(R.string.order_center_add_fee_max_error);
            return;
        }
                Double amount = total + point / 10d;

        ResponseListener<GatewayTransferResp<AddFeeResp>> listener = LoadingResponseListener
                .ensure(new ResponseListener<GatewayTransferResp<AddFeeResp>>() {
                    @Override
                    public void onResponse(ResponseObject<GatewayTransferResp<AddFeeResp>> response
                    ) {
                        if (ResponseObject.isOk(response)) {
                            if (response.getContent() != null) {
                                ToastUtil.showShortToast(response.getContent().getMessage());
                                if (response.getContent().isOk()) {
                                    iView.dismissDialog();
                                }
                            }
                        } else {
                            ToastUtil.showShortToast(response.getMessage());
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        ToastUtil.showShortToast(error.getMessage());
                    }
                }, iView.getViewFragmentManager());
        operates.requestAddFee(deliveryOrderId, deliveryPlatform, amount, listener);
    }

    @Override
    public void numClick(int num) {
        switch (num) {
            case -1:                 if (hasPoint) {
                    return;
                }
                if (total >= limit) {
                    ToastUtil.showShortToast(R.string.order_center_add_fee_max_error);
                    return;
                }
                hasPoint = true;
                break;
            case -2:                 if (hasPoint && point != 0) {
                    point = 0;
                } else if (hasPoint) {
                    hasPoint = false;
                } else {
                    total = total / 10;
                }
                break;
            default:
                if (hasPoint) {
                    int result = point * 10 + num;
                    if (result >= 10) {
                                                ToastUtil.showShortToast(R.string.order_center_add_fee_limit_error);
                    } else {
                        point = num;
                    }
                } else {
                    int result = total * 10 + num;
                    if (result > limit) {
                        ToastUtil.showShortToast(R.string.order_center_add_fee_max_error);
                    } else {
                        total = result;
                    }
                }
                break;
        }
                checkValueLimit();
    }


    @SuppressWarnings("WeakerAccess")
    void checkValueLimit() {
        if (total > limit || (total == limit && (point > 0 || (point == 0 && hasPoint)))) {
            hasPoint = false;
            total = limit;
            point = 0;
        }
                iView.setMinusEnable(total > 0 || (total == 0 && point > 0));
        iView.setPlusEnable(total < limit);
        iView.setFeeValue(feeValue());
    }


    String feeValue() {
        if (hasPoint && point == 0) {
            return String.format(Locale.CHINA, ShopInfoCfg.getInstance().getCurrencySymbol() + "%d.", total);
        } else if (hasPoint) {
            return String.format(Locale.CHINA, ShopInfoCfg.getInstance().getCurrencySymbol() + "%d.%d", total, point);
        }
        return String.format(Locale.CHINA, ShopInfoCfg.getInstance().getCurrencySymbol() + "%d", total);
    }
}
