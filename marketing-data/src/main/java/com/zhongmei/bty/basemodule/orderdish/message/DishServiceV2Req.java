package com.zhongmei.bty.basemodule.orderdish.message;

import com.zhongmei.yunfu.util.ValueEnum;
import com.zhongmei.yunfu.util.ValueEnums;
import com.zhongmei.yunfu.db.entity.trade.Trade;

import java.util.ArrayList;
import java.util.List;

/**
 * v8.4 修改划菜对象，添加抽象方法获取主单id 和 子单id
 */
public class DishServiceV2Req {

    /**
     * 普通划菜数据
     */
    public List<TradeItemsBean> tradeItems;

    /**
     * 联台划菜数据
     */
    public List<TradeItemsBean> itemServings;

    /**
     * 主单Trade(如果是批量菜，则非空;如果是子单菜可以为空 , 普通菜不传)
     */
    public Trade mainTrade;

    /**
     * 子单Trade(如果是批量菜，则非空;如果是子单菜可以为空 , 普通菜不传)
     */
    public Trade subTrade;

    /**
     * 主单TradeId(如果是批量菜，则非空;如果是子单菜可以为空 , 普通菜不传)
     */
    public Long tradeId;
//
//    /**
//     * 子单TradeId(如果是批量菜，则非空;如果是子单菜可以为空 , 普通菜不传)
//     */
//    public Long subTradeId = getSunTradeId();

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

    /**
     * 普通单划菜
     *
     * @param id
     * @param servingStatus
     * @param serverUpdateTime
     */
    public void addTradeItem(Long id, Integer servingStatus, Long serverUpdateTime) {
        if (tradeItems == null) {
            tradeItems = new ArrayList<>();
        }
        tradeItems.add(getTradeItem(id, servingStatus, serverUpdateTime));
    }

    /**
     * 设置联台子单划菜数据
     *
     * @param id               批量菜数据ID
     * @param servingStatus    服务器更新时间
     * @param serverUpdateTime 批量菜当前的服务状态：1.未上菜；2.已上菜
     */
    public void addUionMainItemServings(Long id, Integer servingStatus, Long serverUpdateTime) {
        if (itemServings == null) {
            itemServings = new ArrayList<>();
        }
        itemServings.add(getTradeItem(id, servingStatus, serverUpdateTime));
    }

    /**
     * 设置联台子单划菜数据
     *
     * @param id               批量菜数据ID
     * @param servingStatus    服务器更新时间
     * @param serverUpdateTime 批量菜当前的服务状态：1.未上菜；2.已上菜
     * @param type             批量菜当前的服务状态：1.未上菜；2.已上菜
     */
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

        // 主单菜
        MAINBATCH(1),
        // 子单菜
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

//    /**
//     * 主单TradeId(如果是批量菜，则非空;如果是子单菜可以为空 , 普通菜不传)
//     * @return 主单TradeId
//     */
//    public abstract Long getMainTradeId();
//
//    /**
//     * 子单TradeId(如果是子单操作才传，否则为null)
//     * @return 子单TradeId
//     */
//    public abstract Long getSunTradeId();


}
