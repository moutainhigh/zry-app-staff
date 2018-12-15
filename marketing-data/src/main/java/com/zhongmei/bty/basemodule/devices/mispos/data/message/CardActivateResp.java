package com.zhongmei.bty.basemodule.devices.mispos.data.message;

import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardActivateResp.ActivateResult;

import java.util.List;

public class CardActivateResp extends CardBaseResp<ActivateResult> {

    public class ActivateResult {
        public long customerId;

        public List<String> entityCardNos;

        public long getCustomerId() {
            return customerId;
        }

        public void setCustomerId(long customerId) {
            this.customerId = customerId;
        }

        public List<String> getEntityCardNos() {
            return entityCardNos;
        }

        public void setEntityCardNos(List<String> entityCardNos) {
            this.entityCardNos = entityCardNos;
        }

    }

}
