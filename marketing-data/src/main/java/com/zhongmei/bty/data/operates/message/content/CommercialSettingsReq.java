package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.bty.basemodule.commonbusiness.entity.CommercialCustomSettings;

import java.util.List;

/**
 * 门店设置请求体
 */
public class CommercialSettingsReq {
    List<CommercialCustomSettings> settingItems;

    public List<CommercialCustomSettings> getSettingItems() {
        return settingItems;
    }

    public void setSettingItems(List<CommercialCustomSettings> settingItems) {
        this.settingItems = settingItems;
    }
}
