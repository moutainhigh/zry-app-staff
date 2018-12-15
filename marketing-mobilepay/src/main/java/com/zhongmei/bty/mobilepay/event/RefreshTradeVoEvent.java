package com.zhongmei.bty.mobilepay.event;

import com.zhongmei.bty.basemodule.trade.bean.TradeVo;

/**
 * Created by demo on 2018/12/15
 */

public class RefreshTradeVoEvent {

    private boolean isLogin = false;

    public TradeVo tradeVo;

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public TradeVo getTradeVo() {
        return tradeVo;
    }

    public void setTradeVo(TradeVo tradeVo) {
        this.tradeVo = tradeVo;
    }
}
