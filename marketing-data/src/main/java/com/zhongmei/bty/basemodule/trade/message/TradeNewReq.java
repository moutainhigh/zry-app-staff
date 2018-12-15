package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.yunfu.http.NationInfo;
import com.zhongmei.yunfu.http.NationInfoInterface;
import com.zhongmei.bty.basemodule.inventory.message.InventoryChangeReq;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装Trade相关的实时请求的Request数据
 *
 * @Date：2017-9-15 下午6:30:02
 */
public class TradeNewReq implements NationInfoInterface {

    private TradeReq tradeRequest;

    private InventoryChangeReq inventoryRequest;

    public TradeReq getTradeRequest() {
        return tradeRequest;
    }

    public void setTradeRequest(TradeReq tradeRequest) {
        this.tradeRequest = tradeRequest;
    }

    public TradeNewReq(TradeReq tradeRequest, InventoryChangeReq inventoryRequest) {
        this.tradeRequest = tradeRequest;
        this.inventoryRequest = inventoryRequest;
    }

    @Override
    public List<NationInfo> getNationInfos() {
        List<NationInfo> result = new ArrayList<>();
        if (tradeRequest != null && tradeRequest.getTradeCustomers() != null) {
            for (TradeCustomer tradeCustomer : tradeRequest.getTradeCustomers()) {
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
