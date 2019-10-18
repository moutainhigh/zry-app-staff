package com.zhongmei.bty.basemodule.orderdish.message;

import com.zhongmei.yunfu.util.ValueEnum;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.entity.trade.Trade;

import java.util.ArrayList;
import java.util.List;


public class DishServiceV2Req {


    public List<TradeItemsBean> tradeItems;


    public List<TradeItemsBean> itemServings;


    public Trade mainTrade;


    public Trade subTrade;


    public Long tradeId;

    public static class TradeItemsBean {
        public Long id;
        public Integer servingStatus;
        public Long serverUpdateTime;
        public Integer type;
    }

    public TradeItemsBean getTradeItem(Long id, Integer servingStatus, Long serverUpdateTime) {
        TradeItemsBean bean = new TradeItemsBean();
        bean.id = id;
        bean.servingStatus = servingStatus;
        bean.serverUpdateTime = serverUpdateTime;
        return bean;
    }


    public void addTradeItem(Long id, Integer servingStatus, Long serverUpdateTime) {
        if (tradeItems == null) {
            tradeItems = new ArrayList<>();
        }
        tradeItems.add(getTradeItem(id, servingStatus, serverUpdateTime));
    }


    public void addUionMainItemServings(Long id, Integer servingStatus, Long serverUpdateTime) {
        if (itemServings == null) {
            itemServings = new ArrayList<>();
        }
        itemServings.add(getTradeItem(id, servingStatus, serverUpdateTime));
    }


    public void addUionSubItemServings(Long id, Integer servingStatus, Long serverUpdateTime, Type type) {
        if (itemServings == null) {
            itemServings = new ArrayList<>();
        }
        TradeItemsBean bean = getTradeItem(id, servingStatus, serverUpdateTime);
        if (type != null) {
            bean.type = ValueEnums.toValue(type);
        }
        itemServings.add(bean);
    }

    public int size() {
        return tradeItems == null ? 0 : tradeItems.size();
    }

    public int itemServingsSize() {
        return itemServings == null ? 0 : itemServings.size();
    }


    public enum Type implements ValueEnum<Integer> {

                MAINBATCH(1),
                SUB(2);

        private final Helper<Integer> helper;

        Type(Integer value) {
            helper = Helper.valueHelper(value);
        }

        Type() {
            helper = Helper.unknownHelper();
        }

        @Override
        public Integer value() {
            return helper.value();
        }

        @Override
        public boolean equalsValue(Integer value) {
            return helper.equalsValue(this, value);
        }

        @Override
        public boolean isUnknownEnum() {
            return helper.isUnknownEnum();
        }

        @Override
        public void setUnknownValue(Integer value) {
            helper.setUnknownValue(value);
        }

        @Override
        public String toString() {
            return "" + value();
        }
    }



}
