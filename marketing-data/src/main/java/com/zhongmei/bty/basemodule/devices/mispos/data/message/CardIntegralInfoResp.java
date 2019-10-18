package com.zhongmei.bty.basemodule.devices.mispos.data.message;

import java.math.BigDecimal;
import java.util.List;

import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardIntegralInfoResp.CardIntegralInfo;


public class CardIntegralInfoResp extends CardBaseResp<List<CardIntegralInfo>> {

    public static class CardIntegralInfo {
        private String serverCreateTime;

        private BigDecimal addIntegral;


        private Integer operateType;

        private BigDecimal endIntegral;

        private String creatorId;

        private Long id;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getServerCreateTime() {
            return serverCreateTime;
        }

        public void setServerCreateTime(String serverCreateTime) {
            this.serverCreateTime = serverCreateTime;
        }

        public BigDecimal getAddIntegral() {
            return addIntegral;
        }

        public void setAddIntegral(BigDecimal addIntegral) {
            this.addIntegral = addIntegral;
        }

        public Integer getOperateType() {
            return operateType;
        }

        public void setOperateType(Integer operateType) {
            this.operateType = operateType;
        }

        public BigDecimal getEndIntegral() {
            return endIntegral;
        }

        public void setEndIntegral(BigDecimal endIntegral) {
            this.endIntegral = endIntegral;
        }

        public String getCreatorId() {
            return creatorId;
        }

        public void setCreatorId(String creatorId) {
            this.creatorId = creatorId;
        }

    }

}
