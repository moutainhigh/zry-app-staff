package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.bty.basemodule.inventory.message.InventoryChangeReq;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;



public class TradeUnionModifySubWarpReq {
    private TradeUnionModifySubReq modifyRequest;
    private InventoryChangeReq inventoryRequest;

    public TradeUnionModifySubReq getModifyRequest() {
        return modifyRequest;
    }

    public void setModifyRequest(TradeUnionModifySubReq modifyRequest) {
        this.modifyRequest = modifyRequest;
    }

    public InventoryChangeReq getInventoryRequest() {
        return inventoryRequest;
    }

    public void setInventoryRequest(InventoryChangeReq inventoryRequest) {
        this.inventoryRequest = inventoryRequest;
    }

    public static class TradeUnionModifySubReq extends TradeUnionModifyComReq {

        public TradeUnionModifySubReq() {
        }

        private TradeUnionRequest subTrade;
        private TradeExtra subTradeExtra;

        public TradeUnionRequest getSubTrade() {
            return subTrade;
        }

        public void setSubTrade(TradeUnionRequest subTrade) {
            this.subTrade = subTrade;
        }

        public TradeExtra getSubTradeExtra() {
            return subTradeExtra;
        }

        public void setSubTradeExtra(TradeExtra subTradeExtra) {
            this.subTradeExtra = subTradeExtra;
        }
    }
}
