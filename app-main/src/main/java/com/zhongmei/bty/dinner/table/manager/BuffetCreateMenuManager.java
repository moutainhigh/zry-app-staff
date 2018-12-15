package com.zhongmei.bty.dinner.table.manager;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.message.TradeResp;
import com.zhongmei.bty.basemodule.trade.operates.TradeOperates;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;

/**
 * Created by demo on 2018/12/15
 */

public class BuffetCreateMenuManager extends BuffetModifyTradeManager {

    private Context context;

    public BuffetCreateMenuManager(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void setmTradeVo(TradeVo mTradeVo) {
        super.setmTradeVo(mTradeVo);
        mShoppingCart = new DinnerShoppingCart();
        mShoppingCart.setTradeVo(mTradeVo);
    }

    @Override
    void changeTable(ResponseListener<TradeResp> listener) {
        final TradeOperates mTradeOperates = OperatesFactory.create(TradeOperates.class);
        final TradeVo mTradeVo = mShoppingCart.createOrder(mShoppingCart.getShoppingCartVo(), false);
        FragmentActivity activity = (FragmentActivity) context;
        mTradeOperates.buffetCreateMenu(mTradeVo, LoadingResponseListener.ensure(listener, activity.getSupportFragmentManager()));
    }
}
