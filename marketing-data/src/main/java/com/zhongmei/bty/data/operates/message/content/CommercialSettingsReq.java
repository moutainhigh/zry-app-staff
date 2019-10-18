package com.zhongmei.bty.data.operates.message.content;

import com.zhongmei.bty.basemodule.commonbusiness.entity.CommercialCustomSettings;

import java.util.List;


public class CommercialSettingsReq {
    List<CommercialCustomSettings> settingItems;

    public List<CommercialCustomSettings> getSettingItems() {
        return settingItems;
    }

    public void setSettingItems(List<CommercialCustomSettings> settingItems) {
        this.settingItems = settingItems;
    }
}
