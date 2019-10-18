package com.zhongmei.bty.dinner.shopcart.adapter;

import android.content.Context;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.enums.ExtraItemType;
import com.zhongmei.bty.basemodule.orderdish.enums.ItemType;
import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class BuffetDeskShopCartAdapter extends DinnerDeskShopcartAdapter {
    private Context mContext;
    private Map<Integer, List<IShopcartItem>> dishGroup = null;

    protected BigDecimal shellActumalAmount = BigDecimal.ZERO;


    public BuffetDeskShopCartAdapter(Context context) {
        super(context);
        this.mContext = context;
    }


    public void updateData(List<IShopcartItem> dataList, TradeVo tradeVo, boolean isShowInvalid) {
        initCommonData(tradeVo);
        updateGroupData(dataList, tradeVo, false);
        initialDishCheckStatus();        updateTrade(tradeVo, isShowInvalid);        initialRelateDishInfo();    }



    public BigDecimal getShellActumalAmount() {
        return shellActumalAmount;
    }


    public void updateGroupData(List<IShopcartItem> dataList, TradeVo tradeVo, boolean isShowInvalid) {
        this.data.clear();        this.mAllDishCount = BigDecimal.ZERO;

        this.dishGroup = BuffetAdapterUtil.buildBuffetShopcartData(context, tradeVo, dataList, data, this);
    }


    private void initOtherDataItem(TradeVo tradeVo, ArrayList<DishDataItem> data) {
        if (tradeVo.getTradeDeposit() != null) {
            DishDataItem dishDataItem = new DishDataItem(ItemType.BUFFET_EXTRA_DEPOSIT);
            dishDataItem.setName(mContext.getString(R.string.buffet_deposit));
            dishDataItem.setExtraType(ExtraItemType.DEPOSIT);
            if (tradeVo.getTradeDepositPaymentItem() != null)
                dishDataItem.setStandText(tradeVo.getTradeDepositPaymentItem().getPayModeName() + mContext.getString(R.string.record_pay));
            else
                dishDataItem.setStandText(mContext.getString(R.string.buffet_deposit_property));
            dishDataItem.setValue(tradeVo.getTradeDeposit().getDepositPay().doubleValue());
            data.add(dishDataItem);
        }
    }


    public Map<Integer, List<IShopcartItem>> getGroup() {
        return dishGroup;
    }
}
