package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.yunfu.resp.data.SupplyTransferRespBase;


public class InventorySetResp extends SupplyTransferRespBase {

        private Integer saleNumOpen;

        private Integer showSaleVal;

        private Integer autoClearStatus;

    public Integer getSaleNumOpen() {
        return saleNumOpen;
    }

    public void setSaleNumOpen(Integer saleNumOpen) {
        this.saleNumOpen = saleNumOpen;
    }

    public Integer getShowSaleVal() {
        return showSaleVal;
    }

    public void setShowSaleVal(Integer showSaleVal) {
        this.showSaleVal = showSaleVal;
    }

    public Integer getAutoClearStatus() {
        return autoClearStatus;
    }

    public void setAutoClearStatus(Integer autoClearStatus) {
        this.autoClearStatus = autoClearStatus;
    }
}
