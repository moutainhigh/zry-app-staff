package com.zhongmei.bty.basemodule.shopmanager.closing.bean;

/**
 * @date:2015年12月25日下午2:40:20
 */
public class HandoverItem {

    // pad编号
    Integer padNo;

    // 交接对象
    HandoverCloseItem handover;

    public Integer getPadNo() {
        return padNo;
    }

    public void setPadNo(Integer padNo) {
        this.padNo = padNo;
    }

    public HandoverCloseItem getHandover() {
        return handover;
    }

    public void setHandover(HandoverCloseItem handover) {
        this.handover = handover;
    }

}
