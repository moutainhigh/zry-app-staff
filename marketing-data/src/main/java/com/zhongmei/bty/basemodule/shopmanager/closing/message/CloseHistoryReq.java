package com.zhongmei.bty.basemodule.shopmanager.closing.message;

/**
 * @date:2015年12月17日下午4:49:13
 */
public class CloseHistoryReq {
    //开始时间
    String startTime;
    //结束时间
    String endTime;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


}
