package com.zhongmei.bty.snack.offline;

import android.content.Context;

import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.SourceChild;
import com.zhongmei.yunfu.net.RequestManager;



public class SnackImpl implements ISnack {
    private static final String TAG = Snack.class.getSimpleName() + "--->";

        @Override
    public boolean isOfflineTrade(TradeVo tradeVo) {
        return tradeVo != null && isOfflineTrade(tradeVo.getTrade());
    }

    @Override
    public boolean isOfflineTrade(Trade trade) {
        return trade != null
                && trade.getSourceChild() == SourceChild.OFFLINE;
    }


    @Override
    public boolean isOfflineEnable() {
        return false;
    }

    @Override
    public boolean isSnackBusiness(TradeVo tradeVo) {
        return tradeVo != null
                && tradeVo.getTrade() != null
                && isSnackBusiness(tradeVo.getTrade().getBusinessType());

    }

    @Override
    public boolean isSnackBusiness(BusinessType businessType) {
        return false;
    }

    @Override
    public boolean isSnackBusiness(Trade trade) {
        return isSnackBusiness(trade.getBusinessType());
    }

    @Override
    public boolean netWorkAvailable() {
        return true;
    }

    @Override
    public boolean netWorkUnavailable() {
        return false;
    }

    public SnackImpl(Context context) {


        RequestManager.init(context);
    }


    public void release() {
            }


}
