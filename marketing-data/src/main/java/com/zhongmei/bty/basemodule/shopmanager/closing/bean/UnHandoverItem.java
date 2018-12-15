package com.zhongmei.bty.basemodule.shopmanager.closing.bean;

import java.io.Serializable;

/**
 * @date:2015年12月25日上午10:19:02
 */
public class UnHandoverItem implements Serializable {

    /**
     * @date：2015年12月25日 上午10:19:34
     * @Description:TODO
     */
    private static final long serialVersionUID = 1L;
    String deviceId;
    //	pad 编号
    Integer padNo;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getPadNo() {
        return padNo;
    }

    public void setPadNo(Integer padNo) {
        this.padNo = padNo;
    }

}
