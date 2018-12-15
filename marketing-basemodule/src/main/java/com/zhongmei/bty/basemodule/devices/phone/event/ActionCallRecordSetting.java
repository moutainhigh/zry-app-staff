package com.zhongmei.bty.basemodule.devices.phone.event;

/**
 * 通话记录保存时间设置
 */
public class ActionCallRecordSetting {
    private int saveDay;

    public ActionCallRecordSetting(int saveDay) {
        this.saveDay = saveDay;
    }

    public int getSaveDay() {
        return saveDay;
    }

    public void setSaveDay(int saveDay) {
        this.saveDay = saveDay;
    }

    public ActionCallRecordSetting() {
    }

}
