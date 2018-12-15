package com.zhongmei.bty.basemodule.trade.message;

import java.util.List;

import com.zhongmei.bty.commonmodule.database.entity.TableNumberSetting;

public class TableNumberSettingReq {
    private List<TableNumberSetting> tableNumberSettings;

    public List<TableNumberSetting> getTableNumberSettings() {
        return tableNumberSettings;
    }

    public void setTableNumberSettings(List<TableNumberSetting> tableNumberSettings) {
        this.tableNumberSettings = tableNumberSettings;
    }

}
