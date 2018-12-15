package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.bty.basemodule.inventory.bean.InventoryInfo;
import com.zhongmei.yunfu.resp.data.SupplyTransferResp;

import java.util.List;

/**
 * @Date： 2017/2/28
 * @Description:库存数据返回
 * @Version: 1.0
 */
public class InventoryInfoResp extends SupplyTransferResp<List<InventoryInfo>> {

    private String updateTime;

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
