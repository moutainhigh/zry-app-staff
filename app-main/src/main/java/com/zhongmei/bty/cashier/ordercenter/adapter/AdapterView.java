package com.zhongmei.bty.cashier.ordercenter.adapter;

import android.view.View;

import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;


public interface AdapterView<T extends TradeItemVo> {


    public abstract View getView();


    public abstract void bindData(int position, T data);

}
