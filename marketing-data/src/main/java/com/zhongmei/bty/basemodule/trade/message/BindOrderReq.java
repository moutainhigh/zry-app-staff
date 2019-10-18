package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.context.util.Utils;

import java.util.ArrayList;
import java.util.List;



public class BindOrderReq {
    private Long userId;      private String userName;     private List<TradeInfo> tradeInfos;
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<TradeInfo> getTradeInfos() {
        return tradeInfos;
    }

    public void setTradeInfos(List<TradeVo> tradeVos) {
        if (Utils.isNotEmpty(tradeVos)) {
            List<TradeInfo> tradeInfos = new ArrayList<TradeInfo>();
            for (TradeVo tradeVo : tradeVos) {
                TradeInfo tradeInfo = new TradeInfo();
                tradeInfo.setTradeId(tradeVo.getTrade().getId());
                TradeExtra tradeExtra = tradeVo.getTradeExtra();
                if (tradeExtra != null) {
                    tradeInfo.setTradeExtraServerUpdateTime(tradeExtra.getServerUpdateTime());
                }
                tradeInfos.add(tradeInfo);
            }
            this.tradeInfos = tradeInfos;
        }
    }

    private class TradeInfo {
        private Long tradeId;        private Long tradeExtraServerUpdateTime;
        public Long getTradeId() {
            return tradeId;
        }

        public void setTradeId(Long tradeId) {
            this.tradeId = tradeId;
        }

        public Long getTradeExtraServerUpdateTime() {
            return tradeExtraServerUpdateTime;
        }

        public void setTradeExtraServerUpdateTime(Long tradeExtraServerUpdateTime) {
            this.tradeExtraServerUpdateTime = tradeExtraServerUpdateTime;
        }
    }
}
