package com.zhongmei.bty.basemodule.orderdish.bean;

import com.zhongmei.bty.basemodule.orderdish.entity.AddItemBatch;
import com.zhongmei.bty.basemodule.orderdish.entity.AddItemRecord;

import java.util.List;



public class AddItemVo implements java.io.Serializable {

        private AddItemBatch mAddItemBatch;

        private List<AddItemRecord> mAddItemRecords;

    public AddItemBatch getmAddItemBatch() {
        return mAddItemBatch;
    }

    public void setmAddItemBatch(AddItemBatch mAddItemBatch) {
        this.mAddItemBatch = mAddItemBatch;
    }

    public List<AddItemRecord> getmAddItemRecords() {
        return mAddItemRecords;
    }

    public void setmAddItemRecords(List<AddItemRecord> mAddItemRecords) {
        this.mAddItemRecords = mAddItemRecords;
    }
}
