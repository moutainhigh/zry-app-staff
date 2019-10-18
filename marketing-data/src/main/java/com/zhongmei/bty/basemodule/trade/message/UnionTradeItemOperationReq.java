package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.yunfu.db.entity.dish.TradeItemOperation;

import java.util.List;


public class UnionTradeItemOperationReq {

    private Long tradeId;

    private BatchTradeItemOperationRequest batchTradeItemOperationRequest;

    private TradeItemOperationRequest tradeItemOperationRequest;

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public BatchTradeItemOperationRequest getBatchTradeItemOperationRequest() {
        return batchTradeItemOperationRequest;
    }

    public void setBatchTradeItemOperationRequest(BatchTradeItemOperationRequest batchTradeItemOperationRequest) {
        this.batchTradeItemOperationRequest = batchTradeItemOperationRequest;
    }

    public TradeItemOperationRequest getTradeItemOperationRequest() {
        return tradeItemOperationRequest;
    }

    public void setTradeItemOperationRequest(TradeItemOperationRequest tradeItemOperationRequest) {
        this.tradeItemOperationRequest = tradeItemOperationRequest;
    }

    public static class BatchTradeItemOperationRequest {

        private List<TradeItemOperation> tradeItemOperationsForUuid;

        private TradeUnionModifyMainWarpReq modifyRequest;

        public List<TradeItemOperation> getTradeItemOperationsForUuid() {
            return tradeItemOperationsForUuid;
        }

        public void setTradeItemOperationsForUuid(List<TradeItemOperation> tradeItemOperationsForUuid) {
            this.tradeItemOperationsForUuid = tradeItemOperationsForUuid;
        }

        public TradeUnionModifyMainWarpReq getModifyRequest() {
            return modifyRequest;
        }

        public void setModifyRequest(TradeUnionModifyMainWarpReq modifyRequest) {
            this.modifyRequest = modifyRequest;
        }
    }

    public static class TradeItemOperationRequest {
        private List<TradeItemOperation> tradeItemOperationsForUuid;

        private TradeUnionModifySubWarpReq modifyRequest;

        public List<TradeItemOperation> getTradeItemOperationsForUuid() {
            return tradeItemOperationsForUuid;
        }

        public void setTradeItemOperationsForUuid(List<TradeItemOperation> tradeItemOperationsForUuid) {
            this.tradeItemOperationsForUuid = tradeItemOperationsForUuid;
        }

        public TradeUnionModifySubWarpReq getModifyRequest() {
            return modifyRequest;
        }

        public void setModifyRequest(TradeUnionModifySubWarpReq modifyRequest) {
            this.modifyRequest = modifyRequest;
        }
    }
}
