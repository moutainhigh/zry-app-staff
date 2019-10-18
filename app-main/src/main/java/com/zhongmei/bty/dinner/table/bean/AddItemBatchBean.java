package com.zhongmei.bty.dinner.table.bean;

import com.zhongmei.bty.basemodule.orderdish.bean.AddItemVo;


public class AddItemBatchBean {
    private String sequenceNumber;
    private Long time;
    private AddItemVo addItemVo;

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public AddItemVo getAddItemVo() {
        return addItemVo;
    }

    public void setAddItemVo(AddItemVo addItemVo) {
        this.addItemVo = addItemVo;
    }
}
