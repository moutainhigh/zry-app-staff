package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.yunfu.http.NationInfo;
import com.zhongmei.yunfu.http.NationInfoInterface;
import com.zhongmei.yunfu.db.entity.trade.TradeUser;
import com.zhongmei.bty.basemodule.trade.message.TradeItemReq;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.bty.basemodule.trade.bean.TradeGroupInfo;
import com.zhongmei.bty.commonmodule.database.entity.TradeInitConfig;
import com.zhongmei.bty.commonmodule.database.entity.TradeTax;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装团餐开台Trade相关数据
 *
 * @date 2017/6/28 17:55
 */
public class GroupOpenTableTradeReq extends Trade implements NationInfoInterface {

    private static final long serialVersionUID = 1L;

    public TradeExtra tradeExtra;
    public List<TradeExtra> tradeExtras;
    public List<TradeCustomer> tradeCustomers;
    public List<TradeTable> tradeTables;
    public List<TradeItemReq> tradeItems;
    public TradeGroupInfo tradeGroup;
    public TradeUser tradeUser;//add 20170916 订单推销员
    public List<TradeTax> tradeTaxs;
    public List<TradeInitConfig> tradeInitConfigs;

    @Override
    public List<NationInfo> getNationInfos() {
        List<NationInfo> result = new ArrayList<>();
        if (tradeCustomers != null) {
            for (TradeCustomer tradeCustomer : tradeCustomers) {
                NationInfo nationInfo = new NationInfo();
                nationInfo.mobile = tradeCustomer.getCustomerPhone();
                nationInfo.nationalTelCode = tradeCustomer.nationalTelCode;
                nationInfo.nation = tradeCustomer.nation;
                nationInfo.country = tradeCustomer.country;
                result.add(nationInfo);
            }
        }
        return result;
    }
}
