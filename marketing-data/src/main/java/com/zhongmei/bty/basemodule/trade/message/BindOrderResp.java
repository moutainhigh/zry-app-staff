package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.yunfu.db.entity.trade.TradeExtra;

import java.util.List;



public class BindOrderResp {
    private List<ResultInfo> resultInfos;

    public List<ResultInfo> getResultInfos() {
        return resultInfos;
    }

    public void setResultInfos(List<ResultInfo> resultInfos) {
        this.resultInfos = resultInfos;
    }

    public class ResultInfo {
        private Integer resultStatus;
        private String resultMessage;
        private TradeExtra tradeExtra;

        public Integer getResultStatus() {
            return resultStatus;
        }

        public void setResultStatus(Integer resultStatus) {
            this.resultStatus = resultStatus;
        }

        public String getResultMessage() {
            return resultMessage;
        }

        public void setResultMessage(String resultMessage) {
            this.resultMessage = resultMessage;
        }

        public TradeExtra getTradeExtra() {
            return tradeExtra;
        }

        public void setTradeExtra(TradeExtra tradeExtra) {
            this.tradeExtra = tradeExtra;
        }
    }
}
