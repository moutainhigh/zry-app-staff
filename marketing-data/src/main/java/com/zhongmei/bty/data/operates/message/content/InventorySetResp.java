package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.yunfu.resp.data.SupplyTransferRespBase;

/**
 * @Date： 2017/2/28
 * @Description:库存开关设置返回
 * @Version: 1.0
 */
public class InventorySetResp extends SupplyTransferRespBase {

    //可售数展示开关：1、关闭 2、开启
    private Integer saleNumOpen;

    //商品可售数：1、商品每日售卖量 2、商品实际库存数
    private Integer showSaleVal;

    //自动估清开关：1、关闭 2、开启
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
