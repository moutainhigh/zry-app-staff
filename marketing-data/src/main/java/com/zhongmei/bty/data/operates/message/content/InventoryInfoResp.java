package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.bty.basemodule.inventory.bean.InventoryInfo;
import com.zhongmei.yunfu.resp.data.SupplyTransferResp;

import java.util.List;


public class InventoryInfoResp extends SupplyTransferResp<List<InventoryInfo>> {

    private String updateTime;

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
