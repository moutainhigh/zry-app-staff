package com.zhongmei.bty.data.operates.message.content;

import java.util.List;

import com.zhongmei.yunfu.db.enums.PrintStatus;
import com.zhongmei.yunfu.util.ValueEnums;

public class TradeItemOperationReq {

    private Long tradeId;

    private Long serverUpdateTime;

    private List<InnerTradeItem> tradeItems;

    public TradeItemOperationReq(Long tradeId, Long serverUpdateTime, List<InnerTradeItem> tradeItems) {
        this.tradeId = tradeId;
        this.serverUpdateTime = serverUpdateTime;
        this.tradeItems = tradeItems;
    }

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

    public Long getServerUpdateTime() {
        return serverUpdateTime;
    }

    public void setServerUpdateTime(Long serverUpdateTime) {
        this.serverUpdateTime = serverUpdateTime;
    }

    public List<InnerTradeItem> getTradeItems() {
        return tradeItems;
    }

    public void setTradeItems(List<InnerTradeItem> tradeItems) {
        this.tradeItems = tradeItems;
    }

    public static final class InnerTradeItem {
        private Long tradeItemId;

        private Integer printStatus;

        private String deviceIdenty;

        private Long creatorId;

        private String creatorName;

        public InnerTradeItem(Long tradeItemId, PrintStatus printStatus, String deviceIdenty, Long creatorId,
                              String creatorName) {
            this.tradeItemId = tradeItemId;
            this.printStatus = ValueEnums.toValue(printStatus);
            this.deviceIdenty = deviceIdenty;
            this.creatorId = creatorId;
            this.creatorName = creatorName;
        }

        public Long getTradeItemId() {
            return tradeItemId;
        }

        public void setTradeItemId(Long tradeItemId) {
            this.tradeItemId = tradeItemId;
        }

        public Integer getPrintStatus() {
            return printStatus;
        }

        public void setPrintStatus(Integer printStatus) {
            this.printStatus = printStatus;
        }

        public String getDeviceIdenty() {
            return deviceIdenty;
        }

        public void setDeviceIdenty(String deviceIdenty) {
            this.deviceIdenty = deviceIdenty;
        }

        public Long getCreatorId() {
            return creatorId;
        }

        public void setCreatorId(Long creatorId) {
            this.creatorId = creatorId;
        }

        public String getCreatorName() {
            return creatorName;
        }

        public void setCreatorName(String creatorName) {
            this.creatorName = creatorName;
        }

    }

}
