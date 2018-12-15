package com.zhongmei.bty.basemodule.devices.mispos.data.message;

import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardInfo;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.CardRangeSearchResp.CardRangeSearchResult;

import java.util.List;

public class CardRangeSearchResp extends CardBaseResp<CardRangeSearchResult> {

    public class CardRangeSearchResult {
        private List<EcCardInfo> dataList;

        public List<EcCardInfo> getDataList() {
            return dataList;
        }

        public void setDataList(List<EcCardInfo> dataList) {
            this.dataList = dataList;
        }
    }
}
